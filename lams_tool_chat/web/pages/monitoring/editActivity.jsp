<%@ include file="/common/taglibs.jsp"%>

<c:set var="dto" value="${requestScope.monitoringDTO}" />

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
		<c:when test='${dto.chatEditable == "true"}'>
			<c:url value="/authoring.do" var="authoringUrl">
				<c:param name="toolContentID" value="${dto.toolContentId}" />
			</c:url>
			<html:link href="${authoringUrl}" styleClass="button" target="_blank">
				<fmt:message key="button.editActivity" />
			</html:link>
		</c:when>
		<c:otherwise>
			<fmt:message key="message.contentInUseSet" />
		</c:otherwise>
	</c:choose>
</p>


