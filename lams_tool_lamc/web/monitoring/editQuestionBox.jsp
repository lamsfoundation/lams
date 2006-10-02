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
			
		</script>		
	</head>
	
	<body>
		<table cellpadding="0">

			<html:form  action="/monitoring?validate=false" styleId="newQuestionForm" enctype="multipart/form-data" method="POST">			
			<html:hidden property="dispatch" value="saveSingleQuestion"/>
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
						<div class="field-name" style="text-align: left;">
							<fmt:message key="label.edit.question"></fmt:message>
						</div>
						
						<lams:FCKEditor id="newQuestion"
							value="${mcGeneralAuthoringDTO.editableQuestionText}"
							contentFolderID="${mcGeneralAuthoringDTO.contentFolderID}"></lams:FCKEditor>
					</td>
				</tr>


				<tr>
					<td>
						<%@ include file="/monitoring/candidateAnswersList.jsp"%>					
					</td>
				</tr>
				
				<tr>
					<td>
						<html:submit onclick="javascript:submitMethod('newCandidateBox');" style="float:right;width:150px"  styleClass="button-add-item">
			            	<bean:message key="label.add.candidates" />
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
						<div class="field-name" style="text-align: left;">
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
								<a href="#" onclick="getElementById('newQuestionForm').submit();" class="button-add-item">
									<bean:message key="label.save.question" />
								</a>
							</td> 
	
							<td>
								<a href="#" onclick="javascript:window.parent.hideMessage()" class="button">
									<bean:message key="label.cancel" />
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
