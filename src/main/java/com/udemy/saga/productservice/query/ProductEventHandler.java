package com.udemy.saga.productservice.query;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.udemy.saga.productservice.core.data.ProductEntity;
import com.udemy.saga.productservice.core.data.ProductRepository;
import com.udemy.saga.productservice.core.events.ProductCreatedEvent;

@Component
@ProcessingGroup("product-group")
public class ProductEventHandler {

	private ProductRepository repository;
	
	public ProductEventHandler(ProductRepository repository) {
		this.repository = repository;
	}
	
	@ExceptionHandler(resultType = Exception.class)
	public void handle(Exception e) throws Exception{
		throw e;
	}
	
	@EventHandler
	public void on(ProductCreatedEvent event) throws Exception {
		
		ProductEntity entity = new ProductEntity();
		BeanUtils.copyProperties(event, entity);
		
		repository.save(entity);
		throw new Exception("Forced exception thrown from event handler class");
	}
}
