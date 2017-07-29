<%@ include file="/common/taglibs.jsp"%>

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

		//time limit is expired but leader hasn't submitted required notebook/burning questions yet. Non-leaders
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
		
		$.each(input, function(itemUid, answers) {
			$.each(answers, function(answerUid, isCorrect){
				// only updates come via websockets
				scratchImage(itemUid, answerUid, isCorrect);
			});
		});
	};
	</script>
</c:if>

<c:forEach var="item" items="${sessionMap.itemList}" varStatus="status">
	<div class="lead">
		<c:out value="${item.title}" escapeXml="true" />
	</div>
	<div class="panel-body-sm">
		<c:out value="${item.description}" escapeXml="false" />
	</div>

	<table id="scratches" class="table table-hover">
		<c:forEach var="answer" items="${item.answers}" varStatus="status">
			<tr id="tr${answer.uid}">
				<td style="width: 40px;"><c:choose>
						<c:when test="${answer.scratched && answer.correct}">
							<img src="<html:rewrite page='/includes/images/scratchie-correct.png'/>" class="scartchie-image"
								 id="image-${item.uid}-${answer.uid}">
						</c:when>
						<c:when test="${answer.scratched && !answer.correct}">
							<img src="<html:rewrite page='/includes/images/scratchie-wrong.png'/>" class="scartchie-image"
								 id="image-${item.uid}-${answer.uid}">
						</c:when>
						<c:when test="${sessionMap.userFinished || item.unraveled || !isUserLeader || (mode == 'teacher')}">
							<img src="<html:rewrite page='/includes/images/answer-${status.index + 1}.png'/>" class="scartchie-image"
								 id="image-${item.uid}-${answer.uid}">
						</c:when>
						<c:otherwise>
							<a href="#nogo" onclick="scratchItem(${item.uid}, ${answer.uid}); return false;"
								id="imageLink-${item.uid}-${answer.uid}"> <img
								src="<html:rewrite page='/includes/images/answer-${status.index + 1}.png'/>" class="scartchie-image"
								id="image-${item.uid}-${answer.uid}" />
							</a>
						</c:otherwise>
					</c:choose> <c:if test="${(mode == 'teacher') && (answer.attemptOrder != -1)}">
						<div style="text-align: center; margin-top: 2px;">
							<fmt:message key="label.choice.number">
								<fmt:param>${answer.attemptOrder}</fmt:param>
							</fmt:message>
						</div>
					</c:if></td>

				<td style="vertical-align: middle;"><c:out value="${answer.description}" escapeXml="false" /></td>
			</tr>
		</c:forEach>
	</table>

</c:forEach>

<%-- show reflection (only for teacher) --%>
<c:if test="${sessionMap.userFinished and sessionMap.reflectOn and (mode == 'teacher')}">
	<div class="voffset5">
		<div class="panel panel-default">
			<div class="panel-heading-sm panel-title">
				<fmt:message key="monitor.summary.td.notebookInstructions" />
			</div>
			<div class="panel-body-sm">
				<div class="panel">
					<lams:out value="${sessionMap.reflectInstructions}" escapeHtml="true" />
				</div>
				<c:choose>
					<c:when test="${empty sessionMap.reflectEntry}">
							<fmt:message key="message.no.reflection.available" />
					</c:when>
					<c:otherwise>
						<p>
							<lams:out escapeHtml="true" value="${sessionMap.reflectEntry}" />
						</p>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
	</div>
</c:if>

<c:if test="${mode != 'teacher'}">
	<div class="voffset10 pull-right">
		<c:choose>
			<c:when test="${isUserLeader && sessionMap.isBurningQuestionsEnabled}">
				<input type="hidden" name="method" id="method" value="showBurningQuestions">
				<html:button property="finishButton" styleId="finishButton" onclick="return finish(false);"
					styleClass="btn btn-sm btn-default">
					<fmt:message key="label.continue.burning.questions" />
				</html:button>
			</c:when>
			<c:when test="${isUserLeader && sessionMap.reflectOn}">
				<input type="hidden" name="method" id="method" value="newReflection">
				<html:button property="finishButton" styleId="finishButton" onclick="return finish(false);"
					styleClass="btn btn-default">
					<fmt:message key="label.continue" />
				</html:button>
			</c:when>
			<c:otherwise>
				<input type="hidden" name="method" id="method" value="showResults">
				<html:button property="finishButton" styleId="finishButton" onclick="return finish(false);"
					styleClass="btn btn-default">
					<fmt:message key="label.submit" />
				</html:button>
			</c:otherwise>
		</c:choose>
	</div>
</c:if>