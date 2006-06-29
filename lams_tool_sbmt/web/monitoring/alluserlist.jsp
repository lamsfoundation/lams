<%@include file="/common/taglibs.jsp"%>

<c:set var="sessionUserMap" scope="request" value="${sessionUserMap}" />
<logic:notEmpty name="sessionUserMap">
	<logic:iterate id="element" name="sessionUserMap">
		<bean:define id="sessionDto" name="element" property="key" />
		<bean:define id="userlist" name="element" property="value" />
		<table cellpadding="0">
			<tr>
				<th colspan="3">
					<fmt:message key="label.session.name" />
					:
					<c:out value="${sessionDto.sessionName}" />
				</th>
			</tr>
			<logic:notEmpty name="userlist">
				<logic:iterate id="user" name="userlist">
					<tr>
						<td>	
							<bean:define id="details" name="user" property="userID" />
							<html:hidden property="userID" value="${details}" />
							<html:hidden property="toolSessionID" value="${sessionDto.sessionID}" />							
							
							<bean:write name="user" property="firstName" />
							<bean:write name="user" property="lastName" />
						</td>
						<td>
							<bean:write name="user" property="login" />
						</td>
						<td>
							<html:link href="javascript:doSubmit('getFilesUploadedByUser', 5);" property="Mark" styleClass="button">
								<bean:message key="label.monitoring.Mark.button" />
							</html:link>
						</td>
					</tr>
				</logic:iterate>
			</logic:notEmpty>
			<logic:empty name="userlist">
				<tr>
					<td colspan="3">
						<fmt:message key="label.no.user.available" />
					</td>
				</tr>
			</logic:empty>
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
	</logic:iterate>
</logic:notEmpty>
