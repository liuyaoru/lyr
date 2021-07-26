package com.cf.crs.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cf.crs.common.redis.RedisUtils;
import com.cf.crs.config.config.HeiBeiClientConfig;
import com.cf.crs.entity.HeiBeiSmartUser;
import com.cf.crs.mapper.HeiBeiSmartUserMapper;
import com.cf.util.http.ResultJson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@Transactional
public class HeiBeiSmartUserSynService {


    @Autowired
    private HeiBeiSmartUserMapper heiBeiSmartUserMapper;


    @Autowired
    private HeBeiNetManagerServerUserService heBeiNetManagerServerAddUserService;



    /**
     * 也是同步更新但是没啥用
     * @param heiBeiSmartUser
     */
    public void update(HeiBeiSmartUser heiBeiSmartUser)
    {


        heiBeiSmartUserMapper.update(heiBeiSmartUser,new QueryWrapper<HeiBeiSmartUser>().eq("userId", heiBeiSmartUser.getUserId()));
        heBeiNetManagerServerAddUserService.NetManagerServerUpdateUser(heiBeiSmartUser);

    }


    public void delete(long id)
    {
       HeiBeiSmartUser user=heiBeiSmartUserMapper.selectOne(new QueryWrapper<HeiBeiSmartUser>().eq("userId", id));
     heBeiNetManagerServerAddUserService.NetManagerServerDeleteUser(user);
     heiBeiSmartUserMapper.delete(new QueryWrapper<HeiBeiSmartUser>().eq("userId", id));

    }

    public void insert(HeiBeiSmartUser heiBeiSmartUser)
    {
        heiBeiSmartUserMapper.insert(heiBeiSmartUser);
     JSONObject jsObject=heBeiNetManagerServerAddUserService.NetManagerServerAddUser(heiBeiSmartUser);
    }


}
