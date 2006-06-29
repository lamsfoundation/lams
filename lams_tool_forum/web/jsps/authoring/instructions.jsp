<%@ include file="/includes/taglibs.jsp"%>

<!--   Instruction Tab Content -->

<table>
	<!-- Instructions Row -->
	<tr>
		<td colspan="2">
			<lams:SetEditor id="forum.onlineInstructions" small="true" text="${forumForm.forum.onlineInstructions}"  key="label.authoring.online.instruction" />
		</td>
	</tr>
	<tr>
		<td></td>
		<td>

			<div id="onlinefile">
				<c:forEach var="file" items="${forumForm.onlineFileList}">
					<li>
						<c:out value="${file.fileName}" />
						<c:set var="viewURL">
							<html:rewrite page="/download/?uuid=${file.fileUuid}&preferDownload=false" />
						</c:set>
						<a href="javascript:launchInstructionsPopup('<c:out value='${viewURL}' escapeXml='false'/>')">
							<fmt:message key="label.view" />
						</a>
						&nbsp;
						<c:set var="downloadURL">
							<html:rewrite page="/download/?uuid=${file.fileUuid}&versionID=${file.fileVersionId}&preferDownload=true" />
						</c:set>
						<a href="<c:out value='${downloadURL}' escapeXml='false'/>">
							<fmt:message key="label.download" />
						</a>
						&nbsp;
						<c:set var="deleteonline">
							<html:rewrite page="/authoring/deleteOnline.do?toolContentID=${forumForm.toolContentID}&uuID=${file.fileUuid}&versionID=${file.fileVersionId}" />
						</c:set>
						<html:link href="javascript:loadDoc('${deleteonline}','onlinefile')">
							<fmt:message key="label.authoring.online.delete" />
						</html:link>
					</li>
				</c:forEach>
			</div>
		</td>
	</tr>
	<tr>
		<td  class="field-name">
			<fmt:message key="label.authoring.online.file" />
			:
		</td>
		<td>
			<html:file property="onlineFile">
				<fmt:message key="label.authoring.choosefile.button" />
			</html:file>
			<c:set var="uploadonline">
				<html:rewrite page="/authoring/uploadOnline.do" />
			</c:set>
			<a href="#" onclick="doSubmit('${uploadonline}')" class="button">
				<fmt:message key="label.authoring.upload.online.button" />
			</a>
		</td>
	</tr>
	<!-- Offline Instructions -->
	<tr>
		<td colspan="2">
			<lams:SetEditor id="forum.offlineInstructions" small="true"  text="${forumForm.forum.offlineInstructions}" key="label.authoring.offline.instruction" />
		</td>
	</tr>
	<tr>
		<td></td>
		<td>
			<div id="offlinefile">
				<c:forEach var="file" items="${forumForm.offlineFileList}">
					<li>
						<c:out value="${file.fileName}" />
						<c:set var="viewURL">
							<html:rewrite page="/download/?uuid=${file.fileUuid}&preferDownload=false" />
						</c:set>
						<html:link href="javascript:launchInstructionsPopup('${viewURL}')">
							<fmt:message key="label.view" />
						</html:link>
						<c:set var="downloadURL">
							<html:rewrite page="/download/?uuid=${file.fileUuid}&versionID=${file.fileVersionId}&preferDownload=true" />
						</c:set>
						<html:link href="${downloadURL}">
							<fmt:message key="label.download" />
						</html:link>
						<c:set var="deleteoffline">
							<html:rewrite page="/authoring/deleteOffline.do?toolContentID=${forumForm.toolContentID}&uuID=${file.fileUuid}&versionID=${file.fileVersionId}" />
						</c:set>
						<html:link href="javascript:loadDoc('${deleteoffline}','offlinefile')">
							<fmt:message key="label.authoring.offline.delete" />
						</html:link>
					</li>
				</c:forEach>
			</div>
		</td>
	</tr>
	<tr>
		<td class="field-name">
			<fmt:message key="label.authoring.offline.file" />
			:
		</td>
		<td>
			<html:file property="offlineFile">
				<fmt:message key="label.authoring.choosefile.button" />
			</html:file>
			<c:set var="uploadoffline">
				<html:rewrite page="/authoring/uploadOffline.do" />
			</c:set>
			<a href="#" onclick="doSubmit('${uploadoffline}')" class="button">
				<fmt:message key="label.authoring.upload.offline.button" />
			</a>
		</td>
	</tr>
</table>
