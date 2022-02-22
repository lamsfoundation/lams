function iframelyCallback(itemUid, response) {
	let panel = $('#item-content-' + itemUid);
	if (!response) {
		 $('.embedded-open-button', panel).removeClass('btn-default btn-sm pull-right').addClass('btn-primary');
		return;
	}
	
	if (response.title) {
		$('.embedded-title', panel).text(response.title);
		$('#new-window-icon', panel).removeClass('hidden');
	}
	if (response.description) {
		$('.embedded-description', panel).text(response.description);
	}
	if (response.html) {
		$('.embedded-content', panel).append(response.html);
		setIframeHeight(panel);
	}
}

function setIframeHeight(panel) {
	var frame = $('.embedded-content iframe', panel);
	if (frame.length === 0) {
		return;
	}
	frame = frame[0];
	
    var doc = frame.contentDocument? frame.contentDocument : frame.contentWindow.document,
    	body = doc.body,
    	html = doc.documentElement.
    	height = Math.max(body.scrollHeight, body.offsetHeight, 
        				  html.clientHeight, html.scrollHeight, html.offsetHeight);
    frame.style.height = height + "px";
}
