<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>

<lams:html>
<lams:head>
	<html:base />
	<title><fmt:message key="activity.title" /></title>

	<lams:css />

	<script type="text/javascript">
		function refresh() {
			location.reload();
		}
		
		//refresh page every 30 sec
		setTimeout("refresh();",30000);
    </script>
</lams:head>
<body class="stripes">

	<div id="content">
		<h1>
			<c:out value="${content.title}" escapeXml="true"/>
		</h1>
		
		<h2>
			<fmt:message key="label.waiting.for.leader" />
		</h2>

		<div>
			<fmt:message key="label.users.from.group" />
		</div>
		
		<div>
			<c:forEach var="user" items="${groupUsers}" varStatus="status">
				<div>
					<c:out value="${user.fullname}" escapeXml="true"/>
				</div>
			</c:forEach>
		</div>
		
		<div class="space-bottom-top align-right">
			<html:button property="refreshButton" onclick="refresh();" styleClass="button">
				<fmt:message key="label.refresh" />
			</html:button>
		</div>

	</div>

	<div id="footer">
	</div>

</body>
</lams:html>
