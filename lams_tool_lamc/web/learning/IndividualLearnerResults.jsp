<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>

<c:set var="lams">
	<lams:LAMSURL />
</c:set>
<c:set var="tool">
	<lams:WebAppURL />
</c:set>

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
		
	    function submitForm(methodName){
	        var f = document.getElementById('Form1');
	        f.submit();
	    }
	</script>
</lams:head>

<body class="stripes">

	<div id="content">

		<h1>
			<c:out value="${mcGeneralLearnerFlowDTO.activityTitle}" escapeXml="true" />
		</h1>

		<html:form action="/learning?method=displayMc&validate=false" method="POST" target="_self" onsubmit="disableFinishButton();"  styleId="Form1">
			<html:hidden property="toolContentID" />
			<html:hidden property="toolSessionID" />
			<html:hidden property="httpSessionID" />
			<html:hidden property="userID" />
			<html:hidden property="userOverPassMark" />
			<html:hidden property="passMarkApplicable" />
			<html:hidden property="learnerProgress" />
			<html:hidden property="learnerProgressUserId" />

			<c:choose>
				<c:when test="${mcGeneralLearnerFlowDTO.retries == 'true'}">
					<h3>
						<fmt:message key="label.individual.results.withRetries" />
					</h3>
				</c:when>
				
				<c:when test="${mcGeneralLearnerFlowDTO.retries != 'true'}">
					<h3>
						<fmt:message key="label.individual.results.withoutRetries" />
					</h3>
				</c:when>
			</c:choose>
			
			<c:if test="${(mcGeneralLearnerFlowDTO.retries == 'true') or (mcGeneralLearnerFlowDTO.displayAnswers == 'true')}">
				<p>
					<strong><fmt:message key="label.mark" /> </strong>
					<c:out value="${mcGeneralLearnerFlowDTO.learnerMark}" />
					<fmt:message key="label.outof" />
					<c:out value="${mcGeneralLearnerFlowDTO.totalMarksPossible}" />
				</p>
			</c:if>
			
			<p>
				<c:if test="${mcGeneralLearnerFlowDTO.retries == 'true'
					&& mcGeneralLearnerFlowDTO.userOverPassMark != 'true'
					&& mcGeneralLearnerFlowDTO.passMarkApplicable == 'true'}">

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
				</c:if>
			</p>

			<h4>
				<fmt:message key="label.yourAnswers" />
			</h4>

			<c:forEach var="dto" varStatus="status" items="${answerDtos}">

				<div class="shading-bg">

					<div style="overflow: auto;">
						<span class="float-left space-right">
							${dto.displayOrder})
						</span>

						<strong> <fmt:message key="label.question.col" /> </strong>
					
						<c:out value="${dto.question}" escapeXml="false" />
						
						<c:if test="${mcGeneralLearnerFlowDTO.displayAnswers == 'true'}">
							<strong> <fmt:message key="label.mark" /> </strong>
							<c:out value="${dto.mark}" /> 
						</c:if>
					</div>		

					<p>
						<c:out value="${dto.answerOption.mcQueOptionText}" escapeXml="false" />
								
                    	<c:if test="${mcGeneralLearnerFlowDTO.displayAnswers == 'true'}">
                    		<c:choose>
                    			<c:when test="${dto.attemptCorrect}">
                    				<img src="<c:out value="${tool}"/>images/tick.gif" border="0" class="middle">
                    			</c:when>
                    			<c:otherwise>
                    				<img src="<c:out value="${tool}"/>images/cross.gif" border="0" class="middle">
                    			</c:otherwise>
                    		</c:choose>
						</c:if>
					</p>

					<c:if test="${(dto.feedback != null) && (dto.feedback != '')}">
						<div style="overflow: auto;">
							<strong> <fmt:message key="label.feedback.simple" /> </strong> 
							<c:out value="${dto.feedback}" escapeXml="true" /> 
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
				<p>
					<fmt:message key="label.learner.bestMark"/>
					<c:out value="${mcGeneralLearnerFlowDTO.latestAttemptMark}"/> 
					<fmt:message key="label.outof"/> 
					<c:out value="${mcGeneralLearnerFlowDTO.totalMarksPossible}"/> 
				</p>
				
				<html:submit property="redoQuestions" styleClass="button">
					<fmt:message key="label.redo.questions" />
				</html:submit>
			</c:if>

			<c:if test="${(mcGeneralLearnerFlowDTO.retries != 'true')
				|| (mcGeneralLearnerFlowDTO.retries == 'true') && (mcGeneralLearnerFlowDTO.passMarkApplicable == 'true') 
				&& (mcGeneralLearnerFlowDTO.userOverPassMark == 'true')}">

				<div class="space-bottom-top align-right">
					<c:if test="${mcGeneralLearnerFlowDTO.reflection != 'true'}">
						<html:hidden property="learnerFinished" value="Finished" />
																  			  		
						<html:link href="#" styleClass="button" styleId="finishButton" onclick="submitForm('finish');return false">
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

