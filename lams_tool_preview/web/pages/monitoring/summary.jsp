<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL /></c:set>
<c:set var="webapp"><lams:WebAppURL/></c:set>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="summaryList" value="${sessionMap.summaryList}"/>

<link type="text/css" href="${lams}css/jquery-ui-bootstrap-theme.css" rel="stylesheet">
<lams:css suffix="jquery.jRating"/>
<link type="text/css" href="${webapp}includes/css/learning.css" rel="stylesheet" >
<link type="text/css" href="${lams}css/free.ui.jqgrid.min.css" rel="stylesheet">
<style type="text/css">
	#common-buttons-area {
		overflow: auto;
		margin-bottom: 10px;
	}
</style>

<lams:JSImport src="includes/javascript/common.js" />
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
</script>
<lams:JSImport src="includes/javascript/monitorToolSummaryAdvanced.js" />

<script src="${lams}includes/javascript/download.js" type="text/javascript" ></script>
<script src="${lams}includes/javascript/portrait.js" type="text/javascript" ></script>
<script type="text/javascript" src="${lams}includes/javascript/free.jquery.jqgrid.min.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.jRating.js"></script>
<lams:JSImport src="includes/javascript/rating.js" />
<script type="text/javascript">
	function exportResults() {
		var exportExcelUrl = '<c:url value="/monitoring/exportTeamReport.do"/>?<csrf:token/>&sessionMapID=${sessionMapID}&toolSessionId=${groupSummary.sessionId}&toolContentID=${sessionMap.toolContentID}';
		return downloadFile(exportExcelUrl, 'messageArea_Busy', '<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.file.downloaded"/></spring:escapeBody>', 'messageArea', 'btn-disable-on-submit');
	}

	function getResultsElement(sessionId, selector) {
		let element = null;
		if (sessionId) {
			// if Peer Review is grouped, try to find the element within own group
			element = $('#collapse' + sessionId + ' ' + selector);
		}
		if (!element || element.length == 0) {
			element = $(selector);
		}
		return element;
	}

	function sendResults(sessionId) {
		if (!confirm('<spring:escapeBody javaScriptEscape="true"><fmt:message key="confirm.notify.user.of.results" /></spring:escapeBody>')) {
			return;
		}
		let buttons = sessionId ? getResultsElement(sessionId, ".btn-disable-on-submit") : $('.btn-disable-on-submit'),
				messageArea = sessionId ? getResultsElement(sessionId, ".messageArea2") : $('#message-area-general'),
				messageAreaBusy = sessionId ? getResultsElement(sessionId, ".messageArea2_Busy") : $('#message-area-busy-general'),
				emailsSentIcons = sessionId ? $('#heading' + sessionId + ' .emails-sent-icon') : $('.emails-sent-icon'),
				url = "<c:url value="/monitoring/sendResultsToSessionUsers.do"/>";

		messageArea.html("");
		messageAreaBusy.show();
		buttons.prop("disabled", true);
		messageArea.load(
				url,
				{
					sessionMapID: "${sessionMapID}",
					toolContentID: ${sessionMap.toolContentID},
					toolSessionId: sessionId,
					reqID: (new Date()).getTime()
				},
				function() {
					messageAreaBusy.hide();
					buttons.prop("disabled", false);
					emailsSentIcons.removeClass('hidden');
				}
		);
		return false;
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
        	<a class="${status.first ? '' : 'collapsed'}" role="button" data-toggle="collapse"
			   href="#collapse${groupSummary.sessionId}"
			   aria-expanded="${status.first ? 'false' : 'true'}" aria-controls="collapse${groupSummary.sessionId}">
				<fmt:message key="monitoring.label.group"/>: ${groupSummary.sessionName}</a>
				<span title="<fmt:message key="label.notified.users.of.results" />" class="${groupSummary.emailsSent ? "" : "hidden"}">
  					<i class="emails-sent-icon loffset20 fa fa-envelope" aria-hidden="true"></i>
  					<i class="emails-sent-icon fa fa-check-circle text-success" aria-hidden="true"></i>
				</span>
			</span>
		</div>

		<div id="collapse${groupSummary.sessionId}" class="panel-collapse collapse ${status.first ? 'in' : ''}" role="tabpanel" aria-labelledby="heading${groupSummary.sessionId}">
	</c:if>

	<c:set var="rubricsCriteriaCounter" value="1" />
	<c:choose>
		<c:when test="${fn:length(criterias) eq 1}">
			<c:set var="criteria" value="${criterias[0]}" scope="request"/>
			<c:set var="toolSessionId" value="${groupSummary.sessionId}" scope="request"/>
			<c:set var="criteria" value="${criteria}" scope="request"/>
			<c:set var="sessionMap" value="${sessionMap}" scope="request"/>
			<c:choose>
				<c:when test="${criteria.rubricsStyleRating}">
					<c:set var="rubricsLearnerData" value="${rubricsData[toolSessionId]}" scope="request"/>
					<%@ include file="rubricspart.jsp" %>
				</c:when>
				<c:otherwise>
					<h4><c:out value="${criteria.title}" escapeXml="true"/></h4>
					<%@ include file="criteriapart.jsp" %>
				</c:otherwise>
			</c:choose>
		</c:when>
		<c:otherwise>
			<c:forEach var="criteria" items="${criterias}">
				<c:set var='url'><c:url value="/monitoring/criteria.do"/>?sessionMapID=${sessionMapID}&toolSessionId=${groupSummary.sessionId}&criteriaId=${criteria.ratingCriteriaId}</c:set>
				<button onclick="javascript:launchPopup('${url}');return false;" class="btn btn-default btn-disable-on-submit voffset5 loffset5">
					<fmt:message key="label.monitoring.view">
						<fmt:param>
							<c:choose>
								<c:when test="${criteria.rubricsStyleRating}">
									<fmt:message key="label.rating.style.rubrics" />&nbsp;${rubricsCriteriaCounter}
									<c:set var="rubricsCriteriaCounter" value="${rubricsCriteriaCounter + 1}" />
								</c:when>
								<c:otherwise>
									<c:out value="${criteria.title}" escapeXml="true"/>
								</c:otherwise>
							</c:choose>
						</fmt:param>
					</fmt:message>
				</button>
			</c:forEach>
		</c:otherwise>
	</c:choose>

	<div id="btns${groupSummary.sessionId}" class="offset5">
		<button onClick="return sendResults(${groupSummary.sessionId});"
				class="btn btn-default btn-disable-on-submit"><fmt:message key="label.notify.user.of.results"/></button>

		<c:if test="${fn:length(criterias) > 1}">
			<!--For send results feature-->
			<i class="fa fa-spinner messageArea2_Busy" style="display:none"></i>
			<div class="voffset5 loffset5 help-block messageArea2"></div>
		</c:if>
	</div>

	<c:if test="${sessionMap.isGroupedActivity}">
		</div> <!-- end collapse area  -->
		</div> <!-- end collapse panel  -->
	</c:if>
	${ !sessionMap.isGroupedActivity || ! status.last ? '<div class="voffset5">&nbsp;</div>' :  ''}

</c:forEach>

<c:if test="${sessionMap.isGroupedActivity}">
	</div> <!--  end panel group -->
</c:if>

<div style="text-align: right">
	<lams:WaitingSpinner id="message-area-busy-general"></lams:WaitingSpinner>
	<div class="voffset5 help-block" id="message-area-general"></div>
</div>

<div id="common-buttons-area">
	<button onClick="return exportResults()" class="btn btn-default loffset5 pull-right btn-disable-on-submit">
		<i class="fa fa-download" aria-hidden="true"></i>
		<fmt:message key="label.export.team.results" />
	</button>

	<button onClick="return sendResults();"
			class="btn btn-default loffset5 pull-right btn-disable-on-submit">
		<i class="fa fa-envelope"></i>
		<fmt:message key="label.notify.user.of.results.all"/>
	</button>

	<c:if test="${!empty summaryList}">
		<c:set var='url'>
			<c:url value="/monitoring/manageUsers.do"/>?sessionMapID=${sessionMapID}
		</c:set>
		<button onClick="javascript:launchPopup('${url}');return false;"
				class="btn btn-default btn-disable-on-submit pull-right">
			<i class="fa fa-user-circle-o" aria-hidden="true"></i>
			<fmt:message key="label.manage.users" />
		</button>
	</c:if>
</div>


<c:if test="${!sessionMap.tblMonitoring}">
	<%@ include file="advanceoptions.jsp"%>
</c:if>