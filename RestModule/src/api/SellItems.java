package api;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import org.jdom2.Content;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.Text;
import org.jdom2.Content.CType;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;

/**
 * Class is a model of a SellItem.
 */

public class SellItems {
	public static final String ID = "itemID";
	public static final String SHOPKEY = "shopKey";
	public static final String SALEAMOUNT = "saleAmount";
	public static final String CUSTOMERID = "customerID";

	private final static String namespaceUrl = "http://www.cs.au.dk/dWebTek/2014";
	private final static Namespace ns = Namespace.getNamespace("", namespaceUrl);

	private long shopKey;
	private int saleAmount;
	private long itemID;
	private long customerID;

	public SellItems() {

	}

	public SellItems(Element element) {
		setItemID(Long.parseLong(element.getChildText(SellItems.ID, ns)));
		setShopKey(Long.parseLong(element.getChildText(SellItems.SHOPKEY, ns)));
		setSaleAmount(Integer.parseInt(element.getChildText(SellItems.SALEAMOUNT, ns)));
		setCustomerID(Long.parseLong(element.getChildText(SellItems.CUSTOMERID, ns)));
	}

	public long getShopKey() {
		return shopKey;
	}

	public void setShopKey(long shopKey) {
		this.shopKey = shopKey;
	}

	public int getSaleAmount() {
		return saleAmount;
	}

	public void setSaleAmount(int saleAmount) {
		this.saleAmount = saleAmount;
	}
	
	public long getItemID() {
		return itemID;
	}

	public void setItemID(long itemID) {
		this.itemID = itemID;
	}
	
	public long getCustomerID() {
		return customerID;
	}

	public void setCustomerID(long customerID) {
		this.customerID = customerID;
	}
}
