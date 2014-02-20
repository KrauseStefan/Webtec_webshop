package au.webtech.bean;

import java.net.HttpURLConnection;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import org.jdom2.Document;

import au.webtech.CloudCon;
import au.webtech.DocumentGenerator;

/**
 * Class is responsible for handling of the adjust view and the adjustment of the item stock.
 */

@ManagedBean
public class AdjustController {
	@ManagedProperty(value="#{overViewController}") 
	private OverViewController overViewController;
	private ShopItem shopItem;
	
	public AdjustController() {
		this.shopItem = new ShopItem();
	}
	
	public String adjustItemStock() throws Exception {
		HttpURLConnection connection = CloudCon.createConnection(CloudCon.ADJUST);
		
		Document doc = DocumentGenerator.adjustItemStockDocument(String.valueOf(shopItem.getItemID()), String.valueOf(shopItem.getItemStock()));
		
		CloudCon.sendDocument(connection, doc);
		
		overViewController.updateItems();
		
		return "overview?faces-redirect=true";
	}
	
	public ShopItem getShopItem() {
		return shopItem;
	}

	public void setShopItem(ShopItem shopItem) {
		this.shopItem = shopItem;
	}
	
	public OverViewController getOverViewController() {
		return overViewController;
	}
	
	public void setOverViewController(OverViewController overViewController) {
		this.overViewController = overViewController;
	}
}
