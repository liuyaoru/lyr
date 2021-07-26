package com.cf.crs.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cf.crs.common.redis.RedisUtils;
import com.cf.crs.entity.HeiBeiSmartUser;
import com.cf.crs.mapper.HeiBeiSmartUserMapper;
import com.cf.util.http.HttpWebResult;
import com.cf.util.http.ResultJson;
import com.cf.util.utils.CacheKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class HeBeiSmartLoginOutService {



    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private HeiBeiSmartUserMapper heiBeiSmartUserMapper;

/***
 * 用户退出删除redis中的token
 */
   public ResultJson loginOut(Integer userId)
   {

       HeiBeiSmartUser heiBeiSmartUser=heiBeiSmartUserMapper.selectOne(new QueryWrapper<HeiBeiSmartUser>().eq("userId",userId));

       if(heiBeiSmartUser!=null)
       {
           redisUtils.delete(CacheKey.USER_NAME_TOKEN + ":" + heiBeiSmartUser.getUserName());

       }
      return HttpWebResult.getMonoSucStr();

   }

}
