package com.cf.crs.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cf.crs.entity.HeBeiSmartSystemConfig;
import com.cf.crs.mapper.HeBeiSmartConfigMapper;
import com.cf.util.http.HttpWebResult;
import com.cf.util.http.ResultJson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
public class HeBeiSmartSystemConfigService {

    @Autowired
    private HeBeiSmartConfigMapper configMapper;

    public ResultJson insertAllSystemConfig(HeBeiSmartSystemConfig smartSystemConfig)
    {
        try {
            configMapper.insert(smartSystemConfig);
        }catch (Exception e)
        {
            return HttpWebResult.getMonoError("插入失败");
        }
        return HttpWebResult.getMonoSucStr();
    }

    public ResultJson updateAllSystemConfigBySerName(HeBeiSmartSystemConfig smartSystemConfig)
    {

      return HttpWebResult.getMonoSucResult(configMapper.update(null,new UpdateWrapper<HeBeiSmartSystemConfig>().eq("MAIL_SERVER_NAME",smartSystemConfig.getMAIL_SERVER_NAME())
              .set("MAIL_PORT",smartSystemConfig.getMAIL_PORT())
              .set("MAIL_TIMEOUT",smartSystemConfig.getMAIL_TIMEOUT())
                      .set("SEND_EMAIL",smartSystemConfig.getSEND_EMAIL())
                      .set("RECEIVE_EMAIL",smartSystemConfig.getRECEIVE_EMAIL())
                      .set("MAIL_USERNAME",smartSystemConfig.getMAIL_USERNAME())
                      .set("MAIL_PASSWORD",smartSystemConfig.getMAIL_PASSWORD())
                      .set("MAIL_SECURITY",smartSystemConfig.getMAIL_SECURITY())
                       .set("IFADDREADYMAIL",smartSystemConfig.getIFADDREADYMAIL())



              )




      );



    }
    public ResultJson selectSystemConfigList()
    {
     return    HttpWebResult.getMonoSucResult( configMapper.selectList(new QueryWrapper<HeBeiSmartSystemConfig>()));

    }

}
