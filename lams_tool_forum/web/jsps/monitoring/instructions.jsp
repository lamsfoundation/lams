<%@ include file="/includes/taglibs.jsp"%>
<html:errors />
<table cellpadding="0">
	<!-- Instructions Row -->
	<tr>
		<td class="field-name" width="30%">
			<fmt:message key="label.authoring.online.instruction" />
			:
		</td>
		<td>
			<c:out value="${forumBean.forum.onlineInstructions}" escapeXml="false" />
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<div id="onlinefile">
				<ul>
					<c:forEach var="file" items="${forumBean.onlineFileList}">
						<li>
							<c:out value="${file.fileName}" />
							<c:set var="viewURL">
								<html:rewrite page="/download/?uuid=${file.fileUuid}&preferDownload=false" />
							</c:set>
							<a href="javascript:launchInstructionsPopup('<c:out value='${viewURL}' escapeXml='false'/>')"> <fmt:message key="label.view" /> </a> &nbsp;
							&nbsp;&nbsp;
							<c:set var="downloadURL">
								<html:rewrite page="/download/?uuid=${file.fileUuid}&versionID=${file.fileVersionId}&preferDownload=true" />
							</c:set>
							<a href="<c:out value='${downloadURL}' escapeXml='false'/>"> <fmt:message key="label.download" /> </a> &nbsp;
						</li>
					</c:forEach>
				</ul>
			</div>
		</td>
	</tr>

	<tr>
		<td class="field-name" width="30%">
			<fmt:message key="label.authoring.offline.instruction" />
			:
		</td>
		<td>
			<c:out value="${forumBean.forum.offlineInstructions}" escapeXml="false" />
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<div id="offlinefile">
				<ul>
					<c:forEach var="file" items="${forumBean.offlineFileList}">
						<li>
							<c:out value="${file.fileName}" />
							<c:set var="viewURL">
								<html:rewrite page="/download/?uuid=${file.fileUuid}&preferDownload=false" />
							</c:set>
							<html:link href="javascript:launchInstructionsPopup('${viewURL}')">
								<fmt:message key="label.view" />
							</html:link>&nbsp;&nbsp;
							<c:set var="downloadURL">
								<html:rewrite page="/download/?uuid=${file.fileUuid}&versionID=${file.fileVersionId}&preferDownload=true" />
							</c:set>
							<html:link href="${downloadURL}">
								<fmt:message key="label.download" />
							</html:link>
						</li>
					</c:forEach>
				</ul>
			</div>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<html:errors property="instruction.globel" />
		</td>
</table>



