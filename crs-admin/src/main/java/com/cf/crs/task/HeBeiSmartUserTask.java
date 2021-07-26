package com.cf.crs.task;

import com.cf.crs.job.task.ITask;
import com.cf.crs.service.HeBeiSmartUserListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("SmartUserTask")
public class HeBeiSmartUserTask implements ITask {
    @Autowired
    private HeBeiSmartUserListService UserListService;

    @Override
    public void run(String params) {


    }
}
