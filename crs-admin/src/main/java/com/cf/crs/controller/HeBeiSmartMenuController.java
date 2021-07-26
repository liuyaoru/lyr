package com.cf.crs.controller;


import com.cf.crs.entity.HeBeiSmartMenu;
import com.cf.crs.entity.HeBeiSmartOrganization;
import com.cf.crs.service.HeBeiSmartMenuService;
import com.cf.util.http.HttpWebResult;
import com.cf.util.http.ResultJson;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/city/menu")
public class HeBeiSmartMenuController {

    @Autowired
    private HeBeiSmartMenuService smartMenuService;

    @ApiOperation("获取菜单列表")
    @PostMapping("/getMenuList")
    public ResultJson<List<HeBeiSmartMenu>> getSmartMenu()
    {

        return   HttpWebResult.getMonoSucResult(smartMenuService.buildTreeMeNu());

    }
}
