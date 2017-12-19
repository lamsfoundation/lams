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
		
		$.each(input, function(itemUid, answers) {
			$.each(answers, function(answerUid, isCorrect){
				// only updates come via websockets
				scratchImage(itemUid, answerUid, isCorrect);
			});
		});
	};
	</script>
</c:if>

<form id="burning-questions" name="burning-questions" method="post" action="">

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
				<td style="width: 40px;">
					<c:choose>
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
					</c:choose> 
					
					<c:if test="${(mode == 'teacher') && (answer.attemptOrder != -1)}">
						<div style="text-align: center; margin-top: 2px;">
							<fmt:message key="label.choice.number">
								<fmt:param>${answer.attemptOrder}</fmt:param>
							</fmt:message>
						</div>
					</c:if>
				</td>

				<td 
					<c:if test="${fn:length(answer.confidenceLevelDtos) > 0}">class="answer-with-confidence-level-portrait"</c:if>
				>
					<c:out value="${answer.description}" escapeXml="false" />
					
					<c:if test="${scratchie.confidenceLevelsActivityUiid != null}">
						<div id="user-confidence-levels">
							<c:forEach var="confidenceLevelDto" items="${answer.confidenceLevelDtos}" varStatus="status">
							
								<div class="c100 p${confidenceLevelDto.level}0 small">
									<span>
										<c:choose>
										<c:when test="${confidenceLevelDto.portraitUuid == null}">
											<div class="portrait-generic-sm portrait-color-${confidenceLevelDto.userId % 7}"></div>
										</c:when>
										<c:otherwise>
			    								<img class="portrait-sm portrait-round" src="${lams}download/?uuid=${confidenceLevelDto.portraitUuid}&preferDownload=false&version=4" alt="">
										</c:otherwise>
										</c:choose>
									</span>
									<div class="slice">
										<div class="bar"></div>
										<div class="fill"></div>
									</div>
									<div class="confidence-level-percentage">
										${confidenceLevelDto.level}0%
									</div>
								</div>
	    
							</c:forEach>
						</div>
					</c:if>
				</td>
			</tr>
		</c:forEach>
	</table>
	
	<%-- show burning questions --%>
	<c:if test="${isUserLeader && scratchie.burningQuestionsEnabled || (mode == 'teacher')}">
		<div class="form-group burning-question-container">
			<a data-toggle="collapse" data-target="#burning-question-item${item.uid}">
				<i class="fa fa-xs fa-plus-square-o roffset5" aria-hidden="true"></i>
				<fmt:message key="label.burning.question" />
			</a>
			
			<div id="burning-question-item${item.uid}" class="collapse <c:if test="${not empty item.burningQuestion}">in</c:if>">
				<textarea rows="5" name="burningQuestion${item.uid}" class="form-control"
					<c:if test="${mode == 'teacher'}">disabled="disabled"</c:if>
				>${item.burningQuestion}</textarea>
			</div>
		</div>
	</c:if>

</c:forEach>

<%-- show general burning question --%>
<c:if test="${isUserLeader && scratchie.burningQuestionsEnabled || (mode == 'teacher')}">
	<div class="form-group burning-question-container">
		<a data-toggle="collapse" data-target="#burning-question-general">
			<i class="fa fa-xs fa-plus-square-o roffset5" aria-hidden="true"></i>
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