package sm.classes.user;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class User implements IEnvVariables {
	private String username;
	private String password;
	private String address;
	private RoleType roleType;
		
	public User(String username, String password) {
		this.username = username;
		this.password = password;
		this.address = "";
		this.roleType = RoleType.ADMIN;
	}
	
	public User(String username, String password, String address) {
		this.username = username;
		this.password = password;
		this.address = address;
		this.roleType = RoleType.USER;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public RoleType getRoleType() {
		return roleType;
	}
	public void setRoleType(RoleType roleType) {
		this.roleType = roleType;
	}
	
	public static ArrayList<User> retrieveAdminsFromJSON() {
		ArrayList<User> admins = new ArrayList<>();
		try(FileReader file = new FileReader(ADMINS_JSON)) {
			JSONArray jArray = new JSONArray(new JSONTokener(file));
			jArray.forEach(jObj -> {
				JSONObject admin = (JSONObject) jObj;
				admins.add(new User(
						admin.getString("username"),
						admin.getString("password")));
			});
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return admins;
	}
	
	public void displayMenu() {
		if(this.roleType.equals(RoleType.ADMIN)) {
			
		} else {
			
		}
	}
}
