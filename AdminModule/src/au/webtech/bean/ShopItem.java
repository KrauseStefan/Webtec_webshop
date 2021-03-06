package au.webtech.bean;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import javax.faces.bean.ManagedBean;

import org.jdom2.Content;
import org.jdom2.Content.CType;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.Text;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;

/**
 * Class is a model of a ShopItem.
 */

@ManagedBean
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

	@SuppressWarnings("deprecation")
	public void setItemDescription(String itemDescription)
			throws JDOMException, IOException, URISyntaxException {
		StringReader is = new StringReader(itemDescription);

		SAXBuilder sb = new SAXBuilder();
		sb.setValidation(true);
		
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		URL url = loader.getResource("../../xml/cloud.xsd");
		
		File file = new File(url.toURI());

		if(file != null){
			sb.setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema"); 
			sb.setProperty("http://java.sun.com/xml/jaxp/properties/schemaSource", file); 
		}
		
		Document dDoc = sb.build(is);

		Element dDocElm = dDoc.getRootElement().clone();

		this.descElm.removeContent();
		this.descElm.addContent(dDocElm);
	}

	private String parseXmlToHtml(List<Content> docElements) {
		String html = "";
		Iterator<Content> li = docElements.listIterator();
		while (li.hasNext()) {
			Content content = li.next();
			CType type = content.getCType();
			if (type == CType.Element) {
				Element element = (Element) content;
				if (element.getName().equals("bold"))
					html += "<b>";
				else if (element.getName().equals("italics"))
					html += "<i>";
				else if (element.getName().equals("list"))
					html += "<ul>";
				else if (element.getName().equals("item")){
					html = html.trim();
					html += "<li>";
				}

				html += parseXmlToHtml(element.getContent()).trim();

				if (element.getName().equals("bold"))
					html += "</b>";
				else if (element.getName().equals("italics"))
					html += "</i>";
				else if (element.getName().equals("list"))
					html += "</ul>";
				else if (element.getName().equals("item"))
					html += "</li>";

			} else if (type == CType.Text) {
				Text text = (Text) content;
				String str = text.getText();
				html += str;
			}
		}
		return html;
	}

	public String getFormattedDocument() {

		List<Content> docElements = descElm.getChild("document", ns)
				.getContent();
		String html = parseXmlToHtml(docElements).trim();
		html = html.replaceAll("\n", "<br>");
		return html;
	}

	public Element getItemDescriptionElm() {
		return descElm;
	}

}
