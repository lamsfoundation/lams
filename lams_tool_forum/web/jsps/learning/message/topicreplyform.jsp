<%@ include file="/common/taglibs.jsp"%>

<div class="form-group">
    <label><fmt:message key="message.label.subject" />&nbsp;</label>&nbsp;
	<html:text size="50" tabindex="1" property="message.subject" maxlength="60"/>&nbsp;
	<html:errors property="message.subject" />
  </div>

 <div class="form-group">
    <label><fmt:message key="message.label.body" /> *</label><BR/>
	<%@include file="bodyarea.jsp"%>
</div>

<c:if test="${sessionMap.allowUpload}">

	 <div class="form-group">
    	<label><fmt:message key="message.label.attachment" />&nbsp;</label>
		<html:file tabindex="3" property="attachmentFile" />

		<div style="font-style: italic; margin-top: 10px">
			<fmt:message key="label.upload.info">
				<fmt:param>${sessionMap.uploadMaxFileSize}</fmt:param>
			</fmt:message>
		</div>
		<html:errors property="message.attachment" />
	</div>
</c:if>

<div class="btn-group-xs voffset5">
<html:button property="goback" styleId="cancelButton" onclick="javascript:cancelReply();" styleClass="btn btn-default">
	<fmt:message key="button.cancel" />
</html:button>
<html:submit styleClass="btn btn-default" styleId="submitButton">
	<fmt:message key="button.submit" />
</html:submit>
</div>
