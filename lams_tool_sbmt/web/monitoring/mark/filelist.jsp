<dt>
	<fmt:message key="label.learner.marks" />:
</dt>
<dd>
	<c:choose>
		<c:when test="${empty fileInfo.marks}">
			<fmt:message key="label.learner.notAvailable" />
		</c:when>
		<c:otherwise>
			<c:out value="${fileInfo.marks}" escapeXml="true" />
		</c:otherwise>
	</c:choose>
	<div id="updateMarksButton" class="pull-right">
		<a href="javascript:updateMark(${fileInfo.submissionID},${fileInfo.reportID},${toolSessionID},${fileInfo.owner.userID});" 
				  name="submit" class="btn btn-xs btn-default">
			<i class="fa fa-pencil-square-o" title="<fmt:message key="label.monitoring.updateMarks.button" />"></i> <span class="hidden-xs"><fmt:message key="label.monitoring.updateMarks.button" /></span>
		</a>
	</div>	
</dd>

<!--  Comments -->
<c:choose>
	<c:when test="${not empty fileInfo.comments}">
		<dt>
			<fmt:message key="label.learner.comments" />:
		</dt>
		<dd>
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
		</dd>	
	</c:when>
</c:choose>

<!--  Response file -->
<c:choose>
	<c:when test="${not empty fileInfo.markFileUUID}">
		<dt>
				<fmt:message key="label.monitor.mark.markedFile" />:
		</dt>		
		<dd>	
			<c:out value="${fileInfo.markFileName}" />
			<div id="markedActionButtons" class="pull-right">
				<c:set var="markFileViewURL">
					<lams:WebAppURL/>download/?uuid=${fileInfo.markFileDisplayUuid}&versionID=${fileInfo.markFileVersionID}&preferDownload=false
				</c:set>
				<a href="javascript:launchInstructionsPopup('${markFileViewURL}')" class="btn btn-xs btn-default">
					<i class="fa fa-eye" title="<fmt:message key="label.view" />"></i>
				</a>
				<c:set var="markFileDownloadURL">
					<lams:WebAppURL/>download/?uuid=${fileInfo.markFileDisplayUuid}&versionID=${fileInfo.markFileVersionID}&preferDownload=true
				</c:set>
				<a href="${markFileDownloadURL}" class="btn btn-xs btn-default loffset10">
					<i class="fa fa-download" title="<fmt:message key="label.download" />"></i>
				</a>
			</div>
		</dd>				
	</c:when>
</c:choose>

