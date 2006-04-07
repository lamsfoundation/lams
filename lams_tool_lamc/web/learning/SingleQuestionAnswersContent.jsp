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

<%@ taglib uri="tags-bean" prefix="bean"%> 
<%@ taglib uri="tags-html" prefix="html"%>
<%@ taglib uri="tags-logic" prefix="logic" %>
<%@ taglib uri="tags-logic-el" prefix="logic-el" %>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="fck-editor" prefix="FCK" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

<c:set var="lams"><lams:LAMSURL/></c:set>
<c:set var="tool"><lams:WebAppURL/></c:set>


	<!--options content goes here-->
				<table align=center bgcolor="#FFFFFF">
					  <tr>
					  	<td NOWRAP align=left class="input" valign=top bgColor="#333366" colspan=2> 
						  	<font size=2 color="#FFFFFF"> <b>  <bean:message key="label.assessment"/> </b> </font>
					  	</td>
					  </tr>
					  
					  
					  <tr>
					  	<td NOWRAP align=left class="input" valign=top bgColor="white" colspan=2> 
						  	<c:out value="${activityTitle}" escapeXml="false" /> 
					  	</td>
					  </tr>
					  
					  <tr>
					  	<td NOWRAP align=left class="input" valign=top bgColor="white" colspan=2> 
						  	<c:out value="${activityInstructions}" escapeXml="false" /> 
					  	</td>
					  </tr>


			 		<c:if test="${sessionScope.isRetries == 'true'}"> 		
					  <tr>
					  	<td NOWRAP align=center class="input" valign=top colspan=2> 
						  	<font size=3> <b>  <bean:message key="label.withRetries"/> </b> </font>
					  	</td>
					  </tr>
					</c:if> 			
				
					<c:if test="${sessionScope.isRetries != 'true'}"> 		
					  <tr>
					  	<td NOWRAP align=center class="input" valign=top colspan=2> 
						  	<font size=3> <b>  <bean:message key="label.withoutRetries"/> </b> </font>
					  	</td>
					  </tr>
					</c:if> 			

			 		<c:if test="${sessionScope.isRetries == 'true' && sessionScope.passMark > 0}"> 		
					  <tr>
					  	<td NOWRAP align=left class="input" valign=top colspan=2> 
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
						  	<td NOWRAP align=left class="input" valign=top bgColor="#999966" colspan=2> 
							  	<font color="#FFFFFF"> 
								  	<font size=2>
								  		<c:out value="${questionEntry.value}"/> 
							  		</font>
							  	</font> 
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
									  		
							  		  	 		<c:set var="checkedOptionFound" scope="request" value="0"/>
												<!-- traverse the selected option from here --> 									  		
	  											<c:forEach var="selectedMainEntry" items="${sessionScope.mapGeneralCheckedOptionsContent}">
														<c:if test="${selectedMainEntry.key == sessionScope.queIndex}"> 		
													  		<c:forEach var="selectedSubEntry" items="${selectedMainEntry.value}">

																<c:if test="${subEntry.key == selectedSubEntry.key}"> 		
									  							
																	<tr> 
																		<td NOWRAP align=left class="input" valign=top> 
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
																		<td NOWRAP align=left class="input" valign=top> 
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
																		<td NOWRAP align=left class="input" valign=top> 
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
																		<td NOWRAP align=left class="input" valign=top> 
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


		  		<c:if test="${sessionScope.totalCountReached != 'true'  &&  sessionScope.totalQuestionCount != '1'}"> 			
	  	   		  <tr>
				  	<td NOWRAP colspan=2 align=right class="input" valign=top> 
					  	<font size=2>
				  			<html:submit property="nextOptions" styleClass="button">
								<bean:message key="button.nextQuestion"/>
							</html:submit>	 				 		  					
						</font>
				  	 </td>
				  </tr>
				</c:if> 								  

		 		<c:if test="${sessionScope.totalQuestionCount == '1'}"> 		
	  	   		  <tr>
				  	<td NOWRAP colspan=2 align=right class="input" valign=top> 
					  	<font size=2>
				  			<html:submit property="continueOptionsCombined" styleClass="button">
								<bean:message key="button.continue"/>
							</html:submit>	 				 		  					
						</font>
				  	 </td>
				  </tr>
				</c:if> 								  
				
		 		<c:if test="${sessionScope.totalCountReached == 'true'}"> 		
	  	   		  <tr>
				  	<td NOWRAP colspan=2 align=right class="input" valign=top> 
					  	<font size=2>
				  			<html:submit property="continueOptionsCombined" styleClass="button">
								<bean:message key="button.continue"/>
							</html:submit>	 				 		  					
						</font>
				  	 </td>
				  </tr>
				</c:if> 								  
			</table>
	<!--options content ends here-->

