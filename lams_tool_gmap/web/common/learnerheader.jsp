<%@ include file="/common/taglibs.jsp"%>

<c:set var="lams">
	<lams:LAMSURL />
</c:set>
<c:set var="tool">
	<lams:WebAppURL />
</c:set>

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
<c:set var="lrnForm" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />

<lams:head>  
	<title>
		<fmt:message key="activity.title" />
	</title>
	<lams:css/>
	
	<script type="text/javascript" src="${lams}includes/javascript/common.js"></script>
	<script type="text/javascript" src="${tool}includes/javascript/mapFunctionsLearning.js"></script>

	<%@ include file="/includes/jsp/mapFunctions.jsp"%>

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
		
		var sessionName = '${gmapSessionDTO.sessionName}';
		
		var map;
		var markers;
		var users;
		var geocoder = null;
		var currUser;
		var currUserId;
		var userMarkerCount =0;
		var limitMarkers = ${gmapDTO.limitMarkers};
		var markerLimit = ${gmapDTO.maxMarkers};
		
		
		function initLearnerGmap()
		{
			if (GBrowserIsCompatible()) 
			{
				map = new GMap2(document.getElementById("map_canvas"));
				markers = new Array();
		    	users = new Array();
		    	geocoder = new GClientGeocoder();

				map.setCenter(new GLatLng('${gmapDTO.mapCenterLatitude}', '${gmapDTO.mapCenterLongitude}' ));
				map.setZoom(${gmapDTO.mapZoom});

				currUser = '<c:out value="${gmapUserDTO.firstName} ${gmapUserDTO.lastName}" escapeXml="true"/>';
				currUserId = '${gmapUserDTO.uid}'
				
				// Set up map controls
				map.addControl(new GMapTypeControl());
				<c:if test="${gmapDTO.allowZoom == true}">map.addControl(new GLargeMapControl());</c:if>
				<c:if test="${gmapDTO.allowTerrain == true}">map.addMapType(G_PHYSICAL_MAP);</c:if>
				<c:if test="${gmapDTO.allowSatellite == false}">map.removeMapType(G_SATELLITE_MAP);</c:if>
				<c:if test="${gmapDTO.allowHybrid == false}">map.removeMapType(G_HYBRID_MAP);</c:if>
				map.setMapType(${gmapDTO.mapType});
		    	
		    	
		    	
		    	addUserToList('0',"<fmt:message key="label.authoring.basic.authored"></fmt:message>" );
		    	<c:forEach var="user" items="${gmapSessionDTO.userDTOs}">
		    		addUserToList('${user.uid}','<c:out value="${user.firstName} ${user.lastName}" escapeXml="true"/>');
		    	</c:forEach>
		    	
		    	<c:forEach var="marker" items="${gmapSessionDTO.markerDTOs}">
					<c:choose>
					<c:when test="${marker.createdBy.loginName == gmapUserDTO.loginName && marker.isAuthored == false}">
					    addMarker(new GLatLng('${marker.latitude}', '${marker.longitude}' ), decode_utf8('<c:out value="${marker.infoWindowMessage}" escapeXml="true"/>'), decode_utf8('<c:out value="${marker.title}" escapeXml="true"/>'), '${marker.uid}', true, ${gmapDTO.allowEditMarkers}, '<c:out value="${marker.createdBy.firstName} ${marker.createdBy.lastName}" escapeXml="true"/>', '${marker.createdBy.uid}');	
						userMarkerCount ++;
					</c:when>
					<c:otherwise>
						<c:choose>
						<c:when  test="${marker.isAuthored}">
						addMarker(new GLatLng('${marker.latitude}', '${marker.longitude}' ), decode_utf8('<c:out value="${marker.infoWindowMessage}" escapeXml="true"/>'), decode_utf8('<c:out value="${marker.title}" escapeXml="true" />'), '${marker.uid}', true, false, "<fmt:message key="label.authoring.basic.authored"></fmt:message>", '0' );
						</c:when>
						<c:when  test="${gmapDTO.allowShowAllMarkers}">
						addMarker(new GLatLng('${marker.latitude}', '${marker.longitude}' ), decode_utf8('<c:out value="${marker.infoWindowMessage}" escapeXml="true"/>'), decode_utf8('<c:out value="${marker.title}" escapeXml="true"/>'), '${marker.uid}', true, false, '<c:out value="${marker.createdBy.firstName} ${marker.createdBy.lastName}" escapeXml="true"/>', '${marker.createdBy.uid}' );
						</c:when>
						</c:choose>
					</c:otherwise>
					</c:choose>
				</c:forEach>

		    	refreshSideBar(sessionName);
		    }
		}

	//-->
	
	
	</script>
</lams:head>
