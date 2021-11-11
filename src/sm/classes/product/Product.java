package sm.classes.product;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class Product {
	private String productName;
	private String productType;
	private double price;
	private int quantity;
	
	public Product(String productName, String productType, double price, int quantity) {
		this.productName = productName;
		this.productType = productType;
		this.price = price;
		this.quantity = quantity;
	}

	public String getProductName() {
		return productName;
	}

	public String getProductType() {
		return productType;
	}

	public double getPrice() {
		return price;
	}

	public int getQuantity() {
		return quantity;
	}
	
	public static ArrayList<Product> readProductsFromTxt(String fileName) {
		ArrayList<Product> products = new ArrayList<Product>();
		try {
			FileReader f = new FileReader(fileName);
			Scanner sc = new Scanner(f).useDelimiter(",|" + System.lineSeparator());
			while(sc.hasNextLine()) {
				String productName = sc.next();
				String productType = sc.next();
				double price = sc.nextDouble();
				int quantity = sc.nextInt();
				products.add(new Product(productName, productType, price, quantity));
			}
			sc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return products;
	}
	
	
}
