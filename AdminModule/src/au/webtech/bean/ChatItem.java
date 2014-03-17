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
public class ChatItem {
	public static final String USERID = "userID";
	public static final String MESSAGE = "message";

	private final static String namespaceUrl = "http://www.cs.au.dk/dWebTek/2014";
	private final static Namespace ns = Namespace
			.getNamespace("", namespaceUrl);

	private long userID;
	private String message;

	public ChatItem() {

	}

	public ChatItem(Element element) {

		setUserID(Long.parseLong(element.getChildText(ChatItem.USERID, ns)));
		setMessage(element.getChildText(ChatItem.MESSAGE, ns));
	}

	public long getUserID() {
		return userID;
	}

	public void setUserID(long userID) {
		this.userID = userID;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
