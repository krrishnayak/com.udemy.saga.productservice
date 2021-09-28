package org.krish.microservices.saga.productservice.command;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.krish.microservices.saga.productservice.core.data.ProductLookupEntity;
import org.krish.microservices.saga.productservice.core.data.ProductLookupRepository;
import org.krish.microservices.saga.productservice.core.events.ProductCreatedEvent;
import org.springframework.stereotype.Component;

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
