<%@ taglib uri="/WEB-INF/struts-html-el.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic-el.tld" prefix="logic-el" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<%@ taglib uri="fck-editor" prefix="FCK" %>

					<table class="forms" align=left>
 						<tr> 
					 		<td class="formlabel" valign=top> <bean:message key="label.authoring.title"/>: </td>
							<td class="formcontrol" valign=top>
								<FCK:editor id="richTextTitle" basePath="/lams/fckeditor/">
									  <c:out value="${sessionScope.richTextTitle}" escapeXml="false" />						  
								</FCK:editor>
							</td> 
					  	</tr>
					  	
					  	<tr> 
					 		<td class="formlabel" valign=top> <bean:message key="label.authoring.instructions"/>: </td>
							<td class="formcontrol" valign=top>
								<FCK:editor id="richTextInstructions" basePath="/lams/fckeditor/">
									  <c:out value="${sessionScope.richTextInstructions}" escapeXml="false" />						  
								</FCK:editor>
							</td>
						</tr>
				
					<tr>
					 	<td colspan=2 align=center valign=top>								
							&nbsp&nbsp
					  	</td>
					</tr>
				
					<tr> <td colspan=2 align=center valign=top>
					
					<table align=center>
					<tr>
						<td class="formlabel" valign=top>
						</td>
						<td class="input" align="center" valign=top>
							 <bean:message key="label.Questions"/> 
						</td>
						<td class="input" align="center" valign=top>
							 <bean:message key="label.weight"/> 
						</td>
						<td class="input" colspan=2 align="center" valign=top>
							 <bean:message key="label.actions"/> 
						</td>
					</tr>
			  	 		<c:set var="queIndex" scope="session" value="1"/>
						<c:forEach var="questionEntry" items="${sessionScope.mapQuestionsContent}">
									<c:if test="${questionEntry.key == 1}"> 			
									    <tr>
										  	<td class="formlabel" valign=top> <c:out value="Question ${queIndex}"/> : </td>
										  	
									  		<td class="input" valign=top> 
									  			<input type="text" name="questionContent<c:out value="${queIndex}"/>" value="<c:out value="${questionEntry.value}"/>"
										  		size="50" maxlength="255" >
										  	</td>

                                            <td class="input" align=center valign=top>										  	
	                                            &nbsp&nbsp
												<c:forEach var="weightsEntry" items="${sessionScope.mapWeights}">                                            
														<c:if test="${questionEntry.key == weightsEntry.key}"> 			
													  			<input type="text" name="questionWeight<c:out value="${queIndex}"/>" value="<c:out value="${weightsEntry.value}"/>"
														  		size="3" maxlength="3">
														</c:if>
												</c:forEach>
												 <bean:message key="label.percent"/> 
	 											 &nbsp&nbsp&nbsp&nbsp
                                            </td>

											                                                   
                                            <td class="input" valign=top>								
                   								    <img src="images/add.gif" align=left onclick="javascript:document.forms[0].addQuestion.value=1; document.forms[0].submit();">		  	
											</td>	
											
											&nbsp&nbsp&nbsp
                                            <td class="input" valign=top>								
												  <img src="images/edit.gif" align=left onclick="javascript:document.forms[0].questionIndex.value=<c:out value="${queIndex}"/>; document.forms[0].editOptions.value=1; document.forms[0].submit();">		  	
											</td>
										  </tr>
									</c:if> 			
									
							  		<c:if test="${questionEntry.key > 1}"> 			
										<c:set var="queIndex" scope="session" value="${queIndex +1}"/>
										  <tr>
										  	<td class="formlabel" valign=top> <c:out value="Question ${queIndex}"/> : </td>
										  	
										  		<td class="input" valign=top> 
										  			<input type="text" name="questionContent<c:out value="${queIndex}"/>" value="<c:out value="${questionEntry.value}"/>"  size="50" maxlength="255" > 
												</td>											

	                                            <td class="input" align=center valign=top>										  	
		                                            &nbsp&nbsp
													<c:forEach var="weightsEntry" items="${sessionScope.mapWeights}">                                            
															<c:if test="${questionEntry.key == weightsEntry.key}"> 			
														  			<input type="text" name="questionWeight<c:out value="${queIndex}"/>" value="<c:out value="${weightsEntry.value}"/>"
															  		size="3" maxlength="3">
															</c:if>
													</c:forEach>
												 <bean:message key="label.percent"/> 
	 											 &nbsp&nbsp&nbsp&nbsp
	                                            </td>
											
                                               <td class="input" valign=top>								
													<img src="images/delete.gif" align=left onclick="javascript:document.forms[0].questionIndex.value=<c:out value="${queIndex}"/>; document.forms[0].removeQuestion.value=1; document.forms[0].submit();">                                               		  	
												</td>
												&nbsp&nbsp&nbsp
	                                            <td class="input" valign=top>							
												    <img src="images/edit.gif" align=left onclick="javascript:document.forms[0].questionIndex.value=<c:out value="${queIndex}"/>; document.forms[0].editOptions.value=1; document.forms[0].submit();">	                                            			  	
											  	</td>
										  </tr>
									</c:if> 
						</c:forEach>
							<html:hidden property="questionIndex"/>
							<html:hidden property="addQuestion"/>
							<html:hidden property="editOptions"/>
							<html:hidden property="removeQuestion"/>

					<tr>
	 				 	<td colspan=5 align=center>
							&nbsp&nbsp
					  	</td>
					</tr>		
					
					<tr>
					  	<td class="formlabel" valign=top><bean:message key="radiobox.passmark"/>: </td>
						<td class="input" valign=top> 
							  <html:text property="passmark"  size="3" maxlength="3"/>							
						</td>
						<td valign=top> &nbsp</td> <td valign=top>&nbsp </td>
					</tr>
					
					<tr>
	 				 	<td colspan=5 align=center valign=top>
							&nbsp&nbsp
					  	</td>
					</tr>
						
					<tr>
	 				 	<td colspan=5 align=center valign=top>								
							&nbsp&nbsp
					  	</td>
					</tr>

	 				 <tr>
	 				 	<td colspan=5 align=left valign=top>								
							<html:submit property="submitQuestions" styleClass="linkbutton" 
							onmouseover="pviiClassNew(this,'linkbutton')" onmouseout="pviiClassNew(this,'linkbutton')">
								<bean:message key="button.submit"/>
							</html:submit>	 				 		  										  		
		 			  	</td>
	 				 </tr>
							
					</table>		
					</td> </tr>
							

	 				</table> 	 
