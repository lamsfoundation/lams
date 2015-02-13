<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>
<lams:html>
	<lams:head>
		<%@ include file="/common/header.jsp"%>
		<lams:css style="tabbed" />
		<script type="text/javascript">
			var removeItemAttachmentUrl = "<c:url value="/authoring/deleteAttachment.do"/>";
		</script>

		<script type="text/javascript" src="${tool}includes/javascript/message.js"></script>
		<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/jquery.js"></script>
		<script type="text/javascript">			
			$(function() {
				//change size of an iframe on ckeditor's autogrow 
				CKEDITOR.instances["message.body"].on("instanceReady", function(e) {
				    e.editor.on('resize', function(reEvent){
				    	var iframe = window.parent.document.getElementById("messageArea");
				    	iframe.style.height = eval(iframe.contentWindow.document.body.scrollHeight) + 'px';
				    });
				});
			});
		</script>

	</lams:head>
	<body>

		<!-- Basic Info Form-->
		<%@ include file="/common/messages.jsp"%>
		<html:form action="/authoring/updateTopic.do" focus="message.subject" enctype="multipart/form-data" styleId="topicFormId">
			<input type="hidden" name="topicIndex" value="<c:out value="${topicIndex}"/>">
			<html:hidden property="sessionMapID" />
			<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
			<c:set var="sessionMap" value="${sessionScope[formBean.sessionMapID]}" />

			<div class="field-name">
				<fmt:message key="message.label.subject" />
				*
			</div>
			<div class="small-space-bottom">
				<html:text size="30" tabindex="1" property="message.subject" maxlength="60" />
				<html:errors property="message.subject" />
			</div>

			<div class="field-name">
				<fmt:message key="message.label.body" />
				*
			</div>
			<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />

			<div class="small-space-bottom">
				<lams:CKEditor id="message.body" value="${formBean.message.body}" width="99%"
					contentFolderID="${sessionMap.contentFolderID}" resizeParentFrameName="messageArea" />
				<html:errors property="message.body" />
			</div>
			
			<div class="field-name">
				<fmt:message key="message.label.attachment" />
			</div>
			<c:set var="itemAttachment"
				value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
										
			<div id="itemAttachmentArea" class="small-space-bottom">
				<%@ include file="/jsps/authoring/parts/msgattachment.jsp"%>
			</div>

			<lams:ImgButtonWrapper>
				<a href="#" onclick="javascript:window.parent.hideMessage()"
					class="button space-left"> <fmt:message key="button.cancel" />
				</a>
				&nbsp;
				<a href="#" onclick="getElementById('topicFormId').submit();"
					class="button-add-item"> <fmt:message key="button.add" /> </a>
					
			</lams:ImgButtonWrapper>
		</html:form>
	</body>
</lams:html>
