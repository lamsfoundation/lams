<%@ include file="/common/taglibs.jsp"%>

<c:set var="lams">
	<lams:LAMSURL />
</c:set>
<c:set var="tool">
	<lams:WebAppURL />
</c:set>

<%-- Learning Script --%>

<script language="JavaScript" type="text/javascript" src="${tool}includes/javascript/jsjac/sha1.js"></script>
<script language="JavaScript" type="text/javascript" src="${tool}includes/javascript/jsjac/xmlextras.js"></script>
<script language="JavaScript" type="text/javascript" src="${tool}includes/javascript/jsjac/JSJaCConnection.js"></script>
<script language="JavaScript" type="text/javascript" src="${tool}includes/javascript/jsjac/JSJaCPacket.js"></script>
<script language="JavaScript" type="text/javascript" src="${tool}includes/javascript/jsjac/JSJaCHttpPollingConnection.js"></script>
<script language="JavaScript" type="text/javascript" src="${tool}includes/javascript/jsjac/JSJaCHttpBindingConnection.js"></script>

<!--  Uncomment to include debugger -->
<!--  <script language="JavaScript" type="text/javascript" src="Debugger.js"></script> -->

<script language="JavaScript" type="text/javascript">

// VARIABLES

var HTTPBASE = "${tool}JHB/";

var XMPPDOMAIN = "${XMPPDOMAIN}";
var USERNAME = "${USERNAME}";
var PASSWORD = "${PASSWORD}";
var CONFERENCEROOM = "${CONFERENCEROOM}";
var NICK = "${NICK}";

function htmlEnc(str) {
  if (!str)
    return null;

  str = str.replace(/&/g,"&amp;");
  str = str.replace(/</g,"&lt;");
  str = str.replace(/>/g,"&gt;");
  str = str.replace(/\"/g,"&quot;");
	str = str.replace(/\n/g,"<br />");
  return str;
}

function handleEvent(aJSJaCPacket) {
	document.getElementById('iResp').innerHTML += "IN (raw):<br/>" +htmlEnc(aJSJaCPacket.xml()) + '<hr/>';
}

function handleMessage(aJSJaCPacket) {
	var messageFrom = aJSJaCPacket.getFrom().substring(aJSJaCPacket.getFrom().indexOf('/')+1);
	var html = '';
	html += '<b>'+ messageFrom + ':</b>';
	html += htmlEnc(aJSJaCPacket.getBody()) + '<br/>';
	document.getElementById('iResp').innerHTML += html;
}

function handlePresence(aJSJaCPacket) {
	var html = '';
	if (!aJSJaCPacket.getType() && !aJSJaCPacket.getShow()) 
		html += '<b>'+aJSJaCPacket.getFrom()+' has become available.</b>';
	else {
		html += '<b>'+aJSJaCPacket.getFrom()+' has set his presence to ';
		if (aJSJaCPacket.getType())
			html += aJSJaCPacket.getType() + '.</b>';
		else
			html += aJSJaCPacket.getShow() + '.</b>';
		if (aJSJaCPacket.getStatus())
			html += ' ('+htmlEnc(aJSJaCPacket.getStatus())+')';
	}
	html += '<hr/>';

	document.getElementById('iResp').innerHTML += html;
}

function handleConnected() {
	document.getElementById('login_pane').style.display = 'none';
	document.getElementById('sendmsg_pane').style.display = '';


	// send presence
	var aPresence = new JSJaCPresence();
	aPresence.setTo(CONFERENCEROOM+"/"+NICK);
	aPresence.setFrom(USERNAME+"@"+XMPPDOMAIN);

	var x = aPresence.getDoc().createElement('x');
	x.setAttribute('xmlns','http://jabber.org/protocol/muc');
	x.appendChild(aPresence.getDoc().createElement('password')).appendChild(aPresence.getDoc().createTextNode(PASSWORD));

	aPresence.getNode().appendChild(x);
	con.send(aPresence);	

	//con.send(new JSJaCPresence());
}


function handleError(e) {
	document.getElementById('login_err').innerHTML = "Couldn't connect. Please try again...<br />"+ 
		htmlEnc("Code: "+e.getAttribute('code')+"\nType: "+e.getAttribute('type')+"\nCondition: "+e.firstChild.nodeName); 
}

function doLogin() {

	try {

		// setup args for contructor
		oArgs = new Object();
		oArgs.httpbase = HTTPBASE;
		oArgs.timerval = 2000;

		if (typeof(oDbg) != 'undefined')
			oArgs.oDbg = oDbg;

		con = new JSJaCHttpBindingConnection(oArgs);

		con.registerHandler('message',handleMessage);
		con.registerHandler('presence',handlePresence);
		con.registerHandler('iq',handleEvent);
		con.registerHandler('onconnect',handleConnected);
		con.registerHandler('onerror',handleError);

		// setup args for connect method
		oArgs = new Object();
		oArgs.domain = XMPPDOMAIN;
		oArgs.username = USERNAME;
		oArgs.resource = 'jsjac_simpleclient';
		oArgs.pass = PASSWORD;
		con.connect(oArgs);
	} catch (e) {
		document.getElementById('login_err').innerHTML = e.toString();
	} finally {
		return false;
	}
}

function sendMsg(aForm) {
	var aMsg = new JSJaCMessage();
	aMsg.setTo(CONFERENCEROOM);
	aMsg.setType('groupchat');
	aMsg.setBody(aForm.msg.value);
	con.send(aMsg);

	aForm.msg.value = '';

	return false;
}

function init() {
	if (typeof(Debugger) == 'function') {
		oDbg = new Debugger(4,'simpleclient');
		oDbg.start();
	}

	doLogin();
}
onload = init;

onunload = function() { if (typeof(con) != 'undefined' && con.disconnect) con.disconnect(); };

</script>

<style type="text/css">
	
	input {
		border: 1px solid grey;
	}
	
	#iResp {
		height: 480px;

		overflow: scroll;
		border: solid thin black;
		
		padding: 4px;
		
		background-color: white
	}

	#msgArea {
		height: 45px;

		padding: 4px;
		margin: 0;

		border: solid thin black;
	}

</style>
