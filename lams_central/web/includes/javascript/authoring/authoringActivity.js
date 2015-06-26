﻿/**
 * This file contains methods for Activity definition and manipulation on canvas.
 */

/**
 * Stores different Activity types structures.
 */
var ActivityDefs = {
		
	/**
	 * Either branching (start) or converge (end) point.
	 */
	BranchingEdgeActivity : function(id, uiid, x, y, title, branchingType, branchingActivity) {
		this.transitions = {
			'from' : [],
			'to'   : []
		};
		
		if (branchingActivity) {
			// branchingActivity already exists, so this is the converge point
			this.isStart = false;
			branchingActivity.end = this;
		} else {
			// this is the branching point
			this.isStart = true;
			branchingActivity = new ActivityDefs.BranchingActivity(id, uiid, this);
			branchingActivity.branchingType = branchingType || 'chosen';
			branchingActivity.title = title || LABELS.DEFAULT_BRANCHING_TITLE;
		}
		this.branchingActivity = branchingActivity;
		
		if (!isReadOnlyMode){
			this.loadPropertiesDialogContent = PropertyDefs.branchingProperties;
		}
		
		this.draw = ActivityDraw.branching;
		this.draw(x, y);
	},
	
	
	/**
	 * Represents a set of branches. It is not displayed on canvas, but holds all the vital data.
	 */
	BranchingActivity : function(id, uiid, branchingEdgeStart) {
		this.id = +id || null;
		this.uiid = +uiid || ++layout.ld.maxUIID;
		this.start = branchingEdgeStart;
		this.branches = [];
		// mapping between groups and branches, if applicable
		this.groupsToBranches = [];
		// mapping between tool output and branches, if applicable
		this.conditionsToBranches = [];
		
		this.minOptions = 0;
		this.maxOptions = 0;
	},
	
	
	/**
	 * Represents a subsequence of activities. It is not displayed on canvas, but is the parent activity for its children.
	 */
	BranchActivity : function(id, uiid, title, branchingActivity, transitionFrom, defaultBranch) {
		this.id = +id || null;
		this.uiid = +uiid || ++layout.ld.maxUIID;
		this.title = title || (LABELS.DEFAULT_BRANCH_PREFIX + (branchingActivity.branches.length + 1));
		this.branchingActivity = branchingActivity;
		this.transitionFrom = transitionFrom;
		if (defaultBranch) {
			this.defaultBranch = true;
			// there can be only one default branch
			$.each(branchingActivity.branches, function(){
				this.defaultBranch = false;
			});
		}
	},
	
	
	/**
	 * Constructor for a Floating Activity.
	 */
	FloatingActivity : function(id, uiid, x, y) {
		DecorationDefs.Container.call(this, id, uiid, LABELS.SUPPORT_ACTIVITY_TITLE);
		
		this.draw = ActivityDraw.floatingActivity;
		this.draw(x, y);
		
		// there can only be one Floating Activity container
		layout.floatingActivity = this;
	},
	
	
	/**
	 * Constructor for a Gate Activity.
	 */
	GateActivity : function(id, uiid, x, y, title, description, gateType, startTimeOffset, gateActivityCompletionBased) {
		this.id = +id || null;
		this.uiid = +uiid || ++layout.ld.maxUIID;
		this.title = title;
		this.description = description;
		this.gateType = gateType || 'permission';
		if (gateType == 'schedule') {
			var day = 24*60;
			this.offsetDay = Math.floor(startTimeOffset/day);
			startTimeOffset -= this.offsetDay * day;
			this.offsetHour = Math.floor(startTimeOffset/60);
			this.offsetMinute = startTimeOffset - this.offsetHour * 60;
			
			this.gateActivityCompletionBased = gateActivityCompletionBased;
		};
		// mapping between tool output and gate states ("branches"), if applicable
		this.conditionsToBranches = [];
		this.transitions = {
			'from' : [],
			'to'   : []
		};
		
		if (!isReadOnlyMode){
			this.loadPropertiesDialogContent = PropertyDefs.gateProperties;
		}
		
		this.draw = ActivityDraw.gate;
		this.draw(x, y);
	},
	
	
	/**
	 * Constructor for a Grouping Activity.
	 */
	GroupingActivity : function(id, uiid, x, y, title, groupingID, groupingUIID, groupingType, groupDivide,
								groupCount, learnerCount, equalSizes, viewLearners, groups) {
		this.id = +id || null;
		this.groupingID = +groupingID || null;
		this.groupingUIID = +groupingUIID  || ++layout.ld.maxUIID;
		this.uiid = +uiid || ++layout.ld.maxUIID;
		this.title = title || LABELS.DEFAULT_GROUPING_TITLE;
		this.groupingType = groupingType || 'random';
		this.groupDivide = groupDivide || 'groups';
		this.groupCount = +groupCount || layout.conf.defaultGroupingGroupCount;
		this.learnerCount = +learnerCount || layout.conf.defaultGroupingLearnerCount;
		this.equalSizes = equalSizes || false;
		this.viewLearners = viewLearners || false;
		// either groups are already defined or create them with default names
		this.groups = groups || PropertyLib.fillNameAndUIIDList(this.groupCount, [], 'name', LABELS.DEFAULT_GROUP_PREFIX);
		this.transitions = {
			'from' : [],
			'to'   : []
		};
		
		if (!isReadOnlyMode){
			this.loadPropertiesDialogContent = PropertyDefs.groupingProperties;
		}
		this.draw = ActivityDraw.grouping;
		this.draw(x, y);
	},
	
	
	/**
	 * Constructor for an Optional Activity.
	 */
	OptionalActivity : function(id, uiid, x, y, title, minOptions, maxOptions) {
		DecorationDefs.Container.call(this, id, uiid, title || LABELS.DEFAULT_OPTIONAL_ACTIVITY_TITLE);
		
		this.id = +id || null;
		this.uiid = +uiid || ++layout.ld.maxUIID;
		this.minOptions = minOptions || 0;
		this.maxOptions = maxOptions || 0;
		this.transitions = {
			'from' : [],
			'to'   : []
		};
		
		if (!isReadOnlyMode){
			this.loadPropertiesDialogContent = PropertyDefs.optionalActivityProperties;
		}
		this.draw = ActivityDraw.optionalActivity;
		this.draw(x, y);
	},
	
	
	/**
	 * Constructor for a Parallel (double) Activity
	 */
	ParallelActivity : function(id, uiid, learningLibraryID, x, y, title, childActivities){
		DecorationDefs.Container.call(this, id, uiid, title);
		
		this.id = +id || null;
		this.uiid = +uiid || ++layout.ld.maxUIID;
		this.learningLibraryID = +learningLibraryID;
		this.transitions = {
			'from' : [],
			'to'   : []
		};
		if (childActivities){
			this.childActivities = childActivities;
		}
		
		if (!isReadOnlyMode){
			this.loadPropertiesDialogContent = PropertyDefs.parallelProperties;
		}
		
		this.draw = ActivityDraw.parallelActivity;
		this.draw(x, y);
	},
	
	
	/**
	 * Constructor for a Tool Activity.
	 */
	ToolActivity : function(id, uiid, toolContentID, toolID, learningLibraryID, authorURL, x, y, title) {
		this.id = +id || null;
		this.uiid = +uiid || ++layout.ld.maxUIID;
		this.toolContentID = toolContentID;
		this.toolID = +toolID;
		this.learningLibraryID = +learningLibraryID;
		this.authorURL = authorURL;
		this.title = title;
		this.transitions = {
			'from' : [],
			'to'   : []
		};
		
		if (!isReadOnlyMode){
			this.loadPropertiesDialogContent = PropertyDefs.toolProperties;
		}
		
		this.draw = ActivityDraw.tool;
		this.draw(x, y);
	},
	
	
	/**
	 * Constructor for a Transition
	 */
	Transition : function(id, uiid, fromActivity, toActivity, title) {
		this.id = +id || null;
		this.uiid = +uiid || ++layout.ld.maxUIID;
		this.fromActivity = fromActivity;
		this.toActivity = toActivity;
		if (title) {
			// only branches have titles
			this.title = title;
			
			if (!isReadOnlyMode){
				this.loadPropertiesDialogContent = PropertyDefs.transitionProperties;
			}
		}
		
		this.draw = ActivityDraw.transition;
		this.draw();
		
		// set up references in edge activities
		fromActivity.transitions.from.push(this);
		toActivity.transitions.to.push(this);
	}
},


/**
 * Mehtods for drawing various kinds of activities.
 * They are not defined in constructors so there is a static reference, 
 * not a separate definition for each object instance.
 */
ActivityDraw = {
	
	/**
	 * Draws a Branching Activity
	 */
	branching : function(x, y) {
		if (x == undefined || y == undefined) {
			// just redraw the activity
			x = this.items.shape.getBBox().x;
			y = this.items.shape.getBBox().y;
		}
		
		if (this.items) {
			this.items.remove();
		}
		
		// create activity SVG elements
		this.items = Snap.set();
		var shape = paper.path(Snap.format('M {x} {y} a 8 8 0 1 0 16 0 a 8 8 0 1 0 -16 0',
										   {
											'x' : x,
											'y' : y + 8
										   })
							  )
						 .attr({
							 'stroke' : layout.colors.activityBorder,
							 'fill' : this.isStart ? layout.colors.branchingEdgeStart
						                           : layout.colors.branchingEdgeEnd
						 }),
			title = this.branchingActivity.title,
			label = paper.text(x + 8, y + 27,  title + ' ' + (this.isStart ? LABELS.BRANCHING_START_SUFFIX
	                                        		 	 				   : LABELS.BRANCHING_END_SUFFIX))
	                     .attr(layout.defaultTextAttributes);
		
		this.items.push(shape).push(label);
		this.items.shape = shape;
		
		ActivityLib.activityHandlersInit(this);
	},
	
	
	/**
	 * Draws a Floating (support) Activity container
	 */
	floatingActivity : function(x, y, ignoredParam1, ignoredParam2, childActivities) {
		if (x == undefined || y == undefined) {
			// if no new coordinates are given, just redraw the activity
			x = this.items.shape.getBBox().x;
			y = this.items.shape.getBBox().y;
		}
		
		// either check what children are on canvas or use the priovided parameter
		if (childActivities) {
			this.childActivities = childActivities;
		}
		
		if (this.childActivities && this.childActivities.length > 0) {
			// draw one by one, horizontally
			var activityX = x + layout.conf.containerActivityPadding,
				allElements = Snap.set(),
				floatingActivity = this,
				box = this.items.shape.getBBox();
			$.each(this.childActivities, function(orderID){
				this.parentActivity = floatingActivity;
				this.orderID = orderID;
				var childBox = this.items.shape.getBBox();
				this.draw(activityX, y + Math.max(layout.conf.containerActivityPadding + 10, (box.height - childBox.height)/2));
				childBox = this.items.shape.getBBox();
				activityX = childBox.x2 + layout.conf.containerActivityChildrenPadding;
				allElements.push(this.items.shape);
			});
			// area containing all drawn child activities
			box = allElements.getBBox();
			
			this.drawContainer(x, y,
							   box.x2 + layout.conf.containerActivityPadding,
							   box.y2 + layout.conf.containerActivityPadding,
							   layout.colors.optionalActivity);
		} else {
			this.drawContainer(x, y,
							   x + layout.conf.containerActivityEmptyWidth,
							   y + layout.conf.containerActivityEmptyHeight,
							   layout.colors.optionalActivity);
		}
		
		GeneralLib.applyToSet(this.items, 'data', ['parentObject', this]);
	},
	
	
	/**
	 * Draws a Gate activity
	 */
	gate : function(x, y) {
		if (x == undefined || y == undefined) {
			x = this.items.shape.getBBox().x;
			y = this.items.shape.getBBox().y;
		}

		if (this.items) {
			this.items.remove();
		}
		
		// create activity SVG elements
		this.items = Snap.set();
		var shape = paper.path(Snap.format('M {x} {y} l-9 9 v16 l9 9 h16 l9 -9 v-16 l-9 -9 z',
										   {
											'x' : x + 9,
											'y' : y
										   })
							  )
						 .attr({
							 'stroke' : layout.colors.activityBorder,
							 'fill'   : layout.colors.gate
						 }),
			label = paper.text(x + 17, y + 20, LABELS.GATE_ACTIVITY_LABEL)
						 .attr(layout.defaultTextAttributes)
						 .attr('stroke', layout.colors.gateText);
		
		this.items.push(shape).push(label);
		this.items.shape = shape;
		
		ActivityLib.activityHandlersInit(this);
	},
	
	
	/**
	 * Draws a Gropuing activity
	 */
	grouping : function(x, y) {
		if (x == undefined || y == undefined) {
			// just redraw the activity
			x = this.items.shape.getBBox().x;
			y = this.items.shape.getBBox().y;
		}

		if (this.items) {
			this.items.remove();
		}
		
		// create activity SVG elements
		this.items = Snap.set();
		var shape = paper.path(Snap.format('M {x} {y} h 125 v 50 h -125 z',
										   {
											'x' : x,
											'y' : y
										   })
							  )
						 .attr({
							    'stroke' : layout.colors.activityBorder,
								'fill' : layout.colors.grouping
							 }),
			icon = paper.image('../images/grouping.gif', x + 47, y + 2, 30, 30),
			label = paper.text(x + 62, y + 40, ActivityLib.shortenActivityTitle(this.title))
						 .attr(layout.defaultTextAttributes);
		
		this.items.push(shape).push(icon).push(label);
		this.items.shape = shape;
		
		ActivityLib.activityHandlersInit(this);
	},
	
	
	/**
	 * Draws an Optional Activity container
	 */
	optionalActivity : function(x, y, ignoredParam1, ignoredParam2, childActivities) {
		if (x == undefined || y == undefined) {
			// if no new coordinates are given, just redraw the activity
			x = this.items.shape.getBBox().x;
			y = this.items.shape.getBBox().y;
		}
		
		// either check what children are on canvas or use the priovided parameter
		if (childActivities) {
			this.childActivities = childActivities;
		}
		
		if (this.childActivities && this.childActivities.length > 0) {
			// draw one by one, vertically
			var activityY = y + layout.conf.containerActivityPadding + 10,
				allElements = Snap.set(),
				optionalActivity = this,
				box = this.items.shape.getBBox(),
				boxWidth = box.width;
			$.each(this.childActivities, function(orderID){
				this.parentActivity = optionalActivity;
				this.orderID = orderID + 1;
				var childBox = this.items.shape.getBBox();
				this.draw(x + Math.max(layout.conf.containerActivityPadding, (boxWidth - childBox.width)/2), activityY);
				childBox = this.items.shape.getBBox();
				activityY = childBox.y2 + layout.conf.containerActivityChildrenPadding;
				allElements.push(this.items.shape);
			});
			// area containing all drawn child activities
			box = allElements.getBBox();
			
			this.drawContainer(x, y,
							  box.x2 + layout.conf.containerActivityPadding,
							  box.y2 + layout.conf.containerActivityPadding,
							  layout.colors.optionalActivity);
		} else {
			this.drawContainer(x, y,
							   x + layout.conf.containerActivityEmptyWidth,
							   y + layout.conf.containerActivityEmptyHeight,
							   layout.colors.optionalActivity);
		}
		
		if (!isReadOnlyMode){
			// allow transition drawing and other activity behaviour
			GeneralLib.applyToSet(this.items, 'unmousedown');
			GeneralLib.applyToSet(this.items, 'mousedown', [HandlerActivityLib.activityMousedownHandler]);
		}
		
		GeneralLib.applyToSet(this.items, 'data', ['parentObject', this]);
	},
	
	
	/**
	 * Draws a Parallel (double) Activity container
	 */
	parallelActivity : function(x, y) {
		// if no new coordinates are given, just redraw the activity or give default value
		if (x == undefined) {
			x = this.items ? this.items.shape.getBBox().x : 0;
		}
		if (y == undefined) {
			y = this.items ? this.items.shape.getBBox().y : 0;
		}
		
		if (this.childActivities && this.childActivities.length > 0) {
			// draw one by one, vertically
			var activityY = y + layout.conf.containerActivityPadding + 10,
				allElements = Snap.set(),
				optionalActivity = this;
			$.each(this.childActivities, function(orderID){
				this.parentActivity = optionalActivity;
				this.orderID = orderID + 1;
				this.draw(x + layout.conf.containerActivityPadding, activityY);
				activityY = this.items.shape.getBBox().y2 + layout.conf.containerActivityChildrenPadding;
				allElements.push(this.items.shape);
			});
			// area containing all drawn child activities
			var box = allElements.getBBox();
			
			this.drawContainer(x, y,
							  box.x2 + layout.conf.containerActivityPadding,
							  box.y2 + layout.conf.containerActivityPadding,
							  layout.colors.optionalActivity);
		} else {
			this.drawContainer(x, y,
							   x + layout.conf.containerActivityEmptyWidth,
							   y + layout.conf.containerActivityEmptyHeight,
							   layout.colors.optionalActivity);
		}
		
		if (this.grouping) {
			ActivityLib.addGroupingEffect(this);
		}
		
		if (!isReadOnlyMode){
			// allow transition drawing and other activity behaviour
			GeneralLib.applyToSet(this.items, 'unmousedown');
			GeneralLib.applyToSet(this.items, 'mousedown', [HandlerActivityLib.activityMousedownHandler]);
		}
		
		GeneralLib.applyToSet(this.items, 'data', ['parentObject', this]);
	},
	
	
	/**
	 * Draws a Tool activity
	 */
	tool : function(x, y) {
		if (x == undefined || y == undefined) {
			// if no new coordinates are given, just redraw the activity
			x = this.items.shape.getBBox().x;
			y = this.items.shape.getBBox().y;
		}
		
		if (this.items) {
			this.items.remove();
		}
		
		// create activity SVG elements
		this.items = Snap.set();
		var shape = paper.path(Snap.format('M {x} {y} h 125 v 50 h -125 z',
										   {
											'x' : x,
											'y' : y
										   }))
						 // activity colour depends on its category ID
						 .attr({
							'stroke' : layout.colors.activityBorder,
							'fill' : layout.colors.activity[layout.toolMetadata[this.learningLibraryID].activityCategoryID]
						 }),
			// check for icon in the library
			icon =  paper.image(layout.toolMetadata[this.learningLibraryID].iconPath, x + 47, y + 3, 30, 30),
			label = paper.text(x + 62, y + 43, ActivityLib.shortenActivityTitle(this.title))
			 			 .attr(layout.defaultTextAttributes)
			 			 .attr('fill', layout.colors.activityText);
			 			 
		this.items.push(shape).push(icon).push(label);
		this.items.shape = shape;
		
		if (this.grouping) {
			ActivityLib.addGroupingEffect(this);
		}
		
		ActivityLib.activityHandlersInit(this);
	},
	
	
	/**
	 * Draws a Transition
	 */
	transition : function() {
		// clear previous canvas elements
		if (this.items) {
			this.items.remove();
		}
		
		// calculate middle points of each activity
		var points = ActivityLib.findTransitionPoints(this.fromActivity, this.toActivity);
		
		// create transition SVG elements
		this.items = Snap.set();
		var arrowShaft = paper.path(Snap.format('M {startX} {startY} L {endX} {endY}', points))
				              .attr({
						          	 'stroke'       : layout.colors.transition,
						        	 'stroke-width' : 2
				              	  }),
			// draw the arrow and turn it in the same direction as the line
			angle = 90 + Math.atan2(points.endY - points.startY, points.endX - points.startX) * 180 / Math.PI,
			arrowPath = paper.path(Snap.format('M {middleX} {middleY} l 10 15 a 25 25 0 0 0 -20 0 z', points))
						     .transform(Snap.format('R {angle} {points.middleX} {points.middleY}',
						    		                {
						    	 					 'angle' : angle,
						    	 					 'points' : points
						    	 					})
						    	 	   )
						     .attr({
								'stroke' : layout.colors.transition,
								'fill'   : layout.colors.transition
							 });
		this.items.push(arrowShaft).push(arrowPath);
		if (this.title) {
			// adjust X & Y depending on the angle, so the label does not overlap with the transition;
			// angle in Javascript is -90 <= a <= 270
			var label = paper.text(points.middleX + ((angle > -45 && angle < 45) || (angle > 135 && angle < 225) ? 20 : 0),
					   			   points.middleY + ((angle > 45 && angle < 135) || angle > 225 || angle < 45 ? -20 : 0),
					   			   this.title)
					   	     .attr(layout.defaultTextAttributes)
					   	     .attr('text-anchor', 'start');
			
			this.items.push(label);
		}

		GeneralLib.toBack(this.items);
		
		// region annotations could cover grouping effect
		$.each(layout.regions, function(){
			GeneralLib.toBack(this.items.shape);
		});
		
		GeneralLib.applyToSet(this.items, 'data', ['parentObject', this]);
		
		if (!isReadOnlyMode){
			GeneralLib.applyToSet(this.items, 'attr', ['cursor', 'pointer']);
			GeneralLib.applyToSet(this.items, 'mousedown', [HandlerTransitionLib.transitionMousedownHandler]);
			GeneralLib.applyToSet(this.items, 'click', [HandlerLib.itemClickHandler]);
		}
	}
},



/**
 * Contains utility methods for Activity manipulation.
 */
ActivityLib = {
		
	/**
	 * Make a new activity fully functional on canvas.
	 */
	activityHandlersInit : function(activity) {
		GeneralLib.applyToSet(activity.items, 'data', ['parentObject', activity]);
		
		if (isReadOnlyMode) {
			if (activitiesOnlySelectable) {
				GeneralLib.applyToSet(activity.items, 'click', [HandlerLib.itemClickHandler]);
				GeneralLib.applyToSet(activity.items, 'attr', [{'cursor' : 'pointer'}]);
			}
		} else {
			// set all the handlers
			GeneralLib.applyToSet(activity.items, 'mousedown', [HandlerActivityLib.activityMousedownHandler]);
			GeneralLib.applyToSet(activity.items, 'click', [HandlerLib.itemClickHandler]);
			GeneralLib.applyToSet(activity.items, 'dblclick', [HandlerActivityLib.activityDblclickHandler]);
			GeneralLib.applyToSet(activity.items, 'attr', [{'cursor' : 'pointer'}]);
			
			if (activity instanceof ActivityDefs.BranchingEdgeActivity
					&& activity.branchingActivity.end) {
				// highligh branching edges on hover
				GeneralLib.applyToSet(activity.branchingActivity.start.items, 'hover',
						[HandlerActivityLib.branchingEdgeMouseoverHandler,
						HandlerActivityLib.branchingEdgeMouseoutHandler]);
				GeneralLib.applyToSet(activity.branchingActivity.end.items, 'hover',
						[HandlerActivityLib.branchingEdgeMouseoverHandler,
						HandlerActivityLib.branchingEdgeMouseoutHandler]);
			}
		}

	},

	
	
	/**
	 * Adds branching activity when user draws an extra outbout transition from.
	 */
	addBranching : function(fromActivity, toActivity1) {
		// find the other toActivity
		var existingTransition = fromActivity.transitions.from[0],
			toActivity2 = existingTransition.toActivity,
			branchingEdgeStart = null,
			branchingEdgeEnd = null,
			convergeActivity1 = toActivity1,
		    convergeActivity2 = toActivity2;
		// find converge activity of the new branch
		while (convergeActivity1.transitions.from.length > 0) {
			convergeActivity1 = convergeActivity1.transitions.from[0].toActivity;
		};
		
		if (toActivity2 instanceof ActivityDefs.BranchingEdgeActivity && toActivity2.isStart) {
			// there is already a branching activity, reuse existing items
			branchingEdgeStart = toActivity2;
			branchingEdgeEnd = toActivity2.branchingActivity.end;
		} else {
			// add new branching
			ActivityLib.removeTransition(existingTransition);
			
			// calculate position of branching point
			var branchPoints1 = ActivityLib.findTransitionPoints(fromActivity, toActivity1),
			    branchPoints2 = ActivityLib.findTransitionPoints(fromActivity, toActivity2),
			    branchEdgeStartX = branchPoints1.middleX + (branchPoints2.middleX - branchPoints1.middleX)/2,
			    branchEdgeStartY = branchPoints1.middleY + (branchPoints2.middleY - branchPoints1.middleY)/2,
			    branchingEdgeStart = new ActivityDefs.BranchingEdgeActivity(null, null, branchEdgeStartX,
			    		branchEdgeStartY, null, null, null);
			layout.activities.push(branchingEdgeStart);
			
			// find last activities in subsequences and make an converge point between them
			while (convergeActivity2.transitions.from.length > 0) {
				convergeActivity2 = convergeActivity2.transitions.from[0].toActivity;
			};

			var convergePoints = ActivityLib.findTransitionPoints(convergeActivity1, convergeActivity2),
				branchingEdgeEnd = new ActivityDefs.BranchingEdgeActivity(null, null, convergePoints.middleX,
					convergePoints.middleY, null, null, branchingEdgeStart.branchingActivity);
			layout.activities.push(branchingEdgeEnd);
			
			// draw all required transitions
			ActivityLib.addTransition(fromActivity, branchingEdgeStart);
			ActivityLib.addTransition(branchingEdgeStart, toActivity2);
			ActivityLib.addTransition(convergeActivity2, branchingEdgeEnd);
		}

		ActivityLib.addTransition(branchingEdgeStart, toActivity1);
		ActivityLib.addTransition(convergeActivity1, branchingEdgeEnd);
		GeneralLib.setModified(true);
	},
	

	
	/**
	 * Adds visual grouping effect on an activity.
	 */
	addGroupingEffect : function(activity) {
		// do not draw twice if it already exists
		if (!activity.items.groupingEffect) {
			var shape = activity.items.shape,
				activityBox = shape.getBBox();
			
			activity.items.groupingEffect = paper.rect(
					activityBox.x + layout.conf.groupingEffectPadding,
					activityBox.y + layout.conf.groupingEffectPadding,
					activityBox.width,
					activityBox.height)
				.attr({
					'stroke' : layout.colors.activityBorder,
					'fill' : shape.attr('fill')
				});
			
			GeneralLib.toBack(activity.items.groupingEffect);
			activity.items.push(activity.items.groupingEffect);
			
			// region annotations could cover grouping effect
			$.each(layout.regions, function(){
				GeneralLib.toBack(this.items.shape);
			});
		}
	},
	
	
	/**
	 * Adds visual select effect around an activity.
	 */
	addSelectEffect : function (object, markSelected) {
		// do not draw twice
		if (!object.items.selectEffect) {
			// different effects for different types of objects
			if (object instanceof DecorationDefs.Region) {
				object.items.shape.attr({
					'stroke'           : layout.colors.selectEffect,
					'stroke-dasharray' : '5,3'
				});
				object.items.selectEffect = true;
				
				if (!isReadOnlyMode) {
					object.items.resizeButton.attr('display', null);
					GeneralLib.toFront(object.items.resizeButton);
					// also select encapsulated activities
					var childActivities = DecorationLib.getChildActivities(object.items.shape);
					if (childActivities.length > 0) {
						object.items.fitButton.attr('display', null);
						
						$.each(childActivities, function(){
							if (!this.parentActivity || !(this.parentActivity instanceof DecorationDefs.Container)) {
								ActivityLib.addSelectEffect(this, false);
							}
						});
					}
				}
			} else if (object instanceof ActivityDefs.Transition) {
				// show only if Transition is selectable, i.e. is a branch, has a title
				if (object.loadPropertiesDialogContent) {
					object.items.attr({
						'stroke' : layout.colors.selectEffect,
						'fill'   : layout.colors.selectEffect
					 });
					
					object.items.selectEffect = true;
				}
			} else {
				// this goes for ActivityDefs and Labels
				var box = object.items.getBBox();
				
				// a simple rectange a bit wider than the actual activity boundaries
				object.items.selectEffect = paper.path(Snap.format('M {x} {y} h {width} v {height} h -{width} z',
								   {
									'x'      : box.x - layout.conf.selectEffectPadding,
									'y'      : box.y - layout.conf.selectEffectPadding,
									'width'  : box.width + 2*layout.conf.selectEffectPadding,
									'height' : box.height + 2*layout.conf.selectEffectPadding
								   }))
							.attr({
									'stroke'           : layout.colors.selectEffect,
									'stroke-dasharray' : '5,3',
									'fill' : 'none'
								});
				object.items.push(object.items.selectEffect);
				
				// if it's "import part" select children activities
				if (activitiesOnlySelectable) {
					if (object instanceof ActivityDefs.BranchingEdgeActivity) {
						if (object.isStart){
							ActivityLib.addSelectEffect(object.branchingActivity.end);
							
							$.each(object.branchingActivity.branches, function(){
								var transition = this.transitionFrom;
								while (transition) {
									var activity = transition.toActivity;
									if (activity instanceof ActivityDefs.BranchingEdgeActivity) {
										return true;
									}
									ActivityLib.addSelectEffect(activity);
									transition = activity.transitions.from.length > 0 ? activity.transitions.from[0] : null;
								}
							});
						} else {
							ActivityLib.addSelectEffect(object.branchingActivity.start);
						}
					} else if (object instanceof DecorationDefs.Container){
						$.each(object.childActivities, function(){
							ActivityLib.addSelectEffect(this);
						});
					}
				}
			}
			
			// make it officially marked?
			if (markSelected && object.items.selectEffect){
				layout.selectedObject = object;
				// show the properties dialog for the selected object
				if (object.loadPropertiesDialogContent) {
					PropertyLib.openPropertiesDialog(object);
				}
			}
		}
	},
	
	
	/**
	 * Draws a transition between two activities.
	 */
	addTransition : function(fromActivity, toActivity, redraw, id, uiid, branchData) {
		// check if a branching's start does not connect with another branching's end
		if (fromActivity instanceof ActivityDefs.BranchingEdgeActivity
				&& toActivity instanceof ActivityDefs.BranchingEdgeActivity
				&& fromActivity.isStart && !toActivity.isStart
				&& fromActivity.branchingActivity != toActivity.branchingActivity) {
			alert(LABELS.CROSS_BRANCHING_ERROR);
			return;
		}
		
		// if a child activity was detected, use the parent activity as the target
		if (toActivity.parentActivity && toActivity.parentActivity instanceof DecorationDefs.Container){
			toActivity = toActivity.parentActivity;
		}
		if (fromActivity.parentActivity && fromActivity.parentActivity instanceof DecorationDefs.Container){
			fromActivity = fromActivity.parentActivity;
		}
		// no transitions to/from support activities
		if (toActivity instanceof ActivityDefs.FloatingActivity
			|| fromActivity instanceof ActivityDefs.FloatingActivity){
			return;
		}
		
		// only converge points are allowed to have few inbound transitions
		if (!redraw
				&& toActivity.transitions.to.length > 0
				&& !(toActivity instanceof ActivityDefs.BranchingEdgeActivity && !toActivity.isStart)) {
			alert(LABELS.TRANSITION_TO_EXISTS_ERROR);
			return;
		}

		// check for circular sequences
		var activity = fromActivity;
		do {
			if (activity.transitions && activity.transitions.to.length > 0) {
				activity = activity.transitions.to[0].fromActivity;
			} else if (activity.branchingActivity && !activity.isStart) {
				activity = activity.branchingActivity.start;
			} else {
				activity = null;
			}
			
			if (toActivity == activity) {
				alert(LABELS.CIRCULAR_SEQUENCE_ERROR);
				return;
			}
		} while (activity);

		// user chose to create outbound transition from an activity that already has one
		if (!redraw
				&& fromActivity.transitions.from.length > 0
				&& !(fromActivity instanceof ActivityDefs.BranchingEdgeActivity && fromActivity.isStart)) {
			if (confirm(LABELS.BRANCHING_CREATE_CONFIRM)) {
				ActivityLib.addBranching(fromActivity, toActivity);
			}
			return;
		}
		
		// start building the transition
		
		// branchData can be either an existing branch or a title for the new branch
		var branch = branchData && branchData instanceof ActivityDefs.BranchActivity ? branchData : null,
			transition = null;
		// remove the existing transition
		$.each(fromActivity.transitions.from, function(index) {
			if (this.toActivity == toActivity) {
				id = this.id;
				uiid = this.uiid;
				transition = this;
				if (!branch){
					branch = this.branch;
				}

				return false;
			}
		});
		
		if (!branch && fromActivity instanceof ActivityDefs.BranchingEdgeActivity && fromActivity.isStart) {
			// if a title was provided, try to find the branch based on this information
			$.each(fromActivity.branchingActivity.branches, function(){
				if (branchData == this.title) {
					branch = this;
					return false;
				}
			});
			if (!branch) {
				// create a new branch
				branch = new ActivityDefs.BranchActivity(null, null, branchData, fromActivity.branchingActivity, false);
			}
		}
		
		if (transition) {
			ActivityLib.removeTransition(transition, redraw);
		}
		
		// finally add the new transition
		transition = new ActivityDefs.Transition(id, uiid, fromActivity, toActivity,
						 branch ? branch.title : null);

		if (branch) {
			// set the corresponding branch (again)
			branch.transitionFrom = transition;
			transition.branch = branch;
			fromActivity.branchingActivity.branches.push(branch);
			if (fromActivity.branchingActivity.branches.length == 1) {
				branch.defaultBranch = true;
			}
		}
		
		
		// after adding the transition, check for self-nested branching
		activity = fromActivity;
		var branchingActivity = null;
		// find the top-most enveloping branching activity, if any
		do {
			if (activity.transitions && activity.transitions.to.length > 0) {
				activity = activity.transitions.to[0].fromActivity;
				
				if (activity instanceof ActivityDefs.BranchingEdgeActivity) {
					if (activity.isStart) {
						// found the top branching the activity belongs to
						branchingActivity = activity.branchingActivity;
					} else {
						// jump over nested branching
						activity = activity.branchingActivity.start;
					}
				}
			} else {
				activity = null;
			}
		} while (activity);
		
		
		if (branchingActivity) {
			// look for all nested branchings
			var nestedBranchings = ActivityLib.findNestedBranching(branchingActivity);
			// check each of them
			$.each(nestedBranchings, function(){
				var branching = this;
				// check if one branching's end does not match with another branching's start
				$.each(branching.end.transitions.to, function(){
					// crawl from end to start
					var activity = this.fromActivity;
					while (activity) {
						if (activity instanceof ActivityDefs.BranchingEdgeActivity) {
							if (activity.isStart) {
								// this branching's end matches with its own start, all OK
								if (activity.branchingActivity == branching) {
									break;
								}
								// this branching's end does not match with own start, error
								alert(LABELS.CROSS_BRANCHING_ERROR);
								// remove the just added transition
								ActivityLib.removeTransition(transition);
								// tell the outer iteration loop to quit
								transition = null;
								return false;
							}
							// a nested branching encountered when crawling, just jump over it
							activity = activity.branchingActivity.start;
						}
						// keep crawling
						if (activity.transitions && activity.transitions.to.length > 0) {
							activity = activity.transitions.to[0].fromActivity;
						} else {
							activity = null;
						}
					}
				});
				
				if (!transition) {
					// there was an error, do not carry on
					return false;
				}
			});
		}
		
		GeneralLib.setModified(true);
		return transition;
	},
	
	
	findNestedBranching : function(branchingActivity) {
		var nestedBranching = [];
		$.each(branchingActivity.branches, function(){
			var activity = this.transitionFrom.toActivity;
			while (activity) {
				if (activity instanceof ActivityDefs.BranchingEdgeActivity) {
					if (activity.branchingActivity == branchingActivity){
						break;
					}
					if (nestedBranching.indexOf(activity.branchingActivity) == -1) {
						nestedBranching.push(activity.branchingActivity);
					}
					if (activity.isStart) {
						nestedBranching = nestedBranching.concat(ActivityLib.findNestedBranching(activity.branchingActivity));
						activity = activity.branchingActivity.end;
					}
				}
				
				if (activity.transitions && activity.transitions.from.length > 0) {
					activity = activity.transitions.from[0].toActivity;
				} else {
					activity = null;
				}
			}
		});
		
		return nestedBranching;
	},

	
	/**
	 * Calculates start, middle and end points of a line between two activities. 
	 */
	findTransitionPoints : function(fromActivity, toActivity) {
		var fromActivityBox = fromActivity.items.shape.getBBox(),
			toActivityBox = toActivity.items.shape.getBBox(),
			
			// find points in the middle of each activity
			points = {
				'startX'  : fromActivityBox.x + fromActivityBox.width / 2,
				'startY'  : fromActivityBox.y + fromActivityBox.height / 2,
				'endX'    : toActivityBox.x + toActivityBox.width / 2,
				'endY'    : toActivityBox.y + toActivityBox.height / 2
			},

			// find intersection points of the temporary transition
			tempTransitionPath = Snap.format('M {startX} {startY} L {endX} {endY}', points),
			fromIntersect = Snap.path.intersection(tempTransitionPath, fromActivity.items.shape.attr('path')),
			toIntersect = Snap.path.intersection(tempTransitionPath, toActivity.items.shape.attr('path'));
		
		// find points on borders of activities, if they exist
		if (fromIntersect.length > 0) {
			points.startX = fromIntersect[0].x;
			points.startY = fromIntersect[0].y;
		}
		if (toIntersect.length > 0) {
			points.endX = toIntersect[0].x;
			points.endY = toIntersect[0].y;
		}
		// middle point of the transition
		points.middleX = points.startX + (points.endX - points.startX)/2;
		points.middleY = points.startY + (points.endY - points.startY)/2;
		
		return points;
	},
	
	
	/**
	 * Drop the dragged activity on the canvas.
	 */
	dropActivity : function(activity, x, y) {
		if (!(activity instanceof ActivityDefs.OptionalActivity || activity instanceof ActivityDefs.FloatingActivity)) {
			// check if it was removed from an Optional or Floating Activity
			if (activity.parentActivity && activity.parentActivity instanceof DecorationDefs.Container) {
				var childActivities = DecorationLib.getChildActivities(activity.parentActivity.items.shape);
				if ($.inArray(activity, childActivities) == -1) {
					activity.parentActivity.draw();
					ActivityLib.redrawTransitions(activity.parentActivity);
					activity.parentActivity = null;
				}
			}
			
			// check if it was added to an Optional or Floating Activity
			var container = layout.floatingActivity
							&& Snap.path.isPointInsideBBox(layout.floatingActivity.items.getBBox(),x,y)
							? layout.floatingActivity : null;
			if (!container) {
				$.each(layout.activities, function(){
					if (this instanceof ActivityDefs.OptionalActivity
						&& Snap.path.isPointInsideBBox(this.items.getBBox(),x,y)) {
						container = this;
						return false;
					}
				});
			}
			if (container) {
				// system activities can not be added to optional and support activities
				if (activity instanceof ActivityDefs.GateActivity
					|| activity instanceof ActivityDefs.GroupingActivity
					|| activity instanceof ActivityDefs.BranchingEdgeActivity){
					alert(LABELS.ACTIVITY_IN_CONTAINER_ERROR);
					return false;
				}
				
				if ($.inArray(activity, container.childActivities) == -1) {
					$.each(activity.transitions.from, function(){
						ActivityLib.removeTransition(this);
					});
					$.each(activity.transitions.to, function(){
						ActivityLib.removeTransition(this);
					});
	
					// for properties dialog to reload
					ActivityLib.removeSelectEffect(container);
					
					container.childActivities.push(activity);
					container.draw(null, null, null, null, childActivities);
					ActivityLib.redrawTransitions(container);
				}
			}
		}
		
		ActivityLib.redrawTransitions(activity);
		
		$.each(layout.regions, function(){
			// redraw all annotation regions so they are pushed to back
			this.draw();
		});
		
		GeneralLib.setModified(true);
		return true;
	},
	

	/**
	 * Open separate window with activity authoring on double click.
	 */
	openActivityAuthoring : function(activity){
		// fetch authoring URL for a Tool Activity
		if (!activity.authorURL && activity.toolID) {
			$.ajax({
				async : false,
				cache : false,
				url : LAMS_URL + "authoring/author.do",
				dataType : 'json',
				data : {
					'method'          : 'createToolContent',
					'toolID'          : activity.toolID,
					// if toolContentID exists, a new content will not be created, only authorURL will be fetched
					'toolContentID'   : activity.toolContentID,
					'contentFolderID' : layout.ld.contentFolderID
				},
				success : function(response) {
					activity.authorURL = response.authorURL;
					if (!activity.toolContentID) {
						activity.toolContentID = response.toolContentID;
					}
					if (!layout.ld.contentFolderID) {
						// if LD did not have contentFolderID, it was just generated
						// so remember it
						layout.ld.contentFolderID = response.contentFolderID;
					}
				}
			});
		}
		
		if (activity.authorURL) {
			showDialog("dialogActivity" + activity.toolContentID, {
				'height' : 800,
				'width' : 1024,
				'title' : activity.title + ' ' + LABELS.ACTIVITY_DIALOG_TITLE_SUFFIX,
				'beforeClose' : function(event){
					// ask the user if he really wants to exit before saving his work
					var iframe = $('iframe', this);
					// if X button was clicked, currentTarget is set
					// if it is not the last Re-Edit/Close page, doCancel() exists
					if (event.currentTarget && iframe[0].contentWindow.doCancel) {
						iframe[0].contentWindow.doCancel();
						return false;
					} else {
						iframe.attr('src', null);
					}
				},
				'open' : function() {
					var dialog = $(this);
					// load contents after opening the dialog
					$('iframe', dialog).attr('src', activity.authorURL).load(function(){
						// override the close function so it works with the dialog, not window
						this.contentWindow.closeWindow = function(){
							dialog.dialog('close');
						}
					});
				}
			}, true);
			
			GeneralLib.setModified(true);
		}
	},
	
	
	/**
	 * Draw each of activity's inboud and outbound transitions again.
	 */
	redrawTransitions : function(activity) {
		if (activity.transitions) {
			$.each(activity.transitions.from.slice(), function(){
				ActivityLib.addTransition(activity, this.toActivity, true);
			});
			$.each(activity.transitions.to.slice(), function(){
				ActivityLib.addTransition(this.fromActivity, activity, true);
			});
		}
	},
	
	
	/**
	 * Deletes the given activity.
	 */
	removeActivity : function(activity, forceRemove) {
		var coreActivity =  activity.branchingActivity || this;
		if (!forceRemove && activity instanceof ActivityDefs.BranchingEdgeActivity){
			// user removes one of the branching edges, so remove the whole activity
			if (confirm(LABELS.REMOVE_ACTIVITY_CONFIRM)){
				var otherEdge = activity.isStart ? coreActivity.end
						                         : coreActivity.start;
				ActivityLib.removeActivity(otherEdge, true);
			} else {
				return;
			}
		}
		
		if (activity instanceof ActivityDefs.FloatingActivity) {
			layout.floatingActivity = null;
			// re-enable the button, as the only possible Floating Activity is gone now
			$('#floatingActivityButton').attr('disabled', null)
									 	.css('opacity', 1);
		} else {
			// remove the transitions
			// need to use slice() to copy the array as it gets modified in removeTransition()
			$.each(activity.transitions.from.slice(), function() {
				ActivityLib.removeTransition(this);
			});
			$.each(activity.transitions.to.slice(), function() {
				ActivityLib.removeTransition(this);
			});
			
			// remove the activity from reference tables
			layout.activities.splice(layout.activities.indexOf(activity), 1);
			if (layout.copiedActivity = activity) {
				layout.copiedActivity = null;
			}

			// find references of this activity as grouping or input
			$.each(layout.activities, function(){
				if (activity == coreActivity.grouping) {
					coreActivity.grouping = null;
					this.draw();
				} else if (activity == coreActivity.input) {
					coreActivity.input = null;
				}
			});
		}
		
		// remove the activity from parent activity
		if (activity.parentActivity && activity.parentActivity instanceof DecorationDefs.Container) {
			activity.parentActivity.childActivities.splice(activity.parentActivity.childActivities.indexOf(activity), 1);
		}
		
		// remove child activities
		if (activity instanceof DecorationDefs.Container) {
			$.each(activity.childActivities, function(){
				ActivityLib.removeActivity(this);
			});
		}
		
		// visually remove the activity
		activity.items.remove();
		GeneralLib.setModified(true);
	},
	
	
	/**
	 * Deselects an activity/transition/annotation
	 */
	removeSelectEffect : function(object) {
		// remove the effect from the given object or the selected one, whatever it is
		if (!object) {
			object = layout.selectedObject;
		}
		
		if (object) {
			var selectEffect = object.items.selectEffect;
			if (selectEffect) {
				object.items.selectEffect = null;
				// different effects for different types of objects
				if (object instanceof DecorationDefs.Region) {
					object.items.shape.attr({
	 			    	'stroke' : layout.colors.activityBorder,
						'stroke-dasharray' : null
					});
					object.items.fitButton.attr('display','none');
					object.items.resizeButton.attr('display','none');
					
					var childActivities = DecorationLib.getChildActivities(object.items.shape);
					$.each(childActivities, function(){
						ActivityLib.removeSelectEffect(this);
					});
				} else if (object instanceof ActivityDefs.Transition) {
					// just redraw the transition, it's easier
					object.draw();
				} else {
					selectEffect.remove();
					
					// if it's "import part" do special processing for branching
					if (activitiesOnlySelectable) {
						if (object instanceof ActivityDefs.BranchingEdgeActivity) {
							if (object.isStart) {
								ActivityLib.removeSelectEffect(object.branchingActivity.end);
								
								// deselect all children in branches
								$.each(object.branchingActivity.branches, function(){
									var transition = this.transitionFrom;
									while (transition) {
										var activity = transition.toActivity;
										if (activity instanceof ActivityDefs.BranchingEdgeActivity) {
											return true;
										}
										ActivityLib.removeSelectEffect(activity);
										transition = activity.transitions.from.length > 0 ? activity.transitions.from[0] : null;
									}
								});
							} else {
								ActivityLib.removeSelectEffect(object.branchingActivity.start);
							}
						}

						// deselect Parallel Activity children
						$.each(layout.activities, function(){
							if (this instanceof ActivityDefs.ParallelActivity && this.childActivities.indexOf(object) > -1){
								ActivityLib.removeSelectEffect(this);
								$.each(this.childActivities, function(){
									if (this != object) {
										this.items.selectEffect.remove();
										this.items.selectEffect = null;
									}
								});
							}
						});
					}
				}
			}
			
			if (layout.propertiesDialog) {
				// no selected activity = no properties dialog
				layout.propertiesDialog.dialog('close');
			}
			layout.selectedObject = null;
		}
	},
	
	
	/**
	 * Removes the given transition.
	 */
	removeTransition : function(transition, redraw) {
		// find the transition and remove it
		var transitions = transition.fromActivity.transitions.from;
		transitions.splice(transitions.indexOf(transition), 1);
		transitions = transition.toActivity.transitions.to;
		transitions.splice(transitions.indexOf(transition), 1);
		
		if (transition.branch) {
			// remove corresponding branch
			var branches = transition.branch.branchingActivity.branches;
			branches.splice(branches.indexOf(transition.branch), 1);
			
			if (transition.branch.defaultBranch && branches.length > 0) {
				// reset the first branch as the default one
				branches[0].defaultBranch = true;
			}
		}
		
		// redraw means that the transition will be drawn again in just a moment
		// so do not do any structural changes
		if (!redraw){
			// remove grouping or input references if chain was broken by the removed transition
			$.each(layout.activities, function(){
				var coreActivity =  this.branchingActivity || this;
				if (coreActivity.grouping || coreActivity.input) {
					var candidate = this.branchingActivity ? coreActivity.start : this,
						groupingFound = false,
						inputFound = false;
					do {
						if (candidate.transitions && candidate.transitions.to.length > 0) {
							candidate = candidate.transitions.to[0].fromActivity;
						} else if (candidate.branchingActivity && !candidate.isStart) {
							candidate = candidate.branchingActivity.start;
						}  else if (!candidate.branchingActivity && candidate.parentActivity) {
							candidate = candidate.parentActivity;
						} else {
							candidate = null;
						}
						
						if (coreActivity.grouping == candidate) {
							groupingFound = true;
						}
						if (coreActivity.input == candidate) {
							inputFound = true;
						}
					} while (candidate != null);
					
					if (!groupingFound) {
						coreActivity.grouping = null;
						this.draw();
					}
					if (!inputFound) {
						coreActivity.input = null;
					}
				}
			});
		}
		
		transition.items.remove();
		GeneralLib.setModified(true);
	},
	
	/**
	 * Reduce length of activity's title so it fits in its SVG shape.
	 */
	shortenActivityTitle : function(title) {
		if (title.length > 18) {
			title = title.substring(0, 17) + '...';
		}
		return title;
	},
	
	
	/**
	 * Crawles through branches setting their lengths and finding the longest one.
	 */
	updateBranchesLength : function(branchingActivity) {
		var longestBranchLength = 0;
		$.each(branchingActivity.branches, function(){
			// include the first activity
			var branchLength = 1,
				activity = this.transitionFrom.toActivity;
			if (activity instanceof ActivityDefs.BranchingEdgeActivity
					&& branchingActivity == activity.branchingActivity){
				// branch with no activities
				return true;
			}
			
			while (activity.transitions.from.length > 0) {
				activity = activity.transitions.from[0].toActivity;
				// check if reached the end of branch
				if (activity instanceof ActivityDefs.BranchingEdgeActivity) {
					break;
				} else {
					branchLength++;
				}
			};
			this.branchLength = branchLength;
			if (branchLength > longestBranchLength) {
				longestBranchLength = branchLength;
			}
		});
		
		branchingActivity.longestBranchLength = longestBranchLength;
	}
};