<%@ include file="/common/taglibs.jsp"%>

<c:set var="authoringForm" value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />
<html:hidden property="deleteFileUuid" />

<div id="authoring_instructions">
	<table class="form">
		<!-- ==========  Online Instructions ========== -->
		<tr>
			<td class="formlabel">
				<fmt:message key="instructions.onlineInstructions" />
			</td>
			<td NOWRAP width="700">
				<lams:SetEditor id="OnlineInstruction" text="${authoringForm.onlineInstruction}" />
			</td>
		</tr>


		<!-- ==========  Online Attachments ========== -->
		<tr>
			<td>
				&nbsp;
			</td>
			<td>
				<div id="onlinefile">
					<br />
					<c:forEach var="file" items="${authoringForm.authSession.onlineFilesList}">
						<li>
							<c:out value="${file.fileName}" />
							<c:set var="viewURL">
								<html:rewrite page="/download/?uuid=${file.fileUuid}&preferDownload=false" />
							</c:set>
							<a href="javascript:launchInstructionsPopup('<c:out value='${viewURL}' escapeXml='false'/>')">
								<fmt:message key="link.view" />
							</a>
							&nbsp;

							<c:set var="downloadURL">
								<html:rewrite page="/download/?uuid=${file.fileUuid}&preferDownload=true" />
							</c:set>
							<a href="<c:out value='${downloadURL}' escapeXml='false'/>">
								<fmt:message key="link.download" />
							</a>
							&nbsp;

							<html:link href="javascript:deleteAttachment('deleteOnline','${file.fileUuid}')">
								<fmt:message key="link.delete" />
							</html:link>


						</li>
					</c:forEach>

					<%-- Displaying unsaved Files --%>
					<c:forEach var="file" items="${authoringForm.authSession.unsavedOnlineFilesList}">
						<li>
							<c:out value="${file.fileName}" />
							*
							<c:set var="viewURL">
								<html:rewrite page="/download/?uuid=${file.fileUuid}&preferDownload=false" />
							</c:set>
							<a href="javascript:launchInstructionsPopup('<c:out value='${viewURL}' escapeXml='false'/>')">
								<fmt:message key="link.view" />
							</a>
							&nbsp;

							<c:set var="downloadURL">
								<html:rewrite page="/download/?uuid=${file.fileUuid}&preferDownload=true" />
							</c:set>
							<a href="<c:out value='${downloadURL}' escapeXml='false'/>">
								<fmt:message key="link.download" />
							</a>
							&nbsp;

							<html:link href="javascript:deleteAttachment('removeUnsavedOnline','${file.fileUuid}')">
								<fmt:message key="link.delete" />
							</html:link>
						</li>
					</c:forEach>
				</div>
			</td>
		</tr>


		<!-- ==========  Online Attachments Upload========== -->
		<tr>
			<td class="formlabel">
				<fmt:message key="instructions.uploadOnlineInstr" />
			</td>
			<td class="formcontrol">
				<html:file property="onlineFile">
					<!--<fmt:message key="label.authoring.choosefile.button" />  -->
					choose
				</html:file>
				<html:link href="javascript:doSubmit('uploadOnline');" property="submit">
					<fmt:message key="button.upload" />
				</html:link>
			</td>
		</tr>

		<tr>
			<td>
				&nbsp;
			</td>
			<td>
				&nbsp;
			</td>
		</tr>

		<!-- ==========  Offline Instructions ========== -->
		<tr>
			<td class="formlabel">
				<fmt:message key="instructions.offlineInstructions" />
			</td>
			<td NOWRAP width="700">
				<%-- TODO get rid of the nowrap --%>
				<lams:SetEditor id="OfflineInstruction" text="${authoringForm.offlineInstruction}" />
			</td>
		</tr>


		<!-- ==========  Offline Attachments ========== -->
		<tr>
			<td>
				&nbsp;
			</td>
			<td>
				<div id="offlinefile">
					<br />
					<!-- Saved attachments -->
					<c:forEach var="file" items="${authoringForm.authSession.offlineFilesList}">
						<li>
							<c:out value="${file.fileName}" />
							<c:set var="viewURL">
								<html:rewrite page="/download/?uuid=${file.fileUuid}&preferDownload=false" />
							</c:set>
							<a href="javascript:launchInstructionsPopup('<c:out value='${viewURL}' escapeXml='false'/>')">
								<fmt:message key="link.view" />
							</a>
							&nbsp;

							<c:set var="downloadURL">
								<html:rewrite page="/download/?uuid=${file.fileUuid}&preferDownload=true" />
							</c:set>
							<a href="<c:out value='${downloadURL}' escapeXml='false'/>">
								<fmt:message key="link.download" />
							</a>
							&nbsp;

							<html:link href="javascript:deleteAttachment('deleteOffline','${file.fileUuid}')">
								<fmt:message key="link.delete" />
							</html:link>
						</li>
					</c:forEach>

					<!-- Unsaved attachments -->
					<c:forEach var="file" items="${authoringForm.authSession.unsavedOfflineFilesList}">
						<li>
							<c:out value="${file.fileName}" />
							*
							<c:set var="viewURL">
								<html:rewrite page="/download/?uuid=${file.fileUuid}&preferDownload=false" />
							</c:set>
							<a href="javascript:launchInstructionsPopup('<c:out value='${viewURL}' escapeXml='false'/>')">
								<fmt:message key="link.view" />
							</a>
							&nbsp;

							<c:set var="downloadURL">
								<html:rewrite page="/download/?uuid=${file.fileUuid}&preferDownload=true" />
							</c:set>
							<a href="<c:out value='${downloadURL}' escapeXml='false'/>">
								<fmt:message key="link.download" />
							</a>
							&nbsp;

							<html:link href="javascript:deleteAttachment('removeUnsavedOffline','${file.fileUuid}')">
								<fmt:message key="link.delete" />
							</html:link>
						</li>
					</c:forEach>

				</div>
			</td>
		</tr>


		<!-- ==========  Offline Attachments Upload ========== -->

		<tr>
			<td class="formlabel">
				<fmt:message key="instructions.uploadOfflineInstr" />
			</td>
			<td class="formcontrol">
				<html:file property="offlineFile">
					<!--<fmt:message key="label.authoring.choosefile.button" />  -->
					choose
				</html:file>
				<html:link href="javascript:doSubmit('uploadOffline');" property="submit">
					<fmt:message key="button.upload" />
				</html:link>
			</td>
		</tr>


	</table>
</div>
