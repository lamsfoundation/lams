
function init() {
	
	// initialising tabs
	initTabSize(4);
	
	// open the first tab 
	selectTab(1);	
}

function doSelectTab(tabId) {
	selectTab(tabId);
}
function doSubmit(method, tabId) {
	document.monitoringForm.method.value = method;
	document.monitoringForm.submit();
}

