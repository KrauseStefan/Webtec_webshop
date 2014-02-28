package api;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.jdom2.Document;

import au.webtech.CloudCon;
import au.webtech.DocumentGenerator;

import com.owlike.genson.GenericType;
import com.owlike.genson.Genson;

@Path("/service")
public class PayResource {
	
	@POST
	@Path("pay") 
	public void pay(String jsonArray) throws Exception {
		Genson genson = new Genson();
		List<ShopItem> items = new ArrayList<ShopItem>();
		
		items = genson.deserialize(jsonArray, new GenericType<List<ShopItem>>() {});
		/*
		HttpURLConnection connection = CloudCon.createConnection(CloudCon.MODIFY);

		Document modifiedDocument = DocumentGenerator.itemDocument(String.valueOf(shopItem.getItemID()), 
																	shopItem.getItemName(),
																	shopItem.getItemUrl(), 
																	String.valueOf(shopItem.getItemPrice()), 
																	String.valueOf(shopItem.getItemStock()), 
									s								shopItem.getItemDescriptionElm());
		
		Document doc = DocumentGenerator.modifyItemDocuemnt(modifiedDocument, String.valueOf(this.getShopItem().getItemID()));
		

		CloudCon.sendDocument(connection, doc);
		
		overViewController.updateItems();
		*/
		//return "overview?faces-redirect=true";
	}
	
}
