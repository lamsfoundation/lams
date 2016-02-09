<!DOCTYPE html>

<%@ include file="/common/taglibs.jsp"%>

<c:set scope="request" var="lams">
	<lams:LAMSURL />
</c:set>
<c:set scope="request" var="tool">
	<lams:WebAppURL />
</c:set>

<lams:html>
<lams:head>
	<html:base />
	<lams:css />
	<title><fmt:message key="activity.title" /></title>

	<script language="JavaScript" type="text/JavaScript">
		function submitMethod(actionMethod) {
			if (actionMethod == 'learnerFinished') {
				document.getElementById("finishButton").disabled = true;
			}
			document.VoteLearningForm.action += "&dispatch=" + actionMethod; 
			document.VoteLearningForm.submit();
		}
	</script>
</lams:head>

<body class="stripes">
	<div id="content">
		<h1>
			<c:out value="${voteGeneralLearnerFlowDTO.activityTitle}" escapeXml="true" />
		</h1>
		
		<c:if test="${VoteLearningForm.lockOnFinish and voteGeneralLearnerFlowDTO.learningMode != 'teacher'}">
			<div class="info space-bottom">
				<fmt:message key="message.warnLockOnFinish" />
			</div>
		</c:if>	
			
		<html:form action="/learning?validate=false" enctype="multipart/form-data" method="POST" target="_self">
			<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
			<c:set var="isUserLeader" value="${formBean.userLeader}" />
			<c:set var="isLeadershipEnabled" value="${formBean.useSelectLeaderToolOuput}" />
			<c:set var="hasEditRight" value="${!isLeadershipEnabled || isLeadershipEnabled && isUserLeader}" />
			
			<html:hidden property="toolSessionID" />
			<html:hidden property="userID" />
			<html:hidden property="revisitingUser" />
			<html:hidden property="previewOnly" />
			<html:hidden property="maxNominationCount" />
			<html:hidden property="minNominationCount" />
			<html:hidden property="allowTextEntry" />
			<html:hidden property="lockOnFinish" />
			<html:hidden property="reportViewOnly" />
			<html:hidden property="userEntry" />
			<html:hidden property="showResults" />
			<html:hidden property="userLeader" />
			<html:hidden property="groupLeaderName" />
			<html:hidden property="useSelectLeaderToolOuput" />
			
			<c:if test="${isLeadershipEnabled}">
				<h4>
					<fmt:message key="label.group.leader" >
						<fmt:param>${formBean.groupLeaderName}</fmt:param>
					</fmt:message>
				</h4>
			</c:if>
			
			<p>
				<strong> <fmt:message key="label.learning.reportMessage" /></strong>
			</p>

			<ul>
				<c:forEach var="entry" items="${requestScope.mapGeneralCheckedOptionsContent}">
					<li>
						<c:out value="${entry.value}" escapeXml="false" />
					</li>
				</c:forEach>

				<c:if test="${not empty VoteLearningForm.userEntry}">
					<li>
						<c:out value="${VoteLearningForm.userEntry}" escapeXml="true"/> 
					</li>
				</c:if>
			</ul>

			<div class="space-bottom-top">
				<c:if test="${hasEditRight}">
					<html:submit property="redoQuestions" styleClass="button" onclick="submitMethod('redoQuestions');">
						<fmt:message key="label.retake" />
					</html:submit>
				</c:if>

				<c:choose>
				<c:when test="${VoteLearningForm.showResults=='true'}">
					<html:submit property="viewAllResults" styleClass="button" onclick="submitMethod('viewAllResults');">
						<fmt:message key="label.overAllResults" />
					</html:submit>
				</c:when>
				
				<c:when test="${voteGeneralLearnerFlowDTO.reflection != 'true' || !hasEditRight}">
					<span class="right-buttons">
						<html:link href="#nogo" property="learnerFinished" styleId="finishButton"
							onclick="javascript:submitMethod('learnerFinished');return false"
							styleClass="button big-space-top">
							<span class="nextActivity">
								<c:choose>
									<c:when test="${activityPosition.last}">
										<fmt:message key="button.submitActivity" />
									</c:when>
									<c:otherwise>
										<fmt:message key="button.endLearning" />
									</c:otherwise>
								</c:choose>
							</span>
						</html:link>
					</span>
				</c:when>

				<c:otherwise>
					<html:submit property="forwardtoReflection"
						onclick="javascript:submitMethod('forwardtoReflection');"
						styleClass="button">
						<fmt:message key="label.continue" />
					</html:submit>
				</c:otherwise>
				</c:choose>
				
			</div>
		</html:form>

	</div>
	<div id="footer"></div>
</body>
</lams:html>
