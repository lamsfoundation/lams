<%@ include file="/common/taglibs.jsp"%>

<style>
	.discussion-sentiment-start-button-cell {
		width: 200px;
		text-align: right;
	}	
	
	.discussion-sentiment-chart-row {
		display: none;
	}
	
	.burning-questions-likes {
		width: 50px;
	}
</style>
<script>
	$(document).ready(function(){
		openEventSource('<lams:WebAppURL />tblmonitoring/burningQuestionsFlux.do?toolContentId=' + traToolContentId, function (event) {
			$("#burningQuestionsTable").load('<lams:WebAppURL />tblmonitoring/burningQuestionsTable.do?toolContentID=' + traToolContentId);
		});
	});

	<c:if test="${discussionSentimentEnabled}">
		function startDiscussionSentiment(toolQuestionUid, burningQuestionUid, markAsActive) {
			var idSuffix = toolQuestionUid + '-' + burningQuestionUid,
				chartRow = $('#discussion-sentiment-chart-row-' + idSuffix).css('display', 'table-row');
			
			$('#discussion-sentiment-chart-panel-container-' + idSuffix, chartRow).load(
				'<lams:LAMSURL />learning/discussionSentiment/startMonitor.do',
				{
					toolQuestionUid    : toolQuestionUid,
					burningQuestionUid : burningQuestionUid,
					markAsActive       : markAsActive
				},
				function(){
					$('#discussion-sentiment-start-button-' + idSuffix).closest('td').remove();
				}
			)
		}
	</c:if>
</script>

<div id="burningQuestionsTable" class="container-fluid"></div>