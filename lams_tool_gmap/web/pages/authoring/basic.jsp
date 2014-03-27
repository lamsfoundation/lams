<%@ include file="/common/taglibs.jsp"%>
<c:set var="formBean"value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
<c:set var="sessionMap" value="${sessionScope[formBean.sessionMapID]}" />
<c:set var="sessionMapID" value="${formBean.sessionMapID}" />
<c:set var="tool"><lams:WebAppURL /></c:set>
<!-- ========== Basic Tab ========== -->
<table cellpadding="0" border='0'>
	<tr>
		<td>
			<div class="field-name">
				<fmt:message key="label.authoring.basic.title"></fmt:message>
			</div>
			<html:text property="title" style="width: 99%;"></html:text>
		</td>
	</tr>
	<tr>
		<td>
			<div class="field-name">
				<fmt:message key="label.authoring.basic.instructions"></fmt:message>
			</div>
			<lams:CKEditor id="instructions"
				value="${formBean.instructions}"
				contentFolderID="${sessionMap.contentFolderID}"></lams:CKEditor>
		</td>		
	</tr>
	<tr>
		<td>
			<div class="field-name">
				<fmt:message key="label.authoring.basic.map"></fmt:message>
			</div>
			
			<div id="map_legend" style="width:100%;" >
			<iframe marginwidth="0" align="left" height="60px" width="100%" frameborder="0" src="${tool}/common/mapLegend.jsp"></iframe>
			</div>
			<div id="map_canvas" style="float: left; width:80%; height:400px"><fmt:message key="error.cantLoadMap"></fmt:message></div>
			<div id="sidebar" style="float: right; width:20%; overflow:auto; height:400px; background:WhiteSmoke; "></div>		

		</td>
	</tr>
	<tr>
		<td>
			<a href="javascript:addMarkerToCenter()" class="button"/><fmt:message key="button.addMarker"/></a>
			<a href="javascript:fitMapMarkers()" class="button"/><fmt:message key="button.fitMarkers"/></a>
		</td>
	</tr>
	<tr>
		<td>
			<div class="field-name">
				<fmt:message key="label.authoring.basic.centerMap"></fmt:message>
			</div>
			
			<html:text property="defaultGeocoderAddress" size="60" styleId="address" onkeypress="javascript:if (event.keyCode==13){showAddress(); return false;}"></html:text>
       		<a href="javascript:showAddress()" class="button"/><fmt:message key="button.go"/></a>
		</td>
	</tr>
</table>

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
 		

