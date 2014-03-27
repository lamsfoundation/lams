<%@ include file="/common/taglibs.jsp"%>

<c:if test="${kaltura.contentInUse}">
	<p class="warning">
		<fmt:message key="message.alertContentEdit" />
	</p>
</c:if>

<table cellspacing="0">
	<tbody>
		<tr>
			<td class="field-name" width="30%">
				<fmt:message key="label.authoring.basic.title" />
			</td>
			<td>
				<c:out value="${kaltura.title}" escapeXml="true" />
			</td>
		</tr>
		<tr>
			<td class="field-name" width="30%">
				<fmt:message key="label.authoring.basic.instructions" />
			</td>
			<td>
				<c:out value="${kaltura.instructions}" escapeXml="false" />
			</td>
		</tr>
	</tbody>
</table>

<p class="align-right">
	<c:url value="/authoring.do" var="authoringUrl">
		<c:param name="toolContentID" value="${kaltura.toolContentId}" />
		<c:param name="mode" value="teacher" />
		<c:param name="contentFolderID" value="${contentFolderID}" />
	</c:url>
	<html:link href="${authoringUrl}" styleClass="button" target="_blank">
		<fmt:message key="button.editActivity" />
	</html:link>
</p>


