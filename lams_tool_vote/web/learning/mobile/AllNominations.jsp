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
	<title><fmt:message key="activity.title" />
	</title>
		
	<link rel="stylesheet" href="${lams}css/jquery.mobile.css" />
	<link rel="stylesheet" href="${lams}css/defaultHTML_learner_mobile.css" />
	<script src="${lams}includes/javascript/jquery.js"></script>
	<script src="${lams}includes/javascript/jquery.mobile.js"></script>		
	<script type="text/javascript" src="${lams}includes/javascript/common.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/raphael/raphael.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/raphael/g.raphael.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/raphael/g.pie.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/raphael/g.bar.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/raphael/chart.js"></script>
	
	<script type="text/javascript">
		var chartDataUrl = '<lams:WebAppURL />chartGenerator.do';
		
		function submitMethod(actionMethod) 
		{
			if (actionMethod == 'learnerFinished') {
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
			<c:out value="${voteGeneralLearnerFlowDTO.activityTitle}" escapeXml="false" />
		</h1>
	</div><!-- /header -->

	<div data-role="content">
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

			<h2>
				<fmt:message key="label.progressiveResults" />
			</h2>

			<!--present  a mini summary table here -->
			<ul data-role="listview" data-inset="true">
			<table cellspacing="0" class="alternative-color table-content-padding-left">
				<tr class="ui-bar-c">
					<th>
						<fmt:message key="label.nomination" />
					</th>
					<th>
						<fmt:message key="label.total.votes" />
					</th>
				</tr>

				<c:forEach var="currentNomination" items="${voteGeneralLearnerFlowDTO.mapStandardNominationsHTMLedContent}">
					<c:set var="currentNominationKey" scope="request" value="${currentNomination.key}" />
					<tr class="ui-btn-up-c">
						<td class="space">
							<c:out value="${currentNomination.value}" escapeXml="false" />
						</td>

						<td>
							<c:forEach var="currentUserCount" items="${voteGeneralLearnerFlowDTO.mapStandardUserCount}">
								<c:set var="currentUserKey" scope="request" value="${currentUserCount.key}" />
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
			</ul>

			<div>
				<div class="float-right">
					<img src='<c:out value="${tool}"/>images/piechart.gif' width="30"
						title="<fmt:message key='label.tip.displayPieChart'/>"
						style="cursor: pointer;" height="30" border="0"
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


				<c:if
					test="${voteGeneralLearnerFlowDTO.notebookEntry != null && voteGeneralLearnerFlowDTO.notebookEntry != ''}">
	
				<h2>
					<fmt:message key="label.notebook.entries" />
				</h2>
	
				<p>
					<c:out value="${voteGeneralLearnerFlowDTO.notebookEntry}"
						escapeXml="false" />
				</p>
			</c:if>			
							
			<div id="chartDiv0" style="height: 220px; display: none;"></div>
				
			<div class="space-bottom-top button-inside">
				<c:if test="${voteGeneralLearnerFlowDTO.reportViewOnly != 'true' }">
					<button name="refreshVotes"
						onclick="submitMethod('viewAllResults');" data-icon="refresh">
						<fmt:message key="label.refresh" />
					</button>

					<c:if test="${VoteLearningForm.lockOnFinish != 'true' && hasEditRight}">
						<button name="redoQuestionsOk"
							onclick="submitMethod('redoQuestionsOk');" data-icon="back">
							<fmt:message key="label.retake" />
						</button>
					</c:if>
				</c:if>
			</div>

		</html:form>
	</div>

	<div data-role="footer" data-theme="b" class="ui-bar">
		<c:if test="${voteGeneralLearnerFlowDTO.reportViewOnly != 'true' }">
			<span class="ui-finishbtn-right">
				<c:if test="${voteGeneralLearnerFlowDTO.reflection != 'true' || !hasEditRight}">
					<a href="#" name="learnerFinished"
						onclick="javascript:submitMethod('learnerFinished');return false"
						style="float:right" id="finishButton" data-role="button" data-icon="arrow-r" data-theme="b">
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
				</c:if>
	
				<c:if test="${voteGeneralLearnerFlowDTO.reflection == 'true' && hasEditRight}">
					<button name="forwardtoReflection"
						onclick="javascript:submitMethod('forwardtoReflection');" data-icon="arrow-r" data-theme="b">
						<fmt:message key="label.continue" />
					</button>
				</c:if>	
			</span>	
		</c:if>
		<c:if test="${voteGeneralLearnerFlowDTO.reportViewOnly == 'true' }">
			<h2>&nbsp;</h2>
		</c:if>
	</div><!-- /footer -->

</div>
</body>
</lams:html>







