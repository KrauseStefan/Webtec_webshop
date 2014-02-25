"use strict";

$(document).ready(function() {
	var scrolling = false;
	
	//Load shopping basket
	loadShoppingBasket();
	
	//Add scroll event listener
	$(window).scroll(function() {
		var set = $(document).scrollTop();
		
		if(scrolling && set <= 55)
		{
			$("#floatingHeader").removeClass("scrolling");
			scrolling = false;
		}
		
		else if(!scrolling && set > 55)
		{
			$("#floatingHeader").addClass("scrolling");
			scrolling = true;
		}

	});

});

var loadShoppingBasket = function() {
	/*var items = new Array();
	items[0] = "Eiffeltårnet,23.000.000";
	items[1] = "Rundetårn,23.000";
	sessionStorage["cart"] = JSON.stringify(items);*/
	
	var items = JSON.parse(sessionStorage["cart"]);
	var table = document.getElementById("shoppingCartList");
	var total = document.getElementById("total");
	var totalCount = 0;
	
	var row;
	var cell;
	
	for(var i = 0; i < items.length; i++)
	{
		row = table.insertRow(i);
		
		var content = items[i].split(",");
		for(var j = 0; j<content.length; j++)
		{
			cell = row.insertCell(j);
			
			if(j == 0)
				cell.innerHTML = content[j] + ": ";
			else
			{
				cell.innerHTML = content[j] + " kr.";
				totalCount += parseInt(content[j].replace(/\./g,''));
			}
		}
	}
	total.removeChild( total.firstChild );
	total.appendChild(document.createTextNode(totalCount + " kr."));
};