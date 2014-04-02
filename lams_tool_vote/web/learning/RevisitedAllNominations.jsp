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

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

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
	
	<script type="text/javascript"
		src="${lams}includes/javascript/common.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/raphael/raphael.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/raphael/g.raphael.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/raphael/g.pie.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/raphael/g.bar.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/raphael/chart.js"></script>
	
	<script type="text/javascript">
		var chartDataUrl = '<lams:WebAppURL />chartGenerator.do';
		
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

<body class="stripes">

	<div id="content">

		<h1>
			<c:out value="${voteGeneralLearnerFlowDTO.activityTitle}" escapeXml="true" />
		</h1>

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
				<jsp:include page="/learning/RevisitedDisplay.jsp" />
			</c:if>
							
			<c:if test="${VoteLearningForm.showResults != 'true'}">
				<jsp:include page="/learning/RevisitedNoDisplay.jsp" />
			</c:if>

				<h4>
						<lams:out value="${voteGeneralLearnerFlowDTO.reflectionSubject}" escapeHtml="true" />												
				</h4>

				<lams:out value="${voteGeneralLearnerFlowDTO.notebookEntry}"
					escapeHtml="true" />
			
				<c:if test="${voteGeneralLearnerFlowDTO.lockOnFinish == 'true' && hasEditRight}">					
				<br>
						<html:button property="forwardtoReflection" styleClass="button"
							onclick="submitMethod('forwardtoReflection');"> 
							<fmt:message key="label.edit" />
						</html:button>
				</c:if>													

			<div class="space-bottom-top">

				<c:if test="${hasEditRight}">
					<html:submit property="redoQuestionsOk" styleClass="button"
						onclick="submitMethod('redoQuestionsOk');">
						<fmt:message key="label.retake" />
					</html:submit>
				</c:if>

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
			</div>
		</html:form>

	</div>

	<div id="footer"></div>
</body>
</lams:html>
