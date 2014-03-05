package api;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.POST;
import javax.ws.rs.Path; 
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import au.webtech.*;

import org.jdom2.Document;

import java.net.HttpURLConnection;

@Path("/login")
public class LoginResource {
	@Context HttpServletRequest session;
	
	final String USER_ID = "userID";
	
	@POST
	public boolean Login(@QueryParam("username") String userName, @QueryParam("password") String password) throws Exception {
		HttpURLConnection connection = CloudCon.createConnection(CloudCon.LOGIN);
		Document doc = DocumentGenerator.loginDocument(userName, password);
		
		CloudCon.sendDocument(connection, doc);
		
		HttpSession hs = session.getSession(true);
		
		//String loggedin = (String) hs.getAttribute("loggedin");
		
		int responseCode = connection.getResponseCode();
		
		if(responseCode >= 200 && responseCode < 300){			
	//		hs.setAttribute("loggedin", "sand");
			Document response = CloudCon.receiveDocument(connection);
			String id = DocumentGenerator.getItemValueUsingXpath(response, "//x:customerID", DocumentGenerator.getNS("x"));
			hs.setAttribute(USER_ID, id);
			
			return true;//sessionStore.createNewSessionID(userName);
		}

//		hs.setAttribute("loggedin", "falsk");
		return false;		
	}
}
