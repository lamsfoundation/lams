<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>

<c:set var="lams">
	<lams:LAMSURL />
</c:set>
<c:set var="tool">
	<lams:WebAppURL />
</c:set>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="isUserLeader" value="${sessionMap.isUserLeader}" />
<c:set var="mode" value="${sessionMap.mode}" />
<c:set var="isLeadershipEnabled" value="${sessionMap.content.useSelectLeaderToolOuput}" />
<c:set var="hasEditRight" value="${!isLeadershipEnabled || isLeadershipEnabled && isUserLeader}" />

<lams:html>
<lams:head>
	<html:base />
	<lams:css />
	<title><fmt:message key="activity.title" /></title>
	<script type="text/javascript">
		function disableFinishButton() {
			var elem = document.getElementById("finishButton");
			if (elem != null) {
				elem.disabled = true;
			}
		}
		
		function submitForm(methodName) {
			var f = document.getElementById('Form1');
			f.submit();
		}
	</script>
</lams:head>

<body class="stripes">

	<div id="content">
		<h1>
			<c:out value="${mcGeneralLearnerFlowDTO.activityTitle}"	escapeXml="true" />
		</h1>
		
		<c:if test="${isLeadershipEnabled}">
			<h4>
				<fmt:message key="label.group.leader" >
					<fmt:param>${sessionMap.groupLeader.fullname}</fmt:param>
				</fmt:message>
			</h4>
		</c:if>
		
		<c:if test="${hasEditRight && mcGeneralLearnerFlowDTO.retries == 'true'
					&& mcGeneralLearnerFlowDTO.userOverPassMark != 'true'
					&& mcGeneralLearnerFlowDTO.passMarkApplicable == 'true'}">
			<p>
				<fmt:message key="label.notEnoughMarks">
					<fmt:param>
						<c:if test="${mcGeneralLearnerFlowDTO.passMark != mcGeneralLearnerFlowDTO.totalMarksPossible}">
							<fmt:message key="label.atleast" />
						</c:if>
								
						<c:out value="${mcGeneralLearnerFlowDTO.passMark}" />
						<fmt:message key="label.outof" />
						<c:out value="${mcGeneralLearnerFlowDTO.totalMarksPossible}" />
					</fmt:param>
				</fmt:message>
			</p>
			<p>
				<strong><fmt:message key="label.mark" /> </strong>
				<c:out value="${mcGeneralLearnerFlowDTO.learnerMark}" />
				<fmt:message key="label.outof" />
				<c:out value="${mcGeneralLearnerFlowDTO.totalMarksPossible}" />
			</p>
		</c:if>	

		<html:form action="/learning?method=displayMc&validate=false" method="POST" target="_self" onsubmit="disableFinishButton();" styleId="Form1">
			<html:hidden property="toolContentID" />
			<html:hidden property="toolSessionID" />
			<html:hidden property="httpSessionID" />
			<html:hidden property="userID" />
			<html:hidden property="userOverPassMark" />
			<html:hidden property="passMarkApplicable" />

			<h2>
				<fmt:message key="label.viewAnswers" />
			</h2>

			<!--  QUESTIONS  -->
			<c:set var="mainQueIndex" scope="request" value="0" />
			<c:forEach var="questionEntry" varStatus="status" items="${mcGeneralLearnerFlowDTO.mapQuestionsContent}">
				<c:set var="mainQueIndex" scope="request" value="${mainQueIndex +1}" />

				<div class="shading-bg">
					<div style="overflow: auto;">
						<span class="float-left space-right">
							${status.count})
						</span>					
						<c:out value="${questionEntry.value}" escapeXml="false" />
					</div>

					<!--  CANDIDATE ANSWERS  -->
					<c:set var="queIndex" scope="request" value="0" />
					<c:forEach var="mainEntry" items="${mcGeneralLearnerFlowDTO.mapGeneralOptionsContent}">
						<c:set var="queIndex" scope="request" value="${queIndex +1}" />

						<c:if test="${requestScope.mainQueIndex == requestScope.queIndex}">
							<ul>
								<c:forEach var="subEntry" items="${mainEntry.value}">
									<li>
										<c:out value="${subEntry.value}" escapeXml="false" /> 
									</li>
								</c:forEach>
							</ul>
						
							<p>
								<c:forEach var="attemptEntry" items="${mcGeneralLearnerFlowDTO.attemptMap}">
									<c:if test="${requestScope.mainQueIndex == attemptEntry.key}">
										<c:out value="${attemptEntry.value.mcOptionsContent.mcQueOptionText}" escapeXml="false"/>
									</c:if>
								</c:forEach>
	
								<c:if test="${mcGeneralLearnerFlowDTO.displayAnswers == 'true'}">
									<c:forEach var="attemptEntry" items="${mcGeneralLearnerFlowDTO.attemptMap}">
										<c:if test="${requestScope.mainQueIndex == attemptEntry.key}">
										
											<c:choose>
												<c:when test="${attemptEntry.value.mcOptionsContent.correctOption}">
													<img src="<c:out value="${tool}"/>images/tick.gif" border="0" class="middle">
												</c:when>
												<c:otherwise>
													<img src="<c:out value="${tool}"/>images/cross.gif" border="0" class="middle">
												</c:otherwise>
											</c:choose>
											
										</c:if>
									</c:forEach>
								</c:if>
							</p>
						</c:if>
					</c:forEach>

					<c:if test="${mcGeneralLearnerFlowDTO.displayAnswers == 'true'}">
						<c:forEach var="feedbackEntry" items="${mcGeneralLearnerFlowDTO.mapFeedbackContent}">
							<c:if test="${(requestScope.mainQueIndex == feedbackEntry.key)
								&& (feedbackEntry.value != null) && (feedbackEntry.value != '')}">
								
								<div style="overflow: auto;">									  							
									<strong> <fmt:message key="label.feedback.simple" /> </strong> <c:out value="${feedbackEntry.value}" escapeXml="true" />
								</div>	
																									
							</c:if>
						</c:forEach>
					</c:if>
				</div>
			</c:forEach>
			<!-- END QUESTION  -->
			
			<c:if test="${(mcGeneralLearnerFlowDTO.retries == 'true') && hasEditRight || (mcGeneralLearnerFlowDTO.displayAnswers == 'true')}">
				<p>
					<strong><fmt:message key="label.mark" /> </strong>
					<c:out value="${mcGeneralLearnerFlowDTO.learnerMark}" />
					<fmt:message key="label.outof" />
					<c:out value="${mcGeneralLearnerFlowDTO.totalMarksPossible}" />
				</p>
			</c:if>	
			
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

			<c:if test="${(mcGeneralLearnerFlowDTO.retries == 'true') && hasEditRight}">			
				<html:submit property="redoQuestions" styleClass="button">
					<fmt:message key="label.redo.questions" />
				</html:submit>
			</c:if>
									
			<c:if test="${mcGeneralLearnerFlowDTO.reflection && (notebookEntry != null) && hasEditRight}">
				<h2>
					<lams:out value="${mcGeneralLearnerFlowDTO.reflectionSubject}" escapeHtml="true" />		
				</h2>

				<p>
					<c:choose>
						<c:when test="${not empty mcGeneralLearnerFlowDTO.notebookEntry}">
							<lams:out value="${mcGeneralLearnerFlowDTO.notebookEntry}" escapeHtml="true"/>
						</c:when>
						<c:otherwise>
							<em><fmt:message key="message.no.reflection.available" /></em>  
						</c:otherwise>
					</c:choose>
				</p>
			</c:if>

			<c:if test="${(mcGeneralLearnerFlowDTO.retries != 'true') 
				|| (mcGeneralLearnerFlowDTO.retries == 'true') && (mcGeneralLearnerFlowDTO.passMarkApplicable == 'true') && (mcGeneralLearnerFlowDTO.userOverPassMark == 'true')}">

				<div class="space-bottom-top align-right">
					<c:if test="${(mcGeneralLearnerFlowDTO.reflection != 'true') || !hasEditRight}">
						<html:hidden property="learnerFinished" value="Finished" />
									
						<html:link href="#nogo" styleClass="button" styleId="finishButton" onclick="submitForm('finish'); return false;">
							<span class="nextActivity">
								<c:choose>
				 					<c:when test="${activityPosition.last}">
				 						<fmt:message key="label.submit" />
				 					</c:when>
				 					<c:otherwise>
				 		 				<fmt:message key="label.finished" />
				 					</c:otherwise>
					 			</c:choose>
							</span>
						</html:link>
					</c:if>

					<c:if test="${(mcGeneralLearnerFlowDTO.reflection == 'true') && hasEditRight}">
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
