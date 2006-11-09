<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="tool"><lams:WebAppURL/></c:set>

<table  class="alternative-color" cellspacing="0">
	<tr>
		<th class="first">
			<fmt:message key="label.session.name"/>
		</th>
		<th class="first">
			<fmt:message key="label.number.learners"/>
		</th>
	</tr>
	<c:forEach var="entry" items="${sessionMap.statistic}">
		<c:set var="session" value="${entry.key}"/>
		<c:set var="usersInSession" value="${entry.value}"/>
		<tr>
			<td>${session.sessionName}</td>
			<td>${usersInSession}</td>
		</tr>
	</c:forEach>
</table>
