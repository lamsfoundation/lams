<%@ include file="/common/taglibs.jsp"%>
<c:set var="formBean"value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
<c:set var="sessionMap" value="${sessionScope[formBean.sessionMapID]}" />
<c:set var="sessionMapID" value="${formBean.sessionMapID}" />
<c:set var="tool"><lams:WebAppURL /></c:set>
<!-- ========== Basic Tab ========== -->
<div class="form-group">
	<label for="title"><fmt:message key="label.authoring.basic.title"></fmt:message></label>
	<html:text property="title" styleClass="form-control"></html:text>
</div>

<div class="form-group">
	<label for="instructions"><fmt:message key="label.authoring.basic.instructions"></fmt:message></label>
	<lams:CKEditor id="instructions" value="${formBean.instructions}"
				contentFolderID="${sessionMap.contentFolderID}"></lams:CKEditor>
</div>

	<label><fmt:message key="label.authoring.basic.map"></fmt:message></label>
	
	<!-- map UI -->
	<div class="panel panel-default">
	<div class="panel-body">
	    
		<%@ include file="../../common/mapLegend.jsp"%>
	
		<div class="row no-gutter">
			<div class="col-sm-9 col-md-7">
				<div id="map_canvas" style="height:400px"><fmt:message key="error.cantLoadMap"></fmt:message></div>
			</div>
			<div class="col-sm-3 col-md-5">
				<div id="sidebar" style="border:1px"></div>		
			</div>	
		</div>
				
		<div class="row no-gutter">
			<div class="col-sm-12">
				<a href="javascript:addMarkerToCenter()" class="btn btn-default btn-sm voffset5" role="button"/><fmt:message key="button.addMarker"/></a>
				<a href="javascript:fitMapMarkers()" class="btn btn-default btn-sm  voffset5" role="button"/><fmt:message key="button.fitMarkers"/></a>
			</div>
		</div>
					
		<div class="row no-gutter voffset5">
			<div class="col-sm-12">
				<label><fmt:message key="label.authoring.basic.centerMap"></fmt:message></label>
			</div>
		</div>
		
		<div class="row no-gutter">
			<div class="col-sm-12">
				<c:set var="goText"><fmt:message key="button.go"/></c:set>
				<html:text property="defaultGeocoderAddress" size="60" styleId="address" onkeypress="javascript:if (event.keyCode==13){showAddress(); return false;}" 
					styleClass="form-control form-control-inline input-sm"></html:text>
	       		<a href="javascript:showAddress()" class="btn btn-default btn-sm"/><i class="fa fa-search" title="${goText}"></i></a>
			</div>
		</div>		
		
	</div>	
	</div>
	<!-- end map UI -->
	
<script type="text/javascript">
<!--

function initGmap()
{
	if (GBrowserIsCompatible()) 
	{
		//map = new GMap2(document.getElementById("map_canvas"), { size: new GSize(640,320) } );
		map = new GMap2(document.getElementById("map_canvas"), { size: new GSize(500,400) } );
    	map.addControl(new GLargeMapControl());
    	map.addControl(new GMapTypeControl());
    	map.addMapType(G_PHYSICAL_MAP); 

    	markers = new Array();
    	geocoder = new GClientGeocoder();

    	currUser = "<fmt:message key="label.authoring.basic.authored"></fmt:message>";
    	currUserId = '0';
		map.setCenter(new GLatLng('${formBean.gmap.mapCenterLatitude}', '${formBean.gmap.mapCenterLongitude}' ));
		map.setCenter(new GLatLng('${formBean.gmap.mapCenterLatitude}', '${formBean.gmap.mapCenterLongitude}' ));
		map.setZoom(${formBean.gmap.mapZoom});
		map.setMapType(${formBean.gmap.mapType});
		
		addAuthorMarkers();	
		refreshSideBarAuthoring();
		
    }
}

function addAuthorMarkers()
{
	<c:forEach var="marker" items="${formBean.gmap.gmapMarkers}">
	addMarker(new GLatLng('${marker.latitude}', 
		'${marker.longitude}' ), 
		decode_utf8('<c:out value="${marker.infoWindowMessage}" escapeXml="true"/>'), 
		decode_utf8('<c:out value="${marker.title}" escapeXml="true"/>'), 
		'${marker.uid}', 
		true, 
		true, 
		currUser, 
		currUserId);
	</c:forEach>		
}

-->
</script>
 		

