package org.krish.microservices.saga.productservice.command.interceptors;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.BiFunction;

import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.krish.microservices.saga.productservice.command.CreateProductCommand;
import org.krish.microservices.saga.productservice.core.data.ProductEntity;
import org.krish.microservices.saga.productservice.core.data.ProductLookupEntity;
import org.krish.microservices.saga.productservice.core.data.ProductLookupRepository;
import org.krish.microservices.saga.productservice.core.data.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CreateProductCommandInterceptor implements MessageDispatchInterceptor<CommandMessage<?>> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CreateProductCommandInterceptor.class);
	private final ProductLookupRepository repo;
	
	public CreateProductCommandInterceptor(ProductLookupRepository repo) {
		this.repo = repo;
	}
 
	@Override
	public BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> handle(
			List<? extends CommandMessage<?>> messages) {
		 
		return (index, command) -> {
			
			LOGGER.info("Intercepted command: " + command.getPayloadType());
			
			if(CreateProductCommand.class.equals(command.getPayloadType())) {
				
				CreateProductCommand createProductCommand = (CreateProductCommand)command.getPayload();
				
				if(createProductCommand.getTitle() == null ||
						createProductCommand.getTitle().isBlank())
				{
					throw new IllegalArgumentException("Title cannot be blank");
				}
				
				ProductLookupEntity entity =  repo.findByProductIdOrTitle(createProductCommand.getProductId(),
						createProductCommand.getTitle());
				
				if(entity != null) {
					throw new IllegalStateException(
							String.format("Product with productId %s or title %s already exist", 
									createProductCommand.getProductId(), createProductCommand.getTitle())
							);
				}

			}
			
			return command;
		};
	}

}

