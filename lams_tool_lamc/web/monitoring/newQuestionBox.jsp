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
  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA

  http://www.gnu.org/licenses/gpl.txt
--%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>
<html>
	<head>
		<%@ include file="/common/learnerheader.jsp"%>
		
		<script language="JavaScript" type="text/JavaScript">

			function submitMethod() {
				document.McMonitoringForm.submit();
			}
			
			function submitMethod(actionMethod) {
				document.McMonitoringForm.dispatch.value=actionMethod; 
				document.McMonitoringForm.submit();
			}
			

			function submitModifyAddedCandidate(candidateIndexValue, actionMethod) 
			{
				document.McMonitoringForm.candidateIndex.value=candidateIndexValue; 
				submitMethod(actionMethod);
			}

			function removeAddedCandidate(candidateIndexValue) 
			{
				document.McMonitoringForm.candidateIndex.value=candidateIndexValue; 
				submitMethod("removeAddedCandidate");
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
				}
			}
			
		</script>		
	</head>
	
	<body>
		<table cellpadding="0">
	

			<html:form  action="/monitoring?validate=false" styleId="newQuestionForm" enctype="multipart/form-data" method="POST">				
			<html:hidden property="dispatch" value="addSingleQuestion"/>
			<html:hidden property="currentField" />
			<html:hidden property="toolContentID"/>
			<html:hidden property="currentTab" styleId="currentTab" />
			<html:hidden property="activeModule"/>
			<html:hidden property="httpSessionID"/>								
			<html:hidden property="defaultContentIdStr"/>								
			<html:hidden property="defineLaterInEditMode"/>										
			<html:hidden property="contentFolderID"/>														
			<html:hidden property="editQuestionBoxRequest" value="false"/>																				
			
			<tr>
			<td>
			<table class="innerforms">
		
				<tr>
					<td>
						<div class="field-name">
							<fmt:message key="label.add.new.question"></fmt:message>
						</div>
						<lams:FCKEditor id="newQuestion"
							value="${mcGeneralAuthoringDTO.editableQuestionText}"
							contentFolderID="${mcGeneralAuthoringDTO.contentFolderID}"></lams:FCKEditor>
					</td>
				</tr>
				
				
				<tr>
					<td>
						<%@ include file="/monitoring/candidateAnswersAddList.jsp"%>					
					</td>
				</tr>
				
				<tr>
					<td>
						<html:submit onclick="javascript:submitMethod('newAddedCandidateBox');" style="float:right;width:150px"  styleClass="button-add-item">
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

				
				
				

				<tr>
					<td align="center" valign="bottom">
						<table>
							<tr>
							<td> &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
								<a href="#" onclick="validateDuplicateCorrectAnswers(); getElementById('newQuestionForm').submit();" class="button-add-item">
									<fmt:message key="label.save.question" />
								</a>
							</td> 
	
							<td>
								<a href="#" onclick="javascript:window.parent.hideMessage()" class="button">
									<fmt:message key="label.cancel" />
								</a>
							</td> 	
							</tr>					
						</table>
					</td>
				</tr>



			</table>				
			</td>
			</tr>
				
			</html:form>
		</table>

	</body>
</html>