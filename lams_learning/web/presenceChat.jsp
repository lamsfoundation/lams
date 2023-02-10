<!DOCTYPE html>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-lams" prefix="lams"%>

<lams:html>
<lams:head>
	
	<%-- css --%>
	<link type="text/css" rel="stylesheet" href="<lams:LAMSURL/>css/jquery-ui-bootstrap-theme.css">
	<link type="text/css" rel="stylesheet" href="<lams:WebAppURL />css/presence.css" />

	<%-- javascript --%>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui.js"></script>

	<%-- if the page has tabs, we need to reload bootstrap or the tabs won't work --%>
	<c:if test="${param.reloadBootstrap}">
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap.min.js"></script>		
	</c:if>
		
	<script type="text/javascript">
		var LAMS_URL = "<lams:LAMSURL/>",
			APP_URL = "<lams:WebAppURL />",
			lessonId = '<c:out value="${param.lessonID}" />',
			<%-- Learner interface uses attribues, Monitor uses parameters --%>
			presenceEnabled = ${param.presenceEnabledPatch eq 'true' or presenceEnabledPatch},
			presenceShown = ${param.presenceShown eq 'true' or presenceShown},
			nickname = '<lams:user property="firstName"/>' + ' ' + '<lams:user property="lastName"/>',
			// labels used in JS file
			labelSend = '<fmt:message key="learner.im.send"/>',
			labelUsers = '<fmt:message key="learner.im.users"/>',
			groupChatInfo = {
							 nick : '<fmt:message key="learner.im.group.chat"/>',
							 tag  : "groupchat"
							};
	</script>
	<lams:JSImport src="includes/javascript/presence.js" relative="true" />
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
						<input type="button" value="<fmt:message key="learner.im.send"/>" class="sendButton btn btn-default btn-xs"
						       onclick="javascript:sendMessage(groupChatInfo.nick)" />
					</div>
	   			</div>
			</div>
		</c:if>
		
		<%-- always pop the roster --%>
		<div id="presenceChatRoster" class="ui-corner-all">
			<div id="presenceUserCount" class="ui-corner-all" onclick="javascript:handlePresenceClick()"></div>
			<div id="presenceUserListings"></div>
		</div>
	</div>
</body>

</lams:html>