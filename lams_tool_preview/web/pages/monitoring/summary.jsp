<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL /></c:set>
<c:set var="webapp"><lams:WebAppURL/></c:set>

<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="summaryList" value="${sessionMap.summaryList}"/>

<link type="text/css" href="${lams}css/jquery-ui-smoothness-theme.css" rel="stylesheet">
<link type="text/css" href="${lams}css/jquery.jqGrid.css" rel="stylesheet" />
<link type="text/css" href="${lams}css/jquery.jRating.css" rel="stylesheet"/>
<link rel="stylesheet" href="${webapp}includes/css/learning.css'/>">
<style type="text/css">
	#common-buttons-area {
		overflow: auto;
		margin-bottom: 10px;
	}
</style>

<script type="text/javascript" src="${lams}includes/javascript/jquery.cookie.js"></script>
<script type="text/javascript">
	//var for jquery.jRating.js
	var pathToImageFolder = "${lams}images/css/";
		
	//vars for rating.js
	var MAX_RATES = 0,
	MIN_RATES = 0,
	COMMENTS_MIN_WORDS_LIMIT = 0,
	MAX_RATINGS_FOR_ITEM = 0,
	LAMS_URL = '',
	COUNT_RATED_ITEMS = 0,
	COMMENT_TEXTAREA_TIP_LABEL = '',
	WARN_COMMENTS_IS_BLANK_LABEL = '',
	WARN_MIN_NUMBER_WORDS_LABEL = '';
	
	function sendResults(sessionId) {
		var url = "<c:url value="/monitoring/sendResultsToSessionUsers.do"/>";
		$("#messageArea").html("");
		$("#messageArea_Busy").show();
		$(".btn-disable-on-submit").prop("disabled", true);
		$("#messageArea").load(
			url,
			{
				sessionMapID: "${sessionMapID}",
				toolContentID: ${sessionMap.toolContentID},
				toolSessionId: sessionId, 
				reqID: (new Date()).getTime()
			},
			function() {
				$("#messageArea_Busy").hide();
				$(".btn-disable-on-submit").prop("disabled", false);
			}
		);
		return false;
	}
	
</script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.jqGrid.locale-en.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.jqGrid.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/monitorToolSummaryAdvanced.js" ></script>
<script src="${lams}includes/javascript/jquery.jRating.js" type="text/javascript"></script>
<script src="${lams}includes/javascript/rating.js" type="text/javascript" ></script>
<script src="${lams}includes/javascript/download.js" type="text/javascript" ></script>

<script type="text/javascript">
	function exportResults() {
		var exportExcelUrl = 'exportTeamReport.do?sessionMapID=${sessionMapID}&toolSessionId=${groupSummary.sessionId}&toolContentID=${sessionMap.toolContentID}';
		return downloadFile(exportExcelUrl, 'messageArea_Busy', '<fmt:message key="label.file.downloaded"/>', 'messageArea', 'btn-disable-on-submit');
	}
</script>

<div class="panel">
	<h4>
	    <c:out value="${sessionMap.peerreview.title}" escapeXml="true"/>
	</h4>
	<div class="instructions voffset5">
		<c:out value="${sessionMap.peerreview.instructions}" escapeXml="false"/>
	</div>
	
	<c:if test="${empty summaryList}">
		<lams:Alert type="info" id="no-session-summary" close="false">
			<fmt:message key="message.monitoring.summary.no.session" />
		</lams:Alert>
	</c:if>
	
	<!--For send results feature-->
	<lams:WaitingSpinner id="messageArea_Busy"></lams:WaitingSpinner>
	<div class="voffset5 help-block" id="messageArea"></div>
	
</div>

<c:if test="${sessionMap.isGroupedActivity}">
	<div class="panel-group" id="accordionSessions" role="tablist" aria-multiselectable="true"> 
</c:if>

<c:forEach var="groupSummary" items="${summaryList}" varStatus="status">
	
 	<c:if test="${sessionMap.isGroupedActivity}">
	    <div class="panel panel-default" >
        <div class="panel-heading" id="heading${groupSummary.sessionId}">
        	<span class="panel-title collapsable-icon-left">
        	<a class="${status.first ? '' : 'collapsed'}" role="button" data-toggle="collapse" href="#collapse${groupSummary.sessionId}" 
					aria-expanded="${status.first ? 'false' : 'true'}" aria-controls="collapse${groupSummary.sessionId}">
			<fmt:message key="monitoring.label.group" />: ${groupSummary.sessionName}</a>
			</span>
        </div>
        
        <div id="collapse${groupSummary.sessionId}" class="panel-collapse collapse ${status.first ? 'in' : ''}" role="tabpanel" aria-labelledby="heading${groupSummary.sessionId}">
	</c:if>
	
	<c:choose>
		<c:when test="${not empty criterias && fn:length(criterias) eq 1}">
			<c:forEach var="criteria" items="${criterias}">
			<h4><c:out value="${criteria.title}" escapeXml="true"/></h4>
			<c:set var="toolSessionId" value="${groupSummary.sessionId}" scope="request"/>
			<c:set var="criteria" value="${criteria}" scope="request"/>
			<c:set var="sessionMap" value="${sessionMap}" scope="request"/>
			<jsp:include page="criteriapart.jsp"/>
			</c:forEach>
		</c:when>
		<c:otherwise>
			<c:forEach var="criteria" items="${criterias}">
				<c:set var='url'>criteria.do?sessionMapID=${sessionMapID}&toolSessionId=${groupSummary.sessionId}&criteriaId=${criteria.ratingCriteriaId}</c:set>
				<button onclick="javascript:launchPopup('${url}');return false;" class="btn btn-default btn-disable-on-submit voffset5 loffset5">
					<fmt:message key="label.monitoring.view"><fmt:param><c:out value="${criteria.title}" escapeXml="true"/></fmt:param></fmt:message></button>
			</c:forEach>
		</c:otherwise>
	</c:choose>

	<c:if test="${sessionMap.peerreview.reflectOnActivity || sessionMap.peerreview.notifyUsersOfResults}">
	<div id="btns${groupSummary.sessionId}" class="offset5">
		<c:set var="offset"></c:set>
		<c:if test="${sessionMap.peerreview.reflectOnActivity}">
			<c:set var='url'>reflections.do?sessionMapID=${sessionMapID}&toolSessionId=${groupSummary.sessionId}&toolContentID=${sessionMap.toolContentID}</c:set>
			<button onclick="javascript:launchPopup('${url}');return false;" class="btn btn-default  btn-disable-on-submit ${offset}"><fmt:message key="title.reflection"/></button>
			<c:set var="offset">loffset5</c:set>
		</c:if>
		<c:if test="${sessionMap.peerreview.notifyUsersOfResults}">
			<button onClick="return sendResults(${groupSummary.sessionId});" class="btn btn-default btn-disable-on-submit ${offset}"><fmt:message key="label.notify.user.of.results"/></button>
		</c:if>
	</div>
	</c:if>
	
	<c:if test="${sessionMap.isGroupedActivity}">
		</div> <!-- end collapse area  -->
		</div> <!-- end collapse panel  -->
	</c:if>
	${ !sessionMap.isGroupedActivity || ! status.last ? '<div class="voffset5">&nbsp;</div>' :  ''}

</c:forEach>

<c:if test="${sessionMap.isGroupedActivity}">
	</div> <!--  end panel group -->
</c:if>

<div id="common-buttons-area">
	<button onClick="return exportResults()" class="btn btn-default loffset5 pull-right btn-disable-on-submit">
		<i class="fa fa-download" aria-hidden="true"></i> 
		<fmt:message key="label.export.team.results" />
	</button>
	
	<c:if test="${!empty summaryList}">
		<c:set var='url'>
			<c:url value="/monitoring/manageUsers.do"/>?sessionMapID=${sessionMapID}
		</c:set>
		<button onClick="javascript:launchPopup('${url}');return false;" class="btn btn-default btn-disable-on-submit pull-right">
			<i class="fa fa-check-square" aria-hidden="true"></i> 
			<fmt:message key="label.manage.users" />
		</button>
	</c:if>
</div>

<%@ include file="advanceoptions.jsp"%>
