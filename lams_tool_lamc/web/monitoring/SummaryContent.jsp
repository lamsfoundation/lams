<%@ taglib uri="tags-html-el" prefix="html" %>
<%@ taglib uri="tags-bean" prefix="bean" %>
<%@ taglib uri="tags-logic-el" prefix="logic-el" %>
<%@ taglib uri="tags-c" prefix="c" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-fck-editor" prefix="FCK" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

	<table>
		<tr> <td NOWRAP class="formlabel" valign=top align=right> 
				<select name="toolSessionId1" onchange="selectToolSession();">
				<c:forEach var="toolSessionId" items="${sessionScope.summaryToolSessions}">
						<c:set var="SELECTED_SESSION" scope="request" value=""/>
						<c:if test="${sessionScope.selectionCase == 2}"> 			
							<c:set var="currentMonitoredToolSession" scope="session" value="All"/>
						</c:if>						
						
						<c:if test="${toolSessionId.value == sessionScope.currentMonitoredToolSession}"> 			
								<c:set var="SELECTED_SESSION" scope="request" value="SELECTED"/>
						</c:if>						
						
						<c:if test="${toolSessionId.value != 'All'}"> 	
							<option value="<c:out value="${toolSessionId.value}"/>"  <c:out value="${requestScope.SELECTED_SESSION}"/>> Group <c:out value="${toolSessionId.key}"/>  </option>						
						</c:if>						
						
						<c:if test="${toolSessionId.value == 'All'}"> 	
							<option value="<c:out value="${toolSessionId.value}"/>"  <c:out value="${requestScope.SELECTED_SESSION}"/>>  All  </option>						
						</c:if>						

				</c:forEach>		  	
				</select>
	  	</td></tr>
	  	<input type="hidden" name="isToolSessionChanged"/>

		<tr> <td NOWRAP class="formlabel" valign=top align=center> 
			<table align=center>
				<c:forEach var="currentDto" items="${sessionScope.listMonitoredAnswersContainerDto}">
					<tr>			
						<td NOWRAP class="formlabel" valign=top> <bean:message key="label.question"/> </td>  
						<td NOWRAP class="formlabel" valign=top> <c:out value="${currentDto.question}"/>  </td>  
					</tr>	
					<tr>					
						<td NOWRAP class="formlabel" valign=top><bean:message key="label.mc.options"/></td>  
						<td NOWRAP class="formlabel" valign=top><c:out value="${currentDto.candidateAnswers}"/></td>  
					</tr>			
				</c:forEach>		  	
			</table>
		</td> </tr>
		
	</table>

	
<SCRIPT language="JavaScript"> 
	function selectToolSession()
	{
		document.forms[0].isToolSessionChanged.value='1';
		document.forms[0].submit();
	}
</SCRIPT>	
	