var paper = null;
var canvas = null;
var maxActivityId = 0;
var activities = [];

var layout = {
	'activityWidth' : 125,
	'activityHeight' : 50
}

$(document).ready(function() {
	paper = Raphael('canvas');
	canvas = $('#canvas');
	
	initTemplates();
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
			   ActivityUtils.constructActivityFromTemplate(draggable);
		   }
	});
}


var ActivityUtils = {
	constructActivityFromTemplate : function(template) {
		$(template.helper).remove();
		
		var activityId = ++maxActivityId;
		var activity = new Activity(activityId);
		activities.push(activity);
		
		var canvasOffset = canvas.offset();
		var shape = activity.shape = paper.rect(
				template.offset.left - canvasOffset.left,
				template.offset.top - canvasOffset.top,
				125, 50)
		.attr({
			'fill' : '#A9C8FD'
		});
		activity.items.push(shape);
		
		var icon = paper.image($('img', template.draggable).attr('src'), shape.attr('x')
				+ shape.attr('width') / 2 - 15, shape.attr('y') + 2, 30, 30);
		activity.items.push(icon);
		
		var label = paper.text(shape.attr('x') + shape.attr('width') / 2, shape
				.attr('y') + 40, $('div', template.draggable).text());
		activity.items.push(label);
		
		activity.addDecoration();
		activity.addEffects();
	},
	
	drawTransition : function(fromActivity, toActivity) {
		if (fromActivity.fromTransition) {
			fromActivity.fromTransition.remove();
		}
		if (toActivity.toTransition) {
			toActivity.toTransition.remove();
		}
		
		var transition = paper.set();
		transition.fromActivity = fromActivity;
		transition.toActivity = toActivity;
		fromActivity.fromTransition = transition;
		toActivity.toTransition = transition;
		
		var path = paper.path('M ' + 
				(fromActivity.shape.attr('x') + fromActivity.shape.attr('width') / 2) + ' ' +
				(fromActivity.shape.attr('y') + fromActivity.shape.attr('height') / 2) + ' L  ' +
				(toActivity.shape.attr('x') + toActivity.shape.attr('width') / 2) + ' ' +
				(toActivity.shape.attr('y') + toActivity.shape.attr('height') / 2));
		transition.push(path);
		transition.toBack();
	}
};


function Activity(id) {
	this.id = id;
	this.items = paper.set();
	
	this.addEffects = function(){
		var activity = this;
		
		activity.items.mousedown(function(event, x, y){
			if (event.ctrlKey) {
				var startX = x - canvas.offset().left;
				var startY = y - canvas.offset().top;
				
				canvas.mousemove(function(event){
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
				}).mouseup(function(event){
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
				});
			}
			
			else {
				activity.items.toFront();
				activity.items.attr('cursor', 'move');
				
				canvas.mousemove(function(event) {
					var dx = event.pageX - x;
					var dy = event.pageY - y;
					activity.items.transform('t' + dx + ' ' + dy);
				});
				
				activity.mouseupHandler = function(){
					canvas.off('mousemove');
					activity.items.unmouseup(activity.mouseupHandler);
					activity.mouseupHandler = null;
					
					var transformation = activity.shape.attr('transform');
					activity.items.forEach(function(elem) {
						elem.attr({
							'x' : elem.attr('x') + transformation[0][1],
							'y' : elem.attr('y') + transformation[0][2]
						});
					});
					activity.items.transform('');
					
					if (activity.fromTransition) {
						ActivityUtils.drawTransition(activity, activity.fromTransition.toActivity);
					}
					if (activity.toTransition) {
						ActivityUtils.drawTransition(activity.toTransition.fromActivity, activity);
					}
				};
				
				activity.items.mouseup(activity.mouseupHandler);
			}
		});
	};
	
	this.addDecoration = function(){
		this.items.attr({
			'cursor' : 'pointer'
		});
	};
}