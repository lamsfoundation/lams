<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>
<html>
	<head>
		<%@ include file="/common/header.jsp"%>
		<lams:css style="tabbed"/>
		<script type="text/javascript">
			var removeItemAttachmentUrl = "<c:url value="/authoring/deleteAttachment.do"/>";
		</script>

		<script type="text/javascript"
			src="${tool}includes/javascript/message.js"></script>

	</head>
	<body>
	
			<table>
				<!-- Basic Info Form-->
				<%@ include file="/common/messages.jsp"%>
				<html:form action="/authoring/updateTopic.do"
					focus="message.subject" enctype="multipart/form-data"
					styleId="topicFormId">
					<input type="hidden" name="topicIndex"
						value="<c:out value="${topicIndex}"/>">
					<html:hidden property="sessionMapID" />
					<c:set var="formBean"
						value="<%=request
										.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
					<c:set var="sessionMap"
						value="${sessionScope[formBean.sessionMapID]}" />

					<tr>
						<td>
							<div class="field-name">
								<fmt:message key="message.label.subject" />
								*
							</div>
							<html:text size="30" tabindex="1" property="message.subject" />
							<br />
							<html:errors property="message.subject" />
						</td>
					</tr>
					<tr>
						<td>
							<div class="field-name"><fmt:message key="message.label.body" />*
					</div>
							<c:set var="formBean"
								value="<%=request
										.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
							<lams:FCKEditor id="message.body"
								value="${formBean.message.body}"
								contentFolderID="${sessionMap.contentFolderID}"></lams:FCKEditor>
							<html:errors property="message.body" />
						</td>
					</tr>
					<tr>
						<td>
							<div class="field-name"><fmt:message key="message.label.attachment" />
							</div>
							<c:set var="itemAttachment"
								value="<%=request
										.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
							<div id="itemAttachmentArea">
								<%@ include file="/jsps/authoring/parts/msgattachment.jsp"%>
							</div>
					
							<div class="space-top">
								<a href="#" onclick="getElementById('topicFormId').submit();"
									class="button-add-item"> <fmt:message key="button.add" />
								</a>
								<a href="#" onclick="javascript:window.parent.hideMessage()"
									class="button space-left"> <fmt:message key="button.cancel" /> </a>
							</div>
						</td>
					</tr>
				</html:form>
			</table>
	
	</body>
</html>
