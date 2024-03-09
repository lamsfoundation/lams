<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL /></c:set>
<c:set var="webapp"><lams:WebAppURL/></c:set>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="summaryList" value="${sessionMap.summaryList}"/>

<link type="text/css" href="${lams}css/jquery-ui-bootstrap-theme5.css" rel="stylesheet">
<link href="${lams}css/rating.css" rel="stylesheet" type="text/css">
<link type="text/css" href="${webapp}includes/css/learning.css" rel="stylesheet" >
<link type="text/css" href="${lams}css/free.ui.jqgrid.custom.css" rel="stylesheet">

<script type="text/javascript">
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
<script type="text/javascript" src="${lams}includes/javascript/free.jquery.jqgrid.min.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.cookie.js"></script>
<script src="${lams}includes/javascript/download.js" type="text/javascript" ></script>
<script src="${lams}includes/javascript/portrait5.js" type="text/javascript" ></script>
<lams:JSImport src="includes/javascript/dialog5.js" />
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

	function sendResults(sessionId, isNotifyAllUsers) {
		showConfirm('<spring:escapeBody javaScriptEscape="true"><fmt:message key="confirm.notify.user.of.results" /></spring:escapeBody>', function(){
			let buttons = isNotifyAllUsers ? $('.btn-disable-on-submit') : getResultsElement(sessionId, ".btn-disable-on-submit"),
				messageArea = isNotifyAllUsers ?  $('#message-area-general') : getResultsElement(sessionId, ".messageArea2"),
				messageAreaBusy = isNotifyAllUsers ? $('#message-area-busy-general') : getResultsElement(sessionId, ".messageArea2_Busy"),
				emailsSentIcons = isNotifyAllUsers ? $('.emails-sent-icon') : $('#heading' + sessionId + ' .emails-sent-icon');

			messageArea.html("");
			messageAreaBusy.show();
			buttons.prop("disabled", true);
			messageArea.load(
					"<c:url value="/monitoring/sendResultsToSessionUsers.do"/>",
					{
						sessionMapID: "${sessionMapID}",
						toolContentID: ${sessionMap.toolContentID},
						toolSessionId: sessionId ? sessionId : "",
						reqID: (new Date()).getTime()
					},
					function() {
						messageAreaBusy.hide();
						buttons.prop("disabled", false);
						$.each(emailsSentIcons, (index, item) => {
						    $(item).parent().removeClass('d-none');
						});
					}
			);
		});
	}
</script>

<div class="container-main ms-4">
	<h3 class="mb-2">
		<c:out value="${sessionMap.peerreview.title}" escapeXml="true"/>
	</h3>
	<div class="instructions">
		<c:out value="${sessionMap.peerreview.instructions}" escapeXml="false"/>
	</div>

	<c:if test="${empty summaryList}">
		<lams:Alert5 type="info" id="no-session-summary">
			<fmt:message key="message.monitoring.summary.no.session" />
		</lams:Alert5>
	</c:if>

	<div class="mt-4">
		<!--For send results feature-->
		<lams:WaitingSpinner id="messageArea_Busy"></lams:WaitingSpinner>
		<div class="clearfix my-2">
			<div class="badge text-bg-info help-block" id="messageArea"></div>
		</div>
	</div>

	<c:if test="${sessionMap.isGroupedActivity}">
		<div class="accordion" id="accordionSessions" aria-multiselectable="true">
	</c:if>

	<c:forEach var="groupSummary" items="${summaryList}" varStatus="status">
		<c:if test="${sessionMap.isGroupedActivity}">
			<div class="accordion-item">
				<h4 class="accordion-header" id="heading${groupSummary.sessionId}">
					<button class="accordion-button ${status.first ? '' : 'collapsed'}" type="button"
							data-bs-toggle="collapse" data-bs-target="#collapse${groupSummary.sessionId}"
							aria-expanded="${status.first}">
						<fmt:message key="monitoring.label.group" />: ${groupSummary.sessionName}</a>
						
						<span title="<fmt:message key="label.notified.users.of.results" />" class="${groupSummary.emailsSent ? '' : 'd-none'}">
		  					<i class="emails-sent-icon ms-3 fa fa-envelope" aria-hidden="true"></i>
  							<i class="emails-sent-icon fa fa-check-circle text-success" aria-hidden="true"></i>
						</span>
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
					<button type="button" onclick="launchPopup('${url}')" class="btn btn-light btn-disable-on-submit me-2">
						<i class="fa-solid fa-eye me-1"></i>
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
			<c:if test="${sessionMap.isGroupedActivity}">
				<button type="button" onclick="sendResults(${groupSummary.sessionId}, false)" class="btn btn-light btn-disable-on-submit">
					<i class="fa-solid fa-envelope me-1"></i>
					<fmt:message key="label.notify.user.of.results"/>
				</button>
			</c:if>

			<c:if test="${fn:length(criterias) > 1}">
				<!--For send results feature-->
				<i class="fa fa-spinner messageArea2_Busy" style="display:none"></i>
				<div class="clearfix">
					<div class="badge text-bg-info messageArea2 mt-2"></div>
				</div>
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

	<div class="clearfix">
	<div class="float-end">
		<lams:WaitingSpinner id="message-area-busy-general"></lams:WaitingSpinner>
		<div class=" my-2">
			<div class="badge text-bg-info help-block" id="message-area-general"></div>
		</div>
	</div>
	</div>

	<div id="activity-bottom-buttons" class="mt-2 d-flex justify-content-end my-3">
		<c:if test="${!empty summaryList}">
			<c:set var='url'>
				<c:url value="/monitoring/manageUsers.do"/>?sessionMapID=${sessionMapID}
			</c:set>
			<button type="button" onclick="launchPopup('${url}')" class="btn btn-light btn-disable-on-submit">
				<i class="fa-solid fa-users-gear me-1"></i>
				<fmt:message key="label.manage.users" />
			</button>
		</c:if>
		
		<c:if test="${!sessionMap.isTbl}">
			<button type="button" onclick="sendResults(null, true)" class="btn btn-light ms-2 btn-disable-on-submit">
				<i class="fa-solid fa-envelope me-1"></i>
				<fmt:message key="label.notify.user.of.results.all"/>
			</button>
		</c:if>

		<button type="button" onclick="exportResults()" class="btn btn-light ms-2 btn-disable-on-submit">
			<i class="fa fa-download me-1" aria-hidden="true"></i>
			<fmt:message key="label.export.team.results" />
		</button>
	</div>

	<c:if test="${!sessionMap.isTbl}">
		<%@ include file="advanceoptions.jsp"%>
	</c:if>
</div>
