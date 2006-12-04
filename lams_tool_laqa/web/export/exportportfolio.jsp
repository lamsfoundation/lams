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

	<lams:html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title> <fmt:message key="label.exportPortfolio"/> </title>
		<lams:css localLinkPath="../"/>
	</head>
	<body class="stripes">

    <html:form  action="/monitoring?validate=false" enctype="multipart/form-data" method="POST" target="_self">		
		<html:hidden property="method"/>
		<html:hidden property="toolContentID"/>
	
		
			<div id="content">
		
			<h1>
			<c:if test="${(userExceptionNoToolSessions != 'true') }"> 	
				<c:if test="${(portfolioExportMode == 'learner')}"><fmt:message key="label.export.learner"/></c:if>			
				<c:if test="${(portfolioExportMode != 'learner')}"><fmt:message key="label.export.teacher"/> </h1></c:if>			
			</c:if>
	        </h1>
			
				<c:if test="${(userExceptionNoToolSessions == 'true')}"> 	
						<table align="center">
							<tr> 
								<td NOWRAP valign=top align=center> 
									<b>  <fmt:message key="error.noLearnerActivity"/> </b>
								</td> 
							<tr>
						</table>
				</c:if>			
		
		
		<c:if test="${(userExceptionNoToolSessions != 'true') }"> 	

			<table width="80%" cellspacing="8" class="forms">

			<c:if test="${(portfolioExportMode == 'learner')}">
					<c:forEach var="currentDto" items="${generalLearnerFlowDTO.listMonitoredAnswersContainerDTO}"> 	 
					       <c:set var="currentQuestionId" scope="request" value="${currentDto.questionUid}"/>
			
					<tr>
						<td> &nbsp&nbsp&nbsp</td>
					</tr>
			
					   <tr>
						<td NOWRAP valign=top class="align-left"><b>  <fmt:message key="label.question"/> : </b>
							<c:out value="${currentDto.question}" escapeXml="false"/>
						</td>
					   </tr>
			
					  <tr>
						<td NOWRAP valign=top>
						<table align=center>
						<tr>
							<td NOWRAP valign=top> <b>  <fmt:message key="label.user"/>  </b> </td>
							<td NOWRAP valign=top> <b>  <fmt:message key="label.learning.attemptTime"/> </b></td> 	 
							<td NOWRAP valign=top> <b>  <fmt:message key="label.response"/>        </b></td> 	 
						</tr>
			
			
						  <c:forEach var="questionAttemptData"items="${currentDto.questionAttempts}">
							  <c:forEach var="sData" items="${questionAttemptData.value}">
							    <c:set var="userData" scope="request" value="${sData.value}"/>
							     <c:set var="responseUid" scope="request" value="${userData.uid}"/>
			
									<c:if test="${currentQuestionId == userData.questionUid}">
									    <tr>
										<td NOWRAP valign=top>
											<c:out value="${userData.userName}"/>   </td>
										<td NOWRAP valign=top> 
											<lams:Date value="${userData.attemptTime}"/>  </td>
										<td NOWRAP valign=top>
											<c:out value="${userData.responsePresentable}" escapeXml="false"/>  </td>
									     </tr>
									 </c:if>
							</c:forEach>
						</c:forEach>
					     </table>
					     </td>
					</tr>
					</c:forEach>
			</c:if>	


			<c:if test="${(portfolioExportMode != 'learner')}">

					<c:forEach var="groupDto" items="${listAllGroupsDTO}">
			  	 		<c:set var="sessionId" scope="request" value="${groupDto.sessionId}"/>
			  	 		<c:set var="sessionName" scope="request" value="${groupDto.sessionName}"/>
			  	 		<c:set var="groupData" scope="request" value="${groupDto.groupData}"/>
			  	 		
			  	 		<tr>
			  	 			<td> <b> <fmt:message key="group.label"/> : </b> <c:out value="${sessionName}"/> </td>
			  	 		</tr>
			  	 		
									  	 		
			  	 		
			  	 		
					<c:forEach var="currentDto" items="${groupData}">
			  	 		<c:set var="currentQuestionId" scope="request" value="${currentDto.questionUid}"/>

			  	 		
						<tr>			
							<td valign=top class="align-left"><b>  <fmt:message key="label.question"/> : </b> 
								<c:out value="${currentDto.question}" escapeXml="false"/>
							 </td>
						</tr>	
						
						<tr> 
							<td valign=top>
								<table align=center>

		  							<c:forEach var="questionAttemptData" items="${currentDto.questionAttempts}">
										<c:forEach var="sData" items="${questionAttemptData.value}">
								  	 		<c:set var="userData" scope="request" value="${sData.value}"/>
								  	 		<c:set var="responseUid" scope="request" value="${userData.uid}"/>
								  	 		<c:set var="userSessionId" scope="request" value="${userData.sessionId}"/>
								  	 		
								  	 		
	  	 									<c:if test="${sessionId == userSessionId}"> 											  	 		
		  	 									<c:if test="${currentQuestionId == userData.questionUid}"> 			

															<tr> 
																 <td  valign=top>  <c:out value="${userData.userName}"/>   </td>  
																 <td  valign=top>   <lams:Date value="${userData.attemptTime}"/> </td>														 
															</tr>	 
															
															<tr>
																 <td colspan=2  valign=top>   
																	 <c:out value="${userData.responsePresentable}" escapeXml="false"/> 
																 </td>
															</tr>
												</c:if>														  					 									  													  			
											</c:if>														  					 									  													  														
 									</c:forEach>		
	 									
								  	 		<tr>
								  	 			<td> &nbsp&nbsp&nbsp</td>
								  	 		</tr>
	 									  	
									</c:forEach>		  	
								</table>
							</td>  
			  			</tr>
					</c:forEach>		  	
				</c:forEach>		  	
		</c:if>			

						<tr>			
							<td valign=top class="align-left">
								&nbsp
							</td>
						</tr>

					
						<tr>			
							<td valign=top class="align-left">
								<table align=center>
								
										<tr>			
											<td colspan=3 valign=top align=center>
												<b>  <fmt:message key="label.reflection"/>  </b> 
											 </td>
										</tr>	
								
								
									<c:forEach var="currentDto" items="${reflectionsContainerDTO}">
							  	 		<c:set var="userName" scope="request" value="${currentDto.userName}"/>
							  	 		<c:set var="userId" scope="request" value="${currentDto.userId}"/>
							  	 		<c:set var="sessionId" scope="request" value="${currentDto.sessionId}"/>
							  	 		<c:set var="reflectionUid" scope="request" value="${currentDto.reflectionUid}"/>
							  	 		<c:set var="entry" scope="request" value="${currentDto.entry}"/>
										<tr>			
											<td valign=top class="align-left">
												  <b>  <fmt:message key="label.user"/>:  </b>
											 </td>

											<td valign=top class="align-left">
	 												  <c:out value="${userName}" escapeXml="false"/> 
											 </td>

											<td valign=top class="align-left">
												 <c:out value="${entry}" escapeXml="false"/> 
											 </td>
										</tr>	
									</c:forEach>		
								</table>  	
							 </td>
						</tr>	

					
			</table>			
			
		</c:if>					

		</div>  <!--closes content-->
		
		
			<div id="footer">
			</div><!--closes footer-->
		
		</html:form>

	</body>
</lams:html>



