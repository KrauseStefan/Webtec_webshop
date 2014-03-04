package api;

import java.net.HttpURLConnection;

import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.jdom2.Document;

import au.webtech.CloudCon;
import au.webtech.DocumentGenerator;

@Path("/createuser")
public class UserResource {

	@PUT
	public void CreateUser(@QueryParam("userName") String userName, @QueryParam("password") String password) throws Exception {
		HttpURLConnection connection = CloudCon.createConnection(CloudCon.CREATE);
		Document doc = DocumentGenerator.createCustomerDocument(userName, password);
		
		CloudCon.sendDocument(connection, doc);
		
		//return "overview?faces-redirect=true";
	}
}
