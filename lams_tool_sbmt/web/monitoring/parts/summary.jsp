<%@include file="/common/taglibs.jsp"%>
<c:set var="tool"><lams:WebAppURL /></c:set>
<c:set var="lams"><lams:LAMSURL /></c:set>

<style>
	.group-mark-release-label {
		margin-top: 8px;
	}
	.group-mark-release-label span{
		font-size: small;
	}
</style>

<script type="text/javascript">
	// pass settings to monitorToolSummaryAdvanced.js
	var submissionDeadlineSettings = {
		lams: '${lams}',
		submissionDeadline: '${submissionDeadline}',
		submissionDateString: '${submissionDateString}',
		setSubmissionDeadlineUrl: '<c:url value="/monitoring/setSubmissionDeadline.do"/>?<csrf:token/>',
		toolContentID: '${param.toolContentID}',
		messageNotification: '<fmt:message key="monitor.summary.notification" />',
		messageRestrictionSet: '<fmt:message key="monitor.summary.date.restriction.set" />',
		messageRestrictionRemoved: '<fmt:message key="monitor.summary.date.restriction.removed" />'
	};
</script>
<script type="text/javascript" src="${lams}/includes/javascript/monitorToolSummaryAdvanced.js" ></script>
<script type="text/javascript" src="${lams}/includes/javascript/portrait.js" ></script>
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
				ajaxUrl : "<c:url value='/monitoring/getUsers.do'/>?sessionMapID=${sessionMapID}&toolContentID=${param.toolContentID}&page={page}&size={size}&{sortList:column}&{filterList:fcol}&toolSessionID=" + $(this).attr('data-session-id'),
				ajaxProcessing: function (data, table) {
					if (data && data.hasOwnProperty('rows')) {
			    		var rows = [],
			            json = {};
			    		
						for (i = 0; i < data.rows.length; i++){
							var userData = data.rows[i],
								userId = userData["userID"],
								fullName = userData["fullName"];
							
							rows += '<tr>';
 							rows += '<td>'+ definePortraitPopover(userData["portraitId"], userId, fullName, fullName) +'</td>';
 							rows += '<td align="center">'
							rows += userData["numFiles"];
							rows += '</td>';
							
							rows += '<td align="center">';
							if ( userData["marked"] ) {
								rows += '<fmt:message key="label.yes"/>';
							} else {
								rows += '<fmt:message key="label.no"/>';
							}
							rows += ' [<a href=\"javascript:viewMark('+userId+','+$(table).attr("data-session-id")+');\"><fmt:message key="label.view.files" /></a>]';
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
			}).bind('pagerInitialized pagerComplete', function(event, options){
				initializePortraitPopover('${lams}');
            })
		});
  	});
  	
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
		var act = "<c:url value="/monitoring/listMark.do"/>";
		launchPopup(act + "?userID=" + userId
				+ "&toolSessionID=" + sessionId, "mark");
	}
	function viewAllMarks(sessionId) {
		var act = "<c:url value="/monitoring/listAllMarks.do"/>";
		launchPopup(act + "?toolSessionID=" + sessionId,
				"mark");
	}

	function releaseMarks(sessionId) {
		$("#messageArea_Busy").show();
		
		$.ajax({
            type: 'POST',
			url: "<c:url value="/monitoring/releaseMarks.do"/>?<csrf:token/>",
			data: {
				toolSessionID: sessionId, 
				reqID: (new Date()).getTime()
			},
			success: function() {
				$("#messageArea_Busy").hide();
				$("#release-marks-" + sessionId).hide();
				$("#release-marks-info-" + sessionId).show();
			}
		});
	}
</script>

<div class="panel">
	<h4>
	    <c:out value="${content.title}" escapeXml="true"/>
	</h4>
	<div class="instructions voffset5">
	    <c:out value="${content.instruction}" escapeXml="false"/>
	</div>
	
	<c:if test="${empty sessions}">
		<lams:Alert type="info" id="no-session-summary" close="false">
			<fmt:message key="label.no.user.available" />
		</lams:Alert>
	</c:if>
	
	<!--For release marks feature-->
	<lams:WaitingSpinner id="messageArea_Busy"/>

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
		<div id="release-marks-info-${sessionDto.sessionID}" class="loffset5 group-mark-release-label"
				<c:if test="${!sessionDto.marksReleased}">style="display:none;"</c:if>>
			<span class="label label-success">
                <i class="fa fa-check-circle"></i>
				<fmt:message key="label.marks.released" />
			</span>
		</div>
		
		<button name="viewAllMarks" onclick="javascript:viewAllMarks(${sessionDto.sessionID})"
					class="btn btn-default loffset5 voffset10" >
			<fmt:message key="label.monitoring.viewAllMarks.button" />
		</button>
		<c:if test="${!sessionDto.marksReleased}">
			<button name="releaseMarks" onclick="releaseMarks(${sessionDto.sessionID})"
					 class="btn btn-default loffset5 voffset10" >
				<fmt:message key="label.monitoring.releaseMarks.button" />
			</button>
		</c:if>
		<form action="downloadMarks.do" method="post" style="display:inline">
			<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
			<input type="hidden" name="toolSessionID" value="${sessionDto.sessionID}" />
			<input type="submit" name="downloadMarks" value="<fmt:message key="label.monitoring.downloadMarks.button" />" class="btn btn-default loffset5 voffset10" />
		</form>
	</P>
		
	<c:if test="${isGroupedActivity}">
		</div> <!-- end collapse area  -->
		</div> <!-- end collapse panel  -->
	</c:if>
	${ !isGroupedActivity || ! status.last ? '<div class="voffset5">&nbsp;</div>' :  ''}
		
</c:forEach>

<%@ include file="advanceoptions.jsp"%>	

<%@include file="daterestriction.jsp"%>
