
/* ******* Constants ******* */
var GROUPCHAT_MSG = "";
var PRIVATE_MSG = "private_message";
var PALETTE = ["#0000FF", "#006699", "#0066FF", "#6633FF", "#00CCFF", "#009900", "#00CC33", "#339900", "#008080", "#66FF66", "#CC6600", "#FF6600", "#FF9900", "#CC6633", "#FF9933", "#990000", "#A50021", "#990033", "#CC3300", "#FF6666", "#330033", "#663399", "#6633CC", "#660099", "#FF00FF", "#999900", "#808000", "#FF9FF2", "#666633", "#292929", "#666666"];
/* ******* Connection variables ******* */
var CONFERENCEROOM = "";
var XMPPDOMAIN = "";
var USERNAME = "";
var PASSWORD = "";
var NICK = "";
var RESOURCE = "";
var FROMFLASH;
/* ******* Helper Functions ******* */
function getColour(nick) {
	var charSum = 0;
	for (var i = 0; i < nick.length; i++) {
		charSum += nick.charCodeAt(i);
	}
	return PALETTE[charSum % (PALETTE.length)];
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

function correctPresenceName(o){
	var s = o.nick;
	var newNick = new String("");
	for (var i = 0; i < s.length; i++) {
		var char = s.charCodeAt(i);
		if (char >= 192 && char <= 197)
			newNick += "A";
		else if (char == 199)
			newNick += "C";
		else if (char >= 200 && char <= 203)
			newNick += "E";
		else if (char >= 204 && char <= 207)
			newNick += "I";
		else if (char == 209)
			newNick += "N";
		else if ((char >= 210 && char <= 214) || char == 216)
			newNick += "O";
		else if (char >= 217 && char <= 220)
			newNick += "U";
		else if (char == 221)
			newNick += "Y";
		else if (char >= 224 && char <= 229)
			newNick += "a";
		else if (char == 231)
			newNick += "c";
		else if (char >= 232 && char <= 235)
			newNick += "e";
		else if (char >= 236 && char <= 239)
			newNick += "i";
		else if (char == 241)
			newNick += "n";
		else if ((char >= 242 && char <= 246) || char == 240 || char == 248)
			newNick += "o";
		else if (char >= 249 && char <= 252)
			newNick += "u";
		else if (char == 253 || char == 255)
			newNick += "y";
		else
			newNick += s.charAt(i);
	}
	
	o.nick = newNick;
	return o;
}

function correctPresenceRoomName(s){
	s = s.replace(/ /g, "_");
	s = s.replace(/:/g, "_");
	
	return s;
}

function createElem(name, attrs, style, text) {
	var e = document.createElement(name);
	if (attrs) {
		for (var key in attrs) {
			if (key == "attrClass") {
				e.className = attrs[key];
			} else {
				if (key == "attrId") {
					e.id = attrs[key];
				} else {
					e.setAttribute(key, attrs[key]);
				}
			}
		}
	}
	if (style) {
		for (key in style) {
			e.style[key] = style[key];
		}
	}
	if (text) {
		e.appendChild(document.createTextNode(text));
	}
	return e;
}

/* ******* Roster ******* */
function RosterUser(nick) {
	this.nick = nick;
	this.status = "unavailable";
}
function getRosterUserByNick(nick) {
	for (var i = 0; i < this.users.length; i++) {
		if (this.users[i].nick == nick) {
			return this.users[i];
		}
	}
	return null;
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
	if(FROMFLASH){
		// send users to flash
		var availableUsers = [];
		for (var i = 0; i < this.users.length; i++) {
			if (this.users[i].status != "unavailable") {
				availableUsers[availableUsers.length] = correctPresenceName(this.users[i]);
			}
		}
		flashProxy.call("sendUsersToFlash", availableUsers);
	}
	else {
		// send roster to no flash version
		var rosterDiv = document.getElementById("roster");
		this.users.sort(sortFunction);
		var availableCount = 0;
		
		// so sorry about this
		for (var i = 0; i < this.users.length; i++) {
			if (this.users[i].status != "unavailable") {
				availableCount++;
			}
		}
		
		var presenceString = presenceLabel + " (" + availableCount + ")";
		rosterDiv.innerHTML = "<h5>" + presenceString + "</h5>";
		
		for (var i = 0; i < this.users.length; i++) {
			if (this.users[i].status != "unavailable") {
				var nick = this.users[i].nick;
				var userDiv = createElem("div", {attrId:"user-" + i}, {width:"100%", color:"#0000FF"}, this.users[i].nick);
				rosterDiv.appendChild(userDiv);
			}
		}
	}
}

function sortFunction(a, b){
  if (a.nick < b.nick)
    return -1;
  if (a.nick > b.nick)
    return 1;
  return 0;
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

function selectUser(userDiv) {
	if (MODE == "teacher") {
		var newIndex = userDiv.id.substring(userDiv.id.indexOf("-") + 1, userDiv.id.length);
		if (roster.currentIndex == newIndex) {
			roster.currentIndex = null;
		} else {
			roster.currentIndex = newIndex;
		}
		
		// update "send message to" display
		var sendToUserSpan = document.getElementById("sendToUser");
		var sendToEveryoneSpan = document.getElementById("sendToEveryone");
		if (roster.currentIndex == null) {
			sendToEveryoneSpan.style.display = "inline";
			sendToUserSpan.style.display = "none";
		} else {
			sendToEveryoneSpan.style.display = "none";
			sendToUserSpan.style.display = "inline";
			var nick = roster.users[roster.currentIndex].nick;
			var nickElem = createElem("span", null, {color:getColour(nick)}, nick);
			sendToUserSpan.innerHTML = ""; // clear previous user name
			sendToUserSpan.appendChild(nickElem);
		}
		roster.updateDisplay();
	}
}

/* ******* Chat functions ******* */
function setFocusOnTextarea() {
	document.forms[0].msg.focus();
}
function scrollMessageDisplay() {
	var iRespDiv = document.getElementById("iResp");
	iRespDiv.scrollTop = iRespDiv.scrollHeight;
}
function generateMessageHTML(nick, message, type) {
	var colour = getColour(nick);
	var fromElem = createElem("div", {attrClass:"messageFrom"}, null, nick);
	var msgElem = createElem("div", {attrClass:"message " + type}, {color:colour}, null);
	msgElem.innerHTML = message;
	msgElem.insertBefore(fromElem, msgElem.firstChild);
	return msgElem;
}
function updateMessageDisplay(htmlMessage) {
	var iRespDiv = document.getElementById("iResp");
	iRespDiv.appendChild(htmlMessage);
	iRespDiv.scrollTop = iRespDiv.scrollHeight;
}
function sendMsg(aForm) {
	if (aForm.msg.value === "") {
		return false;  // do not send empty messages.
	}
	var aMsg = new JSJaCMessage();
	if (MODE == "teacher" && !(roster.currentIndex === null)) {
		var toNick = roster.users[roster.currentIndex].nick;
		aMsg.setTo(CONFERENCEROOM + "/" + toNick);
		aMsg.setType("chat");
		var message = "[" + toNick + "] " + aForm.msg.value;
		aMsg.setBody(message);
  		// apending the private message to the incoming window,
  		// since the jabber server will not echo sent private messages.
  		// TODO: need to check if this is correct behaviour
		if (!(NICK == toNick)) {
			updateMessageDisplay(generateMessageHTML(NICK, message, PRIVATE_MSG));
		}
	} else {
		aMsg.setTo(CONFERENCEROOM);
		aMsg.setType("groupchat");
		aMsg.setBody(aForm.msg.value);
	}
  		
  // }
	aMsg.setFrom(USERNAME + "@" + XMPPDOMAIN + "/" + RESOURCE);
	con.send(aMsg);
	aForm.msg.value = "";
	return false;
}

/* ******* Event Handlers ******* */
function handleEvent(aJSJaCPacket) {
	document.getElementById("iResp").innerHTML += "IN (raw):<br>" + htmlEnc(aJSJaCPacket.xml()) + "<hr>";
}
function handleMessage(aJSJaCPacket) {
	var nick = aJSJaCPacket.getFrom().substring(aJSJaCPacket.getFrom().indexOf("/") + 1);
	var message = htmlEnc(aJSJaCPacket.getBody());
	var type = aJSJaCPacket.getType();
	var htmlMessage;
	if (type == "chat") {
		htmlMessage = generateMessageHTML(nick, message, PRIVATE_MSG);
	} else {
		if (type == "groupchat") {
			htmlMessage = generateMessageHTML(nick, message, GROUPCHAT_MSG);
		} else {
			/* somethings wrong, dont add anything.*/
			htmlMessage = "";
		}
	}
	updateMessageDisplay(htmlMessage);
}
function handlePresence(presence) {
	// debug statement for presence
	//alert("presence");
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
	
	// extract nick.		
	var nick;
	if (from.indexOf("/") != -1) {
		nick = from.substring(from.indexOf("/") + 1);
	}
	var user = roster.getUserByNick(nick);
	if (!user) {
		user = new RosterUser(nick);
		roster.addUser(user);
	}
	if (show) {
		user.status = show;
	} else {
		if (type) {
			if (type == "unavailable") {
				user.status = "unavailable";
			}
		} else {
		// default: means presence is available.
			user.status = "available";
		}
	}
	
	if(FROMFLASH){
		// notify flash
		flashProxy.call("sendMessageToFlash", "Presence handled");
	}
	else {
		// NoFlash equivalent
	}	
	
	roster.updateDisplay();
}
function handleConnected() {
	//debug statement for connecton
	//alert("connected");
	
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
	
	if(FROMFLASH){
		// notify flash
		flashProxy.call("sendMessageToFlash", "Connected");
	}
	else {
		// NoFlash equivalent
	}
}
function handleError(e) {
	if(FROMFLASH){
		// notify flash
		flashProxy.call("sendMessageToFlash", "Code: " + e.getAttribute("code") + " Type: " + e.getAttribute("type"));
		
		switch(e.getAttribute("code")){
		// unauthorized, try register
		case "401":
			flashProxy.call("attemptRegistration");
			break;
		}
	}
	else{
		switch(e.getAttribute("code")){
			// unauthorized, try register
			case "401":
				attemptRegistration();
				break;
		}
	}
}

function breakPoint() {
	var i = 0;
}

/* ******* Init ******* */
function doLogin(presenceServerUrl, userID, password, resource, chatroom, nickname, register, fromFlash) {
	try {
		// if register is set to false (first time login), store connection info, otherwise, just use it again
		if(!register) {
			USERNAME = userID;
			PASSWORD = password;
			RESOURCE = resource;
			XMPPDOMAIN = presenceServerUrl;
			NICK = nickname;
			CONFERENCEROOM = chatroom + "@conference." + presenceServerUrl;
			FROMFLASH = fromFlash;
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
		con.registerHandler("onerror", handleError);

		// setup args for connect method
		oArgs = {domain:XMPPDOMAIN, username:USERNAME, resource:RESOURCE, pass:PASSWORD, register:register};
		con.connect(oArgs);
		
		if(FROMFLASH){
			// notify flash
			flashProxy.call("sendMessageToFlash", "Attempting connection");
		}
		else {
			// NoFlash equivalent
		}
	}
	catch (e) {
		flashProxy.call("sendMessageToFlash", e.toString());
	}
	finally {
		return false;
	}
}
function getUsers() {
	flashProxy.call("sendMessageToFlash", GetAllRosterUsers());
}
function init() {
	/*
	if (typeof (Debugger) == "function") {
		var oDbg = new Debugger(4, "simpleclient");
		oDbg.start();
	}
	doLogin();
	*/
}
onload = init;
onunload = function () {
	if (typeof (con) != "undefined" && con.disconnect) {
		con.disconnect();
	}
};
/* ******* Helper functions ******* */
function checkEnter(e) { 			//e is event object passed from function invocation
	var characterCode; 				//literal character code will be stored in this variable
	if (e && e.which) { 			//if which property of event object is supported (NN4)
		e = e;
		characterCode = e.which; 	//character code is contained in NN4's which property
	} else {
		e = event;
		characterCode = e.keyCode; 	//character code is contained in IE's keyCode property
	}
	if (characterCode == 13) { 		//if generated character code is equal to ascii 13 (if enter key)
		//document.forms[0].submit();	//submit the form
		sendMsg(document.forms[0]);
		return false;
	} else {
		return true;
	}
}

