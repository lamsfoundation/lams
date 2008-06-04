<%@ include file="/common/taglibs.jsp"%>

<c:set var="formBean"
	value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
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
			<table cellpadding='0' cellspacing='0' border='1'>
			<tr><td><div id="map_canvas" style="width: 400px; height: 300px;" ></div></td>
			<td><div id="sidebar" style="width:100px; 
									overflow:auto;
									height:320px; 
									background:WhiteSmoke; "></div></td></tr>
			</table>
			
			<a href="javascript:addMarkerToCenter()" class="button"/><fmt:message key="button.addMarker"/></a>
			<a href="javascript:fitMapMarkers()" class="button"/><fmt:message key="button.fitMarkers"/></a>
			<input type="button" onclick="test()"  value="Test" />
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
	initGmap();
	currUser = '${gmapUser.firstName} ${gmapUser.lastName}';
	map.setCenter(new GLatLng('${formBean.gmap.mapCenterLatitude}', '${formBean.gmap.mapCenterLongitude}' ));
	map.setCenter(new GLatLng('${formBean.gmap.mapCenterLatitude}', '${formBean.gmap.mapCenterLongitude}' ));
	map.setZoom(${formBean.gmap.mapZoom});
	map.setMapType(${formBean.gmap.mapType});
//-->
</script>

<c:forEach var="marker" items="${formBean.gmap.gmapMarkers}">
	<script type="text/javascript">
	<!--
		var savedPoint = new GLatLng('${marker.latitude}', '${marker.longitude}' );
		addMarker(savedPoint, '${marker.infoWindowMessage}', '${marker.title}', '${marker.uid}', true, true, "${marker.createdBy.firstName} ${marker.createdBy.lastName}");
	//-->
	</script>
</c:forEach>
   		
<script type="text/javascript">
<!--
	refreshSideBar();
//-->
</script>

