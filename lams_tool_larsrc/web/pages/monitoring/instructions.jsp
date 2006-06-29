<%@ include file="/common/taglibs.jsp"%>


<table cellpadding="0">
	<!-- Instructions Row -->
	<tr>
		<td class="field-name" width="30%">
			<fmt:message key="label.authoring.online.instruction" />
			:
		</td>
		<td>
			<c:out value="${resource.onlineInstructions}" escapeXml="false" />
		</td>
	</tr>
	<tr>
		<td>
			&nbsp;
		</td>
		<td>

			<div id="onlinefile">
				<c:forEach var="file" items="${resource.onlineFileList}">
					<li>
						<c:out value="${file.fileName}" />
						<c:set var="viewURL">
							<html:rewrite page="/download/?uuid=${file.fileUuid}&preferDownload=false" />
						</c:set>
						<a href="javascript:launchInstructionsPopup('<c:out value='${viewURL}' escapeXml='false'/>')"> <fmt:message key="label.view" /> </a> &nbsp;
						<c:set var="downloadURL">
							<html:rewrite page="/download/?uuid=${file.fileUuid}&versionID=${file.fileVersionId}&preferDownload=true" />
						</c:set>
						<a href="<c:out value='${downloadURL}' escapeXml='false'/>"> <fmt:message key="label.download" /> </a>
					</li>
				</c:forEach>
			</div>
		</td>
	</tr>
	<tr>
		<td class="field-name"  width="30%">
			<fmt:message key="label.authoring.offline.instruction" />
			:
		</td>
		<td>
			<c:out value="${resource.offlineInstructions}" escapeXml="false" />
		</td>
	</tr>
	<tr>
		<td>
			&nbsp;
		</td>
		<td>
			<div id="offlinefile">
				<c:forEach var="file" items="${resource.offlineFileList}">
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
					</li>
				</c:forEach>
			</div>
		</td>
	</tr>
</table>


