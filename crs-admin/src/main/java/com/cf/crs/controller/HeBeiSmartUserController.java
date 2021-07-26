package com.cf.crs.controller;

import com.cf.crs.common.annotation.LogOperation;
import com.cf.crs.entity.HeiBeiSmartUser;
import com.cf.crs.service.HeBeiGetAllUserByClientToken;
import com.cf.crs.service.HeBeiSmartUserListService;
import com.cf.util.http.HttpWebResult;
import com.cf.util.http.ResultJson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "河北石家庄项目")
@RequestMapping("/city/user")
@RestController
public class HeBeiSmartUserController {
    @Autowired
    private HeBeiGetAllUserByClientToken getAllUserByClientToken;

    @Autowired
    private HeBeiSmartUserListService userListService;

    @ApiOperation("获取所有用户")
    @PostMapping("/getHeBeiUserList")
    public ResultJson<List<HeiBeiSmartUser>> GetAllUserList() {
     return  userListService.smartUserList();

    }
    //获取统一认证所有用户
    @PostMapping("/getAlluser")
    @ApiOperation("获取统一认证所有用户")
    @LogOperation("获取统一认证所有用户信息")
    public ResultJson getAll()
    {

        return getAllUserByClientToken.ByPageConfigGetAllData();

    }

    @ApiOperation("通过id删除用户")
    @PostMapping("/delete")
    public ResultJson deleteMySystemUserById(Long id)
    {
        return  userListService.deleteMySystemUserById(id);

    }
   @ApiOperation("设置三个系统用户的权限")
    @PostMapping("/setHeBeiAuth")
    public Object setRole(Integer id,String ItoPrivilege,String NetPrivilege,String ItsPrivilege){
        return userListService.setRole(id,ItoPrivilege,NetPrivilege,ItsPrivilege);
    }


}
