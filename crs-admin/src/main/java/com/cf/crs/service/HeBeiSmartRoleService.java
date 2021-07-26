package com.cf.crs.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cf.crs.config.config.ClientConfig;
import com.cf.crs.config.config.HeiBeiClientConfig;
import com.cf.crs.entity.CityRole;
import com.cf.crs.entity.HeiBeiSmartUser;
import com.cf.crs.mapper.CityRoleMapper;
import com.cf.util.http.HttpWebResult;
import com.cf.util.http.ResultJson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@Slf4j
public class HeBeiSmartRoleService {

    @Autowired
    private CityRoleService cityRoleService;

    @Autowired
    private CityRoleMapper cityRoleMapper;

    @Autowired
    private HeiBeiClientConfig clientConfig;

    @Autowired
    private HeBeiGetAllUserByClientToken clientToken;

    @Autowired
    private RestTemplate restTemplate;

    /***
     * 本系统插入角色
     * @return
     */

    public ResultJson insertRole(CityRole cityRole)
    {
      cityRoleMapper.insert(cityRole);
        return  null;
    };

    /**
     * 本系统修改角色
     * @return
     */
    public ResultJson alterRole(CityRole cityRole)
    {
        cityRoleMapper.update(null,new UpdateWrapper<CityRole>().set("","").eq("",""));
        return  null;
    }

    /**
     *
     * 删除角色
     * @return
     */
    public ResultJson deleteRole(CityRole cityRole)
    {

     cityRoleMapper.delete(new QueryWrapper<CityRole>().eq("",""));
        return  null;
    }


    /**
     * 向统一认证插入角色
     * @param cityRole
     * @return
     */
    public ResultJson insertAndSysRole(CityRole cityRole)
    {
        String url=clientConfig.getUrl()+"/idaas/manage/open-api/pmi/role/";
        String token=clientToken.GeToken();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization",token);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("roleName",cityRole.getRole_key());
        HttpEntity requestEntity = new HttpEntity<>(map, httpHeaders);
        ResponseEntity<JSONObject> exchange = restTemplate.exchange(url,HttpMethod.POST,requestEntity,JSONObject.class);
       JSONObject jsonObject1=exchange.getBody();
       log.info("Roleinfo:{}",jsonObject1);
       return HttpWebResult.getMonoSucResult(jsonObject1);
    }

    /**
     * 把角色推到统一认证
     */
    public ResultJson insertSysRole()
    {


        List<CityRole> cityRoleList=cityRoleMapper.selectList(new QueryWrapper<CityRole>());
        cityRoleList.stream().forEach((role->{

        insertAndSysRole(role);

        }));

        return  HttpWebResult.getMonoSucStr() ;
    }

    public ResultJson updateAndSysRole(long ROLE_ID,String roleName)
    {

        String url=clientConfig.getUrl()+"/idaas/manage/open-api/pmi/role/"+ROLE_ID;
        String token=clientToken.GeToken();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization",token);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("ROLE_ID",ROLE_ID);
        map.put("roleName",roleName);
        HttpEntity<Map<String,Object>> requestEntity = new HttpEntity<>(map, httpHeaders);
        ResponseEntity<JSONObject> exchange = restTemplate.exchange(url,HttpMethod.PUT,requestEntity,JSONObject.class);
        JSONObject jsonObject=exchange.getBody();
        log.info("updateinfo:{}",jsonObject);
        return HttpWebResult.getMonoSucResult(jsonObject);
    }

    /**
     * 统一认证删除角色
     * @param
     * @return
     */
    public ResultJson deleteAndSysRole(long id)
    {
        String url=clientConfig.getUrl()+"/idaas/manage/open-api/pmi/role/"+id;
        String token=clientToken.GeToken();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization",token);
        HttpEntity<Map<String,Object>> requestEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<JSONObject> exchange = restTemplate.exchange(url,HttpMethod.DELETE,requestEntity,JSONObject.class);
        JSONObject jsonObject=exchange.getBody();
        log.info("updateinfo:{}",jsonObject);
        return HttpWebResult.getMonoSucResult(jsonObject);
    }

    /**
     *
     * @param cityRole
     * @return//统一认证修改角色
     */
    public ResultJson alterAndSysRole(CityRole cityRole)
    {
        String url=clientConfig.getUrl()+"/idaas/manage/open-api/pmi/role/page";
        String token=clientToken.GeToken();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization",token);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("roleNam",cityRole.getName());
        HttpEntity<Map<String,Object>> requestEntity = new HttpEntity<>(map, httpHeaders);
        ResponseEntity<JSONObject> exchange = restTemplate.postForEntity(url, requestEntity, JSONObject.class);
        JSONObject jsonObject=exchange.getBody();
        return HttpWebResult.getMonoSucResult(jsonObject);
    }

}
