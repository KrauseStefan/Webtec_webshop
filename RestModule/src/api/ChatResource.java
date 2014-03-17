package api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import com.owlike.genson.Genson;
import com.owlike.genson.TransformationException;

@Path("/chat")
public class ChatResource {

	@Context
	HttpServletRequest session;

	public static Map<Integer, Map<String, Object>> chatsUser = new HashMap<Integer, Map<String, Object>>();
	public static Map<Integer, Map<String, Object>> chatsAdmin = new HashMap<Integer, Map<String, Object>>();

	public static final String JSON_MESSAGE_ARRAY = "messages";

	private static final String JSON_USERNAME = "username";
	
	// The get method for the user
	@SuppressWarnings({ "unchecked", "null" })
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String receive(@DefaultValue("false") @QueryParam("admin") boolean admin) throws TransformationException, IOException, InterruptedException {

		final int TIMEOUT_MS = 20000;
		final int REFRESH_RATE_MS = 2000;
		long timeStart = System.currentTimeMillis();
		
		String json;
		Map<String, Object> userObj = null;
		List<String> userMsgList = null;
		if (!admin) {
			if (getUserID() == 0)
				return "{\"error\":true}";

			userObj = (Map<String, Object>) chatsUser.get(getUserID());
			
			if(userObj == null)
				userMsgList = (List<String>) userObj.get(JSON_MESSAGE_ARRAY);
			else{
				userMsgList = new LinkedList<>();
				userObj.put(JSON_MESSAGE_ARRAY, userMsgList);
				userObj.put(JSON_USERNAME, getUserName());
			}
//			if (userObj == null){
//				chatsUser.put(getUserID(), new ArrayList<String>());
//				userObj = chatsUser.get(getUserID());
//			}
			
		}

		while (System.currentTimeMillis() - timeStart < TIMEOUT_MS) {

			if (!admin) {
				synchronized (chatsUser) {
					if (userMsgList.size() > 0) {
						json = new Genson().serialize(userObj);
						userMsgList.clear();
						return json;
					}
				}
			} else {
				synchronized (chatsAdmin) {
					if (chatsAdmin.size() > 0) {
						json = new Genson().serialize(chatsAdmin);
						chatsAdmin.clear();
						return json;
					}
				}
			}

			Thread.sleep(REFRESH_RATE_MS);
		}

		return "{}";
	}

	@POST
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.TEXT_PLAIN)
	public boolean send(String message, @DefaultValue("-1") @QueryParam("id") int id) {

		Integer customerID = getUserID();
		if (id == -1) { // admin message
			if (customerID.equals(0)) {
				return false;
			}
			persistMessage(chatsAdmin, customerID, message);

		} else { //normal user message
			persistMessage(chatsUser, customerID, message);
		}

		return true;
	}

	@SuppressWarnings("unchecked")
	private void persistMessage(Map<Integer, Map<String, Object>> chats, int customerID, String message) {

		// If active chat session
		synchronized (chats) {
			if (chats.containsKey(customerID)) {
				List<String> messages = (List<String>) chats.get(customerID).get(JSON_MESSAGE_ARRAY);
				messages.add(message);
			}
			// else create new session
			else {
				Map<String, Object> userObj = new LinkedHashMap<String, Object>();				
				List<String> list = new ArrayList<String>();
				
				list.add(message);
				userObj.put(JSON_MESSAGE_ARRAY, list);
				userObj.put(JSON_USERNAME, getUserName());
				chats.put(customerID, userObj);
			}
		}
	}

	private int getUserID() {

		HttpSession hs = session.getSession();

		Object id = hs.getAttribute(LoginResource.USER_ID);

		if (id != null)
			return (int) id;

		return 0;
	}
	
	private String getUserName() {

		HttpSession hs = session.getSession();

		String userName = (String) hs.getAttribute(LoginResource.USER_NAME);

		if (userName != null)
			return userName;

		return "Chat Support";
	}
}
