"use strict";

$(document).ready(function() {
	var scrolling = false;
	
	//createTestItems();
	loadItems();	
	
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


var loadShoppingBasket = function() {
	
	var items = JSON.parse(sessionStorage["cart"]);
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

var loadItems = function() {
	var url = 
	
	$.ajaxSetup({url:"http://localhost:8260/rest/service/items", type:"GET", success:function() {
    		alert( "success" );
		},
		error:function() {
			alert("Error!");
		},
		complete:function() {
			alert("Complete");
		}
  	});
  
  	$.ajax();
}
