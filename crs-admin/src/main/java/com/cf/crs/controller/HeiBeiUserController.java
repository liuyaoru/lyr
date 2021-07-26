package com.cf.crs.controller;


import com.cf.crs.common.annotation.LogOperation;
import com.cf.crs.entity.HeiBeiSmartUser;
import com.cf.crs.service.HeiBeiSmartUserSynService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Api(tags="河北智慧园区项目")
@Slf4j
@RestController
@RequestMapping("/api/hebei/uniauth")
public class HeiBeiUserController {

    @Autowired
  private HeiBeiSmartUserSynService smartUserService;



    public  String getCode(HttpServletRequest servletRequest)
    {
        String code= servletRequest.getParameter("code");
        return  code;
    }

    @PostMapping("/add")
    @ApiOperation("统一认证对接接口增加操作")
    @LogOperation(value = "统一认证插入用户")
    public String addUser(HeiBeiSmartUser smartUser, HttpServletRequest servletRequest)
    {
        String code=getCode(servletRequest);

        if(code==null||code.equals(""))
        {
            return  null;

        }
        else{
            int codeInt=Integer.valueOf(code);
            if(codeInt==11)
            {
                smartUserService.insert(smartUser);

            }else{
                return  null;
            }
        }
        return  null;
    }
    @PostMapping("/delete")
    @ApiOperation("统一认证对接接口删除操作")
    @LogOperation(value = "统一认证删除用户")
    public String deleteUser(HeiBeiSmartUser smartUser, HttpServletRequest servletRequest)
    {


        String code=getCode(servletRequest);
        if(code==null||code.equals(""))
        {
            return  null;

        }
        else{
            int codeInt=Integer.valueOf(code);
            if(codeInt==1)
            {
                smartUserService.delete(smartUser.getUserId());
            }else{
                return  null;
            }

        }
        return  null;
    }
    @PostMapping("/update")
    @ApiOperation("统一认证对接接口更新操作")
    @LogOperation(value = "统一认证更新用户信息")
    public String updateUser(HeiBeiSmartUser smartUser, HttpServletRequest servletRequest)
    {
        String code=getCode(servletRequest);
        if(code==null||code.equals(""))
        {
            return  null;

        }
        else{
            int codeInt=Integer.valueOf(code);
            if(codeInt==12)
            {
                smartUserService.update(smartUser);

            }else{
                return  null;
            }
        }
     return  null;
    }


}
