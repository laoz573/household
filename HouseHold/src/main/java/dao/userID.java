package dao;

public class userID {
	private int userID;
	private String username;
	private String password;
	
	public userID(int userID , String username , String password) {
		this.userID = userID;
		this.username = username;
		this.password = password;
	}
	
	public int getUserID() {
		return this.userID;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public String password() {
		return this.password;
	}
}
