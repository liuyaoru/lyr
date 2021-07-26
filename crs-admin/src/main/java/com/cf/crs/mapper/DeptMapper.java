package com.cf.crs.mapper;


import com.cf.crs.common.dao.BaseDao;
import com.cf.crs.entity.Dept;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DeptMapper extends BaseDao<Dept> {

    public List<Dept> selectDeptList();
}

