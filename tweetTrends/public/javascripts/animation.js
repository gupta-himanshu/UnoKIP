if (window.console) {
  console.log("Welcome to Animation.js file");
}
$(document).ready(function() {
	var divs = $('.goToTop');
	divs.fadeOut(0);
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
			    opacity: .6
			  }, {duration: 'slow',
				  easing: 'easeOutBounce'
				});
		});
	$(window).scroll(function() {
		
		var top = $(this).scrollTop();
		if(top >30){
			divs.fadeIn("fast");
			$(".logo").stop(true);	
			$(".logo").clearQueue();
			$(".logo").animate({
				    width: 30,
				    height: 30
				  }, {duration: 'fast',
					  easing: 'easeInSine'
					});	
		}
		else{
	         divs.fadeOut("fast");
			$(".logo").stop(true);	
			$(".logo").clearQueue();
			$( ".logo" ).animate({
			    width: 50,
			    height: 50
			  }, {duration: 'slow',
				  easing: 'easeOutBounce'
				});
		}
		});
});