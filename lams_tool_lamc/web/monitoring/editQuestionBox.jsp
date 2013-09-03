<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>
<lams:html>
	<lams:head>
		<%@ include file="/common/header.jsp"%>
		
		<script language="JavaScript" type="text/JavaScript">

			function submitMethod() {
				document.McMonitoringForm.submit();
			}
			
			function submitMethod(actionMethod) {
				document.McMonitoringForm.dispatch.value=actionMethod; 
				document.McMonitoringForm.submit();
			}
			
			
			function submitModifyMonitoringCandidate(questionIndexValue, candidateIndexValue, actionMethod) 
			{
				document.McMonitoringForm.candidateIndex.value=candidateIndexValue; 
				document.McMonitoringForm.questionIndex.value=questionIndexValue; 
				submitMethod(actionMethod);
			}

			function removeCandidate(questionIndexValue, candidateIndexValue) 
			{
				document.McMonitoringForm.candidateIndex.value=candidateIndexValue; 
				document.McMonitoringForm.questionIndex.value=questionIndexValue; 
				submitMethod("removeCandidate");
			}

			function validateDuplicateCorrectAnswers() 
			{
				var correctCount = 0;
				for(i = 1; i < 51; i++)
				{
					var currentId="select".concat(i)
					var currentField=document.getElementById(currentId);
					
					if (currentField != null)
					{
						if ((typeof(currentField) != 'undefined') && (typeof(currentField) != null))
						{
							if (currentField.value == 'Correct')
							{
								correctCount = correctCount + 1;
							}
						}
					}
				}
				
				if (correctCount > 1)
				{
					var msg = "<fmt:message key="candidates.duplicate.correct"/>";
					alert(msg);
					return false;
				}
				return true;
			}

			function validateSingleCorrectAnswer() 
			{
				var singleCorrectEntry = 0;
				var radioCorrect=document.McMonitoringForm.correct;

				if ((radioCorrect == 'null') || (radioCorrect == 'undefined'))
				{
					var msg = "<fmt:message key="candidates.groupSize.warning"/>";
					alert(msg);
					return false;				
				}

				var radioGroupSize=radioCorrect.length;
				
				if ((radioGroupSize == 'undefined') || (radioGroupSize < 2))				
				{
					var msg = "<fmt:message key="candidates.groupSize.warning"/>";
					alert(msg);
					return false;				
				}
				
				
				for(i = 0; i < 51; i++)
				{
					if (radioCorrect[i] != null)
					{
						if ((typeof(radioCorrect[i]) != 'undefined') && (typeof(radioCorrect[i]) != null))
						{
							if (radioCorrect[i].checked) 
							{
								singleCorrectEntry =1;
							}
						}
					}
				}
				
				if (singleCorrectEntry == 0)
				{
					var msg = "<fmt:message key="candidates.none.correct"/>";
					var msgSetFirst = "<fmt:message key="candidates.setFirst"/>";
					alert(msg);

					if ((radioCorrect[0] != 'undefined') && (radioCorrect[0] != null))
					{
						radioCorrect[0].checked=true;					
						alert(msgSetFirst);						
					}

					return false;
				}
				return true;
			}


			function validateMinumumCandidateCount() 
			{
				var radioCorrect=document.McMonitoringForm.correct;

				if ((radioCorrect == 'undefined') || (radioCorrect == null))
				{
					var msg = "<fmt:message key="candidates.unremovable.groupSize"/>";
					alert(msg);
					return false;				
				}

				
				var radioGroupSize=radioCorrect.length;
				
				if ((radioGroupSize == 'undefined') || (radioGroupSize <= 2))				
				{
					var msg = "<fmt:message key="candidates.unremovable.groupSize"/>";
					alert(msg);
					return false;				
				}
				
				return true;
			}
			
			
		</script>		
	</lams:head>
	
	<body>
	
		<table cellpadding="0">

			<html:form  action="/monitoring?validate=false" styleId="newQuestionForm" enctype="multipart/form-data" method="POST">			
			<html:hidden property="dispatch" value="saveSingleQuestion"/>
			<html:hidden property="currentField" />			
			<html:hidden property="toolContentID"/>
			<html:hidden property="currentTab" styleId="currentTab" />
			<html:hidden property="activeModule"/>
			<html:hidden property="httpSessionID"/>								
			<html:hidden property="defaultContentIdStr"/>								
			<html:hidden property="defineLaterInEditMode"/>										
			<html:hidden property="contentFolderID"/>														
			<html:hidden property="editableQuestionIndex"/>														
			<html:hidden property="editQuestionBoxRequest" value="true"/>																	
			
			
			<tr>
			<td>
			<table class="innerforms">
		
				<tr>
					<td>
						<div class="field-name">
							<fmt:message key="label.edit.question"></fmt:message>
						</div>
						
						<lams:CKEditor id="newQuestion"
							value="${mcGeneralAuthoringDTO.editableQuestionText}"
							contentFolderID="${mcGeneralAuthoringDTO.contentFolderID}"></lams:CKEditor>
					</td>
				</tr>


				<tr>
					<td>
						<%@ include file="/monitoring/candidateAnswersList.jsp"%>					
					</td>
				</tr>
				
				<tr>
					<td>
						<html:submit onclick="javascript:submitMethod('newCandidateBox');" style="width:150px"  styleClass="button-add-item float-right">
			            	<fmt:message key="label.add.candidates" />
						</html:submit>	 				 		  											
					</td>
				</tr>

				
				<tr>
					<td>
						<fmt:message key="label.questions.worth"></fmt:message> 


									<select name="mark">
										<c:forEach var="markEntry" items="${mcGeneralAuthoringDTO.marksMap}">
											<c:set var="SELECTED_MARK" scope="request" value=""/>
											<c:if test="${markEntry.value == mcGeneralAuthoringDTO.markValue}"> 			
														<c:set var="SELECTED_MARK" scope="request" value="SELECTED"/>
											</c:if>						

												<option value="<c:out value="${markEntry.value}"/>"  <c:out value="${SELECTED_MARK}"/>> <c:out value="${markEntry.value}"/>  </option>																				
										</c:forEach>		  	
									</select>


						<fmt:message key="label.marks"></fmt:message> 
					</td>
				</tr>

				

				<tr>
					<td>
						<div class="field-name">
							<fmt:message key="label.feedback"></fmt:message>
						</div>
						<html:textarea property="feedback" rows="3" cols="70"></html:textarea>							
					</td>
				</tr>
			</html:form>
			
		<tr><td>
		<lams:ImgButtonWrapper>
			<a href="#" onclick="if (validateSingleCorrectAnswer()) { getElementById('newQuestionForm').submit(); }"
				class="button-add-item"> <fmt:message key="label.add.new.question" />
			</a>
			<a href="#" onclick="javascript:window.parent.hideMessage()" class="button space-left">
				<fmt:message key="label.cancel" />
			</a>
		</lams:ImgButtonWrapper>
		</td></tr>	
		</table>
	</body>
</lams:html>