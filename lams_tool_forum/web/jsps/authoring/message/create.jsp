<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration" %>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys" %>
<%@ page import="org.lamsfoundation.lams.util.FileValidatorSpringUtil" %>
<c:set var="UPLOAD_FILE_MAX_SIZE_AS_USER_STRING"><%=FileValidatorSpringUtil.formatSize(Configuration.getAsInt(ConfigurationKeys.UPLOAD_FILE_LARGE_MAX_SIZE))%></c:set>

<lams:html>
	<lams:head>
		<%@ include file="/common/header.jsp"%>
		<lams:css />
	</lams:head>
	
	<body>
		<div class="panel panel-default">
		<div class="panel-body">
	
		<!-- Basic Info Form-->
		<%@ include file="/common/messages.jsp"%>

		<form:form action="createTopic.do" modelAttribute="messageForm" id="messageForm" focus="message.subject" enctype="multipart/form-data">
			<form:hidden path="sessionMapID" />
			<c:set var="sessionMap" value="${sessionScope[messageForm.sessionMapID]}" />

			<div class="form-group">
		    <label for="message.subject"><fmt:message key="message.label.subject" /> *</label>
		    <form:input type="text" size="30" tabindex="1" path="message.subject" value="${message.subject}" maxlength="60" cssClass="form-control"/>
		    <c:set var="message.subject" value="errorMap${forum.uid}" />
			</div>
			
			<div class="form-group">
		    <label for="forum.instructions"><fmt:message key="message.label.body" /> *</label>
			<c:set var="body" value=""/>
			<c:if test="${not empty messageForm.message}">
				<c:set var="body" value="${messageForm.message.body}"/>
			</c:if>
			<lams:CKEditor id="message.body" value="${body}" contentFolderID="${sessionMap.contentFolderID}"/>
			<c:set var="message.body" value="errorMap${forum.uid}" />
			</div>

			<c:set var="itemAttachment" value="${messageForm}" />
			<div class="form-group">
				<label for="attachmentFile"><fmt:message key="message.label.attachment" /></label>
				<lams:FileUpload fileFieldname="attachmentFile" maxFileSize="${UPLOAD_FILE_MAX_SIZE_AS_USER_STRING}" tabindex="3" />
				<c:set var="message.attachment" value="errorMap${forum.uid}" />
				<lams:WaitingSpinner id="itemAttachmentArea_Busy"/>
			</div>

			<div class="voffset5 pull-right">
				<a href="#" onclick="hideMessage()" class="btn btn-default btn-xs"> <fmt:message key="button.cancel" /></a>
				<a href="#" onclick="submitMessage()" class="btn btn-default btn-xs loffset5"> <fmt:message key="button.add" /> </a>
			</div>

		</form:form>
	</body>
</lams:html>
