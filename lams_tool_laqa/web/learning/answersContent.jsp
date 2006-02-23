<%@ taglib uri="tags-html-el" prefix="html" %>
<%@ taglib uri="tags-bean" prefix="bean" %>
<%@ taglib uri="tags-logic" prefix="logic" %>
<%@ taglib uri="tags-logic-el" prefix="logic-el" %>
<%@ taglib uri="tags-c" prefix="c" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-fck-editor" prefix="FCK" %>

		<html:form  action="/learning?method=displayQ&validate=false" method="POST" target="_self">
		<br>
		<table align=center bgcolor="#FFFFFF"> 
			<tr><td NOWRAP class="input" valign=top>
				<font size=2> <b> <c:out value="${sessionScope.activityTitle}" escapeXml="false"/> </b> </font>
			</td></tr>
			
			<tr> <td class="error">
				<html:errors/>
			</td></tr>
		
			<c:if test="${sessionScope.isDefineLater == 'true'}"> 			
				<tr> <td class="error">
					<bean:message key="error.defineLater"/>
				</td></tr>
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
					  <c:otherwise>
						  	<jsp:include page="/learning/CombinedAnswersContent.jsp" /> 
					  </c:otherwise>
					</c:choose> 
				</c:if> 		
		  	</c:if> 		
	 	</table>
		</html:form>
	
	
	