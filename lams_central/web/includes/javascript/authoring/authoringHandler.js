/**
 * This file contains event handlers for interaction with canvas and other Authoring elements.
 */

var HandlerLib = {
		
	/**
	 * Default mode for canvas. Run after special mode is no longer needed.
	 */
	resetCanvasMode : function(){
		canvas.css('cursor', 'default')
		      .off('click')
		      // if clicked anywhere, activity selection is gone
		      .click(HandlerLib.unselectActivityHandler)
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
	 * Show selection border around the clicked activity.
	 */
	selectActivityHandler : function(event, activity) {
		if (activity != layout.items.selectedActivity) {
			HandlerLib.unselectActivityHandler(event);
			ActivityLib.addSelectEffect(activity);
		}
		// so canvas handler unselectActivityHandler() is not run
		event.preventDefault();
	},
	
	
	/**
	 * Remove activity selection when user clicks on canvas.
	 */
	unselectActivityHandler : function(event) {
		// check if user clicked on empty space on canvas
		// or on some element on top of it
		var defaultPrevented = event.originalEvent ?
				event.originalEvent.defaultPrevented : event.defaultPrevented;
		if (!defaultPrevented) {
			var selectedActivity = layout.items.selectedActivity;
			// does selection exist at all?
			if (selectedActivity) {
				selectedActivity.items.selectEffect.remove();
				selectedActivity.items.selectEffect = null;
				
				// no selected activity = no properties dialog
				layout.items.propertiesDialog.dialog('close');
				layout.items.selectedActivity = null;
			}
		}
	},
	
	
	/**
	 * Open separate window with activity authoring on double click.
	 */
	openActivityAuthoringHandler : function(activity){
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
					'contentFolderID' : contentFolderID
				},
				success : function(response) {
					activity.authorURL = response.authorURL;
					activity.id = response.toolContentID;
					if (!contentFolderID) {
						// if LD did not have contentFolderID, it was just generated
						// so remember it
						contentFolderID = response.contentFolderID;
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
			
			var mouseup = function(){
				// finish dragging - restore various elements' default state
				items.isDragged = false;
				items.unmouseup();
				HandlerLib.resetCanvasMode();
				layout.items.bin.attr('fill', 'transparent');
				
				// do whetver needs to be done with the dragged elements
				mouseupHandler();
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
		var dx = event.pageX - startX;
		var dy = event.pageY - startY;
		items.transform('t' + dx + ' ' + dy);
		
		if (items.groupingEffect) {
			items.groupingEffect.toBack();
		}
		
		// highlight rubbish bin if dragged elements are over it
		if (Raphael.isBBoxIntersect(layout.items.bin.getBBox(), items.getBBox())) {
			layout.items.bin.attr('fill', layout.colors.binActive);
		} else {
			layout.items.bin.attr('fill', 'transparent');
		}
	},
	
	
	/**
	 * Drop the dragged activity on the canvas.
	 */
	dragActivityEndHandler : function(activity) {
		// if the activity was over rubbish bin, remove it
		if (Raphael.isBBoxIntersect(layout.items.bin.getBBox(), activity.items.shape.getBBox())) {
			ActivityLib.removeActivity(activity);
			return;
		}
		
		// finally transform the dragged elements
		var transformation = activity.items.shape.attr('transform');
		activity.items.transform('');
		if (transformation.length > 0) {
			activity.items.forEach(function(elem) {
				// some elements (rectangles) have "x", some are paths
				if (elem.attr('x')) {
					elem.attr({
						'x' : elem.attr('x') + transformation[0][1],
						'y' : elem.attr('y') + transformation[0][2]
					});
				} else {
					var path = elem.attr('path');
					elem.attr('path', Raphael.transformPath(path, transformation));
				}
			});
		}
		
		if (activity.items.groupingEffect) {
			activity.items.groupingEffect.toBack();
		}
		
		// redraw transitions
		if (activity.fromTransition) {
			ActivityLib.drawTransition(activity, activity.fromTransition.toActivity);
		}
		if (activity.toTransition) {
			ActivityLib.drawTransition(activity.toTransition.fromActivity, activity);
		}
	},
	
	
	/**
	 * Drop the dragged transition.
	 */
	dragTransitionEndHandler : function(transition) {
		// if the transition was over rubbish bin, remove it
		if (Raphael.isBBoxIntersect(layout.items.bin.getBBox(), transition.getBBox())) {
			ActivityLib.removeTransition(transition);
		} else {
			// cancel drag
			transition.transform('');
			transition.toBack();
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
		
		var startX = x - canvas.offset().left;
		var startY = y - canvas.offset().top;
		
		canvas.mousemove(function(event){
			HandlerLib.drawTransitionMoveHandler(activity, event, startX, startY);
		}).mouseup(function(event){
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
		
		var endX = event.pageX - canvas.offset().left;
		var endY = event.pageY - canvas.offset().top;
		
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
		HandlerLib.resetCanvasMode();
		
		//remove the temporary transition
		if (activity.tempTransition) {
			activity.tempTransition.remove();
			activity.tempTransition = null;
		}
		
		var endX = event.pageX - canvas.offset().left;
		var endY = event.pageY - canvas.offset().top;
		var endActivity = null;
		// find the target activity
		$.each(activities, function(){
			if (this.items.shape.isPointInside(endX, endY)) {
				endActivity = this;
				return false;
			}
		});

		if (endActivity) {
			ActivityLib.drawTransition(activity, endActivity);
		}
	}
};