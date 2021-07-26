package com.cf.crs.controller;

import com.cf.crs.common.annotation.LogOperation;
import com.cf.crs.entity.CityRole;
import com.cf.crs.service.HeBeiGetAllUserByClientToken;
import com.cf.crs.service.HeBeiSmartRoleService;
import com.cf.crs.service.HeiBeiClientLoginService;
import com.cf.crs.service.HeiBeiSmartUserSynService;
import com.cf.util.http.HttpWebResult;
import com.cf.util.http.ResultJson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/city/roleSyn")
public class HeBeiSmartRoleSynController {

    @Autowired
    private HeBeiGetAllUserByClientToken userByClientToken;

    @Autowired
    private HeBeiSmartRoleService smartRoleService;

      @RequestMapping("/insertRole")
        public ResultJson insertRole(CityRole cityRole)
        {

      return  smartRoleService.insertAndSysRole(cityRole);
        }
    @RequestMapping("/updateRole")
    public ResultJson updateRole(CityRole cityRole)
    {

        return  smartRoleService.updateAndSysRole(1,null);
    }

    @RequestMapping("/deleteRole")
    public ResultJson deleteRole(CityRole cityRole)
    {
        return  smartRoleService.deleteAndSysRole(1);
    }


    @RequestMapping("/test")
    public ResultJson test()
    {
        userByClientToken.updateAllUserRole();
        return  null;

    }
    @PostMapping("/insertRoleSys")
    public ResultJson roleSys()
    {
        smartRoleService.insertSysRole();
        return HttpWebResult.getMonoSucStr();
    }


}
