<%@ include file="/includes/taglibs.jsp"%>
<html:errors />

<div class="datatablecontainer">
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
			<td>
				<html:link forward="forwardToAuthorPage" styleClass="button">
					<fmt:message key="label.monitoring.edit.activity.edit" />
				</html:link>
			</td>
			<td>
				&nbsp;
			</td>
		</tr>
	</table>
</div>
