<script>
	if (typeof stopDiscussionSentimentLearnerWidget == 'function') {
		// this method is defined in learner.jsp
		stopDiscussionSentimentLearnerWidget();
	} else {
		// this should not happen, i.e. get stop signal when widget is not open
		$('#discussion-sentiment-widget, #discussion-sentiment-command').remove();
	}
</script>