<%@ taglib uri="/WEB-INF/struts-html-el.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic-el.tld" prefix="logic-el" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>



	<c:if test="${	requestScope.userExceptionContentDoesNotExist 	== 'true' || 
				  	requestScope.userExceptionContentIdRequired 	== 'true' || 
				  	requestScope.userExceptionUserIdNotNumeric 		== 'true' || 
				  	requestScope.userExceptionUserIdNotAvailable 	== 'true' || 
				  	requestScope.userExceptionUserIdExisting 		== 'true' || 				  	
				  	requestScope.userExceptionToolSessionIdRequired == 'true' || 
					requestScope.userExceptionNumberFormat 			== 'true'}"> 	
		<table align=center> <!-- Dave to take off-->
			<tr> <td class="error">   
				<html:errors/>
			</td></tr>
		</table>
	</c:if>						

	<c:if test="${	requestScope.userExceptionContentDoesNotExist 	!= 'true'  && 
					requestScope.userExceptionContentIdRequired 	!= 'true'  && 
					requestScope.userExceptionUserIdNotNumeric		!= 'true'  &&
				  	requestScope.userExceptionUserIdNotAvailable 	!= 'true'  && 
				  	requestScope.userExceptionUserIdExisting 		!= 'true'  && 				  	
					requestScope.userExceptionToolSessionIdRequired != 'true'  && 
				  	requestScope.userExceptionNumberFormat 			!= 'true'}"> 	
		<!-- Dave to do the styling-->
		<!-- this form is exists temporarily to represent tool icon, remove this form once the tool is deployed into authoring environment -->
		<html:form  action="/learning?method=displayQ&validate=false" method="POST" target="_self">
		<br>
		<table align=center> <!-- Dave to take off-->
			<tr><td>
				<fmt:message key="label.learning.qa"/>
			</td></tr>
			
			<tr> <td class="error">
				<html:errors/>
			</td></tr>
		
			<c:if test="${sessionScope.isDefineLater == 'true'}"> 			
				<tr> <td class="error">
					<fmt:message key="error.defineLater"/>
				</td></tr>
			</c:if> 		
			<c:if test="${sessionScope.isDefineLater == 'false'}"> 			
				<c:if test="${sessionScope.isToolActivityOnline == 'true'}"> 			
					<tr> <td class="error">
						<fmt:message key="label.learning.forceOfflineMessage"/>
					</td></tr>
				</c:if> 		
			
				<c:if test="${sessionScope.isToolActivityOnline == 'false'}"> 			
					<tr> <td class="error">
						<c:out value="${sessionScope.userFeedback}"/>
					</td></tr>
				
					<c:choose> 
					  <c:when test="${sessionScope.questionListingMode == sessionScope.questionListingModeSequential}" > 
							<jsp:include page="SequentialAnswersContent.jsp" /> 
					  </c:when> 
					  <c:otherwise>
						  	<jsp:include page="CombinedAnswersContent.jsp" /> 
					  </c:otherwise>
					</c:choose> 
				</c:if> 		
		  	</c:if> 		
	 	</table>
		</html:form>
	</c:if>						
	
	
	
	
	