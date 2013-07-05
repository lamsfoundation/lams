var windowHeight;
var pollInProgress = false;

var roster = {
	users : [],
	// map "tab" -> "last message in tab", so we don't fetch all messages every time, only new ones
	lastMessageUids : [],
	
	// when user clicked another user in roster
	handleUserClicked : function(index) {
		var nick = roster.users[index];
	
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
		this.users = users;

		// for all users
		jQuery.each(this.users, function(index, nick){
			var tag = nickToTag(nick);
			var listingName = tagToListing(tag);
			var listing = $("#" + listingName);
								
			// if no listing in roster exists
			if (listing.length == 0){
				// create listing div
				var listingDiv = $('<div id="' + listingName
						            + '" onClick="javascript:roster.handleUserClicked(' + index + ');" class="presenceListing">'
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
		presenceTabLabelDiv.html(labelUsers + " (" + this.users.length + ")");
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
	var tabLabelsLocal = $(".ui-tabs-label");
	var label = tabLabelsLocal[tabIndex].innerHTML;

	if (label == groupChatInfo.nick) {
		return groupChatInfo.nick;
	} else {
		for ( var i = 0; i < roster.users.length; i++) {
			if (roster.users[i] == label) {
				return roster.users[i];
			}
		}
	}

	return null;
}

function addTab(nick, tag) {
	// add a tab with the the nick specified
	presenceChatTabs.tabs('add', '#' + tag,
			createPrivateTabLabel(nick, tag));
	// add the content
	$("#" + tag).html(createPrivateTabContent(nick, tag));
}

function nickToTag(nick) {
	return nick == groupChatInfo.nick ? groupChatInfo.tag : nick.replace(/ /g, "_");
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
					  var messageArea = $("#" + (nickToMessageArea(from ? from : groupChatInfo.tag)));
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
		minMaxIcon.attr("src", lamsUrl + "images/icons/bullet_arrow_top.png")
		presenceShown = false;
	} else {
		presenceChat.animate({
			top : windowHeight - 270 + "px"
		}, 1000);
		minMaxIcon.attr("src", lamsUrl + "images/icons/bullet_arrow_bottom.png");
		presenceShown = true;
	}
}

function handleMessageInput(e, nick) {
	if (e.which == 13) {
		e.preventDefault();
		sendMessage(nick);
	}
}