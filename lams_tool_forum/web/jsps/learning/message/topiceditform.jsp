<%@ include file="/common/taglibs.jsp"%>

<div class="mb-3">
    <label for="message.subject">
    	<fmt:message key="message.label.subject" />&nbsp;
    </label>
    <form:input cssClass="form-control" value="${message.subject}" path="message.subject" maxlength="60"/> &nbsp;

	<lams:errors path="message.subject"/>
</div>

<div class="mb-3">
    <label id="label-body">
    	<fmt:message key="message.label.body" />  *
    </label>
    <BR/>
    
	<%@include file="bodyarea.jsp"%>
</div>

<c:if test="${topic.hasAttachment || sessionMap.allowUpload}">
	<c:set var="allowUpload" value="${sessionMap.allowUpload}" />

	<div id="itemAttachmentArea">
		<%@ include file="/jsps/learning/message/msgattachment.jsp"%>
	</div>

	<lams:WaitingSpinner id="itemAttachmentArea_Busy"/>
</c:if>

<c:if test="${sessionMap.allowAnonym}">
	<div class="checkbox form-control-inline form-check form-check-reverse">
		<form:checkbox path="message.isAnonymous" id="isAnonymous" cssClass="form-check-input"/> 
				
		<label for="isAnonymous" class="form-check-label">	
			<fmt:message key="label.post.anonomously" />
		</label>

		<span data-bs-toggle="popover" data-bs-placement="right" data-bs-trigger="hover focus" aria-hidden="true">
			<i class="fa fa-info-circle"></i>
		</span>
	</div>
</c:if>

<div class="btn-group-sm mt-2 float-end">
	<button type="button" name="goback" id="cancelButton" onclick="javascript:cancelEdit();" class="btn btn-secondary btn-icon-cancel">
		<fmt:message key="button.cancel" />
	</button>

	<button type="submit" class="btn btn-secondary" id="submitButton">
		<i class="fa-solid fa-check me-1"></i>
		<fmt:message key="button.submit" />
	</button>
</div>
