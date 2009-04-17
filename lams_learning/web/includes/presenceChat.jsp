			<%-- css --%>
			<link type="text/css" rel="stylesheet" href="<lams:LAMSURL/>includes/javascript/jquery-ui/themes/cupertino/ui.all.css" />
			<link type="text/css" rel="stylesheet" href="<lams:LAMSURL/>includes/javascript/jquery-ui/ui/plugins/jquery.contextMenu.css" />
			<link type="text/css" rel="stylesheet" href="/lams/learning/includes/presence.css" />
			
			<%-- javascript --%>
			<script type="text/javascript" src="/lams/learning/includes/presence.js"></script>
			<script type="text/javascript" src="/lams/learning/includes/jsjac-1.3.1/jsjac.js"></script>
			<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui/ui/ui.core.js"></script>
			<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui/ui/ui.tabs.scrollable.js"></script>
			<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui/ui/plugins/jquery.contextMenu.js"></script>
			
			<script type="text/javascript">
				// populated from jsp
				var lamsUrl = "<lams:LAMSURL/>";
				var xmppServlet = lamsUrl + "Presence.do";
				var presenceEnabled = "${param.presenceEnabled}";
				var presenceImEnabled = "${param.presenceImEnabled}";
				var presenceUrl = "${param.presenceUrl}";
				var userId = "<lams:user property="userID"/>";
				var nickname = "<lams:user property="firstName"/>" + " " + "<lams:user property="lastName"/>";
				
				var HTTPBASE = lamsUrl + "JHB";
				var roomName = "${param.lessonID}" + "${param.createDateTime}";
				
				// labels
				var presenceLabel = "<fmt:message key='label.presence'/>";
								
				$(document).ready(function (){
					// if presence im is enabled
					<c:if test="${param.presenceImEnabled}">
						// create chat tabs
						$("#presenceChatTabs").tabs({ scrollable: true });

						// bind the select function which resets label to non-bold when clicked
						$("#presenceChatTabs").bind('tabsselect', function(event, ui) {
							var nick = getUserFromTag(ui.panel.id).nick;
							$('#' + ui.panel.id + '_tabLabel').html(nick);
							$("#presenceChatTabs").tabs('scrollTo', ui.tab.offsetLeft);
						});
					</c:if>
					
					// create roster tab
					$("#presenceChatRoster").tabs({ scrollable: false });

					// correct room name				
					roomName = correctPresenceRoomName(roomName);
					
					// attempt to login once the window is loaded
					doLogin(presenceUrl, userId, userId, userId, roomName, nickname, false);
				});
				
			</script>
			
			<%-- initial html / presence.js adds on html into here --%>
			<div id="presenceChat">
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
								<table border="0" align="center" cellpadding="3" cellspacing="0" style="width: 100%">
								  <tr>
								    <td width="85%" align="center" valign="middle"><input id="messageInput" name="messageInput" type="text" style="width: 100%;"></td>
								    <td width="15%" align="center" valign="middle"><input type="submit" name="sendButton" id="sendButton" value="Send"></td>
								  </tr>
								</table>
							</form>
						</div>
				    </div>
				</div>
				</c:if>
				<div id="presenceChatRoster">
					<ul onclick="javascript:handlePresenceClick()">
				        <li><a href="#presenceUserListings" onclick="javascript:handlePresenceClick()">
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
			<div id="presenceContextMenus">		
			</div>