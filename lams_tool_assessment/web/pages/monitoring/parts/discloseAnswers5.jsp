<%@ include file="/common/taglibs.jsp"%>

<script>
	function loadResultsPane(resultsPane, isRefresh) {
		var toolContentId = resultsPane.data('toolContentId');
		// load an embedded results list
		// show details button needs to be set in the page which included this page
		resultsPane.load("<c:url value='/learning/showResultsForTeacher.do'/>?embedded=true&sessionMapID=${sessionMapID}&showQuestionDetailsButton="
						  + showQuestionDetailsButton + "&toolContentID=" + toolContentId, function(){
			var assessmentQuestionsPane = $(this).closest('.assessment-questions-pane').length 
					? $(this).closest('.assessment-questions-pane') : $('.assessment-questions-pane-ira'),
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
					showConfirm(isCorrectButton ? "<spring:escapeBody javaScriptEscape='true'><fmt:message key='message.disclose.correct.answers' /></spring:escapeBody>"
								: "<spring:escapeBody javaScriptEscape='true'><fmt:message key='message.disclose.groups.answers' /></spring:escapeBody>", function(){
						// check if correct answers are disclosed before groups' answers
						if (isCorrectButton &&
							!button.closest('.disclose-button-group').find('.disclose-groups-button').is('[disabled]')) {
								showConfirm("<spring:escapeBody javaScriptEscape='true'><fmt:message key='message.disclose.correct.before.groups.answers' /></spring:escapeBody>", function(){
									discloseAnswers(button, resultsPane, true);
								});
						} else {
							discloseAnswers(button, resultsPane, true);
						}
					});
				});
			});
		
			// if disclose all correct/groups answers buttons are clickable, add a click handler
			// and disable if not
			var allCorrectButton = $('.disclose-all-correct-button', assessmentQuestionsPane);
			if (discloseAllCorrectEnabled) {
				// do not add a handler twice
				if (!isRefresh) {
					allCorrectButton.click(function(){
						showConfirm("<spring:escapeBody javaScriptEscape='true'><fmt:message key='message.disclose.all.correct.answers' /></spring:escapeBody>", function(){
							// check if correct answers are disclosed before groups' answers
							let discloseFunction = function(){
								let nonDisclosedQuestions = $('.disclose-correct-button', assessmentQuestionsPane).not('[disabled]'),
				    				lastQuestionUid = nonDisclosedQuestions.last().closest('.disclose-button-group').attr('questionUid');
								nonDisclosedQuestions.each(function() {
									let isLast = lastQuestionUid == $(this).closest('.disclose-button-group').attr('questionUid'); 
									discloseAnswers($(this), resultsPane, isLast);
								});
								disabledAndCheckButton(allCorrectButton);
							};
							
							if (!$('.disclose-all-groups-button', assessmentQuestionsPane).is('[disabled]')) {
								showConfirm("<spring:escapeBody javaScriptEscape='true'><fmt:message key='message.disclose.correct.before.groups.answers' /></spring:escapeBody>", discloseFunction);
								return;
							}
							
							discloseFunction();
						});
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
						showConfirm("<spring:escapeBody javaScriptEscape='true'><fmt:message key='message.disclose.all.groups.answers' /></spring:escapeBody>", function(){
							let nonDisclosedQuestions = $('.disclose-groups-button', assessmentQuestionsPane).not('[disabled]'),
			    				lastQuestionUid = nonDisclosedQuestions.last().closest('.disclose-button-group').attr('questionUid');
							nonDisclosedQuestions.each(function() {
								let isLast = lastQuestionUid == $(this).closest('.disclose-button-group').attr('questionUid'); 
								discloseAnswers($(this), resultsPane, isLast);
							});
							disabledAndCheckButton(allGroupsButton);
						});
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
		button.attr('disabled', true).html('<i class="fa fa-check">&nbsp;</i>' + button.text());
	}
</script>