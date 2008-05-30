<%@ include file="/common/taglibs.jsp"%>

<c:set var="formBean"
	value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
<c:set var="sessionMap" value="${sessionScope[formBean.sessionMapID]}" />
<c:set var="sessionMapID" value="${formBean.sessionMapID}" />

<!-- ========== Basic Tab ========== -->
<table cellpadding="0">
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
				Map:
			</div>
			<div id="map_canvas" style="width: 500px; height: 300px"></div>
			
			<input type="button" onclick="addMarkerToCenter()"  value="Add Marker" />
			<input type="button" onclick="fitMapMarkers()" value="Fit Markers" />
			<input type="button" onclick="test()"  value="Test" />
			<a href='map.jsp' class='thickbox'>Edit</a>
		</td>
	</tr>
	
	<tr>
		<td>
			<div class="field-name">
				Center map on address:
			</div>
			<input type="text" size="60" name="address" id="address" value="Macquarie University, Sydney NSW" />
       		<input type="button" onclick="showAddress()" value="Go!" />
       		
       		
       		
       		<script type="text/javascript">
			<!--
	       		initGmap();
       		//-->
			</script>
       		<c:forEach var="marker" items="${formBean.gmap.gmapMarkers}">
	        	<script type="text/javascript">
				<!--
					var savedPoint = new GLatLng('${marker.latitude}', '${marker.longitude}' );
					addMarker(savedPoint, '${marker.infoWindowMessage}', '${marker.uid}', true);
				//-->
				</script>
      		</c:forEach>
      		<script type="text/javascript">
			<!--
	       		fitMapMarkers();
       		//-->
			</script>

		</td>
	</tr>
</table>

