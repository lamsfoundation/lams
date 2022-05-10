<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<lams:html>
	<lams:head>
		<%@ include file="/common/header.jsp"%>
		<lams:css/>
		<script type="text/javascript">
			var removeItemAttachmentUrl = "<c:url value="/authoring/deleteAttachment.do"/>";
		</script>
	</lams:head>
	<body>

		<div class="panel panel-default">
		<div class="panel-body">
	
		<!-- Basic Info Form-->
		<lams:errors/>
		<c:set var="csrfToken"><csrf:token/></c:set>
		<form:form action="updateTopic.do?${csrfToken}" focus="message.subject" enctype="multipart/form-data" id="topicFormId" modelAttribute="topicFormId" onsubmit="return validate();">
			
			<input type="hidden" name="topicIndex" value="<c:out value="${topicIndex}"/>">
			<form:hidden path="sessionMapID" />
			<c:set var="sessionMap" value="${sessionScope[forumForm.sessionMapID]}" />

			<div class="form-group">
		    <label for="message.subject"><fmt:message key="message.label.subject" /> *</label>
			<form:input type="text" size="30" tabindex="1" path="message.subject" value="${message.subject}" maxlength="60" cssClass="form-control"/>
		    
		    <lams:errors path="message.subject"/>
			</div>
			
			<div class="form-group">
		    <label for="forum.instructions"><fmt:message key="message.label.body" /> *</label>
			<c:set var="body" value=""/>
			<c:if test="${not empty topicFormId.message}">
				<c:set var="body" value="${topicFormId.message.body}"/>
			</c:if>
			<lams:CKEditor id="message.body" value="${body}" contentFolderID="${sessionMap.contentFolderID}" />
		    <lams:errors path="message.body"/>
			</div>

			<c:set var="itemAttachment" value="${topicFormId}" />
			<div class="form-group">
				<div id="itemAttachmentArea" class="small-space-bottom">
					<%@ include file="/jsps/authoring/parts/msgattachment.jsp"%>
				</div>
			</div>

			<div class="voffset5 pull-right">
				<a href="#" onclick="hideMessage()" class="btn btn-default btn-xs"> <fmt:message key="button.cancel" /></a>
				<a href="#" onclick="submitMessage()" class="btn btn-default btn-xs loffset5"> <fmt:message key="button.add" /> </a>
			</div>

		</form:form>

		</div>
		</div>

	</body>
</lams:html>
