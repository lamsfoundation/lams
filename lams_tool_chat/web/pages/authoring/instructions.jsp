<%@ include file="/common/taglibs.jsp"%>

<html:hidden property="deleteFileUuid" />

<table cellpadding="0">

	<%-- Online Instructions --%>
	<tr>
		<td>
			<div class="field-name">
				<fmt:message key="instructions.onlineInstructions"></fmt:message>
			</div>
			<html:textarea property="onlineInstruction" rows="3" cols="75"></html:textarea>
		</td>
	</tr>

	<%-- Online Attachments --%>
	<c:if
		test="${not empty requestScope.sessionMap.onlineFiles or not empty requestScope.sessionMap.unsavedOnlineFiles}">
		<tr>
			<td>
				<div class="space-left">
					<ul>

						<%-- Online Saved Files	--%>
						<c:forEach var="file"
							items="${requestScope.sessionMap.onlineFiles}">
							<li>
								<c:out value="${file.fileName}" />
								<c:set var="viewURL">
									<html:rewrite
										page="/download/?uuid=${file.fileUuid}&amp;preferDownload=false" />
								</c:set>
								<a
									href="javascript:launchInstructionsPopup('<c:out value='${viewURL}' escapeXml='false'/>')">
									<fmt:message key="link.view" /> </a> &nbsp;

								<c:set var="downloadURL">
									<html:rewrite
										page="/download/?uuid=${file.fileUuid}&amp;preferDownload=true" />
								</c:set>
								<a href="<c:out value='${downloadURL}' escapeXml='false'/>">
									<fmt:message key="link.download" /> </a> &nbsp;

								<html:link
									href="javascript:deleteAttachment('deleteOnline','${file.fileUuid}')">
									<fmt:message key="link.delete" />
								</html:link>
							</li>
						</c:forEach>

						<%-- Online Unsaved Files --%>
						<c:forEach var="file"
							items="${requestScope.sessionMap.unsavedOnlineFiles}">
							<li>
								<c:out value="${file.fileName}" />
								*
								<c:set var="viewURL">
									<html:rewrite
										page="/download/?uuid=${file.fileUuid}&amp;preferDownload=false" />
								</c:set>
								<a
									href="javascript:launchInstructionsPopup('<c:out value='${viewURL}' escapeXml='false'/>')">
									<fmt:message key="link.view" /> </a> &nbsp;

								<c:set var="downloadURL">
									<html:rewrite
										page="/download/?uuid=${file.fileUuid}&amp;preferDownload=true" />
								</c:set>
								<a href="<c:out value='${downloadURL}' escapeXml='false'/>">
									<fmt:message key="link.download" /> </a> &nbsp;

								<html:link
									href="javascript:deleteAttachment('removeUnsavedOnline','${file.fileUuid}')">
									<fmt:message key="link.delete" />
								</html:link>
							</li>
						</c:forEach>

					</ul>
				</div>
			</td>
		</tr>
	</c:if>

	<%-- Online Attachments Upload --%>
	<tr>
		<td>
			<div class="field-name">
				<fmt:message key="instructions.uploadOnlineInstr" />
			</div>
			<html:file property="onlineFile">
			</html:file>
			<html:link href="javascript:doSubmit('uploadOnline');"
				property="submit" styleClass="button">
				<fmt:message key="button.upload" />
			</html:link>
		</td>
	</tr>

	<tr>
		<td>
			<hr />
		</td>
	</tr>




	<%-- Offline Instructions --%>
	<tr>
		<td>
			<div class="field-name-alternative-color">
				<fmt:message key="instructions.offlineInstructions"></fmt:message>
			</div>
			<html:textarea property="offlineInstruction" rows="3" cols="75"></html:textarea>
		</td>
	</tr>



	<%-- Offline Attachments --%>
	<c:if
		test="${not empty requestScope.sessionMap.offlineFiles or not empty requestScope.sessionMap.unsavedOfflineFiles}">
		<tr>
			<td>
				<div class="space-left">
					<ul>

						<%-- Offline Saved Attachments --%>
						<c:forEach var="file"
							items="${requestScope.sessionMap.offlineFiles}">
							<li>
								<c:out value="${file.fileName}" />
								<c:set var="viewURL">
									<html:rewrite
										page="/download/?uuid=${file.fileUuid}&amp;preferDownload=false" />
								</c:set>
								<a
									href="javascript:launchInstructionsPopup('<c:out value='${viewURL}' escapeXml='false'/>')">
									<fmt:message key="link.view" /> </a> &nbsp;

								<c:set var="downloadURL">
									<html:rewrite
										page="/download/?uuid=${file.fileUuid}&amp;preferDownload=true" />
								</c:set>
								<a href="<c:out value='${downloadURL}' escapeXml='false'/>">
									<fmt:message key="link.download" /> </a> &nbsp;

								<html:link
									href="javascript:deleteAttachment('deleteOffline','${file.fileUuid}')">
									<fmt:message key="link.delete" />
								</html:link>
							</li>
						</c:forEach>

						<%-- Offline Unsaved Attachments --%>
						<c:forEach var="file"
							items="${requestScope.sessionMap.unsavedOfflineFiles}">
							<li>
								<c:out value="${file.fileName}" />
								*
								<c:set var="viewURL">
									<html:rewrite
										page="/download/?uuid=${file.fileUuid}&amp;preferDownload=false" />
								</c:set>
								<a
									href="javascript:launchInstructionsPopup('<c:out value='${viewURL}' escapeXml='false'/>')">
									<fmt:message key="link.view" /> </a> &nbsp;

								<c:set var="downloadURL">
									<html:rewrite
										page="/download/?uuid=${file.fileUuid}&amp;preferDownload=true" />
								</c:set>
								<a href="<c:out value='${downloadURL}' escapeXml='false'/>">
									<fmt:message key="link.download" /> </a> &nbsp;

								<html:link
									href="javascript:deleteAttachment('removeUnsavedOffline','${file.fileUuid}')">
									<fmt:message key="link.delete" />
								</html:link>
							</li>
						</c:forEach>

					</ul>
				</div>
			</td>
		</tr>
	</c:if>

	<%--Offline Attachments Upload --%>
	<tr>
		<td>
			<div class="field-name-alternative-color">
				<fmt:message key="instructions.uploadOfflineInstr" />
			</div>
			<html:file property="offlineFile">
			</html:file>
			<html:link href="javascript:doSubmit('uploadOffline');"
				property="submit" styleClass="button">
				<fmt:message key="button.upload" />
			</html:link>
		</td>
	</tr>

</table>
