<%@ include file="/common/taglibs.jsp"%>
<c:set var="dto" value="${notebookDTO}" />

<c:if test="${dto.contentInUse}">
	<lams:Alert type="warn" id="no-edit" close="false">
		<fmt:message key="message.alertContentEdit" />
	</lams:Alert>
</c:if>


<table class="table table-condensed">
	<tr>
		<td class="field-name" width="10%" valign="top">
			<fmt:message key="label.authoring.basic.title" />
		</td>
		<td>
			<c:out value="${dto.title}" escapeXml="true" />
		</td>
	</tr>

	<tr>
		<td class="field-name" width="10%" valign="top" NOWRAP>
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
<a href="javascript:;" onclick="launchPopup('${authoringUrl}','definelater')" style="btn btn-default pull-right">
	<fmt:message key="button.editActivity" />

