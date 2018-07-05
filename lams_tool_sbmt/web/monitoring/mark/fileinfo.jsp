<%@ include file="/common/taglibs.jsp"%>
<c:set var="localeLanguage"><lams:user property="localeLanguage" /></c:set>
<script type="text/javascript" src="<lams:LAMSURL />/includes/javascript/jquery.timeago.js"></script>
<script type="text/javascript" src="<lams:LAMSURL />/includes/javascript/timeagoi18n/jquery.timeago.${fn:toLowerCase(localeLanguage)}.js"></script>
<c:if test="${status.first}">
			<div class="lead well" style="padding:5px"><lams:Portrait userId="${fileInfo.owner.userID}"/>&nbsp;<strong><c:out value="${fileInfo.owner.firstName}" />&nbsp;<c:out value="${fileInfo.owner.lastName}" /></strong> </div>
</c:if> 

<dl class="dl-horizontal">
	<c:choose>
	<c:when test="${fileInfo.removed}">
		<dt>
			<span style="color:red"><fmt:message key="label.deleted"/> : 
		</dt>
		<dd>
			<span style="color:red"><c:out value="${fileInfo.filePath}" /></span>
		</dd>
	</c:when>
	<c:otherwise>
		<dt>
			<fmt:message key="label.learner.filePath" />:
		</dt>
		<dd>
			<c:out value="${fileInfo.filePath}" />
	</c:otherwise>
	</c:choose>
	<div id="actionButtons" class="pull-right">
		<c:choose>
		<c:when test="${fileInfo.removed and empty updateMode}">
			<html:link href="javascript:restoreLearnerFile(${fileInfo.submissionID},${toolSessionID},${fileInfo.owner.userID},'${fileInfo.filePath}');" styleClass="btn btn-xs btn-default loffset10">
				<i class="fa fa-life-saver" title="<fmt:message key="label.monitoring.original.learner.file.restore" />"></i> <span class="hidden-xs"><fmt:message key="label.monitoring.original.learner.file.restore" /></span>
			</html:link>
		</c:when>
	
		<c:otherwise>
			<c:set var="viewURL">
				<html:rewrite page="/download/?uuid=${fileInfo.uuID}&versionID=${fileInfo.versionID}&preferDownload=false" />
			</c:set>
			<html:link href="javascript:launchInstructionsPopup('${viewURL}')" styleClass="btn btn-xs btn-default">
				<i class="fa fa-eye" title="<fmt:message key="label.view" />"></i> <span class="hidden-xs"><fmt:message key="label.view" /></span>
			</html:link>
			<c:set var="downloadURL">
				<html:rewrite page="/download/?uuid=${fileInfo.uuID}&versionID=${fileInfo.versionID}&preferDownload=true" />
			</c:set>
			<html:link href="${downloadURL}" styleClass="btn btn-xs btn-default loffset10">
				<i class="fa fa-download" title="<fmt:message key="label.download" />"></i> <span class="hidden-xs"><fmt:message key="label.download" /></span>
			</html:link>
			<c:if test="${empty updateMode}">
				<html:link href="javascript:removeLearnerFile(${fileInfo.submissionID},${toolSessionID},${fileInfo.owner.userID},'${fileInfo.filePath}');" styleClass="btn btn-xs btn-danger loffset10">
					<i class="fa fa-trash" title="<fmt:message key="label.monitoring.original.learner.file.delete" />"></i> <span class="hidden-xs"><fmt:message key="label.monitoring.original.learner.file.delete" /></span>
				</html:link>
			</c:if>
		</c:otherwise>
		</c:choose>
	</div>
		</dd>


<c:if test="${not fileInfo.removed}">
	<dt>
		<fmt:message key="label.learner.fileDescription" />:
	</dt>
	<dd>
		<c:choose>
			<c:when test="${empty fileInfo.fileDescription}">
				<fmt:message key="label.learner.notAvailable" />
			</c:when>
			<c:otherwise>
				<lams:out value="${fileInfo.fileDescription}" escapeHtml="true" />
			</c:otherwise>
		</c:choose>
	</dd>

</c:if>

<c:set var="style">${fileInfo.removed?"style=\"margin-bottom: 5px; border-bottom: 5px solid #ddd\"":""}</c:set>
	<dt>
		<fmt:message key="label.learner.dateOfSubmission" />:
	</dt>
	<dd>
		<lams:Date value="${fileInfo.dateOfSubmission}" timeago="true"/>
	</dd>

<c:if test="${not fileInfo.removed and empty updateMode}">
<!--  do not show full details if removed or on mark screen --> 
	<%@include file="filelist.jsp"%>
</c:if>
</dl>
<script type="text/javascript">
	jQuery(document).ready(function() {
		jQuery("time.timeago").timeago();
	});	
</script>
