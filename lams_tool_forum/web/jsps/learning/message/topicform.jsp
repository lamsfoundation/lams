<%@ include file="/common/taglibs.jsp"%>

<div class="mb-3">
    <label><fmt:message key="message.label.subject" />&nbsp;</label>
    <form:input size="50" tabindex="1" value="${message.subject}" path="message.subject" maxlength="60"/> &nbsp;
    <lams:errors path="message.subject"/>
</div>

<div class="mb-3">
    <label><fmt:message key="message.label.body" />  *</label><BR/>
	<%@include file="bodyarea.jsp"%>
</div>

<c:if test="${sessionMap.allowUpload}">
	<div class="mb-3">
		<input type="hidden" id="tmpFileUploadId" name="tmpFileUploadId"
	   		value="${messageForm.tmpFileUploadId}" />
		<label for="image-upload-area"><fmt:message key="message.label.attachment" /></label>
		<div id="image-upload-area" class="mt-4"></div>
		<script>
			initFileUpload('${messageForm.tmpFileUploadId}', true, '<lams:user property="localeLanguage"/>');
		</script>
		<lams:errors path="message.attachments"/>
	</div>
</c:if>

<c:set var="backToForum">
	<lams:WebAppURL />learning/viewForum.do?toolSessionID=${sessionMap.toolSessionID}&hideReflection=${sessionMap.hideReflection}
</c:set>
<input type="submit" value="<fmt:message key="button.submit" />" class="btn btn-primary mt-2 float-end" id="submitButton"/>

<button name="goback" onclick="javascript:location.href='${backToForum}';" class="btn btn-secondary mt-2 float-end" id="cancelButton">
	<fmt:message key="button.cancel" />
</button>&nbsp;

