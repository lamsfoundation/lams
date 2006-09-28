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

<html:html>
<head>
	<html:base />
	<lams:headItems />
	<title><bean:message key="activity.title" /></title>
</head>

<body class="stripes">
	
	<h1>
		<c:out value="${mcGeneralLearnerFlowDTO.activityTitle}" escapeXml="false" />
	</h1>



<div id="content">

		<html:form  action="/learning?method=displayMc&validate=false" method="POST" target="_self">
			<html:hidden property="toolContentID"/>						
			<html:hidden property="toolSessionID"/>								
			<html:hidden property="httpSessionID"/>			
			<html:hidden property="userID"/>																			
			<html:hidden property="userOverPassMark"/>						
			<html:hidden property="passMarkApplicable"/>										
			<html:hidden property="learnerProgress"/>										
			<html:hidden property="learnerProgressUserId"/>										
			<html:hidden property="questionListingMode"/>													
		
				<table class="forms">
					  <tr>
						  	<th scope="col" valign=top colspan=2> 
						  	  <bean:message key="label.assessment"/> 
						  	</th>
					  </tr>

					  <tr>
					  	<td NOWRAP align=center valign=top colspan=2> 
						  	<c:out value="${mcGeneralLearnerFlowDTO.reportTitleLearner}" escapeXml="false" />
					  	</td>
					  </tr>

				
			 		<c:if test="${mcGeneralLearnerFlowDTO.retries == 'true'}"> 		
						  <tr>
						  	<td NOWRAP align=center valign=top colspan=2> 
							  	<b> <bean:message key="label.individual.results.withRetries"/> </b>
						  	</td>
						  </tr>
  					</c:if> 			

					<c:if test="${mcGeneralLearnerFlowDTO.retries != 'true'}"> 							  
						  <tr>
						  	<td NOWRAP align=center valign=top colspan=2> 
								  <b>  <bean:message key="label.individual.results.withoutRetries"/> </b>
						  	</td>
						  </tr>
					</c:if> 			


					  <tr>
					  	<td NOWRAP align=right valign=top colspan=2> 
						  	<b> <bean:message key="label.mark"/> </b>
							<c:out value="${mcGeneralLearnerFlowDTO.learnerMark}"/>  &nbsp
							<bean:message key="label.outof"/> &nbsp
						  	<c:out value="${mcGeneralLearnerFlowDTO.totalQuestionCount}"/>
					  	</td>
					  </tr>	

			 		<c:if test="${mcGeneralLearnerFlowDTO.retries == 'true'}"> 							
						<c:if test="${mcGeneralLearnerFlowDTO.userOverPassMark != 'true' && 
									mcGeneralLearnerFlowDTO.passMarkApplicable == 'true' }">
							 <tr>
							  	<td NOWRAP align=left valign=top colspan=2> 
								   <bean:message key="label.notEnoughMarks"/> 
							  	</td>
							  </tr>	
	  					</c:if> 			
  					</c:if> 			  					
					

				  <tr>						 
					  	<td NOWRAP align=left valign=top colspan=2> 
							<b>	<bean:message key="label.yourAnswers"/> </b>
						</td>
					</tr>
					  	
				  <tr>						 
					  	<td NOWRAP align=left valign=top colspan=2> 
						<table align=left>
								<c:forEach var="dto" varStatus="status" items="${requestScope.listSelectedQuestionCandidateAnswersDto}">
									  <tr>
									  	<td NOWRAP align=left valign=top> 
										  		<c:out value="${dto.question}"/> &nbsp[ <b> <bean:message key="label.weight"/> : </b>
										  		<c:out value="${dto.weight}"/> ]
									  	</td>
									  </tr>
									
									  <tr>
									  	<td NOWRAP align=left valign=top> 
											<c:forEach var="caText" varStatus="status" items="${dto.candidateAnswers}">
													<c:out value="${caText.value}" escapeXml="false"/><BR>											
											</c:forEach>
									  	</td>
									  </tr>


									  <tr>
									  	<td NOWRAP align=left valign=top> 
											<b> <bean:message key="label.feedback"/> </b>																				  									  	
											<c:if test="${dto.attemptCorrect == 'true'}"> 											  
													 <c:out value="${dto.feedbackCorrect}" escapeXml="false"/><BR>											
											</c:if>																
											<c:if test="${dto.attemptCorrect != 'true'}"> 		
													<c:out value="${dto.feedbackIncorrect}" escapeXml="false"/><BR>											
											</c:if>																
									  	</td>
									  </tr>
									  
								</c:forEach>								
						</table>
						</td>
					</tr>

					
			 		<c:if test="${mcGeneralLearnerFlowDTO.retries == 'true'}"> 					  	   
		  	   		  <tr>
					  	<td NOWRAP colspan=2 valign=top> 
					  			<html:submit property="redoQuestions" styleClass="button">
									<bean:message key="label.redo.questions"/>
								</html:submit>	 		
			       
		   						<html:submit property="viewSummary" styleClass="button">
									<bean:message key="label.view.summary"/>
								</html:submit>	 				 		  					

								<c:if test="${((mcGeneralLearnerFlowDTO.passMarkApplicable == 'true') && (mcGeneralLearnerFlowDTO.userOverPassMark == 'true'))}">
									<c:if test="${mcGeneralLearnerFlowDTO.reflection != 'true'}"> 						  			  		
										<html:submit property="learnerFinished"  styleClass="button">
											<bean:message key="label.finished"/>
										</html:submit>	 				
								  	</c:if> 				    					
					
									<c:if test="${mcGeneralLearnerFlowDTO.reflection == 'true'}"> 						  			  		
										<html:submit property="forwardtoReflection" styleClass="button">
											<bean:message key="label.continue"/>
										</html:submit>	 				
								  	</c:if> 				    					
						  	   </c:if>	
								<c:if test="${(mcGeneralLearnerFlowDTO.passMarkApplicable != 'true')}">
									<c:if test="${mcGeneralLearnerFlowDTO.reflection != 'true'}"> 						  			  		
										<html:submit property="learnerFinished"  styleClass="button">
											<bean:message key="label.finished"/>
										</html:submit>	 				
								  	</c:if> 				    					
					
									<c:if test="${mcGeneralLearnerFlowDTO.reflection == 'true'}"> 						  			  		
										<html:submit property="forwardtoReflection" styleClass="button">
											<bean:message key="label.continue"/>
										</html:submit>	 				
								  	</c:if> 				    					
						  	   </c:if>	
					  	 </td>
					  </tr>
					</c:if> 																		

					<c:if test="${mcGeneralLearnerFlowDTO.retries != 'true'}"> 							  
		  	   		  <tr>
		  	   		    <td NOWRAP valign=top>
		   						<html:submit property="viewSummary" styleClass="button">
									<bean:message key="label.view.summary"/>
								</html:submit>	 				 		  					
						</td>


		  	   		    <td NOWRAP valign=top>						
	  						<div class="right-buttons">												
								<c:if test="${mcGeneralLearnerFlowDTO.reflection != 'true'}"> 						  			  		
									<html:submit property="learnerFinished"  styleClass="button">
										<bean:message key="label.finished"/>
									</html:submit>	 				
							  	</c:if> 				    					
				
								<c:if test="${mcGeneralLearnerFlowDTO.reflection == 'true'}"> 						  			  		
									<html:submit property="forwardtoReflection" styleClass="button">
										<bean:message key="label.continue"/>
									</html:submit>	 				
							  	</c:if> 				    					
 							</div>						   
						</td> 							 							

					  	 
					  </tr>
					</c:if> 																		
					
				</table>
	</html:form>

</div>

<div id="footer"></div>

</div>
</body>
</html:html>

