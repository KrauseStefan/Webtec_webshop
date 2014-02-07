package au.webtech;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.jdom2.input.JDOMParseException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;
import org.jdom2.Document;

public class App {
	private final static String baseUrl = "http://services.brics.dk/java4/cloud";
	private final static String modifyUrl = "/modifyItem";
	private final static String createUrl = "/createItem";
	private final static String listUrl = "/listItems?shopID=194";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Document d = null;//ValidateDocument(args);
		
		try {
			SendDocumentToShop(d);
		} catch (Exception e) {
			// TODO: handle exception
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
	
	private static void SendDocumentToShop(Document d) throws Exception {
		
		HttpURLConnection connection = (HttpURLConnection) new URL(baseUrl).openConnection();
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);
		
		DataOutputStream stream = new DataOutputStream(connection.getOutputStream());
		
		System.out.println("Writing to host");
		
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
}
