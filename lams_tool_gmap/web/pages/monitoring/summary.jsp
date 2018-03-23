<%@ include file="/common/taglibs.jsp"%>
<c:set var="tool"><lams:WebAppURL /></c:set>
<c:set var="lams"><lams:LAMSURL /></c:set>

<c:set var="dto" value="${gmapDTO}" />

<script type="text/javascript">
	$(document).ready(function(){
	    
		$(".tablesorter").tablesorter({
			theme: 'bootstrap',
			headerTemplate : '{content} {icon}',
		    sortInitialOrder: 'desc',
            sortList: [[0]],
            widgets: [ "uitheme", "resizable", "filter" ],
            headers: { 1: { filter: false} }, 
            widgetOptions: {
            	resizable: true,
            	// include column filters 
                filter_columnFilters: true, 
                filter_placeholder: { search : '<fmt:message key="label.search"/>' }, 
                filter_searchDelay: 700 
            }
		});
		
		$(".tablesorter").each(function() {
			
			$(this).tablesorterPager({
				savePages: false,
		        container: $(this).find(".ts-pager"),
		        output: '{startRow} to {endRow} ({totalRows})',
		        cssPageDisplay: '.pagedisplay',
		        cssPageSize: '.pagesize',
				ajaxUrl : "<c:url value='/monitoring.do'/>?dispatch=getUsers&toolContentID=${dto.toolContentId}&page={page}&size={size}&{sortList:column}&{filterList:fcol}&toolSessionID=" + $(this).attr('data-session-id'),
				ajaxProcessing: function (data, table) {
					if (data && data.hasOwnProperty('rows')) {
			    		var rows = [],
			            json = {};
					
			    		for (i = 0; i < data.rows.length; i++){
							var userData = data.rows[i],
								userId = userData["userId"], 
								fullName = userData["fullName"];

							rows += '<tr>';
 							rows += '<td>'+ definePortraitPopover(userData["portraitId"], userId, fullName, fullName) +'</td>';

							<c:if test="${dto.reflectOnActivity}">
							rows += '<td width="75%">';
							if ( userData["reflection"] ) {
								rows += userData["reflection"];
							} else {
								rows += '-';
							}
							rows += '</td>';
							</c:if>
							
							rows += '</tr>';
						}
			            
						json.total = data.total_rows;
						json.rows = $(rows);
						return json;
			            
			    	}
			}
			}).bind('pagerInitialized pagerComplete', function(event, options){
				initializePortraitPopover('${lams}');
            })
		});
  	});

</script>
<div class="panel">
	<h4>
	    <c:out value="${dto.title}" escapeXml="true"/>
	</h4>
	<div class="instructions voffset5">
	    <c:out value="${dto.instructions}" escapeXml="false"/>
	</div>
	
</div>

<c:if test="${empty dto.sessionDTOs}">
	<lams:Alert type="info" id="no-session-summary" close="false">
		<fmt:message key="label.nogroups" />
	</lams:Alert>
</c:if>

<c:forEach var="session" items="${dto.sessionDTOs}" varStatus="status">
	<c:set var="toggleJS">javascript:clearMap();addUsersForSession${session.sessionID}();addMarkersForSession${session.sessionID}();</c:set>
		
	<c:if test="${isGroupedActivity}">	
	    <div class="panel panel-default" >
        <div class="panel-heading" id="heading${session.sessionID}">
        	<span class="panel-title collapsable-icon-left">
        	<a class="${status.first ? '' : 'collapsed'}" role="button" data-toggle="collapse" href="#collapse${session.sessionID}" 
					aria-expanded="${status.first ? 'false' : 'true'}" aria-controls="collapse${session.sessionID}" >
			<fmt:message key="heading.table.group" />:	<c:out value="${session.sessionName}" /></a>
			</span>
		<a href="${toggleJS}" class="btn btn-default btn-xs pull-right"><fmt:message key="label.show.on.map"/></a>
			
        </div>
        
        <div id="collapse${session.sessionID}" class="panel-collapse collapse ${status.first ? 'in' : ''}" role="tabpanel" aria-labelledby="heading${session.sessionID}">
	</c:if>
	
	<lams:TSTable numColumns="${dto.reflectOnActivity ? 2 : 1}" dataId="data-session-id='${session.sessionID}'">
			<th><fmt:message key="monitoring.user.fullname"/></th>
			<c:if test="${dto.reflectOnActivity}">
				<th align="center" class="sorter-false" width="75%"><fmt:message key="monitoring.user.reflection"/></th>
			</c:if>
	</lams:TSTable>
	
	<c:if test="${isGroupedActivity}">
		</div> <!-- end collapse area  -->
		</div> <!-- end collapse panel  -->
	</c:if>
	${ !isGroupedActivity || ! status.last ? '<div class="voffset5">&nbsp;</div>' :  ''}
		
</c:forEach>

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
				addMarker(new GLatLng('${marker.latitude}', '${marker.longitude}' ), decode_utf8('<c:out value="${marker.infoWindowMessage}" escapeXml="true"/>'), decode_utf8('<c:out value="${marker.title}" escapeXml="true"/>'), '${marker.uid}', true, true, '<c:out value="${marker.createdBy.firstName} ${marker.createdBy.lastName}" escapeXml="true"/>', '${marker.createdBy.uid}');
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
	

