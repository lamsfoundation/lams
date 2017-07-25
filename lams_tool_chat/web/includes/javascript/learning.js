// for chat users to be indetified by different colours
var PALETTE = ["#008CD2", "#DF7C08", "#83B532", "#E0BE40", "#AE8124", "#5F0704", "#004272", "#CD322B", "#254806"],
	// to be accessible from learning.jsp
	sendChatToolMessage;

$(document).ready(function() {
	messageDiv = $("#messages");
	rosterDiv = $("#roster");
	sendToUserSpan = $('#sendToUser');
	sendToEveryoneSpan = $('#sendToEveryone');
	sendMessageArea = $('#sendMessageArea');
	sendMessageButton = $('#sendMessageButton');

	// react to Enter key
	sendMessageArea.keydown(function(e) {
		if (e.which == 13) {
			e.preventDefault();
			sendChatToolMessage();
		}
	});
	
	// only Monitor can send a personal message
	var selectedUser = null,
		chatWebsocketInitTime = Date.now(),
		// init the connection with server using server URL but with different protocol
		chatWebsocket = new WebSocket(APP_URL.replace('http', 'ws')
				+ 'learningWebsocket?toolSessionID=' + TOOL_SESSION_ID),
		chatWebsocketPingTimeout = null,
		chatWebsocketPingFunc = null;
	
	chatWebsocketPingFunc = function(skipPing){
		if (chatWebsocket.readyState == chatWebsocket.CLOSING 
				|| chatWebsocket.readyState == chatWebsocket.CLOSED){
			if (Date.now() - chatWebsocketInitTime < 1000) {
				return;
			}
			location.reload();
		}
		
		// check and ping every 3 minutes
		chatWebsocketPingTimeout = setTimeout(chatWebsocketPingFunc, 3*60*1000);
		// initial set up does not send ping
		if (!skipPing) {
			chatWebsocket.send("ping");
		}
	};
	// set up timer for the first time
	chatWebsocketPingFunc(true);
	
	chatWebsocket.onclose = function(e){
		// react only on abnormal close
		if (e.code === 1006 &&
			Date.now() - chatWebsocketInitTime > 1000) {
			location.reload();
		}
	};

	chatWebsocket.onmessage = function(e){
		// reset ping timer
		clearTimeout(chatWebsocketPingTimeout);
		chatWebsocketPingFunc(true);
		
		// create JSON object
		var input = JSON.parse(e.data);
		// clear old messages
		messageDiv.html('');
		
		// all messasges need to be written out, not only new ones,
		// as old ones could have been edited or hidden by Monitor
		jQuery.each(input.messages, function(){
			var container = $('<div />',{
				'class' : 'message ' + (this.type == 'chat' ? 'private_message' : '')
			});
			$('<div />',{
				'class' : 'messageFrom',
				'text'  : this.from
			}).css('color' , getColour(this.from)).appendTo(container);
			$('<span />',{
				'text'  : this.body
			}).appendTo(container);
				
			container.appendTo(messageDiv);
		});
	  
		// move to the bottom
		messageDiv.scrollTop(messageDiv.prop('scrollHeight'));
		rosterDiv.html('');
		jQuery.each(input.roster, function(index, value){
			var userDiv = $('<div />', {
				'class' : (value == selectedUser ? 'selected' : 'unselected'),
				'text'  : value
			}).css('color', getColour(value))
			  .appendTo(rosterDiv);
				
				// only Monitor can send a personal message
			if (MODE == 'teacher') {
				userDiv.click(function(){
					userSelected($(this));
				});
			}
		});
		
	}
	
	function userSelected(userDiv) {
		var userDivContent = userDiv.html();
		// is Monitor clicked the already selectedd user, desect him and make message go to everyone
		selectedUser = userDivContent == selectedUser ? null : userDivContent;

		if (selectedUser) {
			sendToUserSpan.html(selectedUser)
			              .css('color', getColour(selectedUser));
		}
		sendToEveryoneSpan.css('display', selectedUser ? 'none' : 'inline');
		sendToUserSpan.css('display', selectedUser ? 'inline' : 'none');
		$('.selected').attr('class', 'unselected');
		userDiv.attr('class', selectedUser ? 'selected' : 'unselected');
	}

	sendChatToolMessage = function() {
		var message = sendMessageArea.val();
		if (!message || message == '') {
			return false;  // do not send empty messages.
		}
		sendMessageArea.val('');
		
		// only Monitor can send a personal message
		var isPrivate = MODE == 'teacher' && selectedUser,
			output = {
				 'toolSessionID' : TOOL_SESSION_ID,
			  	 'toUser'   : isPrivate ? selectedUser : '',
			  	 'message'  : isPrivate ? '[' + selectedUser + '] ' + message : message
			};
		
		// send it to server
		chatWebsocket.send(JSON.stringify(output));
		
		// reset ping timer
		clearTimeout(chatWebsocketPingTimeout);
		chatWebsocketPingFunc(true);
	}
});

function getColour(nick) {
	// same nick should give same colour
	var charSum = 0;
	for ( var i = 0; i < nick.length; i++) {
		charSum += nick.charCodeAt(i);
	}
	return PALETTE[charSum % (PALETTE.length)];
}