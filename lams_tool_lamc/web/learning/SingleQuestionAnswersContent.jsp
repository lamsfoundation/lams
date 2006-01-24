<%@ taglib uri="tags-html-el" prefix="html" %>
<%@ taglib uri="tags-bean" prefix="bean" %>
<%@ taglib uri="tags-logic-el" prefix="logic-el" %>
<%@ taglib uri="tags-c" prefix="c" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-fck-editor" prefix="FCK" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

	<!--options content goes here-->
				<table align=center bgcolor="#FFFFFF">
					  <tr>
					  	<td align=left class="input" valign=top bgColor="#333366" colspan=2> 
						  	<font size=2 color="#FFFFFF"> <b>  <bean:message key="label.assessment"/> </b> </font>
					  	</td>
					  </tr>
					  

			 		<c:if test="${sessionScope.isRetries == 'true'}"> 		
					  <tr>
					  	<td align=center class="input" valign=top colspan=2> 
						  	<font size=3> <b>  <bean:message key="label.withRetries"/> </b> </font>
					  	</td>
					  </tr>
					</c:if> 			
				
					<c:if test="${sessionScope.isRetries != 'true'}"> 		
					  <tr>
					  	<td align=center class="input" valign=top colspan=2> 
						  	<font size=3> <b>  <bean:message key="label.withoutRetries"/> </b> </font>
					  	</td>
					  </tr>
					</c:if> 			

			 		<c:if test="${sessionScope.isRetries == 'true' && sessionScope.passMark > 0}"> 		
					  <tr>
					  	<td align=left class="input" valign=top colspan=2> 
						  	<font size=2> <b>  <bean:message key="label.learner.message"/>
						  		 (<c:out value="${sessionScope.passMark}"/><bean:message key="label.percent"/> ) 
						  	</b> </font>
					  	</td>
					  </tr>
					</c:if> 								  
				
  		  	 		<c:set var="mainQueIndex" scope="session" value="0"/>
					<c:forEach var="questionEntry" items="${sessionScope.mapQuestionContentLearner}">
					<c:set var="mainQueIndex" scope="session" value="${mainQueIndex +1}"/>
					
			  		<c:if test="${questionEntry.key == sessionScope.currentQuestionIndex  &&  sessionScope.questionListingMode == sessionScope.questionListingModeSequential}"> 			
					
						  <tr>
						  	<td align=left class="input" valign=top bgColor="#999966" colspan=2> 
							  	<font color="#FFFFFF"> 
								  	<font size=2>
								  		<c:out value="${questionEntry.value}"/> 
							  		</font>
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
									  		

							  		  	 		<c:set var="checkedOptionFound" scope="request" value="0"/>
												<!-- traverse the selected option from here --> 									  		
	  											<c:forEach var="selectedMainEntry" items="${sessionScope.mapGeneralCheckedOptionsContent}">
														<c:if test="${selectedMainEntry.key == sessionScope.queIndex}"> 		
													  		<c:forEach var="selectedSubEntry" items="${selectedMainEntry.value}">

																<c:if test="${subEntry.key == selectedSubEntry.key}"> 		
									  							
																	<tr> 
																		<td align=left class="input" valign=top> 
																			<font size=2>
																				<input type="checkbox" 
																				name=optionCheckBox<c:out value="${sessionScope.queIndex}"/>-<c:out value="${subEntry.key}"/>
																				onclick="javascript:document.forms[0].optionCheckBoxSelected.value=1; 
																				document.forms[0].questionIndex.value=<c:out value="${sessionScope.queIndex}"/>; 
																				document.forms[0].optionIndex.value=<c:out value="${subEntry.key}"/>;
																				document.forms[0].optionValue.value='<c:out value="${subEntry.value}"/>';
																				
																				if (this.checked == 1)
																				{
																					document.forms[0].checked.value=true;
																				}
																				else
																				{
																					document.forms[0].checked.value=false;
																				}
																				document.forms[0].submit();" CHECKED> 
																			</font>
																		</td> 
																		<td align=left class="input" valign=top> 
																			<font color="#CCCC99"> 	
																				<font size=2>
																					<c:out value="${subEntry.value}"/> 
																				</font>
																			</font>
																		</td>
																	</tr>	
												  		  	 		<c:set var="checkedOptionFound" scope="request" value="1"/>
				  												</c:if> 			

														</c:forEach>																						
	  												</c:if> 			
												</c:forEach>									
												<!-- till  here --> 									  					

												<c:if test="${requestScope.checkedOptionFound == 0}"> 		
																	<tr> 
																		<td align=left class="input" valign=top> 
																			<font size=2>
																				<input type="checkbox" 
																				name=optionCheckBox<c:out value="${sessionScope.queIndex}"/>-<c:out value="${subEntry.key}"/>
																				onclick="javascript:document.forms[0].optionCheckBoxSelected.value=1; 
																				document.forms[0].questionIndex.value=<c:out value="${sessionScope.queIndex}"/>; 
																				document.forms[0].optionIndex.value=<c:out value="${subEntry.key}"/>;
																				document.forms[0].optionValue.value='<c:out value="${subEntry.value}"/>';																			
	
																				if (this.checked == 1)
																				{
																					document.forms[0].checked.value=true;
																				}
																				else
																				{
																					document.forms[0].checked.value=false;
																				}
																				document.forms[0].submit();"> 
																			</font>
																		</td> 
																		<td align=left class="input" valign=top> 
																			<font size=2>
																				<font color="#CCCC99"> <c:out value="${subEntry.value}"/> </font>
																			</font>
																		</td>
																	</tr>	
  												</c:if> 			

											</c:forEach>
										</c:if> 			
								</c:forEach>
							</table>
							</td>
						</tr>
						</c:if> 				
					</c:forEach>

			  	   	<tr> 
				  	   	<html:hidden property="optionCheckBoxSelected"/>
						<html:hidden property="questionIndex"/>
						<html:hidden property="optionIndex"/>
						<html:hidden property="optionValue"/>						
						<html:hidden property="checked"/>
				 		<td colspan=2 class="input" valign=top> 
				 		&nbsp
				 		</td>
			  	   </tr>

		 		<c:if test="${sessionScope.totalCountReached != 'true'}"> 		
	  	   		  <tr>
				  	<td colspan=2 align=right class="input" valign=top> 
					  	<font size=2>
				  			<html:submit property="nextOptions" styleClass="a.button">
								<bean:message key="button.nextQuestion"/>
							</html:submit>	 				 		  					
						</font>
				  	 </td>
				  </tr>
				</c:if> 								  

		 		<c:if test="${sessionScope.totalCountReached == 'true'}"> 		
	  	   		  <tr>
				  	<td colspan=2 align=right class="input" valign=top> 
					  	<font size=2>
				  			<html:submit property="continueOptionsCombined" styleClass="a.button">
								<bean:message key="button.continue"/>
							</html:submit>	 				 		  					
						</font>
				  	 </td>
				  </tr>
				</c:if> 								  
			</table>
	<!--options content ends here-->

