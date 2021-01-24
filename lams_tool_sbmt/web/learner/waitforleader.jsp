<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

<lams:html>
<lams:head>
	<title><fmt:message key="activity.title" /></title>

	<lams:css />
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>

	<script type="text/javascript">
		function refresh() {
			location.reload(true);
		}
		
		//refresh page every 30 sec
		setTimeout("refresh();",30000);
    </script>
</lams:head>
<body class="stripes">

	<lams:Page type="learner" title="${sessionMap.title}">
	
		<div class="panel">
			<c:out value="${sessionMap.instruction}" escapeXml="false" />
		</div>

		<h4>
			<fmt:message key="${waitingMessageKey}" />
		</h4>
		
		<c:if test="${not empty groupUsers}">
		
			<div class="voffset5">
				<fmt:message key="label.users.from.group" />
			</div>
		
			<div id="usersInGroup">
				<c:forEach var="user" items="${groupUsers}">
					<div class="voffset10 loffset10">
						<lams:Portrait userId="${user.userID}"/>
						<span>
							<c:out value="${user.firstName} ${user.lastName}" escapeXml="true" />
						</span>
					</div>
				</c:forEach>
			</div>
		</c:if>
		
		<button name="refreshButton" onclick="refresh();" class="btn btn-sm btn-primary pull-right">
			<fmt:message key="label.refresh" />
		</button>

	</lams:Page>

</body>
</lams:html>