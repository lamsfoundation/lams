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
  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
  USA

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
	<lams:headItems />
	<title><bean:message key="activity.title" /></title>

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

<body>
	<div id="page-learner">
	
	<h1 class="no-tabs-below">
		<c:out value="${generalLearnerFlowDTO.activityTitle}" escapeXml="false" />
	</h1>

	<div id="header-no-tabs-learner"></div>
	

	<div id="content-learner">
		<c:if test="${generalLearnerFlowDTO.requestLearningReportProgress != 'true'}"> 			
			<c:if test="${ generalLearnerFlowDTO.requestLearningReportViewOnly != 'true'}"> 			
		     	<table class="forms"> 	  
					<tr> <th scope="col">
						 <c:out value="${generalLearnerFlowDTO.reportTitleLearner}" escapeXml="false"/> 
					 </th>
					</tr>
				</table>		
			</c:if> 				    
		
			<c:if test="${generalLearnerFlowDTO.requestLearningReportViewOnly == 'true'}"> 			
		       	<table class="forms"> 	  
					<tr> <th scope="col">
						<bean:message key="label.learning.viewOnly"/>
					 </th>
					</tr>
				</table>				
			</c:if> 				    
		
				<c:set var="monitoringURL">
					<html:rewrite page="/monitoring.do" />
				</c:set>
		
			  <html:form  action="/learning?validate=false" enctype="multipart/form-data" method="POST" target="_self">		
		  		<html:hidden property="method"/>	 
				<html:hidden property="toolSessionID"/>						
				<html:hidden property="httpSessionID"/>		
				<html:hidden property="totalQuestionCount"/>		

					<c:forEach var="currentDto" items="${generalLearnerFlowDTO.listMonitoredAnswersContainerDTO}">
			  	 		<c:set var="currentQuestionId" scope="request" value="${currentDto.questionUid}"/>
			  	 		<tr>
			  	 			<td> &nbsp&nbsp&nbsp</td>
			  	 		</tr>
						<tr>			
							<td NOWRAP valign=top align=left><b>  <bean:message key="label.question"/> : </b> 
								<c:out value="${currentDto.question}" escapeXml="false"/>
							 </td>
						</tr>	
						
						<tr> 
							<td NOWRAP valign=top>
								<table align=center>
									<tr> 
										 <td NOWRAP valign=top> <b>  <bean:message key="label.user"/>  </b> </td>  
				  						 <td NOWRAP valign=top> <b>  <bean:message key="label.attemptTime"/> </b></td>
				  						 <td NOWRAP valign=top> <b>  <bean:message key="label.response"/> 	</b></td>
						  			</tr>				 
		  							<c:forEach var="questionAttemptData" items="${currentDto.questionAttempts}">
										<c:forEach var="sData" items="${questionAttemptData.value}">
								  	 		<c:set var="userData" scope="request" value="${sData.value}"/>
								  	 		<c:set var="responseUid" scope="request" value="${userData.uid}"/>

	  	 									<c:if test="${currentQuestionId == userData.questionUid}"> 			
		  	 									<c:if test="${requestScope.currentMonitoredToolSession == 'All'}"> 			
													<tr> 
														 <td NOWRAP valign=top>   <c:out value="${userData.userName}"/>   </td>  
														 <td NOWRAP valign=top>   <c:out value="${userData.attemptTime}"/> </td>
														 <td NOWRAP valign=top>   <c:out value="${userData.response}"/>  </td>
													</tr>															
												</c:if>														  					 									  			
												
		  	 									<c:if test="${requestScope.currentMonitoredToolSession != 'All'}"> 			
		  	 										<c:if test="${requestScope.currentMonitoredToolSession == userData.sessionId}"> 			
														<tr> 
															 <td NOWRAP valign=top>   <c:out value="${userData.userName}"/>   </td>  
															 <td NOWRAP valign=top>   <c:out value="${userData.attemptTime}"/> </td>
															 <td NOWRAP valign=top>   <c:out value="${userData.response}"/>  </td>
														</tr>															
													</c:if>														  					 									  													  			
												</c:if>														  					 									  													  			
											</c:if>														  					 
	 									</c:forEach>		  	
									</c:forEach>		  	
								</table>
							</td>  
			  			</tr>
					</c:forEach>		  	


		

			       	<table> 	  
							<tr> <td  valign=top>
								<div class="right-buttons">

									<html:submit onclick="javascript:submitMethod('endLearning');" styleClass="button">
										<bean:message key="button.endLearning"/>
									</html:submit>	 				
								</div> 		  															 		  					
								</td> 
							</tr>
					</table>

		
		
			</html:form>
		</c:if> 				    
		
		<c:if test="${generalLearnerFlowDTO.requestLearningReportProgress == 'true'}"> 			
		       	<table class="forms"> 	  
					<tr> <th scope="col">
						<bean:message key="label.learner.progress"/>
					 </th>
					</tr>
				</table>						 
		
				<c:set var="monitoringURL">
					<html:rewrite page="/monitoring.do" />
				</c:set>
		
			  <html:form  action="/learning?validate=false" enctype="multipart/form-data" method="POST" target="_self">		
		  		<html:hidden property="method"/>	 
				<html:hidden property="toolSessionID"/>						
				<html:hidden property="httpSessionID"/>		
				<html:hidden property="totalQuestionCount"/>		
						
				<c:forEach var="currentDto" items="${generalLearnerFlowDTO.listMonitoredAnswersContainerDTO}">
			  	 		<c:set var="currentQuestionId" scope="request" value="${currentDto.questionUid}"/>
			  	 		<tr>
			  	 			<td> &nbsp&nbsp&nbsp</td>
			  	 		</tr>
						<tr>			
							<td NOWRAP valign=top align=left><b>  <bean:message key="label.question"/> : </b> 
								<c:out value="${currentDto.question}" escapeXml="false"/>
							 </td>
						</tr>	
						
						<tr> 
							<td NOWRAP valign=top>
								<table align=center>
									<tr> 
										 <td NOWRAP valign=top> <b>  <bean:message key="label.user"/>  </b> </td>  
				  						 <td NOWRAP valign=top> <b>  <bean:message key="label.attemptTime"/> </b></td>
				  						 <td NOWRAP valign=top> <b>  <bean:message key="label.response"/> 	</b></td>
						  			</tr>				 
		  							<c:forEach var="questionAttemptData" items="${currentDto.questionAttempts}">
										<c:forEach var="sData" items="${questionAttemptData.value}">
								  	 		<c:set var="userData" scope="request" value="${sData.value}"/>
								  	 		<c:set var="responseUid" scope="request" value="${userData.uid}"/>

	  	 									<c:if test="${currentQuestionId == userData.questionUid}"> 			
		  	 									<c:if test="${requestScope.currentMonitoredToolSession == 'All'}"> 			
													<tr> 
														 <td NOWRAP valign=top>   <c:out value="${userData.userName}"/>   </td>  
														 <td NOWRAP valign=top>   <c:out value="${userData.attemptTime}"/> </td>
														 <td NOWRAP valign=top>   <c:out value="${userData.response}"/>  </td>
													</tr>															
												</c:if>														  					 									  			
												
		  	 									<c:if test="${requestScope.currentMonitoredToolSession != 'All'}"> 			
		  	 										<c:if test="${requestScope.currentMonitoredToolSession == userData.sessionId}"> 			
														<tr> 
															 <td NOWRAP valign=top>   <c:out value="${userData.userName}"/>   </td>  
															 <td NOWRAP valign=top>   <c:out value="${userData.attemptTime}"/> </td>
															 <td NOWRAP valign=top>   <c:out value="${userData.response}"/>  </td>
														</tr>															
													</c:if>														  					 									  													  			
												</c:if>														  					 									  													  			
											</c:if>														  					 
	 									</c:forEach>		  	
									</c:forEach>		  	
								</table>
							</td>  
			  			</tr>
					</c:forEach>		  	

			</html:form>
		</c:if> 				    

	</div>
	

<div id="footer-learner"></div>	

</div>
</body>
</html:html>









	
	