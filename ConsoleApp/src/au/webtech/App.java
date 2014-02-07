package au.webtech;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import javax.swing.text.Document;

import org.jdom2.input.JDOMParseException;
import org.jdom2.input.SAXBuilder;

public class App {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Document d = ValidateDocument(args);
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
				 Document d = (Document) b.build(new File(args[0])); 
				 return d;
			 } catch (JDOMParseException e ) { 
				 msg = e.getMessage(); 
			 } 
			 System.out.println(msg); 
		 } catch (Exception e) { e.printStackTrace(); } 
		 return null;
	}
	
	private static void SendDocumentToShop(Document d) {
		
	}
}
