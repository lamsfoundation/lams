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
		<br>
		<table align=center> <!-- Dave to take off-->
			<c:set var="totalCount" scope="request" value="0"/>
			<c:set var="groupCounter" scope="request" value="0"/>
			<c:forEach var="toolSessionId" items="${sessionScope.mapStats}">
				<c:set var="userCount" scope="request" value="${toolSessionId.value}"/>
				<c:set var="totalCount" scope="request" value="${totalCount + toolSessionId.value}"/>
				<c:set var="groupCounter" scope="request" value="${groupCounter + 1}"/>
				<tr> 
					<td> <bean:message key="group.label"/>
						 <c:out value="${groupCounter}"/>:</td>
					<td>
						<bean:message key="label.stats.totalLearners"/>
					</td>
					<td> 
						<c:out value="${userCount}"/> 
					</td>
				</tr> 
				<tr> <td> &nbsp&nbsp</td> </tr> 
			</c:forEach>		  	
			<tr> 
				<td> <bean:message key="label.stats.allGroups"/></td>
				<td>
					<bean:message key="label.stats.totalAllGroups"/>
				</td>
				<td> 
						<c:out value="${totalCount}"/> 
				</td>
			</tr> 
		</table>
</c:if>						