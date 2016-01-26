package com.agentdemo.entity;

public class ProductItem {

	private String id = "";
	private String name = "";
	private String barcode = "";
	private double price = 0.0;
	private int quantity = 0;
	
	
	public ProductItem() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ProductItem(String name, String barcode, double price, int quantity) {
		super();
		this.name = name;
		this.barcode = barcode;
		this.price = price;
		this.quantity = quantity;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}
