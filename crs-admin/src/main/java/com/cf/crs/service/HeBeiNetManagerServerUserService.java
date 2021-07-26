package com.cf.crs.service;



import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cf.crs.config.config.NetManagerClientConfig;
import com.cf.crs.entity.HeiBeiSmartUser;
import com.cf.crs.entity.ItsmUser;
import com.cf.crs.entity.NetManagerUser;
import com.cf.crs.mapper.HeiBeiSmartUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;

import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class HeBeiNetManagerServerUserService {

    @Autowired
    private NetManagerClientConfig netManagerClientConfig;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private HeiBeiSmartUserMapper smartUserMapper;
    public String GetUrl()
    {
        String url=netManagerClientConfig.getUrl();
        return url;
    }

    //ServiceDesk REST API v3的接口用户同步
    public void  NetManagerServerAddUse(HeiBeiSmartUser heiBeiSmartUser)
    {

        try {
        String url="http://10.131.238.156:8800/api/v3/users";
        HttpClient httpClient = null;
        HttpPost postMethod = null;
        HttpResponse response = null;
        ItsmUser itsmUser=new ItsmUser();
        itsmUser.setIs_vipuser(false);
        itsmUser.setName(heiBeiSmartUser.getUserName());
        itsmUser.setRequester_allowed_to_view("0");
        itsmUser.setLogin_name(heiBeiSmartUser.getLoginName());
        itsmUser.setPassword(heiBeiSmartUser.getPassword());
            JSONObject Forms=new JSONObject();
            Forms.put("name",heiBeiSmartUser.getUserName());
            Forms.put("is_vipuser",false);
            Forms.put("requester_allowed_to_view","0");
            Forms.put("login_name",heiBeiSmartUser.getLoginName());
            Forms.put("password",heiBeiSmartUser.getPassword());
            JSONObject Formsl=new JSONObject();
            Formsl.put("user",Forms);
        httpClient = HttpClients.createDefault();
        postMethod = new HttpPost(url);// 传入URL地址
        // 设置请求头
        //postMethod.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:50.0) Gecko/20100101 Firefox/50.0");
        postMethod.addHeader("Content-type", "application/x-www-form-urlencoded; charset=utf-8");
        postMethod.addHeader("TECHNICIAN_KEY","46B9174A-6CAB-4B90-B175-2C1A1B30D990"); //设置APIKEY
        // 传入请求参数
        List<NameValuePair> paramList = new ArrayList<NameValuePair>();
        paramList.add(new BasicNameValuePair("input_data", Formsl.toJSONString()));
        postMethod.setEntity(new UrlEncodedFormEntity(paramList, "UTF-8"));

            response = httpClient.execute(postMethod);// 获取响应
            int statusCode = response.getStatusLine().getStatusCode();
            //System.out.println("HTTP Status Code:" + statusCode);
//			if (statusCode != 200 && statusCode != 201) {
//				System.out.println("HTTP请求未成功！HTTP Status Code:" + response.getStatusLine());
//				return null;
//			}
            HttpEntity httpEntity = response.getEntity();
            String responseContent = EntityUtils.toString(httpEntity);
        /*    JSONObject respJson = new JSONObject(responseContent);*/
            EntityUtils.consume(httpEntity);// 释放资源
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    /**
     * 判断用户是否更新,并推送给监控子系统
     */
    public void  updateOrInsertUser(HeiBeiSmartUser heiBeiSmartUser,List<HeiBeiSmartUser> heiBeiSmartUsers)
    {
        boolean flag=false;
      List<HeiBeiSmartUser> smartUsers=heiBeiSmartUsers;
       Iterator iterator=smartUsers.iterator();
       Map map=new HashMap();
       while(iterator.hasNext())
       {
            HeiBeiSmartUser heiBeiSmartUser1=(HeiBeiSmartUser)iterator.next();
            map.put(heiBeiSmartUser1.getUserId(),heiBeiSmartUser1);
       }
       if(!StringUtils.isEmpty(map.get(heiBeiSmartUser.getUserId())))
       {
           if(!StringUtils.isEmpty(heiBeiSmartUser.getRoleIds()))
           {
               HeiBeiSmartUser smartUser=(HeiBeiSmartUser)map.get(heiBeiSmartUser.getUserId());
               if(!heiBeiSmartUser.getRoleIds().equals(smartUser.getRoleIds()))
               {
              updateUser(heiBeiSmartUser);
               }

           }
       }else{
           NetManagerServerAddUse(heiBeiSmartUser);
       }
    }

 /*   *//**
     * 监控子系统用户更新
     * @param heiBeiSmartUser
     * @return
     *//*
    public void updateUser(HeiBeiSmartUser heiBeiSmartUser)
    {
        String url="http://10.131.238.214:8600/api/json/v2/admin/updateUserConfiguration";
        Map map=new HashMap();
        map.put("userName",heiBeiSmartUser.getUserName());
        map.put("privilege",heiBeiSmartUser.getNetPrivilege());
        map.put("emailId",heiBeiSmartUser.getUserMail());
        map.put("apiKey","bee831e2999162b8385c7d69f893aeb6");
       ResponseEntity responseEntity=restTemplate.exchange(url,HttpMethod.POST,null,JSONObject.class,map);

    }*/


    /**
     * 监控子系统用户更新
     * @param heiBeiSmartUser
     * @return
     */
    public void updateUser(HeiBeiSmartUser heiBeiSmartUser)
    {
        String url="http://10.131.238.214:8600/api/json/v2/admin/updateUserConfiguration";
        Map map=new HashMap();
         String rolesIds=heiBeiSmartUser.getRoleIds();
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher matcher=p.matcher(regEx);
        //将输入的字符串中非数字部分用空格取代并存入一个字符串
        String string = matcher.replaceAll("").trim();
      //以空格为分割符在讲数字存入一个字符串数组中
        String[] strArr = string.split("");
         if(StringUtils.isEmpty(rolesIds))
         {
         for(String s: strArr)
         {
             int roleId=Integer.parseInt(s);
             if(roleId!=0)
             {
                 if(roleId==1)
                 {
                     heiBeiSmartUser.setRoleIds("Administrator");

                 }else  if(roleId==4)
                 {
                     heiBeiSmartUser.setRoleIds("Operator");

                 }
             }
         }
         }
        map.put("userName",heiBeiSmartUser.getUserName());
         map.put("privilege",heiBeiSmartUser.getRoleIds());
        map.put("emailId",heiBeiSmartUser.getUserMail());
        map.put("apiKey","bee831e2999162b8385c7d69f893aeb6");
        ResponseEntity responseEntity=restTemplate.exchange(url,HttpMethod.POST,null,JSONObject.class,map);

    }
    //同步推送新增用户
    public  JSONObject   NetManagerServerAddUser(HeiBeiSmartUser heiBeiSmartUser)
    {

        String url=GetUrl()+"addUser";
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<String, Object>();
        paramMap.add("apiKey",netManagerClientConfig.getApikey());
        String UserName=heiBeiSmartUser.getLoginName();
        UserName=UserName+"@company.com";
        paramMap.add("userName",UserName);
      //  paramMap.add("userName",heiBeiSmartUser.getUserName());
        paramMap.add("privilege",heiBeiSmartUser.getNetPrivilege());
        paramMap.add("password",heiBeiSmartUser.getPassword());
        paramMap.add("emailId",heiBeiSmartUser.getUserMail()+".com");;
        JSONObject jsonObject=new JSONObject();
        try {
         jsonObject = restTemplate.postForObject(url, paramMap, JSONObject.class);

            log.info("userAddSys:{}", JSON.toJSONString(jsonObject));
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        return jsonObject;

    }
    //同步推送更新用户
    public void    NetManagerServerUpdateUser(HeiBeiSmartUser heiBeiSmartUser)
    {

      String url=GetUrl()+"updateContactDetails";
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<String, Object>();
        paramMap.add("apiKey",netManagerClientConfig.getApikey());
        String UserName=heiBeiSmartUser.getLoginName();
        UserName=UserName+"@company.com";
        paramMap.add("userName",UserName);
        paramMap.add("emailId",heiBeiSmartUser.getUserMail());
        paramMap.add("mobileNumber",heiBeiSmartUser.getUserMobile());
        JSONObject jsonObject=restTemplate.postForObject(url,paramMap,JSONObject.class);
        log.info("userUpdate:{}", JSON.toJSONString(jsonObject));
              //  phoneNumber=04424453446& mobileNumber=04424453446||domainName=testdo


    }

    //同步更新密码
    public JSONObject NetManagerServerUpdateByUserPassword(HeiBeiSmartUser heiBeiSmartUser,String oldPassword,String oldLoginName)
    {

        String url=GetUrl()+"changePassword";
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<String, Object>();
        paramMap.add("apiKey",netManagerClientConfig.getApikey());
        String UserName=oldLoginName;
        UserName=UserName+"@company.com";
        paramMap.add("userName",UserName);
        paramMap.add("newPassword",heiBeiSmartUser.getPassword());
        paramMap.add("oldPassword",oldPassword);
        paramMap.add("userId",findUserIdByLoginName(UserName));
        JSONObject jsonObject=restTemplate.postForObject(url,paramMap,JSONObject.class);
        log.info("userInfo:{}", JSON.toJSONString(jsonObject));
        return jsonObject;


    }
    //通过UserName获取ITSM系统的uid
    public String findUserIdByLoginName(String UserName)
    {
        List<NetManagerUser> netManagerUsers=null;
        String UserId="";

        netManagerUsers=NetManagerGetAllUser();
        Optional<NetManagerUser> netManagerUser = netManagerUsers.stream().filter(item -> item.getUSERNAME().equals(UserName)).findFirst();
        if (netManagerUser.isPresent()) {
            // 存在
            NetManagerUser managerUser=  netManagerUser.get();
           UserId=managerUser.getUSERID();
            log.info("userInfo:{}", JSON.toJSONString(UserId));
        } else {
            System.out.println("没有这个用户");
            // 不存在
        }
        return UserId;

    }

    //同步推送删除用户
    public void   NetManagerServerDeleteUser(HeiBeiSmartUser smartUser)
    {
        List<NetManagerUser> netManagerUsers=null;
        JSONObject jsonObject=null;
        String url=GetUrl()+"deleteUser";
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<String, Object>();
    /*    String Username=smartUser.getLoginName();
        Username=Username+"@company.com";*/
        String Username=smartUser.getLoginName()+"@company.com";
        paramMap.add("apiKey",netManagerClientConfig.getApikey());
        //获取netmanageruid
        netManagerUsers=NetManagerGetAllUser();
        Optional<NetManagerUser> netManagerUser = netManagerUsers.stream().filter(item -> item.getUSERNAME().equals(Username)).findFirst();
        if (netManagerUser.isPresent()) {
            // 存在
            NetManagerUser managerUser=  netManagerUser.get();
            paramMap.add("userId",managerUser.getUSERID());
            jsonObject=restTemplate.postForObject(url,paramMap,JSONObject.class);
            log.info("userInfoDelete:{}", JSON.toJSONString(jsonObject));
        } else {
            log.info("userInfoDelete:{}", "没有这个用户");
           /* System.out.println("没有这个用户");*/
            // 不存在
        }

/*        System.out.println(jsonObject);*/
    }


    //获取所有用户
    public List<NetManagerUser>  NetManagerGetAllUser()
    {
        String url=GetUrl()+"listUsers?apiKey="+netManagerClientConfig.getApikey();
      List<NetManagerUser> userList=restTemplate.getForObject(url,List.class);
        List<NetManagerUser> netManagerUsers = JSONObject.parseArray(JSONObject.toJSONString(userList)).toJavaList(NetManagerUser.class);
        log.info("getAllUserInfo:{}", JSON.toJSONString(userList));
       return  netManagerUsers;
    }

}
