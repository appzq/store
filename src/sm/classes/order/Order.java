package sm.classes.order;

import sm.classes.product.Product;

public class Order {
	private String orderId;
	private String username;
	private Product product;
	private static int id = 1;
		
	public Order(String username, Product product) {
		this.username = username;
		this.product = new Product(product.getProductName(),
				product.getProductType(), 
				product.getPrice(), 
				product.getQuantity());
		this.orderId = this.username + "_" + this.product.getProductName().substring(0, 1) + "_" + id;
		id++;
	}
	
	public String getOrderId() {
		return this.orderId;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public Product getProduct() {
		Product prod = new Product(product.getProductName(), product.getProductType(), product.getPrice(), product.getQuantity());
		return prod;
	}
	
	public double getTotal() {
		return this.product.getPrice() * this.product.getQuantity();
	}
	
}
