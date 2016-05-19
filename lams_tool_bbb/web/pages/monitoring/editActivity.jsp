<%@ include file="/common/taglibs.jsp"%>

<c:set var="dto" value="${contentDTO}" />

<div class="panel">
	<p>
		<fmt:message key="label.authoring.basic.title" />
		:</br>
		<c:out value="${dto.title}" escapeXml="true" />
	</p>
	<p>
		<fmt:message key="label.authoring.basic.instructions" />
		: </br>
		<c:out value="${dto.instructions}" escapeXml="false" />
	</p>
</div>

<c:if test="${dto.contentInUse}">
	<lams:Alert id="edit" type="info" close="false">
		<fmt:message key="message.alertContentEdit" />
	</lams:Alert>
</c:if>


<div>
	<c:url value="/authoring.do" var="authoringUrl">
		<c:param name="toolContentID" value="${dto.toolContentId}" />
		<c:param name="mode" value="teacher" />
		<c:param name="contentFolderID" value="${contentFolderID}" />
	</c:url>
	<html:link href="${authoringUrl}" styleClass="btn btn-primary pull-right voffset10" target="_blank">
		<fmt:message key="button.editActivity" />
	</html:link>
</div>


