package sm.classes.main;

import java.util.ArrayList;

import sm.classes.dao.DBConnection;
import sm.classes.product.Product;
import sm.classes.user.User;

public class Main {

	public static void main(String[] args) {
		ArrayList<User> admins = User.retrieveAdminsFromJSON();
		
		DBConnection dbc = DBConnection.getConnection();
		dbc.insertUser(admins);
		dbc.selectUsers();
		
		ArrayList<User> users = new ArrayList<>();
		users.add(new User("apop", "pwpwpwp", "asdas"));
		users.add(new User("apop2", "pwpwpwp", "asdas"));
		users.add(new User("apop", "pwpwpwp", "asdas"));
		
		dbc.insertUser(users);
		dbc.selectUsers();
		//dbc.closeDB();
		
		ArrayList<Product> products = Product.readProductsFromTxt("products.txt");
		products.forEach(prod -> {
			System.out.println(prod.getProductName() + ", " + prod.getPrice());
		});
		products.add(new Product("CLOTHING", "sweater", 125, 2));
		dbc.insertProduct(products);
		dbc.selectProducts();
	}

}
