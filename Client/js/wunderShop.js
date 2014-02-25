$(document).ready(function() {

	// on window scroll fire it will call a function.
	$(window).scroll(function() {

		// after window scroll fire it will add define pixel added to that
		// element.
		set = $(document).scrollTop() + "px";

		// this is the jQuery animate function to fixed the div position after
		// scrolling.
		$('#floatDiv').animate({
			top : set
		}, {
			duration : 1000,
			queue : false
		});

	});

});