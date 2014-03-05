package api;

import javax.ws.rs.POST;
import javax.ws.rs.Path; 
import javax.ws.rs.QueryParam;

import au.webtech.*;

import org.jdom2.Document;
import org.jdom2.output.XMLOutputter;

import java.net.HttpURLConnection;

@Path("/login")
public class LoginResource {

	@SuppressWarnings("unused")
	@POST
	public Boolean Login(@QueryParam("username") String userName, @QueryParam("password") String password) throws Exception {
		HttpURLConnection connection = CloudCon.createConnection(CloudCon.LOGIN);
		Document doc = DocumentGenerator.loginDocument(userName, password);
		
		CloudCon.sendDocument(connection, doc);
		
		int responseCode = connection.getResponseCode();
		
		if(responseCode >= 200 && responseCode < 300)
			return true;

		return false;		
	}
}
