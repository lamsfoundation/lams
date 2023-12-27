<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL /></c:set>
<c:set var="webapp"><lams:WebAppURL/></c:set>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="summaryList" value="${sessionMap.summaryList}"/>

<lams:css suffix="jquery.jRating"/>
<link type="text/css" href="${webapp}includes/css/learning.css" rel="stylesheet" >

<script type="text/javascript">
	//var for jquery.jRating.js
	var pathToImageFolder = "${lams}images/css/";

	//vars for rating.js
	var MAX_RATES = 0,
			MIN_RATES = 0,
			COMMENTS_MIN_WORDS_LIMIT = 0,
			MAX_RATINGS_FOR_ITEM = 0,
			COUNT_RATED_ITEMS = 0,
			COMMENT_TEXTAREA_TIP_LABEL = '',
			WARN_COMMENTS_IS_BLANK_LABEL = '',
			WARN_MIN_NUMBER_WORDS_LABEL = '';
</script>
<lams:JSImport src="includes/javascript/monitorToolSummaryAdvanced.js" />

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
		showConfirm('<spring:escapeBody javaScriptEscape="true"><fmt:message key="confirm.notify.user.of.results" /></spring:escapeBody>', function(){
			let buttons = getResultsElement(sessionId, ".btn-disable-on-submit"),
					messageArea = getResultsElement(sessionId, ".messageArea2"),
					messageAreaBusy = getResultsElement(sessionId, ".messageArea2_Busy"),
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
					}
			);
		});
	}
</script>

<div class="container-fluid">
	<div class="row mb-2">
		<div class="col-4 offset-4">
			<h3 class="text-center">
				<c:out value="${sessionMap.peerreview.title}" escapeXml="true"/>
			</h3>
		</div>
	</div>

	<div class="row">
		<div class="col-10 offset-1 instructions">
			<c:out value="${sessionMap.peerreview.instructions}" escapeXml="false"/>
		</div>
	</div>

	<c:if test="${empty summaryList}">
		<div class="row">
			<div class="col-4 offset-4">
				<lams:Alert type="info" id="no-session-summary" close="false">
					<fmt:message key="message.monitoring.summary.no.session" />
				</lams:Alert>
			</div>
		</div>
	</c:if>

	<div class="row">
		<div class="col-4 offset-4">
			<!--For send results feature-->
			<lams:WaitingSpinner id="messageArea_Busy"></lams:WaitingSpinner>
			<div class="mt-3 help-block" id="messageArea"></div>
		</div>
	</div>

	<c:if test="${sessionMap.isGroupedActivity}">
	<div class="accordion" id="accordionSessions" aria-multiselectable="true">
		</c:if>

		<c:forEach var="groupSummary" items="${summaryList}" varStatus="status">

			<c:if test="${sessionMap.isGroupedActivity}">
				<div class="accordion-item">
				<h4 class="accordion-header" id="heading${groupSummary.sessionId}">
					<button class="accordion-button "${status.first ? '' : 'collapsed'}" type="button"
					data-bs-toggle="collapse" data-bs-target="#collapse${groupSummary.sessionId}"
					aria-expanded="${status.first ? 'false' : 'true'}">
					<fmt:message key="monitoring.label.group" />: ${groupSummary.sessionName}</a>
					</button>
				</h4>

				<div id="collapse${groupSummary.sessionId}" class="accordion-collapse collapse ${status.first ? 'show' : ''}"
				aria-labelledby="heading${groupSummary.sessionId}">
				<div class="accordion-body">
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
						<button onclick="javascript:launchPopup('${url}');return false;" class="btn btn-secondary btn-disable-on-submit">
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

			<div id="btns${groupSummary.sessionId}" class="mt-2">
				<button onClick="return sendResults(${groupSummary.sessionId});" class="btn btn-secondary btn-disable-on-submit"><fmt:message key="label.notify.user.of.results"/></button>

				<c:if test="${fn:length(criterias) > 1}">
					<!--For send results feature-->
					<i class="fa fa-spinner messageArea2_Busy" style="display:none"></i>
					<div class="voffset5 messageArea2"></div>
				</c:if>
			</div>

			<c:if test="${sessionMap.isGroupedActivity}">
				</div> <!-- end accordion body  -->
				</div> <!-- end accordion collapse  -->
				</div> <!-- end accordion item  -->
			</c:if>
		</c:forEach>

		<c:if test="${sessionMap.isGroupedActivity}">
	</div> <!--  end accordion -->
	</c:if>

	<div id="common-buttons-area" class="mt-2 d-flex justify-content-end">
		<c:if test="${!empty summaryList}">
			<c:set var='url'>
				<c:url value="/monitoring/manageUsers.do"/>?sessionMapID=${sessionMapID}
			</c:set>
			<button onClick="javascript:launchPopup('${url}');return false;" class="btn btn-secondary btn-disable-on-submit">
				<i class="fa fa-user-circle-o" aria-hidden="true"></i>
				<fmt:message key="label.manage.users" />
			</button>
		</c:if>

		<button onClick="return exportResults()" class="btn btn-secondary ms-2 btn-disable-on-submit">
			<i class="fa fa-download" aria-hidden="true"></i>
			<fmt:message key="label.export.team.results" />
		</button>
	</div>
</div>