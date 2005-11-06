<%@ taglib uri="/WEB-INF/struts-html-el.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic-el.tld" prefix="logic-el" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<%@ taglib uri="fck-editor" prefix="FCK" %>

					<table class="forms">
					
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

					  	</td>
					</tr>
				
					<tr> <td colspan=2 align=center valign=top>
					


				<!--
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
				-->
				
					<table align="center" border="1" cellspacing="2" cellpadding="2" summary="layout and Content">
			  	 		<c:set var="queIndex" scope="session" value="1"/>
						<c:forEach var="questionEntry" items="${sessionScope.mapQuestionsContent}">
						
									<c:if test="${questionEntry.key == 1}"> 			
									    <tr>
										  	<td bgcolor="#EEEEEE" class="input" valign=top> <font size=2> <b> <c:out value="Question ${queIndex}"/> : </b> </font>  </td>
										  	
									  		<td bgcolor="#EEEEEE" class="input" valign=top> 
									  			<input type="text" name="questionContent<c:out value="${queIndex}"/>" value="<c:out value="${questionEntry.value}"/>"
										  		size="50" maxlength="255" >
										  	</td>
										  	
										  	<td  bgcolor="#EEEEEE" class="input" align=center valign=top>										  	
                   								    <img src="images/down.gif" align=left onclick="javascript:document.forms[0].moveDown.value=1; document.forms[0].questionIndex.value=<c:out value="${queIndex}"/>; document.forms[0].submit();">		  	
                                            </td>
										  	
                                            <td bgcolor="#EEEEEE" class="input" align=left valign=top>										  	
												<c:forEach var="weightsEntry" items="${sessionScope.mapWeights}">                                            
														<c:if test="${questionEntry.key == weightsEntry.key}"> 			
													  			<input type="text" name="questionWeight<c:out value="${queIndex}"/>" value="<c:out value="${weightsEntry.value}"/>" 
														  		size="3" maxlength="3"><bean:message key="label.percent"/> 
														</c:if>
												</c:forEach>
                                            </td>

                                            <td bgcolor="#EEEEEE" class="input" valign=top>								
												    <img src="images/edit.gif" align=left onclick="javascript:document.forms[0].questionIndex.value=<c:out value="${queIndex}"/>; document.forms[0].editOptions.value=1; document.forms[0].submit();">		  	
                   								    <img src="images/add.gif" align=left onclick="javascript:document.forms[0].addQuestion.value=1; document.forms[0].submit();">		  													    
											</td>
										  </tr>
									</c:if> 			
									
									
							  		<c:if test="${questionEntry.key > 1}"> 			
										<c:set var="queIndex" scope="session" value="${queIndex +1}"/>
										
										  <tr>
										  	<td bgcolor="#EEEEEE" class="input" valign=top>  <font size=2> <b> <c:out value="Question ${queIndex}"/> : </b>  </font> </td>
										  	
									  		<td bgcolor="#EEEEEE" class="input" valign=top> 
									  			<input type="text" name="questionContent<c:out value="${queIndex}"/>" value="<c:out value="${questionEntry.value}"/>"
										  		size="50" maxlength="255" >
										  	</td>

										  	<td bgcolor="#EEEEEE" class="input" align=center valign=top>										  	

							  				<c:if test="${sessionScope.maxQuestionIndex != sessionScope.queIndex}"> 			
               								    <img src="images/down.gif" align=left onclick="javascript:document.forms[0].moveDown.value=1; document.forms[0].questionIndex.value=<c:out value="${queIndex}"/>; document.forms[0].submit();">		  	
											</c:if> 	    
                 								 <img src="images/up.gif" align=left onclick="javascript:document.forms[0].moveUp.value=1; document.forms[0].questionIndex.value=<c:out value="${queIndex}"/>; document.forms[0].submit();">		  	
                                            </td>

	                                            <td bgcolor="#EEEEEE" class="input" align=left valign=top>										  	
													<c:forEach var="weightsEntry" items="${sessionScope.mapWeights}">                                            
															<c:if test="${questionEntry.key == weightsEntry.key}"> 			
														  			<input type="text" name="questionWeight<c:out value="${queIndex}"/>" value="<c:out value="${weightsEntry.value}"/>"
															  		size="3" maxlength="3"><bean:message key="label.percent"/>
															</c:if>
													</c:forEach>
	                                            </td>
																								
                                               <td bgcolor="#EEEEEE" class="input" valign=top>								
													<img src="images/edit.gif" align=left onclick="javascript:document.forms[0].questionIndex.value=<c:out value="${queIndex}"/>; document.forms[0].editOptions.value=1; document.forms[0].submit();">	                                            			  	
													<img src="images/delete.gif" align=left onclick="javascript:document.forms[0].questionIndex.value=<c:out value="${queIndex}"/>; document.forms[0].removeQuestion.value=1; document.forms[0].submit();">                                               		  														
											  	</td>
										  </tr>
									</c:if> 
						</c:forEach>
						
					</table> </td> </tr>
							<html:hidden property="questionIndex"/>
							<html:hidden property="addQuestion"/>
							<html:hidden property="editOptions"/>
							<html:hidden property="removeQuestion"/>
							<html:hidden property="moveDown"/>							
							<html:hidden property="moveUp"/>														
					<tr>
	 				 	<td colspan=5 align=center>

					  	</td>
					</tr>		
					
					<tr>
					  	<td class="formlabel" valign=top><bean:message key="radiobox.passmark"/>: </td>
						<td class="input" valign=top> 
							  <html:text property="passmark"  size="3" maxlength="3"/>							
						</td>
						<td valign=top> </td> 
						<td valign=top> </td>
					</tr>
					
					<tr>
	 				 	<td colspan=5 align=center valign=top>

					  	</td>
					</tr>
						
					<tr>
	 				 	<td colspan=5 align=center valign=top>								

					  	</td>
					</tr>

	 				 <tr>
 						<td valign=top> </td> 
						<td class="input" colspan=3 align=left valign=top>								
							<html:submit property="submitQuestions" styleClass="linkbutton" 
							onmouseover="pviiClassNew(this,'linkbutton')" onmouseout="pviiClassNew(this,'linkbutton')">
								<bean:message key="button.submit"/>
							</html:submit>	 				 		  										  		
		 			  	</td>
						<td valign=top> </td> 
						<td valign=top> </td>
					</tr>
	 				 
	 				</table> 	 
