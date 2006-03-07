<%--
Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

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

		<html:form  action="/learning?method=displayQ&validate=false" method="POST" target="_self">
		<br>
		<table align=center bgcolor="#FFFFFF"> 
		
			<c:if test="${questionListingMode != 'questionListingModePreview'}"> 						
				<tr><td NOWRAP class="input" valign=top>
					<font size=2> <b> <c:out value="${sessionScope.activityTitle}" escapeXml="false"/> </b> </font>
				</td></tr>
			</c:if> 		
			
			<c:if test="${questionListingMode == 'questionListingModePreview'}"> 						
				<tr><td NOWRAP class="input" valign=top>
					<font size=2> <b> <bean:message key="label.preview"/> </b> </font>
				</td></tr>
				<tr><td NOWRAP class="input" valign=top>
					&nbsp&nbsp&nbsp&nbsp&nbsp
				</td></tr>
			</c:if> 		
			
			<c:if test="${questionListingMode != 'questionListingModePreview'}"> 						
				<tr> <td class="error">
					<html:errors/>
				</td></tr>

				<c:if test="${sessionScope.isDefineLater == 'true'}"> 			
					<tr> <td class="error">
						<bean:message key="error.defineLater"/>
					</td></tr>
				</c:if> 		
			</c:if>														  					 									  													  			
			
			<c:if test="${sessionScope.isDefineLater == 'false'}"> 			
				<c:if test="${sessionScope.isToolActivityOffline == 'true'}"> 			
					<tr> <td class="error">
						<bean:message key="label.learning.forceOfflineMessage"/>
					</td></tr>
				</c:if> 		
			
				<c:if test="${sessionScope.isToolActivityOffline == 'false'}"> 			
					<tr><td NOWRAP class="input" valign=top>
						<font size=2> 	<c:out value="${sessionScope.activityInstructions}" escapeXml="false"/> </font>
					</td></tr>
		
					<tr> <td class="error">
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
	
	
	