<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL /></c:set>
<c:set var="tool"><lams:WebAppURL /></c:set>
<c:if test="${not empty param.sessionMapID}">
	<c:set var="sessionMapID" value="${param.sessionMapID}" />
</c:if>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="forum" value="${sessionMap.forum}" />

<link type="text/css" href="${lams}/css/jquery-ui-smoothness-theme.css" rel="stylesheet">
<link type="text/css" href="${lams}/css/jquery-ui.timepicker.css" rel="stylesheet">
<link rel="stylesheet" href="${lams}css/jquery.tablesorter.theme.bootstrap.css">
<link type="text/css" href="${lams}css/jquery.tablesorter.pager.css" rel="stylesheet">
	
<script type="text/javascript">
	//pass settings to monitorToolSummaryAdvanced.js
	var submissionDeadlineSettings = {
		lams: '${lams}',
		submissionDeadline: '${sessionMap.submissionDeadline}',
		submissionDateString: '${submissionDateString}',
		setSubmissionDeadlineUrl: '<c:url value="/monitoring/setSubmissionDeadline.do"/>',
		toolContentID: '${param.toolContentID}',
		messageNotification: '<fmt:message key="monitor.summary.notification" />',
		messageRestrictionSet: '<fmt:message key="monitor.summary.date.restriction.set" />',
		messageRestrictionRemoved: '<fmt:message key="monitor.summary.date.restriction.removed" />'
	};
</script>
<script type="text/javascript" src="${lams}/includes/javascript/monitorToolSummaryAdvanced.js" ></script>
<script src="${lams}includes/javascript/jquery.timeago.js" type="text/javascript"></script>
<script src="${lams}includes/javascript/timeagoi18n/jquery.timeago.${fn:toLowerCase(localeLanguage)}.js" type="text/javascript"></script>
<script type="text/javascript">

	function releaseMarks(sessionId){
		var url = "<c:url value="/monitoring/releaseMark.do"/>";
		
		$("#message-area-busy").show();
		$("#message-area").load(
			url,
			{
				toolSessionID: sessionId, 
				reqID: (new Date()).getTime()
			},
			function() {
				$("#message-area").show("slow").effect("highlight", {}, 2000);;
				$("#message-area-busy").hide();
			}
		);
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
							rows += 	userData["userName"];
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
								var anyPostsMarked = (userData["anyPostsMarked"]) ? '<fmt:message key="label.yes"/>' : '<fmt:message key="label.no"/>';
								rows += anyPostsMarked;	
								
								var viewUserMarkUrl = '<c:url value="/monitoring/viewUserMark.do"/>?sessionMapID=${sessionMapID}&userUid=' + userData["userUid"] + "&toolSessionID=" + $(table).attr('data-session-id');
								rows += 	'<a href="javascript:launchPopup(\'' + viewUserMarkUrl + '\')" style="margin-left: 7px;" styleClass="button">';
								rows += 		'<fmt:message key="lable.topic.title.mark" />';
								rows += 	'</a>';
							}
							rows += '</td>';
							
							if (${forum.reflectOnActivity}) {
								rows += '<td>';
								rows += 	(userData["notebookEntry"]) ? userData["notebookEntry"] : '-' ;
								rows += '</td>';
							}
							
							rows += '</tr>';
						}
			            
						json.total = data.total_rows;
						json.rows = $(rows);
						return json;
			            
			    	}
				}})
			  .bind('pagerInitialized pagerComplete', function(event, options){
					$("time.timeago").timeago();
					initializeJRating();
				})
			 
			});
	  	})

</script>

<div class="panel">
	<h4>
	    <c:out value="${title}" escapeXml="true"/>
	</h4>
	<div class="instructions voffset5">
	    <c:out value="${instruction}" escapeXml="false"/>
	</div>
	
	<c:if test="${empty sessionMap.sessionDtos}">
		<lams:Alert type="info" id="no-session-summary" close="false">
			<fmt:message key="message.monitoring.summary.no.session" />
		</lams:Alert>
	</c:if>
	
	<!--For release marks feature-->
	<lams:WaitingSpinner id="message-area-busy"/>
	<div id="message-area"></div>

</div>

<c:if test="${sessionMap.isGroupedActivity}">
<div class="panel-group" id="accordionSessions" role="tablist" aria-multiselectable="true"> 
</c:if>

<c:forEach var="sessionDto" items="${sessionMap.sessionDtos}" varStatus="status">

	<c:if test="${sessionMap.isGroupedActivity}">	
	    <div class="panel panel-default" >
        <div class="panel-heading" id="heading${sessionDto.sessionID}">
        	<span class="panel-title collapsable-icon-left">
        	<a class="${status.first ? '' : 'collapsed'}" role="button" data-toggle="collapse" href="#collapse${sessionDto.sessionID}" 
					aria-expanded="${status.first ? 'false' : 'true'}" aria-controls="collapse${sessionDto.sessionID}" >
			<fmt:message key="message.session.name" />:	<c:out value="${sessionDto.sessionName}" /></a>
			</span>
        </div>
        
        <div id="collapse${sessionDto.sessionID}" class="panel-collapse collapse ${status.first ? 'in' : ''}" role="tabpanel" aria-labelledby="heading${sessionDto.sessionID}">
	</c:if>
	
		<c:choose>
		<c:when test="${forum.reflectOnActivity}">
			<c:set var="numColumns">5</c:set>
			<c:set var="postingWidth">15%</c:set>
		</c:when>
		<c:otherwise>
			<c:set var="numColumns">4</c:set>
			<c:set var="postingWidth">25%</c:set>
		</c:otherwise>
		</c:choose>
		
		<lams:TSTable numColumns="${numColumns}" dataId="data-session-id='${sessionDto.sessionID}'">
				<th><fmt:message key="monitoring.user.fullname"/></th>
				<th width="5%" align="center"><fmt:message key="label.number.of.posts"/></th>
				<th width="${postingWidth}" align="center"><fmt:message key="label.latest.posting.date"/></th>
				<th width="10%" align="center"><fmt:message key="monitoring.marked.question"/></th>
				<c:if test="${forum.reflectOnActivity}">
					<th width="40%" align="center" class="sorter-false"><fmt:message key="monitoring.user.reflection"/></th>
				</c:if>
		</lams:TSTable>

		<P style="display: inline"> 
			<c:set var="viewforum">
				<html:rewrite page="/learning/viewForum.do?toolSessionID=${sessionDto.sessionID}&topicID=${topic.message.uid}&mode=teacher&hideReflection=true" />
			</c:set>
			<html:link href="javascript:launchPopup('${viewforum}');" styleClass="btn btn-default loffset5 voffset10">
				<fmt:message key="label.monitoring.summary.view.forum"/>
			</html:link>
			<html:button property="releaseMarks" onclick="releaseMarks(${sessionDto.sessionID})" styleClass="btn btn-default loffset5 voffset10" >
				<fmt:message key="button.release.mark" />
			</html:button>
			<html:form action="/monitoring/downloadMarks"  style="display:inline">
				<html:hidden property="toolSessionID" value="${sessionDto.sessionID}" />
				<html:submit property="downloadMarks" styleClass="btn btn-default loffset5 voffset10" >
					<fmt:message key="message.download.marks" />
				</html:submit>
			</html:form>
			<c:url value="/monitoring.do" var="refreshMonitoring">
				<c:param name="contentFolderID" value="${contentFolderID}"/>
				<c:param name="toolContentID" value="${toolContentID}" />
			</c:url>
			<html:link href="${refreshMonitoring}" styleClass="btn btn-default loffset5 voffset10" >
					<fmt:message key="label.refresh" />
			</html:link>
		</P>
	
	<c:if test="${sessionMap.isGroupedActivity}">
		</div> <!-- end collapse area  -->
		</div> <!-- end collapse panel  -->
	</c:if>
	${ !sessionMap.isGroupedActivity || ! status.last ? '<div class="voffset5">&nbsp;</div>' :  ''}

</c:forEach>

<c:if test="${sessionMap.isGroupedActivity}">
	</div> <!--  end panel group -->
</c:if>
<%@include file="parts/advanceOptions.jsp"%>

<%@include file="parts/daterestriction.jsp"%>
