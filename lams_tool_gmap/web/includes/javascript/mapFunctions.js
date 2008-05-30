
// add a marker at the given point
function addMarker(point, infoMessage, uid, isSaved)
{
	map.closeInfoWindow();
	var marker = new GMarker(point, {draggable: true})
    map.addOverlay(marker);
    
    GEvent.addListener(marker, "dragstart", function() {
    	map.closeInfoWindow();
    });

    GEvent.addListener(marker, "dragend", function() {
		if (marker.state == "unchanged") {marker.state = "update";}
		//serialiseMarkers();
		updateMarkerInfoWindowHtml(marker);	
    });
    
    GEvent.addListener(marker, "click", function() {
    	marker.openInfoWindowHtml(marker.infoWindowHtml);
    });
    
    GEvent.addListener(marker, "infowindowclose", function() {
    	updateMarkerInfoWindowHtml(marker);
    });
    
    if (infoMessage!=null)
    {
    	marker.infoMessage = unescape(infoMessage);
    }
    else
    {
    	marker.infoMessage = "";
    }
    
    // set the state of the marker
    marker.editingOn = !isSaved;
    marker.uid = uid;
    if (isSaved){marker.state = "unchanged";}
    else {marker.state="save";}
    

    marker.removeLink = "<a href='javascript:removeMarker(" + markers.length + ")'>Remove</a>" ;
    //marker.editLink = "<a href='pages/authoring/map.jsp' class='thickbox'>Edit</a>";
   	marker.editLink = "<a href='javascript:editMarker(" + markers.length + ")'>Edit</a>";
   	
   	marker.saveLink = "<a href='javascript:saveMarkerInfo(" + markers.length + "); openInfoWindow("+ markers.length +");'>Save</a>";
   	marker.cancelLink = "<a href='javascript:cancelEditMarkerInfo(" + markers.length + ")'>Cancel</a>";
   	
   	//markerManager.addMarkers(marker,  5);
   	
    updateMarkerInfoWindowHtml(marker);
    markers[markers.length] = marker;
}

// Add a new marker to the center of the map
function addMarkerToCenter()
{
	var bounds = map.getBounds();
	var point = bounds.getCenter();
	addMarker(point, "", -1, false)
}

function test()
{
	serialiseMarkers();
	alert(document.authoringForm.markersXML.value);
}

function removeMarker(x)
{
	var ans = confirm("Are you sure you want to remove this marker?");
	if (ans)
	{
		try{map.removeOverlay(markers[x]);}
		catch (e){}
		markers[x].state = "remove";
		//serialiseMarkers();
	}
}

function editMarker(x)
{
	markers[x].editingOn = true;
	updateMarkerInfoWindowHtml(markers[x]);
	openInfoWindow(x);
}	

function cancelEditMarkerInfo(x)
{
	markers[x].editingOn = false;
	updateMarkerInfoWindowHtml(markers[x]);
	openInfoWindow(x);
}

function updateMarkerInfoWindowHtml(markerIn)
{
	markerIn.locationMessage = "<font size='small'><i>";
	markerIn.locationMessage += "Latitude: " + markerIn.getPoint().lat() + "<br />";
	markerIn.locationMessage += "Longitude: " + markerIn.getPoint().lng() + "<br /><br />";
	markerIn.locationMessage += "</i></font>";
	markerIn.infoWindowHtml = markerIn.locationMessage;
	
	if (markerIn.state == "unchanged")
	{
		markerIn.setImage(webAppUrl + "/images/blue_Marker.png");
	}
	else if (markerIn.state == "update")
	{
		markerIn.setImage(webAppUrl + "/images/paleblue_Marker.png");
	}
	else
	{
		markerIn.setImage(webAppUrl + "/images/red_Marker.png");
	}
	
	if (markerIn.editingOn)
	{
		markerIn.linksBar = "<br/ >" + markerIn.saveLink + "&nbsp;" + markerIn.cancelLink;
		
		//markerIn.inputForm = "New Info Window Text: <br>";
		//markerIn.inputForm += "<lams:FCKEditor id='infoWindow'";
		//markerIn.inputForm += "value='"+markerIn.infoMessage+"'";
		//markerIn.inputForm += "contentFolderID='${sessionMap.contentFolderID}'>";
		//markerIn.inputForm += "</lams:FCKEditor>";
		
		markerIn.infoWindowTextarea = "<textarea id='infoWindow' name='infoWindow' rows='5' cols='50'>" + markerIn.infoMessage + "</textarea>";
		markerIn.inputForm = "New Info Window Text: <br>" + markerIn.infoWindowTextarea;
		markerIn.infoWindowHtml += markerIn.inputForm + markerIn.linksBar;
	}
	else
	{
		markerIn.linksBar = "<br/ >" + markerIn.removeLink + "&nbsp;" + markerIn.editLink;
		markerIn.inputForm = "";
		markerIn.infoWindowHtml += markerIn.infoMessage + "<br />" + markerIn.inputForm + markerIn.linksBar;
	}	
}

function openInfoWindow(x)
{
	markers[x].openInfoWindowHtml(markers[x].infoWindowHtml);
}

function showAddress() 
{
	var address = document.getElementById('address').value;
	
	if (geocoder) 
	{
		geocoder.getLatLng(address,
			function(point) 
			{
				if (!point) 
				{
					alert(address + " not found");
				} 
				else 
				{
					map.setCenter(point, 13);
					//var marker = new GMarker(point);
					//map.addOverlay(marker);
					//marker.openInfoWindowHtml(address);
				}
			}
		);
	}
}

function fitMapMarkers() 
{
   var bounds = new GLatLngBounds();
   for (var i=0; i< markers.length; i++) {
      bounds.extend(markers[i].getPoint());
   }
   map.setZoom(map.getBoundsZoomLevel(bounds));
   map.setCenter(bounds.getCenter());
}

function saveMarkerInfo(x)
{
	if (markers[x] != null)
	{
		var info=document.getElementById("infoWindow");
		markers[x].infoMessage = info.value;
		markers[x].editingOn = false;
		
		// change the state to update if it is a pre-existing marker
		if (markers[x].state == "unchanged") {markers[x].state = "update";}
		
		//markers[x].isSaved = persistMarker(markers[x], "createMarker");
		updateMarkerInfoWindowHtml(markers[x]);

	}
}

/*
function getAjaxObject()
{
	var req=null;
	if ( window.XMLHttpRequest ) {req = new XMLHttpRequest();} 
	else {req = new ActiveXObject("MSXML2.XMLHTTP");} 
	return req;	
}
*/

/*
function persistMarker(marker, method)
{
	var ajax = getAjaxObject();
	var result = null;
	ajax.onreadystatechange = function() 
	{ 
	    if(ajax.readyState == 4) 
	    {
	    	result = ajax.responseText;
	    }
	}
	
 	var url = webAppUrl + "/marker.do?" +
 		"&method="  + method +
 		"&toolContentID=" + toolContentID + 
 		"&latitude=" + marker.getPoint().lat() +
 		"&longitude=" + marker.getPoint().lng() +
 		"&infoMessage=" + marker.infoMessage;
 		
 	try
 	{
		ajax.open("GET",url,false);
		ajax.send(null);
	
		if (result=="success")
		{
			alert("yay!");
			return true;
		}
		else
		{
			//TODO: handle marker save failure
			alert("boo!");
			return false;
		}
	}
	catch(e)
	{
		return e;
		//alert("An error occurred: " + e);
	}
}*/	


function serialiseMarkers()
{
	var xmlString = '<?xml version="1.0"?><markers>';
	var i =0;
	
	for (;i<markers.length;i++)
	{
		if (markers[i].state != "unchanged")
		{
			var markerString = '<marker'+
			' latitude="'+ markers[i].getPoint().lat()+ '"'+
			' longitude="'+ markers[i].getPoint().lng()+ '"'+
			' infoMessage="'+ escape(markers[i].infoMessage)+ '"' +
			' markerUID="'+ markers[i].uid + '"' +
			' state="'+ markers[i].state + '"' +
			' />';
			xmlString += markerString;
		}	
	}
	xmlString += "</markers>"
	document.authoringForm.markersXML.value=xmlString;
}






	