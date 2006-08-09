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

		    	<table align=center> 	  
					<tr> <td>
						<jsp:include page="/McErrorBox.jsp" />
					</td> </tr>
				</table>


				<table class="forms">
					<tr>
	 				 	<td NOWRAP valign=top> 
								<b> <bean:message key="label.question"/>: </b>
						</td>
						
						<td colspan=3 valign=top>
				  				<input type="text" name="selectedQuestion" value="<c:out value="${sessionScope.selectedQuestion}"/>"   
						  			size="50" maxlength="255"> 
					  	</td>
					</tr>
					
					<tr>
	 				 	<td colspan=4 align=center valign=top>				
	 				 	&nbsp				
					  	</td>
					</tr>

					<tr> <td colspan=4 align=center valign=top width="80%">
					
					<table align="center">
					     <tr>
							  	<td bgcolor="#A8C7DE" colspan=4 valign=top align=left>
							  	 	 <b> <bean:message key="label.mc.options"/> </b> 
							  	</td>
						 </tr>					
					
						<c:set var="optionIndex" scope="session" value="1"/>
			  	 		<c:set var="selectedOptionFound" scope="request" value="0"/>
						<c:forEach var="optionEntry" items="${sessionScope.mapOptionsContent}">
							  <c:if test="${optionEntry.key == 1}"> 			
								  <tr>
								  
									  	<td bgcolor="#EEEEEE" valign=top>   <b> <bean:message key="label.candidateAnswer"/> ${optionIndex} :  </b>  

							  			<input type="text" name="optionContent<c:out value="${optionIndex}"/>" value="<c:out value="${optionEntry.value}"/>"   
								  		size="50" maxlength="255">

							
			  				  	 		<c:set var="selectedOptionFound" scope="request" value="0"/>
			  							<c:forEach var="selectedOptionEntry" items="${sessionScope.mapSelectedOptions}">
												  <c:if test="${selectedOptionEntry.value == optionEntry.value}"> 			
							  					
														  	<select name="checkBoxSelected<c:out value="${optionIndex}"/>">
																<option value="Incorrect" >  <bean:message key='option.incorrect'/>  </option>
																<option value="Correct" SELECTED>  <bean:message key='option.correct'/> </option>
															</select>
												
							  				  	 		<c:set var="selectedOptionFound" scope="request" value="1"/>
							 					</c:if> 			
										</c:forEach>
										
									  <c:if test="${selectedOptionFound == 0}"> 			
													  	<select name="checkBoxSelected<c:out value="${optionIndex}"/>">
															<option value="Incorrect" SELECTED>  <bean:message key='option.incorrect'/>  </option>
															<option value="Correct">   <bean:message key='option.correct'/>  </option>
														</select>
					 					</c:if> 			
										</td>
	
										<td bgcolor="#EEEEEE"  valign=top>
											<a title="<bean:message key='label.tip.removeCandidateAnswer'/>" href="javascript:;" onclick="javascript:deleteOption(1,'removeOption');">
		                                                <img src="<c:out value="${tool}"/>images/delete.gif" border="0">
											</a> 
									  	</td>
							 </tr>
							</c:if> 			
					  		<c:if test="${optionEntry.key > 1}"> 			
								<c:set var="optionIndex" scope="session" value="${optionIndex +1}"/>
								  <tr>
								  	<td bgcolor="#EEEEEE" valign=top> <b> <bean:message key="label.candidateAnswer"/> ${optionIndex} : </b>

							  			<input type="text" name="optionContent<c:out value="${optionIndex}"/>" value="<c:out value="${optionEntry.value}"/>"
								  		size="50" maxlength="255">

			  				  	 		<c:set var="selectedOptionFound" scope="request" value="0"/>
			  							<c:forEach var="selectedOptionEntry" items="${sessionScope.mapSelectedOptions}">

											  <c:if test="${selectedOptionEntry.value == optionEntry.value}"> 			
													  	<select name="checkBoxSelected<c:out value="${optionIndex}"/>">
															<option value="Incorrect" >  <bean:message key='option.incorrect'/>  </option>
															<option value="Correct" SELECTED>  <bean:message key='option.correct'/>   </option>
														</select>
						  				  	 		<c:set var="selectedOptionFound" scope="request" value="1"/>
							 					</c:if> 			
										</c:forEach>
										
									  <c:if test="${selectedOptionFound == 0}"> 			

												  	<select name="checkBoxSelected<c:out value="${optionIndex}"/>">
														<option value="Incorrect" SELECTED> <bean:message key='option.incorrect'/>  </option>
														<option value="Correct">   <bean:message key='option.correct'/>  </option>
													</select>
					 					</c:if> 			
										</td>
	
										<td bgcolor="#EEEEEE" valign=top>
		       								    <img src="<c:out value="${tool}"/>images/delete.gif" align=left onclick="javascript:deleteOption(<c:out value="${optionIndex}"/>,'removeOption');">
    								  	</td>
								  </tr>
							</c:if> 			
						  		<input type=hidden name="checkBoxSelected<c:out value="${optionIndex}"/>">
						</c:forEach>
						
						 <tr>
							  	<td bgcolor="#EEEEEE" colspan=4 valign=top align=right>
										<html:submit onclick="javascript:submitMethod('addOption');" styleClass="button">
												<bean:message key="label.add.option"/>
										</html:submit>	 				 		  										  		
							  	</td>
						 </tr>							
					</table> </td> </tr>	
					
					<html:hidden property="deletableOptionIndex"/>							
					
					<tr>
	 				 	<td colspan=4 align=center valign=top>								
							&nbsp
					  	</td>
					</tr>


					<tr> 
						<td NOWRAP colspan=4 valign=top>
							<lams:SetEditor id="richTextIncorrectFeedback" text="${sessionScope.richTextIncorrectFeedback}" small="true" key="label.feedback.incorrect"/>																						
						</td> 
					</tr>

					<tr> 
						<td NOWRAP colspan=4 valign=top>
							<lams:SetEditor id="richTextCorrectFeedback" text="${sessionScope.richTextCorrectFeedback}" small="true" key="label.feedback.correct"/>																											
						</td> 
					</tr>

	 				 <tr>
	 				 <td align=right colspan=4 valign=top>
							<html:submit onclick="javascript:submitMethod('doneOptions');" styleClass="button">
									<bean:message key="button.done"/>
							</html:submit>	 				 		  										  		
	 			  	</td>
	 				</tr>
				</table> 	 
