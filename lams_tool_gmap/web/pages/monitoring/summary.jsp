<%@ include file="/common/taglibs.jsp"%>
<c:set var="tool"><lams:WebAppURL /></c:set>
<c:set var="dto" value="${gmapDTO}" />

<div class="panel">
	<h4>
	    <c:out value="${dto.title}" escapeXml="true"/>
	</h4>
	<div class="instructions voffset5">
	    <c:out value="${dto.instructions}" escapeXml="false"/>
	</div>
	
</div>

<c:choose>
<c:when test="${empty dto.sessionDTOs}">
	<lams:Alert type="info" id="no-session-summary" close="false">
		<fmt:message key="label.nogroups" />
	</lams:Alert>
</c:when>
<c:otherwise>

	<h4><fmt:message key="monitor.summary.title.groups"/></h4>
	
<c:choose>
<c:when test="${dto.reflectOnActivity}">
	
	<div class="panel-group" id="accordionSessions" role="tablist" aria-multiselectable="true"> 
	
	<c:forEach var="session" items="${dto.sessionDTOs}" varStatus="status">
		<c:set var="toggleJS">javascript:clearMap();addUsersForSession${session.sessionID}();addMarkersForSession${session.sessionID}();</c:set>
		<div id="sessionDiv${session.sessionID}">

	    <div class="panel panel-default" >
        <div class="panel-heading" id="heading${session.sessionID}">
			<div class="row no-gutter">
				<div class="col-sm-5">
					<span  class="collapsable-icon-left">
		        	<a class="collapsed" role="button" data-toggle="collapse" href="#collapse${session.sessionID}" onClick="${toggleJS}"
						aria-expanded="false" aria-controls="collapse${session.sessionID}" >
					<b><c:out value="${session.sessionName}" /></b>
					</span>
				</div>
				<div class="col-sm-5">
					<fmt:message key="heading.totalLearners" />:&nbsp;${session.numberOfLearners}</a>
				</div>
				<div class="col-sm-2">
					<a href="${toggleJS}"
						class="btn btn-default btn-xs pull-right"><fmt:message key="label.show.on.map"/></a>
				</div>
			</div>
        </div>
        
        <div id="collapse${session.sessionID}" class="panel-collapse collapse" role="tabpanel" aria-labelledby="heading${session.sessionID}">
		
		<table class="table table-condensed table-striped">
		<c:forEach var="user" items="${session.userDTOs}">
			<tr>
				<td>
					<lams:Portrait userId="${user.userId}" hover="true"><c:out value="${user.firstName} ${user.lastName}" escapeXml="true"/></lams:Portrait>
				</td>
				<td>
					<c:if test="${user.finishedReflection}">
						<c:url value="monitoring.do" var="openNotebook">
							<c:param name="dispatch" value="openNotebook" />
							<c:param name="userID" value="${user.userId}" />
							<c:param name="toolSessionID" value="${session.sessionID}" />
						</c:url>
				
						<html:link styleClass="btn btn-default btn-xs pull-right"
							href="javascript:launchPopup('${openNotebook}')">
							<fmt:message key="pageTitle.monitoring.notebook" />
						</html:link>
					</c:if>
				</td>
			</tr>	
		</c:forEach>
		</table>
		
		</div> <!-- end collapse area  -->
		</div> <!-- end collapse panel  -->

		<c:if test="${!status.last}"><div class="voffset5">&nbsp;</div></c:if>
		
		</div>
	</c:forEach>

	</div> <!--  end panel group -->

</c:when> 

<c:otherwise>
    <div class="panel panel-default" >
	<table class="table table-condensed table-striped">
		<tr>
			<th>
				<fmt:message key="heading.table.group" />
			</th>
			<th>
				<fmt:message key="heading.totalLearners" />
			</th>
			<th></th>
		</tr>
		
		<c:forEach var="session" items="${dto.sessionDTOs}">
			<tr>
				<td>
					${session.sessionName}
				</td>
				<td>
					${session.numberOfLearners}
				</td>
				<td>
					<a href="javascript:clearMap();addUsersForSession${session.sessionID}();addMarkersForSession${session.sessionID}();"
					class="btn btn-default btn-xs pull-right"><fmt:message key="label.show.on.map"/></a>
				</td>
			</tr>
		</c:forEach>
	</table>	
	</div>
</c:otherwise>
</c:choose>

</c:otherwise>
</c:choose>

	<html:form action="/monitoring" method="post">
		<html:hidden property="dispatch" styleId = "dispatch" value="unspecified" />
		<html:hidden property="markersXML" value="" styleId="markersXML" />
		<html:hidden property="toolSessionID" styleId="toolSessionID"/>
		<html:hidden property="toolContentID" />
		<html:hidden property="contentFolderID" />
	
	<div id="gmapDiv">	
	<h4><fmt:message key="monitor.summary.title.map"/></h4>
	
	<!-- map UI -->
	<div class="panel panel-default">
	<div class="panel-body">
	    
		<%@ include file="../../common/mapLegend.jsp"%>
	
		<div class="row no-gutter">
			<div class="col-sm-9 col-md-7">
				<div id="map_canvas" style="height:400px"><fmt:message key="error.cantLoadMap"></fmt:message></div>
			</div>
			<div class="col-sm-3 col-md-5">
				<div id="usersidebar" style="border:1px"></div>		
			</div>	
		</div>
				
		<div class="row no-gutter">
			<div class="col-sm-12">
				<a href="javascript:addMarkerToCenterMonitoring()" class="btn btn-default btn-sm voffset5" role="button"/><fmt:message key="button.addMarker"/></a>
				<a href="javascript:fitMapMarkers()" class="btn btn-default btn-sm voffset5" role="button"/><fmt:message key="button.fitMarkers"/></a>
				<a href="javascript:if(confirmLeavePage()){refreshPage();}" class="btn btn-default btn-sm voffset5" role="button"/><fmt:message key="button.refresh"/></a>
			</div>
		</div>
					
		<div class="row no-gutter">
			<div class="col-sm-10">
				<c:set var="goText"><fmt:message key="button.go"/></c:set>
				<input type="text" onkeypress="javascript:if (event.keyCode==13){showAddress();return false;}" size="60" name="address" 
					id="address" value="${dto.defaultGeocoderAddress}" class="form-control form-control-inline input-sm  voffset5"/>			
				<a href="javascript:showAddress()" class="btn btn-default btn-sm"/><i class="fa fa-search" title="${goText}"></i></a>
			</div>
			<div class="col-sm-2">
				<html:submit styleClass="btn btn-default pull-right" styleId="finishButton" onclick="javascript:serialiseMarkers();document.getElementById('dispatch').value = 'saveMarkers';">
				<fmt:message key="button.save"></fmt:message>
				</html:submit>
			</div>
		</div>
	</div>	
	</div>
	<!-- end map UI -->
	</div>

</html:form>

<%@include file="advancedOptions.jsp"%>
	
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
	</c:if>
	</c:forEach>
	
	function refreshPage()
	{
		var url = "<lams:WebAppURL/>/monitoring.do?toolContentID=${dto.toolContentId}&contentFolderID=${contentFolderID}";
		window.location = url;
	}
	
//-->
</script>
<script type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/monitorToolSummaryAdvanced.js" ></script>
	

