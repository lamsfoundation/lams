<%@ include file="/includes/taglibs.jsp"%>

<div class="voffset10">

<c:if test="${formBean.totalLearners >= 1}">
	<lams:Alert type="warn" id="no-edit" close="false">
		<fmt:message key="message.alertContentEdit" />
	</lams:Alert>
</c:if>

<table class="table table-condensed">
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
	<html:link forward="forwardToAuthorPage" name="NbMonitoringForm" property="parametersToAppend" styleClass="btn btn-default">
		<fmt:message key="button.edit" />
	</html:link>
</p>

</div>

