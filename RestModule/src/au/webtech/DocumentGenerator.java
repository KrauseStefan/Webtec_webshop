package au.webtech;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.filter.Filters;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

/**
 * Class is responsible for generation of the different documents.
 */

public class DocumentGenerator {
	private final static String namespaceUrl = "http://www.cs.au.dk/dWebTek/2014";
	private final static String shopKey = "5247EFB974D2D4D06403F61B";
	private final static Namespace nsX = Namespace.getNamespace("x", namespaceUrl);
	
	public static Namespace getNS(String prefix){
		
		return Namespace.getNamespace(prefix, namespaceUrl);
	}
	
	public static Document createItemDocuemnt(String itemName){
		Element createItem = new Element("createItem", nsX);
		Document doc = new Document(createItem);

		createItem.addContent((new Element("shopKey", nsX)).setText(shopKey));
		createItem.addContent((new Element("itemName", nsX)).setText(itemName));
		
		return doc;
	}

	public static Document modifyItemDocuemnt(Document itemDoc, String itemID){
		Document _itemDoc = itemDoc.clone();
		Element root = _itemDoc.getRootElement();
		root.setName("modifyItem");
		root.addContent(1, (new Element("shopKey", nsX)).setText(shopKey));
		root.getChild("itemID", nsX).setText(itemID);
		root.removeChild("itemStock", nsX);
				
		return _itemDoc;	
	}	
	
	public static Document loginDocument(String customerName, String customerPass){
		Element createItem = new Element("login", nsX);
		Document doc = new Document(createItem);

		createItem.addContent((new Element("customerName", nsX)).setText(customerName));
		createItem.addContent((new Element("customerPass", nsX)).setText(customerPass));
		
		return doc;
	}
	
	
	public static Document sellItemsDocument(String itemID, String customerID){
		Element createItem = new Element("sellItems", nsX);
		Document doc = new Document(createItem);

		createItem.addContent((new Element("shopKey", nsX)).setText(shopKey));
		createItem.addContent((new Element("itemID", nsX)).setText(itemID));
		createItem.addContent((new Element("customerID", nsX)).setText(customerID));
		createItem.addContent((new Element("itemID", nsX)).setText(itemID));
		createItem.addContent((new Element("saleAmount", nsX)).setText(itemID));
		
		return doc;
	}

	
	public static Document createCustomerDocument(String customerName, String customerPass){
		Element createItem = new Element("createCustomer", nsX);
		Document doc = new Document(createItem);

		createItem.addContent((new Element("shopKey", nsX)).setText(shopKey));
		createItem.addContent((new Element("customerName", nsX)).setText(customerName));
		createItem.addContent((new Element("customerPass", nsX)).setText(customerPass));
		
		return doc;
	}
	
	
	public static Document adjustItemStockDocument(String ItemID, String adjustment){
		Element createItem = new Element("adjustItemStock", nsX);
		Document doc = new Document(createItem);

		createItem.addContent((new Element("shopKey", nsX)).setText(shopKey));
		createItem.addContent((new Element("itemID", nsX)).setText(ItemID));
		createItem.addContent((new Element("adjustment", nsX)).setText(adjustment));
		
		return doc;	
	}
	
	
	public static Document deleteItemDocument(String id){
		Element item = new Element("deleteItem", nsX);

		item.addContent((new Element("shopKey", nsX)).setText(shopKey));
		item.addContent((new Element("itemID", nsX)).setText(id));
		
		return new Document(item);
	}
	
	public static Document itemDocument(String id, String name, String url, String price, String stock, Element description){
		Element item = new Element("item", nsX);
						
		item.addContent((new Element("itemID", nsX)).setText(id));
		item.addContent((new Element("itemName", nsX)).setText(name));
		item.addContent((new Element("itemURL", nsX)).setText(url));
		item.addContent((new Element("itemPrice", nsX)).setText(price));
		item.addContent((new Element("itemStock", nsX)).setText(stock));
		item.addContent(description.clone());
		
		return new Document(item);
	}
	
	public static Document sellItemDocument(String itemID, String customerID, String saleAmount){
		Element sellItems = new Element("sellItems", nsX);
						
		sellItems.addContent((new Element("shopKey", nsX)).setText(shopKey))
			.addContent((new Element("itemID", nsX)).setText(itemID))
			.addContent((new Element("customerID", nsX)).setText(customerID))
			.addContent((new Element("saleAmount", nsX)).setText(saleAmount));
		
		return new Document(sellItems);
	}

	/**
	 * @param d
	 * @param exp
	 * @param ns
	 * @return Element
	 */
	public  static Element getItemUsingXpath(Document d, String exp, Namespace ns){
		XPathFactory factory = XPathFactory.instance();
		XPathExpression<Element> expression = factory.compile(exp, Filters.element(), null, ns);

		return expression.evaluateFirst(d);
	}	
	
	/**
	 * @param document
	 * @param xPathExp
	 * @param ns
	 * @return String
	 */
	public static String getItemValueUsingXpath(Document document, String xPathExp, Namespace ns){
		Element element = getItemUsingXpath(document, xPathExp, ns);

		if(element == null)
			return null;
	
		return element.getTextTrim();
	}

}
