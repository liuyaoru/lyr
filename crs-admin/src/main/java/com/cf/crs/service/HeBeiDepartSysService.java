package com.cf.crs.service;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cf.crs.config.config.HeiBeiClientConfig;
import com.cf.crs.entity.CityOrganization;
import com.cf.crs.entity.HeBeiSmartOrganization;
import com.cf.crs.mapper.HeBeiDepartMapper;
import com.cf.util.http.HttpWebResult;
import com.cf.util.http.ResultJson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Slf4j
@Service
@Transactional
public class HeBeiDepartSysService {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private HeiBeiClientConfig heiBeiClientConfig;
    @Autowired
    private HeBeiGetAllUserByClientToken  clientToken;
    @Autowired
    private HeBeiDepartMapper departMapper;
//获取河北智慧园区所有部门
    public List<HeBeiSmartOrganization> getAllDepartByMyDateSource()
    {


        List<HeBeiSmartOrganization> smartOrganizationList = departMapper.selectList(new QueryWrapper<HeBeiSmartOrganization>());//查询当前
        return  smartOrganizationList;

    }

    /**
     * 得到子节点列表
     */
    private List<HeBeiSmartOrganization> getChildList(List<HeBeiSmartOrganization> list, HeBeiSmartOrganization t)
    {
        List<HeBeiSmartOrganization> tlist = new ArrayList<HeBeiSmartOrganization>();
        Iterator<HeBeiSmartOrganization> it = list.iterator();
        while (it.hasNext())
        {
          HeBeiSmartOrganization n  = (HeBeiSmartOrganization) it.next();
          if(n.getParentId()==t.getOrgId())
          {
              tlist.add(n);
          }

        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<HeBeiSmartOrganization> list, HeBeiSmartOrganization t)
    {
        return getChildList(list, t).size() > 0 ? true : false;
    }

    /**
     * 递归列表
     */
    private void recursionFn(List<HeBeiSmartOrganization> list, HeBeiSmartOrganization t)
    {
        // 得到子节点列表
        List<HeBeiSmartOrganization> childList = getChildList(list, t);
        t.setChildren(childList);
        for (HeBeiSmartOrganization tChild : childList)
        {
            if (hasChild(list, tChild))
            {
                recursionFn(list, tChild);
            }
        }
    }
    //获取所有的根部门
    public List<HeBeiSmartOrganization> buildDeptTree()
    {
        List<HeBeiSmartOrganization> smartOrganizationList=getAllDepartByMyDateSource();
        List<HeBeiSmartOrganization> returnList = new ArrayList<HeBeiSmartOrganization>();
        List tempList = new ArrayList();
        for (HeBeiSmartOrganization dept : smartOrganizationList)
        {
            tempList.add(dept.getOrgId());
        }
        for (Iterator<HeBeiSmartOrganization> iterator =smartOrganizationList.iterator(); iterator.hasNext();)
        {
            HeBeiSmartOrganization dept = (HeBeiSmartOrganization) iterator.next();
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(dept.getParentId()))
            {

               recursionFn(smartOrganizationList, dept);
                returnList.add(dept);

            }

        }
    /*    if (returnList.isEmpty())
        {
            returnList = smartOrganizationList;
        }*/
        return returnList;
    }

    public JSONObject getHeBeiCityOrganization()
    {
        String access_token=clientToken.GeToken();
        String url=heiBeiClientConfig.getUrl();
        url=url+"/idaas/manage/open-api/pmi/org/tree";
        HttpHeaders headers=new HttpHeaders();
        headers.add("Authorization",access_token);
        ResponseEntity response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<String>(headers),JSONObject.class);
      JSONObject jsonObject=(JSONObject)response.getBody();
       log.info("HeBeiDepartSysService",jsonObject);
       return jsonObject;

        }

        //从统一认证获取所有的部门并插入数据库
        public ResultJson getAllHeBeiOrganizationData()
        {
            try {
                JSONObject jsonObject = getHeBeiCityOrganization();
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                InsertJsonArray(jsonArray);
            }catch (Exception e)
            {
                return  HttpWebResult.getMonoError("获取统一认证失败");
            }
            return HttpWebResult.getMonoSucStr();
        }

        public void InsertJsonArray(JSONArray jsonArray)
        {
            HeBeiSmartOrganization smartOrganization = new HeBeiSmartOrganization();
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                InsertEveryone(json,smartOrganization);
            }
        }

    public  void InsertEveryone(JSONObject jsonObject,HeBeiSmartOrganization smartOrganization )
    {


        smartOrganization.setOrgId(Integer.valueOf(jsonObject.getString("orgId")).longValue());
        smartOrganization.setParentId(Integer.valueOf(jsonObject.getString("parentId")).longValue());
        smartOrganization.setOrgName(jsonObject.getString("orgName"));
        smartOrganization.setOrgCode(jsonObject.getString("orgCode"));
        smartOrganization.setOrgType(Integer.valueOf(jsonObject.getString("orgType")));
        smartOrganization.setOrderNo(jsonObject.getString("orderNo"));
        smartOrganization.setNote(jsonObject.getString("note"));
        smartOrganization.setCreateAt(new Date());
        smartOrganization.setUpdateAt(new Date());
           departMapper.insert(smartOrganization);
         if((jsonObject.getString("children"))!=null) {
            InsertJsonArray(jsonObject.getJSONArray("children"));

            log.info("HeBeiDepartSysService :{}", smartOrganization);
        }

    }

}
