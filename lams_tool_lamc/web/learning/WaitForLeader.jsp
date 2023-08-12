<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<lams:html>
<lams:head>
	<title><fmt:message key="activity.title" /></title>
	<lams:css />

	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script type="text/javascript">
		function refresh() {
			location.reload();
		}

		//refresh page every 30 sec
		setTimeout("refresh();", 30000);
	</script>
</lams:head>
<body class="stripes">

	<lams:Page type="learner" title="${content.title}">
		<h4 class="text-left">
			<fmt:message key="label.waiting.for.leader" />
		</h4>

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

		<button name="refreshButton" onclick="refresh();" class="btn btn-primary float-end">
			<fmt:message key="label.refresh" />
		</button>
	</lams:Page>
</body>
</lams:html>
