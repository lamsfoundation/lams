<%@ include file="/common/taglibs.jsp"%>

<!--  JsJaC Library -->
<script type="text/javascript" src="${tool}includes/javascript/jsjac/sha1.js"></script>
<script type="text/javascript" src="${tool}includes/javascript/jsjac/xmlextras.js"></script>
<script type="text/javascript" src="${tool}includes/javascript/jsjac/JSJaCConnection.js"></script>
<script type="text/javascript" src="${tool}includes/javascript/jsjac/JSJaCPacket.js"></script>
<script type="text/javascript" src="${tool}includes/javascript/jsjac/JSJaCHttpPollingConnection.js"></script>
<script type="text/javascript" src="${tool}includes/javascript/jsjac/JSJaCHttpBindingConnection.js"></script>
<!--  <script language="JavaScript" type="text/javascript" src="Debugger.js"></script> -->

<!--  Chat Config -->
<script type="text/javascript">
	var HTTPBASE = "${tool}JHB/";
	var XMPPDOMAIN = "${XMPPDOMAIN}";
	var USERNAME = "${USERNAME}";
	var PASSWORD = "${PASSWORD}";
	var CONFERENCEROOM = "${CONFERENCEROOM}";
	var NICK = "${NICK}";
	var RESOURCE = "lams_chatclient";
	var MODE = "${MODE}";
	var USER_UID = "${USER_UID}";	
	var SESSION_ID = "${SESSION_ID}";
	var LEARNER_FINISHED = "${LEARNER_FINISHED}";
	var LOCK_ON_FINISHED = "${LOCK_ON_FINISHED}";	
</script>

<!--  Chat Client -->
<script src="${tool}chat_client/chat_app.js" type="text/javascript"></script>

<h1 class="no-tabs-below">
	<c:out value="${chatTitle}" escapeXml="false" />
</h1>
<div id="header-no-tabs-learner"></div>
<div id="content-learner">
	<div id="chat_content">

		<div id="login_pane">
			<div id="loading_message">
				<p>
					<fmt:message>message.loading</fmt:message>
				</p>
			</div>
			<div id="login_err"></div>
		</div>

		<div id="chat_pane" style="display:none;">
			<form name="sendForm" action="" onSubmit="return sendMsg(this);">
				<p>
					<c:out value="${chatInstructions}" escapeXml="false" />
				</p>

				<div id="presence">
					<select id="roster_user_selector" size="2" onchange="updateSendDisplay();">
						<%--  options dynamically updated as users join/leave --%>
						<option>
							loading
						</option>
					</select>
				</div>
				<div id="iResp"></div>


				<h2>
					<fmt:message>label.sendMessageTo</fmt:message>
					<span id="sendToEveryone"><fmt:message>label.everyone</fmt:message></span><span id="sendToUser" style="display: none"></span>
				</h2>

				<textarea name="msg" id="msgArea" rows="3" cols="80"></textarea>

				<div class="left-buttons">
					<input id="sendButton" class="button" type="submit" value='<fmt:message>button.send</fmt:message>' />
					<input id="clearButton" class="button" type="button" onclick="resetInputs();" value='<fmt:message>button.clear</fmt:message>' />
				</div>
			</form>
		</div>

		<div id="finishButton_pane" class="space-bottom" style="display:none;">
			<form name="finishActivity" action="${tool}learning.do" method="post">
				<div class="right-buttons">
					<input type="hidden" name="dispatch" value="finishActivity" />
					<input type="hidden" name="chatUserUID" value="${USER_UID}" />
					<input type="hidden" name="toolSessionID" value="${SESSION_ID}" />
					<input type="submit" class="button" value='<fmt:message>button.finish</fmt:message>' />
				</div>
			</form>
		</div>
	</div>
</div>
<div id="footer-learner"></div>


