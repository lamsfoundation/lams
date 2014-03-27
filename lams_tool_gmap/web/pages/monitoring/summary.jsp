<%@ include file="/common/taglibs.jsp"%>
<c:set var="tool"><lams:WebAppURL /></c:set>
<c:set var="dto" value="${gmapDTO}" />


<h1>
	<img src="<lams:LAMSURL/>/images/tree_closed.gif" id="treeIcon" onclick="javascript:toggleAdvancedOptionsVisibility(document.getElementById('advancedDiv'), document.getElementById('treeIcon'), '<lams:LAMSURL/>');" />

	<a href="javascript:toggleAdvancedOptionsVisibility(document.getElementById('advancedDiv'), document.getElementById('treeIcon'),'<lams:LAMSURL/>');" >
		<fmt:message key="monitor.summary.th.advancedSettings" />
	</a>
</h1>
<br />

<div class="monitoring-advanced" id="advancedDiv" style="display:none">
<table class="alternative-color">

	<tr>
		<td>
			<fmt:message key="advanced.lockOnFinished" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${dto.lockOnFinish == true}">
					<fmt:message key="label.on" />
				</c:when>
				<c:otherwise>
					<fmt:message key="label.off" />
				</c:otherwise>
			</c:choose>	
		</td>
	</tr>
	
	<tr>
		<td>
			<fmt:message key="advanced.allowEditMarkers" />
		</td>
		<td>
			<c:choose>
				<c:when test="${dto.allowEditMarkers == true}">
					<fmt:message key="label.on" />
				</c:when>
				<c:otherwise>
					<fmt:message key="label.off" />
				</c:otherwise>
			</c:choose>	
		</td>
	</tr>

	<tr>
		<td>
			<fmt:message key="advanced.allowShowAllMarkers" />
		</td>
		<td>	
			<c:choose>
				<c:when test="${dto.allowShowAllMarkers == true}">
					<fmt:message key="label.on" />
				</c:when>
				<c:otherwise>
					<fmt:message key="label.off" />
				</c:otherwise>
			</c:choose>	
		</td>
	</tr>
	
	
	<tr>
		<td>
			<fmt:message key="advanced.markerLimitsMessage" />
		</td>	
		<td>
			<c:choose>
				<c:when test="${dto.limitMarkers == true}">
					<fmt:message key="label.on" />, ${dto.maxMarkers}
				</c:when>
				<c:otherwise>
					<fmt:message key="label.off" />
				</c:otherwise>
			</c:choose>
		</td>
	</tr>
	 		
	<tr>
	 	<td>
			<fmt:message key="advanced.allowZoom" />
		</td>
		<td>	
			<c:choose>
				<c:when test="${dto.allowZoom == true}">
					<fmt:message key="label.on" />
				</c:when>
				<c:otherwise>
					<fmt:message key="label.off" />
				</c:otherwise>
			</c:choose>	
		</td>
	</tr>

	<tr>
		<td>
			<fmt:message key="advanced.allowTerrain" />
		</td>
		<td>	
			<c:choose>
				<c:when test="${dto.allowTerrain == true}">
					<fmt:message key="label.on" />
				</c:when>
				<c:otherwise>
					<fmt:message key="label.off" />
				</c:otherwise>
			</c:choose>	
		</td>
	</tr>

	<tr>
		<td>
			<fmt:message key="advanced.allowSatellite" />
		</td>
		<td>	
			<c:choose>
				<c:when test="${dto.allowSatellite == true}">
					<fmt:message key="label.on" />
				</c:when>
				<c:otherwise>
					<fmt:message key="label.off" />
				</c:otherwise>
			</c:choose>	
		</td>
	</tr>
	
	<tr>
		<td>
			<fmt:message key="advanced.allowHybrid" />
		</td>
		<td>	
			<c:choose>	
				<c:when test="${dto.allowHybrid == true}">
					<fmt:message key="label.on" />
				</c:when>
				<c:otherwise>
					<fmt:message key="label.off" />
				</c:otherwise>
			</c:choose>	
		</td>
	</tr>
	
	<tr>
		<td>
			<fmt:message key="monitor.summary.td.addNotebook" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${dto.reflectOnActivity == true}">
					<fmt:message key="label.on" />
				</c:when>
				<c:otherwise>
					<fmt:message key="label.off" />
				</c:otherwise>
			</c:choose>	
		</td>
	</tr>
	
	<c:choose>
		<c:when test="${dto.reflectOnActivity == true}">
			<tr>
				<td>
					<fmt:message key="monitor.summary.td.notebookInstructions" />
				</td>
				<td>
					<lams:out value="${dto.reflectInstructions}" escapeHtml="true"/>
				</td>
			</tr>
		</c:when>
	</c:choose>
</table>
</div>



	<div id="groupsTable">
	<h1><fmt:message key="monitor.summary.title.groups"/></h1>
	
	<br />
	
	<table class="alternative-color">
	<tr>
		<th>
			<fmt:message key="heading.table.group" />
		</th>
		<th>
			<fmt:message key="heading.totalLearners" />
		</th>
	</tr>
	
	<c:if test="${empty dto.sessionDTOs}">
		<td colspan="2"><i><fmt:message key="label.nogroups" /></i></td>
	</c:if>
	<c:forEach var="session" items="${dto.sessionDTOs}">
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

	<html:form action="/monitoring" method="post">
		<html:hidden property="dispatch" styleId = "dispatch" value="unspecified" />
		<html:hidden property="markersXML" value="" styleId="markersXML" />
		<html:hidden property="toolSessionID" styleId="toolSessionID"/>
		<html:hidden property="toolContentID" />
		<html:hidden property="contentFolderID" />
	
	<div id="gmapDiv">	
	<h1><fmt:message key="monitor.summary.title.map"/></h1>
	
	<br />
	<table>
		<tr>
			 <td>
			 	<div id="map_legend" style="width:100%;" >
				<iframe marginwidth="0" align="left" height="60px" width="100%" frameborder="0" src="${tool}/common/mapLegend.jsp"></iframe>
				</div>
			 	<div id="map_canvas" style="float: left; width:80%; height:400px"><fmt:message key="error.cantLoadMap"></fmt:message></div>
				<div id="usersidebar" style="float: right; width:20%; overflow:auto; height:400px; background:WhiteSmoke; "></div>			 	
			 </td> 			
		</tr>
		<tr>
			<td>
				<a href="javascript:addMarkerToCenterMonitoring()" class="button"/><fmt:message key="button.addMarker"/></a>
				<a href="javascript:fitMapMarkers()" class="button"/><fmt:message key="button.fitMarkers"/></a>
				<a href="javascript:if(confirmLeavePage()){refreshPage();}" class="button"/><fmt:message key="button.refresh"/></a>
			</td>
		</tr>
	</table>
	
	<table>
	<tr><td>
		<input type="text" onkeypress="javascript:if (event.keyCode==13){showAddress();return false;}" size="60" name="address" id="address" value="${dto.defaultGeocoderAddress}" />			
		<a href="javascript:showAddress()" class="button"/><fmt:message key="button.go"/></a>
	</td></tr>
	</table>
	</div>
	
	<table><tr><td align="right">
		<html:submit styleClass="button" styleId="finishButton" onclick="javascript:serialiseMarkers();document.getElementById('dispatch').value = 'saveMarkers';">
			<fmt:message key="button.save"></fmt:message>
		</html:submit>
	</td></tr></table>
	
	<c:if test="${dto.reflectOnActivity}">
	
	
	<c:forEach var="session" items="${dto.sessionDTOs}">
		<div id="reflectionDiv${session.sessionID}" style="display:none;">
		
		<h1>
			<c:if test="${isGroupedActivity}">
				${session.sessionName}
			</c:if>	
			<fmt:message key="heading.reflection"></fmt:message>
		</h1>
		<br />
		
		
		<table class="alternative-color">
		<c:forEach var="user" items="${session.userDTOs}">
			<tr>
				<td>
					<c:out value="${user.firstName} ${user.lastName}" escapeXml="true"/>
				</td>
				<td>
					<c:if test="${user.finishedReflection}">
						<c:url value="monitoring.do" var="openNotebook">
							<c:param name="dispatch" value="openNotebook" />
							<c:param name="userID" value="${user.userId}" />
							<c:param name="toolSessionID" value="${session.sessionID}" />
						</c:url>
				
						<html:link
							href="javascript:launchPopup('${openNotebook}')">
							<fmt:message key="link.view" />
						</html:link>
					</c:if>
				</td>
			</tr>	
		</c:forEach>
		</table>
		</div>
	</c:forEach>
	</c:if>
</html:form>

	
<script type="text/javascript">
<!--
	initMonotorGmap();
	var sessionName;
	
	<c:forEach var="session" items="${dto.sessionDTOs}" varStatus="status">
	
	function addUsersForSession${session.sessionID}()
	{
		sessionName = '${session.sessionName}';
		document.getElementById("toolSessionID").value ="${session.sessionID}";
		addUserToList('0',"<fmt:message key="label.authoring.basic.authored"></fmt:message>" );
		<c:forEach var="user" items="${session.userDTOs}">
			addUserToList('${user.uid}','${user.firstName} ${user.lastName}' );
		</c:forEach>
	}
	
	function addMarkersForSession${session.sessionID}()
	{
		<c:forEach var="marker" items="${session.markerDTOs}">
			<c:choose>
			<c:when test="${marker.isAuthored == true}">
				addMarker(new GLatLng('${marker.latitude}', '${marker.longitude}' ), decode_utf8('<c:out value="${marker.infoWindowMessage}" escapeXml="true"/>'), decode_utf8('<c:out value="${marker.title}" escapeXml="true"/>'), '${marker.uid}', true, true, '<c:out value="${marker.createdBy.firstName} ${marker.createdBy.lastName}" escapeXml="true"/> (<fmt:message key="label.authoring.basic.authored"></fmt:message>)', '0');
			</c:when>
			<c:otherwise>
				addMarker(new GLatLng('${marker.latitude}', '${marker.longitude}' ), decode_utf8('<c:out value="${marker.infoWindowMessage}" escapeXml="true"/>'), decode_utf8('<c:out value="${marker.title}" escapeXml="true"/>'), '${marker.uid}', true, true, '<c:out value="${marker.createdBy.firstName} ${marker.createdBy.lastName}"escapeXml="true"/>', '${marker.createdBy.uid}');
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
	
	function makeReflectionDivVisible(id)
	{
		var i;
		if (document.getElementById(id) != null)
		{
		<c:forEach var="session" items="${dto.sessionDTOs}">
			document.getElementById("reflectionDiv${session.sessionID}").style.display = "none";
		</c:forEach>
		document.getElementById(id).style.display = "block";
		}
	}	
	
	function refreshPage()
	{
		var url = "<lams:WebAppURL/>/monitoring.do?toolContentID=${dto.toolContentId}&contentFolderID=${contentFolderID}";
		window.location = url;
	}
	
//-->
</script>
<script type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/monitorToolSummaryAdvanced.js" ></script>
	

