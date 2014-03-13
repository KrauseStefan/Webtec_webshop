package api;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

import org.jdom2.Document;
import au.webtech.CloudCon;
import au.webtech.DocumentGenerator;

import com.owlike.genson.GenericType;
import com.owlike.genson.Genson;

@Path("/pay") 
public class PayResource {
	
	@Context HttpServletRequest session;
	
	public final static int SHOP_ID = 194;
	
	private int getUserID(){
		
		HttpSession hs = session.getSession();
		
		Object id = hs.getAttribute(LoginResource.USER_ID);
		if(id != null)
			return (int) id;
		return 0;
	}
	
	@POST 
	@Consumes("application/json")
	public boolean pay(String jsonArray) throws Exception {
		
		int userID = getUserID();
		
		if(userID == 0)
			return false;
		
		Genson genson = new Genson();
		List<SellItems> sellItems = new ArrayList<SellItems>();
		
		//sell items
		sellItems = genson.deserialize(jsonArray, new GenericType<List<SellItems>>() {});
		
		HttpURLConnection connection = CloudCon.createConnection(CloudCon.SELL);
		
		for (SellItems sellItem : sellItems) {
			Document doc = DocumentGenerator.sellItemDocument(String.valueOf(sellItem.getItemID()), 
					String.valueOf(userID),
					String.valueOf(sellItem.getSaleAmount()));

			connection = CloudCon.createConnection(CloudCon.SELL);
			CloudCon.sendDocument(connection, doc);
		}
		
		return true;
	}
}
