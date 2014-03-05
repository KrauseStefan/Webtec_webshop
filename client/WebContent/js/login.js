
$(document).ready(function() {
	var loginDialogElm = $('#loginDialog');
	loginDialogElm.find("#backdrop").on('click', function(event){
		loginDialogElm.hide();		
	});


	loginDialogElm.find('#btnCreateUser').on('click', function(event){
		
		var params = {
			userName: loginDialogElm.find('#inpUserName').val(),
			password: loginDialogElm.find('#inpPassword').val()
		}
		
		if(!params.userName || !params.password){
			return;
		}
		
		params = $.param(params);
			
		$.ajax({
			type: "PUT",
			url: "/rest/createuser?" + params
		})
		.done(function( msg ) {
			var data = {};
			eval("data = " + msg);
			
			if(data.successful)
				loginDialogElm.hide();
			alert(data.message);
		});	
		
	});

	loginDialogElm.find('#btnUserLogin').on('click', function(event){	
		$.ajax({
			type: "POST",
			url: "some.php",
			data: { 
				name: "John", 
				location: "Boston" 
			}
		})
		.done(function( msg ) {
			alert( "Data Saved: " + msg );
		});	
	});

	$('#lnkUserLogin').on('click', function(event){
		loginDialogElm.show();		
	});	

});
