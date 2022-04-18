/**
 * Identifies data source and runs chart drawing function.
 */
function drawChart(type, chartID, dataSource, legendOnHover){
	switch (typeof dataSource) {
		case 'string': 
	// get data with the given URL
			d3.json(dataSource, function(error, response){
				if (error) {
					// forward error to browser
					throw error;
				}
				
				if (!response || $.isEmptyObject(response)) {
					// if there is no data to display
					return;
				}
		
				_drawChart(type, chartID, response.data, legendOnHover);
			});
			break;
		case 'object':
			_drawChart(type, chartID, dataSource, legendOnHover);
			break;
	}
}

/**
 * Prepares SVG area for drawing and runs concrete chart function.
 */
function _drawChart(type, chartID, rawData, legendOnHover) {
		// clear previous chart
	var chartDiv = $('#' + chartID).empty().show(),
			width = chartDiv.width(),
			//Changed for LDEV-4475 Missing information in Legend for Pie Chart when more than 7 items
			height = chartDiv.height(),
			// add SVG elem
		    svg = d3.select(chartDiv[0])
					.append('svg')
					.attr('width', width)
					.attr('height', height);
			// build domain out of keys
			domainX = rawData.map(function(d) { return d.name }),
			// 10 color palette
			scaleColor = d3.scaleOrdinal(d3.schemeCategory10)
							     .domain(domainX),
			legend = null;
			
		if (!legendOnHover) {
			legend = svg.append('g');
			// build legend first so we know how much space we've got for the chart
			$.each(rawData, function(index, d){
				// a small rectangle with proper color
				legend.append('rect')
					  .attr('x', 0)
					  .attr('y', index * 30)
					  .attr('width', 15)
					  .attr('height', 15)
					  .attr('fill', scaleColor(d.name));
				// label
				var val = Math.round(+d.value);
				if (isNaN(val)) {
					val = 0;
				}
				legend.append('text')
					  .attr('x', 20)
					  .attr('y', index * 30 + 11)
					  .attr('text-anchor', 'start')
					  .text(d.name + ' (' + val + ' %)');
			});
		}
		
		// draw proper chart
		if (type == 'bar') {
		_drawBarChart(chartDiv, legend, width, height, rawData, domainX, scaleColor);
		} else if (type == 'pie') {
		_drawPieChart(chartDiv, legend, width, height, rawData, scaleColor);
		}
}

// margins are needed so bar chart Y axis is not clipped
var CHART_MARGIN = {'top' : 10, 'left' : 40, 'right' : 20},
	MIN_BAR_WIDTH = 30;

/**
 * Draws a bar chart.
 */
function _drawBarChart(chartDiv, legend, width, height, rawData, domainX, scaleColor) {
	// if all bars easily fit in a half of SVG width, limit the drawing width
	// otherwise the chart would be too wide
	var svg = d3.select(chartDiv[0]).select('svg'),
		tooltip = legend ? null : d3.select(chartDiv[0])
									.append('div')
								    .attr('class', 'chartTooltip'),
		legendWidth = legend ? legend.node().getBBox().width : 0,
		// guess how wide the chart can be and compare it to half of available width
		canvasWidth = (rawData.length * MIN_BAR_WIDTH * 1.1 + CHART_MARGIN.left > width / 2 ?
						    width - legendWidth - (legend ? CHART_MARGIN.right : 0)
						  : width / 2) - CHART_MARGIN.left,
						  
		canvas = svg.append('g')
					// do not start drawing from the very edges as Y axis labels will be clipped
					.attr('transform', 'translate(' + CHART_MARGIN.left + ',-' + CHART_MARGIN.top + ')'),
	
		// map keys to bars
		scaleX = d3.scaleBand()
						 .domain(domainX)
			    		 .rangeRound([0, canvasWidth - CHART_MARGIN.left])
			    		 .padding(0.1),

		// map values to bar height
	    scaleY = d3.scaleLinear()
				       	 .domain([0, 1])
				       	 .range([height, CHART_MARGIN.top + 10]),
		// scale expects 0..1, we've got 0..100, this function converts it
	    y = function(value) {return scaleY(value / 100)},
	    // declare Y axis
		axisY = d3.axisLeft(scaleY)
	    		  .ticks(10, "%");
		
		// draw Y axis
		canvas.append("g")
		   .attr("class", "y axis")
		   .call(axisY);

	// draw bars
	canvas.selectAll('.bar').data(rawData).enter()
		.append('rect')
		.attr('class', 'bar')
		.attr("x", function(d) {return scaleX(d.name)})
		.attr("width", scaleX.bandwidth())
		.attr("y", function(d) {return isNaN(d.value) ? 0 : y(d.value)})
		.attr("height", function(d){return height - (isNaN(d.value) ? height : y(d.value))})
		.attr('fill', function(d) {return scaleColor(d.name)})
		.on('mouseover', function(d) {
			if (tooltip) {
				var node = d3.select(this).node(),
					offset = $(node).offset(),
					box = node.getBBox();
				tooltip.transition()		
                	   .duration(200)		
                	   .style('opacity', 1);	
				tooltip.text(d.name + ' (' + Math.round(+d.value) + ' %)')
					   .style('left', +offset.left + box.width/2 - $(tooltip.node()).width()/2 - 22 + 'px')		
					   .style('top', +offset.top - 20 + 'px');	
	            }
		})					
        .on('mouseout', function(d) {
        	if (tooltip) {
        		tooltip.transition()		
	                   .duration(500)		
	                   .style('opacity', 0);
        	}
        });
	
	if (legend) {
		// move the legend to the right of the chart
		legend.attr('transform', 'translate(' + (canvasWidth + CHART_MARGIN.right) + ',' + (CHART_MARGIN.top + 20) + ')');
	}
	
	// functions to animate changed data
	chartDiv.data('updateFunctions', {
		'y' 	 :  function(d) {return y(d.value);},
		'height' :  function(d) {return height - y(d.value);}
	});
}

/**
 * Draws a pie chart.
 */
function _drawPieChart(chartDiv, legend, width, height, rawData, scaleColor){
	// calculate how much space we've got for the chart
	var svg = d3.select(chartDiv[0]).select('svg'),
		tooltip = legend ? null : d3.select(chartDiv[0])
									.append('div')
								    .attr('class', 'chartTooltip'),
		legendWidth = legend ? legend.node().getBBox().width : 0,
		canvasWidth = width - legendWidth - (legend ? CHART_MARGIN.right : 0),
		radius = Math.min(canvasWidth, height) / 2,
		canvas = svg.append('g')
					// set centre of the pie
					.attr("transform", "translate(" + radius + "," + radius + ")"),
		// pie chunk drawing function
		arc = d3.arc()
				.outerRadius(radius - 10)
				.innerRadius(0),
		pie = d3.pie()
				// for each data element extract the value to feed chart
				.value(function(d) { return d.value });
		
	canvas.selectAll(".arc")
		  // feed processed data to the document
     	  .data(pie(rawData))
      	  .enter()
      	  .append("g")
      	  .attr("class", "arc")
      	  // draw pie chunks
	      .append("path")
	      .attr("d", arc)
	      .style("fill", function(d) { return scaleColor(d.data.name) })
		  .on('mouseover', function(d) {
			if (tooltip) {
				var node = d3.select(this).node(),
					offset = $(node).offset(),
					box = node.getBBox();
				tooltip.transition()		
	            	   .duration(200)		
	            	   .style('opacity', 1);	
				tooltip.text(d.data.name + ' (' + Math.round(+d.data.value) + ' %)')
					   .style('left', +offset.left + box.width/2 + 'px')	
					   .style('top', +offset.top + box.height/2 + 'px');	
	            }
		  })					
	      .on('mouseout', function(d) {
	    	if (tooltip) {
	    		tooltip.transition()		
	                   .duration(500)		
	                   .style('opacity', 0);
	    	}
	      });
	
	if (legend) {
		// move the legend to the right of the chart
		legend.attr('transform', 'translate(' + (radius * 2 + CHART_MARGIN.right)  + ',' + (CHART_MARGIN.top + 20) + ')');
	}
	
	// function to animate changed data
	canvas.selectAll("path").each(function(d){
		this.currentData = d;
	});
	chartDiv.data('updateFunctions', {
		'pie' : pie,
		'arcTween' : function(a) {
			var interpolation = d3.interpolate(this.currentData, a);
			this.currentData = interpolation(0);
			return function(t) {
				return arc(interpolation(t));
			};
		}
	});
}

function drawHistogram(chartID, url, xAxisLabel, yAxisLabel){

	// clear previous chart
	var xaxisTicksHeight = 35,
		xaxisHeight = 50,
	 	yaxisLabelWidth = 20,
	 	yaxisWidth = 50,
		formatCount = d3.format(",.0f"),
		chartDiv = $('#' + chartID).empty().show(),
		svgWidth = chartDiv.width(),
		svgHeightOffset = 10,
		svgHeight = chartDiv.height() - svgHeightOffset,
		margin = {top: 10, right: 40, bottom: 120, left: 0},
	    margin2 = {top: 405, right: 40, bottom: xaxisHeight, left: 0},
	    width = svgWidth - margin.left - margin.right,
	    height = svgHeight - margin.top - margin.bottom,
	    height2 = svgHeight - margin2.top - margin2.bottom;

	var svg = d3.select(chartDiv[0])
		.append("svg")
		.attr('width', svgWidth)
		.attr('height', svgHeight)
	    .append("g")
	    .attr("transform", "translate(0," + svgHeightOffset + ")");
	
	var focus = svg.append("g")
	    .attr("class", "focus")
	    .attr("transform", "translate(" + margin.left + "," + margin.top + ")");
	
	var context = svg.append("g")
	    .attr("class", "context")
	    .attr("transform", "translate(" + margin2.left + "," + margin2.top + ")");

	var brush = d3.brushX()
	    .extent([[0, 0], [width, height2]])
	    .on("brush end", _brushed);

	var	zoom;
	var x;
	var x2;
	var y = d3.scaleLinear()
		.range([height, 0]);

	var y2 = d3.scaleLinear()
		.range([height2, 0]);
	
	var xAxis;
	var xAxis2;
	var yAxis;
	var contextbar;
	var data;
	var buckets;

	// get data with the given URL
	d3.json(url, function(error, response){
		
		if (error) {
			// forward error to browser
			throw error;
		}
		
		if (!response || $.isEmptyObject(response)) {
			// if there is no data to display
			return;
		}
			
		// clear previous chart
		data = response.data;
		var	max = Number(d3.max(data))+1;
		var min = Number(d3.min(data));
		var range = max-min;
		if ( range > 11 ) {
			// tweak the range to have more space at the top so it charts better for larger numbers
			range+=3;
			max+=3;
		}
		var buckets = range <= 11 ? range : 10;

		// where the range > 11, aim for a zoom where bucket width is no more than a half a mark
		var zoomFactor = range <= 11 ? 10 : Math.round(range / 5);
		// console.log("range "+range+" zoomFactor "+zoomFactor);
		zoom = d3.zoom()
		    .scaleExtent([1, zoomFactor])
		    .translateExtent([[0, 0], [width, height]])
		    .extent([[0, 0], [width, height]])
		    .on("zoom", _zoomed);

		// x, y are top detailed graph, x2, y2 are the bottom overview graph. x, x2 declared already so that
		// so that they can be accessed by function
		x = d3.scaleLinear()
			.domain([min, max])
	    	.rangeRound([0, width]);
		
		x2 = d3.scaleLinear()
			.domain([min, max])
			.rangeRound([0, width]);

		xAxis = d3.axisBottom(x),
	    xAxis2 = d3.axisBottom(x2),
	    yAxis = d3.axisLeft(y);
		
		var histogram = d3.histogram()
	    	.domain(x.domain())
	    	.thresholds(buckets);
		
		var bins = histogram(data);

		y.domain([0, d3.max(bins, function(d) { return d.length; })]);
		y2.domain([0, d3.max(bins, function(d) { return d.length; })]);

		// top detailed histogram
		focus.append("g")
		    .attr("class", "axis axis--x")
		    .attr("transform", "translate(" + yaxisWidth +"," + height + ")")
		    .call(xAxis);

		focus.append("g")
			.attr("class", "axis axis--y")
			.attr("transform", "translate(" + yaxisWidth + ",0)")
			.call(yAxis);

		focus.append("text")
	        .attr("transform", "rotate(-90)")
	        .attr("y", 0)
	        .attr("x", 0 - (height / 2))
	        .attr("dy", "1em")
	        .style("text-anchor", "middle")
	        .text(yAxisLabel);
		
		var focusbar = generateFocusBars(x, focus, bins);
		
		// bottom overall histogram
		context.append("g")
		    .attr("class", "axis axis--x")
		    .attr("transform", "translate(" + yaxisWidth +"," + height2 + ")")
		    .call(xAxis);

		context.append("text") 
			.attr("x", (width - yaxisWidth) / 2 )
			.attr("y",  height2 + xaxisTicksHeight)
	        .style("text-anchor", "middle")
	        .text(xAxisLabel);
	
		contextbar = context.selectAll(".bar")
		    .data(bins)
		    .enter().append("g")
		    .attr("class", "bar")
		    .attr("transform", function(d) { return "translate(" + (x2(d.x0) + yaxisWidth) + "," + y2(d.length) + ")"; });
	
		contextbar.append("rect")
		    .attr("x", 1)
		    .attr("width", function(d) { return x2(d.x1) - x2(d.x0) - 1; })
		    .attr("height", function(d) { return height2 - y2(d.length); });
	
		contextbar.append("text")
		    .attr("dy", ".75em")
		    .attr("y", 6)
		    .attr("x", function(d) { return (x2(d.x1) - x2(d.x0)) / 2; })
		    .attr("text-anchor", "middle")
		    .text(function(d) { return formatCount(d.length); });
	
		context.append("g")
	      	.attr("class", "brush")
		    .attr("transform", "translate("+yaxisWidth+",0)")
	      	.call(brush)
	      	.call(brush.move, x.range());

		svg.append("rect")
			.attr("class", "zoom")
			.attr("width", width)
			.attr("height", height)
			.attr("transform", "translate(" + (margin.left + yaxisWidth) + "," + margin.top + ")")
			.call(zoom);
		  
	});


	function _brushed() {
		if (d3.event.sourceEvent && d3.event.sourceEvent.type === "zoom") return; // ignore brush-by-zoom
		var s = d3.event.selection || x2.range();
		x.domain(s.map(x2.invert, x2));
		focus.select(".axis--x").call(xAxis);
		svg.select(".zoom").call(zoom.transform, d3.zoomIdentity
		      .scale(width / (s[1] - s[0]))
		      .translate(-s[0], 0));
	}

	function _zoomed() {
		if (d3.event.sourceEvent && d3.event.sourceEvent.type === "brush") return; // ignore zoom-by-brush
		var t = d3.event.transform;
		x.domain(t.rescaleX(x2).domain());
		var histogram = d3.histogram()
	    	.domain(x.domain())
	    	.thresholds(10);
		var bins = histogram(data);
		y.domain([0, d3.max(bins, function(d) { return d.length; })]);
		yAxis = d3.axisLeft(y);
		var focusbar = generateFocusBars(x, focus, bins);
		focus.select(".axis--x").call(xAxis);
		focus.select(".axis--y").call(yAxis);
		context.select(".brush").call(brush.move, x.range().map(t.invertX, t));
	}

	function generateFocusBars(x, focus, bins) {
		focus.selectAll(".bar").remove();

		var focusbar = focus.selectAll(".bar")
		    .data(bins)
		    .enter().append("g")
		    .attr("class", "bar")
		    .attr("transform", function(d) { return "translate(" + (x(d.x0) + yaxisWidth) + "," + y(d.length) + ")"; });

		focusbar.append("rect")
		    .attr("x", 1)
		    .attr("width", function(d) { return x(d.x1) - x(d.x0) - 1; })
		    .attr("height", function(d) { return height - y(d.length); });
	
		focusbar.append("text")
		    .attr("dy", ".75em")
		    .attr("y", 6)
		    .attr("x", function(d) { return (x(d.x1) - x(d.x0)) / 2; })
		    .attr("text-anchor", "middle")
		    .text(function(d) { return formatCount(d.length); });
		
		return focusbar;
	}
}

