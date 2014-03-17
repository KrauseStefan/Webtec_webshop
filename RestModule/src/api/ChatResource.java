package api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.*;



@Path("/chat")
public class Chat {
	
	@Context HttpServletRequest session;
	
	public static HashMap<Integer, List<String>> chatsUser = new HashMap<Integer, List<String>>();
	public static HashMap<Integer, List<String>> chatsAdmin = new HashMap<Integer, List<String>>();
	
	//The get method for the user
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String receive(){
		final int timeout = 20000;
		final int refreshRate = 2000;
		long timeStart = System.currentTimeMillis();
		String message = "";

		Integer customerID = getUserID();
		
		while(System.currentTimeMillis() - timeStart < timeout && customerID != null)
		{
			List<String> messages = chatsUser.get(customerID);
			
			if(messages != null && 0 < messages.size())
			{
				for(String content: messages)
				{
					message += content + " ";
				}
				
				chatsUser.clear();
				
				return message;
			}
			
			sleep(refreshRate);
		}
		
		return message;
	}
	
	//The get method for the admin
	/*@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String receiveAll(){
		final int timeout = 20000;
		final int refreshRate = 2000;
		long timeStart = System.currentTimeMillis();
		String message = "";

		Integer customerID = getUserID();
		
		while(System.currentTimeMillis() - timeStart < timeout && customerID != null)
		{
			List<String> messages = chatsAdmin.get(customerID);
			
			if(messages != null && 0 < messages.size())
			{
				for(String content: messages)
				{
					message += content + " ";
				}
				
				chatsAdmin.clear();
				
				return message;
			}
			
			sleep(refreshRate);
		}
		
		return message;
	}*/
	
	@POST
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.TEXT_PLAIN)
	public boolean send(String message){
		
		Integer customerID = getUserID();
		
		if(customerID.equals(0))
		{
			return false;
		}
		
		persistMessage(chatsUser, customerID, message);
		persistMessage(chatsAdmin, customerID, message);
		return true;
	}
	
	private void persistMessage(HashMap<Integer, List<String>> chats, int customerID, String message)
	{
		if(!chats.isEmpty())
		{
			//If active chat session
			if(chats.containsKey(customerID))
			{
				chats.get(customerID).add(message);
			}
			
			//else create new session
			else
			{
				List<String> list = new ArrayList<String>();
				list.add(message);
				chats.put(customerID, list);
			}
		}
		
		else
		{
			chats.clear();
			
			List<String> list = new ArrayList<String>();
			list.add(message);
			chats.put(customerID, list);
		}
	}
	
	
	
	private int getUserID(){
		
		HttpSession hs = session.getSession();
		
		Object id = hs.getAttribute(LoginResource.USER_ID);
		
		if(id != null)
			return (int) id;
		
		return 0;
	}
	
	private void sleep(int milliseconds)
	{
		try {
			Thread.sleep(milliseconds);
		} 
		
		catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
