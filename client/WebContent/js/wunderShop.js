"use strict";

$(document).ready(function() {
	var scrolling = false;
	
	//createTestItems();
	$.getJSON('/rest/items', updateItemTable);
	
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

function createTestItems (){
	var data = [
  {
    "itemID": 334,
    "itemName": "Stone Henge",
    "itemPrice": 1337000,
    "itemQuantity": 1
  },{
    "itemID": 1510,
    "itemName": "Triumf buen",
    "itemPrice": 10100,
    "itemQuantity": 2
  }
  ]
  
  data = JSON.stringify(data);
  
  window.sessionStorage['cart'] = data;
  
}


function loadShoppingBasket() {
	var data = sessionStorage["cart"];
	if(data == undefined)
		return;
	
	var items = JSON.parse(data);
	var table = $("#shoppingCartList");
	
	table.empty();
	
	var row;
	var cell;
	var rows = [];
	
	function uptateTotal(items){
		var total = eval(items.map(function(item){return item.itemPrice * item.itemQuantity;}).join('+'))
		$("#total").text(total)
	}
	
	for(var i = 0; i < items.length; i++){
		var item = items[i];
		if(item.itemQuantity <= 0) continue;
		var row = '<tr>' +
				'<td>' + item.itemName + '</td>' +
				'<td class="CartItemPrice">' + item.itemPrice +'</td>' +
				'<td class="CartItemQuantity">' + item.itemQuantity +'</td>' +
				'<td> '+
					'<button class="cartPlusOne">+</button>'+
					'<button class="cartMinusOne">-</button>'+
				'</td>' +
			'</tr>';
		
		var row = $($.parseHTML(row));
		
		row.find('button').on('click', $.proxy(function(row, item, event){
			var button = $(event.toElement);
			if(button.hasClass('cartPlusOne'))
				item.itemQuantity++;
			else if(button.hasClass('cartMinusOne'))
				item.itemQuantity--;
			
			window.sessionStorage['cart'] = JSON.stringify(items);
			uptateTotal(items);
			if(item.itemQuantity <= 0){
				row.remove();
				return;
			}else{
			row.find('.CartItemQuantity').text(item.itemQuantity);
			}

		}, this, row, item));
		
		table.append(row);
	}
	uptateTotal(items);
	
};

function updateItemTable(data){
	var table = $('#itemsTable');
	
	for(var i = 0; i < data.length; i++){
		var d = data[i];
		var row = "<tr>" + 
			"<td>" +
				d.itemName +
			"</td>" +
		"</tr>";
		
		table.append($(row));
	}
	
}

