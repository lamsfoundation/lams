$(document).ready(function() {
	// has this script already run?
	let otherPopoversProcessed = $('.lams-popover-processed').length > 0;
	
	// go through only non-processed LAMS popovers
	$('.lams-popover:not(.lams-popover-processed)')
		// mark this popover as processed and add icons
		.addClass('lams-popover-processed fa fa-question-circle')
		// add accessibility attributes
		.attr({
			"aria-expanded" : "false",
			"aria-haspopup" : "true",
			"tabindex"      : "0",
			"role"          : "button"
		})
		.on('keypress', function(e){
		    if (e.keyCode === 32 || e.keyCode == 13) {
				// check for Space or Enter key and simulate a click
		        $(this).trigger('click');
		        return false;
		    }
		})
		.popover({
			'container' : 'body',
			'html'      : true,
			'trigger'   : 'focus',
			// without this option the close button in popover title does not show up
			'sanitize'  : false,
			'title' : function(){
				// first try to get the title straight from element attributes
				let popover = $(this),
					title = popover.data('title');
				if (!title) {
					// if no title is present in attributes then see if the element is using attributes for content
					// if so and no data-title was provided, it means that no title is needed
					if (popover.data('content')) {
						return "";
					}
					
					// try to find the content using ID
					let popoverId = popover.attr('id');
					if (!popoverId) {
						return "";
					}
					
					let content = $('#' + popoverId + '-content.lams-popover-content');
					if (content.length === 0) {
						return "";
					}
	
					// if content found then try to extract title from it, if any
					title = content.children('.lams-popover-title');
					title = title.length === 0 ? "" : title.html();
				}
				
				if (title) {
					// add an extra accessibility attribute
					popover.attr('aria-label', title);
					// put title text on the left and a close button on the right
					title = '<div style="display: flex; justify-content: space-between;"><span>' + title + 
							'</span><a role="button" class="close" aria-label="Close" tabindex="0" style="height: 100%"><span aria-hidden="true">&times;</span></a>';
					popover.data('titlePresent', true);
				}
				
				return title;
			},
			'content' : function(){
				// first try to get content straight from element attributes
				let popover = $(this),
					content = popover.data('content');
				if (!content) {
					// try to find the content using ID
					let popoverId = popover.attr('id');
					if (!popoverId) {
						return "";
					}
					
					content = $('#' + popoverId + '-content.lams-popover-content');
					if (content.length === 0) {
						return "";
					}
		
					// if content found then try to extract body from it
					content = content.children('.lams-popover-body');
					content = content.length === 0 ? "" : content.html();
				}
				
				// if there is not title then we need to put close button directly on content
				if (content && !popover.data('titlePresent')) {
					content = '<a role="button" class="close pull-right" aria-label="Close" tabindex="0" style="height: 100%"><span aria-hidden="true">&times;</span></a>'
					          + content;
				}
				
				return content;
			}
		}).each(function(){
			// trigger title() function for each popover so aria-label gets set up
			$(this).data('bs.popover').options.title.call(this);
		});
		
	if (otherPopoversProcessed) {
		// if this script has run before, no need to run code below again
		return;
	}
		
	// when a close button is clicked, close the popover
	$(document).on('keypress click', '.popover .close', function(e){
		if (e.type == "click" || e.keyCode === 32 || e.keyCode == 13) {
			var popoverId = $(this).closest('.popover').attr('id');
			$('.lams-popover-processed[data-original-title], .lams-popover-processed[aria-describedby="' + popoverId + '"]').popover('hide');
	    }
	});

	// Dismiss popover on a click outside an open popover
	// Taken from https://stackoverflow.com/a/33953365
	$(document).on('click', function (e) {
	    var $popover,
	        $target = $(e.target);
	
	    //do nothing if there was a click on popover content
	    if ($target.hasClass('popover') || $target.closest('.popover').length) {
	        return;
	    }
	
	    $('.lams-popover').each(function () {
	        $popover = $(this);
	
	        if (!$popover.is(e.target) &&
	            $popover.has(e.target).length === 0 &&
	            $('.popover').has(e.target).length === 0) {
	            $popover.popover('hide');
	        } else {
	            //fixes issue described above
	            $popover.popover('toggle');
	        }
	    });
	});
});
