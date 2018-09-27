<%@ include file="/common/taglibs.jsp"%>

<div class="form-group">
    <label><fmt:message key="message.label.subject" />&nbsp;</label>
    <form:input size="50" tabindex="1" value="${message.subject}" path="message.subject" maxlength="60"/> &nbsp;
    <lams:errors path="message.subject"/>
</div>

<div class="form-group">
    <label><fmt:message key="message.label.body" />  *</label><BR/>
	<%@include file="bodyarea.jsp"%>
</div>

<c:if test="${sessionMap.allowUpload}">
	<div class="form-group">
		<label><fmt:message key="message.label.attachment" /></label><BR/>
		<lams:FileUpload fileFieldname="attachmentFile" maxFileSize="${sessionMap.uploadMaxFileSize}" tabindex="3" />
		<lams:errors path="message.attachments"/>
	</div>
</c:if>

<c:set var="backToForum">
	<lams:WebAppURL />learning/viewForum.do?toolSessionID=${sessionMap.toolSessionID}&hideReflection=${sessionMap.hideReflection}
</c:set>
<input type="submit" value="<fmt:message key="button.submit" />" class="btn btn-primary voffset5 pull-right" id="submitButton"/>

<button name="goback" onclick="javascript:location.href='${backToForum}';" class="btn btn-default voffset5 pull-right" id="cancelButton">
	<fmt:message key="button.cancel" />
</button>&nbsp;

