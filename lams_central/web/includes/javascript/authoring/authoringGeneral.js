/**
 * This file contains global methods for Authoring.
 */

// few publicly visible variables 
var paper = null,
	canvas = null;

// configuration and storage of various elements
var layout = {
	'activities' : null,
	'items' : {
		'bin'               : null,
		'selectedActivity'  : null,
		'propertiesDialog'  : null,
		'infoDialog'        : null,
		'copiedActivity'    : null,
		'groupNamingDialog' : null
	},
	'toolIcons': {
		'grouping' : '../images/grouping.gif',
		'gate'     : '../images/stop.gif'
	},
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
		'activity'     		  : '#A9C8FD',
		'offlineActivity' 	  : 'black',
		'activityText' 		  : 'black',
		'offlineActivityText' : 'white',
		'gate'         		  : 'red',
		'gateText'     		  : 'white',
		'transition'   		  : 'rgb(119,126,157)',
		'binActive'    		  : 'red',
		'selectEffect'        : 'blue',
		'branchingEdgeStart'  : 'green',
		'branchingEdgeEnd'    : 'red',
		'branchingEdgeMatch'  : 'blue'
	},
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
			    var toolID = draggable.draggable.attr('toolId'),
			    	x = draggable.offset.left  + canvas.scrollLeft() - canvas.offset().left,
			    	y = draggable.offset.top   + canvas.scrollTop()  - canvas.offset().top,
			    	label = $('div', draggable.draggable).text();
				
				layout.activities.push(new ActivityLib.ToolActivity(null, toolID, x, y, label));
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
		$('<div />')
			.appendTo('body')
			.dialog({
				'autoOpen'      : false,
				'closeOnEscape' : false,
				'position'      : {
					'my' : 'left top',
					'at' : 'left top',
					'of' :  '#canvas'
				},
				'resizable'     : true,
				'title'         : 'Properties'
			});
	// for proximity detection throttling (see handlers)
	propertiesDialog.lastRun = 0;
	// remove close button, add dimming
	propertiesDialog.container = propertiesDialog.closest('.ui-dialog');
	propertiesDialog.container.addClass('propertiesDialogContainer')
							  .css('opacity', layout.conf.propertiesDialogDimOpacity)
	 						  .mousemove(HandlerLib.approachPropertiesDialogHandler)
	                          .find('.ui-dialog-titlebar-close').remove();
	

	
	
	// initalise open Learning Design dialog
	var ldStoreDialog = $('#ldStoreDialog').dialog({
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
		            	'click'  : function() {
							$(this).dialog('close');
						}
		             }
		]
	});
	$('#ldScreenshotAuthor', ldStoreDialog).load(function(){
		// hide "loading" animation
		$('#ldStoreDialog .ldChoiceDependentCanvasElement').css('display', 'none');
		// show the thumbnail
		$(this).css('display', 'inline');
	});
	// there should be no focus, just highlight
	YAHOO.widget.TreeView.FOCUS_CLASS_NAME = null;
	var tree = new YAHOO.widget.TreeView('ldStoreDialogTree');
	// store the tree in the dialog's data
	ldStoreDialog.dialog('option', 'ldTree', tree);
	// make folder contents load dynamically on open
	tree.setDynamicLoad(function(node, callback){
		// load subfolder contents
		var childNodeData = MenuLib.getFolderContents(node.data.folderID);
		if (childNodeData) {
		$.each(childNodeData, function(){
				// create and add a leaf
				new YAHOO.widget.TextNode(this, node);
			});
		}
		
		// required by YUI
		callback();
	});
	tree.singleNodeHighlight = true;
	tree.subscribe('clickEvent', function(event){
		if (!event.node.data.learningDesignId){
			// it is a folder
			return false;
		}

		// display "loading" animation and finally LD thumbnail
		$('#ldStoreDialog .ldChoiceDependentCanvasElement').css('display', 'none');
		if (event.node.highlightState == 0) {
			$('#ldStoreDialog #ldScreenshotLoading').css('display', 'inline');
			// get the image of the chosen LD
			$('#ldStoreDialog #ldScreenshotAuthor').attr('src', LD_THUMBNAIL_URL_BASE + event.node.data.learningDesignId);
			$('#ldStoreDialog #ldScreenshotAuthor').css('width', 'auto').css('height', 'auto');
		} else {
			// toggleCanvasResize(CANVAS_RESIZE_OPTION_NONE);
		}
	});
	tree.subscribe('clickEvent',tree.onEventToggleHighlight);

	// initialise a small info dialog
	layout.items.infoDialog = $('<div />').attr('id', 'infoDialog').dialog({
		'autoOpen'   : false,
		'width'      : 290,
		'height'     : 35,
		'resizable'  : false,
		'show'       : 'fold',
		'hide'       : 'fold',
		'dialogClass': 'dialog-no-title',
		'position'   : {
						my: "right top",
					    at: "right top+5px",
					    of: '#canvas'
				      }
	});
	
	
	// initialise dialog from group nameing
	layout.items.groupNamingDialog = $('<div />').dialog({
		'autoOpen' : false,
		'modal'  : true,
		'show'   : 'fold',
		'hide'   : 'fold',
		'position' : {
			'of' :  '#canvas'
		},
		'title'  : 'Group Naming',
		'buttons' : [
		             {
		            	'text'   : 'OK',
		            	'click'  : function() {
		            		var dialog = $(this),
		            			activity = dialog.dialog('option', 'activity');
		            		
		            		// extract group names from text fields
		            		activity.groupNames = [];
		            		$('input', dialog).each(function(){
		            			// do not take into account empty group names
		            			var groupName = $(this).val().trim();
		            			if (groupName) {
		            				activity.groupNames.push(groupName);
		            			}
		            		});
		            		
		            		dialog.dialog('close');
						}
		             },
		             {
		            	'text'   : 'Cancel',
		            	'click'  : function() {
							$(this).dialog('close');
						}
		             }
		]
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
			
			// remove existing activities
			MenuLib.newLearningDesign(true);
			
			var resizeNeeded = false,
				arrangeNeeded = false,
				// current paper dimensions 
				paperWidth = paper.width,
				paperHeight = paper.height,
				// helper for finding last activity in a branch
				branchToActivities = {};
			
			// create visual representation of the loaded activities
			$.each(ld.activities, function() {
				var activityData = this,
					activity = null;

				switch(activityData.activityTypeID) {
					// Tool Activity
					case 1 :
						activity = new ActivityLib.ToolActivity(activityData.activityID,
										activityData.toolID,
										activityData.xCoord,
										activityData.yCoord,
										activityData.activityTitle);
						break;
					
					// Grouping Activity
					case 2 :
						// find extra metadata for this grouping
						$.each(ld.groupings, function(){
							var groupingData = this;
							if (groupingData.groupingID == activityData.createGroupingID) {
								var groupingType = null,
									groupNames = [];
								
								// translate backend grouping type to human readable for better understanding
								switch(groupingData.groupingTypeID) {
									case 2:
										groupingType = 'monitor';
										break;
									case 4:
										groupingType = 'learner';
										break;
									default: 
										groupingType = 'random';
										break;
								};
								// get groups names
								$.each(groupingData.groups, function(){
									groupNames.push(this.groupName);
								});
								
								activity = new ActivityLib.GroupingActivity(activityData.activityID,
										activityData.xCoord,
										activityData.yCoord,
										activityData.activityTitle,
										groupingData.groupingID,
										groupingType,
										groupingData.learnersPerGroup ? 'learners' : 'groups',
										groupingData.numberOfGroups,
										groupingData.learnersPerGroup,
										groupingData.equalNumberOfLearnersPerGroup,
										groupingData.viewStudentsBeforeSelection,
										groupNames);
								return false;
							}
						})
						break;
					
					// Gate Activity
					case 3:				
						activity = new ActivityLib.GateActivity(activityData.activityID,
							activityData.xCoord,
							activityData.yCoord,
							'sync');
						break;
					
					// Gate Activity
					case 4:
						activity = new ActivityLib.GateActivity(activityData.activityID,
								activityData.xCoord,
								activityData.yCoord,
								'schedule');
						break;
						
					// Gate Activity	
					case 5:
						activity = new ActivityLib.GateActivity(activityData.activityID,
							activityData.xCoord,
							activityData.yCoord,
							'permission');
						break;
						
					// Gate Activity
					case 14:
						activity = new ActivityLib.GateActivity(activityData.activityID,
							activityData.xCoord,
							activityData.yCoord,
							'condition');
						break;
					
					// Branching Activity
					case 10:
					case 11:
					case 12:
						// draw both edge points straight away and mark the whole canvas for auto reaarange
						arrangeNeeded = true;
						var branchingEdge = new ActivityLib.BranchingEdgeActivity(activityData.activityID, 0, 0, null);
						layout.activities.push(branchingEdge);
						branchingEdge = new ActivityLib.BranchingEdgeActivity(
								null, 0, 0, branchingEdge.branchingActivity);
						layout.activities.push(branchingEdge);
						break;
					
					// Sequence Activity, i.e. a branch
					case 8: 
						$.each(layout.activities, function(){
							if (this.type == 'branchingEdge'
								&& activityData.parentActivityID == this.branchingActivity.id) {
								// create a branch inside the branching activity
								this.branchingActivity.branches.push(
										new ActivityLib.BranchActivity(activityData.activityID,
																	   activityData.activityTitle,
																	   this.branchingActivity));
								return false;
							}
						});
						break;
				}
				
				
				if (!activity) {
					// activity type not supported yet
					return true;
				}
				layout.activities.push(activity);
				// for later reference
				activityData.activity = activity;
				
				
				// store information about the branch the activity belongs to
				if (activityData.parentActivityID) {
					var branchData = branchToActivities[activityData.parentActivityID];
					if (!branchData) {
						branchData = branchToActivities[activityData.parentActivityID] = {
							'lastActivityOrderID' : activityData.orderID,
							'lastActivity'        : activity
						};
					}
					
					if (activityData.orderID > branchData.lastActivityOrderID) {
						// is it the last activity in the branch?
						branchData.lastActivityOrderID = activityData.orderID;
						branchData.lastActivity = activity;
					}
					if (activityData.orderID == 1) {
						// is it the first activity in the branch
						branchData.firstActivity = activity;
					}
				}
				
				// if we do arranging afterwards, paper will be resized anyway
				if (!arrangeNeeded) {
					// find new dimensions of paper
					var activityBox = activity.items.shape.getBBox(),
						maxX = activityBox.x + activityBox.width,
						maxY = activityBox.y + activityBox.height;
					if (maxX > paperWidth) {
						resizeNeeded = true;
						paperWidth = maxX;
					}
					if (maxY > paperHeight) {
						resizeNeeded = true;
						paperHeight = maxY;
					}
				}
			});
			
			// apply existing groupings to activities 
			$.each(ld.activities, function(){
				if (this.applyGrouping && this.activity) {
					var groupedActivity = this.activity,
						groupingID = this.groupingID;
					$.each(layout.activities, function(){
						if (this.type == 'grouping' && groupingID == this.groupingID) {
							// add reference and redraw the grouped activity
							groupedActivity.grouping = this;
							groupedActivity.draw();
							return false;
						}
					});
				}
			});
			
			
			// draw starting and ending transitions in branches
			$.each(layout.activities, function(){
				if (this.type == 'branchingEdge' && this.isStart) {
					var branchingActivity = this.branchingActivity;
					$.each(branchingActivity.branches, function(){
						var branch = this,
							branchData = branchToActivities[branch.id];
						
						// add reference to the transition inside branch
						branch.transitionFrom = ActivityLib.drawTransition(branchingActivity.start,
								branchData.firstActivity, true);
						ActivityLib.drawTransition(branchData.lastActivity, branchingActivity.end, true);	
					});
				}
			});
			
			// draw transitions from last activities in branches to end edge point

			
			// draw plain transitions
			$.each(ld.transitions, function(){
				var transition = this,
					fromActivity = null,
					toActivity = null;
				
				// find which activities the transition belongs to
				$.each(layout.activities, function(){
					var activity = this,
						isBranching = activity.type == 'branchingEdge';
					
					// check if transition IDs match either a plain activity or a complex one
					if (isBranching ? !activity.isStart && transition.fromActivityID == activity.branchingActivity.id 
									: transition.fromActivityID == activity.id) {
						fromActivity = activity;
					} else if (isBranching ? activity.isStart && transition.toActivityID == activity.branchingActivity.id 
										  : transition.toActivityID == activity.id) {
						toActivity = activity;
					}
					
					// found both transition ends, draw it and stop the iteration
					if (fromActivity && toActivity) {
						ActivityLib.drawTransition(fromActivity, toActivity, true);
						return false;
					}
				});
			});
			
			
			if (arrangeNeeded) {
				MenuLib.arrangeActivities();
			} else if (resizeNeeded) {
				ActivityLib.resizePaper(paperWidth, paperHeight);
			} else {	
				HandlerLib.resetCanvasMode();
			}
		}
	});
}


/**
 * Sets new paper dimensions and moves some static elements.
 */
function resizePaper(width, height) {
	if (!paper) {
		return;
	}

	if (!width) {
		width = canvas.width() - 5;
	}
	if (!height || (height == canvas.height() - 5)) {
		// take into account the horizontal scrollbar
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