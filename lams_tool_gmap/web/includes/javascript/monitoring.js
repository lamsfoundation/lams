
function doSelectTab(tabId) {
	selectTab(tabId);
}
function doSubmit(method, tabId) {
	document.monitoringForm.method.value = method;
	document.monitoringForm.submit();
}

