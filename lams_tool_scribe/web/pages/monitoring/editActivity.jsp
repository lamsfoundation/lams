<%@ include file="/common/taglibs.jsp"%>

<c:set var="dto" value="${requestScope.monitoringDTO}" />

<table cellspacing="0">
	<tbody>
		<tr>
			<td class="field-name" style="width: 30%;">
				<fmt:message key="label.authoring.basic.title" />
			</td>
			<td>
				<c:out value="${dto.title}" escapeXml="true" />
			</td>
		</tr>
		<tr>
			<td class="field-name" style="width: 30%;">
				<fmt:message key="label.authoring.basic.instructions" />
			</td>
			<td>
				<c:out value="${dto.instructions}" escapeXml="false" />
			</td>
		</tr>
	</tbody>
</table>

<c:choose>
	<c:when test="${not dto.contentInUse}">
		<c:url value="/authoring.do" var="authoringUrl">
			<c:param name="toolContentID" value="${dto.toolContentID}" />
			<c:param name="mode" value="teacher" />
			<c:param name="contentFolderID" value="${contentFolderID}"></c:param>
		</c:url>
		<html:link href="${fn:escapeXml(authoringUrl)}"
			styleClass="button right-buttons" target="_blank">
			<fmt:message key="button.editActivity" />
		</html:link>
	</c:when>
	<c:otherwise>
		<div class="warning">
			<fmt:message key="message.contentInUseSet" /> 
		</div>
	</c:otherwise>
</c:choose>

