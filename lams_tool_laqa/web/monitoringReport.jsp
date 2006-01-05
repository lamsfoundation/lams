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
	
	<c:if test="${requestScope.userExceptionWrongFormat 			!= 'true' && 
				  requestScope.userExceptionUncompatibleIds 		!= 'true' && 
				  requestScope.userExceptionContentDoesNotExist 	!= 'true' &&
				  requestScope.userExceptionUserIdNotNumeric 		!= 'true' &&
				  requestScope.userExceptionUserIdNotAvailable 		!= 'true' &&				  				  
				  requestScope.userExceptionToolSessionDoesNotExist != 'true' &&				  
				  requestScope.userExceptionNumberFormat 			!= true}"> 	
		<!-- this form is exists temporarily to represent tool icon, remove this form once the tool is deployed into authoring environment -->
        
		<html:form action="/monitoring?method=generateToolSessionDataMap&validate=false" method="POST" target="_self">
        <input type="hidden" name="dispatch" value=""/>
		
		<table align=center> <!-- Dave to take off-->
			<tr>
				<td>
					<html:submit styleClass="linkbutton" 
					             onmouseover="pviiClassNew(this,'linkbutton')" 
					             onmouseout="pviiClassNew(this,'linkbutton')"
					             onclick="submitMethod('showSummary');">
						<bean:message key="button.summary"/>
					</html:submit> 
				</td><td>
					<html:submit styleClass="linkbutton" 
					             onmouseover="pviiClassNew(this,'linkbutton')" 
					             onmouseout="pviiClassNew(this,'linkbutton')"
					             onclick="submitMethod('showInstructions');">
						<bean:message key="button.instructions"/>
					</html:submit> 
				</td><td>
					<html:submit styleClass="linkbutton" 
					             onmouseover="pviiClassNew(this,'linkbutton')" 
					             onmouseout="pviiClassNew(this,'linkbutton')"
					             onclick="submitMethod('showEditActivity');">
						<bean:message key="button.editActivity"/>
					</html:submit> 
				</td><td>			
					<html:submit styleClass="linkbutton" 
					             onmouseover="pviiClassNew(this,'linkbutton')" 
					             onmouseout="pviiClassNew(this,'linkbutton')"
					             onclick="submitMethod('showStats');">
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

    function submitMethod(actionMethod) {
        document.QaMonitoringForm.dispatch.value=actionMethod; 
        document.QaMonitoringForm.submit();
    }

	function selectToolSession()
	{
		document.QaMonitoringForm.isToolSessionChanged.value='1';
		submitMethod("selectToolSession");
	}
	
	function selectResponse(responseId)
	{
		document.QaMonitoringForm.responseId.value=responseId;
		submitMethod("editResponse");
	}
	
	function hideResponse(responseId)
	{
    
        document.QaMonitoringForm.hiddenResponseId.value=responseId;
		submitMethod("hideResponse");
	}
	
	function unHideResponse(responseId)
	{
		document.QaMonitoringForm.unHiddenResponseId.value=responseId;
		submitMethod("unhideResponse");
	}
	
 </SCRIPT>	
	
	
	