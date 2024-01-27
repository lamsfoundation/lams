<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL /></c:set>
<%-- param has higher level for request attribute --%>
<c:if test="${not empty param.sessionMapID}">
	<c:set var="sessionMapID" value="${param.sessionMapID}" />
</c:if>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="mode" value="${sessionMap.mode}" />
<c:set var="toolSessionID" value="${sessionMap.toolSessionID}" />
<c:set var="assessment" value="${sessionMap.assessment}" />
<c:set var="pageNumber" value="${sessionMap.pageNumber}" />
<c:set var="isResubmitAllowed" value="${sessionMap.isResubmitAllowed}" />
<c:set var="hasEditRight" value="${sessionMap.hasEditRight}" />
<c:set var="result" value="${sessionMap.assessmentResult}" />
<c:set var="isUserLeader" value="${sessionMap.isUserLeader}" />
<c:set var="isLeadershipEnabled" value="${assessment.useSelectLeaderToolOuput}" />

<lams:PageLearner title="${assessment.title}" toolSessionID="${toolSessionID}">
	<link href="<lams:WebAppURL/>includes/css/assessment.css" rel="stylesheet" type="text/css">
	<link href="${lams}css/rating.css" rel="stylesheet" type="text/css">
	<style>
		.form-check-input[disabled] ~ .form-check-label, .form-check-input:disabled ~ .form-check-label {
		    opacity: 1 !important; /* overwrite bootstrap rule only for results page */ 
		}
	</style>
	<c:if test="${not empty codeStyles}">
		<link rel="stylesheet" type="text/css" href="${lams}css/codemirror.css" />
		<link rel="stylesheet" type="text/css" href="${lams}css/codemirror_simplescrollbars.css" />
		<style>
			pre {
				background-color: initial;
				border: none;
			}
		</style>
		<script type="text/javascript" src="${lams}includes/javascript/codemirror/addon/runmode/runmode-standalone.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/codemirror/addon/runmode/colorize.js"></script>
	</c:if>
	<%-- codeStyles is a set, so each code style will be listed only once --%>
	<c:forEach items="${codeStyles}" var="codeStyle">
		<c:choose>
			<c:when test="${codeStyle == 1}">
				<script type="text/javascript" src="${lams}includes/javascript/codemirror/mode/python.js"></script>
			</c:when>
			<c:when test="${codeStyle == 2}">
				<script type="text/javascript" src="${lams}includes/javascript/codemirror/mode/javascript.js"></script>
			</c:when>
			<c:when test="${codeStyle >= 3}">
				<script type="text/javascript" src="${lams}includes/javascript/codemirror/mode/clike.js"></script>
			</c:when>
		</c:choose>
	</c:forEach>
	<c:if test="${not empty codeStyles}">
		<script type="text/javascript">
			// initialise syntax highlighter depending on programming language in data-lang attribute
			$(document).ready(function() {
				CodeMirror.colorize($('.code-style'));
			});
		</script>
	</c:if>
	
	<script type="text/javascript">
		 checkNextGateActivity('finishButton', '${toolSessionID}', '', function(){
			 document.location.href ='<c:url value="/learning/finish.do"/>?sessionMapID=${sessionMapID}&mode=${mode}&toolSessionID=${toolSessionID}';
		 });
		 
		function disableButtons() {
			$('.btn').prop('disabled',true);
		}
		
		function nextPage(pageNumber){
			disableButtons();
        	document.location.href="<c:url value='/learning/nextPage.do'/>?sessionMapID=${sessionMapID}&pageNumber=" + pageNumber;
		}	
		
		function resubmit(){
			disableButtons();
			document.location.href ="<c:url value='/learning/resubmit.do?sessionMapID=${sessionMapID}'/>";
			return false;			
		}
    </script>
	
	<div id="container-main">
		<c:if test="${not empty sessionMap.submissionDeadline && (sessionMap.mode == 'author' || sessionMap.mode == 'learner')}">
			<lams:Alert5 id="submission-deadline" type="info" close="true">
				<fmt:message key="authoring.info.teacher.set.restriction" >
					<fmt:param><lams:Date value="${sessionMap.submissionDeadline}" /></fmt:param>
				</fmt:message>
			</lams:Alert5>
		</c:if>
		
		<c:if test="${sessionMap.isUserFailed}">
			<lams:Alert5 id="passing-mark-not-reached" type="warning" close="true">
				<fmt:message key="label.learning.havent.reached.passing.mark">
					<fmt:param>${assessment.passingMark}</fmt:param>
				</fmt:message>
			</lams:Alert5>
		</c:if>
		
		<c:if test="${isLeadershipEnabled and not empty sessionMap.groupLeader}">
			<lams:LeaderDisplay username="${sessionMap.groupLeader.firstName} ${sessionMap.groupLeader.lastName}" userId="${sessionMap.groupLeader.userId}"/>
		</c:if>
		
		<lams:errors5/>
		
		<c:if test="${not empty result}">
			<%@ include file="results/attemptsummary.jsp"%>
		</c:if>
		
		<c:if test="${assessment.displaySummary}">
			<c:if test="${not empty assessment.instructions}">
				<div id="instructions" class="instructions">
					<c:out value="${assessment.instructions}" escapeXml="false"/>
				</div>
			</c:if>

			<div id="all-questions">
				<%@ include file="results/allquestions.jsp"%>
				
				<%@ include file="parts/paging.jsp"%>
			</div>
		</c:if>

		<c:if test="${mode != 'teacher'}">
			<div class="activity-bottom-buttons">
				<c:if test="${!sessionMap.isUserFailed}">
					<button type="button" id="finishButton" name="FinishButton" class="btn btn-primary na">
						<c:choose>
							<c:when test="${sessionMap.isLastActivity}">
								<fmt:message key="label.submit" />
							</c:when>
							<c:otherwise>
								<fmt:message key="label.finished" />
							</c:otherwise>
						</c:choose>
					</button>
				</c:if>
				
				<c:if test="${isResubmitAllowed && hasEditRight}">
					<button type="button" onclick="resubmit()" class="btn btn-secondary btn-icon-return me-2">
						<fmt:message key="label.learning.resubmit" />
					</button>
				</c:if>
			</div>
		</c:if>
	</div>
</lams:PageLearner>
