package com.cf.crs.service;



import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cf.crs.config.config.HuaweiConfig;
import com.cf.crs.config.config.RestTemplateConfig;
import com.cf.crs.controller.HeBeiGetDeviceListController;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
/*import net.sf.json.JSONObject;*/
import org.apache.poi.ss.formula.functions.Rate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class HeBeiGetDeviceListService {

    @Resource(name = "httpsresttemplate")
    private  RestTemplate restTemplate;
    @Autowired
    private HuaweiConfig Huaweiconfig;

    public String GetUrl()
    {

      return   Huaweiconfig.getUrl();

    }
    public String GetSetCookie(){
        try {
            restTemplate = new RestTemplate(RestTemplateConfig.generateHttpRequestFactory());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        String LoginUrl=GetUrl()+"loginInfo/login/v1.0";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type","application/json");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userName",Huaweiconfig.getUsername());
        map.put("password",Huaweiconfig.getPassword());
        HttpEntity<Map<String,Object>> requestEntity = new HttpEntity<>(map, headers);
        ResponseEntity<String> exchange = restTemplate.postForEntity(LoginUrl, requestEntity, String.class);
        HttpHeaders httpHeaders=exchange.getHeaders();
        List<String> list= httpHeaders.get("Set-cookie");
        String setCookie=list.get(0);
        return  setCookie;
    }
    //分别获取摄像机设备在线离线的数量以及全部设备的数量
    public Map getDeviceTypeAllStatus(Integer deviceType,String cameraName)
    {
        Map map=new HashMap();
        double onlineStatusNumber=0;
        double outLineStatusNumber=0;
        double totalStatus=0;
        JSONObject jsonObject=GetTotalDeviceListByDevice(deviceType);
        JSONArray cameraBriefInfoJson=jsonObject.getJSONArray(cameraName);
       for(int i=0;i<cameraBriefInfoJson.size();i++)
       {
          JSONObject jsonObject1=cameraBriefInfoJson.getJSONObject(i);
           String status=jsonObject1.getString("status");
           if("1".equals(status))
           {
               onlineStatusNumber++;
           }else if ("0".equals(status)){
               outLineStatusNumber++;
           }

       }
        totalStatus=outLineStatusNumber+onlineStatusNumber;
       if(totalStatus!=0) {
           double onlineRate = onlineStatusNumber / totalStatus;
           BigDecimal bigDecimal = new BigDecimal(onlineRate).setScale(2, RoundingMode.HALF_UP);
           double rate = bigDecimal.doubleValue();
           map.put("onlineStatusNumber", onlineStatusNumber);
           map.put("outLineStatusNumber", outLineStatusNumber);
           map.put("totalStatus", totalStatus);
           map.put("onLineRate", rate);
       }
        return  map;
    }
    //通过设备类型获取子设备列表的总记录的信息
     public JSONObject GetTotalDeviceListByDevice(int deviceType) {
         int to_index = Integer.valueOf(Huaweiconfig.getTo_index());
         int startPageIndex = Integer.valueOf(Huaweiconfig.getFrom_index());
         String DeviceListTotal = GetDeviceListTotal(deviceType, startPageIndex, to_index);
          int total=Integer.valueOf(DeviceListTotal);
          return GetDeviceListByDeviceType(deviceType,startPageIndex,total);
     }

    //通过设备类型并且设置查询条数查询子设备列表,起始索引必须为1
    public JSONObject GetDeviceListByDeviceType(int deviceType, int fromIndex, int toIndex)
    {
        String setCookie=GetSetCookie();
        String DeviceUrl=GetUrl()+"device/deviceList/v1.0/?deviceType="+deviceType+"&fromIndex="+fromIndex+"&toIndex="+toIndex;
        HttpHeaders httpHeaders=new HttpHeaders();
        httpHeaders.add("Set-Cookie",setCookie);
        httpHeaders.add("Content-Type","application/json");
        ResponseEntity response = restTemplate.exchange(DeviceUrl, HttpMethod.GET, new HttpEntity<String>(httpHeaders), JSONObject.class);
        JSONObject deviceList= (JSONObject) response.getBody();
        JSONObject cameraDeviceInfos=null;
        JSONObject cameraBriefInfoListByPageNum=null;
        log.info("deviceList:{}", JSON.toJSONString(deviceList));
        switch (deviceType)
        {
            case 2:
                cameraDeviceInfos=deviceList.getJSONObject("cameraBriefInfos");
                cameraBriefInfoListByPageNum=cameraDeviceInfos.getJSONObject("cameraBriefInfoList");
                log.info("deviceList:{}", JSON.toJSONString(deviceList));
                break;
            case 30:
                log.info("deviceList:{}", JSON.toJSONString(deviceList));
                break;
            case 32:
                cameraDeviceInfos=deviceList.getJSONObject("shadowCameraBriefInfos");
                cameraBriefInfoListByPageNum=cameraDeviceInfos.getJSONObject("shadowCameraBriefInfoList");
                log.info("deviceList:{}", JSON.toJSONString(deviceList));
                break;
            case 33:
                cameraDeviceInfos=deviceList.getJSONObject("cameraBriefExInfos");
                cameraBriefInfoListByPageNum=cameraDeviceInfos.getJSONObject("cameraBriefInfoExList");
                log.info("deviceList:{}", JSON.toJSONString(deviceList));
                break;
            case 35:
                cameraDeviceInfos=deviceList.getJSONObject("cameraBriefInfosV2");
                cameraBriefInfoListByPageNum=cameraDeviceInfos.getJSONObject("cameraBriefInfoList");
                log.info("deviceList:{}", JSON.toJSONString(deviceList));
                break;
        }

        return  cameraBriefInfoListByPageNum;

    }
    //获取设备列表的总数
    public String GetDeviceListTotal(int deviceType, int fromIndex, int toIndex)
    {
        String setCookie=GetSetCookie();
        String DeviceUrl=GetUrl()+"device/deviceList/v1.0/?deviceType="+deviceType+"&fromIndex="+fromIndex+"&toIndex="+toIndex;
        HttpHeaders httpHeaders=new HttpHeaders();
        httpHeaders.add("Set-Cookie",setCookie);
        httpHeaders.add("Content-Type","application/json");
        ResponseEntity response = restTemplate.exchange(DeviceUrl, HttpMethod.GET, new HttpEntity<String>(httpHeaders), JSONObject.class);
        JSONObject deviceList= (JSONObject) response.getBody();
        String GetDeviceInfoTotal="null";
        switch (deviceType)
       {
           case 2:
               JSONObject cameraBriefInfos = deviceList.getJSONObject("cameraBriefInfos");
               GetDeviceInfoTotal = cameraBriefInfos.getString("total");
               break;
           case 30:
                GetDeviceInfoTotal ="1";
               break;
           case 32:
               JSONObject shadowCameraBriefInfos = deviceList.getJSONObject("shadowCameraBriefInfos");
               GetDeviceInfoTotal = shadowCameraBriefInfos.getString("total");
               break;
           case 33:
               JSONObject cameraBriefExInfos = deviceList.getJSONObject("cameraBriefExInfos");
               GetDeviceInfoTotal = cameraBriefExInfos.getString("total");
               break;
           case 35:
               JSONObject cameraBriefInfosV2 = deviceList.getJSONObject("cameraBriefInfosV2");
               GetDeviceInfoTotal = cameraBriefInfosV2.getString("total");
               break;
       }


        return  GetDeviceInfoTotal;
    }


}
