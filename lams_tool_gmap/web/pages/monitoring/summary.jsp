<%@ include file="/common/taglibs.jsp"%>

<c:set var="dto" value="${gmapDTO}" />

<table>
<tr>
	<th>
		<h1>Group:</h1>
	</th>
	<th>
		<h1><fmt:message key="heading.totalLearners" /></h1>
	</th>
</tr>

<c:if test="${empty dto.sessionDTOs}">
	<td><i>No groups found for lesson.</i></td>
</c:if>
<c:forEach var="session" items="${dto.sessionDTOs}">
		<tr>
			<td>
				<a href="javascript:clearMap();addUsersForSession${session.sessionID}();addMarkersForSession${session.sessionID}();">${session.sessionName}</a>
			</td>
			<td>
				${session.numberOfLearners}
			</td>
		</tr>
</c:forEach>
</table>	
	
<table style="cellpadding:0; cellspacing:0; border:0; width:500px;">
	<tr>
		<td width="80%">
		<div id="map_canvas" style="width:400px;height:300px;" ><fmt:message key="error.cantLoadMap"></fmt:message></div></td>
		<td width="20%">
		<div id="usersidebar" style="width:100px; overflow:auto;height:320px; background:WhiteSmoke; "></div>
		</td>
	</tr>
	<tr>
		<td>
		<a href="javascript:fitMapMarkers()" class="button"/><fmt:message key="button.fitMarkers"/></a>
		</td>
	</tr>
</table>

<table>
<tr><td>
	<input type="text" size="60" name="address" id="address" value="<fmt:message key="label.authoring.basic.sampleAddress"></fmt:message>" />
		<a href="javascript:showAddress()" class="button"/><fmt:message key="button.go"/></a>
</td></tr>
</table>
	
	
	
<script type="text/javascript">
<!--
	initMonotorGmap();
	
	<c:forEach var="session" items="${dto.sessionDTOs}" varStatus="status">
	
	function addUsersForSession${session.sessionID}()
	{
		addUserToList('0','<fmt:message key="label.authoring.basic.authored"></fmt:message>' );
		<c:forEach var="user" items="${session.userDTOs}">
			addUserToList('${user.uid}','${user.firstName} ${user.lastName}' );
		</c:forEach>
	}
	
	function addMarkersForSession${session.sessionID}()
	{
		<c:forEach var="marker" items="${session.markerDTOs}">
			<c:choose>
			<c:when test="${marker.isAuthored == true}">
				addMarker(new GLatLng('${marker.latitude}', '${marker.longitude}' ), '${marker.infoWindowMessage}', '${marker.title}', '${marker.uid}', true, false, '${marker.createdBy.firstName} ${marker.createdBy.lastName} (<fmt:message key="label.authoring.basic.authored"></fmt:message>)', '0');
			</c:when>
			<c:otherwise>
				addMarker(new GLatLng('${marker.latitude}', '${marker.longitude}' ), '${marker.infoWindowMessage}', '${marker.title}', '${marker.uid}', true, false, '${marker.createdBy.firstName} ${marker.createdBy.lastName}', '${marker.createdBy.uid}');
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
	</c:if>
	</c:forEach>
	
	
//-->
</script>
	

