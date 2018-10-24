<%@ include file="/common/taglibs.jsp"%>

<div class="form-group">
	<label><fmt:message key="message.label.subject" />&nbsp;</label>&nbsp;
	<form:input cssClass="form-control" tabindex="1" path="message.subject" value="${message.subject}" maxlength="60" />
	&nbsp;
	<lams:errors path="message.subject"/>
</div>
<div class="form-group">
	<label><fmt:message key="message.label.body" /> *</label><BR />
	<%@include file="bodyarea.jsp"%>
</div>
<c:if test="${sessionMap.allowUpload}">

	<div class="form-group">
 		<lams:FileUpload fileFieldname="attachmentFile" maxFileSize="${sessionMap.uploadMaxFileSize}" tabindex="3" />
 		<lams:errors path="message.attachments"/>
	</div>
	
	<lams:WaitingSpinner id="itemAttachmentArea_Busy"/>
</c:if>

<div class="btn-group-xs voffset5 pull-right">
	<button name="goback" id="cancelButton" onclick="javascript:cancelReply();"
		class="btn btn-default roffset5">
		<fmt:message key="button.cancel" />
	</button>
	<input type="submit" class="btn btn-default" id="submitButton" value="<fmt:message key="button.submit" />"/>
</div>
