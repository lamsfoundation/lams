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
});

// for chat users to be indetified by different colours
var PALETTE = ["#008CD2", "#DF7C08", "#83B532", "#E0BE40", "#AE8124", "#5F0704", "#004272", "#CD322B", "#254806"],
	// only Monitor can send a personal message
	selectedUser = null,
	// init the connection with server using server URL but with different protocol
	chatToolWebsocket = new WebSocket(APP_URL.replace('http', 'ws') + 'learningWebsocket?toolSessionID=' + TOOL_SESSION_ID);

chatToolWebsocket.onmessage = function(e){
	
	//if messageDiv has been already initialized in $(document).ready(..) simply invoke onmessageHandler, otherwise run it with a timeout
	if (typeof messageDiv == 'undefined') {
		setTimeout(function() {
			onmessageHandler(e);
		}, 500);
	} else {
		onmessageHandler(e);
	}
	
}

chatToolWebsocket.onerror = function(e){
	alert("Error estabilishing connection to server: " + e.data);
}

function onmessageHandler(e) {
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

function sendChatToolMessage() {
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
	chatToolWebsocket.send(JSON.stringify(output)); 
}

function getColour(nick) {
	// same nick should give same colour
	var charSum = 0;
	for ( var i = 0; i < nick.length; i++) {
		charSum += nick.charCodeAt(i);
	}
	return PALETTE[charSum % (PALETTE.length)];
}