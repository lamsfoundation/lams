<%@ include file="/common/taglibs.jsp"%>
<c:set var="formBean"value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
<c:set var="sessionMap" value="${sessionScope[formBean.sessionMapID]}" />
<c:set var="sessionMapID" value="${formBean.sessionMapID}" />
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
			<lams:FCKEditor id="instructions"
				value="${formBean.instructions}"
				contentFolderID="${sessionMap.contentFolderID}"></lams:FCKEditor>
		</td>		
	</tr>
	<tr>
		<td>
			<div class="field-name">
				<fmt:message key="label.authoring.basic.map"></fmt:message>
			</div>
			<!--  
			<div id="map_canvas" style="width: 500px; height: 300px"></div> 
			-->
			<table cellpadding='0' cellspacing='0' border='0'>
			<tr><td><div id="map_canvas" style="width: 400px; height: 300px;" ><fmt:message key="error.cantLoadMap"></fmt:message></div></td>
			<td><div id="sidebar" style="width:100px; overflow:auto;height:320px; background:WhiteSmoke; "></div></td></tr>
			<tr><td>
			<a href="javascript:addMarkerToCenter()" class="button"/><fmt:message key="button.addMarker"/></a>
			<a href="javascript:fitMapMarkers()" class="button"/><fmt:message key="button.fitMarkers"/></a>
			</td></tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<div class="field-name">
				<fmt:message key="label.authoring.basic.centerMap"></fmt:message>
			</div>
			<input type="text" size="60" name="address" id="address" value="<fmt:message key="label.authoring.basic.sampleAddress"></fmt:message>" />
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
		map = new GMap2(document.getElementById("map_canvas"), { size: new GSize(500,320) } );
    	map.addControl(new GLargeMapControl());
    	map.addControl(new GMapTypeControl());
    	map.addMapType(G_PHYSICAL_MAP); 

    	markers = new Array();
    	geocoder = new GClientGeocoder();
    	
    	//currUser = '${gmapUser.firstName} ${gmapUser.lastName}';
    	//currUserId = '${gmapUser.uid}';
    	currUser = '<fmt:message key="label.authoring.basic.authored"></fmt:message>';
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
	addMarker(new GLatLng('${marker.latitude}', '${marker.longitude}' ), '${marker.infoWindowMessage}', '${marker.title}', '${marker.uid}', true, true, currUser, currUserId);
	</c:forEach>		
}

-->
</script>
 		

