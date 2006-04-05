<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-core" prefix="c" %>

<table width="60%" border="0" cellspacing="0" cellpadding="0">
	<c:set var="groupCounter" scope="request" value="0"/>
	<c:forEach var="group" items="${requestScope.groupStatsMap}">
		<c:set var="groupCounter" scope="request" value="${groupCounter+1}"/>
	<tr>
		<td>
			<fmt:message key="heading.group">
				<fmt:param value="${groupCounter}" />
			</fmt:message>
			&nbsp;&nbsp;
			<fmt:message key="heading.totalLearners"/>
			<c:out value="${group.value}"/>
			
		</td>
	
	</tr>
	</c:forEach>
	<tr>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td>
			<fmt:message key="heading.totalLearnersInGroup" />&nbsp;&nbsp;<c:out value="${requestScope.totalLearners}"/>
		</td>
	</tr>
</table>
