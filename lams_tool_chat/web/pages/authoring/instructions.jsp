<%@ include file="/common/taglibs.jsp"%>

<c:set var="authForm" value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />
<html:hidden property="deleteFileUuid" />

<table cellpadding="0">
	<tbody>
		<!-- ==========  Online Instructions ========== -->
		<tr>
			<td>
				<lams:SetEditor id="OnlineInstruction" text="${authForm.onlineInstruction}" key="instructions.onlineInstructions" />
			</td>
		</tr>


		<!-- ==========  Online Attachments Upload========== -->
		<tr>
			<td class="field-name">
				<fmt:message key="instructions.uploadOnlineInstr" />
			</td>
		</tr>
		<tr>
			<td>
				<html:file property="onlineFile">
					<!--<fmt:message key="label.authoring.choosefile.button" />  -->
					choose
				</html:file>
				<html:link href="javascript:doSubmit('uploadOnline');" property="submit">
					<fmt:message key="button.upload" />
				</html:link>
			</td>
		</tr>

		<!-- ==========  Online Attachments ========== -->
		<c:if test="${not empty requestScope.sessionMap.onlineFiles or not empty requestScope.sessionMap.unsavedOnlineFiles}">
			<tr>
				<td>
					<div class="space-left">
						<c:forEach var="file" items="${requestScope.sessionMap.onlineFiles}">
							<li>
								<c:out value="${file.fileName}" />
								<c:set var="viewURL">
									<html:rewrite page="/download/?uuid=${file.fileUuid}&preferDownload=false" />
								</c:set>
								<a href="javascript:launchInstructionsPopup('<c:out value='${viewURL}' escapeXml='false'/>')"> <fmt:message key="link.view" /> </a> &nbsp;

								<c:set var="downloadURL">
									<html:rewrite page="/download/?uuid=${file.fileUuid}&preferDownload=true" />
								</c:set>
								<a href="<c:out value='${downloadURL}' escapeXml='false'/>"> <fmt:message key="link.download" /> </a> &nbsp;

								<html:link href="javascript:deleteAttachment('deleteOnline','${file.fileUuid}')">
									<fmt:message key="link.delete" />
								</html:link>
							</li>
						</c:forEach>

						<%-- Displaying unsaved Files --%>
						<c:forEach var="file" items="${requestScope.sessionMap.unsavedOnlineFiles}">
							<li>
								<c:out value="${file.fileName}" />
								*
								<c:set var="viewURL">
									<html:rewrite page="/download/?uuid=${file.fileUuid}&preferDownload=false" />
								</c:set>
								<a href="javascript:launchInstructionsPopup('<c:out value='${viewURL}' escapeXml='false'/>')"> <fmt:message key="link.view" /> </a> &nbsp;

								<c:set var="downloadURL">
									<html:rewrite page="/download/?uuid=${file.fileUuid}&preferDownload=true" />
								</c:set>
								<a href="<c:out value='${downloadURL}' escapeXml='false'/>"> <fmt:message key="link.download" /> </a> &nbsp;

								<html:link href="javascript:deleteAttachment('removeUnsavedOnline','${file.fileUuid}')">
									<fmt:message key="link.delete" />
								</html:link>
							</li>
						</c:forEach>
					</div>
				</td>
			</tr>
		</c:if>
	</tbody>
</table>

<hr />

<table>
	<tbody>
		<!-- ==========  Offline Instructions ========== -->
		<tr>
			<td>
				<lams:SetEditor id="OfflineInstruction" text="${authForm.offlineInstruction}" key="instructions.offlineInstructions" alt="true" />
			</td>
		</tr>

		<!-- ==========  Offline Attachments Upload ========== -->

		<tr>
			<td class="field-name-alternative-color">
				<fmt:message key="instructions.uploadOfflineInstr" />
			</td>
		</tr>
		<tr>
			<td>
				<html:file property="offlineFile">
					<!--<fmt:message key="label.authoring.choosefile.button" />  -->
					choose
				</html:file>
				<html:link href="javascript:doSubmit('uploadOffline');" property="submit">
					<fmt:message key="button.upload" />
				</html:link>
			</td>
		</tr>

		<!-- ==========  Offline Attachments ========== -->
		<c:if test="${not empty requestScope.sessionMap.offlineFiles or not empty requestScope.sessionMap.unsavedOfflineFiles}">
			<tr>
				<td>
					<div class="space-left">
						<!-- Saved attachments -->
						<c:forEach var="file" items="${requestScope.sessionMap.offlineFiles}">
							<li>
								<c:out value="${file.fileName}" />
								<c:set var="viewURL">
									<html:rewrite page="/download/?uuid=${file.fileUuid}&preferDownload=false" />
								</c:set>
								<a href="javascript:launchInstructionsPopup('<c:out value='${viewURL}' escapeXml='false'/>')"> <fmt:message key="link.view" /> </a> &nbsp;

								<c:set var="downloadURL">
									<html:rewrite page="/download/?uuid=${file.fileUuid}&preferDownload=true" />
								</c:set>
								<a href="<c:out value='${downloadURL}' escapeXml='false'/>"> <fmt:message key="link.download" /> </a> &nbsp;

								<html:link href="javascript:deleteAttachment('deleteOffline','${file.fileUuid}')">
									<fmt:message key="link.delete" />
								</html:link>
							</li>
						</c:forEach>

						<!-- Unsaved attachments -->
						<c:forEach var="file" items="${requestScope.sessionMap.unsavedOfflineFiles}">
							<li>
								<c:out value="${file.fileName}" />
								*
								<c:set var="viewURL">
									<html:rewrite page="/download/?uuid=${file.fileUuid}&preferDownload=false" />
								</c:set>
								<a href="javascript:launchInstructionsPopup('<c:out value='${viewURL}' escapeXml='false'/>')"> <fmt:message key="link.view" /> </a> &nbsp;

								<c:set var="downloadURL">
									<html:rewrite page="/download/?uuid=${file.fileUuid}&preferDownload=true" />
								</c:set>
								<a href="<c:out value='${downloadURL}' escapeXml='false'/>"> <fmt:message key="link.download" /> </a> &nbsp;

								<html:link href="javascript:deleteAttachment('removeUnsavedOffline','${file.fileUuid}')">
									<fmt:message key="link.delete" />
								</html:link>
							</li>
						</c:forEach>
					</div>
				</td>
			</tr>
		</c:if>
	</tbody>
</table>
