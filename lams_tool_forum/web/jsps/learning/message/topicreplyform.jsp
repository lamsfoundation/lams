<table cellpadding="0">
	<tr>
		<td class="field-name">
			<bean:message key="message.label.subject" />*
		</td>
		<td>
			<html:text size="30" tabindex="1" property="message.subject" />
			<html:errors property="message.subject" />
		</td>
	</tr>
	<tr>
		<td class="field-name">
			<bean:message key="message.label.body" />*
		</td>
		<td>
			<%@include file="bodyarea.jsp"%>
		</td>
	</tr>
	<tr>
		<td class="field-name">
			<b><bean:message key="message.label.attachment" /></b>
		</td>
		<td>
			<html:file tabindex="3" property="attachmentFile" />
			<html:errors property="message.attachment" />
		</td>
	</tr>
	<tr>
		<td></td>
		<td>
			
			<html:submit styleClass="button">
				<bean:message key="button.submit" />
			</html:submit>
			<c:set var="backToTopic">
				<html:rewrite page="/learning/viewTopic.do?topicId=${rootUid}&create=${message.created.time}" />
			</c:set>
			<html:button property="goback" onclick="javascript:location.href='${backToTopic}';" styleClass="button">
				<bean:message key="button.cancel" />
			</html:button>
		</td>
	</tr>
</table>
