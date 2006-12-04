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

		<c:if
			test="${generalLearnerFlowDTO.requestLearningReportProgress != 'true'}">


			<html:form action="/learning?validate=false"
				enctype="multipart/form-data" method="POST" target="_self">
				<html:hidden property="method" />
				<html:hidden property="toolSessionID" />
				<html:hidden property="userID" />
				<html:hidden property="httpSessionID" />
				<html:hidden property="totalQuestionCount" />

				<c:forEach var="currentDto"
					items="${generalLearnerFlowDTO.listMonitoredAnswersContainerDTO}">
					<c:set var="currentQuestionId" scope="request"
						value="${currentDto.questionUid}" />

					<div class="shading-bg">
						<p>
							<strong> <fmt:message key="label.question" /> : </strong>
							<c:out value="${currentDto.question}" escapeXml="false" />
						</p>

						<c:forEach var="questionAttemptData"
							items="${currentDto.questionAttempts}">
							<c:forEach var="sData" items="${questionAttemptData.value}">
								<c:set var="userData" scope="request" value="${sData.value}" />
								<c:set var="responseUid" scope="request" value="${userData.uid}" />

								<c:if test="${generalLearnerFlowDTO.userUid == userData.queUsrId}">
		
										<c:if test="${currentQuestionId == userData.questionUid}">
											<p>
												<span class="field-name"> <c:out
														value="${userData.userName}" /> </span> -
												<lams:Date value="${userData.attemptTime}" />
											</p>
											<p>
												<c:out value="${userData.responsePresentable}"
													escapeXml="false" />
		
											</p>
										</c:if>
								</c:if>								
							</c:forEach>
						</c:forEach>
					</div>
				</c:forEach>
				
				
				<c:if test="${generalLearnerFlowDTO.existMultipleUserResponses == 'true'}">				
					<h2>
						<fmt:message key="label.other.answers" />
					</h2>
					
	
					<c:forEach var="currentDto"
								items="${generalLearnerFlowDTO.listMonitoredAnswersContainerDTO}">
								<c:set var="currentQuestionId" scope="request"
									value="${currentDto.questionUid}" />
			
								<div class="shading-bg">
			
									<c:forEach var="questionAttemptData"
										items="${currentDto.questionAttempts}">
										<c:forEach var="sData" items="${questionAttemptData.value}">
											<c:set var="userData" scope="request" value="${sData.value}" />
											<c:set var="responseUid" scope="request" value="${userData.uid}" />
		
										<c:if test="${generalLearnerFlowDTO.userUid != userData.queUsrId}">	
											<c:if test="${currentQuestionId == userData.questionUid}">
												<p>
													<span class="field-name"> <c:out
															value="${userData.userName}" /> </span>
															
													<c:if test="${generalLearnerFlowDTO.userNameVisible == 'true'}">																			
																 -
														<lams:Date value="${userData.attemptTime}" />
													</c:if>																						
												</p>
												<p>
													<c:out value="${userData.responsePresentable}"
														escapeXml="false" />
													<hr size=1> 												
												</p>
											</c:if>
										</c:if>									
										</c:forEach>
									</c:forEach>
								</div>
					</c:forEach>
				</c:if>				

						

				<c:if
					test="${generalLearnerFlowDTO.requestLearningReportViewOnly != 'true' }">
					<c:if test="${generalLearnerFlowDTO.teacherViewOnly != 'true' }">

						<html:button property="refreshAnswers" styleClass="button"
							onclick="submitMethod('refreshAllResults');">
							<fmt:message key="label.refresh" />
						</html:button>

						<c:if test="${generalLearnerFlowDTO.lockWhenFinished != 'true'}">
							<html:button property="redoQuestions" styleClass="button"
								onclick="submitMethod('redoQuestions');">
								<fmt:message key="label.redo" />
							</html:button>
						</c:if>						


						<div class="space-bottom-top" align="right">
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
						</div>
					</c:if>
				</c:if>
			</html:form>
		</c:if>



		<c:if
			test="${generalLearnerFlowDTO.requestLearningReportProgress == 'true'}">

			<h2>
				<fmt:message key="label.learnerReport" />
			</h2>

			<html:form action="/learning?validate=false"
				enctype="multipart/form-data" method="POST" target="_self">
				<html:hidden property="method" />
				<html:hidden property="toolSessionID" />
				<html:hidden property="userID" />
				<html:hidden property="httpSessionID" />
				<html:hidden property="totalQuestionCount" />

				<c:forEach var="currentDto"
					items="${generalLearnerFlowDTO.listMonitoredAnswersContainerDTO}">
					<c:set var="currentQuestionId" scope="request"
						value="${currentDto.questionUid}" />

					<div class="shading-bg">
						<p>
							<strong> <fmt:message key="label.question" /> : </strong>
							<c:out value="${currentDto.question}" escapeXml="false" />
						</p>

						<c:forEach var="questionAttemptData"
							items="${currentDto.questionAttempts}">
							<c:forEach var="sData" items="${questionAttemptData.value}">
								<c:set var="userData" scope="request" value="${sData.value}" />
								<c:set var="responseUid" scope="request" value="${userData.uid}" />

								<c:if test="${generalLearnerFlowDTO.userUid == userData.queUsrId}">
		
										<c:if test="${currentQuestionId == userData.questionUid}">
											<p>
												<span class="field-name"> <c:out
														value="${userData.userName}" /> </span> -
												<lams:Date value="${userData.attemptTime}" />
											</p>
											<p>
												<c:out value="${userData.responsePresentable}"
													escapeXml="false" />
		
											</p>
										</c:if>
								</c:if>								
							</c:forEach>
						</c:forEach>
					</div>
				</c:forEach>
				
				
				<c:if test="${generalLearnerFlowDTO.existMultipleUserResponses == 'true'}">				
					<h2>
						<fmt:message key="label.other.answers" />
					</h2>
					
	
						<c:forEach var="currentDto"
								items="${generalLearnerFlowDTO.listMonitoredAnswersContainerDTO}">
								<c:set var="currentQuestionId" scope="request"
									value="${currentDto.questionUid}" />
			
								<div class="shading-bg">
			
									<c:forEach var="questionAttemptData"
										items="${currentDto.questionAttempts}">
										<c:forEach var="sData" items="${questionAttemptData.value}">
											<c:set var="userData" scope="request" value="${sData.value}" />
											<c:set var="responseUid" scope="request" value="${userData.uid}" />
		
										<c:if test="${generalLearnerFlowDTO.userUid != userData.queUsrId}">	
											<c:if test="${currentQuestionId == userData.questionUid}">
												<p>
													<span class="field-name"> <c:out
															value="${userData.userName}" /> </span>
															
													<c:if test="${generalLearnerFlowDTO.userNameVisible == 'true'}">																			
																 -
														<lams:Date value="${userData.attemptTime}" />
													</c:if>																						
												</p>
												<p>
													<c:out value="${userData.responsePresentable}"
														escapeXml="false" />
													<hr size=1> 												
												</p>
											</c:if>
										</c:if>									
										</c:forEach>
									</c:forEach>
								</div>
					</c:forEach>

				</c:if>				

					

				<c:if
					test="${generalLearnerFlowDTO.requestLearningReportViewOnly != 'true' }">
					<c:if test="${generalLearnerFlowDTO.teacherViewOnly != 'true' }">

						<html:button property="refreshAnswers" styleClass="button"
							onclick="submitMethod('refreshAllResults');">
							<fmt:message key="label.refresh" />
						</html:button>


						<c:if test="${generalLearnerFlowDTO.lockWhenFinished != 'true'}">
							<html:button property="redoQuestions" styleClass="button"
								onclick="submitMethod('redoQuestions');">
								<fmt:message key="label.redo" />
							</html:button>
						</c:if>						


						<div class="space-bottom-top" align="right">
							<c:if test="${generalLearnerFlowDTO.reflection != 'true'}">
								<html:button property="endLearning"
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
						</div>
					</c:if>
				</c:if>
			</html:form>
		</c:if>
	</div>
	<div id="footer"></div>
</body>
</lams:html>











