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
		//handler for expand/collapse all button
		$("#toggle-burning-questions-button").click(function() {
			var isExpanded = eval($(this).data("expanded"));
				
			//fire the actual buttons so burning questions can be closed/expanded
			if (isExpanded) {
				$("#burning-questions-accordion .accordion-button:not('.collapsed')").each(function() {
					this.click();
				});
								
			} else {
				$("#burning-questions-accordion .accordion-button").each(function() {
					this.click();
				});					
			}

			//change button label
			var newButtonLabel = isExpanded ? "<fmt:message key='label.expand.all' />" : "<fmt:message key='label.collapse.all' />";
			$(".hidden-xs", $(this)).text(newButtonLabel);

			//change button icon
			if (isExpanded) {
				$(".fa", $(this)).removeClass("fa-minus-circle").addClass("fa-plus-circle");
			} else {
				$(".fa", $(this)).removeClass("fa-plus-circle").addClass("fa-minus-circle");
			}

			//change button's data-expanded attribute
			$(this).data("expanded", !isExpanded);
		});

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