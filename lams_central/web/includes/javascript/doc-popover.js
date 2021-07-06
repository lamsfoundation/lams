$(document).ready(function() {
	$('[data-toggle="doc-popover"]')
		.attr({
			"aria-expanded" : "false",
			"aria-haspopup" : "true",
			"tabindex"      : "0",
			"role"          : "button"
		})
		.on('keypress', function(e){
		    if (e.keyCode === 32 || e.keyCode == 13) {
				// check for Space or Enter key
		        $(this).trigger('click');
		        return false;
		    }
		})
		.popover({
			'html'      : true,
			'trigger'   : 'click',
			// without this option the close button in popover title does not show up
			'sanitize'  : false,
			'title' : function(){
				// first try to get the title straight from element attributes
				let popover = $(this),
					title = popover.data('title');
				if (title) {
					return title;
				}
				// if no title is present in attributes then see if the element is using attributes for content
				// if so and no data-title was provided, it means that no title is needed
				if (popover.data('content')) {
					return "";
				}
	
				// next attempt: try to find a sibling that contains the content
				let content = popover.siblings('.doc-popover-content');
				if (content.length === 0) {
					// no sibling found, so try to find the content using ID
					let popoverId = popover.attr('id');
					if (!popoverId) {
						return "";
					}
					content = $('#' + popoverId + '-content');
					if (content.length === 0) {
						return "";
					}
				}
				// if content found then try to extract title from it, if any
				title = content.children('.doc-popover-title');
				title = title.length === 0 ? "" : title.html();
				if (title) {
					// put title text on the left and a close button on the right
					title = '<div style="display: flex; justify-content: space-between;"><span>' + title + 
							'</span><a role="button" class="close" aria-label="Close" tabindex="0" style="height: 100%"><span aria-hidden="true">&times;</span></a>';
				}
				
				return title;
			},
			'content' : function(){
				// first try to get content straight from element attributes
				let popover = $(this),
					content = popover.data('content');
				if (content) {
					return content;
				}
	
				// next attempt: try to find a sibling that contains the content
				content = popover.siblings('.doc-popover-content');
				if (content.length === 0) {
					// no sibling found, so try to find the content using ID
					let popoverId = popover.attr('id');
					if (!popoverId) {
						return "";
					}
					content = $('#' + popoverId + '-content');
					if (content.length === 0) {
						return "";
					}
				}
	
				// if content found then try to extract body from it
				let contentBody = content.children('.doc-popover-body');
				return contentBody.length === 0 ? "" : contentBody.html();
			}
		});
		
	// when a close button is clicked, close the popover
	$(document).on('keypress click', '.popover .popover-title .close', function(e){
		if (e.type == "click" || e.keyCode === 32 || e.keyCode == 13) {
			var popoverId = $(this).closest('.popover').attr('id');
			$('[data-original-title], [data-toggle="doc-popover"][aria-describedby="' + popoverId + '"]').popover('hide');
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
	
	    $('[data-toggle="doc-popover"]').each(function () {
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