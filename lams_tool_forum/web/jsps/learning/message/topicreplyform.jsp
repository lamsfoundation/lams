<%@ include file="/common/taglibs.jsp"%>

<div class="field-name title-space-top">
	<fmt:message key="message.label.subject" />
</div>

<html:text size="30" tabindex="1" property="message.subject"
	maxlength="60" />
<html:errors property="message.subject" />

<div class="field-name title-space-top">
	<fmt:message key="message.label.body" />
	*
</div>
<%@include file="bodyarea.jsp"%>

<c:if test="${sessionMap.allowUpload}">

	<div class="field-name title-space-top">
		<fmt:message key="message.label.attachment" />
	</div>
	<html:file tabindex="3" property="attachmentFile" />

	<div style="font-style: italic; margin-top: 10px">
		<fmt:message key="label.upload.info">
			<fmt:param>${sessionMap.uploadMaxFileSize}</fmt:param>
		</fmt:message>
	</div>
	<html:errors property="message.attachment" />

</c:if>
<div class="space-bottom-top">
	<div class="right-buttons">
		<html:button property="goback" styleId="cancelButton" 
			onclick="javascript:cancelReply();"
			styleClass="button">
			<fmt:message key="button.cancel" />
		</html:button>
		<html:submit styleClass="button" styleId="submitButton" >
			<fmt:message key="button.submit" />
		</html:submit>
	</div>
</div>
