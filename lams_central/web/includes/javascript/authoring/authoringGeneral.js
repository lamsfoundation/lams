/**
 * This file contains global methods for Authoring.
 */

// few publicly visible variables 
var paper = null,
	canvas = null,
	
// configuration and storage of various elements
	layout = {
	// 'isZoomed'   : false,
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
	'dialogs' : [],
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
		'activity'     		  : ['','#d0defd','#fffccb','#ece9f7','#fdf1d3','#FFFFFF','#e9f9c0'],
		'offlineActivity' 	  : 'black',
		'activityText' 		  : 'black',
		'offlineActivityText' : 'white',
		'grouping'			  : '#A9C8FD',
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
		var toolId = +$(this).attr('toolId');
		// register tool properties so they are later easily accessible
		layout.toolMetadata[toolId] = {
			'iconPath' 			 : $('img', this).attr('src'),
			'supportsOutputs' 	 : $(this).attr('supportsOutputs'),
			'activityCategoryID' : +$(this).attr('activityCategoryID')
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
				
				layout.activities.push(new ActivityLib.ToolActivity(null, null, null, toolID, x, y, label));
		   }
	});
}


/**
 * Initialises various Authoring widgets.
 */
function initLayout() {
	$('#tabs').tabs({
		'activate' : function(){
			// close all opened dialogs
			$.each(layout.dialogs, function() {
				this.dialog('close');
			});
		}
	});
	$('#tabs .ui-tabs-nav, #tabs .ui-tabs-nav > *')
		.removeClass( "ui-corner-all ui-corner-top" )
		.addClass( "ui-corner-bottom" );
	$('#tabs .ui-tabs-nav').appendTo('#tabs');
	
	// the close button is shared by both load and save dialogs
	var closeLdStoreDialogButton ={
    	'text'   : 'Cancel',
    	'click'  : function() {
			$(this).dialog('close');
		}
    },
    
	// initalise Learning Design load/save dialog
	ldStoreDialog = $('#ldStoreDialog').dialog({
		'autoOpen'      : false,
		'position'      : {
			'my' : 'left top',
			'at' : 'left top',
			'of' :  'body'
		},
		'resizable'     : false,
		'width'			: 1000,
		'height'		: 785,
		'draggable'     : false,
		'buttonsLoad' : [
		             {
		            	'text'   : 'Open',
		            	'click'  : function() {
		            		var dialog = $(this),
		            			tree = dialog.dialog('option', 'ldTree'),
		            			ldNode = tree.getHighlightedNode();
		            		// no LD was chosen
		            		if (!ldNode || !ldNode.data.learningDesignId) {
		            			alert("Please choose a sequence");
		            			return;
		            		}
		            		
		            		dialog.dialog('close');
		            		openLearningDesign(ldNode.data.learningDesignId);
						}
		             },
		             closeLdStoreDialogButton
		],
		
		'buttonsSave' : [
			             {
			            	'text'   : 'Save',
			            	'click'  : function() {	
			            		var dialog = $(this),
			            			container = dialog.closest('.ui-dialog'),
			            			title = $('#ldStoreDialogNameField', container).val();
			            		if (!title) {
			            			alert('Please enter a title for the sequence');
			            			return;
			            		}
			            		
			            		var tree = dialog.dialog('option', 'ldTree'),
			            			node = tree.getHighlightedNode();
			            		if (!node) {
			            			// although an existing sequence can be highlighted 
			            			alert('Please choose a folder');
			            			return;
			            		}
			            		
			            		var nodeTitle = $(node.getContentHtml()).text(),
			            			// if a node is highlighted but user modified the title,
			            			// it is considered a new sequence
			            			learningDesingID = node.data.learningDesignId && nodeTitle == title ?
			            					node.data.learningDesignId : null;
			            		if (learningDesingID
			            				&& !confirm('Are you sure you want to overwrite the existing sequence?')) {
			            			return;
			            		}
			            		
			            		var folderID = node.data.folderID;
			            		if (!folderID) {
			            			folderID = node.parent.data.folderID;
			            		}

			            		var result = saveLearningDesign(folderID, learningDesingID, title);
			            		if (result) {
			            			dialog.dialog('close');
			            		}
							}
			             },
			             closeLdStoreDialogButton
		],
		'open' : function(){
			var nameContainer = $('#ldStoreDialogNameContainer');
			$('input', nameContainer).val(null);
			$(this).siblings('.ui-dialog-buttonpane').append(nameContainer);
		}
	});
	
	layout.dialogs.push(ldStoreDialog);
	
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
		var dialog = $(this.getEl()).closest('.ui-dialog'),
					 isSaveDialog = dialog.hasClass('ldStoreDialogSave');
		// load subfolder contents
		var childNodeData = MenuLib.getFolderContents(node.data.folderID, isSaveDialog);
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
		var dialog = $(this.getEl()).closest('.ui-dialog'),
			isSaveDialog = dialog.hasClass('ldStoreDialogSave');
		
		if (!isSaveDialog && !event.node.data.learningDesignId){
			// it is a folder in load sequence dialog, do not highlight
			return false;
		}
		// display "loading" animation and finally LD thumbnail
		$('.ldChoiceDependentCanvasElement', dialog).css('display', 'none');
		if (event.node.highlightState == 0) {
			if (event.node.data.learningDesignId) {
				$('#ldStoreDialog #ldScreenshotLoading', dialog).css('display', 'inline');
				// get the image of the chosen LD and prevent caching
				$('#ldStoreDialog #ldScreenshotAuthor', dialog)
					.attr('src', LD_THUMBNAIL_URL_BASE + event.node.data.learningDesignId + '&_=' + new Date().getTime());
				$('#ldStoreDialog #ldScreenshotAuthor', dialog).css({
					'width'  : 'auto',
					'height' : 'auto'
				});
				if (isSaveDialog) {
					// copy title of the highligthed sequence to title field
					var title = $(event.node.getContentHtml()).text();
					$('#ldStoreDialogNameField', dialog).val(title);
				}
			}
		}
	});
	tree.subscribe('clickEvent', tree.onEventToggleHighlight);

	// initialise a small info dialog
	layout.items.infoDialog = $('<div />').attr('id', 'infoDialog').dialog({
		'autoOpen'   : false,
		'width'      : 290,
		'height'     : 35,
		'resizable'  : false,
		'show'       : 'fold',
		'hide'       : 'fold',
		'draggable'  : false,
		'dialogClass': 'dialog-no-title',
		'position'   : {
						my: "right top",
					    at: "right top+5px",
					    of: '#canvas'
				      }
	});
	
	layout.dialogs.push(layout.items.infoDialog);
}


/**
 * Replace current canvas contents with the loaded sequence.
 */
function openLearningDesign(learningDesignID) {
	// get LD details
	$.ajax({
		cache : false,
		url : LAMS_URL + "authoring/author.do",
		dataType : 'json',
		data : {
			'method'          : 'getLearningDesignJSON',
			'learningDesignID': learningDesignID
		},
		success : function(ld) {
			if (!ld) {
				alert('Error while loading sequence');
				return;
			}
			
			// remove existing activities
			MenuLib.newLearningDesign(true);
			layout.ld = {
				'learningDesignID' : learningDesignID,
				'folderID'		   : ld.workspaceFolderID,
				'contentFolderID'  : ld.contentFolderID,
				'title'			   : ld.title
			};
			
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
				
				// find max uiid so newly created elements have, unique ones
				if (activityData.activityUIID && layout.ld.maxUIID < activityData.activityUIID) {
					layout.ld,maxUIID = activityData.activityUIID;
				}

				switch(activityData.activityTypeID) {
					// Tool Activity
					case 1 :
						activity = new ActivityLib.ToolActivity(activityData.activityID,
										activityData.activityUIID,
										activityData.toolContentID,
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
										activityData.activityUIID,
										activityData.xCoord,
										activityData.yCoord,
										activityData.activityTitle,
										groupingData.groupingID,
										groupingData.groupingUIID,
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
							activityData.activityUIID,
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
							branchingEdge = new ActivityLib.BranchingEdgeActivity(activityData.activityID,
									activityData.activityUIID,
									0, 0, 
									activityData.activityTitle,
									branchingType);
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
						ActivityLib.drawTransition(fromActivity, toActivity, true,
								transition.transitionID, transition.transitionUIID);
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
 * Stores the sequece in database.
 */
function saveLearningDesign(folderID, learningDesignID, title) {
	var activities = [],
		transitions = [],
		groupings = [],
		// trim the 
		title = title.trim(),
		description = $('#ldDescriptionFieldDescription').val().trim(),
		// final success/failure of the save
		result = false;
	
	$.each(layout.activities, function(){
		var activity = this,
			activityBox = activity.items.shape.getBBox(),
			activityTypeID = null,
			toolID = activity.toolID,
			iconPath = null,
			isGrouped = activity.grouping ? true : false;
		
		if (toolID) {
			// find out what is the icon for tool acitivty
			var templateIcon = $('.template[toolId=' + toolID +'] img');
			if (templateIcon.width() > 0) {
				 iconPath = layout.toolMetadata[toolID].iconPath;
			}
		}
		// translate grouping type to back-end understandable
		switch(activity.type) {
			case 'tool' :
				activityTypeID = 1;
				break;
			case 'grouping' :
				activityTypeID = 2;
				break;
		}
		
		// add activity
		activities.push({
			'activityID' 			 : activity.id,
			'activityUIID' 			 : activity.uiid,
			'toolID' 				 : toolID,
			'learningLibraryID' 	 : toolID,
			'toolContentID' 	 	 : activity.toolContentID,
			'stopAfterActivity' 	 : false,
			'groupingSupportType' 	 : 2,
			'applyGrouping' 		 : isGrouped,
			'groupingUIID'			 : isGrouped ? activity.grouping.groupingUIID : null,
		    'createGroupingUIID'	 : activity.type == 'grouping' ? activity.groupingUIID : null,
			'parentActivityID' 		 : null,
			'parentUIID' 			 : null,
			'libraryActivityUIImage' : iconPath,
			'xCoord' 				 : activityBox.x,
			'yCoord' 				 : activityBox.y,
			'activityTitle' 		 : activity.title,
			'activityCategoryID' 	 : activity.type == 'tool' ?
											layout.toolMetadata[toolID].activityCategoryID : 1,
			'activityTypeID'     	 : activityTypeID,
			
			'gradebookToolOutputDefinitionName' : null,
			'helpText' : null,
			'description' : null
		});

		// iterate over transitions and create a list
		$.each(activity.transitions.from, function(){
			var transition = this;
			
			transitions.push({
				'transitionID'   : transition.id,
				'transitionUIID' : transition.uiid,
				'fromUIID' 		 : activity.uiid,
				'toUIID'   		 : transition.toActivity.uiid,
				'transitionType' : 0
			});
		});
		
		if (activity.type == 'grouping') {
			// create a list of groupings
			var groups = [],
				groupingType = null;
			$.each(activity.groups, function(groupIndex, group){
				groups.push({
					'groupName' : group.name,
					'groupID'   : group.id,
					'groupUIID' : group.uiid,
					'orderID'   : groupIndex
				});
			});
			
			switch(activity.groupingType) {
				case 'random' :
					groupingType = 1;
					break;
				case 'monitor' :
					groupingType = 2;
					break;
				case 'learner' :
					groupingType = 4;
					break;
			}
			
			groupings.push({
				'groupingUIID'   				: activity.groupingUIID,
				'groupingTypeID' 				: groupingType,
				'learnersPerGroup' 				: activity.learnerCount,
				'equalNumberOfLearnersPerGroup' : activity.equalSizes,
				'viewStudentsBeforeSelection'   : activity.viewLearners,
				'maxNumberOfGroups' 			: activity.groupCount,
				'numberOfGroups' 				: groups.length,
				'groups' 						: groups
			});
		}
	});
	
	// serialise the sequence
	var ld = {
		// it is null if it is a new sequence
		'learningDesignID'   : learningDesignID,
		'workspaceFolderID'  : folderID,
		'copyTypeID'         : 1,
		'originalUserID'     : null,
		'title'              : title,
		'description'        : description,
		'maxID'				 : layout.ld.maxUIID,
		'readOnly'			 : false,
		'editOverrideLock'   : false,
		'dateReadOnly'       : null,
		'version'        	 : null,
		'contentFolderID'    : layout.ld.contentFolderID,
		'saveMode'			 : 0,
		'originalLearningDesignID' : null,
		
		'activities'		 : activities,
		'transitions'		 : transitions,
		'groupings'			 : groupings,
		'branchMappings'     : null,
		
		'helpText'           : null,
		'duration'			 : null,
		'licenseID'			 : null,
		'licenseText'   	 : null
	};
	
	// get LD details
	$.ajax({
		cache : false,
		async : false,
		url : LAMS_URL + "authoring/author.do",
		dataType : 'json',
		data : {
			'method'          : 'saveLearningDesign',
			'ld'			  : JSON.stringify(ld)
		},
		success : function(response) {
			// if save was successful
			if (response.learningDesignID) {
				// assing the database-generated values
				layout.ld.learningDesignID = response.learningDesignID;
				layout.ld.folderID = folderID;
				layout.ld.title = title;
				if (!layout.ld.contentFolderID) {
					layout.ld.contentFolderID = response.contentFolderID;
				}
				$('#ldDescriptionFieldTitle').text(title);
				$('#ldDescriptionFieldDescription').text(description);
				
				// check if there were any validation errors
				if (response.validation.length > 0) {
					var message = 'While saving the sequence there were following validation issues:\n';
					$.each(response.validation, function() {
						var uiid = this.UIID,
							title = '';
						// find which activity is the error about
						$.each(layout.activities, function(){
							if (uiid == this.uiid) {
								title = this.title + ': ';
							}
						});
						message += title + this.message + '\n';
					});
					
					alert(message);
					return;
				}
				
				// assign database-generated properties to activities
				$.each(response.activities, function() {
					var updatedActivity = this;
					$.each(layout.activities, function(){
						var activity = this;
						if (updatedActivity.activityUIID == activity.uiid) {
							activity.id = updatedActivity.activityID;
							activity.toolContentID = updatedActivity.toolContentID;
						}
					});
				});
				
				alert('Congratulations! Your design is valid and has been saved.');
				result = true;
			} else {
				alert('Error while saving the sequence');
			}
		},
		error : function(){
			alert('Error while saving the sequence');
		}
	});
	
	return result;
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