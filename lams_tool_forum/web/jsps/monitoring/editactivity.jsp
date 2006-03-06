<%@ include file="/includes/taglibs.jsp"%>

<table>
	<tr>
		<td>
			<fmt:message key="label.authoring.basic.title" />
			:
		</td>
		<td>
			<c:out value="${title}" escapeXml="false" />
		</td>
	</tr>

	<tr>
		<td>
			<fmt:message key="label.authoring.basic.instruction" />
			:
		</td>
		<td>
			<c:out value="${instruction}" escapeXml="false" />
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<c:set var="isPageEditable" value="${isPageEditable}" />
			<c:choose>
				<c:when test='${isPageEditable == "true"}'>
					<html:link forward="forwardToAuthorPage" name="monitorForm" property="parametersToAppend" styleClass="button" target="_blank">
						<fmt:message key="label.monitoring.edit.activity.edit" />
					</html:link>
					
					
				</c:when>
				<c:otherwise>
					<fmt:message key="message.monitoring.edit.activity.not.editable" />
				</c:otherwise>
			</c:choose>
		</td>
	</tr>
</table>
