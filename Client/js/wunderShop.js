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
	//item = [name, cost, quantity]
	/*var items = new Array();
	items[0] = "Eiffeltårnet,23.000.000,2";
	items[1] = "Rundetårn,23.000,1";
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
		row.className = "cartItem";
		
		var content = items[i].split(",");
		
		//Add content to rows
		for(var j = 0; j<content.length; j++)
		{
			cell = row.insertCell(j);
			
			if(j == 0)
				cell.innerHTML = content[j] + ": ";
			else if (j == 1)
			{
				cell.innerHTML = content[j] + " kr.";
			}
			
			else
			{
				cell.innerHTML = content[j];
				totalCount += parseInt(content[j-1].replace(/\./g,'')) * parseInt(content[j].replace(/\./g,''));
			}
		}
		
		//Add modify bottons
		cell = row.insertCell(content.length);
		cell.innerHTML = "<input type=\"button\" value=\"-\" onclick=\"function(){updateShoppingBasket(\"-\", $(this).closest(\".cartItem\"));};\"/>";
		cell = row.insertCell(content.length+1);
		cell.innerHTML = "<input type=\"button\" value=\"+\" class=\"incrementButton\"/>";
		
		$(".incrementButton").click(function(){
			alert($(this).closest('tr').attr('id'));
		});
	}
	total.removeChild(total.firstChild);
	total.appendChild(document.createTextNode(totalCount + " kr."));
};

var incrementItem = function(){
	updateShoppingBasket("+", $(this).closest(".row-of-data"));
};

var updateShoppingBasket = function(mode, content) {
	var items = JSON.parse(sessionStorage["cart"]);
	var row = $(this).closest(".cartItem");
	var newContent = items[row.rowIndex].split(",");
	
	if(mode == "-")
	{
		newContent[2] = parseInt(newContent[2]) - 1;
	}
	
	else
	{
		newContent[2] = parseInt(newContent[2]) + 1;
	}
	
	items[row.rowIndex] = newContent;
	sessionStorage["cart"] = JSON.stringify(items);
	
	loadShoppingBasket();
};