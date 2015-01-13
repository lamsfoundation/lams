<%@ include file="/common/taglibs.jsp"%>

<div class="field-name title-space-top">
	<fmt:message key="message.label.subject" />
</div>

<html:text size="50" tabindex="1" property="message.subject" maxlength="60" />
<html:errors property="message.subject" />

<div class="field-name title-space-top">
	<fmt:message key="message.label.body" />
	*
</div>

<%@include file="bodyarea.jsp"%>

<c:if test="${topic.hasAttachment || sessionMap.allowUpload}">

	<div class="field-name title-space-top">
		<fmt:message key="message.label.attachment" />
	</div>

	<c:set var="allowUpload" value="${sessionMap.allowUpload}" />

	<div id="itemAttachmentArea">
		<%@ include file="/jsps/learning/message/msgattachment.jsp"%>
	</div>

</c:if>

<div class="space-bottom-top">
	<div class="right-buttons">
		<html:button property="goback"
			onclick="javascript:location.href='${backToTopic}';"
			styleClass="button">
			<fmt:message key="button.cancel" />
		</html:button>
		<c:set var="backToTopic">
			<html:rewrite
				page="/learning/viewTopic.do?sessionMapID=${sessionMapID}&topicID=${sessionMap.rootUid}&create=${topic.message.created.time}&hideReflection=${sessionMap.hideReflection}" />
		</c:set>
		<html:submit styleClass="button">
			<fmt:message key="button.submit" />
		</html:submit>
	</div>
</div>
