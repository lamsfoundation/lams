<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<lams:html>
<lams:head>
	<title><fmt:message key="activity.title" /></title>
	<lams:css />
	<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/jquery.js"></script>
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

		<h4>
			<fmt:message key="label.waiting.for.leader" />
		</h4>

		<div>
			<fmt:message key="label.users.from.group" />
		</div>

		<div id="usersInGroup">
			<c:forEach var="user" items="${groupUsers}">
				<div class="voffset10 loffset10">
					<lams:Portrait userId="${user.queUsrId}"/>
					<span>
						<c:out value="${user.fullname}" escapeXml="true" />
					</span>
				</div>
			</c:forEach>
		</div>

		<div class="right-buttons pull-right">
			<button onclick="refresh();" class="btn btn-default voffset5">
				<fmt:message key="label.refresh" />
			</button>
		</div>


		<div id="footer">
	</lams:Page>

</body>
</lams:html>
