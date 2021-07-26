package com.cf.crs.controller;

import com.cf.crs.service.HeBeiSmartLoginOutService;
import com.cf.util.http.HttpWebResult;
import com.cf.util.http.ResultJson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/idaas/auth/oauth2")
public class HeiBeiSmartLogoutController {

    @Autowired
    private HeBeiSmartLoginOutService smartLoginOutService;

    @PostMapping("/logout")
    public ResultJson<Object> logout(Integer userId)
    {

        return smartLoginOutService.loginOut(userId);

    }
}
