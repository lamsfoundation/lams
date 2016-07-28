<!DOCTYPE html>

<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-lams" prefix="lams"%>

<lams:html>
<lams:head>
	
	<%-- css --%>
	<link type="text/css" href="<lams:LAMSURL/>css/jquery-ui-smoothness-theme.css" rel="stylesheet">
	<link type="text/css" rel="stylesheet" href="<lams:WebAppURL />css/presence.css" />

	<%-- javascript --%>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript">
		var LAMS_URL = "<lams:LAMSURL/>",
			APP_URL = "<lams:WebAppURL />",
			lessonId = "${param.lessonID}",
			<%-- Learner interface uses attribues, Monitor uses parameters --%>
			presenceEnabled = ${param.presenceEnabledPatch eq 'true' or presenceEnabledPatch},
			presenceShown = ${param.presenceShown eq 'true' or presenceShown},
			presenceImEnabled = ${param.presenceImEnabled eq 'true' or presenceImEnabled},
			nickname = '<lams:user property="firstName"/> <lams:user property="lastName"/>',
			// labels used in JS file
			labelSend = '<fmt:message key="learner.im.send"/>',
			labelUsers = '<fmt:message key="learner.im.users"/>',
			groupChatInfo = {
							 nick : '<fmt:message key="learner.im.group.chat"/>',
							 tag  : "groupchat"
							};
	</script>
	<script type="text/javascript" src="<lams:WebAppURL />includes/javascript/presence.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			resizeChat();
		});
	</script>
	
</lams:head>

<body>
	<%-- initial html / presence.js adds on html into here --%>
	<div id="presenceChat" class="startHidden">
		<%-- only pop the message box if im is enabled --%>
		<c:if test="${param.presenceImEnabled or presenceImEnabled}">
			<div id="presenceChatTabs">
				<ul>
			        <li>
			        	<a href="#groupchat"><fmt:message key="learner.im.group.chat"/></a>
			       </li>
				</ul>
				<div id="groupchat">
			    	<div id="groupchat_messageArea" class="messageArea"></div>
			    	<div id="groupchat_sendArea" class="sendArea">
						<input id="groupchat_messageInput" onkeydown="javascript:handleMessageInput(event, groupChatInfo.nick)" type="text" class="messageInput" />
						<input type="button" value="<fmt:message key="learner.im.send"/>" class="sendButton"
						       onclick="javascript:sendMessage(groupChatInfo.nick)" />
					</div>
	   			</div>
			</div>
		</c:if>
		
		<%-- always pop the roster --%>
		<div id="presenceChatRoster" class="ui-corner-all">
			<div id="presenceUserCount" onclick="javascript:handlePresenceClick()"></div>
			<div id="presenceUserListings"></div>
		</div>
	</div>
</body>

</lams:html>