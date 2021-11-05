package sm.classes.main;

import java.util.ArrayList;

import sm.classes.user.User;

public class Main {

	public static void main(String[] args) {
		ArrayList<User> admins = User.retrieveAdminsFromJSON();
		admins.forEach(a -> {
			System.out.println(a.getUsername() + " | " + a.getPassword());
		});
	}

}
