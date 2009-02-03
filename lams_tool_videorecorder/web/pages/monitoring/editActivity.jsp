<%@ include file="/common/taglibs.jsp"%>

<c:set var="dto" value="${videoRecorderDTO}" />

<c:if test="${dto.contentInUse}">
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
				<c:out value="${dto.title}" escapeXml="false" />
			</td>
		</tr>
		<tr>
			<td class="field-name" width="30%">
				<fmt:message key="label.authoring.basic.instructions" />
			</td>
			<td>
				<c:out value="${dto.instructions}" escapeXml="false" />
			</td>
		</tr>
		<tr>
			<td class="field-name" width="30%">
				<fmt:message key="advanced.allowUseVoice" />
			</td>
			<td>
				<c:out value="${dto.allowUseVoice}" escapeXml="false" />
			</td>
		</tr>
		<tr>
			<td class="field-name" width="30%">
				<fmt:message key="advanced.allowUseCamera" />
			</td>
			<td>
				<c:out value="${dto.allowUseCamera}" escapeXml="false" />
			</td>
		</tr>
		<tr>
			<td class="field-name" width="30%">
				<fmt:message key="advanced.allowLearnerVideoVisibility" />
			</td>
			<td>
				<c:out value="${dto.allowLearnerVideoVisibility}" escapeXml="false" />
			</td>
		</tr>
		<tr>
			<td class="field-name" width="30%">
				<fmt:message key="advanced.allowLearnerVideoExport" />
			</td>
			<td>
				<c:out value="${dto.allowLearnerVideoExport}" escapeXml="false" />
			</td>
		</tr>
		<tr>
			<td class="field-name" width="30%">
				<fmt:message key="advanced.allowComments" />
			</td>
			<td>
				<c:out value="${dto.allowComments}" escapeXml="false" />
			</td>
		</tr>
		<tr>
			<td class="field-name" width="30%">
				<fmt:message key="advanced.allowRatings" />
			</td>
			<td>
				<c:out value="${dto.allowRatings}" escapeXml="false" />
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
	<html:link href="${authoringUrl}" styleClass="button" target="_blank">
		<fmt:message key="button.editActivity" />
	</html:link>
</p>


