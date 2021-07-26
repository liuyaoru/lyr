package com.cf.crs.service;

import com.cf.crs.entity.Dept;
import com.cf.crs.entity.TreeSelect;
import com.cf.crs.mapper.DeptMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
public class DepartService {

    @Autowired
    private DeptMapper deptMapper;

    public List<Dept> getListDept()
    {

        return deptMapper.selectDeptList();
    }
    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<Dept> list, Dept t)
    {
        return getChildList(list, t).size() > 0 ? true : false;
    }
    /**
     * 得到子节点列表
     */
    private List<Dept> getChildList(List<Dept> list, Dept t)
    {
        List<Dept> tlist = new ArrayList<Dept>();
        Iterator<Dept> it = list.iterator();
        while (it.hasNext())
        {
            Dept n = (Dept) it.next();

            if((Object) n.getParent_id()!=null&&n.getParent_id()==t.getDept_id())
            {
                tlist.add(n);
            }

        }
        return tlist;
    }

    /**
     * 递归列表
     */
    private void recursionFn(List<Dept> list, Dept t)
    {
        // 得到子节点列表
        List<Dept> childList = getChildList(list, t);
        t.setChildren(childList);
        for (Dept tChild : childList)
        {
            if (hasChild(list, tChild))
            {
                recursionFn(list, tChild);
            }
        }
    }
    /**
     * 构建前端所需要树结构
     *
     * @param depts 部门列表
     * @return 树结构列表
     */
    public List<Dept> buildDeptTree(List<Dept> depts)
    {
        List<Dept> returnList = new ArrayList<Dept>();
        List<Long> tempList = new ArrayList<Long>();
        for (Dept dept : depts)
        {
            tempList.add(dept.getDept_id());
        }
        for (Iterator<Dept> iterator = depts.iterator(); iterator.hasNext();)
        {
            Dept dept = (Dept) iterator.next();
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(dept.getParent_id()))
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


    /**
     * 构建前端所需要下拉树结构
     *
     * @param depts 部门列表
     * @return 下拉树结构列表
     */
    public List<Dept> buildDeptTreeSelect(List<Dept> depts)
    {

        List<Dept> deptTrees = buildDeptTree(depts);



        return deptTrees;
    }

}
