/******** Connection variables ******* */
var CONFERENCEROOM = "";
var XMPPDOMAIN = "";
var USERNAME = "";
var PASSWORD = "";
var NICK = "";
var RESOURCE = "";
	
/******** Other variables ******* */
var groupChatInfo = {nick:"Group Chat", tag:"groupchat", blocked:false};
var tabLabelsLocal = null;
var windowHeight;
var presenceShown = false;
var showStatus = false;

/* ******* HTML writer functions ******* */
function createPrivateTabLabel(label, tag){
	return '<table border="0" cellpadding="5" cellspacing="0" class="tabLabelTable">' +
				'<tr>' +
					'<td><img src="../images/icons/user_edit.png" width="16" height="16" border="0"/></td>' +
					'<td><div id="' + tag + '_tabLabel" class="ui-tabs-label">' + label + '</div></td>' +
					'<td><div><img onclick="javascript:handleCloseTabClick(\'' + label + '\')" src="../images/icons/cross.png" width="16" height="16" border="0"/></div></td>' +
				'</tr>' +
			'</table>';
}

function createPrivateTabContent(label, tag){
	return '<div id="' + tag + '_messageArea" class="messageArea"></div><br>' +
			'<div id="' + tag + '_sendArea" class="sendArea">' + 
				'<form name="' + tag + '_sendForm" id="' + tag + '_sendForm" method="get" onsubmit="return sendMsg(this)">' +
				'<table border="0" align="center" cellpadding="3" cellspacing="0" style="width: 100%">' +
					'<input id="sendTo" name="sentTo" type="hidden" value="' + label + '">' +
					'<input id="messageInput" name="messageInput" type="text" class="messageInput">' +
					'<input id="sendButton" name="sendButton" type="submit" value="Send" class="sendButton">' +
				'</table>' +
				'</form>' +
			'</div>';
}

function createPresenceListing(nick, tag){
	return '<table border="0" cellpadding="5" cellspacing="0">' +
				'<tr>' +
					'<td width="10"><img id="'+ tag + '_listingImage" src="../images/icons/user_offline.png" width="16" height="16" /></td>' +
					'<td><div id="' + tag + '_listingNick">' + nick + '</div></td>' +
				'</tr>' +
			'</table>';
}

function createContextMenu(tag){
	return '<ul id="' + tag + '_contextMenu" class="contextMenu">' +
				'<li id="allowContextOption" class="allow"><a href="#allow">Allow</a>' +
				'</li><li id="blockContextOption" class="block"><a href="#block">Block</a></li>' +
			'</ul>';
}

/* ******* Helper Functions ******* */
// resizes chat window
function resizeChat(){
	// refresh the window height
	windowHeight = $(window).height() - 30;
 		
	// if presence is shown
	if(presenceShown){
		// set presence chat to maximized height
		$("#presenceChat").css({'top': windowHeight - 270 + "px"});
	}
	// otherwise
	else{
		// set presence chat to minimized height
  		$("#presenceChat").css({'top': windowHeight + "px"});
	}
	
	$("#presenceChatWarning").css({'top': windowHeight - 10 + "px"});
}
 		
function getUserFromTag(tag){
	if(tag == groupChatInfo.tag){
		return groupChatInfo;
	}
	else{
		for(var i = 0; i < roster.users.length; i++){
			if(roster.users[i].tag == tag){
				return roster.users[i];
			}
		}
	}
	
	return null;
}

function getUserFromLabel(label){
   if(label == groupChatInfo.nick){
		return groupChatInfo;
	}
	else{
		for(var i = 0; i < roster.users.length; i++){
			if(roster.users[i].nick == label){
				return roster.users[i];
			}
		}
	}
	
	return null;
}
 		
function getUserFromTabIndex(tabIndex){
 	tabLabelsLocal = $(".ui-tabs-label");
	return getUserFromLabel(tabLabelsLocal[tabIndex].innerHTML);
}

function addTab(nick, tag){
	// add a tab with the the nick specified
	$("#presenceChatTabs").tabs('add' , '#' + tag, createPrivateTabLabel(nick, tag));
	// correct the holding div
	$("#" + tag).attr("style", "width: 95%; height: 80%;");
	// add the content
	$("#" + tag).html(createPrivateTabContent(nick, tag));
	
	var anticache = new Date().getTime();
	$.post(lamsUrl + "PresenceChatLogger.do", {method: "getConversationHistory", roomName:CONFERENCEROOM, from:NICK, to:nick, anticache:anticache}, handleGetConversation);
}

function htmlEnc(str) {
	if (!str) {
		return null;
	}
	str = str.replace(/&/g, "&amp;");
	str = str.replace(/</g, "&lt;");
	str = str.replace(/>/g, "&gt;");
	str = str.replace(/\"/g, "&quot;");
	str = str.replace(/\n/g, "<br>");
	return str;
}


function correctPresenceRoomName(s){
	s = s.replace(/ /g, "_");
	s = s.replace(/:/g, "_");
	
	return s;
}

function nickToTag(nick){
	return nick.replace(/ /g, "_");
}

function nickToMessageArea(nick){
	return tagToMessageArea(nickToTag(nick));
}

function tagToMessageArea(tag){
	return tag + '_messageArea';
}

function nickToListing(nick){
	return tagToListing(nickToTag(nick));
}

function tagToListing(tag){
	return tag + '_listing';
}

function nickToListingImage(nick){
	return tagToListingImage(nickToTag(nick));
}

function tagToListingImage(tag){
	return tag + '_listingImage';
}

function nickToContentMenu(nick){
	return tagToContextMenu(nickToTag(nick));
}

function tagToContextMenu(tag){
	return tag + '_contextMenu';
}

function nickToTabLabel(nick){
	return tagToTabLabel(nickToTag(nick));
}

function tagToTabLabel(tag){
	return tag + '_tabLabel';
}

function parseXml(xml) {  
	if (jQuery.browser.msie) {  
		var xmlDoc = new ActiveXObject("Microsoft.XMLDOM");  
		xmlDoc.loadXML(xml);  
		xml = xmlDoc;  
	}  
	
	return xml;  
}  
   
function getRosterUserByNick(nick) {
	for (var i = 0; i < this.users.length; i++) {
		if (this.users[i].nick == nick) {
			return this.users[i];
		}
	}
	return null;
}

function getRosterIndexByNick(nick) {
	for (var i = 0; i < this.users.length; i++) {
		if (this.users[i].nick == nick) {
			return this.users[i];
		}
	}
	return null;
}

function sortFunction(a, b){
	if(a.status < b.status){
		return -1;
	}
	else if(a.status > b.status){
		return 1;
	}
	else{
		if (a.nick < b.nick){
			return -1;
		}
		if (a.nick > b.nick){
			return 1;
		}
	}
	
	return 0;
}

function getTime(){
	var currentTime = new Date();
	var hours = currentTime.getHours();
	var minutes = currentTime.getMinutes();
	var seconds = currentTime.getSeconds();
	
	if (hours < 10){
		hours = "0" + hours;
	}
	
	if (minutes < 10){
		minutes = "0" + minutes;
	}
	
	if (seconds < 10){
		seconds = "0" + seconds;
	}
	
	return "(" + hours + ":" + minutes + ":" + seconds + ")";
}

function trimTime(time){
	return "(" + time.substring(11, 19) + ")";
}

function refreshContextMenu(tag, blocked){
	var contextMenu = $('#' + tag + '_contextMenu');

	if(blocked){
		contextMenu.enableContextMenuItems('#allow');
		contextMenu.disableContextMenuItems('#block');
	}
	else{
		contextMenu.enableContextMenuItems('#block');
		contextMenu.disableContextMenuItems('#allow');
	}
}

function handleAllowBlockClick(tag, blocked){
	refreshContextMenu(tag, !blocked);
	return !blocked;
}

function connectUserContextMenu(div, contextMenu){
	var myDiv = $("#" + div);
	myDiv.contextMenu({
		menu: contextMenu
	},
		function(action, el, pos) {
			// get roster index from clicked div
			var rosterIndex = $(el).attr('rosterIndex');
			
			// get info
			var user = roster.users[rosterIndex];
			var tag = user.tag;
			var nick = user.nick;
			var blocked = user.blocked;
			
			if(action == "allow" && blocked){
				roster.users[rosterIndex].blocked = handleAllowBlockClick(tag, blocked);
			}
			else if(action == "block" && !blocked){
				roster.users[rosterIndex].blocked = handleAllowBlockClick(tag, blocked);
			}
			
			roster.updateDisplay();
	});
}

/* ******* Click handlers ******* */
function handleLeftScrollClick(){
	$("#presenceChatTabs").tabs('scrollLeft');
}	

function handleRightScrollClick(){
	$("#presenceChatTabs").tabs('scrollRight');
}

function handlePresenceLeftClick(divName){
	var div = $("#" + divName);
	var rosterIndex = div.attr("rosterIndex");
	var user = roster.users[rosterIndex];
	var tag = user.tag;
	var nick = user.nick;
				
	// if the clicked user is one's self
	if(nick == NICK){
		// do nothing
	}
	// if the clicked user's tab is not already open, create it and select it
	else if(document.getElementById(tag) == null){
		// add a tab
		addTab(nick, tag);
	}
	// if the clicked user's tab is open
	else{
		// make the sender's tab label unbold
		$('#' + tag + '_tabLabel').html(nick);
	}
	
	// select the added tab
	$("#presenceChatTabs").tabs('select' , tag);
}

function handleCloseTabClick(label){
	tabLabelsLocal = $(".ui-tabs-label");
	for(var i = 0; i < tabLabelsLocal.length; i++){
		if(tabLabelsLocal[i].innerHTML == label){
			$("#presenceChatTabs").tabs('remove' , i);
		}
	}
}

function handlePresenceClick() {
	if(presenceShown){
		$("#presenceChat").animate({top: windowHeight + "px"}, 1000 );
		$("#minMaxIcon").attr("src", "../images/icons/bullet_arrow_top.png")
		presenceShown = false;
	}
	else{
		$("#presenceChat").animate({top: windowHeight - 270 + "px"}, 1000 );
		$("#minMaxIcon").attr("src", "../images/icons/bullet_arrow_bottom.png");
		presenceShown = true;
	}
}

/* ******* Roster ******* */
function RosterUser(nick) {
	this.nick = nick;
	this.tag = nickToTag(nick);
	this.status = "unavailable";
	this.blocked = false;
	this.staff = false;
}

function AddRosterUser(user) {
	this.users[this.users.length] = user;
}

function UpdateRosterUser(user) {
	// TODO do we need this.
}

function RemoveRosterUser() {
}

function GetAllRosterUsers() {
	return this.users;
}

function UpdateRosterDisplay() {
	// sort users by name
	this.users.sort(sortFunction);
	
	// reset the count
	var availableCount = 0;
	
	// get rosterDiv
	var rosterDiv = $("#presenceUserListings");

	// if presence im is enabled
	if(presenceImEnabled == "true"){
		// get rosterDiv
		var contextMenus = $("#presenceContextMenus");
	}
	
	// for all users
	for (var i = 0; i < this.users.length; i++) {
		// get available
		var available = this.users[i].status;
		// get nick
		var nick = this.users[i].nick;
		// get tag
		var tag = this.users[i].tag;
		// get blocked
		var blocked = this.users[i].blocked;
		
		// get listing name
		var listingName = tagToListing(tag);
		
		// get the actual listing
		var listing = $("#" + listingName);
							
		// if no listing exists
		if(listing.length == 0){
			// create listing div
			// note: attrId must be added to array before onClick
			var listingDiv = $('<div id="' + listingName + '" rosterIndex="' + i + '" onClick="handlePresenceLeftClick(\'' + listingName + '\');" class="presenceListing">' + createPresenceListing(nick, tag) + '</div>');
			
			// add the listing div
			rosterDiv.append(listingDiv);
			
			// if presence im is enabled
			if(presenceImEnabled == "true"){
				// if not oneself
				if(nick != NICK){
					// add the context menu
					contextMenus.html(contextMenus.html() + createContextMenu(tag));
				}
			}
		}
		else{
			// remove and append at the right place (from sort)
			rosterDiv.append(listing.remove());
			
			// refresh roster index
			listing.attr("rosterIndex", i);
		}
		
		// get context menu div name
		var contextMenuName = tagToContextMenu(tag);
			
		/*
		connect the context menu to the last added div
		when listings are moved events are scrapped... must reconnect them each time
		*/
		connectUserContextMenu(listingName, contextMenuName);
					
		// refresh the context menu
		refreshContextMenu(tag, blocked);
			
		// get the listingImage
		var listingImage = $("#" + tagToListingImage(tag));
		
		// set the correct icon depending on status
		if(available == "available"){
			availableCount++;
			
			if(blocked){
				listingImage.attr("src", "../images/icons/user_online_blocked.png");
			}else{
				listingImage.attr("src", "../images/icons/user_online.png");
			}
		}else{
			if(blocked){
				listingImage.attr("src", "../images/icons/user_offline_blocked.png");
			}else{
				listingImage.attr("src", "../images/icons/user_offline.png");
			}
		}
	}
	
	// update presenceTabLabel
	var presenceTabLabelDiv = $("#presence_tabLabel");
	presenceTabLabelDiv.html("Users (" + availableCount + ")");
}

function Roster() {
	this.users = [];
	this.currentIndex = null;
    // objects methods 
	this.getUserByNick = getRosterUserByNick;
	this.addUser = AddRosterUser;
	this.updateUser = UpdateRosterUser;
	this.removeUser = RemoveRosterUser;
	this.updateDisplay = UpdateRosterDisplay;
	this.getAllUsers = GetAllRosterUsers;
}

var roster = new Roster();

/* ******* Chat functions ******* */
function setFocusOnTextarea() {
	document.forms[0].msg.focus();
}

function generateMessageHTML(nick, message, date) {
	var completeElem;
	var fromElem;
	var msgElem;
	
	completeElem = $('<div></div>');
	
	if(!date){
		fromElem = $('<div class="presenceMessageFrom">' + getTime() + ' ' + nick + '</div>');
	}else{
		fromElem = $('<div class="presenceMessageFrom">' + trimTime(date) + ' ' + nick + '</div>');
	}
	
	msgElem = $('<div class="presenceMessage">' + message + '</div>');
	
	completeElem.append(fromElem);
	completeElem.append(msgElem);
	
	return completeElem;
}

function generateEmoteHTML(emote){
	return $('<div class="presenceMessageEmote">' + getTime() + ' ' + emote + '</div>');
}

function updateMessageDisplay(div, htmlMessage, prepend) {
	var messageArea = $("#" + div);
	
	if(prepend){
		messageArea.prepend(htmlMessage);
	}else{
		messageArea.append(htmlMessage);
	}

	messageArea.attr("scrollTop", messageArea.attr("scrollHeight"));
}

function sendMsg(aForm) {
	// if messageInput in form is empty
	if (aForm.messageInput.value === "") {
		// send nothing
		return false;
	}
	
	// creaet a new message object
	var aMsg = new JSJaCMessage();
	
	// prepare nick
	var toNick;
	
	// prepare user
	var user;
	
	// if the form has a hidden sendTo input, it's a private message
	if (aForm.sendTo) {
		toNick = aForm.sendTo.value;
		user = roster.getUserByNick(toNick);
		aMsg.setTo(CONFERENCEROOM + "/" + toNick);
		aMsg.setType("chat");
		aMsg.setBody(aForm.messageInput.value);
		
		// append message to one's own window (jabber doesn't send message back to sender)
		if (!(NICK == toNick)) {
			updateMessageDisplay(nickToMessageArea(toNick), generateMessageHTML(NICK, aForm.messageInput.value, null));
		}
	}
	// otherwise, it's a group chat message
	else {
		toNick = null;
		user = null
		aMsg.setTo(CONFERENCEROOM);
		aMsg.setType("groupchat");
		aMsg.setBody(aForm.messageInput.value);
	}
  	
	aMsg.setFrom(USERNAME + "@" + XMPPDOMAIN + "/" + RESOURCE);
	
	// if groupchat or if user is available
	if(!user || user.status == "available"){
		// log message
		$.post(lamsUrl + "PresenceChatLogger.do", {method: "saveMessage", roomName:CONFERENCEROOM, from:NICK, to:toNick, dateSent:new Date(), message:aForm.messageInput.value}, null);
	
		// send message
		con.send(aMsg);
	}
	// otherwise if not groupchat and user is unavailable
	else if(user && user.status == "unavailable"){
		// generate the emote message and display it to user
		var emoteMessage = generateEmoteHTML("Your previous message has not been sent because " + toNick + " is unavailable");
		updateMessageDisplay(nickToMessageArea(toNick), emoteMessage);
	}
	
	aForm.messageInput.value = "";
	aForm.messageInput.focus();
	return false;
}

/* ******* Event Handlers ******* */
function handleEvent(aJSJaCPacket) {
	document.getElementById("iResp").innerHTML += "IN (raw):<br>" + htmlEnc(aJSJaCPacket.xml()) + "<hr>";
}

function handleMessage(aJSJaCPacket) {
	// get nick
	var nick = aJSJaCPacket.getFrom().substring(aJSJaCPacket.getFrom().indexOf("/") + 1);
	
	// get message
	var message = htmlEnc(aJSJaCPacket.getBody());
	
	// get type (private or group)
	var type = aJSJaCPacket.getType();
	
	// prepare htmlMessage
	var htmlMessage;
				
	// if message is private chat
	if (type == "chat") {	
		// get the tag from nick
		var tag = nickToTag(nick);
		
		// get user from nick
		var user = roster.getUserByNick(nick);
		
		// if sending user is not blocked
		if(!user.blocked){
			// if the receiver doesn't have a tab with the sender open
			if(document.getElementById(nickToMessageArea(nick)) == null){
				// add a tab
				addTab(nick, tag);
				
				// don't add the message, let the log take care of that
			}
			// if there is a tab for the send open
			else{				
				// generate html
				htmlMessage = generateMessageHTML(nick, message, null);
			}
			
			// get the selected tab
			var selected = $("#presenceChatTabs").tabs().data('selected.tabs');
			
			// if the selected tab is any other than sender's tab
			if(getUserFromTabIndex(selected).tag != tag){
				// notify of new message
				$("#" + tagToTabLabel(tag)).addClass('presenceTabNewMessage');
				$("#" + tagToListing(tag)).addClass('presenceListingNewMessage');
			}
				
			// add the div to the sender's tab
			updateMessageDisplay(nickToMessageArea(nick), htmlMessage);
		}
	}
	// if message is groupchat
	else {
		if (type == "groupchat") {
			// get the selected tab
			var selected = $("#presenceChatTabs").tabs().data('selected.tabs');
		
			// if the selected tab is any other than groupchat
			if(selected != 0){
				// make the group chat label label bold
				$("#" + tagToTabLabel(groupChatInfo.tag)).addClass('presenceTabNewMessage');
			}
		
			htmlMessage = generateMessageHTML(nick, message, null);
			updateMessageDisplay('groupchat_messageArea', htmlMessage);
		} else {
			// somethings wrong, dont add anything
			htmlMessage = "";
		}
	}
}

function handlePresence(presence) {
	// get presence attributes
	var from = presence.getFrom();
	var status = presence.getStatus();
	var show = presence.getShow();
	var type = presence.getType();
	
    // get x element for MUC 
	var x;
	for (var i = 0; i < presence.getNode().getElementsByTagName("x").length; i++) {
		if (presence.getNode().getElementsByTagName("x").item(i).getAttribute("xmlns") == "http://jabber.org/protocol/muc#user") {
			x = presence.getNode().getElementsByTagName("x").item(i);
			break;
		}
	}
	
	// extract nick	
	var nick;
	if (from.indexOf("/") != -1) {
		nick = from.substring(from.indexOf("/") + 1);
	}
	
	// get user from nick
	var user = roster.getUserByNick(nick);
		
	// if user is new
	if (!user) {
		// add use to roster
		user = new RosterUser(nick);
		roster.addUser(user);
	}
	
	// get tag
	var tag = "" + user.tag;
	
	// if presence has a specific status, assign it
	if (show) {
		user.status = show;
	} else {
		if (type) {
			// if type is unavailable
			if (type == "unavailable") {	
				// set unavailable status
				user.status = "unavailable";
			}
		}
		// default: means presence is available
		else {
			// set status to available
			user.status = "available";
		}
	}
	
	// if presence im is enabled (there is a chatbox)
	if(presenceImEnabled == "true"){
		// get the selected tab
		var selected = $("#presenceChatTabs").tabs().data('selected.tabs');
		
		if(showStatus){
			// if the selected tab is any other than groupchat
			if(selected != 0){
				// make the group chat label label bold
				$("#" + tagToTabLabel(groupChatInfo.tag)).addClass('presenceTabNewMessage');
			}
			
			// generate the emote message and display it to group chat
			var emoteMessage;
			if(user.status == "unavailable"){
				emoteMessage = generateEmoteHTML(nick + "has gone offline");
			}
			else if(user.status == "available"){
				emoteMessage = generateEmoteHTML(nick + "has come online");
			}
			updateMessageDisplay('groupchat_messageArea', emoteMessage);
		}
		
		// if the person who got disconnected has a tab open
		if(document.getElementById(nickToMessageArea(nick)) != null){		
			// if showing status messages			
			if(showStatus){
				// if the selected tab is any other than sender's tab
				if(getUserFromTabIndex(selected).tag != tag){
					// notify of new message
					$("#" + tagToTabLabel(tag)).addClass('presenceTabNewMessage');
				}
				
				// generate the emote message and display it to private chat
				var emoteMessage;
				if(user.status == "unavailable"){
					emoteMessage = generateEmoteHTML(nick + "has gone offline");
				}
				else if(user.status == "available"){
					emoteMessage = generateEmoteHTML(nick + "has come online");
				}
				updateMessageDisplay(nickToMessageArea(nick), emoteMessage);
			}
		}
	}
	
	// update the roster
	roster.updateDisplay();
}

function handleConnected() {
	// if presence im is enabled (there is a chatbox)
	if(presenceImEnabled == "true" && showStatus){
		// generate the emote message and display it to group chat
		var emoteMessage = generateEmoteHTML("You have been connected to LAMS instant messaging");
		updateMessageDisplay('groupchat_messageArea', emoteMessage);
	}
	
	// send presence
	var aPresence = new JSJaCPresence();
	aPresence.setTo(CONFERENCEROOM + "/" + NICK);
	aPresence.setFrom(USERNAME + "@" + XMPPDOMAIN);
	var x = aPresence.getDoc().createElement("x");
	x.setAttribute("xmlns", "http://jabber.org/protocol/muc");
	x.appendChild(aPresence.getDoc().createElement("password")).appendChild(aPresence.getDoc().createTextNode(PASSWORD));
	aPresence.getNode().appendChild(x);
	con.send(aPresence);	
	
	// set up roster
	roster = new Roster();
	
	// get group chat history
	var anticache = new Date().getTime();
	$.post(lamsUrl + "PresenceChatLogger.do", {method: "getGroupHistory", roomName:CONFERENCEROOM, anticache:anticache}, handleGetGroupHistory);
}

function handleDisconnected() {
	// if presence im is enabled (there is a chatbox)
	if(presenceImEnabled == "true" && showStatus){
		// generate the emote message and display it to group chat
		var emoteMessage = generateEmoteHTML("You have been disconnected from LAMS instant messaging");
		updateMessageDisplay('groupchat_messageArea', emoteMessage);
	}
}

function handleError(e) {
	switch(e.getAttribute("code")){
		// unauthorized, try register
		case "401":
			doRegistration();
			break;
	}
}

function handlePresenceRegistration(registrationInfo){
	// if registrationInfo exists, registration worked
	if (registrationInfo) {
		// attempt to re-login
		doLogin(presenceUrl, userId, userId, userId, roomName, nickname, false);
	}
}

function handleGetGroupHistory(groupHistory){
	var xml = parseXml(groupHistory);  
	if(xml){
		var groupHistory = $('<div id="groupHistory"></div>');
		$(xml).find("clause").each(function() {
			var from = $(this).find('from').text();
			var dateSent = $(this).find('dateSent').text();
			var message = $(this).find('message').text();
			groupHistory.append(generateMessageHTML(from, message, dateSent));
		});
		
		updateMessageDisplay("groupchat_messageArea", groupHistory, true);
	}
}

function handleGetConversation(conversation){
	var xml = parseXml(conversation);
	if(xml){
		var nick = $(xml).find('nick').text();
		var conversation = $('<div id="' + nickToTag(nick) + '_conversation"></div>');
		$(xml).find("clause").each(function() {
			var from = $(this).find('from').text();
			var to = $(this).find('to').text();
			var dateSent = $(this).find('dateSent').text();
			var message = $(this).find('message').text();
			conversation.append(generateMessageHTML(from, message, dateSent));
		});
		
		updateMessageDisplay(nickToMessageArea(nick), conversation, true);
	}
}

/* ******* Connection function ******* */
function doLogin(presenceServerUrl, userID, password, resource, chatroom, nickname, register) {
	try {
		// if register is set to false (first time login), store connection info, otherwise, just use it again
		if(!register) {
			USERNAME = userID;
			PASSWORD = password;
			RESOURCE = resource;
			XMPPDOMAIN = presenceServerUrl;
			NICK = unescape(nickname);
			CONFERENCEROOM = chatroom + "@conference." + presenceServerUrl;
			
			// debug statement for connection information
			//alert(HTTPBASE + USERNAME + PASSWORD + RESOURCE + XMPPDOMAIN + NICK + CONFERENCEROOM + FROMFLASH);
		}
				
		// setup args for contructor
		// HTTPBASE is defined in controlFrame for flash interface and in mainnoflash for flashless interface
		var oArgs = {httpbase:HTTPBASE, timerval:6000};
		if (typeof (oDbg) != "undefined") {
			oArgs.oDbg = oDbg;
		}
		con = new JSJaCHttpBindingConnection(oArgs);
		con.registerHandler("message", handleMessage);
		con.registerHandler("presence", handlePresence);
		con.registerHandler("iq", handleEvent);
		con.registerHandler("onconnect", handleConnected);
		con.registerHandler("ondisconnect", handleDisconnected);
		con.registerHandler("onerror", handleError);

		// setup args for connect method
		oArgs = {domain:XMPPDOMAIN, username:USERNAME, resource:RESOURCE, pass:PASSWORD, register:register};
		con.connect(oArgs);
	}
	catch (e) {
		// error
	}
	finally {
		return false;
	}
}

function doRegistration(){
	$.post(xmppServlet, {method: "createXmppId"}, handlePresenceRegistration);
}

/*
function init() {
	if (typeof (Debugger) == "function") {
		var oDbg = new Debugger(4, "simpleclient");
		oDbg.start();
	}
	doLogin();
}

onload = init;
*/

onunload = function () {
	if (typeof (con) != "undefined" && con.disconnect) {
		con.disconnect();
	}
};