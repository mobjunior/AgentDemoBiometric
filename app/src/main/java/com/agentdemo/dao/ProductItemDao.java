package com.agentdemo.dao;

import com.agentdemo.entity.ProductItem;

public interface ProductItemDao {

	public abstract void addProductItem(ProductItem productItem);

	public abstract void updateQuantity(String barcode, int quantity);

}