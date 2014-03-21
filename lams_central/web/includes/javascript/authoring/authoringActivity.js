/**
 * This file contains methods for Activity manipulation on canvas.
 */

var ActivityLib = {
		
	/**
	 * Constructor for a Transition
	 */
	Transition : function(id, uiid, fromActivity, toActivity, title) {
		this.id = +id;
		this.uiid = +uiid || ++layout.ld.maxUIID;
		this.fromActivity = fromActivity;
		this.toActivity = toActivity;
		if (title) {
			// only branches have titles
			this.title = title;
			this.loadPropertiesDialogContent = PropertyLib.transitionProperties;
		}
		
		this.draw = ActivityLib.draw.transition;
		this.draw();
		
		// set up references in activities and the transition
		fromActivity.transitions.from.push(this);
		toActivity.transitions.to.push(this);
	},
	
	
	/**
	 * Constructor for a Tool Activity.
	 */
	ToolActivity : function(id, uiid, toolContentID, toolID, x, y, title, supportsOutputs) {
		this.id = +id;
		this.uiid = +uiid || ++layout.ld.maxUIID;
		this.toolContentID = toolContentID;
		this.toolID = +toolID;
		this.title = title;
		this.supportsOutputs = supportsOutputs;
		this.transitions = {
			'from' : [],
			'to'   : []
		};
		
		this.loadPropertiesDialogContent = PropertyLib.toolProperties;
		
		this.draw = ActivityLib.draw.tool;
		this.draw(x, y);
	},

	/**
	 * Constructor for a Grouping Activity.
	 */
	GroupingActivity : function(id, uiid, x, y, title, groupingID, groupingUIID, groupingType, groupDivide,
								groupCount, learnerCount, equalSizes, viewLearners, groups) {
		this.id = +id;
		this.groupingID = +groupingID;
		this.groupingUIID = +groupingUIID  || ++layout.ld.maxUIID;
		this.uiid = +uiid || ++layout.ld.maxUIID;
		this.title = title || 'Grouping';
		this.groupingType = groupingType || 'random';
		this.groupDivide = groupDivide || 'groups';
		this.groupCount = +groupCount || 2;
		this.learnerCount = +learnerCount || 1;
		this.equalSizes = equalSizes || false;
		this.viewLearners = viewLearners || false;
		this.groups = groups || [];
		this.transitions = {
			'from' : [],
			'to'   : []
		};
		
		this.loadPropertiesDialogContent = PropertyLib.groupingProperties;

		this.draw = ActivityLib.draw.grouping;		
		this.draw(x, y);
	},

	/**
	 * Constructor for a Gate Activity.
	 */
	GateActivity : function(id, uiid, x, y, gateType) {
		this.id = +id;
		this.uiid = +uiid || ++layout.ld.maxUIID;
		this.gateType = gateType || 'permission';
		this.transitions = {
			'from' : [],
			'to'   : []
		};
		
		this.loadPropertiesDialogContent = PropertyLib.gateProperties;

		this.draw = ActivityLib.draw.gate;
		this.draw(x, y);
	},
	
	
	/**
	 * Either branching or converge point.
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
			branchingActivity = new ActivityLib.BranchingActivity(id, uiid, this);
			branchingActivity.branchingType = branchingType || 'chosen';
			branchingActivity.title = title || 'Branching';
		}
		this.branchingActivity = branchingActivity;
		
		this.loadPropertiesDialogContent = PropertyLib.branchingProperties;
		
		this.draw = ActivityLib.draw.branching;
		this.draw(x, y);
	},
	
	
	/**
	 * Represents a set of branches. It is not displayed on canvas.
	 */
	BranchingActivity : function(id, uiid, branchingEdgeStart) {
		this.id = +id;
		this.uiid = +uiid || ++layout.ld.maxUIID;
		this.start = branchingEdgeStart;
		this.branches = [];
		// mapping between groups and branches, if applicable
		this.groupsToBranches = [];
	},
	
	
	/**
	 * Represents a subsequence of activities. It is not displayed on canvas.
	 */
	BranchActivity : function(id, uiid, title, branchingActivity, transitionFrom) {
		this.id = +id;
		this.uiid = +uiid || ++layout.ld.maxUIID;
		this.title = title || ('Branch ' + (branchingActivity.branches.length + 1));
		this.branchingActivity = branchingActivity;
		this.transitionFrom = transitionFrom;
	},
	
	
	/**
	 * Constructor for an Optional Activity.
	 */
	OptionalActivity : function(id, uiid, x, y, title, minActivities, maxActivities) {
		DecorationLib.Container.call(this, title || 'Optional Activity');
		
		this.id = +id;
		this.uiid = +uiid || ++layout.ld.maxUIID;
		this.minActivities = minActivities || 0;
		this.maxActivities = maxActivities || 0;
		this.transitions = {
			'from' : [],
			'to'   : []
		};
		
		this.loadPropertiesDialogContent = PropertyLib.optionalActivityProperties;
		
		this.draw = ActivityLib.draw.optionalActivity;
		this.draw(x, y);
	},
	
	
	/**
	 * Constructor for a Floating Activity.
	 */
	FloatingActivity : function(id, uiid, x, y) {
		DecorationLib.Container.call(this, 'Support Activity');
		
		this.id = +id;
		this.uiid = +uiid || ++layout.ld.maxUIID;

		this.draw = ActivityLib.draw.floatingActivity;
		this.draw(x, y);
		
		// there can only be one
		layout.floatingActivity = this;
	},
	
	
	/**
	 * Mehtods for drawing various kinds of activities.
	 * They are not defined in constructors so there is always a reference to an existing method,
	 * not a separate definition for each object instance.
	 */
	draw : {
		
		transition : function() {
			// clear previous canvas elements
			if (this.items) {
				this.items.remove();
			}
			
			// calculate middle points of each activity
			var points = ActivityLib.findTransitionPoints(this.fromActivity, this.toActivity);
			
			// create transition SVG elements
			paper.setStart();
			paper.path(Raphael.format('M {0} {1} L {2} {3}', points.startX, points.startY, points.endX, points.endY))
			                  .attr({
			                 	'stroke'       : layout.colors.transition,
			                	'stroke-width' : 2
			                  });

			// draw the arrow and turn it in the same direction as the line
			var angle = 90 + Math.atan2(points.endY - points.startY, points.endX - points.startX) * 180 / Math.PI,
				arrowPath = Raphael.transformPath(Raphael.format('M {0} {1}' + layout.defs.transArrow, points.middleX, points.middleY), 
					                              Raphael.format('R {0} {1} {2}', angle, points.middleX, points.middleY));
			paper.path(arrowPath)
							 .attr({
								'stroke' : layout.colors.transition,
								'fill'   : layout.colors.transition
							 });
			if (this.title) {
				// adjust X & Y depending on the angle, so the label does not overlap with the transition;
				// angle is -90 <= a <= 270
				paper.text(points.middleX + ((angle > -45 && angle < 45) || (angle > 135 && angle < 225) ? 20 : 0),
						   points.middleY + ((angle > 45 && angle < 135) || angle > 225 || angle < 45 ? -20 : 0),
						   this.title)
					 .attr('text-anchor', 'start');
			}
			this.items = paper.setFinish();

			this.items.toBack();
			this.items.attr('cursor', 'pointer');
			this.items.data('parentObject', this);
			this.items.mousedown(HandlerLib.transitionMousedownHandler);
			this.items.click(HandlerLib.itemClickHandler);
		},
		
		
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
			paper.setStart();
			var shape = paper.path(Raphael.format('M {0} {1}' + layout.defs.activity, x, y))
							 .attr({
								'fill' : layout.colors.activity[layout.toolMetadata[this.toolID].activityCategoryID]
							 });
			// check for icon in the library
			paper.image(layout.toolMetadata[this.toolID].iconPath, x + 47, y + 2, 30, 30);
			paper.text(x + 62, y + 40, ActivityLib.shortenActivityTitle(this.title))
				 .attr({
					 'fill' : layout.colors.activityText
				 });
			
			this.items = paper.setFinish();
			this.items.shape = shape;
			
			if (this.grouping) {
				ActivityLib.addGroupingEffect(this);
			}
			
			ActivityLib.activityHandlersInit(this);
		},
		
		
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
			paper.setStart();
			var shape = paper.path(Raphael.format('M {0} {1}' + layout.defs.activity, x, y))
							 .attr({
									'fill' : layout.colors.grouping
								 });
			
			paper.image('../images/grouping.gif', x + 47, y + 2, 30, 30);
			paper.text(x + 62, y + 40, ActivityLib.shortenActivityTitle(this.title));
			
			this.items = paper.setFinish();
			this.items.shape = shape;
			
			ActivityLib.activityHandlersInit(this);
		},
		
		
		gate : function(x, y) {
			if (x == undefined || y == undefined) {
				x = this.items.shape.getBBox().x;
				y = this.items.shape.getBBox().y;
			}

			if (this.items) {
				this.items.remove();
			}
			
			// create activity SVG elements
			paper.setStart();
			var shape = paper.path(Raphael.format('M {0} {1}' + layout.defs.gate, x + 8, y))
							 .attr({
								 'fill' : layout.colors.gate
							 });
			
			paper.text(x + 15, y + 14, 'STOP')
			     .attr({
					'font-size' : 9,
					'font' : 'sans-serif',
					'stroke' : layout.colors.gateText
			     });
			
			this.items = paper.setFinish();
			this.items.shape = shape;
			
			ActivityLib.activityHandlersInit(this);
		},
		
		
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
			paper.setStart();
			var shape = paper.path(Raphael.format('M {0} {1}'
								                  + (this.isStart ? layout.defs.branchingEdgeStart : layout.defs.branchingEdgeEnd),
								                  x, y + 8))
							 .attr({
								 'fill' : this.isStart ? layout.colors.branchingEdgeStart
							                           : layout.colors.branchingEdgeEnd
							 });
			
			var title = this.branchingActivity.title;
			paper.text(x + 8, y + 27,  title + (this.isStart ? '\nstart'
		                                        		 : '\nend'))
			     .attr({
					'font-size' : 9,
					'font' : 'sans-serif'
			     });
			
			this.items = paper.setFinish();
			this.items.shape = shape;
			
			ActivityLib.activityHandlersInit(this);
		},
		
		
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
				var activityY = y + 30,
					allElements = paper.set(),
					optionalActivity = this;
				$.each(this.childActivities, function(){
					this.parentActivity = optionalActivity;
					this.draw(x + 20, activityY);
					activityY = this.items.shape.getBBox().y2 + 10;
					allElements.push(this.items.shape);
				});
				// area containing all drawn child activities
				var box = allElements.getBBox();
				
				this.drawContainer(x, y, box.x2 + 20, box.y2 + 20, layout.colors.optionalActivity);
			} else {
				this.drawContainer(x, y, x + 50, y + 70, layout.colors.optionalActivity);
			}
			
			// allow transition drawing and other activity behaviour
			this.items.shape.unmousedown().mousedown(HandlerLib.activityMousedownHandler);
			
			this.items.data('parentObject', this);
		},
		
		
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
				var activityX = x + 20,
					allElements = paper.set(),
					floatingActivity = this;
				$.each(this.childActivities, function(){
					this.parentActivity = floatingActivity;
					this.draw(activityX, y + 30);
					activityX = this.items.shape.getBBox().x2 + 10;
					allElements.push(this.items.shape);
				});
				// area containing all drawn child activities
				var box = allElements.getBBox();
				
				this.drawContainer(x, y, box.x2 + 20, box.y2 + 20, layout.colors.optionalActivity);
			} else {
				this.drawContainer(x, y, x + 50, y + 70, layout.colors.optionalActivity);
			}
			
			this.items.data('parentObject', this);
		}
	},
	
	
	/**
	 * Make a new activity fully functional on canvas.
	 */
	activityHandlersInit : function(activity) {
		// set all the handlers
		activity.items
			.data('parentObject', activity)
			.mousedown(HandlerLib.activityMousedownHandler)
			.click(HandlerLib.itemClickHandler)
			.dblclick(HandlerLib.activityDblclickHandler)
			.attr({
				'cursor' : 'pointer'
			});
			
			if (activity instanceof ActivityLib.BranchingEdgeActivity
					&& activity.branchingActivity.end) {
				// highligh branching edges on hover
				activity.branchingActivity.start.items.hover(HandlerLib.branchingEdgeMouseoverHandler,
						HandlerLib.branchingEdgeMouseoutHandler);
				activity.branchingActivity.end.items.hover(HandlerLib.branchingEdgeMouseoverHandler,
						HandlerLib.branchingEdgeMouseoutHandler);
			}
		},

	
	/**
	 * Deletes the given activity.
	 */
	removeActivity : function(activity, forceRemove) {
		if (!forceRemove && activity instanceof ActivityLib.BranchingEdgeActivity){
			// user removes one of the branching edges, so remove the whole activity
			if (confirm('Are you sure you want to remove the whole branching activity?')){
				var otherEdge = activity.isStart ? activity.branchingActivity.end
						                         : activity.branchingActivity.start;
				ActivityLib.removeActivity(otherEdge, true);
			} else {
				return;
			}
		}
		
		if (activity instanceof ActivityLib.FloatingActivity) {
			layout.floatingActivity = null;
			// re-enable the button
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
			if (layout.items.copiedActivity = activity) {
				layout.items.copiedActivity = null;
			}
			if (activity instanceof ActivityLib.GroupingActivity) {
				$.each(layout.activities, function(){
					if (activity == this.grouping) {
						this.grouping = null;
						this.draw();
					}
				});
			}
		}
		
		// remove the activity from parent activity
		if (activity.parentActivity) {
			activity.parentActivity.childActivities.splice(layout.parentActivity.childActivities.indexOf(activity), 1);
		}
		
		// remove child activities
		if (activity instanceof ActivityLib.OptionalActivity
			|| activity instanceof ActivityLib.FloatingActivity) {
			$.each(activity.childActivities, function(){
				ActivityLib.removeActivity(this);
			});
		}
		
		// visually remove the activity
		activity.items.remove();
	},
	
	
	/**
	 * Draws a transition between two activities.
	 */
	addTransition : function(fromActivity, toActivity, redraw, id, uiid, branchData) {
		if (toActivity.parentActivity){
			toActivity = toActivity.parentActivity;
		}
		if (fromActivity.parentActivity){
			fromActivity = fromActivity.parentActivity;
		}
		if (toActivity instanceof ActivityLib.FloatingActivity
			|| fromActivity instanceof ActivityLib.FloatingActivity){
			return;
		}
		
		// only converge points are allowed to have few inbound transitions
		if (!redraw
				&& toActivity.transitions.to.length > 0
				&& !(toActivity instanceof ActivityLib.BranchingEdgeActivity && !toActivity.isStart)) {
			alert('Transition to this activity already exists');
			return;
		}

		// user chose to create outbound transition from an activity that already has one
		if (!redraw
				&& fromActivity.transitions.from.length > 0
				&& !(fromActivity instanceof ActivityLib.BranchingEdgeActivity && fromActivity.isStart)
				&& !(toActivity instanceof ActivityLib.BranchingEdgeActivity  && toActivity.isStart)) {
			if (confirm('Transition from this activity already exists.\n' +
					    'Do you want to create branching here?')) {
				ActivityLib.addBranching(fromActivity, toActivity);
			}
			return;
		}
		
		// branchData can be either existing branch or a title for the new branch
		var branch = branchData && branchData instanceof ActivityLib.BranchActivity ? branchData : null,
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
		
		if (!branch && fromActivity instanceof ActivityLib.BranchingEdgeActivity && fromActivity.isStart) {
			$.each(fromActivity.branchingActivity.branches, function(){
				if (branchData == this.title) {
					branch = this;
					return false;
				}
			});
			if (!branch) {
				// create a new branch
				branch = new ActivityLib.BranchActivity(null, null, branchData, fromActivity.branchingActivity);
			}
		}
		
		
		if (transition) {
			ActivityLib.removeTransition(transition);
		}
		
		transition = new ActivityLib.Transition(id, uiid, fromActivity, toActivity,
						 branch ? branch.title : null);

		if (branch) {
			// set the corresponding branch (again)
			branch.transitionFrom = transition;
			transition.branch = branch;
			fromActivity.branchingActivity.branches.push(branch);
		}
		
		return transition;
	},
	
	
	/**
	 * Removes the given transition.
	 */
	removeTransition : function(transition) {
		// find the transition and remove it
		var transitions = transition.fromActivity.transitions.from;
		transitions.splice(transitions.indexOf(transition), 1);
		transitions = transition.toActivity.transitions.to;
		transitions.splice(transitions.indexOf(transition), 1);
		
		if (transition.branch) {
			// remove corresponding branch
			var branches = transition.branch.branchingActivity.branches;
			branches.splice(branches.indexOf(transition.branch), 1);
		}
		
		transition.items.remove();
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
		
		if (toActivity2 instanceof ActivityLib.BranchingEdgeActivity && toActivity2.isStart) {
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
			    branchingEdgeStart = new ActivityLib.BranchingEdgeActivity(null, null, branchEdgeStartX,
			    		branchEdgeStartY, null, null, null);
			layout.activities.push(branchingEdgeStart);
			
			// find last activities in subsequences and make an converge point between them
			while (convergeActivity2.transitions.from.length > 0) {
				convergeActivity2 = convergeActivity2.transitions.from[0].toActivity;
			};

			var convergePoints = ActivityLib.findTransitionPoints(convergeActivity1, convergeActivity2),
				branchingEdgeEnd = new ActivityLib.BranchingEdgeActivity(null, null, convergePoints.middleX,
					convergePoints.middleY, null, null, branchingEdgeStart.branchingActivity);
			layout.activities.push(branchingEdgeEnd);
			
			// draw all required transitions
			ActivityLib.addTransition(fromActivity, branchingEdgeStart);
			ActivityLib.addTransition(branchingEdgeStart, toActivity2);
			ActivityLib.addTransition(convergeActivity2, branchingEdgeEnd);
		}

		ActivityLib.addTransition(branchingEdgeStart, toActivity1);
		ActivityLib.addTransition(convergeActivity1, branchingEdgeEnd);
	},
	
	
	/**
	 * Highlights the selected canvas element.
	 */
	addSelectEffect : function (object, markSelected) {
		// do not draw twice
		if (!object.items.selectEffect) {
			// different effects for different types of objects
			if (object instanceof DecorationLib.Region) {
				object.items.shape.attr({
					'stroke'           : layout.colors.selectEffect,
					'stroke-dasharray' : '-'
				});
				object.items.resizeButton.show();
				object.items.resizeButton.toFront();
				object.items.selectEffect = true;
				
				// also select encapsulated activities
				var childActivities = DecorationLib.getChildActivities(object.items.shape);
				if (childActivities.length > 0) {
					object.items.fitButton.show();
					
					$.each(childActivities, function(){
						if (!this.parentActivity) {
							ActivityLib.addSelectEffect(this, false);
						}
					});
				}
			} else if (object instanceof ActivityLib.Transition) {
				// show only if Transition is selectable, i.e. is a branch, has a title
				if (object.loadPropertiesDialogContent) {
					object.items.attr({
						'stroke' : layout.colors.selectEffect,
						'fill'   : layout.colors.selectEffect
					 });
					
					object.items.selectEffect = true;
				}
			} else {
				// this goes for Activities and Labels
				var box = object.items.getBBox();
				
				// a simple rectange a bit wider than the actual activity boundaries
				object.items.selectEffect = paper.rect(
						box.x - 7,
						box.y - 7,
						box.width + 14,
						box.height + 14)
					.attr({
						'stroke'           : layout.colors.selectEffect,
						'stroke-dasharray' : '-'
					});
				object.items.push(object.items.selectEffect);
			}
			
			// make it officially marked?
			if (markSelected && object.items.selectEffect){
				layout.items.selectedObject = object;
				// show the properties dialog for the selected object
				if (object.loadPropertiesDialogContent) {
					object.loadPropertiesDialogContent();
					var dialog = layout.items.propertiesDialog;
					dialog.children().detach();
					dialog.append(object.propertiesContent);
					dialog.dialog('open');
					dialog.find('input').blur();
				}
			}
		}
	},
	
	
	removeSelectEffect : function(object) {
		// remove the effect from the given object or the selected one, whatever it is
		if (!object) {
			object = layout.items.selectedObject;
		}
		
		if (object) {
			if (object.items.selectEffect) {
				// different effects for different types of objects
				if (object instanceof DecorationLib.Region) {
					object.items.shape.attr({
						'stroke'           : 'black',
						'stroke-dasharray' : ''
					});
					object.items.fitButton.hide();
					object.items.resizeButton.hide();
					
					var childActivities = DecorationLib.getChildActivities(object.items.shape);
					$.each(childActivities, function(){
						ActivityLib.removeSelectEffect(this);
					});
				} else if (object instanceof ActivityLib.Transition) {
					// just redraw the transition, it's easier
					object.draw();
				} else {
					object.items.selectEffect.remove();
				}
				object.items.selectEffect = null;
			}
			
			// no selected activity = no properties dialog
			layout.items.propertiesDialog.dialog('close');
			layout.items.selectedObject = null;
		}
	},
	
	
	/**
	 * Adds visual grouping effect on an activity.
	 */
	addGroupingEffect : function(activity) {
		if (!activity.items.groupingEffect) {
			var shape = activity.items.shape,
				activityBox = shape.getBBox();
			
			activity.items.groupingEffect = paper.rect(
					activityBox.x + 5,
					activityBox.y + 5,
					activityBox.width,
					activityBox.height)
				.attr({
					'fill' : shape.attr('fill')
				})
				.toBack();
			activity.items.push(activity.items.groupingEffect);
			
			// this is needed, for some reason, otherwise the activity can not be selected
			HandlerLib.resetCanvasMode(true);
		}
	},
	
	
	/**
	 * Drop the dragged activity on the canvas.
	 */
	dropActivity : function(activity, x, y) {
		if (!(activity instanceof DecorationLib.Container)) {
			// check if it was removed from an Optional or Floating Activity
			if (activity.parentActivity) {
				var childActivities = DecorationLib.getChildActivities(activity.parentActivity.items.shape);
				if ($.inArray(activity, childActivities) == -1) {
					activity.parentActivity.draw();
					ActivityLib.redrawTransitions(activity.parentActivity);
					activity.parentActivity = null;
				}
			}
			
			// check if it was added to an Optional or Floating Activity
			var container = layout.floatingActivity && layout.floatingActivity.items.shape.isPointInside(x, y)
							? layout.floatingActivity : null;
			if (!container) {
				$.each(layout.activities, function(){
					if (this instanceof ActivityLib.OptionalActivity && this.items.shape.isPointInside(x, y)) {
						container = this;
						return false;
					}
				});
			}
			if (container) {
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
	},
	
	
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
					'contentFolderID' : layout.ld.contentFolderID
				},
				success : function(response) {
					activity.authorURL = response.authorURL;
					activity.toolContentID = response.toolContentID;
					if (!layout.ld.contentFolderID) {
						// if LD did not have contentFolderID, it was just generated
						// so remember it
						layout.ld.contentFolderID = response.contentFolderID;
					}
				}
			});
		}
		
		if (activity.authorURL) {
			window.open(activity.authorURL, 'activityAuthoring' + activity.id,
					"HEIGHT=800,WIDTH=1024,resizable=yes,scrollbars=yes,status=false," +
					"menubar=no,toolbar=no");
		}
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
			tempTransition = Raphael.parsePathString(Raphael.format(
				'M {0} {1} L {2} {3}', points.startX, points.startY, points.endX, points.endY)),
			fromIntersect = Raphael.pathIntersection(tempTransition, fromActivity.items.shape.attr('path')),
			toIntersect = Raphael.pathIntersection(tempTransition, toActivity.items.shape.attr('path'));
		
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
	 * Crawles through branches setting their lengths and finding the longest one.
	 */
	updateBranchesLength : function(branchingActivity) {
		var longestBranchLength = 0;
		$.each(branchingActivity.branches, function(){
			// include the first activity
			var branchLength = 1,
				activity = this.transitionFrom.toActivity;
			if (activity instanceof ActivityLib.BranchingEdgeActivity
					&& branchingActivity == activity.branchingActivity){
				// branch with no activities
				return true;
			}
			
			while (activity.transitions.from.length > 0) {
				activity = activity.transitions.from[0].toActivity;
				// check if reached the end of branch
				if (activity instanceof ActivityLib.BranchingEdgeActivity) {
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
	 * Get real coordinates on paper, based on event coordinates.
	 */
	translateEventOnCanvas : function(event) {
		return [event.pageX + canvas.scrollLeft() - canvas.offset().left,
		        event.pageY + canvas.scrollTop()  - canvas.offset().top];
	}
};