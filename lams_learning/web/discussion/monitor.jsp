<%@include file="/common/taglibs.jsp"%>
<c:set var="idSuffix" value='${param.toolQuestionUid}${empty param.burningQuestionUid ? "" : "-".concat(param.burningQuestionUid)}' />

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
		background-color: rgba(255, 195, 55, 1);
	}
	
	.discussion-sentiment-table-move-header-row td {
		background-color: rgba(5, 204, 214, 1);
	}
	
	.discussion-sentiment-table-option-row td:first-child {
		/* Option cells are slightly moved to the right */
		padding-left: 30px;
	}
</style>

<script>
	function stopDiscussionSentiment(toolQuestionUid, burningQuestionUid) {
		// stop discussion on server and reflect it on widget
		$.ajax({
			url : '<lams:LAMSURL />learning/discussionSentiment/stopMonitor.do',
			data : {
				toolQuestionUid : toolQuestionUid,
				burningQuestionUid : burningQuestionUid
			},
			type : 'post',
			success : function(){
				 onStopDiscussionSentiment(toolQuestionUid, burningQuestionUid);
			}
		});
	}

	function onStopDiscussionSentiment(toolQuestionUid, burningQuestionUid) {
		// stop auto refresh for this widget and make it look like disabled
		 var widget = $('#discussion-sentiment-chart-panel-content-' + toolQuestionUid + (burningQuestionUid ? '-' + burningQuestionUid : ''))
		 		.data('stop', true)
				.closest('.discussion-sentiment-chart-panel').addClass('disabled');
		$('.discussion-sentiment-stop-button', widget).remove();	
		$('.discussion-sentiment-start-button', widget).show();	
	}

	function getDiscussionSentimentMonitorData(toolQuestionUid, burningQuestionUid) {
		$.ajax({
			url : '<lams:LAMSURL />learning/discussionSentiment/getMonitorData.do',
			data : {
				toolQuestionUid : toolQuestionUid,
				burningQuestionUid : burningQuestionUid
			},
			dataType : 'json',
			success : function(response) {
				if (!response.isActive) {
					 // probably another teacher stopped this discussion
					 onStopDiscussionSentiment(toolQuestionUid, burningQuestionUid);
				}

				// process raw data in response, which is "option" -> "number of votes"
				var data = response.votes, 
					stayVotes  = 0,
					moveVotes  = 0,
					idSuffix = toolQuestionUid + (burningQuestionUid ? '-' + burningQuestionUid : '');
				for (var optionNumber = 1; optionNumber <= 4; optionNumber++) {
					var votes = data[optionNumber];
					if (votes) {
						stayVotes += votes;
					}
				}
				$('#discussion-sentiment-table-stay-header-votes-' + idSuffix).text(stayVotes);

				for (var optionNumber = 11; optionNumber <= 14; optionNumber++) {
					var votes = data[optionNumber];
					if (votes) {
						moveVotes += votes;
					}
				}
				$('#discussion-sentiment-table-move-header-votes-' + idSuffix).text(moveVotes);
				
				var totalVotes = stayVotes + moveVotes;
				$('#discussion-sentiment-votes-' + idSuffix).text(totalVotes);
				var stayPercent = totalVotes === 0 ? 0 : Math.round(stayVotes / totalVotes * 100);
				$('#discussion-sentiment-table-stay-header-value-' + idSuffix).text(stayPercent + '%');
				var movePercent = totalVotes === 0 ? 0 : Math.round(moveVotes / totalVotes * 100);
				$('#discussion-sentiment-table-move-header-value-' + idSuffix).text(movePercent + '%');

				for (var optionNumber = 1; optionNumber <= 14; optionNumber++) {
					if (optionNumber > 4 && optionNumber < 11) {
						// currently options are 1-4 (stay) and 11-14 (move)
						continue;
					}
					
					var votes = data[optionNumber],
						optionRow = $('#discussion-sentiment-table-option-row-' + idSuffix + '-' + optionNumber);
					if (votes && totalVotes > 0) {
						optionRow.css('display', 'table-row')
								 .find('.discussion-sentiment-table-option-value')
								 .text(Math.round(votes / totalVotes * 100) + '%');
					} else {
						// if there are no votes for given option, do not display it at all
						optionRow.css('display', 'none');
					}
				}

				var chartCell = $('#discussion-sentiment-chart-cell-' + idSuffix);
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
								backgroundColor : [ 'rgba(255, 195, 55, 1)',
													'rgba(5, 204, 214, 1)'
													
												  ],
								borderWidth : 0,
							} ],
							labels : [ '<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.discussion.stay.header" /></spring:escapeBody>',
									   '<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.discussion.move.header" /></spring:escapeBody>']
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
		var toolQuestionUid = <c:out value="${param.toolQuestionUid}" />,
			burningQuestionUid = '<c:out value="${param.burningQuestionUid}" />',
			idSuffix = toolQuestionUid + (burningQuestionUid ? '-' + burningQuestionUid : ''),
			// how often to refresh the chart
			dataRefreshInterval = 5 * 1000;
		getDiscussionSentimentMonitorData(toolQuestionUid, burningQuestionUid);
		
	
		var dataRefresh = window.setInterval(function(){
			var panelContent = $('#discussion-sentiment-chart-panel-content-' + idSuffix),
				// If in TBL monitor the tab with the chart got refreshed, then old interval is still ticking.
				// We need to clear it so only the new interval does it job.
				existingDataRefresh = panelContent.data('dataRefresh');
			if (panelContent.length == 0 || panelContent.data('stop') || existingDataRefresh != dataRefresh) {
				window.clearInterval(dataRefresh);
				return;
			}
			getDiscussionSentimentMonitorData(toolQuestionUid, burningQuestionUid);
		}, dataRefreshInterval);
	
		$('#discussion-sentiment-chart-panel-content-' + idSuffix).data('dataRefresh', dataRefresh);
	});
</script>


<div class="panel panel-default discussion-sentiment-chart-panel">
	<div class="panel-heading"
		 id="discussion-sentiment-chart-panel-heading-${idSuffix}">
       	<div class="panel-title">
	    	<i class="fa fa-comments"></i>
	    	<fmt:message key="label.discussion.header" />
	       	(<span id="discussion-sentiment-votes-${idSuffix}"></span>
	       	 <fmt:message key="label.discussion.votes.of.students">
	       		<fmt:param value="${learnerCount}" />
	       	 </fmt:message>
	       	)
     	</div>
     	<div class="btn btn-default discussion-sentiment-start-button" onClick="javascript:startDiscussionSentiment(<c:out value="${param.toolQuestionUid}" />, '<c:out value="${param.burningQuestionUid}" />', true)">
			<fmt:message key="label.discussion.restart"/>
		</div>
  		<div class="btn btn-default discussion-sentiment-stop-button" onClick="javascript:stopDiscussionSentiment(<c:out value="${param.toolQuestionUid}" />, '<c:out value="${param.burningQuestionUid}" />')">
			<fmt:message key="label.discussion.stop"/>
		</div>
    </div>

    <div id="discussion-sentiment-chart-panel-content-${idSuffix}" class="panel-body container-fluid" role="tabpanel"
       	 aria-labelledby="discussion-sentiment-chart-panel-heading-${idSuffix}">
		<div class="row">
			<div class="col-xs-12 col-sm-6" id="discussion-sentiment-chart-cell-${idSuffix}">
			</div>
			<div class="col-xs-12 col-sm-6">
				<table class="table">
			       	 <tr class="discussion-sentiment-table-header-row discussion-sentiment-table-stay-header-row">
			       	 	<td>
			       	 		<fmt:message key="label.discussion.stay.header" />
			       	 		(<span id="discussion-sentiment-table-stay-header-votes-${idSuffix}"></span>&nbsp;<fmt:message key="label.discussion.votes" />)
			       	 	</td>
			       	 	<td id="discussion-sentiment-table-stay-header-value-${idSuffix}">
			       	 	</td>
			       	 </tr>
			       	 <c:forEach begin="1" end="4" var="optionNumber">
				       	 <tr id="discussion-sentiment-table-option-row-${idSuffix}-${optionNumber}" class="discussion-sentiment-table-option-row">
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
			       	 		(<span id="discussion-sentiment-table-move-header-votes-${idSuffix}"></span>&nbsp;<fmt:message key="label.discussion.votes" />)
			       	 	</td>
			       	 	<td id="discussion-sentiment-table-move-header-value-${idSuffix}">
			       	 	</td>
			       	 </tr>
			       	 <c:forEach begin="11" end="14" var="optionNumber">
				       	 <tr id="discussion-sentiment-table-option-row-${idSuffix}-${optionNumber}" class="discussion-sentiment-table-option-row">
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