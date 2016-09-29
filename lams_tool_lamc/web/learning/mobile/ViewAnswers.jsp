<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>

<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="isUserLeader" value="${sessionMap.isUserLeader}" />
<c:set var="mode" value="${sessionMap.mode}" />
<c:set var="isLeadershipEnabled" value="${sessionMap.content.useSelectLeaderToolOuput}" />
<c:set var="hasEditRight" value="${!isLeadershipEnabled || isLeadershipEnabled && isUserLeader}" />

<lams:html>
<lams:head>
	<title><fmt:message key="activity.title" /></title>
	<%@ include file="/common/mobileheader.jsp"%>
	
	<script type="text/javascript">
		function disableFinishButton() {
			var elem = document.getElementById("finishButton");
			if (elem != null) {
				elem.disabled = true;
			}
		}
	    function submitForm(methodName){
        	var f = document.getElementById('Form1');
	    	f.submit();
	    }
	</script>
</lams:head>

<body class="large-font">
<div data-role="page" data-cache="false">

	<div data-role="header" data-theme="b" data-nobackbtn="true">
		<h1>
			<c:out value="${mcGeneralLearnerFlowDTO.activityTitle}"	escapeXml="true" />
		</h1>
	</div>

	<html:form action="/learning?method=displayMc&validate=false"
		method="POST" target="_self" onsubmit="disableFinishButton();"  styleId="Form1">
		<div data-role="content">	
			<html:hidden property="toolContentID" />
			<html:hidden property="toolSessionID" />
			<html:hidden property="httpSessionID" />
			<html:hidden property="userID" />
			<html:hidden property="userOverPassMark" />
			<html:hidden property="passMarkApplicable" />

			<h2>
				<fmt:message key="label.learner.viewAnswers" />
			</h2>
			
			<c:if test="${isLeadershipEnabled}">
				<h4>
					<fmt:message key="label.group.leader" >
						<fmt:param>${sessionMap.groupLeader.fullname}</fmt:param>
					</fmt:message>
				</h4>
			</c:if>

			<!--  QUESTIONS  -->
			<ul data-role="listview" data-inset="true" data-theme="d" >
			<c:set var="mainQueIndex" scope="request" value="0" />
			<c:forEach var="questionEntry" varStatus="status"
				items="${mcGeneralLearnerFlowDTO.mapQuestionsContent}">
				<c:set var="mainQueIndex" scope="request" value="${mainQueIndex +1}" />

				<li>
					<div class="regular-li">
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
						<c:forEach var="feedbackEntry"
							items="${mcGeneralLearnerFlowDTO.mapFeedbackContent}">
							<c:if test="${requestScope.mainQueIndex == feedbackEntry.key}">
								<c:if test="${(feedbackEntry.value != null) && (feedbackEntry.value != '')}">
									<div style="overflow: auto;">									  							
										<strong> <fmt:message key="label.feedback.simple" /> </strong> <c:out value="${feedbackEntry.value}" escapeXml="true" />
									</div>								
								</c:if>																			
							</c:if>
						</c:forEach>
					</c:if>
				</div>
				</li>
			</c:forEach>
			</ul>
			<!-- END QUESTION  -->


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
				<div class="small-space-top button-inside" >
					<button type="submit" name="redoQuestions" data-icon="back">
						<fmt:message key="label.redo.questions" />
					</button>
				</div>
			</c:if>
									
			<c:if test="${mcGeneralLearnerFlowDTO.reflection && hasEditRight}">
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
		</div>

	<div data-role="footer" data-theme="b" class="ui-bar">
		<span class="ui-finishbtn-right">
			<c:if test="${(mcGeneralLearnerFlowDTO.retries != 'true') || (mcGeneralLearnerFlowDTO.retries == 'true') && (mcGeneralLearnerFlowDTO.passMarkApplicable == 'true') && (mcGeneralLearnerFlowDTO.userOverPassMark == 'true')}">

				<c:if test="${(mcGeneralLearnerFlowDTO.reflection != 'true') || !hasEditRight}">
					<html:hidden property="learnerFinished" value="Finished" />
									
					<button id="finishButton" name="finishButton" onclick="submitForm('finish');return false" data-icon="arrow-r" data-theme="b">
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
					</button>
				</c:if>

				<c:if test="${(mcGeneralLearnerFlowDTO.reflection == 'true') && hasEditRight}">
					<button name="forwardtoReflection" type="submit" data-icon="arrow-r" data-theme="b">
						<fmt:message key="label.continue" />
					</button>
				</c:if>
			</c:if>
		</span>
	</div>			
	</html:form>

	</div>
</body>
</lams:html>
