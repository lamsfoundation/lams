

var map;
var markers;
function init() {
	// open the current tab    
	var tag = document.getElementById("currentTab");
	if (tag.value != "") {
		selectTab(tag.value);
	} else {
		selectTab(1);
	}
	
	if (GBrowserIsCompatible()) 
	{
		map = new GMap2(document.getElementById("map_canvas"), { size: new GSize(640,320) } );
		map.setCenter(new GLatLng(37.4419, -122.1419), 13);
    	map.addControl(new GLargeMapControl());
    	map.addControl(new GMapTypeControl());
    	markers = new Array();
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

	
