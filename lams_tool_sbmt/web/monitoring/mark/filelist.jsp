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
		<html:link href="javascript:updateMark(${fileInfo.submissionID},${fileInfo.reportID},${toolSessionID},${fileInfo.owner.userID});" 
				  property="submit" styleClass="btn btn-xs btn-default">
			<i class="fa fa-pencil-square-o" title="<fmt:message key="label.monitoring.updateMarks.button" />"></i> <span class="hidden-xs"><fmt:message key="label.monitoring.updateMarks.button" /></span>
		</html:link>
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
					<html:rewrite page="/download/?uuid=${fileInfo.markFileUUID}&versionID=${fileInfo.markFileVersionID}&preferDownload=false" />
				</c:set>
				<html:link href="javascript:launchInstructionsPopup('${markFileViewURL}')" styleClass="btn btn-xs btn-default">
					<i class="fa fa-eye" title="<fmt:message key="label.view" />"></i>
				</html:link>
				<c:set var="markFileDownloadURL">
					<html:rewrite page="/download/?uuid=${fileInfo.markFileUUID}&versionID=${fileInfo.markFileVersionID}&preferDownload=true" />
				</c:set>
				<html:link href="${markFileDownloadURL}" styleClass="btn btn-xs btn-default loffset10">
					<i class="fa fa-download" title="<fmt:message key="label.download" />"></i>
				</html:link>
			</div>
		</dd>				
	</c:when>
</c:choose>

