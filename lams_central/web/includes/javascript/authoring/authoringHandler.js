/**
 * This file contains event handlers for interaction with canvas and other Authoring elements.
 */

var HandlerLib = {
		
	/**
	 * Default mode for canvas. Run after special mode is no longer needed.
	 */
	resetCanvasMode : function(){
		layout.drawMode = false;
		
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
		if (layout.items.selectedObject) {
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
		if (layout.drawMode || (event.originalEvent ?
				event.originalEvent.defaultPrevented : event.defaultPrevented)){
			return;
		}
		
		ActivityLib.removeSelectEffect();
	},
	
	
	/**
	 * Start dragging an activity or transition.
	 */
	dragItemsStartHandler : function(items, draggedElement, mouseupHandler, event, startX, startY) {
		// clear "clicked" flag, just in case
		items.clicked = false;
		
		if (items.dragStarter) {
			// prevent confusion when double clicking
			clearTimeout(items.dragStarter);
		}
		
		if (layout.drawMode || (event.originalEvent ?
				event.originalEvent.defaultPrevented : event.defaultPrevented)){
			return;
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
		
		var startX = x  + canvas.scrollLeft() - canvas.offset().left,
			startY = y  + canvas.scrollTop()  - canvas.offset().top;
		
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
		
		var translatedEvent = ActivityLib.translateEventOnCanvas(event),
			endX = translatedEvent[0],
			endY = translatedEvent[1];
		
		// draw a temporary transition so user sees what he is doing
		activity.tempTransition = paper.set();
		activity.tempTransition.push(paper.circle(startX, startY, 3));
		activity.tempTransition.push(paper.path(Raphael.format('M {0} {1} L {2} {3}',
														startX, startY, endX, endY))
					.attr({
						'arrow-end' : 'open-wide-long',
						'stroke-dasharray' : '- '
					}));
	},
	
	
	/**
	 * Finalise transition drawing.
	 */
	drawTransitionEndHandler : function(activity, event) {
		// prevent triggering event on several activity items; we just need it on transition
		event.stopImmediatePropagation();
		event.preventDefault();
		HandlerLib.resetCanvasMode();
		
		//remove the temporary transition (dashed line)
		if (activity.tempTransition) {
			activity.tempTransition.remove();
			activity.tempTransition = null;
		}
		
		var endActivity = null,
			targetElement = paper.getElementByPoint(event.pageX, event.pageY);
		if (targetElement) {
			endActivity = targetElement.data('parentObject');
		}

		if (endActivity && activity != endActivity) {
			ActivityLib.addTransition(activity, endActivity);
		}
	},
	
	
	/**
	 * Lighthens up branching edges in the same colour for identifictation.
	 */
	branchingEdgeMouseoverHandler : function() {
		var branchingActivity = this.data('parentObject').branchingActivity,
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
		var branchingActivity = this.data('parentObject').branchingActivity,
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
		if (layout.drawMode || (event.originalEvent ?
				event.originalEvent.defaultPrevented : event.defaultPrevented)){
			return;
		}
		
		var activity = this.data('parentObject');
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
	itemClickHandler : function(event) {
		if (layout.drawMode || (event.originalEvent ?
				event.originalEvent.defaultPrevented : event.defaultPrevented)){
			return;
		}
		
		var parentObject = this.data('parentObject');
		// inform that user wants to select, not drag the activity
		parentObject.items.clicked = true;
		if (parentObject != layout.items.selectedObject) {
			HandlerLib.canvasClickHandler(event);
			ActivityLib.addSelectEffect(parentObject);
		}	
		
		// so canvas handler unselectActivityHandler() is not run
		event.preventDefault();
	},
	
	
	/**
	 * Opens activity authoring.
	 */
	activityDblclickHandler : function(event) {
		var activity = this.data('parentObject');
		// inform that user wants to open, not drag the activity
		activity.items.clicked = true;
		ActivityLib.openActivityAuthoring(activity);
	},
	
	
	/**
	 * Starts dragging a transition.
	 */
	transitionMousedownHandler : function(event, x, y){
		var transition = this.data('parentObject');
		// allow transition dragging
		var mouseupHandler = function(event){
			if (HandlerLib.isElemenentBinned(event)) {
				// if the transition was over rubbish bin, remove it
				ActivityLib.removeTransition(transition);
			} else {
				transition.draw();
			}
		}
			
		HandlerLib.dragItemsStartHandler(transition.items, this, mouseupHandler, event, x, y);
	},
	
	
	/**
	 * Checks whether activity or transition is over rubbish bin.
	 */
	isElemenentBinned : function(event) {
		var translatedEvent = ActivityLib.translateEventOnCanvas(event);
		
		// highlight rubbish bin if dragged elements are over it
		return Raphael.isPointInsideBBox(layout.items.bin.getBBox(),
										 translatedEvent[0], translatedEvent[1]); 
	}
};