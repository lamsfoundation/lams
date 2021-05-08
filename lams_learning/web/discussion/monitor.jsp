<%@include file="/common/taglibs.jsp"%>

<style>
	.discussion-sentiment-chart-panel .panel-title .fa-comments {
		/* Override global LAMS CSS */
		color: black;
	}
	
	.discussion-sentiment-chart-panel.disabled td,
	.discussion-sentiment-chart-panel.disabled .panel-title,
	.discussion-sentiment-chart-panel.disabled .panel-title .fa-comments {
		/* Make panel look disabled */
		color: gray;
	}

	.discussion-sentiment-chart-panel .panel-heading {
		display: flex;
		justify-content: space-between;
	}
	
	.discussion-sentiment-chart-panel .panel-heading .panel-title {
		/* Make title be on the same level as Stop/Restart button */
		padding-top: 10px;
	}
	
	.discussion-sentiment-chart-panel .panel-heading .discussion-sentiment-start-button {
		/* Start button is hidden by default */
		display: none;
	}
	
	.discussion-sentiment-table-header-row td {
		/* Table headers look better this way */
		font-weight: bold;
		text-align: left !important;
	}
	
	.discussion-sentiment-table-stay-header-row td {
		background-color: #fcf8e3;
	}
	
	.discussion-sentiment-table-move-header-row td {
		background-color: #dff0d8;
	}
	
	.discussion-sentiment-table-option-row td:first-child {
		/* Option cells are slightly moved to the right */
		padding-left: 30px;
	}
</style>

<script>
	function stopDiscussionSentiment(toolQuestionUid) {
		// stop discussion on server and reflect it on widget
		$.ajax({
			url : '<lams:LAMSURL />learning/discussionSentiment/stopMonitor.do',
			data : {
				toolQuestionUid : toolQuestionUid
			},
			type : 'post',
			success : function(){
				 onStopDiscussionSentiment(toolQuestionUid);
			}
		});
	}

	function onStopDiscussionSentiment(toolQuestionUid) {
		// stop auto refresh for this widget and make it look like disabled
		 var widget = $('#discussion-sentiment-chart-panel-content-' + toolQuestionUid)
		 		.data('stop', true)
				.closest('.discussion-sentiment-chart-panel').addClass('disabled');
		$('.discussion-sentiment-stop-button', widget).remove();	
		$('.discussion-sentiment-start-button', widget).show();	
	}

	function getDiscussionSentimentMonitorData(toolQuestionUid) {
		$.ajax({
			url : '<lams:LAMSURL />learning/discussionSentiment/getMonitorData.do',
			data : {
				toolQuestionUid : toolQuestionUid
			},
			dataType : 'json',
			success : function(response) {
				if (!response.isActive) {
					 // probably another teacher stopped this discussion
					 onStopDiscussionSentiment(toolQuestionUid);
				}

				// process raw data in response, which is "option" -> "number of votes"
				var data = response.votes, 
					stayVotes  = 0,
					moveVotes  = 0;
				for (var optionNumber = 1; optionNumber <= 4; optionNumber++) {
					var votes = data[optionNumber];
					if (votes) {
						stayVotes += votes;
					}
				}
				$('#discussion-sentiment-table-stay-header-votes-' + toolQuestionUid).text(stayVotes);

				for (var optionNumber = 11; optionNumber <= 14; optionNumber++) {
					var votes = data[optionNumber];
					if (votes) {
						moveVotes += votes;
					}
				}
				$('#discussion-sentiment-table-move-header-votes-' + toolQuestionUid).text(moveVotes);
				
				var totalVotes = stayVotes + moveVotes;
				$('#discussion-sentiment-votes-' + toolQuestionUid).text(totalVotes);
				var stayPercent = totalVotes === 0 ? 0 : Math.round(stayVotes / totalVotes * 100);
				$('#discussion-sentiment-table-stay-header-value-' + toolQuestionUid).text(stayPercent + '%');
				var movePercent = totalVotes === 0 ? 0 : Math.round(moveVotes / totalVotes * 100);
				$('#discussion-sentiment-table-move-header-value-' + toolQuestionUid).text(movePercent + '%');

				for (var optionNumber = 1; optionNumber <= 14; optionNumber++) {
					if (optionNumber > 4 && optionNumber < 11) {
						// currently options are 1-4 (stay) and 11-14 (move)
						continue;
					}
					
					var votes = data[optionNumber],
						optionRow = $('#discussion-sentiment-table-option-row-' + toolQuestionUid + '-' + optionNumber);
					if (votes && totalVotes > 0) {
						optionRow.css('display', 'table-row')
								 .find('.discussion-sentiment-table-option-value')
								 .text(Math.round(votes / totalVotes * 100) + '%');
					} else {
						// if there are no votes for given option, do not display it at all
						optionRow.css('display', 'none');
					}
				}

				var chartCell = $('#discussion-sentiment-chart-cell-' + toolQuestionUid);
				if (totalVotes === 0) {
					// no votes, no chart
					chartCell.empty();
					return;
				}

				var chartCanvas = $('canvas', chartCell),
					chartData = [stayPercent, movePercent];
				if (chartCanvas.length === 1) {
					// chart already exists, just update it
					var chart = chartCanvas.data('chart');
					chart.data.datasets[0].data = chartData;
					chart.update();
					return;
				}

				// create a new chart
				chartCanvas = $('<canvas />').appendTo(chartCell);
				
				var ctx = chartCanvas[0].getContext('2d'),
					chart = new Chart(ctx, {
						type : 'doughnut',
						borderWidth : 0,
						data : {
							elements : {
								arc : {
									borderWidth : 0,
									fontSize : 0,
								}
							},
							datasets : [ {
								data : chartData,
								backgroundColor : [ '#fcf8e3',
													'#dff0d8'
												  ],
								borderWidth : 0,
							} ],
							labels : [ '<fmt:message key="label.discussion.stay.header" />',
									   '<fmt:message key="label.discussion.move.header" />']
						},
						options : {
							layout : {
								padding : {
									top: 10,
									bottom: 10
								}
							},
							legend : {
								display : false
							},
							animation : {
								animateScale : true,
								animateRotate : true,
								duration : 1000
							},
				            tooltips: {
				                callbacks: {
				                    label: function(tooltipItem, data) {
					                    // own implementation of tooltip on hover: add percent sign
				                        return data.labels[tooltipItem.index] + ': ' + data.datasets[0].data[tooltipItem.index] + '%';
				                    }
				                }
				            }
						}
					});

				// store it here so update process can find it
				chartCanvas.data('chart', chart);
			}
		});
	}
	
	$(document).ready(function(){
		var toolQuestionUid = ${param.toolQuestionUid},
			// how often to refresh the chart
			dataRefreshInterval = 5 * 1000;
		getDiscussionSentimentMonitorData(toolQuestionUid);
		
	
		var dataRefresh = window.setInterval(function(){
			var panelContent = $('#discussion-sentiment-chart-panel-content-' + toolQuestionUid),
				// If in TBL monitor the tab with the chart got refreshed, then old interval is still ticking.
				// We need to clear it so only the new interval does it job.
				existingDataRefresh = panelContent.data('dataRefresh');
			if (panelContent.length == 0 || panelContent.data('stop') || existingDataRefresh != dataRefresh) {
				window.clearInterval(dataRefresh);
				return;
			}
			getDiscussionSentimentMonitorData(toolQuestionUid);
		}, dataRefreshInterval);
	
		$('#discussion-sentiment-chart-panel-content-' + toolQuestionUid).data('dataRefresh', dataRefresh);
	});
</script>

<div class="panel panel-default discussion-sentiment-chart-panel">
	<div class="panel-heading"
		 id="discussion-sentiment-chart-panel-heading-${param.toolQuestionUid}">
       	<div class="panel-title">
	    	<i class="fa fa-comments"></i>
	    	<fmt:message key="label.discussion.header" />
	       	(<span id="discussion-sentiment-votes-${param.toolQuestionUid}"></span>
	       	 <fmt:message key="label.discussion.votes.of.students">
	       		<fmt:param value="${learnerCount}" />
	       	 </fmt:message>
	       	)
     	</div>
     	<div class="btn btn-default discussion-sentiment-start-button" onClick="javascript:startDiscussionSentiment(${param.toolQuestionUid}, true)">
			<fmt:message key="label.discussion.restart"/>
		</div>
  		<div class="btn btn-default discussion-sentiment-stop-button" onClick="javascript:stopDiscussionSentiment(${param.toolQuestionUid})">
			<fmt:message key="label.discussion.stop"/>
		</div>
    </div>

    <div id="discussion-sentiment-chart-panel-content-${param.toolQuestionUid}" class="panel-body container-fluid" role="tabpanel"
       	 aria-labelledby="discussion-sentiment-chart-panel-heading-${param.toolQuestionUid}">
		<div class="row">
			<div class="col-xs-12 col-sm-6" id="discussion-sentiment-chart-cell-${param.toolQuestionUid}">
			</div>
			<div class="col-xs-12 col-sm-6">
				<table class="table">
			       	 <tr class="discussion-sentiment-table-header-row discussion-sentiment-table-stay-header-row">
			       	 	<td>
			       	 		<fmt:message key="label.discussion.stay.header" />
			       	 		(<span id="discussion-sentiment-table-stay-header-votes-${param.toolQuestionUid}"></span>&nbsp;<fmt:message key="label.discussion.votes" />)
			       	 	</td>
			       	 	<td id="discussion-sentiment-table-stay-header-value-${param.toolQuestionUid}">
			       	 	</td>
			       	 </tr>
			       	 <c:forEach begin="1" end="4" var="optionNumber">
				       	 <tr id="discussion-sentiment-table-option-row-${param.toolQuestionUid}-${optionNumber}" class="discussion-sentiment-table-option-row">
				       	 	<td>
				       	 		<fmt:message key="label.discussion.stay.option.${optionNumber}" />
				       	 	</td>
				       	 	<td class="discussion-sentiment-table-option-value">
				       	 	</td>
				       	 </tr>
			       	 </c:forEach>
			       	 
			       	 <tr class="discussion-sentiment-table-header-row discussion-sentiment-table-move-header-row">
			       	 	<td>
			       	 		<fmt:message key="label.discussion.move.header" />
			       	 		(<span id="discussion-sentiment-table-move-header-votes-${param.toolQuestionUid}"></span>&nbsp;<fmt:message key="label.discussion.votes" />)
			       	 	</td>
			       	 	<td id="discussion-sentiment-table-move-header-value-${param.toolQuestionUid}">
			       	 	</td>
			       	 </tr>
			       	 <c:forEach begin="11" end="14" var="optionNumber">
				       	 <tr id="discussion-sentiment-table-option-row-${param.toolQuestionUid}-${optionNumber}" class="discussion-sentiment-table-option-row">
				       	 	<td>
				       	 		<fmt:message key="label.discussion.move.option.${optionNumber}" />
				       	 	</td>
				       	 	<td class="discussion-sentiment-table-option-value">
				       	 	</td>
				       	 </tr>
			       	 </c:forEach>
		       	 </table>
			</div>
		</div>
	</div>
</div>