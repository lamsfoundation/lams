<%@ include file="/common/taglibs.jsp"%>

<table cellpadding="0">
	<!-- Instructions Row -->
	<tr>
		<td class="field-name">
			<fmt:message key="label.authoring.online.instruction" />
			:
		</td>
	</tr>
	<tr>
		<td>
			<c:out value="${authoring.onlineInstruction}" escapeXml="false" />
		</td>
	</tr>
	<tr>
		<td class="field-name" colspan="2">
			<fmt:message key="label.authoring.online.filelist" />
			:
		</td>
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
					<html:link href="../download/?uuid=${file.uuID}&preferDownload=true">
						<fmt:message key="label.download" />
					</html:link>
				</li>
			</c:forEach>
			</ul>
		</td>
	</tr>
</table>

<hr />

<table cellpadding="0">
	<tr>
		<td class="field-name-alternative-color">
			<fmt:message key="label.authoring.offline.instruction" />
			:
		</td>
	</tr>
	<tr>
		<td>
			<c:out value="${authoring.offlineInstruction}" escapeXml="false" />
		</td>
	</tr>
	<tr>
		<td class="field-name-alternative-color" colspan="2">
			<fmt:message key="label.authoring.offline.filelist" />
			:
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<ul>
				<c:forEach var="file" items="${authoring.offlineFiles}">
					<li>
						<c:out value="${file.name}" />
						<html:link href="javascript:launchInstructionsPopup('download/?uuid=${file.uuID}&preferDownload=false')">
							<fmt:message key="label.view" />
						</html:link>
						<html:link href="../download/?uuid=${file.uuID}&preferDownload=true">
							<fmt:message key="label.download" />
						</html:link>
					</li>
				</c:forEach>
			</ul>
		</td>
	</tr>
</table>
