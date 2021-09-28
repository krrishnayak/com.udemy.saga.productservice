package org.krish.microservices.saga.productservice.command;

import java.math.BigDecimal;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.krish.microservices.saga.productservice.core.events.ProductCreatedEvent;
import org.springframework.beans.BeanUtils;

@Aggregate
public class ProductAggregate {
	
	@AggregateIdentifier
	private String productId;
	private String title;
	private BigDecimal price;
	private Integer quantity;
	
	public ProductAggregate() {
	}
	
	@CommandHandler
	public ProductAggregate(CreateProductCommand command) throws Exception {
		if(command.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
			throw new IllegalArgumentException("Price cannot be less or equal than zero");
		}
		
		if(command.getTitle() == null 
				|| command.getTitle().isBlank()) {
			throw new IllegalArgumentException("Title cannot be empty");
		}
		ProductCreatedEvent event = new ProductCreatedEvent();
		
		BeanUtils.copyProperties(command, event);
		
		AggregateLifecycle.apply(event);
		
		//throw new Exception("Forced exception thrown from aggregate class");
		
	}
	
	@EventSourcingHandler
	public void on(ProductCreatedEvent productCreatedEvent) {
		this.productId = productCreatedEvent.getProductId();
		this.price = productCreatedEvent.getPrice();
		this.title = productCreatedEvent.getTitle();
		this.quantity = productCreatedEvent.getQuantity();
		
	}
}
