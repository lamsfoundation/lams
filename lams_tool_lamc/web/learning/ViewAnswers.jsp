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
  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
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
		<c:out value="${sessionScope.activityTitle}" escapeXml="false" />
	</h1>

<div id="header-no-tabs-learner"></div>

<div id="content-learner">
		<html:form  action="/learning?method=displayMc&validate=false" method="POST" target="_self">
				<table class="forms">
					<c:if test="${sessionScope.learnerProgress != 'true'}"> 							  
					  <tr>
					  	<th scope="col" valign=top colspan=2> 
						  	 <bean:message key="label.assessment"/> 
					  	</th>
					  </tr>
					</c:if> 								  					  

					  <tr>
					  	<td NOWRAP align=center valign=top colspan=2> 
						  	<c:out value="${reportTitleLearner}" escapeXml="false" />
					  	</td>
					  </tr>


					<c:if test="${sessionScope.learnerProgress != 'true'}"> 							  
					  <tr>
					  	<td NOWRAP align=center valign=top colspan=2> 
						  	<b>  <bean:message key="label.viewAnswers"/>  </b>
					  	</td>
					  </tr>
					</c:if> 								  

					<c:if test="${sessionScope.learnerProgress == 'true'}"> 							  
					  <tr>
					  	<td NOWRAP align=center valign=top colspan=2> 
						  	 <b> <bean:message key="label.learner.viewAnswers"/> </b>
					  	</td>
					  </tr>
					</c:if> 								  

					
  		  	 		<c:set var="mainQueIndex" scope="session" value="0"/>
					<c:forEach var="questionEntry" items="${sessionScope.mapQuestionContentLearner}">
					<c:set var="mainQueIndex" scope="session" value="${mainQueIndex +1}"/>
						  <tr>
						  	<td NOWRAP align=left valign=top colspan=2> 
								  		<c:out value="${questionEntry.value}"/> 
						  	</td>
						  </tr>

								  								  
						  <tr>						 
							<td NOWRAP align=left>
							<table align=left>
			  		  	 		<c:set var="queIndex" scope="session" value="0"/>
								<c:forEach var="mainEntry" items="${sessionScope.mapGeneralOptionsContent}">
									<c:set var="queIndex" scope="session" value="${queIndex +1}"/>
										<c:if test="${sessionScope.mainQueIndex == sessionScope.queIndex}"> 		
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
														<c:forEach var="attemptEntry" items="${sessionScope.mapQueAttempts}">
															<c:if test="${sessionScope.mainQueIndex == attemptEntry.key}"> 		
 											  		  	 		<c:set var="aIndex" scope="session" value="1"/>
																 <c:forEach var="i" begin="1" end="30" step="1">

					 													<c:forEach var="distinctAttemptEntry" items="${attemptEntry.value}">
																				<c:if test="${distinctAttemptEntry.key == i}"> 	
																					<tr>
																						<td NOWRAP align=left valign=top> 
																								<b> <bean:message key="label.attempt"/> <c:out value="${sessionScope.aIndex}"/>:  </b>
																						</td>
																						<c:set var="aIndex" scope="session" value="${sessionScope.aIndex +1}"/>
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



				<c:if test="${sessionScope.learnerProgress != 'true'}"> 							  
				 		<c:if test="${sessionScope.isRetries == 'true'}"> 					  	   
			  	   		  <tr>
						  	<td NOWRAP valign=top> 
						  			<html:submit property="redoQuestions" styleClass="button">
										<bean:message key="label.redo.questions"/>
									</html:submit>	 		
				       
			   						<html:submit property="viewSummary" styleClass="button">
										<bean:message key="label.view.summary"/>
									</html:submit>	 				 		  					

								<c:if test="${((McLearningForm.passMarkApplicable == 'true') && (McLearningForm.userOverPassMark == 'true'))}">
								  	   <html:submit property="learnerFinished" styleClass="button">
											<bean:message key="label.finished"/>
									   </html:submit>
							  	   </c:if>
						  	 </td>
						  </tr>
						</c:if> 																		
	
						<c:if test="${sessionScope.isRetries != 'true'}"> 							  
			  	   		  <tr>
			  	   		    <td  valign=top>
				   						<html:submit property="viewSummary" styleClass="button">
											<bean:message key="label.view.summary"/>
										</html:submit>	 				 		  					
						  	 </td>
						  	 
 			  	   		    <td  valign=top>
	    	  						<div class="right-buttons">
								  	   <html:submit property="learnerFinished" styleClass="button">
													<bean:message key="label.finished"/>
									   </html:submit>
									</div>
						  	 </td>
						  	 
						  </tr>
						</c:if> 																		
					</c:if> 																								
					
				  	<html:hidden property="doneLearnerProgress"/>						   
				</table>
	</html:form>

</div>

<div id="footer-learner"></div>

</div>
</body>
</html:html>









	
	