<%@ include file="/common/taglibs.jsp"%>

<c:set var="dto" value="${requestScope.monitoringDTO}" />

<c:if test="${dto.contentInUse}">
	<lams:Alert type="warn" id="no-edit" close="false">
		<fmt:message key="message.alertContentEdit" />
	</lams:Alert>
</c:if>

<table class="table table-condensed">
	<tbody>
		<tr>
			<td class="field-name" style="width: 10%;" nowrap>
				<fmt:message key="label.authoring.basic.title" />
			</td>
			<td>
				<c:out value="${dto.title}" escapeXml="true" />
			</td>
		</tr>
		<tr>
			<td class="field-name" style="width: 10%;" nowrap valign="top">
				<fmt:message key="label.authoring.basic.instructions" />
			</td>
			<td>
				<c:out value="${dto.instructions}" escapeXml="false" />
			</td>
		</tr>
	</tbody>
</table>

<c:url value="/authoring/authoring.do" var="authoringUrl">
	<c:param name="toolContentID" value="${dto.toolContentId}" />
	<c:param name="mode" value="teacher" />
	<c:param name="contentFolderID" value="${contentFolderID}"></c:param>
</c:url>
<a href="#" onClick="javascript:launchPopup('${authoringUrl}')" class="btn btn-default pull-right">
	<fmt:message key="button.editActivity" />
</a>

