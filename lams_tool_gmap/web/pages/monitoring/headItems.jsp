<%@ include file="/common/taglibs.jsp"%>

<c:set var="tool">
	<lams:WebAppURL />
</c:set>

<script type="text/javascript" src="${tool}includes/javascript/monitoring.js"></script>

<%@ include file="/includes/jsp/mapFunctions.jsp"%>

<script type="text/javascript">
<!--

var YELLOW_MARKER_ICON = "${tool}/images/yellow_Marker.png";
var BLUE_MARKER_ICON = "${tool}/images/blue_Marker.png";
var LIGHTBLUE_MARKER_ICON = "${tool}/images/paleblue_Marker.png";
var RED_MARKER_ICON = "${tool}/images/red_Marker.png";
var TREE_CLOSED_ICON = "${tool}/images/tree_closed.gif";
var TREE_OPEN_ICON = "${tool}/images/tree_open.gif";

var map;
var markers;
var users;
var geocoder = null;
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

		
		// Set up map controls
		map.addControl(new GMapTypeControl());
		map.addControl(new GLargeMapControl());
		map.addMapType(G_PHYSICAL_MAP);
		
		// Set map type
		map.setMapType(${gmapDTO.mapType});
    }
}


function init()
{
	selectTab(1);
}

function clearMap()
{
	users= null;
	users = new Array();
	markers = null;
	markers = new Array();
	selectedUser = -1;
	selectedMarker = -1;
	
	
	refreshSideBar("");
	
	map.setCenter(new GLatLng('${gmapDTO.mapCenterLatitude}', '${gmapDTO.mapCenterLongitude}' ));
	map.setZoom(${gmapDTO.mapZoom});
	map.setMapType(${gmapDTO.mapType});
	map.clearOverlays();
}

//-->
</script>
	
	
	
	
	
	

