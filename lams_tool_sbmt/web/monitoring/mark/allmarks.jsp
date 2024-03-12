<!DOCTYPE html>
<%@include file="/common/taglibs.jsp"%>
<c:set var="tool"><lams:WebAppURL /></c:set>
<c:set var="lams"><lams:LAMSURL /></c:set>

<c:set var="title"><fmt:message key="label.monitoring.heading.marking" /></c:set>
<lams:PageMonitor title="${title}" hideHeader="true">
	<lams:JSImport src="includes/javascript/readmore.min.js" />
	<script type="text/javascript" src="<c:url value="/"/>/includes/javascript/marks.js"></script>
	<script type="text/javascript">
		//constants for marks.js
		var LABEL_DELETE = '<spring:escapeBody javaScriptEscape="true"><fmt:message key="message.monitor.confirm.original.learner.file.delete" /></spring:escapeBody>';
		var LABEL_RESTORE = '<spring:escapeBody javaScriptEscape="true"><fmt:message key="message.monitor.confirm.original.learner.file.restore" /></spring:escapeBody>';
		var MONITOR_URL = "<c:url value="/monitoring"/>";

		function updateMark(detailId,reportId,sessionId,userId){
			location.href="<lams:WebAppURL/>mark/newMark.do?updateMode=listAllMarks&userID="+userId+"&toolSessionID="+sessionId+"&detailID="+detailId+"&reportID="+reportId;
		}
	</script>

	<div class="container-main">
		<h1 class="fs-3 mb-4">
			${title}
		</h1>

		<c:if test="${isMarksReleased}">
			<lams:Alert5 type="success" id="marks-released">
				<fmt:message key="label.marks.released" />
			</lams:Alert5>
		</c:if>
	
		<c:forEach var="user" items="${reports}">
			<div class="lcard">
				<c:forEach items="${user.value}" var="fileInfo" varStatus="status">
					<%@include file="fileinfo.jsp"%>
					
					<c:if test="${not status.last}">
						<hr width="100%">
					</c:if>
				</c:forEach>
			</div>
		</c:forEach>
	
		<div class="activity-bottom-buttons">
			<button type="button" onclick="window.close()" class="btn btn-primary float-end">
				<i class="fa-regular fa-circle-xmark me-1"></i>
				<fmt:message key="label.monitoring.done.button" />
			</button>
		</div>
	</div>
</lams:PageMonitor>
