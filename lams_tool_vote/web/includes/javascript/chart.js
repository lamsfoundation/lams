/*
 * Functions using gRaphael to draw charts
 */

// prevent columns in bar chart from being too wide
var MAX_BAR_WIDTH = 100;

var currentChartType = null;
var legend = null;
var values = null;
var paper = null;
var pieChart = null;
var barChart = null;

// all labels will have this CSS set
Raphael.g.txtattr = {
	'font-family' : 'verdana,arial,helvetica,sans-serif',
	'font-size' : '11 px'
};

function drawChart(type, currentSessionId) {
	var chartDiv = $('#chartDiv');
	
	// redrawing charts was unstable, so it is better to draw once and only show/hide later
	if (pieChart) {
		pieChart.hide();
	}
	if (barChart) {
		barChart.hide();
	}
	
	if ((type == currentChartType) && chartDiv.is(':visible')) {
		chartDiv.slideUp('slow');
		currentChartType = null;
	} else {
		if (!values) {
			// get data only once
			getChartData(currentSessionId);
		}

		currentChartType = type;

		chartDiv.slideDown('slow', function() {
			if (!paper) {
				paper = Raphael('chartDiv');
			}
			
			if (type == 'pie') {
				drawPieChart();
			} else if (type == 'bar') {
				drawBarChart();
			}
		});
	}
}

function drawPieChart() {
	if (pieChart) {
		// show if already was drawn and hidden
		pieChart.show();
	} else {
		pieChart = paper.piechart(110, 110, 100, values, {
			'legend' : legend,
			'legendpos' : 'east'
		}).hover(function() {
			this.sector.stop();
			this.sector.scale(1.1, 1.1, this.cx, this.cy);
	
			highlightLabel(this.label);
		}, function() {
			this.sector.animate({
				transform : 's1 1 ' + this.cx + ' ' + this.cy
			}, 500, "bounce");
	
			normaliseLabel(this.label);
		});
	}
}

function drawBarChart() {
	if (barChart) {
		// show if already was drawn and hidden
		barChart.show();
	} else {
		var chartWidth = Math.round(Math.min(values.length * MAX_BAR_WIDTH, $(
				'#chartDiv').width() * 0.6));
	
		barChart = paper.barchart(10, 10, chartWidth, 200, values, {
			'gutter' : '10%', // space between columns
			'type' : 'soft' // edges round a bit
		});
	
		// bar chart does not provide a legend similar to pie chart, so draw it manually
		var legendX = chartWidth + 20;
		var entryY = 120;
		barChart.labels = paper.set();
	
		for ( var i = 0; i < legend.length; i++) {
			var color = barChart.bars[i].attr("fill");
	
			barChart.labels.push(paper.set());
			barChart.labels[i].push(paper.circle(legendX + 5, entryY, 5).attr({
				fill : color,
				stroke : 'none'
			}));
	
			var txt = paper.text(legendX + 20, entryY, legend[i]).attr(
					Raphael.g.txtattr).attr({
				fill : '#000',
				'text-anchor' : 'start'
			});
			barChart.labels[i].push(txt);
	
			entryY += txt.getBBox().height * 1.2;
		}
	
		// make it appear in the middle
		barChart.labels.translate.apply(barChart.labels, [ 0,
				-barChart.labels.getBBox().height / 2 ]);
		barChart.push(barChart.labels);
	
		barChart.hover(function() {
			highlightLabel(barChart.labels[this.bar.id]);
		}, function() {
			normaliseLabel(barChart.labels[this.bar.id]);
		});
	}
}

function getChartData(currentSessionId) {
	$.ajax({
		url : TOOL_URL + 'chartGenerator.do',
		data : {'currentSessionId' : currentSessionId},
		async : false,
		cache : false,
		dataType : 'json',
		success : function(result) {
			legend = [];
			values = [];
			jQuery.each(result.data, function(index, nomination) {
				// pie chart has problems reading string values
				var value = +nomination.value;
				if (value > 0) {
					// pie chart has problems reading 0 values, so just skip them
					legend.push(value + '% - ' + nomination.name);
					values.push(value);
				}
			});
		}
	});
}

function highlightLabel(label) {
	if (label) {
		label[0].stop();
		label[0].attr({
			r : 7.5
		});
		label[1].attr({
			"font-weight" : 800
		});
	}
}

function normaliseLabel(label) {
	if (label) {
		label[0].animate({
			r : 5
		}, 500, "bounce");
		label[1].attr({
			"font-weight" : 400
		});
	}
}