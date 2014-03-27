<%@ include file="/common/taglibs.jsp"%>

<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
<script type="text/javascript" src="includes/javascript/learning.js"></script>	
<script type="text/javascript">
	var MODE = "${MODE}";
	var TOOL_SESSION_ID = '${param.toolSessionID}';
	var LEARNING_ACTION = "<c:url value='/learning.do'/>";
	var MESSAGE_POLL_INTERVAL = 2 * 1000; // poll every 2 seconds
	
	$(document).ready(function() {
		messageDiv = $("#messages");
		rosterDiv = $("#roster");
		sendToUserSpan = $('#sendToUser');
		sendToEveryoneSpan = $('#sendToEveryone');
		sendMessageArea = $('#sendMessageArea');
		sendMessageButton = $('#sendMessageButton');
		
		updateChat();
		setInterval(updateChat, MESSAGE_POLL_INTERVAL);

		// react to Enter key
		sendMessageArea.keydown(function(e) {
			if (e.which == 13) {
				e.preventDefault();
				sendMessage();
			}
		});
	});
</script>

<div id="content">
	<h1>
		<c:out value="${chatDTO.title}" escapeXml="true" />
	</h1>

	<p>
		<c:out value="${chatDTO.instructions}" escapeXml="false" />
	</p>

   <c:if test="${MODE == 'learner' || MODE == 'author'}">
	<c:if test="${chatDTO.lockOnFinish}"> 
		<div class="info">
			<c:choose>
				<c:when test="${chatUserDTO.finishedActivity}">
					<fmt:message key="message.activityLocked"/> 
				</c:when>
				<c:otherwise>
					<fmt:message key="message.warnLockOnFinish" />
				</c:otherwise>
			</c:choose>
		</div>
	</c:if>
	<c:if test="${not empty submissionDeadline}">
		<div class="info">
			<fmt:message key="authoring.info.teacher.set.restriction" >
				<fmt:param><lams:Date value="${submissionDeadline}" /></fmt:param>
			</fmt:message>	
		</div>
	</c:if>	
   </c:if>

	<table id="chatContent">
		<tr>
			<td style="width: 75%"><div id="messages"> </div></td>
			<td><div id="roster"> </div></td>
		</tr>
		
		<c:if test="${MODE == 'teacher'}">
			<tr>
				<td colspan="2" />
					<div class="field-name">
						<fmt:message key="label.sendMessageTo" />
						<span id="sendToEveryone"><fmt:message key="label.everyone" /></span>
						<span id="sendToUser" style="display: none"></span>
					</div>
				</td>
			</tr>
		</c:if>
		
		<c:if test="${MODE != 'learner' || !chatDTO.lockOnFinish || !chatUserDTO.finishedActivity}">
			<tr>
				<td><textarea id="sendMessageArea" rows="3"></textarea></td>
				<td id="sendMessageButtonCell"><input id="sendMessageButton" class="button" type="button"
						   onclick="javascript:sendMessage()"
						   value='<fmt:message key="button.send"/>' />
				</td>
			</tr>
		</c:if>
		
	</table>

	<c:if test="${MODE == 'learner' || MODE == 'author'}">
		<%@ include file="parts/finishButton.jsp"%>
	</c:if>
</div>
