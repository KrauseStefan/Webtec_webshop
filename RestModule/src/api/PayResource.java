package api;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.XMLOutputter;

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
		List<SellItems> sellItems = new ArrayList<SellItems>();
		
		//sell items
		sellItems = genson.deserialize(jsonArray, new GenericType<List<SellItems>>() {});
		
		HttpURLConnection connection = CloudCon.createConnection(CloudCon.SELL);
		
		for (SellItems sellItem : sellItems) {
			Document doc = DocumentGenerator.sellItemDocument(String.valueOf(sellItem.getItemID()), 
					String.valueOf(sellItem.getCustomerID()),
					String.valueOf(sellItem.getSaleAmount()), 
					String.valueOf(sellItem.getShopKey()));

			connection = CloudCon.createConnection(CloudCon.SELL);
			CloudCon.sendDocument(connection, doc);
		}
		
		//load items
		List<ShopItem> items = new ArrayList<ShopItem>();
		HttpURLConnection itemConnection = CloudCon.createConnection(CloudCon.LIST);
		HttpURLConnection deletedConnection = CloudCon.createConnection(CloudCon.LISTDELETED);
		Document itemDoc = CloudCon.receiveDocument(itemConnection);
		Document deletedDoc = CloudCon.receiveDocument(deletedConnection);
		
		List<Long> deletedItemIDs = new ArrayList<Long>();
		
		Iterator<Element> i = deletedDoc.getRootElement().getChildren().iterator();

		while(i.hasNext()) {			
			Element element = i.next();
			
			deletedItemIDs.add(Long.parseLong(element.getText()));
		}
		
		items.clear();
	
		Iterator<Element> it = itemDoc.getRootElement().getChildren().iterator();

		
		while(it.hasNext()) {			
			Element element = it.next();
			ShopItem item = new ShopItem(element);
			
			if (!deletedItemIDs.contains(item.getItemID()))
				items.add(new ShopItem(element));
		}
		
		//modify according to sell
		for (SellItems sellItem : sellItems) {
			for(ShopItem item : items) {
				if(item.getItemID() == sellItem.getItemID()) {
					Document modifiedDocument = DocumentGenerator.itemDocument(String.valueOf(item.getItemID()), 
							item.getItemName(),
							item.getItemUrl(), 
							String.valueOf(item.getItemPrice()), 
							String.valueOf((item.getItemStock() - sellItem.getSaleAmount())), 
							item.itemDescriptionElm());

					String xmlstrong = new XMLOutputter().outputString(modifiedDocument);
					
					HttpURLConnection modify = CloudCon.createConnection(CloudCon.MODIFY);
					Document doc = DocumentGenerator.modifyItemDocuemnt(modifiedDocument, String.valueOf(item.getItemID()));
					
					int errorCode = CloudCon.sendDocument(modify, doc);
					
					String shis = "";
				}
			}
		}
	}
}
