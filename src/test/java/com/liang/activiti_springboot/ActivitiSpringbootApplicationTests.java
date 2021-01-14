package com.liang.activiti_springboot;

import com.liang.activiti_springboot.utils.SecurityUtil;
import org.activiti.api.process.model.ProcessDefinition;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.api.task.model.Task;
import org.activiti.api.task.model.builders.ClaimTaskPayloadBuilder;
import org.activiti.api.task.model.builders.TaskPayloadBuilder;
import org.activiti.api.task.runtime.TaskRuntime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ActivitiSpringbootApplicationTests {

	@Autowired
	private ProcessRuntime processRuntime; //实现流程定义相关操作

	@Autowired
	private TaskRuntime taskRuntime;//任务操作相关的类

	@Autowired
	private SecurityUtil securityUtil;

	@Test
	void contextLoads() {
	}

	//流程定义
	@Test
	void testDefinition() {
		securityUtil.logInAs("salaboy");
		//分页查询出流程定义信息  注意：流程部署工作在activiti7与springboot整合后，会自动部署resources/processes/*.bpmn
		Page<ProcessDefinition> processDefinitionPage = processRuntime.processDefinitions(Pageable.of(0, 10));
		System.out.println(processDefinitionPage.getTotalItems());//查看已部署的流程个数
		for (ProcessDefinition processDefinition:processDefinitionPage.getContent()){
			System.out.println(processDefinition);
		}

	}
	//自动流程实例
	@Test
	void testStartInstance() {
		securityUtil.logInAs("salaboy");//security认证
		ProcessInstance processInstance = processRuntime.start(ProcessPayloadBuilder.start().withProcessDefinitionKey("myProcess_1").build());//启动流程实例
		System.out.println("processInstance.getId()>>>"+processInstance.getId());
	}

	//查询任务并完成任务
	@Test
	void testTask() {
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
