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
			itemID: items[i].itemID,
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
  	.done(function( isLoggedin ) {
		if(!isLoggedin){
			alert('You are not logged in please do so.')
			return;
		}
		
		sessionStorage.removeItem("cart");
		loadShoppingBasket();
		$.getJSON('/rest/items', updateItemTable);
//    	alert("Payment completed");
  	})
	.fail(function( jqXHR, textStatus ) {
  		alert( "Request failed: " + textStatus );
	});
}