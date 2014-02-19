package au.webtech.bean;

import java.net.HttpURLConnection;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import org.jdom2.Document;

import au.webtech.CloudCon;
import au.webtech.DocumentGenerator;

@ManagedBean
public class CreateController {
	public CreateController() {
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
	
	public String createItem() throws Exception {
		HttpURLConnection connection = CloudCon.createConnection(CloudCon.CREATE);
		Document doc = DocumentGenerator.createItemDocuemnt(shopItem.getItemName());
		
		CloudCon.sendDocument(connection, doc);
		
		overViewController.updateItems();
		
		return "overview?faces-redirect=true";
	}
}
