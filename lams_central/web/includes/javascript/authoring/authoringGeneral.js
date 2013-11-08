/**
 * This file contains global methods for Authoring.
 */

// few publicly visible variables 
var paper = null,
	canvas = null,
	activities = null;

// configuration and storage of various elements
var layout = {
	'toolIcons': {},
	'conf' : {
		'propertiesDialogDimOpacity'   : 0.3,
		'propertiesDialogDimThreshold' : 100,
		'propertiesDialogDimThrottle'  : 100,
		'dragStartThreshold'           : 300,
		'arrangeHorizontalSpace'       : 200,
		'arrangeVerticalSpace'         : 100,
		'arrangeHorizontalPadding'     : 40,
		'arrangeVerticalPadding'       : 50
	},
	'defs' : {
		'activity'      : ' h 125 v 50 h -125 z',
		'bin'           : 'M 0 0 h -50 l 10 50 h 30 z',
		'transArrow'    : ' l 10 15 a 25 25 0 0 0 -20 0 z',
		'gate'          : ' l-8 8 v14 l8 8 h14 l8 -8 v-14 l-8 -8 z',
		'branchingEdgeStart' : ' m -8 0 a 8 8 0 1 0 16 0 a 8 8 0 1 0 -16 0',
		'branchingEdgeEnd'   : ' m -8 0 a 8 8 0 1 0 16 0 a 8 8 0 1 0 -16 0'
	},
	'colors' : {
		'activity'     : '#A9C8FD',
		'gate'         : 'red',
		'gateText'     : 'white',
		'transition'   : 'rgb(119,126,157)',
		'binActive'    : 'red',
		'selectEffect' : 'blue',
		'branchingEdgeStart' : 'green',
		'branchingEdgeEnd'   : 'red',
		'branchingEdgeMatch' : 'blue'
	},
	'items' : {
		'bin'              : null,
		'selectedActivity' : null,
		'propertiesDialog' : null
	}
};


/**
 * Initialises the whole Authoring window.
 */
$(document).ready(function() {
	canvas = $('#canvas');
	MenuLib.newLearningDesign(true);
	
	initLayout();
	initTemplates();
});


/**
 * Draw boxes with Tool Activity templates.
 */
function initTemplates(){
	$('.template').each(function(){
		var toolId = $(this).attr('toolId');
		// register tool icons so they are later easily accessible
		layout.toolIcons[toolId] = $('img', this).attr('src');

		// if a tool's name is too long and gets broken into two lines
		// make some adjustments to layout
		var toolName = $('div', this);
		if (toolName.text().length > 15){
			toolName.css('padding-top', '4px');
		}
		
		// allow dragging a template to canvas
		$(this).draggable({
			'containment' : '#authoringTable',
		    'revert'      : 'invalid',
		    'distance'    : 20,
		    'scroll'      : false,
		    'scope'       : 'template',
		    'helper'      : function(event){
		    	// build a simple helper
				var helper = $(this).clone().css({
					'width'   : '135px',
					'border'  : 'thin black solid',
					'z-index' : 1,
					'cursor'  : 'move'
				});
				
				// Chrome does not render name of the tool correctly in the helper
				// so just remove it
				helper.children('div').remove();
				return helper;
			}
		});
	});
	
	// allow dropping templates to canvas
	canvas.droppable({
		   'tolerance'   : 'touch',
		   'scope'       : 'template',
		   'drop'	     : function (event, draggable) {
			    // get rid of helper div
				$(draggable.helper).remove();
				
				// calculate the position and create an instance of the tool activity
			    var toolID = draggable.draggable.attr('toolId');
				var canvasOffset = canvas.offset();
				var x = draggable.offset.left - canvasOffset.left;
				var y = draggable.offset.top - canvasOffset.top;
				var label = $('div', draggable.draggable).text();
				
				activities.push(new ActivityLib.ToolActivity(null, toolID, x, y, label));
		   }
	});
}


/**
 * Initialises various Authoring widgets.
 */
function initLayout() {
	// add jQuery UI button functionality
	$('.ui-button').button();
	$(".split-ui-button").each(function(){
		// drop down buttons
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
						my : "left top+2px",
						at : "left bottom",
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
	
	// initialise the properties dialog singleton
	var propertiesDialog = layout.items.propertiesDialog =
		$('<div>Properties go here</div>')
			.appendTo('body')
			.dialog({
				'autoOpen'      : false,
				'closeOnEscape' : false,
				'position'      : {
					'my' : 'left top',
					'at' : 'left top',
					'of' :  '#canvas'
				},
				'resizable'     : false,
				'title'         : 'Properties'
			});
	// for proximity detection throttling (see handlers)
	propertiesDialog.lastRun = 0;
	// remove close button, add dimming
	propertiesDialog.container = propertiesDialog.closest('.ui-dialog');
	propertiesDialog.container.addClass('propertiesDialog')
							  .css('opacity', layout.conf.propertiesDialogDimOpacity)
	 						  .mousemove(HandlerLib.approachPropertiesDialogHandler)
	                          .find('.ui-dialog-titlebar-close').remove();
	
	// initalise open Learning Design dialog
	var openLDDialog = $('#openLearningDesignDialog').dialog({
		'autoOpen'      : false,
		'position'      : {
			'my' : 'left top',
			'at' : 'left+5px top+5px',
			'of' :  'body'
		},
		'resizable'     : false,
		'width'			: 990,
		'height'		: 780,
		'dialogClass': 'dialog-no-title',
		'buttons' : [
		             {
		            	'text'   : 'Open',
		            	'id'     : 'openLDButton',
		            	'click'  : function() {
		            		var dialog = $(this);
		            		var tree = dialog.dialog('option', 'ldTree');
		            		var ldNode = tree.getHighlightedNode();
		            		// no LD was chosen
		            		if (!ldNode || !ldNode.data.learningDesignId) {
		            			alert("Please choose a sequence");
		            			return;
		            		}
		            		
		            		dialog.dialog('close');
		            		openLearningDesign(ldNode.data.learningDesignId);
						}
		             },
		             {
		            	'text'   : 'Cancel',
		            	'id'     : 'cancelLDButton',
		            	'click'  : function() {
							$(this).dialog('close');
						}
		             }
		]
	});

	$('#ldScreenshotAuthor', openLDDialog).load(function(){
		// hide "loading" animation
		$('.ldChoiceDependentCanvasElement').css('display', 'none');
		// show the thumbnail
		$(this).css('display', 'inline');
	});
	// there should be no focus, just highlight
	YAHOO.widget.TreeView.FOCUS_CLASS_NAME = null;
	
	
	$('#infoDialog').dialog({
		'autoOpen'   : false,
		'height'     : 35,
		'width'      : 290,
		'modal'      : false,
		'resizable'  : false,
		'show'       : 'fold',
		'hide'       : 'fold',
		'dialogClass': 'dialog-no-title',
		'position'   : {my: "right top",
					   at: "right top+5px",
					   of: '#canvas'
				      }
	});
}


/**
 * Replace current canvas contents with the loaded sequence.
 */
function openLearningDesign(learningDesignId) {
	// get LD details
	$.ajax({
		cache : false,
		url : LAMS_URL + "authoring/author.do",
		dataType : 'json',
		data : {
			'method'          : 'getLearningDesignJSON',
			'learningDesignID': learningDesignId
		},
		success : function(ld) {
			if (!ld) {
				alert('Error while loading sequence');
				return;
			}
			
			MenuLib.newLearningDesign(true);
			// create visual representation of the loaded activities
			$.each(ld.activities, function() {
				var activity = this;
				activities.push(new ActivityLib.ToolActivity(activity.activityID,
						activity.toolID, activity.xCoord, activity.yCoord,
						activity.activityTitle));
			});
			
			// draw transitions
			$.each(ld.transitions, function(){
				var transition = this;
				var fromActivity = null
				var toActivity = null;
				
				// find which activities the transition belongs to
				$.each(activities, function(){
					var activity = this;
					if (activity.id == transition.fromActivityID) {
						fromActivity = activity;
					} else if (activity.id == transition.toActivityID) {
						toActivity = activity;
					}
					
					// found both transition ends, draw it and stop the iteration
					if (fromActivity && toActivity) {
						ActivityLib.drawTransition(fromActivity, toActivity);
						return false;
					}
				});
			});
			
			HandlerLib.resetCanvasMode();
		}
	});
}


function resizePaper(width, height) {
	if (!paper) {
		return;
	}

	if (!width) {
		width = canvas.width() - 5;
	}
	if (!height || (height == canvas.height() - 5)) {
		height = canvas.height() - 5 - (width > canvas.width() - 5 ? 20 : 0);
	}
	paper.setSize(width, height);
	
	if (layout.items.bin) {
		layout.items.bin.remove();
	}
	
	// draw rubbish bin on canvas
	var binPath = Raphael.parsePathString(layout.defs.bin);
	binPath = Raphael.transformPath(binPath, Raphael.format('t {0} {1}', width, height - 50));
	layout.items.bin = paper.path(binPath);
	
	HandlerLib.resetCanvasMode();
}