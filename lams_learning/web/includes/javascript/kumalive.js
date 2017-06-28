$(document).ready(function(){
	$('#teacher').click(raiseHandPromptShow);
});

var kumaliveWebsocket = new WebSocket(LEARNING_URL.replace('http', 'ws') + 'kumaliveWebsocket?organisationID=' + orgId),
	initialised = false,
	learnerDivTemplate = $('<div />').addClass('learner')
		.append($('<div />').addClass('profilePictureWrapper').append($('<div />').addClass('profilePicture profilePictureHidden')))
		.append($('<div />').addClass('name'));

kumaliveWebsocket.onopen = function(e) {
	kumaliveWebsocket.send(JSON.stringify({
		'type' : 'join'
	}));
};
kumaliveWebsocket.onclose = function(e){
	$('body').text("Websocket closed");
};

kumaliveWebsocket.onmessage = function(e){
	// read JSON object
	var message = JSON.parse(e.data),
		type = message.type,
		container = $('#learnersContainer');
	switch(type) {
		case 'start' : {
			kumaliveWebsocket.send(JSON.stringify({
				'type' : 'start',
				'name' : 'random name'
			}));
		}
		break;
		case 'join' : {
			kumaliveWebsocket.send(JSON.stringify({
				'type' : 'join'
			}));
		}
		break;
		case 'refresh': {
			if (!initialised) {
				$('#dialogKumaliveLabel', window.parent.document).text('Kumalive: ' + message.name);
				$('#teacher .profilePicture').css('background-image',
						'url(' + LAMS_URL + 'download?preferDownload=false&uuid=' + message.teacherPortraitUuid + ')');
				$('#teacher .name').text(message.teacherName);
				initialised = true;
			}
			
			for (var i = 0;i<30;i++) {
				$.each(message.learners, function(index, learner){
					var learnerDiv = learnerDivTemplate.clone().data('id', learner.id).appendTo(container);
					$('.profilePicture', learnerDiv).css('background-image',
							'url(' + LAMS_URL + 'download?preferDownload=false&uuid=' + learner.portraitUuid + ')');
					$('.name', learnerDiv).text(learner.firstName + ' ' + learner.lastName + (i % 2 ? 'asdfassafasfsdafd' : ''));
					if (message.isTeacher) {
						learnerDiv.attr('title', learner.login);
					}
					learnerFadeIn(learnerDiv);
					if (i % 30 == 0) {
						setTimeout(function(){
							learnerFadeOut(learnerDiv);
						}, 4000);
					}
					if (i % 30 == 15) {
						setTimeout(function(){
							raiseHand(learnerDiv);
						}, 7000);
					}
					if (i % 30 > 17) {
						setTimeout(function(){
							raiseHand(learnerDiv);
						}, (i % 30) * 500);
					}
				});
			}
		}
		break;
	} 
};

function learnerFadeIn(learnerDiv) {
	var nameDiv = $('.name', learnerDiv).css('color', 'green');
	learnerDiv.show();

	$('.profilePicture', learnerDiv).switchClass('profilePictureHidden', 'profilePictureShown', 1000, function(){
		nameDiv.css('color', 'initial');
	});
}

function learnerFadeOut(learnerDiv) {
	var nameDiv = $('.name', learnerDiv).css('color', 'red');

	$('.profilePicture', learnerDiv).switchClass('profilePictureShown', 'profilePictureHidden', 1000, function(){
		nameDiv.remove();
		learnerDiv.animate({
			'width' : 'toggle'
		}, 1000, function(){
			learnerDiv.remove();
		});
	});
}

function raiseHand(learnerDiv) {
	var raiseHandContainer = $('#raiseHandContainer'),
		firstHand = raiseHandContainer.children('.learner').length == 0;
	if (firstHand) {
		raiseHandContainer.css({
			'display' : 'none',
		})
	}
	
	var targetLearnerDiv = learnerDiv.clone(true).css({
				'visibility' : 'hidden',
				'cursor' : 'pointer'
			}).click(learnerSpeak)
			  .appendTo(raiseHandContainer);
	if (firstHand) {
		raiseHandContainer.slideDown(500);
	}
	
	var targetOffset = $('.profilePicture', targetLearnerDiv).offset(),
		profilePicture = $('.profilePicture', learnerDiv),
		transitionCopy = profilePicture.clone().appendTo('body')
			.css({
				'position' : 'fixed'
			}).offset(profilePicture.offset())
	        .animate({
		    	'left' : targetOffset.left,
		    	'top' : targetOffset.top
		    }, 1000, function(){
		    	targetLearnerDiv.css('visibility', 'visible');
		    	transitionCopy.remove();
		    });
}

function raiseHandPromptShow() {
	$('#teacher').slideUp(function(){
		$('#raiseHandPrompt').slideDown();
	});
}

function learnerSpeak() {
	var learnerDiv = $(this),
		id = learnerDiv.data('id'),
		speaker = $('<div />').addClass('speaker').css({
			'margin-top' : '20px',
			'visibility' : 'hidden'
		}).appendTo('#actionCell'),
		targetProfilePicture = $('.profilePicture', learnerDiv).clone().removeClass('profilePictureShown').appendTo(speaker);
	$('.name', learnerDiv).clone().appendTo(speaker);
	$('#raiseHandPrompt').slideUp(function(){
		var targetOffset = targetProfilePicture.offset();
			profilePicture = $('.profilePicture', learnerDiv),
			transitionCopy = profilePicture.clone().appendTo('body')
				.css({
					'position' : 'fixed'
				}).offset(profilePicture.offset())
		        .animate({
			    	'left' : targetOffset.left,
			    	'top' : targetOffset.top,
			    	'width' : '200px',
			    	'height' : '200px'
			    }, 1000, function(){
					speaker.css('visibility', 'visible');
			    	transitionCopy.remove();
			    });

	});

}