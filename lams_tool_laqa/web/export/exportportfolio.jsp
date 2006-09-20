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

	<html:html locale="true">
	<head>
	<title> <bean:message key="label.exportPortfolio"/> </title>
	<lams:css localLinkPath="../"/>
	</head>
	<body>

    <html:form  action="/monitoring?validate=false" enctype="multipart/form-data" method="POST" target="_self">		
		<html:hidden property="method"/>
		<html:hidden property="toolContentID"/>
	
		<div id="page-learner"><!--main box 'page'-->
	
			<h1 class="no-tabs-below">
			<c:if test="${(userExceptionNoToolSessions != 'true') }"> 	
				<c:if test="${(portfolioExportMode == 'learner')}"><bean:message key="label.export.learner"/></c:if>			
				<c:if test="${(portfolioExportMode != 'learner')}"><bean:message key="label.export.teacher"/> </h1></c:if>			
			</c:if>
	        </h1>
			<div id="header-no-tabs-learner">
		
			</div><!--closes header-->
		
			<div id="content-learner">
		
			
				<c:if test="${(userExceptionNoToolSessions == 'true')}"> 	
						<table align="center">
							<tr> 
								<td NOWRAP valign=top align=center> 
									<b>  <bean:message key="error.noLearnerActivity"/> </b>
								</td> 
							<tr>
						</table>
				</c:if>			
		
		
		<c:if test="${(userExceptionNoToolSessions != 'true') }"> 	
			<table width="80%" cellspacing="8" class="forms">
			
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
				  						 <td NOWRAP valign=top> <b>  <bean:message key="label.learning.attemptTime"/> </b></td>
				  						 <td NOWRAP valign=top> <b>  <bean:message key="label.response"/>  	</b></td>
						  			</tr>				 
		  							<c:forEach var="questionAttemptData" items="${currentDto.questionAttempts}">
										<c:forEach var="sData" items="${questionAttemptData.value}">
								  	 		<c:set var="userData" scope="request" value="${sData.value}"/>
								  	 		<c:set var="responseUid" scope="request" value="${userData.uid}"/>

	  	 									<c:if test="${currentQuestionId == userData.questionUid}"> 			
			  	 									<tr>
														 <td NOWRAP valign=top>   <c:out value="${userData.userName}"/>   </td>  
														 <td NOWRAP valign=top>    <lams:Date value="${userData.attemptTime}"/>  </td>														 
														 <td NOWRAP valign=top>   <c:out value="${userData.responsePresentable}" escapeXml="false"/>  </td>
													</tr>
											</c:if>														  					 
	 									</c:forEach>		  	
									</c:forEach>		  	
								</table>
							</td>  
			  			</tr>
					</c:forEach>		  	

						<tr>			
							<td valign=top align=left>
								&nbsp
							</td>
						</tr>

					
						<tr>			
							<td valign=top align=left>
								<table align=center>
								
										<tr>			
											<td colspan=3 valign=top align=center>
												<b>  <bean:message key="label.reflection"/>  </b> 
											 </td>
										</tr>	
								
								
									<c:forEach var="currentDto" items="${reflectionsContainerDTO}">
							  	 		<c:set var="userName" scope="request" value="${currentDto.userName}"/>
							  	 		<c:set var="userId" scope="request" value="${currentDto.userId}"/>
							  	 		<c:set var="sessionId" scope="request" value="${currentDto.sessionId}"/>
							  	 		<c:set var="reflectionUid" scope="request" value="${currentDto.reflectionUid}"/>
							  	 		<c:set var="entry" scope="request" value="${currentDto.entry}"/>
										<tr>			
											<td valign=top align=left>
												  <b>  <bean:message key="label.user"/>:  </b>
											 </td>

											<td valign=top align=left>
	 												  <c:out value="${userName}" escapeXml="false"/> 
											 </td>

											<td valign=top align=left>
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
		
		
			<div id="footer-learner">
			</div><!--closes footer-->
		
		</div><!--closes page-->
	
		</html:form>

	</body>
</html:html>



