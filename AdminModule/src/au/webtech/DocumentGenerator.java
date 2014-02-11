package au.webtech;

import java.net.HttpURLConnection;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.filter.Filters;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

public class DocumentGenerator {
	private final static String namespaceUrl = "http://www.cs.au.dk/dWebTek/2014";
	private final static String shopKey = "5247EFB974D2D4D06403F61B";
	private final static Namespace nsX = Namespace.getNamespace("x", namespaceUrl);
//	private final static Namespace ns = Namespace.getNamespace("", namespaceUrl);
	
	public static Document createItemDocuemnt(String itemName, Namespace ns){
		Element createItem = new Element("createItem", ns);
		Document doc = new Document(createItem);

		createItem.addContent((new Element("shopKey", ns)).setText(shopKey));
		createItem.addContent((new Element("itemName", ns)).setText(itemName));
		
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
	
	public  static Element getItemUsingXpath(Document d, String exp, Namespace ns){
		XPathFactory factory = XPathFactory.instance();
		XPathExpression<Element> expression = factory.compile(exp, Filters.element(), null, ns);

		return expression.evaluateFirst(d);
	}	
	
	/**
	 * Hacked version that assumes no namespace and then adds our namespace.
	 * 
	 * @param document
	 * @param xPathExp
	 * @return
	 */
	public static String getItemValueUsingXpath(Document document, String xPathExp){
		xPathExp.replaceAll("\\/", "/x:");
		xPathExp.replaceAll("\\//", "//x:");

		return getItemValueUsingXpath(document, xPathExp, nsX);		
	}
	
	public static String getItemValueUsingXpath(Document document, String xPathExp, Namespace ns){
		Element element = getItemUsingXpath(document, xPathExp, ns);
		return element.getTextTrim();	
	}

}
