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
</script>

<!--  Chat Client -->
<script src="${tool}chat_client/chat_app.js" type="text/javascript"></script>

<div id="login_pane">
	<div id="login_err"></div>
	<p>
		<fmt:message>message.loading</fmt:message>
	</p>
</div>

<div id="chat_pane" style="display:none;">
	<form name="sendForm" action="" onSubmit="return sendMsg(this);">
		<div id="presence">
			<select id="roster_user_selector" size="2" onchange="updateSendDisplay();">
				<%--  options dynamically updated as users join/leave --%>
				<option>
					-
				</option>
			</select>
		</div>
		<div id="iResp"></div>

		<h3>
			<fmt:message>label.sendMessageTo</fmt:message>
			<span id="sendToEveryone"><fmt:message>label.everyone</fmt:message></span><span id="sendToUser" style="display: none"></span>
		</h3>
		
		<div id="msgAreaDiv">
			<textarea name="msg" id='msgArea' rows="3" cols="80"></textarea>
			<input type="submit" value='<fmt:message>button.send</fmt:message>' />
			<input type="button" onclick="resetInputs();" value='<fmt:message>button.clear</fmt:message>' />
		</div>
	</form>
</div>
