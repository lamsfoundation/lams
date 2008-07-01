<%@ include file="/common/taglibs.jsp"%>

<c:set var="tool">
	<lams:WebAppURL />
</c:set>

<script type="text/javascript" src="${tool}includes/javascript/monitoring.js"></script>
<script type="text/javascript" src="${tool}includes/javascript/mapFunctionsMonitoring.js"></script>
<%@ include file="/includes/jsp/mapFunctions.jsp"%>

<script src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=ABQIAAAAvPAE96y1iioFQOnrP1RCBxS3S_A0Q4kgEfsHF6TMv6-oezFszBTPVN72_75MGlxr3nP_6ixxWd30jw" type="text/javascript"></script>	
<script type="text/javascript">
<!--
var webAppUrl = "${tool}";
var map;
var markers;
var users;
var geocoder = null;
var currUser;
var userMarkerCount =0;
var limitMarkers = ${gmapDTO.limitMarkers};
var markerLimit = ${gmapDTO.maxMarkers};

function initMonotorGmap()
{
	if (GBrowserIsCompatible()) 
	{
		//map = new GMap2(document.getElementById("map_canvas"), { size: new GSize(640,320) } );
		map = new GMap2(document.getElementById("map_canvas"), { size: new GSize(500,320) } );
		markers = new Array();
    	geocoder = new GClientGeocoder();
    	
		map.setCenter(new GLatLng('${gmapDTO.mapCenterLatitude}', '${gmapDTO.mapCenterLongitude}' ));
		map.setZoom(${gmapDTO.mapZoom});

		currUser = '${gmapUserDTO.firstName} ${gmapUserDTO.lastName}';
		
		// Set up map controls
		map.addControl(new GMapTypeControl());
		map.addControl(new GLargeMapControl());
		map.addMapType(G_PHYSICAL_MAP);
		
		// Set map type
		map.setMapType(${gmapDTO.mapType});
    	
    	<c:forEach var="marker" items="${gmapDTO.gmapMarkers}">
    		<c:choose>
				<c:when test="${marker.isAuthored == true}">
					addMarker(new GLatLng('${marker.latitude}', '${marker.longitude}' ), '${marker.infoWindowMessage}', '${marker.title}', '${marker.uid}', true, false, '${marker.createdBy.firstName} ${marker.createdBy.lastName} (<fmt:message key="label.authoring.basic.authored"></fmt:message>)', '0');
				</c:when>
				<c:otherwise>
					addMarker(new GLatLng('${marker.latitude}', '${marker.longitude}' ), '${marker.infoWindowMessage}', '${marker.title}', '${marker.uid}', true, false, '${marker.createdBy.firstName} ${marker.createdBy.lastName}', '${marker.createdBy.uid}');
				</c:otherwise>
			</c:choose>
		</c:forEach>
		
    	refreshSideBarMonitor();
    	fitMapMarkers();
    }
}

function addUserToList(id, name)
{
	var user = new Object();
	user.name = name;
	user.id = id;
	users[users.length] = user;
}

function refreshSideBarMonitor()
{
	var sideBarText = "";
	
	var j=0;
	var i;
	
	//sideBarText += "<a href='javascript:refreshSideBar()'>View All</a><br>";
	for (;j<users.length; j++)
	{
		sideBarText += "<nobr><img src='" + webAppUrl + "/images/tree_closed.gif' id='userTreeIcon" + users[j].id + "' onclick='javascript:makeUsersSideBarVisible(" + users[j].id + ");' />";
		sideBarText += "<a href='javascript:makeUsersSideBarVisible(" + users[j].id + ");'> " + users[j].name + "</a></nobr><br>";
		sideBarText += "<div style='display:none;' id='userdiv" + users[j].id + "'>";
		for (i=0;i<markers.length; i++)
		{
			if (markers[i].createdById == users[j].id)
			{
				sideBarText += "<span class='sidebar'><nobr>";
				sideBarText += "&nbsp;&nbsp;&nbsp;&nbsp;<a href='javascript:GEvent.trigger(markers[" + markers[i].sideBarIndex + "],\"click\")' ";
				sideBarText += "title='" + markers[i].createdBy + "' >" + markers[i].title + "</a>"
				sideBarText += "</span></nobr><br />";
			}
			
		}
		sideBarText += "</div>";
	}
	document.getElementById("usersidebar").innerHTML = sideBarText;
}



function makeUsersSideBarVisible(id)
{
	
	var div = document.getElementById("userdiv" + id)
	
	if (div.style.display == "block")
	{
		document.getElementById("userdiv" + id).style.display = "none";
		document.getElementById("userTreeIcon" + id).src = webAppUrl + "/images/tree_closed.gif";
	}
	else if (div.style.display == "none")
	{
		document.getElementById("userdiv" + id).style.display = "block";
		document.getElementById("userTreeIcon" + id).src = webAppUrl + "/images/tree_open.gif";
	}
}

/*
function makeUsersSideBarVisible(id)
{
	var sideBarText = "";
	var i=0;
	for (;i<markers.length; i++)
	{
		
		if (markers[i].state != "remove" && markers[i].state != "unsaved" && markers[i].createdById == id)
		{
			alert(markers[i].title + " Createdby: " + markers[i].createdBy + " " + markers[i].createdById + "\n" + id);
			//sideBarText += "<a href='javascript:makeUsersSideBarVisible('" + markers[i].uid "');'>" + markers[i].createdBy + "</a>";
			//sideBarText += "<div id='user" + markers[i].uid"' visible='false'>";
			sideBarText += "<span class='sidebar'>";
			sideBarText += "<a href='javascript:GEvent.trigger(markers[" + markers[i].sideBarIndex + "],\"click\")' ";
			sideBarText += "title='" + markers[i].createdBy + "' >" + markers[i].title + "</a>"
			sideBarText += "</span><br />";
			//sideBarText += "</div>";
			
		}
	}
	document.getElementById("sidebar").innerHTML = sideBarText;
}
*/

function init()
{
	selectTab(1);
	initMonotorGmap();
}


//-->
</script>
	
	
	
	
	
	

