<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL /></c:set>

<lams:html>
<lams:head>
	<title><fmt:message key="label.learning.title" /></title>
	<%@ include file="/common/header.jsp"%>
	
	<c:set var="localeLanguage"><lams:user property="localeLanguage" /></c:set>
	<script type="text/javascript" src="<lams:LAMSURL />/includes/javascript/jquery.timeago.js"></script> 
	<script type="text/javascript" src="${lams}includes/javascript/timeagoi18n/jquery.timeago.${fn:toLowerCase(localeLanguage)}.js"></script>

	<c:set var="ctxPath" value="${pageContext.request.contextPath}"	scope="request" />
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
		$(document).ready(function() {
			$("time.timeago").timeago();
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
			<h4>
				<fmt:message key="label.group.leader" >
					<fmt:param>${sessionMap.groupLeader.firstName} ${sessionMap.groupLeader.lastName}</fmt:param>
				</fmt:message>
			</h4>
		</c:if>
		
		<%@ include file="/common/messages.jsp"%>
		<br>
		
		<%@ include file="results/attemptsummary.jsp"%>
		
		<c:if test="${assessment.displaySummary}">
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
							<html:button property="FinishButton" onclick="return continueReflect()" 
									styleClass="btn btn-sm btn-default pull-left voffset10">
								<fmt:message key="label.edit" />
							</html:button>
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
							<html:button property="FinishButton" onclick="return continueReflect()" 
									styleClass="btn btn-primary voffset10 pull-right na">
								<fmt:message key="label.continue" />
							</html:button>
						</c:when>
						
						<c:otherwise>
							<button name="FinishButton"
									onClick="return finishSession()"
									class="btn btn-primary voffset10 pull-right na">
								<span class="nextActivity">
									<c:choose>
										<c:when test="${sessionMap.activityPosition.last}">
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