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
	
	//query for leader status (only in case there is notebook at the end of activity and leader hasn't answered it yet)
	var checkLeaderIntervalId = null;
	if (${!isUserLeader && isScratchingFinished && isWaitingForLeaderToSubmitNotebook && mode != "teacher"}) {
		checkLeaderIntervalId = setInterval("checkLeaderSubmittedNotebook();",2000);// ask for leader status every 20 seconds
	}
	
	//check Leader Submitted Notebook and if true show finishButton
	function checkLeaderSubmittedNotebook() {
        $.ajax({
            url: '<c:url value="/learning/checkLeaderSubmittedNotebook.do"/>',
            data: 'sessionMapID=${sessionMapID}',
            dataType: 'json',
            type: 'post',
            success: function (json) {
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
							<img src="<html:rewrite page='/includes/images/scratchie-correct.png'/>" class="scartchie-image">
						</c:when>
						<c:when test="${answer.scratched && !answer.correct}">
							<img src="<html:rewrite page='/includes/images/scratchie-wrong.png'/>" id="image-${item.uid}-${answer.uid}"
								class="scartchie-image">
						</c:when>
						<c:when test="${sessionMap.userFinished || item.unraveled || !isUserLeader || (mode == 'teacher')}">
							<img src="<html:rewrite page='/includes/images/answer-${status.index + 1}.png'/>" class="scartchie-image">
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
				<html:button property="finishButton" styleId="finishButton" onclick="return finish('showBurningQuestions')"
					styleClass="btn btn-sm btn-default">
					<fmt:message key="label.continue.burning.questions" />
				</html:button>
			</c:when>
			<c:when test="${isUserLeader && sessionMap.reflectOn}">
				<html:button property="finishButton" styleId="finishButton" onclick="return finish('newReflection')"
					styleClass="btn btn-default">
					<fmt:message key="label.continue" />
				</html:button>
			</c:when>
			<c:when
				test="${isUserLeader && (!sessionMap.isBurningQuestionsEnabled || !sessionMap.reflectOn) || !isUserLeader && isScratchingFinished}">
				<html:button property="finishButton" styleId="finishButton" onclick="return finish('showResults')"
					styleClass="btn btn-default">
					<fmt:message key="label.submit" />
				</html:button>
			</c:when>
		</c:choose>
	</div>
</c:if>