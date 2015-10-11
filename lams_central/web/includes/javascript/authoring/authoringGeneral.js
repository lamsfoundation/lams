﻿/**
 * This file contains main methods for Authoring.
 */


/**
 * Initialises each part of the Authoring window.
 */
$(document).ready(function() {
	canvas = $('#canvas');
	
	GeneralInitLib.initTemplates();
	if (!isReadOnlyMode) {
		// in read-only mode (SVG generator), some parts are not necessary and not loaded
		GeneralInitLib.initLayout();
		PropertyLib.init();
		MenuLib.init();
	}
	
	GeneralLib.newLearningDesign(true, true);
	if (!isReadOnlyMode) {
		layout.ld.contentFolderID = initContentFolderID;
	}
	if (initLearningDesignID) {
		GeneralLib.openLearningDesign(+initLearningDesignID);
	}
});


/**
 * A few publicly visible variables .
 */

	// The Snap Paper object
var paper = null,
	// container for the paper 
	canvas = null,
	
	// configuration and storage of various elements
	layout = {
		// draw mode prevents some handlers (click, mouseover etc.) from triggering
		'drawMode'   : false,
		// is the LD being live edited?
		'liveEdit'   : false,
		// should the sequence be saved before exiting?
		'modified'   : false,
		// list of all dialogs, so they can be easily closed all at once 
		'dialogs' : [],
		// stores precached tool images so they can be used in exported SVG
		'iconLib' : {
			'bin' : 'images/authoring_bin.svg'
		},
		// icons for special activities
		'toolMetadata': {
			'gate'     : {		
				'iconPath' : '../images/stop.gif'
			},
			'grouping' : {
				'iconPath' : '../images/grouping.gif'
			}
		},
		
		// graphics contants
		'conf' : {
			'arrangeHorizontalSpace'           : 200,
			'arrangeVerticalSpace'             : 100,
			'arrangeHorizontalPadding'         : 35,
			'arrangeVerticalPadding'           : 50,
			
			'dragStartThreshold'               : 300,
			
			'propertiesDialogDimOpacity'       : 0.3,
			'propertiesDialogDimThreshold'     : 100,
			'propertiesDialogDimThrottle'      : 100,
			
			'defaultGroupingGroupCount'        : 2,
			'defaultGroupingLearnerCount'      : 1,
			
			'containerActivityEmptyWidth'  	   : 50,
			'containerActivityEmptyHeight' 	   : 70,
			'containerActivityPadding'	       : 20,
			'containerActivityChildrenPadding' : 10,
			'regionEmptyWidth'				   : 20,
			'regionEmptyHeight'				   : 20,
			
			'groupingEffectPadding'			   : 5,
			'selectEffectPadding'			   : 7,
			
			'supportsDownloadAttribute'		   : typeof $('<a/>')[0].download != 'undefined',
			
			// will be initialised when paper gets created
			'readOnlyFilter'				   : null
		},
		
		'colors' : {
			'activityBorder'      : 'black',
			// each activity type has its own colour
			'activity'     		  : ['','#d0defd','#fffccb','#ece9f7','#fdf1d3','#FFFFFF','#e9f9c0'],
			'activityText' 		  : 'black',
			// default region colour
			'annotation'		  : '#FFFF00',
			// region colours to choose from
			'annotationPalette'	  : ['FFFF00', '00FFFF', '8A2BE2', '7FFF00', '6495ED',
			                   	     'FFF8DC', 'FF8C00', '00BFFF', 'DCDCDC', 'ADD8E6', '20B2AA',
			                   	     'B0C4DE', 'FFE4E1', 'FF4500', 'EE82EE'],

			// when mouse hovers over rubbish bin
			'binSelect' 		  : 'red',
			
			'branchingEdgeStart'  : 'green',
			'branchingEdgeEnd'    : 'red',
			// highlight branching edges on mouse hover
			'branchingEdgeMatch'  : 'blue',
			'gate'         		  : 'red',
			'gateText'     		  : 'white',
			'grouping'			  : '#A9C8FD',
			'optionalActivity'    : 'rgb(194,213,254)',
			// dashed border around a selected activity 
			'selectEffect'        : 'blue',
			'transition'   		  : 'rgb(119,126,157)'
		},
	
		'defaultTextAttributes' : {
			'text-anchor' : 'middle',
			'font-size'   : 10,
			'font-family' : 'sans-serif'
		}
},


/**
 * Contains methods for Authoring window initialisation which are run only once at the beginning 
 */
GeneralInitLib = {
	/**
	 * Draw boxes with Tool Activity templates in the panel on the left.
	 */
	initTemplates : function(){
		// store some template data in JS structures
		$('.template').each(function(){
			var learningLibraryID = +$(this).attr('learningLibraryId'),
				activityCategoryID = +$(this).attr('activityCategoryId'),
				parallelChildActivityDefs = null;
			
			if (activityCategoryID == 5) {
				var childToolIds = $(this).attr('childToolIds').split(',');
				$.each(childToolIds, function(){
					var childToolId = this.trim();
					if (childToolId) {
						parallelChildActivity = $('.template[toolId=' + childToolId + ']');
						if (parallelChildActivityDefs) {
							parallelChildActivityDefs = parallelChildActivityDefs.add(parallelChildActivity);
						} else {
							parallelChildActivityDefs = parallelChildActivity;
						}
					}
				});
			}
			
			var iconPath = $('img', this).attr('src');
			// register tool properties so they are later easily accessible
			layout.toolMetadata[learningLibraryID] = {
				'iconPath' 			 	  : iconPath,
				'defaultToolContentID'    : $(this).attr('defaultToolContentId'),
				'supportsOutputs' 	 	  : $(this).attr('supportsOutputs'),
				'activityCategoryID' 	  : activityCategoryID,
				'parallelChildActivityDefs' : parallelChildActivityDefs
			};
			
			if (!isReadOnlyMode) {
				// if a tool's name is too long and gets broken into two lines
				// make some adjustments to layout
				var toolName = $('div', this);
				if (toolName.text().length > 12){
					toolName.css('padding-top', '8px');
				}
			}
		});
		
		if (!isReadOnlyMode){
			// create list of learning libraries for each group
			var templateContainerCell = $('#templateContainerCell'),
				learningLibraryGroupSelect = $('select', templateContainerCell),
				allGroup = $('option', learningLibraryGroupSelect),
				allTemplates = $('#templateContainerCell .templateContainer').show();
		
			learningLibraryGroupSelect.change(function(){
				$('.templateContainer').hide();
				// show DIV with the selected learning libraries group
				$('option:selected', this).data('templates').show();
			});
			allGroup.data('templates', allTemplates);
			
			$.each(learningLibraryGroups, function(){
				var templates = allTemplates.clone().appendTo(templateContainerCell),
					learningLibraries = this.learningLibraries;
				// cloned everything, now remove ones that are not in the list
				$('.template', templates).each(function(){
					var learningLibraryId = $(this).attr('learningLibraryId'),
						found = false;
					$.each(learningLibraries, function(){
						if (learningLibraryId == this) {
							found = true;
							return false;
						}
					});
					
					if (!found) {
						$(this).remove();
					}
				});
				
				$('<option />').text(this.name)
							   .data('templates', templates)
							   .appendTo(learningLibraryGroupSelect);
			});
			
			$('.template').each(function(){
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
					    	toolID = +draggable.draggable.attr('toolId'),
							activityCategoryID = +draggable.draggable.attr('activityCategoryId'),
					    	x = draggable.offset.left  + canvas.scrollLeft() - canvas.offset().left,
					    	y = draggable.offset.top   + canvas.scrollTop()  - canvas.offset().top,
					    	label = $('div', draggable.draggable).text(),
					    	activity = null,
					    	translatedEvent = GeneralLib.translateEventOnCanvas(event),
							eventX = translatedEvent[0],
							eventY = translatedEvent[1],
							iconPath = layout.toolMetadata[learningLibraryID].iconPath,
							pngPath = iconPath && /\.svg$/i.test(iconPath)
								? iconPath.substring(0, iconPath.indexOf('.svg')) + '.png' : iconPath;
										    
					    if (activityCategoryID == 5) {
					    	// construct child activities out of previously referenced HTML templates
					    	var childActivities = [];
					    	layout.toolMetadata[learningLibraryID].parallelChildActivityDefs.each(function(){
					    		var childLearningLibraryID = +$(this).attr('learningLibraryId'),
					    			childToolID = +$(this).attr('toolId'),
					    			toolLabel = $('div', this).text(),
					    			childActivity = new ActivityDefs.ToolActivity(null, null, null,
					    					childToolID, childLearningLibraryID, null, x, y, toolLabel);
					    		
					    		layout.activities.push(childActivity);
					    		childActivities.push(childActivity);
					    	});
					    	
					    	activity = new ActivityDefs.ParallelActivity(null, null, learningLibraryID, x, y, label, false, childActivities);
					    } else {
					    	activity = new ActivityDefs.ToolActivity(null, null, null, toolID, learningLibraryID, null, x, y, label, false);
					    }
					    
						layout.activities.push(activity);
						HandlerLib.dropObject(activity);
						ActivityLib.dropActivity(activity, eventX, eventY);
						
						// precache PNG icons for SVG export
						if (pngPath && !layout.iconLib[pngPath]) {
							var ajax = new XMLHttpRequest();
							ajax.open("GET", pngPath, true);
							ajax.responseType = "arraybuffer";
							ajax.onload = function() {
								if (ajax.response) {
					                layout.iconLib[pngPath] =
					                	'data:image/png;base64,' + btoa(String.fromCharCode.apply(null, new Uint8Array(ajax.response)));
								}
				            };
				            ajax.send(null);
						}
				   }
			});
		}
	},


	/**
	 * Initialises various Authoring widgets.
	 */
	initLayout : function(){
		// buttons shared by both load and save dialogs
		var sharedButtons = [
		{
		'id'	   : 'cancelButton',
	    	'text'     : 'Cancel',
	    	'tabIndex' : -1,
	    	'click'    : function() {
				$(this).dialog('close');
			}
	    },
	    	
		// creates a new folder
	    {
			'class'    : 'leftDialogButton',
			'text'     : LABELS.NEW_FOLDER_BUTTON,
			'tabIndex' : -1,
			'click'    : function(){
	    		var dialog = $(this),
					tree = dialog.dialog('option', 'ldTree'),
					// hightlighted sequence/folder in the tree
					ldNode = tree.getHighlightedNode();
	    		if (!ldNode) {
	    			return;
	    		}
	    		
				var	title = prompt(LABELS.NEW_FOLDER_TITLE_PROMPT);
				// skip if no name was provided
				if (!title) {
					return;
				}
				if (!GeneralLib.nameValidator.test(title)) {
	    			alert(LABELS.TITLE_VALIDATION_ERROR);
	    			return;
	    		}
				
				var parentFolder = ldNode.data.learningDesignId ? ldNode.parent : ldNode;
				$.each(parentFolder.children, function(){
					if (this.label == title) {
						alert(LABELS.FOLDER_EXISTS_ERROR);
						title = null;
						return false;
					}
				});
				if (!title) {
					return;
				}
				
				
				$.ajax({
					cache : false,
					async : false,
					url : LAMS_URL + "workspace.do",
					dataType : 'text',
					data : {
						'method'         : 'createFolderForFlash',
						'name' 		     : title,
						'parentFolderID' : parentFolder.data.folderID
					},
					success : function(response) {
						// still process WDDX packet, to be changed when we get rid of Flash Authoring
						var messageIndex = response.indexOf("folderID'><number>") + 18,
							folderID = response.substring(messageIndex, response.indexOf('<', messageIndex));
						
						if (!GeneralLib.numberValidator.test(folderID)) {
							// error
							var messageIndex = response.indexOf("messageValue'><string>") + 22,
								message = response.substring(messageIndex, response.indexOf('<', messageIndex));
							alert(message);
							return;
						}
						
						tree.removeChildren(parentFolder);
						parentFolder.expand();
					}
				});
			}
		},
		
		// copy sequence or folder
	    {
			'class'    : 'leftDialogButton',
			'text'     : LABELS.COPY_BUTTON,
			'tabIndex' : -1,
			'click'    : function(){
	    		var dialog = $(this),
					tree = dialog.dialog('option', 'ldTree'),
					// hightlighted sequence/folder in the tree
					ldNode = tree.getHighlightedNode(),
					isFolder = ldNode && !ldNode.data.learningDesignId;
	    		if (!ldNode) {
	    			return;
	    		}
	    		
	    		dialog.dialog('option', 'copiedResource', {
	    			'isFolder'   : isFolder,
	    			'resourceID' : isFolder ?  ldNode.data.folderID : ldNode.data.learningDesignId
	    		});
			}
		},
		
		// pastes sequence or folder
	    {
			'class'    : 'leftDialogButton',
			'text'     : LABELS.PASTE_BUTTON,
			'tabIndex' : -1,
			'click'    : function(){
	    		var dialog = $(this),
					tree = dialog.dialog('option', 'ldTree'),
					// hightlighted sequence/folder in the tree
					ldNode = tree.getHighlightedNode(),
					folderNode = ldNode ? (ldNode.data.learningDesignId ? ldNode.parent : ldNode) : null,
					copiedResource = dialog.dialog('option','copiedResource');
					
				if (!folderNode || !copiedResource) {
	    			return;
	    		}

				$.ajax({
					cache : false,
					async : false,
					url : LAMS_URL + "workspace.do",
					dataType : 'text',
					data : {
						'method'         : 'copyResource',
						'targetFolderID' : folderNode.data.folderID,
						'resourceID'     : copiedResource.resourceID,
						'resourceType'   : copiedResource.isFolder ? 'Folder' : 'LearningDesign'
					},
					success : function(response) {
						// still process WDDX packed, to be changed when we get rid of Flash Authoring
						var messageIndex = response.indexOf("messageValue'><number>") + 22,
							message = response.substring(messageIndex, response.indexOf('<', messageIndex));
						if (!GeneralLib.numberValidator.test(message)) {
							// error
							alert(message);
							return;
						}
						
						tree.removeChildren(folderNode);
						folderNode.expand();
					}
				});
			}
		},
		
		// removes sequence or folder
	    {
			'class'    : 'leftDialogButton',
			'text'     : LABELS.DELETE_BUTTON,
			'tabIndex' : -1,
			'click'    : function(){
	    		var dialog = $(this),
					tree = dialog.dialog('option', 'ldTree'),
					// hightlighted sequence/folder in the tree
					ldNode = tree.getHighlightedNode(),
					isFolder = ldNode && !ldNode.data.learningDesignId;
	    		if (!ldNode || !confirm(LABELS.DELETE_NODE_CONFIRM + (isFolder ? LABELS.FOLDER : LABELS.SEQUENCE) + '?')) {
	    			return;
	    		}
				
				$.ajax({
					cache : false,
					async : false,
					url : LAMS_URL + "workspace.do",
					dataType : 'text',
					data : {
						'method'       : 'deleteResource',
						'resourceID'   : isFolder? ldNode.data.folderID : ldNode.data.learningDesignId,
						'resourceType' : isFolder ? 'Folder' : 'LearningDesign'
					},
					success : function(response) {
						// still process WDDX packed, to be changed when we get rid of Flash Authoring
						var messageIndex = response.indexOf("messageValue'><string>") + 22,
							message = response.substring(messageIndex, response.indexOf('<', messageIndex));
						if (!/^.*:\d+$/.test(message)) {
							// error
							alert(message);
							return;
						}
						
						var parentFolder = ldNode.parent;
						tree.removeChildren(parentFolder);
						parentFolder.expand();
					}
				});
			}
		},
		
		
		// renames sequence or folder
	    {
			'class'    : 'leftDialogButton',
			'text'     : LABELS.RENAME_BUTTON,
			'tabIndex' : -1,
			'click'    : function(){
	    		var dialog = $(this),
					tree = dialog.dialog('option', 'ldTree'),
					// hightlighted sequence/folder in the tree
					ldNode = tree.getHighlightedNode(),
					isFolder = ldNode && !ldNode.data.learningDesignId;
	    		if (!ldNode) {
	    			return;
	    		}
				var title = prompt(LABELS.RENAME_TITLE_PROMPT + (isFolder ? LABELS.FOLDER : LABELS.SEQUENCE)
							+ ' "' + ldNode.label + '"');
				
				// skip if no name or the same name was provided
				if (!title || ldNode.label == title) {
					return;
				}
				if (!GeneralLib.nameValidator.test(title)) {
	    			alert(LABELS.TITLE_VALIDATION_ERROR);
	    			return;
	    		}
				
				$.each(ldNode.parent.children, function(){
					if (this.label == title && (isFolder == (this.data.folderID != null))) {
						alert(isFolder ? LABELS.FOLDER_EXISTS_ERROR : LABELS.SEQUENCE_EXISTS_ERROR);
						title = null;
						return false;
					}
				});
				if (!title) {
					return;
				}
				
				$.ajax({
					cache : false,
					async : false,
					url : LAMS_URL + "workspace.do",
					dataType : 'text',
					data : {
						'method'       : 'renameResourceJSON',
						'name' 		   : title,
						'resourceID'   : isFolder? ldNode.data.folderID : ldNode.data.learningDesignId,
						'resourceType' : isFolder ? 'Folder' : 'LearningDesign'
					},
					success : function(response) {
						// still process WDDX packed, to be changed when we get rid of Flash Authoring
						var messageIndex = response.indexOf("messageValue'><string>") + 22,
							message = response.substring(messageIndex, response.indexOf('<', messageIndex));
						if (message != title) {
							// error
							alert(message);
							return;
						}
						if (isFolder) {
							ldNode.label = title;
							ldNode.getLabelEl().innerHTML = title;
						} else {
							// refresh all opened folders in the tree
		    				var folders = tree.getRoot().children;
		    				if (folders) {
		    					$.each(folders, function(){
		    						var expanded = this.expanded;
									tree.removeChildren(this);
		    						if (expanded) {
		    							this.expand();
		    						}
		    					});
		    				}
		    				
		    				// fetch access list again
							GeneralLib.updateAccess(null, true);
						}
					}
				});
			}
		}];
	    
		// initalise Learning Design load/save dialog
		layout.ldStoreDialog = $('#ldStoreDialog').dialog({
			'autoOpen'      : false,
			'position'      : {
				'my' : 'left top',
				'at' : 'left top',
				'of' :  'body'
			},
			'resizable'     : false,
			'width'			: 1240,
			'draggable'     : false,
			'buttonsLoad' : sharedButtons.concat([
			             {
			            	'id'	 : 'openLdStoreButton',
			            	'class'  : 'defaultFocus',
			            	'text'   : LABELS.OPEN_BUTTON,
			            	'click'  : function() {
			            		var dialog = $(this),
			            			tree = dialog.dialog('option', 'ldTree'),
			            			ldNode = tree.getHighlightedNode(),
			            			learningDesignID = ldNode ? ldNode.data.learningDesignId : null;
			            		
			            		if (!learningDesignID) {
			            			learningDesignID = +$('#ldStoreDialogAccessCell > div.selected', this)
			            							   .attr('learningDesignId');
			            		}
			            		
			            		// no LD was chosen
			            		if (!learningDesignID) {
			            			alert(LABELS.SEQUENCE_NOT_SELECTED_ERROR);
			            			return;
			            		}
			            		
			            		dialog.dialog('close');
			            		GeneralLib.openLearningDesign(learningDesignID);
							}
			             }
			]),
			
			/**
			 * Button for saving the current design.
			 */
			'buttonsSave' : sharedButtons.concat([
				             {
				            	'text'   : LABELS.SAVE_BUTTON,
                                                'id'     : 'saveLdStoreButton',
				            	'click'  : function() {	
				            		var dialog = $(this),
				            			container = dialog.closest('.ui-dialog'),
				            			title = $('#ldStoreDialogNameField', container).val().trim();
				            		if (!title) {
				            			alert(LABELS.SAVE_SEQUENCE_TITLE_PROMPT);
				            			return;
				            		}
				            		
				            		if (!GeneralLib.nameValidator.test(title)) {
				            			alert(LABELS.TITLE_VALIDATION_ERROR);
				            			return;
				            		}
				            		
				            		var learningDesignID = null,
				            			folderNode = null,
				            			folderID = null,
				            			tree = dialog.dialog('option', 'ldTree'),
				            			node = tree.getHighlightedNode();
				            		if (node) {
					            		// get folder from LD tree
				            			folderNode = node.data.learningDesignId ? node.parent : node;
				            			folderID = folderNode.data.folderID;
				            		} else {
				            			// get data from "recently used sequences" list
				            			var selectedAccess = $('#ldStoreDialogAccessCell > div.selected', this);
				            			// if title was altered, do not consider this an overwrite
				            			if (selectedAccess.length > 0 && title == selectedAccess.text()) {
				            				learningDesignID = +selectedAccess.attr('learningDesignId');
				            				folderID = +selectedAccess.attr('folderID');
				            				
				            				var folders = tree.getRoot().children;
				            				if (folders) {
				            					$.each(folders, function(){
				            						if (folderID == this.data.folderID) {
				            							this.highlight();
				            							folderNode = this;
				            							return false;
				            						}
				            					});
				            				}
				            			}
				            		}
				            		
				            		if (!folderID) {
				            			// although an existing sequence can be highlighted 
				            			alert(LABELS.FOLDER_NOT_SELECTED_ERROR);
				            			return;
				            		}
				            		
			            			// if a node is highlighted but user modified the title,
			            			// it is considered a new sequence
			            			// otherwise check if there is no other sequence with the same name
				            		if (folderNode && folderNode.children) {
				            			$.each(folderNode.children, function(){
				            				if (this.label == title) {
				            					this.highlight();
				            					learningDesignID = this.data.learningDesignId;
				            					return false;
				            				}
				            			});
				            		}
				            		if (learningDesignID
				            				&& !confirm(LABELS.SEQUENCE_OVERWRITE_CONFIRM)) {
				            			return;
				            		}

				            		var result = GeneralLib.saveLearningDesign(folderID, learningDesignID, title);
				            		if (result) {
				            			dialog.dialog('close');
				            		}
								}
				             }
			]),
			
			
			/**'
			 * Button for importing activities from an existing LD.
			 */
			'buttonsImportPart' : sharedButtons.concat([
     			             {
     			            	'id'	 : 'importPartLdStoreButton',
     			            	'class'  : 'defaultFocus',
     			            	'text'   : LABELS.IMPORT_BUTTON,
     			            	'click'  : function() {
     			            		var dialog = $(this),
     			            			frameLayout = $('#ldStoreDialogImportPartFrame', dialog)[0].contentWindow.layout,
     			            			selectedActivities = [],
     			            			addActivities = [],
     			            			selectedAnnotations = [];
     			            			
     			            		
     			            		$.each(frameLayout.activities, function(){
     			            			if (this.items.selectEffect) {
     			            				selectedActivities.push(this);
     			            				dialog.dialog('option', 'importActivity')(this, addActivities);
     			            			}
     			            		});
     			            		$.each(frameLayout.regions.concat(frameLayout.labels), function(){
     			            			if (this.items.selectEffect) {
     			            				selectedAnnotations.push(this);
     			            				// unlike importActivity(), this method already takes care of UIIDs
     			            				// and adding new items to collections
     			            				dialog.dialog('option', 'importAnnotation')(this);
     			            			}
     			            		});
     			            		
     			            		if (addActivities.length == 0 && selectedAnnotations.length == 0) {
     			            			alert(LABELS.IMPORT_PART_CHOOSE_PROMPT);
     			            			return;
     			            		}
     			            		
     			            		// add child activities to containers (Optional/Parallel/Floating)
     			            		if (addActivities.length > 0) {
	     			            		$.each(addActivities, function(){
	     			            			var activity = this;
	     			            			if (activity.childActivities) {
	     			            				$.each(selectedActivities, function(){
		     			            				if (this.uiid == activity.uiid) {
		     			            					$.each(this.childActivities, function(){
		     			            						var childActivity = this;
		     			            						$.each(addActivities, function(){
		     			            							if (this.uiid == childActivity.uiid) {
		     			            								if (!activity.childActivities) {
		     			            									activity.childActivities = [];
		     			            								}
		     			            								activity.childActivities.push(this);
		     			            								// container will expand to its child activities
		     			            								activity.draw(0, 0);
		     			            							}
		     			            						});
		     			            					});
		     			            				}
		     			            			});
	     			            			}
	     			            		});
     			            			
	     			            		// put UIID specific to current LD
	     			            		$.each(addActivities, function(){
	     			            			if (this instanceof ActivityDefs.BranchingEdgeActivity) {
	     			            				if (this.isStart) {
	     			            					this.branchingActivity.uiid = ++layout.ld.maxUIID;
	     			            				}
	     			            			} else {
	     			            				this.uiid = ++layout.ld.maxUIID;
	     			            			}
	     			            		});
	     			            		
	     			            		// arrange just the new activities
	     			            		GeneralLib.arrangeActivities(addActivities);
	     			            		
	     			            		layout.activities = layout.activities.concat(addActivities);
     			            		}
     	
     			            		dialog.dialog('close');
     							}
     			           }
     		]),
			             			
			'open' : function(){
				// calculate initial height of the table and maximum height of LD list
				var documentHeight = +$(document).height();
				$('table', this).css('height', documentHeight - 120 + 'px');
				$('#ldStoreDialogTree', this).css('max-height', documentHeight - 285 + 'px');
				
				GeneralLib.showLearningDesignThumbnail();
				
				$('#leftDialogButtonContainer').remove();
				var nameContainer = $('#ldStoreDialogNameContainer'),
					leftButtonContainer = $('<div />').attr('id','leftDialogButtonContainer');
				$('input', nameContainer).val($(this).dialog('option', 'learningDesignTitle'));
				$(this).siblings('.ui-dialog-buttonpane').append(leftButtonContainer).append(nameContainer);

				$('.leftDialogButton')
				   .attr('disabled', 'disabled')
				   .button('option', 'disabled', true)
				   .appendTo(leftButtonContainer);
				$('.defaultFocus').focus();
				
				$(this).dialog('option', 'copiedResource', null);
			},
			
			/**
			 * Extracts a selected activity from another LD.
			 */
			'importActivity' : function(activity, addActivities) {
				$.each(addActivities, function(){
					if (this.uiid == activity.uiid) {
						return;
					}
				});
				
				// activities in the another LD have different clousures, so they can not be imported directly
				// they need to be recreated from a scratch with current LD being their context
				var frameWindow = $('#ldStoreDialogImportPartFrame', layout.ldStoreDialog)[0].contentWindow,
	     			frameActivities = frameWindow.layout.activities,
	     			frameActivityDefs = frameWindow.ActivityDefs,
	     			// the local activity
	     			addActivity = null;

				if (activity instanceof frameActivityDefs.BranchingEdgeActivity) {
					// add both branching edges right away
					if (activity.isStart) {
						var branchingActivity = activity.branchingActivity,
							branchingEdge = addActivity = new ActivityDefs.BranchingEdgeActivity(
								null, branchingActivity.uiid, 0, 0, branchingActivity.title, false, branchingActivity.branchingType);
						
						branchingEdge = new ActivityDefs.BranchingEdgeActivity(
								null, null, 0, 0, null, null, false, branchingEdge.branchingActivity);
						addActivities.push(branchingEdge);
						
						if (branchingActivity.branchingType == 'optional'){
							branchingEdge.branchingActivity.minOptions = branchingActivity.minOptions;
							branchingEdge.branchingActivity.maxOptions = branchingActivity.maxOptions;
						}
					}
				} else if (activity instanceof frameActivityDefs.FloatingActivity) {
					if (layout.floatingActivity) {
						return;
					}
					addActivity = new ActivityDefs.FloatingActivity(null, activity.uiid, 0, 0);
				} else if (activity instanceof frameActivityDefs.GateActivity) {
					addActivity = new ActivityDefs.GateActivity(
							null, activity.uiid, 0, 0, activity.title, activity.description, false, activity.gateType,
							activity.startTimeoffset, activity.gateActivityCompletionBased
							);
				} else if (activity instanceof frameActivityDefs.GroupingActivity) {
					addActivity = new ActivityDefs.GroupingActivity(
							null, activity.uiid, 0, 0, activity.title, false, null, null, activity.groupingType, activity.groupDivide,
							activity.groupCount, activity.learnerCount, activity.equalSizes, activity.viewLearners, null
							);
				} else if (activity instanceof frameActivityDefs.OptionalActivity) {
					addActivity = new ActivityDefs.OptionalActivity(
							null, activity.uiid, 0, 0, activity.title, false, activity.minOptions, activity.maxOptions
							);
				} else if (activity instanceof frameActivityDefs.ParallelActivity) {
					addActivity = new ActivityDefs.ParallelActivity(
							null, activity.uiid, activity.learningLibraryID, 0, 0, activity.title
							);
				} else if (activity instanceof frameActivityDefs.ToolActivity) {
					// copy tool content
					var toolContentID = null;
					// tool content ID can be null if the activity had the default content, i.e. was not edited yet
					if (activity.toolContentID) {
						$.ajax({
							cache : false,
							async : false,
							url : LAMS_URL + "authoring/author.do",
							data : {
								'method'        : 'copyToolContentPlain',
								'toolContentID' : activity.toolContentID
							},
							dataType : 'text',
							success : function(response) {
								toolContentID = response;
							}
						});
					}
					
					addActivity = new ActivityDefs.ToolActivity(
							null, activity.uiid, toolContentID, activity.toolID, activity.learningLibraryID, null, 0, 0, activity.title
							);
				}
				
				// recreate the transitions
				if (addActivity) {
					if (activity.transitions) {
						$.each(activity.transitions.from, function(){
							var transition = this;
							$.each(addActivities, function(){
								// special processing for transitions from/to branching edges
								var uiid = this instanceof ActivityDefs.BranchingEdgeActivity
								 		 ? this.branchingActivity.uiid : this.uiid,
									toUiid = transition.toActivity instanceof frameActivityDefs.BranchingEdgeActivity
										   ? transition.toActivity.branchingActivity.uiid : transition.toActivity.uiid;
								// isStart can be undefined, true or false
								if (uiid == toUiid && this.isStart == transition.toActivity.isStart) {
									ActivityLib.addTransition(addActivity, this, true, null, null,
															  transition.branch ? transition.branch.title : null);
									return false;
								}
							});
						});
						
						$.each(activity.transitions.to, function(){
							var transition = this;
							$.each(addActivities, function(){
								var uiid = this instanceof ActivityDefs.BranchingEdgeActivity
										 ? this.branchingActivity.uiid : this.uiid,
									fromUiid = transition.fromActivity instanceof frameActivityDefs.BranchingEdgeActivity
										 ? transition.fromActivity.branchingActivity.uiid : transition.fromActivity.uiid;
								if (uiid == fromUiid && this.isStart == transition.fromActivity.isStart) {
									ActivityLib.addTransition(this, addActivity, true, null, null,
															  transition.branch ? transition.branch.title : null);
									return false;
								}
							});
						});
					}

					addActivities.push(addActivity);
				}
			},
			
			/**
			 * Extracts a selected annotation from another LD.
			 */
			'importAnnotation' : function(annotation) {
				// annotations in the another LD have different clousures, so they can not be imported directly
				// they need to be recreated from a scratch with current LD being their context
				var frameWindow = $('#ldStoreDialogImportPartFrame', layout.ldStoreDialog)[0].contentWindow,
	     			frameDecorationDefs = frameWindow.DecorationDefs,
	     			box = annotation.items.shape.getBBox();

				// there are no transitions or child/parent relations, so they can be directly recreated
				if (annotation instanceof frameDecorationDefs.Region) {
					DecorationLib.addRegion(box.x, box.y, box.x2, box.y2, annotation.title, annotation.items.shape.attr('fill'));
				} else if (annotation instanceof frameDecorationDefs.Label) {
					DecorationLib.addLabel(box.x, box.y, annotation.title);
				}
			}
		});
		
		layout.dialogs.push(layout.ldStoreDialog);
		
		
		$('#ldScreenshotAuthor', layout.ldStoreDialog).load(function(){
			// hide "loading" animation
			$('.ldChoiceDependentCanvasElement', layout.ldStoreDialog).hide();
			// show the thumbnail
			$(this).show();
		});

		$('#ldStoreDialogImportPartFrame').load(function() {
			if (!$(this).attr('src')){
				return;
			}
			
		    $(this).css('visibility', 'visible').height(+$(this).contents().find('svg').attr('height') + 40);
		});
		
		// there should be no focus, just highlight
		YAHOO.widget.TreeView.FOCUS_CLASS_NAME = null;
		var tree = new YAHOO.widget.TreeView('ldStoreDialogTree');
		// store the tree in the dialog's data
		layout.ldStoreDialog.dialog('option', 'ldTree', tree);
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
			var isSaveDialog = layout.ldStoreDialog.closest('.ui-dialog').hasClass('ldStoreDialogSave');
		
			$('.leftDialogButton')
			   .attr('disabled', event.node.highlightState > 0 ? 'disabled' : null)
			   .button('option', 'disabled', event.node.highlightState > 0);

			if (!isSaveDialog && !event.node.data.learningDesignId){
				// it is a folder in load sequence dialog, highlight but stop processing
				return true;
			}
			
			var learningDesignID = event.node.highlightState == 0   ? +event.node.data.learningDesignId : null,
				title            = isSaveDialog && learningDesignID ? event.node.label : null;
			
			GeneralLib.showLearningDesignThumbnail(learningDesignID, title);
		});
		tree.subscribe('clickEvent', tree.onEventToggleHighlight);
		
		GeneralLib.updateAccess(initAccess);

		// initialise a small info dialog
		layout.infoDialog = $('<div />').attr('id', 'infoDialog').dialog({
			'autoOpen'   : false,
			'width'      : 290,
			'minHeight'  : 'auto',
			'show'       : 'fold',
			'hide'       : 'fold',
			'draggable'  : false,
			'dialogClass': 'dialog-no-title',
			'defaultPosition' : {
							my: "right top",
						    at: "right top+5px",
						    of: '#canvas'
					      }
		});
		
		layout.infoDialog.dialog('option', 'position', layout.infoDialog.dialog('option', 'defaultPosition'));
		
		layout.dialogs.push(layout.infoDialog);
		
		// license widgets init
		$('#ldDescriptionLicenseSelect').change(function(){
			var option = $('option:selected', this);
			if (option.val() == "0") {
				$('#ldDescriptionLicenseTextContainer, #ldDescriptionLicenseImage, #ldDescriptionLicenseButton').hide();
			} else {
				if (option.attr('url')) {
					$('#ldDescriptionLicenseTextContainer').hide();
					$('#ldDescriptionLicenseImage').attr('src', option.attr('pictureURL')).show();
					$('#ldDescriptionLicenseButton').show();
				} else {
					$('#ldDescriptionLicenseTextContainer').show();
					$('#ldDescriptionLicenseImage, #ldDescriptionLicenseButton').hide();
				}
			}
		});
		$('#ldDescriptionLicenseButton').click(function(){
			var option = $('#ldDescriptionLicenseSelect option:selected'),
				url = option.attr('url');
			if (url) {
				 var win = window.open(url, '_blank');
				 win.focus();
			}
		});
	}
},



/**
 * Contains main methods for Authoring window management
 */
GeneralLib = {
	// regex validators for checking user entered strings
	nameValidator   : /^[^<>^*@%$]*$/i,
	numberValidator : /^[\d\.]+$/,

	
	/**
	 * Runs the method with parameters on each item of the set.
	 */
	applyToSet : function(set, method, params) {
		set.forEach(function(item){
			item[method].apply(item, params);
		});
	},
	
	
	/**
	 * Sorts activities on canvas.
	 */
	arrangeActivities : function(activities){
		// when importing parts of another LD, activities get appended and only they get sorted
		var append = activities,
			activities = activities || layout.activities;
		
		if (activities.length == 0) {
			// no activities, nothing to do
			return;
		}
		
		if (!append && (layout.regions.length > 0 || layout.labels.length > 0)
			&& !isReadOnlyMode && !confirm(LABELS.ARRANGE_CONFIRM)) {
			return;
		}
		
		if (!isReadOnlyMode) {
			// just to refresh the state of canvas
			HandlerLib.resetCanvasMode(true);
		}
		
		var row = 0;
		if (append) {
			// find the lowest existing activity and append the new activities beneath
			$.each(layout.activities, function(){
				row = Math.max(row,
						Math.ceil((this.items.getBBox().y2 - layout.conf.arrangeVerticalPadding)
								/ layout.conf.arrangeVerticalSpace));
			});
		}
		
		// activities are arranged in a grid
		var column = 0,
			// for special cases when row needs to shifted more
			forceRowY = null,
			// check how many columns current paper can hold
			maxColumns = Math.floor((paper.attr('width') - layout.conf.arrangeHorizontalPadding)
					                 / layout.conf.arrangeHorizontalSpace),
			// the initial max length of subsequences is limited by paper space
			subsequenceMaxLength = maxColumns,
	        // a shallow copy of activities array without inner activities
			activitiesCopy = [],
			// just to speed up processing when there are only activities with no transitions left
			onlyDetachedLeft = false;
	
		$.each(activities, function(){
			if (!this.parentActivity || !(this.parentActivity instanceof DecorationDefs.Container)){
				activitiesCopy.push(this);
			}
		});
		
		// branches will not be broken into few rows; if they are long, paper will be resized
		// find the longes branch to find the new paper size
		$.each(activities, function(){
			if (this instanceof ActivityDefs.BranchingEdgeActivity && this.isStart) {
				// add start and end edges to the longest branch length in the branching
				var longestBranchLength = ActivityLib.updateBranchesLength(this.branchingActivity) + 2;
				if (longestBranchLength > subsequenceMaxLength) {
					subsequenceMaxLength = longestBranchLength;
				}
			}
		});
		
		// check how many child activities are in Floating Activity, if any
		if (layout.floatingActivity && layout.floatingActivity.childActivities.length > subsequenceMaxLength) {
				subsequenceMaxLength = childActivities.length;
		}
		
		// resize paper horizontally, if needed
		if (subsequenceMaxLength > maxColumns) {
			maxColumns = subsequenceMaxLength;
			GeneralLib.resizePaper(layout.conf.arrangeHorizontalPadding
					      + maxColumns * layout.conf.arrangeHorizontalSpace,
					      paper.height);
		}
		
		// main loop; iterate over whatever is left in the array
		while (activitiesCopy.length > 0) {
			// look for activities with transitions first; detached ones go to the very end
			var activity = null;
			if (!onlyDetachedLeft) {
				$.each(activitiesCopy, function(){
					if (this.transitions.to.length > 0) {
						activity = this;
						while (activity.transitions.to.length > 0) {
							// check if previous activity was not drawn already
							// it can happen for branching edges
							var activityLookup = activity.transitions.to[0].fromActivity;
							if (activitiesCopy.indexOf(activityLookup) == -1) {
								break;
							}
							activity = activityLookup; 
						};
						return false;
					}
				});
			}

			if (!activity) {
				// no activities with transitions left, take first detached one
				onlyDetachedLeft = true;
				activity = activitiesCopy[0];
			}
				
			// markers for complex activity processing
			var complex = null;
			
			// crawl through a sequence of activities
			while (activity) {
				if (activity instanceof ActivityDefs.BranchingEdgeActivity) {
					if (activity.isStart) {
						// draw branching edges straight away and remove them from normall processing
						var branchingActivity = activity.branchingActivity,
							start = branchingActivity.start,
							end = branchingActivity.end,
							complex = {
								end : end
							},
							// can the whole branching fit in current canvas width?
							branchingFits = column + branchingActivity.longestBranchLength + 2 <= maxColumns;
						if (!branchingFits) {
							// start branching from the left side of canvas
							row++;
							if (forceRowY) {
								while (forceRowY > layout.conf.arrangeVerticalPadding + 10 + row * layout.conf.arrangeVerticalSpace) {
									row++;
								}
								forceRowY = null;
							}
							column = 0;
						}
						// store the column of converge point
						end.column = column + branchingActivity.longestBranchLength + 1;
						
						complex.branchingRow = row + Math.floor(branchingActivity.branches.length / 2);
						// edge points go to middle of rows with branches
						var startX = layout.conf.arrangeHorizontalPadding +
									 column * layout.conf.arrangeHorizontalSpace + 54,
							edgeY = layout.conf.arrangeVerticalPadding +
									complex.branchingRow * layout.conf.arrangeVerticalSpace + 17,
							endX = layout.conf.arrangeHorizontalPadding +
								   end.column * layout.conf.arrangeHorizontalSpace + 54;
						
						activitiesCopy.splice(activitiesCopy.indexOf(start), 1);
						activitiesCopy.splice(activitiesCopy.indexOf(end), 1);
						
						// start point goes to very left, end goes wherever the longes branch ends
						start.draw(startX, edgeY);
						end.draw(endX, edgeY);
	
						complex.branchingColumn = column;
						column++;

						$.each(branchingActivity.branches, function(){
							if (this.transitionFrom.toActivity == branchingActivity.end) {
								complex.emptyBranch = this;
								return false;
							}
						});
						
						if (branchingActivity.branches.length > (complex.emptyBranch ? 1 : 0)) {
							// set up branch drawing
							// skip the first branch if it is the empty one
							complex.branchIndex =
								complex.emptyBranch == branchingActivity.branches[0] ? 1 : 0;
							// next activity for normal processing will be first one from the first branch
							activity = branchingActivity.branches[complex.branchIndex].transitionFrom.toActivity;
							continue;
						} else {
							// no branches, nothing to do, carry on with normal activity processing
							activity = complex.end;
							activity.column = null;
							complex = null;
						}
					}
				} else {
					// it is a simple activity, so redraw it
					var x = layout.conf.arrangeHorizontalPadding + column * layout.conf.arrangeHorizontalSpace,
						y = layout.conf.arrangeVerticalPadding + row * layout.conf.arrangeVerticalSpace;
					
					if (activity instanceof ActivityDefs.GateActivity) {
						// adjust placement for gate activity, so it's in the middle of its cell
						x += 57;
						y += 10;
					} else if (activity instanceof ActivityDefs.OptionalActivity){
						x -= 20;
					}
					
					activity.draw(x, y);
					// remove the activity so we do not process it twice
					activitiesCopy.splice(activitiesCopy.indexOf(activity), 1);
					
					// learn where a tall Optional Activity has its end
					// and later start drawing activities lower than in the next row
					if (activity instanceof DecorationDefs.Container && activity.childActivities.length > 1) {
						var activityEndY = activity.items.shape.getBBox().y2;
						if (!forceRowY || activityEndY > forceRowY) {
							forceRowY = activityEndY;
						}
					}
				}
				
				// find the next row and column
				column = (column + 1) % maxColumns;
				if (column == 0) {
					row++;
					// if an Optional Activity forced next activities to be drawn lower than usual
					if (forceRowY) {
						while (forceRowY > layout.conf.arrangeVerticalPadding + 10 + row * layout.conf.arrangeVerticalSpace) {
							row++;
						}
						forceRowY = null;
					}
				}
				
				// does the activity has further activities?
				if (activity.transitions.from.length > 0) {
					activity = activity.transitions.from[0].toActivity;
				} else {
					activity = null;
				}
				
				if (complex && (!activity || activity == complex.end)) {
					// end of branch
					complex.branchIndex++;

					var branches = complex.end.branchingActivity.branches;
					if (branches.length > complex.branchIndex) {
						if (branches[complex.branchIndex] == complex.emptyBranch) {
							// skip the empty branch
							complex.branchIndex++;
						}
					}
					
					if (branches.length > complex.branchIndex) {
						// there is another branch to process
						activity = branches[complex.branchIndex].transitionFrom.toActivity;
						// go back to left side of canvas and draw next branch
						row++;
						if (complex.emptyBranch && complex.branchingRow == row) {
							row++;
						}
						
						column = complex.branchingColumn + 1;
					} else {

						// no more branches, return to normal activity processing
						activity = complex.end.transitions.from.length == 0 ?
								null : complex.end.transitions.from[0].toActivity;
						column = (complex.end.column + 1) % maxColumns;
						if (column == 0) {
							row++;
						}
						if (row < complex.branchingRow) {
							row = complex.branchingRow;
						}
						complex.end.column = null;
						complex = null;
					}
				}
				
				if (!activity || activitiesCopy.indexOf(activity) == -1) {
					// next activity was already processed, so stop crawling
					break;
				}
			};
		};
		
		if (layout.floatingActivity) {
			if (column > 0) {
				// if the last activity was in the last column, there is no need for another row
				row++;
				column = 0;
			}
			var x = layout.conf.arrangeHorizontalPadding,
				y = layout.conf.arrangeVerticalPadding - 30 + row * layout.conf.arrangeVerticalSpace;
			
			layout.floatingActivity.draw(x, y);
		}
		
		// redraw transitions one by one
		$.each(activities, function(){
			$.each(this.transitions.from.slice(), function(){
				ActivityLib.addTransition(this.fromActivity, this.toActivity, true);
			});
		});
		
		GeneralLib.resizePaper();
		GeneralLib.setModified(true);
	},
	
	
	/**
	 * Tells the backend to remove the system gate.
	 */
	cancelLiveEdit : function(){
		if (GeneralLib.canClose() || confirm(LABELS.LIVEEDIT_CANCEL_CONFIRM)) {
			$.ajax({
				type  : 'POST',
				async : false,
				cache : false,
				url : LAMS_URL + 'authoring/author.do',
				data : {
					'method' : 'finishLearningDesignEdit',
					'learningDesignID' : layout.ld.learningDesignID,
					'cancelled' : 'true'
				},
				success : function() {
					GeneralLib.setModified(false);
					window.parent.closeDialog('dialogFlashlessAuthoring');
				}
			});
		}
	},
	
	
	canClose : function(){
		return !(layout.modified &&
			(layout.activities.length > 0
			|| layout.regions.length > 0
			|| layout.labels.length > 0
			|| layout.floatingActivity));
	},
	
	/**
	 * Escapes HTML tags to prevent XSS injection.
	 */
	escapeHtml : function(unsafe) {
	    return unsafe
	        .replace(/&/g, "&amp;")
	        .replace(/</g, "&lt;")
	        .replace(/>/g, "&gt;")
	        .replace(/"/g, "&quot;")
	        .replace(/'/g, "&#039;");
	},
	

	/**
	 * Removes existing activities and prepares canvas for a new sequence.
	 */
	newLearningDesign : function(force, soft){
		// force means that user should not be asked for confirmation.
		if (!force && (layout.activities.length > 0
					  || layout.regions.length > 0
					  || layout.labels.length > 0
					  || layout.floatingActivity)
				&& !confirm(LABELS.CLEAR_CANVAS_CONFIRM)){
			return;
		}
		
		$('#ldDescriptionDetails').slideUp();
		
		// soft means that data is manually reset, instead of simply reloading the page.
		if (soft) {
			layout.ld = {
				'maxUIID' : 0,
				'designType' : null
			};
			layout.activities = [];
			layout.regions = [];
			layout.labels = [];
			layout.floatingActivity = null;
			
			if (!isReadOnlyMode) {
				$('#ldDescriptionFieldTitle').text('Untitled');
				var editor = CKEDITOR.instances['ldDescriptionFieldDescription'];
				editor.once('dataReady', function(){
					// do nothing
					// this listener is just for saveLearningDesign() to detect that data was already cleared
				});
				editor.setData(null);
				$('#ldDescriptionLicenseSelect').val(0);
				$('#ldDescriptionLicenseText').text('');
				GeneralLib.setModified(true);
			}
			
			if (paper) {
				paper.clear();
			} else {
				// need to set size right away for Chrome
				paper = Snap(canvas.width() - 5, canvas.height() - 5);
				canvas.append(paper.node);
			}
			// initialise filter for read-only activities in Live Edit
			layout.conf.readOnlyFilter = paper.filter(Snap.filter.grayscale(1));
			
			GeneralLib.resizePaper();
		} else {
			// do not prompt again
			window.onbeforeunload = null;
			// full window reload so new content ID gets generated
			document.location.href = LAMS_URL + 'authoring/author.do?method=openAuthoring';
		}
	},
	
	
	/**
	 * Replace current canvas contents with the loaded sequence.
	 */
	openLearningDesign : function(learningDesignID) {
		// get LD details
		$.ajax({
			cache : false,
			async : false,
			url : LAMS_URL + "authoring/author.do",
			dataType : 'json',
			data : {
				'method'          : 'openLearningDesign',
				'learningDesignID': learningDesignID
			},
			success : function(response) {
				if (!response) {
					if (!isReadOnlyMode) {
						alert(LABELS.SEQUENCE_LOAD_ERROR);
					}
					return;
				}
				
				var ld = response.ld;
				
				// remove existing activities
				GeneralLib.newLearningDesign(true, true);
				layout.ld = {
					'learningDesignID' : learningDesignID,
					'folderID'		   : ld.workspaceFolderID,
					'contentFolderID'  : ld.contentFolderID,
					'title'			   : ld.title,
					'maxUIID'		   : 0,
					'designType'	   : ld.designType
				};
				
				if (!isReadOnlyMode) {
					$('#ldDescriptionFieldTitle').html(GeneralLib.escapeHtml(ld.title));
					var editor = CKEDITOR.instances['ldDescriptionFieldDescription'];
					if (editor.hasListeners('dataReady')) {
						// data was not cleared yet by newLearningDesign, so queue the current change
						editor.once('dataReady', function(eventInfo){
							editor.setData(ld.description);
						});
					} else {
						// data was already cleared, so just set the description
						editor.setData(ld.description);
					}
					
					if (ld.licenseID) {
						$('#ldDescriptionLicenseSelect').val(ld.licenseID || 0).change();
						$('#ldDescriptionLicenseText').text(ld.licenseText);
					}
				}
				
				var arrangeNeeded = false,
					// if system gate is found, it is Live Edit
					systemGate = null,
					branchToBranching = {},
					// helper for finding last activity in a branch
					branchToActivityDefs = {};
				
				// create visual representation of the loaded activities
				$.each(ld.activities, function() {
					var activityData = this,
						activity = null;
					
					// find max uiid so newly created elements have unique ones
					if (activityData.activityUIID && layout.ld.maxUIID < activityData.activityUIID) {
						layout.ld.maxUIID = activityData.activityUIID;
					}

					switch(activityData.activityTypeID) {
						// Tool Activity
						case 1 :
							activity = new ActivityDefs.ToolActivity(
											activityData.activityID,
											activityData.activityUIID,
											activityData.toolContentID,
											activityData.toolID,
											activityData.learningLibraryID,
											LAMS_URL + activityData.authoringURL
													 + '?toolContentID='   + activityData.toolContentID
													 + '&contentFolderID=' + layout.ld.contentFolderID,
											activityData.xCoord ? activityData.xCoord : 1,
											activityData.yCoord ? activityData.yCoord : 1,
											activityData.activityTitle,
											activityData.readOnly);
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
									
									// sort groups by asceding UIID
									groups.sort(function(a,b) {
										return a.uiid - b.uiid;
									});
									
									activity = new ActivityDefs.GroupingActivity(
											activityData.activityID,
											activityData.activityUIID,
											activityData.xCoord,
											activityData.yCoord,
											activityData.activityTitle,
											activityData.readOnly,
											groupingData.groupingID,
											groupingData.groupingUIID,
											groupingType,
											groupingData.learnersPerGroup ? 'learners' : 'groups',
											groupingType == 'monitor' ? 
													groupingData.maxNumberOfGroups : groupingData.numberOfGroups,
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
						case 5: var gateType = gateType || 'permission';
						case 9: var gateType = gateType || 'system';
						case 14:
							var gateType = gateType || 'condition';
							activity = new ActivityDefs.GateActivity(
								activityData.activityID,
								activityData.activityUIID,
								activityData.xCoord,
								activityData.yCoord,
								activityData.activityTitle,
								activityData.description,
								activityData.readOnly,
								gateType,
								activityData.gateStartTimeOffset,
								activityData.gateActivityCompletionBased);
							
							if (gateType == 'system'){
								systemGate = activity;
							};
							break;

						// Parallel Activity
						case 6:
							activity = new ActivityDefs.ParallelActivity(
									activityData.activityID,
									activityData.activityUIID,
									activityData.learningLibraryID,
									activityData.xCoord,
									activityData.yCoord,
									activityData.activityTitle,
									activityData.readOnly);
							// for later reference
							activityData.activity = activity;
							// for later reference
							activityData.activity = activity;
							break;
							
						// Optional Activity
						case 7:
							activity = new ActivityDefs.OptionalActivity(
									activityData.activityID,
									activityData.activityUIID,
									activityData.xCoord,
									activityData.yCoord,
									activityData.activityTitle,
									activityData.readOnly,
									activityData.minOptions,
									activityData.maxOptions);
							break;
							
						// Branching Activity
						case 10: var branchingType = 'chosen';
						case 11: var branchingType = branchingType || 'group';
						case 12: var branchingType = branchingType || 'tool';
						case 13:
							// draw both edge points straight away and mark the whole canvas for auto reaarange,
							// re-arrange only if it is old SVG being converted into new one
							arrangeNeeded |= activityData.xCoord && activityData.yCoord;
							var branchingType = branchingType || 'optional',
								branchingEdge = new ActivityDefs.BranchingEdgeActivity(activityData.activityID,
										activityData.activityUIID,
										arrangeNeeded ? 0 : activityData.startXCoord,
										arrangeNeeded ? 0 : activityData.startYCoord,
										activityData.activityTitle,
										activityData.readOnly,
										branchingType);
							layout.activities.push(branchingEdge);
							// for later reference
							activityData.activity = branchingEdge;
							activity = branchingEdge.branchingActivity;
							
							branchingEdge = new ActivityDefs.BranchingEdgeActivity(
									null, null,
									arrangeNeeded ? 0 : activityData.endXCoord,
									arrangeNeeded ? 0 : activityData.endYCoord,
									null, null, null,
									branchingEdge.branchingActivity);
							layout.activities.push(branchingEdge);
							
							branchingEdge.branchingActivity.defaultActivityUIID = activityData.defaultActivityUIID;
							if (branchingType == 'optional'){
								branchingEdge.branchingActivity.minOptions = activityData.minOptions;
								branchingEdge.branchingActivity.maxOptions = activityData.maxOptions;
							}

							break;
						
						// Branch (i.e. Sequence Activity)
						case 8:
							var branches = branchToBranching[activityData.parentActivityID];
							if (!branches) {
								branches = branchToBranching[activityData.parentActivityID] = [];
							}
							branches.push(new ActivityDefs.BranchActivity(activityData.activityID,
																		 activityData.activityUIID,
																		 activityData.activityTitle));
							
							var branchData = branchToActivityDefs[activityData.activityID];
							if (!branchData) {
								branchData = branchToActivityDefs[activityData.activityID] = {};
							}
							branchData.stopAfterActivity = activityData.stopAfterActivity;
							
							break;
							
						// Support (Floating) activity
						case 15:
							activity = new ActivityDefs.FloatingActivity(
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
					
					if (!(activity instanceof ActivityDefs.FloatingActivity)
						&& !(activity instanceof ActivityDefs.BranchingActivity)) {
						layout.activities.push(activity);
					}
					
					// store information about the branch the activity belongs to
					if (activityData.parentActivityID) {
						var branchData = branchToActivityDefs[activityData.parentActivityID];
						if (branchData) {
							if (!branchData.lastActivityOrderID || activityData.orderID > branchData.lastActivityOrderID) {
								// is it the last activity in the branch?
								branchData.lastActivityOrderID = activityData.orderID;
								branchData.lastActivity = activity;
							}
						} else {
							branchData = branchToActivityDefs[activityData.parentActivityID] = {
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
						if (this instanceof ActivityDefs.BranchingEdgeActivity
								&& this.branchingActivity.id == branchingID){
							var branchingActivity = this.branchingActivity;
							branchingActivity.branches = branches;
							var defaultBranchSet = false;
							$.each(branches, function(){
								this.branchingActivity = branchingActivity;
								if (branchingActivity.defaultActivityUIID == this.uiid && !defaultBranchSet){
									this.defaultBranch = true;
									defaultBranchSet = true;
								}
							});
							
							branchingActivity.defaultActivityUIID = null;
							if (!defaultBranchSet && branches.length > 0) {
								branches[0].defaultBranch = true;
							}
							return false;
						}
					});
				});
				
				
				// apply existing groupings and parent-child references to activities 
				$.each(ld.activities, function(){
					var activityData = this,
						activity = this.activity;
					
					if (activity) {
						if (layout.floatingActivity && layout.floatingActivity.id == activityData.parentActivityID) {
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
										&& (this instanceof ActivityDefs.ParallelActivity
											|| this instanceof ActivityDefs.OptionalActivity)) {
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
				
				// apply existing groupings 
				$.each(ld.activities, function(){
					var activityData = this,
						activity = this.activity;
					
					if (activity && activityData.applyGrouping) {
						$.each(layout.activities, function(){
							if (this instanceof ActivityDefs.GroupingActivity
									&& this.groupingID == activityData.groupingID) {
								var grouping = this;
								// add reference and redraw the grouped activity
								if (activity instanceof ActivityDefs.BranchingEdgeActivity) {
									activity.branchingActivity.grouping = grouping;
								} else if (activity instanceof ActivityDefs.ParallelActivity) {
									$.each(activity.childActivities, function(){
										this.grouping = grouping;
									});
									activity.grouping = grouping;
									activity.draw();
								} else {
									activity.grouping = grouping;
									activity.draw();
								}
								return false;
							}
						});
					}
				});
				
				// apply group -> branch mappings
				$.each(ld.branchMappings, function(){
					var entry = this,
						input = null,
						group = null,
						branch = null,
						gate = null;
					$.each(layout.activities, function(){
						// is it the branch we're looking for?
						if (this instanceof ActivityDefs.BranchingEdgeActivity && this.isStart) {
							$.each(this.branchingActivity.branches, function(){
								if (entry.sequenceActivityUIID == this.uiid) {
									branch = this;
									return false;
								}
							});
						// is it the grouping we're looking for?
						} else if (this instanceof ActivityDefs.GroupingActivity) {
							$.each(this.groups, function(){
								if (entry.groupUIID == this.uiid) {
									group = this;
									return false;
								}
							});
						}
						// is it the gate we're looking for
						else if (this instanceof ActivityDefs.GateActivity && entry.gateActivityUIID == this.uiid) {
							gate = this;
						} else if (entry.condition && entry.condition.toolActivityUIID == this.uiid) {
							input = this;
						}
						
						// found both, no need to continue iteration
						if ((gate || branch) && (input || group)) {
							return false;
						}
					});
					
					if (group) {
						if (branch) {
							branch.branchingActivity.groupsToBranches.push({
								'id'	 : entry.entryID,
								'uiid'   : entry.entryUIID,
								'group'  : group,
								'branch' : branch
							});
						}
					} else if (input) {
						if (branch) {
							branch.branchingActivity.input = input;
							branch.branchingActivity.conditionsToBranches.push({
								'id'	    : entry.entryID,
								'uiid'      : entry.entryUIID,
								'condition' : entry.condition,
								'branch'    : branch
							});
						} else if (gate) {
							gate.input = input;
							gate.conditionsToBranches.push({
								'id'	    : entry.entryID,
								'uiid'      : entry.entryUIID,
								'condition' : entry.condition,
								'branch'    : entry.gateOpenWhenConditionMet ? 'open' : 'closed'
							});
						}
					}
				});
				
				// draw starting and ending transitions in branches
				$.each(layout.activities, function(){
					if (this instanceof ActivityDefs.BranchingEdgeActivity && this.isStart) {
						var branchingActivity = this.branchingActivity,
							branches = branchingActivity.branches.slice();
						branchingActivity.branches = [];
						
						$.each(branches, function(){
							var branch = this,
								branchData = branchToActivityDefs[branch.id],
								firstActivity = branchData.firstActivity || branchingActivity.end,
								lastActivity = branchData.stopAfterActivity ? null : branchData.lastActivity;
							
							// check for nested branching
							if (firstActivity instanceof ActivityDefs.BranchingActivity) {
								firstActivity = firstActivity.start;
							}
							if (lastActivity && lastActivity instanceof ActivityDefs.BranchingActivity){
								lastActivity = lastActivity.end;
							}
							
							// add reference to the transition inside branch
							ActivityLib.addTransition(branchingActivity.start, firstActivity, true, null, null, branch);
							if (lastActivity) {
								ActivityLib.addTransition(lastActivity, branchingActivity.end, true);
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
							isBranching = activity instanceof ActivityDefs.BranchingEdgeActivity;
						
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
							// find max uiid so newly created elements have unique ones
							if (transition.transitionUIID && layout.ld.maxUIID < transition.transitionUIID) {
								layout.ld.maxUIID = transition.transitionUIID;
							}
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
				 	GeneralLib.arrangeActivities();
				} else {
					GeneralLib.resizePaper();
				}


				if (systemGate) {
					// if system gate exists, it is Live Edit
					layout.liveEdit = true;
					
					// remove unnecessary buttons, show Cancel, move Open after Save and Cancel
					$('#newButton, #importSequenceButton, #saveAsButton, #exportLamsButton, #exportImsButton, #previewButton').remove();
					$('#cancelLiveEditButton').show()
											  .after($('#openButton').parent().parent());
				}
				
				GeneralLib.setModified(false);
				GeneralLib.updateAccess(response.access);
				
				if (!ld.validDesign && !isReadOnlyMode) {
					layout.infoDialog.html(LABELS.SEQUENCE_NOT_VALID)
									 .dialog('open');
					
					setTimeout(function(){
						layout.infoDialog.text('').dialog('close');
					}, 5000);
				}
			}
		});
	},
	
	
	/**
	 * Sets new paper dimensions and moves some static elements.
	 */
	resizePaper : function(width, height) {
		if (!paper) {
			return;
		}
		
		var windowHeight = $(window).height();
		// height of window minus toolbar, padding...
		$('.templateContainer').height(windowHeight - 81);
		$('#canvas').height(windowHeight - 75)
		// width of window minus templates on the left; minimum is toolbar width so it does not collapse
					.width(Math.max($('#toolbar').width() - 160, $(window).width() - 170));
		
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
		height = Math.max(height + (isReadOnlyMode ? 20 : 50), canvas.height()) - 20;
		
		paper.attr({
			'width'  : width, 
			'height' : height
		});

		if (!isReadOnlyMode){
			if (layout.bin) {
				layout.bin.remove();
			}
			
			// draw rubbish bin on canvas
			layout.bin = paper.image(LAMS_URL + layout.iconLib.bin, width - 55, height - 55, 48, 48);
			// so it can be found when SVG code gets cloned
			$(layout.bin.node).attr('id', 'rubbishBin');
			
			HandlerLib.resetCanvasMode(true);
		}
	},
	
	
	/**
	 * Stores the sequece in database.
	 */
	saveLearningDesign : function(folderID, learningDesignID, title) {
		var activities = [],
			transitions = [],
			groupings = [],
			branchMappings = [],
			annotations = [],
			layoutActivityDefs = [],
			systemGate = null,
			title = title.trim(),
			description = CKEDITOR.instances['ldDescriptionFieldDescription'].getData(),
			// final success/failure of the save
			result = false,
			error = null;
		
		$.each(layout.activities, function(){
			if (this.parentActivity	&& (this.parentActivity instanceof ActivityDefs.BranchingActivity
							|| this.parentActivity instanceof ActivityDefs.BranchActivity)){
				// remove previously set parent activities as they will be re-set from the start
				this.parentActivity = null;
				this.orderID = null;
			}
		});
		
		$.each(layout.activities, function(){
			// add all branch activities for iteration and saving
			if (this instanceof ActivityDefs.BranchingEdgeActivity){
				var branchingActivity = this.branchingActivity;
				if (this.isStart){
					branchingActivity.defaultActivityUIID = null;
					layoutActivityDefs.push(branchingActivity);
					
					$.each(branchingActivity.branches, function(branchOrderID){
						if (!branchingActivity.defaultActivityUIID && this.defaultBranch) {
							branchingActivity.defaultActivityUIID = this.uiid;
						}
						this.defaultActivityUIID = null;
						this.orderID = branchOrderID + 1;
						this.parentActivity = branchingActivity;
						this.stopAfterActivity = false;
						layoutActivityDefs.push(this);
						
						var childActivity = this.transitionFrom.toActivity,
							orderID = 1;
						while (!(childActivity instanceof ActivityDefs.BranchingEdgeActivity
								&& !childActivity.isStart
								&& childActivity.branchingActivity == branchingActivity)) {
							if (childActivity instanceof ActivityDefs.BranchingEdgeActivity) {
								// it's a nested branching
								childActivity = childActivity.branchingActivity;
							}
							
							childActivity.parentActivity = this;
							childActivity.orderID = orderID;
							if (orderID == 1){
								this.defaultActivityUIID = childActivity.uiid;
							}
							orderID++;
							
							if (childActivity instanceof ActivityDefs.BranchingActivity){
								// it is a nested branching, so move to the end
								childActivity = childActivity.end;
							}
							if (childActivity.transitions.from.length == 0) {
								this.stopAfterActivity = true;
								break;
							}
							childActivity = childActivity.transitions.from[0].toActivity;
						}
					});
					
					if (!branchingActivity.defaultActivityUIID && branchingActivity.branches.length > 0) {
						branchingActivity.defaultActivityUIID = branchingActivity.branches[0].uiid;
						branchingActivity.branches[0].defaultBranch = true;
					}
				} else {
					// validate if all branches start and end in the same branching
					$.each(this.transitions.to, function(){
						error = true;
						var activity = this.fromActivity;
						do {
							if (activity instanceof ActivityDefs.BranchingEdgeActivity) {
								if (activity.isStart) {
									if (branchingActivity == activity.branchingActivity) {
										// found out that the branch starts in the proper point
										error = false;
										break;
									}
									// the branch is shared between two branchings
									// it should have been detected when adding a transition
									alert(LABELS.CROSS_BRANCHING_ERROR);
									return false;
								}
								// a nested branching encountered when crawling, just jump over it
								activity = activity.branchingActivity.start;
							}
							
							// keep crawling
							if (activity.transitions && activity.transitions.to.length > 0) {
								activity = activity.transitions.to[0].fromActivity;
							} else {
								activity = null;
							}
						} while (activity);
						
						if (error) {
							alert(branchingActivity.title + LABELS.END_MATCH_ERROR);
							return false;
						}
					});
					
					if (error) {
						return false;
					}
				}
			} else {
				layoutActivityDefs.push(this);
			}
		});
		
		if (error) {
			return false;
		}
		
		if (layout.floatingActivity){
			layoutActivityDefs.push(layout.floatingActivity);
		}
		
		$.each(layoutActivityDefs, function(){
			var activity = this,
				activityBox = activity.items ? activity.items.shape.getBBox() : null,
				x = activityBox ? parseInt(activityBox.x) : null,
				y = activityBox ? parseInt(activityBox.y) : null,
				activityTypeID = null,
				activityCategoryID = activity instanceof ActivityDefs.ToolActivity ?
						   			 layout.toolMetadata[activity.learningLibraryID].activityCategoryID : 
						             activity instanceof ActivityDefs.ParallelActivity ? 5 : 1,
				iconPath = null,
				isGrouped = activity.grouping ? true : false,
				parentActivityID = activity.parentActivity ? activity.parentActivity.id : null;
			
			if (activity.toolID) {
				activityTypeID = 1;
				// find out what is the icon for tool acitivty
				var templateIcon = $('.template[learningLibraryId=' + activity.learningLibraryID +'] img');
				if (templateIcon.width() > 0) {
					 iconPath = layout.toolMetadata[activity.learningLibraryID].iconPath;
				}
			}
			// translate activity type to back-end understandable
			else if (activity instanceof ActivityDefs.GroupingActivity){
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
					'learnersPerGroup' 				: activity.groupDivide == 'learners' ? activity.learnerCount : null,
					'equalNumberOfLearnersPerGroup' : activity.groupingType == 'learner' && activity.groupDivide == 'groups' 
													  ? activity.equalSizes : null,
					'viewStudentsBeforeSelection'   : activity.groupingType == 'learner' ? activity.viewLearners : 0,
					'maxNumberOfGroups' 			: activity.groupingType == 'monitor' ? activity.groupCount : null,
					'numberOfGroups' 				: activity.groupingType != 'monitor' && activity.groupDivide == 'groups'
													  ? activity.groupCount : null,
					'groups' 						: groups
				});
				
			} else if (activity instanceof  ActivityDefs.GateActivity){
				switch(activity.gateType) {
					case 'sync'       : activityTypeID = 3; break;
					case 'schedule'   : activityTypeID = 4; break;
					case 'permission' : activityTypeID = 5; break;
					case 'system' 	  : activityTypeID = 9; systemGate = activity; break;
					case 'condition'  :
						activityTypeID = 14;
							
						if (activity.input) {
							$.each(activity.conditionsToBranches, function(index){
								if (!this.branch) {
									return true;
								}
								
								var condition = this.condition;
								if (condition) {
									condition.orderID = index + 1;
									if (condition.exactMatchValue) {
										condition.startValue = condition.endValue = null;
									}
								}
								
								branchMappings.push({
									'entryID'			       : this.id,
									'entryUIID'			       : this.uiid,
									'gateActivityUIID'	   	   : activity.uiid,
									'gateOpenWhenConditionMet' : this.branch == 'open',
									'condition'			       : condition
								});
							});
						}
						
						break;
				}
			} else if (activity instanceof ActivityDefs.ParallelActivity) {
				activityTypeID = 6;
			} else if (activity instanceof ActivityDefs.OptionalActivity) {
				activityTypeID = 7;
			} else if (activity instanceof ActivityDefs.BranchingActivity) {
				activityBox = activity.start.items.shape.getBBox();
				
				switch(activity.branchingType) {
					case 'chosen' : activityTypeID = 10; break;
					case 'group'  :
						activityTypeID = 11;
						var branchMappingCopy = activity.groupsToBranches.slice(),
							branchMapping = activity.groupsToBranches = [];
						// no break, so fall to 'tool'
					case 'tool' :
						activityTypeID = activityTypeID  || 12;
							
						if (activity.defaultActivityUIID && (activityTypeID == 11 || activity.input)) {
							var branchMappingCopy = branchMappingCopy || activity.conditionsToBranches.slice(),
								// yes, yes, a lousy construction
								branchMapping = branchMapping || (activity.conditionsToBranches = []);
							
							$.each(branchMappingCopy, function(index){
								if (activity.branches.indexOf(this.branch) == -1){
									return true;
								}
								if (this.group && activity.grouping.groups.indexOf(this.group) == -1) {
									return true;
								}
								
								var condition = this.condition;
								if (condition) {
									condition.orderID = index + 1;
									if (condition.exactMatchValue) {
										condition.startValue = condition.endValue = null;
									}
								}
								
								branchMappings.push({
									'entryID'			   : this.id,
									'entryUIID'			   : this.uiid,
									'branchingActivityUIID': this.branch.branchingActivity.uiid,
									'sequenceActivityUIID' : this.branch.uiid,
									'groupUIID' 		   : this.group ? this.group.uiid : null,
									'condition'			   : condition
								});
								
								branchMapping.push(this);
							});
						}
						
						break;
					case 'optional' : activityTypeID = 13; break;
				}
			} else if (activity instanceof ActivityDefs.BranchActivity){
				activityTypeID = 8;
			} else if (activity instanceof ActivityDefs.FloatingActivity){
				activityTypeID = 15;
			}
			
			if (activity.parentActivity && activity.parentActivity instanceof DecorationDefs.Container){
				// positions are relative to parent container
				var activityBox = activity.parentActivity.items.getBBox();
				x -= activityBox.x;
				y -= activityBox.y;
			}
						
			// add activity
			activities.push({
				'activityID' 			 : activity.id,
				'activityUIID' 			 : activity.uiid,
				'toolID' 				 : activity.toolID,
				'learningLibraryID' 	 : activity.learningLibraryID,
				'toolContentID' 	 	 : activity.toolContentID,
				'stopAfterActivity' 	 : false,
				'groupingSupportType' 	 : 2,
				'applyGrouping' 		 : isGrouped,
				'groupingUIID'			 : isGrouped ? activity.grouping.groupingUIID : null,
			    'createGroupingUIID'	 : activity instanceof ActivityDefs.GroupingActivity ? activity.groupingUIID : null,
				'parentActivityID' 		 : activity.parentActivity ? activity.parentActivity.id : null,
				'parentUIID' 			 : activity.parentActivity ? activity.parentActivity.uiid : null,
				'libraryActivityUIImage' : iconPath,
				'xCoord' 				 : x,
				'yCoord' 				 : y,
				'startXCoord'			 : activity instanceof ActivityDefs.BranchingActivity ?
												parseInt(activity.start.items.shape.getBBox().x) : null,
				'startYCoord'			 : activity instanceof ActivityDefs.BranchingActivity ?
												parseInt(activity.start.items.shape.getBBox().y) : null,
				'endXCoord'			 	 : activity instanceof ActivityDefs.BranchingActivity ?
												parseInt(activity.end.items.shape.getBBox().x) : null,
				'endYCoord'			 	 : activity instanceof ActivityDefs.BranchingActivity ?
												parseInt(activity.end.items.shape.getBBox().y) : null,
				'activityTitle' 		 : activity.title,
				'description'			 : activity.description,
				'activityCategoryID' 	 : activityCategoryID,
				'activityTypeID'     	 : activityTypeID,
				'orderID'				 : activity.orderID,
				'defaultActivityUIID'    : activity.defaultActivityUIID,
				'gateStartTimeOffset'	 : activity.gateType == 'schedule' ?
											activity.offsetDay*24*60 + activity.offsetHour*60 + activity.offsetMinute : null,
				'gateActivityCompletionBased' : activity.gateActivityCompletionBased,
				'gateActivityLevelID'    : activity instanceof ActivityDefs.GateActivity ? 1 : null,
				'minOptions'			 : activity.minOptions || null,
				'maxOptions'			 : activity.maxOptions || null,
				'stopAfterActivity'		 : activity.stopAfterActivity ? true : false,
				'toolActivityUIID'		 : activity.input ? activity.input.uiid : null,
				'gradebookToolOutputDefinitionName' : activity.gradebookToolOutputDefinitionName
			});

			var activityTransitions = activity instanceof ActivityDefs.BranchingActivity ?
					activity.end.transitions : activity.transitions;
			
			if (activityTransitions) {
				// iterate over transitions and create a list
				$.each(activityTransitions.from, function(){
					var transition = this,
						toActivity = transition.toActivity;
					if (toActivity instanceof ActivityDefs.BranchingEdgeActivity) {
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
				isRegion = this instanceof DecorationDefs.Region;
			
			annotations.push({
				'id'			 : this.id,
				'annotationUIID' : this.uiid,
				'title' 		 : this.title,
				'xCoord'    	 : parseInt(box.x),
				'yCoord'    	 : parseInt(box.y),
				'endXCoord' 	 : isRegion ? parseInt(box.x2) : null,
				'endYCoord' 	 : isRegion ? parseInt(box.y2) : null,
				'color'	    	 : isRegion ? Snap.color(this.items.shape.attr('fill')).hex : null
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
			'licenseID'			 : $('#ldDescriptionLicenseSelect').val(),
			'licenseText'   	 : $('#ldDescriptionLicenseSelect').val() == "0"
								   || $('#ldDescriptionLicenseSelect option:selected').attr('url')
								   ? null : $('#ldDescriptionLicenseText').val(),
			'designType'		 : layout.ld.designType,
			'activities'		 : activities,
			'transitions'		 : transitions,
			'groupings'			 : groupings,
			'branchMappings'     : branchMappings,
			'annotations'		 : annotations,
			
			'helpText'           : null,
			'duration'			 : null,
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
					var message = LABELS.SEQUENCE_VALIDATION_ISSUES + '\n';
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
					$('#ldDescriptionFieldTitle').html(GeneralLib.escapeHtml(title));
					
					// assign database-generated properties to activities
					$.each(response.ld.activities, function() {
						var updatedActivity = this;
						$.each(layout.activities, function(){
							var isBranching = this instanceof ActivityDefs.BranchingEdgeActivity,
								found = false;
							if (isBranching && !this.isStart) {
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
										existingTransition.id = +this.transitionID || null;
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
					
					if (layout.liveEdit) {
						// let backend know that system gate needs to be removed
						$.ajax({
							type  : 'POST',
							async : false,
							cache : false,
							url : LAMS_URL + 'authoring/author.do',
							data : {
								'method' : 'finishLearningDesignEdit',
								'learningDesignID' : layout.ld.learningDesignID,
								'cancelled' : 'false'
							},
							success : function() {
								// prepare for LD image generate
								// remove system gate from the SVG
								var fromActivity = null,
									toActivity = null,
									transitionUIID = null;
								if (systemGate.transitions.from.length > 0 && systemGate.transitions.to.length > 0){
									var toTransition = systemGate.transitions.to[0];
									transitionUIID = toTransition.uiid;
									toActivity = systemGate.transitions.from[0].toActivity;
									fromActivity = toTransition.fromActivity;
								}
								
								ActivityLib.removeActivity(systemGate, true);
								if (fromActivity && toActivity) {
									ActivityLib.addTransition(fromActivity, toActivity, null, transitionUIID);
								}
								
								// draw all activities as writable
								$.each(layout.activities, function(){
									this.readOnly = false;
									this.draw();
								});
								
								// set as not modified so dialog will not prompt user on close
								GeneralLib.setModified(false);
								// create the updated LD image
								GeneralLib.saveLearningDesignImage();
								
								// close the Live Edit dialog
								alert('Changes were successfully applied.');
								window.parent.closeDialog('dialogFlashlessAuthoring');
							}
						});
						
						// if it is Live Edit, exit
						return;
					}
					
					GeneralLib.saveLearningDesignImage();
					
					if (response.validation.length == 0) {
						alert(LABELS.SAVE_SUCCESSFUL);
					}
					
					result = true;
					GeneralLib.setModified(false);
				}
				
				GeneralLib.updateAccess(response.access);
			},
			error : function(){
				alert(LABELS.SEQUENCE_SAVE_ERROR);
			}
		});
		
		return result;
	},
	
	
	/**
	 * Stores SVG LD thumbnail on server.
	 */
	saveLearningDesignImage : function() {
		$.ajax({
			type : 'POST',
			url : LAMS_URL + 'authoring/author.do',
			data : {
				'method' : 'saveLearningDesignImage',
				'learningDesignID' : layout.ld.learningDesignID,
				'extension' : 'SVG',
				'image' : MenuLib.exportSVG()
			}
		});
	},
	

	/**
	 * Tells that current sequence was modified and not saved.
	 */
	setModified : function(modified) {
		if (isReadOnlyMode){
			return;
		}
		layout.modified = modified;
		var activitiesExist = layout.activities.length > 0,
			enableExportButton = false;
		if (!modified && activitiesExist) {
			$('#previewButton').attr('disabled', null)
						   	   .button('option', 'disabled', false);
			$('.exportSequenceButton').attr('disabled', null)
								  	  .css('opacity', 1);
			$('#ldDescriptionFieldModified').text('');
			enableExportButton = true;
		} else {
			$('#previewButton').attr('disabled', 'disabled')
			   				   .button('option', 'disabled', true);
			$('.exportSequenceButton').attr('disabled', 'disabled')
							      	  .css('opacity', 0.2);
			$('#ldDescriptionFieldModified').text('*');
		}
		
		// if the browser does not support HTML5 "download" attribute,
		// the image can only be received from the LD saved on the server
		if ((!modified || layout.conf.supportsDownloadAttribute) && activitiesExist) {
			$('.exportImageButton').attr('disabled', null)
			  					   .css('opacity', 1);
			enableExportButton = true;
		} else {
			$('.exportImageButton').attr('disabled', 'disabled')
			   					   .css('opacity', 0.2);
		}
		
		// disabled the whole export button if all children are disabled
		if (enableExportButton) {
			$('#exportButton').attr('disabled', null)
		  	  				  .css('opacity', 1);
		} else {
			$('#exportButton').attr('disabled', 'disabled')
	      	  				  .css('opacity', 0.2);
		}
	},
	
	
	/**
	 * Displays sequence image in Open/Save dialog.
	 */
	showLearningDesignThumbnail : function(learningDesignID, title) {
		// display "loading" animation and finally LD thumbnail
		$('.ldChoiceDependentCanvasElement').hide();
		// the "import part" frame can't use "display:none" CSS attribute as its JS won't execute right
		$('#ldStoreDialogImportPartFrame', layout.ldStoreDialog).css('visibility', 'hidden');
		
		if (learningDesignID) {
			if (layout.ldStoreDialog.closest('.ui-dialog').hasClass('ldStoreDialogImportPart')) {
				// get read-only Authoring of the chosen LD and prevent caching
				$('#ldStoreDialogImportPartFrame', layout.ldStoreDialog).attr('src',
				  LAMS_URL + 'authoring/author.do?method=generateSVG&selectable=true&learningDesignID='
				  		   + learningDesignID + '&_=' + new Date().getTime());
			} else {
				$('#ldScreenshotLoading', layout.ldStoreDialog).show();
				// get the image of the chosen LD and prevent caching
				$('#ldScreenshotAuthor', layout.ldStoreDialog)
					.attr('src', LD_THUMBNAIL_URL_BASE + learningDesignID + '&_=' + new Date().getTime())
					.css({
						'width'  : 'auto',
						'height' : 'auto'
					});
			}
			
			if (title) {
				// copy title of the highligthed sequence to title field
				$('#ldStoreDialogNameField').val(title).focus();
			}
			
			var tree =  layout.ldStoreDialog.dialog('option', 'ldTree'),
				ldNode = tree.getHighlightedNode();
				// no LD was chosen
			if (ldNode && learningDesignID != ldNode.data.learningDesignId) {
				ldNode.unhighlight(true);
			}
		}
		
		$('#ldStoreDialogAccessCell > div.access', layout.ldStoreDialog).each(function(){
			var access = $(this);
			if (+access.attr('learningDesignId') == learningDesignID){
				access.addClass('selected');
			} else {
				access.removeClass('selected');
			}
		});
	},
	
	/**
	 * Puts given object (set or shape) to back of parent.
	 */
	toBack : function(object) {
		if (object.type == 'set') {
			object.forEach(function(item) {
				GeneralLib.toBack(item);
			});
		} else {
			$(object.node).parent().prepend(object.node);
		}
	},
	
	/**
	 * Puts given object (set or shape) to front of parent.
	 */
	toFront : function(object) {
		if (object.type == 'set') {
			object.forEach(function(item) {
				GeneralLib.toFront(item);
			});
		} else {
			$(object.node).parent().append(object.node);
		}
	},
	
	
	/**
	 * Get real coordinates on paper, based on event coordinates.
	 */
	translateEventOnCanvas : function(event) {
		return [event.pageX + canvas.scrollLeft() - canvas.offset().left,
		        event.pageY + canvas.scrollTop()  - canvas.offset().top];
	},
	
	
	/**
	 * Fills "Recently used sequences" box in Open/Save dialog.
	 */
	updateAccess : function(access, fetchIfEmpty){
		if (isReadOnlyMode) {
			return;
		}
		
		if (fetchIfEmpty && !access) {
			$.ajax({
				cache : false,
				async : false,
				url : LAMS_URL + "authoring/author.do",
				dataType : 'json',
				data : {
					'method' : 'getLearningDesignAccess'
				},
				success : function(response) {
					access = response;
				}
			});
		}
		
		if (access) {
			var accessCell = $('#ldStoreDialogAccessCell', layout.ldStoreDialog);
			accessCell.children('div.access').remove();
			$.each(access, function(){
				$('<div />').addClass('access')
							.attr({
								'learningDesignId' : this.learningDesignId,
								'folderID'         : this.workspaceFolderId
							})
							.text(this.title)
							.appendTo(accessCell)
							.click(function(){
								var accessEntry = $(this);
								if (accessEntry.hasClass('selected')) {
									return;
								}
								
								var	isSaveDialog = layout.ldStoreDialog.closest('.ui-dialog').hasClass('ldStoreDialogSave'),
									learningDesignID = +accessEntry.attr('learningDesignId'),
									title = isSaveDialog ? accessEntry.text() : null;
									
								GeneralLib.showLearningDesignThumbnail(learningDesignID, title);
							});
			});
		}
	}
};