package au.webtech.bean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import java.io.Serializable;

/**
 * Class is responsible for login and logout.
 */

@ManagedBean
@SessionScoped
public class AdminLogin implements Serializable {
	private static final long serialVersionUID = -7318774474932321476L;

	private final String nameSecret = "admin";
	private final String passwordSecret = "gud";
	
	private String name;
	private String password;
	
	public String logout() {
		password = "";
		name = "";
		
		return "login";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public boolean isLooggedin(){
		return (nameSecret.equals(name) && password.equals(passwordSecret));		
	}
	
	public String getSuccess(){
		if(this.isLooggedin())
			return "overview";
		
		return "Admin";
	}
	
}