package au.webtech.bean;

/**
 * Class is a session scoped bean for storing the values of the ShopItem that is 
 * currently being adjusted, modified or deleted.
 */

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class CurrentItem extends ShopItem {
	
}
