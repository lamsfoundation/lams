<%-- 
Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
License Information: http://lamsfoundation.org/licensing/lams/2.0/

  This program is free software; you can redistribute it and/or modify
  it under the terms of the GNU General Public License version 2 as 
  published by the Free Software Foundation.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
  USA

  http://www.gnu.org/licenses/gpl.txt
--%>

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
	<title><fmt:message key="activity.title" /></title>
	
	<link rel="stylesheet" href="${lams}css/jquery.mobile.css" />
	<link rel="stylesheet" href="${lams}css/defaultHTML_learner_mobile.css" />
	<link type="text/css" href="${lams}/css/chart.css" rel="stylesheet" />
	
	<script src="${lams}includes/javascript/jquery.js"></script>
	<script src="${lams}includes/javascript/jquery.mobile.js"></script>			
	<script type="text/javascript" src="${lams}includes/javascript/common.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/d3.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/chart.js"></script>
	
	<script type="text/javascript">
		function submitMethod(actionMethod) 
		{
			if (actionMethod == "learnerFinished") {
				document.getElementById("finishButton").disabled = true;
			}			
			document.VoteLearningForm.dispatch.value=actionMethod; 
			document.VoteLearningForm.submit();
		}
	</script>
</lams:head>

<body>
<div data-role="page" data-cache="false">

	<div data-role="header" data-theme="b" data-nobackbtn="true">
		<h1>
			<c:out value="${voteGeneralLearnerFlowDTO.activityTitle}" escapeXml="true" />
		</h1>
	</div><!-- /header -->
	
	<div data-role="content">
		
		<c:if test="${VoteLearningForm.lockOnFinish and voteGeneralLearnerFlowDTO.learningMode != 'teacher'}">
			<div class="info space-bottom">
				<fmt:message key="message.activityLocked" />
			</div>
		</c:if>

		<html:form action="/learning?validate=false" enctype="multipart/form-data" method="POST" target="_self">
			<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
			<c:set var="isUserLeader" value="${formBean.userLeader}" />
			<c:set var="isLeadershipEnabled" value="${formBean.useSelectLeaderToolOuput}" />
			<c:set var="hasEditRight" value="${!isLeadershipEnabled || isLeadershipEnabled && isUserLeader}" />
			
			<html:hidden property="dispatch" />
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

			<c:if test="${VoteLearningForm.showResults == 'true'}">
				<jsp:include page="RevisitedDisplay.jsp" />
			</c:if>
							
			<c:if test="${VoteLearningForm.showResults != 'true'}">
				<jsp:include page="RevisitedNoDisplay.jsp" />
			</c:if>
				<c:if test="${voteGeneralLearnerFlowDTO.reflection}">
					<h2>
							<lams:out value="${voteGeneralLearnerFlowDTO.reflectionSubject}" escapeHtml="true" />												
					</h2>
	
					<lams:out value="${voteGeneralLearnerFlowDTO.notebookEntry}"
						escapeHtml="true" />
				</c:if>

				<c:if test="${voteGeneralLearnerFlowDTO.learningMode != 'teacher' && hasEditRight}">
				<br>
					<c:if test="${voteGeneralLearnerFlowDTO.reflection}">
						<span class="button-inside">
							<html:button property="forwardtoReflection" styleClass="button"
								onclick="submitMethod('forwardtoReflection');"> 
								<fmt:message key="label.edit" />
							</html:button>
						</span>
					</c:if>
				</c:if>

		</html:form>

	</div>
	
	<div data-role="footer" data-theme="b" class="ui-bar">
		<c:if test="${voteGeneralLearnerFlowDTO.learningMode != 'teacher'}">
			<span class="ui-finishbtn-right">
				<a href="#" name="learnerFinished" id="finishButton"
					onclick="javascript:submitMethod('learnerFinished');return false" data-role="button" data-icon="arrow-r" data-theme="b">
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
				</a>
			</span>
		</c:if>
		<c:if test="${voteGeneralLearnerFlowDTO.learningMode == 'teacher'}">
			<h2>&nbsp;</h2>
		</c:if>	
	</div><!-- /footer -->

</div>
</body>
</lams:html>
