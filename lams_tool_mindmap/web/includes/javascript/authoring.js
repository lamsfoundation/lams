
function init() {
	selectTab(1);
}

function doSelectTab(tabId) {
	selectTab(tabId);
}

function doSubmit(method) {
	document.forms.authoringForm.dispatch.value = method;
	document.forms.authoringForm.submit();
}

