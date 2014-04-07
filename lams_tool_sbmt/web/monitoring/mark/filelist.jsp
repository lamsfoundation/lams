<%@include file="fileinfo.jsp"%>
<tr>
	<td class="field-name">
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
		<html:link href="javascript:updateMark(${fileInfo.submissionID},${fileInfo.reportID},${toolSessionID},${fileInfo.owner.userID});" 
			property="submit" styleClass="button">
			<fmt:message key="label.monitoring.updateMarks.button" />
		</html:link>
	</td> 
</tr>
<tr>
	<td class="field-name">
		<fmt:message key="label.learner.comments" />
		:
	</td>
	<td>
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
<tr>
	<td class="field-name">
		<fmt:message key="label.monitor.mark.markedFile" />
		:
	</td>
	<td>
		<c:choose>
			<c:when test="${empty fileInfo.markFileUUID}">
				<fmt:message key="label.learner.notAvailable" />
			</c:when>
			<c:otherwise>
				<c:out value="${fileInfo.markFileName}" />
				<c:set var="markFileViewURL">
					<html:rewrite page="/download/?uuid=${fileInfo.markFileUUID}&versionID=${fileInfo.markFileVersionID}&preferDownload=false" />
				</c:set>
				<a href="javascript:launchInstructionsPopup('<c:out value='${markFileViewURL}' escapeXml='false'/>')" class="button"> <fmt:message key="label.view" /> </a>&nbsp;
				<c:set var="markFileDownloadURL">
					<html:rewrite page="/download/?uuid=${fileInfo.markFileUUID}&versionID=${fileInfo.markFileVersionID}&preferDownload=true" />
				</c:set>
				<a href="<c:out value='${markFileDownloadURL}' escapeXml='false'/>"  class="button"> <fmt:message key="label.download" /> </a>
			</c:otherwise>
		</c:choose>
	</td>
</tr>

<tr>
	<td colspan="2">
		<hr size="1" style="width:500px"/>
		<br />
	</td>
</tr>
