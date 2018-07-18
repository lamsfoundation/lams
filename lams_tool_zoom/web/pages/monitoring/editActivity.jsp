<%@ include file="/common/taglibs.jsp"%>

<c:set var="dto" value="${contentDTO}" />

<c:if test="${dto.contentInUse}">
	<lams:Alert id="edit" type="info" close="false">
		<fmt:message key="message.alertContentEdit" />
	</lams:Alert>
</c:if>

<table class="table table-condensed">
	<tr>
		<td width="20%">
		<fmt:message key="label.authoring.basic.title" />
		</td>
		<td>
		<c:out value="${dto.title}" escapeXml="true" />
		</td>
	</tr>
	<tr>
		<td>
		<fmt:message key="label.authoring.basic.instructions" />
		</td>
		<td>
		<c:out value="${dto.instructions}" escapeXml="false" />
		</td>
	</tr>
</table>

<c:url value="/authoring.do" var="authoringUrl">
	<c:param name="toolContentID" value="${dto.toolContentId}" />
	<c:param name="mode" value="teacher" />
	<c:param name="contentFolderID" value="${contentFolderID}" />
</c:url>
<html:link href="#nogo" onclick="javascript:launchPopup('${authoringUrl}','definelater')" styleClass="btn btn-default pull-right">
	<fmt:message key="button.editActivity" />
</html:link>


