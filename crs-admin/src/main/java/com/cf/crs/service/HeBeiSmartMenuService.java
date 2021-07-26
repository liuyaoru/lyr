package com.cf.crs.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cf.crs.entity.HeBeiSmartMenu;
import com.cf.crs.entity.HeBeiSmartOrganization;
import com.cf.crs.mapper.HeBeiSmartMenuMapper;
import com.cf.crs.mapper.HeiBeiSmartUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
@Service
@Transactional
public class HeBeiSmartMenuService {

    @Autowired
    private HeBeiSmartMenuMapper heBeiSmartMenuMapper;



    //获取河北智慧园区所有菜单
    public List<HeBeiSmartMenu> getAllMenuByMyDateSource()
    {
        List<HeBeiSmartMenu> smartMenuList = heBeiSmartMenuMapper.selectList(new QueryWrapper<HeBeiSmartMenu>());//查询当前

        return  smartMenuList;

    }

    public List<HeBeiSmartMenu> buildTreeMeNu() {
        List<HeBeiSmartMenu> smartMenuList=getAllMenuByMyDateSource();
        List<HeBeiSmartMenu> returnList = new ArrayList<HeBeiSmartMenu>();
        List tempList = new ArrayList();
        for (HeBeiSmartMenu smartMenu : smartMenuList)
        {
            tempList.add(smartMenu.getId());
        }
        for(Iterator iterator=smartMenuList.iterator();iterator.hasNext();)
        {
            HeBeiSmartMenu smartMenu = (HeBeiSmartMenu) iterator.next();
                if (Integer.valueOf(smartMenu.getParentId())==0)
            {
                recursionFn(smartMenuList, smartMenu);
                returnList.add(smartMenu);

            }
        }

        return  returnList;

    }
    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<HeBeiSmartMenu> list, HeBeiSmartMenu t)
    {
        return getChildList(list, t).size() > 0 ? true : false;
    }

    private void recursionFn(List<HeBeiSmartMenu> smartMenuList, HeBeiSmartMenu smartMenu) {
        // 得到子节点列表
        List<HeBeiSmartMenu> childList = getChildList(smartMenuList, smartMenu);
        smartMenu.setChildren(childList);
        for (HeBeiSmartMenu tChild : childList)
        {
            if (hasChild(childList, tChild))
            {
                recursionFn(childList, tChild);
            }
        }

    }

    private List<HeBeiSmartMenu> getChildList(List<HeBeiSmartMenu> smartMenuList, HeBeiSmartMenu smartMenu) {

        List<HeBeiSmartMenu> tlist = new ArrayList<HeBeiSmartMenu>();
        Iterator<HeBeiSmartMenu> it = smartMenuList.iterator();
        while (it.hasNext())
        {
            HeBeiSmartMenu n  = (HeBeiSmartMenu) it.next();
            if(Long.parseLong(n.getParentId())==smartMenu.getId())
            {
                tlist.add(n);
            }

        }
        return tlist;
    }
}
