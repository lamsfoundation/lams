<%@ include file="/common/taglibs.jsp"%>

<script>
	function loadResultsPane(resultsPane, isRefresh) {
		var toolContentId = resultsPane.data('toolContentId');
		// load an embedded results list
		// show details button needs to be set in the page which included this page
		resultsPane.load("<c:url value='/learning/showResultsForTeacher.do'/>?embedded=true&showQuestionDetailsButton=" + showQuestionDetailsButton 
						  + "&toolContentID=" + toolContentId, function(){
			var assessmentQuestionsPane = $(this).closest('.assessment-questions-pane'),
				// are any correct/groups buttons clickable?
				discloseAllCorrectEnabled = false,
				discloseAllGroupsEnabled = false; 
			
			// disclose correct/group answers on click
			$('.disclose-button-group .btn', assessmentQuestionsPane).not('[disabled]').each(function(){
				let button = $(this),
					isCorrectButton = button.hasClass('disclose-correct-button');
				if (isCorrectButton) {
					discloseAllCorrectEnabled = true;
				} else {
					discloseAllGroupsEnabled = true;
				}
				
				
				button.click(function(event) {	
					if (!confirm(isCorrectButton ? "<fmt:message key='message.disclose.correct.answers' />"
												: "<fmt:message key='message.disclose.groups.answers' />")) {
						return;
					}
		
					// check if correct answers are disclosed before groups' answers
					if (isCorrectButton &&
						!button.closest('.disclose-button-group').find('.disclose-groups-button').is('[disabled]') &&
						!confirm("<fmt:message key='message.disclose.correct.before.groups.answers' />")) {
						return;
					}
					
					discloseAnswers(button, resultsPane, true);
				});
			});
		
			// if disclose all correct/groups answers buttons are clickable, add a click handler
			// and disable if not
			var allCorrectButton = $('.disclose-all-correct-button', assessmentQuestionsPane);
			if (discloseAllCorrectEnabled) {
				// do not add a handler twice
				if (!isRefresh) {
					allCorrectButton.click(function(){
						if (!confirm("<fmt:message key='message.disclose.all.correct.answers' />")) {
							return;
						}
		
						// check if correct answers are disclosed before groups' answers
						if (!$('.disclose-all-groups-button', assessmentQuestionsPane).is('[disabled]') &&
							!confirm("<fmt:message key='message.disclose.correct.before.groups.answers' />")) {
							return;
						}
						
						let nonDisclosedQuestions = $('.disclose-correct-button', assessmentQuestionsPane).not('[disabled]'),
			    			lastQuestionUid = nonDisclosedQuestions.last().closest('.disclose-button-group').attr('questionUid');
						nonDisclosedQuestions.each(function() {
							let isLast = lastQuestionUid == $(this).closest('.disclose-button-group').attr('questionUid'); 
							discloseAnswers($(this), resultsPane, isLast);
						});
						disabledAndCheckButton(allCorrectButton);
					});
				}
			} else {
				disabledAndCheckButton(allCorrectButton);
			}
		
			var allGroupsButton = $('.disclose-all-groups-button', assessmentQuestionsPane);
			if (discloseAllGroupsEnabled) {
				// do not add a handler twice
				if (!isRefresh) {
					allGroupsButton.click(function(){
						if (!confirm("<fmt:message key='message.disclose.all.groups.answers' />")) {
							return;
						}
		
						let nonDisclosedQuestions = $('.disclose-groups-button', assessmentQuestionsPane).not('[disabled]'),
		    				lastQuestionUid = nonDisclosedQuestions.last().closest('.disclose-button-group').attr('questionUid');
						nonDisclosedQuestions.each(function() {
							let isLast = lastQuestionUid == $(this).closest('.disclose-button-group').attr('questionUid'); 
							discloseAnswers($(this), resultsPane, isLast);
						});
						disabledAndCheckButton(allGroupsButton);
					});
				}
			} else {
				disabledAndCheckButton(allGroupsButton);
			}
		});
	}

	function discloseAnswers(button, resultsPane, isLast) {
		let isCorrectButton = button.hasClass('disclose-correct-button'),
			toolContentId = resultsPane.data('toolContentId');
		
		$.ajax({
			'url'  : '<lams:WebAppURL />monitoring/' 
					  + (isCorrectButton ? 'discloseCorrectAnswers' : 'discloseGroupsAnswers')
					  + '.do',
			'type': 'POST',
			'data' : {
				'questionUid'   : button.closest('.disclose-button-group').attr('questionUid'),
				'toolContentID' : toolContentId,
				'<csrf:tokenname/>' : '<csrf:tokenvalue/>',
				'skipLearnersNotification' : !isLast
			}
		}).done(function(){
			// reload results after disclosing answers
			loadResultsPane(resultsPane, true);
		});
	}

	function disabledAndCheckButton(button){
		button.attr('disabled', true).html('<i class="fa fa-check text-success">&nbsp;</i>' + button.text());
	}
</script>