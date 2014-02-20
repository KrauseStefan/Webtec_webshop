package au.webtech.bean;

import java.net.HttpURLConnection;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

import org.jdom2.Document;

import au.webtech.CloudCon;
import au.webtech.DocumentGenerator;

@ManagedBean
@RequestScoped
public class ModifyController {
	public ModifyController() {
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

	public String modifyItem() throws Exception {
		HttpURLConnection connection = CloudCon.createConnection(CloudCon.MODIFY);
		
		Document modifiedDocument = DocumentGenerator.itemDocument(String.valueOf(shopItem.getItemID()), 
																	shopItem.getItemName(),
																	shopItem.getItemUrl(), 
																	String.valueOf(shopItem.getItemPrice()), 
																	String.valueOf(shopItem.getItemStock()), 
																	shopItem.getItemDescription());
		
		Document doc = DocumentGenerator.modifyItemDocuemnt(modifiedDocument, String.valueOf(this.getShopItem().getItemID()));
		

		CloudCon.sendDocument(connection, doc);
		
		overViewController.updateItems();
		
		return "overview?faces-redirect=true";
	}
}
