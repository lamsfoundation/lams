<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration" %>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys" %>

<c:set var="tool">
	<lams:WebAppURL />
</c:set>



<script src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=<%= Configuration.get(ConfigurationKeys.GMAP_KEY) %>" type="text/javascript"></script>


<script type="text/javascript">
<!--

var selectedUser = -1;
var selectedMarker = -1;
var currentOpenMarkerImportance= 0;


// add a marker at the given point
function addMarker(point, infoMessage, title, uid, isSaved, editAble, createdBy, createdById)
{
	map.closeInfoWindow();
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
    	showSelectedMarkerSideBar(marker.sideBarIndex);
    	currentOpenMarkerImportance = marker.importance;
    	marker.importance = 3;
    });
    
    GEvent.addListener(marker, "infowindowclose", function() {
    	updateMarkerInfoWindowHtml(marker);
    	showSelectedMarkerSideBar(marker.sideBarIndex);
    	marker.importance = currentOpenMarkerImportance
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
    
	//marker.sideBarLinkPrefix = "<span class='sidebar'><a href='javascript:GEvent.trigger(markers["+markers.length+"],\"click\")'";
    marker.highlight = false;
    marker.removeLink = "<a href='javascript:removeMarker(" + markers.length + ")'><fmt:message key='button.remove'/></a>" ;
   	marker.editLink = "<a href='javascript:editMarker(" + markers.length + ")'><fmt:message key='button.edit'/></a>";
   	marker.saveFuncStr = "saveMarkerInfo(" + markers.length + ");";
   	marker.saveLink = "<a href='javascript:" +marker.saveFuncStr+ "'><fmt:message key='button.save'/></a>";
   	marker.cancelLink = "<a href='javascript:cancelEditMarkerInfo(" + markers.length + ")'><fmt:message key='button.cancel'/></a>";
    updateMarkerInfoWindowHtml(marker);
    markers[markers.length] = marker;
    
    if (!isSaved) {openInfoWindow(markers.length - 1);}
}

function addUserToList(id, name)
{
	var user = new Object();
	user.name = name;
	user.id = id;
	users[users.length] = user;
}

function importanceOrder (marker,b) 
{
	return GOverlay.getZIndex(marker.getPoint().lat()) + marker.importance*1000000;
}

function makeUsersSideBarVisible(id)
{
	var div = document.getElementById("userdiv" + id);
	
	if (div.style.display == "block")
	{
		document.getElementById("userdiv" + id).style.display = "none";
		document.getElementById("userTreeIcon" + id).src = TREE_CLOSED_ICON;
	}
	else if (div.style.display == "none")
	{
		document.getElementById("userdiv" + id).style.display = "block";
		document.getElementById("userTreeIcon" + id).src = TREE_OPEN_ICON;
		
	}
}

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
			//markers[i].setImage(YELLOW_MARKER_ICON);
			markers[i].highlight=true;
		}
		else
		{
			//markers[i].setImage(BLUE_MARKER_ICON);
			markers[i].highlight=false;
		}
		updateMarkerInfoWindowHtml(markers[i]);
	}
	
}

function showSelectedMarkerSideBar(id)
{
	
	//alert ("Marker to select: " +id+ "\nSelected marker: " + selectedMarker);
	
	var selectedMarkerSpan = document.getElementById("markerSpan" + selectedMarker);
	var markerSpanToSelect = document.getElementById("markerSpan" + id);
	
	if (selectedMarker == -1 && markerSpanToSelect != null)
	{
		document.getElementById("markerSpan" + id).style.backgroundColor = "yellow";
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
		document.getElementById("markerSpan" + id).style.backgroundColor = "yellow";
		selectedMarker = id;
	}
}

function refreshSideBar(groupName)
{
	var sideBarText = "";
	
	var j;
	var i;
	
	//sideBarText += "<a href='javascript:refreshSideBar()'>View All</a><br>";
	sideBarText += "<nobr><h2>" + groupName + "</h2></nobr>";
	for (j=0;j<users.length; j++)
	{
		sideBarText += "<nobr><img src='" +TREE_CLOSED_ICON+ "' id='userTreeIcon" + users[j].id + "' onclick='javascript:makeUsersSideBarVisible(" + users[j].id + ");' />";
		sideBarText += " <a href='javascript:showSelectedUser(" + users[j].id + ");' title='" +users[j].name+ "'><span id='userSpan" + users[j].id +"'>" + users[j].name + "</span></a></nobr><br>";
		sideBarText += "<div style='display:none;' id='userdiv" + users[j].id + "'>";
		for (i=0;i<markers.length; i++)
		{
			if (markers[i].createdById == users[j].id && markers[i].state != "remove" && markers[i].state != "unsaved")
			{
				sideBarText += "&nbsp;&nbsp;&nbsp;&nbsp;<span id='markerSpan" + markers[i].sideBarIndex + "'><nobr>";
				sideBarText += "<a href='javascript:GEvent.trigger(markers[" + markers[i].sideBarIndex + "],\"click\");' ";
				sideBarText += "title='" + markers[i].title + "' >" + markers[i].title + "</a>"
				sideBarText += "</span></nobr><br />";
			}
		}
		sideBarText += "</div>";
	}
	document.getElementById("usersidebar").innerHTML = sideBarText;
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
					alert('<fmt:message key="error.cantFindAddress"/> ' + address );
				} 
				else 
				{
					map.setCenter(point, 13);
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
		if (markers[i] && markers[i].getPoint() && markers[i].state != "remove")
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
		markerIn.markerMetaData = "<font size='tiny' color='grey'>";
		markerIn.markerMetaData += '<nobr><fmt:message key="label.latitudeLongitude"/> (' + markerIn.getPoint().lat() + "-" + markerIn.getPoint().lng() +")</nobr>";
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
		markerIn.markerMetaData += '<fmt:message key="label.createdBy"/> ' + markerIn.createdBy + "<br />";
		markerIn.markerMetaData += '<nobr><fmt:message key="label.latitudeLongitude"/> (' + markerIn.getPoint().lat() + "-" + markerIn.getPoint().lng() +")</nobr>";
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
		if (markers[i].state == "unsaved")
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





function test()
{
	serialiseMarkers();
	alert(document.getElementById("markersXML").value);
}
//-->
</script>