package api;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path; 
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import au.webtech.*;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.XMLOutputter;

import com.owlike.genson.Genson;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

@Path("/login")
public class LoginResource {

	@SuppressWarnings("unused")
	@POST 
	public Boolean Login(@QueryParam("username") String userName, @QueryParam("password") String password) throws Exception {
		HttpURLConnection connection = CloudCon.createConnection(CloudCon.LOGIN);
		Document doc = DocumentGenerator.loginDocument(userName, password);

		String docrequest = new XMLOutputter().outputString(doc);
		
		CloudCon.sendDocument(connection, doc);
		
		Document response = CloudCon.receiveDocument(connection);
		
		String docresponse = new XMLOutputter().outputString(response);
		
		String e = "";
		
		return true;
	}
}
