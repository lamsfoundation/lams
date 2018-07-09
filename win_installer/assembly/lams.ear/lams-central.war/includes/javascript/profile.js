function updateMyProfileDialogSettings(title, height) {
	var id = 'dialogMyProfile';
	var dialog = $("#" + id, window.parent.document);

	// update height
	$('.modal-content', dialog).height(height);
	if (height.match("%$")) {
		$('.modal-dialog', dialog).height(height);
	} else {
		$('.modal-dialog', dialog).height('');
	}

	// update title
	$('.modal-title', dialog).attr('id',  id + 'Label').text(title);
}