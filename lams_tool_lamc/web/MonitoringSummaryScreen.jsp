<%@ taglib uri="/WEB-INF/struts-html-el.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic-el.tld" prefix="logic-el" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>

<c:if test="${requestScope.userExceptionOnlyContentAndNoSessions == 'true'}"> 	
		<table align=center> <!-- Dave to take off-->
			<tr> <td class="error">   
				<html:errors/>
			</td></tr>
		</table>
</c:if>						

<c:if test="${requestScope.userExceptionOnlyContentAndNoSessions != 'true'}"> 	
	<table align=center> 
		<c:if test="${sessionScope.noAvailableSessions == 'true'}"> 	
			<tr> <td class="error">   
				<html:errors/>
			</td></tr>
		</c:if>						
		
		<c:if test="${sessionScope.noAvailableSessions == 'false'}"> 		
			<tr><td>
				<c:out value="${sessionScope.reportTitleMonitor}"/>
			</td></tr>
			
			<tr><td> &nbsp
			</td></tr>
			
			<c:if test="${sessionScope.checkAllSessionsCompleted == 'true'}"> 			
				<c:if test="${sessionScope.isAllSessionsCompleted == 'false'}"> 			
				<tr> <td class="error">  
					<html:errors/>
				</td></tr>
				</c:if>						
			</c:if>						
			
			<c:if test="${sessionScope.checkAllSessionsCompleted == 'false'}"> 		
				<tr> <td align=right> 
						<select name="toolSessionId1" onchange="selectToolSession();">
						<c:forEach var="toolSessionId" items="${sessionScope.summaryToolSessions}">
								<c:set var="SELECTED_SESSION" scope="request" value=""/>
								<c:if test="${sessionScope.selectionCase == 2}"> 			
									<c:set var="currentMonitoredToolSession" scope="session" value="All"/>
								</c:if>						
								
								<c:if test="${toolSessionId.value == sessionScope.currentMonitoredToolSession}"> 			
										<c:set var="SELECTED_SESSION" scope="request" value="SELECTED"/>
								</c:if>						
							<option value="<c:out value="${toolSessionId.value}"/>"  <c:out value="${requestScope.SELECTED_SESSION}"/>   ><c:out value="${toolSessionId.key}"/>   </option>
						</c:forEach>		  	
						</select>
			  	</td></tr>
				<html:hidden property="responseId"/>
				<html:hidden property="hiddenResponseId"/>
				<html:hidden property="unHiddenResponseId"/>

				<jsp:include page="groupsReport.jsp" />	
				
			</c:if>						
		</table>
		<input type="hidden" name="isToolSessionChanged"/>
	</c:if>						
</c:if>						

