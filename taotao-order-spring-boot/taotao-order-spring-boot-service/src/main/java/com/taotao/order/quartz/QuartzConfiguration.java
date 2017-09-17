package com.taotao.order.quartz;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.taotao.order.service.OrderService;

@Configuration
public class QuartzConfiguration {

	/**
	 * 任务详细信息bean
	 * @param orderService 订单业务对象
	 * @return
	 */
	@Bean("orderJobDetail")
	public MethodInvokingJobDetailFactoryBean getJobDetail(OrderService orderService) {
		MethodInvokingJobDetailFactoryBean jobDetail = new MethodInvokingJobDetailFactoryBean();
		jobDetail.setTargetObject(orderService);//执行对象
		jobDetail.setTargetMethod("autoCloseOrder");//执行对象对应的方法
		jobDetail.setConcurrent(false);//不可并发执行
		return jobDetail;
	}
	
	/**
	 * 任务触发器bean
	 * @param jobDetailFactoryBean 任务详细信息
	 * @param cronExpression 执行表达式（在application.properties中配置的）
	 * @return
	 */
	@Bean("orderCronTrigger")
	public CronTriggerFactoryBean getCronTrigger(MethodInvokingJobDetailFactoryBean jobDetailFactoryBean,
			@Value("${quartz.cronExpression}")String cronExpression) {
		CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setJobDetail(jobDetailFactoryBean.getObject());
		cronTriggerFactoryBean.setCronExpression(cronExpression);
		return cronTriggerFactoryBean;
	}
	
	/**
	 * 任务触发器调度工厂bean
	 * @param cronTrigger 任务调度触发器
	 * @return
	 */
	@Bean("orderSchedulerFactoryBean")
	public SchedulerFactoryBean getSchedulerFactoryBean(CronTriggerFactoryBean cronTrigger) {
		SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
		schedulerFactoryBean.setTriggers(cronTrigger.getObject());
		return schedulerFactoryBean;
	}
}
