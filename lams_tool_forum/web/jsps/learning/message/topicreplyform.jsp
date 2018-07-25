<%@ include file="/common/taglibs.jsp"%>

<div class="form-group">
	<label><fmt:message key="message.label.subject" />&nbsp;</label>&nbsp;
	<html:text styleClass="form-control" tabindex="1" property="message.subject" maxlength="60" />
	&nbsp;
	<html:errors property="message.subject" />
</div>
<div class="form-group">
	<label><fmt:message key="message.label.body" /> *</label><BR />
	<%@include file="bodyarea.jsp"%>
</div>
<c:if test="${sessionMap.allowUpload}">

	<div class="form-group">
 		<lams:FileUpload fileFieldname="attachmentFile" maxFileSize="${sessionMap.uploadMaxFileSize}" tabindex="3" />
 		<html:errors property="message.attachment" />
	</div>
	
	<lams:WaitingSpinner id="itemAttachmentArea_Busy"/>
</c:if>

<div class="btn-group-xs voffset5 pull-right">
	<html:button property="goback" styleId="cancelButton" onclick="javascript:cancelReply();"
		styleClass="btn btn-default roffset5">
		<fmt:message key="button.cancel" />
	</html:button>
	<html:submit styleClass="btn btn-default" styleId="submitButton">
		<fmt:message key="button.submit" />
	</html:submit>
</div>
