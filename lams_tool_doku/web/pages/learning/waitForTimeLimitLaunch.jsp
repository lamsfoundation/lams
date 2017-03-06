<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<lams:html>
<lams:head>
	<title><fmt:message key="label.learning.title" /></title>
	<%@ include file="/common/header.jsp"%>

	<script type="text/javascript">
		//function refresh() {
			//location.reload();
		//}

		//refresh page every 30 sec
		//setTimeout("refresh();", 30000);
		
		//init the connection with server using server URL but with different protocol
		var websocket = new WebSocket('<lams:WebAppURL />'.replace('http', 'ws') 
						+ 'learningWebsocket?toolContentID=' + ${toolContentID});
		
		// run when the server pushes new reports and vote statistics
		websocket.onmessage = function(e) {
			// create JSON object
			var input = JSON.parse(e.data);
			
			// Monitor has added one more minute to the time limit. All learners will need
     		// to add +1 minute to their countdown counters.
			if (input.pageRefresh) {
				location.reload();
				return;
			}
		};
	</script>
</lams:head>
<body class="stripes">

	<lams:Page type="learner" title="${dokumaran.title}">

		<lams:Alert id="waitingForLeader" type="info" close="false">
			<fmt:message key="label.waiting.for.teacher.launch.activity" />
		</lams:Alert>

		<div class="voffset10">
			<html:button property="refreshButton" onclick="refresh();" styleClass="btn btn-sm btn-default pull-right">
				<fmt:message key="label.refresh" />
			</html:button>
		</div>

		<div id="footer"></div>
	</lams:Page>
</body>
</lams:html>
