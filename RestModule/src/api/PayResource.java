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
		List<ShopItem> items = new ArrayList<ShopItem>();
		
		items = genson.deserialize(jsonArray, new GenericType<List<ShopItem>>() {});
		
		HttpURLConnection connection = CloudCon.createConnection(CloudCon.MODIFY);

		
		for (ShopItem shopItem : items) {
			Document modifiedDocument = DocumentGenerator.itemDocument(String.valueOf(shopItem.getItemID()), 
																	   shopItem.getItemName(),
																	   shopItem.getItemUrl(), 
																	   String.valueOf(shopItem.getItemPrice()), 
																	   String.valueOf(shopItem.getItemStock()), 
																	   shopItem.itemDescriptionElm());

			Document doc = DocumentGenerator.modifyItemDocuemnt(modifiedDocument, String.valueOf(shopItem.getItemID()));

			CloudCon.sendDocument(connection, doc);
			
		}
		
		//overViewController.updateItems();
		
		//return "overview?faces-redirect=true";
	}
}
