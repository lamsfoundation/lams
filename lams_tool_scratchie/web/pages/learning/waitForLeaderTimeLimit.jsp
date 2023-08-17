<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

<lams:PageLearner title="${sessionMap.title}" toolSessionID="${sessionMap.toolSessionID}">
	<script type="text/javascript">
		function refresh() {
			location.reload();
		}

		//refresh page every 30 sec
		setTimeout("refresh();", 30000);
	</script>

	<div class="container-lg">
		<lams:Alert5 id="waitingForLeader" type="info" close="false">
			<fmt:message key="${waitingMessageKey}" />
		</lams:Alert5>

		<div class="activity-bottom-buttons">
			<button id="refreshButton" name="refreshButton" onclick="refresh();" class="btn btn-primary">
				<fmt:message key="label.refresh" />
			</button>
		</div>
	</div>
</lams:PageLearner>

