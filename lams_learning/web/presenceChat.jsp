<%-- css --%>
<link type="text/css" href="<lams:LAMSURL/>css/jquery-ui-smoothness-theme.css" rel="stylesheet">
<link type="text/css" rel="stylesheet" href="<c:url value="/"/>css/presence.css" />

<%-- javascript --%>
<script type="text/javascript" src="<c:url value="/"/>includes/javascript/presence.js"></script>
<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui.js"></script>

<script type="text/javascript">
	var messagePollInterval = 2 * 1000; // poll every 2 seconds
		
	var lamsUrl = "<lams:LAMSURL/>";
	var actionUrl = lamsUrl + "PresenceChat.do";
	var lessonId = "${lessonID}";
	var presenceEnabled = ${presenceEnabledPatch eq 'true'};
	var presenceShown = ${presenceShown eq 'true'};
	var presenceImEnabled = ${presenceImEnabled eq 'true'};
	<c:set var="displayName">
		<lams:user property="firstName"/>" + " " + "<lams:user property="lastName"/>
	</c:set>
	var nickname = "<c:out value='${displayName}'/>";
	
	// labels used in JS file
	var labelSend = '<fmt:message key="learner.im.send"/>';
	var labelUsers = '<fmt:message key="learner.im.users"/>';
	var groupChatInfo = {nick : '<fmt:message key="learner.im.group.chat"/>',
						 tag  : "groupchat"
						};
	// on startup
	$(document).ready(function (){
		presenceChat = $("#presenceChat");
		rosterDiv = $("#presenceUserListings");
		
		// if browser is ie6
		if($.browser.msie && parseInt($.browser.version) == 6){
			// make warningvisible
			$("#presenceChatWarning").removeClass("startHidden");
		}
		// otherwise enable presence chat
		else {
			// if presence im is enabled
			if (presenceEnabled) {
				// make visible
				presenceChat.removeClass("startHidden");
				
				// create chat tabs
				presenceChatTabs = $("#presenceChatTabs").tabs({
					'scrollable'    : true,
					// set default class for new panel
					'panelTemplate' : '<div class="chatPanel"></div>',
					'activate' : function(event, ui) {
						// remove visual indicators of new message
						var nick =  getUserFromTabIndex(presenceChatTabs.tabs('option','active'));
						var tag = nickToTag(nick);
						$("#" + tagToTabLabel(tag)).removeClass('presenceTabNewMessage');
						
						if (nick != groupChatInfo.nick) {
							$("#" + tagToListing(tag)).removeClass('presenceListingNewMessage');
						}
						
						updateChat();
					}
				   });
			}
			
			// create roster tab
			$("#presenceChatRoster").tabs({ scrollable: false });
			
			// update chat now and every few seconds
			updateChat();
			setInterval(updateChat, messagePollInterval);
		}
	});
</script>

<%-- initial html / presence.js adds on html into here --%>
<div id="presenceChat" class="startHidden">
	<%-- only pop the message box if im is enabled --%>
	<c:if test="${presenceImEnabled}">
	<div id="presenceChatTabs">
		<div id="tabWrapper">
			<div id="leftSliderDiv">
				<a href="javascript:handleLeftScrollClick();"><img class="smallImage" src="<lams:LAMSURL/>images/icons/arrow_left.png" /></a>
			</div>
			<div id="tabsHolder">
				<ul id="wrappedTabs">
			        <li><a href="#groupchat">
			        	<span>
			        		<table border="0" cellpadding="5" cellspacing="0" class="tabLabelTable">
			        			<tr>
			        				<td width="10"><img class="smallImage" src="<lams:LAMSURL/>images/icons/group_edit.png"/></td>
			        				<td><div id="groupchat_tabLabel" class="ui-tabs-label"><fmt:message key="learner.im.group.chat"/></div></td>
			        			</tr>
			        		</table>
			        	</span>
			        </a></li>
				</ul>
				<div id="groupchat" class="chatPanel">
			    	<div id="groupchat_messageArea" class="messageArea">
			    	</div>
			    	<br />
			    	<div id="groupchat_sendArea" class="sendArea">
						<input id="groupchat_messageInput" onkeydown="javascript:handleMessageInput(event, groupChatInfo.nick)" type="text" class="messageInput" />
						<input type="button" value="<fmt:message key="learner.im.send"/>" class="sendButton"
						       onclick="javascript:sendMessage(groupChatInfo.nick)" />
				</div>
	    </div>
			</div>
			<div id="rightSliderDiv">
		 		<a href="javascript:handleRightScrollClick();"><img class="smallImage" src="<lams:LAMSURL/>images/icons/arrow_right.png" /></a>
			</div>
		</div>
	</div>
	</c:if>
	
	<%-- always pop the roster --%>
	<div id="presenceChatRoster">
		<ul>
	        <li><a href="#presenceUserListings" class="userListings" onclick="javascript:handlePresenceClick()">
		        <span>
	        		<table border="0" cellpadding="5" cellspacing="0" class="tabLabelTable">
	        			<tr>
	        				<td width="10"><img class="smallImage" src="<lams:LAMSURL/>images/icons/group.png"/></td>
	        				<td><div id="presence_tabLabel"><fmt:message key="learner.im.users"/></div></td>
	        			</tr>
	        		</table>
	        	</span>
	        </a></li>
	    </ul>
	    <div id="presenceUserListings">
	    </div>
	</div>
</div>

<%-- floating div shown when IE6 is being used --%>
<div id="presenceChatWarning" class="startHidden warning">
	<fmt:message key="learner.im.ie6.warning"/>
</div>