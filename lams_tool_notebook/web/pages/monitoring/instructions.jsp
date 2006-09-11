<%@ include file="/common/taglibs.jsp"%>

<c:set var="dto" value="${notebookDTO}" />
<table>
	<tbody>
		<tr>
			<td class="field-name" width="30%">
				<fmt:message key="instructions.onlineInstructions" />
			</td>
			<td>
				<c:out value="${dto.onlineInstructions}" escapeXml="false" />
			</td>
		</tr>

		<tr>
			<td>
				&nbsp;
			</td>
			<td>
				<div id="onlinefile">
					<c:if test="${not empty dto.onlineInstructionsFiles}">
						<ul>
							<c:forEach var="file" items="${dto.onlineInstructionsFiles}">
								<li>
									<c:out value="${file.fileName}" />

									<c:set var="viewURL">
										<html:rewrite page="/download/?uuid=${file.fileUuid}&amp;preferDownload=false" />
									</c:set>
									<a href="javascript:launchInstructionsPopup('<c:out value='${viewURL}' escapeXml='false'/>')"> <fmt:message key="link.view" /> </a> &nbsp;

									<c:set var="downloadURL">
										<html:rewrite page="/download/?uuid=${file.fileUuid}&amp;versionID=${file.fileVersionId}&amp;preferDownload=true" />
									</c:set>
									<a href="<c:out value='${downloadURL}' escapeXml='false'/>"> <fmt:message key="link.download" /> </a>
								</li>
							</c:forEach>
						</ul>
					</c:if>
				</div>
			</td>
		</tr>
	</tbody>
</table>



<table>
	<tbody>
		<tr>
			<td class="field-name" width="30%">
				<fmt:message key="instructions.offlineInstructions" />
			</td>
			<td>
				<c:out value="${dto.offlineInstructions}" escapeXml="false" />
			</td>
		</tr>

		<tr>
			<td>
				&nbsp;
			</td>
			<td>
				<div id="offlinefile">
					<c:if test="${not empty dto.offlineInstructionsFiles}">
						<ul>
							<c:forEach var="file" items="${dto.offlineInstructionsFiles}">
								<li>
									<c:out value="${file.fileName}" />

									<c:set var="viewURL">
										<html:rewrite page="/download/?uuid=${file.fileUuid}&amp;preferDownload=false" />
									</c:set>
									<a href="javascript:launchInstructionsPopup('<c:out value='${viewURL}' escapeXml='false'/>')"> <fmt:message key="link.view" /> </a> &nbsp;

									<c:set var="downloadURL">
										<html:rewrite page="/download/?uuid=${file.fileUuid}&amp;versionID=${file.fileVersionId}&amp;preferDownload=true" />
									</c:set>
									<a href="<c:out value='${downloadURL}' escapeXml='false'/>"> <fmt:message key="link.download" /> </a>
								</li>
							</c:forEach>
						</ul>
					</c:if>
				</div>
			</td>
		</tr>
	</tbody>
</table>
