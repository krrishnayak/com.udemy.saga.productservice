package com.udemy.saga.productservice.query;

import java.util.ArrayList;
import java.util.List;

import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.udemy.saga.productservice.core.data.ProductEntity;
import com.udemy.saga.productservice.core.data.ProductRepository;

@Component
public class ProductQueryHandler {
private ProductRepository repository;
	
	public ProductQueryHandler(ProductRepository repository) {
		this.repository = repository;
	}
	
	@QueryHandler
	public List<ProductQueryModel> findProduct(FindProductQuery query) {
		
		List<ProductQueryModel> productsRest = new ArrayList<>();
		
		List<ProductEntity> storedProducts =  repository.findAll();
		
		for(ProductEntity productEntity: storedProducts) {
			ProductQueryModel productRestModel = new ProductQueryModel();
			BeanUtils.copyProperties(productEntity, productRestModel);
			productsRest.add(productRestModel);
		}
		
		return productsRest;
	}
}
