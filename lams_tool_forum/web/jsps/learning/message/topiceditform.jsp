<%@ include file="/common/taglibs.jsp"%>

<div class="mb-3">
    <label><fmt:message key="message.label.subject" />&nbsp;</label>
    <form:input size="50" tabindex="1" value="${message.subject}" path="message.subject" maxlength="60"/> &nbsp;
	<form:errors path="message.subject"/>
	<lams:errors path="message.subject"/>
</div>

<div class="mb-3">
    <label><fmt:message key="message.label.body" />  *</label><BR/>
	<%@include file="bodyarea.jsp"%>
</div>

<c:if test="${topic.hasAttachment || sessionMap.allowUpload}">
	<c:set var="allowUpload" value="${sessionMap.allowUpload}" />

	<div id="itemAttachmentArea">
		<%@ include file="/jsps/learning/message/msgattachment.jsp"%>
	</div>

	<lams:WaitingSpinner id="itemAttachmentArea_Busy"/>
</c:if>

<div class="btn-group-sm mt-2 float-end">
	<button name="goback" id="cancelButton" onclick="javascript:cancelEdit();" class="btn btn-secondary">
		<fmt:message key="button.cancel" />
	</button>

	<input type="submit" class="btn btn-secondary" id="submitButton" value="<fmt:message key="button.submit" />"/>
</div>
