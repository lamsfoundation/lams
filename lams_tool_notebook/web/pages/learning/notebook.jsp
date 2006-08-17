<%@ include file="/common/taglibs.jsp"%>

<h1 class="no-tabs-below">
	${notebookDTO.title}
</h1>
<div id="header-no-tabs-learner"></div>
<div id="content-learner">
	<c:if test="${mode == 'learner' || mode == 'author'}">
		<html:form action="/learning" method="post">

			<table>
				<tr>
					<td>
						${notebookDTO.instructions}
					</td>
				</tr>

				<tr>
					<td>
						<c:set var="lrnForm"
							value="<%=request
											.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
						<c:choose>
							<c:when test="${contentEditable}">
								<c:choose>
									<c:when test="${notebookDTO.allowRichEditor}">
										<c:set var="language"><lams:user property="localeLanguage"/></c:set>
										<fck:editor id="entryText" basePath="/lams/fckeditor/"
											imageBrowserURL="/FCKeditor/editor/filemanager/browser/default/browser.html?Type=Image&amp;Connector=connectors/jsp/connector"
											linkBrowserURL="/FCKeditor/editor/filemanager/browser/default/browser.html?Connector=connectors/jsp/connector"
											flashBrowserURL="/FCKeditor/editor/filemanager/browser/default/browser.html?Type=Flash&amp;Connector=connectors/jsp/connector"
											imageUploadURL="/FCKeditor/editor/filemanager/upload/simpleuploader?Type=Image"
											linkUploadURL="/FCKeditor/editor/filemanager/upload/simpleuploader?Type=File"
											flashUploadURL="/FCKeditor/editor/filemanager/upload/simpleuploader?Type=Flash"
											toolbarSet="Default-Learner" defaultLanguage="${language}" autoDetectLanguage="false">
												${lrnForm.entryText}
										</fck:editor>
									</c:when>

									<c:otherwise>
										<html:textarea cols="66" rows="8" property="entryText"></html:textarea>
									</c:otherwise>
								</c:choose>
							</c:when>

							<c:otherwise>
								${lrnForm.entryText}
							</c:otherwise>
						</c:choose>
					</td>
				</tr>

				<tr>
					<td class="right-buttons">
						<html:hidden property="dispatch" value="finishActivity" />
						<html:hidden property="toolSessionID" />
						<html:submit styleClass="button">
							<fmt:message>button.finish</fmt:message>
						</html:submit>
					</td>
				</tr>
			</table>
		</html:form>
	</c:if>
</div>
<div id="footer-learner"></div>
