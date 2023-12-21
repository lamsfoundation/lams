<%@ include file="/common/taglibs.jsp"%>

<div class="mb-3">
    <label for="message.subject">
    	<fmt:message key="message.label.subject" />
    </label>
    <form:input value="${message.subject}" path="message.subject" maxlength="60" cssClass="form-control"/>
    <lams:errors5 path="message.subject"/>
</div>

<div class="mb-3">
    <label id="label-body">
    	<fmt:message key="message.label.body" />  *
    </label>
    <BR/>
    
	<%@include file="bodyarea.jsp"%>
</div>

<c:if test="${sessionMap.allowUpload}">
	<div class="mb-3">
		<input type="hidden" id="tmpFileUploadId" name="tmpFileUploadId" value="${messageForm.tmpFileUploadId}" />
		<label for="uppy-upload-button">
			<fmt:message key="message.label.attachment" />
		</label>
		<div id="image-upload-area"></div>
		<script>
			initFileUpload('${messageForm.tmpFileUploadId}', true, '<lams:user property="localeLanguage"/>');
		</script>
		<lams:errors5 path="message.attachments"/>
	</div>
</c:if>

<c:set var="backToForum">
	<lams:WebAppURL />learning/viewForum.do?toolSessionID=${sessionMap.toolSessionID}&hideReflection=${sessionMap.hideReflection}
</c:set>
<button type="submit" class="btn btn-primary mt-2 ms-2 float-end" id="submitButton">
	<i class="fa-solid fa-check me-1"></i>
	<fmt:message key="button.submit" />
</button>

<button type="button" name="goback" onclick="javascript:location.href='${backToForum}';" class="btn btn-secondary btn-icon-cancel mt-2 float-end" id="cancelButton">
	<fmt:message key="button.cancel" />
</button>

