function drawCompletionCharts(toolContentId, useGroups,animate) {
	$.ajax({
		'url' : WEB_APP_URL + 'monitoring/getCompletionChartsData.do',
		'data': {
			'toolContentId' : toolContentId
		},
		'dataType' : 'json',
		'success'  : function(data) {
			// draw charts for the first time
			drawActivityCompletionChart(data, animate);
			drawAnsweredQuestionsChart(data, useGroups, animate);
		}
	});
	
	if (activityCompletionChart == null && answeredQuestionsChart == null && COMPLETION_CHART_UPDATE_INTERVAL > 0) {
		if (typeof completionChartInterval != 'undefined' && completionChartInterval) {
			window.clearInterval(completionChartInterval);
		}
		
		// set up update interval for the charts
		completionChartInterval = window.setInterval(function(){
			drawCompletionCharts(toolContentId, useGroups,animate);
		}, COMPLETION_CHART_UPDATE_INTERVAL);
	}
}

function drawActivityCompletionChart(data, animate){
	var newData = [ data.possibleLearners - data.startedLearners,
		 			data.startedLearners - data.completedLearners,
		 			data.completedLearners
	   			  ];
	if (activityCompletionChart != null) {
		// chart already exists, just update data
		activityCompletionChart.data.datasets[0].data = newData;
		activityCompletionChart.update();
		return;
	}
	
	let ctx = document.getElementById('activity-completion-chart').getContext('2d');
	
	activityCompletionChart = new Chart(ctx, {
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
				data : newData,
				backgroundColor : [ 'rgba(5, 204, 214, 1)',
									'rgba(255, 195, 55, 1)',
									'rgba(253, 60, 165, 1)',
								  ],
				borderWidth : 0,
			} ],
			labels : [ LABELS.ACTIVITY_COMPLETION_CHART_POSSIBLE_LEARNERS,
					   LABELS.ACTIVITY_COMPLETION_CHART_STARTED_LEARNERS,
					   LABELS.ACTIVITY_COMPLETION_CHART_COMPLETED_LEARNERS ]
		},
		options : {
			layout : {
				padding : {
					top: 10
				}
			},
			legend : {
				position: 'left',
				labels : {
					generateLabels : function(chart) {
						var data = chart.data;
						if (data.labels.length && data.datasets.length) {
							return data.labels.map(function(label, i) {
								var meta = chart.getDatasetMeta(0),
									style = meta.controller.getStyle(i),
									value = data.datasets[0].data[i];

								return {
									text: label + ": " + value,
									fillStyle: style.backgroundColor,
									strokeStyle: style.borderColor,
									lineWidth: style.borderWidth,
									hidden: isNaN(value) || meta.data[i].hidden,

									// Extra data used for toggling the
									// correct item
									index: i
								};
							});
						}
						return [];
					}
				}
			},
			title : {
				display: true,
				fontSize : '15',
				text : LABELS.ACTIVITY_COMPLETION_CHART_TITLE
			},
			animation : {
				animateScale : true,
				animateRotate : true,
				duration : animate ? 1000 : 0
			}
		}
	});
}

function drawAnsweredQuestionsChart(data, useGroups, animate){
	if (!data.answeredQuestionsByUsers) {
		return;
	}
	
	// store current data for custom tooltip
	$('#answered-questions-chart').data('tooltip-input', data.answeredQuestionsByUsers);
	
	if (answeredQuestionsChart != null) {
		// chart already exists, just update data
		answeredQuestionsChart.data.datasets[0].data =  Object.values(data.answeredQuestionsByUsersCount);
		answeredQuestionsChart.update();
		return;
	}
	
	let ctx = document.getElementById('answered-questions-chart').getContext('2d');
	answeredQuestionsChart = new Chart(ctx, {
		type : 'bar',
		data : {
			datasets : [ {
				data :  Object.values(data.answeredQuestionsByUsersCount),
				backgroundColor : 'rgba(255, 195, 55, 1)'
								  
			} ],
			labels :  Object.keys(data.answeredQuestionsByUsersCount),
		},
		options : {
			layout : {
				paddint : {
					top : 30
				}
			},
			legend : {
				display : false
			},
			title : {
				display: true,
				fontSize : '15',
				lineHeight: 3,
				text : useGroups ? LABELS.ANSWERED_QUESTIONS_CHART_TITLE_GROUPS : LABELS.ANSWERED_QUESTIONS_CHART_TITLE
			},
			animation : {
				duration : animate ? 1000 : 0
			},
			scales : {
				xAxes : [{
						scaleLabel : {
							display : true,
							labelString : LABELS.ANSWERED_QUESTIONS_CHART_X_AXIS
						}
					}
				],
				yAxes : [
					{
					    ticks : {
							beginAtZero   : true,
							stepSize      : 1,
							maxTicksLimit : 5,
							// prevent scale to change on each update
							// set suggested max number of students to 3/4
							// of all possible learners
							suggestedMax  : Math.max(2, Math.floor(3 * (useGroups ? data.sessionCount : data.possibleLearners) / 4))
						},
						scaleLabel : {
							display : true,
							labelString : useGroups ? LABELS.ANSWERED_QUESTIONS_CHART_Y_AXIS_GROUPS : LABELS.ANSWERED_QUESTIONS_CHART_Y_AXIS_STUDENTS,
							fontSize : 14
						}
					}
				]
			},
			tooltips : {
				 enabled : false,
				 custom  : function(tooltipModel) {
							// always remove the tooltip at the beginning
			                $('.answered-questions-chart-tooltip').remove();
			 				if (tooltipModel.opacity === 0) {
								// if it should be hidden, there is nothing to do
								return;
							}
							
			                // create tooltip
		                    var tooltipEl = $('<div />').addClass('answered-questions-chart-tooltip')
														.appendTo(document.body)
														.css({
															'opacity' : 1,
															'position' :'absolute',
															'pointerEvents' : 'none',
															'background-color' : 'white',
															'padding' : '15px',
															'border'  : 'thin solid #ddd',
															'border-radius' : '25px'
														});
			
			                // iterate over learners, get their names and portraits
							var counter = 0,
								users = $('#answered-questions-chart').data('tooltip-input')[tooltipModel.dataPoints[0].label];
							$(users).each(function(){
								var portraitDiv = $(definePortrait(this[1], this[0], STYLE_SMALL, true, LAMS_URL)).css({
										'vertical-align' : 'middle'
									}),
									userDiv = $('<div />').append(portraitDiv).appendTo(tooltipEl).css({
										'padding-bottom' : '5px'
									});

								$('<span />').text(this[3] ? this[3] + ' (' + this[2] + ')' : this[2]).appendTo(userDiv).css({
									'padding-left' : '10px'
								});
								
								if (counter === 15) {
									// do not display more than 15 learners
									if (users.length > 16) {
										$('<div />').text('...').appendTo(tooltipEl).css({
											'font-weight' : 'bold',
											'font-size'   : '20px',
											'text-align'  : 'center'
 										});
									}
									return false;									
								}
								counter++;
							});
							
							// do not add padding for the last element as the tooltip does not look symmetric
							tooltipEl.children(':last-child').css({
								'padding-bottom' : '0'
							});
			
			                var position = this._chart.canvas.getBoundingClientRect();
							tooltipEl.css({
								'left'  : position.left + window.pageXOffset + tooltipModel.caretX - tooltipEl.width() - 60 + 'px',
								'top'   : Math.max(10, position.top  + window.pageYOffset + tooltipModel.caretY - tooltipEl.height()/2) + 'px',
							});
						}
			}
		}
	});
}