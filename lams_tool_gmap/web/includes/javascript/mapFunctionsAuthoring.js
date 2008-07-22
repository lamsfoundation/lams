// Add a new marker to the center of the map
function addMarkerToCenter()
{
	var bounds = map.getBounds();
	var point = bounds.getCenter();
	addMarker(point, "", "", -1, false, true, currUser, currUserId);
}

function removeMarker(x)
{
	var ans = confirm(confirmDelete);
	if (ans)
	{
		try{map.removeOverlay(markers[x]);}
		catch (e){}
		markers[x].state = "remove";
		refreshSideBarAuthoring();
		selectedMarker = -1;
	}
}

function saveMarkerInfo(x)
{
	if (markers[x] != null)
	{
		var title= trim(document.getElementById("markerTitle").value);
		if (title==null || title == "")
		{
			alert(errorMissingTitle);
		}
		else
		{		
			var info=document.getElementById("infoWindow").value;
			markers[x].title = title;
			markers[x].infoMessage = info;
			markers[x].editingOn = false;
			
			// change the state to update if it is a pre-existing marker
			if (markers[x].state == "unchanged") 		{markers[x].state = "update";}
			else if (markers[x].state != "update") 		{markers[x].state ="save"};
			
			updateMarkerInfoWindowHtml(markers[x]);
			refreshSideBarAuthoring();
			openInfoWindow(x);
		}
	}
}

function saveMapState()
{
	document.getElementById("mapZoom").value=map.getZoom();
	document.getElementById("mapCenterLatitude").value=map.getCenter().lat();
	document.getElementById("mapCenterLongitude").value=map.getCenter().lng();
	var mapTypeName = map.getCurrentMapType().getName();
	var mapType = "";	
	if 		(mapTypeName == "Satellite"){ mapType = "G_SATELLITE_MAP"; }
	else if (mapTypeName == "Hybrid")	{ mapType = "G_HYBRID_MAP"; }
	else if (mapTypeName == "Terrain") 	{ mapType = "G_PHYSICAL_MAP"; }
	else 								{ mapType = "G_NORMAL_MAP"; }
	document.getElementById("mapType").value=mapType;
}

function refreshSideBarAuthoring()
{
	//marker.sideBarLinkPrefix = "<span class='sidebar'><a href='javascript:GEvent.trigger(markers["+markers.length+"],\"click\")'";
	var sideBarText = "<div class='field-name'>" + authoredMarkerMsg + "</div>";
	var i=0;
	for (;i<markers.length; i++)
	{
		
		if (markers[i].state != "remove" && markers[i].state != "unsaved")
		{
			sideBarText += "<span id='markerSpan" +markers[i].sideBarIndex+ "'>";
			sideBarText += "<a href='javascript:GEvent.trigger(markers[" + markers[i].sideBarIndex + "],\"click\")' ";
			sideBarText += "title='" + markers[i].title + "' >" + markers[i].title + "</a>"
			sideBarText += "</span><br />";
			//sideBarText += markers[i].sideBarLinkPrefix + " title='" + markers[i].createdBy + "' >" + markers[i].title+"</a></span><br />";
		}
	}
	document.getElementById("sidebar").innerHTML = sideBarText;
}