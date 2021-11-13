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
		
		//dbc.insertUser(admins);
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
		
		System.out.println("---------------------------------------------------------------------");
		
		System.out.println("Welcome to our shop!\nPress 1 to login or 2 to register:\n");
		User user = null;
		int option = sc.nextInt();
		if(option == 1) {
			System.out.println("Please enter your username and password:\nUsername: ");
			String username = sc.next();
			System.out.println("\nPassword: ");
			String password = sc.next();
			user = dbc.checkUser(username, password);
			if(user != null) {
				System.out.println("Login successful!");
			} else {
				System.out.println("Incorrect credentials. Please register!");
				registration();
			}
		} else {
			registration();
		}
		if(user.getRoleType().equals(RoleType.ADMIN)) {
			System.out.println("Choose the desired operation:\n1.Add a new admin\n2.View all users\n3.Update a product\n0.Sign out");
			option = 1;
			while(option != 0) {
				option = sc.nextInt();
				switch(option) {
				case 1:
					
					break;
				case 2:
					dbc.selectUsers();
					System.out.println("Please choose another option!");
					break;
				case 3:
					
					break;
				case 0:
					System.out.println("Thank you!");
					break;
				default:
					System.out.println("Try another option!");
				}
			}
		} else {
			System.out.println("Choose the desired operation:\n1.Purchase a product\n2.View previous orders\n0.Sign out");
			option = 1;
			while(option != 0) {
				option = sc.nextInt();
				switch(option) {
				case 1:
					dbc.selectProducts();
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
	
	private static void registration() {		
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
	}

}
