package com.cf.crs.controller;


import com.cf.crs.entity.Role;
import com.cf.crs.service.RoleService;
import com.cf.util.http.HttpWebResult;
import com.cf.util.http.ResultJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/newsystem/liuyaoru/auth")
public class RoleController {
    @Autowired
    private RoleService roleService;


    @RequestMapping("/selectRole")
    public ResultJson selectRole()
    {

      return HttpWebResult.getMonoSucResult(roleService.selectRoleList());
    }


  public ResultJson InsertRole(Role role)
  {

      try
      {
          roleService.insertRole(role);
      }catch (Exception e)
      {
          return  HttpWebResult.getMonoError("插入失败");

      }


      return  HttpWebResult.getMonoSucStr();
  }


    public ResultJson addRole(Role role)
    {

        try {
            roleService.addRole(role);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return HttpWebResult.getMonoSucStr();
    }

}
