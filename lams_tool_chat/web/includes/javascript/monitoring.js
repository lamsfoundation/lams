
function init() {
	if (initialTabId) {
		selectTab(initialTabId);
	} else {
		selectTab(1);
	}
}

function doSelectTab(tabId) {
	selectTab(tabId);
}
function doSubmit(method, tabId) {
	document.monitoringForm.method.value = method;
	document.monitoringForm.submit();
}

