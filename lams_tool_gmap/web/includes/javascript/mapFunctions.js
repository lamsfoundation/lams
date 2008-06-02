
// add a marker at the given point
function addMarker(point, infoMessage, title, uid, isSaved)
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
    
    GEvent.addListener(marker,'mouseover',function(){
		//marker[i].setImage(markerImage[3]);
		//alert("hello");
		//document.getElementById("sidebar").getElementsByTagName("span")[markers.length].style.background ="blue";
	});
	
	GEvent.addListener(marker,'mouseout',function()
	{
		//document.getElementById("sidebar").getElementsByTagName("span")[markers.length].style.background ="blue";
		
		/*if(marker[i].visited){
			marker[i].setImage(markerImage[4]);
			document.getElementById("sidebar").getElementsByTagName("span")[i].style.color ="gray";
		
		}else{
		marker[i].setImage(markerImage[0]);
		document.getElementById("sidebar").getElementsByTagName("span")[i].style.color ="black";
		*/
	});
    
    if (infoMessage!=null)
    {
    	marker.infoMessage = unescape(infoMessage);
    }
    else
    {
    	marker.infoMessage = "";
    }
    
    //marker.title = "Marker #" + markers.length;
    marker.title = title;
    
    //sideBar(marker.infoMessage, markers.length);
    
    
    // set the state of the marker
    marker.editingOn = !isSaved;
    marker.uid = uid;
    if (isSaved){marker.state = "unchanged";}
    else {marker.state="unsaved";}
    
	marker.sideBarLinkPrefix = "<span class='sidebar'><a href='javascript:GEvent.trigger(markers["+markers.length+"],\"click\")'>";
    //marker.sideBarLinkSuffix = marker.title+"</a></span><br />"
    marker.removeLink = "<a href='javascript:removeMarker(" + markers.length + ")'>Remove</a>" ;
   	marker.editLink = "<a href='javascript:editMarker(" + markers.length + ")'>Edit</a>";
   	marker.saveLink = "<a href='javascript:saveMarkerInfo(" + markers.length + "); openInfoWindow("+ markers.length +");'>Save</a>";
   	marker.cancelLink = "<a href='javascript:cancelEditMarkerInfo(" + markers.length + ")'>Cancel</a>";
    updateMarkerInfoWindowHtml(marker);
    markers[markers.length] = marker;
    
}

// Add a new marker to the center of the map
function addMarkerToCenter()
{
	var bounds = map.getBounds();
	var point = bounds.getCenter();
	addMarker(point, "", "", -1, false);
}

function refreshSideBar()
{
	var sideBarText = "";
	var i=0;
	for (;i<markers.length; i++)
	{
		if (markers[i].state != "remove")
		{
			sideBarText += markers[i].sideBarLinkPrefix + markers[i].title+"</a></span><br />";
		}
	}
	document.getElementById("sidebar").innerHTML = sideBarText;
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
		refreshSideBar();
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
	if (markers[x].state == "unsaved")
	{
		removeMarker(x);
	}
	else
	{
		markers[x].editingOn = false;
		updateMarkerInfoWindowHtml(markers[x]);
		openInfoWindow(x);
	}
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
	else if (markerIn.state == "update" || markerIn.state == "save")
	{
		markerIn.setImage(webAppUrl + "/images/paleblue_Marker.png");
	}
	else if (markerIn.state == "unsaved")
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
		
		//markerIn.titleInput = "Title:<br><textarea id='markerTitle' name='markerTitle' value='" + markerIn.title +"' /><br>";
		//markerIn.infoWindowTextarea = "New Info Window Text:<br><textarea id='infoWindow' name='infoWindow' rows='5' cols='50'>" + markerIn.infoMessage + "</textarea>";
		markerIn.inputForm = "Title:<br><input tupe='text' id='markerTitle' name='markerTitle' value='" + markerIn.title +"' /><br>";
		markerIn.inputForm += "New Info Window Text:<br><textarea id='infoWindow' name='infoWindow' rows='5' cols='50'>" + markerIn.infoMessage + "</textarea>";
		//markerIn.inputForm = "" + markerIn.infoWindowTextarea;
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

/*
function saveClick(x)
{
	var title=document.getElementById("markerTitle");
	if (title==null || trim(title) == "")
	{
		alert("Title is required.");
		return false;
	}
	else
	{
		saveMarkerInfo(x);
	}
}
*/

function saveMarkerInfo(x)
{
	if (markers[x] != null)
	{
		var title= trim(document.getElementById("markerTitle").value);
		if (title==null || title == "")
		{
			alert("Title is required.");
			return false;
		}
		else
		{		
			
			var info=document.getElementById("infoWindow").value;
			markers[x].title = title;
			markers[x].infoMessage = info;
			markers[x].editingOn = false;
			
			// change the state to update if it is a pre-existing marker
			if (markers[x].state == "unchanged") {markers[x].state = "update";}
			else (markers[x].state ="save");
			
			//markers[x].isSaved = persistMarker(markers[x], "createMarker");
			updateMarkerInfoWindowHtml(markers[x]);
			refreshSideBar();
		}
	}
}

function trim(x)
{
	return x.replace(/^\s+|\s+$/g, '')
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
		if (markers[i].state != "unchanged" && markers[i].state !="unsaved")
		{
			var markerString = '<marker'+
			' latitude="'+ markers[i].getPoint().lat()+ '"'+
			' longitude="'+ markers[i].getPoint().lng()+ '"'+
			' infoMessage="'+ escape(markers[i].infoMessage)+ '"' +
			' markerUID="'+ markers[i].uid + '"' +
			' title="'+ markers[i].title + '"' +
			' state="'+ markers[i].state + '"' +
			' />';
			xmlString += markerString;
		}	
	}
	xmlString += "</markers>"
	document.authoringForm.markersXML.value=xmlString;
}






	