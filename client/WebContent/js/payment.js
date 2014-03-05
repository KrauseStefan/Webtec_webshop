"use strict";

function pay()
{
	var data = sessionStorage["cart"];
	if(data == undefined)
	{
		alert("Error. No items to buy");
		return;
	}
	
	var items = JSON.parse(data);
	var sellItems = [];
	
	for(var i = 0; i < items.length; i++)
	{
		var sellItem = {
			shopKey: 194,
			itemID: items[i].itemID,
			customerID: 381,
			saleAmount: items[i].itemQuantity
		};
		
		sellItems.push(sellItem);
	}
	
	$.ajax({
		type: "POST",
		url: "/rest/pay",
		contentType: "application/json",
		data: JSON.stringify(sellItems)
	})
  	.done(function( msg ) {
		sessionStorage.removeItem("cart");
		loadShoppingBasket();
    	alert("Payment completed");
  	})
	.fail(function( jqXHR, textStatus ) {
  		alert( "Request failed: " + textStatus );
	});
}