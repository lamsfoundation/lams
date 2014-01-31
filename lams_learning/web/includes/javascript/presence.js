var windowHeight;
var pollInProgress = false;

var roster = {
	// association nick -> localId, the latter being just some ID made in this script
	users : {},
	maxUserLocalId : 0,
	// map "tab" -> "last message in tab", so we don't fetch all messages every time, only new ones
	lastMessageUids : [],
	
	// when user clicked another user in roster
	handleUserClicked : function(localId) {
		var nick = null;
		$.each(roster.users, function(key, value){
			if (value == localId) {
				nick = key;
				return false;
			}
		});
	
		if(nick != nickname){
			var tag = nickToTag(nick);
			var tabLabel = tagToTabLabel(tag);
			var tab = $('#' + tabLabel);
			// if the clicked user's tab is open
			if(tab.length){
				// make the sender's tab label unbold
				tab.html(nick);
			} else {
				// if the clicked user's tab is not already open, create it and select it
				addTab(nick, tag);
			}
			
			// select the added tab
			presenceChatTabs.tabs('select' , tag);
		}
	},

	updateDisplay : function(users) {
		// sort alphabetically
		users.sort();
		// repopulate user objects array
		var rosterUsers = {};
		$.each(users, function(index, nick){
			var localId = roster.users[nick];
			rosterUsers[nick] = localId ? localId :  ++roster.maxUserLocalId;
		});
		this.users = rosterUsers;
		
		jQuery.each(this.users, function(nick, localId){
			var tag = nickToTag(nick);
			var listingName = tagToListing(tag);
			var listing = $("#" + listingName);
			// if no listing in roster exists
			if (listing.length == 0){
				// create listing div
				var listingDiv = $('<div id="' + listingName
						            + '" onClick="javascript:roster.handleUserClicked(' + localId + ');" class="presenceListing">'
						            + createPresenceListing(nick, tag)
						            + '</div>');
				
				// add the listing div
				rosterDiv.append(listingDiv);
			} else {
				// remove and append at the right place (from sort)
				rosterDiv.append(listing.remove());
			}
		});
		
		// update presenceTabLabel
		var presenceTabLabelDiv = $("#presence_tabLabel");
		presenceTabLabelDiv.html(labelUsers + " (" + users.length + ")");
	}
}

/* ******* HTML write Functions ******* */

function createPrivateTabLabel(nick, tag){
	return '<table border="0" cellpadding="5" cellspacing="0" class="tabLabelTable">' +
				'<tr>' +
					'<td><img class="smallImage" src="' + lamsUrl + 'images/icons/user_edit.png"/></td>' +
					'<td><div id="' + tag + '_tabLabel" class="ui-tabs-label" >' + nick + '</div></td>' +
					'<td><div><img onclick="javascript:handleCloseTab(\'' + nick + '\')" class="smallImage" src="' + lamsUrl + 'images/icons/cross.png" /></div></td>' +
				'</tr>' +
			'</table>';
}

function createPrivateTabContent(nick, tag){
	return '<div id="' + tag + '_messageArea" class="messageArea"></div><br />' +
			'<div id="' + tag + '_sendArea" class="sendArea">' + 
				'<input id="' + tag + '_messageInput" onkeydown="javascript:handleMessageInput(event, \'' + nick + '\')" type="text" class="messageInput">' +
				'<input type="button" value="' + labelSend + '" onclick="javascript:sendMessage(\'' + nick + '\')" class="sendButton">' +
			'</div>';
}

function createPresenceListing(nick, tag){
	return '<table border="0" cellpadding="5" cellspacing="0">' +
				'<tr>' +
					'<td width="10"><img id="'+ tag + '_listingImage" class="smallImage" src="' + lamsUrl + 'images/icons/user_online.png" /></td>' +
					'<td><div id="' + tag + '_listingNick">' + nick + '</div></td>' +
				'</tr>' +
			'</table>';
}

/* ******* Helper Functions ******* */
function generateMessageHTML(nick, message, date) {
	var fromElem = $('<div class="presenceMessageFrom">(' + date.substring(11, 19) + ') ' + nick + '</div>');
	var msgElem = $('<div class="presenceMessage">' + message + '</div>');
	
	return completeElem = $('<div />').append(fromElem).append(msgElem);
}

function resizeChat() {
	// refresh the window height
	windowHeight = $(window).height() - 30;

	// if presence is shown
	if (presenceShown) {
		// set presence chat to maximized height
		presenceChat.css({
			'top' : windowHeight - 270 + "px"
		});
	}
	// otherwise
	else {
		// set presence chat to minimized height
		presenceChat.css({
			'top' : windowHeight + "px"
		});
	}

	$("#presenceChatWarning").css({
		'top' : windowHeight - 10 + "px"
	});
}	

function getUserFromTabIndex(tabIndex) {
	return $(".ui-tabs-label")[tabIndex].innerHTML;
}

function addTab(nick, tag) {
	// add a tab with the the nick specified
	presenceChatTabs.tabs('add', '#' + tag,
			createPrivateTabLabel(nick, tag));
	// add the content
	$("#" + tag).html(createPrivateTabContent(nick, tag));
}

function nickToTag(nick) {
	return nick == groupChatInfo.nick ? groupChatInfo.tag : "presenceUser" + roster.users[nick];
}

function nickToMessageArea(nick) {
	return nickToTag(nick) + '_messageArea';
}

function tagToListing(tag) {
	return tag + '_listing';
}

function tagToTabLabel(tag) {
	return tag + '_tabLabel';
}

function getTime() {
	var currentTime = new Date();
	var hours = currentTime.getHours();
	var minutes = currentTime.getMinutes();
	var seconds = currentTime.getSeconds();

	if (hours < 10) {
		hours = "0" + hours;
	}

	if (minutes < 10) {
		minutes = "0" + minutes;
	}

	if (seconds < 10) {
		seconds = "0" + seconds;
	}

	return "(" + hours + ":" + minutes + ":" + seconds + ")";
}

/* ******* Main chat functions ******* */

function sendMessage(receiver) {
	var tag = nickToTag(receiver);
	var messageInput = $('#' + tag + '_messageInput');
	var message = messageInput.val();
	if (!message || message == '') {
		return false;  // do not send empty messages.
	}
	
	messageInput.val('');
	messageInput.focus();
	
	$.ajax({
		  url     : actionUrl,
		  data    : {'method'   : 'sendMessage',
			  		 'lessonID' : lessonId,
				  	 'from'     : nickname,
				  	 'to'       : tag == groupChatInfo.tag ? null : receiver,
				  	 'message'  : message
				  	},
		  cache   : false,
		  complete : updateChat
		});
}

function updateChat(){
	// skip another attempt if previous did not return yet (slow server?)
	if (!pollInProgress) {
		pollInProgress = true;
		var from = null;
		var selected = null;
		var lastMessageUid = null;
		var getMessages = presenceShown && presenceImEnabled;
		if (getMessages) {
			selected = presenceChatTabs.tabs('option','active');
			from = getUserFromTabIndex(selected);
			if (groupChatInfo.nick == from) {
				from = null;
			}
			lastMessageUid = roster.lastMessageUids[from ? from : 'group'];
		}

		$.ajax({
			  url      : actionUrl,
			  data     : {'method'          : 'getChatContent',
					  	  'lessonID'        : lessonId,
					  	  'getMessages'     : getMessages,
					  	  'lastMessageUid'  : lastMessageUid,
					  	  'to'              : nickname,
					  	  'from'            : from
					  	 },
			  cache    : false,
			  dataType : 'json',
			  complete : function(){
				  pollInProgress = false;
			  },
			  success  : function (result) {
				  roster.updateDisplay(result.roster);
				  
				  // real new messages for the opentab
				  if (result.messages) {
					  var messageArea = $("#" + (nickToMessageArea(from ? from : groupChatInfo.nick)));
					  var lastMessageUid = null;
					  jQuery.each(result.messages, function(){
						  messageArea.append(generateMessageHTML(this.from, this.message, this.dateSent));
						  lastMessageUid = this.uid;
					  });
					  // store last message uid and get new messages starting from this one
					  roster.lastMessageUids[from ? from : 'group'] = lastMessageUid;
					  messageArea.scrollTop(messageArea.prop('scrollHeight'));
				  }
				  
				  // check if other users wrote something new
				  if (result.newConversations) {
					  var selectedTabTag = nickToTag(getUserFromTabIndex(selected));
					  jQuery.each(result.newConversations, function(index, nick){
						  	var tag = nick == 'group' ? groupChatInfo.tag : nickToTag(nick);
							if (tag != selectedTabTag) {
								var tab = $("#" + tagToTabLabel(tag));
								if (tab.length == 0) {
									addTab(this, tag);
									tab = $("#" + tagToTabLabel(tag));
								}
								
								// notify of new message
								tab.addClass('presenceTabNewMessage');
								if (tag != groupChatInfo.tag) {
									$("#" + tagToListing(tag)).addClass('presenceListingNewMessage');
								}
							}
					  	});
				  }
			  }
		});
	}
}


/* ******* Click handlers ******* */


function handleCloseTab(label){
	var tabLabelsLocal = $(".ui-tabs-label");
	for (var i = 0; i < tabLabelsLocal.length; i++){
		if(tabLabelsLocal[i].innerHTML == label){
			presenceChatTabs.tabs('remove' , i);
			roster.lastMessageUids[label] = null;
		}
	}
}

function handleLeftScrollClick(){
	presenceChatTabs.tabs('scrollLeft');
}	

function handleRightScrollClick(){
	presenceChatTabs.tabs('scrollRight');
}

function handlePresenceClick() {
	if (presenceShown) {
		presenceChat.animate({
			top : windowHeight + "px"
		}, 1000);
		presenceShown = false;
	} else {
		presenceChat.animate({
			top : windowHeight - 270 + "px"
		}, 1000);
		presenceShown = true;
	}
}

function handleMessageInput(e, nick) {
	if (e.which == 13) {
		e.preventDefault();
		sendMessage(nick);
	}
}