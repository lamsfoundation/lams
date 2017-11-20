"use strict"

var kumaliveWebsocket = new WebSocket(LEARNING_URL.replace('http', 'ws') 
		+ 'kumaliveWebsocket?organisationID=' + orgId + '&role=' +role),
	kumaliveWebsocketPingTimeout = null,
	kumaliveWebsocketPingFunc = null,
	// is the user a learenr or a teacher
	roleTeacher = false,
	// was the initial set up run
	initialised = false,
	// is a refresh already running, so next messages need to wait
	refreshing = false,
	// refresh message awaiting processing
	queuedMessage = null,
	// is there a learner speaking right now
	speakerId = null,
	// rubrics to evaluate speaker
	rubrics = null,
	// is a poll running now and what ID
	pollId = null,
	// answers will be numbered 
	pollAnswerBullets = 'abcdefghij',
	// index of user icon colour currently used
	learnerColorIndex = 1,
	// template of a HTML structure of a learner
	learnerDivTemplate = $('<div />').addClass('learner changing')
		.append($('<div />').addClass('profilePicture profilePictureHidden'))
		.append($('<div />').addClass('name')),
	REFRESH_DELAY = 1000,
	ANIMATION_DURATION = 1000,
	PING_DELAY = 3*60*1000;

kumaliveWebsocketPingFunc = function(skipPing){
	if (kumaliveWebsocket.readyState == kumaliveWebsocket.CLOSING 
			|| kumaliveWebsocket.readyState == kumaliveWebsocket.CLOSED){
		location.reload();
	}
	
	// check and ping every few minutes
	kumaliveWebsocketPingTimeout = setTimeout(kumaliveWebsocketPingFunc, PING_DELAY);
	// initial set up does not send ping
	if (!skipPing) {
		kumaliveWebsocket.send("ping");
	}
};
// set up timer for the first time
kumaliveWebsocketPingFunc(true);

kumaliveWebsocket.onclose = function(e){
	// react only on abnormal close
	if (e.code === 1006) {
		location.reload();
	}
};
// when the server pushes new messages and roster to the learner's browser


/**
 * Fetches existing Kumalive session
 */
kumaliveWebsocket.onopen = function(e) {
	kumaliveWebsocket.send(JSON.stringify({
		'type' : 'start'
	}));
};

/**
 * Display information to an user when he gets disconnected
 */
kumaliveWebsocket.onclose = function(e){
	$('body > *').hide();
	$('#closedDiv').show();
};

/**
 * Process a message from server.
 */
kumaliveWebsocket.onmessage = function(e){
	 // reset ping timer
    clearTimeout(kumaliveWebsocketPingTimeout);
    kumaliveWebsocketPingFunc(true);
    
	// read JSON object
	var message = JSON.parse(e.data),
		type = message.type;
	// check what is this message about
	switch(type) {
		case 'start' : {
			// user tried to join a Kumalive which is not started yet
			// try to start it, if user is a teacher
			// otherwise just wait for a teacher
			kumaliveWebsocket.send(JSON.stringify({
				'type' : 'start',
				'role' : role
			}));
		}
		break;
		case 'create' : {
			// user is a teacher and will now create a new Kumalive
			
			// hide splash screen
			$('#initDiv').hide();
			// show name input
			var createDiv = $('#createKumaliveDiv'),
				rubricsDiv = $('#rubrics .panel-body', createDiv);
			if (message.rubrics) {
				$.each(message.rubrics, function(){
					if (this) {
						var checkbox = $('<div />').addClass('checkbox').appendTo(rubricsDiv),
							label = $('<label />').appendTo(checkbox);
						$('<span />').text(this).appendTo(label);
						$('<input />').attr('type', 'checkbox')
									  .prop('checked', true)
									  .prependTo(label);
					}
				});
			}
			// do not show the box at all if there are no rubrics
			// (exactly: there is the single default blank one)
			if ($('.checkbox', rubricsDiv).length == 0){
				rubricsDiv.remove();
			}
			
			var createButton = createDiv.show().children('button').click(create).prop('disabled', true);
			createDiv.children('input').focus().keyup(function(){
				// name can not be empty
				createButton.prop('disabled', !$(this).val());
			})
		}
		break;
		case 'join' : {
			// server tell user to join Kumalive, so user obeys
			kumaliveWebsocket.send(JSON.stringify({
				'type' : 'join'
			}));
		}
		break;
		case 'init' : {
			if (!initialised) {
				// it is the first refresh message ever
				init(message);
			}
		}
		break;
		case 'refresh': {
			if (refreshing) {
				// set current message as the next one to be processed
				queuedMessage = message;
			} else {
				// no refresh is running, so process current message
				processRefresh(message);
			}
		}
		break;
		case 'finish' : {
			// tell user that Kumalive is finished and close the window
			window.alert(LABELS.FINISH_KUMALIVE_MESSAGE);
			window.close();
		}
		break;
	} 
};


/**
 * Initialise basic Kumalive information when first refresh message arrives
 */
function init(message) {
	initialised = true;
	roleTeacher = message.isTeacher && message.roleTeacher;
	
	// hide all buttons and enable ones appropriate for the role
	$('#actionCell button').hide();
	if (roleTeacher) {
		$('#raiseHandPromptButton').click(raiseHandPrompt);
		$('#downHandPromptButton').click(downHandPrompt);
		$('#score i').click(score);
		$('#pollButton').click(setupPoll).show();
		$('#pollSetupAnswer').change(function(){
			if ($('#pollSetupAnswer option:selected').val() === 'custom') {
				$('#pollSetupAnswerCustom').show();
			} else {
				$('#pollSetupAnswerCustom').hide();
			}
		});
		$('#pollSetupCancelButton').click(setupPollCancel);
		$('#pollSetupStartButton').click(startPoll);
		$('#pollRunFinishButton').click(finishPoll);
		$('#pollRunCloseButton').click(closePoll);
		$('#finishButton').click(finish).show();
	} else {
		$('#raiseHandButton').click(raiseHand);
		$('#downHandButton').click(downHand);
		$('#pollRunVoteButton').click(votePoll);
	}
	
	// set dialog name
	$('head title').text(LABELS.KUMALIVE_TITLE + ' ' + message.name);
	// set teacher portrait and name
	addPortrait($('#actionCell #teacher .profilePicture'), message.teacherPortraitUuid, 
			message.teacherId, "large", true, LAMS_URL);
	$('#teacher .name').text(message.teacherName);
	
	rubrics = message.rubrics;
	
	// show proper work screen
	$('#initDiv').hide();
	$('#mainDiv').show();
}

/**
 * Main function for processing refresh messages
 */
function processRefresh(message) {
	// block other refresh messages from running until this one is processed
	refreshing = true;

	// if an element is now being changed and it takes a while,
	// try processing the same message again after a second
	var repeat = toggleRaiseHandPrompt(message);
	repeat |= processParticipants(message);
	repeat |= processRaisedHand(message);
	repeat |= toggleSpeak(message);
	processPoll(message);

	if (repeat || queuedMessage) {
		setTimeout(function() {
			// get the newest message
			var nextMessage = queuedMessage || message;
			queuedMessage = null;
			processRefresh(nextMessage);
		}, REFRESH_DELAY);
	} else {
		refreshing = false;
	}
}

/**
 * Show whether a question is currently asked
 */
function toggleRaiseHandPrompt(message) {
	var raiseHandPrompt = $('#raiseHandPrompt');
	if (message.raiseHandPrompt) {
		if (roleTeacher) {
			// show button for finishing the question
			$('#downHandPromptButton').show();
		}
		if (!message.speaker) {
			// no learner is currently speaking, so show "hand up" icon
			$('#teacher').slideUp(function(){
				raiseHandPrompt.slideDown();
			});
		} 
	} else if (!message.speaker) {
		if (roleTeacher){
			// allow teacher to ask a question
			$('#raiseHandPromptButton').show();
		}
		
		$('.score[userId]').slideUp(function() {
			$(this).remove();
		});
		// no question is asked at the moment
		raiseHandPrompt.slideUp(function(){
			$('#teacher').slideDown();
		});
	}
}

/**
 * Add/removes current learners
 */
function processParticipants(message) {
	var learnersContainer = $('#learnersContainer'),
		currentLearnerIds = [],
		// should refresh be repeated?
		result = false;
	$.each(message.learners, function(index, learner){
		if (learner.roleTeacher) {
			// do not add teachers to learners container
			return true;
		}
		currentLearnerIds.push(+learner.id);
		
		// check if a learner already exists
		var learnerDiv = $('.learner[userId="' + learner.id + '"]', learnersContainer);
		if (learnerDiv.length > 0) {
			if (learnerDiv.is('.changing')) {
				// maybe he exists, but is fading out? See in the next run
				result = true;
			}
			return true;
		}
		
		// build a new learner
		learnerDiv = learnerDivTemplate.clone()
									   .attr('userId', learner.id)
									   .appendTo(learnersContainer);
		var profilePicture = $('.profilePicture', learnerDiv);
		// use profile picture or a coloured icon
		addPortrait(profilePicture, learner.portraitUuid, learner.id, "large", true, LAMS_URL);
		$('.name', learnerDiv).text(learner.firstName + ' ' + learner.lastName);
		
		if (roleTeacher) {
			// teacher can see logins and chooses who speaks
			learnerDiv.attr('title', message.logins['user' + learner.id])
					  .css('cursor', 'pointer')
					  .click(speak);
		}
		learnerFadeIn(learnerDiv);
	});
	// remove learners who left
	$('.learner', learnersContainer).each(function(){
		var learnerDiv = $(this),
			userId = +learnerDiv.attr('userId');
		if (currentLearnerIds.indexOf(userId) < 0) {
			// remove both from learners container and "raised hand" container
			learnerFadeOut(learnerDiv);
			learnerFadeOut($('#raiseHandContainer .learner[userId="' + userId + '"]'));
		}
	});
	
	return result;
}

/**
 * Display current poll results
 */
function processPoll(message) {
	var poll = message.poll;
	// is there a poll running now?
	if (!poll) {
		if (pollId) {
			// there is no poll anymore, i.e. current poll was closed
			pollId = null;
			// close the panel
			$('#pollCell').hide();
			$('#pollButton').prop('disabled', false);
		}
		return;
	}
	
	// open panel if closed
	$('#pollCell, #pollRun').show();
	
	// init poll fields or make them read only after voting
	if (poll.id != pollId || (poll.finished && $('#pollRunVoteButton').is(':visible'))) {
		initPoll(poll);
	}
	if (poll.votes) {
		// show votes if user is teacher or votes were released
		$.each(poll.votes, function(index, count) {
			var answerElement = $('#pollAnswer' + index),
				badge = $('.badge', answerElement);
			// missing badge means that votes were made available just now
			if (badge.length === 0) {
				badge = $('<span />').addClass('badge').appendTo(answerElement);
			}
			badge.text(count);
		});
	}
	if (poll.voted != null) {
		// highlight the answer user voted for
		$('#pollAnswer' + poll.voted).addClass('active');
	}
}

/**
 * Add/remove learners who raised hand
 */
function processRaisedHand(message) {
	var raiseHandContainer = $('#raiseHandContainer'),
		// should refresh be repeated?
		result = false,
		raisedHand = false;
	
	// are there any learners who raised hand?
	if (message.raisedHand) {
		// remove learners who raised hand before and now they put it down
		$('.learner', raiseHandContainer).each(function(){
			var raisedHandDiv = $(this),
				learnerId = +raisedHandDiv.attr('userId');
			if (message.raisedHand.indexOf(learnerId) < 0) {
				learnerFadeOut(raisedHandDiv);
			}
		});
		
		// add learners who raised hand
		$.each(message.raisedHand, function() {
			// if this user has raised hand, set buttons properly
			if (userId == this) {
				raisedHand = true;
			}
			// if the given user has already raised hand, do nothing
			var	raisedHandDiv = $('.learner[userId="' + this + '"]', raiseHandContainer);
			if (raisedHandDiv.length > 0) {
				return true;
			}
			
			var learnerDiv = $('#learnersContainer .learner[userId="' + this + '"]');
			if (learnerDiv.hasClass('changing')){
				result = true;
				return true;
			}
			
			// create a new raised hand learner
			var targetLearnerDiv = learnerDiv.addClass('changing').clone(true).css({
					'visibility' : 'hidden'
				}).appendTo(raiseHandContainer);
			
			raiseHandContainer.slideDown(function(){
				// animate learner's profile picture
				var targetOffset = $('.profilePicture', targetLearnerDiv).offset(),
					profilePicture = $('.profilePicture', learnerDiv),
					transitionCopy = profilePicture.clone()
						.css({
							'position' : 'fixed'
						})
						.appendTo('body')
						.offset(profilePicture.offset())
				        .animate({
					    	'left' : targetOffset.left,
					    	'top' : targetOffset.top
					    }, ANIMATION_DURATION, function(){
					    	targetLearnerDiv.css('visibility', 'visible');
					    	transitionCopy.remove();
					    	learnerDiv.removeClass('changing');
					    	targetLearnerDiv.removeClass('changing');
					    });
			});
		});
	} else {
		 // hide raised hand container if no learner raised hand
		 raiseHandContainer.slideUp(function() {
			 raiseHandContainer.children('.learner').remove();
		 });
	}
	
	// show buttons for raising/putting down hand
	if (!roleTeacher) {
		if (raisedHand) {
			$('#raiseHandButton').hide();
			$('#downHandButton').show();
		} else {
			$('#raiseHandButton').show();
			$('#downHandButton').hide();
		}
	}
	
	return result;
}

/**
 * Set current learner speaker
 */
function toggleSpeak(message) {
	if (message.speaker) {
		speakerId = message.speaker;
		
		var learnerDiv = $('#raiseHandContainer .learner[userId="' + speakerId + '"]');
		if (learnerDiv.length == 0) {
			learnerDiv =  $('#learnersContainer .learner[userId="' + speakerId + '"]');
		}
		// if current learner is in a process of raising hand,
		// run the refresh again and only then set him as a speaker
		if (learnerDiv.hasClass('changing')) {
			return true;
		}
	}
	
	var speaker = $('#actionCell .speaker').not('#teacher');
	if (!message.speaker) {
		if (speaker.length > 0) {
			speaker.slideUp(function(){
				// no speaker anymore
				// show scoring buttons for a teacher
				speaker.remove();
				if (roleTeacher) {
					if ($('#actionCell .score[userId="' + speakerId + '"]').length == 0) {
						// create a score panel for each rubric
						var batch = Math.floor(new Date().getTime() / 1000);
						$.each(rubrics, function(){
							$('#score').clone(true).attr({
								'id'       : null,
								'userId'   : speakerId,
								'rubricId' : this.id,
								'batch'    : batch
							}).appendTo('#actionCell')
							  .slideDown()
							  // user name and rubric
							  .find('p').html('<strong>' + $('#learnersContainer .learner[userId="' + speakerId + '"] .name').text() + '</strong>'
										+ (this.name ? '<br />' + this.name : ''));
						});
					}
					speakerId = null;
				} else if (message.raiseHandPrompt) {
					$('#raiseHandPrompt').slideDown();
				} else {
					$('#teacher').slideDown();
				}
			});
		}
		return;
	}
	
	if (speaker.length > 0){
		if (speaker.attr('userId') == message.speaker) {
			return;
		}
		speaker.remove();
	}
	
	// prepare room for speaker
	$('#teacher').slideUp();
	$('#raiseHandPrompt').slideUp(function(){
		speaker = $('<div />').addClass('speaker')
			.attr('userId', speakerId)
			.css({
				'margin-top' : '20px',
				'visibility' : 'hidden'
			})
			.prependTo('#actionCell');
		
		// create speaker HTML element
		$('.name', learnerDiv).clone().appendTo(speaker);
		if (roleTeacher) {
			$('<button />').addClass('btn btn-default').click(stopSpeak).text(LABELS.SPEAK_FINISH).appendTo(speaker);
		}
		
		var targetProfilePicture = $('.profilePicture', learnerDiv)
				.clone()
				.prependTo(speaker),
			targetOffset = targetProfilePicture.offset(),
			targetWidth = targetProfilePicture.width(),
			targetHeight = targetProfilePicture.height(),
			profilePicture = $('.profilePicture', learnerDiv),
			transitionCopy = profilePicture.clone().appendTo('body')
				.css({
					'position' : 'fixed'
				})
				.offset(profilePicture.offset())
				// animate moving speaker from learners to speaker panel
		        .animate({
			    	'left'      : targetOffset.left + targetWidth / 4,
			    	'top'       : targetOffset.top + targetHeight / 4
			    }, ANIMATION_DURATION, function(){
			    		speaker.css('visibility', 'visible');
			    		transitionCopy.remove();
			    });
	});
}

/**
 * Animate learner arrival
 */
function learnerFadeIn(learnerDiv) {
	var nameDiv = $('.name', learnerDiv);
	learnerDiv.css('display', 'inline-block');

	$('.profilePicture', learnerDiv).switchClass('profilePictureHidden', 'profilePictureShown', ANIMATION_DURATION, function(){
		$(this).removeClass('profilePictureShown');
		nameDiv.css('color', 'initial');
		learnerDiv.removeClass('changing');
	});
}

/**
 * Animate learner departure
 */
function learnerFadeOut(learnerDiv) {
	if (learnerDiv.length == 0) {
		return;
	}
	learnerDiv.addClass('changing');
	var nameDiv = $('.name', learnerDiv).css('color', 'red');

	$('.profilePicture', learnerDiv).switchClass('profilePictureShown', 'profilePictureHidden', ANIMATION_DURATION, function(){
		nameDiv.remove();
		learnerDiv.animate({
			'width' : 'toggle'
		}, ANIMATION_DURATION, function(){
			learnerDiv.remove();
		});
	});
}

function raiseHandPrompt() {
	kumaliveWebsocket.send(JSON.stringify({
		'type' : 'raiseHandPrompt'
	}));
}

function downHandPrompt() {
	kumaliveWebsocket.send(JSON.stringify({
		'type' : 'downHandPrompt'
	}));
}

function raiseHand() {
	kumaliveWebsocket.send(JSON.stringify({
		'type' : 'raiseHand'
	}));
}

function downHand() {
	kumaliveWebsocket.send(JSON.stringify({
		'type' : 'downHand'
	}));
}

/**
 * Set a learner as a speaker
 */
function speak() {
	if (!$('#raiseHandPrompt').is(':visible')) {
		return;
	}
	var speakerId = $(this).attr('userId');
	// the learner did not raise a hand; is the teacher sure to set him as a speaker?
	if ($('#raiseHandContainer .learner[userId="' + speakerId + '"]').length == 0 
			&& !confirm(LABELS.SPEAK_CONFIRM)){
		return;
	}
	
	kumaliveWebsocket.send(JSON.stringify({
		'type' : 'speak',
		'speaker' : speakerId
	}));
}

function stopSpeak() {
	kumaliveWebsocket.send(JSON.stringify({
		'type' : 'speak'
	}));
}

/**
 * Show scoring buttons
 */
function score(){
	var button = $(this),
		container = button.closest('.score'),
		score = null;
	if (button.is('.scoreGood')) {
		score = 2;
	} else if (button.is('.scoreNeutral')) {
		score = 1;
	} else if (button.is('.scoreBad')) {
		score = 0;
	} 
	
	if (score !== null) {
		kumaliveWebsocket.send(JSON.stringify({
			'type'     : 'score',
			'userID'   : container.attr('userId'),
			'rubricId' : container.attr('rubricId'),
			'batch'    : container.attr('batch'),
			'score'    : score
		}));
		
		container.slideUp(function(){
			$(this).remove();
		});
	}
}

/**
 * Show form where teacher can build poll
 */
function setupPoll() {
	$('#pollButton').prop('disabled', true);
	$('#pollRun').hide();
	// reset form inputs
	$('#pollSetup input').val(null);
	$('#pollSetup select option:first-child').prop('selected', true);
	$('#pollSetupAnswerCustom').hide();
	$('#pollCell, #pollSetup').show();
	$('#pollSetupQuestion').focus();
}

/**
 * Cancel poll building
 */
function setupPollCancel() {
	$('#pollSetup').hide();
	if (pollId) {
		$('#pollRun').show();
	} else {
		$('#pollCell').hide();
		$('#pollButton').prop('disabled', false);
	}
}

/**
 * Create poll widgets: answer list, radio buttons etc.
 */
function initPoll(poll) {
	pollId = poll.id;
	$('#pollRun button').hide();
	$('#pollRunQuestion').text(poll.name);
	var radioList = $('#pollRunAnswerRadios').empty(),
		answerList = $('#pollRunAnswerList').empty();

	// teachers can't vote; learner can't vote twice; learner can't vote for finished poll
	if (roleTeacher || (poll.voted != null) || poll.finished) {
		// build simple list of answers
		$.each(poll.answers, function(index, answer){
			var answerElement = $('<li />').addClass('list-group-item').attr('id', 'pollAnswer' + index)
										   .text(pollAnswerBullets[index] + ') ' + answer)
										   .appendTo(answerList);
			if (poll.votes) {
				// show votes if user is teacher or votes were released
				$('<span />').addClass('badge').text(poll.votes[index]).appendTo(answerElement);
			}
		});
		$('#pollRunAnswerList').show();
		// extra options for teacher
	
		if (roleTeacher) {
			if (poll.finished) {
				$('#pollRunCloseButton').show();
			} else {
				$('#pollRunFinishButton').show();
			}
		}
	} else {
		// learner can vote, build radio buttons
		$.each(poll.answers, function(index, answer){
			$('#pollRunAnswerRadioTemplate').clone().attr('id', null).appendTo(radioList)
				.find('label').append($('<span />').text(pollAnswerBullets[index] + ') ' + answer))
				.find('input').val(index);
		});
		$('#pollRunVoteButton').show();
	}
}

/**
 * Create a poll with parameters set up in form
 */
function startPoll(){
	var question = $('#pollSetupQuestion').val(),
		poll = {};
	// validation
	if (question) {
		$('#pollSetupQuestionGroup').removeClass('has-error');
		poll.name = question;
	} else {
		$('#pollSetupQuestionGroup').addClass('has-error');
	}
	
	var selectedOption = $('#pollSetupAnswer option:selected');
	if (selectedOption.val() === 'custom'){
		$('#pollSetupAnswerCustomParseError, #pollSetupAnswerCustomCountError').hide();
		var answerString = $('#pollSetupAnswerCustom').val();
		// check if brackets are closed and there is nothing between them, for example "{aaa} {bb" or "{aaa} bb {ccc}"
		if (answerString) {
			var index = -1,
				indexEnd = -1,
				answers = [];
			do {
				// find opening bracket
				index = answerString.indexOf('{', index + 1);
				if (index >= 0) {
					// is there anything other than whitespace between } and {
					if (answerString.substring(indexEnd + 1, index).trim()) {
						answers = [];
						break;
					}
					// is there a matching }
					indexEnd = answerString.indexOf('}', index + 1);
					if (indexEnd < 0) {
						answers = [];
						break;
					}
					var answer = answerString.substring(index + 1, indexEnd);
					// is the answer not empty, i.e. {   }
					if (answer.trim()) {
						answers.push(answer);
					} else {
						answers = [];
						break;
					}
				} else if (indexEnd && answerString.substring(indexEnd + 1).trim()) {
					// is there anything after last }
					answers = [];
					break;
				}
			} while (index >= 0);
			if (answers.length === 0) {
				$('#pollSetupAnswerCustomGroup').addClass('has-error');
				$('#pollSetupAnswerCustomParseError').show();
			} else if (answers.length > 10) {
				$('#pollSetupAnswerCustomGroup').addClass('has-error');
				$('#pollSetupAnswerCustomCountError').show();
			} else {
				$('#pollSetupAnswerCustomGroup').removeClass('has-error');
				poll.answers = answers;
			}
		} else {
			$('#pollSetupAnswerCustomGroup').addClass('has-error');
		}
	} else {
		// parse simple options, for example "True, False"
		var answers = [];
		$.each(selectedOption.text().split(','), function() {
			answers.push(this.trim());
		});
		poll.answers = answers;
	}
	
	// there were errors, do not carry on
	if (!poll.name || !poll.answers) {
		return;
	}

	$('#pollSetup').hide();
	kumaliveWebsocket.send(JSON.stringify({
		'type' : 'startPoll',
		'poll' : poll
	}));
}

/**
 * Send vote to the server
 */ 
function votePoll() {
	var checkedAnswer = $('#pollRunAnswerRadios input[name="pollAnswer"]:checked');
	if (checkedAnswer.length !== 1) {
		return;
	}
	pollId = null;
	kumaliveWebsocket.send(JSON.stringify({
		'type' 		  : 'votePoll',
		'answerIndex' : checkedAnswer.val()
	}));
}

/**
 * Prevent learners from voting
 */
function finishPoll() {
	if (!confirm(LABELS.POLL_FINISH_CONFIRM)) {
		return;
	}
	kumaliveWebsocket.send(JSON.stringify({
		'type' : 'finishPoll',
		'pollId' : pollId
	}));
	$('#pollRunFinishButton').hide();
	$('#pollRunCloseButton').show();
}

/**
 * Hide poll for everyone
 */
function closePoll() {
	kumaliveWebsocket.send(JSON.stringify({
		'type' : 'closePoll'
	}));
}

/**
 * Create a new Kumalive
 */
function create(){
	var createDiv = $('#createKumaliveDiv').hide(), 
		name = $('input', createDiv).val(),
		rubrics = [];
	// find checked rubrics and prepare them for serialization
	$('#rubrics input:checked', createDiv).each(function(){
		rubrics.push($(this).siblings('span').text());
	});
	kumaliveWebsocket.send(JSON.stringify({
		'type'    : 'start',
		'role'    : role,
		'name'    : name,
		'rubrics' : rubrics.length > 0 ? rubrics : null
	}));
}


/**
 * End Kumalive
 */
function finish(){
	if (confirm(LABELS.FINISH_KUMALIVE_CONFIRM)) {
		kumaliveWebsocket.send(JSON.stringify({
			'type' : 'finish'
		}));
	}
}