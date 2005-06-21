<%@ taglib uri="/WEB-INF/struts-html-el.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic-el.tld" prefix="logic-el" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>


	<c:if test="${requestScope.userExceptionWrongFormat 		== 'true' || 
				requestScope.userExceptionUncompatibleIds 		== 'true' || 				
				requestScope.userExceptionContentDoesNotExist 	== 'true' || 
				requestScope.userExceptionUserIdNotNumeric 		== 'true' || 
				requestScope.userExceptionUserIdNotAvailable 	== 'true' || 
				requestScope.userExceptionToolSessionDoesNotExist == 'true' || 
				requestScope.userExceptionNumberFormat 			== 'true'}"> 	
		<table align=center> <!-- Dave to take off-->
			<tr> <td class="error">   
				<html:errors/>
			</td></tr>
		</table>
	</c:if>						
	
	<c:if test="${requestScope.userExceptionWrongFormat 		!= 'true'  && 
				  requestScope.userExceptionUncompatibleIds 	!= 'true'  && 
				  requestScope.userExceptionContentDoesNotExist != 'true' &&
				  requestScope.userExceptionUserIdNotNumeric 	!= 'true' &&
				  requestScope.userExceptionUserIdNotAvailable 	!= 'true' &&				  				  
				  requestScope.userExceptionToolSessionDoesNotExist != 'true' &&				  
				  requestScope.userExceptionNumberFormat 		!= true}"> 	
		<!-- this form is exists temporarily to represent tool icon, remove this form once the tool is deployed into authoring environment -->
		<html:form  action="/monitoring?method=generateToolSessionDataMap&validate=false" method="POST" target="_self">
		
		<table align=center> <!-- Dave to take off-->
			<tr>
				<td>
					<html:submit property="summary" styleClass="linkbutton" onmouseover="pviiClassNew(this,'linkbutton')" onmouseout="pviiClassNew(this,'linkbutton')">
						<bean:message key="button.summary"/>
					</html:submit> 
				</td><td>
					<html:submit property="instructions" styleClass="linkbutton" onmouseover="pviiClassNew(this,'linkbutton')" onmouseout="pviiClassNew(this,'linkbutton')">
						<bean:message key="button.instructions"/>
					</html:submit> 
				</td><td>
					<html:submit property="editActivity" styleClass="linkbutton" onmouseover="pviiClassNew(this,'linkbutton')" onmouseout="pviiClassNew(this,'linkbutton')">
						<bean:message key="button.editActivity"/>
					</html:submit> 
				</td><td>			
					<html:submit property="stats" styleClass="linkbutton" onmouseover="pviiClassNew(this,'linkbutton')" onmouseout="pviiClassNew(this,'linkbutton')">
						<bean:message key="button.stats"/>
					</html:submit> 
				</td>
			</tr>
		</table>
		
		<c:choose> 
		  <c:when test="${sessionScope.choiceMonitoring == 'choiceTypeMonitoringSummary'}" >  
				<jsp:include page="MonitoringSummaryScreen.jsp" />
		  </c:when> 
		  <c:when test="${sessionScope.choiceMonitoring == 'choiceTypeMonitoringInstructions'}" > 
				<jsp:include page="MonitoringInstructionsScreen.jsp" />
		  </c:when> 
		  <c:when test="${sessionScope.choiceMonitoring == 'choiceTypeMonitoringEditActivity'}" > 
				<jsp:include page="BasicContent.jsp" />
		  </c:when> 
		  <c:when test="${sessionScope.choiceMonitoring == 'choiceTypeMonitoringStats'}" > 
				<jsp:include page="MonitoringStatsScreen.jsp" />
		  </c:when> 
		  <c:otherwise> 
			  	<jsp:include page="MonitoringSummaryScreen.jsp" />
		  </c:otherwise>
		</c:choose> 
		
	</html:form>
	</c:if>						
	
<SCRIPT language="JavaScript"> 
	function selectToolSession()
	{
		document.forms[0].isToolSessionChanged.value='1';
		document.forms[0].submit();
	}
	
	function selectResponse(responseId)
	{
		document.forms[0].responseId.value=responseId;
		document.forms[0].submit();
	}
	
	function hideResponse(responseId)
	{
		document.forms[0].hiddenResponseId.value=responseId;
		document.forms[0].submit();
	}
	
	function unHideResponse(responseId)
	{
		document.forms[0].unHiddenResponseId.value=responseId;
		document.forms[0].submit();
	}
	
 </SCRIPT>	
	
	
	