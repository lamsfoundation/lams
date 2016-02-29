<%-- 
This is the google map library for that handles most of the google map functionality in LAMS
The functions are in javascript, but it was made as a jsp page so the custom language and other
tags could be used

Author: lfoxton
--%>	

<%@ include file="/common/taglibs.jsp"%>

<c:set var="tool">
	<lams:WebAppURL />
</c:set>

<script type="text/javascript">
<!--

var selectedUser = -1;
var selectedMarker = -1;
var currentOpenMarkerImportance= 0;

// add a marker at the given point
function addMarker(point, infoMessage, title, uid, isSaved, editAble, createdBy, createdById)
{
	
	map.closeInfoWindow();
	
	// Create the marker and set whether is is editable, and some other properties
	var marker;
	if(editAble) 	
	{
		marker = new GMarker(point, {draggable: true, zIndexProcess:importanceOrder});
		if (isSaved) 	{marker.importance = 1;}
		else 			{marker.importance = 2;}
	}
	else 			
	{
		marker = new GMarker(point, {draggable: false, zIndexProcess:importanceOrder})
		marker.importance = 0;
	};
	marker.editAble = editAble;
	marker.createdBy = createdBy;
	marker.createdById = createdById;
	marker.sideBarIndex = markers.length;
	
    // add marker to the map
    map.addOverlay(marker);
    
    // Adding marker event handling
    GEvent.addListener(marker, "dragstart", function() {
    	map.closeInfoWindow();
    });

    GEvent.addListener(marker, "dragend", function() {
		if (marker.state == "unchanged") {marker.state = "update";}
		updateMarkerInfoWindowHtml(marker);	
    });
    
    GEvent.addListener(marker, "click", function() {
    	marker.openInfoWindowHtml(marker.infoWindowHtml);
    	showSelectedMarkerSideBar(marker.sideBarIndex);
    	
    	// backup the current marker importance (z index), then set the marker importance to high
    	// marker's importance is restored in the "infowindowclose" event below
    	currentOpenMarkerImportance = marker.importance;
    	marker.importance = 3;
    });
    
    GEvent.addListener(marker, "infowindowclose", function() {
    	updateMarkerInfoWindowHtml(marker);
    	showSelectedMarkerSideBar(marker.sideBarIndex);
    	
    	// reset the marker's importance
    	marker.importance = currentOpenMarkerImportance
    });
    
    
    // set the marker's info message
    if (infoMessage!=null)
    {
    	marker.infoMessage = decode_utf8(infoMessage);
    }
    else
    {
    	marker.infoMessage = "";
    }
    marker.title = <c:out value="title" escapeXml="true"/>;
    
    // set the state of the marker, determines how the info window will display for this marker, and the sidebar 
    marker.editingOn = !isSaved;
    marker.uid = uid;
    if (isSaved) {marker.state = "unchanged";} else {marker.state="unsaved";}
    marker.highlight = false;
    marker.removeLink = "<a href='javascript:removeMarker(" + markers.length + ")'><fmt:message key='button.remove'/></a>" ;
   	marker.editLink = "<a href='javascript:editMarker(" + markers.length + ")'><fmt:message key='button.edit'/></a>";
   	marker.saveFuncStr = "saveMarkerInfo(" + markers.length + ");";
   	marker.saveLink = "<a href='javascript:" +marker.saveFuncStr+ "'><fmt:message key='button.placeMarker'/></a>";
   	marker.cancelLink = "<a href='javascript:cancelEditMarkerInfo(" + markers.length + ")'><fmt:message key='button.cancel'/></a>";
    updateMarkerInfoWindowHtml(marker);
    markers[markers.length] = marker;
    
    // open the marker window if it is a new marker
    if (!isSaved) {openInfoWindow(markers.length - 1);}
}

// add a user to the user array
function addUserToList(id, name)
{
	var user = new Object();
	user.name = name;
	user.id = id;
	user.divOpen = false;
	users[users.length] = user;
}

// set the z index of the markers
function importanceOrder (marker,b) 
{
	return GOverlay.getZIndex(marker.getPoint().lat()) + marker.importance*1000000;
}

// Changes the marker state to edit, then refreshes the info window
function editMarker(x)
{
	markers[x].editingOn = true;
	updateMarkerInfoWindowHtml(markers[x]);
	openInfoWindow(x);
}

// opens/closes the user's sidebar div onclick
function makeUsersSideBarVisible(id)
{
	var div = document.getElementById("userdiv" + id);
	
	// get the user
	var userTemp;
	for (i=0;i<users.length; i++)
	{
		if (users[i].id == id)
		{
			userTemp = users[i];
			break;
		}
	}
	
	if (div.style.display == "block")
	{
		document.getElementById("userdiv" + id).style.display = "none";
		document.getElementById("userTreeIcon" + id).src = TREE_CLOSED_ICON;
		userTemp.divOpen = false;	
	}
	else if (div.style.display == "none")
	{
		document.getElementById("userdiv" + id).style.display = "block";
		document.getElementById("userTreeIcon" + id).src = TREE_OPEN_ICON;
		userTemp.divOpen = true;
	}
}

// Highlights the markers of a user
function showSelectedUser(id)
{
	map.closeInfoWindow();
	
	var div = document.getElementById("userdiv" + id);
	var i;
	
	if (selectedUser == -1)
	{
		fitMapMarkers();
		document.getElementById("userSpan" + id).style.backgroundColor = "yellow";
		selectedUser = id;
	}
	else if (selectedUser == id)
	{
		document.getElementById("userSpan" + selectedUser).style.backgroundColor = "";
		selectedUser = -1;
	}
	else
	{
		fitMapMarkers();
		document.getElementById("userSpan" + selectedUser).style.backgroundColor = "";
		document.getElementById("userSpan" + id).style.backgroundColor = "yellow";
		selectedUser = id;
	}

	
	for (i=0;i<markers.length; i++)
	{
		if (markers[i].createdById == selectedUser)
		{
			markers[i].highlight=true;
		}
		else
		{
			markers[i].highlight=false;
		}
		updateMarkerInfoWindowHtml(markers[i]);
	}
	
}

// Highlighs the selected marker in the sidebar
function showSelectedMarkerSideBar(id)
{
	var selectedMarkerSpan = document.getElementById("markerSpan" + selectedMarker);
	var markerSpanToSelect = document.getElementById("markerSpan" + id);
	
	if (selectedMarker == -1 && markerSpanToSelect != null)
	{
		document.getElementById("markerSpan" + id).style.backgroundColor = "#FFFFCC";
		selectedMarker = id;
	}
	else if (selectedMarker == id && selectedMarkerSpan!= null)
	{
		document.getElementById("markerSpan" + selectedMarker).style.backgroundColor = "";
		selectedMarker = -1;
	}
	else if (selectedMarkerSpan != null && markerSpanToSelect!= null)
	{
		document.getElementById("markerSpan" + selectedMarker).style.backgroundColor = "";
		document.getElementById("markerSpan" + id).style.backgroundColor = "#FFFFCC";
		selectedMarker = id;
	}
}

// if a marker has been added, the sidebar needs to be refreshed to include it
// preserves the user divs that are already open
function refreshSideBar(groupName)
{
	var sideBarText = "";
	var j;
	var i;
	
 	sideBarText += "<p>" + groupName + "</p>";
	for (j=0;j<users.length; j++)
	{
		// leave open image if the user div was already open
		users[j].divOpen ? sideBarText += "<nobr><img src='" +TREE_OPEN_ICON  + "'" : sideBarText += "<nobr><img src='" +TREE_CLOSED_ICON + "'";
		
		sideBarText += " id='userTreeIcon" + users[j].id + "' onclick='javascript:makeUsersSideBarVisible(" + users[j].id + ");' />";
		sideBarText += " <a href='javascript:showSelectedUser(" + users[j].id + ");' title='" +users[j].name+ "'><span id='userSpan" + users[j].id +"'>" + users[j].name + "</span></a></nobr><br>";
		
		// leave the user div open if it was already open
		users[j].divOpen ? sideBarText += "<div style='display:block;'" : sideBarText += "<div style='display:none;'";
		sideBarText += "id='userdiv" + users[j].id + "'>";

		// add the markers created by this user to their div
		for (i=0;i<markers.length; i++)
		{
			if (markers[i].createdById == users[j].id && markers[i].state != "remove" && markers[i].state != "unsaved")
			{
				var sideBarTitle = markers[i].title;
				sideBarText += "&nbsp;&nbsp;&nbsp;&nbsp;<span id='markerSpan" + markers[i].sideBarIndex + "'><nobr>";
				sideBarText += "<a href='javascript:GEvent.trigger(markers[" + markers[i].sideBarIndex + "],\"click\");' ";
				sideBarText += "title='" + sideBarTitle + "' >" + sideBarTitle + "</a>"
				sideBarText += "</span></nobr><br />";
			}
		}
		sideBarText += "</div>";
	}
 	document.getElementById("usersidebar").innerHTML = sideBarText;
}

// Cancels the edit and returns to the view window for the marker
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

// Opens a marker's info window
function openInfoWindow(x)
{
	markers[x].openInfoWindowHtml(markers[x].infoWindowHtml);
}

// Uses the gmap geocoder to find an address for the user, and move the map center there
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
					alert("<fmt:message key="error.cantFindAddress"/> " + address );
				} 
				else 
				{
					map.setCenter(point, 13);
				}
			}
		);
	}
}

// Alters the zoom and center of the map to fit all the markers in the screen
function fitMapMarkers() 
{
   	var bounds = new GLatLngBounds();
	
	for (var i=0; i< markers.length; i++) 
	{
		if (markers[i] && markers[i].getPoint() && markers[i].state != "remove")
		{
			
			bounds.extend(markers[i].getPoint());	
		}
	}
	
   	var zoom = map.getBoundsZoomLevel(bounds) - 1;
   	
   	// max zoom set to 16 to prevent ambiguous map display
   	if (zoom > 16) {zoom = 16;}
   	
   	map.setZoom(zoom);
   	map.panTo(bounds.getCenter());
   
}

// string trim
function trim(x)
{
	return x.replace(/^\s+|\s+$/g, '')
}

// Update's the marker info window, and marker display based on it's state
function updateMarkerInfoWindowHtml(markerIn)
{
	if (markerIn.state == "unchanged")
	{
		markerIn.setImage(BLUE_MARKER_ICON);
	}
	else if (markerIn.state == "update" || markerIn.state == "save")
	{
		markerIn.setImage(LIGHTBLUE_MARKER_ICON);
	}
	else if (markerIn.state == "unsaved")
	{
		markerIn.setImage(RED_MARKER_ICON);
	}
	
	if (markerIn.highlight)
	{
		markerIn.setImage(YELLOW_MARKER_ICON);
	}
	
	if (markerIn.editingOn)
	{
		markerIn.linksBar = "<br/ >" + markerIn.saveLink + "&nbsp;" + markerIn.cancelLink;
		markerIn.inputForm ="<fmt:message key='label.authoring.basic.title'/>"
		markerIn.inputForm += "<br><input type='text' onkeypress='javascript:if (event.keyCode==13){" +markerIn.saveFuncStr+ "}' maxlength='50' size='50' id='markerTitle' name='markerTitle' value='" + markerIn.title +"' /><br>";
		markerIn.inputForm += "<fmt:message key='label.newInfoWindowText'/><br><textarea id='infoWindow' name='infoWindow' rows='5' cols='50'>" + markerIn.infoMessage + "</textarea>";
		markerIn.markerMetaData = "<span style='font-size:90%; color:#999999;'>";
		markerIn.markerMetaData += '<nobr><fmt:message key="label.latitudeLongitude"/> (' + markerIn.getPoint().lat() + "-" + markerIn.getPoint().lng() +")</nobr>";
		markerIn.markerMetaData += "</span>";
		markerIn.infoWindowHtml =  markerIn.inputForm + "<br />" +markerIn.markerMetaData + markerIn.linksBar;
	}
	else
	{
		markerIn.linksBar = ""
		if (markerIn.editAble)
		{
			markerIn.linksBar = "<br/ >" + markerIn.removeLink + "&nbsp;" + markerIn.editLink;
		}
		markerIn.markerMetaData = "<span style='font-size:90%; color:#999999;'>";
		markerIn.markerMetaData += "<fmt:message key="label.createdBy"/> " + markerIn.createdBy + "<br />";
		markerIn.markerMetaData += "<nobr><fmt:message key="label.latitudeLongitude"/> (" + markerIn.getPoint().lat() + "-" + markerIn.getPoint().lng() +")</nobr>";
		markerIn.markerMetaData += "</span>";
		markerIn.infoWindowHtml = "<h4>" + markerIn.title + "</h4><div style='overflow:auto; width:350px; height:80px'>" +  markerIn.infoMessage.replace(/\n/g, "<br />") + "</div>"+ markerIn.markerMetaData  + markerIn.linksBar;
	}	
}

// hack to ensure that the saved strings are safe to send in xml
function encode_utf8( s )
{
	// "'&<>\
	var re = new RegExp("[<>\\\\]", "g");
	var re1 = new RegExp('"', "g");
	var re2 = new RegExp("'", "g");
	var re3 = new RegExp("&", "g");
	s = s.replace(re, "_");
	s = s.replace(re1, "&quot;");
	s = s.replace(re2, "&apos;");
	s = s.replace(re3, "&amp;");
	return s;
}

// hack to ensure that the saved strings are safe to send in xml
function decode_utf8( s )
{
  	var re1 = new RegExp("&quot;", "g");
	var re2 = new RegExp("&apos;", "g");
	var re3 = new RegExp("&amp;", "g");
	s = s.replace(re1, '"');
	s = s.replace(re2, "'");
	s = s.replace(re3, "&");
	
  	return s;
}


function confirmLeavePage()
{
	var i =0;
	for (;i<markers.length;i++)
	{
		// Check if there are unsaved markers
		if (markers[i].state == "unsaved" || markers[i].state == "update")
		{
			var ans = confirm("<fmt:message key="label.unsavedMarkers" />");
			if (ans)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
	}
	return true;
}

// Serialises the marker array into an xml string for processing on the back end
function serialiseMarkers()
{
	if (!(markers))
	{
		var xmlString = '<?xml version="1.0"?><markers></markers>';
	} 
	else 
	{

	var xmlString = '<?xml version="1.0"?><markers>';
	var i =0;
	for (;i<markers.length;i++)
	{
		// Check if there are unsaved markers
		if (markers[i].state == "unsaved")
		{
			var ans = confirm("<fmt:message key="label.unsavedMarkers" />");
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
			' infoMessage="'+ encode_utf8(markers[i].infoMessage)+ '"' +
			' markerUID="'+ markers[i].uid + '"' +
			' title="'+ encode_utf8(markers[i].title) + '"' +
			' state="'+ markers[i].state + '"' +
			' />';
			xmlString += markerString;
		}	
	}
	xmlString += "</markers>"
	}
	document.getElementById("markersXML").value=xmlString;
	return true;
}

//-->
</script>
