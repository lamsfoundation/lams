<%@ taglib uri="/WEB-INF/struts-html-el.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic-el.tld" prefix="logic-el" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>


<c:if test="${requestScope.monitoringInstructionsUpdateMessage == 'true'}"> 	
		<table align=center> <!-- Dave to take off-->
      		<tr>
				<td class="error">   
      				<fmt:message key="monitoring.feedback.instructionUpdate" />
      			</td>
      		</tr>
		</table>
</c:if>		


<c:if test="${requestScope.userExceptionMonitoringTabContentIdRequired == 'true'}"> 	
		<table align=center> <!-- Dave to take off-->
			<tr> <td class="error">   
				<html:errors/>
			</td></tr>
		</table>
</c:if>		

<c:if test="${requestScope.userExceptionMonitoringTabContentIdRequired != 'true'}"> 	
	<table align=center> <!-- Dave to take off-->
		<tr> 
					<td>
          				<fmt:message key="label.offlineInstructions" />:
          			</td>
					<td NOWRAP width=700>
						<c:out value="${sessionScope.monitoredOfflineInstructions}" escapeXml="false" />
					</td> 
				</tr>
		 <tr> 
          			<td>
          				<fmt:message key="label.onlineInstructions"/>:
          			</td>
					<td NOWRAP width=700>
						<c:out value="${sessionScope.monitoredOnlineInstructions}" escapeXml="false" />
					</td> 
		</tr>
	</table>      		
</c:if>		