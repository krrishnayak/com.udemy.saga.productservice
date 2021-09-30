package org.krish.microservices.saga.productservice.command;

import java.math.BigDecimal;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.krish.microservices.saga.core.commands.ReserveProductCommand;
import org.krish.microservices.saga.core.events.ProductReservedEvent;
import org.krish.microservices.saga.productservice.core.events.ProductCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

@Aggregate
public class ProductAggregate {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductAggregate.class);

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
	
	@CommandHandler
	public ProductAggregate(ReserveProductCommand reserveProductCommand) {
		
		if(quantity < reserveProductCommand.getQuantity()) {
			throw new IllegalArgumentException("Insufficient number of items in stock");
		}
		
		ProductReservedEvent productReservedEvent = ProductReservedEvent.builder()
				.orderId(reserveProductCommand.getOrderId())
				.productId(reserveProductCommand.getProductId())
				.quantity(reserveProductCommand.getQuantity())
				.userId(reserveProductCommand.getUserId())
				.build();
		
		AggregateLifecycle.apply(productReservedEvent);
		
	}
	
	@EventSourcingHandler
	public void on(ProductCreatedEvent productCreatedEvent) {
		this.productId = productCreatedEvent.getProductId();
		this.price = productCreatedEvent.getPrice();
		this.title = productCreatedEvent.getTitle();
		this.quantity = productCreatedEvent.getQuantity();
		
	}
	
	@EventSourcingHandler
	public void on(ProductReservedEvent productReservedEvent) {
		this.quantity -= productReservedEvent.getQuantity();
		LOGGER.info("Updated Quantity: " +quantity );
	}
}
