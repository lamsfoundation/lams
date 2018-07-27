
function init() {
    var tag = document.getElementById("currentTab");
   	if(tag != null && tag.value != "") {
   		selectTab(tag.value);
   	}
    else {
        selectTab(1); //select the default tab;
    }
}

function doSelectTab(tabId) {
	selectTab(tabId);
}
function doSubmit(method, tabId) {
	document.monitoringForm.method.value = method;
	document.monitoringForm.submit();
}

