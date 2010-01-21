<%@ include file="/taglibs.jsp"%>

<tiles:insert attribute="breadcrumbs" />

<h1>Clone Lessons for <c:out value="${org.name}" /></h1>

<c:if test="${not empty errors}">
	<p class="warning">
		<c:forEach items="${errors}" var="error">
			<c:out value="${error}" />
		</c:forEach>
	</p>
</c:if>

