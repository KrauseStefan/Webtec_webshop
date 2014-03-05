package au.webtech;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Class is responsible for login and logout.
 */

public class SessionStore {
	
	Map<String, String> sessionStoreMap = new LinkedHashMap<String, String>();
	
	
	public String createNewSessionID(String username){
		String sid = String.valueOf((int)(Math.random() * (20000 + 1)));
		sessionStoreMap.put(username, sid);
		return sid;
	}
	
	public String getSessionID(String username){
		return sessionStoreMap.get(username);
	}
	
	public Boolean clearSession(String username){
		return sessionStoreMap.remove(username) != null;
	}
	
}