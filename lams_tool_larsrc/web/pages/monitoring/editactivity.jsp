<%@ include file="/common/taglibs.jsp"%>

<table cellpadding="0">
	<tr>
		<td>
			<fmt:message key="label.authoring.basic.title" />
			:
		</td>
		<td>
			<c:out value="${resource.title}" escapeXml="false" />
		</td>
	</tr>

	<tr>
		<td>
			<fmt:message key="label.authoring.basic.instruction" />
			:
		</td>
		<td>
			<c:out value="${resource.instructions}" escapeXml="false" />
		</td>
	</tr>

	<tr>
		<td colspan="2">
			<c:set var="isPageEditable" value="${isPageEditable}" />
			<c:choose>
				<c:when test='${isPageEditable == "true"}'>
					<c:url value="/authoring/init.do" var="authoringUrl">
						<c:param name="toolContentID" value="${toolContentID}" />
					</c:url>
					<html:link href="${authoringUrl}" styleClass="button" target="_blank">
						<fmt:message key="label.monitoring.edit.activity.edit" />
					</html:link>
				</c:when>
				<c:otherwise>
					<div align="center">
						<b> <fmt:message key="message.monitoring.edit.activity.not.editable" /> </b>
					</div>
				</c:otherwise>
			</c:choose>
		</td>
	</tr>
</table>
