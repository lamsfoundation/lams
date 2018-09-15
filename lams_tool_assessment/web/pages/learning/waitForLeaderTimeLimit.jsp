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
		setTimeout("refresh();",30000);
    </script>
</lams:head>
<body class="stripes">

	<lams:Page type="learner" title="${content.title}">
		
		<h4>
			<fmt:message key="${waitingMessageKey}" />
		</h4>
		
		<button type="button" name="refreshButton" onclick="refresh();" class="btn btn-sm btn-primary pull-right">
			<fmt:message key="label.refresh" />
		</button>

	</lams:Page>

</body>
</lams:html>
