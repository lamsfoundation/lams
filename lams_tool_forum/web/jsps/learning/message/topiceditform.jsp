<%@ include file="/common/taglibs.jsp"%>

<div class="form-group">
    <label><fmt:message key="message.label.subject" />&nbsp;</label>
	<html:text size="50" tabindex="1" property="message.subject" maxlength="60"/> &nbsp;
	<html:errors property="message.subject" />
</div>

<div class="form-group">
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

<div class="btn-group-xs voffset5 pull-right">
<html:button property="goback" styleId="cancelButton" onclick="javascript:cancelEdit();" styleClass="btn btn-default">
	<fmt:message key="button.cancel" />
</html:button>
<html:submit styleClass="btn btn-default" styleId="submitButton">
	<fmt:message key="button.submit" />
</html:submit>
</div>
