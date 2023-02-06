<%@include file="/common/taglibs.jsp"%>
<script>
	var discussionSentimentWidget = $('#discussion-sentiment-widget');
	if (discussionSentimentWidget.length === 0) {
		// there was no discussion before - load the widget from scratch
		$('<div />').attr('id', 'discussion-sentiment-widget').appendTo('body')
			.load('<lams:LAMSURL />learning/discussion/learner.jsp?lessonId=<c:out value="${param.lessonId}" />&selectedOption=${selectedOption}');
	} else {
		// the widget already exists and monitor changed the topic, so clear out the selected option and optionally set a new one
		selectDiscussionSentimentOption('${selectedOption}', true, false);
	}
</script>