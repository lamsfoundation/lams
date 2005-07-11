<%@ taglib uri="/WEB-INF/struts-html-el.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic-el.tld" prefix="logic-el" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<%@ taglib uri="fck-editor" prefix="FCK" %>

<div id="basicTabContainer">	
		<c:set var="formIndex" scope="session" value="${requestScope.formIndex}"/>
		<c:if test="${(requestScope.startMonitoringSummaryRequest != 'true')  || (sessionScope.editActivityEditMode=='true') }"> 			
			<c:if test="${requestScope.stopRenderingQuestions != 'true'}"> 			
			<tr> <td>
					<table>
						<tr> 
					 		<td> <bean:message key="label.authoring.title"/>: </td>
					 		<td NOWRAP width=700>
							<FCK:editor id="richTextTitle" basePath="/lams/fckEditor/"
							      height="200"
								  width="100%">
								  <c:out value="${sessionScope.richTextTitle}" escapeXml="false" />						  
							</FCK:editor>
							</td> 
					  	</tr>
					  	<tr> 
					 		<td> <bean:message key="label.authoring.instructions"/>: </td>
							<td NOWRAP width=700>
							<FCK:editor id="richTextInstructions" basePath="/lams/fckEditor/"
							      height="200"
								  width="100%">
								  <c:out value="${sessionScope.richTextInstructions}" escapeXml="false" />						  
							</FCK:editor>
							</td>
						</tr>
				
			 		<!--default question content, this entry can not be deleted but can be updated -->
				 		<tr> 
						  	<td> 
						 		<bean:message key="label.question1"/> : 
						 	</td>
						  	<td>
								  	<input type="text" name="questionContent0" size="50" 
								  	maxlength="255" value="<c:out value="${sessionScope.defaultQuestionContent}"/>"> 
								  	&nbsp
									<html:submit property="addContent" styleClass="linkbutton" disabled="${sessionScope.isDefineLater}"
									onmouseover="pviiClassNew(this,'linkbutton')" onmouseout="pviiClassNew(this,'linkbutton')">
									<bean:message key="button.addNewQuestion"/>
									</html:submit>
						  	</td>
					  	</tr>
		
				  	<!--end of default question content -->
				  	
			  		<!-- if there is more than just the default content start presenting them -->
			  	 		<c:set var="queIndex" scope="session" value="1"/>
						<c:forEach var="questionEntry" items="${sessionScope.mapQuestionContent}">
					  		<c:if test="${questionEntry.key > 1}"> 			
								<c:set var="queIndex" scope="session" value="${queIndex +1}"/>
								  <tr>
								  	<td> <c:out value="Question ${queIndex}"/> : </td>
								  		<td> <input type="text" name="questionContent<c:out value="${queIndex-1}"/>" value="<c:out value="${questionEntry.value}"/>"   
									  		size="50" maxlength="255"> 
									  	&nbsp
		 								<html:submit property="removeContent" styleClass="linkbutton"  onclick="removeQuestion(${formIndex}, ${queIndex});"
		 								disabled="${sessionScope.isDefineLater}" onmouseover="pviiClassNew(this,'linkbutton')" onmouseout="pviiClassNew(this,'linkbutton')">
											<bean:message key="button.removeQuestion"/>
										</html:submit>
									  	</td>
								  </tr>
							</c:if> 			
						</c:forEach>
						<html:hidden property="toolContentId" value="${sessionScope.toolContentId}"/>
						<html:hidden property="questionIndex"/>
						<html:hidden property="isRemoveContent"/>
					</tr>
					</table>
		
					<hr>
					<table>
							<tr>
									 <td> 
										 <html:submit property="submitAllContent" styleClass="linkbutton" onmouseover="pviiClassNew(this,'linkbutton')" onmouseout="pviiClassNew(this,'linkbutton')">
											<bean:message key="button.submitAllContent"/>
										</html:submit>
									</td> 
							</tr>
					</table>
			</td></tr>			
			</c:if>	
		</c:if>	
	
		
		<c:if test="${sessionScope.editActivityEditMode=='false'}"> 
			<c:if test="${requestScope.startMonitoringSummaryRequest == 'true'}"> 
			<c:if test="${requestScope.stopRenderingQuestions != 'true'}"> 			
			<tr> <td>
					<table>
						<tr> 
					 		<td> <bean:message key="label.authoring.title"/>: </td>
					 		<td NOWRAP width=700>
							<FCK:editor id="richTextTitle" basePath="/lams/fckEditor/"
							      height="200"
								  width="100%">
								  <c:out value="${sessionScope.richTextTitle}" escapeXml="false" />						  
							</FCK:editor>
							</td> 
					  	</tr>

					  	<tr> 
					 		<td> <bean:message key="label.authoring.instructions"/>: </td>
							<td NOWRAP width=700>
							<FCK:editor id="richTextInstructions" basePath="/lams/fckEditor/"
							      height="200"
								  width="100%">
								  <c:out value="${sessionScope.richTextInstructions}" escapeXml="false" />						  
							</FCK:editor>
							</td>
						</tr>
				
			 		<!--default question content, this entry can not be deleted but can be updated -->
		
				 		<tr> 
						  	<td> 
						  		<c:out value="Question 1"/> : </td>
						  		<td>
									<c:out value="${sessionScope.defaultQuestionContent}"/>
						  		</td>
					  	</tr>
		
				  	<!--end of default question content -->
				  	
			  		<!-- if there is more than just the default content start presenting them -->
			  	 		<c:set var="queIndex" scope="session" value="1"/>
						<c:forEach var="questionEntry" items="${sessionScope.mapQuestionContent}">
					  		<c:if test="${questionEntry.key > 1}"> 			
								<c:set var="queIndex" scope="session" value="${queIndex +1}"/>
								  <tr>
								  	<td> <c:out value="Question ${queIndex}"/> : </td>
								  		<td> <c:out value="${questionEntry.value}"/>
									  	&nbsp
									  	</td>
								  </tr>
							</c:if> 			
						</c:forEach>
						<html:hidden property="toolContentId" value="${sessionScope.toolContentId}"/>
						<html:hidden property="questionIndex"/>
						<html:hidden property="isRemoveContent"/>
					</table>
		
					<hr>
					<table>
							<tr>
									 <td> 
										 <html:submit property="edit" styleClass="linkbutton" onmouseover="pviiClassNew(this,'linkbutton')" onmouseout="pviiClassNew(this,'linkbutton')">
											<bean:message key="label.edit"/>
										</html:submit>
									</td> 
							</tr>
					</table>
			</td></tr>			
			</c:if>	
			</c:if>	
		</c:if>	
</div>	

<SCRIPT language="JavaScript"> 

<!--  addQuestion gets called everytime a new question content is added to the UI -->
	
	function removeQuestion(formIndex, questionIndex)
	{
		document.forms[formIndex].questionIndex.value=questionIndex;
		document.forms[formIndex].isRemoveQuestion.value='1';
		document.forms[formIndex].submit();
	}
	
 </SCRIPT>

		