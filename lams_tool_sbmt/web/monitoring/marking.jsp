<%@ include file="/common/taglibs.jsp"%>

<table cellpadding="0">
	<tr>
		<td>
			<html:link href="javascript:doSubmit('viewAllMarks', 5);" property="viewAllMarks" styleClass="button">
				<bean:message key="label.monitoring.viewAllMarks.button" />
			</html:link>
		</td>
		<td>
			<html:link href="javascript:doSubmit('releaseMarks');" property="releaseMarks" styleClass="button">
				<bean:message key="label.monitoring.releaseMarks.button" />
			</html:link>
		</td>
		<td>
			<html:link href="javascript:doSubmit('downloadMarks');" property="downloadMarks" styleClass="button">
				<bean:message key="label.monitoring.downloadMarks.button" />
			</html:link>
		</td>
	</tr>
</table>
