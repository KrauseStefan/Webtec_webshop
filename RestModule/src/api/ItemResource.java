package api;

import javax.ws.rs.GET;
import javax.ws.rs.Path; 
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import au.webtech.*;

import org.jdom2.Document;
import org.jdom2.Element;

import com.owlike.genson.Genson;

import java.net.HttpURLConnection;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

@Path("/service")
public class ItemResource {

	@GET 
	@Path("items") 
	@Produces(MediaType.APPLICATION_JSON)
	public String GetItems() throws Exception {
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
		Genson genson = new Genson();
		String json = genson.serialize(items);
		
		return json;
		
	}
}
