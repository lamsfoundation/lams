/*
 * Functions using gRaphael to draw charts.
 * 
 * To use them, 'value' and 'legends' arrays should be defined.
 * If not, 'chartDataUrl' must be defined, so getChartData() can run.
 */

// prevent columns in bar chart from being too wide
var MAX_BAR_WIDTH = 100;
var SHOW_HIDE_ANIMATION_DURATION_MS = 1000;

// there can be multiple charts on the screen, thus arrays
var currentChartType = [];
var values = [];
var legends = [];
var papers = [];
var pieCharts = [];
var barCharts = [];

// all labels will have this CSS set
Raphael.g.txtattr = {
	'font-family' : 'verdana,arial,helvetica,sans-serif',
	'font-size' : '11 px'
};

function drawChart(type, chartNumber, ajaxParams) {
	var chartDiv = $('#chartDiv' + chartNumber);

	if ((type == currentChartType[chartNumber]) && chartDiv.is(':visible')) {
		hideChart(pieCharts[chartNumber]);
		hideChart(barCharts[chartNumber]);
		chartDiv.slideUp('slow');
		currentChartType[chartNumber] = null;
	} else {
		if (!values[chartNumber]) {
			// get data only once
			getChartData(chartNumber, ajaxParams);
		}
		
		// values could have been just set by getChartData() call
		if (values[chartNumber]) {
			currentChartType[chartNumber] = type;
	
			chartDiv.slideDown('slow', function() {
				if (!papers[chartNumber]) {
					papers[chartNumber] = Raphael('chartDiv'+chartNumber);
				}
				
				if (type == 'pie') {
					drawPieChart(chartNumber);
				} else if (type == 'bar') {
					drawBarChart(chartNumber);
				}
			});
		}
	}
}

function drawPieChart(chartNumber) {
	hideChart(barCharts[chartNumber]);
	// if already exists, show the chart
	if (!showChart(pieCharts[chartNumber])){
		var currentPieChart = papers[chartNumber].piechart(110, 110, 100, values[chartNumber], {
			'legend' : legends[chartNumber],
			'legendpos' : 'east'
		}).attr('opacity', 0)
		  .animate({'opacity' : 1}, SHOW_HIDE_ANIMATION_DURATION_MS)
		  
		  .hover(function() {
			this.sector.stop();
			this.sector.scale(1.05, 1.05, this.cx, this.cy);
	
			highlightChartPart(this.sector, this.label);
		}, function() {
			this.sector.stop();
			this.sector.animate({
				transform : 's1 1 ' + this.cx + ' ' + this.cy
			}, 500, "bounce");
			
			normaliseChartPart(this.sector, this.label);
		});
		
		setHiglightColors(currentPieChart.series);
		
		pieCharts[chartNumber] = currentPieChart;
	}
}

function drawBarChart(chartNumber) {
	hideChart(pieCharts[chartNumber]);
	// if already exists, show the chart
	if (!showChart(barCharts[chartNumber])){
		var chartWidth = Math.round(Math.min(values[chartNumber].length * MAX_BAR_WIDTH, $(
				'#chartDiv'+chartNumber).width() * 0.6));
	
		var currentBarChart = papers[chartNumber].barchart(25, 10, chartWidth, 200, values[chartNumber], {
			'gutter' : '10%', // space between columns
			'type' : 'soft' // edges round a bit
		}).attr('opacity', 0); // hide the chart first to smoothly show it afterwards
	
		setHiglightColors(currentBarChart.bars);
		
		// add Y axis with percent
		var maxValue = Math.max.apply(Math, values[chartNumber]);
		currentBarChart.push(Raphael.g.axis(23, 189, 158, 0, maxValue, 10, 1, [], '|', 3, papers[chartNumber]).all);
		currentBarChart.push(papers[chartNumber].text(20, 20, '%').attr('font-weight', 800));
		
		// bar chart does not provide a legends similar to pie chart, so draw it manually
		var legendsX = chartWidth + 40;
		var entryY = 120;
		currentBarChart.labels = papers[chartNumber].set();
	
		for ( var i = 0; i < legends[chartNumber].length; i++) {
			var color = currentBarChart.bars[i].attr("fill");
			
			currentBarChart.labels.push(papers[chartNumber].set());
			currentBarChart.labels[i].push(papers[chartNumber].circle(legendsX + 5, entryY, 5).attr({
				fill : color,
				stroke : 'none',
				opacity : 0
			}));
	
			var txt = papers[chartNumber].text(legendsX + 20, entryY, legends[chartNumber][i]).attr(
					Raphael.g.txtattr).attr({
				fill : '#000',
				'text-anchor' : 'start',
				opacity : 0
			});
			currentBarChart.labels[i].push(txt);
			currentBarChart.bars[i].label = currentBarChart.labels[i];
			
			entryY += txt.getBBox().height * 1.2;
		}
		// make it appear in the middle
		currentBarChart.labels.translate.apply(currentBarChart.labels, [ 0,
				-currentBarChart.labels.getBBox().height / 2 ]);
		currentBarChart.push(currentBarChart.labels);
		
		// show the whole chart
		currentBarChart.animate({'opacity' : 1}, SHOW_HIDE_ANIMATION_DURATION_MS);
		
		currentBarChart.hover(function() {
			highlightChartPart(this.bar, this.bar.label);
		}, function() {
			this.bar.attr("fill", this.bar.normalColor);
			normaliseChartPart(this.bar, this.bar.label);
		});
		
		barCharts[chartNumber] = currentBarChart;
	}
}

function getChartData(chartNumber, ajaxParams) {
	if (chartDataUrl) {
		$.ajax({
			url : chartDataUrl,
			data : ajaxParams,
			async : false,
			cache : false,
			dataType : 'json',
			success : function(result) {
				legends[chartNumber] = [];
				values[chartNumber] = [];
				jQuery.each(result.data, function(index, nomination) {
					// pie chart has problems reading string values
					var value = +nomination.value;
					// pie chart has problems reading 0 values, so just skip them
					if (value > 0) {
						// a bit of formatting
						legends[chartNumber].push(value + '% - ' + nomination.name);
						values[chartNumber].push(value);
					}
				});
			}
		});
	} else {
		// see comment on top of this file
		console.log("Error: no URL defined to obtain data for charts.");
	}
}

// chart part gets a bit brighter when hovered on
function setHiglightColors(chartParts) {
	for (var i = 0; i < chartParts.length; i++) {
		var color = chartParts[i].attr("fill");
		// just for later reference when hovering
		chartParts[i].normalColor = color;
		var hsbColor = Raphael.rgb2hsb(Raphael.getRGB(color));
		// brightness delta can be higher if desired
		chartParts[i].higlightedColor = Raphael.hsb(hsbColor.h, hsbColor.s, hsbColor.b + 0.15);
	}
}

function highlightChartPart(part, label) {
	part.attr("fill", part.higlightedColor);
	// highlight the label as well
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

function normaliseChartPart(part, label) {
	part.attr("fill", part.normalColor);
	if (label) {
		label[0].animate({
			r : 5
		}, 500, "bounce");
		label[1].attr({
			"font-weight" : 400
		});
	}
}

function showChart(chart) {
	if (chart) {
		chart.show();
		chart.animate({'opacity' : 1}, SHOW_HIDE_ANIMATION_DURATION_MS);
		return true;
	}
	
	return false;
}

function hideChart(chart) {
	if (chart) {
		chart.animate({'opacity' : 0}, SHOW_HIDE_ANIMATION_DURATION_MS, 'linear', function() {
			chart.hide();
		});
		return true;
	}
	
	return false;
}