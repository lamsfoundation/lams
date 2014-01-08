<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">

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
			${content.title}
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
					${user.fullname}
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