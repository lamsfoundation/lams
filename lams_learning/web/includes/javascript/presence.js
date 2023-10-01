$(document).ready(function (){
	presenceChat = $("#presenceChat");
	rosterDiv = $("#presenceUserListings");
	$(window).resize(function() {
		resizeChat();
	});

	// if presence IM is enabled
	if (presenceEnabled) {
		// make visible
		presenceChat.removeClass("startHidden");
		
		// create chat tabs
		presenceChatTabs = $("#presenceChatTabs").tabs({
			'activate' : function(event, ui) {
				// remove visual indicators (underline) of new message
				var nick = getUserFromTabIndex(presenceChatTabs.tabs('option','active')),
					tag = nickToTag(nick),
					tabLabel = tagToTabLabel(tag);
				$("#" + tabLabel).removeClass('presenceTabNewMessage');
			}
		});
	}
	
	roster = {
		// association nick -> localId, the latter being just some ID made in this script
		users : {},
		maxUserLocalId : 0,
		lastMessageUids : {},
		
		// when user clicked another user in roster
		handleUserClicked : function(localId) {
			var nick = null;
			// find the nick based on the ID
			$.each(roster.users, function(key, value){
				if (value == localId) {
					nick = key;
					return false;
				}
			});
		
			if (nick != nickname){
				var tag = nickToTag(nick),
					tabLabel = tagToTabLabel(tag),
					tab = $('#' + tabLabel);
				// if the clicked learner's tab is open
				if (tab.length == 0){
					// if the clicked user's tab is not already open, create it and select it
					addTab(nick, tag);
				}
				// select the added tab
				presenceChatTabs.tabs( "option", "active", $('li', presenceChatTabs).length - 1);
			}
		},
	
		updateDisplay : function(users) {
			// repopulate user objects array
			var rosterUsers = {};
			$.each(users, function(index, nick){
				var localId = roster.users[nick];
				rosterUsers[nick] = localId ? localId :  ++roster.maxUserLocalId;
			});
			this.users = rosterUsers;
			
			// add HTML entries for each user
			rosterDiv.empty();
			jQuery.each(this.users, function(nick, localId){
				var tag = nickToTag(nick),
					listingName = tagToListing(tag),
					listing = $("#" + listingName);
				// if no listing in roster exists
				if (listing.length == 0){
					// create listing div
					var listingDiv = $('<div id="' + listingName
							            + '" onClick="javascript:roster.handleUserClicked(' 
							            + localId + ');" class="presenceListing bg-primary-hover">'
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
			$("#presenceUserCount").html(labelUsers + " (" + users.length + ")");
		}
	};

	initWebsocket('presence' + lessonId,
		APP_URL.replace('http', 'ws') + 'presenceChatWebsocket?lessonID=' + lessonId,
		function (e) {
			// create JSON object
			var input = JSON.parse(e.data);
			if (input.roster) {
				roster.updateDisplay(input.roster);
			}

			if (input.messages) {
				var activeNick = getUserFromTabIndex(presenceChatTabs.tabs('option','active')),
					selectedTabTag = nickToTag(activeNick);

				jQuery.each(input.messages, function(){
					// which tab are we talking about?
					var from = this.to ? (this.from == nickname ? this.to : this.from) : groupChatInfo.nick,
						lastMessageUid = roster.lastMessageUids[from] || 0;

					// are the messages new?
					if (this.uid > lastMessageUid) {
						var tag = nickToTag(from);
						if (tag != selectedTabTag) {
							var tab = $("#" + tagToTabLabel(tag));
							if (tab.length == 0) {
								// no tab opened yet, create it
								tab = addTab(from, tag);
							}

							// notify of new message
							tab.addClass('presenceTabNewMessage');
							if (tag != groupChatInfo.tag) {
								$("#" + tagToListing(tag)).addClass('presenceListingNewMessage');
							}
						}

						roster.lastMessageUids[from] = this.uid;
						var messageArea = $("#" + (nickToMessageArea(from)));
						messageArea.append(generateMessageHTML(this.from, this.message, this.dateSent));
						messageArea.scrollTop(messageArea.prop('scrollHeight'));
					}
				});
			}

			// remove conversation tabs with learners who are gone
			$('li a', presenceChatTabs).each(function() {
				var nick = $(this).text();
				if (nick != groupChatInfo.nick && !roster.users[nick]) {
					var tag = $(this).attr('href');
					$(tag).remove();
					$(this).parent().remove();
					presenceChatTabs.tabs('refresh');
				}
			});

			// reset ping timer
			websocketPing('presence' + lessonId, true);
		});
});


/* ******* HTML write Functions ******* */

function createPrivateTabLabel(nick, tag){
	return '<a href="#' + tag + '">' + nick + '</a><span class="ui-icon ui-icon-close" style="float: right" onclick="javascript:handleCloseTab(\'' + tag + '\')">Remove Tab</span>';
}

function createPrivateTabContent(nick, tag){
	return '<div id="' + tag + '_messageArea" class="messageArea"></div>' +
		   '<div id="' + tag + '_sendArea" class="sendArea">' + 
			  '<input id="' + tag + '_messageInput" onkeydown="javascript:handleMessageInput(event, \'' + nick + '\')" type="text" class="messageInput">' +
			  '<input type="button" value="' + labelSend + '" onclick="javascript:sendMessage(\'' + nick + '\')" class="sendButton">' +
		   '</div>';
}

function createPresenceListing(nick, tag){
	return '<div id="' + tag + '_listingNick">' + nick + '</div>';
}

/* ******* Helper Functions ******* */
function generateMessageHTML(nick, message, date) {
	var fromElem = $('<div class="presenceMessageFrom">(' + date.substring(11, 19) + ') ' + nick + '</div>'),
		msgElem = $('<div class="presenceMessage">' + message + '</div>');
	
	return completeElem = $('<div />').append(fromElem).append(msgElem);
}

function resizeChat() {
	var windowHeight = $(window).height();

	// if presence is shown
	if (presenceShown) {
		// set presence chat to maximized height
		presenceChat.css({
			'top' : windowHeight - 300 + "px"
		});
	}
	// otherwise
	else {
		// set presence chat to minimized height
		presenceChat.css({
			'top' : windowHeight - 23 + "px"
		});
	}
}	

function getUserFromTabIndex(tabIndex) {
	return tabIndex == 0 ? groupChatInfo.nick : $('li a', presenceChatTabs)[tabIndex].innerHTML;
}

// Adds a new tab and fetches conversation history
function addTab(nick, tag) {
	var tab = $('<li />').attr('id', tag + '_tabLabel').html(createPrivateTabLabel(nick, tag)).appendTo($('ul', presenceChatTabs));
	$('<div />').attr('id', tag).html(createPrivateTabContent(nick, tag)).appendTo(presenceChatTabs);
	presenceChatTabs.tabs('refresh');
	
	// fetch all messages from the start
	roster.lastMessageUids[nick] = null;
	var data = {
		 'type'     : 'fetchConversation',
		 'to'       : nick
		};
	sendToWebsocket('presence' + lessonId, JSON.stringify(data));

	return tab;
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
	var currentTime = new Date(),
		hours = currentTime.getHours(),
		minutes = currentTime.getMinutes(),
		seconds = currentTime.getSeconds();

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
	var tag = nickToTag(receiver),
		messageInput = $('#' + tag + '_messageInput'),
		message = messageInput.val();
	if (!message || message == '') {
		return false;  // do not send empty messages.
	}
	
	messageInput.val('');
	messageInput.focus();
	
	var data = {
		 'type'     : 'message',
		 'to'       : tag == groupChatInfo.tag ? '' : receiver,
		 'message'  : message
		};
	sendToWebsocket('presence' + lessonId, JSON.stringify(data));

	// reset ping timer
	websocketPing('presence' + lessonId, true);
}

/* ******* Click handlers ******* */

function handleCloseTab(tag){
	$('#' + tag + '_tabLabel').remove();
	$('#' + tag).remove();
	presenceChatTabs.tabs('refresh');
}

function handlePresenceClick() {
	var windowHeight = $(window).height();
	if (presenceShown) {
		presenceChat.animate({
			top : windowHeight - 23 + "px"
		}, 1000);
		presenceShown = false;
	} else {
		presenceChat.animate({
			top : windowHeight - 300 + "px"
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