<%@ include file="/taglibs.jsp"%>

<tiles:insert attribute="breadcrumbs" />

<h1><fmt:message key="title.clone.lessons.for"><fmt:param value="${org.name}" /></fmt:message></h1>

<c:if test="${not empty errors}">
	<p class="warning">
		<c:forEach items="${errors}" var="error">
			<c:out value="${error}" />
		</c:forEach>
	</p>
</c:if>

<p>
	<fmt:message key="message.cloned.lessons"><fmt:param value="${result}" /></fmt:message>
</p>

<p align="center">
	<input type="button" class="button" value="<fmt:message key="label.return.to.group" />" 
		onclick="document.location='orgmanage.do?org=<c:out value="${org.organisationId}" />';"
	>
</p>