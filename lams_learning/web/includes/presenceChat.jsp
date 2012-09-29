			<%-- css --%>
			<link type="text/css" href="<lams:LAMSURL/>css/jquery-ui-smoothness-theme.css" rel="stylesheet">
			<link type="text/css" rel="stylesheet" href="/lams/learning/includes/jquery-contextmenu/jquery.contextMenu.css" />
			<link type="text/css" rel="stylesheet" href="/lams/learning/includes/presence.css" />
			
			<%-- javascript --%>
			<script type="text/javascript" src="/lams/learning/includes/presence.js"></script>
			<script type="text/javascript" src="/lams/learning/includes/jsjac-1.3.1/jsjac.js"></script>
			<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui.js"></script>
			<script type="text/javascript" src="/lams/learning/includes/jquery-contextmenu/jquery.contextMenu.js"></script>
			
			<script type="text/javascript">
				// populated from jsp
				var lamsUrl = "<lams:LAMSURL/>";
				var xmppServlet = lamsUrl + "Presence.do";
				var presenceEnabled = "${param.presenceEnabledPatch}";
				var presenceImEnabled = "${param.presenceImEnabled}";
				var presenceUrl = "${requestScope.presenceUrl}";
				var userId = "<lams:user property="userID"/>";
				var nickname = "<lams:user property="firstName"/>" + " " + "<lams:user property="lastName"/>";
				
				var HTTPBASE = lamsUrl + "JHB";
				var roomName = "${param.lessonID}" + "_" + "${param.createDateTime}";
				
				// labels
				var presenceLabel = "<fmt:message key='label.presence'/>";
				
				// on startup
				$(document).ready(function (){
					// if browser is ie6
					if($.browser.msie && parseInt($.browser.version) == 6){
						// make warningvisible
						$("#presenceChatWarning").removeClass("startHidden");
					}
					// otherwise enable presence chat
					else{
						// if presence im is enabled
						<c:if test="${param.presenceEnabledPatch}">
							// make visible
							$("#presenceChat").removeClass("startHidden");

							$("div#presenceChatRoster ul").click(function() {
								handlePresenceClick();
							});
								
							// create chat tabs
							$("#presenceChatTabs").tabs({ scrollable: true });
							
							// bind the select function to do extra stuff
							$("#presenceChatTabs").bind('tabsselect', function(event, ui) {
								// remove visual indicators of new message
								var tag = ui.panel.id;
								$("#" + tagToTabLabel(tag)).removeClass('presenceTabNewMessage');
								$("#" + tagToListing(tag)).removeClass('presenceListingNewMessage');
								
								// scroll to the clicked tab
								$("#presenceChatTabs").tabs('scrollTo', ui.tab.offsetLeft);
							});
							
							// bind the show function to do extra stuff
							$("#presenceChatTabs").bind('tabsshow', function(event, ui) {
								// get the tag
								var tag = ui.panel.id;
								
								// scroll to the bottom of the given message area
								var messageArea = $("#" + tagToMessageArea(tag));
								messageArea.attr("scrollTop", messageArea.attr("scrollHeight"));
							});

							setPresenceShown("${param.presenceShown}");
							
						</c:if>
						
						// create roster tab
						$("#presenceChatRoster").tabs({ scrollable: false });
	
						// correct room name				
						roomName = correctPresenceRoomName(roomName);
						
						// attempt to login once the window is loaded
						doLogin(presenceUrl, userId, userId, userId, roomName, nickname, false);
					}
				});
				
			</script>
			
			<%-- initial html / presence.js adds on html into here --%>
			<div id="presenceChat" class="startHidden">
				<%-- only pop the message box if im is enabled --%>
				<c:if test="${param.presenceImEnabled}">
				<div id="presenceChatTabs">
					<div id="tabWrapper">
						<div style="float: left; width: 5%; text-align: left;">
							<a href="javascript:handleLeftScrollClick();"><img id="leftSlider" src="../images/icons/arrow_left.png" width="16" height="16" border="0" /></a>
						</div>
						<div id="tabsHolder" style="float: left; width: 90%; overflow:hidden;">
							<ul id="wrappedTabs">
						        <li><a href="#groupchat">
						        	<span>
						        		<table border="0" cellpadding="5" cellspacing="0" class="tabLabelTable">
						        			<tr>
						        				<td width="10"><img src="../images/icons/group_edit.png" width="16" height="16" border="0"/></td>
						        				<td><div id="groupchat_tabLabel" class="ui-tabs-label">Group Chat</div></td>
						        			</tr>
						        		</table>
						        	</span>
						        </a></li>
							</ul>
						</div>
						<div style="float: left; width: 5%; text-align: right;">
					 		<a href="javascript:handleRightScrollClick();"><img id="rightSlider" src="../images/icons/arrow_right.png" width="16" height="16" border="0" /></a>
						</div>
					</div>
				    <div id="groupchat" class="chatPanel">
				    	<div id="groupchat_messageArea" class="messageArea">
				    	</div>
				    	<br>
				    	<div id="groupchat_sendArea" class="sendArea">
							<form name="groupchat_sendForm" id="groupchat_sendForm" method="get" onsubmit="return sendMsg(this)">
								<input id="messageInput" name="messageInput" type="text" class="messageInput">
								<input type="submit" name="sendButton" id="sendButton" value="Send" class="sendButton">
							</form>
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
				        				<td width="10"><img src="../images/icons/group.png" width="16" height="16" border="0"/></td>
				        				<td><div id="presence_tabLabel">Users</div></td>
				        			</tr>
				        		</table>
				        	</span>
				        </a></li>
				        <img id="minMaxIcon" src="../images/icons/bullet_arrow_top.png" width="16" height="16" border="0"/>
				    </ul>
				    <div id="presenceUserListings">
				    </div>
				</div>
			</div>
			
			<%-- div reserved for storing context menus --%>
			<div id="presenceContextMenus">		
			</div>
			
			<%-- floating div shown when IE6 is being used --%>
			<div id="presenceChatWarning" class="startHidden warning">
				Sorry, Internet Explorer 6 is not compatible with Presence Chat
			</div>