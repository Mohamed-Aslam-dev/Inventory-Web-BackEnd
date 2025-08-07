package com.inventory_management;

public class ProductsDTO {
	
	private String productName;
	private Double productQuantity;
	
	public ProductsDTO() {
		
	}

	public ProductsDTO(String productName, Double productQuantity) {
		
		this.productName = productName;
		this.productQuantity = productQuantity;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Double getProductQuantity() {
		return productQuantity;
	}

	public void setProductQuantity(Double productQuantity) {
		this.productQuantity = productQuantity;
	}

	@Override
	public String toString() {
		return "ProductsDTO [productName=" + productName + ", productQuantity=" + productQuantity + "]";
	}
	
	

}
