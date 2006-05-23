<%@ include file="/common/taglibs.jsp"%>

<c:set var="dto" value="${requestScope.monitoringDTO}" />

<div class="datatablecontainer">
	<table class="forms">
		<tr>
			<td class="formlabel">
				<fmt:message key="label.authoring.basic.title" />
			</td>
			<td class="formcontrol">
				<c:out value="${dto.title}" escapeXml="false" />
			</td>
		</tr>
		<tr>
			<td class="formlabel">
				<fmt:message key="label.authoring.basic.instructions" />
			</td>
			<td class="formcontrol">
				<c:out value="${dto.instructions}" escapeXml="false" />
			</td>
		</tr>
		<tr>
			<td>
				&nbsp;
			</td>
			<td class="formcontrol">
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
			</td>
		</tr>
	</table>
</div>
