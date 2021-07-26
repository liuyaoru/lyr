package com.cf.crs.controller;


import com.cf.crs.common.annotation.LogOperation;
import com.cf.crs.service.HeBeiGetDeviceListService;
import com.cf.util.http.HttpWebResult;
import com.cf.util.http.ResultJson;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Api(tags="获取设备列表接口")
@Slf4j
@RestController
@RequestMapping("/city/getdevice")
/*@RequestMapping("/api/hebei/uniauth")*/
public class HeBeiGetDeviceListController {
    @Autowired
    private HeBeiGetDeviceListService devicelistService;

    @LogOperation(value = "获取分页的条数查询子设备列表")
    @PostMapping("/alldevice")
    public ResultJson HeBeiGetDeviceList(int deviceType, int fromIndex, int toIndex)
    {
   return HttpWebResult.getMonoSucResult(devicelistService.GetDeviceListByDeviceType( deviceType, fromIndex, toIndex));

    }
    @LogOperation(value = "获取所有的设备列表通过设备类型")
    @PostMapping("/allDeviceByDeviceType")
    public ResultJson HeBeiGetDeviceListTotal(int deviceType)
    {

    return  HttpWebResult.getMonoSucResult(devicelistService.GetTotalDeviceListByDevice(deviceType));
    }


    @PostMapping("/cameraStatus")
    public ResultJson heBeiGetDeviceAllStatus(Integer deviceType,String deviceName)
    {

        return  HttpWebResult.getMonoSucResult(devicelistService.getDeviceTypeAllStatus(deviceType,deviceName));

    }



}
