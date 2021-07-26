package com.cf.crs.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cf.crs.entity.CheckObject;
import com.cf.util.http.HttpWebResult;
import com.cf.util.http.ResultJson;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.asm.Advice;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author frank
 * 2019/11/18
 **/
@Slf4j
@Service
public class CheckObjectAnalyService {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    CheckObjectService checkObjectService;

    @Autowired
    CheckServerService checkServerService;

    @Autowired
    CheckSqlService checkSqlService;

    @Value("${check.server.url}")
    private String url; //NTM API URL地址

    @Value("${check.server.apikey}")
    private String apikey; //NTM API KEY

    public Map<String,JSONObject> getServersMap(){
        HashMap<String,JSONObject> map = Maps.newHashMap();
        try {
            List<JSONObject> servers = checkServerService.getServerList();
            if (servers == null || servers.isEmpty()) return map;
            log.info("serverResult:{}",JSON.toJSONString(servers));
            for (Object obj:servers) {
                JSONObject server = JSON.parseObject(JSON.toJSONString(obj));
                if (server == null || server.isEmpty()) continue;
                String name = server.getString("name");
                map.put(name,server);
            }
        } catch (Exception e) {
            log.info(e.getMessage(),e);
        }
        return map;
    }

    public Map<String,Element> getSqlMap(Integer type){
        Map<String, Element> map = Maps.newHashMap();
        try {
            String html = checkSqlService.getCheckSqlList(type);
            if (StringUtils.isEmpty(html)) return map;
            log.info("getCheckSqlResult:{}", html);
            Document doc = Jsoup.parse(html);
            Elements result = doc.select("response");
            if (result == null) return map;
            if (!result.hasAttr("response-code") || !"4000".equalsIgnoreCase(result.attr("response-code")))
                HttpWebResult.getMonoError("请求失败");
            //请求数据成功
            Elements rowList = result.select("Monitor");
            for (Element element : rowList) {
                String name = element.attr("RESOURCEID");
                map.put(name, element);
            }
        }catch (Exception e){
            log.info(e.getMessage(),e);
        }
        return map;
    }

    public List<Object> getCheckObjectAnalyResult(){
        Map<String, JSONObject> serversMap = getServersMap();
        Map<String, Element> sqlMap = getSqlMap(1);
        Map<String, Element> middlewareMap = getSqlMap(2);
        CheckObject object = checkObjectService.getObject();
        if (object == null) return null;
        String result = object.getObject();
        JSONArray array = JSON.parseArray(result);
        if (array == null || array.isEmpty()) return null;
        ArrayList<Object> list = Lists.newArrayList();
        for(Object obj:array){
            JSONObject symbolJson = new JSONObject();
            //遍历一级菜单
            JSONObject checkObj = JSON.parseObject(JSON.toJSONString(obj));
            String symbol = checkObj.getString("name");
            JSONArray information = checkObj.getJSONArray("information");
            if (information == null ||information.isEmpty()) continue;
            HashMap<Object, Object> deviceMap = Maps.newHashMap();
            analyDeviceData(serversMap, sqlMap, middlewareMap, information, deviceMap);
            symbolJson.put("name",symbol);
            symbolJson.put("information",deviceMap);
            list.add(symbolJson);
        }
        return list;
    }

    private void analyDeviceData(Map<String, JSONObject> serversMap, Map<String, Element> sqlMap, Map<String, Element> middlewareMap, JSONArray information, HashMap<Object, Object> deviceMap) {
        for (Object typeObj:information){
            //遍历二级菜单
            JSONObject typeJson = JSON.parseObject(JSON.toJSONString(typeObj));
            if (typeJson == null || typeJson.isEmpty()) continue;
            String name = typeJson.getString("name");
            int total = 0;
            int waring = 0;
            //获取考评设备列表
            JSONArray deviceList = typeJson.getJSONArray("information");
            if (deviceList == null || deviceList.isEmpty()) continue;
            if ("server".equalsIgnoreCase(name)){
                //服务器
                for (Object deviceObj:deviceList){
                    JSONObject device = JSON.parseObject(JSON.toJSONString(deviceObj));
                    String deviceName = device.getString("name");
                    JSONObject jsonObject = serversMap.get(deviceName);
                    if (jsonObject == null || jsonObject.isEmpty()) continue;
                    total += 1;
                    Integer severity = jsonObject.getInteger("severity");
                    if (severity != 5) waring += 1;
                }
            }else if ("sql".equalsIgnoreCase(name)){
                //数据库
                for (Object deviceObj:deviceList){
                    JSONObject device = JSON.parseObject(JSON.toJSONString(deviceObj));
                    String deviceName = device.getString("name");
                    Element element = sqlMap.get(deviceName);
                    if (element == null) continue;
                    total += 1;
                    String healthstatus = element.attr("HEALTHSTATUS");
                    if (!"clear".equalsIgnoreCase(healthstatus)) waring += 1;
                }
            }else if("middleware".equalsIgnoreCase(name)){
                //中间件
                for (Object deviceObj:deviceList){
                    JSONObject device = JSON.parseObject(JSON.toJSONString(deviceObj));
                    String deviceName = device.getString("name");
                    Element element = middlewareMap.get(deviceName);
                    if (element == null) continue;
                    total += 1;
                    String healthstatus = element.attr("HEALTHSTATUS");
                    if (!"clear".equalsIgnoreCase(healthstatus)) waring += 1;
                }
            }else if("物联网设备".equalsIgnoreCase(name)){
                //物联网设备
            }
            deviceMap.put(name,waring+"/"+total);
        }
    }

    public ResultJson<List> getAnalyResult(){
        return HttpWebResult.getMonoSucResult(getCheckObjectAnalyResult());
    }

    public ResultJson<List> getGroupResult() {
        JSONArray groups = getLogicalGroupsFromNtm();
        if (groups == null || groups.isEmpty())
            return HttpWebResult.getMonoSucResult(null);
        else {
            JSONArray groupInfoArray = new JSONArray();
            for(Object obj:groups) {
                JSONObject groupInfoObj = new JSONObject();
                JSONObject groupObj = JSON.parseObject(JSON.toJSONString(obj));
                groupInfoObj.put("groupDisplayName", groupObj.getString("groupDisplayName")); //组名
                groupInfoObj.put("status", groupObj.getString("status")); //当前状态的数字值
                groupInfoObj.put("statusLabel", groupObj.getString("statusLabel")); //当前状态的文字值
                groupInfoObj.put("count", groupObj.getString("count")); //该组下的设备总数
                groupInfoObj.put("alertCount", getAlertCountByGroupName(groupObj.getString("groupName")));
                groupInfoObj.put("availability", groupObj.getString("availability")); //该组的可用性数据

                //TODO: 获取APM模块下数据库、中间件，以及物联网设备信息，合并后返回

                groupInfoArray.add(groupInfoObj);
            } //end for

            return HttpWebResult.getMonoSucResult(groupInfoArray);
        }
    }

    /**
     * 从NTM中获取逻辑组信息
     * @return 分组的json对象列表
     */
    private JSONArray getLogicalGroupsFromNtm() {
        String requestUrl = url+"/api/json/admin/listAllLogicalGroups?apiKey="+apikey;
        log.info("getLogicalGroupsFromNtm:{}", requestUrl);

        try {
            JSONArray groups = restTemplate.getForObject(requestUrl, JSONArray.class);
            return groups;
            //无有效数据时返回空的数组，即[]
        } catch (Exception e) {
            //API未放开、参数错误时，返回是Object{}对象，而不是Array[{}]数组，因此会抛出解析异常
            /* 参数错误返回示例：
               {
                    "error": {
                        "code": 5013,
                        "message": "这个API用于系统内部操作。"
                    }
                }
            * */
            return null;
        }
    }

    /**
     * 根据组名获取该组中告警设备数
     * @param name : 组名(groupName)，注意不是组显示名(groupDisplayName)
     * @return 告警设备数
     */
    private int getAlertCountByGroupName(String name) {
        String requestUrl = url+"/api/json/admin/getLogicalGroupInfo?apiKey="+apikey+"&groupName="+name;
        try {
            int count = -1;
            JSONObject group = restTemplate.getForObject(requestUrl, JSONObject.class);
            if (group != null) {
                JSONObject groupDetail = group.getJSONObject("GroupDetails");
                if (groupDetail != null)
                    count = groupDetail.getInteger("alertCount");
            }
            return count;
        } catch (Exception e) {
            return -1;
        }

    }

}
