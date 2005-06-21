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
      				<fmt:message key="label.offlineInstructions" />
      			</td>
      			<td>
      				<html:textarea property="offlineInstructions" value="${sessionScope.monitoredOfflineInstructions}" rows="5" cols="50"/>
      			</td>
      		</tr>
      		<tr>
      			<td>
      				<fmt:message key="label.onlineInstructions" />
      			</td>
      			<td>
      				<html:textarea property="onlineInstructions"  value="${sessionScope.monitoredOnlineInstructions}"rows="5" cols="50"/>
      			</td>
      		</tr>	
      		<tr>
      		<td> &nbsp&nbsp</td>
      		 	<td> 
				 	 <html:submit property="submitMonitoringInstructions" styleClass="linkbutton" onmouseover="pviiClassNew(this,'linkbutton')" onmouseout="pviiClassNew(this,'linkbutton')">
						<bean:message key="button.submitAllContent"/>
					</html:submit>
				</td> 
			</tr>
	</table>      		
</c:if>		