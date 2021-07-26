package com.cf.crs.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.cf.crs.config.config.GetPageNumberAndNumber;
import com.cf.crs.config.config.HeiBeiClientConfig;
import com.cf.crs.entity.HeiBeiSmartUser;
import com.cf.crs.mapper.HeiBeiSmartUserMapper;
import com.cf.util.http.HttpWebResult;
import com.cf.util.http.ResultJson;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import java.util.*;


@Slf4j
@Service
public class HeBeiGetAllUserByClientToken {

    @Autowired
    private HeBeItsmOperationUserSynService itsmUserSynService;
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HeiBeiClientConfig heiBeiClientConfig;

    @Autowired
    private GetPageNumberAndNumber pageNumberAndNumber;

    @Autowired
    private HeiBeiSmartUserMapper heiBeiSmartUserMapper;

    @Autowired
    private HeBeiNetManagerServerUserService managerServerUserServiceAddUser;

    /**
     * 通过用户id更新部门id
     * @param UserId
     * @param departIds
     * @return
     */


    public Integer updateAllUserDepartByUserId(long UserId,String departIds)
    {

      return heiBeiSmartUserMapper.update(null,new UpdateWrapper<HeiBeiSmartUser>().eq("userId",UserId).set("departIds",departIds));

    }

    /**
     * 通过用户id更新角色id
     * @param UserId
     * @param RoleIds
     * @return
     */
    public Integer updateAllUserRolesByUserId(long UserId,String RoleIds)
    {
      return  heiBeiSmartUserMapper.update(null,new UpdateWrapper<HeiBeiSmartUser>().eq("userId",UserId).set("roleIds",RoleIds));
    }

    /**
     *
     * 根据userid查询用户
     */
    public HeiBeiSmartUser selectUserByUserId(long id)
    {
       return heiBeiSmartUserMapper.selectOne(new QueryWrapper<HeiBeiSmartUser>().eq("userId",id));
    }
    /**
     * 点击同步按钮同步其它系统的用户
     */
    public void  synNetManageAndItsmOperationUser(List<HeiBeiSmartUser> userList)
    {
        userList.stream().forEach((user)->{

            itsmUserSynService.operationSyn(user);
            managerServerUserServiceAddUser.updateOrInsertUser(user,userList);
        });
        //**插入itsm系统的用户
    }

    /**
     *
     * list转map
     * @param smartList
     */
    public Map map(List<HeiBeiSmartUser> smartList)
    {

        Iterator iterator=smartList.iterator();
        Map map=new HashMap();
        while(iterator.hasNext())
        {
         HeiBeiSmartUser smartUser= (HeiBeiSmartUser) iterator.next();
            map.put(smartUser.getUserId(),smartUser);
        }
        return  map;

    }
    private List<HeiBeiSmartUser> getAllUserList()
    {
          List<HeiBeiSmartUser> smartList=heiBeiSmartUserMapper.selectList(new QueryWrapper<HeiBeiSmartUser>());
 return  smartList;
    }
    //通过uid更新所有用户的角色
    public void updateAllUserRole()
    {

       List<HeiBeiSmartUser> smartList=getAllUserList();
        String token=GeToken();
       Iterator iterator=smartList.iterator();
       HttpHeaders httpHeaders=new HttpHeaders();
        httpHeaders.add("Authorization",token);
      while(iterator.hasNext())
      {
          HeiBeiSmartUser heiBeiSmartUser=(HeiBeiSmartUser)iterator.next();
          String url=heiBeiClientConfig.getUrl()+"/idaas/manage/open-api/pmi/user/"+heiBeiSmartUser.getUserId();
          Map<String, Object> map = new HashMap<String, Object>();
          map.put("USER_ID",heiBeiSmartUser.getUserId());
          HttpEntity<Map<String,Object>> requestEntity = new HttpEntity<>(map, httpHeaders);
          ResponseEntity<JSONObject> exchange = restTemplate.exchange(url,HttpMethod.GET,requestEntity,JSONObject.class);
          JSONObject jsonObject=exchange.getBody();
            JSONObject jsonObject1=jsonObject.getJSONObject("data");
         JSONArray jsonArray=jsonObject1.getJSONArray("roles");
          JSONArray jsonOrgs=jsonObject1.getJSONArray("orgs");
         if(jsonArray.size()>0) {
             StringBuilder stringBuilder = new StringBuilder();
             for (int i = 0; i < jsonArray.size(); i++) {
                 JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                 stringBuilder.append(jsonObject2.getString("roleId"));
                 stringBuilder.append(":");

             }
             String roleIds = stringBuilder.deleteCharAt(stringBuilder.length() - 1).toString();

             if (!heiBeiSmartUser.getRoleIds().equals(roleIds)) {
            updateAllUserRolesByUserId(heiBeiSmartUser.getUserId(),roleIds);
             }

         }
          if(jsonOrgs.size()>0) {
              StringBuilder stringBuilder = new StringBuilder();
              for (int i = 0; i < jsonOrgs.size(); i++) {
                  JSONObject jsonObject2 = jsonOrgs.getJSONObject(i);
                  stringBuilder.append(jsonObject2.getString("orgId"));
                  stringBuilder.append(":");

              }
              String orgsIds = stringBuilder.deleteCharAt(stringBuilder.length() - 1).toString();
              if (!heiBeiSmartUser.getDepartIds().equals(orgsIds)) {
                  updateAllUserDepartByUserId(heiBeiSmartUser.getUserId(),orgsIds);
              }

          }
      }

    }

    //通过配置文件设置每页查询多少并插入数据库
    public ResultJson ByPageConfigGetAllData()
    {
        try {
            InsertAllData(pageNumberAndNumber.getPageSetProperties().getEvery_page());
           return  HttpWebResult.getMonoSucStr();
        }catch (Exception e)
        {
           return HttpWebResult.getMonoError("查询失败");
        }

    }
    public JSONObject GetTokenByClient()
    {
        String url=heiBeiClientConfig.getUrl()+"/idaas/auth/oauth2/token?" +
                "grantType="+heiBeiClientConfig.getClientgrantType()+"&clientSecret=" +
                heiBeiClientConfig.getClientSecret()+"&clientId="+
                heiBeiClientConfig.getClientId();
        JSONObject object=restTemplate.getForObject(url,JSONObject.class);
           return  object;
    }


    //获取用户判断用户是否更新
    public boolean JudeGeIfExist(Map map,HeiBeiSmartUser smartUser)
    {
        boolean flag=false;
        boolean flag1=false;
        boolean flag2=false;
        boolean flag3=false;
        boolean flag4=false;
        boolean flag5=false;
        boolean flag6=false;
        boolean flag7=false;
         HeiBeiSmartUser smartUser1=(HeiBeiSmartUser) map.get(smartUser.getUserId());
         if(StringUtils.isNotEmpty(smartUser.getNote())) {
             if (!smartUser.getNote().equals(smartUser1.getNote())) {
                 flag1 = true;
             }
         }
        if(StringUtils.isNotEmpty(smartUser.getLoginName())) {
            if (!smartUser.getLoginName().equals(smartUser1.getLoginName())) {
                flag2 = true;
            }
        }
        if(StringUtils.isNotEmpty(smartUser.getUserMail())) {
            if (!smartUser.getUserMail().equals(smartUser1.getUserMail())) {
                flag3 = true;
            }
        }
        if(StringUtils.isNotEmpty(smartUser.getUserMobile())) {
            if (!smartUser.getUserMobile().equals(smartUser1.getUserMobile())) {
                flag4 = true;
            }
        }
        if(StringUtils.isNotEmpty(smartUser.getUserName())) {
            if (!smartUser.getUserName().equals(smartUser1.getUserName())) {
                flag5 = true;
            }
        }
        if(StringUtils.isNotEmpty(smartUser.getUserOph())) {
            if (!smartUser.getUserOph().equals(smartUser1.getUserOph())) {
                flag6 = true;
            }
        }
        if(smartUser.getGender()!=smartUser1.getGender()) {
        flag7=true;
        }
             flag=flag1||flag2||flag3||flag4||flag5||flag6||flag7;


        return  flag;
    }


    //通过客户端模式获取token
    public String GeToken()
    {
        JSONObject jsonObject=GetTokenByClient();
        HashMap hashMap= (HashMap) jsonObject.get("data");
        String access_token=hashMap.get("accessToken").toString();
        return  access_token;
    }
       @Transactional  // 开启事务获取所有用户并插入数据库
    public List<HeiBeiSmartUser> InsertAllData(String  everyPageNumber)
      {
          Map UserMaps=map(getAllUserList());
          List<HeiBeiSmartUser> smartUserList=null;
          List<JSONObject> jsonObjects=GetUserBySelectPagel(everyPageNumber);
          Iterator<JSONObject> iter = jsonObjects.iterator();
          HeiBeiSmartUser userCopy=null;
          HeiBeiSmartUser userCopy1=null;
          try {
              while (iter.hasNext()) {
                  JSONObject everyPageSizeJsonObject = iter.next();
                  JSONArray jsonArray = everyPageSizeJsonObject.getJSONArray("rows");
                  smartUserList = jsonArray.toJavaList(HeiBeiSmartUser.class);
                  synNetManageAndItsmOperationUser(smartUserList);
                  for (HeiBeiSmartUser user : smartUserList) {
                      if (!ObjectUtils.isEmpty(UserMaps.get(user.getUserId()))) {
                          if (JudeGeIfExist(UserMaps, user)) {
                              heiBeiSmartUserMapper.update(user,new UpdateWrapper<HeiBeiSmartUser>().eq("userId",user.getUserId())
                              );
                          }

                      } else {
                          log.info("insert{}", user);
                          heiBeiSmartUserMapper.insert(user);
                      }
                  }

              }
          }catch (Exception e)
          {
              e.printStackTrace();
          }
          updateAllUserRole();

          return smartUserList;
      }

 //获得用户总记录的条数
   public int GetTotalSize (String token,String CurrentPage, String everyPageNumber)
    {
    String access_token=token;
    HttpHeaders headers=new HttpHeaders();
    headers.add("Authorization",access_token);
    Map<String,Object> param = new HashMap<>();
    param.put("page", CurrentPage);
    param.put("rows", everyPageNumber);
    String pageUrl=heiBeiClientConfig.getUrl()+"/idaas/manage/open-api/pmi/user/page?page={page}&rows={rows}";
    ResponseEntity response = restTemplate.exchange(pageUrl, HttpMethod.GET, new HttpEntity<String>(headers),JSONObject.class,param);
    JSONObject userInfo= (JSONObject) response.getBody();
    log.info("userInfo:{}", JSON.toJSONString(userInfo));
    int totalSize=(int)userInfo.get("total");
    return totalSize;
}
    //循环获取所有用户
    public List<JSONObject> GetUserBySelectPagel(String  everyPageNumber)
    {

        String access_token=GeToken();
        int totalSize=GetTotalSize(access_token,"1",everyPageNumber);
       int everyPageSize=Integer.valueOf(everyPageNumber);
       int totalPage;
      totalPage=totalSize%everyPageSize==0?(totalSize/everyPageSize):((totalSize/everyPageSize)+1);
      List<JSONObject> totalUserInfo=new ArrayList();
      for(int CurrentPage=1;CurrentPage<=totalPage;CurrentPage++)
      {
          String  CurrentPagel=CurrentPage+"" ;
          HttpHeaders headers=new HttpHeaders();
          headers.add("Authorization",access_token);
          Map<String,Object> param = new HashMap<>();
          param.put("page", CurrentPagel);
          param.put("rows", everyPageNumber);
          String pageUrl=heiBeiClientConfig.getUrl()+"/idaas/manage/open-api/pmi/user/page?page={page}&rows={rows}";
          ResponseEntity response = restTemplate.exchange(pageUrl, HttpMethod.GET, new HttpEntity<String>(headers),JSONObject.class,param);
          JSONObject everyPageUserInfo= (JSONObject) response.getBody();
          log.info("userInfo:{}", JSON.toJSONString(everyPageUserInfo));
            totalUserInfo.add(everyPageUserInfo);
      }

        return totalUserInfo;


    }



}
