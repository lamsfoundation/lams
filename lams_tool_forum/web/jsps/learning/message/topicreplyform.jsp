<%@ include file="/common/taglibs.jsp"%>

<div class="mb-3">
	<label><fmt:message key="message.label.subject" />&nbsp;</label>&nbsp;
	<form:input cssClass="form-control" tabindex="1" path="message.subject" value="${message.subject}" maxlength="60" />
	&nbsp;
	<lams:errors path="message.subject"/>
</div>
<div class="mb-3">
	<label><fmt:message key="message.label.body" /> *</label><BR />
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
	
	<lams:WaitingSpinner id="itemAttachmentArea_Busy"/>
</c:if>

<div class="btn-group-sm mt-2 float-end">
	<button name="goback" id="cancelButton" onclick="javascript:cancelReply();"
		class="btn btn-secondary me-2">
		<fmt:message key="button.cancel" />
	</button>
	<input type="submit" class="btn btn-secondary" id="submitButton" value="<fmt:message key="button.submit" />"/>
</div>
