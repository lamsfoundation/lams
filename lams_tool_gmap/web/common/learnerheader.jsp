<%@ include file="/common/taglibs.jsp"%>

<c:set var="lams">
	<lams:LAMSURL />
</c:set>
<c:set var="tool">
	<lams:WebAppURL />
</c:set>

<lams:head>  
	<title>
		<fmt:message key="activity.title" />
	</title>
	<link href="${tool}pages/learning/gmap_style.css" rel="stylesheet" type="text/css">
	<lams:css/>

	<link href="${lams}css/fckeditor_style.css" rel="stylesheet" type="text/css">

	<script type="text/javascript" src="${lams}includes/javascript/common.js"></script>
	<script type="text/javascript" src="${lams}fckeditor/fckeditor.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/fckcontroller.js"></script>
	<script type="text/javascript" src="${tool}includes/javascript/mapFunctionsLearning.js"></script>

	<%@ include file="/includes/jsp/mapFunctions.jsp"%>


	<script src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=ABQIAAAAvPAE96y1iioFQOnrP1RCBxS3S_A0Q4kgEfsHF6TMv6-oezFszBTPVN72_75MGlxr3nP_6ixxWd30jw" type="text/javascript"></script>
	<script type="text/javascript">
	<!--
		var webAppUrl = "${tool}";
		var errorMissingTitle = '<fmt:message key="error.missingMarkerTitle"/>';
		var errorCantFindLocation = '<fmt:message key="error.cantFindAddress"/>';
		var confirmDelete = '<fmt:message key="label.authoring.basic.confirmDelete"/>';
		var createdByMsg = '<fmt:message key="label.createdBy"/>';
		var latitudeLongitudeMsg = '<fmt:message key="label.latitudelongitude"/>';
		var titleMsg = '<fmt:message key="label.authoring.basic.title"/>';
		var newInfoWindowTextMsg = '<fmt:message key="label.newInfoWindowText"/>';
		var editMsg = '<fmt:message key="button.Edit"/>';
		var saveMsg = '<fmt:message key="button.Save"/>';
		var removeMsg = '<fmt:message key="button.Remove"/>';
		var cancelMsg = '<fmt:message key="button.Cancel"/>';
		var map;
		var markers;
		var geocoder = null;
		var currUser;
		var currUserId;
		var userMarkerCount =0;
		var limitMarkers = ${gmapDTO.limitMarkers};
		var markerLimit = 0;
		var markerLimit = ${gmapDTO.maxMarkers};
		function initLearnerGmap()
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
				currUserId = '${gmapUserDTO.uid}'
				
				// Set up map controls
				map.addControl(new GMapTypeControl());
				
				<c:if test="${gmapDTO.allowZoom == true}">
				map.addControl(new GLargeMapControl());
				</c:if>
				
				<c:if test="${gmapDTO.allowTerrain == true}">
				map.addMapType(G_PHYSICAL_MAP);
				</c:if>
				
				<c:if test="${gmapDTO.allowSatellite == false}">
				map.removeMapType(G_SATELLITE_MAP);
				</c:if>
				
				<c:if test="${gmapDTO.allowHybrid == false}">
				map.removeMapType(G_HYBRID_MAP);
				</c:if>
				
				map.setMapType(${gmapDTO.mapType});
		    	
		    	<c:forEach var="marker" items="${gmapDTO.gmapMarkers}">
					<c:choose>
						<c:when test="${marker.createdBy.loginName == gmapUserDTO.loginName && marker.isAuthored == false}">
						    addMarker(new GLatLng('${marker.latitude}', '${marker.longitude}' ),'${marker.infoWindowMessage}', '${marker.title}', '${marker.uid}', true, ${gmapDTO.allowEditMarkers}, '${marker.createdBy.firstName} ${marker.createdBy.lastName}', '${marker.createdBy.uid}');	
							userMarkerCount ++;
						</c:when>
						
						<c:otherwise>
							<c:choose>
								<c:when  test="${marker.isAuthored}">
								    addMarker(new GLatLng('${marker.latitude}', '${marker.longitude}' ), '${marker.infoWindowMessage}', '${marker.title}', '${marker.uid}', true, false, '<fmt:message key="label.authoring.basic.authored"></fmt:message>', '0' );
								</c:when>
								<c:when  test="${gmapDTO.allowShowAllMarkers}">
								    addMarker(new GLatLng('${marker.latitude}', '${marker.longitude}' ), '${marker.infoWindowMessage}', '${marker.title}', '${marker.uid}', true, false, '${marker.createdBy.firstName} ${marker.createdBy.lastName}', '${marker.createdBy.uid}' );
								</c:when>
							</c:choose>
						</c:otherwise>
					</c:choose>
				</c:forEach>

		    	refreshSideBar();
		    }
		}
	//-->
	</script>
	
		
	
	
</lams:head>
