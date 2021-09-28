package org.krish.microservices.saga.productservice.query;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ProductQueryModel {
	private String productId;
	private String title;
	private BigDecimal price;
	private Integer quantity;
}
