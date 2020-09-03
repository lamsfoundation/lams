/**
 * This file contains event handlers for interaction with canvas and other Authoring elements.
 */

/**
 * Contains general (canvas, group of shapes) action handlers
 */
var HandlerLib = {
	// taken from http://jsfiddle.net/LQuyr/8/
	touchHandler : function(event) {
	    var self = this,
	    	touches = event.changedTouches,
	        first = touches[0],
	        type = "";

	    switch (event.type) {
	    case "touchstart":
	        type = "mousedown";
	        window.startY = event.pageY;
	        break;
	    case "touchmove":
	        type = "mousemove";
	        break;
	    case "touchend":
	        type = "mouseup";
	        break;
	    default:
	        return;
	    }
	    var simulatedEvent = document.createEvent("MouseEvent");
	    simulatedEvent.initMouseEvent(type, true, true, window, 1, first.screenX, first.screenY,
	    		first.clientX, first.clientY, false, false, false, false, 0 /*left*/ , null);

	    first.target.dispatchEvent(simulatedEvent);

	    var scrollables = [],
	    	clickedInScrollArea = false,
	    	// check if any of the parents has is-scollable class
	    	parentEls = $(event.target).parents().map(function() {
	        try {
	            if ($(this).hasClass('scrollable')) {
	                clickedInScrollArea = true;
	                // get vertical direction of touch event
	                var direction = (window.startY < first.clientY) ? 'down' : 'up';
	                // calculate stuff... :o)
	                if ((($(this).scrollTop() <= 0) && (direction === 'down'))
	                		|| (($(this).height() <= $(this).scrollTop()) && (direction === 'up')) ){

	                } else {
	                    scrollables.push(this);
	                }
	            }
	        } catch (e) {}
	    });
	    
	    // if not, prevent default to prevent bouncing
	    if ((scrollables.length === 0) && (type === 'mousemove')) {
	        event.preventDefault();
	    }
	},
		
	
	/**
	 * Remove activity selection when user clicks on canvas.
	 */
	canvasClickHandler : function(event) {
		// check if user clicked on empty space on canvas
		// or on some element on top of it
		if (layout.drawMode || (event.originalEvent ?
				event.originalEvent.defaultPrevented : event.defaultPrevented)){
			return;
		}
		
		ActivityLib.removeSelectEffect();
	},
	
	
	/**
	 * Start dragging an activity or a transition.
	 */
	dragItemsStartHandler : function(object, draggedElement, mouseupHandler, event, startX, startY) {
		if (layout.drawMode || (event.originalEvent ?
				event.originalEvent.defaultPrevented : event.defaultPrevented)){
			return;
		}
		
		// if user clicks or drags very shortly, do not take it into account 
		var items = object.items,
			dragCancel = function(){
			canvas.off('mouseup');
			// if there is already a function waiting to be started, clear it
			if (items.dragStarter) {
				// prevent confusion when double clicking
				clearTimeout(items.dragStarter);
				items.dragStarter = null;
				return;
			}
		}
		dragCancel();
		canvas.mouseup(dragCancel);
		
		// run only if "click" event was not generated, i.e. user really wants to drag
		items.dragStarter = setTimeout(function(){
			items.dragStarter = null;
			
			// show that we are in the middle of something
			HandlerLib.resetCanvasMode();
			items.isDragged = true;
			items.attr('cursor', 'move');
			
			var parentObject = ActivityLib.getParentObject(draggedElement);
				sticky = parentObject && (parentObject instanceof ActivityDefs.ParallelActivity
										  || parentObject instanceof ActivityDefs.OptionalActivity
										  || parentObject instanceof ActivityDefs.FloatingActivity);
				
			// hide child activities while moving the parent around
			// they will be redrawn when the parent is dropped
			if (sticky) {
				$.each(parentObject.childActivities, function(){
					this.items.attr('display', 'none');
					if (this.childActivities) {
						$.each(this.childActivities, function() {
							this.items.attr('display', 'none');
						});
					}
				});
			}
			
			canvas.mousemove(function(event) {
				 HandlerLib.dragItemsMoveHandler(object, event, startX, startY);
			});
			
			var mouseup = function(mouseupEvent){
				// finish dragging - restore various elements' default state
				items.isDragged = false;
				items.unmouseup();
				HandlerLib.resetCanvasMode(true);
				if (layout.bin.glowEffect) {
					layout.bin.glowEffect.remove();
					layout.bin.glowEffect = null;
				}
				
				// do whetver needs to be done with the dragged elements
				mouseupHandler(mouseupEvent);
			};
			
			/* The event is passed from items to canvas, so it is OK to assign it only to canvas.
			   Unfortunately, this does not apply to the icon.
			   Also, if mousedown was on items and mouseup on canvas (very quick move),
			   items will not accept mouseup until click.
			*/
			canvas.mouseup(mouseup);
			
			GeneralLib.setModified(true);
		}, layout.conf.dragStartThreshold);
	},
	
	
	/**
	 * Moves dragged elements on the canvas.
	 */
	dragItemsMoveHandler : function(object, event, startX, startY) {
		var dx = GeneralLib.snapToGrid(event.pageX - startX, true),
			dy = GeneralLib.snapToGrid(event.pageY - startY, true);

		object.items.transform('t' + dx + ' ' + dy);
		
		if (object.transitions) {
			$.each(object.transitions.from, function(){
				this.draw();
			});
			$.each(object.transitions.to, function(){
				this.draw();
			});
		}
		
		// highlight rubbish bin if dragged elements are over it
		if (HandlerLib.isElemenentBinned(event)) {
			if (!layout.bin.glowEffect) {
				layout.bin.glowEffect = paper.path(Snap.format('M {x} {y} h {side} v {side} h -{side} z',
												   {
													'x'     : layout.bin.attr('x'),
													'y'     : layout.bin.attr('y'),
													'side'  : layout.bin.attr('width')
												   }))
								   			.attr({
												   'stroke'           : layout.colors.binSelect,
												   'stroke-width'     : 2,
												   'stroke-dasharray' : '5,3',
												   'fill' : 'none'
												  });
			}
		} else if (layout.bin.glowEffect){
			layout.bin.glowEffect.remove();
			layout.bin.glowEffect = null;
		}
	},
	
	
	/**
	 * Rewrites a shape's coordinates, so it is where the user dropped it.
	 */
	dropObject : function(object, cancel) {
		// finally transform the dragged elements
		var transformation = Snap.parseTransformString(object.items.transform().string);
		object.items.transform('');
		
		// cancel means that the object will be redrawn in its original place
		if (cancel) {
			object.draw();
		} else {
			// finialise the drop
			var box = object.items.getBBox(),
				originalCoordinates = {
					x : box.x,
					// adjust this coordinate for annotation labels
					y : box.y + (object instanceof DecorationDefs.Label ? 6 : 0)
				};
			
			if (transformation && transformation.length > 0) {
				object.draw(originalCoordinates.x + transformation[0][1],
							originalCoordinates.y + transformation[0][2]);
			}
			
			// add space if dropped object is next to border
			GeneralLib.resizePaper();
			
			return originalCoordinates;
		}
	},
	
	
	/**
	 * Checks whether activity or transition is over rubbish bin.
	 */
	isElemenentBinned : function(event) {
		var translatedEvent = GeneralLib.translateEventOnCanvas(event);
		return Snap.path.isPointInsideBBox(layout.bin.getBBox(), translatedEvent[0], translatedEvent[1]); 
	},
	
	
	/**
	 * Selects an activity/transition/annotation.
	 */
	itemClickHandler : function(event) {
		if ((event.ctrlKey || event.metaKey) || layout.drawMode ||
			(event.originalEvent ? event.originalEvent.defaultPrevented : event.defaultPrevented)){
			return;
		}
		
		var parentObject = ActivityLib.getParentObject(this);
		// if it's "import part" allow multiple selection of activities
		if (activitiesOnlySelectable) {
			if (parentObject.items.selectEffect) {
				ActivityLib.removeSelectEffect(parentObject);
			} else {
				ActivityLib.addSelectEffect(parentObject);	
			} 
		} else if (parentObject != layout.selectedObject) {
			HandlerLib.canvasClickHandler(event);
			ActivityLib.addSelectEffect(parentObject, true);
		}
		
		// so canvas handler unselectActivityHandler() is not run
		event.preventDefault();
	},
	
	
	/**
	 * Default mode for canvas. Run after draw mode is no longer needed.
	 */
	resetCanvasMode : function(init){
		// so elements understand that no events should be triggered, i.e. canvas is "dead"
		layout.drawMode = !init;
		
		// remove selection if exists
		ActivityLib.removeSelectEffect();
		canvas.css('cursor', 'default')
		      .off('click')
		      .off('mousedown')
		      .off('mouseup')
		      .off('mousemove');
		
		// if the user started adding a branching and did not finish it
		if (layout.addBranchingStart){
			layout.infoDialog.modal('hide');
			
			if (layout.addBranchingStart instanceof ActivityDefs.BranchingEdgeActivity) {
				layout.activities.splice(layout.activities.indexOf(layout.addBranchingStart), 1);
				layout.addBranchingStart.items.remove();
			}
			layout.addBranchingStart = null;
		}

		if (init) {
			 // if clicked anywhere, activity selection is gone
			canvas.click(HandlerLib.canvasClickHandler)
		     // when mouse gets closer to properties dialog, make it fully visible
		    	  .mousemove(HandlerPropertyLib.approachPropertiesDialogHandler);
		}
	}
},



/**
 * Contains handlers for actions over Activities.
 */
HandlerActivityLib = {
	// double tap support
	tapTimeout : 500,
	lastTapTime : 0,
	lastTapTarget : null,
		
	/**
	 * Double click opens activity authoring.
	 */
	activityClickHandler : function(event) {
		var activity = ActivityLib.getParentObject(this),
			currentTime = new Date().getTime();
		// is the second click on the same activity as the first one?
		if (activity == HandlerActivityLib.lastTapTarget) {
			// was the second click quick enough after the first one?
			var tapLength = currentTime - HandlerActivityLib.lastTapTime;
			if (tapLength < HandlerActivityLib.tapTimeout && tapLength > 0) {
				event.preventDefault();
				if (activity.readOnly) {
					layout.infoDialog.data('show')(LABELS.LIVEEDIT_READONLY_ACTIVITY_ERROR);
				} else {
					ActivityLib.openActivityAuthoring(activity);
				}
				return;
			}
		}
		HandlerActivityLib.lastTapTime = currentTime;
		HandlerActivityLib.lastTapTarget = activity;
		// single click
		HandlerLib.itemClickHandler.call(this, event);
	},
	
	
	/**
	 * Starts drawing a transition or dragging an activity.
	 */
	activityMousedownHandler : function(event, x, y){
		if (layout.drawMode || (event.originalEvent ?
				event.originalEvent.defaultPrevented : event.defaultPrevented)){
			return;
		}
		
		var activity = ActivityLib.getParentObject(this);
		if (event.ctrlKey || event.metaKey) {
			 // when CTRL is held down, start drawing a transition
			 HandlerTransitionLib.drawTransitionStartHandler(activity, event, x, y);
		} else if (!activity.parentActivity
				|| !(activity.parentActivity instanceof ActivityDefs.ParallelActivity)){
			var mouseupHandler = function(event){
				if (HandlerLib.isElemenentBinned(event)) {
					// if the activity was over rubbish bin, remove it
					var canRemove = true;
					// check if the activity or its parent are read-only
					if (activity.readOnly) {
						canRemove = false;
						layout.infoDialog.data('show')(LABELS.LIVEEDIT_REMOVE_ACTIVITY_ERROR);
					} else if (activity.branchingActivity){
						if (activity.branchingActivity.readOnly) {
							canRemove = false;
							layout.infoDialog.data('show')(LABELS.LIVEEDIT_REMOVE_ACTIVITY_ERROR);
						}
					}
					else if (activity.parentActivity && activity.parentActivity.readOnly){
						canRemove = false;
						layout.infoDialog.data('show')(LABELS.LIVEEDIT_REMOVE_PARENT_ERROR);
					}
					
					if (canRemove) {
						ActivityLib.removeActivity(activity);
					} else {
						//revert to the original position
						HandlerLib.dropObject(activity, true);
					}
				} else {
					// finalise movement - rewrite coordinates, see if the activity was not added to a container
					var originalCoordinates = HandlerLib.dropObject(activity),
						translatedEvent = GeneralLib.translateEventOnCanvas(event),
						endX = translatedEvent[0],
						endY = translatedEvent[1],
						dropAllowed = ActivityLib.dropActivity(activity, endX, endY);
					
					if (!dropAllowed) {
						// dropping the activity in this place is forbidden, revert the changes
						activity.draw(originalCoordinates.x, originalCoordinates.y);
					}
				}
			}

			var transitions = activity.transitions.from.concat(activity.transitions.to);
			// start dragging the activity
			HandlerLib.dragItemsStartHandler(activity, this, mouseupHandler, event, x, y, transitions);
		}
	},
	
	
	/**
	 * Lighthens up branching edges in the same colour for identifictation.
	 */
	branchingEdgeMouseoverHandler : function() {
		var branchingActivity = ActivityLib.getParentObject(this).branchingActivity,
			startItems = branchingActivity.start.items,
			endItems = branchingActivity.end.items;
		if (!startItems.isDragged && !endItems.isDragged) {
			startItems.shape.addClass('branching-match');
			endItems.shape.addClass('branching-match');
		}
	},
	
	
	/**
	 * Return branching edges to their normal colours.
	 */
	branchingEdgeMouseoutHandler : function() {
		var branchingActivity = ActivityLib.getParentObject(this).branchingActivity,
			startItems = branchingActivity.start.items,
			endItems = branchingActivity.end.items;
		
		if (!startItems.isDragged && !endItems.isDragged) {
			startItems.shape.removeClass('branching-match');
			endItems.shape.removeClass('branching-match');
		}
	}
},



/**
 * Contains handlers for actions over Decoration elements.
 */
HandlerDecorationLib = {
		
	/**
	 * Starts dragging a container
	 */
	containerMousedownHandler : function(event, x, y){
		if (layout.drawMode || (event.originalEvent ?
				event.originalEvent.defaultPrevented : event.defaultPrevented)){
			return;
		}
		
		var container = ActivityLib.getParentObject(this);
		// allow transition dragging
		var mouseupHandler = function(event){
			if (HandlerLib.isElemenentBinned(event)) {
				// if the container was over rubbish bin, remove it
				if (container instanceof DecorationDefs.Region) {
					DecorationLib.removeRegion(container);
				} else {
					var canRemove = true;
					if (container.readOnly) {
						canRemove = false;
						layout.infoDialog.data('show')(LABELS.LIVEEDIT_REMOVE_ACTIVITY_ERROR);
					} else if (container.childActivities){
						// if any of the child activities is read-only, the parent activity can not be removed
						$.each(container.childActivities, function(){
							if (this.readOnly) {
								canRemove = false;
								layout.infoDialog.data('show')(LABELS.LIVEEDIT_REMOVE_CHILD_ERROR);
								return false;
							}
						});
					}
					
					if (canRemove) {
						ActivityLib.removeActivity(container);
					} else {
						// revert the activity back to its original place
						HandlerLib.dropObject(container, true);
					}
				}
			} else {
				HandlerLib.dropObject(container);
				if (container instanceof ActivityDefs.FloatingActivity
					|| container instanceof ActivityDefs.OptionalActivity
					|| container instanceof ActivityDefs.ParallelActivity) {
					ActivityLib.dropActivity(container);
				}
			}
		}
			
		HandlerLib.dragItemsStartHandler(container, this, mouseupHandler, event, x, y);
	},
		
	/**
	 * Start drawing a region.
	 */
	drawRegionStartHandler : function(startEvent) {
		HandlerLib.resetCanvasMode();
		
		// remember what were the drawing start coordinates
		var translatedEvent = GeneralLib.translateEventOnCanvas(startEvent),
			data = {
				'startX' : translatedEvent[0],
				'startY' : translatedEvent[1]
			};
			
		canvas.mousemove(function(event){
			HandlerDecorationLib.drawRegionMoveHandler(data, event);
		})
		.mouseup(function(event){
			HandlerDecorationLib.drawRegionEndHandler(data);
		});
	},
	
	
	/**
	 * Keep drawing a region.
	 */
	drawRegionMoveHandler : function(data, event) {
		var translatedEvent = GeneralLib.translateEventOnCanvas(event),
			x = translatedEvent[0],
			y = translatedEvent[1];
		
		if (data.shape) {
			// remove the previous rectangle
			data.shape.remove();
		}
		
		data.shape = paper.path(Snap.format('M {x} {y} h {width} v {height} h -{width} z',
											{
											 'x' : x < data.startX ? x : data.startX,
											 'y' : y < data.startY ? y : data.startY,
											 'width' : Math.abs(x - data.startX),
											 'height' : Math.abs(y - data.startY)
											})
							   )
						  .attr({
							'fill'    : layout.colors.annotation,
							'opacity' : 0.3
						  });
		
		// immediatelly show which activities will be enveloped
		var childActivities = DecorationLib.getChildActivities(data.shape);
		$.each(layout.activities, function(){
			if (!this.parentActivity && $.inArray(this, childActivities) > -1){
				ActivityLib.addSelectEffect(this, false);
			} else {
				ActivityLib.removeSelectEffect(this);
			}
		});
	},
	
	
	/**
	 * Finalise region drawing.
	 */
	drawRegionEndHandler : function(data) {
		// remove select effect from all activities
		$.each(layout.activities, function(){
			ActivityLib.removeSelectEffect(this);
		});
		
		if (data.shape) {
			var box = data.shape.getBBox(),
				region = DecorationLib.addRegion(box.x, box.y, box.x2, box.y2);
			data.shape.remove();
			ActivityLib.addSelectEffect(region, true);
		}
		HandlerLib.resetCanvasMode(true);
	},
	
	
	/**
	 * Starts dragging a label
	 */
	labelMousedownHandler : function(event, x, y){
		if (layout.drawMode || (event.originalEvent ?
				event.originalEvent.defaultPrevented : event.defaultPrevented)){
			return;
		}
		
		var label = ActivityLib.getParentObject(this);
		// allow transition dragging
		var mouseupHandler = function(event){
			if (HandlerLib.isElemenentBinned(event)) {
				// if the region was over rubbish bin, remove it
				DecorationLib.removeLabel(label);
			} else {
				HandlerLib.dropObject(label);
			}
		}
			
		HandlerLib.dragItemsStartHandler(label, this, mouseupHandler, event, x, y);
	},
	
	
	/**
	 * Start resizing a region.
	 */
	resizeRegionStartHandler : function(event) {
		// otherwise mousedown handler (dragging) can be triggered
		event.stopImmediatePropagation();
		event.preventDefault();
		
		HandlerLib.resetCanvasMode();
		
		var region = ActivityLib.getParentObject(this);
			
		canvas.mousemove(function(event){
			HandlerDecorationLib.resizeRegionMoveHandler(region, event);
		})
		.mouseup(function(){
			HandlerLib.resetCanvasMode(true);
			ActivityLib.addSelectEffect(region, true);
		});
		
		GeneralLib.setModified(true);
	},
	
	
	/**
	 * Keep resising a region.
	 */
	resizeRegionMoveHandler : function(region, event){
		var translatedEvent = GeneralLib.translateEventOnCanvas(event),
			x = translatedEvent[0],
			y = translatedEvent[1];
	
		// keep the initial coordinates and adjust end coordinates
		region.draw(null, null, x, y);
	}
},



/**
 * Contains handlers for actions over Properties dialog.
 */
HandlerPropertyLib = {
		
	/**
	 * Makes properties dialog fully visible.
	 */
	approachPropertiesDialogHandler : function(event) {
		// properties dialog is a singleton
		var dialog = layout.propertiesDialog,
			// do not run this method too often
			thisRun = new Date().getTime();
		if (thisRun - dialog.data('lastRun') < layout.conf.propertiesDialogDimThrottle){
			return;
		}
		dialog.data('lastRun', thisRun);
		
		// is the dialog visible at all?
		if (layout.selectedObject) {
			// calculate dim/show threshold
			var dialogPosition = dialog.offset(),
			    dialogStartX = dialogPosition.left,
			    dialogStartY = dialogPosition.top,
			    dialogEndX   = dialogStartX + dialog.width(),
			    dialogEndY   = dialogStartY + dialog.height(),
			    dimTreshold = layout.conf.propertiesDialogDimThreshold,
			    tooFarX = event.pageX < dialogStartX - dimTreshold || event.pageX > dialogEndX + dimTreshold,
			    tooFarY = event.pageY < dialogStartY - dimTreshold || event.pageY > dialogEndY + dimTreshold,
			    opacity = tooFarX || tooFarY ? layout.conf.propertiesDialogDimOpacity : 1;

			    dialog.css('opacity', opacity);
		}
	}	
},



/**
 * Contains handlers for actions over Transitions
 */
HandlerTransitionLib = {

	/**
	 * Start drawing a transition.
	 */
	drawTransitionStartHandler : function(activity, event, x, y) {
		if (activity.fromTransition && !(activity instanceof ActivityDefs.BranchingEdgeActivity)) {
			layout.infoDialog.data('show')(LABELS.TRANSITION_FROM_EXISTS_ERROR);
		}
		
		HandlerLib.resetCanvasMode();
		
		var startX = x  + canvas.scrollLeft() - canvas.offset().left,
			startY = y  + canvas.scrollTop()  - canvas.offset().top;
		
		canvas.mousemove(function(event){
			HandlerTransitionLib.drawTransitionMoveHandler(activity, event, startX, startY);
		})
		.mouseup(function(event){
			HandlerTransitionLib.drawTransitionEndHandler(activity, event);
		});
	},
	
	
	/**
	 * Keep drawing a transition.
	 */
	drawTransitionMoveHandler : function(activity, event, startX, startY) {
		// remove the temporary transition (dashed line)
		if (activity.tempTransition) {
			activity.tempTransition.remove();
			activity.tempTransition = null;
		}
		
		var translatedEvent = GeneralLib.translateEventOnCanvas(event),
			endX = translatedEvent[0],
			endY = translatedEvent[1];
		// draw a temporary transition so user sees what he is doing
		activity.tempTransition = Snap.set();
		activity.tempTransition.push(paper.circle(startX, startY, 3).attr({
			'stroke' : layout.colors.transition
		}));
		activity.tempTransition.push(paper.path(Snap.format('M {startX} {startY} L {endX} {endY}',
															{
															 'startX' : startX,
															 'startY' : startY,
															 'endX'   : endX,
															 'endY'   : endY
															}))
										  .addClass('transition-draw')
									);
	},
	
	
	/**
	 * Finalise transition drawing.
	 */
	drawTransitionEndHandler : function(activity, event) {
		// prevent triggering event on several activity items; we just need it on transition
		event.stopImmediatePropagation();
		event.preventDefault();
		
		//remove the temporary transition (dashed line)
		if (activity.tempTransition) {
			activity.tempTransition.remove();
			activity.tempTransition = null;
		}
		var endActivity = null,
			targetElement = Snap.getElementByPoint(event.pageX, event.pageY);
		if (targetElement) {
			endActivity = ActivityLib.getParentObject(targetElement);
		}
		if (endActivity == null) {
			endActivity = ActivityLib.getParentObject(targetElement.parent());
		}

		if (endActivity && activity != endActivity) {
			ActivityLib.addTransition(activity, endActivity);
		}
		
		HandlerLib.resetCanvasMode(true);
	},
	
	
	
	/**
	 * Starts dragging a transition.
	 */
	transitionMousedownHandler : function(event, x, y){
		var transition = ActivityLib.getParentObject(this);
		// allow transition dragging
		var mouseupHandler = function(event){
			if (HandlerLib.isElemenentBinned(event)) {
				if (transition.toActivity.readOnly || transition.fromActivity.readOnly) {
					layout.infoDialog.data('show')(LABELS.LIVEEDIT_REMOVE_TRANSITION_ERROR);
					// just draw it again in the original place
					transition.draw();
				} else {
					// if the transition was over rubbish bin, remove it
					ActivityLib.removeTransition(transition);
				}
			} else {
				transition.draw();
			}
		}
			
		HandlerLib.dragItemsStartHandler(transition, this, mouseupHandler, event, x, y);
	}
};
