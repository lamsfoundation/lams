

var map;
var markers;
var geocoder = null;
function init() {
	// open the current tab    
	var tag = document.getElementById("currentTab");
	if (tag.value != "") {
		selectTab(tag.value);
	} else {
		selectTab(1);
	}

}

function initGmap()
{
	if (GBrowserIsCompatible()) 
	{
		//map = new GMap2(document.getElementById("map_canvas"), { size: new GSize(640,320) } );
		map = new GMap2(document.getElementById("map_canvas"), { size: new GSize(500,320) } );
		//map.setCenter(new GLatLng(-33.774322, 151.111988), 13);
    	map.addControl(new GLargeMapControl());
    	map.addControl(new GMapTypeControl());
    	map.addMapType(G_PHYSICAL_MAP); 
    	
    	/*
		G_NORMAL_MAP 	This map type (which is the default) displays a normal street map.
		G_SATELLITE_MAP 	This map type displays satellite images.
		G_HYBRID_MAP
    	*/
    	markers = new Array();
    	geocoder = new GClientGeocoder();
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

	
