<%@ include file="/taglibs.jsp"%>

<tiles:insert attribute="breadcrumbs" />

<h4><fmt:message key="title.clone.lessons.for"><fmt:param value="${org.name}" /></fmt:message></h4>

<c:if test="${not empty errors}">
	<lams:Alert type="danger" id="errorKey" close="false">	
		<c:forEach items="${errors}" var="error">
			<c:out value="${error}" />
		</c:forEach>
	</lams:Alert>
</c:if>

<p>
	<fmt:message key="message.cloned.lessons"><fmt:param value="${result}" /></fmt:message>
</p>

<input type="button" class="btn btn-default pull-right" value="<fmt:message key="label.return.to.group" />" 
		onclick="document.location='orgmanage.do?org=<c:out value="${org.organisationId}" />';"	>
