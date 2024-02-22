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
	// global variable that stores information which burning question collapse panel was expanded before reload
	var lamsTratBurningQuestionExpanded = null;

	// copied from parent window's monitorLesson.js
	<c:if test="${!isTbl}">
	    var eventSources = [],
	    	traToolContentId = ${scratchie.contentId};
	    	
		function openEventSource(url, onMessageFunction, skipExisting) {
		    if (skipExisting) {
		        for (let i = 0; i < eventSources.length; i++) {
		            if (eventSources[i].url === url) {
		                return eventSources[i];
		            }
		        }
		    }
	
		    const eventSource = new EventSource(url);
		    eventSources.push(eventSource);
		    eventSource.onmessage = onMessageFunction;
		    return eventSource;
		}
	</c:if>
	
	$(document).ready(function(){
		openEventSource('<lams:WebAppURL />tblmonitoring/burningQuestionsFlux.do?toolContentId=' + traToolContentId, function (event) {
			lamsTratBurningQuestionExpanded = [];
			// store information about expanded panels
			$('#burningQuestionsTable .collapse.show').each(function(){
				let collapse = $(this),
					itemUid = collapse.data('itemUid');
				lamsTratBurningQuestionExpanded.push(
					{ 
						itemUid : itemUid,
						optionsExpanded : $('#options-show-' + itemUid, collapse).length === 0
					}						
				);
			});		
			$("#burningQuestionsTable").load('<lams:WebAppURL />tblmonitoring/burningQuestionsTable.do?isTbl=${isTbl}&toolContentID=' + traToolContentId,
					function(){
						// expand same panels as before the reload
						lamsTratBurningQuestionExpanded.forEach(function(entry){
							let collapse = $('#burningQuestionsTable #collapse' + entry.itemUid).addClass('show');
							collapse.closest('.accordion-item').find('.accordion-button')
								.removeClass('collapsed').attr('aria-expanded', 'true');
							if (entry.optionsExpanded) {
								$('#options-show-' + entry.itemUid, collapse).remove();
								$('#options-' + entry.itemUid, collapse).show();
							}
						});
					}
			);
		});
	});

	<c:if test="${isTbl and discussionSentimentEnabled}">
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
					$('#discussion-sentiment-start-button-' + idSuffix).remove();
				}
			)
		}
	</c:if>
</script>

<div id="burningQuestionsTable" class="${isTbl ? 'container-main ms-4' : 'p-2'}"></div>
