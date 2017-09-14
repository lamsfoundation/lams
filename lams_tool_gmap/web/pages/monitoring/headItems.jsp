<%@ include file="/common/taglibs.jsp"%>

<c:set var="tool">
	<lams:WebAppURL />
</c:set>

<script type="text/javascript" src="${tool}includes/javascript/monitoring.js"></script>
<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/portrait.js"></script>
<!-- Include the gmap API header -->
<c:choose>
	<c:when test="${gmapKey != null}">
		<script src="https://maps.google.com/maps?file=api&amp;v=2&amp;key=${gmapKey}" type="text/javascript"></script>
	</c:when>
	<c:otherwise>
		<script type="text/javascript">	
		<!--
			alert("<fmt:message key='error.gmapKeyMissing'/>");
		-->
		</script>
	</c:otherwise>
</c:choose>
<%@ include file="/includes/jsp/mapFunctions.jsp"%>

<%-- learning javascript map functions library is sufficient for monitor --%>
<script type="text/javascript" src="${tool}includes/javascript/mapFunctionsLearning.js"></script>

<script type="text/javascript">
<!--
var GBrowserIsCompatible = GBrowserIsCompatible || function() {return false;};

var YELLOW_MARKER_ICON = "${tool}/images/yellow_Marker.png";
var BLUE_MARKER_ICON = "${tool}/images/blue_Marker.png";
var LIGHTBLUE_MARKER_ICON = "${tool}/images/paleblue_Marker.png";
var RED_MARKER_ICON = "${tool}/images/red_Marker.png";
var TREE_CLOSED_ICON = "${tool}/images/tree_closed.gif";
var TREE_OPEN_ICON = "${tool}/images/tree_open.gif";
var errorMissingTitle = "<fmt:message key="error.missingMarkerTitle"/>";
var markerLimitMsg = "<fmt:message key="label.learner.markerLimitReached"/>"
var confirmDelete = "<fmt:message key="label.authoring.basic.confirmDelete"/>";

var map;
var markers;
var users;
var sessionReflectionDivs = new Array();
var geocoder = null;
var userMarkerCount =0;
var limitMarkers = ${gmapDTO.limitMarkers};
var markerLimit = ${gmapDTO.maxMarkers};

function initMonotorGmap()
{
	initializePortraitPopover('<lams:LAMSURL />');
	if (GBrowserIsCompatible()) 
	{
		//map = new GMap2(document.getElementById("map_canvas"), { size: new GSize(640,320) } );
		map = new GMap2(document.getElementById("map_canvas"), { size: new GSize(500,400) } );
		//map = new GMap2(document.getElementById("map_canvas"));
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

// Add a new marker to the center of the map for monitoring
function addMarkerToCenterMonitoring()
{
	var bounds = map.getBounds();
	var point = bounds.getCenter();
	addMarker(point, "", "", -1, false, true, "<fmt:message key="label.authoring.basic.authored"></fmt:message>", 0);
}

//-->
</script>
