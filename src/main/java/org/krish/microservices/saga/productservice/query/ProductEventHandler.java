package org.krish.microservices.saga.productservice.query;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.krish.microservices.saga.core.events.ProductReservedEvent;
import org.krish.microservices.saga.productservice.core.data.ProductEntity;
import org.krish.microservices.saga.productservice.core.data.ProductRepository;
import org.krish.microservices.saga.productservice.core.events.ProductCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@ProcessingGroup("product-group")
public class ProductEventHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProductEventHandler.class);

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
	}
	
	@EventHandler
	public void on(ProductReservedEvent productReservedEvent) throws Exception {
		
		ProductEntity productEntity = repository.findByProductId(productReservedEvent.getProductId());
		
		LOGGER.debug("ProductReservedEvent: Current product quantity " + productEntity.getQuantity());
		
		productEntity.setQuantity(productEntity.getQuantity() - productReservedEvent.getQuantity());
		
		
		repository.save(productEntity);
		
		LOGGER.debug("ProductReservedEvent: New product quantity " + productEntity.getQuantity());
 	
		LOGGER.info("ProductReservedEvent is called for productId:" + productReservedEvent.getProductId() +
				" and orderId: " + productReservedEvent.getOrderId());
	}
}
