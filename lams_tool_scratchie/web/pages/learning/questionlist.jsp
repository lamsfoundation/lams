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

<!-- isUserLeader=${sessionMap.isUserLeader} -->

<script type="text/javascript">

	//stop refreshing non-leaders in case scratching is finished
	if (${isScratchingFinished} && (refreshIntervalId != null)) {
		clearInterval(refreshIntervalId);
	}
	
	//hide finish button if isWaitingForLeaderToSubmitNotebook
	$(document).ready(function(){
		if (${!isUserLeader && isWaitingForLeaderToSubmitNotebook}) {
			$("#finishButton").hide();
		}
	});
	
	
	// if time limit is ON and time is over (i.e. isScratchingFinished is true) - refresh page for non-leaders in order to stop showing them learning.jsp
	if (${!isUserLeader && isScratchingFinished && (scratchie.timeLimit > 0) && isTimeLimitExceeded && mode != "teacher"}) {
		alert("isTimeLimitExceeded${isTimeLimitExceeded}");
		location.reload();
	}
		
	// in case there is notebook at the end of activity and leader hasn't answered it yet - query for leader's status
	var checkLeaderIntervalId = null;
	if (${!isUserLeader && isScratchingFinished && isWaitingForLeaderToSubmitNotebook && mode != "teacher"}) {
		checkLeaderIntervalId = setInterval("checkLeaderSubmittedNotebook();", 2000);// ask for leader status every 20 seconds
	}	
	//check Leader Submitted Notebook and if true show finishButton
	function checkLeaderSubmittedNotebook() {
        $.ajax({
            url: '<c:url value="/learning/checkLeaderSubmittedNotebook.do"/>',
            data: 'sessionMapID=${sessionMapID}',
            dataType: 'json',
            type: 'post',
            success: function (json) {
				if (json.isTimeLimitExceeded) {
					alert("isTimeLimitExceededx${isTimeLimitExceeded}");
					location.reload();
				}
                
            	if (!json.isWaitingForLeaderToSubmitNotebook) {
            		if (checkLeaderIntervalId != null) {
            			clearInterval(checkLeaderIntervalId);
            		}
					
					$("#finishButton").show();
            	}
            }
       	});
	}
</script>

<c:forEach var="item" items="${sessionMap.itemList}" varStatus="status">
	<h3><c:out value="${item.title}" escapeXml="true"/></h3>
	<div><c:out value="${item.description}" escapeXml="false"/></div>
	
	<table id="scratches" class="alternative-color">
		<c:forEach var="answer" items="${item.answers}" varStatus="status">
			<tr id="tr${answer.uid}">
				<td style="width: 40px;">
					<c:choose>
						<c:when test="${answer.scratched && answer.correct}">
							<img src="<html:rewrite page='/includes/images/scratchie-correct.png'/>" class="scartchie-image">
						</c:when>
						<c:when test="${answer.scratched && !answer.correct}">
							<img src="<html:rewrite page='/includes/images/scratchie-wrong.png'/>" id="image-${item.uid}-${answer.uid}" class="scartchie-image">
						</c:when>
						<c:when test="${sessionMap.userFinished || item.unraveled || !isUserLeader || (mode == 'teacher')}">
							<img src="<html:rewrite page='/includes/images/answer-${status.index + 1}.png'/>" class="scartchie-image">
						</c:when>
						<c:otherwise>
							<a href="#nogo" onclick="javascript:scratchItem(${item.uid}, ${answer.uid}); return false;" id="imageLink-${item.uid}-${answer.uid}">
								<img src="<html:rewrite page='/includes/images/answer-${status.index + 1}.png'/>" class="scartchie-image" id="image-${item.uid}-${answer.uid}" />
							</a>
						</c:otherwise>
					</c:choose>
							
					<c:if test="${(mode == 'teacher') && (answer.attemptOrder != -1)}">
						<div style="text-align: center; margin-top: 2px;">
							<fmt:message key="label.choice.number" >
								<fmt:param>${answer.attemptOrder}</fmt:param>
							</fmt:message>
						</div>
					</c:if>
				</td>
						
				<td style="vertical-align: middle;">
					${answer.description} 
				</td>
			</tr>
		</c:forEach>
	</table>
					
</c:forEach>

<%-- show reflection (only for teacher) --%>
<c:if test="${sessionMap.userFinished and sessionMap.reflectOn and (mode == 'teacher')}">
	<div class="small-space-top">
		<h3><fmt:message key="monitor.summary.td.notebookInstructions"/></h3>
		<p>
			<strong><lams:out value="${sessionMap.reflectInstructions}" escapeHtml="true"/></strong>
		</p>

		<c:choose>
			<c:when test="${empty sessionMap.reflectEntry}">
				<p>
					<em> <fmt:message key="message.no.reflection.available" />
					</em>
				</p>
			</c:when>
			<c:otherwise>
				<p>
					<lams:out escapeHtml="true" value="${sessionMap.reflectEntry}" />
				</p>
			</c:otherwise>
		</c:choose>
	</div>
</c:if>

<c:if test="${mode != 'teacher'}">
	<div class="space-bottom-top align-right">
		<c:choose>
			<c:when test="${isUserLeader && sessionMap.isBurningQuestionsEnabled}">
				<input type="hidden" name="method" id="method" value="showBurningQuestions">
				<html:button property="finishButton" styleId="finishButton" onclick="return finish(false);" styleClass="button">
					<fmt:message key="label.continue.burning.questions" />
				</html:button>
			</c:when>
			<c:when test="${isUserLeader && sessionMap.reflectOn}">
				<input type="hidden" name="method" id="method" value="newReflection">
				<html:button property="finishButton" styleId="finishButton" onclick="return finish(false);" styleClass="button">
					<fmt:message key="label.continue" />
				</html:button>
			</c:when>
			<c:when test="${isUserLeader && (!sessionMap.isBurningQuestionsEnabled || !sessionMap.reflectOn) || !isUserLeader && isScratchingFinished}">
				<input type="hidden" name="method" id="method" value="showResults">
				<html:button property="finishButton" styleId="finishButton" onclick="return finish(false);" styleClass="button">
					<fmt:message key="label.submit" />
				</html:button>
			</c:when>
		</c:choose>
	</div>
</c:if>