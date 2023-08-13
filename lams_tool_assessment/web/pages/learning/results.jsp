<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL /></c:set>

<lams:html>
<lams:head>
	<title><fmt:message key="label.learning.title" /></title>
	<%@ include file="/common/header.jsp"%>
	<lams:css suffix="jquery.jRating"/>
	<c:if test="${not empty codeStyles}">
		<link rel="stylesheet" type="text/css" href="${lams}css/codemirror.css" />
		<link rel="stylesheet" type="text/css" href="${lams}css/codemirror_simplescrollbars.css" />
		<style>
			pre {
				background-color: initial;
				border: none;
			}
		</style>
	</c:if>
	
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
		
	<c:if test="${not empty codeStyles}">
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
	
	<lams:JSImport src="learning/includes/javascript/gate-check.js" />
	<script type="text/javascript">
		 checkNextGateActivity('finishButton', '${toolSessionID}', '', function(){
			 document.location.href ='<c:url value="/learning/finish.do"/>?sessionMapID=${sessionMapID}&mode=${mode}&toolSessionID=${toolSessionID}';
		 });
		 
		function disableButtons() {
			$('.btn').prop('disabled',true);
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
		
		<c:if test="${isLeadershipEnabled and not empty sessionMap.groupLeader}">
			<lams:LeaderDisplay username="${sessionMap.groupLeader.firstName} ${sessionMap.groupLeader.lastName}" userId="${sessionMap.groupLeader.userId}"/>
		</c:if>
		
		<lams:errors/>
		<br>
		
		<c:if test="${not empty result}">
			<%@ include file="results/attemptsummary.jsp"%>
		</c:if>
		
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
									class="btn btn-sm btn-secondary float-start voffset10">
								<fmt:message key="label.edit" />
							</button>
						</c:if>
					</div>
		
				</div>
			</div>
		</c:if>

		<c:if test="${mode != 'teacher'}">
			<div class="activity-bottom-buttons">
				<c:if test="${isResubmitAllowed && hasEditRight}">
					<button type="submit" onclick="resubmit()" class="btn btn-default">
						<fmt:message key="label.learning.resubmit" />
					</button>
				</c:if>	
						
				<c:if test="${!sessionMap.isUserFailed}">
					<c:choose>
						<c:when	test="${sessionMap.reflectOn && (not sessionMap.userFinished) && hasEditRight}">
							<button type="button" name="FinishButton" onclick="return continueReflect()" 
									class="btn btn-primary na">
								<fmt:message key="label.continue" />
							</button>
						</c:when>
						
						<c:otherwise>
							<button id="finishButton"
							        name="FinishButton"
									class="btn btn-primary na">
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