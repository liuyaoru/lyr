package com.cf.crs.service;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cf.crs.common.redis.RedisUtils;
import com.cf.crs.config.config.HeiBeiClientConfig;
import com.cf.crs.entity.*;
import com.cf.crs.mapper.CityMenuMapper;
import com.cf.crs.mapper.CityUserMapper;
import com.cf.crs.mapper.HeiBeiSmartUserMapper;
import com.cf.crs.mapper.SysUserMapper;
import com.cf.util.http.HttpWebResult;
import com.cf.util.http.ResultJson;
import com.cf.util.utils.CacheKey;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
@Slf4j
public class HeiBeiClientLoginService {
    @Autowired
  private HeiBeiSmartUserMapper smartUserMapper;
    @Autowired
    private HeiBeiClientConfig ClientConfig;
    @Autowired
    private CityRoleService cityRoleService;

    @Autowired
      private CityMenuMapper cityMenuMapper;
    @Autowired
    private CityUserMapper cityUserMapper;

    @Autowired
   private  SysUserMapper sysUserMapper;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    RestTemplate restTemplate;




    /**
     * 判断是否更新了,如果更新了就update
     */
/*    public void UpdateUser(HeiBeiSmartUser smartUser,JSONObject jsonObject)
    {
        HeiBeiSmartUser heiBeiSmartUser=new HeiBeiSmartUser();
            String userId=jsonObject.getString("userId");
            String login=jsonObject.getString("loginName");
             String userMobile=jsonObject.getString("userMobile");
           String userMail=jsonObject.getString("userMail");
        String userName=jsonObject.getString("userName");
         String userOph=jsonObject.getString("userOph");
          String note= jsonObject.getString("note");
          boolean flag1=false;
        boolean flag2=false;
        boolean flag3=false;
        boolean flag4=false;
        boolean flag5=false;
        boolean flag6=false;
        boolean flag7=false;
        boolean flag8=false;
        boolean flag9=false;
        boolean flag10=false;
        if(StringUtils.isNotEmpty(jsonObject.getString("gender")));
        {
            Integer gender=(int)jsonObject.get("gender");
            heiBeiSmartUser.setGender(gender);
            if(gender!=smartUser.getGender())
            {
                flag1=true;

            }

        }
          if(StringUtils.isNotEmpty(userId))
          {
              if(!userId.equals(heiBeiSmartUser.getUserId()))
              {
                  flag2=true;
              }

          }else {

          }
          if(StringUtils.isNotEmpty(login))
          {
              if(!login.equals(heiBeiSmartUser.getLoginName()))
              {
                  flag3=true;
              }

          }else{

          }
          if(StringUtils.isNotEmpty(userMobile))
          {
              if(!userMobile.equals(heiBeiSmartUser.getUserMobile()))
              {
                  flag4=true;
              }

          }else{

          }
          if(StringUtils.isNotEmpty(userMail))
          {
              if(!userMail.equals(heiBeiSmartUser.getUserMail()))
              {
                  flag5=true;
              }
          }else{

          }
          if(StringUtils.isNotEmpty(userName))
          {
              if(!userName.equals(heiBeiSmartUser.getUserName()))
              {
                  flag6=true;
              }

          }else{

          }
          if(StringUtils.isNotEmpty(userOph))
          {
              if(!userOph.equals(heiBeiSmartUser.getUserOph()))
              {
                  flag7=true;
              }

          }else {

          }
          if(StringUtils.isNotEmpty(note))
          {
              if(!note.equals(heiBeiSmartUser.getNote()))
              {
                  flag8=true;
              }

          }else{

         }
        JSONArray jsonArray =jsonObject.getJSONArray("roles");
        JSONArray departArray =jsonObject.getJSONArray("orgs");
        if(jsonArray.size()>0) {
            StringBuilder stringBuilder=new StringBuilder();
            for (int i = 0; i > jsonArray.size(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String roleId = jsonObject1.getString("roleId");
                if(StringUtils.isNotEmpty(smartUser.getRoles()))
                {
                    if(!smartUser.getRoles().contains(roleId))
                    {
                        flag9=true;
                        break;
                    }
                }
             stringBuilder.append(roleId);
            }
            heiBeiSmartUser.setRoles(stringBuilder.toString());
        }
        if(departArray.size()>0) {
            StringBuilder stringBuilder=new StringBuilder();
            for (int i = 0; i > departArray.size(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String orgId= jsonObject1.getString("orgId");
                stringBuilder.append(orgId);
            }
            heiBeiSmartUser.setDepartIds(stringBuilder.toString());
        }
        if(flag1&&flag2&&flag3&&flag4&&flag5&&flag6&&flag7&&flag8&&flag9&&flag10)
        {
            heiBeiSmartUserMapper.update(smartUser,new UpdateWrapper<HeiBeiSmartUser>().eq("loginName",smartUser.getLoginName())
                    .set("userId",userId)
                    .set("gender",heiBeiSmartUser.getGender())
                    .set("loginName",login)
                    .set("userName",userName)
                    .set("userMobile",userMobile)
                    .set("userMail",userMail)
                    .set("note",note)
                    .set("userOph",userOph)
                    .set("roles",heiBeiSmartUser.getRoles())
                    .set("departs",heiBeiSmartUser.getDepartIds())
            );
        }


    }*/

/*

    */
/**
     * 查询是否有用户没有就插入
     *//*

    public void  getAllUserByAccessToken(String Accesstoken) {

        if (StringUtils.isNotEmpty(Accesstoken)) {
            String url = heiBeiClientConfig.getUrl() + "/idaas/manage/open-api/auth/user-info";
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", Accesstoken);
            ResponseEntity response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<String>(headers), JSONObject.class);
            JSONObject deviceList = (JSONObject) response.getBody();
            JSONObject jsonObject=deviceList.getJSONObject("data");
            String userId=jsonObject.getString("userId");
            if(StringUtils.isNotEmpty(userId))
            {

                HeiBeiSmartUser heiBeiSmartUser=smartUserMapper.selectOne(new QueryWrapper<HeiBeiSmartUser>().eq("userId",userId));
                 UpdateUser(heiBeiSmartUser,jsonObject);


            }else {
                HeiBeiSmartUser heiBeiSmartUser=new HeiBeiSmartUser();
                heiBeiSmartUser.setUserId(Integer.valueOf(jsonObject.getString("userId")));
                heiBeiSmartUser.setLoginName(jsonObject.getString("loginName"));
                heiBeiSmartUser.setUserMobile(jsonObject.getString("userMobile"));
                heiBeiSmartUser.setUserMail(jsonObject.getString("userMail"));
                heiBeiSmartUser.setGender((int)jsonObject.get("gender"));
                heiBeiSmartUser.setUserName(jsonObject.getString("userName"));
                heiBeiSmartUser.setUserMail(jsonObject.getString("userMail"));
                heiBeiSmartUser.setUserOph(jsonObject.getString("userOph"));
                heiBeiSmartUser.setNote(jsonObject.getString("note"));
                JSONArray jsonArray =jsonObject.getJSONArray("roles");
                JSONArray departArray =jsonObject.getJSONArray("orgs");
                if(jsonArray.size()>0) {
                    StringBuilder stringBuilder=new StringBuilder();
                    for (int i = 0; i > jsonArray.size(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String roleId = jsonObject1.getString("roleId");
                        stringBuilder.append(roleId);
                    }
                    heiBeiSmartUser.setRoles(stringBuilder.toString());
                }
                if(departArray.size()>0) {
                    StringBuilder stringBuilder=new StringBuilder();
                    for (int i = 0; i > departArray.size(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String orgId= jsonObject1.getString("orgId");
                        stringBuilder.append(orgId);
                    }
                    heiBeiSmartUser.setDepartIds(stringBuilder.toString());
                }
       heiBeiSmartUserMapper.insert(heiBeiSmartUser);

            }

        }
    }
*/

 /*   *//***
     * 授权码模式赋予用户角色
     * @param
     *//*

    public Integer updateUserSetRole(String userId, String roleId)
    {

        Integer index= heiBeiSmartUserMapper.update(null,new UpdateWrapper<HeiBeiSmartUser>().eq("userId",userId).set("roleId",roleId));

        return index;
    }
*/
    /**
     * 验证第三方登录用户
     *
     * @param code
     * @return
     */


    public ResultJson getUser(String code) {
        //获取第三方登录信息
        JSONObject result = getTokenByCode(code);
        /*   result.getString("data");*/
        HashMap hashMap= (HashMap) result.get("data");
        String access_token=hashMap.get("accessToken").toString();
        //  String loginName=getLoginNameByToken(access_token);
        if (StringUtils.isEmpty(access_token)) {
            //获取token失败
            log.info("获取iam登录token失败");
            //return HttpWebResult.getMonoError(result.getString("msg"));
            return HttpWebResult.getMonoError("用户名或密码错误");
        }
        JSONObject user = getUserByToken(access_token);
        HashMap hashMapLoginName=(HashMap) user.get("data");
        String loginName=hashMapLoginName.get("loginName").toString();
        if (StringUtils.isEmpty(loginName)) {
            return HttpWebResult.getMonoError("用户名或密码错误");
        }
        //验证第三方用户登录权限
        HeiBeiSmartUser heiBeiSmartUser=smartUserMapper.selectOne(new QueryWrapper<HeiBeiSmartUser>().eq("loginName",loginName));
        if (heiBeiSmartUser == null) return HttpWebResult.getMonoError("用户名或密码错误");
        String auth = heiBeiSmartUser.getAuth();
        if (StringUtils.isEmpty(auth) || "0".equalsIgnoreCase(auth)) return HttpWebResult.getMonoError("用户名或密码错误");
        return createToken(heiBeiSmartUser.getLoginName(), heiBeiSmartUser, heiBeiSmartUser.getAuth());
    }



    /**
     * 获取登录token
     *
     * @param code
     * @return
     */

private JSONObject getTokenByCode(String code)
{

    String url=ClientConfig.getUrl()+"/idaas/auth/oauth2/token?" +
            "grantType=" +ClientConfig.getGrantType()+
            "&clientSecret=" +ClientConfig.getClientSecret()+
            "&" +
            "clientId=" +ClientConfig.getClientId()+
            "&"+ "code=" +code+
            "&" +
            "redirectUri="+
            ClientConfig.getRedirectUri();
    log.info("code:{}",code);
    JSONObject result=restTemplate.getForObject(url,JSONObject.class);
    log.info("code login result:{}",JSON.toJSONString(result));
    return result;

}


    private ResultJson createToken(String userName, Object sysUser, String auth) {
        String token = CacheKey.USER_TOKEN + ":" + System.currentTimeMillis();
        redisUtils.set(CacheKey.USER_NAME_TOKEN + ":" + userName, token, 10);
        //验证成功，返回token和用户信息
        redisUtils.set(token, sysUser, 60 * 60 * 2);
     redisUtils.set(token + ":menu", getMenuIds(auth), 60 * 60 * 2);
        return HttpWebResult.getMonoSucResult(token, sysUser);
    }
    /**
     * 获取用户的菜单和考评对象权限
     *
     * @param auth 用户的角色id（多个id以逗号隔开）
     * @return
     */
    public Map<String, Set> getMenuIds(String auth) {
        Map<String, Set> menus = Maps.newHashMap();
        //菜单id列表
        Set menuIdSet = Sets.newHashSet();
        //菜单列表
        Set menuSet = Sets.newHashSet();
        //考评对象列表
        Set disPlaySet = Sets.newHashSet();
        if (StringUtils.isNotEmpty(auth)) {
            List<CityRole> roleList = cityRoleService.getRoleList(auth);
            if (!CollectionUtils.isEmpty(roleList)) {
                roleList.forEach(role -> {
                    String auths = role.getAuth();
                    String displayNameList = role.getDisplayNameList();
                    if (StringUtils.isNotEmpty(auths)) menuIdSet.addAll(Arrays.asList(auths.split(",")));
                    if (StringUtils.isNotEmpty(displayNameList))
                        disPlaySet.addAll(Arrays.asList(displayNameList.split(",")));
                });
            }
        }
        List<CityMenu> menuList = cityMenuMapper.selectList(new QueryWrapper<CityMenu>());

        menuList.forEach(menu -> {
            Long id = menu.getId();
            if (menuIdSet.contains(String.valueOf(id)) || "1".equalsIgnoreCase(auth)) menuSet.add(menu);
        });
        //menus.put("menuId",menuIdSet);
        menus.put("menu", menuSet);
        menus.put("display", disPlaySet);
        return menus;
    }


    /**
     * 根据token获取
     *
     * @param access_token
     * @return
     */

    private JSONObject getUserByToken(String access_token)
    {

        //token="676e9122-9b96-4c84-93b5-151dd23cdadd";
        HttpHeaders headers=new HttpHeaders();
        headers.add("Authorization",access_token);
        String url=ClientConfig.getUrl()+"/idaas/manage/open-api/auth/user-info";
        ResponseEntity response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<String>(headers),JSONObject.class);
        JSONObject userInfo= (JSONObject) response.getBody();
        log.info("userInfo:{}", JSON.toJSONString(userInfo));
        return userInfo;
    }
   /* private JSONObject getUserByToken(String access_token) {
        String userUrl = clientConfig.getUrl() + "/idp/oauth2/getUserInfo?access_token={access_token}&client_id={client_id}";
        JSONObject userInfo = restTemplate.getForObject(userUrl, JSONObject.class, access_token, clientConfig.getClientId());
        log.info("userInfo:{}", JSON.toJSONString(userInfo));
        return userInfo;
    }
*/
    public ResultJson login(String userName, String password, String code) {
        //第三方登录
        if (StringUtils.isNotEmpty(code)) return getUser(code);
        //自己用户登录
        SysUser sysUser = sysUserMapper.selectOne(new QueryWrapper<SysUser>().eq("username", userName));
        if (sysUser == null) return HttpWebResult.getMonoError("用户名或密码错误");
        String md5Password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!md5Password.equalsIgnoreCase(sysUser.getPassword())) return HttpWebResult.getMonoError("用户名或密码错误");
        Object o = redisUtils.get(CacheKey.USER_NAME_TOKEN + ":" + userName);
        if (o != null && StringUtils.isNotEmpty(o.toString()))
            return HttpWebResult.getMonoError("此用户登录过于频繁，请10s再进行登录操作");
        sysUser.setPassword(null);
        return createToken(sysUser.getUsername(), sysUser, sysUser.getAuth());
    }


    public ResultJson logout(String userName) {
        redisUtils.delete(CacheKey.USER_NAME_TOKEN + ":" + userName);
        return HttpWebResult.getMonoSucStr();
    }


}
