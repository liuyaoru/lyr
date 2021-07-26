package com.cf.crs.controller;


import com.cf.crs.service.DepartService;
import com.cf.util.http.HttpWebResult;
import com.cf.util.http.ResultJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/newsystem/liuyaoru/auth")
public class DeptController {

    @Autowired
    private DepartService departService;

    @GetMapping("/treeselect")
    public ResultJson getAllDeptList()
    {

      return   HttpWebResult.getMonoSucResult(departService.buildDeptTreeSelect(departService.getListDept()));

    }

}
