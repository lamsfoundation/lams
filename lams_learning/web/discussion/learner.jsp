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
	
	/* Underlining the link in title does not look nice*/
	#discussion-sentiment-widget .panel-title a {
		text-decoration: none !important;
	}
	
	#discussion-sentiment-widget #discussion-sentiment-widget-title {
	 	/* Collapsed by default */
		display: none;
	}
	
	#discussion-sentiment-widget th {
		text-align: center;
		font-weight: bold;
		font-style: normal;
	}
	
	#discussion-sentiment-widget td {
		/* Cells are clickable */
		cursor: pointer;
	}
</style>

<script>
	// This method is called when monitor stops discussion via stopLearner.jsp and on server error
	function stopDiscussionSentimentLearnerWidget(){
		$('#discussion-sentiment-widget-content').data('remove', true).collapse('hide');
	}

	$(document).ready(function(){
		let discussionSentimentWidget = $('#discussion-sentiment-widget'),
			discussionSentimentContent = $('#discussion-sentiment-widget-content', discussionSentimentWidget)
				.on('hidden.bs.collapse', function () {
					// leave just the discussion icon
					$('#discussion-sentiment-widget-title', discussionSentimentWidget).hide();
					
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
						content.css('visibility', 'visible');
					});
				});

		// the widget is collapsed at the beginning, then we expand it
		window.setTimeout(function(){
			discussionSentimentContent.collapse('show');
		}, 1000);

		// cells are clickable
		$('#discussion-sentiment-widget-content td', discussionSentimentWidget).click(function(){
			var selectedCell = $(this),
				selectedOption = selectedCell.data('optionNumber');
			$.ajax({
				url      : '<lams:LAMSURL />learning/discussionSentiment/vote.do',
				data     : {
					lessonId  : ${param.lessonId},
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
						// highlight the successfuly selected cell
						$('#discussion-sentiment-widget td').removeClass('selected warning success');
						selectedCell.addClass('selected')
									.addClass(selectedCell.hasClass('discussion-sentiment-widget-option-stay') ? 'warning' : 'success');
					}
				},
				error    : function(){
					stopDiscussionSentimentLearnerWidget();
				}
			});
		});
	});
</script>

<div class="panel panel-default" >
	<div class="panel-heading"
		 id="discussion-sentiment-widget-heading">
       	<span class="panel-title">
	    	<a class="collapsed" role="button" data-toggle="collapse" href="#discussion-sentiment-widget-content"
	    	   aria-expanded="false" aria-controls="discussion-sentiment-widget-content" >
	    	   <i class="fa fa-comments"></i>
	    	   <span id="discussion-sentiment-widget-title"><fmt:message key="label.discussion.header" /></span>
	       	</a>
     	</span>
    </div>

    <div id="discussion-sentiment-widget-content" class="panel-collapse collapse" role="tabpanel"
       	 aria-labelledby="discussion-sentiment-widget-heading">
       	 <table class="table table-bordered table-condensed">
	       	 <tr>
	       	 	<th class="discussion-sentiment-widget-option-stay warning">
	       	 		<fmt:message key="label.discussion.stay.header" />
	       	 	</th>
	       	 	<th class="discussion-sentiment-widget-option-move success">
	       	 		<fmt:message key="label.discussion.move.header" />
	       	 	</th>
	       	 </tr>
	       	 <c:forEach begin="1" end="4" var="optionNumber">
		       	 <tr>
		       	 	<td class="discussion-sentiment-widget-option-stay ${optionNumber eq param.selectedOption ? 'selected warning' : ''}"
		       	 		data-option-number="${optionNumber}">
		       	 		<fmt:message key="label.discussion.stay.option.${optionNumber}" />
		       	 	</td>
		       	 	<%-- Stay options start from 1, Move options start from 11 --%>
		       	 	<td class="discussion-sentiment-widget-option-move ${(10 + optionNumber) eq param.selectedOption ? 'selected success' : ''}"
		       	 		data-option-number="${10 + optionNumber}">
		       	 		<fmt:message key="label.discussion.move.option.1${optionNumber}" />
		       	 	</td>
		       	 </tr>
	       	 </c:forEach>
       	 </table>
	</div>
</div>