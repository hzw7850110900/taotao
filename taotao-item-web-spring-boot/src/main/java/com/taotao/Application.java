package com.taotao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

/**
 * @SpringBootApplication组合注解：如果使用spring boot这是必须的；默认扫描当前类所在的包及其子包的spring注解
 *
 */
@SpringBootApplication
@ImportResource(locations= {"classpath:spring/applicationContext-dubbo.xml"})//加载额外的spring 配置文件
public class Application {//Application任意写

	public static void main(String[] args) {
		//启动的时候会在控制台输出spring logo字符串
		SpringApplication.run(Application.class, args);
	}
}
