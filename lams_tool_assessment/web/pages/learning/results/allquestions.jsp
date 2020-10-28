<%@ include file="/common/taglibs.jsp"%>

<c:set var="lams"><lams:LAMSURL /></c:set>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="assessment" value="${sessionMap.assessment}" />
<c:set var="pageNumber" value="${sessionMap.pageNumber}" />
<c:set var="hasEditRight" value="${sessionMap.hasEditRight}"/>
<c:set var="isUserLeader" value="${sessionMap.isUserLeader}"/>
<c:set var="isLeadershipEnabled" value="${assessment.useSelectLeaderToolOuput}"/>
<c:set var="localeLanguage"><lams:user property="localeLanguage" /></c:set>

<c:if test="${param.embedded}">
	<lams:css suffix="jquery.jRating"/>
	<link href="<lams:WebAppURL/>includes/css/assessment.css" rel="stylesheet" type="text/css">
</c:if>

<link rel="stylesheet" type="text/css" href="${lams}css/bootstrap-slider.css" />
<style>
	tr.selected-by-groups td {
		border-top: none !important;
	}
	
	tr.selected-by-groups span {
		font-weight: bold;
	}
	.slider.slider-horizontal {
		margin-left: 40px;
	}
</style>
	
<script type="text/javascript" src="${lams}includes/javascript/jquery.timeago.js"></script> 
<script type="text/javascript" src="${lams}includes/javascript/timeagoi18n/jquery.timeago.${fn:toLowerCase(localeLanguage)}.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/bootstrap-slider.js"></script>
<script type="text/javascript">
	//var for jquery.jRating.js
	var pathToImageFolder = "${lams}images/css/",
	
	//vars for rating.js
		AVG_RATING_LABEL = '<fmt:message key="label.average.rating"><fmt:param>@1@</fmt:param><fmt:param>@2@</fmt:param></fmt:message>',
		YOUR_RATING_LABEL = '<fmt:message key="label.your.rating"><fmt:param>@1@</fmt:param><fmt:param>@2@</fmt:param><fmt:param>@3@</fmt:param></fmt:message>',
		COMMENTS_MIN_WORDS_LIMIT = 0,
		MAX_RATES = 0,
		MIN_RATES = 0,
		LAMS_URL = '${lams}',
		COUNT_RATED_ITEMS = true,
		COMMENT_TEXTAREA_TIP_LABEL = '<fmt:message key="label.comment.textarea.tip"/>',
		WARN_COMMENTS_IS_BLANK_LABEL = '<fmt:message key="error.resource.image.comment.blank"/>',
		ALLOW_RERATE = false;
	<%-- Do not allow rating if it is a teacher view --%>
	<c:if test="${not empty toolSessionID}">
		var SESSION_ID = ${toolSessionID};
	</c:if>
	
	function refreshToRating(questionUid) {
		// LDEV-5052 Refresh page and scroll to the given ID on comment submit

		// setting href does not navigate if url contains #, we still need a reload
		location.hash = '#rating-table-' + questionUid;
		location.reload();
		return false;
	}
	
	$(document).ready(function() {
		$("time.timeago").timeago();
	
		//initialize bootstrap-sliders if "Enable confidence level" option is ON
		$('.bootstrap-slider').bootstrapSlider();
	
		<%-- Connect to command websocket only if it is learner UI --%>
		<c:if test="${not empty toolSessionID}">
			// command websocket stuff for refreshing
			// trigger is an unique ID of page and action that command websocket code in Page.tag recognises
			commandWebsocketHookTrigger = 'assessment-results-refresh-${assessment.contentId}';
			// if the trigger is recognised, the following action occurs
			commandWebsocketHook = function() {
				location.reload();
			};
		</c:if>
	});
</script>
<script type="text/javascript" src="${lams}includes/javascript/rating.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.jRating.js"></script>
	
<c:forEach var="question" items="${sessionMap.pagedQuestions[pageNumber-1]}" varStatus="status">						
							
	<div class="panel panel-default">
		<div class="panel-heading">			
			<c:if test="${param.embedded and empty toolSessionID and assessment.allowDiscloseAnswers}">
				<div class="btn-group-xs pull-right disclose-button-group" questionUid="${question.uid}">
					<%-- Allow disclosing correct answers only for multiple choice questions --%>
					<c:if test="${question.type == 1}">
						<div class="btn btn-default disclose-correct-button"
							<c:if test="${question.correctAnswersDisclosed}">
								disabled="disabled"><i class="fa fa-check text-success">&nbsp;</i
							</c:if>
							>
							<fmt:message key="label.disclose.correct.answers"/>
						</div>
					</c:if>
					<div class="btn btn-default disclose-groups-button"
						<c:if test="${question.groupsAnswersDisclosed}">
							disabled="disabled"><i class="fa fa-check text-success">&nbsp;</i
						</c:if>
						>
						<fmt:message key="label.disclose.groups.answers"/>
					</div>
				</div>
			</c:if>
			
			<h3 class="panel-title" style="margin-bottom: 10px;font-size: initial;">
				<c:if test="${assessment.numbered}">
						${status.index + sessionMap.questionNumberingOffset}.
				</c:if>

  				<c:if test="${not sessionMap.hideTitles}">
					${question.title}
				</c:if>
			</h3>
			
			<c:if test="${question.answerRequired}">
				<span class="asterisk pull-right">
					<i class="fa fa-xs fa-asterisk text-danger" title="<fmt:message key="label.answer.required"/>" 
							alt="<fmt:message key="label.answer.required"/>"></i>
				</span>
			</c:if>
							
			<c:if test="${empty question.question}">
				<!--  must have something here otherwise the question-numbers span does not float properly -->
				&nbsp;
			</c:if>
			${question.question}
		</div>
					
		<div class="panel-body" id="question-area-${status.index}">
			<c:choose>
				<c:when test="${question.type == 1}">
					<%@ include file="multiplechoice.jsp"%>
				</c:when>
				<c:when test="${question.type == 2}">
					<%@ include file="matchingpairs.jsp"%>
				</c:when>
				<c:when test="${question.type == 3}">
					<%@ include file="vsa.jsp"%>
				</c:when>
				<c:when test="${question.type == 4}">
					<%@ include file="numerical.jsp"%>
				</c:when>
				<c:when test="${question.type == 5}">
					<%@ include file="truefalse.jsp"%>
				</c:when>
				<c:when test="${question.type == 6}">
					<%@ include file="essay.jsp"%>
				</c:when>
				<c:when test="${question.type == 7}">
					<%@ include file="ordering.jsp"%>
				</c:when>
				<c:when test="${question.type == 8}">
					<c:set var="questionIndex" value="${status.index}"/>
					<%@ include file="markhedging.jsp"%>
				</c:when>			
			</c:choose>
			
			<c:if test="${question.type != 8}">
				<%@ include file="markandpenaltyarea.jsp"%>
			</c:if>
			
			<c:if test="${not empty toolSessionID}">			
				<%@ include file="historyresponses.jsp"%>
				
				<c:if test="${assessment.enableConfidenceLevels}">
					<%@ include file="confidencelevel.jsp"%>
				</c:if>
				
				<c:if test="${assessment.allowAnswerJustification and not empty question.justification}">
					<div class="question-type">
						<fmt:message key="label.answer.justification" />
					</div>
					<p>
						<c:out value="${question.justificationHtml}" escapeXml="false" />
					</p>
				</c:if>
			</c:if>
		</div>
					
	</div>
</c:forEach>