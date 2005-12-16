<%@ taglib uri="tags-html-el" prefix="html" %>
<%@ taglib uri="tags-bean" prefix="bean" %>
<%@ taglib uri="tags-logic-el" prefix="logic-el" %>
<%@ taglib uri="tags-c" prefix="c" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="fck-editor" prefix="FCK" %>

<script language="JavaScript" type="text/JavaScript">
<!--
	function deleteOption(deletableOptionIndex, actionMethod) {
		document.McAuthoringForm.deletableOptionIndex.value=deletableOptionIndex; 
		submitMethod(actionMethod);
	}
//-->     
</script>

				<table class="forms" align=center>
					<tr>
	 				 	<td class="formlabel" align=right valign=top> 
							<bean:message key="label.question"/>:
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

					<tr> <td colspan=4 align=center valign=top width="100%">
					
					<table align="center" border="1">
					     <tr>
							  	<td bgcolor="#A8C7DE" colspan=4 class="input" valign=top align=left>
								  	 <font size=2> <b> <bean:message key="label.mc.options"/> </b> </font>
							  	</td>
						 </tr>					
					
						<c:set var="optionIndex" scope="session" value="1"/>
			  	 		<c:set var="selectedOptionFound" scope="request" value="0"/>
						<c:forEach var="optionEntry" items="${sessionScope.mapOptionsContent}">
							  <c:if test="${optionEntry.key == 1}"> 			
								  <tr>
									  	<td bgcolor="#EEEEEE" class="input" valign=top>  <font size=2> <b> <c:out value="Candidate Answer ${optionIndex}"/> :  </b>  </font>

								  			<input type="text" name="optionContent<c:out value="${optionIndex}"/>" value="<c:out value="${optionEntry.value}"/>"   
									  		size="50" maxlength="255">
							
									  	
			  				  	 		<c:set var="selectedOptionFound" scope="request" value="0"/>
			  							<c:forEach var="selectedOptionEntry" items="${sessionScope.mapSelectedOptions}">
											  <c:if test="${selectedOptionEntry.value == optionEntry.value}"> 			
						  					
													  	<select name="checkBoxSelected<c:out value="${optionIndex}"/>">
															<option value="Incorrect" > Incorrect </option>
															<option value="Correct" SELECTED>   Correct  </option>
														</select>
											
						  				  	 		<c:set var="selectedOptionFound" scope="request" value="1"/>
						 					</c:if> 			
										</c:forEach>
										
									  <c:if test="${selectedOptionFound == 0}"> 			
								  			
												  	<select name="checkBoxSelected<c:out value="${optionIndex}"/>">
														<option value="Incorrect" SELECTED> Incorrect </option>
														<option value="Correct">   Correct  </option>
													</select>
					 					</c:if> 			
										</td>
	
										<td bgcolor="#EEEEEE" class="input" valign=top>
	       								    <img src="images/delete.gif" align=left onclick="javascript:deleteOption(1,'removeOption');">
									  	</td>
							 </tr>
							</c:if> 			
					  		<c:if test="${optionEntry.key > 1}"> 			
								<c:set var="optionIndex" scope="session" value="${optionIndex +1}"/>
								  <tr>
								  	<td bgcolor="#EEEEEE" class="input" valign=top> <font size=2> <b> <c:out value="Candidate Answer ${optionIndex}"/> : </b></font>

								  			<input type="text" name="optionContent<c:out value="${optionIndex}"/>" value="<c:out value="${optionEntry.value}"/>"
									  		size="50" maxlength="255">

			  				  	 		<c:set var="selectedOptionFound" scope="request" value="0"/>
			  							<c:forEach var="selectedOptionEntry" items="${sessionScope.mapSelectedOptions}">
											  <c:if test="${selectedOptionEntry.value == optionEntry.value}"> 			

													  	<select name="checkBoxSelected<c:out value="${optionIndex}"/>">
															<option value="Incorrect" > Incorrect </option>
															<option value="Correct" SELECTED>   Correct  </option>
														</select>

						  				  	 		<c:set var="selectedOptionFound" scope="request" value="1"/>
						 					</c:if> 			
										</c:forEach>
										
									  <c:if test="${selectedOptionFound == 0}"> 			

												  	<select name="checkBoxSelected<c:out value="${optionIndex}"/>">
														<option value="Incorrect" SELECTED> Incorrect </option>
														<option value="Correct">   Correct  </option>
													</select>
					 					</c:if> 			
										</td>
	
										<td bgcolor="#EEEEEE" class="input" valign=top>
	       								    <img src="images/delete.gif" align=left onclick="javascript:deleteOption(<c:out value="${optionIndex}"/>,'removeOption');">
    								  	</td>
								  </tr>
							</c:if> 			
						  		<input type=hidden name="checkBoxSelected<c:out value="${optionIndex}"/>">
						</c:forEach>
						
						 <tr>
							  	<td bgcolor="#EEEEEE" colspan=4 class="input" valign=top align=right>
									<html:submit onclick="javascript:submitMethod('addOption');" styleClass="a.button" >
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
	 				 	<td colspan=4 align=center valign=top>								
							&nbsp
					  	</td>
					</tr>

					<tr>
	 				 	<td class="formlabel" align=right valign=top> 
							<bean:message key="label.feedback.incorrect"/>
						</td>
						<td class="formcontrol" colspan=3 valign=top>
							<FCK:editor id="richTextFeedbackInCorrect" basePath="/lams/fckeditor/">
								  <c:out value="${sessionScope.richTextFeedbackInCorrect}" escapeXml="false" />						  
							</FCK:editor>
						</td> 
					</tr>
					
					<tr>
	 				 	<td class="formlabel" align=right valign=top> 
							<bean:message key="label.feedback.correct"/>
						</td>
						<td class="formcontrol" colspan=3 valign=top>
							<FCK:editor id="richTextFeedbackCorrect" basePath="/lams/fckeditor/">
								  <c:out value="${sessionScope.richTextFeedbackCorrect}" escapeXml="false" />						  
							</FCK:editor>
						</td> 
					</tr>

			
	 				 <tr>
	 				 <td valign=top> </td>
	 				 <td class="input" align=left colspan=3 valign=top>
							<html:submit onclick="javascript:submitMethod('doneOptions');" styleClass="a.button">
								<bean:message key="button.done"/>
							</html:submit>	 				 		  										  		
		 			  	</td>
	 				 </tr>
	 				 
				</table> 	 
