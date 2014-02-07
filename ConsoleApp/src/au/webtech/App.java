package au.webtech;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.jdom2.filter.Filters;
import org.jdom2.input.JDOMParseException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import org.jdom2.DocType;
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

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Document d = ValidateDocument(args);
		
		try {
			createItem(d);
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
					 new File(args[1])); 
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
	
	private static void SendDocumentToShop(HttpURLConnection connection, Document d) throws Exception {		
		DataOutputStream stream = new DataOutputStream(connection.getOutputStream());
		
		System.out.println("Writing to host");
		
		XPathFactory factory = XPathFactory.instance();
		
		Namespace ns = Namespace.getNamespace("w", namespaceUrl);
		XPathExpression<Element> expression = factory.compile("//w:itemName", Filters.element(), null, ns);
		Element element = expression.evaluateFirst(d);
		String itemName = element.getText();
		
		Element createItem = new Element("createItem", ns);
		Document doc = new Document(createItem);
		//doc.setDocType(new DocType());

		createItem.addContent((new Element("shopKey", ns)).setText(shopKey));
		createItem.addContent((new Element("itemName", ns)).setText(itemName));
		
		XMLOutputter out = new XMLOutputter();
		stream.writeChars(out.outputString(d));
		stream.flush();
		stream.close();
		
		int responseCode = connection.getResponseCode();
		
		System.out.println("ResponseCode: " + responseCode);
		System.out.println("Reading input:");
		
		BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String inputLine = "";
		StringBuffer response = new StringBuffer();
		
		while((inputLine = input.readLine()) != null)
		{
			response.append(inputLine);
		}
		
		input.close();
		System.out.println(inputLine);
	}
	
	private static void createItem(Document d) throws Exception {
		HttpURLConnection connection = (HttpURLConnection) new URL(baseUrl+createUrl).openConnection();
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);
		
		SendDocumentToShop(connection, d);
	}
	
	private static void modifyItem(Document d) throws Exception {
		HttpURLConnection connection = (HttpURLConnection) new URL(baseUrl+modifyUrl).openConnection();
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);
		
		SendDocumentToShop(connection, d);
	}
}
