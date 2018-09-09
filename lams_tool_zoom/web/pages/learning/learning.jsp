<%@ include file="/common/taglibs.jsp"%>

<lams:Page type="learner" title="${contentDTO.title}">
	<div class="panel">
		<c:out value="${contentDTO.instructions}" escapeXml="false" />
	</div>

	<logic:messagesPresent>
		<lams:Alert type="danger" id="form-error" close="false">
			<html:messages id="error">
				<c:out value="${error}" escapeXml="false" />
				<br />
			</html:messages>
		</lams:Alert>
	</logic:messagesPresent>
	
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
				<html:link styleClass="btn btn-sm btn-default" href="#" onclick="window.location.reload()">
					<fmt:message key="label.refresh" />
				</html:link>
			</c:when>
			<c:otherwise>
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
