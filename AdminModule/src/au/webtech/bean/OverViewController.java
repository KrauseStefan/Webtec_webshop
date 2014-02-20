package au.webtech.bean;

import java.net.HttpURLConnection;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.jdom2.Document;
import org.jdom2.Element;
import au.webtech.CloudCon;
import au.webtech.DocumentGenerator;

/**
 * Class is responsible for the overview view and for updating 
 * the list of ShopItems every time a change has occurred.
 */

@ManagedBean
@SessionScoped
public class OverViewController {
	
	private List<ShopItem> items;
	private ShopItem shopItem;
	
	public OverViewController() throws Exception {
		this.items = new ArrayList<ShopItem>();
		this.updateItems();
	}
	
	public void updateItems() throws Exception {
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
	}
	
	public String deleteItem() throws Exception
	{
		HttpURLConnection connection = CloudCon.createConnection(CloudCon.DELETE);
		Document doc = DocumentGenerator.deleteItemDocument(String.valueOf(shopItem.getItemID()));
		CloudCon.sendDocument(connection, doc);
		
		this.updateItems();
		
		return "overview";
	}
	
	public ShopItem getShopItem() {
		return shopItem;
	}

	public void setShopItem(ShopItem shopItem) {
		this.shopItem = shopItem;
	}
	
	public List<ShopItem> getItems() throws Exception {
		return items;
	}
	public void setItemID(List<ShopItem> itemList) {
		this.items = itemList;
	}
}
