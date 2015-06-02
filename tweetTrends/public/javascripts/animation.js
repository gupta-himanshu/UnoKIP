if (window.console) {
  console.log("Welcome to Animation.js file");
}
$(document).ready(function() {
	var divs = $('.goToTop');
	divs.fadeOut(0);
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