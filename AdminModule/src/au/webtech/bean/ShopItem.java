package au.webtech.bean;

import javax.faces.bean.ManagedBean;

import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.output.XMLOutputter;

@ManagedBean
public class ShopItem {
	public static final String ID = "itemID";
	public static final String NAME = "itemName";
	public static final String URL = "itemURL";
	public static final String PRICE = "itemPrice";
	public static final String STOCK = "itemStock";
	public static final String DESCRIPTION = "itemDescription";
	private final static String namespaceUrl = "http://www.cs.au.dk/dWebTek/2014";
	private final static Namespace nsX = Namespace.getNamespace("x", namespaceUrl);

	private long itemID;
	private String itemName;
	private String itemUrl;
	private long itemPrice;
	private long itemStock;
	private String itemDescription;
	
	public ShopItem(){
		
	}
	
	public ShopItem(Element element){
		
		Element descriptionElement = element.getChild(ShopItem.DESCRIPTION, nsX);
		
		String description =  new XMLOutputter().outputString(descriptionElement);
		
		setItemDescription(description);

		setItemID(Long.parseLong(element.getChildText(ShopItem.ID, nsX)));
		setItemPrice(Long.parseLong(element.getChildText(ShopItem.PRICE, nsX)));
		setItemStock(Long.parseLong(element.getChildText(ShopItem.STOCK, nsX)));
		setItemName(element.getChildText(ShopItem.NAME, nsX));
		setItemUrl(element.getChildText(ShopItem.URL, nsX));

	}

	public long getItemID() {
		return itemID;
	}
	public void setItemID(long itemID) {
		this.itemID = itemID;
	}
	
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	
	public String getItemUrl() {
		return itemUrl;
	}
	public void setItemUrl(String itemUrl) {
		this.itemUrl = itemUrl;
	}
	
	public long getItemPrice() {
		return itemPrice;
	}
	public void setItemPrice(long itemPrice) {
		this.itemPrice = itemPrice;
	}
	
	public long getItemStock() {
		return itemStock;
	}
	public void setItemStock(long itemStock) {
		this.itemStock = itemStock;
	}
	
	public String getItemDescription() {
		return itemDescription;
	}
	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}
}
