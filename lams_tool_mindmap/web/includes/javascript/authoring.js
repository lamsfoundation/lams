
function init() {
	selectTab(1);
}

function doSelectTab(tabId) {
	selectTab(tabId);
}

function doSubmit(method) {
	document.authoringForm.dispatch.value = method;
	document.authoringForm.submit();
}

