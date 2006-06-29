<%@ include file="/common/taglibs.jsp"%>

<input type="hidden" name="toolContentID" value="<c:out value='${sessionScope.toolContentID}'/>" />
<table cellpadding="0">
	<tr>
		<td class="field-name" width="30%">
			<bean:message key="label.authoring.basic.title" />
			:
		</td>
		<td>
			<c:out value="${authoring.title}" escapeXml="false" />
		</td>
	</tr>
	<!-- Instructions Row -->
	<tr>
		<td class="field-name" width="30%">
			<bean:message key="label.authoring.basic.instruction" />
			:
		</td>
		<td>
			<c:out value="${authoring.instruction}" escapeXml="false" />
		</td>
	</tr>
	<tr>
		<c:set var="isPageEditable" value="${isPageEditable}" />
		<c:choose>
			<c:when test='${isPageEditable == "true"}'>
				<td colspan="2">
					<c:url value="/authoring.do" var="authoringUrl">
						<c:param name="mode" value="teacher" />
						<c:param name="toolContentID" value="${sessionScope.toolContentID}" />
					</c:url>
					<html:link href="${authoringUrl}" styleClass="button" target="_blank">
						<fmt:message key="label.monitoring.edit.activity.edit" />
					</html:link>
				</td>
			</c:when>
			<c:otherwise>
				<td colspan="2">
					<p>
						<fmt:message key="message.monitoring.edit.activity.not.editable" />
					</p>
				</td>
			</c:otherwise>
		</c:choose>
	</tr>
</table>
