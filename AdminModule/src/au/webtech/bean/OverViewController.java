package au.webtech.bean;

import java.net.HttpURLConnection;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import javax.faces.bean.ManagedBean;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;

import au.webtech.CloudCon;

@ManagedBean
public class OverViewController {
	private final static String namespaceUrl = "http://www.cs.au.dk/dWebTek/2014";
	private final static Namespace nsX = Namespace.getNamespace("x", namespaceUrl);
	
	public OverViewController() throws Exception {
		this.items = new ArrayList<ShopItem>();
		this.updateItems();
	}
	
	private List<ShopItem> items;
	
	public List<ShopItem> getItems() throws Exception {
		return items;
	}
	public void setItemID(List<ShopItem> itemList) {
		this.items = itemList;
	}
	
	public void updateItems() throws Exception {
		HttpURLConnection connection = CloudCon.createConnection(CloudCon.LIST);
		Document doc = CloudCon.receiveDocument(connection);		
		
		items.clear();
		
		Iterator it = doc.getRootElement().getChildren().iterator();
		
		while(it.hasNext()) {			
			Element element = (Element)it.next();
			
			items.add(new ShopItem(element));
		}
	}
}
