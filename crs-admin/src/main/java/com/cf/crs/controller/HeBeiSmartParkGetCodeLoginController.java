package com.cf.crs.controller;

import com.cf.crs.service.HeiBeiClientLoginService;
import com.cf.util.http.ResultJson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Api(tags = "河北智慧园区项目")
@RestController
@Slf4j
@RequestMapping("/city/user")
public class  HeBeiSmartParkGetCodeLoginController {

    @Autowired
private HeiBeiClientLoginService heiBeiclientLoginService;

    @PostMapping("/login")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "userName", value = "用户名", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "password", value = "密码", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "code", value = "第三方登录code(如果此参数存在，将认为是第三方登录，可以不传其他参数)", required = false, dataType = "String")
    })
    public ResultJson<Object> login(String userName, String password, String code){
    return  heiBeiclientLoginService.login(userName,password,code);
    }


    @ApiOperation("退出登录")
    @PostMapping("/logout")
    @ApiImplicitParam(paramType="query", name = "userName", value = "用户名", required = true, dataType = "String")
    public ResultJson<Object> login(String userName){
        return heiBeiclientLoginService.logout(userName);
    }


}
