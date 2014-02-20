package au.webtech.bean;

import java.net.HttpURLConnection;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import org.jdom2.Document;

import au.webtech.CloudCon;
import au.webtech.DocumentGenerator;

/**
 * Class is responsible for the create view and creation of new items.
 */

@ManagedBean
public class CreateController {
	@ManagedProperty(value="#{overViewController}") 
	private OverViewController overViewController;
	private ShopItem shopItem;

	public CreateController() {
		this.shopItem = new ShopItem();
	}
	
	public String createItem() throws Exception {
		HttpURLConnection connection = CloudCon.createConnection(CloudCon.CREATE);
		Document doc = DocumentGenerator.createItemDocuemnt(shopItem.getItemName());
		
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
