package com.udemy.saga.productservice;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.config.EventProcessingConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.ApplicationContext;

import com.udemy.saga.productservice.command.interceptors.CreateProductCommandInterceptor;
import com.udemy.saga.productservice.core.errorhandling.ProductsServiceEventsErrorHandler;

@EnableEurekaClient
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Autowired
	public void registerCreateProductCommandInterceptor(ApplicationContext context, 
			CommandBus commandBus) {
		commandBus.registerDispatchInterceptor(context.getBean(CreateProductCommandInterceptor.class));
		
	}
	
	@Autowired
	public void configure(EventProcessingConfigurer config) {
		config.registerListenerInvocationErrorHandler("product-group", 
				conf -> new ProductsServiceEventsErrorHandler());
		
//		config.registerListenerInvocationErrorHandler("product-group", 
//				conf -> PropagatingErrorHandler.instance());
	}
}
