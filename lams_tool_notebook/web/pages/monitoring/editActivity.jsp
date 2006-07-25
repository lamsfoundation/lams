<%@ include file="/common/taglibs.jsp"%>

<c:set var="dto" value="${notebookDTO}" />

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
	</tbody>
</table>

<p align="right">
	<c:choose>
		<c:when test="${not dto.contentInUse}">
			<c:url value="/authoring.do" var="authoringUrl">
				<c:param name="toolContentID" value="${dto.toolContentId}" />
				<c:param name="mode" value="teacher" />
			</c:url>
			<html:link href="${authoringUrl}" styleClass="button" target="_blank">
				<fmt:message key="button.editActivity" />
			</html:link>
		</c:when>
		<c:otherwise>
			<p>
				<fmt:message key="message.contentInUseSet" />
			</p>
		</c:otherwise>
	</c:choose>
</p>


