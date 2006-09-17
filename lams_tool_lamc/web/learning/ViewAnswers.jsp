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

<body>
	<div id="page-learner">
	
	<h1 class="no-tabs-below">
		<c:out value="${mcGeneralLearnerFlowDTO.activityTitle}" escapeXml="false" />
	</h1>

<div id="header-no-tabs-learner"></div>

<div id="content-learner">
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
					<c:if test="${mcGeneralLearnerFlowDTO.learnerProgress != 'true'}"> 							  
					  <tr>
					  	<th scope="col" valign=top colspan=2> 
						  	 <bean:message key="label.assessment"/> 
					  	</th>
					  </tr>
					</c:if> 								  					  

					  <tr>
					  	<td NOWRAP align=center valign=top colspan=2> 
						  	<c:out value="${mcGeneralLearnerFlowDTO.reportTitleLearner}" escapeXml="false" />
					  	</td>
					  </tr>


					<c:if test="${mcGeneralLearnerFlowDTO.learnerProgress != 'true'}"> 							  
					  <tr>
					  	<td NOWRAP align=center valign=top colspan=2> 
						  	<b>  <bean:message key="label.viewAnswers"/>  </b>
					  	</td>
					  </tr>
					</c:if> 								  

					<c:if test="${mcGeneralLearnerFlowDTO.learnerProgress == 'true'}"> 							  
					  <tr>
					  	<td NOWRAP align=center valign=top colspan=2> 
						  	 <b> <bean:message key="label.learner.viewAnswers"/> </b>
					  	</td>
					  </tr>
					</c:if> 								  

					
  		  	 		<c:set var="mainQueIndex" scope="request" value="0"/>
					<c:forEach var="questionEntry" items="${mcGeneralLearnerFlowDTO.mapQuestionsContent}">
					<c:set var="mainQueIndex" scope="request" value="${mainQueIndex +1}"/>
						  <tr>
						  	<td NOWRAP align=left valign=top colspan=2> 
								  		<c:out value="${questionEntry.value}"/> 
						  	</td>
						  </tr>

								  								  
						  <tr>						 
							<td NOWRAP align=left>
							<table align=left>
			  		  	 		<c:set var="queIndex" scope="request" value="0"/>
								<c:forEach var="mainEntry" items="${mcGeneralLearnerFlowDTO.mapGeneralOptionsContent}">
									<c:set var="queIndex" scope="request" value="${queIndex +1}"/>
										<c:if test="${requestScope.mainQueIndex == requestScope.queIndex}"> 		
									  		<c:forEach var="subEntry" items="${mainEntry.value}">
				  								<tr> 
													<td NOWRAP align=left valign=top> 
						   								    <img src="<c:out value="${tool}"/>images/dot.jpg" align=left> &nbsp
															<c:out value="${subEntry.value}"/> 
													</td> 
												</tr>	
											</c:forEach>

												<tr>												
												<td NOWRAP colspan=2 align=left valign=top> 
					   								   <b>  <bean:message key="label.attempts"/> </b>
												</td> 
												</tr>
												
												<tr>
													<td  NOWRAP align=left valign=top> 											
													<table align=left>
														<c:forEach var="attemptEntry" items="${mcGeneralLearnerFlowDTO.mapQueAttempts}">
															<c:if test="${requestScope.mainQueIndex == attemptEntry.key}"> 		
 											  		  	 		<c:set var="aIndex" scope="request" value="1"/>
																 <c:forEach var="i" begin="1" end="30" step="1">

					 													<c:forEach var="distinctAttemptEntry" items="${attemptEntry.value}">
																				<c:if test="${distinctAttemptEntry.key == i}"> 	
																					<tr>
																						<td NOWRAP align=left valign=top> 
																								<b> <bean:message key="label.attempt"/> <c:out value="${requestScope.aIndex}"/>:  </b>
																						</td>
																						<c:set var="aIndex" scope="request" value="${requestScope.aIndex +1}"/>
								 														<td align=left valign=top> 																					
									 														<table align=left>
											 													<c:forEach var="singleAttemptEntry" items="${distinctAttemptEntry.value}">
																									<tr>
																										<td NOWRAP align=left valign=top>
													 															<c:out value="${singleAttemptEntry.value}"/> 
												 														</td>
																									</tr>	
																								</c:forEach>
																							</table>	
								 														</td>
																					</tr>		
																				</c:if> 																																						
																		</c:forEach>
																		
																</c:forEach>
															</c:if> 																		
														</c:forEach>
													</table>
													</td>
											  </tr>
										</c:if> 			
								</c:forEach>
							</table>
							</td>
						</tr>
					</c:forEach>


					<c:if test="${mcGeneralLearnerFlowDTO.reportViewOnly != 'true'}"> 							  
						<c:if test="${mcGeneralLearnerFlowDTO.learnerProgress != 'true'}"> 							  
						 		<c:if test="${mcGeneralLearnerFlowDTO.retries == 'true'}"> 					  	   
					  	   		  <tr>
								  	<td NOWRAP valign=top> 
								  			<html:submit property="redoQuestions" styleClass="button">
												<bean:message key="label.redo.questions"/>
											</html:submit>	 		
						       
					   						<html:submit property="viewSummary" styleClass="button">
												<bean:message key="label.view.summary"/>
											</html:submit>	 				 		  					
								  	 </td>
								  </tr>
								</c:if> 																		
			
								<c:if test="${mcGeneralLearnerFlowDTO.retries != 'true'}"> 							  
					  	   		  <tr>
					  	   		    <td  valign=top>
						   						<html:submit property="viewSummary" styleClass="button">
													<bean:message key="label.view.summary"/>
												</html:submit>	 				 		  					
								  	 </td>
								  	 
		 			  	   		    <td  valign=top>
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
						</c:if> 																								
					</c:if> 					
					

							<tr> 
							<td valign=top>
									<table>
										<tr> 
						  	   		  		<td>
												<b> <bean:message key="label.notebook.entries"/> </b>						
											 </td>
										</tr>
										
										<tr> 
						  	   		  		<td>
												<c:out value="${mcGeneralLearnerFlowDTO.notebookEntry}" escapeXml="false"/>				  	   		  		
											 </td>
										</tr>
								</table>
		
							</td>
							</tr>

					
				  	<html:hidden property="doneLearnerProgress"/>						   
				</table>
	</html:form>

</div>

<div id="footer-learner"></div>

</div>
</body>
</html:html>









	
	