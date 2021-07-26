package com.cf.crs.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cf.crs.entity.Role;
import com.cf.crs.mapper.RoleMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Service
@Slf4j
public class RoleService {
    @Autowired
    private RoleMapper roleMapper;



    public List<Role> selectRoleList() {
        List<Role> roles = roleMapper.selectList(new QueryWrapper<>());
     /*   Iterator iterator = roles.iterator();
        while (iterator.hasNext()) {
            Role role = (Role) iterator.next();
            Date create_date = role.getCreate_date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = simpleDateFormat.format(create_date);
        }
*/

        return roles;
    }
  public void insertRole(Role role)
  {
      if(role!=null)
      {

      }
  }
    public  void  addRole(Role role)
    {
        roleMapper.insert(role);

    }
}
