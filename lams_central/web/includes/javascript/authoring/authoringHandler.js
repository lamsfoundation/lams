/**
 * This file contains event handlers for interaction with canvas and other Authoring elements.
 */

var HandlerLib = {
		
	/**
	 * Default mode for canvas. Run after special mode is no longer needed.
	 */
	resetCanvasMode : function(){
		// remove selection if exists
		ActivityLib.removeSelectEffect();
		
		canvas.css('cursor', 'default')
		      .off('click')
		      // if clicked anywhere, activity selection is gone
		      .click(HandlerLib.canvasClickHandler)
		      .off('mouseup')
		      .off('mousemove')
		      // when mouse gets closer to properties dialog, make it fully visible
		      .mousemove(HandlerLib.approachPropertiesDialogHandler);
	},
	
	
	/**
	 * Makes properties dialog fully visible.
	 */
	approachPropertiesDialogHandler : function(event) {
		// properties dialog is a singleton
		var dialog = layout.items.propertiesDialog;
		// do not run this method too often
		var thisRun = new Date().getTime();
		if (thisRun - dialog.lastRun < layout.conf.propertiesDialogDimThrottle){
			return;
		}
		dialog.lastRun = thisRun;
		
		// is the dialog visible at all?
		var activity = layout.items.selectedActivity;
		if (activity) {
			// calculate dim/show threshold
			var container = dialog.container,
			    dialogPosition = container.offset(),
			    dialogStartX = dialogPosition.left,
			    dialogStartY = dialogPosition.top,
			    dialogEndX   = dialogStartX + container.width(),
			    dialogEndY   = dialogStartY + container.height(),
			    dimTreshold = layout.conf.propertiesDialogDimThreshold,
			    tooFarX = event.pageX < dialogStartX - dimTreshold || event.pageX > dialogEndX + dimTreshold,
			    tooFarY = event.pageY < dialogStartY - dimTreshold || event.pageY > dialogEndY + dimTreshold,
			    opacity = tooFarX || tooFarY ? layout.conf.propertiesDialogDimOpacity : 1;

			container.css('opacity', opacity);
		}
	},

	/**
	 * Remove activity selection when user clicks on canvas.
	 */
	canvasClickHandler : function(event) {
		// check if user clicked on empty space on canvas
		// or on some element on top of it
		var defaultPrevented = event.originalEvent ?
				event.originalEvent.defaultPrevented : event.defaultPrevented;
		if (!defaultPrevented) {
			ActivityLib.removeSelectEffect();
		}
	},
	
	
	/**
	 * Start dragging an activity or transition.
	 */
	dragItemsStartHandler : function(items, draggedElement, mouseupHandler, event, startX, startY) {
		items.toFront();
		// clear "clicked" flag, just in case
		items.clicked = false;
		
		if (items.dragStarter) {
			// prevent confusion when double clicking
			clearTimeout(items.dragStarter);
		}
		
		// run only if "click" event was not generated, i.e. user really wants to drag
		items.dragStarter = setTimeout(function(){
			items.dragStarter = null;
			// "clicked" flag is set by mouseup event
			// and it means that user does not want to drag, but to click
			if (items.clicked) {
				items.clicked = false;
				return;
			}
			items.isDragged = true;
			items.attr('cursor', 'move');
			
			canvas.mousemove(function(event) {
				 HandlerLib.dragItemsMoveHandler(items, event, startX, startY);
			});
			
			var mouseup = function(mouseupEvent){
				// finish dragging - restore various elements' default state
				items.isDragged = false;
				items.unmouseup();
				HandlerLib.resetCanvasMode();
				layout.items.bin.attr('fill', 'transparent');
				
				// do whetver needs to be done with the dragged elements
				mouseupHandler(mouseupEvent);
			};
			
			// if user moves mouse very quickly, mouseup event can be triggered
			// for canvas, not the dragged elements
			canvas.mouseup(mouseup);
			items.mouseup(mouseup);
		}, layout.conf.dragStartThreshold);
	},
	
	
	/**
	 * Moves dragged elements on the canvas.
	 */
	dragItemsMoveHandler : function(items, event, startX, startY) {
		var dx = event.pageX - startX,
			dy = event.pageY - startY;
		
		items.transform('t' + dx + ' ' + dy);
		
		if (items.groupingEffect) {
			items.groupingEffect.toBack();
		}
		
		// highlight rubbish bin if dragged elements are over it
		if (HandlerLib.isElemenentBinned(event)) {
			layout.items.bin.attr('fill', layout.colors.binActive);
		} else {
			layout.items.bin.attr('fill', 'transparent');
		}
	},
	
	/**
	 * Start drawing a transition.
	 */
	drawTransitionStartHandler : function(activity, event, x, y) {
		if (activity.fromTransition && activity.type != 'branchingEdge') {
			alert('Transition from this activity already exists');
			return;
		}
		
		var startX = x - canvas.offset().left,
			startY = y - canvas.offset().top;
		
		canvas.mousemove(function(event){
			HandlerLib.drawTransitionMoveHandler(activity, event, startX, startY);
		})
		.mouseup(function(event){
			HandlerLib.drawTransitionEndHandler(activity, event);
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
		if (!event.ctrlKey) {
			canvas.off('mousemove');
			canvas.off('mouseup');
			return;
		}
		
		var endX = event.pageX - canvas.offset().left,
			endY = event.pageY - canvas.offset().top;
		
		// draw a temporary transition so user sees what he is doing
		activity.tempTransition = paper.set();
		activity.tempTransition.push(paper.circle(startX, startY, 3));
		activity.tempTransition.push(paper.path('M ' + startX + ' ' + startY
				+ 'L ' + endX  + ' ' + endY).attr({
					'arrow-end' : 'open-wide-long',
					'stroke-dasharray' : '- '
				}));
	},
	
	
	/**
	 * Finalise transition drawing.
	 */
	drawTransitionEndHandler : function(activity, event) {
		// prevent triggering event on several activity items; we just need on transition
		event.stopImmediatePropagation();
		HandlerLib.resetCanvasMode();
		
		//remove the temporary transition (dashed line)
		if (activity.tempTransition) {
			activity.tempTransition.remove();
			activity.tempTransition = null;
		}
		
		var endActivity = null;
		var targetElement = paper.getElementByPoint(event.pageX, event.pageY);
		if (targetElement) {
			endActivity = targetElement.data('activity');
		}

		if (endActivity && activity != endActivity) {
			ActivityLib.drawTransition(activity, endActivity);
		}
	},
	
	
	/**
	 * Lighthens up branching edges in the same colour for identifictation.
	 */
	branchingEdgeMouseoverHandler : function() {
		var branchingActivity = this.data('activity').branchingActivity,
			startItems = branchingActivity.start.items,
			endItems = branchingActivity.end.items;
		if (!startItems.isDragged && !endItems.isDragged) {
			startItems.shape.attr('fill', layout.colors.branchingEdgeMatch);
			endItems.shape.attr('fill', layout.colors.branchingEdgeMatch);
		}
	},
	
	
	/**
	 * Return branching edges to their normal colours.
	 */
	branchingEdgeMouseoutHandler : function() {
		var branchingActivity = this.data('activity').branchingActivity,
			startItems = branchingActivity.start.items,
			endItems = branchingActivity.end.items;
		
		if (!startItems.isDragged && !endItems.isDragged) {
			startItems.shape.attr('fill', layout.colors.branchingEdgeStart);
			endItems.shape.attr('fill', layout.colors.branchingEdgeEnd);
		}
	},
	
	
	/**
	 * Starts drawing a transition or dragging an activity.
	 */
	activityMousedownHandler : function(event, x, y){
		var activity = this.data('activity');
		if (event.ctrlKey) {
			 // when CTRL is held down, start drawing a transition
			 HandlerLib.drawTransitionStartHandler(activity, event, x, y);
		} else {
			var mouseupHandler = function(event){
				if (HandlerLib.isElemenentBinned(event)) {
					// if the activity was over rubbish bin, remove it
					ActivityLib.removeActivity(activity);
				} else {
					ActivityLib.dropActivity(activity);
				}
			}
			// start dragging the activity
			HandlerLib.dragItemsStartHandler(activity.items, this, mouseupHandler, event, x, y);
		}
	},
	
	
	/**
	 * Selects an activity.
	 */
	activityClickHandler : function(event) {
		var activity = this.data('activity');
		// inform that user wants to select, not drag the activity
		activity.items.clicked = true;
		if (activity != layout.items.selectedActivity) {
			HandlerLib.canvasClickHandler(event);
			ActivityLib.addSelectEffect(activity);
		}
		// so canvas handler unselectActivityHandler() is not run
		event.preventDefault();
	},
	
	
	/**
	 * Opens activity authoring.
	 */
	activityDblclickHandler : function(event) {
		var activity = this.data('activity');
		// inform that user wants to open, not drag the activity
		activity.items.clicked = true;
		ActivityLib.openActivityAuthoring(activity);
	},
	
	
	/**
	 * Starts dragging a transition.
	 */
	transitionMousedownHandler : function(event, x, y){
		var transition = this.data('transition');
		// allow transition dragging
		var mouseupHandler = function(event){
			if (HandlerLib.isElemenentBinned(event)) {
				// if the transition was over rubbish bin, remove it
				ActivityLib.removeTransition(transition);
			} else {
				ActivityLib.dropTransition(transition);
			}
		}
			
		HandlerLib.dragItemsStartHandler(transition, this, mouseupHandler, event, x, y);
	},
	
	
	/**
	 * Checks whether activity or transition is over rubbish bin.
	 */
	isElemenentBinned : function(event) {
		var canvasX = event.pageX + canvas.scrollLeft() - canvas.offset().left,
			canvasY = event.pageY + canvas.scrollTop()  - canvas.offset().top;
		
		// highlight rubbish bin if dragged elements are over it
		return Raphael.isPointInsideBBox(layout.items.bin.getBBox(), canvasX, canvasY); 
	}
};