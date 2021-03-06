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
	private final static String sellUrl = "/sellItems";
	private final static String createUrl = "/createItem";
	private final static String adjustUrl = "/adjustItemStock";
	private final static String deleteUrl = "/deleteItem";
	private final static String loginUrl = "/login";
	private final static String listUrl = "/listItems?shopID=194";
	private final static String listDeletedUrl = "/listDeletedItemIDs?shopID=194";
	private final static String listSoldUrl = "/listShopSales?shopKey=5247EFB974D2D4D06403F61B";
	private final static String createCustomerUrl = "/createCustomer";

	public final static int MODIFY = 0;
	public final static int CREATE = 1;
	public final static int ADJUST = 2;
	public final static int LIST = 3;
	public final static int DELETE = 4;
	public final static int LISTDELETED = 5;
	public final static int SELL = 6;
	public final static int LOGIN = 7;
	public final static int CREATE_CUSTOMER = 8;
	public final static int LIST_SOLD = 9;	
	
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
		case CREATE_CUSTOMER:
			url = url + createCustomerUrl;
			break;
		case SELL:
			url = url + sellUrl;
			break;
		case LOGIN:
			url = url + loginUrl;
			break;
		case LIST_SOLD:
			url += listSoldUrl;
			break;
		default:
			break;
		}
				
		HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
		connection.setDoOutput(true);
		connection.setRequestProperty("Content-type", "text/xml");
		
		if (mode == LIST || mode == LISTDELETED || mode == LIST_SOLD) {
			connection.setRequestMethod("GET");
		}
		else {
			connection.setRequestMethod("POST");
		}
		
		return connection;
	}
}
