package api;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
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
	
	@POST 
	@Consumes("application/json")
	public void pay(String jsonArray) throws Exception {
		Genson genson = new Genson();
		List<SellItems> items = new ArrayList<SellItems>();
		
		items = genson.deserialize(jsonArray, new GenericType<List<SellItems>>() {});
		
		HttpURLConnection connection = CloudCon.createConnection(CloudCon.SELL);
		
		for (SellItems sellItems : items) {
			Document doc = DocumentGenerator.sellItemDocument(String.valueOf(sellItems.getItemID()), 
					String.valueOf(sellItems.getCustomerID()),
					String.valueOf(sellItems.getSaleAmount()), 
					String.valueOf(sellItems.getShopKey()));

			connection = CloudCon.createConnection(CloudCon.SELL);
			CloudCon.sendDocument(connection, doc);
		}
		
		//overViewController.updateItems();
		
		//return "overview?faces-redirect=true";
	}
}
