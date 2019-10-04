<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams">
	<lams:LAMSURL />
</c:set>
<%-- param has higher level for request attribute --%>
<c:if test="${not empty param.sessionMapID}">
	<c:set var="sessionMapID" value="${param.sessionMapID}" />
</c:if>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="mode" value="${sessionMap.mode}" />
<c:set var="toolSessionID" value="${sessionMap.toolSessionID}" />
<c:set var="scratchie" value="${sessionMap.scratchie}" />
<c:set var="isUserLeader" value="${sessionMap.isUserLeader}" />
<c:set var="isScratchingFinished" value="${sessionMap.isScratchingFinished}" />
<c:set var="isWaitingForLeaderToSubmitNotebook" value="${sessionMap.isWaitingForLeaderToSubmitNotebook}" />
<c:set var="hideFinishButton" value="${!isUserLeader && (!isScratchingFinished || isWaitingForLeaderToSubmitNotebook)}" />

<!-- Used by TestHarness 
	 isUserLeader=${isUserLeader}
	 hideFinishButton=${hideFinishButton} -->

<c:if test="${not isUserLeader}">
	<script type="text/javascript">
	$(document).ready(function(){
		// hide Finish button for non-leaders until leader finishes
		if (${hideFinishButton}) {
			$("#finishButton").hide();
		}
	});
	
	//init the connection with server using server URL but with different protocol
	var scratchieWebsocketInitTime = Date.now(),
		scratchieWebsocket = new WebSocket('<lams:WebAppURL />'.replace('http', 'ws') 
					+ 'learningWebsocket?toolSessionID=' + ${toolSessionID}),
		scratchieWebsocketPingTimeout = null,
		scratchieWebsocketPingFunc = null;
	
	scratchieWebsocket.onclose = function(e) {
		// react only on abnormal close
		if (e.code === 1006 &&
			Date.now() - scratchieWebsocketInitTime > 1000) {
			location.reload();		
		}
	};
	
	scratchieWebsocketPingFunc = function(skipPing){
		if (scratchieWebsocket.readyState == scratchieWebsocket.CLOSING 
				|| scratchieWebsocket.readyState == scratchieWebsocket.CLOSED){
			return;
		}
		
		// check and ping every 3 minutes
		scratchieWebsocketPingTimeout = setTimeout(scratchieWebsocketPingFunc, 3*60*1000);
		// initial set up does not send ping
		if (!skipPing) {
			scratchieWebsocket.send("ping");
		}
	};
	
	// set up timer for the first time
	scratchieWebsocketPingFunc(true);
	
	// run when the server pushes new reports and vote statistics
	scratchieWebsocket.onmessage = function(e) {
		// create JSON object
		var input = JSON.parse(e.data);

		//time limit is expired but leader hasn't submitted required notebook yet. Non-leaders
        //will need to refresh the page in order to stop showing them questions page.
		if (input.pageRefresh) {
			location.reload();
			return;
		}
		
		// reset ping timer
		clearTimeout(scratchieWebsocketPingTimeout);
		scratchieWebsocketPingFunc(true);
		
		// leader finished the activity
		if (input.close) {
			$('#finishButton').show();
			return;
		}

		// only updates come via websockets
		$.each(input, function(itemUid, options) {
			$.each(options, function(optionUid, isCorrect){
				
				var isVsaItem = !Number.isInteger(optionUid);
				if (isVsaItem) {
					var answer = optionUid;
					optionUid = hashCode(optionUid);

					//check if such image exists, create it otherwise
					if ($('#image-' + itemUid + '-' + optionUid).length == 0) {
						paintNewVsaAnswer(eval(itemUid), answer);
					}
				}
				
				scratchImage(itemUid, optionUid, isCorrect);
			});
		});
	};
	</script>
</c:if>

<form id="burning-questions" name="burning-questions" method="post" action="">

<%@ include file="scratchies.jsp"%>

<%-- show general burning question --%>
<c:if test="${isUserLeader && scratchie.burningQuestionsEnabled || (mode == 'teacher')}">
	<div class="form-group burning-question-container">
		<a data-toggle="collapse" data-target="#burning-question-general" href="#bqg"
				<c:if test="${empty sessionMap.generalBurningQuestion}">class="collapsed"</c:if>>
			<span class="if-collapsed"><i class="fa fa-xs fa-plus-square-o roffset5" aria-hidden="true"></i></span>
  			<span class="if-not-collapsed"><i class="fa fa-xs fa-minus-square-o roffset5" aria-hidden="true"></i></span>
			<fmt:message key="label.general.burning.question" />
		</a>

		<div id="burning-question-general" class="collapse <c:if test="${not empty sessionMap.generalBurningQuestion}">in</c:if>">
			<textarea rows="5" name="generalBurningQuestion" class="form-control"
				<c:if test="${mode == 'teacher'}">disabled="disabled"</c:if>
			>${sessionMap.generalBurningQuestion}</textarea>
		</div>
	</div>
</c:if>

</form>

<c:if test="${mode != 'teacher'}">
	<div class="voffset10 pull-right">
		<c:choose>
			<c:when test="${isUserLeader && sessionMap.reflectOn}">
				<input type="hidden" name="method" id="method" value="newReflection">
				<button nae="finishButton" id="finishButton" onclick="return finish(false);"
					class="btn btn-default">
					<fmt:message key="label.continue" />
				</button>
			</c:when>
			<c:otherwise>
				<input type="hidden" name="method" id="method" value="showResults">
				<button name="finishButton" id="finishButton" onclick="return finish(false);"
					class="btn btn-default">
					<fmt:message key="label.submit" />
				</button>
			</c:otherwise>
		</c:choose>
	</div>
</c:if>