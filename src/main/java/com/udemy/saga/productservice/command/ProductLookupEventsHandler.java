package com.udemy.saga.productservice.command;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

import com.udemy.saga.productservice.core.data.ProductLookupEntity;
import com.udemy.saga.productservice.core.data.ProductLookupRepository;
import com.udemy.saga.productservice.core.events.ProductCreatedEvent;

@Component
@ProcessingGroup("product-group")
public class ProductLookupEventsHandler {
	
	private final ProductLookupRepository productLookupRepository;
	
	public ProductLookupEventsHandler(ProductLookupRepository productLookupRepository) {
		this.productLookupRepository = productLookupRepository;
	}
	
	@EventHandler
	public void on(ProductCreatedEvent event) {
		
		ProductLookupEntity entity = new ProductLookupEntity(event.getProductId(), event.getTitle());
		
		productLookupRepository.save(entity);
		
	}

}
