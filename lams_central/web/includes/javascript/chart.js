/**
 * Prepares SVG area for drawing and runs concrete chart function.
 */
function drawChart(type, chartID, url, legendOnHover){
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
		var rawData = response.data, 
			chartDiv = $('#' + chartID).empty().show(),
			width = chartDiv.width(),
			height = chartDiv.height(),
			// add SVG elem
		    svg = d3.select(chartDiv[0])
					.append('svg')
					.attr('width', width)
					.attr('height', height);
			// build domain out of keys
			domainX = rawData.map(function(d) { return d.name }),
			// 10 color palette
			scaleColor = d3.scale.category10()
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
				legend.append('text')
					  .attr('x', 20)
					  .attr('y', index * 30 + 11)
					  .attr('text-anchor', 'start')
					  .text(d.name + ' (' + Math.round(+d.value) + ' %)');
			});
		}
		
		// draw proper chart
		if (type == 'bar') {
			_drawBarChart(svg, legend, width, height, rawData, domainX, scaleColor);
		} else if (type == 'pie') {
			_drawPieChart(svg, legend, width, height, rawData, scaleColor);
		}
	});

	
}

// margins are needed so bar chart Y axis is not clipped
var CHART_MARGIN = {'top' : 10, 'left' : 40, 'right' : 20},
	MIN_BAR_WIDTH = 30;

/**
 * Draws a bar chart.
 */
function _drawBarChart(svg, legend, width, height, rawData, domainX, scaleColor) {
	// if all bars easily fit in a half of SVG width, limit the drawing width
	// otherwise the chart would be too wide
	var tooltip = legend ? null : d3.select($(svg.node()).parent()[0])
									.append('div')
								    .attr('class', 'tooltip'),
		legendWidth = legend ? legend.node().getBBox().width : 0,
		// guess how wide the chart can be and compare it to half of available width
		canvasWidth = (rawData.length * MIN_BAR_WIDTH * 1.1 + CHART_MARGIN.left > width / 2 ?
						    width - legendWidth - (legend ? CHART_MARGIN.right : 0)
						  : width / 2) - CHART_MARGIN.left,
						  
		canvas = svg.append('g')
					// do not start drawing from the very edges as Y axis labels will be clipped
					.attr('transform', 'translate(' + CHART_MARGIN.left + ',-' + CHART_MARGIN.top + ')'),
	
		// map keys to bars
		scaleX = d3.scale.ordinal()
						 .domain(domainX)
			    		 .rangeRoundBands([0, canvasWidth - CHART_MARGIN.left], .1),
		// map values to bar height
	    scaleY = d3.scale.linear()
				       	 .domain([0, 1])
				       	 .range([height, CHART_MARGIN.top + 10]),
		// scale expects 0..1, we've got 0..100, this function converts it
	    y = function(value) {return scaleY(value / 100)},
	    // declare Y axis
		axisY = d3.svg.axis()
					  .scale(scaleY)
		    		  .orient("left")
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
		.attr("width", scaleX.rangeBand())
		.attr("y", function(d) {return y(d.value)})
		.attr("height", function(d){return height - y(d.value)})
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
}

/**
 * Draws a pie chart.
 */
function _drawPieChart(svg, legend, width, height, rawData, scaleColor){
	// calculate how much space we've got for the chart
	var tooltip = legend ? null : d3.select($(svg.node()).parent()[0])
									.append('div')
								    .attr('class', 'tooltip'),
		legendWidth = legend ? legend.node().getBBox().width : 0,
		canvasWidth = width - legendWidth - (legend ? CHART_MARGIN.right : 0),
		radius = Math.min(canvasWidth, height) / 2,
		canvas = svg.append('g')
					// set centre of the pie
					.attr("transform", "translate(" + radius + "," + radius + ")"),
		// pie chunk drawing function
		arc = d3.svg.arc()
				.outerRadius(radius - 10)
				.innerRadius(0),
		pie = d3.layout.pie()
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
}