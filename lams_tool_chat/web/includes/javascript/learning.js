// for chat users to be indetified by different colours
var PALETTE = ["#008CD2", "#DF7C08", "#83B532", "#E0BE40", "#AE8124", "#5F0704", "#004272", "#CD322B", "#254806"];
// only Monitor can send a personal message
var selectedUser = null;
// last message in chat window
var lastMessageUid = null;
var pollInProgress = false;

function updateChat() {
	if (!pollInProgress) {
		// synchronise: if polling takes too long, don't try to do it again
		pollInProgress = true;
		$.ajax({
			  url      : LEARNING_ACTION,
			  data     : {'dispatch'       : 'getChatContent',
					  	  'toolSessionID'  : TOOL_SESSION_ID,
					  	  'lastMessageUid' : lastMessageUid
					  	 },
			  cache    : false,
			  dataType : 'json',
			  success  : handleUpdateChatResult,
			  complete : function(){
				  pollInProgress = false;
			  }
			});
	}
}

function handleUpdateChatResult(result) {
  if (result.lastMessageUid) {
	  messageDiv.html('');
	  // all messasges need to be written out, not only new ones,
	  // as old ones could have been edited or hidden by Monitor
	  
	  jQuery.each(result.messages, function(){
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
	  
	  lastMessageUid = result.lastMessageUid;
	  messageDiv.scrollTop(messageDiv.prop('scrollHeight'));
  }
  
  rosterDiv.html('');
  jQuery.each(result.roster, function(index, value){
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

function sendMessage() {
	var message = sendMessageArea.val();
	if (!message || message == '') {
		return false;  // do not send empty messages.
	}
	sendMessageArea.val('');
	
	// only Monitor can send a personal message
	var isPrivate = MODE == 'teacher' && selectedUser;
	
	$.ajax({
		  url     : LEARNING_ACTION,
		  data    : {'dispatch' : 'sendMessage',
				  	 'toolSessionID' : TOOL_SESSION_ID,
				  	 'message'  : isPrivate ? '[' + selectedUser + '] ' + message : message,
				  	 'user'     : isPrivate ? selectedUser : null
				  	},
		  cache   : false,
		  success : updateChat
		});
}

function getColour(nick) {
	// same nick should give same colour
	var charSum = 0;
	for ( var i = 0; i < nick.length; i++) {
		charSum += nick.charCodeAt(i);
	}
	return PALETTE[charSum % (PALETTE.length)];
}