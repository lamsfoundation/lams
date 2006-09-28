<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>
<html>
	<head>
		<%@ include file="/common/header.jsp"%>
		<script type="text/javascript">
			var removeItemAttachmentUrl = "<c:url value="/authoring/deleteAttachment.do"/>";
		</script>
		
		<script type="text/javascript" src="${tool}includes/javascript/message.js"></script>
		
	</head>
	<body>
		<div class="content">
		<table cellpadding="3" width="100%">
			<!-- Basic Info Form-->
			<%@ include file="/common/messages.jsp"%>
			<html:form action="/authoring/updateTopic.do" focus="message.subject" enctype="multipart/form-data" styleId="topicFormId">
				<input type="hidden" name="topicIndex" value="<c:out value="${topicIndex}"/>">
				<html:hidden property="sessionMapID"/>

				<tr>
					<td>
						<b><bean:message key="message.label.subject" />*</b><BR>
						<html:text size="30" tabindex="1" property="message.subject" />
						<BR>
						<html:errors property="message.subject" />
					</td>
				</tr>
				<tr>
					<td>
						<b><bean:message key="message.label.body" />*</b>
						<c:set var="formBean" value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />
						<c:set var="language"><lams:user property="localeLanguage"/></c:set>
						<fck:editor id="message.body" basePath="/lams/fckeditor/"
							imageBrowserURL="/FCKeditor/editor/filemanager/browser/default/browser.html?Type=Image&amp;Connector=connectors/jsp/connector"
							linkBrowserURL="/FCKeditor/editor/filemanager/browser/default/browser.html?Connector=connectors/jsp/connector"
							flashBrowserURL="/FCKeditor/editor/filemanager/browser/default/browser.html?Type=Flash&amp;Connector=connectors/jsp/connector"
							imageUploadURL="/FCKeditor/editor/filemanager/upload/simpleuploader?Type=Image"
							linkUploadURL="/FCKeditor/editor/filemanager/upload/simpleuploader?Type=File"
							flashUploadURL="/FCKeditor/editor/filemanager/upload/simpleuploader?Type=Flash"
								toolbarSet="Default-Learner" defaultLanguage="${language}" autoDetectLanguage="false">
							<c:out value="${formBean.message.body}" escapeXml="false" />
						</fck:editor>							
						
						<html:errors property="message.body" />
					</td>
				</tr>
				<tr>
					<td>
						<b><bean:message key="message.label.attachment" /></b>
						<c:set var="itemAttachment" value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />
							<div id="itemAttachmentArea">
								<%@ include file="/jsps/authoring/parts/msgattachment.jsp"%>
							</div>						
					</td>
				</tr>
				<tr>
					<td align="center" valign="bottom"><p>
						<a href="#" onclick="getElementById('topicFormId').submit();" class="button-add-item">
							<bean:message key="button.add" />
						</a>
						&nbsp; &nbsp;
						<a href="#" onclick="javascript:window.parent.hideMessage()" class="button">
							<bean:message key="button.cancel" />
						</a>					
						</p>
					</td>
				</tr>
			</html:form>
		</table>
		</div>
	</body>
</html>
