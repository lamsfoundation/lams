<%@ include file="/common/taglibs.jsp"%>
<c:set var="localeLanguage"><lams:user property="localeLanguage" /></c:set>
<script type="text/javascript" src="<lams:LAMSURL />/includes/javascript/jquery.timeago.js"></script>
<script type="text/javascript" src="<lams:LAMSURL />/includes/javascript/timeagoi18n/jquery.timeago.${fn:toLowerCase(localeLanguage)}.js"></script>

<c:if test="${status.first}">
	<div class="card-header">
		<lams:Portrait userId="${fileInfo.owner.userID}"/>&nbsp;
		<strong><c:out value="${fileInfo.owner.firstName}" />&nbsp;<c:out value="${fileInfo.owner.lastName}" /></strong> 
	</div>
</c:if> 

<div class="p-2">
<div class="row">
	<c:choose>
		<c:when test="${fileInfo.removed}">
			<div class="col-2 fw-bold">
				<span style="color:red"><fmt:message key="label.deleted"/> : 
			</div>
			<div class="col">
				<span style="color:red"><c:out value="${fileInfo.filePath}" /></span>
		</c:when>
		
		<c:otherwise>
			<div class="col-2 fw-bold">
				<fmt:message key="label.learner.filePath" />:
			</div>
			<div class="col">
				<c:out value="${fileInfo.filePath}" />
		</c:otherwise>
	</c:choose>
	
				<div id="actionButtons" class="float-end">
					<c:choose>
						<c:when test="${fileInfo.removed and empty updateMode}">
							<button type="button" onclick="restoreLearnerFile(${fileInfo.submissionID},${toolSessionID},${fileInfo.owner.userID},'${fileInfo.filePath}');" class="btn btn-sm btn-light ms-2">
								<i class="fa-regular fa-life-ring me-1" title="<fmt:message key="label.monitoring.original.learner.file.restore" />"></i> 
								<span class="d-none d-sm-block float-end"><fmt:message key="label.monitoring.original.learner.file.restore" /></span>
							</button>
						</c:when>
					
						<c:otherwise>
							<c:set var="viewURL">
								<lams:WebAppURL/>download/?uuid=${fileInfo.displayUuid}&versionID=${fileInfo.versionID}&preferDownload=false
							</c:set>
							<button type="button" onclick="launchInstructionsPopup('${viewURL}')" class="btn btn-sm btn-light ms-2">
								<i class="fa fa-eye me-1" title="<fmt:message key="label.view" />"></i> 
								<span class="d-none d-sm-block float-end"><fmt:message key="label.view" /></span>
							</button>
							
							<c:set var="downloadURL">
								<lams:WebAppURL/>download/?uuid=${fileInfo.displayUuid}&versionID=${fileInfo.versionID}&preferDownload=true
							</c:set>
							<a href="${downloadURL}" class="btn btn-sm btn-light ms-2">
								<i class="fa fa-download me-1" title="<fmt:message key="label.download" />"></i> 
								<span class="d-none d-sm-block float-end"><fmt:message key="label.download" /></span>
							</a>
							
							<c:if test="${empty updateMode}">
								<button type="button" onclick="removeLearnerFile(${fileInfo.submissionID},${toolSessionID},${fileInfo.owner.userID},'${fileInfo.filePath}')" class="btn btn-sm btn-light ms-2">
									<i class="fa fa-trash me-1" title="<fmt:message key="label.monitoring.original.learner.file.delete" />"></i> 
									<span class="d-none d-sm-block float-end"><fmt:message key="label.monitoring.original.learner.file.delete" /></span>
								</button>
							</c:if>
						</c:otherwise>
					</c:choose>
				</div>
			</div>
</div>

<div class="row">
	<c:if test="${not fileInfo.removed}">
		<div class="col-2 fw-bold">
			<fmt:message key="label.learner.fileDescription" />:
		</div>
		<div class="col">
			<c:choose>
				<c:when test="${empty fileInfo.fileDescription}">
					<div class="badge text-bg-warning">
						<fmt:message key="label.learner.notAvailable" />
					</div>
				</c:when>
				<c:otherwise>
					<lams:out value="${fileInfo.fileDescription}" escapeHtml="true" />
				</c:otherwise>
			</c:choose>
		</div>
	</c:if>
</div>

<div class="row">
	<c:set var="style">${fileInfo.removed?"style=\"margin-bottom: 5px; border-bottom: 5px solid #ddd\"":""}</c:set>
	<div class="col-2 fw-bold">
		<fmt:message key="label.learner.dateOfSubmission" />:
	</div>
	<div class="col">
		<lams:Date value="${fileInfo.dateOfSubmission}" timeago="true"/>
	</div>
</div>

<c:if test="${not fileInfo.removed and empty updateMode}">
	<!--  do not show full details if removed or on mark screen --> 
	<%@include file="filelist.jsp"%>
</c:if>
</div>

<script type="text/javascript">
	jQuery(document).ready(function() {
		jQuery("time.timeago").timeago();
	});	
</script>
