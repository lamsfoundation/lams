<!DOCTYPE html>

<%@ include file="/common/taglibs.jsp"%>

<lams:html>
<c:set var="lams">
	<lams:LAMSURL />
</c:set>
<c:set var="tool">
	<lams:WebAppURL />
</c:set>

<lams:head>  
	<title>
		<fmt:message key="activity.title" />
	</title>
	
	<lams:css/>
	
	<%-- TODO is this the best place to import these scripts ?	--%>
	<lams:JSImport src="includes/javascript/common.js" />
	<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/bootstrap.min.js"></script>
</lams:head>
<body class="stripes">
<lams:Page type="learner" title="${contentDTO.title}">
	<div class="panel">
		<c:out value="${contentDTO.instructions}" escapeXml="false" />
	</div>

	<lams:errors/>
	
	<c:if test="${not skipContent}">
		<c:choose>
			<c:when test="${empty meetingURL}">
				<script type="text/javascript">
					// refresh in 20 seconds
					window.setTimeout(function() {
						window.location.reload();
					}, 20*1000);
				</script>
				
				<p>
					<fmt:message key="label.learning.conferenceNotAvailable" />
				</p>
				<a href="#" onclick="window.location.reload()" class="btn btn-secondary"><fmt:message key="label.refresh" /></a>
			</c:when>
			<c:otherwise>
				<c:if test="${not empty meetingPassword}">
					<h4><fmt:message key="label.meeting.password" />&nbsp;<code>${meetingPassword}</code></h4>
				</c:if>
				
				<iframe id="zoomJoinFrame" style="width: 100%; height: 680px; border: none; display: none" src="${meetingURL}"></iframe>
				<a id="zoomJoinButton" href="${meetingURL}" target="_blank" style="display: none" class="btn btn-secondary">
					<fmt:message key="button.enter" />
				</a>

				<lams:JSImport src="includes/javascript/getSysInfo.js" />
				<script type="text/javascript">
					if (isMac) {
						$('#zoomJoinButton').show();
					} else {
						$('#zoomJoinFrame').show();
					}
				</script>
			</c:otherwise>
		</c:choose>
	</c:if>

	<hr class="msg-hr">

	<c:if test="${mode == 'learner' || mode == 'author'}">
		<%@ include file="parts/finishButton.jsp"%>
	</c:if>

</lams:Page>
</body>
</lams:html>