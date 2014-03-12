"use strict";

var updateChat = requestMessages;

function sendMessage()
{
	var message = $("#chatYouSay").val();
	$("#chatYouSay").val("");
	
	if(message != "")
	{
		$.ajax({
			type: "POST",
			url: "/rest/chat",
			contentType: "text/plain",
			data: message
		})
		.fail(function( jqXHR, textStatus ) {
			alert("Error sending :(");
		});
	}
	
	else
	{
		alert("Please write a message before sending.");
	}
}

function requestMessages()
{
	$.ajax({
		type: "GET",
		url: "/rest/chat"
	})
  	.success(function( message ) {
		if(message.length > 0)
			$("#chatMessages").append("<p>"+ message +"</p>");
		requestMessages();
  	})
	.fail(function( jqXHR, textStatus ) {
		$("#chatMessages").text(":( the chat is down...");
		requestMessages();
	});
}

function stopSupport()
{
	$("#support").hide();
}

function startSupport()
{
	$("#support").show();
	requestMessages();
}

function enterEvent(e)
{
	if(e && e.keyCode == 13)
   	{
    	sendMessage();
   	}
}