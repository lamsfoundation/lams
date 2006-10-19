<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>
<html>
	<head>
		<%@ include file="/common/header.jsp"%>
		<lams:css style="tabbed" />
	</head>
	<body>
		<!-- Basic Info Form-->
		<%@ include file="/common/messages.jsp"%>

		<html:form action="/authoring/createTopic.do" focus="message.subject"
			enctype="multipart/form-data" styleId="topicFormId">
			<html:hidden property="sessionMapID" />
			<c:set var="formBean"
				value="<%=request
										.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
			<c:set var="sessionMap"
				value="${sessionScope[formBean.sessionMapID]}" />

			<div class="field-name">
				<fmt:message key="message.label.subject" />
				*
			</div>
			<div class="small-space-bottom">
				<html:text size="30" tabindex="1" property="message.subject" />
				<html:errors property="message.subject" />
			</div>

			<div class="field-name">
				<fmt:message key="message.label.body" />
				*
			</div>

			<div class="small-space-bottom">
				<lams:FCKEditor id="message.body" value=""
					contentFolderID="${sessionMap.contentFolderID}"></lams:FCKEditor>
				<html:errors property="message.body" />
			</div>

			<div class="field-name">
				<fmt:message key="message.label.attachment" />
			</div>

			<div class="small-space-bottom">
				<html:file tabindex="3" property="attachmentFile" />
			</div>

			<div style="padding-bottom:20px;">
				<a href="#" onclick="getElementById('topicFormId').submit();"
					class="button-add-item"> <fmt:message key="button.add" /> </a>

				<a href="#" onclick="javascript:window.parent.hideMessage()"
					class="button space-left"> <fmt:message key="button.cancel" />
				</a>
			</div>

		</html:form>
	</body>
</html>
