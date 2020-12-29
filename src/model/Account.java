package model;

public class Account {
	private String loginname;
	private String password;
	
	public Account(String loginname, String password) {
		super();
		this.loginname = loginname;
		this.password = password;
	}
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

}
