<%@include file="fileinfo.jsp"%>

<tr>
	<td>
		<fmt:message key="label.learner.marks" />
		:
	</td>
	<td>
		<c:choose>
			<c:when test="${empty fileInfo.marks}">
				<fmt:message key="label.learner.notAvailable" />
			</c:when>
			<c:otherwise>
				<c:out value="${fileInfo.marks}" escapeXml="true" />
			</c:otherwise>
		</c:choose>
	</td>
	<td>
		<html:link href="javascript:updateMark(${fileInfo.submissionID},${fileInfo.reportID},${toolSessionID},${fileInfo.owner.userID});" 
				  property="submit" styleClass="btn btn-default">
			<fmt:message key="label.monitoring.updateMarks.button" />
		</html:link>
	</td> 
</tr>
<tr>
	<td>
		<fmt:message key="label.learner.comments" />
		:
	</td>
	<td colspan="2">
		<c:choose>
			<c:when test="${empty fileInfo.comments}">
				<fmt:message key="label.learner.notAvailable" />
			</c:when>
			<c:otherwise>
				<c:out value="${fileInfo.comments}" escapeXml="false" />
			</c:otherwise>
		</c:choose>
	</td>
</tr>
<tr style="margin-bottom: 5px; border-bottom: 5px solid #ddd">
	<td>
		<fmt:message key="label.monitor.mark.markedFile" />
		:
	</td>
	
	<c:choose>
		<c:when test="${empty fileInfo.markFileUUID}">
			<td colspan="2">
				<fmt:message key="label.learner.notAvailable" />
			</td>
		</c:when>
		<c:otherwise>
			<td>
				<c:out value="${fileInfo.markFileName}" />
			</td>
			<td>
				<c:set var="markFileViewURL">
					<html:rewrite page="/download/?uuid=${fileInfo.markFileUUID}&versionID=${fileInfo.markFileVersionID}&preferDownload=false" />
				</c:set>
				<html:link href="javascript:launchInstructionsPopup('${markFileViewURL}')" styleClass="btn btn-default">
					<fmt:message key="label.view" />
				</html:link>
				<c:set var="markFileDownloadURL">
					<html:rewrite page="/download/?uuid=${fileInfo.markFileUUID}&versionID=${fileInfo.markFileVersionID}&preferDownload=true" />
				</c:set>
				<html:link href="${markFileDownloadURL}" styleClass="btn btn-default loffset10">
					<fmt:message key="label.download" />
				</html:link>
			</td>
		</c:otherwise>
	</c:choose>
</tr>
