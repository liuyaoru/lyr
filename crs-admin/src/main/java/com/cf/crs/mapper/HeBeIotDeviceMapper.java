package com.cf.crs.mapper;

import com.cf.crs.common.dao.BaseDao;
import com.cf.crs.entity.HeBeIotDevice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface HeBeIotDeviceMapper extends BaseDao<HeBeIotDevice> {
   public  List<String> getDeviceTypeByGroup();
   public Integer selectCountNumberByDeviceType(@Param("deviceTyp")String deviceTyp, @Param("STATUS")String STATUS);
}
