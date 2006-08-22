<table cellpadding="0">
	<tr>
		<td>
			<span  class="field-name"><bean:message key="message.label.subject" /></span><BR>		
			<html:text size="30" tabindex="1" property="message.subject" />
			<html:errors property="message.subject" />
		</td>
	</tr>
	<tr>
		<td>
			<span  class="field-name"><bean:message key="message.label.body" />*</span><BR>		
			<%@include file="bodyarea.jsp"%>
		</td>
	</tr>
	<c:if test="${sessionMap.allowUpload}">
		<tr>
			<td>
				<span class="field-name"><bean:message key="message.label.attachment" /></span>
				<html:file tabindex="3" property="attachmentFile" />
				<html:errors property="message.attachment" />
			</td>
		</tr>
	</c:if>
</table>
<div class="right-buttons">
			<html:submit styleClass="button">
				<bean:message key="button.submit" />
			</html:submit>
			<c:set var="backToTopic">
				<html:rewrite page="/learning/viewTopic.do?sessionMapID=${sessionMapID}&topicID=${sessionMap.rootUid}&create=${message.created.time}" />
			</c:set>
			<html:button property="goback" onclick="javascript:location.href='${backToTopic}';" styleClass="button">
				<bean:message key="button.cancel" />
			</html:button>
</div>