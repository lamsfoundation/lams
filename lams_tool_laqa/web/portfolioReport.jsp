<%@ taglib uri="/WEB-INF/struts-html-el.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic-el.tld" prefix="logic-el" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>


	<c:if test="${requestScope.userExceptionUserIdNotNumeric 	== 'true' || 
				requestScope.userExceptionUserIdNotAvailable 	== 'true' ||
				requestScope.userExceptionModeRequired			== 'true' ||
				requestScope.userExceptionNumberFormat 			== 'true' ||
				requestScope.userExceptionToolSessionIdRequired	== 'true' ||
				requestScope.userExceptionContentIdRequired		== 'true' ||
				requestScope.userExceptionContentDoesNotExist	== 'true' }"> 	
		<table align=center> <!-- Dave to take off-->
			<tr> <td class="error">   
				<html:errors/>
			</td></tr>
		</table>
	</c:if>						
	
	<c:if test="${requestScope.userExceptionUserIdNotNumeric 	!= 'true' && 
				  requestScope.userExceptionUserIdNotAvailable 	!= 'true' && 
				  requestScope.userExceptionModeRequired 		!= 'true' &&
				  requestScope.userExceptionNumberFormat 		!= 'true' &&
  				  requestScope.userExceptionToolSessionIdRequired != 'true' &&
				  requestScope.userExceptionContentIdRequired	  != 'true' &&
				  requestScope.userExceptionContentDoesNotExist != 'true' 
				}"> 	
		<table align=center> <!-- Dave to take off-->
				<jsp:include page="groupsReport.jsp" />	
		</table>
	</c:if>						
		
	

	