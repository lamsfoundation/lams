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
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib uri="tags-bean" prefix="bean"%> 
<%@ taglib uri="tags-html" prefix="html"%>
<%@ taglib uri="tags-logic" prefix="logic" %>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="fck-editor" prefix="FCK" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

<c:set var="lams"><lams:LAMSURL/></c:set>
<c:set var="tool"><lams:WebAppURL/></c:set>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html locale="true">
<head>
	<jsp:include page="/learning/learningHeader.jsp" />
	
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
			<table width="80%" cellspacing="8" align="CENTER" class="forms">
		
			<c:if test="${questionListingMode != 'questionListingModePreview'}"> 						
				<tr><td NOWRAP valign=top>
					 <c:out value="${sessionScope.activityTitle}" escapeXml="false"/> 
				</td></tr>
			</c:if> 		
			
			<c:if test="${questionListingMode == 'questionListingModePreview'}"> 						
				<tr><th scope="col" NOWRAP valign=top>
					<font size=2> <b> <bean:message key="label.preview"/> </b> </font>
				</th></tr>
				<tr><td NOWRAP valign=top>
					&nbsp&nbsp&nbsp&nbsp&nbsp
				</td></tr>
			</c:if> 		
			
			<c:if test="${questionListingMode != 'questionListingModePreview'}"> 						
				<tr> <td>
					<html:errors/>
				</td></tr>

				<c:if test="${sessionScope.isDefineLater == 'true'}"> 			
					<tr> <td>
						<bean:message key="error.defineLater"/>
					</td></tr>
				</c:if> 		
			</c:if>														  					 									  													  			
			
			<c:if test="${sessionScope.isDefineLater == 'false'}"> 			
				<c:if test="${sessionScope.isToolActivityOffline == 'true'}"> 			
					<tr> <td>
						<bean:message key="label.learning.forceOfflineMessage"/>
					</td></tr>
				</c:if> 		
			
				<c:if test="${sessionScope.isToolActivityOffline == 'false'}"> 			
					<tr><td NOWRAP valign=top>
						<c:out value="${sessionScope.activityInstructions}" escapeXml="false"/> 
					</td></tr>
		
					<tr> <td>
						 <font size=2>	<c:out value="${sessionScope.userFeedback}" escapeXml="true"/> </font>
					</td></tr>
				
					<c:choose> 
					  <c:when test="${sessionScope.questionListingMode == sessionScope.questionListingModeSequential}" > 
							<jsp:include page="/learning/SequentialAnswersContent.jsp" /> 
					  </c:when> 

  					  <c:when test="${sessionScope.questionListingMode == sessionScope.questionListingModePreview}" > 
							<jsp:include page="/learning/Preview.jsp" /> 
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

	
	