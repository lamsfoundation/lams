function iframelyCallback(itemUid, response) {
	let panel = $('#item-content-' + itemUid);
	if (!response) {
		 $('.embedded-open-button', panel).removeClass('btn-default btn-sm pull-right').addClass('btn-primary');
		return;
	}
	
	if (response.title) {
		$('.embedded-title', panel).text(response.title);
		$('.new-window-icon', panel).removeClass('hidden');
	}
	if (response.description) {
		$('.embedded-description', panel).text(response.description);
	}
	if (response.html) {
		$('.embedded-content', panel).append(response.html);
	}
}