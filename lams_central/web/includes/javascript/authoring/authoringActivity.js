/**
 * This file contains methods for Activity manipulation on canvas.
 */

var ActivityLib = {
	
	/**
	 * Constructor for a Tool Activity.
	 */
	ToolActivity: function(id, toolID, x, y, label) {
		this.id = id;
		this.type = 'tool';
		this.toolID = toolID;
		
		// create activity SVG elements
		paper.setStart();
		var shape = paper.rect(x, y, layout.conf.activityWidth, layout.conf.activityHeight)
						 .attr({
							'fill' : layout.colors.activity
						 });
						
		paper.image(layout.toolIcons[toolID], shape.attr('x')
				+ shape.attr('width') / 2 - 15, shape.attr('y') + 2, 30, 30);
		
		paper.text(shape.attr('x') + shape.attr('width') / 2, shape
				.attr('y') + 40, label);
		
		this.items = paper.setFinish();
		this.items.shape = shape;
		
		ActivityLib.initActivity(this);
	},

	/**
	 * Constructor for a Grouping Activity.
	 */
	GroupingActivity : function(id, x, y) {
		this.id = id;
		this.type = 'group';
		
		// create activity SVG elements
		paper.setStart();
		var shape = paper.rect(
				x - layout.conf.activityWidth/2,
				y - layout.conf.activityHeight/2,
				layout.conf.activityWidth, layout.conf.activityHeight)
		.attr({
			'fill' : layout.colors.activity
		});
		
		paper.image('../images/grouping.gif',
				    shape.attr('x') + shape.attr('width') / 2 - 15,
				    shape.attr('y') + 2,
				    30, 30);
		
		paper.text(shape.attr('x') + shape.attr('width') / 2, shape
				.attr('y') + 40, 'Grouping');
		
		this.items = paper.setFinish();
		this.items.shape = shape;
		
		ActivityLib.initActivity(this);
	},

	/**
	 * Constructor for a Gate Activity.
	 */
	GateActivity : function(id, x, y) {
		this.id = id;
		this.type = 'gate';

		// create activity SVG elements
		paper.setStart();
		var shape = paper.path('M ' + x + ' ' + y + layout.defs.gate)
						 .attr({
							 'fill' : layout.colors.gate
						 });
		
		paper.text(x + 7, y + 14, 'STOP')
		     .attr({
				'font-size' : 9,
				'font' : 'sans-serif',
				'stroke' : layout.colors.gateText
		     });
		
		this.items = paper.setFinish();
		this.items.shape = shape;
		
		ActivityLib.initActivity(this);
	},
	
	
	BranchingEdgeActivity : function(id, x, y, branchingActivity) {
		this.type = 'branchingEdge';
		if (branchingActivity) {
			this.isStart = false;
			branchingActivity.end = this;
		} else {
			this.isStart = true;
			branchingActivity = new ActivityLib.BranchingActivity(id, this);
		}
		this.branchingActivity = branchingActivity;
			
		// create activity SVG elements
		paper.setStart();
		var shape = paper.path('M ' + x + ' ' + y +
			(this.isStart ? layout.defs.branchingEdgeStart : layout.defs.branchingEdgeEnd))
						 .attr({
							 'fill' : this.isStart ? layout.colors.branchingEdgeStart
						                           : layout.colors.branchingEdgeEnd
						 });
						
		paper.text(x, y + 14,  this.isStart ? 'Branching point'
	                                        : 'Converge point')
		     .attr({
				'font-size' : 9,
				'font' : 'sans-serif'
		     });
		
		this.items = paper.setFinish();
		this.items.shape = shape;
		
		ActivityLib.initActivity(this);
	},
	
	
	BranchingActivity : function(id, branchingEdgeStart) {
		this.id = id;
		this.start = branchingEdgeStart;
		this.branches = [];
	},
	
	
	BranchActivity : function(id, branchingActivity, transitionFrom) {
		this.id = id;
		this.transitionsFrom = transitionFrom;
		this.branchingActivity = branchingActivity;
		branchingActivity.branches.push(this);
	},
	
	
	/**
	 * Make a new activity fully functional on canvas.
	 */
	initActivity : function(activity) {	
		activity.items.mousedown(function(event, x, y){
			if (event.ctrlKey) {
				 // when CTRL is held down, start drawing a transition
				 HandlerLib.drawTransitionStartHandler(activity, event, x, y);
			} else {
				// start dragging the activity
				var mouseupHandler = function(){
					HandlerLib.dragActivityEndHandler(activity);
				};

				HandlerLib.dragItemsStartHandler(activity.items, this, mouseupHandler, event, x, y);
			}
		})
		.click(function(event){
			// inform that user wants to select, not drag the activity
			activity.items.clicked = true;
			HandlerLib.selectActivityHandler(event, activity);
		})
		.dblclick(function(){
			// inform that user wants to open, not drag the activity
			activity.items.clicked = true;
			HandlerLib.openActivityAuthoringHandler(activity);
		})
		.attr({
			'cursor' : 'pointer'
		});
		
		if (activity.type == 'branchingEdge' && activity.branchingActivity.end) {
			var startItems = activity.branchingActivity.start.items;
			var endItems = activity.branchingActivity.end.items;
			var mouseover = function(){
				if (!startItems.isDragged && !endItems.isDragged) {
					startItems.shape.attr('fill', layout.colors.branchingEdgeMatch);
					endItems.shape.attr('fill', layout.colors.branchingEdgeMatch);
				}
			};
			var mouseout = function(){
				if (!startItems.isDragged && !endItems.isDragged) {
					startItems.shape.attr('fill', layout.colors.branchingEdgeStart);
					endItems.shape.attr('fill', layout.colors.branchingEdgeEnd);
				}
			};
			startItems.shape.hover(mouseover, mouseout);
			endItems.shape.hover(mouseover, mouseout);
		}
	},

	
	/**
	 * Deletes the given activity.
	 */
	removeActivity : function(activity, forceRemove) {
		if (!forceRemove && activity.type == 'branchingEdge'){
			if (confirm('Are you sure you want to remove the whole branching activity?')){
				var otherEdge = activity.isStart ? activity.branchingActivity.end
						                         : activity.branchingActivity.start;
				ActivityLib.removeActivity(otherEdge, true);
			} else {
				return;
			}
		}
		
		// remove the transitions
		if (activity.fromTransition) {
			var toActivity = activity.fromTransition.toActivity;
			// if grouping activity is gone, remove the grouping effect
			if (activity.type == 'group' && toActivity.items.groupingEffect) {
				toActivity.items.groupingEffect.remove();
				toActivity.items.groupingEffect = null;
			}
			toActivity.toTransition = null;
			activity.fromTransition.remove();
		}
		if (activity.toTransition){
			activity.toTransition.fromActivity.fromTransition = null
			activity.toTransition.remove();
		}
		
		// remove the activity from activities table
		activities.splice(activities.indexOf(activity), 1);
		// visually remove the activity
		activity.items.remove();
	},
	
	
	/**
	 * Draws a transition between two activities.
	 */
	drawTransition : function(fromActivity, toActivity) {
		// remove the existing activities
		if (fromActivity.fromTransition) {
			fromActivity.fromTransition.remove();
		}
		if (toActivity.toTransition) {
			toActivity.toTransition.remove();
		}
		
		// calculate middle points of each activity
		var fromActivityBox = fromActivity.items.shape.getBBox();
		var toActivityBox = toActivity.items.shape.getBBox();
		var startX = fromActivityBox.x + fromActivityBox.width / 2;
		var startY = fromActivityBox.y + fromActivityBox.height / 2;
		var endX   =   toActivityBox.x + toActivityBox.width / 2;
		var endY   =   toActivityBox.y + toActivityBox.height / 2;
		
		// do the actual drawing
		paper.setStart();
		paper.path('M ' + startX + ' ' + startY + ' L  ' + endX + ' ' + endY)
		                .attr({
		                	'stroke'       : layout.colors.transition,
		                	'stroke-width' : 2
		                });

		// draw the arrow and turn it in the same direction as the line
		var angle = 90 + Math.atan2(endY - startY, endX - startX) * 180 / Math.PI;
		var arrowX = (startX + (endX - startX)/2);
		var arrowY = (startY + (endY - startY)/2);
		var arrowPath = Raphael.transformPath('M ' + arrowX + ' ' + arrowY + layout.defs.transArrow, 
				                        'R ' + angle + ' ' + arrowX + ' ' + arrowY);
		paper.path(arrowPath)
						 .attr({
							'stroke' : layout.colors.transition,
							'fill'   : layout.colors.transition
						 });
		transition = paper.setFinish();
		
		// set up references in activities and the transition
		transition.fromActivity = fromActivity;
		transition.toActivity = toActivity;
		fromActivity.fromTransition = transition;
		toActivity.toTransition = transition;
		transition.toBack();
		transition.mousedown(function(event, x, y){
			// allow transition dragging
			var mouseupHandler = function(){
				HandlerLib.dragTransitionEndHandler(transition);
			};
			HandlerLib.dragItemsStartHandler(transition, this, mouseupHandler, event, x, y)
		});
		
		// add grouping effect if previous activity is of grouping type
		if (fromActivity.type == 'group' && toActivity.type != 'gate') {
			ActivityLib.addGroupingEffect(toActivity);
		}
	},
	
	
	/**
	 * Removes the given transition.
	 */
	removeTransition : function(transition) {
		if (transition.toActivity.items.groupingEffect) {
			transition.toActivity.items.groupingEffect.remove();
			transition.toActivity.items.groupingEffect = null;
		}
		transition.fromActivity.fromTransition = null;
		transition.toActivity.toTransition = null;
		transition.remove();
	},
	
	
	/**
	 * Draws an extra border around the selected activity.
	 */
	addSelectEffect : function (activity) {
		// do not draw twice
		if (!activity.items.selectEffect) {
			var box = activity.items.shape.getBBox();
			
			// a simple rectange a bit wider than the actual activity boundaries
			activity.items.selectEffect = paper.rect(
					box.x - 7,
					box.y - 7,
					box.width + 14,
					box.height + 14)
				.attr({
					'stroke'           : layout.colors.selectEffect,
					'stroke-dasharray' : '-'
				});
			activity.items.push(activity.items.selectEffect);
			
			// show the properties dialog for the selected activity
			layout.items.selectedActivity = activity;
			layout.items.propertiesDialog.dialog('open');
		}
	},
	
	
	/**
	 * Adds visual grouping effect on an activity.
	 */
	addGroupingEffect : function(activity) {
		if (!activity.items.groupingEffect) {
			var shape = activity.items.shape;
			
			activity.items.groupingEffect = paper.rect(
					shape.attr('x') + 5,
					shape.attr('y') + 5,
					layout.conf.activityWidth,
					layout.conf.activityHeight)
				.attr({
					'fill' : shape.attr('fill')
				})
				.toBack();
			activity.items.push(activity.items.groupingEffect);
		}
	}
};