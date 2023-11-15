var GRAPH_COLORS = {
	blue: 'rgba(1, 117, 226, 0.85)',
	yellow: 'rgba(249, 248, 113, 0.85)',
	green: 'rgba(0, 145, 74, 0.85)',
	gray: '#6c757d'
};

function drawActivityCompletionChart(data, animate){
	// prepare data for the chart
	let notStartedLearners = data.possibleLearners.filter(function (learner) {
			let found = false;
			$.each(data.startedLearners, function (index, startedLearner) {
				if (learner.id == startedLearner.id) {
					found = true;
					return false;
				}
			});
			return !found;
		}),
		startedNotCompletedLearners = data.startedLearners.filter(function (learner) {
			let found = false;
			$.each(data.completedLearners, function (index, completedLearner) {
				if (learner.id == completedLearner.id) {
					found = true;
					return false;
				}
			});
			return !found;
		}),
		newData = [notStartedLearners.length,
			startedNotCompletedLearners.length,
			data.completedLearners.length
		];

	let chartPlaceholder = $('#activity-completion-chart');
	if (chartPlaceholder.length === 0) {
		// no sessions yet, so no chart placeholder
		return;
	}

	// store current data for custom tooltip
	chartPlaceholder.data('tooltip-input', [notStartedLearners, startedNotCompletedLearners, data.completedLearners]);
	chartPlaceholder.data('useGroupsAsNames', false);
	chartPlaceholder.data('isGrouped', data.isGrouped);

	if (activityCompletionChart != null) {
		// chart already exists, just update data
		activityCompletionChart.data.datasets[0].data = newData;
		activityCompletionChart.update();
		return;
	}

	let ctx = chartPlaceholder[0].getContext('2d');

	activityCompletionChart = new Chart(ctx, {
		type : 'doughnut',
		data : {
			datasets : [ {
				data : newData,
				backgroundColor : [
					GRAPH_COLORS.blue,
					GRAPH_COLORS.yellow,
					GRAPH_COLORS.green
				],
				borderWidth : 1,
				borderColor : COLORS.gray
			} ],
			labels : [ LABELS.ACTIVITY_COMPLETION_CHART_POSSIBLE_LEARNERS,
				LABELS.ACTIVITY_COMPLETION_CHART_STARTED_LEARNERS,
				LABELS.ACTIVITY_COMPLETION_CHART_COMPLETED_LEARNERS ]
		},
		options : {
			animation : {
				animateScale : true,
				animateRotate : true,
				duration : animate ? 1000 : 0
			},
			plugins : {
				title : {
					display: true,
					font : {
						size : 18
					},
					text : LABELS.ACTIVITY_COMPLETION_CHART_TITLE
				},
				legend : {
					position: 'bottom',
					labels : {
						font : {
							size: 16
						},
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
				tooltip : {
					enabled : false,
					external  : function(context) {
						listCompletionChartLearners.call(this, chartPlaceholder)
					}
				}
			}
		}
	});
}


function drawAnsweredQuestionsChart(data, animate){
	if (!data.answeredQuestionsByUsers) {
		$('#answered-questions-chart-none').show();
		return;
	}

	let chartPlaceholder = $('#answered-questions-chart'),
		useGroupsAsNames = data.useLeader && data.isGrouped;

	if (chartPlaceholder.length === 0) {
		// no sessions yet, so no chart placeholder
		return;
	}

	$('#answered-questions-chart-none').hide();

	// store current data for custom tooltip
	chartPlaceholder.data('tooltip-input', data.answeredQuestionsByUsers);
	chartPlaceholder.data('useGroupsAsNames', useGroupsAsNames);
	chartPlaceholder.data('isGrouped', data.isGrouped);

	if (answeredQuestionsChart != null) {
		// chart already exists, just update data
		answeredQuestionsChart.data.datasets[0].data =  Object.values(data.answeredQuestionsByUsersCount);
		answeredQuestionsChart.update();
		return;
	}

	let ctx = chartPlaceholder[0].getContext('2d'),
		// prevent scale to change on each update
		// set suggested max number of students to 3/4
		// of all possible learners
		suggestedBarMax = Math.max(2, Math.floor(3 * (useGroupsAsNames ? data.sessionCount : data.possibleLearners.length) / 4));

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
			animation : {
				duration : animate ? 1000 : 0
			},
			interaction : {
				mode : 'index',
				intersect : suggestedBarMax <= 21
			},
			scales : {
				x : {
					title: {
						display : true,
						text: LABELS.ANSWERED_QUESTIONS_CHART_X_AXIS,
						font : {
							size : 16
						}
					}
				},
				y :
					{
						beginAtZero   : true,
						suggestedMax : suggestedBarMax,
						ticks : {
							stepSize      : 1,
							maxTicksLimit : 5,
						},
						title : {
							display : true,
							text : useGroupsAsNames ? LABELS.ANSWERED_QUESTIONS_CHART_Y_AXIS_GROUPS : LABELS.ANSWERED_QUESTIONS_CHART_Y_AXIS_STUDENTS,
							font : {
								size : 16
							}
						}
					}
			},
			plugins : {
				title : {
					display : true,
					font : {
						size : 18,
						lineHeight: 2
					},
					text : useGroupsAsNames ? LABELS.ANSWERED_QUESTIONS_CHART_TITLE_GROUPS : LABELS.ANSWERED_QUESTIONS_CHART_TITLE

				},
				legend : {
					display: false
				},
				tooltip : {
					enabled : false,
					external  : function(context) {
						listCompletionChartLearners.call(this, chartPlaceholder)
					}
				}
			}
		}
	});
}


function listCompletionChartLearners(chartPlaceholder) {
	let tooltipModel = this.$context.tooltip,
		tooltipClassName = chartPlaceholder.attr('id') + '-tooltip';
	// always remove the tooltip at the beginning
	$('.' + tooltipClassName).remove();
	if (tooltipModel.opacity === 0) {
		// if it should be hidden, there is nothing to do
		return;
	}
	// iterate over learners, get their names and portraits
	var counter = 0,
		data = chartPlaceholder.data('tooltip-input'),
		useGroupsAsNames = chartPlaceholder.data('useGroupsAsNames'),
		isGrouped = chartPlaceholder.data('isGrouped'),
		users = data[tooltipModel.dataPoints[0].dataIndex];

	if (!users || users.length === 0) {
		return;
	}

	// create tooltip
	var tooltipEl = $('<div />').addClass(tooltipClassName)
		.appendTo(document.body)
		.css({
			'opacity': 1,
			'position': 'absolute',
			'pointerEvents': 'none',
			'background-color': 'white',
			'padding': '15px',
			'border': 'thin solid #ddd',
			'border-radius': '25px'
		});


	$(users).each(function () {
		var portraitDiv = $(definePortrait(this.portraitUuid, this.id, STYLE_SMALL, true, LAMS_URL)).css({
				'vertical-align': 'middle'
			}),
			userDiv = $('<div />').append(portraitDiv).appendTo(tooltipEl).css({
				'padding-bottom': '5px'
			});

		$('<span />').text(useGroupsAsNames ? this.group + ' (' + this.name + ')'
			: (this.name + (isGrouped && this.group ? ' (' + this.group + ') ' : '')))
			.appendTo(userDiv).css({
			'padding-left': '10px'
		});

		if (counter === 15) {
			// do not display more than 15 learners
			if (users.length > 16) {
				$('<div />').text('...').appendTo(tooltipEl).css({
					'font-weight': 'bold',
					'font-size': '20px',
					'text-align': 'center'
				});
			}
			return false;
		}
		counter++;
	});

	// do not add padding for the last element as the tooltip does not look symmetric
	tooltipEl.children(':last-child').css({
		'padding-bottom': '0'
	});

	var position = this.chart.canvas.getBoundingClientRect();
	tooltipEl.css({
		'left': position.left + window.pageXOffset + tooltipModel.caretX - tooltipEl.width() - 60 + 'px',
		'top': Math.max(10, position.top + window.pageYOffset + tooltipModel.caretY - tooltipEl.height() / 2) + 'px',
	});
}