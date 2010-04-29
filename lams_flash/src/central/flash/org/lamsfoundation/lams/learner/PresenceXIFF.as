/***************************************************************************
 * Copyright (C) 2008 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0 
 * as published by the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ************************************************************************
 */

import org.jivesoftware.xiff.conference.Room;
import org.jivesoftware.xiff.data.IQ;
import org.jivesoftware.xiff.conference.InviteListener;
import org.jivesoftware.xiff.data.muc.*;
import org.jivesoftware.xiff.data.DelayExtension;
import org.jivesoftware.xiff.im.Roster;
import org.jivesoftware.xiff.core.XMPPConnection;
import org.jivesoftware.xiff.data.XMPPStanza;
import org.jivesoftware.xiff.data.Message;
import org.jivesoftware.xiff.data.register.RegisterExtension;
import org.jivesoftware.xiff.data.vcard.VCardExtension;
import org.jivesoftware.xiff.data.VCardManager;
import org.jivesoftware.xiff.data.PrivateDataManager;
import org.jivesoftware.xiff.data.PrivateDataExtension;

import org.lamsfoundation.lams.learner.*;
import org.lamsfoundation.lams.learner.ls.*;
import org.lamsfoundation.lams.common.Sequence;
import org.lamsfoundation.lams.common.ToolTip;
import org.lamsfoundation.lams.common.util.*;
import org.lamsfoundation.lams.common.dict.*;
import org.lamsfoundation.lams.common.style.*;
import org.lamsfoundation.lams.common.ApplicationParent;
import mx.controls.*
import mx.utils.*
import mx.managers.*
import mx.events.*

class Presence extends MovieClip {
	
	//Height Properties
	private var presenceHeightHide:Number = 20;
	private var presenceHeightFull:Number = 217;
	
	//Open Close Identifier
	private var _presenceIsExpanded:Boolean;
	
	//Component properties
	private var _presence_mc:MovieClip;
	private var presenceHead_pnl:MovieClip;
	private var presenceTitle_lbl:Label;
	private var presenceMinIcon:MovieClip;
	private var presenceMaxIcon:MovieClip;
	private var presenceClickTarget_mc:MovieClip;
	private var _tip:ToolTip;
	private var _users_dg:DataGrid;
	
	private var panel:MovieClip;
	private var _lessonModel:LessonModel;
	private var _lessonController:LessonController;
	private var _tm:ThemeManager;
	private var _dictionary:Dictionary;
	
	//These are defined so that the compiler can 'see' the events that are added at runtime by EventDispatcher
	private var dispatchEvent:Function;     
	public var addEventListener:Function;
	public var removeEventListener:Function;
   
	// XIFF things
	private var connection:XMPPConnection;
	private var room:Room;
	private var myHost:InviteListener;
	private var buddyList:Roster;
	private var vcardManager:VCardManager;
	private var privateData:PrivateDataManager;
	private var globalHandler:Object;
	private var chatHandler:Object;
	private var timerTestConnection:Number;

	private var selectedChatItem:Object;
	private var lastClickX:Number;
	private var lastClickY:Number;

	private var attemptConnectionInterval:Number;
	private var testConnectionInterval:Number;
	private var clickInterval:Number;
	private var rejoinOnIdleInterval:Number;
	private var registrationRetryInterval:Number;
	private var joinChatRetryInterval:Number;
	
	private var ROOMCONNECTTIMEOUT:Number =5000;
	private var REGRETRYTIME:Number = 1000;
	private var JOINCHATRETRYTIME:Number = 1000;
	private var DOUBLECLICKSPEED:Number = 500;
	
	private var regNecessaryFields:Object;
	
	private var chatDialog:MovieClip;
	
	// Constructor
	public function Presence() {
		Debugger.log('PRESENCE: started constructor',Debugger.MED,'Presence','Presence');
		
		// Set up this class to use the Flash event delegation model
        EventDispatcher.initialize(this);
		
		_tm = ThemeManager.getInstance();
		_tip = new ToolTip(_tm);
		_dictionary = Dictionary.getInstance();
		_dictionary.addEventListener('init',Proxy.create(this,setLabels));
		regNecessaryFields = new Object();
		
		connection = new XMPPConnection();
		connection.username = String(_root.userID);
		connection.password = String(_root.userID);
		connection.server = String(_root.presenceServerUrl);
		connection.port = Number(_root.presenceServerPort);
		
		var myDate = new Date();
		var h = myDate.getHours().toString(), m = myDate.getMinutes().toString(), s = myDate.getSeconds().toString();
		connection.resource = "LAMSPRESENCE"+h+""+m+""+s;
		
		/*
		myHost = new InviteListener(connection);
		buddyList = new Roster(connection);
		vcardManager = new VCardManager(connection);
		privateData= new PrivateDataManager(connection);
		*/
		
		connection.addEventListener("message", Proxy.create(this, handleGlobalEvent));
		connection.addEventListener("login", Proxy.create(this, handleGlobalEvent));
		connection.addEventListener("connection", Proxy.create(this, handleGlobalEvent));
		connection.addEventListener("disconnection", Proxy.create(this, handleGlobalEvent));
		connection.addEventListener("presence", Proxy.create(this, handleGlobalEvent));
		connection.addEventListener("error", Proxy.create(this, handleGlobalEvent));
		connection.addEventListener("changePasswordSuccess", Proxy.create(this, handleGlobalEvent));
		connection.addEventListener("outgoingData", Proxy.create(this, handleGlobalEvent));
		connection.addEventListener("incomingData", Proxy.create(this, handleGlobalEvent));
		connection.addEventListener("registrationSuccess", Proxy.create(this, handleGlobalEvent));
		connection.addEventListener("registrationFields", Proxy.create(this, handleGlobalEvent));
		
		/*
		buddyList.addEventListener("subscriptionRequest", Proxy.create(this, handleGlobalEvent));
		buddyList.addEventListener("subscriptionRevocation", Proxy.create(this, handleGlobalEvent));
		buddyList.addEventListener("subscriptionDenial", Proxy.create(this, handleGlobalEvent));
		buddyList.addEventListener("userAvailable", Proxy.create(this, handleGlobalEvent));
		buddyList.addEventListener("userUnavailable", Proxy.create(this, handleGlobalEvent));

		vcardManager.addEventListener("VCardReceived", Proxy.create(this, handleGlobalEvent));
		vcardManager.addEventListener("VCardSent", Proxy.create(this, handleGlobalEvent));

		privateData.addEventListener("PrivateDataReceived", Proxy.create(this, handleGlobalEvent));
		privateData.addEventListener("PrivateDataSent", Proxy.create(this, handleGlobalEvent));

		myHost.addEventListener("invited", Proxy.create(this, handleGlobalEvent));
		
		Key.addListener(keyListenerGlobal);
		buddyList.addEventListener("cellPress", userListener);
		*/

		Stage.addListener(this);

		// Let it wait one frame to set up the components.
		MovieClipUtils.doLater(Proxy.create(this,init));
		Debugger.log('PRESENCE: calling init method',Debugger.MED,'Presence','Presence');
	}
	
    // Called a frame after movie attached to allow components to initialise
    public function init(){
		Debugger.log('PRESENCE: started init',Debugger.MED,'init','Presence');
		
		System.security.loadPolicyFile("xmlsocket://172.20.100.18:9090:5222");
		
        //Delete the enterframe dispatcher
        delete this.onEnterFrame;
		_lessonModel = _lessonModel;
		_lessonController = _lessonController;
		_presence_mc = this;
		_presenceIsExpanded = true;
		presenceMaxIcon._visible = false;
		presenceMinIcon._visible = true;
		_lessonModel.setPresenceHeight(presenceHeightFull);
		setLabels();
		resize(Stage.width);
	
		presenceClickTarget_mc.onRelease = Proxy.create(this, localOnRelease);
		
		this.onEnterFrame = setLabels;
	}	
	
	// Click handler
	private function localOnRelease():Void{
		if (_presenceIsExpanded){
			_presenceIsExpanded = false
			presenceMinIcon._visible = false;
			presenceMaxIcon._visible = true;
			_lessonModel.setPresenceHeight(presenceHeightHide);
			
		}else {
			_presenceIsExpanded = true
			presenceMinIcon._visible = true;
			presenceMaxIcon._visible = false;
			_lessonModel.setPresenceHeight(presenceHeightFull);
			//Application.getInstance().onResize();
		}
	}
	
	// First connection attempt called once Lesson is loaded correctly
	private function attemptFirstConnection():Void{
		attemptConnection();
		checkConnectionEstablished();
	}
	
	// Attempts a connection with a given Jabber server
	private function attemptConnection():Void{
		Debugger.log('PRESENCE: attempting to connect',Debugger.MED,'connectionTimer','Presence');
		connection.connect("flash");
	}

	// Checks to see if a connection has been established
	private function checkConnectionEstablished():Void{
		testConnectionInterval = setInterval(Proxy.create(this,connectionEstablishedTimer),500);
	}
	
	// Keeps testing connection
	private function connectionEstablishedTimer():Void{
		Debugger.log('PRESENCE: checking if connection is established',Debugger.MED,'connectionEstablishedTimer','Presence');
		
		if(connection.isLoggedIn()){
			Debugger.log('PRESENCE: connection established',Debugger.MED,'connectionEstablishedTimer','Presence');
			
			clearInterval(testConnectionInterval);

			joinChatRoom();
			setupDataGrid();
		}
	}

	// Attempts to register a new user
	private function attemptRegistration(){
		Debugger.log('PRESENCE: attempting to register',Debugger.MED,'attemptRegistration','Presence');
		
		if(_lessonModel.userFirstName == null || _lessonModel.userLastName == null) {
			if(!registrationRetryInterval) {
				registrationRetryInterval = setInterval(Proxy.create(this, attemptRegistration), REGRETRYTIME);
			}
		}
		else {
			clearInterval(registrationRetryInterval);
			var email:String = new String("");
			var name:String = new String(_lessonModel.userFirstName + _lessonModel.userLastName);
			var userID:String = new String(_root.userID);
			var password:String = new String(_root.userID);
			var loginAfter:Boolean = true;
			
			regNecessaryFields = {username:userID, password:password, name:name, email:email, loginAfter:loginAfter};
			
			connection.sendRegistrationFields(regNecessaryFields);
		}
	}
	
	// Attempts to join a chat room
	private function joinChatRoom():Void{
		Debugger.log('PRESENCE: attempting to create and join chatroom',Debugger.MED,'joinChatRoom','Presence');
					
		if(Application.getInstance().userDataLoaded) {
			if(_lessonModel.userFirstName == null || _lessonModel.userLastName == null) {
				Debugger.log('PRESENCE: names are null, can not join chat room',Debugger.MED,'joinChatRoom','Presence');
				
				clearInterval(joinChatRetryInterval);
				clearInterval(testConnectionInterval);
			}
			else {
				Debugger.log('PRESENCE: attempting to create and join chatroom',Debugger.MED,'joinChatRoom','Presence');
				
				clearInterval(joinChatRetryInterval);
				room = new Room(connection);
				
				room.addEventListener("groupMessage", Proxy.create(this, handleChatEvent));
				room.addEventListener("userJoin", Proxy.create(this, handleChatEvent));
				room.addEventListener("userDeparture", Proxy.create(this, handleChatEvent));
				room.addEventListener("subjectChange", Proxy.create(this, handleChatEvent));
				room.addEventListener("affiliations", Proxy.create(this, handleChatEvent));
				room.addEventListener("roomRosterUpdated", Proxy.create(this, handleChatEvent));
				room.addEventListener("configureForm", Proxy.create(this, handleChatEvent));
				room.addEventListener("declined", Proxy.create(this, handleChatEvent));
				room.addEventListener("nickConflict", Proxy.create(this, handleChatEvent));
				room.addEventListener("privateMessage", Proxy.create(this, handleChatEvent));
				
				room.roomName = String(_root.lessonID);
				room.nickname = String(_lessonModel.userFirstName + _lessonModel.userLastName);
				room.password = null;
				room.conferenceServer = "conference." + _root.presenceServerUrl;
				room.join();
			}
		}
		else{
			if(!joinChatRetryInterval) {
				joinChatRetryInterval = setInterval(Proxy.create(this, joinChatRoom), JOINCHATRETRYTIME);
			}	
		}
	}
	
	// Chat event handler
	private function handleChatEvent(eventObj):Void{
		Debugger.log("PRESENCE: chat event "+eventObj.type,Debugger.MED,'handleChatEvent','Presence');
		switch (eventObj.type) {
			
			case "nickConflict":
				Debugger.log('PRESENCE: nickConflict event',Debugger.MED,'handleChatEvent','Presence');
				/*
				popUpErreur(_root.__("ERROR")+" : "+newline+_root.__("nick already in use")+newline+_root.__("I add ~ to the nickname") );
				chatRoom.nickname=nickn.text+"~";
				nickn.text=chatRoom.nickname;
				if(!chatRoom.isActive())
					chatRoom.join();
				//SEE PRESENCE EVENT
				*/
			break;
				
			case "roomRosterUpdated" :
				Debugger.log('PRESENCE: roomRosterUpdated event',Debugger.MED,'handleChatEvent','Presence');
			break;
				
			case "groupMessage" :
				Debugger.log('PRESENCE: groupMessage event',Debugger.MED,'handleChatEvent','Presence');
				/*
				//Be careful, if there are many chatroom opened, you need to tell them to recognize their message
				var msg = eventObj.data;
				
				//var msgDelay:DelayExtension = msg.getAllExtensionsByNS(DelayExtension.NS)[0];
				//
				var nick = msg.from.split("/")[1];
				
				//	trace(msgDelay.stamp);
				
				addToChatOutput(nick, msg.body);
				*/
			break;
				
			case "subjectChange" :
				Debugger.log('PRESENCE: subject = ' + eventObj.subject,Debugger.MED,'handleChatEvent','Presence');
			break;
				
			case "login" :
				Debugger.log('PRESENCE: room login event',Debugger.MED,'handleChatEvent','Presence');
				
				// Rejoin room if automatically disconnected
				rejoinOnIdleInterval = setInterval(Proxy.create(this, rejoinTimer), 500);
			break;
				
			case "presence" :
				Debugger.log('PRESENCE: room presence event',Debugger.MED,'handleChatEvent','Presence');
				/*
				// Some error message, like wrong nickname, or multiple room nickname change (impossible to do on jive...)
				if(eventObj.data.type=="error" && chatRoom.isThisRoom(eventObj.data.from)){	//It's this room error
					if(eventObj.data.errorCode!=409){		//Not a nickconflict
						if (eventObj.data.errorMessage != undefined) {		//Some have message, if true, we'll display them
							popUpErreur(_root.__("ERROR")+" "+eventObj.data.errorCode+": "+newline+eventObj.data.errorMessage);
							removeMyEventListeners();		//	remove the event listeners (not needed anymore)
							clearInterval(timerTest2);		//	This "room is active" test
							_root.closeFenetre(objectName);	//Useless window, we close it
						}else{
							popUpErreur(_root.__("ERROR")+" "+eventObj.data.errorCode);		//Just the error code!
						}
					}
				}				*/
			break;
				
			case "userJoin" :
				Debugger.log('PRESENCE: userJoin event',Debugger.MED,'handleChatEvent','Presence');
			break;
				
			case "userDeparture" :
				Debugger.log('PRESENCE: userDeparture event',Debugger.MED,'handleChatEvent','Presence');

			break;
					
			case "affiliations" :
				Debugger.log('PRESENCE: affiliations event',Debugger.MED,'handleChatEvent','Presence');
			break;
				
			case "declined" :
				Debugger.log('PRESENCE: declined event',Debugger.MED,'handleChatEvent','Presence');
			break;
				
			case "privateMessage":
				Debugger.log('PRESENCE: privateMessage event',Debugger.MED,'handleChatEvent','Presence');
				/*
				//"message" :
				// Opening a private chat window on new message
				var msg = eventObj.data;
				var nick = msg.from.split("/")[0];	//	b0ris@usuc.dyndns.org       or 	nomChat@conference.usuc.dyndns.org 
				var nick2 = msg.from.split("/")[1];	//	nomClient							b0ris
				var nick3 = nick.split("@")[1];		//	usuc.dyndns.org 	conference.usuc.dyndns.org 
				if (nick3 == roomServer) {
					addPrivateChatWindow(nick2, msg);	//	We add the window, and we send it the message
				}
				*/
			break;
		}
	}
	
	// Glocal event handler
	private function handleGlobalEvent(eventObj):Void{
		Debugger.log("PRESENCE: global event "+eventObj.type,Debugger.MED,'handleGlobalEvent','Presence');

		switch (eventObj.type) {
		
			case "outgoingData":
				Debugger.log('PRESENCE: outgoing data ' + eventObj.data,Debugger.MED,'handleGlobalEvent','Presence');
			break;
			
			case "incomingData":
				Debugger.log('PRESENCE: incoming data ' + eventObj.data,Debugger.MED,'handleGlobalEvent','Presence');
			break;
			
			case "changePasswordSuccess":
				Debugger.log('PRESENCE: changePasswordSuccess event',Debugger.MED,'handleGlobalEvent','Presence');
			break;
			
			case "PrivateDataReceived": 
				Debugger.log('PRESENCE: PrivateDataReceived event',Debugger.MED,'handleGlobalEvent','Presence');
				/*
				wait.log.text+=__("Received private Datas")+"<br>";		
				var tab:Array=new Array();
				tab=PrivateData.getConference();
				for(var i=0;i<tab.length;i++){
					if(tab[i].autojoin=="0"){
						var objectName = generateObjectName(tab[i].jid,"Chat_"); //objectName = "Chat_"+room;			//We must generate a object name compatible with object oriented programming... 
														//xiffian@conference.usuc.dyndns.org isn't correct! so we change it
						if (ajouter(objectName)) {
							if(tab[i].password!=undefined && tab[i].password!="")
								_root.attachMovie("public", objectName, _root.curDepth++, {roomServer:tab[i].jid.split("@")[1],pass:tab[i].password,room:tab[i].jid.split("@")[0], objectName:objectName, pseudo:tab[i].nick,margin:margin});
							else
								_root.attachMovie("public", objectName, _root.curDepth++, {roomServer:tab[i].jid.split("@")[1],room:tab[i].jid.split("@")[0], objectName:objectName, pseudo:tab[i].nick,margin:margin});
							barre.addNewTab(objectName, objectName, false);	
						}
					}			
				}
				*/
			break;
			
			case "PrivateDataSent": 
				Debugger.log('PRESENCE: PrivateDataSent event',Debugger.MED,'handleGlobalEvent','Presence');
				/*
				var objectName:String = "Bookmarks";	

				if (!ajouter(objectName)) {		//We close this window!
					closeFenetre(objectName);
				}
				PrivateData.getPrivateBookmark();//Refresh (due to PrivateDataExtension error)
				*/
			break;
			
			case "VCardReceived": 
				Debugger.log('PRESENCE: VCardReceived event',Debugger.MED,'handleGlobalEvent','Presence');
				/*
				wait.log.text+=__("Received the VCard fields")+"<br>";		
				var resultIQ:IQ=eventObj.resultIQ;
				var ext:VCardExtension = resultIQ.getAllExtensionsByNS(VCardExtension.NS)[0];
				var obj:Array=ext.getAllItems();			//	By default, the form is the message form
				var daForm:Array=obj;
				var leJID:String;
				if(resultIQ.from!=undefined){
					var objectName = generateObjectName("VCard_"+resultIQ.from);	
					leJID=resultIQ.from;
				}else{					//My VCard
					//	The displayed form is the complete form.
					daForm=ext.getAllFields();
					var objectName = generateObjectName("VCard_"+resultIQ.to);	
					leJID=resultIQ.to;
				}
				if (ajouter(objectName)) {
					_root.attachMovie("VCard", objectName, curDepth++, {objectName:objectName,leJID:leJID.split("/")[0], receivedMessage:obj, form:daForm});
					barre.addNewTab(objectName, objectName, true);
				}
				*/
			break;
			
			case "VCardSent":
				Debugger.log('PRESENCE: VCardSent event',Debugger.MED,'handleGlobalEvent','Presence');
				/*
				var resultIQ:IQ=eventObj.resultIQ;
				var objectName = generateObjectName("VCard_"+resultIQ.to);	

				if (!ajouter(objectName)) {		//We close this window!
					closeFenetre(objectName);
				}
				*/
			break;
				
			case "login" :
				Debugger.log('PRESENCE: global login event',Debugger.MED,'handleGlobalEvent','Presence');
			break;	
			
			case "connection" :
				Debugger.log('PRESENCE: global connection event',Debugger.MED,'handleGlobalEvent','Presence');
			break;
			
			case "disconnection" :
				Debugger.log('PRESENCE: global disconnection event',Debugger.MED,'handleGlobalEvent','Presence');
			break;
			
			case "invited":
				Debugger.log('PRESENCE: invited event',Debugger.MED,'handleGlobalEvent','Presence');
			break;
			
			case "registrationFields":
				Debugger.log('PRESENCE: registrationFields event - fields required: ' + eventObj.fields,Debugger.MED,'handleGlobalEvent','Presence');
			break;
			
			case "registrationSuccess":
				Debugger.log('PRESENCE: registrationSuccess event',Debugger.MED,'handleGlobalEvent','Presence');
				attemptConnection();
			break;
			
			case "message" :
				Debugger.log('PRESENCE: message from ' + eventObj.data.from,Debugger.MED,'handleGlobalEvent','Presence');
				/*
				//Opens private chat windows, listen to all entering messages - display broadcast messages (written to SERVER/announces/motd or SERVER/announces/online) 
				var msg = eventObj.data;
				if(msg.from==domaine){		//It's a jabber server broadcast
					d=new Date();
					ch = "<font color='#00FF00'>{"+d.getUTCHours()+":"+d.getUTCMinutes()+":"+d.getUTCSeconds()+"}</font> - ";
					MainMenu.logContacts.text += ch+"Broadcast: <br>"+msg.body+" <br>";
				}									//	We got a b0ris@usuc.dyndns.org/xiff address
				var nick = msg.from.split("/")[0];	//	b0ris@usuc.dyndns.org    
				var nick2 = nick.split("@")[0];		//	clientName	: xiff		
				var nick3 = nick.split("@")[1];		//	Domain Name 	: usuc.dyndns.org					
				if (msg.type == Message.CHAT_TYPE) {
					//	DO IT BETTER ------ parsing chat or conference is not a beautiful solution
					if (nick3.split(".")[0].indexOf("chat") == -1 && nick3.split(".")[0].indexOf("conference") == -1  && nick3.split(".")[0].indexOf("public") == -1) {		
					//It doesn't come from a public chat (the public chat private message interception is made on each public chat)
						//trace("global ouvre prive "+nick)
						addPrivateChatWindow(nick, msg);				//Opens the needed window, and sends the message to the newly created object
					}
				}
				*/
			break;
			
			case "subscriptionRequest" :
				Debugger.log('PRESENCE: subscriptionRequest event',Debugger.MED,'handleGlobalEvent','Presence');
			break;
			
			case "subscriptionRevocation" :
				Debugger.log('PRESENCE: subscriptionRevocation event',Debugger.MED,'handleGlobalEvent','Presence');
			break;
			
			case "subscriptionDenial" :
				Debugger.log('PRESENCE: subscriptionDenial event',Debugger.MED,'handleGlobalEvent','Presence');
			break;
			
			case "presence" :
				Debugger.log('PRESENCE: global presence event',Debugger.MED,'handleGlobalEvent','Presence');
			break;
			
			case "error":
				Debugger.log('PRESENCE: errorevent - error code: '+ String(eventObj.data.errorCode) + ' - errorMessage: ' + String(eventObj.data.errorMessage),Debugger.MED,'handleGlobalEvent','Presence');
				
				// Switch on error code
				switch(eventObj.data.errorCode) {
					// Case 401 login unauthorized, attempt to register user
					case 401:
						Debugger.log('PRESENCE: case 401 - attempting to register',Debugger.MED,'handleGlobalEvent','Presence');
						attemptRegistration();
					break;
					// Case 409 registration details incorrect
					case 409:
						Debugger.log('PRESENCE: case 409 - registration details incorrect',Debugger.MED,'handleGlobalEvent','Presence');
					break;
					// Default error case login unauthorized, attempt to register user
					default:
						Debugger.log('PRESENCE: default case - attempting to register',Debugger.MED,'handleGlobalEvent','Presence');
						attemptRegistration();
					break;
				}
			break;
			
			case "userUnavailable" :
				Debugger.log('PRESENCE: userUnavailable event',Debugger.MED,'handleGlobalEvent','Presence');
			break;
			
			case "userAvailable" :
				Debugger.log('PRESENCE: userAvailable event',Debugger.MED,'handleGlobalEvent','Presence');
			break;
		}
	}
	
	public function isPresenceExpanded():Boolean{
		return _presenceIsExpanded;
	}
	
	public function presenceFullHeight():Number{
		return presenceHeightFull;
	}
	
	public function showToolTip(btnObj, btnTT:String):Void{
		var Xpos = Application.HEADER_X+ 5;
		var Ypos = Application.HEADER_Y+( btnObj._y+btnObj._height)+2;
		var ttHolder = ApplicationParent.tooltip;
		var ttMessage = Dictionary.getValue(btnTT);
		var ttWidth = 150
		_tip.DisplayToolTip(ttHolder, ttMessage, Xpos, Ypos, undefined, ttWidth);
	}
	
	public function hideToolTip():Void{
		_tip.CloseToolTip();
	}
	
	private function setStyles(){
		Debugger.log('PRESENCE: setting styles',Debugger.MED,'setStyles','Presence');
		var styleObj = _tm.getStyleObject('smallLabel');
		
		styleObj = _tm.getStyleObject('textarea');
		presenceTitle_lbl.setStyle('styleName', styleObj);
		
		//For Panels
		styleObj = _tm.getStyleObject('BGPanel');
		presenceHead_pnl.setStyle('styleName',styleObj);
	}
	
	private function setLabels(){
		Debugger.log('PRESENCE: setting labels',Debugger.MED,'setLabels','Presence');
		
		presenceTitle_lbl.text = Dictionary.getValue('pres_panel_lbl');
		setStyles();
		delete this.onEnterFrame; 
		dispatchEvent({type:'load',target:this});
        
	}
	
	private function setupDataGrid(){
		Debugger.log('PRESENCE: setting up dataGrid',Debugger.MED,'setLabels','Presence');
		_users_dg.addEventListener("cellPress", Proxy.create(this, cellPress));
		_users_dg.columnNames = ["nickname"];
		
		var col:mx.controls.gridclasses.DataGridColumn;
		col = _users_dg.getColumnAt(0);
		col.headerText = "Learners:";
		col.width = _users_dg._width;

		_users_dg.vScrollPolicy = "auto";

		_users_dg.dataProvider = room;
		_users_dg.sortItemsBy("nickname");
	}

	private function cellPress(event) {
		//	I'm not clicking on an empty cell
		if (event.target.selectedItem.nickname != undefined) {
			//Double click action
			if (clickInterval != null && _users_dg.selectedItem.nickname == selectedChatItem.nickname) {
				Debugger.log('PRESENCE: double click event',Debugger.MED,'cellPress','Presence');
				
				//_debugDialog = PopUpManager.createPopUp(Application.root, LFWindow, false,{title:'Debug',closeButton:true,scrollContentPath:'debugDialog'});
				
				clearInterval(clickInterval);
				clickInterval = null;			// First click
			} else {
				clearInterval(clickInterval);
				clickInterval = null;
				selectedChatItem = _users_dg.selectedItem;
				lastClickX = _root._xmouse;
				lastClickY = _root._ymouse;
				clickInterval = setInterval(Proxy.create(this, endClickTimer), DOUBLECLICKSPEED);
			}
		}
	}
	
	private function endClickTimer() {
		// Do actions of first click
		Debugger.log('PRESENCE: single click event',Debugger.MED,'cellPress','Presence');
		clearInterval(clickInterval);
		//ajoutMenuChat(xK, yK);
		clickInterval = null;
	}
	
	function rejoinTimer() {
		// If not active, join
		if(!room.isActive()) {
			room.join();
		// If active, clear the interval
		} else{
			clearInterval(rejoinOnIdleInterval);
		}
	}

	public function resize(width:Number){
		panel._width = width;
		
	}
	
	function get className():String { 
        return 'Presence';
    }
}