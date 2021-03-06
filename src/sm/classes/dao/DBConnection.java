package sm.classes.dao;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import sm.classes.order.Order;
import sm.classes.product.Product;
import sm.classes.user.User;

public class DBConnection implements IDBConstants {
	private Connection conn;
	private static DBConnection currentInstance;
	private String dropTable;
	
	private DBConnection() {
		try {
			boolean cdb = false;
			File f = new File("./store.db");
			if(!f.exists()) 
				cdb = true;
			
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:store.db");
			conn.setAutoCommit(false);
			if(cdb)
				createUserTable();
				createProductTable();
				createOrdersTable();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static DBConnection getConnection() {
		if(currentInstance == null) {
			return currentInstance = new DBConnection();
		}
		return currentInstance;
	}
	
	public void closeDB() {
		if(conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void createUserTable() {
		dropTable = "DROP TABLE " + USERS_TABLE;
		String createUserTable = "CREATE TABLE IF NOT EXISTS USERS (USERNAME CHAR(30) NOT NULL, PASSWORD CHAR(30) NOT NULL, ADDRESS TEXT, ROLETYPE CHAR(10))";
		
		try {
			Statement statement = conn.createStatement();
			statement.executeUpdate(dropTable);
			statement.executeUpdate(createUserTable);
			statement.close();
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void createProductTable() {
		dropTable = "DROP TABLE " + PRODUCTS_TABLE;
		String createProductTable = "CREATE TABLE IF NOT EXISTS PRODUCTS (PRODUCTNAME CHAR(40) NOT NULL, PRODUCTTYPE CHAR(40), PRICE REAL, QUANTITY INTEGER)";
		
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(dropTable);
			stmt.executeUpdate(createProductTable);
			stmt.close();
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void createOrdersTable() {
		dropTable = "DROP TABLE " + ORDERS_TABLE;
		String createOrdersTable = "CREATE TABLE IF NOT EXISTS ORDERS (ORDERID CHAR(20) PRIMARY KEY, USERNAME CHAR(30) NOT NULL, PRODUCTNAME CHAR(40) NOT NULL, PRICE REAL, QUANTITY INTEGER, TOTAL REAL)";
	
		try {
			Statement statement = conn.createStatement();
			statement.executeUpdate(dropTable);
			statement.executeUpdate(createOrdersTable);
			statement.close();
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void insertUser(User user) {
		if(conn != null && user != null) {
			String prepStat = "insert into USERS (USERNAME, PASSWORD, ADDRESS, ROLETYPE) values (?, ?, ?, ?);";
			try {
				if(checkNewUsername(user.getUsername()) == false) {
					PreparedStatement ps = conn.prepareStatement(prepStat);
					ps.setString(1, user.getUsername());
					ps.setString(2, user.getPassword());
					ps.setString(3, user.getAddress());
					ps.setString(4, user.getRoleType().name());
					
					ps.executeUpdate();
					ps.close();
				} else {
					System.out.println("Username " + user.getUsername() + " already exists in DB.");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					conn.commit();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void insertProduct(Product product) {
		if(conn != null && product != null) {
			String prepStat = "insert into PRODUCTS (PRODUCTNAME, PRODUCTTYPE, PRICE, QUANTITY) values (?, ?, ?, ?)";
			try {
				if(checkExistingProduct(product.getProductName(), product.getProductType()) == false) {
					PreparedStatement ps = conn.prepareStatement(prepStat);
					ps.setString(1, product.getProductName());
					ps.setString(2, product.getProductType());
					ps.setDouble(3, product.getPrice());
					ps.setInt(4, product.getQuantity());
					
					ps.executeUpdate();
					ps.close();	
				}
				else {
					System.out.println("Product " + product.getProductName() + " already exists in DB.");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					conn.commit();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void insertOrder(Order order) {
		if(conn != null && order != null) {
			String preparedStatement = "insert into ORDERS (ORDERID, USERNAME, PRODUCTNAME, PRICE, QUANTITY, TOTAL) values (?, ?, ?, ?, ?, ?);";
			
			try {
				PreparedStatement ps = conn.prepareStatement(preparedStatement);
				ps.setString(1, order.getOrderId());
				ps.setString(2, order.getUsername());
				ps.setString(3, order.getProduct().getProductName());
				ps.setDouble(4, order.getProduct().getPrice());
				ps.setInt(5, order.getProduct().getQuantity());
				ps.setDouble(6, order.getTotal());
				
				ps.executeUpdate();
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					conn.commit();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void updateProduct(String productName, int quantity) {
		String updateProduct = "UPDATE PRODUCTS SET QUANTITY = QUANTITY + \"" + quantity + "\"WHERE PRODUCTNAME =\"" + productName + "\";";
		
		try {
			Statement statement = conn.createStatement();
			statement.executeUpdate(updateProduct);
			statement.close();
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean checkNewUsername(String username) {
		boolean existingUsername = false;
		String selectUsers = "select USERNAME from USERS where USERNAME = \"" + username + "\" limit 1;";
		
		try {
			Statement stmt = conn.createStatement();
			if(stmt.executeQuery(selectUsers).next())
				existingUsername = true;
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return existingUsername;
	}
	
	public User checkUser(String username, String password) {
		User user = null;
		String selectUser = "select * from USERS where USERNAME = \"" + username + "\" AND PASSWORD = \"" + password + "\";";
		
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(selectUser);
			if(rs.next()) {
				String userN = rs.getString(INDEX_USERS_USERNAME);
				String pw = rs.getString(INDEX_USERS_PASSWORD);
				String address = rs.getString(INDEX_USERS_ADDRESS);
				String roleType = rs.getString(INDEX_USERS_ROLETYPE);
				if(roleType.equals("ADMIN")) {
					user = new User(userN, pw);
				} else {
					user = new User(userN, pw, address);
				}
			}
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}
	
	public boolean checkExistingProduct(String productName, String productType) {
		boolean found = false;
		String selectProduct = "select * from PRODUCTS where PRODUCTNAME =\"" + productName + "\" AND PRODUCTTYPE =\"" + productType + "\" limit 1;";
		
		try {
			Statement statement = conn.createStatement();
			if(statement.executeQuery(selectProduct).next()) {
				found = true;
			};
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return found;
	}
	
	public void selectUsers() {
		System.out.println("---- Users ----");
		
		String selectUsers = "SELECT * FROM USERS;";
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(selectUsers);
			while(rs.next()) {
				String userN = rs.getString(INDEX_USERS_USERNAME);
				String pw = rs.getString(INDEX_USERS_PASSWORD);
				String address = rs.getString(INDEX_USERS_ADDRESS);
				String roleType = rs.getString(INDEX_USERS_ROLETYPE);
				System.out.println(roleType + ": " + userN + ", " + pw + ", " + address);
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Product selectProduct(String productName, int quantity) {
		Product prod = null;
		
		String selectedProduct = "SELECT * from PRODUCTS where PRODUCTNAME = \""+ productName + "\";";		
		try {
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(selectedProduct);
			if(rs.next()) {
				String prodName = rs.getString(INDEX_PRODUCTS_PRODUCTNAME);
				String productType = rs.getString(INDEX_PRODUCTS_PRODUCTTYPE);
				double price = rs.getDouble(INDEX_PRODUCTS_PRICE);
				prod = new Product(prodName, productType, price, quantity);
			}
			rs.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return prod;
	}
	
	public void selectProducts() {
		System.out.println("---- Available products ----");
		
		String selectProducts = "SELECT * from PRODUCTS;";		
		try {
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(selectProducts);
			while(rs.next()) {
				String prodName = rs.getString(INDEX_PRODUCTS_PRODUCTNAME);
				String productType = rs.getString(INDEX_PRODUCTS_PRODUCTTYPE);
				double price = rs.getDouble(INDEX_PRODUCTS_PRICE);
				int quantity = rs.getInt(INDEX_PRODUCTS_QUANTITY);
				System.out.println(productType + ": " + prodName + ", " + price + ", " + quantity);
			}
			rs.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void selectOrdersForUser(String username) {
		System.out.println("---- Previous orders ----");
		
		String selectOrders = "SELECT * FROM ORDERS WHERE USERNAME =\""+ username +"\";";
		try {
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(selectOrders);
			while(rs.next()) {
				String uName = rs.getString(INDEX_ORDERS_USERNAME);
				String productName = rs.getString(INDEX_ORDERS_PRODUCTNAME);
				double price = rs.getDouble(INDEX_ORDERS_PRICE);
				int quantity = rs.getInt(INDEX_ORDERS_QUANTITY);
				double total = rs.getDouble(INDEX_ORDERS_TOTAL);
				System.out.println(uName + ": " + productName + " - $" + price + ", " + quantity + ", $" + total);
			}
			rs.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void selectOrderByOrderId(String orderId) {
		String selectOrders = "SELECT * FROM ORDERS WHERE ORDERID =\""+ orderId +"\";";
		try {
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(selectOrders);
			while(rs.next()) {
				String uName = rs.getString(INDEX_ORDERS_USERNAME);
				String productName = rs.getString(INDEX_ORDERS_PRODUCTNAME);
				double price = rs.getDouble(INDEX_ORDERS_PRICE);
				int quantity = rs.getInt(INDEX_ORDERS_QUANTITY);
				double total = rs.getDouble(INDEX_ORDERS_TOTAL);
				System.out.println(uName + ": " + productName + " - $" + price + ", " + quantity + ", $" + total);
			}
			rs.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
