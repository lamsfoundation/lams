<%@ taglib uri="tags-html-el" prefix="html" %>
<%@ taglib uri="tags-bean" prefix="bean" %>
<%@ taglib uri="tags-logic-el" prefix="logic-el" %>
<%@ taglib uri="tags-c" prefix="c" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-fck-editor" prefix="FCK" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

		<html:form  action="/learning?method=displayMc&validate=false" method="POST" target="_self">
				<table align=center bgcolor="#FFFFFF">
					  <tr>
					  	<td align=left class="input" valign=top bgColor="#333366" colspan=2> 
						  	<font size=2 color="#FFFFFF"> <b>  <bean:message key="label.assessment"/> </b> </font>
					  	</td>
					  </tr>
				

					  <tr>
					  	<td align=center class="input" valign=top colspan=2> 
						  	<font size=3> <b>  <bean:message key="label.viewAnswers"/> </b> </font>
					  	</td>
					  </tr>


					<tr>
						<td align=right class="input" valign=top colspan=2> 
							<hr>
						</td> 
					</tr>
					
  		  	 		<c:set var="mainQueIndex" scope="session" value="0"/>
					<c:forEach var="questionEntry" items="${sessionScope.mapQuestionContentLearner}">
					<c:set var="mainQueIndex" scope="session" value="${mainQueIndex +1}"/>
						  <tr>
						  	<td align=left class="input" valign=top bgColor="#999966" colspan=2> 
							  	<font color="#FFFFFF"> 
							  		<c:out value="${questionEntry.value}"/> 
							  	</font> 
						  	</td>
						  </tr>

								  								  
						  <tr>						 
							<td align=left>
							<table align=left>
			  		  	 		<c:set var="queIndex" scope="session" value="0"/>
								<c:forEach var="mainEntry" items="${sessionScope.mapGeneralOptionsContent}">
									<c:set var="queIndex" scope="session" value="${queIndex +1}"/>
										<c:if test="${sessionScope.mainQueIndex == sessionScope.queIndex}"> 		
									  		<c:forEach var="subEntry" items="${mainEntry.value}">
				  								<tr> 
													<td align=left class="input" valign=top> 
					   								    <img src="images/dot.jpg" align=left> &nbsp
														<font size=2 color="#669966">	<c:out value="${subEntry.value}"/> </font>					   								    
													</td> 
												</tr>	
											</c:forEach>

												<tr>												
												<td colspan=2 align=left class="input" valign=top> 
				   								    <bean:message key="label.you.answered"/>
												</td> 
												</tr>
												
												<tr>
													<td  align=left class="input" valign=top> 											
													<table align=left>
														<c:forEach var="attemptEntry" items="${sessionScope.mapQueAttempts}">
															<c:if test="${sessionScope.mainQueIndex == attemptEntry.key}"> 		
 											  		  	 		<c:set var="aIndex" scope="session" value="1"/>
																 <c:forEach var="i" begin="1" end="30" step="1">

					 													<c:forEach var="distinctAttemptEntry" items="${attemptEntry.value}">
																				<c:if test="${distinctAttemptEntry.key == i}"> 	
																					<tr>
																						<td align=left class="input" valign=top> 
																							<b> <bean:message key="label.attempt"/> <c:out value="${sessionScope.aIndex}"/>: </b>
																						</td>
																						<c:set var="aIndex" scope="session" value="${sessionScope.aIndex +1}"/>
								 														<td align=left class="input" valign=top> 																					
									 														<table align=left>
											 													<c:forEach var="singleAttemptEntry" items="${distinctAttemptEntry.value}">
																									<tr>
																										<td align=left class="input" valign=top>
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

			  	   	<tr> 
				 		<td colspan=2 class="input" valign=top> 
				 		&nbsp
				 		</td>
			  	   </tr>

			  	   

			 		<c:if test="${sessionScope.isRetries == 'true'}"> 					  	   
		  	   		  <tr>
					  	<td colspan=2 align=center class="input" valign=top> 
				  			<html:submit property="redoQuestions" styleClass="a.button">
								<bean:message key="label.redo.questions"/>
							</html:submit>	 		
		       
							<c:if test="${sessionScope.userPassed == 'true'}">
						  	   <html:submit property="learnerFinished" styleClass="a.button">
									<bean:message key="label.finished"/>
							   </html:submit>
					  	   </c:if>

	   						<html:submit property="viewSummary" styleClass="a.button">
								<bean:message key="label.view.summary"/>
							</html:submit>	 				 		  					
					  	 </td>
					  </tr>
					</c:if> 																		

					<c:if test="${sessionScope.isRetries != 'true'}"> 							  
		  	   		  <tr>
		  	   		    <td colspan=2 align=right class="input" valign=top>
			  	   		  	<c:if test="${sessionScope.userPassed == 'true'}">
						  	   <html:submit property="learnerFinished" styleClass="a.button">
											<bean:message key="label.finished"/>
							   </html:submit>
				  	   		</c:if>

	   						<html:submit property="viewSummary" styleClass="a.button">
								<bean:message key="label.view.summary"/>
							</html:submit>	 				 		  					
					  	 </td>
					  </tr>
					</c:if> 																		
					
				</table>
	</html:form>

