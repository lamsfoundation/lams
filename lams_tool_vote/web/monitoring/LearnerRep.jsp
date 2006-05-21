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


<%@ taglib uri="tags-bean" prefix="bean"%> 
<%@ taglib uri="tags-html" prefix="html"%>
<%@ taglib uri="tags-logic" prefix="logic" %>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="fck-editor" prefix="FCK" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

<c:set var="lams"><lams:LAMSURL/></c:set>
<c:set var="tool"><lams:WebAppURL/></c:set>

	<!DOCTYPE HTML PUBLIC "-//W3C//DTD hTML 4.01 Transitional//EN">
	<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
	
	<html:html locale="true">
	<head>
	<meta http-equiv="content-type" content="text/html; charset=UTF-8">
	<title> <bean:message key="label.learning.report"/> </title>
	
	 <lams:css/>
	
	<!-- depending on user / site preference this will get changed probably use passed in variable from flash to select which one to use-->

 	<!-- ******************** FCK Editor related javascript & HTML ********************** -->
    <script type="text/javascript" src="${lams}fckeditor/fckeditor.js"></script>
    <script type="text/javascript" src="${lams}includes/javascript/fckcontroller.js"></script>
    <link href="${lams}css/fckeditor_style.css" rel="stylesheet" type="text/css">
	
	
	<script language="JavaScript" type="text/JavaScript">

		// general submit
		// actionMethod: name of the method to be called in the DispatchAction
		function submitMonitoringMethod(actionMethod) 
		{
			document.VoteMonitoringForm.method.value=actionMethod; 
			document.VoteMonitoringForm.submit();
		}
		
		function submitMethod(actionMethod) 
		{
			submitMonitoringMethod(actionMethod);
		}
		
		//-->
	</script>	
</head>
<body>

		<table width="80%" cellspacing="8" align="CENTER" class="forms">
				<tr>
			 		<th NOWRAP>  <bean:message key="label.learner.progress"/>   </th>
				</tr>
		</table>

		<c:set var="monitoringURL">
			<html:rewrite page="/monitoring.do" />
		</c:set>

	  <html:form  action="/monitoring?validate=false" enctype="multipart/form-data" method="POST" target="_self">		
		<html:hidden property="method"/>	 

			<div class="tabbody content_b" >
			
				<table width="80%" cellspacing="8" align="CENTER" class="forms">
					<tr>
						<td NOWRAP valign=top align=left>
			 			<table align=left>
			 			
						<c:forEach var="currentDto" items="${sessionScope.listMonitoredAnswersContainerDto}">
				  	 		<c:set var="currentQuestionId" scope="request" value="${currentDto.questionUid}"/>
				  	 		<tr>
				  	 			<td> &nbsp&nbsp</td>
				  	 		</tr>
							<tr>			
								<td NOWRAP valign=top align=left><b> <font size=2> <bean:message key="label.nomination"/>: </b>
									<c:out value="${currentDto.question}" escapeXml="false"/>
								</font> </td>
							</tr>	
							
							<tr> 
								<td NOWRAP class="formlabel" valign=top>
									<table align=center>
										<tr> 
											 <td NOWRAP valign=top> <b> <font size=2> <bean:message key="label.user"/> </font> </b> </td>  
					  						 <td NOWRAP valign=top> <b> <font size=2> <bean:message key="label.attemptTime"/></font> </b></td>
							  			</tr>				 
			  							<c:forEach var="questionAttemptData" items="${currentDto.questionAttempts}">
											<c:forEach var="sData" items="${questionAttemptData.value}">
									  	 		<c:set var="userData" scope="request" value="${sData.value}"/>
									  	 		<c:set var="responseUid" scope="request" value="${userData.uid}"/>
	
		  	 									<c:if test="${currentQuestionId == userData.questionUid}">
			  	 									<c:if test="${sessionScope.currentMonitoredToolSession == 'All'}"> 			
																<jsp:include page="/monitoring/UserResponses.jsp" />
													</c:if>														  					 									  			
													
			  	 									<c:if test="${sessionScope.currentMonitoredToolSession != 'All'}"> 			
			  	 										<c:if test="${sessionScope.currentMonitoredToolSession == userData.sessionId}"> 			
																<jsp:include page="/monitoring/UserResponses.jsp" />										
														</c:if>														  					 									  													  			
													</c:if>														  					 									  													  			
												</c:if>														  					 
		 									</c:forEach>		  	
										</c:forEach>		  	
									</table>
								</td>  
				  			</tr>
						</c:forEach>		  
							
						
					<c:forEach var="currentDto" items="${sessionScope.listUserEntries}">
				  	 		<c:set var="currentQuestionId" scope="request" value="${currentDto.questionUid}"/>
				  	 		<tr>
				  	 			<td> &nbsp&nbsp</td>
				  	 		</tr>
							<tr>			
								<td NOWRAP valign=top align=left><b> <font size=2> <bean:message key="label.nomination"/>: </b>
									<c:out value="${currentDto.question}" escapeXml="false"/>
								</font> </td>
							</tr>	
							
							<tr> 
								<td NOWRAP class="formlabel" valign=top>
									<table align=center>
										<tr> 
											 <td NOWRAP valign=top> <b> <font size=2> <bean:message key="label.user"/> </font> </b> </td>  
					  						 <td NOWRAP valign=top> <b> <font size=2> <bean:message key="label.attemptTime"/></font> </b></td>
							  			</tr>				 
	
			  							<c:forEach var="questionAttemptData" items="${currentDto.questionAttempts}">
									  	 		<c:set var="userData" scope="request" value="${questionAttemptData.value}"/>
													<jsp:include page="/monitoring/UserResponses.jsp" />										
										</c:forEach>		  	
							  			
									</table>
								</td>  
				  			</tr>
					</c:forEach>		  	

					</table>
			 		</td>
				</tr>
			</table>
		</div>		         
	</html:form>

</body>
</html:html>







