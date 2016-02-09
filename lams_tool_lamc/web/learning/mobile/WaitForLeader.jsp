<!DOCTYPE html>

<%@ include file="/common/taglibs.jsp"%>

<lams:html>
<lams:head>
	<title><fmt:message key="activity.title" /></title>
	<%@ include file="/common/mobileheader.jsp"%>
	
	<script type="text/javascript">
		function refresh() {
			location.reload();
		}
		
		//refresh page every 30 sec
		setTimeout("refresh();",30000);
    </script>
</lams:head>
<body>
<div data-role="page" data-cache="false">

	<div data-role="header" data-theme="b" data-nobackbtn="true">
		<h1>
			<c:out value="${content.title}" escapeXml="true"/>
		</h1>
	</div><!-- /header -->

	<div data-role="content">
		
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

	</div>
	
	<div data-role="footer" data-theme="b" class="ui-bar">
		<span class="ui-finishbtn-right">
			<button name="refreshButton" onclick="javascript:refresh();" data-icon="refresh" data-theme="b">
				<fmt:message key="label.refresh" />
			</button>
		</span>
	</div>

</div>
</body>
</lams:html>
