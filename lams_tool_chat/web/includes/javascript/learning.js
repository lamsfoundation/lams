var sendChatToolMessage; // to be accessible from learning.jsp

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
	let selectedUser = null,
		websocket = initWebsocket('chat' + TOOL_SESSION_ID,
		APP_URL.replace('http', 'ws')
		+ 'learningWebsocket?toolSessionID=' + TOOL_SESSION_ID);

	if (websocket) {
		// when the server pushes new inputs
		websocket.onmessage = function (e) {
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
					'class' : 'messageFrom '+getPortraitColourClass(this.lamsUserId),
					'text'  : this.from
				}).appendTo(container);
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
					'class' : (value.nickName == selectedUser ? 'selected' : 'unselected'),
				})
					.appendTo(rosterDiv);

				var pictureDivId = 'roster-'+value.lamsUserId;
				$('<div />', {
					'id'    : pictureDivId
				}).appendTo(userDiv);
				addPortrait(jQuery('#'+pictureDivId), value.portraitId, value.lamsUserId, 'small', true, LAMS_URL);

				$('<span />', {
					'class' : getPortraitColourClass(value.lamsUserId),
					'text'  : value.nickName
				}).appendTo(userDiv);


				// only Monitor can send a personal message
				if (MODE == 'teacher') {
					userDiv.click(function(){
						userSelected($(this));
					});
				}
			});

			// reset ping timer
			websocketPing('chat' + TOOL_SESSION_ID, true);
		};
	}
	
	function userSelected(userDiv) {
		var userDivContent = userDiv.children().last().html().trim();
		// is Monitor clicked the already selectedd user, desect him and make message go to everyone
		selectedUser = userDivContent == selectedUser ? null : userDivContent;

		if (selectedUser) {
			sendToUserSpan.html(selectedUser);
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
		websocket.send(JSON.stringify(output));

		// reset ping timer
		websocketPing('chat' + TOOL_SESSION_ID, true);
	}
});