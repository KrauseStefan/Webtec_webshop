package api;

import javax.ws.rs.GET;
import javax.ws.rs.Path; 

@Path("/service")
public class ItemResource {

	@GET 
	@Path("items") 
	public String GetItems() {
		return "Hello World!";
	}
}
