<%@ include file="/common/taglibs.jsp"%>

<c:if test="${empty meetingURL}">
	<script type="text/javascript">
		// refresh in 20 seconds
		window.setTimeout(function() {
			window.location.reload();
		}, 20*1000);
	</script>
</c:if>

<lams:Page type="learner" title="${contentDTO.title}">
	<div class="panel">
		<c:out value="${contentDTO.instructions}" escapeXml="false" />
	</div>

	<div class="buttons">
		<c:choose>
			<c:when test="${empty meetingURL}">
				<p>
					<fmt:message key="label.learning.conferenceNotAvailable" />
				</p>
				<html:link styleClass="btn btn-sm btn-default" href="#" onclick="window.location.reload()">
					<fmt:message key="label.refresh" />
				</html:link>
			</c:when>
			<c:otherwise>
				<iframe id="zoomJoinFrame" style="width: 100%; height: 650px; border: none;" src="${meetingURL}"></iframe>
			</c:otherwise>
		</c:choose>
	</div>

	<hr class="msg-hr">

	<c:if test="${mode == 'learner' || mode == 'author'}">
		<%@ include file="parts/finishButton.jsp"%>
	</c:if>

</lams:Page>
