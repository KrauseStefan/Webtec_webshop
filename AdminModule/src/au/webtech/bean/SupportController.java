package au.webtech.bean;

import java.net.HttpURLConnection;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.jdom2.Document;
import org.jdom2.Element;

import com.sun.corba.se.impl.protocol.giopmsgheaders.Message;

import au.webtech.CloudCon;

/**
 * Class is responsible for the customer support
 */

@ManagedBean
@SessionScoped
public class SupportController {
	
	private List<ChatItem> chats;
	private ChatItem chat;
	private static HttpURLConnection chatConnection;
	
	public SupportController() throws Exception {
		this.chats = new ArrayList<ChatItem>();
		this.updateChatMessages();
	}
	
	public void updateChatMessages() throws Exception {
		
		chatConnection = CloudCon.createConnection(CloudCon.SUPPORT);
		
		ScheduledExecutorService updateService = Executors.newSingleThreadScheduledExecutor();
		ScheduledFuture scheduledFuture = updateService.scheduleAtFixedRate(new Runnable() {
			
			@Override
			public void run() {
				try
				{
					Document itemDoc = CloudCon.receiveDocument(chatConnection);
					
					Iterator<Element> it = itemDoc.getRootElement().getChildren().iterator();
					
					while(it.hasNext()) {			
						Element element = it.next();
						chats.add(new ChatItem(element));
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}, 1000, 2000, TimeUnit.MILLISECONDS);
	}
	
	public ChatItem getChat() {
		return chat;
	}

	public void setChat(ChatItem chat) {
		this.chat = chat;
	}
	
	public List<ChatItem> getChats() throws Exception {
		return chats;
	}
	public void setItemID(List<ChatItem> itemList) {
		this.chats = itemList;
	}
}
