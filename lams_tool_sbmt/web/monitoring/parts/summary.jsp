<%@include file="/common/taglibs.jsp"%>

<c:set var="tool">
	<lams:WebAppURL />
</c:set>

<c:set var="lams">
	<lams:LAMSURL />
</c:set>

<script type="text/javascript">
	// pass settings to monitorToolSummaryAdvanced.js
	var submissionDeadlineSettings = {
		lams: '${lams}',
		submissionDeadline: '${submissionDeadline}',
		submissionDateString: '${submissionDateString}',
		setSubmissionDeadlineUrl: '<c:url value="/monitoring.do?method=setSubmissionDeadline"/>',
		toolContentID: '${param.toolContentID}',
		messageNotification: '<fmt:message key="monitor.summary.notification" />',
		messageRestrictionSet: '<fmt:message key="monitor.summary.date.restriction.set" />',
		messageRestrictionRemoved: '<fmt:message key="monitor.summary.date.restriction.removed" />'
	};
</script>
<script type="text/javascript" src="${lams}/includes/javascript/monitorToolSummaryAdvanced.js" ></script>

<script type="text/javascript">
	$(document).ready(function(){
	    
		$(".tablesorter").tablesorter({
			theme: 'bootstrap',
			headerTemplate : '{content} {icon}',
		    sortInitialOrder: 'desc',
            sortList: [[0]],
            widgets: [ "uitheme", "resizable", "filter" ],
            headers: { 1: { filter: false}, 2: { filter: false }, 3: { sorter: false, filter: false} }, 
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
				ajaxUrl : "<c:url value='/monitoring.do'/>?method=getUsers&sessionMapID=${sessionMapID}&toolContentID=${param.toolContentID}&page={page}&size={size}&{sortList:column}&{filterList:fcol}&toolSessionID=" + $(this).attr('data-session-id'),
				ajaxProcessing: function (data, table) {
					if (data && data.hasOwnProperty('rows')) {
			    		var rows = [],
			            json = {};
			    		
						for (i = 0; i < data.rows.length; i++){
							var userData = data.rows[i];

							rows += '<tr>';
							rows += '<td>'
							rows += userData["fullName"];
							rows += '</td>';
							rows += '<td align="center">'
							rows += userData["numFiles"];
							rows += '</td>';
							
							rows += '<td align="center">';
							if ( userData["marked"] ) {
								rows += '<fmt:message key="label.yes"/>';
							} else {
								rows += '<fmt:message key="label.no"/>';
							}
							rows += ' [<a href=\"javascript:viewMark('+userData["userID"]+','+$(table).attr("data-session-id")+');\"><fmt:message key="label.view.files" /></a>]';
							rows += '</td>';

							<c:if test="${reflectOn}">
							rows += '<td>';
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
			})
		});
  	})

  	
  	function launchPopup(url, title) {
		var wd = null;
		if (wd && wd.open && !wd.closed) {
			wd.close();
		}
		wd = window.open(url, title,
				'resizable,width=1152,height=648,scrollbars');
		wd.window.focus();
	}

	function viewMark(userId, sessionId) {
		var act = "<c:url value="/monitoring.do"/>";
		launchPopup(act + "?method=listMark&userID=" + userId
				+ "&toolSessionID=" + sessionId, "mark");
	}
	function viewAllMarks(sessionId) {
		var act = "<c:url value="/monitoring.do"/>";
		launchPopup(act + "?method=listAllMarks&toolSessionID=" + sessionId,
				"mark");
	}

	function releaseMarks(sessionId) {
		var url = "<c:url value="/monitoring.do"/>";
		
		$("#messageArea_Busy").show();
		$("#messageArea").load(
			url,
			{
				method: "releaseMarks",
				toolSessionID: sessionId, 
				reqID: (new Date()).getTime()
			},
			function() {
				$("#messageArea_Busy").hide();
			}
		);
	}
</script>

<div class="panel">
	<h4>
	    <c:out value="${authoring.title}" escapeXml="true"/>
	</h4>
	<div class="instructions voffset5">
	    <c:out value="${authoring.instruction}" escapeXml="false"/>
	</div>
	
	<c:if test="${empty sessions}">
		<lams:Alert type="info" id="no-session-summary" close="false">
			<fmt:message key="label.no.user.available" />
		</lams:Alert>
	</c:if>
	
	<!--For release marks feature-->
	<i class="fa fa-refresh fa-spin fa-fw" style="display:none" id="messageArea_Busy"></i>
	<div id="messageArea"></div>

</div>



<c:forEach var="sessionDto" items="${sessions}" varStatus="status">
		
	<c:if test="${isGroupedActivity}">	
	    <div class="panel panel-default" >
        <div class="panel-heading" id="heading${sessionDto.sessionID}">
        	<span class="panel-title collapsable-icon-left">
        	<a class="${status.first ? '' : 'collapsed'}" role="button" data-toggle="collapse" href="#collapse${sessionDto.sessionID}" 
					aria-expanded="${status.first ? 'false' : 'true'}" aria-controls="collapse${sessionDto.sessionID}" >
			<fmt:message key="label.session.name" />:	<c:out value="${sessionDto.sessionName}" /></a>
			</span>
        </div>
        
        <div id="collapse${sessionDto.sessionID}" class="panel-collapse collapse ${status.first ? 'in' : ''}" role="tabpanel" aria-labelledby="heading${sessionDto.sessionID}">
	</c:if>
	
	<lams:TSTable numColumns="${reflectOn ? 4 : 3}" dataId="data-session-id='${sessionDto.sessionID}'">
			<th><fmt:message key="monitoring.user.fullname"/></th>
			<th width="15%" align="center"><fmt:message key="monitoring.user.submittedFiles"/></th>
			<th width="10%" align="center"><fmt:message key="monitoring.marked.question"/></th>
			<c:if test="${reflectOn}">
				<th align="center" class="sorter-false"><fmt:message key="monitoring.user.reflection"/></th>
			</c:if>
	</lams:TSTable>
	
	<P style="display: inline"> 
		<html:button property="viewAllMarks" onclick="javascript:viewAllMarks(${sessionDto.sessionID})"
					 styleClass="btn btn-default loffset5 voffset10" >
			<fmt:message key="label.monitoring.viewAllMarks.button" />
		</html:button>
		<html:button property="releaseMarks" onclick="releaseMarks(${sessionDto.sessionID})"
					 styleClass="btn btn-default loffset5 voffset10" >
			<fmt:message key="label.monitoring.releaseMarks.button" />
		</html:button>
		<html:form action="/monitoring" style="display:inline">
			<html:hidden property="method" value="downloadMarks" />
			<html:hidden property="toolSessionID" value="${sessionDto.sessionID}" />
			<html:submit property="downloadMarks" styleClass="btn btn-default loffset5 voffset10" >
				<fmt:message key="label.monitoring.downloadMarks.button" />
			</html:submit>
		</html:form>
	</P>
		
	<c:if test="${isGroupedActivity}">
		</div> <!-- end collapse area  -->
		</div> <!-- end collapse panel  -->
	</c:if>
	${ !isGroupedActivity || ! status.last ? '<div class="voffset5">&nbsp;</div>' :  ''}
		
</c:forEach>


<c:set var="adTitle"><fmt:message key="monitor.summary.th.advancedSettings" /></c:set>
<lams:AdvancedAccordian title="${adTitle}">
             
<table class="table table-striped table-condensed">
	<tr>
		<td>
			<fmt:message key="label.authoring.advance.lock.on.finished" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${authoring.lockOnFinished}">
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
			<fmt:message key="label.limit.number.upload" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${authoring.limitUpload}">
					<fmt:message key="label.on" />, ${authoring.limitUploadNumber}
				</c:when>
				<c:otherwise>
					<fmt:message key="label.off" />
				</c:otherwise>
			</c:choose>	
		</td>
	</tr>
	
	<tr>
		<td>
			<fmt:message key="label.authoring.advanced.notify.mark.release" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${authoring.notifyLearnersOnMarkRelease}">
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
			<fmt:message key="label.authoring.advanced.notify.onfilesubmit" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${authoring.notifyTeachersOnFileSubmit}">
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
				<c:when test="${authoring.reflectOnActivity}">
					<fmt:message key="label.on" />
				</c:when>
				<c:otherwise>
					<fmt:message key="label.off" />
				</c:otherwise>
			</c:choose>	
		</td>
	</tr>
	
	<c:choose>
		<c:when test="${authoring.reflectOnActivity}">
			<tr>
				<td>
					<fmt:message key="monitor.summary.td.notebookInstructions" />
				</td>
				<td>
					<lams:out value="${authoring.reflectInstructions}" escapeHtml="true"/>	
				</td>
			</tr>
	</c:when>
	</c:choose>
</table>
</lams:AdvancedAccordian>		

<%@include file="daterestriction.jsp"%>
