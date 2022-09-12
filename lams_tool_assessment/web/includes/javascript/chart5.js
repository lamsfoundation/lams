let GRAPH_COLORS = {
		blue: 'rgba(1, 117, 226, 0.85)',
		yellow: 'rgba(249, 248, 113, 0.85)',
		green: 'rgba(0, 145, 74, 0.85)',
		gray: '#6c757d'
	};

function drawActivityCompletionChart(data, animate){
	var newData = [ data.startedLearners - data.completedLearners,
		 			data.completedLearners,
					data.possibleLearners - data.startedLearners
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
				backgroundColor : [
									GRAPH_COLORS.yellow,
									GRAPH_COLORS.green,
									GRAPH_COLORS.blue,
								  ],
				borderWidth : 1,
				borderColor : COLORS.gray
			} ],
			labels : [ 
					   LABELS.ACTIVITY_COMPLETION_CHART_STARTED_LEARNERS,
					   LABELS.ACTIVITY_COMPLETION_CHART_COMPLETED_LEARNERS,
					   LABELS.ACTIVITY_COMPLETION_CHART_POSSIBLE_LEARNERS, ]
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
					fontSize : 16,
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
									lineWidth: 0,
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
				fontSize : '18',
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
		$('#answered-questions-chart-none').show();
		return;
	}
	
	$('#answered-questions-chart-none').hide();
	
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
				backgroundColor : GRAPH_COLORS.green
								  
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
				fontSize : '18',
				lineHeight: 2,
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