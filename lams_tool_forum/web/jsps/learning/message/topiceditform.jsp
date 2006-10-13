<%@ include file="/includes/taglibs.jsp"%>

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
	<c:if test="${topic.hasAttachment || sessionMap.allowUpload}">
		<tr>
			<td>
				<span class="field-name"><bean:message key="message.label.attachment" /></span>
				<c:set var="allowUpload" value="${sessionMap.allowUpload}" />
				<div id="itemAttachmentArea">
					<%@ include file="/jsps/learning/message/msgattachment.jsp"%>
				</div>		
			</td>
		</tr>
	</c:if>
		<tr>
			<td>
<div class="right-buttons">
			<html:submit styleClass="button">
				<bean:message key="button.submit" />
			</html:submit>
			<c:set var="backToTopic">
				<html:rewrite page="/learning/viewTopic.do?sessionMapID=${sessionMapID}&topicID=${sessionMap.rootUid}&create=${topic.message.created.time}" />
			</c:set>
			<html:button property="goback" onclick="javascript:location.href='${backToTopic}';" styleClass="button">
				<bean:message key="button.cancel" />
			</html:button>
			</td>
		</tr>
</div>
</table>
