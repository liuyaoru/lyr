package com.cf.crs.mapper;

import com.cf.crs.common.dao.BaseDao;
import com.cf.crs.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface userMapper extends BaseDao<User> {
    public int checkUserName(String userName);
    public int deleteUserByUserName(String userName);

    public int selectIdByUserName(String userName,String nickName);
    public List<User> findAllUserAndDePart();
}
