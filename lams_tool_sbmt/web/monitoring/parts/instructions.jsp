<%@ include file="/common/taglibs.jsp"%>

<table cellpadding="0" border="0">
	<!-- Instructions Row -->
	<tr>
		<td class="field-name" width="30%">
			<fmt:message key="label.authoring.online.instruction" />
			:
		</td>
		<td>
			<c:out value="${authoring.onlineInstruction}" escapeXml="false" />
		</td>
	</tr>
	<tr>
		<td class="field-name-alternative-color">
			<fmt:message key="label.authoring.online.filelist" />
			:
		</td>
		<td></td>
	</tr>
	<tr>
		<td colspan="2">
			<ul>
			<c:forEach var="file" items="${authoring.onlineFiles}">
				<li>
					<c:out value="${file.name}" />
					<html:link href="javascript:launchInstructionsPopup('download/?uuid=${file.uuID}&preferDownload=false')">
						<fmt:message key="label.view" />
					</html:link>
					&nbsp;&nbsp;
					<c:set var="downloadURL">
							<html:rewrite page="/download/?uuid=${file.uuID}&versionID=${file.versionID}&preferDownload=true" />
					</c:set>
					<html:link href="${downloadURL}">
						<fmt:message key="label.download" />
					</html:link>
				</li>
			</c:forEach>
			</ul>
		</td>
	</tr>
	<tr>
		<td colspan="2">
		<hr size="1" style="width:550px"/>
		</td>
	</tr>
	<tr>
		<td class="field-name" width="30%">
			<fmt:message key="label.authoring.offline.instruction" />
			:
		</td>
		<td>
			<c:out value="${authoring.offlineInstruction}" escapeXml="false" />
		</td>
	</tr>
	<tr>
		<td class="field-name-alternative-color">
			<fmt:message key="label.authoring.offline.filelist" />
			:
		</td>
		<td></td>		
	</tr>
	<tr>
		<td colspan="2">
			<ul>
				<c:forEach var="file" items="${authoring.offlineFiles}">
					<li>
						<c:out value="${file.name}" />
						<html:link href="javascript:launchInstructionsPopup('download/?uuid=${file.uuID}&preferDownload=false')">
							<fmt:message key="label.view" />
						</html:link>&nbsp;&nbsp;
						<c:set var="downloadURL">
								<html:rewrite page="/download/?uuid=${file.uuID}&versionID=${file.versionID}&preferDownload=true" />
						</c:set>
						<html:link href="${downloadURL}">
							<fmt:message key="label.download" />
						</html:link>
					</li>
				</c:forEach>
			</ul>
		</td>
	</tr>
</table>
