<%@ taglib uri="/WEB-INF/struts-html-el.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic-el.tld" prefix="logic-el" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<%@ taglib uri="fck-editor" prefix="FCK" %>
 
<div id="basicTabContainer">	
		<c:if test="${(requestScope.startMonitoringSummaryRequest != 'true')  || (sessionScope.editActivityEditMode=='true') }"> 			
			<c:if test="${requestScope.stopRenderingQuestions != 'true'}"> 			
			<tr> <td>
					<table align=center> 
						<tr> 
					 		<td valign="top"><font size=2> <b> <bean:message key="label.authoring.title"/>: </b></font> </td>
					 		<td NOWRAP width=700> <!-- Dave,I found width was necessary to present all the elements of the editor, feel free to change -->                            
                                
                                 <span id="previewTitle" style="visibility: hidden; display: none;">
                                     <div>
                                         <a href="javascript:doWYSWYGEdit('Title','small')"><font size=2> <b> Open Richtext Editor </a>
                                     </div>
                                     <div class="smallPreviewPanel" id="previewTitle.text"></div>
                                 </span>
                                 <span id="txTitle">
                                     <div>
                                         <a href="javascript:doTextToHTML('Title'); doWYSWYGEdit('Title','small')"><font size=2> <b> Open Richtext Editor</a>
                                     </div>
                                     <textarea class="smallTextareaPanel" name="title" id="txTitle.textarea"><c:out value="${QaAuthoringForm.title}" escapeXml="false" /></textarea>
                                 </span>
                                
							</td> 
					  	</tr>
					  	<tr> 
					 		<td valign="top"><font size=2> <b> <bean:message key="label.authoring.instructions"/>:  </b></font></td>
					 		<td NOWRAP width=700> <!-- Dave,I found width was necessary to present all the elements of the editor, feel free to change -->
                                <span id="previewInstructions" style="visibility: hidden; display: none;">
                                    <div>
                                        <a href="javascript:doWYSWYGEdit('Instructions')"><font size=2> <b> Open Richtext Editor</a>
                                    </div>
                                    <div class="previewPanel" id="previewInstructions.text"></div>
                                </span>
                                <span id="txInstructions">
                                    <div>
                                        <a href="javascript:doTextToHTML('Instructions'); doWYSWYGEdit('Instructions')"><font size=2> <b>Open Richtext Editor</a>
                                    </div>
                                    <textarea class="textareaPanel" name="instructions" id="txInstructions.textarea"><c:out value="${QaAuthoringForm.instructions}" escapeXml="false" /></textarea>
                                </span>
							</td>
						</tr>
				
			 		<!--default question content, this entry can not be deleted but can be updated -->
				 		<tr> 
						  	<td valign="top"> 
						 		<font size=2> <b> <bean:message key="label.question1"/> :  </b></font>
						 	</td>
						  	<td>
                                <span id="previewQuestion0" style="visibility: hidden; display: none;">
                                    <div>
                                        <a href="javascript:doWYSWYGEdit('Question0')">Open Richtext Editor</a>
                                    </div>
                                    <div class="previewPanel" id="previewQuestion0.text"></div>
                                </span>
                                <span id="txQuestion0">
                                    <div>
                                        <a href="javascript:doTextToHTML('Question0'); doWYSWYGEdit('Question0')"><font size=2> <b>Open Richtext Editor</a>
                                    </div>
                                    <textarea class="textareaPanel" name="questionContent0" id="txQuestion0.textarea"><c:out value="${sessionScope.defaultQuestionContent}"/></textarea>
                                </span>
						  	</td>
					  	</tr>
		
				  	<!--end of default question content -->
				  	
			  		<!-- if there is more than just the default content start presenting them -->
			  	 		<c:set var="queIndex" scope="session" value="1"/>
						<c:forEach var="questionEntry" items="${sessionScope.mapQuestionContent}">
					  		<c:if test="${questionEntry.key > 1}"> 			
								<c:set var="queIndex" scope="session" value="${queIndex +1}"/>
								  <tr>
								  	<td valign="top"> <font size=2> <b> <c:out value="Question ${queIndex}"/> :  </b></font></td>
								  	<td>
  
                                        <span id="preview<c:out value="Question${queIndex-1}"/>" style="visibility: hidden; display: none;">
                                            <div>
                                                <a href="javascript:doWYSWYGEdit('<c:out value="Question${queIndex-1}"/>')"><font size=2> <b>Open Richtext Editor</a>
                                            </div>
                                            <div class="previewPanel" id="preview<c:out value="Question${queIndex-1}"/>.text"></div>
                                        </span>
                                        <span id="tx<c:out value="Question${queIndex-1}"/>">
                                            <div>
                                                <a href="javascript:doTextToHTML('<c:out value="Question${queIndex-1}"/>'); doWYSWYGEdit('<c:out value="Question${queIndex-1}"/>')"><font size=2> <b>Open Richtext Editor</a>
                                            </div>
                                            <textarea class="textareaPanel" name="<c:out value="questionContent${queIndex-1}"/>" id="tx<c:out value="Question${queIndex-1}"/>.textarea"><c:out value="${questionEntry.value}"/></textarea>
                                        </span>
                                
		 		 						<html:submit property="removeContent" 
                                                     styleClass="linkbutton"  
                                                     onclick="removeQuestion(${queIndex});"
                                                     disabled="${sessionScope.isDefineLater}" 
                                                     onmouseover="pviiClassNew(this,'linkbutton')" 
                                                     onmouseout="pviiClassNew(this,'linkbutton')">
											<bean:message key="button.removeQuestion"/>
										</html:submit>
                                    </td>
								  </tr>
							</c:if> 			
						</c:forEach>
						<html:hidden property="toolContentId" value="${QaAuthoringForm.toolContentId}"/>
						<html:hidden property="questionIndex"/>
                        
                        <tr>
                            <td></td>
                            <td align="right">
                                <html:submit property="addContent" 
                                             styleClass="linkbutton" 
                                             disabled="${sessionScope.isDefineLater}"
                                             onmouseover="pviiClassNew(this,'linkbutton')" 
                                             onmouseout="pviiClassNew(this,'linkbutton')"
                                             onclick="submitMethod('addNewQuestion');">
                                    <bean:message key="button.addNewQuestion"/>
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
					<table align=center>
						<tr> 
					 		<td> <font size=2> <b> <bean:message key="label.authoring.title"/>:  </b></font></td>
					 		<td NOWRAP width=700> <!-- Dave,I found width was necessary to present all the elements of the editor, feel free to change -->
						 		<c:out value="${sessionScope.richTextTitle}" escapeXml="false" />
							</td> 
					  	</tr>

					  	<tr> 
					 		<td> <font size=2> <b> <bean:message key="label.authoring.instructions"/>:  </b></font></td> 
					 		<td NOWRAP width=700> <!-- Dave,I found width was necessary to present all the elements of the editor, feel free to change -->
							  <c:out value="${sessionScope.richTextInstructions}" escapeXml="false" />						  
							</td>
						</tr>
				
			 		<!--default question content, this entry can not be deleted but can be updated -->
		
				 		<tr> 
						  	<td> <font size=2> <b>
						  		<c:out value="Question 1"/> :  </b></font></td>
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
								  	<td> <font size=2> <b><c:out value="Question ${queIndex}"/> :  </b></font></td>
								  		<td> <c:out value="${questionEntry.value}"/>
									  	&nbsp
									  	</td>
								  </tr>
							</c:if> 			
						</c:forEach>
						<html:hidden property="toolContentId" value="${sessionScope.toolContentID}"/>
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
	
	function removeQuestion(questionIndex)
	{
		document.QaAuthoringForm.questionIndex.value=questionIndex;
		//document.QaAuthoringForm.isRemoveQuestion.value='1';
        submitMethod('removeQuestion');
		//document.forms[formIndex].submit();
	}
	
 </SCRIPT>

		