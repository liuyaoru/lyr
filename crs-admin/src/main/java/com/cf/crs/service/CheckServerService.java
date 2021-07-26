package com.cf.crs.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cf.util.http.HttpWebResult;
import com.cf.util.http.ResultJson;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author frank
 * 2019/11/18
 **/
@Slf4j
@Service
public class CheckServerService {

    @Autowired
    RestTemplate restTemplate;

    @Value("${check.server.url}")
    private String url;

    @Value("${check.server.name}")
    private String name;

    @Value("${check.server.apikey}")
    private String apikey;

    public JSONArray getServers(){
        String serverUrl = url+"/api/json/device/listDevices?apiKey="+apikey+"&category="+name;
        log.info("checkServerUrl:{}",serverUrl);

        try {
            JSONArray servers = restTemplate.getForObject(serverUrl, JSONArray.class);
            return servers;
        } catch (Exception e) {
            //参数错误、无有效数据时，返回是Object{}对象，而不是Array[{}]数组，因此会抛出解析异常
            /* 参数错误返回示例：
               {
                    "error": {
                        "code": 5000,
                        "message": "指定的apiKey无效。验证失败。"
                    }
                }
                无有效数据的返回示例：
                {
                    "message": "没有发现设备。"
                }
            * */
            return null;
        }
        //return restTemplate.getForObject(serverUrl, JSONObject.class);
    }

    public List<JSONObject> getServerList(){
        List array = Lists.newArrayList();

        JSONArray serverArray = getServers();
        if (serverArray == null || serverArray.isEmpty()) return array;
        log.info("serverResult:{}",serverArray.toJSONString());
        return serverArray.stream().map(obj-> JSON.parseObject(JSON.toJSONString(obj))).collect(Collectors.toList());
    }

    /**
     * 获取对应告警的的服务器数据
     * @param waringType
     * @return
     */
    public ResultJson<List<JSONObject>> serverList(Integer waringType){
        List<JSONObject> serverList = getServerList();
        if (waringType == null) return HttpWebResult.getMonoSucResult(getServerList());
        List<JSONObject> list = serverList.stream().filter(jsonObject -> {
            int severity = jsonObject.getIntValue("statusNum");
            if (waringType == 1) return severity == 1;
            else if (waringType == 3) return severity == 5;
            else return severity != 1 && severity != 5;
        }).collect(Collectors.toList());
        return HttpWebResult.getMonoSucResult(list);
    }

    /**
     * 性能考评结果
     * @param deviceName
     * @return
     */
    public JSONObject checkAvailabilt(String deviceName){
        String serverUrl = url+"/api/json/device/getAvailabiltyGraphData?apiKey="+apikey+"&deviceName="+deviceName+"&period=YESTERDAY";
        log.info("checkServerAvailabiltUrl:{}",serverUrl);
        return restTemplate.getForObject(serverUrl, JSONObject.class);
    }

}
