<%@ include file="/includes/taglibs.jsp"%>
	<div id="basic">
		<table class="forms">
			<!-- Title Row -->
			<tr>
				<td class="formlabel">
					<fmt:message key="label.authoring.basic.title" />
					:
				</td>
				<td class="formcontrol">
					<c:out value="${title}" escapeXml="false" />
				</td>
			</tr>
			<!-- Instructions Row -->
			<tr>
				<td class="formlabel">
					<fmt:message key="label.authoring.basic.instruction" />
					:
				</td>
				<td class="formcontrol">
					<c:out value="${instruction}" escapeXml="false" />
				</td>
			</tr>
			<tr>
				<td class="formlabel" colspan="2">
					<html:errors property="activity.globel" />
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<html:link forward="forwardToAuthorPage" styleClass="button">
						<fmt:message key="label.monitoring.edit.activity.edit" />
					</html:link>
				</td>
			</tr>
		</table>
	</div>