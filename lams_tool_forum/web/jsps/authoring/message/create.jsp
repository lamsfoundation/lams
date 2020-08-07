<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<lams:html>
	<lams:head>
		<%@ include file="/common/header.jsp"%>
		<lams:css />
		<link href="${lams}css/uppy.min.css" rel="stylesheet" type="text/css" />
		
		<script type="text/javascript" src="${lams}includes/javascript/uppy/uppy.min.js"></script>
		<c:choose>
			<c:when test="${language eq 'es'}">
				<script type="text/javascript" src="${lams}includes/javascript/uppy/es_ES.min.js"></script>
			</c:when>
			<c:when test="${language eq 'fr'}">
				<script type="text/javascript" src="${lams}includes/javascript/uppy/fr_FR.min.js"></script>
			</c:when>
			<c:when test="${language eq 'el'}">
				<script type="text/javascript" src="${lams}includes/javascript/uppy/el_GR.min.js"></script>
			</c:when>
		</c:choose>
		<script>
			$(document).ready(function(){
				initFileUpload('${topicFormId.tmpFileUploadId}', true, '<lams:user property="localeLanguage"/>');
			});
		</script>
	</lams:head>
	
	<body>
		<div class="panel panel-default">
		<div class="panel-body">
	
		<!-- Basic Info Form-->
		<lams:errors/>

		<c:set var="csrfToken"><csrf:token/></c:set>
		<form:form action="createTopic.do?${csrfToken}" modelAttribute="topicFormId" id="topicFormId" focus="message.subject" enctype="multipart/form-data">
			<form:hidden path="sessionMapID" />
			<c:set var="sessionMap" value="${sessionScope[topicFormId.sessionMapID]}" />

			<div class="form-group">
		    <label for="message.subject"><fmt:message key="message.label.subject" /> *</label>
		    <form:input type="text" size="30" tabindex="1" path="message.subject" value="${message.subject}" maxlength="60" cssClass="form-control"/>
		    <lams:errors path="message.subject"/>
			
			<div class="form-group">
		    <label for="forum.instructions"><fmt:message key="message.label.body" /> *</label>
			<c:set var="body" value=""/>
			<c:if test="${not empty topicFormId.message}">
				<c:set var="body" value="${topicFormId.message.body}"/>
			</c:if>
			<lams:CKEditor id="message.body" value="${body}" contentFolderID="${sessionMap.contentFolderID}"/>
		    <lams:errors path="message.body"/>
			</div>

			<c:set var="itemAttachment" value="${topicFormId}" />
			<div class="form-group">
				<input type="hidden" id="tmpFileUploadId" name="tmpFileUploadId" value="${topicFormId.tmpFileUploadId}" />
				<label for="image-upload-area"><fmt:message key="message.label.attachment" /></label>
				<div id="image-upload-area" class="voffset20"></div>
				
				<form:errors path="message.attachments" />
			    <lams:errors path="message.attachments"/>
			</div>

			<div class="voffset5 pull-right">
				<a href="#" onclick="hideMessage()" class="btn btn-default btn-xs"> <fmt:message key="button.cancel" /></a>
				<a href="#" onclick="submitMessage()" class="btn btn-default btn-xs loffset5"> <fmt:message key="button.add" /> </a>
			</div>

		</form:form>
	</body>
</lams:html>
