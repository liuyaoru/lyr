package com.cf.crs.controller;

import com.cf.crs.entity.CityOrganization;
import com.cf.crs.entity.TreeSelect;
import com.cf.crs.service.CityOrganizationService;
import com.cf.util.http.HttpWebResult;
import com.cf.util.http.ResultJson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags="城管项目")
@RequestMapping("/city/organization")
@RestController
public class CityOrganizationController {

    @Autowired
    CityOrganizationService organizationService;


    @ApiOperation("获取所有部门")
    @PostMapping("/getOrganization")
    public ResultJson<List<CityOrganization>> getOrganizationList(){
        return organizationService.getOrganizationList();
    }

     @ApiOperation("通过所有的父部门")
     @PostMapping("/getParentOrganization")
     public ResultJson<List<CityOrganization>> getOrganizationParentList()
     {
         return  HttpWebResult.getMonoSucResult(organizationService.getParentOrganizationList());
     }
     @ApiOperation("通过父部门获取子部门")
     @PostMapping("/getSonOrganization")
     public ResultJson<List<CityOrganization>>getOrganizationSonListByParentCode(Integer parentCode)
     {
         return  organizationService.getSonOrganizationListByParent(parentCode);
     }

    @ApiOperation("设置部门角色")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "id", value = "部门id", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType="query", name = "auth", value = "用户角色 0:无权限，1:管理员 2:普通权限(角色id,多个角色id以逗号隔开)", required = true, dataType = "String")
    })
    @PostMapping("/setRole")
    public ResultJson<String> setRole(Integer id, String auth){
        return organizationService.setRole(id,auth);
    }

    /**
     * 获取部门下拉树列表
     */
    @PostMapping("/treeselect")
    public ResultJson<List<TreeSelect>> treeselect()
    {
        List<CityOrganization> organizations = organizationService.getAllOrganizationList();
        return HttpWebResult.getMonoSucResult(organizationService.buildDeptTreeSelect(organizations));
    }



}
