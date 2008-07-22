// Add a new marker to the center of the map
function addMarkerToCenter()
{
	if (limitMarkers)
	{
		if (userMarkerCount == markerLimit)
		{
			alert(markerLimitMsg);
			return;
		}
	}
	var bounds = map.getBounds();
	var point = bounds.getCenter();
	addMarker(point, "", "", -1, false, true, currUser, currUserId);
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
		refreshSideBar(sessionName);
		userMarkerCount --;
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
			markers[x].highlight = selectedUser == markers[x].createdById;
			
			// change the state to update if it is a pre-existing marker
			if (markers[x].state == "unchanged") 		{markers[x].state = "update";}
			else if (markers[x].state != "update") 		{markers[x].state ="save"};
			
			refreshSideBar(sessionName);
			updateMarkerInfoWindowHtml(markers[x]);
			openInfoWindow(x);
		}
	}
}


