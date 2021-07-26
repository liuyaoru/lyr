package com.cf.crs.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cf.crs.entity.CityOrganization;
import com.cf.crs.entity.CityUser;
import com.cf.crs.entity.HeiBeiSmartUser;
import com.cf.crs.mapper.HeiBeiSmartUserMapper;
import com.cf.util.http.HttpWebResult;
import com.cf.util.http.ResultJson;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class HeBeiSmartUserListService {
    @Autowired
    private HeiBeiSmartUserMapper smartUserMapper;

    @Autowired
    private CityOrganizationService organizationService;
//获取所有的smartUser

    public ResultJson<List<HeiBeiSmartUser>> smartUserList() {

        try {
            List<HeiBeiSmartUser> heiBeiSmartUser = smartUserMapper.selectList(new QueryWrapper<HeiBeiSmartUser>());
            return HttpWebResult.getMonoSucResult(heiBeiSmartUser);
        } catch (Exception e) {
            return HttpWebResult.getMonoError("查询失败");

        }


    }
//通过id删除用户
    public ResultJson deleteMySystemUserById(Long id) {
        try {
            Map userById = new HashMap();
            userById.put("userId", id);
            smartUserMapper.deleteByMap(userById);
            return HttpWebResult.getMonoSucStr();
        } catch (Exception e) {
            return HttpWebResult.getMonoError("用户删除失败");
        }
    }
//设置三个系统的用户的权限
    public Object setRole(Integer id, String ItoPrivilege,String NetPrivilege,String ItsPrivilege) {
        if (id == null || ItoPrivilege == null) return HttpWebResult.getMonoError("参数不能为空");
       smartUserMapper.update(null, new UpdateWrapper<HeiBeiSmartUser>().set("NetPrivilege", NetPrivilege).eq("id", id));
        smartUserMapper.update(null, new UpdateWrapper<HeiBeiSmartUser>().set("ItoPrivilege", ItoPrivilege).eq("id", id));
        smartUserMapper.update(null, new UpdateWrapper<HeiBeiSmartUser>().set("ItsPrivilege", ItsPrivilege).eq("id", id));
        return HttpWebResult.getMonoSucStr();
    }
}

