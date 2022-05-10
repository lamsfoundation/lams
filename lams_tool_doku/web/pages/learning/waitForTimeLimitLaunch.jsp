<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="dokumaran" value="${sessionMap.dokumaran}" />

<lams:html>
<lams:head>
	<title><fmt:message key="label.learning.title" /></title>
	<%@ include file="/common/header.jsp"%>

	<%@ include file="websocket.jsp"%>
</lams:head>
<body class="stripes">

	<lams:Page type="learner" title="${dokumaran.title}">

		<lams:Alert id="waitingForLeader" type="info" close="false">
			<fmt:message key="label.waiting.for.teacher.launch.activity" />
		</lams:Alert>

		<div class="voffset10">
			<button name="refreshButton" onclick="javascript:location.reload();" class="btn btn-sm btn-default pull-right">
				<fmt:message key="label.refresh" />
			</button>
		</div>

		<div id="footer"></div>
	</lams:Page>
</body>
</lams:html>
