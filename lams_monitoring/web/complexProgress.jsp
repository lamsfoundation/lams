<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

<div id="content">

<h1><c:out value="${activityTitle}" /></h1>

<c:if test="${!hasSequenceActivity}">
<table class="alternative-color" cellspacing="0">
<c:forEach items="${subActivities}" var="subActivity">
	<c:set var="id" value="${subActivity.activityId}"/>
	<tr>
		<td><c:out value="${subActivity.title}"/></td>
		<td><c:if test="${startedMap[id]}"><fmt:message key="label.started" /></c:if>
			<c:if test="${!startedMap[id]}"><fmt:message key="label.not.started" /></c:if>
		</td>
	</tr>
</c:forEach>
</table>
</c:if>

<c:if test="${hasSequenceActivity}">
<c:forEach items="${subActivities}" var="subActivity">
	<c:set var="id" value="${subActivity.activityId}" />
	<h2><c:out value="${subActivity.title}" />
		<c:if test="${startedMap[id]}"> - <fmt:message key="label.started" /></c:if>
		<c:if test="${!startedMap[id]}"> - <fmt:message key="label.not.started" /></c:if>
	</h2>
	<ul>
		<c:forEach items="${subActivity.activities}" var="child">
			<c:set var="childId" value="${child.activityId}"/>
			<li>
				<c:if test="${startedMap[id]}">
				<a href="<lams:LAMSURL />/<c:out value="${urlMap[childId]}" />"><c:out value="${child.title}"/></a>
				</c:if>
				<c:if test="${!startedMap[id]}">
				<c:out value="${child.title}"/>
				</c:if>
			</li>
		</c:forEach>
	</ul>
</c:forEach>
</c:if>

</div>


