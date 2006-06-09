<table width="80%" cellspacing="8" align="CENTER" class="forms">
	<tr>
		<td valign="MIDDLE">
			<b><bean:message key="message.label.subject" /></b><b class="required">*</b>
		</td>
		<td valign="MIDDLE" align="left">
			<html:text size="30" tabindex="1" property="message.subject" />
			<html:errors property="message.subject" />
		</td>
	</tr>
	<tr>
		<td valign="MIDDLE">
			<b><bean:message key="message.label.body" /></b><b class="required">*</b>
		</td>
		<td valign="TOP" align="left">
			<%@include file="bodyarea.jsp"%>
		</td>
	</tr>
	<tr>
		<td valign="MIDDLE">
			<b><bean:message key="message.label.attachment" /></b>
		</td>
		<td valign="MIDDLE" align="left">
			<html:file tabindex="3" property="attachmentFile" />
			<html:errors property="message.attachment" />
		</td>
	</tr>
	<tr>
		<td></td>
		<td align="left">
			<BR>
			<html:submit styleClass="buttonStyle" style="width:120px">
				<bean:message key="button.submit" />
			</html:submit>
			<c:set var="backToTopic">
				<html:rewrite page="/learning/viewTopic.do?topicId=${rootUid}&create=${message.created.time}" />
			</c:set>
			<html:button property="goback" onclick="javascript:location.href='${backToTopic}';" styleClass="buttonStyle" style="width:120px">
				<bean:message key="button.cancel" />
			</html:button>
		</td>
	</tr>
</table>
