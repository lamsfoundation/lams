<c:if test="${status.first}">
	<tr>
		<th colspan="3" class="active">
			<c:out value="${fileInfo.owner.firstName}" />&nbsp;<c:out value="${fileInfo.owner.lastName}" />&nbsp;<fmt:message key="label.submit.file.suffix" />:
		</th>
	<tr>
</c:if> 

<tr>
	<td>
		<fmt:message key="label.learner.filePath" />
		:
	</td>
	<td>
		<c:out value="${fileInfo.filePath}" />
	</td>
	<td>
		<c:set var="viewURL">
			<html:rewrite page="/download/?uuid=${fileInfo.uuID}&versionID=${fileInfo.versionID}&preferDownload=false" />
		</c:set>
		<html:link href="javascript:launchInstructionsPopup('${viewURL}')" styleClass="btn btn-default">
			<fmt:message key="label.view" />
		</html:link>
		<c:set var="downloadURL">
			<html:rewrite page="/download/?uuid=${fileInfo.uuID}&versionID=${fileInfo.versionID}&preferDownload=true" />
		</c:set>
		<html:link href="${downloadURL}" styleClass="btn btn-default loffset10">
			<fmt:message key="label.download" />
		</html:link>
	</td>
</tr>
<tr>
	<td>
		<fmt:message key="label.learner.fileDescription" />
		:
	</td>
	<td colspan="2">
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
	<td>
		<fmt:message key="label.learner.dateOfSubmission" />
		:
	</td>
	<td colspan="2">
		<lams:Date value="${fileInfo.dateOfSubmission}" />
	</td>
</tr>