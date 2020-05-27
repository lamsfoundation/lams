<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL /></c:set>

<lams:html>
<lams:head>
	<title><fmt:message key="label.learning.title" /></title>
	<%@ include file="/common/header.jsp"%>
	<lams:css suffix="jquery.jRating"/>
	
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
	
	<c:set var="localeLanguage"><lams:user property="localeLanguage" /></c:set>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.timeago.js"></script> 
	<script type="text/javascript" src="${lams}includes/javascript/timeagoi18n/jquery.timeago.${fn:toLowerCase(localeLanguage)}.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/bootstrap-slider.js"></script>

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
	<c:set var="hasEditRight" value="${sessionMap.hasEditRight}"/>
	<c:set var="result" value="${sessionMap.assessmentResult}" />
	<c:set var="isUserLeader" value="${sessionMap.isUserLeader}"/>
	<c:set var="isLeadershipEnabled" value="${assessment.useSelectLeaderToolOuput}"/>
	
		
	<script type="text/javascript">
		//var for jquery.jRating.js
		var pathToImageFolder = "${lams}images/css/";
		
		//vars for rating.js
		var AVG_RATING_LABEL = '<fmt:message key="label.average.rating"><fmt:param>@1@</fmt:param><fmt:param>@2@</fmt:param></fmt:message>',
		YOUR_RATING_LABEL = '<fmt:message key="label.your.rating"><fmt:param>@1@</fmt:param><fmt:param>@2@</fmt:param><fmt:param>@3@</fmt:param></fmt:message>',
		COMMENTS_MIN_WORDS_LIMIT = 0,
		MAX_RATES = 0,
		MIN_RATES = 0,
		LAMS_URL = '${lams}',
		COUNT_RATED_ITEMS = true,
		COMMENT_TEXTAREA_TIP_LABEL = '<fmt:message key="label.comment.textarea.tip"/>',
		WARN_COMMENTS_IS_BLANK_LABEL = '<fmt:message key="error.resource.image.comment.blank"/>',
		ALLOW_RERATE = false,
		SESSION_ID = ${toolSessionID};
	</script>
	<script type="text/javascript" src="${lams}includes/javascript/rating.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.jRating.js"></script>
	
	<script type="text/javascript">
		$(document).ready(function() {
			$("time.timeago").timeago();

			//initialize bootstrap-sliders if "Enable confidence level" option is ON
			$('.bootstrap-slider').bootstrapSlider();

			// command websocket stuff for refreshing
			// trigger is an unique ID of page and action that command websocket code in Page.tag recognises
			commandWebsocketHookTrigger = 'assessment-results-refresh-${assessment.contentId}';
			// if the trigger is recognised, the following action occurs
			commandWebsocketHook = function() {
				location.reload();
			};
		});

		function disableButtons() {
			$('.btn').prop('disabled',true);
		}
		
		function finishSession(){
			disableButtons();
			document.location.href ='<c:url value="/learning/finish.do"/>?sessionMapID=${sessionMapID}&mode=${mode}&toolSessionID=${toolSessionID}';
			return false;
		}
		
		function continueReflect(){
			disableButtons();
			document.location.href='<c:url value="/learning/newReflection.do"/>?sessionMapID=${sessionMapID}';
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
		
		function refreshToRating(questionUid) {
			// LDEV-5052 Refresh page and scroll to the given ID on comment submit
			var url = location.href,
				anchorIndex = url.lastIndexOf('#');
			if (anchorIndex > 0) {
				url = url.substring(0, anchorIndex);
			}
			url += '#rating-table-' + questionUid;
			location.href = url;
		}
    </script>
</lams:head>
<body class="stripes">

	<lams:Page type="learner" title="${assessment.title}">
		
		<c:if test="${not empty sessionMap.submissionDeadline && (sessionMap.mode == 'author' || sessionMap.mode == 'learner')}">
			<lams:Alert id="submission-deadline" type="info" close="true">
				<fmt:message key="authoring.info.teacher.set.restriction" >
					<fmt:param><lams:Date value="${sessionMap.submissionDeadline}" /></fmt:param>
				</fmt:message>
			</lams:Alert>
		</c:if>
		
		<c:if test="${sessionMap.isUserFailed}">
			<lams:Alert id="passing-mark-not-reached" type="warning" close="true">
				<fmt:message key="label.learning.havent.reached.passing.mark">
					<fmt:param>${assessment.passingMark}</fmt:param>
				</fmt:message>
			</lams:Alert>
		</c:if>
		
		<c:if test="${isLeadershipEnabled}">
			<lams:LeaderDisplay username="${sessionMap.groupLeader.firstName} ${sessionMap.groupLeader.lastName}" userId="${sessionMap.groupLeader.userId}"/>
		</c:if>
		
		<lams:errors/>
		<br>
		
		<%@ include file="results/attemptsummary.jsp"%>
		
		<c:if test="${assessment.displaySummary}">
			<div class="panel">
				<c:out value="${assessment.instructions}" escapeXml="false"/>
			</div>

			<div class="form-group">
				<%@ include file="results/allquestions.jsp"%>
				
				<%@ include file="parts/paging.jsp"%>
			</div>
		</c:if>
		
		<%-- Reflection entry --%>
		<c:if test="${sessionMap.reflectOn && (sessionMap.userFinished || !hasEditRight)}">
		 	 <div class="panel panel-default">
	
				<div class="panel-heading panel-title">
			 		<fmt:message key="label.export.reflection" />
			 	</div>
				 	
			 	<div class="panel-body">
				 	<div class="panel">
				 		<lams:out value="${sessionMap.reflectInstructions}" escapeHtml="true"/>
					</div>
						
					<div class="form-group">
						<c:choose>
							<c:when test="${empty sessionMap.reflectEntry}">
								<p>
									<em> <fmt:message key="message.no.reflection.available" />	</em>
								</p>
							</c:when>
							<c:otherwise>
								<p>
									<lams:out escapeHtml="true" value="${sessionMap.reflectEntry}" />
								</p>
							</c:otherwise>
						</c:choose>
			
						<c:if test="${(mode != 'teacher') && hasEditRight}">
							<button type="button" name="FinishButton" onclick="return continueReflect()" 
									class="btn btn-sm btn-default pull-left voffset10">
								<fmt:message key="label.edit" />
							</button>
						</c:if>
					</div>
		
				</div>
			</div>
		</c:if>

		<c:if test="${mode != 'teacher'}">
			<div class="space-bottom-top align-right">
				<c:if test="${isResubmitAllowed && hasEditRight}">
					<button type="submit" onclick="resubmit()" class="btn btn-default">
						<fmt:message key="label.learning.resubmit" />
					</button>
				</c:if>	
						
				<c:if test="${! sessionMap.isUserFailed}">
					<c:choose>
						<c:when	test="${sessionMap.reflectOn && (not sessionMap.userFinished) && hasEditRight}">
							<button type="button" name="FinishButton" onclick="return continueReflect()" 
									class="btn btn-primary voffset10 pull-right na">
								<fmt:message key="label.continue" />
							</button>
						</c:when>
						
						<c:otherwise>
							<button name="FinishButton"
									onClick="return finishSession()"
									class="btn btn-primary voffset10 pull-right na">
								<span class="nextActivity">
									<c:choose>
										<c:when test="${sessionMap.isLastActivity}">
											<fmt:message key="label.submit" />
										</c:when>
										<c:otherwise>
							 				<fmt:message key="label.finished" />
										</c:otherwise>
									</c:choose>
		 						</span>
							</button>
						</c:otherwise>
					</c:choose>
				</c:if>
			</div>
		</c:if>

	</lams:Page>

</body>
</lams:html>