package com.inventory_management;

public class ProductsDTO {
	
	private String productName;
	private Double productQuantity;
	private String productUnit;
	
	public ProductsDTO() {
		
	}

	public ProductsDTO(String productName, Double productQuantity, String productUnit) {
		this.productName = productName;
		this.productQuantity = productQuantity;
		this.productUnit = productUnit;
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
	
	

	public String getProductUnit() {
		return productUnit;
	}

	public void setProductUnit(String productUnit) {
		this.productUnit = productUnit;
	}

	@Override
	public String toString() {
		return "ProductsDTO [productName=" + productName + ", productQuantity=" + productQuantity + ", productUnit="
				+ productUnit + "]";
	}

	
	
	

}
