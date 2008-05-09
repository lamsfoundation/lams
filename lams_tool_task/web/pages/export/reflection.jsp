<%-- Reflection list  --%>

<c:if test="${sessionMap.taskList.reflectOnActivity}">
	<table cellpadding="0" class="alternative-color" >

		<tr>
			<td colspan="5">
				<h1><fmt:message key="label.monitoring.summary.title.reflection"/>	</h1>
			</td>
		</tr>
		<tr>
			<th colspan="2">
				<fmt:message key="label.monitoring.summary.user"/>
			</th>
			<th>
				<fmt:message key="label.monitoring.summary.reflection"/>
			</th>
		</tr>				
					
		<c:forEach var="user" items="${summary.userNames}" varStatus="userStatus">
	
			<tr>
				<td colspan="2">
					${user.loginName}
				</td>
				<td >
					<c:set var="viewReflection">
						<c:url value="/monitoring/viewReflection.do?userUid=${user.uid}"/>
					</c:set>
					<html:link href="javascript:launchPopup('${viewReflection}')">
						<fmt:message key="label.view" />
					</html:link>
				</td>
			</tr>
		</c:forEach>
					
	</table>
</c:if>