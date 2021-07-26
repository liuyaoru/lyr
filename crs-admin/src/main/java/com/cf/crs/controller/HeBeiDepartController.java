package com.cf.crs.controller;

import com.cf.crs.entity.HeBeiSmartOrganization;
import com.cf.crs.service.HeBeiDepartSysService;
import com.cf.util.http.HttpWebResult;
import com.cf.util.http.ResultJson;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
//@RequestMapping("/api/hebei/uniauth")
@RequestMapping("/city/depart")
@Slf4j
@Api(tags = "河北项目")
public class HeBeiDepartController {


    @Autowired
    private HeBeiDepartSysService sysService;

    @PostMapping("/UniFieDauThenTiCation")
    public ResultJson getUniFieDauThenTiCation()
    {

       return sysService.getAllHeBeiOrganizationData();
    }


    @RequestMapping("/getOrganization")
    public ResultJson<List<HeBeiSmartOrganization>> findAllOrganizationByClient()
    {
       return   HttpWebResult.getMonoSucResult(sysService.buildDeptTree());
        // sysService.getData();

    }




}
