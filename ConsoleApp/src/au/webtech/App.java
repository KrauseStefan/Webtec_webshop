package au.webtech;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.jdom2.filter.Filters;
import org.jdom2.input.JDOMParseException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.input.sax.XMLReaders;
import org.jdom2.output.XMLOutputter;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;

public class App {
	private final static String baseUrl = "http://services.brics.dk/java4/cloud";
	private final static String modifyUrl = "/modifyItem";
	private final static String createUrl = "/createItem";
	private final static String listUrl = "/listItems?shopID=194";
	private final static String shopKey = "5247EFB974D2D4D06403F61B";
	private final static String namespaceUrl = "http://www.cs.au.dk/dWebTek/2014";
		
	private static File fileXSD = null; 
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		fileXSD = new File(args[1]);

		Document d = ValidateDocument(args);		
		String rootName = d.getRootElement().getName();
		
		
		if(rootName != "item") {
			System.out.println("Root element is not 'item'!");
			return;
		}
		
		try {
			modifyItem(d);
		} catch (Exception e) {
			e.printStackTrace(); 
		}
	 }
	
	
	private static Document ValidateDocument(String[] args) {
		try { 
			 SAXBuilder b = new SAXBuilder(); 
			 b.setValidation(true); 
			 b.setProperty( 
					 "http://java.sun.com/xml/jaxp/properties/schemaLanguage", 
					 "http://www.w3.org/2001/XMLSchema"); 
			 b.setProperty( 
					 "http://java.sun.com/xml/jaxp/properties/schemaSource", 
					 fileXSD); 
			 String msg = "No errors!"; 
			 try { 
				 Document d = b.build(new File(args[0])); 
				 System.out.println(msg);
				 return d;
			 } catch (JDOMParseException e ) { 
				 msg = e.getMessage(); 
			 } 
			 System.out.println(msg); 
		 } catch (Exception e) { 
			 e.printStackTrace(); 
		 } 
		 return null;
	}

	
	private static Element getItemUsingXpath(Document d, String exp, Namespace ns){
		XPathFactory factory = XPathFactory.instance();
		XPathExpression<Element> expression = factory.compile(exp, Filters.element(), null, ns);
		return expression.evaluateFirst(d);

	}	
	private static String getItemValueUsingXpath(Document d, String exp, Namespace ns){
		Element element = getItemUsingXpath(d, exp, ns);
		return element.getTextTrim();	
	}
	
	private static Document createItemDocuemnt(String itemName, Namespace ns){
		Element createItem = new Element("createItem", ns);
		Document doc = new Document(createItem);

		createItem.addContent((new Element("shopKey", ns)).setText(shopKey));
		createItem.addContent((new Element("itemName", ns)).setText(itemName));
		
		return doc;
	}
	
	private static Document modifyItemDocuemnt(Document item, String itemID, Namespace ns){		
		Element root = item.getRootElement();
		root.setName("modifyItem");
		root.addContent(1, (new Element("shopKey", ns)).setText(shopKey));
		root.getChild("itemID", ns).setText(itemID);
		root.removeChild("itemStock", ns);
		
//		Element createItem = new Element("modifyItem", ns);
//		Document doc = new Document(createItem);

//		createItem.addContent((new Element("shopKey", ns)).setText(shopKey));
//		createItem.addContent((new Element("itemID", ns)).setText(itemName));
//		createItem.addContent((new Element("itemName", ns)).setText(itemName));
//		createItem.addContent((new Element("itemPrice", ns)).setText(itemName));
//		createItem.addContent((new Element("itemURL", ns)).setText(itemName));
//		createItem.addContent((new Element("itemDescription", ns)).setText(itemName));
		
		return item;	
	}
	
	private static int sendDocument(HttpURLConnection con, Document doc) throws IOException{
		DataOutputStream stream = new DataOutputStream(con.getOutputStream());
		new XMLOutputter().output(doc, stream);
		stream.flush();
		stream.close();
		
		//Receive response
		return con.getResponseCode();		
	}
	
	private static Document resiveDocument(HttpURLConnection con) throws Exception{
		InputStream input = con.getInputStream();
				
		SAXBuilder b = new SAXBuilder();
//		b.setXMLReaderFactory(XMLReaders.XSDVALIDATING); //TODO Test me
		b.setValidation(true); 

		b.setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema"); 
		b.setProperty("http://java.sun.com/xml/jaxp/properties/schemaSource", fileXSD); 

		Document respDoc = b.build(input);
		input.close();
		
		return respDoc;
	}
	
	private static void SendDocumentToShop(HttpURLConnection connection, Document d) throws Exception {		
		
		System.out.println("Writing to host");
		
		Namespace ns = Namespace.getNamespace("", namespaceUrl);
		Namespace nsW = Namespace.getNamespace("w", namespaceUrl);
		
		String itemName = getItemValueUsingXpath(d, "//w:itemName", nsW);
		
		Document createDoc = createItemDocuemnt(itemName, ns);
						
		int responseCode = sendDocument(connection, createDoc);
		
		System.out.println("ResponseCode: " + responseCode);
		
		Document respDoc = resiveDocument(connection);
		 
		String itemID = getItemValueUsingXpath(respDoc, "//w:itemID", nsW);	
		System.out.println("Recived ID: " + itemID);
		
		Document modifyDoc = modifyItemDocuemnt(d, itemID, ns);

		
		connection.disconnect();
		connection = createConnection(baseUrl+modifyUrl);
		responseCode = sendDocument(connection, modifyDoc);
		
		if (!(responseCode >= 200 && responseCode < 300))
			throw new Exception("Faild to update item");
			
	}
	
	private static void createItem(Document d) throws Exception {
		HttpURLConnection connection = (HttpURLConnection) new URL(baseUrl+createUrl).openConnection();
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);
		connection.setRequestProperty("Content-type", "text/xml");
		
		SendDocumentToShop(connection, d);
	}

	private static HttpURLConnection createConnection(String url) throws Exception {
		HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);
		connection.setRequestProperty("Content-type", "text/xml");
		
		return connection;
	}

	
	private static void modifyItem(Document d) throws Exception {
		HttpURLConnection connection = (HttpURLConnection) new URL(baseUrl+modifyUrl).openConnection();
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);
		
		SendDocumentToShop(connection, d);
	}
}
