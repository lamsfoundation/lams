<%@include file="/common/taglibs.jsp"%>
<c:set var="tool"><lams:WebAppURL /></c:set>
<c:set var="lams"><lams:LAMSURL /></c:set>

<link type="text/css" href="${lams}/css/jquery-ui-bootstrap-theme5.css" rel="stylesheet">
<link type="text/css" href="${lams}/css/jquery-ui.timepicker.css" rel="stylesheet">
<link type="text/css" href="${lams}/css/jquery.tablesorter.theme.bootstrap5.css" rel="stylesheet">
<link type="text/css" href="${lams}/css/jquery.tablesorter.pager5.css" rel="stylesheet">
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
<script type="text/javascript" src="${lams}includes/javascript/jquery.cookie.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/download.js"></script>
<script type="text/javascript">
	// pass settings to monitorToolSummaryAdvanced.js
	var submissionDeadlineSettings = {
		lams: '${lams}',
		submissionDeadline: '${submissionDeadline}',
		submissionDateString: '${submissionDateString}',
		setSubmissionDeadlineUrl: '<c:url value="/monitoring/setSubmissionDeadline.do"/>?<csrf:token/>',
		toolContentID: '<c:out value="${param.toolContentID}" />',
		messageNotification: '<spring:escapeBody javaScriptEscape="true"><fmt:message key="monitor.summary.notification" /></spring:escapeBody>',
		messageRestrictionSet: '<spring:escapeBody javaScriptEscape="true"><fmt:message key="monitor.summary.date.restriction.set" /></spring:escapeBody>',
		messageRestrictionRemoved: '<spring:escapeBody javaScriptEscape="true"><fmt:message key="monitor.summary.date.restriction.removed" /></spring:escapeBody>'
	};
</script>
<script type="text/javascript" src="${lams}/includes/javascript/monitorToolSummaryAdvanced.js" ></script>
<script type="text/javascript" src="${lams}/includes/javascript/portrait5.js" ></script>
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
				ajaxUrl : "<c:url value='/monitoring/getUsers.do'/>?sessionMapID=${sessionMapID}&toolContentID=${param.toolContentID}&page={page}&size={size}&{sortList:column}&{filterList:fcol}&toolSessionID=" + $(this).attr('data-session-id'),
				ajaxProcessing: function (data, table) {
					if (data && data.hasOwnProperty('rows')) {
			    		var rows = [],
			            json = {};
			    		
						for (i = 0; i < data.rows.length; i++){
							var userData = data.rows[i],
								userId = userData["userID"],
								fullName = userData["fullName"];
							
							rows += '<tr>' +
 										'<td>'+ definePortraitPopover(userData["portraitId"], userId, fullName, fullName) +'</td>' +
 										'<td align="center">' +
											userData["numFiles"] +
										'</td>' +
										'<td align="center">';
							if ( userData["marked"] ) {
								rows += 	'<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.yes"/></spring:escapeBody>';
							} else {
								rows += 	'<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.no"/></spring:escapeBody>';
							}

							if (userData["numFiles"] > 0) {
							rows +=	 		' <button type=\"button\" class=\"btn btn-sm btn-light\" onclick=\"viewMark('+userId+','+$(table).attr("data-session-id")+');\">' +
												'<i class="fa-solid fa-eye me-1"></i>' +
												'<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.view.files" /></spring:escapeBody>' + 
											'</button>';
							}
							rows +=		'</td>' +
									'</tr>';
							
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

	function downloadMarks(toolSessionID) {
		var url = "<c:url value='/monitoring/downloadMarks.do'/>?<csrf:token/>&toolSessionID=" + toolSessionID + "&reqID="+(new Date()).getTime();
		return downloadFile(url, 'messageArea_Busy', '<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.summary.downloaded"/></spring:escapeBody>', 'messageArea', 'btn-disable-on-submit');
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
				$("#release-marks-notify-" + sessionId).show();
				$("#release-marks-info-" + sessionId).show();
			}
		});
	}

	function notifyLearnersOnMarkRelease(sessionId) {
		$.ajax({
            type: 'POST',
			url: "<c:url value="/monitoring/notifyLearnersOnMarkRelease.do"/>?<csrf:token/>",
			data: {
				toolSessionID: sessionId, 
				reqID: (new Date()).getTime()
			},
			success: function(response) {
				alert("<spring:escapeBody javaScriptEscape='true'><fmt:message key='label.monitoring.releaseMarks.notify.message'/></spring:escapeBody>".replace('[0]', response));
			}
		});
	}
	
	function showChangeLeaderModal(toolSessionId) {
		$('#change-leader-modals').empty()
		.load('<c:url value="/monitoring/displayChangeLeaderForGroupDialogFromActivity.do" />',{
			toolSessionID : toolSessionId
		});
	}

	function onChangeLeaderCallback(response, leaderUserId, toolSessionId){
        if (response.isSuccessful) {
            $.ajax({
    			'url' : '<c:url value="/monitoring/changeLeaderForGroup.do"/>',
    			'type': 'post',
    			'cache' : 'false',
    			'data': {
    				'toolSessionID' : toolSessionId,
    				'leaderUserId' : leaderUserId,
    				'<csrf:tokenname/>' : '<csrf:tokenvalue/>'
    			},
    			success : function(){
    				alert("<spring:escapeBody javaScriptEscape='true'><fmt:message key='label.monitoring.leader.successfully.changed'/></spring:escapeBody>");
    			},
    			error : function(){
    				alert("<spring:escapeBody javaScriptEscape='true'><fmt:message key='label.monitoring.leader.not.changed'/></spring:escapeBody>");
        		}
            });
        	
		} else {
			alert("<spring:escapeBody javaScriptEscape='true'><fmt:message key='label.monitoring.leader.not.changed'/></spring:escapeBody>");
		}
	}
</script>

<div class="container-main ms-4">
	<h4>
	    <c:out value="${content.title}" escapeXml="true"/>
	</h4>
	<div class="instructions">
	    <c:out value="${content.instruction}" escapeXml="false"/>
	</div>
	
	<c:if test="${empty sessions}">
		<lams:Alert5 type="info" id="no-session-summary">
			<fmt:message key="label.no.user.available" />
		</lams:Alert5>
	</c:if>
	
	<!--For release marks feature-->
	<lams:WaitingSpinner id="messageArea_Busy"/>

	<c:forEach var="sessionDto" items="${sessions}" varStatus="status">
		<c:choose>
			<c:when test="${isGroupedActivity}">		  
			    <div class="lcard" >
			        <div class="card-header" id="heading${sessionDto.sessionID}">
			        	<span class="card-title collapsable-icon-left">
				        	<button type="button" class="btn btn-secondary-darker no-shadow ${status.first ? '' : 'collapsed'}" data-bs-toggle="collapse" data-bs-target="#collapse${sessionDto.sessionID}" 
									aria-expanded="${status.first}" aria-controls="collapse${sessionDto.sessionID}" >
								<fmt:message key="label.session.name" />:	<c:out value="${sessionDto.sessionName}" />
							</button>
						</span>
						<c:if test="${content.useSelectLeaderToolOuput and sessionDto.numberOfLearners > 0 and not sessionDto.sessionFinished}">
							<button type="button" class="btn btn-light btn-sm float-end"
									onClick="showChangeLeaderModal(${sessionDto.sessionID})">
								<i class="fa-solid fa-user-pen me-1"></i>
								<fmt:message key='label.monitoring.change.leader'/>
							</button>
						</c:if>
			        </div>
			        
			        <div id="collapse${sessionDto.sessionID}" class="card-body p-2 collapse ${status.first ? 'show' : ''}">
			</c:when>
			
			<c:when test="${content.useSelectLeaderToolOuput and sessionDto.numberOfLearners > 0 and not sessionDto.sessionFinished}">
				<div class="clearfix">
					<button type="button" class="btn btn-secondary btn-sm float-end mb-3 mt-n3"
							onClick="showChangeLeaderModal(${sessionDto.sessionID})">
						<i class="fa-solid fa-user-pen me-1"></i>
						<fmt:message key='label.monitoring.change.leader'/>
					</button>
				</div>
			</c:when>
		</c:choose>
		
		<lams:TSTable5 numColumns="3" dataId="data-session-id='${sessionDto.sessionID}'">
			<th><fmt:message key="monitoring.user.fullname"/></th>
			<th width="15%" class="text-center"><fmt:message key="monitoring.user.submittedFiles"/></th>
			<th width="15%" class="text-center"><fmt:message key="monitoring.marked.question"/></th>
		</lams:TSTable5>
		
		<div class="clearfix" style="border-top: 0.1rem solid #f2f2f2;">
			<lams:WaitingSpinner id="messageArea_Busy"></lams:WaitingSpinner>
			<div class="clearfix">
				<div class="badge text-bg-info float-end px-5 py-3 mt-2" id="messageArea"></div>
			</div>
		
			<div id="release-marks-info-${sessionDto.sessionID}" class="group-mark-release-label clearfix mb-2"
					<c:if test="${!sessionDto.marksReleased}">style="display:none;"</c:if>>
				<span class="float-end text-bg-success badge">
	                <i class="fa fa-check-circle"></i>
					<fmt:message key="label.marks.released" />
				</span>
			</div>
			
			<div class="d-flex float-end mt-2">
				<button type="button" name="viewAllMarks" onclick="viewAllMarks(${sessionDto.sessionID})"
						class="btn btn-secondary ms-2">
					<i class="fa-solid fa-eye me-1"></i>
					<fmt:message key="label.monitoring.viewAllMarks.button" />
				</button>
				
				<c:if test="${!sessionDto.marksReleased}">
					<button type="button" id="release-marks-${sessionDto.sessionID}" onclick="releaseMarks(${sessionDto.sessionID})"
							 class="btn btn-secondary ms-2">
						<i class="fa-solid fa-check me-1"></i>
						<fmt:message key="label.monitoring.releaseMarks.button" />
					</button>
				</c:if>
				
				<button type="button" id="release-marks-notify-${sessionDto.sessionID}" 
						onclick="notifyLearnersOnMarkRelease(${sessionDto.sessionID})"
						class="btn btn-secondary ms-2"
						<c:if test="${!sessionDto.marksReleased}">style="display:none;"</c:if>>
					<i class="fa-regular fa-envelope me-1"></i>
					<fmt:message key="label.monitoring.releaseMarks.notify.button" />
				</button>
				
				<button type="button" class="btn btn-secondary ms-2" onclick="downloadMarks(${sessionDto.sessionID})" >
					<i class="fa fa-download me-1" aria-hidden="true"></i>
					<fmt:message key="label.monitoring.downloadMarks.button" />
				</button>
			</div>
		</div>
			
		<c:choose>
			<c:when test="${isGroupedActivity}">
				</div> <!-- end collapse area  -->
				</div> <!-- end collapse panel  -->
			</c:when>
			<c:otherwise>
				<br>
			</c:otherwise>
		</c:choose>
	</c:forEach>

	<%@ include file="advanceoptions.jsp"%>	
	<lams:RestrictedUsageAccordian submissionDeadline="${submissionDeadline}"/>
	<div id="change-leader-modals"></div>
</div>