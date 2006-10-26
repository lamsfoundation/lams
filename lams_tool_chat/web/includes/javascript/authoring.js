
function init() {
	// open the current tab    
	var tag = document.getElementById("currentTab");
	if (tag.value != "") {
		selectTab(tag.value);
	} else {
		selectTab(1);
	}
}
function doSelectTab(tabId) {
	var tag = document.getElementById("currentTab");
	tag.value = tabId;
	selectTab(tabId);
}
function doSubmit(method) {
	document.authoringForm.dispatch.value = method;
	document.authoringForm.submit();
}
function deleteAttachment(dispatch, uuid) {
	document.authoringForm.dispatch.value = dispatch;
	document.authoringForm.deleteFileUuid.value = uuid;
	document.authoringForm.submit();
}

