<%@ include file="/common/taglibs.jsp"%>

<c:set var="details" value="${fileDetails}" />
<c:set var="user" value="${user}" />
<c:set var="toolSessionID" value="${toolSessionID}" />

<table cellpadding="0">

	<tr>
		<td colspan="2">
			<fmt:message key="label.assign.mark.message.prefix" />
			:
			<c:out value="${user.login}" />
			,
			<c:out value="${user.firstName}" />
			<c:out value="${user.lastName}" />
		</td>
	</tr>

	<tr>
		<td class="field-name" width="30%">
			<fmt:message key="label.learner.filePath" />
			:
		</td>
		<td>
			<c:out value="${details.filePath}" escapeXml="false" />
		</td>
	</tr>
	<tr>
		<td class="field-name" width="30%">
			<fmt:message key="label.learner.fileDescription" />
			:
		</td>
		<td>
			<c:out value="${details.fileDescription}" escapeXml="false" />
		</td>
	</tr>
	<tr>
		<td class="field-name" width="30%">
			<fmt:message key="label.learner.dateOfSubmission" />
			:
		</td>
		<td>
			<c:out value="${details.dateOfSubmission}" />
		</td>
	</tr>

	<tr>
		<td colspan="2">
			<html:errors />
		</td>
	</tr>
	<input type="hidden" name="toolSessionID" value="<c:out value='${toolSessionID}'/>" />
	<input type="hidden" name="reportID" value="<c:out value='${details.reportID}'/>" />
	<input type="hidden" name="userID" value="<c:out value='${user.userID}'/>" />
	<tr>
		<td class="field-name">
			<fmt:message key="label.learner.marks" />
			:
		</td>
		<td>
			<input type="text" name="marks" value=<c:out value="${details.marks}"  escapeXml="false"/>>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<lams:SetEditor id="Comments" text="${details.comments}" small="true" key="label.learner.comments" />
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<html:link href="javascript:doSubmit('updateMarks');" property="submit" styleClass="button">
				<bean:message key="label.monitoring.saveMarks.button" />
			</html:link>
		</td>
	</tr>
</table>
