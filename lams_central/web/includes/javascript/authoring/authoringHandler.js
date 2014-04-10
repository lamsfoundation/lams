/**
 * This file contains event handlers for interaction with canvas and other Authoring elements.
 */

var HandlerLib = {
		
	/**
	 * Default mode for canvas. Run after special mode is no longer needed.
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

		if (init) {
			 // if clicked anywhere, activity selection is gone
			canvas.click(HandlerLib.canvasClickHandler)
		     // when mouse gets closer to properties dialog, make it fully visible
		    	  .mousemove(HandlerLib.approachPropertiesDialogHandler);
		}
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
			
			HandlerLib.resetCanvasMode();
			items.isDragged = true;
			items.attr('cursor', 'move');
			
			var parentObject = draggedElement.data('parentObject');
				sticky = parentObject && (parentObject instanceof ActivityLib.ParallelActivity
										  || parentObject instanceof ActivityLib.OptionalActivity
										  || parentObject instanceof ActivityLib.FloatingActivity);
			if (sticky) {
				$.each(parentObject.childActivities, function(){
					this.items.hide();
				});
			}
			
			canvas.mousemove(function(event) {
				 HandlerLib.dragItemsMoveHandler(items, event, startX, startY);
			});
			
			var mouseup = function(mouseupEvent){
				// finish dragging - restore various elements' default state
				items.isDragged = false;
				items.unmouseup();
				layout.items.bin.attr('fill', 'transparent');
				
				// do whetver needs to be done with the dragged elements
				mouseupHandler(mouseupEvent);
				HandlerLib.resetCanvasMode(true);
			};
			
			// if user moves mouse very quickly, mouseup event can be triggered
			// for canvas, not the dragged elements
			canvas.mouseup(mouseup);
			items.mouseup(mouseup);
			
			setModified(true);
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
		if (activity.fromTransition && !(activity instanceof ActivityLib.BranchingEdgeActivity)) {
			alert('Transition from this activity already exists');
			return;
		}
		
		HandlerLib.resetCanvasMode();
		
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
		
		HandlerLib.resetCanvasMode(true);
	},
	
	
	/**
	 * Start drawing a region.
	 */
	drawRegionStartHandler : function(startEvent) {
		HandlerLib.resetCanvasMode();
		
		// remember what were the drawing start coordinates
		var translatedEvent = ActivityLib.translateEventOnCanvas(startEvent),
			data = {
				'startX' : translatedEvent[0],
				'startY' : translatedEvent[1]
			};
			
		canvas.mousemove(function(event){
			HandlerLib.drawRegionMoveHandler(data, event);
		})
		.mouseup(function(event){
			HandlerLib.drawRegionEndHandler(data);
		});
	},
	
	
	/**
	 * Keep drawing a region.
	 */
	drawRegionMoveHandler : function(data, event) {
		var translatedEvent = ActivityLib.translateEventOnCanvas(event),
			x = translatedEvent[0],
			y = translatedEvent[1];
		
		if (data.shape) {
			// remove the previous rectangle
			data.shape.remove();
		}
		
		data.shape = paper.path(Raphael.format('M {0} {1} h {2} v {3} h -{2} z',
								x < data.startX ? x : data.startX,
								y < data.startY ? y : data.startY,
								Math.abs(x - data.startX),
								Math.abs(y - data.startY)))
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
	 * Start resizing a region.
	 */
	resizeRegionStartHandler : function(event) {
		// otherwise mousedown handler (dragging) can be triggered
		event.stopImmediatePropagation();
		event.preventDefault();
		
		HandlerLib.resetCanvasMode();
		
		var region = this.data('parentObject');
			
		canvas.mousemove(function(event){
			HandlerLib.resizeRegionMoveHandler(region, event);
		})
		.mouseup(function(){
			HandlerLib.resetCanvasMode(true);
			ActivityLib.addSelectEffect(region, true);
		});
		
		setModified(true);
	},
	
	
	/**
	 * Keep resising a region.
	 */
	resizeRegionMoveHandler : function(region, event){
		var translatedEvent = ActivityLib.translateEventOnCanvas(event),
			x = translatedEvent[0],
			y = translatedEvent[1];
	
		// keep the initial coordinates and adjust end coordinates
		region.draw(null, null, x, y);
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
		} else if (!activity.parentActivity
				|| !(activity.parentActivity instanceof ActivityLib.ParallelActivity)){
			var mouseupHandler = function(event){
				if (HandlerLib.isElemenentBinned(event)) {
					// if the activity was over rubbish bin, remove it
					ActivityLib.removeActivity(activity);
				} else {
					HandlerLib.dropObject(activity);

					var translatedEvent = ActivityLib.translateEventOnCanvas(event),
						endX = translatedEvent[0],
						endY = translatedEvent[1];
					ActivityLib.dropActivity(activity, endX, endY);
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
			ActivityLib.addSelectEffect(parentObject, true);
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
	 * Starts dragging a container
	 */
	containerMousedownHandler : function(event, x, y){
		if (layout.drawMode || (event.originalEvent ?
				event.originalEvent.defaultPrevented : event.defaultPrevented)){
			return;
		}
		
		var container = this.data('parentObject');
		// allow transition dragging
		var mouseupHandler = function(event){
			if (HandlerLib.isElemenentBinned(event)) {
				// if the container was over rubbish bin, remove it
				if (container instanceof DecorationLib.Region) {
					DecorationLib.removeRegion(container);
				} else {
					ActivityLib.removeActivity(container);
				}
			} else {
				HandlerLib.dropObject(container);
				if (container instanceof ActivityLib.FloatingActivity
					|| container instanceof ActivityLib.OptionalActivity
					|| container instanceof ActivityLib.ParallelActivity) {
					ActivityLib.dropActivity(container);
				}
			}
		}
			
		HandlerLib.dragItemsStartHandler(container.items, this, mouseupHandler, event, x, y);
	},
	
	
	/**
	 * Starts dragging a label
	 */
	labelMousedownHandler : function(event, x, y){
		if (layout.drawMode || (event.originalEvent ?
				event.originalEvent.defaultPrevented : event.defaultPrevented)){
			return;
		}
		
		var label = this.data('parentObject');
		// allow transition dragging
		var mouseupHandler = function(event){
			if (HandlerLib.isElemenentBinned(event)) {
				// if the region was over rubbish bin, remove it
				DecorationLib.removeLabel(label);
			} else {
				HandlerLib.dropObject(label);
			}
		}
			
		HandlerLib.dragItemsStartHandler(label.items, this, mouseupHandler, event, x, y);
	},
	
	
	dropObject : function(object) {
		// finally transform the dragged elements
		var transformation = object.items.shape.attr('transform');
		object.items.transform('');
		if (transformation.length > 0) {
			// find new X and Y and redraw the object
			var box = object.items.shape.getBBox(),
				x = box.x,
				// adjust this coordinate for annotation labels
				y = box.y + (object instanceof DecorationLib.Label ? 6 : 0);
			object.draw(x + transformation[0][1],
						y + transformation[0][2]);
		}
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