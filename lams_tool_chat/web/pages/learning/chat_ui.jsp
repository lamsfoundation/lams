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
<script src="${tool}pages/learning/chat_app.js" type="text/javascript"></script>

<h1 class="no-tabs-below">
	<c:out value="${chatDTO.title}" escapeXml="false" />
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

		<div>
			<div id="chat_pane" style="display:none;">
				<form name="sendForm" action="" onSubmit="return sendMsg(this);">
					<p>
						<c:out value="${chatDTO.instructions}" escapeXml="false" />
					</p>

					<div id="presence">
						<select id="roster_user_selector" size="2"
							onchange="updateSendDisplay();">
							<%--  options dynamically updated as users join/leave --%>
							<option>
								loading
							</option>
						</select>
					</div>
					<div id="iResp"></div>


					<h2>
						<fmt:message>label.sendMessageTo</fmt:message>
						<span id="sendToEveryone"><fmt:message>label.everyone</fmt:message>
						</span><span id="sendToUser" style="display: none"></span>
					</h2>
					
					
					<span> <textarea name="msg" onKeyPress="return checkEnter(event);"
							id="msgArea" rows="3" cols="80"></textarea> </span>
							
							
					<div class="left-buttons">
						<input id="sendButton" class="button" type="submit"
							value='<fmt:message>button.send</fmt:message>' />
						<input id="clearButton" class="button" type="button"
							onclick="resetInputs();"
							value='<fmt:message>button.clear</fmt:message>' />
					</div>
				</form>
			</div>

			<div id="finishButton_pane" class="space-bottom"
				style="display:none;">
				<c:set var="dispatch" value="finishActivity" />
				<c:set var="buttonLabel" value="button.finish" />
				<c:if test="${chatDTO.reflectOnActivity}">
					<c:set var="dispatch" value="openNotebook" />
					<c:set var="buttonLabel" value="button.reflect" />
				</c:if>

				<html:form action="/learning" method="post">
					<div class="right-buttons">
						<html:hidden property="dispatch" value="${dispatch}" />
						<html:hidden property="chatUserUID" value="${chatUserDTO.uid}" />
						<html:submit styleClass="button space-bottom">
							<fmt:message>${buttonLabel}</fmt:message>
						</html:submit>
					</div>
				</html:form>
			</div>
		</div>

		<div style="height: 65px"></div>

		<div id="notebookEntry_pane" style="width: 100%; display: none;">
			<h4>
				${chatDTO.reflectInstructions}
			</h4>
			<p>
				${chatUserDTO.notebookEntry}
			</p>
		</div>
	</div>
</div>
<div id="footer-learner"></div>
