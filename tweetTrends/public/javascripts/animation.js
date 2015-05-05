if (window.console) {
  console.log("Welcome to Animation.js file");
}
$(document).ready(function() {
	/// mouse hover event
	$(".icon").mouseenter(function() {
			$(this).stop(true);	
			$(this).clearQueue();
			$( this ).animate({
				    width: 60,
				    height: 60,
				    opacity:1
				  }, {duration: 'fast',
					  easing: 'easeOutBack'
					});
			});
	/// mouse out event
	$(".icon").mouseleave(function() {
		$( ".icon" ).animate({
			    width: 40,
			    height: 40,
			    opacity: .5
			  }, {duration: 'slow',
				  easing: 'easeOutBounce'
				});
		});
});