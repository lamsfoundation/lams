<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>

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
			<c:out value="${mcGeneralLearnerFlowDTO.activityTitle}" escapeXml="true" />
		</h1>
	</div><!-- /header -->
	
	<html:form action="/learning?method=displayMc&validate=false"
			method="POST" target="_self" onsubmit="disableFinishButton();"  styleId="Form1">	
		<div data-role="content">
		
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
				<c:if test="${mcGeneralLearnerFlowDTO.retries == 'true'}">
					<c:if
						test="${mcGeneralLearnerFlowDTO.userOverPassMark != 'true' && 
									mcGeneralLearnerFlowDTO.passMarkApplicable == 'true' }">

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
				</c:if>
			</p>

			<h4>
				<fmt:message key="label.yourAnswers" />
			</h4>


			<ul data-role="listview" data-inset="true" data-theme="d" >
			<c:forEach var="dto" varStatus="status"	items="${requestScope.answerDtos}">

				<li>
					<div class="regular-li">
						<span class="float-left space-right">
							${dto.displayOrder})
						</span>
						<strong> <fmt:message key="label.question.col" /> </strong>
						
						<div style="overflow: auto;">
							<c:out value="${dto.question}" escapeXml="false" />
						</div>									

	                    <c:if test="${mcGeneralLearnerFlowDTO.displayAnswers == 'true'}">
							<strong> <fmt:message key="label.mark" /> </strong>
							<c:out value="${dto.mark}" />
						</c:if>

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
								<strong> <fmt:message key="label.feedback.simple" /> </strong> <c:out value="${dto.feedback}" escapeXml="true" /> 
							</div>		
						</c:if>												
					
					</div>
				</li>
			</c:forEach>
			</ul>

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
				
				<div class="small-space-top button-inside" >
					<button type="submit" name="redoQuestions" data-icon="back">
						<fmt:message key="label.redo.questions" />
					</button>
				</div>
			</c:if>
		
	</div>	

	<div data-role="footer" data-theme="b" class="ui-bar">
		<span class="ui-finishbtn-right">
			<c:if test="${mcGeneralLearnerFlowDTO.retries == 'true'}">
					<c:if
						test="${((mcGeneralLearnerFlowDTO.passMarkApplicable == 'true') && (mcGeneralLearnerFlowDTO.userOverPassMark == 'true'))}">
						<c:if test="${mcGeneralLearnerFlowDTO.reflection != 'true'}">
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

						<c:if test="${mcGeneralLearnerFlowDTO.reflection == 'true'}">
							<button name="forwardtoReflection" type="submit" data-icon="arrow-r" data-theme="b">
								<fmt:message key="label.continue" />
							</button>
						</c:if>
					</c:if>
			</c:if>

			<c:if test="${mcGeneralLearnerFlowDTO.retries != 'true'}">
					<c:if test="${mcGeneralLearnerFlowDTO.reflection != 'true'}">
						<html:hidden property="learnerFinished" value="Finished" />
						<button id="finishButton" onclick="submitForm('finish');return false" data-icon="arrow-r" data-theme="b">
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

					<c:if test="${mcGeneralLearnerFlowDTO.reflection == 'true'}">
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

