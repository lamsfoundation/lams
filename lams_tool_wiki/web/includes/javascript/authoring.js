
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
	
	// Making sure the save button is shown at the correct time
	var finishButtonDiv = document.getElementById("finishButtonDiv");
	var viewDiv = document.getElementById("view");
	if (tabId==1 && viewDiv.style.display=="none")
	{
		finishButtonDiv.style.display = "none";
	}
	else
	{
		finishButtonDiv.style.display = "block";
	}
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
