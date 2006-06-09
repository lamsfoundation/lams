// Constants
var GROUPCHAT_MSG = "groupchat_message";
var PRIVATE_MSG = "private_message";

// Variables
var roster = new Roster();

// Helper functions
function htmlEnc(str) {
    if (!str) {
        return null;
    }
    str = str.replace(/&/g, "&amp;");
    str = str.replace(/</g, "&lt;");
    str = str.replace(/>/g, "&gt;");
    str = str.replace(/\"/g, "&quot;");
    str = str.replace(/\n/g, "<br />");
    return str;
}

// Roster 
function RosterUser(nick) {
    this.nick = nick;
    this.status = "unavailable";
}
function getRosterUserByNick(nick) {
    for (var i = 0; i < roster.users.length; i++) {
        if (roster.users[i].nick == nick) {
            return roster.users[i];
        }
    }
}
function AddRosterUser(user) {
    roster.users[roster.users.length] = user;
}
function UpdateRosterUser(user) {
}
function RemoveRosterUser() {
}
function updateRosterDisplay() {
    var oSelect = document.getElementById("roster_user_selector");
    oSelect.innerHTML = "";
    var oOption;
    var nick;
    for (var i = 0; i < roster.users.length; i++) {
        if (roster.users[i].status != "unavailable") {
            nick = roster.users[i].nick;
            oOption = new Option(nick, nick);
            oSelect.options[oSelect.options.length] = oOption;
        }
    }
}
function Roster() {
    this.users = new Array();
    // objects methods 
    this.getUserByNick = getRosterUserByNick;
    this.addUser = AddRosterUser;
    this.updateUser = UpdateRosterUser;
    this.removeUser = RemoveRosterUser;
    this.updateDisplay = updateRosterDisplay;
}
function resetInputs() {
    document.getElementById("roster_user_selector").selectedIndex = -1;
    updateSendDisplay();
}
function updateSendDisplay() {
    var rosterList = document.getElementById("roster_user_selector");
    var selectedIndex = rosterList.selectedIndex;
    var userName;
    if (MODE == "teacher" && !(selectedIndex == -1)) {
        var userName = rosterList.options[rosterList.selectedIndex].value;
        document.getElementById("sendToUser").innerHTML = userName;
        
        document.getElementById("sendToEveryone").style.display="none";
        document.getElementById("sendToUser").style.display="";
        
    } else {
        document.getElementById("sendToUser").style.display="none";
        document.getElementById("sendToEveryone").style.display="";
    }
}
function generateMessageHTML(nick, message, type) {
    return "<div class='" + type + "'><span class='messageFrom'>" + nick + ":</span>" + message + "<div/>";
}
function updateMessageDisplay(htmlMessage) {
    var iRespDiv = document.getElementById("iResp");
    iRespDiv.innerHTML += htmlMessage;
    iRespDiv.scrollTop = iRespDiv.scrollHeight;
}
// Event handlers
function handleEvent(aJSJaCPacket) {
    document.getElementById("iResp").innerHTML += "IN (raw):<br/>" + htmlEnc(aJSJaCPacket.xml()) + "<hr/>";
}
function handleMessage(aJSJaCPacket) {
    var nick = aJSJaCPacket.getFrom().substring(aJSJaCPacket.getFrom().indexOf("/") + 1);
    var message = htmlEnc(aJSJaCPacket.getBody());
    var type = aJSJaCPacket.getType();
    var htmlMessage;
    if (type == "chat") {
        htmlMessage = generateMessageHTML(nick, message, PRIVATE_MSG);
    } else {
        if (type = "groupchat") {
            htmlMessage = generateMessageHTML(nick, message, GROUPCHAT_MSG);
        } else {
            /* somethings wrong, dont add anything.*/
            htmlMessage = "";
        }
    }
    updateMessageDisplay(htmlMessage);
}
function handlePresence(presence) {
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
    roster.updateUser(user);
    roster.updateDisplay();
    updateSendDisplay();
}
function handleConnected() {
    document.getElementById("login_pane").style.display = "none";
    document.getElementById("chat_pane").style.display = "";
	
	if (MODE == "learner") {
	    if ( LEARNER_FINISHED == "true") {
		    if (LOCK_ON_FINISHED == "true") {
    			// disable sending messages.
	    		document.getElementById("msgArea").disabled="disabled";
    			document.getElementById("sendButton").disabled="disabled";
    			document.getElementById("clearButton").disabled="disabled";
    		}       		
   		} else {
			document.getElementById("finishButtonDiv").style.display ="";	
    	}   
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
}
function handleError(e) {
    document.getElementById("login_err").innerHTML = "Couldn't connect. Please try again...<br />" + htmlEnc("Code: " + e.getAttribute("code") + "\nType: " + e.getAttribute("type") + "\nCondition: " + e.firstChild.nodeName);
}
function doLogin() {
    try {

		// setup args for contructor
        oArgs = new Object();
        oArgs.httpbase = HTTPBASE;
        oArgs.timerval = 2000;
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
        oArgs = new Object();
        oArgs.domain = XMPPDOMAIN;
        oArgs.username = USERNAME;
        oArgs.resource = RESOURCE;
        oArgs.pass = PASSWORD;
        con.connect(oArgs);
    }
    catch (e) {
        document.getElementById("login_err").innerHTML = e.toString();
    }
    finally {
        return false;
    }
}
function sendMsg(aForm) {
    var aMsg = new JSJaCMessage();
    var rosterList = document.getElementById("roster_user_selector");
    var selectedIndex = rosterList.selectedIndex;
    if (MODE == "teacher" && !(selectedIndex == -1)) {
        var toNick = rosterList.options[selectedIndex].value;
        aMsg.setTo(CONFERENCEROOM + "/" + toNick);
        aMsg.setType("chat");
  		// apending the private message to the incoming window, 
  		// since the jabber server will not echo sent private messages.
  		// TODO: need to check if this is correct behaviour
        // updateMessageDisplay(generateMessageHTML(NICK, aForm.msg.value, PRIVATE_MSG));
    } else {
        aMsg.setTo(CONFERENCEROOM);
        aMsg.setType("groupchat");
    }
  		
  // }
    aMsg.setFrom(USERNAME + "@" + XMPPDOMAIN + "/" + RESOURCE);
    aMsg.setBody(aForm.msg.value);
    con.send(aMsg);
    aForm.msg.value = "";
    return false;
}
function init() {
    if (typeof (Debugger) == "function") {
        oDbg = new Debugger(4, "simpleclient");
        oDbg.start();
    }
    doLogin();
}
onload = init;
onunload = function () {
    if (typeof (con) != "undefined" && con.disconnect) {
        con.disconnect();
    }
};

