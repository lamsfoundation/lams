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

<c:set var="lams">
	<lams:LAMSURL />
</c:set>
<c:set var="tool">
	<lams:WebAppURL />
</c:set>

<html:html>
<head>
	<html:base />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8">
	<lams:css/>
	<title><fmt:message key="activity.title" /></title>

	<script language="JavaScript" type="text/JavaScript">

		function submitLearningMethod(actionMethod) 
		{
			document.QaLearningForm.method.value=actionMethod; 
			document.QaLearningForm.submit();
		}
		
		function submitMethod(actionMethod) 
		{
			submitLearningMethod(actionMethod);
		}
		
		
	</script>		
</head>

<body class="stripes">
	
	
	

	<div id="content">
	<h1>
		<c:out value="${generalLearnerFlowDTO.activityTitle}" escapeXml="false" />
	</h1>

		<c:if test="${generalLearnerFlowDTO.requestLearningReportProgress != 'true'}"> 			

		       	<table> 	  
					<tr> <td>
						<b> <fmt:message key="label.other.answers"/> </b>
					 </td>
					</tr>
				</table>				

		
			  <html:form  action="/learning?validate=false" enctype="multipart/form-data" method="POST" target="_self">		
		  		<html:hidden property="method"/>	 
				<html:hidden property="toolSessionID"/>		
				<html:hidden property="userID"/>										
				<html:hidden property="httpSessionID"/>		
				<html:hidden property="totalQuestionCount"/>		

					<c:forEach var="currentDto" items="${generalLearnerFlowDTO.listMonitoredAnswersContainerDTO}">
			  	 		<c:set var="currentQuestionId" scope="request" value="${currentDto.questionUid}"/>
			  	 		<tr>
			  	 			<td> &nbsp</td>
			  	 		</tr>
	
						<tr>			
							<td valign=top align=left><b>  <fmt:message key="label.question"/> : </b> 
								<c:out value="${currentDto.question}" escapeXml="false"/>
							 </td>
						</tr>	

						
						<tr> 
							<td valign=top>
								<table cellpadding="0" class="alternative-color">
		  							<c:forEach var="questionAttemptData" items="${currentDto.questionAttempts}">
										<c:forEach var="sData" items="${questionAttemptData.value}">
								  	 		<c:set var="userData" scope="request" value="${sData.value}"/>
								  	 		<c:set var="responseUid" scope="request" value="${userData.uid}"/>

	  	 									<c:if test="${currentQuestionId == userData.questionUid}"> 			
													<tr> 
														 <td NOWRAP valign=top>   <c:out value="${userData.userName}"/>   </td>  
														 <td NOWRAP valign=top>   <lams:Date value="${userData.attemptTime}"/> </td>
													</tr>
													<tr> 															
													 	<td colspan=2 valign=top>   <c:out value="${userData.responsePresentable}" escapeXml="false"/>  </td>														 
													</tr>													 
											</c:if>														  					 
	 									</c:forEach>		  	
									</c:forEach>		  	
								</table>
							</td>  
			  			</tr>
					</c:forEach>		  	


				<c:if test="${generalLearnerFlowDTO.notebookEntriesVisible != 'false'}"> 			
						<tr> 
						<td valign=top>
								<table>
									<tr> 
					  	   		  		<td>
											<b> <fmt:message key="label.notebook.entries"/> </b>						
										 </td>
									</tr>
									
									<tr> 
					  	   		  		<td>
											<c:out value="${generalLearnerFlowDTO.notebookEntry}" escapeXml="false"/>				  	   		  		
										 </td>
									</tr>
							</table>
	
						</td>
						</tr>
				</c:if>														  					 					


				<c:if test="${generalLearnerFlowDTO.requestLearningReportViewOnly != 'true' }"> 
					<c:if test="${generalLearnerFlowDTO.teacherViewOnly != 'true' }"> 				
				       	<table> 	  
								<tr> 
		  	   		  		<td>
	                               <html:button property="refreshAnswers" 
	                                             styleClass="button" 
	                                             onclick="submitMethod('viewAllResults');">
	                                    <fmt:message key="label.refresh"/>
	                                </html:button>
						  	 </td>
	
								<td  valign=top>
									<div class="right-buttons">							
										<c:if test="${generalLearnerFlowDTO.reflection != 'true'}"> 						  			  		
											<html:button property="endLearning"  onclick="javascript:submitMethod('endLearning');" styleClass="button">
												<fmt:message key="button.endLearning"/>
											</html:button>	 				
									  	</c:if> 				    					
						
										<c:if test="${generalLearnerFlowDTO.reflection == 'true'}"> 						  			  		
											<html:button property="forwardtoReflection" onclick="javascript:submitMethod('forwardtoReflection');" styleClass="button">
												<fmt:message key="label.continue"/>
											</html:button>	 				
									  	</c:if> 				    					
									</div> 		  															 		  												  	
								</td> 
								
								</tr>
						</table>
				  	</c:if> 				    				  					
			  	</c:if> 				    				  
		
		
			</html:form>
		</c:if> 				    
		
		<c:if test="${generalLearnerFlowDTO.requestLearningReportProgress == 'true'}"> 			
		       	<table> 	  
					<tr> <td>
						<b> <fmt:message key="label.learnerReport"/> </b>
					 </td>
					</tr>
				</table>						 
		
			  <html:form  action="/learning?validate=false" enctype="multipart/form-data" method="POST" target="_self">		
		  		<html:hidden property="method"/>	 
				<html:hidden property="toolSessionID"/>						
				<html:hidden property="userID"/>														
				<html:hidden property="httpSessionID"/>		
				<html:hidden property="totalQuestionCount"/>		
						
				<c:forEach var="currentDto" items="${generalLearnerFlowDTO.listMonitoredAnswersContainerDTO}">
			  	 		<c:set var="currentQuestionId" scope="request" value="${currentDto.questionUid}"/>
			  	 		<tr>
			  	 			<td> &nbsp</td>
			  	 		</tr>
						<tr>			
							<td valign=top align=left><b>  <fmt:message key="label.question"/> : </b> 
								<c:out value="${currentDto.question}" escapeXml="false"/>
							 </td>
						</tr>	

						
						<tr> 
							<td valign=top>
								<table cellpadding="0" class="alternative-color">
		  							<c:forEach var="questionAttemptData" items="${currentDto.questionAttempts}">
										<c:forEach var="sData" items="${questionAttemptData.value}">
								  	 		<c:set var="userData" scope="request" value="${sData.value}"/>
								  	 		<c:set var="responseUid" scope="request" value="${userData.uid}"/>

	  	 									<c:if test="${currentQuestionId == userData.questionUid}"> 			
													<tr> 
														 <td NOWRAP valign=top>   <c:out value="${userData.userName}"/>   </td>  
 														 <td NOWRAP valign=top>   <lams:Date value="${userData.attemptTime}"/> </td>
													</tr>
													<tr>
														 <td colspan=2 valign=top>  <c:out value="${userData.responsePresentable}" escapeXml="false"/>  </td>														 
													</tr>															
											</c:if>														  					 
	 									</c:forEach>		  	
									</c:forEach>		  	
								</table>
							</td>  
			  			</tr>
					</c:forEach>		  	
					

				<c:if test="${generalLearnerFlowDTO.notebookEntriesVisible != 'false'}"> 			
						<tr> 
						<td valign=top>
								<table>
									<tr> 
					  	   		  		<td>
											<b> <fmt:message key="label.notebook.entries"/> </b>						
										 </td>
									</tr>
									
									<tr> 
					  	   		  		<td>
											<c:out value="${generalLearnerFlowDTO.notebookEntry}" escapeXml="false"/>				  	   		  		
										 </td>
									</tr>
							</table>
	
						</td>
						</tr>
				</c:if>														  					 					
					


				<c:if test="${generalLearnerFlowDTO.requestLearningReportViewOnly != 'true' }"> 								
					<c:if test="${generalLearnerFlowDTO.teacherViewOnly != 'true' }"> 								
				       	<table> 	  
							<tr> 
		  	   		  		<td>
	                               <html:button property="refreshAnswers" 
	                                             styleClass="button" 
	                                             onclick="submitMethod('viewAllResults');">
	                                    <fmt:message key="label.refresh"/>
	                                </html:button>
						  	 </td>
							
								<td>
									<div class="right-buttons">
										<c:if test="${generalLearnerFlowDTO.reflection != 'true'}"> 						  			  		
											<html:button property="endLearning"  onclick="javascript:submitMethod('endLearning');" styleClass="button">
												<fmt:message key="button.endLearning"/>
											</html:button>	 				
									  	</c:if> 				    					
						
										<c:if test="${generalLearnerFlowDTO.reflection == 'true'}"> 						  			  		
											<html:button property="forwardtoReflection" onclick="javascript:submitMethod('forwardtoReflection');" styleClass="button">
												<fmt:message key="label.continue"/>
											</html:button>	 				
									  	</c:if> 				    					
									</div> 		  															 		  					
									</td> 
								</tr>
						</table>
					</c:if> 				    					
				</c:if> 				    

			</html:form>
		</c:if> 				    

	</div>
	
	<div id="footer"></div>
	

</body>
</html:html>









	
	