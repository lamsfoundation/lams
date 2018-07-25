<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

<lams:Page type="monitor" title="${activityTitle}">

<c:if test="${!hasSequenceActivity}">
<table class="table table-bordered table-striped table-condensed">
<c:forEach items="${subActivities}" var="subActivity">
	<c:set var="id" value="${subActivity.activityID}"/>
	<tr>
		<c:choose>
			<c:when test="${statusMap[id] == 1}">
				<td><a href="<lams:LAMSURL />/<c:out value="${urlMap[id]}" />"><c:out value="${subActivity.title}"/></a></td>
				<td><fmt:message key="label.completed" /></td>
			</c:when>
			<c:when test="${statusMap[id] == 2}">
				<td><a href="<lams:LAMSURL />/<c:out value="${urlMap[id]}" />"><c:out value="${subActivity.title}"/></a></td>
				<td><fmt:message key="label.started" /></td>
			</c:when>
			<c:otherwise>
				<td><c:out value="${subActivity.title}"/></td>
				<td><fmt:message key="label.not.started" /></td>
			</c:otherwise>
		</c:choose>
	</tr>
</c:forEach>
</table>
</c:if>

<c:if test="${hasSequenceActivity}">
<c:forEach items="${subActivities}" var="subActivity">
	<c:set var="id" value="${subActivity.activityID}" />
	<h4><c:out value="${subActivity.title}" /> -
	<c:choose>
		<c:when test="${statusMap[id] == 1}"><fmt:message key="label.completed" /></c:when>
		<c:when test="${statusMap[id] == 2}"><fmt:message key="label.started" /></c:when>
		<c:otherwise><fmt:message key="label.not.started" /></c:otherwise>
	</c:choose>
	</h4>
	<table class="table table-bordered table-striped table-condensed">
		<c:forEach items="${subActivity.childActivities}" var="child">
			<c:set var="childId" value="${child.activityID}"/>
			<tr><td>
			<c:choose>
				<c:when test="${statusMap[childId] == 1 or statusMap[childId] == 2}">
				<a href="<lams:LAMSURL />/<c:out value="${urlMap[childId]}" />"><c:out value="${child.title}"/></a>
				</c:when>
				<c:otherwise>
				<c:out value="${child.title}"/>
				</c:otherwise>
			</c:choose>
			</td></tr>
		</c:forEach>
	</table>
</c:forEach>
</c:if>

</lams:Page>

