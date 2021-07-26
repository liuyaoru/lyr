package com.cf.crs.service;


import com.alibaba.fastjson.JSONObject;
import com.cf.crs.entity.HeiBeiSmartUser;
import com.cf.util.http.ResultJson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class HeBeItsmOperationUserSynService {

    @Autowired
    private RestTemplate restTemplate;
//正则匹配是否是规范邮箱
    private static boolean checkEmaile(String emaile){
        String RULE_EMAIL = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
        //正则表达式的模式
        Pattern p = Pattern.compile(RULE_EMAIL);
        //正则表达式的匹配器
        Matcher m = p.matcher(emaile);
        //进行正则匹配
        return m.matches();
    }

    /**向itsm插入用户**/
    public JSONObject operationSyn(HeiBeiSmartUser heiBeiSmartUser)
    {
        JSONObject jsonObject=new JSONObject();
        String url="http://10.131.238.214:8600/api/json/v2/admin/addUser";
        try {
            if(!StringUtils.isNotEmpty(heiBeiSmartUser.getUserMail())||!checkEmaile(heiBeiSmartUser.getUserMail()))
            {
                heiBeiSmartUser.setUserMail("test@test.com");

            }
        url = url + "?apiKey=bee831e2999162b8385c7d69f893aeb6&userName="
                    + heiBeiSmartUser.getLoginName() +
                    "&privilege=" + heiBeiSmartUser.getNetPrivilege() +
                    "&password=" + heiBeiSmartUser.getPassword() +
                    "&emailId=" + heiBeiSmartUser.getUserMail()+"&tZone=Asia/Shanghai";
           ResponseEntity response = restTemplate.exchange(url, HttpMethod.POST, null,JSONObject.class);
        }catch (Exception e)
        {
            log.error("itsm用户同步错误",e);
        }

        return  jsonObject;
    }
}
