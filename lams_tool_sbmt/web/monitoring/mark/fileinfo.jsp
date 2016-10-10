<%@ include file="/common/taglibs.jsp"%>
<c:set var="localeLanguage"><lams:user property="localeLanguage" /></c:set>
<script type="text/javascript" src="<lams:LAMSURL />/includes/javascript/jquery.timeago.js"></script>
<script type="text/javascript" src="<lams:LAMSURL />/includes/javascript/timeagoi18n/jquery.timeago.${fn:toLowerCase(localeLanguage)}.js"></script><c:if test="${status.first}">
	<tr>
		<th colspan="3" class="active">
			<c:out value="${fileInfo.owner.firstName}" />&nbsp;<c:out value="${fileInfo.owner.lastName}" />&nbsp;<fmt:message key="label.submit.file.suffix" />:
		</th>
	<tr>
</c:if> 

<tr>
	<c:choose>
	<c:when test="${fileInfo.removed}">
		<td>
			<span style="color:red"><fmt:message key="label.deleted"/> : 
		</td>
		<td>
			<span style="color:red"><c:out value="${fileInfo.filePath}" /></span>
		</td>
	</c:when>
	<c:otherwise>
		<td>
			<fmt:message key="label.learner.filePath" />
		</td>
		<td>
			<c:out value="${fileInfo.filePath}" />
		</td>
	</c:otherwise>
	</c:choose>
	<td>
		<c:choose>
		<c:when test="${fileInfo.removed and empty updateMode}">
			<html:link href="javascript:restoreLearnerFile(${fileInfo.submissionID},${toolSessionID},${fileInfo.owner.userID},'${fileInfo.filePath}');" styleClass="btn btn-default loffset10">
				<fmt:message key="label.monitoring.original.learner.file.restore" />
			</html:link>
		</c:when>

		<c:otherwise>
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
			<c:if test="${empty updateMode}">
				<html:link href="javascript:removeLearnerFile(${fileInfo.submissionID},${toolSessionID},${fileInfo.owner.userID},'${fileInfo.filePath}');" styleClass="btn btn-default loffset10">
					<fmt:message key="label.monitoring.original.learner.file.delete" />
				</html:link>
			</c:if>
		</c:otherwise>
		</c:choose>
	</td>
</tr>

<c:if test="${not fileInfo.removed}">
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
</c:if>

<c:set var="style">${fileInfo.removed?"style=\"margin-bottom: 5px; border-bottom: 5px solid #ddd\"":""}</c:set>
<tr ${style}>
	<td>
		<fmt:message key="label.learner.dateOfSubmission" />
		:
	</td>
	<td colspan="2">
		<lams:Date value="${fileInfo.dateOfSubmission}" timeago="true"/>
	</td>
</tr>

<c:if test="${not fileInfo.removed and empty updateMode}">
<!--  do not show full details if removed or on mark screen --> 
	<%@include file="filelist.jsp"%>
</c:if>
<script type="text/javascript">
	jQuery(document).ready(function() {
		jQuery("time.timeago").timeago();
	});
</script>