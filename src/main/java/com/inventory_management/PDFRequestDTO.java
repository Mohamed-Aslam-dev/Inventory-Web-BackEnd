package com.inventory_management;

import java.util.List;

public class PDFRequestDTO {

	private String companyName;
	private List<ProductsDTO> products;

	public PDFRequestDTO() {

	}

	public PDFRequestDTO(String companyName, List<ProductsDTO> products) {
		super();
		this.companyName = companyName;
		this.products = products;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public List<ProductsDTO> getProducts() {
		return products;
	}

	public void setProducts(List<ProductsDTO> products) {
		this.products = products;
	}

	@Override
	public String toString() {
		return "PDFProductsDTO [companyName=" + companyName + ", products=" + products + "]";
	}

}
