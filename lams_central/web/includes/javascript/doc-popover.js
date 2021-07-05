$(document).ready(function() {
	$('[data-toggle="doc-popover"]').popover({
		'html'  : true,
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
			return title.length === 0 ? "" : title.html();
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
});