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
  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
  USA

  http://www.gnu.org/licenses/gpl.txt
--%>

<%@ taglib uri="tags-bean" prefix="bean"%> 
<%@ taglib uri="tags-html" prefix="html"%>
<%@ taglib uri="tags-logic" prefix="logic" %>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="fck-editor" prefix="FCK" %>
<%@ taglib uri="tags-lams" prefix="lams" %>


			    	<table align=center> 	  
						<tr>   
						<td NOWRAP class=error>
							<c:if test="${qaGeneralAuthoringDTO.sbmtSuccess == '1'}"> 			
								<img src="images/success.gif" align="left" width=20 height=20>  <bean:message key="submit.successful"/>  </img>
							</c:if> 			
						</td>
						</tr> 

						<tr>   
						<td NOWRAP class=error>
							<c:if test="${qaGeneralAuthoringDTO.userExceptionQuestionsDuplicate == 'true'}"> 			
								<img src="images/error.jpg" align="left" width=20 height=20>  <bean:message key="error.questions.duplicate"/>  </img>
							</c:if> 			
						</td>
						</tr> 
					</table>


					<table class="forms">
						<tr> 
							<td colspan=2>							
								<lams:SetEditor id="title" text="${qaGeneralAuthoringDTO.activityTitle}" small="true" key="label.authoring.title.col"/>								
							</td> 
							
					  	</tr>

					  	<tr> 
							<td colspan=2>							
								<lams:SetEditor id="instructions" text="${qaGeneralAuthoringDTO.activityInstructions}"  key="label.authoring.instructions.col"/>								
							</td> 
						</tr>
				
			 		<!--default question content, this entry can not be deleted but can be updated -->
				 		<tr> 
							<td colspan=2>							
								<lams:SetEditor id="questionContent0" text="${qaGeneralAuthoringDTO.defaultQuestionContent}"  key="label.question.col"/>								
							</td> 
					  	</tr>
		
				  	<!--end of default question content -->
				  	
			  		<!-- if there is more than just the default content start presenting them -->
			  	 		<c:set var="queIndex" scope="request" value="1"/>
						<c:forEach var="questionEntry" items="${qaGeneralAuthoringDTO.mapQuestionContent}">
					  		<c:if test="${questionEntry.key > 1}"> 			
								<c:set var="queIndex" scope="request" value="${queIndex +1}"/>
								  <tr>
									<td colspan=2>							

										<lams:SetEditor id="questionContent${queIndex-1}" text="${questionEntry.value}"  key="label.question.col"/>								
                                
	                                	<c:if test="${ (qaGeneralAuthoringDTO.activeModule != 'monitoring') }"> 			
											<img src="<c:out value="${tool}"/>images/delete.gif" align=left onclick="removeQuestion(${queIndex});">												
										</c:if> 			
										
	                                	<c:if test="${ (qaGeneralAuthoringDTO.activeModule == 'monitoring') }"> 			
											<img src="<c:out value="${tool}"/>images/delete.gif" align=left onclick="removeMonitoringQuestion(${queIndex});">																							
										</c:if> 													
										
                                    </td>
								  </tr>
							</c:if> 			
						</c:forEach>

						<html:hidden property="questionIndex"/>
                        
                        <tr>
                            <td></td>
                            <td align="right">
                                <html:submit property="addContent" 
                                             styleClass="linkbutton" 
                                             onclick="submitMethod('addNewQuestion');">
                                    <bean:message key="label.add.question"/>
                                </html:submit>
                            </td>
                        </tr>
					
					</table>

					<table>
					    <tr> <td> &nbsp </td> </tr>					    
						<tr> 
							<td>							
						      	<c:if test="${qaGeneralAuthoringDTO.activeModule != 'authoring'}"> 					
									<p align="right">
									    <a href="javascript:submitMethod('submitAllContent')" class="button">
								        	<bean:message key="label.save"/></a>
									</p>
								</c:if> 					
					
					
						      	<c:if test="${qaGeneralAuthoringDTO.activeModule == 'authoring'}"> 					
									<c:set var="formBean" value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />
									<lams:AuthoringButton formID="authoringForm" clearSessionActionUrl="/clearsession.do" toolSignature="laqa11" 
									cancelButtonLabelKey="label.cancel" saveButtonLabelKey="label.save" toolContentID="${formBean.toolContentID}" />
								</c:if> 						      	
							</td> 
					  	</tr>
				 </table>
	      				

<SCRIPT language="JavaScript"> 

	function removeQuestion(questionIndex)
	{
		document.QaAuthoringForm.questionIndex.value=questionIndex;
        submitMethod('removeQuestion');
	}

	function removeMonitoringQuestion(questionIndex)
	{
		document.QaMonitoringForm.questionIndex.value=questionIndex;
        submitMonitoringMethod('removeQuestion');
	}
	
 </SCRIPT>

		