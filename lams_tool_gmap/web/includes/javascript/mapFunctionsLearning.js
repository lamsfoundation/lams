// Add a new marker to the center of the map
function addMarkerToCenter()
{
	if (limitMarkers)
	{
		if (userMarkerCount == markerLimit)
		{
			alert("You have reached the marker limit, you cannot add any more");
			return;
		}
	}
	var bounds = map.getBounds();
	var point = bounds.getCenter();
	addMarker(point, "", "", -1, false, true, currUser);
	userMarkerCount ++;
}

function removeMarker(x)
{
	var ans = confirm(confirmDelete);
	if (ans)
	{
		try{map.removeOverlay(markers[x]);}
		catch (e){}
		markers[x].state = "remove";
		refreshSideBar();
		userMarkerCount --;
		//serialiseMarkers();
	}
}

function editMarker(x)
{
	markers[x].editingOn = true;
	updateMarkerInfoWindowHtml(markers[x]);
	openInfoWindow(x);
}

function saveMarkerInfo(x)
{
	if (markers[x] != null)
	{
		var title= trim(document.getElementById("markerTitle").value);
		if (title==null || title == "")
		{
			alert(errorMissingTitle);
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
			
			updateMarkerInfoWindowHtml(markers[x]);
			refreshSideBar();
			openInfoWindow(x);
		}
	}
}


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
	document.getElementById("markersXML").value=xmlString;
}

