<!DOCTYPE html>

<%@ include file="/common/taglibs.jsp"%>

<!-- Include the gmap API header -->
	<c:choose>
		<c:when test="${gmapKey != null}">
			<script src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=${gmapKey}" type="text/javascript"></script>
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

<c:set var="tool">
	<lams:WebAppURL />
</c:set>

<html>
	<lams:head>
	
		<title><c:out value="${gmapDTO.title}" escapeXml="true" />
		</title>
		<lams:css localLinkPath="../" />
		
		<script type="text/javascript">
		<!--
		var GBrowserIsCompatible = GBrowserIsCompatible || function() {return false;};
		
		var YELLOW_MARKER_ICON = "tool_images/yellow_Marker.png";
		var BLUE_MARKER_ICON = "tool_images/blue_Marker.png";
		var LIGHTBLUE_MARKER_ICON = "tool_images/paleblue_Marker.png";
		var RED_MARKER_ICON = "tool_images/red_Marker.png";
		var TREE_CLOSED_ICON = "tool_images/tree_closed.gif";
		var TREE_OPEN_ICON = "tool_images/tree_open.gif";
		
		var map;
		var markers;
		var users = new Array();
		var geocoder = null;
		var selectedUser = -1;
		var	selectedMarker = -1;
		
		function initExportGmap()
		{
			if (GBrowserIsCompatible()) 
			{
				//map = new GMap2(document.getElementById("map_canvas"), { size: new GSize(640,320) } );
				//map = new GMap2(document.getElementById("map_canvas"), { size: new GSize(500,320) } );
				map = new GMap2(document.getElementById("map_canvas"));
				markers = new Array();
		    	geocoder = new GClientGeocoder();
		    	
				map.setCenter(new GLatLng('${gmapDTO.mapCenterLatitude}', '${gmapDTO.mapCenterLongitude}' ));
				map.setZoom(${gmapDTO.mapZoom});
				
								
				// Set up map controls
				map.addControl(new GMapTypeControl());
				if (${mode == "teacher"}) 
				{
					map.addControl(new GLargeMapControl());
					map.addMapType(G_PHYSICAL_MAP);
				}
				else
				{
					<c:if test="${gmapDTO.allowZoom == true}">map.addControl(new GLargeMapControl());</c:if>
					<c:if test="${gmapDTO.allowTerrain == true}">map.addMapType(G_PHYSICAL_MAP);</c:if>
					<c:if test="${gmapDTO.allowSatellite == false}">map.removeMapType(G_SATELLITE_MAP);</c:if>
					<c:if test="${gmapDTO.allowHybrid == false}">map.removeMapType(G_HYBRID_MAP);</c:if>
				}
				
				// Set map type
				map.setMapType(${gmapDTO.mapType});
		    }
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
		
		//-->
		</script>
	
	</lams:head>

	<body class="stripes">

			<div id="content">

			<h1>
				<c:out value="${gmapDTO.title}" escapeXml="true" />
			</h1>

			<p>
				<c:out value="${gmapDTO.instructions}" escapeXml="false" />
			</p>
			<br />
			<c:choose>
			<c:when test='${mode == "teacher"}'>
				<div id="sessionContents">
					<table width="300px" class="alternative-color">
					<tr>
						<th>
							<fmt:message key="heading.table.group" />
						</th>
						<th>
							<fmt:message key="heading.totalLearners" />
						</th>
					</tr>
					
					<c:if test="${empty gmapDTO.sessionDTOs}">
						<td><i>No groups found for lesson.</i></td>
					</c:if>
					<c:forEach var="session" items="${gmapDTO.sessionDTOs}">
							<tr>
								<td>
									<a href="javascript:clearMap();addUsersForSession${session.sessionID}();addMarkersForSession${session.sessionID}();makeReflectionDivVisible('reflectionDiv${session.sessionID}');">${session.sessionName}</a>
								</td>
								<td>
									${session.numberOfLearners}
								</td>
							</tr>
					</c:forEach>
					</table>
				</div>
			</c:when>
			<c:otherwise>
				<br>
				<h2>${sessionDTO.sessionName}</h2>
			</c:otherwise>
			</c:choose>
			
			<br /><br />	
			
			<table>
				<tr>
					 <td>
					 	<div id="map_canvas" style="float: left; width:80%; height:400px"><fmt:message key="error.cantLoadMap"></fmt:message></div>
						<div id="usersidebar" style="float: right; width:20%; overflow:auto; height:400px; background:WhiteSmoke; "></div>			 	
					 </td> 			
				</tr>
				<tr>
					<td>
					<a href="javascript:fitMapMarkers()" class="button"/><fmt:message key="button.fitMarkers"/></a>
					</td>
				</tr>
			</table>

			<br /><br />	
			<table>
			<tr><td>
				<input type="text" onkeypress="javascript:if (event.keyCode==13){showAddress();return false;}" size="60" name="address" id="address" value="${dto.defaultGeocoderAddress}" />
				<a href="javascript:showAddress()" class="button"/><fmt:message key="button.go"/></a>
			</td></tr>
			</table>	
			
			
			<c:choose>
			<c:when test='${mode == "teacher"}'>
				<c:if test="${gmapDTO.reflectOnActivity}">
				<br /><br />
				<c:forEach var="session" items="${gmapDTO.sessionDTOs}">
					<div id="reflectionDiv${session.sessionID}" style="display:none;">
					<h1>${session.sessionName} <fmt:message key="heading.reflection"></fmt:message></h1><br />
					
					<table class="alternative-color">
					<c:forEach var="user" items="${session.userDTOs}">
						<tr>
							<td width="15%">
								<span style="font-weight: bold"><c:out value="${user.firstName} ${user.lastName}" escapeXml="true"/></span>
							</td>
							<td width="85%">
								<c:if test="${user.finishedReflection}">
									<lams:out value="${user.notebookEntry}" escapeHtml="true"/>
								</c:if>
							</td>
						</tr>	
					</c:forEach>
					</table>
					</div>
				</c:forEach>
				</c:if>
			</c:when>
			<c:otherwise>
				<c:if test="${gmapDTO.reflectOnActivity}">
					<br /><br />
					<h1><fmt:message key="heading.reflection"></fmt:message></h1>
					<p>
						<span style="font-weight: bold"><c:out value="${gmapUserDTO.firstName} ${gmapUserDTO.lastName}" escapeXml="true"/> </span>
						<br />
						<lams:out value="${gmapUserDTO.notebookEntry}" escapeHtml="true"/>
					</p>
				</c:if>
			</c:otherwise>
			
			</c:choose>	
				
			</div>
			<!--closes content-->

			<div id="footer">
			</div>
			<!--closes footer-->
	</body>
	
	<script type="text/javascript">
	<!--
		initExportGmap();
		
		<c:choose>
		<c:when test='${mode == "teacher"}'>
			<c:forEach var="session" items="${gmapDTO.sessionDTOs}" varStatus="status">
			function addUsersForSession${session.sessionID}()
			{
				addUserToList('0',"<fmt:message key="label.authoring.basic.authored"></fmt:message>" );
				<c:forEach var="user" items="${session.userDTOs}">
					addUserToList('${user.uid}','<c:out value="${user.firstName} ${user.lastName}" escapeXml="true"/>' );
				</c:forEach>
			}
			
			function addMarkersForSession${session.sessionID}()
			{
				<c:forEach var="marker" items="${session.markerDTOs}">
					<c:choose>
					<c:when test="${marker.isAuthored == true}">
						addMarker(new GLatLng('${marker.latitude}', '${marker.longitude}' ), decode_utf8('<c:out value="${marker.infoWindowMessage}" escapeXml="true"/>'), decode_utf8('<c:out value="${marker.title}" escapeXml="true"/>'), '${marker.uid}', true, false, '<c:out value="${marker.createdBy.firstName} ${marker.createdBy.lastName}" escapeXml="true"/> (<fmt:message key="label.authoring.basic.authored"></fmt:message>)', '0');
					</c:when>
					<c:otherwise>
						addMarker(new GLatLng('${marker.latitude}', '${marker.longitude}' ), decode_utf8('<c:out value="${marker.infoWindowMessage}" escapeXml="true"/>'), decode_utf8('<c:out value="${marker.title}" escapeXml="true"/>'), '${marker.uid}', true, false, '<c:out value="${marker.createdBy.firstName} ${marker.createdBy.lastName}" escapeXml="true"/>', '${marker.createdBy.uid}');
					</c:otherwise>
				</c:choose>
				</c:forEach>
				refreshSideBar("${session.sessionName}");
				fitMapMarkers();
			}
			
			<c:if test="${status.first}" >
				clearMap();
				addUsersForSession${session.sessionID}();
				addMarkersForSession${session.sessionID}();
				makeReflectionDivVisible('reflectionDiv${session.sessionID}');
			</c:if>
			</c:forEach>
		</c:when>
		<c:otherwise>
		
			addUserToList('0',"<fmt:message key="label.authoring.basic.authored"></fmt:message>" );
			<c:forEach var="user" items="${sessionDTO.userDTOs}">
				addUserToList('${user.uid}','${user.firstName} ${user.lastName}' );
			</c:forEach>
			
			<c:forEach var="marker" items="${gmapDTO.gmapMarkers}">							
				<c:choose>
					<c:when test="${marker.isAuthored == true}">
						addMarker(new GLatLng('${marker.latitude}', '${marker.longitude}' ), decode_utf8('<c:out value="${marker.infoWindowMessage}" escapeXml="true"/>'), decode_utf8('<c:out value="${marker.title}" escapeXml="true"/>'), '${marker.uid}', true, false, '<c:out value="${marker.createdBy.firstName} ${marker.createdBy.lastName}" escapeXml="true"/> (<fmt:message key="label.authoring.basic.authored"></fmt:message>)', '0');
					</c:when>
					<c:otherwise>
						<c:choose>
							<c:when test="${gmapDTO.allowShowAllMarkers == true || marker.createdBy.loginName == gmapUserDTO.loginName}">
								addMarker(new GLatLng('${marker.latitude}', '${marker.longitude}' ), decode_utf8('<c:out value="${marker.infoWindowMessage}" escapeXml="true"/>'), decode_utf8('<c:out value="${marker.title}" escapeXml="true"/>'), '${marker.uid}', true, false, '<c:out value="${marker.createdBy.firstName} ${marker.createdBy.lastName}" escapeXml="true"/>', '${marker.createdBy.uid}');
							</c:when>
						</c:choose>
					</c:otherwise>
				</c:choose>	
			</c:forEach>
			
			refreshSideBar("${sessionDTO.sessionName}");
			fitMapMarkers();
		</c:otherwise>	
		</c:choose>
		
		function makeReflectionDivVisible(id)
		{
			var i;
			if (document.getElementById(id) != null)
			{
				<c:forEach var="session" items="${gmapDTO.sessionDTOs}">
					document.getElementById("reflectionDiv${session.sessionID}").style.display = "none";
				</c:forEach>
				document.getElementById(id).style.display = "block";
			}
		}
		
	//-->
	</script>
	
</html>

