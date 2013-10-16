var paper = null;
var canvas = null;
var bin = null;
var maxActivityId = 0;
var activities = [];

var layout = {
	'conf' : {
		'activityWidth'  : 125,
		'activityHeight' : 50,
		'propertiesDialogDimOpacity'   : 0.3,
		'propertiesDialogDimThreshold' : 100,
		'propertiesDialogDimThrottle'  : 100
	},
	'defs' : {
		'bin'        : 'M 830 700 h -50 l 10 50 h 30 Z',
		'transArrow' : ' l 10 15 a 25 25 0 0 0 -20 0 z'
	},
	'colors' : {
		'activity'     : '#A9C8FD',
		'transition'   : 'rgb(119,126,157)',
		'binActive'    : 'red',
		'selectEffect' : 'blue'
	},
	'items' : {
		'bin'              : null,
		'selectedActivity' : null,
		'propertiesDialogDimLastRun' : 0
	}
};

$(document).ready(function() {
	paper = Raphael('canvas');
	canvas = $('#canvas');
	
	initLayout();
	initTemplates();
});


function initTemplates(){
	$('.template').each(function(){
		$(this).draggable({
			'containment' : '#authoringTable',
		    'revert'      : 'invalid',
		    'distance'    : 20,
		    'scroll'      : false,
		    'scope'       : 'template',
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
		   'scope'       : 'template',
		   'drop'	     : function (event, draggable) {
			   ActivityUtils.addActivity(draggable);
		   }
	});
}


function initLayout() {
	layout.items.bin = paper.path(layout.defs.bin);
	
	$('.ui-button').button();
	$(".split-ui-button").each(function(){
		var buttonContainer = $(this);
		var buttons = buttonContainer.children();
		
		buttons.first().button()
			   .next().button({
			text : false,
			icons : {
				primary : "ui-icon-triangle-1-s"
			}
		});
		buttonContainer.buttonset().next().hide().menu();
		
		buttons.each(function(){
			var button = $(this);
			if (!button.attr('onclick')) {
				button.click(function() {
					var menu = $(this).parent().next().show().position({
						my : "right top",
						at : "right bottom",
						of : $(this).parent()
					});
					$(document).one("click", function() {
						menu.hide();
					});
					return false;
				});
			}
		});
	});
	
	
	$('#groupingButton').click(function(){
		canvas.css('cursor', 'url("images/grouping.gif"), move').click(function(event){
			var x = event.pageX - canvas.offset().left;
			var y = event.pageY - canvas.offset().top;
			
			ActivityUtils.addGrouping(x, y);
			CanvasHandlers.resetCanvasMode();
		});
	});
	
	CanvasHandlers.resetCanvasMode();
}


var ActivityUtils = {
		
	addActivity : function(template) {
		$(template.helper).remove();
		
		var activityId = ++maxActivityId;
		var activity = new Activity(activityId, 'tool');
		activities.push(activity);
		
		var canvasOffset = canvas.offset();
		
		paper.setStart();
		var shape = paper.rect(
				template.offset.left - canvasOffset.left,
				template.offset.top - canvasOffset.top,
				layout.conf.activityWidth, layout.conf.activityHeight)
		.attr({
			'fill' : layout.colors.activity
		});
		
		paper.image($('img', template.draggable).attr('src'), shape.attr('x')
				+ shape.attr('width') / 2 - 15, shape.attr('y') + 2, 30, 30);
		
		paper.text(shape.attr('x') + shape.attr('width') / 2, shape
				.attr('y') + 40, $('div', template.draggable).text());
		
		activity.items = paper.setFinish();
		activity.items.shape = shape;
		activity.addDecoration();
		activity.addEffects();
	},
	
	
	addGrouping : function(x, y) {
		var activityId = ++maxActivityId;
		var activity = new Activity(activityId, 'group');
		activities.push(activity);
		

		paper.setStart();
		var shape = paper.rect(
				x - layout.conf.activityWidth/2,
				y - layout.conf.activityHeight/2,
				layout.conf.activityWidth, layout.conf.activityHeight)
		.attr({
			'fill' : layout.colors.activity
		});
		
		paper.image('images/grouping.gif',
				    shape.attr('x') + shape.attr('width') / 2 - 15,
				    shape.attr('y') + 2,
				    30, 30);
		
		paper.text(shape.attr('x') + shape.attr('width') / 2, shape
				.attr('y') + 40, 'Grouping');
		
		activity.items = paper.setFinish();
		activity.items.shape = shape;
		activity.addDecoration();
		activity.addEffects();
	},
	
	
	removeActivity : function(activity) {
		if (activity.fromTransition) {
			var toActivity = activity.fromTransition.toActivity;
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

		var startX = fromActivity.items.shape.attr('x') + fromActivity.items.shape.attr('width') / 2;
		var startY = fromActivity.items.shape.attr('y') + fromActivity.items.shape.attr('height') / 2;
		var endX =   toActivity.items.shape.attr('x') + toActivity.items.shape.attr('width') / 2;
		var endY =   toActivity.items.shape.attr('y') + toActivity.items.shape.attr('height') / 2;
		
		paper.setStart();
		paper.path('M ' + startX + ' ' + startY + ' L  ' + endX + ' ' + endY)
		                .attr({
		                	'stroke'       : layout.colors.transition,
		                	'stroke-width' : 2
		                });

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
		
		if (fromActivity.type == 'group') {
			ActivityUtils.addGroupingEffect(toActivity);
		}
	},
	
	
	removeTransition : function(transition) {
		if (transition.toActivity.items.groupingEffect) {
			transition.toActivity.items.groupingEffect.remove();
			transition.toActivity.items.groupingEffect = null;
		}
		transition.fromActivity.fromTransition = null;
		transition.toActivity.toTransition = null;
		transition.remove();
	},
	
	
	addSelectEffect : function (activity) {
		if (!activity.items.selectEffect) {
			var shape = activity.items.shape;
			
			activity.items.selectEffect = paper.rect(
					shape.attr('x') - 7,
					shape.attr('y') - 7,
					layout.conf.activityWidth + 14,
					layout.conf.activityHeight + 14)
				.attr({
					'stroke'           : layout.colors.selectEffect,
					'stroke-dasharray' : '-'
				});
			activity.items.push(activity.items.selectEffect);
			
			layout.items.selectedActivity = activity;
			
			var propertiesDialog = activity.propertiesDialog;
			if (!activity.propertiesDialog) {
				propertiesDialog = ActivityUtils.addPropertiesDialog(activity);
			}
			
			propertiesDialog.dialog('open');
		}
	},
	
	
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
	},
	
	
	addPropertiesDialog : function(activity) {
		var propertiesDialog = activity.propertiesDialog =
			$('<div>Properties go here</div>')
				.appendTo('body')
				.dialog({
					'closeOnEscape' : false,
					'position'      : {
						'my' : 'left top',
						'at' : 'left top',
						'of' :  '#canvas'
					},
					'resizable'     : false,
					'title'         : 'Properties'
				});
		propertiesDialog.container = propertiesDialog.closest('.ui-dialog');
		propertiesDialog.container.addClass('propertiesDialog')
								  .css('opacity', layout.conf.propertiesDialogDimOpacity)
		 						  .mousemove(CanvasHandlers.approachPropertiesDialogHandler)
		                          .find('.ui-dialog-titlebar-close').remove();
		
		return propertiesDialog;
	}
};


var CanvasHandlers = {
	resetCanvasMode : function(){
		canvas.css('cursor', 'default')
		      .off('click')
		      .click(CanvasHandlers.unselectActivityHandler)
		      .off('mousemove')
		      .mousemove(CanvasHandlers.approachPropertiesDialogHandler);
	},
	
	
	approachPropertiesDialogHandler : function(event) {
		var thisRun = new Date().getTime();
		if (thisRun - layout.items.propertiesDialogDimLastRun < layout.conf.propertiesDialogDimThrottle){
			return;
		}
		layout.items.propertiesDialogDimLastRun = thisRun;
		
		var activity = layout.items.selectedActivity;
		if (activity && activity.propertiesDialog) {
			var dialog = $(activity.propertiesDialog.container),
			    dialogPosition = dialog.offset(),
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
	},
	
	
	selectActivityHandler : function(event, activity) {
		if (activity != layout.items.selectedActivity) {
			CanvasHandlers.unselectActivityHandler(event);
			ActivityUtils.addSelectEffect(activity);
		}
		event.preventDefault();
	},
	
	
	unselectActivityHandler : function(event) {
		var defaultPrevented = event.originalEvent ?
				event.originalEvent.defaultPrevented : event.defaultPrevented;
		if (!defaultPrevented) {
			var selectedActivity = layout.items.selectedActivity;
			if (selectedActivity) {
				selectedActivity.items.selectEffect.remove();
				selectedActivity.items.selectEffect = null;
				
				if (selectedActivity.propertiesDialog) {
					selectedActivity.propertiesDialog.dialog('close');
				}
				layout.items.selectedActivity = null;
			}
		}
	},
	
	
	dragItemsStartHandler : function(items, draggedElement, mouseupHandler, event, startX, startY) {
		items.toFront();
		items.attr('cursor', 'move');
		
		canvas.mousemove(function(event) {
			 CanvasHandlers.dragItemsMoveHandler(items, event, startX, startY);
		});
		
		items.mouseup(function(){
			CanvasHandlers.resetCanvasMode();
			this.unmouseup();
			layout.items.bin.attr('fill', 'transparent');
			
			mouseupHandler();
		});
	},
	
	
	dragItemsMoveHandler : function(items, event, startX, startY) {
		var dx = event.pageX - startX;
		var dy = event.pageY - startY;
		items.transform('t' + dx + ' ' + dy);
		
		if (items.groupingEffect) {
			items.groupingEffect.toBack();
		}
		
		if (Raphael.isBBoxIntersect(layout.items.bin.getBBox(), items.getBBox())) {
			layout.items.bin.attr('fill', layout.colors.binActive);
		} else {
			layout.items.bin.attr('fill', 'transparent');
		}
	},
	
	
	dragActivityEndHandler : function(activity) {
		if (Raphael.isBBoxIntersect(layout.items.bin.getBBox(), activity.items.shape.getBBox())) {
			ActivityUtils.removeActivity(activity);
			return;
		}
		
		var transformation = activity.items.shape.attr('transform');
		if (transformation.length > 0) {
			activity.items.forEach(function(elem) {
				elem.attr({
					'x' : elem.attr('x') + transformation[0][1],
					'y' : elem.attr('y') + transformation[0][2]
				});
			});
		}
		activity.items.transform('');
		
		if (activity.items.groupingEffect) {
			activity.items.groupingEffect.toBack();
		}
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
			if (this.items.shape.isPointInside(endX, endY)) {
				endActivity = this;
				return false;
			}
		});

		if (endActivity) {
			ActivityUtils.drawTransition(activity, endActivity);
		}
	}
};


function Activity(id, type) {
	this.id = id;
	this.type = type;
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
		})
		.click(function(event){
			CanvasHandlers.selectActivityHandler(event, activity);
		});
	};
	
	this.addDecoration = function(){
		this.items.attr({
			'cursor' : 'pointer'
		});
	};
}