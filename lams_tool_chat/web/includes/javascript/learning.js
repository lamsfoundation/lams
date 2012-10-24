// for chat users to be indetified by different colours
var PALETTE = ["#0000FF", "#006699", "#0066FF", "#6633FF", "#00CCFF", "#009900", "#00CC33", "#339900", "#008080", "#66FF66", "#CC6600", "#FF6600", "#FF9900", "#CC6633", "#FF9933", "#990000", "#A50021", "#990033", "#CC3300", "#FF6666", "#330033", "#663399", "#6633CC", "#660099", "#FF00FF", "#999900", "#808000", "#FF9FF2", "#666633", "#292929", "#666666"];
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
			}).css('color' , getColour(this.from));
		$('<div />',{
			'class' : 'messageFrom',
			'text'  : this.from
		  }).appendTo(container);
		$('<span />',{
			'text'  : this.body
		  }).appendTo(container);
		
		container.appendTo(messageDiv);
	  });
	  
	  lastMessageUid = result.lastMessageUid;
	  messageDiv.scrollTop(messageDiv.prop('scrollHeight'));
  }
  
  rosterDiv.html('');
  jQuery.each(result.roster, function(){
	var userDiv = $('<div />', {
		'class' : (this == selectedUser ? 'selected' : 'unselected'),
		'text'  : this
	}).css('color', getColour(this))
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