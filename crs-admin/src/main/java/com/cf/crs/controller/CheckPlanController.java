package com.cf.crs.controller;

import com.cf.crs.entity.CheckPlan;
import com.cf.crs.service.CheckPlanService;
import com.cf.util.http.ResultJson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 考评计划
 * @author frank
 * 2019/10/18
 **/
@Api(tags="城管项目")
@RequestMapping("/city/checkPlan")
@RestController
public class CheckPlanController {

    @Autowired
    CheckPlanService checkPlanService;


    @ApiOperation("获取考评计划")
    @PostMapping("/getCheckPlan")
    public ResultJson<List<CheckPlan>> getCheckPlan(){
        return checkPlanService.getCheckPlan();
    }

    @ApiOperation("保存考评计划")
    @PostMapping("/updateCheckPlan")
    public ResultJson<String> updateCheckPlan(String list){
        return checkPlanService.updateCheckPlan(list);
    }
}
