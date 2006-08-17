<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>
<html>
	<head>
		<%@ include file="/common/header.jsp"%>
		<style type="text/css">
		<!--
		td { 
			padding:4px; 
			font-size:12px;
		}
		-->
		</style>		
	</head>
	<body>
		<table cellpadding="3">
			<!-- Basic Info Form-->
			<%@ include file="/common/messages.jsp"%>
			<html:form action="/authoring/updateTopic.do" focus="message.subject" enctype="multipart/form-data" styleId="topicFormId">
				<input type="hidden" name="topicIndex" value="<c:out value="${topicIndex}"/>">

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
						<c:if test="${topic.hasAttachment}">
							<c:forEach var="file" items="${topic.message.attachments}">
								<c:set var="downloadURL">
									<html:rewrite page="/download/?uuid=${file.fileUuid}&versionID=${file.fileVersionId}&preferDownload=true" />
								</c:set>
								<a href="<c:out value='${downloadURL}' escapeXml='false'/>"> <c:out value="${file.fileName}" /> </a>
								<c:set var="deleteURL">
									<html:rewrite page="/authoring/deleteAttachment.do?uuid=${file.fileUuid}&versionID=${file.fileVersionId}&topicIndex=${topicIndex}" />
								</c:set>
								&nbsp;&nbsp;
								<a href="<c:out value='${deleteURL}'/>" class="button" style="float: none"> <fmt:message key="label.delete" /> </a>
							</c:forEach>
						</c:if>
						<c:if test="${not topic.hasAttachment}">
							<html:file tabindex="3" property="attachmentFile" />
						</c:if>
						<BR>
					</td>
				</tr>
				<tr>
					<td align="center" valign="bottom">
						<a href="#" onclick="getElementById('topicFormId').submit();" class="button-add-item">
							<bean:message key="button.add" />
						</a>
						&nbsp; &nbsp;
						<a href="#" onclick="javascript:window.parent.hideMessage()" class="button">
							<bean:message key="button.cancel" />
						</a>					
						<BR><BR>
					</td>
				</tr>
			</html:form>
		</table>

	</body>
</html>
