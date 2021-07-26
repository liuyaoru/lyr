package com.cf.crs.mapper;


import com.cf.crs.common.dao.BaseDao;
import com.cf.crs.entity.Role;
import com.cf.crs.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RoleMapper extends BaseDao<Role> {
}
