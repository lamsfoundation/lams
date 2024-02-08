<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL /></c:set>
<c:set var="tool"><lams:WebAppURL /></c:set>
<c:if test="${not empty param.sessionMapID}">
	<c:set var="sessionMapID" value="${param.sessionMapID}" />
</c:if>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="forum" value="${sessionMap.forum}" />

<link type="text/css" href="${lams}/css/jquery-ui-bootstrap-theme5.css" rel="stylesheet">
<link type="text/css" href="${lams}/css/jquery-ui.timepicker.css" rel="stylesheet">
<link rel="stylesheet" href="${lams}css/jquery.tablesorter.theme.bootstrap5.css">
<link type="text/css" href="${lams}css/jquery.tablesorter.pager5.css" rel="stylesheet">
 <style>
 	.group-mark-release-label {
 		margin-top: 8px;
 	}
 	.group-mark-release-label span{
 		font-size: small;
 	}
</style>

<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.timepicker.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.blockUI.js"></script>  
<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter.js"></script> 
<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter-widgets.js"></script> 
<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter-pager.js"></script>
<script type="text/javascript" src="${tool}includes/javascript/message.js"></script>
<script type="text/javascript">
	//pass settings to monitorToolSummaryAdvanced.js
	var submissionDeadlineSettings = {
		lams: '${lams}',
		submissionDeadline: '${sessionMap.submissionDeadline}',
		submissionDateString: '${submissionDateString}',
		setSubmissionDeadlineUrl: '<c:url value="/monitoring/setSubmissionDeadline.do"/>?<csrf:token/>',
		toolContentID: '<c:out value="${param.toolContentID}" />',
		messageNotification: '<spring:escapeBody javaScriptEscape="true"><fmt:message key="monitor.summary.notification" /></spring:escapeBody>',
		messageRestrictionSet: '<spring:escapeBody javaScriptEscape="true"><fmt:message key="monitor.summary.date.restriction.set" /></spring:escapeBody>',
		messageRestrictionRemoved: '<spring:escapeBody javaScriptEscape="true"><fmt:message key="monitor.summary.date.restriction.removed" /></spring:escapeBody>'
	};
</script>
<script type="text/javascript" src="${lams}/includes/javascript/monitorToolSummaryAdvanced.js" ></script>
<c:set var="localeLanguage"><lams:user property="localeLanguage" /></c:set>
<script src="${lams}includes/javascript/jquery.timeago.js" type="text/javascript"></script>
<script src="${lams}includes/javascript/timeagoi18n/jquery.timeago.${fn:toLowerCase(localeLanguage)}.js" type="text/javascript"></script>
<script type="text/javascript" src="${lams}/includes/javascript/portrait5.js" ></script>
<script type="text/javascript">
	function releaseMarks(sessionId){
		$("#message-area-busy").show();
		$.ajax({
			url: "<c:url value="/monitoring/releaseMark.do"/>",
			data: {
				toolSessionID: sessionId, 
				reqID: (new Date()).getTime()
			},
			success: function() {
				$("#message-area-busy").hide();
				$("#release-marks-" + sessionId).hide();
				$("#release-marks-info-" + sessionId).show();
			}
		});
	}
	
  	$(document).ready(function(){
  		jQuery("time.timeago").timeago();
		$(".tablesorter").tablesorter({
			theme: 'bootstrap',
			headerTemplate : '{content} {icon}',
		    sortInitialOrder: 'desc',
            sortList: [[0]],
            widgets: [ "uitheme", "resizable", "filter" ],
            headers: { 1: { filter: false}, 2: { filter: false}, 3: { filter: false}, 4: { filter: false} }, 
            widgetOptions: {
            	resizable: true,
            	// include column filters 
                filter_columnFilters: true, 
                filter_placeholder: { search : '<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.search"/></spring:escapeBody>' }, 
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
	            cssDisabled: 'disabled',
				ajaxUrl : "<c:url value='/monitoring/getUsers.do'/>?sessionMapID=${sessionMapID}&page={page}&size={size}&{sortList:column}&{filterList:fcol}&sessionId=" + $(this).attr('data-session-id'),
				ajaxProcessing: function (data, table) {
			    	if (data && data.hasOwnProperty('rows')) {
			    		var rows = [],
			            json = {};
			    		
						for (i = 0; i < data.rows.length; i++){
							var userData = data.rows[i];
							
							rows += '<tr>';
							
							rows += '<td>';
							rows += 	definePortraitPopover(userData["portraitId"], userData["userId"], userData["userName"], userData["userName"]);
							rows += '</td>';
							
							rows += '<td align="right">';
							rows += 	userData["numberOfPosts"];
							rows += '</td>';

							rows += '<td align="right">';
							if ( userData["lastMessageDate"] != null) {
								rows += 	'(<time class="timeago" title="';
								rows += 	userData["lastMessageDate"]
								rows += 	'" datetime="';
								rows += 	userData["timeAgo"];
								rows += 	'"></time>)';
							}
							rows += '</td>';

							//anyPostsMarked column
							rows += '<td align="center">';
							if (userData["numberOfPosts"] == 0) {
								rows += "-";
								
							} else {
								let anyPostsMarked = (userData["anyPostsMarked"]) ? '<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.yes"/></spring:escapeBody>' : '<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.no"/></spring:escapeBody>',
									viewUserMarkUrl = '<c:url value="/monitoring/viewUserMark.do"/>?sessionMapID=${sessionMapID}&userUid=' + userData["userUid"] + "&toolSessionID=" + $(table).attr('data-session-id');

								rows += '<div class="d-flex flex-row align-items-center">' +
											anyPostsMarked +	
											'<button type="button" onclick="launchPopup(\'' + viewUserMarkUrl + '\')" class="btn btn-sm btn-secondary d-flex align-items-center ms-1">' +
												'<i class="fa-solid fa-marker me-1"></i>' +
												'<spring:escapeBody javaScriptEscape="true"><fmt:message key="lable.topic.title.mark" /></spring:escapeBody>' +
									 		'</button>' +
									 	'</div>';
							}
							rows += '</td>';
							
							rows += '</tr>';
						}
			            
						json.total = data.total_rows;
						json.rows = $(rows);
						return json;
			    	}
				}})
				.bind('pagerInitialized pagerComplete', function(event, options){
					initializePortraitPopover('${lams}');
					$("time.timeago").timeago();
				})
		});
	})
	  	
	function downloadMarks(sessionId){
		var url = "<c:url value="/monitoring/downloadMarks.do"/>";
	    var reqIDVar = new Date();
		var param = "?toolSessionID=" + sessionId +"&reqID="+reqIDVar.getTime();
		url = url + param;
		location.href = url;
	}
</script>

<div class="instructions">
	<div class="fs-4">
	    <c:out value="${title}" escapeXml="true"/>
	</div>
	<div class="mt-2">
	    <c:out value="${instruction}" escapeXml="false"/>
	</div>
	
	<c:if test="${empty sessionMap.sessionDtos}">
		<lams:Alert5 type="info" id="no-session-summary" close="false">
			<fmt:message key="message.monitoring.summary.no.session" />
		</lams:Alert5>
	</c:if>
	
	<!--For release marks feature-->
	<lams:WaitingSpinner id="message-area-busy"/>
</div>

<c:forEach var="sessionDto" items="${sessionMap.sessionDtos}" varStatus="status">
	<c:if test="${sessionMap.isGroupedActivity}">	
	    <div class="lcard card-secondary" >
        <div class="card-header" id="heading${sessionDto.sessionID}">
        	<span class="card-title collapsable-icon-left">
        		<button type="button" class="btn btn-secondary-darker no-shadow ${status.first ? '' : 'collapsed'}" data-bs-toggle="collapse" data-bs-target="#collapse${sessionDto.sessionID}" 
						aria-expanded="${status.first ? 'false' : 'true'}" aria-controls="collapse${sessionDto.sessionID}" >					
					<fmt:message key="message.session.name" />:	<c:out value="${sessionDto.sessionName}" />
				</button>
			</span>
        </div>

        <div id="collapse${sessionDto.sessionID}" class="card-body p-2 collapse ${status.first ? 'show' : ''}">
	</c:if>
		
	<lams:TSTable numColumns="4" dataId="data-session-id='${sessionDto.sessionID}'">
		<th><fmt:message key="monitoring.user.fullname"/></th>
		<th width="5%" align="center"><fmt:message key="label.number.of.posts"/></th>
		<th width="25%" align="center"><fmt:message key="label.latest.posting.date"/></th>
		<th width="10%" align="center"><fmt:message key="monitoring.marked.question"/></th>
	</lams:TSTable>

	<div class="clearfix">
		<div class="float-end"> 
			<div id="release-marks-info-${sessionDto.sessionID}" class="group-mark-release-label text-center ms-2 "
					<c:if test="${!sessionDto.marksReleased}">style="display:none;"</c:if>>
				<span class="badge bg-success">
					<i class="fa fa-check-circle me-1"></i>
					<fmt:message key="label.marks.released" />
				</span>
			</div>
					
			<c:set var="viewforum">
				<lams:WebAppURL />learning/viewForum.do?toolSessionID=${sessionDto.sessionID}&topicID=${topic.message.uid}&mode=teacher
			</c:set>
			<button type="button" onclick="launchPopup('${viewforum}');" class="btn btn-secondary ms-2 mt-3">
				<i class="fa-solid fa-eye me-1"></i>
				<fmt:message key="label.monitoring.summary.view.forum"/>
			</button>
			<button type="button" name="releaseMarks" onclick="releaseMarks(${sessionDto.sessionID})" class="btn btn-secondary ms-2 mt-3" >
				<i class="fa-solid fa-box-open me-1"></i>
				<fmt:message key="button.release.mark" />
			</button>
			
			<c:set value="${sessionDto.sessionID}" var="toolSessionID"/>
			<button type="button"  onclick="downloadMarks(${toolSessionID});" name="downloadMarks" class="btn btn-secondary ms-2 mt-3">
				<i class="fa-solid fa-download me-1"></i>
				<fmt:message key="message.download.marks" />
			</button>
			<c:url value="/monitoring/monitoring.do" var="refreshMonitoring">
				<c:param name="contentFolderID" value="${contentFolderID}"/>
				<c:param name="toolContentID" value="${toolContentID}" />
				<c:param name="lessonID" value="${param.lessonID}" />
			</c:url>
			<a href="${refreshMonitoring}" class="btn btn-secondary btn-icon-refresh ms-2 mt-3" >
				<fmt:message key="label.refresh" />
			</a>
		</div>
	</div>
	
	<c:if test="${sessionMap.isGroupedActivity}">
		</div> <!-- end collapse area  -->
		</div> <!-- end collapse panel  -->
	</c:if>
	${ !sessionMap.isGroupedActivity || ! status.last ? '<div class="mt-2">&nbsp;</div>' :  ''}
</c:forEach>

<%@include file="parts/advanceOptions.jsp"%>

<c:set var="restrictedTitle"><spring:escapeBody javaScriptEscape="true"><fmt:message key="monitor.summary.date.restriction" /></spring:escapeBody></c:set>
<lams:RestrictedUsageAccordian title="${restrictedTitle}" submissionDeadline="${forum.submissionDeadline}" cssClass="my-2"/>
