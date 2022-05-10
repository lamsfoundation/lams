<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<lams:html>
<lams:head>
	<title><fmt:message key="label.learning.title" /></title>
	<%@ include file="/common/header.jsp"%>

	<script type="text/javascript">
		function refresh() {
			location.reload();
		}

		//refresh page every 30 sec
		setTimeout("refresh();", 30000);
	</script>
</lams:head>
<body class="stripes">

	<lams:Page type="learner" title="${dokumaran.title}">

		<lams:Alert id="waitingForLeader" type="info" close="false">
			<fmt:message key="label.waiting.for.leader" />
		</lams:Alert>

		<div class="voffset5">
			<fmt:message key="label.users.from.group" />
		</div>
		
		<div id="usersInGroup">
			<c:forEach var="user" items="${groupUsers}">
				<div class="voffset10 loffset10">
					<lams:Portrait userId="${user.userId}"/>
					<span>
						<c:out value="${user.firstName} ${user.lastName}" escapeXml="true" />
					</span>
				</div>
			</c:forEach>
		</div>

		<div class="voffset10">
			<button name="refreshButton" onclick="refresh();" class="btn btn-sm btn-default pull-right">
				<fmt:message key="label.refresh" />
			</button>
		</div>

		<div id="footer"></div>
	</lams:Page>
</body>
</lams:html>
