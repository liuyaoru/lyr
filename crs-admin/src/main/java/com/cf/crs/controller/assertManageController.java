package com.cf.crs.controller;


import com.cf.crs.entity.User;
import com.cf.crs.service.UserService;
import com.cf.util.http.HttpWebResult;
import com.cf.util.http.ResultJson;
import com.cf.util.utils.CacheKey;
import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/newsystem/liuyaoru/auth")
public class assertManageController {

    @Autowired
    private UserService userService;

/*    @GetMapping("/selectUser")
    public ResultJson  addUser()
    {
        return userService.selectUser();

    }*/
    @PostMapping("selectUser")
    public ResultJson loginUser(String userName,String passWord)
    {

       User user= userService.selectUser();
       try {
           if (user.getUserName()!=null&&!user.getUserName().equals("")) {
               {
                   if( user.getUserName().equals(userName))
                   {
                       if(user.getPassWord()!=null&&!user.getPassWord().equals(""))
                       {
                           if(user.getPassWord().equals(passWord))
                           {
                               String token = CacheKey.USER_TOKEN + ":" + System.currentTimeMillis()+ user.getUserName();
                               return  HttpWebResult.getMonoSucResult(token);
                           }

                   }

                   }

               }
               return  HttpWebResult.getMonoError("用户名或者密码错误");
           }
       }catch (Exception e)
       {
           HttpWebResult.getMonoError("登录失败");
       }
       return HttpWebResult.getMonoError("没有这个用户");

    }


    @PostMapping("/getUser")
    public ResultJson getAllUser()
    {

        List<User> user= userService.getAllUser();
        return  HttpWebResult.getMonoSucResult(user);
    }

    @PostMapping("/addUser")
    public ResultJson addUser(User user)
    {
      Date date=new Date();
        String strDateFormat = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat(strDateFormat);
       user.setDate(simpleDateFormat.format(date));
        try {
            userService.insertUser(user);
        }catch (Exception e)
        {
            return  HttpWebResult.getMonoError("用户名已经存在");
        }

        return  HttpWebResult.getMonoSucStr();
    }

    @PostMapping("/delUser")
    public ResultJson delUser(String userName)
    {

        try {
            userService.delUserByUserName(userName);
        }catch (Exception e)
        {
            return  HttpWebResult.getMonoError("用户名删除失败");
        }

        return  HttpWebResult.getMonoSucStr();
    }

    @PostMapping("/alterUser")
    public ResultJson alterUser(String userName,String nickName)
    {

        try {
            userService.alterUserById(userName,nickName);
        }catch (Exception e)
        {
            return  HttpWebResult.getMonoError("用户名更新失败");
        }

        return  HttpWebResult.getMonoSucStr();
    }


}
