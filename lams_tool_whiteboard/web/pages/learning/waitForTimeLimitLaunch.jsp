<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="whiteboard" value="${sessionMap.whiteboard}" />

<lams:PageLearner title="${whiteboard.title}" toolSessionID="${sessionMap.toolSessionID}">
	<div id="container-main">
		<lams:Alert5 id="waitingForLeader" type="info" close="false">
			<fmt:message key="label.waiting.for.teacher.launch.activity" />
		</lams:Alert5>

		<div class="activity-bottom-buttons">
			<button type="button" name="refreshButton" onclick="javascript:location.reload();" class="btn btn-primary btn-icon-refresh">
				<fmt:message key="label.refresh" />
			</button>
		</div>
	</div>
</lams:PageLearner>
