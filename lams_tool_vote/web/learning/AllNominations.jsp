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
	<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/common.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/raphael/raphael.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/raphael/g.raphael.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/raphael/g.pie.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/raphael/g.bar.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/raphael/chart.js"></script>
	
	<script type="text/javascript">
		var chartDataUrl = '<lams:WebAppURL />chartGenerator.do';
		
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
			<c:out value="${voteGeneralLearnerFlowDTO.activityTitle}" escapeXml="false" />
		</h1>

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

				<c:forEach var="currentNomination"	items="${voteGeneralLearnerFlowDTO.mapStandardNominationsHTMLedContent}">
					<c:set var="currentNominationKey" scope="request" value="${currentNomination.key}" />
					<tr>
						<td class="space">
							<c:out value="${currentNomination.value}" escapeXml="false" />
						</td>

						<td>
							<c:forEach var="currentUserCount" items="${voteGeneralLearnerFlowDTO.mapStandardUserCount}">
								<c:set var="currentUserKey" scope="request"	value="${currentUserCount.key}" />
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
					<img src='<c:out value="${tool}"/>images/piechart.gif' width="30"
						title="<fmt:message key='label.tip.displayPieChart'/>"
						style="cursor: pointer; height: 30px; border: none"
						onclick="javascript:drawChart('pie', 0)">

					<img src='<c:out value="${tool}"/>images/columnchart.gif' width="30"
						title="<fmt:message key='label.tip.displayBarChart'/>" 
						style="cursor: pointer;" height="30" border="0"
						onclick="javascript:drawChart('bar', 0)">
				</div>
				
				<c:if test="${VoteLearningForm.allowTextEntry}">
					<strong> <fmt:message key="label.open.votes"/> </strong>
					<c:forEach var="vote"
						items="${requestScope.listUserEntriesContent}">
						<c:if test="${vote.userEntry != null}">
							<div>
								<c:out value="${vote.userEntry}" escapeXml="false" /> 
							</div>
						</c:if>
					</c:forEach>
					<div>&nbsp;</div>
				</c:if>

				<c:choose>
				 <c:when test="${fn:length(requestScope.listGeneralCheckedOptionsContent) > 1}">
                                      <strong><fmt:message key="label.learner.nominations" />  </strong>
				 </c:when>
                                 <c:otherwise>

				     <strong><fmt:message key="label.learner.nomination" />  </strong>
				 </c:otherwise>
				</c:choose>


			</div>

			<c:forEach var="entry"
				items="${requestScope.listGeneralCheckedOptionsContent}">
				<div>
					<c:out value="${entry}" escapeXml="false" />
				</div>
			</c:forEach>

			<div>
				<c:out value="${VoteLearningForm.userEntry}" />
			</div>


			<c:if test="${voteGeneralLearnerFlowDTO.notebookEntry != null && voteGeneralLearnerFlowDTO.notebookEntry != ''}">
	
				<h2>
					<fmt:message key="label.notebook.entries" />
				</h2>
	
				<p>
					<c:out value="${voteGeneralLearnerFlowDTO.notebookEntry}" escapeXml="false" />
				</p>
			</c:if>							

			<div id="chartDiv0" style="height: 220px; display: none;"></div>
			

			<div class="space-bottom-top">
				<c:if test="${voteGeneralLearnerFlowDTO.reportViewOnly != 'true' }">
					<html:submit property="refreshVotes" styleClass="button"
						onclick="submitMethod('viewAllResults');">
						<fmt:message key="label.refresh" />
					</html:submit>

					<c:if test="${VoteLearningForm.lockOnFinish != 'true' && hasEditRight}">
						<html:submit property="redoQuestionsOk" styleClass="button"
							onclick="submitMethod('redoQuestionsOk');">
							<fmt:message key="label.retake" />
						</html:submit>
					</c:if>

					<c:if test="${voteGeneralLearnerFlowDTO.reflection != 'true' || !hasEditRight}">
						<html:link href="#" property="learnerFinished"
							onclick="javascript:submitMethod('learnerFinished');return false"
							styleClass="button big-space-top" style="float:right" styleId="finishButton">
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
					</c:if>

					<c:if test="${voteGeneralLearnerFlowDTO.reflection == 'true' && hasEditRight}">
						<html:submit property="forwardtoReflection"
							onclick="javascript:submitMethod('forwardtoReflection');"
							styleClass="button">
							<fmt:message key="label.continue" />
						</html:submit>
					</c:if>
				</c:if>
			</div>

		</html:form>
	</div>

	<div id="footer"></div>

</body>
</lams:html>