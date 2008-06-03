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
	
	<script type="text/javascript" src="${tool}includes/javascript/mapFunctions.js"></script>

	<script src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=ABQIAAAAvPAE96y1iioFQOnrP1RCBxS3S_A0Q4kgEfsHF6TMv6-oezFszBTPVN72_75MGlxr3nP_6ixxWd30jw" type="text/javascript"></script>
	<script type="text/javascript">
	<!--
		var webAppUrl = "${tool}";
		var errorMissingTitle = '<fmt:message key="error.missingMarkerTitle"/>';
		var errorCantFindLocation = '<fmt:message key="error.cantFindAddress"/>'
		var confirmDelete = '<fmt:message key="label.authoring.basic.confirmDelete"/>'
		var map;
		var markers;
		var geocoder = null;
		function initLearnerGmap()
		{
			if (GBrowserIsCompatible()) 
			{
				//map = new GMap2(document.getElementById("map_canvas"), { size: new GSize(640,320) } );
				map = new GMap2(document.getElementById("map_canvas"), { size: new GSize(500,320) } );
				markers = new Array();
		    	geocoder = new GClientGeocoder();

				map.setCenter(new GLatLng('${gmapDTO.mapCenterLatitude}', '${gmapDTO.mapCenterLongitude}' ));
				map.setCenter(new GLatLng('${gmapDTO.mapCenterLatitude}', '${gmapDTO.mapCenterLongitude}' ));
				map.setZoom(${gmapDTO.mapZoom});

				if (${gmapDTO.allowEditMarkers})
				{
				
				}
				
				// Set up map controls
				map.addControl(new GMapTypeControl());
				if (${gmapDTO.allowZoom}) {map.addControl(new GLargeMapControl());}
				if (${gmapDTO.allowTerrain}) {map.addMapType(G_PHYSICAL_MAP);}
				if (${gmapDTO.allowSatellite}) {map.removeMapType(G_SATELLITE_MAP);}
				if (${gmapDTO.allowHybrid}) {map.removeMapType(G_HYBRID_MAP);}
				map.setMapType(${gmapDTO.mapType});
		    	
		    	<c:forEach var="marker" items="${gmapDTO.gmapMarkers}">
					
					addMarker(new GLatLng('${marker.latitude}', '${marker.longitude}' ), '${marker.infoWindowMessage}', '${marker.title}', '${marker.uid}', true);
					
					if (${gmapDTO.allowEditMarkers}){}
					
					if (${gmapDTO.allowShowAllMarkers}){}
				</c:forEach>
				
				
				var limitMarkers = ${gmapDTO.limitMarkers};
				var markerLimit = markers.length + ${gmapDTO.maxMarkers};
		    }
		}
	//-->
	</script>
	
		
	
	
</lams:head>
