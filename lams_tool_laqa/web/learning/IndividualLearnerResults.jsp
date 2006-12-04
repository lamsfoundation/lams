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
  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA

  http://www.gnu.org/licenses/gpl.txt
--%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>

<c:set var="lams">
	<lams:LAMSURL />
</c:set>
<c:set var="tool">
	<lams:WebAppURL />
</c:set>

<lams:html>
<head>
	<html:base />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8">
	<lams:css />
	<title><fmt:message key="activity.title" /></title>

	<script language="JavaScript" type="text/JavaScript">
		function submitLearningMethod(actionMethod) 
		{
			if (actionMethod == 'endLearning') {
				document.getElementById("finishButton").disabled = true;
			}
			document.QaLearningForm.method.value=actionMethod; 
			document.QaLearningForm.submit();
		}
		
		function submitMethod(actionMethod) 
		{
			submitLearningMethod(actionMethod);
		}
	</script>
</head>

<body class="stripes">




	<div id="content">
		<h1>
			<c:out value="${generalLearnerFlowDTO.activityTitle}"
				escapeXml="false" />
		</h1>
		<html:form action="/learning?validate=false"
			enctype="multipart/form-data" method="POST" target="_self">
			<html:hidden property="method" />
			<html:hidden property="toolSessionID" />
			<html:hidden property="userID" />
			<html:hidden property="httpSessionID" />
			<html:hidden property="totalQuestionCount" />


			<c:forEach var="questionEntry"
				items="${generalLearnerFlowDTO.mapQuestionContentLearner}">

				<div class="shading-bg">
					<p>
						<strong><fmt:message key="label.question" /> <c:out
								value="${questionEntry.key}" />:</strong>
						<br>
						<c:out value="${questionEntry.value}" escapeXml="false" />
					</p>

					<c:forEach var="answerEntry"
						items="${generalLearnerFlowDTO.mapAnswersPresentable}">
						<c:if test="${answerEntry.key == questionEntry.key}">



							<p>
								<strong> <fmt:message key="label.learning.yourAnswer" />
								</strong>

								<br>
								<c:out value="${answerEntry.value}" escapeXml="false" />

							</p>


						</c:if>
					</c:forEach>


					<c:forEach var="feedbackEntry"
						items="${generalLearnerFlowDTO.mapFeedback}">
						<c:if
							test="${(feedbackEntry.value != '') && (feedbackEntry.value != null) }">
							<c:if test="${feedbackEntry.key == questionEntry.key}">

								<span class="field-name"> <fmt:message
										key="label.feedback" />: </span>

								<c:out value="${feedbackEntry.value}" escapeXml="false" />

							</c:if>
						</c:if>
					</c:forEach>

				</div>

			</c:forEach>

			<div class="last-item">

			</div>

			<c:if test="${(generalLearnerFlowDTO.lockWhenFinished == 'true')  && (generalLearnerFlowDTO.showOtherAnswers == 'true') }">
				<p>
					<fmt:message key="label.responses.locked" />
				</p>
			</c:if>


			<div class="space-bottom-top small-space-top">
				<html:button property="redoQuestions" styleClass="button"
					onclick="submitMethod('redoQuestions');">
					<fmt:message key="label.redo" />
				</html:button>



			<c:if test="${generalLearnerFlowDTO.showOtherAnswers == 'true'}">
				<html:button property="viewAllResults"
					onclick="submitMethod('viewAllResults');" styleClass="button">
					<fmt:message key="label.allResponses" />
				</html:button>
			</c:if>				

			<c:if test="${generalLearnerFlowDTO.showOtherAnswers != 'true'}">
				<c:if test="${generalLearnerFlowDTO.reflection != 'true'}">
					<html:button property="endLearning" styleId="finishButton"
						onclick="javascript:submitMethod('endLearning');"
						styleClass="button">
						<fmt:message key="button.endLearning" />
					</html:button>
				</c:if>

				<c:if test="${generalLearnerFlowDTO.reflection == 'true'}">
					<html:button property="forwardtoReflection"
						onclick="javascript:submitMethod('forwardtoReflection');"
						styleClass="button">
						<fmt:message key="label.continue" />
					</html:button>
				</c:if>
			</c:if>				
			
			
			</div>

		</html:form>
	</div>

	<div id="footer"></div>

</body>
</lams:html>
















