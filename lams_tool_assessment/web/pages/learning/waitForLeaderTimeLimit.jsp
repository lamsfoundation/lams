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
		
		<html:button property="refreshButton" onclick="refresh();" styleClass="btn btn-sm btn-primary pull-right">
			<fmt:message key="label.refresh" />
		</html:button>

	</lams:Page>

</body>
</lams:html>
