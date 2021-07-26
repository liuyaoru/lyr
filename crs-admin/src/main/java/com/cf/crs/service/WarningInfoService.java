package com.cf.crs.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cf.crs.entity.WaringParam;
import com.cf.util.http.HttpWebResult;
import com.cf.util.http.ResultJson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @author frank
 * 2019/11/23
 **/
@Slf4j
@Service
public class WarningInfoService {

    @Value("${check.server.url}")
    String ServerUrl;

    @Value("${check.server.apikey}")
    String apiKey;

    @Autowired
    RestTemplate restTemplate;

    public ResultJson<JSONArray> listAlarms(WaringParam waringParam){
        try {
            String url = ServerUrl + "/api/json/alarm/listAlarms?apiKey=" + apiKey;
            if (StringUtils.isNotEmpty(waringParam.getSeverity())) url += ("&severity=" + waringParam.getSeverity());
            if (StringUtils.isNotEmpty(waringParam.getDeviceName())) url += ("&deviceName=" + waringParam.getDeviceName());
            if (StringUtils.isNotEmpty(waringParam.getCategory())) url += ("&category=" + waringParam.getCategory());
            if (StringUtils.isNotEmpty(waringParam.getFromTime())) url += ("&fromTime=" + waringParam.getFromTime());
            if (StringUtils.isNotEmpty(waringParam.getToTime())) url += ("&toTime=" + waringParam.getToTime());
            log.info("waringUrl:{}",url);

            try {
                JSONArray alarms = restTemplate.getForObject(url, JSONArray.class);
                return HttpWebResult.getMonoSucResult(alarms);
            } catch (Exception e) {
                //参数错误、无有效数据等情况下，返回Object{}对象，会抛出解析异常
                return null;
            }
//            JSONObject alarms = restTemplate.getForObject(url, JSONObject.class);
//            String message = alarms.getString("message");
//            if (message != null && !message.isEmpty()) //如果有message，说明调用接口发生了错误或者无有效数据
//                return null;
//            else { //存在有效的告警数据(以数组的形式存在)，则转换成JSONArray对象
//                //JSONArray forObject = restTemplate.getForObject(url, JSONArray.class);
//                JSONArray forObject = JSON.parseArray(alarms.toJSONString());
//                return HttpWebResult.getMonoSucResult(forObject);
//            }
        } catch (RestClientException e) {
            log.error(e.getMessage(),e);
            return HttpWebResult.getMonoError(e.getMessage());
        }
    }

}
