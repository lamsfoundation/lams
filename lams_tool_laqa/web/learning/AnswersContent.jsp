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

	  <html:form  action="/learning?validate=false" enctype="multipart/form-data" method="POST" target="_self">		
  		<html:hidden property="method"/>	 
		<html:hidden property="toolSessionID"/>						
		<html:hidden property="userID"/>						
		<html:hidden property="httpSessionID"/>		
		<html:hidden property="questionIndex"/>		
		<html:hidden property="totalQuestionCount"/>		
								
			<table>

			<tr> <td>
				<html:errors/>
			</td></tr>

			<c:if test="${generalLearnerFlowDTO.activityOffline == 'true'}"> 			
				<tr> <td NOWRAP>
					<fmt:message key="label.learning.forceOfflineMessage"/>
				</td></tr>
			</c:if> 		
		
			<c:if test="${generalLearnerFlowDTO.activityOffline == 'false'}"> 			
				<tr><td NOWRAP>
					<c:out value="${generalLearnerFlowDTO.activityInstructions}" escapeXml="false"/> 
				</td></tr>
	
			
				<c:choose> 
				  <c:when test="${generalLearnerFlowDTO.questionListingMode == 'questionListingModeSequential'}" > 
		
				  		<c:if test="${generalLearnerFlowDTO.initialScreen == 'true'}"> 							  		
						  	<tr> <td NOWRAP>
			 					<fmt:message key="label.feedback.seq"/> &nbsp <c:out value="${generalLearnerFlowDTO.remainingQuestionCount}"/> &nbsp
			 					<fmt:message key="label.questions.simple"/>
							</td></tr>
						</c:if> 									
				  		<c:if test="${generalLearnerFlowDTO.initialScreen != 'true'}"> 							  		
						  	<tr> <td NOWRAP>				  		
			 					<fmt:message key="label.questions.remaining"/> &nbsp <c:out value="${generalLearnerFlowDTO.remainingQuestionCount}"/> 
							</td></tr>			 					
						</c:if> 													  		
				  
						<jsp:include page="/learning/SequentialAnswersContent.jsp" /> 
				  </c:when> 

				  <c:otherwise>
					  	<tr> <td NOWRAP>
		 					<fmt:message key="label.feedback.combined"/> &nbsp <c:out value="${generalLearnerFlowDTO.remainingQuestionCount}"/> &nbsp
		 					<fmt:message key="label.questions.simple"/>
						</td></tr>
				  
					  	<jsp:include page="/learning/CombinedAnswersContent.jsp" /> 
				  </c:otherwise>
				</c:choose> 
			</c:if> 		
	 	</table>
	</html:form>
</div>

<div id="footer"></div>

</body>
</html:html>









	
	