/**
 * This file contains global methods for Authoring.
 */

// few publicly visible variables 
var paper = null,
	canvas = null;

// configuration and storage of various elements
var layout = {
	'isZoomed'   : false,
	'activities' : null,
	'items' : {
		'bin'               : null,
		
		'selectedActivity'  : null,
		'copiedActivity'    : null,
		
		'propertiesDialog'  : null,
		'infoDialog'        : null,
		'groupNamingDialog' : null,
		'groupsToBranchesMappingDialog' : null
	},
	
	'toolMetadata': {
		'grouping' : {
			'iconPath' : '../images/grouping.gif'
		},
		'gate'     : {		
			'iconPath' : '../images/stop.gif'
		}
	},
	'conf' : {
		'propertiesDialogDimOpacity'   : 0.3,
		'propertiesDialogDimThreshold' : 100,
		'propertiesDialogDimThrottle'  : 100,
		'dragStartThreshold'           : 300,
		'arrangeHorizontalSpace'       : 200,
		'arrangeVerticalSpace'         : 100,
		'arrangeHorizontalPadding'     : 35,
		'arrangeVerticalPadding'       : 50
	},
	'defs' : {
		'activity'      : ' h 125 v 50 h -125 z',
		'branchingEdgeStart' : ' a 8 8 0 1 0 16 0 a 8 8 0 1 0 -16 0',
		'branchingEdgeEnd'   : ' a 8 8 0 1 0 16 0 a 8 8 0 1 0 -16 0',
		'transArrow'    : ' l 10 15 a 25 25 0 0 0 -20 0 z',
		'gate'          : ' l-8 8 v14 l8 8 h14 l8 -8 v-14 l-8 -8 z',
		'bin'           : 'M 0 0 h -50 l 10 50 h 30 z'
	},
	'colors' : {
		'activity'     		  : '#A9C8FD',
		'offlineActivity' 	  : 'black',
		'activityText' 		  : 'black',
		'offlineActivityText' : 'white',
		'gate'         		  : 'red',
		'gateText'     		  : 'white',
		'branchingEdgeStart'  : 'green',
		'branchingEdgeEnd'    : 'red',
		'branchingEdgeMatch'  : 'blue',
		'transition'   		  : 'rgb(119,126,157)',
		'binActive'    		  : 'red',
		'selectEffect'        : 'blue'
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
	PropertyLib.init();
	MenuLib.init();
});


/**
 * Draw boxes with Tool Activity templates.
 */
function initTemplates(){
	$('.template').each(function(){
		var toolId = $(this).attr('toolId');
		// register tool properties so they are later easily accessible
		layout.toolMetadata[toolId] = {
			'iconPath' : $('img', this).attr('src'),
			'supportsOutputs' : $(this).attr('supportsOutputs')
		};
		
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
	$('#tabs').tabs();
	$('#tabs .ui-tabs-nav, #tabs .ui-tabs-nav > *')
		.removeClass( "ui-corner-all ui-corner-top" )
		.addClass( "ui-corner-bottom" );
	$('#tabs .ui-tabs-nav').appendTo('#tabs');
	
	
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
			
			$('#ldDescriptionFieldTitle').text(ld.title);
			$('#ldDescriptionFieldDescription').text(ld.description);
			
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
										activityData.activityTitle,
										activityData.supportsOutputs);
						// for later reference
						activityData.activity = activity;
						break;
					
					// Grouping Activity
					case 2 :
						// find extra metadata for this grouping
						$.each(ld.groupings, function(){
							var groupingData = this;
							if (groupingData.groupingID == activityData.createGroupingID) {
								var groupingType = null,
									groups = [];
								
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
									groups.push({
										'name' : this.groupName,
										'id'   : this.groupID,
										'uiid' : this.groupUIID
										});
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
										groups);
								return false;
							}
						})
						break;
					
					// Gate Activity
					case 3: var gateType = 'sync';
					case 4: var gateType = gateType || 'schedule';
					case 5: var gateType = gateType || 'permision';
					case 6:
						var gateType = gateType || 'condition';
						activity = new ActivityLib.GateActivity(activityData.activityID,
							activityData.xCoord,
							activityData.yCoord,
							gateType);
						break;

					// Branching Activity
					case 10: var branchingType = 'chosen';
					case 11: var branchingType = branchingType || 'group';
					case 12:
						// draw both edge points straight away and mark the whole canvas for auto reaarange
						arrangeNeeded = true;
						var branchingType = branchingType || 'tool',
							branchingEdge = new ActivityLib.BranchingEdgeActivity(activityData.activityID, 0, 0, 
									activityData.activityTitle, branchingType, null);
						layout.activities.push(branchingEdge);
						// for later reference
						activityData.activity = branchingEdge;
						
						branchingEdge = new ActivityLib.BranchingEdgeActivity(
								null, 0, 0, null, null, branchingEdge.branchingActivity);
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
																	   activityData.activityUIID,
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
							if (groupedActivity.type == 'branchingEdge') {
								groupedActivity.branchingActivity.grouping = this;
							} else {
								groupedActivity.grouping = this;
								groupedActivity.draw();
							}
							return false;
						}
					});
				}
			});
			
			// apply group -> branch mappings
			$.each(ld.branchMappings, function(){
				var groupUIID = this.groupUIID,
					branchUIID = this.sequenceActivityUIID,
					group = null,
					branch = null;
				$.each(layout.activities, function(){
					// is it the branch we're looking for?
					if (this.type == 'branchingEdge' && this.isStart) {
						$.each(this.branchingActivity.branches, function(){
							if (branchUIID == this.uiid) {
								branch = this;
								return false;
							}
						});
					// is it the grouping we're looking for
					} else if (this.type == 'grouping') {
						$.each(this.groups, function(){
							if (groupUIID == this.uiid) {
								group = this;
								return false;
							}
						});
					}
					
					// found both, no need to continue iteration
					if (group && branch) {
						return false;
					}
				});
				
				if (group && branch) {
					branch.branchingActivity.groupsToBranches.push({
						'group'  : group,
						'branch' : branch 
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
				resizePaper(paperWidth, paperHeight);
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