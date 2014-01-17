
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
	
	var mindmapContent = document.getElementById("mindmapContent");
	
	if (tag.value == "" || tag.value == 1) {
		mindmapContent.value = document['flashContent'].getMindmap();
	}
	
	tag.value = tabId;
	
	if (tag.value == "" || tag.value == 1) {
		flashvars["xml"] = "";
		embedFlashObject(700, 525);
	}
	
	selectTab(tabId);
}

function doSubmit(method) {
	document.authoringForm.dispatch.value = method;
	document.authoringForm.submit();
}

