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
		<%@ include file="/common/messages.jsp"%>
		<html:form action="/authoring/updateTopic.do" focus="message.subject" enctype="multipart/form-data" styleId="topicFormId" onsubmit="return validate();">
			<input type="hidden" name="topicIndex" value="<c:out value="${topicIndex}"/>">
			<html:hidden property="sessionMapID" />
			<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
			<c:set var="sessionMap" value="${sessionScope[formBean.sessionMapID]}" />

			<div class="form-group">
		    <label for="message.subject"><fmt:message key="message.label.subject" /> *</label>
		    <html:text size="30" tabindex="1" property="message.subject" maxlength="60" styleClass="form-control"/>
			<html:errors property="message.subject" />
			</div>
			
			<div class="form-group">
		    <label for="forum.instructions"><fmt:message key="message.label.body" /> *</label>
			<c:set var="body" value=""/>
			<c:if test="${not empty formBean.message}">
				<c:set var="body" value="${formBean.message.body}"/>
			</c:if>
			<lams:CKEditor id="message.body" value="${body}" contentFolderID="${sessionMap.contentFolderID}" />
			<html:errors property="message.body" />
			</div>

			<c:set var="itemAttachment"
				value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
			<div class="form-group">
				<label for="attachmentFile"><fmt:message key="message.label.attachment" /></label>
				<div id="itemAttachmentArea" class="small-space-bottom">
					<%@ include file="/jsps/authoring/parts/msgattachment.jsp"%>
				</div>
			</div>

			<div class="voffset5 pull-right">
				<a href="#" onclick="hideMessage()" class="btn btn-default btn-xs"> <fmt:message key="button.cancel" /></a>
				<a href="#" onclick="submitMessage()" class="btn btn-default btn-xs loffset5"> <fmt:message key="button.add" /> </a>
			</div>

		</html:form>

		</div>
		</div>

	</body>
</lams:html>
