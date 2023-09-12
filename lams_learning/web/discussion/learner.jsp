<%@include file="/common/taglibs.jsp"%>

<style>
	#discussion-sentiment-widget {
	  /* The main widget sticks to right browser edge */
	  position: fixed;
	  top: 30%;
	  right: 0;
	  /* Collapsed by default */
	  width: 40px;
	}	
	
	#discussion-sentiment-widget .card-title a {
		/* Underlining the link in title does not look nice*/
		text-decoration: none !important;
	}
	
	#discussion-sentiment-widget #discussion-sentiment-widget-title {
	 	/* Collapsed by default */
		display: none;
	}
	
	#discussion-sentiment-widget .row:not(:first-child) {
		/* Cells are clickable */
		cursor: pointer;
	}
</style>

<script>
	// This method is called when monitor stops discussion via stopLearner.jsp and on server error
	function stopDiscussionSentimentLearnerWidget(){
		var discussionSentimentContent = $('#discussion-sentiment-widget-content');
		if (discussionSentimentContent.hasClass('show')){
			// if the widget is expanded, collapse it and then remove it
			discussionSentimentContent.data('remove', true).collapse('hide');
		} else {
			// if the widget is collapsed, remove it straight away
			$('#discussion-sentiment-command, #discussion-sentiment-widget').remove();
		}
	}

	function selectDiscussionSentimentOption(selectedOption, autoExpand, autoCollapse) {
		// clear other cells
		var discussionSentimentContent = $('#discussion-sentiment-widget-content'),
			isExpanded = discussionSentimentContent.hasClass('show');
		$('.clickable', discussionSentimentContent).removeClass('selected text-bg-warning text-bg-success');

		if (!selectedOption) {
			if (autoExpand && !isExpanded) {
				// when a teacher starts a fresh discussion and learner has not answered yet,
				// prompt the learner for an answer by expanding the widget
				window.setTimeout(function(){
					discussionSentimentContent.collapse('show');
				}, 1000);
			}
			return;
		}
		
		// highlight the successfuly selected cell
		var selectedCell = $('#discussion-sentiment-widget-option-cell-' + selectedOption, discussionSentimentContent).addClass('selected');
		selectedCell.addClass(selectedCell.hasClass('discussion-sentiment-widget-option-stay') ? 'text-bg-warning' : 'text-bg-success');
		
		if (!autoCollapse) {
			return;
		}
		if (isExpanded) {
			// when learner clicks on an answer, collapse the widget after few seconds
			var collapseTimeout = discussionSentimentContent.data('collapseTimeout');
			if (collapseTimeout) {
				// the learner quickly changed his mind, so count timer from the last click, not the first one
				window.clearTimeout(collapseTimeout);
			}
			collapseTimeout = window.setTimeout(function(){
				discussionSentimentContent.data('collapseTimeout', null).collapse('hide');
			}, 3 * 1000);
			discussionSentimentContent.data('collapseTimeout', collapseTimeout);
		}
	}

	$(document).ready(function(){
		let discussionSentimentWidget = $('#discussion-sentiment-widget'),
			discussionSentimentContent = $('#discussion-sentiment-widget-content', discussionSentimentWidget)
				.on('hidden.bs.collapse', function () {
					// leave just the discussion icon
					$('#discussion-sentiment-widget-title', discussionSentimentWidget).hide();
					$('.card-header', discussionSentimentWidget).removeClass('collapsable-icon-left');
					
					discussionSentimentWidget.animate({
						width: '40px'
					}, function(){
						// if stopDiscussionSentimentLearnerWidget() set it to true, then we are deleting the widget
						var remove = discussionSentimentContent.data('remove');
						if (remove) {
							window.setTimeout(function(){
								// collapse and remove after a short delay
								$('#discussion-sentiment-command').remove();
								discussionSentimentWidget.remove();
							}, 1000);
						}
					});
				})
				.on('show.bs.collapse', function() {
					// hide content at first as it does not look nice when widget get expanded
					var content = $('#discussion-sentiment-widget-content', discussionSentimentWidget).css('visibility', 'hidden');
					discussionSentimentWidget.animate({
						width: '300px'
					}, function(){
						$('#discussion-sentiment-widget-title', discussionSentimentWidget).show();
						$('.card-header', discussionSentimentWidget).addClass('collapsable-icon-left');
						content.css('visibility', 'visible');
					});
				});

		// cells are clickable
		$('#discussion-sentiment-widget-content .clickable', discussionSentimentWidget).click(function(){
			var selectedCell = $(this),
				selectedOption = selectedCell.data('optionNumber');
			$.ajax({
				url      : '<lams:LAMSURL />learning/discussionSentiment/vote.do',
				data     : {
					lessonId  : <c:out value="${param.lessonId}" />,
					selectedOption : selectedOption
				},
				type     : 'post',
				dataType : 'text',
				success  : function(response){
					if (response == 'stop') {
						// no discussion is running, so why this widget is even displayed? remove it
						stopDiscussionSentimentLearnerWidget();
					}
					if (response == 'voted') {
						selectDiscussionSentimentOption(selectedOption, false, true);
					}
				},
				error    : function(){
					stopDiscussionSentimentLearnerWidget();
				}
			});
		});

		selectDiscussionSentimentOption('${param.selectedOption}', true, false);
	});
</script>

<div class="card lcard" >
	<div class="card-header text-bg-secondary" id="discussion-sentiment-widget-heading">
		<span class="card-title">
			<button type="button" data-bs-toggle="collapse" 
				class="btn btn-light collapsed"
				data-bs-target="#discussion-sentiment-widget-content"> 
				<i class="fa fa-comments"></i> 
				<span id="discussion-sentiment-widget-title"> 
					<fmt:message key="label.discussion.header" />
				</span>
			</button>
		</span>
	</div>

	<div id="discussion-sentiment-widget-content" class="collapse" role="tabpanel"
       	 aria-labelledby="discussion-sentiment-widget-heading">
       	 <div class="ltable table-responsive">
	       	 <div class="row">
	       	 	<div class="discussion-sentiment-widget-option-stay text-bg-warning col">
	       	 		<fmt:message key="label.discussion.stay.header" />
	       	 	</div>
	       	 	<div class="discussion-sentiment-widget-option-move text-bg-success col">
	       	 		<fmt:message key="label.discussion.move.header" />
	       	 	</div>
	       	 </div>
	       	 
	       	 <c:forEach begin="1" end="4" var="optionNumber">
		       	 <div class="row">
		       	 	<div id="discussion-sentiment-widget-option-cell-${optionNumber}" class="discussion-sentiment-widget-option-stay col clickable"
		       	 		data-option-number="${optionNumber}">
		       	 		<fmt:message key="label.discussion.stay.option.${optionNumber}" />
		       	 	</div>
		       	 	<%-- Stay options start from 1, Move options start from 11 --%>
		       	 	<div id="discussion-sentiment-widget-option-cell-${10 + optionNumber}" class="discussion-sentiment-widget-option-move col clickable"
		       	 		data-option-number="${10 + optionNumber}">
		       	 		<fmt:message key="label.discussion.move.option.${10 + optionNumber}" />
		       	 	</div>
		       	 </div>
	       	 </c:forEach>
       	 </div>
	</div>
</div>