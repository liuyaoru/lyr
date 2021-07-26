package com.cf.crs.controller;


import com.cf.crs.entity.HeBeiSmartSystemConfig;
import com.cf.crs.service.HeBeiSmartSystemConfigService;
import com.cf.util.http.ResultJson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/city/config")
/*@RequestMapping("/api/hebei/uniauth")*/
public class HeBeiSmartSystemConfigController {

    @Autowired
    private HeBeiSmartSystemConfigService service;

    @PostMapping("/insetAllConfig")
    public ResultJson insertAllSystemConfig(HeBeiSmartSystemConfig smartSystemConfig)
    {

        return service.insertAllSystemConfig(smartSystemConfig);

    }
    @PostMapping("/updateSystemConfig")
    public ResultJson updateAllSystemConfigBySerName(HeBeiSmartSystemConfig smartSystemConfig)
    {

        return  service.updateAllSystemConfigBySerName(smartSystemConfig);

    }
    @PostMapping ("/selectSystemConfigList")
    public ResultJson selectSystemConfigList()
    {
        return  service.selectSystemConfigList();
    }
}
