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

<html:html locale="true">
<head>
	<title> <bean:message key="label.learning.qa"/> </title>

	<%@ include file="/common/header.jsp"%>
	<%@ include file="/common/fckeditorheader.jsp"%>

	
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

	  <html:form  action="/learning?validate=false" enctype="multipart/form-data" method="POST" target="_self">		
  		<html:hidden property="method"/>	 
			<table class="forms">

			<tr><td NOWRAP>
				 <c:out value="${sessionScope.activityTitle}" escapeXml="false"/> 
			</td></tr>


			<tr> <td>
				<html:errors/>
			</td></tr>

			<c:if test="${sessionScope.isDefineLater == 'true'}"> 			
				<tr> <td NOWRAP>
					<bean:message key="error.defineLater"/>
				</td></tr>
			</c:if> 		

			
			<c:if test="${sessionScope.isDefineLater == 'false'}"> 			
				<c:if test="${sessionScope.isToolActivityOffline == 'true'}"> 			
					<tr> <td NOWRAP>
						<bean:message key="label.learning.forceOfflineMessage"/>
					</td></tr>
				</c:if> 		
			
				<c:if test="${sessionScope.isToolActivityOffline == 'false'}"> 			
					<tr><td NOWRAP>
						<c:out value="${sessionScope.activityInstructions}" escapeXml="false"/> 
					</td></tr>
		
					<tr> <td NOWRAP>
						 <c:out value="${sessionScope.userFeedback}" escapeXml="true"/> 
					</td></tr>
				
					<c:choose> 
					  <c:when test="${sessionScope.questionListingMode == sessionScope.questionListingModeSequential}" > 
							<jsp:include page="/learning/SequentialAnswersContent.jsp" /> 
					  </c:when> 

					  <c:otherwise>
						  	<jsp:include page="/learning/CombinedAnswersContent.jsp" /> 
					  </c:otherwise>
					</c:choose> 
				</c:if> 		
		  	</c:if> 		
	 	</table>
	</html:form>
	
</body>
</html:html>

	
	