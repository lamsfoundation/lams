<%@ include file="/includes/taglibs.jsp"%>

<c:if test="${formBean.totalLearners >= 1}">
	<p class="warning">
		<fmt:message key="message.alertContentEdit" />
	</p>
</c:if>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td class="field-name">
					<fmt:message key="basic.title" />
				</td>
				<td>
					<c:out value="${formBean.title}" escapeXml="true" />
				</td>
			</tr>
			<tr>
				<td class="field-name">
					<fmt:message key="basic.content" />
				</td>
				<td>
					<c:out value="${formBean.basicContent}" escapeXml="false" />
				</td>
			</tr>
</table>

<p class="align-right">
	<html:link forward="forwardToAuthorPage" name="NbMonitoringForm" property="parametersToAppend" styleClass="button" target="_blank">
		<fmt:message key="button.edit" />
	</html:link>
</p>

