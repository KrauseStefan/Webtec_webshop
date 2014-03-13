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
//	@Produces(MediaType.APPLICATION_JSON)
	public String CreateUser(@QueryParam("userName") String userName, @QueryParam("password") String password) throws Exception {
		HttpURLConnection connection = CloudCon.createConnection(CloudCon.CREATE_CUSTOMER);
		Document doc = DocumentGenerator.createCustomerDocument(userName, password);
			
		CloudCon.sendDocument(connection, doc);
		Document response = CloudCon.receiveDocument(connection);
				
		String ID = DocumentGenerator.getItemValueUsingXpath(response, "//x:customerID", DocumentGenerator.getNS("x"));
		
		if(ID == null){
			return "{ " + 
					"message: \"Failed to create user. User name already in use!\"," + 
					"successful: false" + 
				"}";
		}
		
		return "{ " + 
			"message: \"User: '" + userName + "' created sucessfully\"," + 
			"successful: true" + 
		"}";
	}
}
