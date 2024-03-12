<div class="row">
	<div class="col-2 fw-bold">
		<fmt:message key="label.learner.marks" />:
	</div>
	<div class="col">
		<c:choose>
			<c:when test="${empty fileInfo.marks}">
				<div class="badge text-bg-warning">
					<fmt:message key="label.learner.notAvailable" />
				</div>
			</c:when>
			<c:otherwise>
				<c:out value="${fileInfo.marks}" escapeXml="true" />
			</c:otherwise>
		</c:choose>
		
		<div id="updateMarksButton" class="float-end">
			<button type="button" onclick="updateMark(${fileInfo.submissionID},${fileInfo.reportID},${toolSessionID},${fileInfo.owner.userID})" 
					class="btn btn-sm btn-light">
				<i class="fa fa-pencil-square me-1" title="<fmt:message key="label.monitoring.updateMarks.button" />"></i> 
				<span class="d-none d-sm-block float-end"><fmt:message key="label.monitoring.updateMarks.button" /></span>
			</button>
		</div>	
	</div>
</div>

<!--  Comments -->
<c:choose>
	<c:when test="${not empty fileInfo.comments}">
		<div class="row">
			<div class="col-2 fw-bold">
				<fmt:message key="label.learner.comments" />:
			</div>
			<div class="col">
				<div id="comments-${fileInfo.submissionID}">
					<c:out value="${fileInfo.comments}" escapeXml="false" />
				</div>
				
				<script type="text/javascript">
					jQuery(document).ready(function() {
						$('#comments-${fileInfo.submissionID}').readmore({
						    speed: 500,
						    collapsedHeight: 85
						});								
					});
				</script>			
			</div>
		</div>
	</c:when>
</c:choose>

<!--  Response file -->
<c:choose>
	<c:when test="${not empty fileInfo.markFileUUID}">
		<div class="row">
			<div class="col-2 fw-bold">
				<fmt:message key="label.monitor.mark.markedFile" />:
			</div>		
			<div class="col">	
				<c:out value="${fileInfo.markFileName}" />
				
				<div id="markedActionButtons" class="float-end">
					<c:set var="markFileViewURL">
						<lams:WebAppURL/>download/?uuid=${fileInfo.markFileDisplayUuid}&versionID=${fileInfo.markFileVersionID}&preferDownload=false
					</c:set>
					<button type="button" onclick="launchInstructionsPopup('${markFileViewURL}')" class="btn btn-sm btn-light">
						<i class="fa fa-eye me-1" title="<fmt:message key="label.view" />"></i>
					</button>
					
					<c:set var="markFileDownloadURL">
						<lams:WebAppURL/>download/?uuid=${fileInfo.markFileDisplayUuid}&versionID=${fileInfo.markFileVersionID}&preferDownload=true
					</c:set>
					<a href="${markFileDownloadURL}" class="btn btn-sm btn-light ms-2">
						<i class="fa fa-download me-1" title="<fmt:message key="label.download" />"></i>
					</a>
				</div>
			</div>	
		</div>			
	</c:when>
</c:choose>
