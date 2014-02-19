package au.webtech.bean;

import java.net.HttpURLConnection;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import org.jdom2.Document;

import au.webtech.CloudCon;
import au.webtech.DocumentGenerator;

@ManagedBean
public class AdjustController {
	public AdjustController() {
		this.shopItem = new ShopItem();
	}
	
	private ShopItem shopItem;
	
	public ShopItem getShopItem() {
		return shopItem;
	}

	public void setShopItem(ShopItem shopItem) {
		this.shopItem = shopItem;
	}
	
	@ManagedProperty(value="#{overViewController}") 
	private OverViewController overViewController;
	
	public OverViewController getOverViewController() {
		return overViewController;
	}
	
	public void setOverViewController(OverViewController overViewController) {
		this.overViewController = overViewController;
	}
	
	public String adjustItemStock() throws Exception {
		HttpURLConnection connection = CloudCon.createConnection(CloudCon.ADJUST);
		
		Document doc = DocumentGenerator.adjustItemStockDocument(String.valueOf(shopItem.getItemID()), String.valueOf(shopItem.getItemStock()));
		
		CloudCon.sendDocument(connection, doc);
		
		overViewController.updateItems();
		
		return "overview?faces-redirect=true";
	}
}
