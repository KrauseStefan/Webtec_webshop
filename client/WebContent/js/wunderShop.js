"use strict";

function copyObj(obj){
	var newObj = {};
	for(var i in obj){
		newObj[i] = obj[i];
	}
	
	return newObj;
}

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
	
	var alreadyObserving = false;
		
	$('.headder2').on('dragenter', function(e){
  		this.classList.add('dragover');
		
		if(!alreadyObserving){
			$(document.body).one('dragenter', function(e){
				$('.headder2').removeClass('dragover');
				alreadyObserving = false;
			})
			alreadyObserving = true;
		}
		
		return false;
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
	var table = $("#shoppingCartList");
	table.empty();
	
	var data = sessionStorage["cart"];
	
	if(data == undefined)
	{
		$("#total").text(0);
		return;
	}
	
	var items = JSON.parse(data);
	
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
				if(item.itemQuantity < item.itemStock)
					item.itemQuantity++;
				else
				{
					alert("Sorry. No more items on stock.");
				}
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

function addToCart(item)
{
	var data = sessionStorage["cart"];
	
	var items = [];
	if(data != undefined)
		items = JSON.parse(data);
	
	
	var dataReady = false;
	
	for(var i = 0; i < items.length; i++)
	{
		if(items[i].itemID == item.itemID)
		{
	 		var cartItem = items[i];
			
			if(cartItem.itemQuantity < item.itemStock)
				cartItem.itemQuantity += 1;
			
			else
			{
				alert("Sorry. No more items on stock.");
			}
			
			dataReady = true;
				
			break;
		}
	}
	
	if(!dataReady && item.itemStock > 0)
	{
		item.itemQuantity = 1;
		items.push(item);
	}
	
	window.sessionStorage['cart'] = JSON.stringify(items);
	loadShoppingBasket();
}

function updateItemTable(data){
	var table = $('#itemsTable');
	table.empty();
	
	for(var i = 0; i < data.length; i++){
		var d = data[i];

		var row = '<tr><td>' +
		'<div draggable="true" class="itemRow">' + 
			'<div><img class="mediumImage" alt="item_image" src="' +
				d.itemUrl +
			'"/></div>' +
				
			'<div>' +
				d.itemName +
				'<br />' +
				d.itemDescriptionHTML + 
			'</div>' +
			
			'<div>' +
				d.itemPrice +
			'</div>' +
			
			'<div>' +
				d.itemStock + " left" +
			'</div>' +
			
			'<div>' +
				'<button class="addToCart">Add</button>'+
			'</div>' +
		'</div></td></tr>';
		
		var row = $($.parseHTML(row));
		var item = copyObj(data[i]);
		
		row.find('button.addToCart').on('click', $.proxy(function(row, item, event){
			addToCart(item);
		}, this, row, item))
		
		row.find('.itemRow').on('dragstart', function(e){
			this.style.opacity = '0.4';
		}).on('dragover', function(e){
			if (e.preventDefault) {
				e.preventDefault(); // Necessary. Allows us to drop.
			}
	
			e.originalEvent.dataTransfer.dropEffect = 'move';  // See the section on the DataTransfer object.
	
			return false;
		}).on('dragend', $.proxy(function(item, e){
			this.style.opacity = '1';
			if($('.headder2').hasClass('dragover')){
				$('.dragover').removeClass('dragover');	
				console.log("droped");
				addToCart(item);
			}
		}, null, item));
		
		table.append($(row));
	}
	
	$('[draggable]')
	
}