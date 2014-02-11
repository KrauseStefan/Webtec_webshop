package au.webtech;

import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;

public class CloudCon {

	public static int sendDocument(HttpURLConnection con, Document doc) throws Exception{
		DataOutputStream stream = new DataOutputStream(con.getOutputStream());
		new XMLOutputter().output(doc, stream);
		stream.flush();
		stream.close();
		
		//Receive response
		return con.getResponseCode();		
	}

	public static Document receiveDocument(HttpURLConnection con) throws Exception{
		return receiveDocument(con, null);
	}
	
	public static Document receiveDocument(HttpURLConnection con, File fileXSD) throws Exception{
		InputStream input = con.getInputStream();
				
		SAXBuilder xmlBuilder = new SAXBuilder();
//		b.setXMLReaderFactory(XMLReaders.XSDVALIDATING); //TODO Test me
		xmlBuilder.setValidation(true); 

		if(fileXSD != null){
			xmlBuilder.setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema"); 
			xmlBuilder.setProperty("http://java.sun.com/xml/jaxp/properties/schemaSource", fileXSD); 
		}
		Document respDoc = xmlBuilder.build(input);
		input.close();
		
		return respDoc;
	}
	
	public static HttpURLConnection createConnection(String url) throws Exception {
		HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);
		connection.setRequestProperty("Content-type", "text/xml");
		
		return connection;
	}

	
}
