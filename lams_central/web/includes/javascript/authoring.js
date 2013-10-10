var paper = null;
var canvas = null;
var bin = null;
var maxActivityId = 0;
var activities = [];

var layout = {
	'conf' : {
		'activityWidth' : 125,
		'activityHeight' : 50	
	},
	'items' : {
		'bin' : null
	}
};

$(document).ready(function() {
	paper = Raphael('canvas');
	canvas = $('#canvas');
	
	initTemplates();
	layout.items.bin = paper.path('M 830 700 h -50 l 10 50 h 30 Z');
});


function initTemplates(){
	$('.template').each(function(){
		$(this).draggable({
			'containment' : '#authoringTable',
		    'revert'      : 'invalid',
		    'distance'    : 20,
		    'scroll'      : false,
		    'helper'      : function(event){
				var helper = $(this).clone().css({
					'border' : 'thin black solid',
					'z-index' : 1,
					'cursor' : 'move'
				});
				return helper;
			}
		});
	});
	
	canvas.droppable({
		   'tolerance'   : 'touch',
		   'accept'      : function (draggable) {
			   return true;
		   },
		   'drop'	     : function (event, draggable) {
			   ActivityUtils.addActivity(draggable);
		   }
	});
}


var ActivityUtils = {
	addActivity : function(template) {
		$(template.helper).remove();
		
		var activityId = ++maxActivityId;
		var activity = new Activity(activityId);
		activities.push(activity);
		
		var canvasOffset = canvas.offset();
		
		paper.setStart();
		var shape = activity.shape = paper.rect(
				template.offset.left - canvasOffset.left,
				template.offset.top - canvasOffset.top,
				layout.conf.activityWidth, layout.conf.activityHeight)
		.attr({
			'fill' : '#A9C8FD'
		});
		
		paper.image($('img', template.draggable).attr('src'), shape.attr('x')
				+ shape.attr('width') / 2 - 15, shape.attr('y') + 2, 30, 30);
		
		paper.text(shape.attr('x') + shape.attr('width') / 2, shape
				.attr('y') + 40, $('div', template.draggable).text());
		
		activity.items = paper.setFinish();
		activity.addDecoration();
		activity.addEffects();
	},
	
	
	removeActivity : function(activity) {
		if (activity.fromTransition) {
			activity.fromTransition.toActivity.toTransition = null;
			activity.fromTransition.remove();
		}
		if (activity.toTransition){
			activity.toTransition.fromActivity.fromTransition = null
			activity.toTransition.remove();
		}
		
		activities = activities.splice(activities.indexOf(activity), 1);
		activity.items.remove();
	},
	
	
	drawTransition : function(fromActivity, toActivity) {
		if (fromActivity.fromTransition) {
			fromActivity.fromTransition.remove();
		}
		if (toActivity.toTransition) {
			toActivity.toTransition.remove();
		}

		var startX = fromActivity.shape.attr('x') + fromActivity.shape.attr('width') / 2;
		var startY = fromActivity.shape.attr('y') + fromActivity.shape.attr('height') / 2;
		var endX =   toActivity.shape.attr('x') + toActivity.shape.attr('width') / 2;
		var endY =   toActivity.shape.attr('y') + toActivity.shape.attr('height') / 2;
		
		paper.setStart();
		paper.path('M ' + startX + ' ' + startY + ' L  ' + endX + ' ' + endY)
		                .attr({
		                	'stroke'       : 'rgb(119,126,157)',
		                	'stroke-width' : 2
		                });

		var angle = 90 + Math.atan2(endY - startY, endX - startX) * 180 / Math.PI;
		var arrowX = (startX + (endX - startX)/2);
		var arrowY = (startY + (endY - startY)/2);
		var arrowPath = Raphael.transformPath('M ' + arrowX + ' ' + arrowY + ' l 10 15 a 25 25 0 0 0 -20 0 z', 
				                        'R ' + angle + ' ' + arrowX + ' ' + arrowY);
		paper.path(arrowPath)
						 .attr({
							'stroke' : 'rgb(119,126,157)',
							'fill'   : 'rgb(119,126,157)'
						 });
		transition = paper.setFinish();
		
		transition.fromActivity = fromActivity;
		transition.toActivity = toActivity;
		fromActivity.fromTransition = transition;
		toActivity.toTransition = transition;
		transition.toBack();
		transition.mousedown(function(event, x, y){
			var mouseupHandler = function(){
				CanvasHandlers.dragTransitionEndHandler(transition);
			};
			CanvasHandlers.dragItemsStartHandler(transition, this, mouseupHandler, event, x, y)
		});
	},
	
	
	removeTransition : function(transition) {
		transition.fromActivity.fromTransition = null;
		transition.toActivity.toTransition = null;
		transition.remove();
	}
};


var CanvasHandlers = {
	dragItemsStartHandler : function(items, draggedElement, mouseupHandler, event, startX, startY) {
		items.toFront();
		items.attr('cursor', 'move');
		
		canvas.mousemove(function(event) {
			 CanvasHandlers.dragItemsMoveHandler(items, event, startX, startY);
		});
		
		items.mouseup(function(){
			canvas.off('mousemove');
			this.unmouseup();
			layout.items.bin.attr('fill', 'transparent');
			
			mouseupHandler();
		});
	},
	
	
	dragItemsMoveHandler : function(items, event, startX, startY) {
		var dx = event.pageX - startX;
		var dy = event.pageY - startY;
		items.transform('t' + dx + ' ' + dy);
		
		if (Raphael.isBBoxIntersect(layout.items.bin.getBBox(), items.getBBox())) {
			layout.items.bin.attr('fill', 'red');
		} else {
			layout.items.bin.attr('fill', 'transparent');
		}
	},
	
	
	dragActivityEndHandler : function(activity) {
		if (Raphael.isBBoxIntersect(layout.items.bin.getBBox(), activity.shape.getBBox())) {
			ActivityUtils.removeActivity(activity);
			return;
		}
		
		var transformation = activity.shape.attr('transform');
		if (transformation.length > 0) {
			activity.items.forEach(function(elem) {
				elem.attr({
					'x' : elem.attr('x') + transformation[0][1],
					'y' : elem.attr('y') + transformation[0][2]
				});
			});
		}
		activity.items.transform('');
		
		if (activity.fromTransition) {
			ActivityUtils.drawTransition(activity, activity.fromTransition.toActivity);
		}
		if (activity.toTransition) {
			ActivityUtils.drawTransition(activity.toTransition.fromActivity, activity);
		}
	},
	
	
	dragTransitionEndHandler : function(transition) {
		if (Raphael.isBBoxIntersect(layout.items.bin.getBBox(), transition.getBBox())) {
			ActivityUtils.removeTransition(transition);
		} else {
			transition.transform('');
			transition.toBack();
		}
	},
	
	
	drawTransitionStartHandler : function(activity, event, x, y) {
		if (activity.fromTransition) {
			alert('Transition from this activity already exists');
			return;
		}
		
		var startX = x - canvas.offset().left;
		var startY = y - canvas.offset().top;
		
		canvas.mousemove(function(event){
			CanvasHandlers.drawTransitionMoveHandler(activity, event, startX, startY);
		}).mouseup(function(event){
			CanvasHandlers.drawTransitionEndHandler(activity, event);
		});
	},
	
	
	drawTransitionMoveHandler : function(activity, event, startX, startY) {
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
		
		activity.tempTransition = paper.set();
		activity.tempTransition.push(paper.circle(startX, startY, 3));
		activity.tempTransition.push(paper.path('M ' + startX + ' ' + startY
				+ 'L ' + endX  + ' ' + endY).attr({
					'arrow-end' : 'open-wide-long',
					'stroke-dasharray' : '- '
				}));
	},
	
	
	drawTransitionEndHandler : function(activity, event) {
		canvas.off('mousemove');
		canvas.off('mouseup');
		
		if (activity.tempTransition) {
			activity.tempTransition.remove();
			activity.tempTransition = null;
		}
		
		var endX = event.pageX - canvas.offset().left;
		var endY = event.pageY - canvas.offset().top;
		var endActivity = null;
		$.each(activities, function(){
			if (this.shape.isPointInside(endX, endY)) {
				endActivity = this;
				return false;
			}
		});

		if (endActivity) {
			ActivityUtils.drawTransition(activity, endActivity);
		}
	}
};


function Activity(id) {
	this.id = id;
	this.items = paper.set();
	
	this.addEffects = function(){
		var activity = this;
		
		activity.items.mousedown(function(event, x, y){
			if (event.ctrlKey) {
				 CanvasHandlers.drawTransitionStartHandler(activity, event, x, y);
			} else {			
				var mouseupHandler = function(){
					CanvasHandlers.dragActivityEndHandler(activity);
				};
				CanvasHandlers.dragItemsStartHandler(activity.items, this, mouseupHandler, event, x, y);
			}
		});
	};
	
	this.addDecoration = function(){
		this.items.attr({
			'cursor' : 'pointer'
		});
	};
}