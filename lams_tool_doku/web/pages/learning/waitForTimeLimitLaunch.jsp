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
		var dokuWebsocketInitTime = Date.now(),
			dokuWebsocket = new WebSocket('<lams:WebAppURL />'.replace('http', 'ws') 
						+ 'learningWebsocket?toolContentID=' + ${sessionMap.toolContentID}),
			dokuWebsocketPingTimeout = null,
			dokuWebsocketPingFunc = null;
		
		dokuWebsocket.onclose = function(){
			// react only on abnormal close
			if (e.code === 1006 &&
				Date.now() - dokuWebsocketInitTime > 1000) {
				location.reload();
			}
		};
		
		dokuWebsocketPingFunc = function(skipPing){
			if (dokuWebsocket.readyState == dokuWebsocket.CLOSING 
					|| dokuWebsocket.readyState == dokuWebsocket.CLOSED){
				return;
			}
			
			// check and ping every 3 minutes
			dokuWebsocketPingTimeout = setTimeout(dokuWebsocketPingFunc, 3*60*1000);
			// initial set up does not send ping
			if (!skipPing) {
				dokuWebsocket.send("ping");
			}
		};
		
		// set up timer for the first time
		dokuWebsocketPingFunc(true);
		
		// run when the server pushes new reports and vote statistics
		dokuWebsocket.onmessage = function(e) {
			// create JSON object
			var input = JSON.parse(e.data);
			
			// Monitor has added one more minute to the time limit. All learners will need
     		// to add +1 minute to their countdown counters.
			if (input.pageRefresh) {
				location.reload();
				return;
			}
			
			// reset ping timer
			clearTimeout(dokuWebsocketPingTimeout);
			dokuWebsocketPingFunc(true);
		};		
	</script>
</lams:head>
<body class="stripes">

	<lams:Page type="learner" title="${dokumaran.title}">

		<lams:Alert id="waitingForLeader" type="info" close="false">
			<fmt:message key="label.waiting.for.teacher.launch.activity" />
		</lams:Alert>

		<div class="voffset10">
			<button name="refreshButton" onclick="refresh();" class="btn btn-sm btn-default pull-right">
				<fmt:message key="label.refresh" />
			</button>
		</div>

		<div id="footer"></div>
	</lams:Page>
</body>
</lams:html>
