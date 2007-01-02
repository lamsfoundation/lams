<%@ include file="/taglibs.jsp"%>

<h2><a href="sysadminstart.do"><fmt:message key="sysadmin.maintain" /></a> : <fmt:message key="sysadmin.library.management" /></h2>
<table class=alternative-color width=100%>
	<tr>
		<th><fmt:message key="sysadmin.library.title" /></th>
		<th><fmt:message key="sysadmin.library.description" /></th>
		<th><fmt:message key="sysadmin.library.activity.title" /></th>
		<th><fmt:message key="sysadmin.library.activity.description" /></th>
		<th><fmt:message key="sysadmin.library.createtime" /></th>
		<th><fmt:message key="sysadmin.function" /></th>
	</tr>
	<c:forEach items="${libraries}" var="library">
	<tr>
		<td><c:out value="${library.title}" /></td>
		<td><c:out value="${library.description}" /></td>
		<c:forEach var="act" items="${library.templateActivities}" varStatus="status">
			<c:if test="${status.first}">
				<td><c:out value="${act.activityTitle}" /></td>
				<td><c:out value="${act.description}" /></td>
			</c:if>
		</c:forEach>
		<td><lams:Date value="${library.createDateTime}" /></td>
		<td>
			<c:choose>
				<c:when test="${library.validFlag}">
					<a href="<c:url value='libraryManage.do?action=disable&libraryID=${library.learningLibraryID}' />"><fmt:message key="admin.disable" /></a>
				</c:when>
				<c:otherwise>
					<a href="<c:url value='libraryManage.do?action=enable&libraryID=${library.learningLibraryID}'/>"><fmt:message key="admin.enable" /></a>
				</c:otherwise>
			</c:choose>
		</td>
	</tr>
	</c:forEach>
</table>
<p>${fn:length(libraries)} <fmt:message key="sysadmin.library.totals" /></p>