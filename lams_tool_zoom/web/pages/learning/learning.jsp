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
	<script type="text/javascript" src="${lams}includes/javascript/common.js"></script>
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
				<a href="#" onclick="window.location.reload()" class="btn btn-default"><fmt:message key="label.refresh" /></a>
			</c:when>
			<c:otherwise>
				<h4><fmt:message key="label.meeting.password" />&nbsp;${meetingPassword}</h4>
			
				<iframe id="zoomJoinFrame" style="width: 100%; height: 680px; border: none; display: none" src="${meetingURL}"></iframe>
				<a id="zoomJoinButton" href="${meetingURL}" target="_blank" style="display: none" class="btn btn-default">
					<fmt:message key="button.enter" />
				</a>
				
				<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/getSysInfo.js"></script>
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