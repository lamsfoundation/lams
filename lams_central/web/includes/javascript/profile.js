function updateMyProfileDialogSettings(title, height) {
	var id = 'dialogMyProfile';
	var dialog = $("#" + id, window.parent.document);

	// update height
	if (height.match("%$")) {
		$('.modal-content', dialog).css('height', height);
		$('.modal-dialog', dialog).css('height', height);
	} else {
		$('.modal-content', dialog).css('height', height + 'px');
		$('.modal-dialog', dialog).css('height', null);
	}

	// update title
	$('.modal-title', dialog).attr('id',  id + 'Label').text(title);
}