package com.cf.crs.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cf.crs.entity.User;
import com.cf.crs.mapper.userMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

@Service
@Slf4j
public class UserService {
    @Autowired
    private userMapper useMapper;

    public User selectUser() {
        List<User> ser = useMapper.selectList(new QueryWrapper<User>());
        Iterator iterator = ser.iterator();
        User user1 = null;
        while (iterator.hasNext()) {
            user1 = (User) iterator.next();
            break;
        }


        return user1;
    }

  public void  delUserByUserName(String userName) throws Exception {
      if(useMapper.deleteUserByUserName(userName)>0)
      {

      }else {
          throw  new Exception();
      }
  }
    public List<User> getAllUser() {
        List<User> ser = useMapper.selectList(new QueryWrapper<User>());

        return ser;
    }

    public  void insertUser (User user) throws Exception {

            if(useMapper.checkUserName(user.getUserName())>0)
            {
                throw  new Exception();
            }else
            {
                useMapper.insert(user);
            }
    }

    public void alterUserById(String userName,String nickName)
    {

        useMapper.selectIdByUserName(userName,nickName);
    }

}
