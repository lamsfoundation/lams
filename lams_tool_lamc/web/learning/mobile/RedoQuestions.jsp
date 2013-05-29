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
			<c:out value="${mcGeneralLearnerFlowDTO.activityTitle}" escapeXml="false" />
		</h1>
	</div>

		<html:form  action="/learning?method=displayMc&validate=false" method="POST" target="_self"
					onsubmit="disableFinishButton();" styleId="Form1">
			<html:hidden property="toolContentID"/>						
			<html:hidden property="toolSessionID"/>								
			<html:hidden property="httpSessionID"/>											
			<html:hidden property="userID"/>											
			<html:hidden property="userOverPassMark"/>						
			<html:hidden property="passMarkApplicable"/>										
			<html:hidden property="learnerProgress"/>										
			<html:hidden property="learnerProgressUserId"/>										
			<html:hidden property="questionListingMode"/>													

			<div data-role="content">		
				
			 		<c:if test="${mcGeneralLearnerFlowDTO.retries == 'true'}"> 		
						 
							  	 <h3><fmt:message key="label.individual.results.withRetries"/></h3>
						   
  					</c:if> 			

					<c:if test="${mcGeneralLearnerFlowDTO.retries == 'false'}"> 							  
					 
								  <h3><fmt:message key="label.individual.results.withoutRetries"/></h3>
						   
					</c:if> 			


					<p>
						  	 <fmt:message key="label.learner.redo"/> 
					  
					  </p>
					  
					  <p>
						  	 <fmt:message key="label.learner.bestMark"/>
						  	   <c:out value="${mcGeneralLearnerFlowDTO.latestAttemptMark}"/> 
						  	<fmt:message key="label.outof"/> 
						  	<c:out value="${mcGeneralLearnerFlowDTO.totalMarksPossible}"/> 
					  	</p>
					  
					  
				  <div class="small-space-top button-inside" >
				  			<button type="submit" name="viewAnswers" data-icon="forward">
								<fmt:message key="label.view.answers"/>
							</button>	 		

	   						<button type="submit" name="redoQuestionsOk" data-icon="back">
								<fmt:message key="label.redo.questions"/>
							</button>	 				 		  					
				 
					</div>
			</div>
				
			<div data-role="footer" data-theme="b" class="ui-bar">
				<span class="ui-finishbtn-right">  	 
		 			<c:if test="${mcGeneralLearnerFlowDTO.retries == 'true'}"> 					  	   

									<c:if test="${((McLearningForm.passMarkApplicable == 'true') && (McLearningForm.userOverPassMark == 'true'))}">
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
												<fmt:message key="label.continue"/>
											</button>	 				
									  	</c:if> 				    					
							  	   </c:if>	
					  	
					</c:if>
					
					<c:if test="${mcGeneralLearnerFlowDTO.retries != 'true'}"> 							  
 			  	   		  
										<c:if test="${mcGeneralLearnerFlowDTO.reflection != 'true'}"> 						  			  		
											<html:hidden property="learnerFinished" value="Finished" />
																  			  		
											<button id="finishButton" onclick="submitForm('finish');return false" data-icon="arrow-r" data-theme="b">
												<c:choose>
								 					<c:when test="${activityPosition.last}">
								 						<fmt:message key="label.submit" />
								 					</c:when>
								 					<c:otherwise>
								 		 				<fmt:message key="label.finished" />
								 					</c:otherwise>
									 			</c:choose>
											</button> 				
								  	</c:if> 				    					
					
									<c:if test="${mcGeneralLearnerFlowDTO.reflection == 'true'}"> 						  			  		
										<button name="forwardtoReflection" type="submit" data-icon="arrow-r" data-theme="b">
											<fmt:message key="label.continue"/>
										</button>	 				
								  	</c:if> 				    					
								   
					  	 
					</c:if> 																		
				</span>
			</div>					  	 
				
			
		 	

	
		</html:form>	
	
</div>
</body>
</lams:html>








	