package api;

import java.net.HttpURLConnection;

import javax.ws.rs.PUT;
import javax.ws.rs.Path;

import org.jdom2.Document;

import au.webtech.CloudCon;
import au.webtech.DocumentGenerator;

//@Path("/service")
public class UserResource {

	//@PUT
	//@Path("createuser") 
	public void CreateUser(String userName, String password) throws Exception {
		HttpURLConnection connection = CloudCon.createConnection(CloudCon.CREATE);
		Document doc = DocumentGenerator.createCustomerDocument(userName, password);
		
		CloudCon.sendDocument(connection, doc);
		
		//return "overview?faces-redirect=true";
	}
}
