package api;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.owlike.genson.Genson;

import au.webtech.CloudCon;

@Path("/sales")
public class soldItemsResource {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Object getShopSales() throws Exception {

		HttpURLConnection con = CloudCon.createConnection(CloudCon.LIST_SOLD);

		InputStream is = con.getInputStream();
		
		JAXBContext jc = JAXBContext.newInstance(Sales.class);
		Unmarshaller unmarshaller = jc.createUnmarshaller();				
		List<Sale> sales = ((Sales) unmarshaller.unmarshal(is)).getSale();
		Genson genson = new Genson();
		String json = genson.serialize(sales);

		return json;
	}
}

@XmlRootElement(namespace = "http://www.cs.au.dk/dWebTek/2014")
class Sales {

	@XmlElement(namespace = "http://www.cs.au.dk/dWebTek/2014")
	public List<Sale> getSale() {
		return sale;
	}

	public void setSale(List<Sale> sale) {
		this.sale = sale;
	}

	List<Sale> sale;
}

class Sale {
	@XmlElement(namespace = "http://www.cs.au.dk/dWebTek/2014")
	public long getSaleTime() {
		return saleTime;
	}

	public void setSaleTime(long saleTime) {
		this.saleTime = saleTime;
	}

	@XmlElement(namespace = "http://www.cs.au.dk/dWebTek/2014")
	public int getSaleAmount() {
		return saleAmount;
	}

	public void setSaleAmount(int saleAmount) {
		this.saleAmount = saleAmount;
	}

	@XmlElement(namespace = "http://www.cs.au.dk/dWebTek/2014")
	public int getShopID() {
		return shopID;
	}

	public void setShopID(int shopID) {
		this.shopID = shopID;
	}

	@XmlElement(namespace = "http://www.cs.au.dk/dWebTek/2014")
	public int getItemID() {
		return itemID;
	}

	public void setItemID(int itemID) {
		this.itemID = itemID;
	}

	@XmlElement(namespace = "http://www.cs.au.dk/dWebTek/2014")
	public int getCustomerID() {
		return customerID;
	}

	public void setCustomerID(int customerID) {
		this.customerID = customerID;
	}

	@XmlElement(namespace = "http://www.cs.au.dk/dWebTek/2014")
	public int getSaleItemPrice() {
		return saleItemPrice;
	}

	public void setSaleItemPrice(int saleItemPrice) {
		this.saleItemPrice = saleItemPrice;
	}

	private long saleTime;
	private int saleAmount;
	private int shopID;
	private int itemID;
	private int customerID;
	private int saleItemPrice;

}

