package sm.classes.dao;

public interface IDBConstants {
	public static final String DB_NAME = "store.db";
	
	public static final String USERS_TABLE = "USERS";
	public static final String PRODUCTS_TABLE = "PRODUCTS";
	public static final String ORDERS_TABLE = "ORDERS";
	
	public static final String COLUMN_USERS_USERNAME = "USERNAME";
	public static final String COLUMN_USERS_PASSWORD = "PASSWORD";
	public static final String COLUMN_USERS_ADDRESS = "ADDRESS";
	public static final String COLUMN_USERS_ROLETYPE = "ROLETYPE";
	public static final int INDEX_USERS_USERNAME = 1;
	public static final int INDEX_USERS_PASSWORD = 2;
	public static final int INDEX_USERS_ADDRESS = 3;
	public static final int INDEX_USERS_ROLETYPE = 4;

	public static final String COLUMN_PRODUCTS_PRODUCTNAME = "PRODUCTNAME";
	public static final String COLUMN_PRODUCTS_PRODUCTTYPE = "PRODUCTTYPE";
	public static final String COLUMN_PRODUCTS_PRICE = "PRICE";
	public static final String COLUMN_PRODUCTS_QUANTITY = "QUANTITY";
	public static final int INDEX_PRODUCTS_PRODUCTNAME = 1;
	public static final int INDEX_PRODUCTS_PRODUCTTYPE = 2;
	public static final int INDEX_PRODUCTS_PRICE = 3;
	public static final int INDEX_PRODUCTS_QUANTITY = 4;
	
	public static final String COLUMN_ORDERS_ORDERID = "ORDERID";
	public static final String COLUMN_ORDERS_USERNAME = "USERNAME";
	public static final String COLUMN_ORDERS_PRODUCTNAME = "PRODUCTNAME";
	public static final String COLUMN_ORDERS_PRICE = "PRICE";
	public static final String COLUMN_ORDERS_QUANTITY = "QUANTITY";
	public static final String COLUMN_ORDERS_TOTAL = "TOTAL";
	public static final int INDEX_ORDERS_ORDERID = 1;
	public static final int INDEX_ORDERS_USERNAME = 2;
	public static final int INDEX_ORDERS_PRODUCTNAME = 3;
	public static final int INDEX_ORDERS_PRICE = 4;
	public static final int INDEX_ORDERS_QUANTITY = 5;
	public static final int INDEX_ORDERS_TOTAL = 6;
}
