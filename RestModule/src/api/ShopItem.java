package api;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.URISyntaxException;
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
 * Class is a model of a ShopItem.
 */

public class ShopItem {
	public static final String ID = "itemID";
	public static final String NAME = "itemName";
	public static final String URL = "itemURL";
	public static final String PRICE = "itemPrice";
	public static final String STOCK = "itemStock";
	public static final String DESCRIPTION = "itemDescription";

	private final static String namespaceUrl = "http://www.cs.au.dk/dWebTek/2014";
	private final static Namespace ns = Namespace
			.getNamespace("", namespaceUrl);

	private long itemID;
	private String itemName;
	private String itemUrl;
	private long itemPrice;
	private long itemStock;

	private Element descElm;

	public ShopItem() {

	}

	public ShopItem(Element element) {

		this.descElm = element.getChild(ShopItem.DESCRIPTION, ns);

		setItemID(Long.parseLong(element.getChildText(ShopItem.ID, ns)));
		setItemPrice(Long.parseLong(element.getChildText(ShopItem.PRICE, ns)));
		setItemStock(Long.parseLong(element.getChildText(ShopItem.STOCK, ns)));
		setItemName(element.getChildText(ShopItem.NAME, ns));
		setItemUrl(element.getChildText(ShopItem.URL, ns));
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
		return new XMLOutputter().outputElementContentString(descElm);
	}

	public Element itemDescriptionElm() {
		return descElm;
	}
}
