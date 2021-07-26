package com.cf.crs.service;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cf.crs.entity.CityOrganization;
import com.cf.crs.entity.TreeSelect;
import com.cf.crs.mapper.CityOrganizationMapper;
import com.cf.util.http.HttpWebResult;
import com.cf.util.http.ResultJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CityOrganizationService {

    @Autowired
    CityOrganizationMapper cityOrganizationMapper;

    /**
     * 获取所有部门
     * @return
     */
    public ResultJson<List<CityOrganization>> getOrganizationList(){
        return HttpWebResult.getMonoSucResult(cityOrganizationMapper.selectList(new QueryWrapper<CityOrganization>()));
    }

    public List<CityOrganization> getAllOrganizationList()
    {
        return cityOrganizationMapper.selectList(new QueryWrapper<CityOrganization>());
    }

    /**获取所有的父部门
     *
     * @return
     */
    public List<CityOrganization> getParentOrganizationList()
    {
       return  cityOrganizationMapper.selectList(new QueryWrapper<CityOrganization>().eq("parent",0));
    }
/**
 *
 * 根据父部门获取所有的子部门
 */
    public ResultJson<List<CityOrganization>> getSonOrganizationListByParent(Integer  parentCode)

    {

        List<CityOrganization> cityOrganizations=cityOrganizationMapper.selectList(new QueryWrapper<CityOrganization>().eq("parent",parentCode));

      return  HttpWebResult.getMonoSucResult(cityOrganizations);
        }



    /**
     * 设置角色
     * @param id
     * @param auth
     * @return
     */
    public ResultJson<String> setRole(Integer id, String auth){
        return HttpWebResult.getMonoSucResult(cityOrganizationMapper.update(null, new UpdateWrapper<CityOrganization>().eq("id", id).set("auth", auth)));
    }

    /**
     * 得到子节点列表
     */
    private List<CityOrganization> getChildList(List<CityOrganization> list, CityOrganization t)
    {
        List<CityOrganization> tlist = new ArrayList<CityOrganization>();
        Iterator<CityOrganization> it = list.iterator();
        while (it.hasNext())
        {
            CityOrganization n  = (CityOrganization) it.next();
             System.out.println( n.getParent().longValue());
             System.out.println(t.getCode().longValue());
            if (StringUtils.isEmpty(String.valueOf(n.getParent())) && n.getParent().longValue() == t.getCode().longValue())
            {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 递归列表
     */
    private void recursionFn(List<CityOrganization> list, CityOrganization t)
    {
        // 得到子节点列表
        List<CityOrganization> childList = getChildList(list, t);
        t.setChildren(childList);
        for (CityOrganization tChild : childList)
        {
            if (hasChild(list, tChild))
            {
                recursionFn(list, tChild);
            }
        }
    }
    //建立子节点
    public List<TreeSelect> buildDeptTreeSelect(List<CityOrganization> depts)
    {
        List<CityOrganization> deptTrees = buildDeptTree(depts);
        return deptTrees.stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<CityOrganization> list, CityOrganization t)
    {
        return getChildList(list, t).size() > 0 ? true : false;
    }
    /**
     * 构建前端所需要树结构
     *
     * @param depts 部门列表
     * @return 树结构列表
     */
    public List<CityOrganization> buildDeptTree(List<CityOrganization> depts)
    {
        List<CityOrganization> returnList = new ArrayList<CityOrganization>();
        List<Integer> tempList = new ArrayList<Integer>();
        for (CityOrganization dept : depts)
        {
            tempList.add(dept.getCode());
        }
        for (Iterator<CityOrganization> iterator = depts.iterator(); iterator.hasNext();)
        {
           CityOrganization dept = (CityOrganization) iterator.next();
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(dept.getParent()))
            {
                recursionFn(depts, dept);
                returnList.add(dept);
            }
        }
        if (returnList.isEmpty())
        {
            returnList = depts;
        }
        return returnList;
    }

}
