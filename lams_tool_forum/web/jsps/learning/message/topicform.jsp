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

<c:if test="${sessionMap.allowUpload}">
	<div class="form-group">
		<label><fmt:message key="message.label.attachment" /></label><BR/>
		<html:file tabindex="3" property="attachmentFile" /><BR/>
		<p class="help-block">
			<fmt:message key="label.upload.info">
				<fmt:param>${sessionMap.uploadMaxFileSize}</fmt:param>
			</fmt:message>
		</p>
		<html:errors property="message.attachment" />
	</div>
</c:if>

<c:set var="backToForum">
	<html:rewrite page="/learning/viewForum.do?toolSessionID=${sessionMap.toolSessionID}&hideReflection=${sessionMap.hideReflection}" />
</c:set>
<html:submit styleClass="btn btn-primary voffset5 pull-right" styleId="submitButton">
	<fmt:message key="button.submit" />
</html:submit>
<html:button property="goback" onclick="javascript:location.href='${backToForum}';" styleClass="btn btn-default voffset5 pull-right" styleId="cancelButton">
	<fmt:message key="button.cancel" />
</html:button>&nbsp;

