<%@ taglib uri="tags-html-el" prefix="html" %>
<%@ taglib uri="tags-bean" prefix="bean" %>
<%@ taglib uri="tags-logic-el" prefix="logic-el" %>
<%@ taglib uri="tags-c" prefix="c" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="fck-editor" prefix="FCK" %>

<script language="JavaScript" type="text/JavaScript">
<!--
// move up/down, edit, remove question 
// questionIndexValue: index of question affected
// actionMethod: name of the method to be called in the DispatchAction
function submitModifyQuestion(questionIndexValue, actionMethod) {
	document.McAuthoringForm.questionIndex.value=questionIndexValue; 
	submitMethod(actionMethod);
}
//-->     
</script>
					<table class="forms">
					
 						<tr> 
					 		<td NOWRAP class="formlabel" valign=top> <bean:message key="label.authoring.title"/>: </td>
							<td NOWRAP class="formcontrol" valign=top>
								<FCK:editor id="richTextTitle" basePath="/lams/fckeditor/">
									  <c:out value="${sessionScope.richTextTitle}" escapeXml="false" />						  
								</FCK:editor>
							</td> 
					  	</tr>
					  	
					  	<tr> 
					 		<td NOWRAP class="formlabel" valign=top> <bean:message key="label.authoring.instructions"/>: </td>
							<td NOWRAP class="formcontrol" valign=top>
								<FCK:editor id="richTextInstructions" basePath="/lams/fckeditor/">
									  <c:out value="${sessionScope.richTextInstructions}" escapeXml="false" />						  
								</FCK:editor>
							</td>
						</tr>
				
					<tr>
					 	<td NOWRAP colspan=2 align=center valign=top>								

					  	</td>
					</tr>
				
					<tr> <td NOWRAP colspan=2 align=center valign=top>
				
					<table align="center" border="1" summary="layout and Content" width="55%">
					     <tr>
								  	<td NOWRAP bgcolor="#A8C7DE" colspan=5 class="input" valign=top align=left>
									  	 <font size=2> <b> <bean:message key="label.mc.questions"/> </b> </font>
								  	</td>
						 </tr>					
					
			  	 		<c:set var="queIndex" scope="session" value="0"/>
						<c:forEach var="questionEntry" items="${sessionScope.mapQuestionsContent}">
							<c:set var="queIndex" scope="session" value="${queIndex +1}"/>
							
							  <tr>
							  	<td NOWRAP bgcolor="#EEEEEE" class="input" valign=top>  <font size=2> <b> <c:out value="Question ${queIndex}"/> : </b>  </font> </td>
							  	
						  		<td NOWRAP bgcolor="#EEEEEE" class="input" valign=top width=50> 
						  			<input type="text" name="questionContent<c:out value="${queIndex}"/>" value="<c:out value="${questionEntry.value}"/>"
							  		size="50" maxlength="255" >
							  	</td>

							  	<td NOWRAP bgcolor="#EEEEEE" class="input"  align=center valign=top>			
							 		<c:if test="${sessionScope.queIndex == 1}"> 		
	   								    <img src="images/down.gif" align=left onclick="javascript:submitModifyQuestion('<c:out value="${queIndex}"/>','moveQuestionDown');">
	   								</c:if> 			
	
					  				<c:if test="${sessionScope.maxQuestionIndex == sessionScope.queIndex}"> 			
	     								 <img src="images/up.gif" align=left onclick="javascript:submitModifyQuestion('<c:out value="${queIndex}"/>','moveQuestionUp');">		  	
	     							</c:if> 	    
									
	 				  				<c:if test="${(sessionScope.maxQuestionIndex != sessionScope.queIndex) && (sessionScope.queIndex != 1)}"> 			
	   								    <img src="images/down.gif" align=left onclick="javascript:submitModifyQuestion('<c:out value="${queIndex}"/>','moveQuestionDown');">		  	   								 
	       								<img src="images/up.gif" align=left onclick="javascript:submitModifyQuestion('<c:out value="${queIndex}"/>','moveQuestionUp');">		  	
									</c:if> 	           								 
                                </td>

                                    <td NOWRAP bgcolor="#EEEEEE" class="input" align=left valign=top>										  	
										<c:forEach var="weightsEntry" items="${sessionScope.mapWeights}">                                            
												<c:if test="${questionEntry.key == weightsEntry.key}"> 			
											  			<input type="text" name="questionWeight<c:out value="${queIndex}"/>" value="<c:out value="${weightsEntry.value}"/>"
												  		size="3" maxlength="3">
												  		<bean:message key="label.percent"/>
												</c:if>
										</c:forEach>
                                    </td>
																					
                                   <td NOWRAP bgcolor="#EEEEEE" class="input" valign=top>								
										<img src="images/edit.gif" align=left onclick="javascript:submitModifyQuestion('<c:out value="${queIndex}"/>','editOptions');">	
										<img src="images/delete.gif" align=left onclick="javascript:submitModifyQuestion('<c:out value="${queIndex}"/>','removeQuestion');">	
								  	</td>
							  </tr>
						</c:forEach>
						
						 <tr>
							  	<td NOWRAP bgcolor="#EEEEEE" colspan=5 class="input" valign=top align=right>
									<html:submit styleClass="a.button" onclick="javascript:submitMethod('addNewQuestion');">
										<bean:message key="label.add.question"/>
									</html:submit>	 				 		  										  		
						  	</td>
						 </tr>							
						
						
					</table> </td> </tr>
							<html:hidden property="questionIndex"/>
					<tr>
	 				 	<td colspan=5 align=center>

					  	</td>
					</tr>		
					
					<tr>
					  	<td NOWRAP class="formlabel" valign=top><bean:message key="radiobox.passmark"/>: </td>
						<td NOWRAP class="input" valign=top> 
							  <html:text property="passmark"  size="3" maxlength="3"/><bean:message key="label.percent"/>
						</td>
						<td NOWRAP valign=top> </td> 
						<td NOWRAP valign=top> </td>
					</tr>
					
					<tr>
	 				 	<td NOWRAP colspan=5 align=center valign=top>
					  	</td>
					</tr>
						
					<tr>
	 				 	<td NOWRAP colspan=5 align=center valign=top>								
					  	</td>
					</tr>

	 				 <tr>
 						<td NOWRAP valign=top> </td> 
						<td NOWRAP class="input" colspan=3 align=left valign=top>								
							<html:submit onclick="javascript:submitMethod('submitQuestions');" styleClass="a.button">
								<bean:message key="button.submit"/>
							</html:submit>	 				 		  					
												  		
		 			  	</td>
						<td NOWRAP valign=top> </td> 
						<td NOWRAP valign=top> </td>
					</tr>
	 				</table> 	 
