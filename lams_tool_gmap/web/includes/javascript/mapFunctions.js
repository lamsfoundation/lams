
function addMarkerToCenter()
{
	var bounds = map.getBounds();
	var point = bounds.getCenter();
	
	var marker = new GMarker(point, {draggable: true})
    map.addOverlay(marker);
    
    GEvent.addListener(marker, "dragstart", function() {
    	map.closeInfoWindow();
    });

    GEvent.addListener(marker, "dragend", function() {
    	//marker.infoWindowHtml = "Latitude: " + marker.getLatLng().lat() + "<br>Longitude: " + marker.getLatLng().lng() + marker.removeButton ;;
    	//marker.openInfoWindowHtml(marker.infoWindowHtml);
    });
    
   	
    GEvent.addListener(marker, "click", function() {
    	marker.openInfoWindowHtml(marker.infoWindowHtml);
    });
    
    GEvent.addListener(marker, "infowindowclose", function() {
    	marker.editingOn = false;
    	updateMarkerInfoWindowHtml(marker)
    });
    
    
    
    
    
    marker.infoMessage = "<i>No message</i><br />";

    marker.removeLink = "<a href='javascript:removeMarker(" + markers.length + ")'>Remove</a>" ;
    //marker.editLink = "<a href='pages/authoring/map.jsp' class='thickbox'>Edit</a>";
   	marker.editLink = "<a href='javascript:editMarker(" + markers.length + ")'>Edit</a>";
   	
   	marker.saveLink = "<a href='javascript:saveMarkerInfo(" + markers.length + ")'>Save</a>";
   	marker.cancelLink = "<a href='javascript:cancelEditMarkerInfo(" + markers.length + ")'>Cancel</a>";
   	
   	
   	marker.editingOn = false;
    
    updateMarkerInfoWindowHtml(marker);
    //alert(marker.infoMessage);
    //alert(marker.inputForm);
    //alert(marker.linksBar);
    
    //marker.infoWindowHtml = "Latitude: " + marker.getLatLng().lat() + "<br>Longitude: " + marker.getLatLng().lng() + marker.removeButton ;
    markers[markers.length] = marker;

}


//<a href="images/single.jpg" title="add a caption to title attribute / or leave blank" class="thickbox"><img src="images/single_t.jpg" alt="Single Image"/></a>

function test()
{
	var string = "";
	var i;
	for (i=0; i<markers.length; i++)
	{
		//string += "Latitude: " + markers[i].getLatLng().lat() + "\n";
		//string += "Longitude: " + markers[i].getLatLng().lng() + "\n\n";
		
		if (markers[i]!=null)
		{
			string += "infoWindow: " + markers[i].infoWindowHtml + "\n\n";
		}
	}
	
	alert(string);
}

function removeMarker(x)
{
	var ans = confirm("Are you sure you want to remove this marker?");
	if (ans)
	{
		map.removeOverlay(markers[x]);
		markers[x] = null;
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
		var info=document.getElementById("infoWindow");
		markers[x].infoMessage = info.value + "<br />";
		markers[x].editingOn = false;
		updateMarkerInfoWindowHtml(markers[x]);
		//markers[x].openInfoWindowHtml(markers[x].infoWindowHtml);
		openInfoWindow(x);
	}
}

function cancelEditMarkerInfo(x)
{
	markers[x].editingOn = false;
	updateMarkerInfoWindowHtml(markers[x]);
	openInfoWindow(x);
}

function updateMarkerInfoWindowHtml(markerIn)
{
	if (markerIn.editingOn)
	{
		markerIn.linksBar = "<br/ >" + markerIn.saveLink + "&nbsp;" + markerIn.cancelLink;
		markerIn.infoWindowTextarea = "<textarea id='infoWindow' name='infoWindow' rows='3' cols='30'>" + markerIn.infoMessage + "</textarea>";
		markerIn.inputForm = "New Info Window Text: <br>" + markerIn.infoWindowTextarea;
		markerIn.infoWindowHtml = markerIn.inputForm + markerIn.linksBar;
	}
	else
	{
		markerIn.linksBar = "<br/ >" + markerIn.removeLink + "&nbsp;" + markerIn.editLink;
		markerIn.inputForm = "";
		markerIn.infoWindowHtml = markerIn.infoMessage + markerIn.inputForm + markerIn.linksBar;
	}

	
}

function openInfoWindow(x)
{
	markers[x].openInfoWindowHtml(markers[x].infoWindowHtml);
}