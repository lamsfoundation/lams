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
<head>
	<html:base />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8">
	<lams:css />
	<script type="text/javascript"
		src="${lams}includes/javascript/common.js"></script>
	<title><fmt:message key="activity.title" />
	</title>

	<script language="JavaScript" type="text/JavaScript">
		function submitMethod(actionMethod) 
		{
			if (actionMethod == 'learnerFinished') {
				document.getElementById("finishButton").disabled = true;
			}
			document.VoteLearningForm.dispatch.value=actionMethod; 
			document.VoteLearningForm.submit();
		}
	</script>
</head>

<body class="stripes">

	<div id="content">

		<h1>
			<c:out value="${voteGeneralLearnerFlowDTO.activityTitle}"
				escapeXml="false" />
		</h1>

		<html:form action="/learning?validate=false"
			enctype="multipart/form-data" method="POST" target="_self">
			<html:hidden property="dispatch" />
			<html:hidden property="toolSessionID" />
			<html:hidden property="userID" />
			<html:hidden property="revisitingUser" />
			<html:hidden property="previewOnly" />
			<html:hidden property="maxNominationCount" />
			<html:hidden property="allowTextEntry" />
			<html:hidden property="lockOnFinish" />
			<html:hidden property="reportViewOnly" />
			<html:hidden property="userEntry" />


			<h2>
				<fmt:message key="label.progressiveResults" />
			</h2>

			<!--present  a mini summary table here -->

			<table cellspacing="0" class="alternative-color">
				<tr>
					<th>
						<fmt:message key="label.nomination" />
					</th>
					<th>
						<fmt:message key="label.total.votes" />
					</th>
				</tr>

				<c:forEach var="currentNomination"
					items="${voteGeneralLearnerFlowDTO.mapStandardNominationsHTMLedContent}">
					<c:set var="currentNominationKey" scope="request"
						value="${currentNomination.key}" />
					<tr>
						<td class="space">
							<c:out value="${currentNomination.value}" escapeXml="false" />
						</td>

						<td>

							<c:forEach var="currentUserCount"
								items="${voteGeneralLearnerFlowDTO.mapStandardUserCount}">
								<c:set var="currentUserKey" scope="request"
									value="${currentUserCount.key}" />
								<c:if test="${currentNominationKey == currentUserKey}">

									<c:if test="${currentUserCount.value != '0' }">
										<c:forEach var="currentQuestionUid"
											items="${voteGeneralLearnerFlowDTO.mapStandardQuestionUid}">
											<c:set var="currentQuestionUidKey" scope="request"
												value="${currentQuestionUid.key}" />
											<c:if test="${currentQuestionUidKey == currentUserKey}">

												<c:forEach var="currentSessionUid"
													items="${voteGeneralLearnerFlowDTO.mapStandardToolSessionUid}">
													<c:set var="currentSessionUidKey" scope="request"
														value="${currentSessionUid.key}" />
													<c:if
														test="${currentSessionUidKey == currentQuestionUidKey}">

														<c:if test="${currentNomination.value != 'Open Vote'}">
															<c:set scope="request" var="viewURL">
																<lams:WebAppURL />monitoring.do?method=getVoteNomination&questionUid=${currentQuestionUid.value}&sessionUid=${currentSessionUid.value}
												</c:set>

															<c:out value="${currentUserCount.value}" />

														</c:if>
														<c:if test="${currentNomination.value == 'Open Vote'}">
															<c:out value="${currentUserCount.value}" />
														</c:if>
													</c:if>
												</c:forEach>
											</c:if>
										</c:forEach>
									</c:if>
									<c:if test="${currentUserCount.value == '0' }">
										<c:out value="${currentUserCount.value}" />
									</c:if>
								</c:if>
							</c:forEach>

							<c:forEach var="currentRate"
								items="${voteGeneralLearnerFlowDTO.mapStandardRatesContent}">
								<c:set var="currentRateKey" scope="request"
									value="${currentRate.key}" />
								<c:if test="${currentNominationKey == currentRateKey}"> 				
																	 &nbsp(<c:out value="${currentRate.value}" />
									<fmt:message key="label.percent" />) 
								</c:if>
							</c:forEach>
						</td>
					</tr>
				</c:forEach>

			</table>

			<div>
				<div class="float-right">

					<c:set scope="request" var="viewURL">
						<html:rewrite page="/chartGenerator?type=pie" />
					</c:set>

					<img src="<c:out value="${tool}"/>images/piechart.gif" width=30
						title="<fmt:message key='label.tip.displayPieChart'/>"
						onclick="javascript:launchInstructionsPopup('<c:out value='${viewURL}' escapeXml='false'/>')"
						height="30" border="0" style="cursor: pointer;">

					<c:set scope="request" var="viewURL">
						<html:rewrite page="/chartGenerator?type=bar" />
					</c:set>

					<img src="<c:out value="${tool}"/>images/columnchart.gif" width=30
						title="<fmt:message key='label.tip.displayBarChart'/>"
						onclick="javascript:launchInstructionsPopup('<c:out value='${viewURL}' escapeXml='false'/>')"
						height=30 border="0" style="cursor: pointer;">
				</div>
				<strong> <fmt:message key="label.learner.nominations" /> </strong>

				<c:forEach var="entry"
					items="${requestScope.listGeneralCheckedOptionsContent}">
					<div>
						<c:out value="${entry}" escapeXml="false" />
					</div>
				</c:forEach>
				<div>
					<c:out value="${VoteLearningForm.userEntry}" />
				</div>

				
					<h2>
							<c:out value="${voteGeneralLearnerFlowDTO.reflectionSubject}" escapeXml="false" />												
					</h2>

					<c:out value="${voteGeneralLearnerFlowDTO.notebookEntry}"
						escapeXml="false" />
				
					<c:if test="${voteGeneralLearnerFlowDTO.lockOnFinish == 'true' }">					
					<br>
							<html:button property="forwardtoReflection" styleClass="button"
								onclick="submitMethod('forwardtoReflection');"> 
								<fmt:message key="label.edit" />
							</html:button>
					</c:if>													
				
				
				

				<div class="space-bottom-top">

					<html:submit property="redoQuestionsOk" styleClass="button"
						onclick="submitMethod('redoQuestionsOk');">
						<fmt:message key="label.retake" />
					</html:submit>

					<html:submit property="learnerFinished" styleId="finishButton"
						onclick="javascript:submitMethod('learnerFinished');"
						styleClass="button">
						<fmt:message key="label.finished" />
					</html:submit>
				</div>
		</html:form>

	</div>

	<div id="footer"></div>
</body>
</lams:html>
