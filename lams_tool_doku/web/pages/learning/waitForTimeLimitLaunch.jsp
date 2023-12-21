<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="dokumaran" value="${sessionMap.dokumaran}" />

<lams:PageLearner title="${dokumaran.title}" toolSessionID="${sessionMap.toolSessionID}">
	<div id="container-main">

		<lams:Alert5 id="waiting-for-leader" type="info" close="false">
			<fmt:message key="label.waiting.for.teacher.launch.activity" />
		</lams:Alert5>

		<div class="activity-bottom-buttons">
			<button type="button" name="refreshButton" onclick="javascript:location.reload();" class="btn btn-primary btn-icon-refresh">
				<fmt:message key="label.refresh" />
			</button>
		</div>

	</div>
</lams:PageLearner>