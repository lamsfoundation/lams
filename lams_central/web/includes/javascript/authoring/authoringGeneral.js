/**
 * This file contains global methods for Authoring.
 */

// few publicly visible variables 
var paper = null,
	canvas = null,
	nameValidator = /^[^<>^*@%$]*$/igm,
	
// configuration and storage of various elements
	layout = {
	'drawMode'   : false,
	'modified'   : false,
	 // 'isZoomed'   : false,
	'activities' : null,
	'floatingActivity' : null,
	'regions' : null,
	'labels'	 : null,
	'items' : {
		'bin'               : null,
		
		'selectedObject'    : null,
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
		'selectEffect'        : 'blue',
		'annotation'		  : '#FFFF00',
		'annotationPalette'	  : ['FFFF00', '00FFFF', '8A2BE2', '7FFF00', '6495ED',
		                   	     'FFF8DC', 'FF8C00', '00BFFF', 'DCDCDC', 'ADD8E6', '20B2AA',
		                   	     'B0C4DE', 'FFE4E1', 'FF4500', 'EE82EE'],
		'optionalActivity'    : 'rgb(194,213,254)'
	},
};


/**
 * Initialises the whole Authoring window.
 */
$(document).ready(function() {
	canvas = $('#canvas');
	
	initLayout();
	initTemplates();
	PropertyLib.init();
	MenuLib.init();
	
	MenuLib.newLearningDesign(true, true);
	layout.ld.contentFolderID = initContentFolderID;
	
	window.onbeforeunload = function(){
		if (layout.modified &&
			(layout.activities.length > 0
			|| layout.regions.length > 0
			|| layout.labels.length > 0
			|| layout.floatingActivity)) {
			return 'Your design is not saved.\nAny changes you made since you last saved will be lost.';
		}
	};
});


/**
 * Draw boxes with Tool Activity templates.
 */
function initTemplates(){
	$('.template').each(function(){
		var learningLibraryID = +$(this).attr('learningLibraryId'),
			activityCategoryID = +$(this).attr('activityCategoryId'),
			parallelChildActivities = null;
		
		if (activityCategoryID == 5) {
				// mark which HTML templates construct this parallel activity
				switch(learningLibraryID){
		    	case 28:
		    		// share resources and forum
		    		parallelChildActivities = $('.template[learningLibraryId=12]')
		    								  .add($('.template[learningLibraryId=6]'));
		    		break;
		    	case 29:
		    		// chat and scribe
		    		parallelChildActivities = $('.template[learningLibraryId=3]')
		    								  .add($('.template[learningLibraryId=20]'));
		    		break;
		    	case 30:
		    		// forum and scribe
		    		parallelChildActivities = $('.template[learningLibraryId=6]')
		    								  .add($('.template[learningLibraryId=20]'));
		    		break;
		    	}
		}
		
		// register tool properties so they are later easily accessible
		layout.toolMetadata[learningLibraryID] = {
			'iconPath' 			 	  : $('img', this).attr('src'),
			'supportsOutputs' 	 	  : $(this).attr('supportsOutputs'),
			'activityCategoryID' 	  : activityCategoryID,
			'parallelChildActivities' : parallelChildActivities
		};
		
		// if a tool's name is too long and gets broken into two lines
		// make some adjustments to layout
		var toolName = $('div', this);
		if (toolName.text().length > 12){
			toolName.css('padding-top', '8px');
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
			    var learningLibraryID = +draggable.draggable.attr('learningLibraryId'),
					activityCategoryID = +draggable.draggable.attr('activityCategoryId'),
			    	x = draggable.offset.left  + canvas.scrollLeft() - canvas.offset().left,
			    	y = draggable.offset.top   + canvas.scrollTop()  - canvas.offset().top,
			    	label = $('div', draggable.draggable).text(),
			    	activity = null,
			    	translatedEvent = ActivityLib.translateEventOnCanvas(event),
					eventX = translatedEvent[0],
					eventY = translatedEvent[1];
			    
			    if (activityCategoryID == 5) {
			    	// construct child activities out of previously referenced HTML templates
			    	var childActivities = [];
			    	layout.toolMetadata[learningLibraryID].parallelChildActivities.each(function(){
			    		var toolActivityLibraryID = +$(this).attr('learningLibraryId'),
			    			toolLabel = $('div', this).text(),
			    			childActivity = new ActivityLib.ToolActivity(null, null, null,
				    				toolActivityLibraryID, null, x, y, toolLabel);
			    		
			    		layout.activities.push(childActivity);
			    		childActivities.push(childActivity);
			    	});
			    	
			    	activity = new ActivityLib.ParallelActivity(null, null, learningLibraryID, x, y, label, childActivities);
			    } else {
			    	activity = new ActivityLib.ToolActivity(null, null, null, learningLibraryID, null, x, y, label);
			    }
			    
				layout.activities.push(activity);
				HandlerLib.dropObject(activity);
				ActivityLib.dropActivity(activity, eventX, eventY);
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
		'width'			: 1240,
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
			            			title = $('#ldStoreDialogNameField', container).val().trim();
			            		if (!title) {
			            			alert('Please enter a title for the sequence');
			            			return;
			            		}
			            		
			            		if (!nameValidator.test(title)) {
			            			alert('Sequence title can not contain any of these characters < > ^ * @ % $');
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
			MenuLib.newLearningDesign(true, true);
			layout.ld = {
				'learningDesignID' : learningDesignID,
				'folderID'		   : ld.workspaceFolderID,
				'contentFolderID'  : ld.contentFolderID,
				'title'			   : ld.title,
				'maxUIID'		   : 0
			};
			
			$('#ldDescriptionFieldTitle').html(escapeHtml(ld.title));
			CKEDITOR.instances['ldDescriptionFieldDescription'].setData(ld.description);
			
			var arrangeNeeded = false,
				branchToBranching = {},
				// helper for finding last activity in a branch
				branchToActivities = {};
			
			// create visual representation of the loaded activities
			$.each(ld.activities, function() {
				var activityData = this,
					activity = null;
				
				// find max uiid so newly created elements have, unique ones
				if (activityData.activityUIID && layout.ld.maxUIID < activityData.activityUIID) {
					layout.ld.maxUIID = activityData.activityUIID;
				}

				switch(activityData.activityTypeID) {
					// Tool Activity
					case 1 :
						activity = new ActivityLib.ToolActivity(
										activityData.activityID,
										activityData.activityUIID,
										activityData.toolContentID,
										activityData.toolID,
										LAMS_URL + activityData.authoringURL
												 + '?toolContentID='   + activityData.toolContentID
												 + '&contentFolderID=' + layout.ld.contentFolderID,
										activityData.xCoord ? activityData.xCoord : 1,
										activityData.yCoord ? activityData.yCoord : 1,
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
								
								activity = new ActivityLib.GroupingActivity(
										activityData.activityID,
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
					case 14:
						var gateType = gateType || 'condition';
						activity = new ActivityLib.GateActivity(
							activityData.activityID,
							activityData.activityUIID,
							activityData.xCoord,
							activityData.yCoord,
							gateType,
							activityData.gateStartTimeOffset,
							activityData.gateActivityCompletionBased);
						break;

					// Parallel Activity
					case 6:
						activity = new ActivityLib.ParallelActivity(
								activityData.activityID,
								activityData.activityUIID,
								activityData.learningLibraryID,
								activityData.xCoord,
								activityData.yCoord,
								activityData.activityTitle);
						break;
						
					// Optional Activity
					case 7:
						activity = new ActivityLib.OptionalActivity(
								activityData.activityID,
								activityData.activityUIID,
								activityData.xCoord,
								activityData.yCoord,
								activityData.activityTitle,
								activityData.minOptions,
								activityData.maxOptions);
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
								null, null, 0, 0, null, null, branchingEdge.branchingActivity);
						layout.activities.push(branchingEdge);

						break;
					
					// Branch (i.e. Sequence Activity)
					case 8:
						var branches = branchToBranching[activityData.parentActivityID];
						if (!branches) {
							branches =  branchToBranching[activityData.parentActivityID] = [];
						}
						branches.push(new ActivityLib.BranchActivity(activityData.activityID,
																	 activityData.activityUIID,
																	 activityData.activityTitle));
						break;
						
					// Support (Floating) activity
					case 15:
						activity = new ActivityLib.FloatingActivity(
								activityData.activityID,
								activityData.activityUIID,
								activityData.xCoord,
								activityData.yCoord);
						break;
				}
				
				
				if (!activity) {
					// activity type not supported yet
					return true;
				}
				
				if (!(activity instanceof ActivityLib.FloatingActivity)) {
					layout.activities.push(activity);
				}
				
				// store information about the branch the activity belongs to
				if (activityData.parentActivityID) {
					var branchData = branchToActivities[activityData.parentActivityID];
					if (branchData) {
						if (activityData.orderID > branchData.lastActivityOrderID) {
							// is it the last activity in the branch?
							branchData.lastActivityOrderID = activityData.orderID;
							branchData.lastActivity = activity;
						}
					} else {
						branchData = branchToActivities[activityData.parentActivityID] = {
								'lastActivityOrderID' : activityData.orderID,
								'lastActivity'        : activity
							};
					}
					
					if (activityData.orderID == 1) {
						// is it the first activity in the branch
						branchData.firstActivity = activity;
					}
				}
			});
			
			// assign previously extracted branches to branching activities
			$.each(branchToBranching, function(branchingID, branches){
				$.each(layout.activities, function(){
					if (this instanceof ActivityLib.BranchingEdgeActivity
							&& this.branchingActivity.id == branchingID){
						var branchingActivity = this.branchingActivity;
						branchingActivity.branches = branches;
						$.each(branches, function(){
							this.branchingActivity = branchingActivity;
						});
						
						return false;
					}
				});
			});
			
			
			// apply existing groupings and parent-child references to activities 
			$.each(ld.activities, function(){
				var activityData = this,
					activity = this.activity;
				
				if (activity) {
					if (activityData.applyGrouping) {
						var groupedActivity = activityData.activity;
						
						$.each(layout.activities, function(){
							if (this instanceof ActivityLib.GroupingActivity
									&& this.groupingID == activityData.groupingID) {
								// add reference and redraw the grouped activity
								if (groupedActivity instanceof ActivityLib.BranchingEdgeActivity) {
									groupedActivity.branchingActivity.grouping = this;
								} else {
									groupedActivity.grouping = this;
									groupedActivity.draw();
								}
								return false;
							}
						});
					} else if (layout.floatingActivity && layout.floatingActivity.id == activityData.parentActivityID) {
						// add a Tool Activity as a Floating Activity element
						if (!layout.floatingActivity.childActivities) {
							layout.floatingActivity.childActivities = [];
						}
						layout.floatingActivity.childActivities.push(activity);
						activity.parentActivity = layout.floatingActivity;
						if (!arrangeNeeded){
							// if no auto re-arrange will be done, just redraw the container with its child activities 
							layout.floatingActivity.draw();
						}	
					}
					
					// find Optional/Parallel Activity
					if (activityData.parentActivityID && !activity.parentActivity) {
						$.each(layout.activities, function(){
							if (activityData.parentActivityID == this.id
									&& (this instanceof ActivityLib.ParallelActivity
										|| this instanceof ActivityLib.OptionalActivity)) {
								// add a Tool Activity as a Optional/Parallel Activity element
								if (!this.childActivities) {
									this.childActivities = [];
								}
								this.childActivities.push(activity);
								activity.parentActivity = this;
								if (!arrangeNeeded) {
									// if no auto re-arrange will be done, just redraw the container with its child activities
									this.draw();
								}
								
								// stop iteration
								return false;
							}
						});
					}
				}
			});
			
			// apply group -> branch mappings
			$.each(ld.branchMappings, function(){
				var entry = this,
					group = null,
					branch = null;
				$.each(layout.activities, function(){
					// is it the branch we're looking for?
					if (this instanceof ActivityLib.BranchingEdgeActivity && this.isStart) {
						$.each(this.branchingActivity.branches, function(){
							if (entry.sequenceActivityUIID == this.uiid) {
								branch = this;
								return false;
							}
						});
					// is it the grouping we're looking for?
					} else if (this instanceof ActivityLib.GroupingActivity) {
						$.each(this.groups, function(){
							if (entry.groupUIID == this.uiid) {
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
						'id'	: entry.entryID,
						'uiid' : entry.entryUIID,
						'group'     : group,
						'branch'    : branch
					});
				}
			});
			
			// draw starting and ending transitions in branches
			$.each(layout.activities, function(){
				if (this instanceof ActivityLib.BranchingEdgeActivity && this.isStart) {
					var branchingActivity = this.branchingActivity,
						branches = branchingActivity.branches.slice();
					branchingActivity.branches = [];
					
					$.each(branches, function(){
						var branch = this,
							// if there is no branch data, the branch is empty
							branchData = branchToActivities[branch.id];
						
						// add reference to the transition inside branch
						ActivityLib.addTransition(branchingActivity.start,
								branchData ? branchData.firstActivity : branchingActivity.end,
								true, null, null, branch);
						if (branchData) {
							ActivityLib.addTransition(branchData.lastActivity, branchingActivity.end, true);
						}
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
						isBranching = activity instanceof ActivityLib.BranchingEdgeActivity;
					
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
						ActivityLib.addTransition(fromActivity, toActivity, true,
								transition.transitionID, transition.transitionUIID);
						return false;
					}
				});
			});
			
			$.each(ld.annotations, function(){
				var isRegion = this.endXcoord;
				if (isRegion) {
					DecorationLib.addRegion(this.xcoord, this.ycoord, this.endXcoord, this.endYcoord,
											this. title, this.color);
				} else {
					DecorationLib.addLabel(this.xcoord, this.ycoord, this.title);
				}
			});
			
			
			if (arrangeNeeded) {
				MenuLib.arrangeActivities();
			} else {
				resizePaper();
			}
			
			setModified(false);
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
		branchMappings = [],
		annotations = [],
		layoutActivities = [],
		// trim the 
		title = title.trim(),
		description = CKEDITOR.instances['ldDescriptionFieldDescription'].getData(),
		// final success/failure of the save
		result = false,
		error = null;
	
	$.each(layout.activities, function(){
		if (this.parentActivity	&& (this.parentActivity instanceof ActivityLib.BranchingActivity
						|| this.parentActivity instanceof ActivityLib.BranchActivity)){
			// remove previously set parent activities as they will be re-set from the start
			this.parentActivity = null;
			this.orderID = null;
		}
	});
	
	$.each(layout.activities, function(){
		// add all branch activities for iteration and saving
		if (this instanceof ActivityLib.BranchingEdgeActivity){
			if (this.isStart){
				var branchingActivity = this.branchingActivity;
				layoutActivities.push(branchingActivity);
				
				$.each(branchingActivity.branches, function(branchOrderID){
					this.defaultActivityUIID = null;
					this.orderID = branchOrderID + 1;
					this.parentActivity = branchingActivity;
					layoutActivities.push(this);
					
					var childActivity = this.transitionFrom.toActivity,
						orderID = 1;
					while (!(childActivity instanceof ActivityLib.BranchingEdgeActivity
							&& !childActivity.isStart)) {
						childActivity.parentActivity = this;
						childActivity.orderID = orderID;
						if (orderID == 1){
							this.defaultActivityUIID = childActivity.uiid;
						}
						orderID++;
						
						if (childActivity.transitions.from.length == 0) {
							// we need to carry on to assign parent to all remaining child activities
							error = 'One of branches does not have a transition to the end point';
							break;
						}
						childActivity = childActivity.transitions.from[0].toActivity;
					}
				});
			}
		} else {
			layoutActivities.push(this);
		}
	});
	
	if (error) {
		alert(error);
		return false;
	}
	
	if (layout.floatingActivity){
		layoutActivities.push(layout.floatingActivity);
	}
	
	$.each(layoutActivities, function(){
		var activity = this,
			activityBox = activity.items ? activity.items.shape.getBBox() : null,
			x = activityBox ? parseInt(activityBox.x) : null,
			y = activityBox ? parseInt(activityBox.y) : null,
			activityTypeID = null,
			toolID = activity.toolID,
			learningLibraryID = toolID ? toolID : activity.learningLibraryID,
			activityCategoryID = activity instanceof ActivityLib.ToolActivity ?
					   			 layout.toolMetadata[learningLibraryID].activityCategoryID : 
					             activity instanceof ActivityLib.ParallelActivity ? 5 : 1,
			iconPath = null,
			isGrouped = activity.grouping ? true : false,
			parentActivityID = activity.parentActivity ? activity.parentActivity.id : null;
		
		if (toolID) {
			activityTypeID = 1;
			// find out what is the icon for tool acitivty
			var templateIcon = $('.template[learningLibraryId=' + learningLibraryID +'] img');
			if (templateIcon.width() > 0) {
				 iconPath = layout.toolMetadata[learningLibraryID].iconPath;
			}
		}
		// translate activity type to back-end understandable
		else if (activity instanceof ActivityLib.GroupingActivity){
			activityTypeID = 2;	
			
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
			
		} else if (activity instanceof  ActivityLib.GateActivity){
			switch(activity.gateType) {
				case 'sync'      : activityTypeID = 3; break;
				case 'schedule'  : activityTypeID = 4; break;
				case 'permision' : activityTypeID = 5; break;
				case 'condition' : activityTypeID = 14; break;
			}
		} else if (activity instanceof ActivityLib.ParallelActivity) {
			activityTypeID = 6;
		} else if (activity instanceof ActivityLib.OptionalActivity) {
			activityTypeID = 7;
		} else if (activity instanceof ActivityLib.BranchingActivity) {
			activityBox = activity.start.items.shape.getBBox();
			
			switch(activity.branchingType) {
				case 'chosen' : activityTypeID = 10; break;
				case 'group'  :
					activityTypeID = 11;
					$.each(activity.groupsToBranches, function(){
						branchMappings.push({
							'entryID'			   : this.id,
							'entryUIID'			   : this.uiid,
							'groupUIID' 		   : this.group.uiid,
							'branchingActivityUIID': this.branch.branchingActivity.uiid,
							'sequenceActivityUIID' : this.branch.uiid,
							'condition'			   : null
						});
					});
					
					break;
				case 'tool'   : activityTypeID = 12; break;
			}
		} else if (activity instanceof ActivityLib.BranchActivity){
			activityTypeID = 8;
		} else if (activity instanceof ActivityLib.FloatingActivity){
			activityTypeID = 15;
		}
		
		if (activity.parentActivity && activity.parentActivity instanceof DecorationLib.Container){
			// positions are relative to parent container
			var activityBox = activity.parentActivity.items.getBBox();
			x -= activityBox.x;
			y -= activityBox.y;
		}
		
		// add activity
		activities.push({
			'activityID' 			 : activity.id,
			'activityUIID' 			 : activity.uiid,
			'toolID' 				 : toolID,
			'learningLibraryID' 	 : learningLibraryID,
			'toolContentID' 	 	 : activity.toolContentID,
			'stopAfterActivity' 	 : false,
			'groupingSupportType' 	 : 2,
			'applyGrouping' 		 : isGrouped,
			'groupingUIID'			 : isGrouped ? activity.grouping.groupingUIID : null,
		    'createGroupingUIID'	 : activity instanceof ActivityLib.GroupingActivity ? activity.groupingUIID : null,
			'parentActivityID' 		 : activity.parentActivity ? activity.parentActivity.id : null,
			'parentUIID' 			 : activity.parentActivity ? activity.parentActivity.uiid : null,
			'libraryActivityUIImage' : iconPath,
			'xCoord' 				 : x,
			'yCoord' 				 : y,
			'activityTitle' 		 : activity.title,
			'activityCategoryID' 	 : activityCategoryID,
			'activityTypeID'     	 : activityTypeID,
			'orderID'				 : activity.orderID,
			'defaultActivityUIID'    : activity.defaultActivityUIID,
			'gateStartTimeOffset'	 : activity.gateType == 'schedule' ?
										activity.offsetDay*24*60 + activity.offsetHour*60 + activity.offsetMinute : null,
			'gateActivityCompletionBased' : activity.gateActivityCompletionBased,
			
			'gradebookToolOutputDefinitionName' : null,
			'helpText' : null,
			'description' : null
		});

		var activityTransitions = activity instanceof ActivityLib.BranchingActivity ?
				activity.end.transitions : activity.transitions;
		
		if (activityTransitions) {
			// iterate over transitions and create a list
			$.each(activityTransitions.from, function(){
				var transition = this,
					toActivity = transition.toActivity;
				if (toActivity instanceof ActivityLib.BranchingEdgeActivity) {
					if (toActivity.isStart) {
						toActivity = toActivity.branchingActivity;
					} else {
						// skip transition from last activity in branch to branching edge marker
						return true;
					}
				}
				
				transitions.push({
					'transitionID'   : transition.id,
					'transitionUIID' : transition.uiid,
					'fromUIID' 		 : activity.uiid,
					'toUIID'   		 : toActivity.uiid,
					'transitionType' : 0
				});
			});
		}
	});
	
	if (error) {
		alert(error);
		return false;
	}
	
	// iterate over labels and regions
	$.each(layout.labels.concat(layout.regions), function(){
		var box = this.items.shape.getBBox(),
			isRegion = this instanceof DecorationLib.Region;
		
		annotations.push({
			'id'		: this.id,
			'annotationUIID' : this.uiid,
			'title' 	: this.title,
			'xCoord'    : box.x,
			'yCoord'    : box.y,
			'endXCoord' : isRegion ? box.x2 : null,
			'endYCoord' : isRegion ? box.y2 : null,
			'color'	    : isRegion ? this.items.shape.attr('fill') : null
		});
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
		'saveMode'			 : layout.ld.learningDesignID
							   && layout.ld.learningDesignID != learningDesignID
							   ? 1 : 0,
		'originalLearningDesignID' : null,
		
		'activities'		 : activities,
		'transitions'		 : transitions,
		'groupings'			 : groupings,
		'branchMappings'     : branchMappings,
		'annotations'		 : annotations,
		
		'helpText'           : null,
		'duration'			 : null,
		'licenseID'			 : null,
		'licenseText'   	 : null
	};
	
	// get LD details
	$.ajax({
		type  : 'POST',
		cache : false,
		async : false,
		url : LAMS_URL + "authoring/author.do",
		dataType : 'json',
		data : {
			'method'          : 'saveLearningDesign',
			'ld'			  : JSON.stringify(ld)
		},
		success : function(response) {
			layout.ld.folderID = folderID;
			layout.ld.title = title;
			
			// check if there were any validation errors
			if (response.validation.length > 0) {
				var message = 'While saving the sequence there were following validation issues:\n';
				$.each(response.validation, function() {
					var uiid = this.UIID,
						title = '';
					if (uiid) {
						// find which activity is the error about
						$.each(layout.activities, function(){
							if (uiid == this.uiid) {
								title = this.title + ': ';
							}
						});
					}
					message += title + this.message + '\n';
				});
				
				alert(message);
			}
			
			// if save (even partially) was successful
			if (response.ld) {
				// assing the database-generated values
				layout.ld.learningDesignID = response.ld.learningDesignID;
				if (!layout.ld.contentFolderID) {
					layout.ld.contentFolderID = response.ld.contentFolderID;
				}
				$('#ldDescriptionFieldTitle').text(title);
				$('#ldDescriptionFieldDescription').text(description);
				
				// assign database-generated properties to activities
				$.each(response.ld.activities, function() {
					var updatedActivity = this;
					$.each(layout.activities, function(){
						var isBranching = this instanceof ActivityLib.BranchingEdgeActivity,
							found = false;
						if (isBranching && !isStart) {
							return true;
						}
						
						if (isBranching) {
							if (updatedActivity.activityUIID == this.branchingActivity.uiid){
								this.branchingActivity.id = updatedActivity.activityID;
								found = true;
							} else {
								$.each(this.branchingActivity.branches, function(){
									if (updatedActivity.activityUIID == this.branchingActivity.uiid){
										this.id = updatedActivity.activityID;
									}
								});
							}
						} else if (updatedActivity.activityUIID == this.uiid) {
							this.id = updatedActivity.activityID;
							this.toolContentID = updatedActivity.toolContentID;
							found = true;
						}
						
						// update transition IDs
						$.each(this.transitions.from, function(){
							var existingTransition = this;
							$.each(response.ld.transitions, function(){
								if (existingTransition.uiid == +this.transitionUIID) {
									existingTransition.id = +this.transitionID;
									return false;
								}
							});
						});
						
						return !found;
					});
				});
				
				if (layout.floatingActivity) {
					layout.floatingActivity.id = response.floatingActivityID;
				}
				
				// update annotation IDs
				$.each(response.ld.annotations, function(){
					var updatedAnnotation = this;
					$.each(layout.labels.concat(layout.regions), function(){
						if (this.uiid == updatedAnnotation.annotationUIID) {
							this.id = updatedAnnotation.uid;
							return false;
						}
					});
				});
				
				if (response.validation.length == 0) {
					setModified(false);
					alert('Congratulations! Your design is valid and has been saved.');
					result = true;
				}
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

	if (!width || !height) {
		var width = 0,
			height = 0;
		$.each(layout.activities, function(){
			// find new dimensions of paper
			var activityBox = this.items.shape.getBBox();
			if (activityBox.x2 + 30 > width) {
				width = activityBox.x2 + 30;
			}
			if (activityBox.y2 + 30 > height) {
				height = activityBox.y2 + 30;
			}
		});
	}
	
	// -20 so Chrome does not create unnecessary scrollbars when dropping a tool template to canvas
	// +50 so there is space for rubbish bin
	width = Math.max(width, canvas.width()) - 20;
	height = Math.max(height + 50, canvas.height()) - 20;
	
	paper.setSize(width, height);
	$('#templateContainer').height($('#ldDescriptionDiv').height() 
								 + $('#canvas').height() - 10);
	
	if (layout.items.bin) {
		layout.items.bin.remove();
	}
	
	// draw rubbish bin on canvas
	var binPath = Raphael.parsePathString(layout.defs.bin);
	binPath = Raphael.transformPath(binPath, Raphael.format('t {0} {1}', width - 5, height - 50));
	layout.items.bin = paper.path(binPath);
	
	HandlerLib.resetCanvasMode(true);
}


/**
 * Tells that current sequence was modified and not saved.
 */
function setModified(modified) {
	layout.modified = modified;
	if (modified) {
		$('#previewButton').attr('disabled', 'disabled')
						   .button('option', 'disabled', true);
		$('#ldDescriptionFieldModified').text('*');
	} else {
		$('#previewButton').attr('disabled', null)
						   .button('option', 'disabled', false);

		$('#ldDescriptionFieldModified').text('');
	}
	
	if (modified || layout.activities.length == 0) {
		$('#exportButton').attr('disabled', 'disabled')
		  				  .css('opacity', 0.2);
	} else {
		$('#exportButton').attr('disabled', null)
		  				  .css('opacity', 1);
	}
}


/**
 * Escapes HTML tags to prevent XSS injection.
 */
function escapeHtml(unsafe) {
    return unsafe
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#039;");
}