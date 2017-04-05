<%@ include file="/common/taglibs.jsp"%>

<c:if test="${kaltura.contentInUse}">
	<lams:Alert type="warn" id="alertContentEdit" close="false">
		<fmt:message key="message.alertContentEdit" />
	</lams:Alert>
</c:if>

<table class="table table-condensed">
	<tbody>
		<tr>
			<td width="30%">
				<fmt:message key="label.authoring.basic.title" />
			</td>
			<td>
				<c:out value="${kaltura.title}" escapeXml="true" />
			</td>
		</tr>
		<tr>
			<td>
				<fmt:message key="label.authoring.basic.instructions" />
			</td>
			<td>
				<c:out value="${kaltura.instructions}" escapeXml="false" />
			</td>
		</tr>
	</tbody>
</table>

<c:url value="/authoring.do" var="authoringUrl">
	<c:param name="toolContentID" value="${kaltura.toolContentId}" />
	<c:param name="mode" value="teacher" />
	<c:param name="contentFolderID" value="${contentFolderID}" />
</c:url>
<html:link href="${authoringUrl}" styleClass="btn btn-default pull-right">
	<fmt:message key="button.editActivity" />
</html:link>


