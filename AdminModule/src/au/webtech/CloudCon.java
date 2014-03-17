package au.webtech;

import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;

/**
 * Class is responsible for handling of http connection to the cloud server.
 */

public class CloudCon {
	private final static String baseUrl = "http://services.brics.dk/java4/cloud";
	private final static String modifyUrl = "/modifyItem";
	private final static String createUrl = "/createItem";
	private final static String adjustUrl = "/adjustItemStock";
	private final static String deleteUrl = "/deleteItem";
	private final static String listUrl = "/listItems?shopID=194";
	private final static String listDeletedUrl = "/listDeletedItemIDs?shopID=194";
	private final static String supportUrl = "/chat";

	public final static int MODIFY = 0;
	public final static int CREATE = 1;
	public final static int ADJUST = 2;
	public final static int LIST = 3;
	public final static int DELETE = 4;
	public final static int LISTDELETED = 5;
	public final static int SUPPORT = 6;
	
	public static int sendDocument(HttpURLConnection con, Document doc) throws Exception{		
		DataOutputStream stream = new DataOutputStream(con.getOutputStream());
		new XMLOutputter().output(doc, stream);
		stream.flush();
		stream.close();
		
		//Receive response
		return con.getResponseCode();		
	}
	
	@SuppressWarnings("deprecation")
	public static Document receiveDocument(HttpURLConnection con) throws Exception{
		InputStream input = con.getInputStream();
				
		SAXBuilder xmlBuilder = new SAXBuilder();
		xmlBuilder.setValidation(true);
				
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		URL url = loader.getResource("../../xml/cloud.xsd");
		
		File file = new File(url.toURI());

		if(file != null){
			xmlBuilder.setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema"); 
			xmlBuilder.setProperty("http://java.sun.com/xml/jaxp/properties/schemaSource", file); 
		}
		
		Document respDoc = xmlBuilder.build(input);
		input.close();
		
		return respDoc;
	}
	
	public static HttpURLConnection createConnection(int mode) throws Exception {
		String url = baseUrl;
		
		switch (mode) {
		case MODIFY:
			url = url + modifyUrl;
			break;
		case CREATE:
			url = url + createUrl;
			break;
		case ADJUST:
			url = url + adjustUrl;
			break;
		case LIST:
			url = url + listUrl;
			break;
		case DELETE:
			url = url + deleteUrl;
			break;
		case LISTDELETED:
			url = url + listDeletedUrl;
			break;
		default:
			break;
		}
				
		HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
		connection.setDoOutput(true);
		connection.setRequestProperty("Content-type", "text/xml");
		
		if (mode == LIST || mode == LISTDELETED) {
			connection.setRequestMethod("GET");
		}
		else {
			connection.setRequestMethod("POST");
		}
		
		return connection;
	}
}
