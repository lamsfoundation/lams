<%@ include file="/common/taglibs.jsp"%>
<c:set var="tool">
	<lams:WebAppURL />
</c:set>

<script type="text/javascript" src="${tool}includes/javascript/authoring.js"></script>
<script type="text/javascript" src="${tool}includes/javascript/mapFunctionsAuthoring.js"></script>

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

<script type="text/javascript">
<!--
	var GBrowserIsCompatible = GBrowserIsCompatible || function() {return false;};
	
	var errorMissingTitle = "<fmt:message key='error.missingMarkerTitle'/>";
	var confirmDelete = "<fmt:message key='label.authoring.basic.confirmDelete'/>";
	var authoredMarkerMsg = "<fmt:message key='label.authoring.authoredMarkers' />";
	var map;
	var markers;
	var geocoder = null;
	var currUser;
	var currUserId;
	var YELLOW_MARKER_ICON = "${tool}/images/yellow_Marker.png";
	var BLUE_MARKER_ICON = "${tool}/images/blue_Marker.png";
	var LIGHTBLUE_MARKER_ICON = "${tool}/images/paleblue_Marker.png";
	var RED_MARKER_ICON = "${tool}/images/red_Marker.png";
	var TREE_CLOSED_ICON = "${tool}/images/tree_closed.gif";
	var TREE_OPEN_ICON = "${tool}/images/tree_open.gif";
//-->
</script>

