<%@ include file="/common/taglibs.jsp"%>

<!--  JsJaC Library -->
<script type="text/javascript"
	src="${tool}includes/javascript/jsjac/sha1.js"></script>
<script type="text/javascript"
	src="${tool}includes/javascript/jsjac/xmlextras.js"></script>
<script type="text/javascript"
	src="${tool}includes/javascript/jsjac/JSJaCConnection.js"></script>
<script type="text/javascript"
	src="${tool}includes/javascript/jsjac/JSJaCPacket.js"></script>
<script type="text/javascript"
	src="${tool}includes/javascript/jsjac/JSJaCHttpPollingConnection.js"></script>
<script type="text/javascript"
	src="${tool}includes/javascript/jsjac/JSJaCHttpBindingConnection.js"></script>
<!--  <script language="JavaScript" type="text/javascript" src="Debugger.js"></script> -->

<!--  Chat Config -->
<script type="text/javascript">
	var HTTPBASE = "${tool}JHB/";
	var XMPPDOMAIN = "${XMPPDOMAIN}";
	var USERNAME = "${chatUserDTO.userID}";
	var PASSWORD = "${chatUserDTO.userID}";
	var CONFERENCEROOM = "${CONFERENCEROOM}";
	var NICK = "${chatUserDTO.jabberNickname}";
	var RESOURCE = "lams_chatclient";
	var MODE = "${MODE}";
	var USER_UID = "${chatUserDTO.uid}";	
	var LEARNER_FINISHED = "${chatUserDTO.finishedActivity}";
	var LOCK_ON_FINISHED = "${chatDTO.lockOnFinish}";
	var REFLECT_ON_ACTIVITY = "${chatDTO.reflectOnActivity}";	
</script>

<!--  Chat Client -->
<script type="text/javascript"
	src="${tool}includes/javascript/learning.js"></script>

<div id="content">
	<h1>
		<c:out value="${chatDTO.title}" escapeXml="false" />
	</h1>

	<div id="chat_content">
		<div id="chat_pane">
			<form name="sendForm" action="" onSubmit="return sendMsg(this);">
				<div>
					<p>
						<c:out value="${chatDTO.instructions}" escapeXml="false" />
					</p>

					<div id="roster"></div>
					<div id="iResp">
						<fmt:message>message.loading</fmt:message>
					</div>

					<br />

					<h4 style="margin-left: 12px;">
						<fmt:message>label.sendMessageTo</fmt:message>
						<span id="sendToEveryone"><fmt:message>label.everyone</fmt:message>
						</span><span id="sendToUser" style="display: none"></span>
					</h4>


					<div style="margin-left: 12px;">
						<textarea name="msg" onKeyPress="return checkEnter(event);"
							id="msgArea" rows="2" cols="20"></textarea>

						<input id="sendButton" class="button" type="submit"
							value='<fmt:message>button.send</fmt:message>' />
					</div>
				</div>
			</form>

			<br />
			<c:if test="${MODE == 'learner' || MODE == 'author'}">
				<%@ include file="parts/finishButton.jsp"%>

			</c:if>
		</div>
	</div>
</div>

