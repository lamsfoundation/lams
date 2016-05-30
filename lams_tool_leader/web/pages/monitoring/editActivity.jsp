<%@ include file="/common/taglibs.jsp"%>

<c:set var="dto" value="${leaderselectionDTO}" />

<c:if test="${dto.contentInUse}">
	<lams:Alert type="info" id="content-in-use" close="false">
		<fmt:message key="message.alertContentEdit" />
	</lams:Alert>
</c:if>

<table class="table table-condensed">
	<tbody>
		<tr>
			<td class="field-name" width="10%" nowrap>
				<fmt:message key="label.authoring.basic.title" />
			</td>
			<td>
				<c:out value="${dto.title}" escapeXml="true" />
			</td>
		</tr>
		<tr>
			<td class="field-name" width="10%" nowrap valign="top">
				<fmt:message key="label.authoring.basic.instructions" />
			</td>
			<td>
				<c:out value="${dto.instructions}" escapeXml="false" />
			</td>
		</tr>
	</tbody>
</table>

<p class="align-right">
	<c:url value="/authoring.do" var="authoringUrl">
		<c:param name="toolContentID" value="${dto.toolContentId}" />
		<c:param name="mode" value="teacher" />
		<c:param name="contentFolderID" value="${contentFolderID}" />
	</c:url>
	<html:link href="${authoringUrl}" styleClass="btn btn-default" target="_blank">
		<fmt:message key="button.editActivity" />
	</html:link>
</p>


