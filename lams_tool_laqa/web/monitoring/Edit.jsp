<%@ include file="/common/taglibs.jsp"%>


<table class="table table-condensed">
	<tr>
		<td class="field-name" width="10%" valign="top">
			<fmt:message key="label.authoring.title" />
		</td>
		<td>
			<c:out value="${content.title}" escapeXml="true" />
		</td>
	</tr>

	<tr>
		<td class="field-name" width="10%" valign="top" NOWRAP>
			<fmt:message key="label.authoring.instructions" />
		</td>
		<td>
			<c:out value="${content.instructions}" escapeXml="false" />
		</td>
	</tr>
</table>

<c:url value="/authoring/authoring.do" var="authoringUrl">
	<c:param name="mode" value="teacher" />
	<c:param name="toolContentID" value="${qaMonitoringForm.toolContentID}" />
	<c:param name="contentFolderID" value="${qaMonitoringForm.contentFolderID}" />
</c:url>
<a href="javascript:;" onclick="launchPopup('${authoringUrl}','definelater')" class="btn btn-default pull-right">
	<fmt:message key="label.edit" />
</a>