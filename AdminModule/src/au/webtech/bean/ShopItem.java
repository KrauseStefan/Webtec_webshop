package au.webtech.bean;

import javax.faces.bean.ManagedBean;

@ManagedBean
public class ShopItem {
	private long itemID;
	private String itemName;
	private String itemUrl;
	private long itemPrice;
	private long itemStock;
	private String itemDescription;
	
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
