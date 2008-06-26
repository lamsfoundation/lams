// add a marker at the given point
function addMarker(point, infoMessage, title, uid, isSaved, editAble, createdBy, createdById)
{
	map.closeInfoWindow();
	var marker;
	
	if(editAble) 	{marker = new GMarker(point, {draggable: true});}
	else 			{marker = new GMarker(point, {draggable: false})};
	
	marker.editAble = editAble;
	marker.createdBy = createdBy;
	marker.createdById = createdById
	
    map.addOverlay(marker);
    
    GEvent.addListener(marker, "dragstart", function() {
    	map.closeInfoWindow();
    });

    GEvent.addListener(marker, "dragend", function() {
		if (marker.state == "unchanged") {marker.state = "update";}
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
    marker.title = title;

    
    // set the state of the marker
    marker.editingOn = !isSaved;
    marker.uid = uid;
    if (isSaved){marker.state = "unchanged";}
    else {marker.state="unsaved";}
    
	marker.sideBarLinkPrefix = "<span class='sidebar'><a href='javascript:GEvent.trigger(markers["+markers.length+"],\"click\")'";
    marker.removeLink = "<a href='javascript:removeMarker(" + markers.length + ")'>Remove</a>" ;
   	marker.editLink = "<a href='javascript:editMarker(" + markers.length + ")'>Edit</a>";
   	marker.saveLink = "<a href='javascript:saveMarkerInfo(" + markers.length + ");'>Save</a>";
   	marker.cancelLink = "<a href='javascript:cancelEditMarkerInfo(" + markers.length + ")'>Cancel</a>";
    updateMarkerInfoWindowHtml(marker);
    markers[markers.length] = marker;
    
}



function refreshSideBar()
{
	var sideBarText = "";
	var i=0;
	for (;i<markers.length; i++)
	{
		if (markers[i].state != "remove" && markers[i].state != "unsaved")
		{
			sideBarText += markers[i].sideBarLinkPrefix + " title='" + markers[i].createdBy + "' >" + markers[i].title+"</a></span><br />";
		}
	}
	document.getElementById("sidebar").innerHTML = sideBarText;
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
					alert(errorCantFindLocation + " " + address );
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
	for (var i=0; i< markers.length; i++) 
	{
		if (markers[i].state != "remove")
		{
			bounds.extend(markers[i].getPoint());	
		}
	}
   	var zoom = map.getBoundsZoomLevel(bounds) - 1;
   	if (zoom > 15) {zoom = 15;}
   	map.setZoom(zoom);
   	map.panTo(bounds.getCenter());
   
}

function trim(x)
{
	return x.replace(/^\s+|\s+$/g, '')
}

function updateMarkerInfoWindowHtml(markerIn)
{
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
		markerIn.inputForm =titleMsg + <br><input tupe='text' maxlength='50' size='50' id='markerTitle' name='markerTitle' value='" + markerIn.title +"' /><br>";
		markerIn.inputForm += newInfoWindowTextMsg + "<br><textarea wrap='hard' id='infoWindow' name='infoWindow' rows='5' cols='50'>" + markerIn.infoMessage + "</textarea>";
		markerIn.markerMetaData = "<font size='tiny' color='grey'>";
		markerIn.markerMetaData += "<nobr>" + latitudeLongitudeMsg + " (" + markerIn.getPoint().lat() + "-" + markerIn.getPoint().lng() +")</nobr>";
		markerIn.markerMetaData += "</font>";
		markerIn.infoWindowHtml =  markerIn.inputForm + "<br />" +markerIn.markerMetaData + markerIn.linksBar;
	}
	else
	{
		markerIn.linksBar = ""
		if (markerIn.editAble)
		{
			markerIn.linksBar = "<br/ >" + markerIn.removeLink + "&nbsp;" + markerIn.editLink;
		}
		markerIn.markerMetaData = "<font size='tiny' color='grey'>";
		markerIn.markerMetaData += createdByMsg + " " + markerIn.createdBy + "<br />";
		markerIn.markerMetaData += "<nobr>" + latitudeLongitudeMsg + " (" + markerIn.getPoint().lat() + "-" + markerIn.getPoint().lng() +")</nobr>";
		markerIn.markerMetaData += "</font>";
		markerIn.infoWindowHtml = "" + "<h4>" + markerIn.title + "</h4><div style='overflow:auto;width:400px;height:80px'>" +  markerIn.infoMessage.replace(/\n/g, "<br />") + "</div>"+ markerIn.markerMetaData  + markerIn.linksBar;
	}	
}

function serialiseMarkers()
{
	var xmlString = '<?xml version="1.0"?><markers>';
	var i =0;
	for (;i<markers.length;i++)
	{
		if (markers[i].state == "unchanged")
		{
			var ans = confirm("You have unsaved markers, do you wish to continue?");
			if (!ans)
			{
				return false;
			}
		}
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
	return true;
}








	