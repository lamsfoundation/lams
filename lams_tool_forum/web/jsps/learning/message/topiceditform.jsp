<%@ include file="/common/taglibs.jsp"%>

<div class="form-group">
    <label><fmt:message key="message.label.subject" />&nbsp;</label>
    <form:input size="50" tabindex="1" value="${message.subject}" path="message.subject" maxlength="60"/> &nbsp;
	<form:errors path="message.subject" />
	<c:set var="errorKey" value="message.subject" />
	        <c:if test="${not empty errorMap and not empty errorMap[errorKey]}">
	            <lams:Alert id="error" type="danger" close="false">
	                <c:forEach var="error" items="${errorMap[errorKey]}">
	                    <c:out value="${error}" />
	                </c:forEach>
	            </lams:Alert>
	        </c:if>
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
<button name="goback" id="cancelButton" onclick="javascript:cancelEdit();" class="btn btn-default">
	<fmt:message key="button.cancel" />
</button>

<input type="submit" class="btn btn-default" id="submitButton" value="<fmt:message key="button.submit" />"/>
</div>
