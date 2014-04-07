<c:if test="${status.first}">
	<tr>
		<td colspan="2">
			<c:out value="${fileInfo.owner.firstName}" />
			<c:out value="${fileInfo.owner.lastName}" />
			<fmt:message key="label.submit.file.suffix" />:
		</td>
	<tr>
</c:if> 

<tr>
	<td class="field-name">
		<fmt:message key="label.learner.filePath" />
		:
	</td>
	<td>
		<c:out value="${fileInfo.filePath}" />
		<c:set var="viewURL">
			<html:rewrite page="/download/?uuid=${fileInfo.uuID}&versionID=${fileInfo.versionID}&preferDownload=false" />
		</c:set>
		<a href="javascript:launchInstructionsPopup('<c:out value='${viewURL}' escapeXml='false'/>')" class="button"> <fmt:message key="label.view" /> </a>&nbsp;
		<c:set var="downloadURL">
			<html:rewrite page="/download/?uuid=${fileInfo.uuID}&versionID=${fileInfo.versionID}&preferDownload=true" />
		</c:set>
		<a href="<c:out value='${downloadURL}' escapeXml='false'/>"  class="button"> <fmt:message key="label.download" /> </a>
	</td>
</tr>
<tr>
	<td class="field-name">
		<fmt:message key="label.learner.fileDescription" />
		:
	</td>
	<td>
		<c:choose>
			<c:when test="${empty fileInfo.fileDescription}">
				<fmt:message key="label.learner.notAvailable" />
			</c:when>
			<c:otherwise>
				<lams:out value="${fileInfo.fileDescription}" escapeHtml="true" />
			</c:otherwise>
		</c:choose>
	</td>
</tr>
<tr>
	<td class="field-name">
		<fmt:message key="label.learner.dateOfSubmission" />
		:
	</td>
	<td>
		<lams:Date value="${fileInfo.dateOfSubmission}" />
	</td>
</tr>