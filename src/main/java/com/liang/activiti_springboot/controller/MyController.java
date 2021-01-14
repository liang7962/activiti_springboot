package com.liang.activiti_springboot.controller;

import com.liang.activiti_springboot.utils.SecurityUtil;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.api.task.model.Task;
import org.activiti.api.task.model.builders.TaskPayloadBuilder;
import org.activiti.api.task.runtime.TaskRuntime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
*   控制器
*   任务执行
* */
@RestController
public class MyController {

    @Autowired
    private ProcessRuntime processRuntime; //实现流程定义相关操作

    @Autowired
    private TaskRuntime taskRuntime;//任务操作相关的类

    @Autowired
    private SecurityUtil securityUtil;

    //查询任务，如果有任务则执行任务
    @RequestMapping("/testHello")
    public void testHello(){
        securityUtil.logInAs("ryandawsonuk");//security认证
        Page<Task> taskPage = taskRuntime.tasks(Pageable.of(0, 10));//分页查询任务列表
        if (taskPage.getTotalItems()>0){
            //说明有任务
            for (Task task:taskPage.getContent()){
                System.out.println("task1>>>"+task);
                //拾取任务
                taskRuntime.claim(TaskPayloadBuilder.claim().withTaskId(task.getId()).build());
                //执行任务
                taskRuntime.complete(TaskPayloadBuilder.complete().withTaskId(task.getId()).build());
            }
        }
        //再次查询任务
        taskPage = taskRuntime.tasks(Pageable.of(0, 10));//分页查询任务列表
        if (taskPage.getTotalItems()>0){
            //说明有任务
            for (Task task:taskPage.getContent()){
                System.out.println("task2>>>"+task);
            }
        }
    }
}
