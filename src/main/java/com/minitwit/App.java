package com.minitwit;

import com.minitwit.config.AdminConfig;
import com.minitwit.service.impl.AdminService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.minitwit.config.WebConfig;
import com.minitwit.service.impl.MiniTwitService;

@Configuration
@ComponentScan({ "com.minitwit" })
public class App {
	
	public static void main(String[] args) {
    	AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(App.class);

		new WebConfig(ctx.getBean(MiniTwitService.class));
		new AdminConfig(ctx.getBean(AdminService.class));

        ctx.registerShutdownHook();
    }
    
    
}
