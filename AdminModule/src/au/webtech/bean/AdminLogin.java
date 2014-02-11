package au.webtech.bean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;

@ManagedBean
@SessionScoped
public class AdminLogin implements Serializable {
	private static final long serialVersionUID = -7318774474932321476L;

	private final String nameSecret = "admin";
	private final String passwordSecret = "gud";
	
	private String name;
	private String password;

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
	
	public String getSuccess(){
		if(nameSecret.equals(name) && password.equals(passwordSecret))
			return "sucess";
		
		return "failure";
	}
	
}