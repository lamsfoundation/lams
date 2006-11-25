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
</head>

<body class="stripes">

	<div id="content">

		<h1>
			<c:out value="${mcGeneralLearnerFlowDTO.activityTitle}" escapeXml="false" />
		</h1>

		<html:form action="/learning?method=displayMc&validate=false"
			method="POST" target="_self">
			<html:hidden property="toolContentID" />
			<html:hidden property="toolSessionID" />
			<html:hidden property="httpSessionID" />
			<html:hidden property="userID" />
			<html:hidden property="userOverPassMark" />
			<html:hidden property="passMarkApplicable" />
			<html:hidden property="learnerProgress" />
			<html:hidden property="learnerProgressUserId" />
			<html:hidden property="questionListingMode" />

			<c:if test="${mcGeneralLearnerFlowDTO.retries == 'true'}">

				<h3>
					<fmt:message key="label.individual.results.withRetries" />
				</h3>

			</c:if>

			<c:if test="${mcGeneralLearnerFlowDTO.retries != 'true'}">

				<h3>
					<fmt:message key="label.individual.results.withoutRetries" />
				</h3>

			</c:if>

			<p>
				<strong><fmt:message key="label.mark" /> </strong>

				<c:out value="${mcGeneralLearnerFlowDTO.totalUserMark}" />
				<fmt:message key="label.outof" />
				<c:out value="${mcGeneralLearnerFlowDTO.totalMarksPossible}" />

			</p>

			<p>
				<c:if test="${mcGeneralLearnerFlowDTO.retries == 'true'}">
					<c:if
						test="${mcGeneralLearnerFlowDTO.userOverPassMark != 'true' && 
									mcGeneralLearnerFlowDTO.passMarkApplicable == 'true' }">

						<fmt:message key="label.notEnoughMarks" />

					</c:if>
				</c:if>
			</p>

			<h4>
				<fmt:message key="label.yourAnswers" />
			</h4>



			<c:forEach var="dto" varStatus="status"
				items="${requestScope.listSelectedQuestionCandidateAnswersDto}">

				<div class="shading-bg">

						<strong> <fmt:message key="label.question.col" /> </strong>
						
						<div style="overflow: auto;">
							<c:out value="${dto.question}" escapeXml="false" />
						</div>									

						[
						<strong> <fmt:message key="label.mark" /> </strong>
						<c:out value="${dto.mark}" />
						]

					<p>
						<c:forEach var="caText" varStatus="status"
							items="${dto.candidateAnswers}">
							<c:out value="${caText.value}" escapeXml="false" />
						</c:forEach>

							<c:if test="${dto.attemptCorrect == 'true'}">
								<img src="<c:out value="${tool}"/>images/tick.gif" border="0" class="middle">
							</c:if>
							<c:if test="${dto.attemptCorrect != 'true'}">
								<img src="<c:out value="${tool}"/>images/cross.gif" border="0" class="middle">
							</c:if>

					</p>

						<c:if test="${(dto.feedback != null) && (dto.feedback != '')}">
							<div style="overflow: auto;">
								<strong> <fmt:message key="label.feedback.simple" /> </strong> <c:out value="${dto.feedback}" escapeXml="false" /> 
							</div>		
						</c:if>												

				</div>
			</c:forEach>

			<c:if test="${mcGeneralLearnerFlowDTO.showMarks == 'true'}">
				<h4>
					<fmt:message key="label.group.results" />
				</h4>
				
				<table class="alternative-color" cellspacing="0">
				  <tr>
				  	<td width="30%"> 
					  	  <strong> <fmt:message key="label.topMark"/> </strong>
					 </td> 
					 <td>	
						  	 <c:out value="${mcGeneralLearnerFlowDTO.topMark}"/>
				  	</td>
				  </tr>	
	
				  <tr>
				  	<td> 
					  	 <strong><fmt:message key="label.avMark"/> </strong>
				  	</td>
				  	<td>
						  	<c:out value="${mcGeneralLearnerFlowDTO.averageMark}"/>
				  	</td>
				  </tr>	
				</table>
			</c:if>				
			


			<c:if test="${mcGeneralLearnerFlowDTO.retries == 'true'}">

				<html:submit property="redoQuestions" styleClass="button">
					<fmt:message key="label.redo.questions" />
				</html:submit>

				<div class="space-bottom-top align-right">
					<c:if
						test="${((mcGeneralLearnerFlowDTO.passMarkApplicable == 'true') && (mcGeneralLearnerFlowDTO.userOverPassMark == 'true'))}">
						<c:if test="${mcGeneralLearnerFlowDTO.reflection != 'true'}">
							<html:submit property="learnerFinished" styleClass="button">
								<fmt:message key="label.finished" />
							</html:submit>
						</c:if>

						<c:if test="${mcGeneralLearnerFlowDTO.reflection == 'true'}">
							<html:submit property="forwardtoReflection" styleClass="button">
								<fmt:message key="label.continue" />
							</html:submit>
						</c:if>
					</c:if>
				</div>

			</c:if>

			<c:if test="${mcGeneralLearnerFlowDTO.retries != 'true'}">

				<div class="space-bottom-top align-right">
					<c:if test="${mcGeneralLearnerFlowDTO.reflection != 'true'}">
						<html:submit property="learnerFinished" styleClass="button">
							<fmt:message key="label.finished" />
						</html:submit>
					</c:if>

					<c:if test="${mcGeneralLearnerFlowDTO.reflection == 'true'}">
						<html:submit property="forwardtoReflection" styleClass="button">
							<fmt:message key="label.continue" />
						</html:submit>
					</c:if>
				</div>

			</c:if>

		</html:form>
	</div>
</body>
</lams:html>

