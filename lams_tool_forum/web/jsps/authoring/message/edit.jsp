<%@ include file="/common/taglibs.jsp"%>
<html>
	<head>
		<%@ include file="/common/header.jsp"%>
	</head>
	<body class="tabpart">
		<table class="forms">
			<!-- Basic Info Form-->
			<tr>
				<td>
					<div align="center">
						<%@ include file="/common/messages.jsp"%>
						<html:form action="/authoring/updateTopic.do" focus="message.subject" enctype="multipart/form-data">
							<fieldset>
								<input type="hidden" name="topicIndex" value="<c:out value="${topicIndex}"/>">

								<table width="80%" cellspacing="8" border="0" align="left" class="innerforms">
									<tr>
										<td>
											<b><bean:message key="message.label.subject" /><b class="required">*</b></b>
										</td>
										<td>
											<html:text size="30" tabindex="1" property="message.subject" />
											<BR>
											<html:errors property="message.subject" />
										</td>
									</tr>
									<tr>
										<td>
											<b><bean:message key="message.label.body" /><b class="required">*</b></b>
										</td>
										<td>
											<c:set var="formBean" value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />
											<FCK:editor id="message.body" basePath="/lams/fckeditor/" height="200" width="100%">
												<c:out value="${formBean.message.body}" escapeXml="false" />
											</FCK:editor>										
											<BR>
											<html:errors property="message.body" />
										</td>
									</tr>
									<tr>
										<td>
											<b><bean:message key="message.label.attachment" /></b>
										</td>
										<td>
											<c:if test="${topic.hasAttachment}">
												<c:forEach var="file" items="${topic.message.attachments}">
													<c:set var="downloadURL">
														<html:rewrite page="/download/?uuid=${file.fileUuid}&versionID=${file.fileVersionId}&preferDownload=true" />
													</c:set>
													<a href="<c:out value='${downloadURL}' escapeXml='false'/>"> <c:out value="${file.fileName}" /> </a>
													<c:set var="deleteURL">
														<html:rewrite page="/authoring/deleteAttachment.do?uuid=${file.fileUuid}&versionID=${file.fileVersionId}&topicIndex=${topicIndex}" />
													</c:set>
													<a href="<c:out value='${deleteURL}'/>" class="button" style="float: none"> <fmt:message key="label.delete" /> </a>
												</c:forEach>
											</c:if>
											<c:if test="${not topic.hasAttachment}">
												<html:file tabindex="3" property="attachmentFile" />
											</c:if>

										</td>
									</tr>
									<tr>
										<td></td>
										<td align="left" valign="bottom">
											<BR>
											<html:submit style="width:120px" styleClass="buttonStyle">
												<bean:message key="button.submit" />
											</html:submit>
											<html:button property="cancel" onclick="javascript:window.parent.hideMessage()" styleClass="buttonStyle" style="width:120px">
												<bean:message key="button.cancel" />
											</html:button>
										</td>
									</tr>
								</table>

							</fieldset>
						</html:form>
					</div>
				</td>
			</tr>
		</table>

	</body>
</html>
