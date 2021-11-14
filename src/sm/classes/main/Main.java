package sm.classes.main;

import java.util.ArrayList;
import java.util.Scanner;

import sm.classes.dao.DBConnection;
import sm.classes.order.Order;
import sm.classes.product.Product;
import sm.classes.user.RoleType;
import sm.classes.user.User;

public class Main {
	private static DBConnection dbc = DBConnection.getConnection();
	private static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) {
		ArrayList<User> admins = User.retrieveAdminsFromJSON();
		
//		admins.forEach(admin -> {
//			dbc.insertUser(admin);
//		});
		//dbc.selectUsers();
		
		ArrayList<User> users = new ArrayList<>();
		users.add(new User("apop", "pwpwpwp", "asdas"));
		users.add(new User("apop2", "pwpwpwp", "asdas"));
		
		//dbc.insertUser(users);
		//dbc.selectUsers();
		//dbc.closeDB();
		
		ArrayList<Product> products = Product.readProductsFromTxt("products.txt");

		//dbc.insertProduct(products);
		dbc.selectUsers();
		dbc.selectProducts();
		
//		users.forEach(user -> {
//			dbc.insertOrder(new Order(user.getUsername(), products.get(0)));
//		});
		
		dbc.selectOrdersForUser("apop");
		
		String username, password, productName, productType;
		int quantity;
		double price;
		
		System.out.println("---------------------------------------------------------------------");
		System.out.println("Welcome to our shop!\nPress 1 to login or 2 to register:");
		User user = null;
		int option = sc.nextInt();
		if(option == 1) {
			System.out.println("Please enter your username and password:\nUsername: ");
			username = sc.next();
			System.out.println("\nPassword: ");
			password = sc.next();
			user = dbc.checkUser(username, password);
			if(user != null) {
				System.out.println("Login successful!");
			} else {
				System.out.println("Incorrect credentials. Please register!");
				user = registration();
			}
		} else {
			user = registration();
		}
		if(user.getRoleType().equals(RoleType.ADMIN)) {
			System.out.println("Choose the desired operation:\n1.Add a new admin\n2.View all users\n3.Add a new product\n4.Update a product\n0.Sign out");
			option = 1;
			while(option != 0) {
				option = sc.nextInt();
				switch(option) {
				case 1:
					System.out.println("Please enter the username: ");
					username = sc.next();
					boolean invalidUser = true;
					while(invalidUser) {
						if(!dbc.checkNewUsername(username)) {
							invalidUser = false;
						} else {
							System.out.println("Username already taken! Please retry");
							username = sc.next();
						}
					}
					System.out.println("Please enter a password: ");
					password = sc.next();
					User admin = new User(username, password);
					dbc.insertUser(admin);
					System.out.println("Registration successful!");
					System.out.println("Please choose another option!");
					break;
				case 2:
					dbc.selectUsers();
					System.out.println("Please choose another option!");
					break;
				case 3:
					System.out.println("Please insert product's name:");
					productName = sc.next();
					System.out.println("Please insert product's type:");
					productType = sc.next();
					System.out.println("Please insert product's price:");
					price = sc.nextDouble();
					System.out.println("Please insert the available quantity:");
					quantity = sc.nextInt();
					dbc.insertProduct(new Product(productName, productType, price, quantity));
					System.out.println("Please choose another option!");
					break;
				case 4:
					System.out.println("Please insert product's name:");
					productName = sc.next();
					System.out.println("Please insert products quantity:");
					quantity = sc.nextInt();
					dbc.updateProduct(productName, quantity);
					dbc.selectProducts();
					System.out.println("Please choose another option!");
					break;
				case 0:
					System.out.println("Thank you!");
					break;
				default:
					System.out.println("Try another option!");
				}
			}
		} else {
			System.out.println("Choose the desired operation:\n1.Purchase a product\n2.View your orders\n0.Sign out");
			option = 1;
			while(option != 0) {
				option = sc.nextInt();
				switch(option) {
				case 1:
					dbc.selectProducts();
					System.out.println("Please provide product's name and the desired quantity to be added:\nProduct's name:");
					productName = sc.next();
					System.out.println("Quantity to be added:");
					quantity = sc.nextInt();
					dbc.updateProduct(productName, -quantity);
					Product product = dbc.selectProduct(productName, quantity);
					Order order  = new Order(user.getUsername(), product);
					dbc.insertOrder(order);
					System.out.println("Order has been placed! Thank you!");
					dbc.selectOrderByOrderId(order.getOrderId());
					System.out.println("Please choose another option!");
					break;
				case 2:
					dbc.selectOrdersForUser(user.getUsername());
					System.out.println("Please choose another option!");
					break;
				case 0:
					System.out.println("Thank you!");
					break;
				default:
					System.out.println("Try another option!");
				}
			}
		}
		dbc.closeDB();
		sc.close();
 	}
	
	private static User registration() {		
		System.out.println("Please enter your username: ");
		String username = sc.next();
		boolean invalidUser = true;
		while(invalidUser) {
			if(!dbc.checkNewUsername(username)) {
				invalidUser = false;
			} else {
				System.out.println("Username already taken! Please retry");
				username = sc.next();
			}
		}
		System.out.println("Please enter your password: ");
		String password = sc.next();
		System.out.println("Please enter your address: ");
		String address = sc.next();
		User user = new User(username, password, address);
		dbc.insertUser(user);
		System.out.println("User successfully created!");
		return user;
	}

}
