﻿﻿﻿﻿﻿﻿﻿/**
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
		
		if (/Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent)){
			$('.desktopButton').hide();
			
			document.addEventListener("touchstart", HandlerLib.touchHandler, true);
			document.addEventListener("touchmove", HandlerLib.touchHandler, true);
			document.addEventListener("touchend", HandlerLib.touchHandler, true);
			document.addEventListener("touchcancel", HandlerLib.touchHandler, true);
		}
	}
	
	GeneralLib.newLearningDesign(true);
	if (!isReadOnlyMode) {
		layout.ld.contentFolderID = initContentFolderID;
	}
	if (initLearningDesignID) {
		GeneralLib.openLearningDesign(+initLearningDesignID);
	}
	
	// remove "loading..." screen
	$('#loadingOverlay').remove();
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
		// was start point of a new branching placed?
		'addBranchingStart' : null,
		// list of all dialogs, so they can be easily closed all at once 
		'dialogs' : [],
		// icons for special activities
		'toolMetadata': {
			'gate'     : {
				'iconPath' : '../images/stop.gif'
			},
			'grouping' : {
				'iconPath' : '../images/grouping.png'
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
			'labelDefaultSize'				   : 10,
			'labelMinSize'					   : 9,
			'labelMaxSize'					   : 20,
			
			'groupingEffectPadding'			   : 5,
			'selectEffectPadding'			   : 7,
			
			'supportsDownloadAttribute'		   : typeof $('<a/>')[0].download != 'undefined',
			
			// will be initialised when paper gets created
			'readOnlyFilter'				   : null
		},
		
		'colors' : {
			'activityBorder'	  : 'black',
			'toolActivityBorder'      : ['','#00007f','#ff8300','#625F67','#ffa500','#00007f','#7aa712'],
			// each activity type has its own colour
			'activity'     		  : ['','#caddfb','#ffffbb','#ece9f7','#fdf1d3','#caddfb','#e9f9c0'],
			'activityText' 		  : 'black',
			// default region colour
			'annotation'		  : '#CCFF99',
			// region colours to choose from
			'annotationPalette'	  : ['CCFF99', 'CDA5F3', 'FFFFCC', '99E6DF', '6495ED',
			                   	     'FFF8DC', 'FFDDB3', 'D1DFFA', 'DCDCDC', '20B2AA',
			                   	     'B0C4DE', 'FFE4E1', 'F9D2F9', 'FF2200'],

			// when mouse hovers over rubbish bin
			'binSelect' 		  : 'red',
			
			'branchingEdgeStart'  : 'green',
			'branchingEdgeEnd'    : 'red',
			// highlight branching edges on mouse hover
			'branchingEdgeMatch'  : 'blue',
			'gate'         		  : 'red',
            'gateBorder'          : '#801515',
			'gateText'     		  : 'white',
			'grouping'		      : '#caddfb',
			'groupingBorder'	  : '#00007f',
			'optionalActivity'    : '#caddfb',
			'optionalActivityBorder'    : '#00007f',
			// dashed border around a selected activity 
			'selectEffect'        : 'black',
			'transition'   		  : 'rgb(119,126,157)',
			// highlight TBL activities which should be grouped
			'activityRequireGrouping' : 'red',
			'activityReadOnly'	  : 'red'
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
				learningLibraryTitle = $(this).attr('learningLibraryTitle'),
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
			
			// assign icons' data uris to their learning library IDs instead of labels
			ActivityIcons[learningLibraryID] = ActivityIcons[learningLibraryTitle];
			delete ActivityIcons[learningLibraryTitle];
			$('<img />').attr('src', ActivityIcons[learningLibraryID]).appendTo(".img-"+learningLibraryID);
			// register tool properties so they are later easily accessible
			layout.toolMetadata[learningLibraryID] = {
				'iconPath'				  : $(this).attr('iconPath'),
				'defaultToolContentID'    : $(this).attr('defaultToolContentId'),
				'supportsOutputs' 	 	  : $(this).attr('supportsOutputs'),
				'activityCategoryID' 	  : activityCategoryID,
				'parallelChildActivityDefs' : parallelChildActivityDefs
			};
			
		});
		
		if (!isReadOnlyMode){
			// store the initial window height now as on iPad the iframe grows when templates are show,
			// reporting incorrect window height to the first resizePaper() run
			layout.initWindowHeight = $(window).height();
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
				var learningLibraries = this.learningLibraries;
				if (!learningLibraries) {
					return true;
				}
				
				var templates = allTemplates.clone().appendTo(templateContainerCell);
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
						return $(this).clone().css({
							'width'   : '150px',
							'border'  : 'thin black solid',
							'z-index' : 1,
							'cursor'  : 'move'
						});
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
					    	label = $('#toolDisplayName', draggable.draggable).text().trim(),
					    	activity = null,
					    	translatedEvent = GeneralLib.translateEventOnCanvas(event),
							eventX = translatedEvent[0],
							eventY = translatedEvent[1];

					    if (activityCategoryID == 5) {
					    	// construct child activities out of previously referenced HTML templates
					    	var childActivities = [];
					    	layout.toolMetadata[learningLibraryID].parallelChildActivityDefs.each(function(){
					    		var childLearningLibraryID = +$(this).attr('learningLibraryId'),
					    			childToolID = +$(this).attr('toolId'),
					    			toolLabel = $('#toolDisplayName', this).text().trim(),
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
				   }
			});
		}
	},


	/**
	 * Initialises various Authoring widgets.
	 */
	initLayout : function(){
		// buttons shared by both load and save dialogs
		var ldStoreDialogContents = $('#ldStoreDialogContents');
		
		$('#ldStoreDialogCancelButton', ldStoreDialogContents).click(function(){
			layout.ldStoreDialog.modal('hide');
		});
		
		$('#ldStoreDialogNewFolderButton', ldStoreDialogContents).click(function(){
    		var dialog = layout.ldStoreDialog,
				tree = dialog.data('ldTree'),
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
			if (!GeneralLib.validateName(title)) {
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
			var data = {
                    'name'           : title,
                    'parentFolderID' : parentFolder.data.folderID
			};
			data[csrfTokenName] = csrfTokenValue;

			$.ajax({
				type  : 'POST',
				cache : false,
				async : true,
				url : LAMS_URL + "workspace/createFolder.do",
				dataType : 'text',
				data : data,
				success : function() {
					tree.removeChildren(parentFolder);
					parentFolder.expand();
				}
			});
		});
		
		// copy sequence or folder
		$('#ldStoreDialogCopyButton, #ldStoreDialogCutButton', ldStoreDialogContents).click(function(){
    		var dialog = layout.ldStoreDialog,
				tree = dialog.data('ldTree'),
				// hightlighted sequence/folder in the tree
				ldNode = tree.getHighlightedNode(),
				isFolder = ldNode && !ldNode.data.learningDesignId,
				isCut = $(this).is('#ldStoreDialogCutButton');
    		if (!ldNode) {
    			return;
    		}
    		if (isCut && !ldNode.data.canModify) {
    			alert(LABELS.RESOURCE_MODIFY_ERROR);
    			return;
    		}
    		
    		dialog.data('copiedResource', {
    			'isCut'	 	  : isCut,
    			'isFolder'    : isFolder,
    			'resourceNode': ldNode
    		});
		});
		
		// pastes sequence or folder
		$('#ldStoreDialogPasteButton', ldStoreDialogContents).click(function(){
    		var dialog = layout.ldStoreDialog,
				tree = dialog.data('ldTree'),
				// hightlighted sequence/folder in the tree
				ldNode = tree.getHighlightedNode(),
				folderNode = ldNode ? (ldNode.data.learningDesignId ? ldNode.parent : ldNode) : null,
				copiedResource = dialog.data('copiedResource');

			if (!folderNode || !copiedResource) {
    			return;
    		}
			
			if (copiedResource.isCut) {
				var parent = ldNode.parent;
				while (parent) {
					if (parent.index == copiedResource.resourceNode.index) {
						alert(LABELS.FOLDER_MOVE_TO_CHILD_ERROR);
						return;
					}
					parent = parent.parent;
				}
			}

			var data = {
                'targetFolderID' : folderNode.data.folderID,
                'resourceID'     : copiedResource.isFolder ? copiedResource.resourceNode.data.folderID
                                                           : copiedResource.resourceNode.data.learningDesignId ,
                'resourceType'   : copiedResource.isFolder ? 'Folder' : 'LearningDesign'
			};
			data[csrfTokenName] = csrfTokenValue;

			$.ajax({
				type  : 'POST',
				cache : false,
				url : copiedResource.isCut ? LAMS_URL + "workspace/moveResource.do" : LAMS_URL + "workspace/copyResource.do",
				dataType : 'text',
				data : data,
				success : function() {
					if (copiedResource.isCut) {
						tree.removeNode(copiedResource.resourceNode, true);
					}
					tree.removeChildren(folderNode);
					folderNode.expand();
					
					dialog.data('copiedResource', null);
				}
			});
		});

		
		// removes sequence or folder
		$('#ldStoreDialogDeleteButton', ldStoreDialogContents).click(function(){
    		var dialog = layout.ldStoreDialog,
				tree = dialog.data('ldTree'),
				// hightlighted sequence/folder in the tree
				ldNode = tree.getHighlightedNode();
    		if (!ldNode) {
    			return;
    		}
    		if (!ldNode.data.canModify) {
    			alert(LABELS.RESOURCE_MODIFY_ERROR);
    			return;
    		}
    		var isFolder = !ldNode.data.learningDesignId;
    		if (!confirm(LABELS.DELETE_NODE_CONFIRM + (isFolder ? LABELS.FOLDER : LABELS.SEQUENCE) + '?')) {
    			return;
    		}
			var data = {
                    'resourceID'   : isFolder? ldNode.data.folderID : ldNode.data.learningDesignId,
                    'resourceType' : isFolder ? 'Folder' : 'LearningDesign'				
			}
			data[csrfTokenName] = csrfTokenValue;
			$.ajax({
				type  : 'POST',
				cache : false,
				async : true,
				url : LAMS_URL + "workspace/deleteResource.do",
				dataType : 'text',
				data : data,
				success : function() {
					var parentFolder = ldNode.parent;
					tree.removeChildren(parentFolder);
					parentFolder.expand();
				}
			});
		});
		
		// renames sequence or folder
		$('#ldStoreDialogRenameButton', ldStoreDialogContents).click(function(){
    		var dialog = layout.ldStoreDialog,
				tree = dialog.data('ldTree'),
				// hightlighted sequence/folder in the tree
				ldNode = tree.getHighlightedNode();
    		if (!ldNode) {
    			return;
    		}
    		if (!ldNode.data.canModify) {
    			alert(LABELS.RESOURCE_MODIFY_ERROR);
    			return;
    		}
    		var isFolder = !ldNode.data.learningDesignId,
    			title = prompt(LABELS.RENAME_TITLE_PROMPT + (isFolder ? LABELS.FOLDER : LABELS.SEQUENCE)
						+ ' "' + ldNode.data.label + '"');
			
			// skip if no name or the same name was provided
			if (!title || ldNode.data.label == title) {
				return;
			}
			if (!GeneralLib.validateName(title)) {
    			alert(LABELS.TITLE_VALIDATION_ERROR);
    			return;
    		}
			
			$.each(ldNode.parent.children, function(){
				if (this.data.label == title && (isFolder == (this.data.folderID != null))) {
					alert(isFolder ? LABELS.FOLDER_EXISTS_ERROR : LABELS.SEQUENCE_EXISTS_ERROR);
					title = null;
					return false;
				}
			});
			if (!title) {
				return;
			}

			var data = {
            	'name'         : title,
                'resourceID'   : isFolder? ldNode.data.folderID : ldNode.data.learningDesignId,
                'resourceType' : isFolder ? 'Folder' : 'LearningDesign'
			};
			data[csrfTokenName] = csrfTokenValue;

			$.ajax({
				type  : 'POST',
				cache : false,
				async : true,
				url : LAMS_URL + "workspace/renameResource.do",
				dataType : 'text',
				data : data,
				success : function(response) {
					if (isFolder) {
						ldNode.data.label = title;
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
		});
	    
		$('#ldStoreDialogSaveButton', ldStoreDialogContents).click(function(){
    		var dialog = layout.ldStoreDialog,
    			saveButton = $('#ldStoreDialogSaveButton', dialog),
				title = $('#ldStoreDialogNameContainer input', dialog).val().trim();
			if (!title) {
				alert(LABELS.SAVE_SEQUENCE_TITLE_PROMPT);
				return;
			}
			
			if (!GeneralLib.validateName(title)) {
				alert(LABELS.TITLE_VALIDATION_ERROR);
				return;
			}
			saveButton.prop('disabled', true).button('loading');
			var folderNode = null,
				folderID = null,
				tree = dialog.data('ldTree'),
				node = tree.getHighlightedNode();
			if (node) {
	    		// get folder from LD tree
				folderNode = node.data.learningDesignId ? node.parent : node;
				if (!folderNode.data.canSave) {
					alert(LABELS.FOLDER_CAN_NOT_SAVE_ERROR);
					saveButton.button('reset');
					return;
				}
				folderID = folderNode.data.folderID;
			} else {
				// get data from "recently used sequences" list
				var selectedAccess = $('#ldStoreDialogAccessDiv > div.selected', dialog);
				// if title was altered, do not consider this an overwrite
				if (selectedAccess.length > 0 && title == selectedAccess.text()) {
					learningDesignID = +selectedAccess.data('learningDesignId');
					folderID = +selectedAccess.data('folderID');
				}
			}
			
			if (!folderID) {
				// although an existing sequence can be highlighted 
				alert(LABELS.FOLDER_NOT_SELECTED_ERROR);
				saveButton.button('reset');
				return;
			}
			
			// if a node is highlighted but user modified the title,
			// it is considered a new sequence
			// otherwise check if there is no other sequence with the same name
			var nodeData = null;
			if (folderNode && folderNode.children) {
				$.each(folderNode.children, function(){
					if (this.data.label == title) {
						this.highlight();
						nodeData = this.data;
						return false;
					}
				});
			}
			if (nodeData && (!nodeData.canModify || (!canSetReadOnly && nodeData.readOnly))){
				alert(LABELS.READONLY_FORBIDDEN_ERROR);
				saveButton.button('reset');
				return;
			}
			if (nodeData && !confirm(LABELS.SEQUENCE_OVERWRITE_CONFIRM)) {
				saveButton.button('reset');
				return;
			}
			var readOnly = (nodeData && !nodeData.canModify) || 
						   (canSetReadOnly && $('#ldStoreDialogReadOnlyCheckbox', dialog).prop('checked')),
				learningDesignID = nodeData ? nodeData.learningDesignId : null,
				result = GeneralLib.saveLearningDesign(folderID, learningDesignID, title, readOnly);
			if (result) {
				GeneralLib.openLearningDesign();
				dialog.modal('hide');
			}
			saveButton.button('reset');
		});
		
		$('#ldStoreDialogOpenButton', ldStoreDialogContents).click(function(){
    		var dialog = layout.ldStoreDialog,
    			openButton = $('#ldStoreDialogOpenButton', dialog),
				tree = dialog.data('ldTree'),
				ldNode = tree.getHighlightedNode(),
				learningDesignID = ldNode ? ldNode.data.learningDesignId : null;
    		
    		openButton.button('loading');
			if (!learningDesignID) {
				learningDesignID = +$('#ldStoreDialogAccessDiv > div.selected', dialog)
								   .data('learningDesignId');
			}
			
			// no LD was chosen
			if (!learningDesignID) {
				alert(LABELS.SEQUENCE_NOT_SELECTED_ERROR);
				openButton.button('reset');
				return;
			}
			
			openButton.button('reset');
			dialog.modal('hide');
			GeneralLib.openLearningDesign(learningDesignID);
		});
		
		// ability to save a sequence on pressing the Enter key in a title input field
		$('#ldStoreDialogNameContainer input', layout.ldStoreDialog).on('keyup', function (e) {
		    if (e.keyCode == 13) {
				$('#ldStoreDialogSaveButton', $('#ldStoreDialogContents')).trigger( "click" );
		    }
		});
		
		$('#ldStoreDialogImportPartButton', ldStoreDialogContents).click(function(){
			var dialog = layout.ldStoreDialog,
	 			frameLayout = $('#ldStoreDialogImportPartFrame', dialog)[0].contentWindow.layout,
	 			selectedActivities = [],
	 			addActivities = [],
	 			selectedAnnotations = [];
 			
 		
	 		$.each(frameLayout.activities, function(){
	 			if (this.items.selectEffect) {
	 				selectedActivities.push(this);
	 				dialog.data('importActivity')(this, addActivities);
	 			}
	 		});
	 		$.each(frameLayout.regions.concat(frameLayout.labels), function(){
	 			if (this.items.selectEffect) {
	 				selectedAnnotations.push(this);
	 				// unlike importActivity(), this method already takes care of UIIDs
	 				// and adding new items to collections
	 				dialog.data('importAnnotation')(this);
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
	
	 		dialog.modal('hide');
		});
		
		// initalise Learning Design load/save dialog
		layout.ldStoreDialog = showDialog('ldStoreDialog',{
			'autoOpen'      : false,
			'resizable'     : false,
			'draggable'     : false,
			'open' : function(){
				
				var dialog = $(this),
					treeHeight = Math.max(90, $(window).height() - 325) + 'px';
				$('.modal-dialog', dialog).width(Math.max(500, $(window).width() - 50));
				$('#ldStoreDialogTree', layout.ldStoreDialog).css({
					'height'     : treeHeight,
					'max-height' : treeHeight
				});
				// limit size of the canvas so dialog does not resize after SVG loads
				$('#ldStoreDialogCanvasDiv', dialog).css({
					'max-width' : $(window).width() - 425 + 'px',
					'max-height':  $(window).height() - 190 + 'px',
				});
				
				GeneralLib.showLearningDesignThumbnail();
				$('#ldStoreDialogLeftButtonContainer button', dialog).prop('disabled', true);
				
				dialog.data('copiedResource', null);
				
				//focus dialog itself so it can be closed on pressing Esc button
				setTimeout(function() {
					if ($("#ldStoreDialogOpenButton:visible").is(':visible')) {
						dialog.focus();
					}
		        }, 500);
				
			},
			'close' : null,
			'data' : {
				'prepareForOpen' : function(dialogTitle, learningDesignTitle, shownElementIDs, highlightFolder){
					// only Save As uses highlightFolder; otherwise the first folder in top level gets expanded and highlighted
					layout.folderPathCurrent = highlightFolder && layout.ld.folderPath ? layout.ld.folderPath.slice() : [];
					MenuLib.loadLearningDesignTree();
					
					$('#ldStoreDialogNameContainer input', layout.ldStoreDialog).val(learningDesignTitle);
					$('.modal-title', layout.ldStoreDialog).text(dialogTitle);
					var rightButtons = $('#ldStoreDialogRightButtonContainer', layout.ldStoreDialog);
					$('button', rightButtons).hide();
					$('#ldStoreDialogReadOnlyLabel *', layout.ldStoreDialog).hide();
					$('#ldStoreDialogNameContainer, #ldStoreDialogImportPartFrame', layout.ldStoreDialog).hide();
					$(shownElementIDs, layout.ldStoreDialog).show();
					
					var isOpenDialog = shownElementIDs.indexOf('ldStoreDialogOpenButton') >= 0;
					if (isOpenDialog) {
						// in open dialog display only information
						$('#ldStoreDialogReadOnlySpan', layout.ldStoreDialog).css('color', layout.colors.activityReadOnly);
					} else if (canSetReadOnly) {
							// the first highlighted folder is user's private folder
							$('#ldStoreDialogReadOnlyCheckbox', layout.ldStoreDialog).show()
								.prop('disabled', false).prop('checked', false);
							$('#ldStoreDialogReadOnlySpan', layout.ldStoreDialog).show().css('color', 'initial');
					} else {
						$('#ldStoreDialogReadOnlySpan', layout.ldStoreDialog).css('color', layout.colors.activityReadOnly);
					}
					
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
								type  : 'POST',
								cache : false,
								async : false,
								url : LAMS_URL + "authoring/copyToolContent.do",
								data : {
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
			}
		}, false);
		
		//alow to close open/save dialog on pressing Esc button
		layout.ldStoreDialog.on('keydown', function(evt) {
	        if (evt.keyCode === $.ui.keyCode.ESCAPE) {
	        	layout.ldStoreDialog.modal('hide');
	        }
	        evt.stopPropagation();
	    });
		
		$('.modal-body', layout.ldStoreDialog).empty().append(ldStoreDialogContents.show());
		
		layout.dialogs.push(layout.ldStoreDialog);

		// there should be no focus, just highlight
		YAHOO.widget.TreeView.FOCUS_CLASS_NAME = null;
		var tree = new YAHOO.widget.TreeView('ldStoreDialogTree');
		// store the tree in the dialog's data
		layout.ldStoreDialog.data('ldTree', tree);
		// make folder contents load dynamically on open
		tree.setDynamicLoad(function(node, callback){
			// load subfolder contents
			var childNodeData = MenuLib.getFolderContents(node.data.folderID, node.data.canSave, node.data.canHaveReadOnly);
			if (childNodeData) {
				$.each(childNodeData, function(){
						// create and add a leaf
						new YAHOO.widget.HTMLNode(this, node);
					});
			}
			
			// expand the folder where existing LD resides, if applicable
			MenuLib.highlightFolder(node);
			
			// required by YUI
			callback();
		});
		tree.singleNodeHighlight = true;
		tree.subscribe('clickEvent', function(event) {
			var isOpenDialog = $('#ldStoreDialogSaveButton', layout.ldStoreDialog).is(':hidden')
				nodeData = event.node.data;
			
			//prevent item from being deselected on any subsequent clicks
			if (isOpenDialog && event.node.highlightState == 1) {
				return false;
			}
			
			//disable edit buttons if no elements is selected
			$('#ldStoreDialogLeftButtonContainer button', layout.ldStoreDialog)
				.prop('disabled', event.node.highlightState > 0);
			
			if (canSetReadOnly && !isOpenDialog) {
				// detect which folders/sequences are marked as read-only
				// and which ones are immutable
				if (event.node.isLeaf) {
					$('#ldStoreDialogReadOnlyCheckbox', layout.ldStoreDialog)
						.prop('disabled', !nodeData.canModify || !nodeData.canHaveReadOnly)
						.prop('checked', nodeData.readOnly || !nodeData.canModify);
				} else {
					$('#ldStoreDialogReadOnlyCheckbox', layout.ldStoreDialog)
						.prop('disabled', !nodeData.canSave || !nodeData.canHaveReadOnly)
						.prop('checked', !nodeData.canSave);
				}
			} else {
				// is this is normal user or open dialog, only show/hide read-only label
				if (event.node.isLeaf ? nodeData.readOnly || !nodeData.canModify : !nodeData.canSave){
					$('#ldStoreDialogReadOnlySpan', layout.ldStoreDialog).show();
				} else {
					$('#ldStoreDialogReadOnlySpan', layout.ldStoreDialog).hide();
				}
			}
			
			// if it's a folder in load sequence dialog - highlight but stop processing
			if (isOpenDialog && !nodeData.learningDesignId){
				return true;
			}
			
			//show LearningDesign thumbnail and title
			var learningDesignID = event.node.highlightState == 0   ? +nodeData.learningDesignId : null,
				title            = !isOpenDialog && learningDesignID ? nodeData.label : null;
			GeneralLib.showLearningDesignThumbnail(learningDesignID, title);				
		});
		tree.subscribe('clickEvent', tree.onEventToggleHighlight);
		
		tree.subscribe('dblClickEvent', function(event){
			
			//trigger "clickEvent" first so that save/open function will know which element is selected 
			tree.fireEvent("clickEvent", event);
			
			// open/save sequence
			var buttonToClick = $('#ldStoreDialogSaveButton', layout.ldStoreDialog).is(':visible') 
					? '#ldStoreDialogSaveButton' : '#ldStoreDialogOpenButton'; 
			$(buttonToClick, $('#ldStoreDialogContents')).trigger( "click" );
		});
		
		GeneralLib.updateAccess(initAccess);
		
		var infoDialogContents = $('#infoDialogContents');
		$('#infoDialogOKButton', infoDialogContents).click(function(){
			layout.infoDialog.modal('hide');
		});
		
		layout.infoDialog = showDialog('infoDialog',{
			'autoOpen'      : false,
			'modal'			: false,
			'resizable'     : false,
			'draggable'     : false,
			'width'			: 290,
			'title'			: LABELS.INFO_DIALOG_TITLE,
			'close' : null,
			'data' : {
				'position' : {
					'my' : 'center top',
					'at' : 'center top+20px',
					'of' : '#canvas'
				},
				'show' : function(html, temporary){
					var timeout = layout.infoDialog.data('temporaryTimeout');
					if (timeout) {
						clearTimeout(timeout);
					}
					
					var body = $('#infoDialogBody', layout.infoDialog),
						// is dialog already open?
						visible = layout.infoDialog.hasClass('in'),
						// should be initialised/kept in temporary mode?
						temporaryMode = visible ? body.hasClass('temporary') : temporary;
					if (visible) {
						if (temporaryMode) {
							body.html(html);
						} else {
							body.html(body.html() + '<br /><br />' + html);
						}
					} else {
						body.html(html);
					}
					
					if (temporaryMode) {
						// temporary dialog hides after 5 seconds or on click
						$('.modal-header, #infoDialogButtons', layout.infoDialog).hide();
						body.addClass('temporary').one('click', function(){
							layout.infoDialog.modal('hide');
						});
						var timeout = setTimeout(function(){
							body.off('click');
							layout.infoDialog.modal('hide');
						}, 5000);
						layout.infoDialog.data('temporaryTimeout', timeout);
					} else {
						$('.modal-header, #infoDialogButtons', layout.infoDialog).show();
						body.removeClass('temporary');
					}
					
					if (!visible) {
						layout.infoDialog.modal('show');
					}
				}
			}
		});
		
		$('.modal-body', layout.infoDialog).empty().append(infoDialogContents.show());
		layout.dialogs.push(layout.infoDialog);
		
		
		var weightsDialogContents = $('#weightsDialogContents');
		layout.weightsDialog = showDialog('weightsDialog',{
			'autoOpen'      : false,
			'modal'			: true,
			'resizable'     : false,
			'draggable'     : true,
			'width'			: 500,
			'close' 		: null,
			'title'			: LABELS.WEIGHTS_TITLE,
			'data' 			: {
				'prepareForOpen' : function(){
					var tbody = $('tbody', weightsDialogContents).empty(),
						weightsEnabled = false;
					if (layout.activities.length > 0) {
						$.each(layout.activities, function(){
							if (this.gradebookToolOutputDefinitionName && this.gradebookToolOutputDefinitionWeightable) {
								weightsEnabled = true;
								var activity = this,
									row = $('<tr />').appendTo(tbody).data('activity', activity).hover(
										function(){
											var row = $(this);
											row.siblings().each(function(){
												$(this).removeClass('selected');
											});
											row.addClass('selected');
											ActivityLib.removeSelectEffect();
											ActivityLib.addSelectEffect(row.data('activity'), false);
										},
										function(){
											$(this).removeClass('selected');
											ActivityLib.removeSelectEffect($(this).data('activity'));
										}),
									weight = $('<input maxlength="3" />');
								$('<td />').text(activity.title).appendTo(row);
								$('<td />').text(activity.gradebookToolOutputDefinitionDescription).appendTo(row);
								$('<td />').append(weight).appendTo(row);
								weight.spinner({
									'min'    : 0,
									'max'    : 100,
									'change' : function(){
										var value = $(this).val();
										if (value == "" || isNaN(value)) {
											value = null;
										}
										activity.gradebookToolOutputWeight = value;
										layout.weightsDialog.data('sumWeights')();
									},
									'spin'  : function(event, ui) {
										activity.gradebookToolOutputWeight = ui.value;
										layout.weightsDialog.data('sumWeights')();
									}
								}).val(activity.gradebookToolOutputWeight);
							}
						});
					}
					
					if (weightsEnabled) {
						layout.weightsDialog.data('sumWeights')(true);
					} else {
						tbody.append($('<tr />').append($('<td colspan="3" />').text(LABELS.WEIGHTS_NONE_FOUND_ERROR)));
						$('#sumWeightCell', layout.weightsDialog).empty();
					}
				},
				
				'sumWeights' : function(firstRun){
					var sum = null;
					$('tbody tr', layout.weightsDialog).each(function(){
						var weight = $('input', this);
						if (!firstRun && !weight.spinner('isValid')) {
							weight.val(null);
							return true;
						}
						var value = $(this).data('activity').gradebookToolOutputWeight;
						if (value) {
							if (sum == null) {
								sum = 0;
							}
							sum += +value;
						}
					});
					
					var sumCell = $('#sumWeightCell', layout.weightsDialog);
					if (sum == null) {
						sumCell.empty();
					} else {
						sumCell.text(sum + '%');
						if (sum == 100) {
							sumCell.removeClass('incorrect');
						} else {
							sumCell.addClass('incorrect');
						}
					}
				}
			}
		});
		
		$('.modal-body', layout.weightsDialog).empty().append(weightsDialogContents.show());
		
		layout.dialogs.push(layout.weightsDialog);
		
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
		$('#arrangeButton').blur();
		
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
		
		if (isReadOnlyMode) {
			// set it so activities' coordinates get updated in the DB when SVG get generated
			layout.wasArranged = true;
		} else {
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
			paperWidth = paper.attr('width'),
			// check how many columns current paper can hold
			maxColumns = Math.floor(((paperWidth < 300 && paperMinWidth ? paperMinWidth : paperWidth) 
									    - layout.conf.arrangeHorizontalPadding)
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
		var cancelLiveEditButton = $('#cancelLiveEditButton');
		cancelLiveEditButton.button('loading');
		
		if (GeneralLib.canClose() || confirm(LABELS.LIVEEDIT_CANCEL_CONFIRM)) {
			$.ajax({
				type  : 'POST',
				async : true,
				cache : false,
				url : LAMS_URL + 'authoring/finishLearningDesignEdit.do',
				data : {
					'learningDesignID' : layout.ld.learningDesignID,
					'cancelled' : 'true'
				},
				success : function() {
					GeneralLib.setModified(false);
					window.parent.closeDialog('dialogAuthoring');
				}
			});
		} else {
			cancelLiveEditButton.button('reset');
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
	 * If sequence starts with of Grouping->(MCQ or Assessment)->Leader Selection->Scratchie,
	 * there is a good chance this is a TBL sequence and all activities must be grouped.
	 */
	checkTBLGrouping : function(){
		var firstActivity = null,
			activities = [],
			getNextActivity = function(activity) {
				var nextActivity = activity;
				do {
					nextActivity = nextActivity.transitions.from.length > 0 ? nextActivity.transitions.from[0].toActivity : null;
					// skip gates along the way
				} while (nextActivity instanceof ActivityDefs.GateActivity);
				return nextActivity;
			};
		// find first activity in the sequence
		// it can be wrong if not all activities are connected
		$.each(layout.activities, function(){
			if (this.transitions && this.transitions.to.length === 0 && this.transitions.from.length > 0){
				firstActivity = this;
				return false;
			}
		});
		if (!firstActivity) {
			return null;
		}
		// the first activity can be grouping or the second or third one (Live Edit gate and notebook can be in front)
		var firstGroupingActivity = firstActivity instanceof ActivityDefs.GroupingActivity ? firstActivity : null;
		if (!firstGroupingActivity) {
			firstGroupingActivity = getNextActivity(firstActivity);
			if (!firstGroupingActivity) {
				return null;
			}
			if (!(firstGroupingActivity instanceof ActivityDefs.GroupingActivity)){
				firstGroupingActivity = getNextActivity(firstGroupingActivity);
				if (!(firstGroupingActivity instanceof ActivityDefs.GroupingActivity)){
					return null;
				}
			}
		}

		// then it is Assessment or MCQ
		var secondActivity = getNextActivity(firstGroupingActivity),
			templateContainer = $('#templateContainerCell'),
			isTBL = secondActivity instanceof ActivityDefs.ToolActivity
			&& (secondActivity.learningLibraryID == $('.template[learningLibraryTitle="Assessment"]', templateContainer).attr('learningLibraryId')
			    || secondActivity.learningLibraryID == $('.template[learningLibraryTitle="MCQ"]', templateContainer).attr('learningLibraryId'));
		if (!isTBL){
			return null;
		}
		activities.push(secondActivity);
		// then leader selection
		var thirdActivity = getNextActivity(secondActivity);
		isTBL = thirdActivity instanceof ActivityDefs.ToolActivity 
				&& thirdActivity.learningLibraryID == $('.template[learningLibraryTitle="Leaderselection"]', templateContainer).attr('learningLibraryId');
		if (!isTBL){
			return null;
		}
		activities.push(thirdActivity);
		// then scratchie
		var fourthActivity = getNextActivity(thirdActivity);
		isTBL = fourthActivity instanceof ActivityDefs.ToolActivity 
				&& fourthActivity.learningLibraryID == $('.template[learningLibraryTitle="Scratchie"]', templateContainer).attr('learningLibraryId');
		if (!isTBL){
			return null;
		}
		activities.push(fourthActivity);
		
		// then optional assessments
		var nextActivity = fourthActivity;
		do {
			nextActivity = getNextActivity(nextActivity);
			if (nextActivity instanceof ActivityDefs.ToolActivity
				&& nextActivity.learningLibraryID == $('.template[learningLibraryTitle="Assessment"]', templateContainer).attr('learningLibraryId')) {
				activities.push(nextActivity);
			} else {
				nextActivity = null;
			}
		} while (nextActivity);
		
		// check which ones are not grouped
		var activitiesToGroup = [];
		$.each(activities, function(){
			if (!this.grouping){
				activitiesToGroup.push(this);
				this.requireGrouping = true;
				this.draw();
			}
		});
		return activitiesToGroup.length === 0 ? null : activitiesToGroup;
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
	newLearningDesign : function(force){
		// force means that user should not be asked for confirmation.
		if (!force && layout.modified && !confirm(LABELS.CLEAR_CANVAS_CONFIRM)){
			return;
		}
		
		$('#ldDescriptionDetails').slideUp();
		
		if (force) {
			layout.ld = {
				'maxUIID' : 0
			};
			layout.activities = [];
			layout.regions = [];
			layout.labels = [];
			layout.floatingActivity = null;
			
			if (paper) {
				paper.clear();
			} else {
				// need to set size right away for Chrome
				paper = Snap(Math.max(0, canvas.width() - 5), Math.max(0, canvas.height() - 5));
				canvas.append(paper.node);
			}
			// initialise filter for read-only activities in Live Edit
			layout.conf.readOnlyFilter = paper.filter(Snap.filter.grayscale(1));
			
			GeneralLib.resizePaper();
			GeneralLib.setModified(false);
		} else {
			// do not prompt again
			window.onbeforeunload = null;
			// full window reload so new content ID gets generated
			document.location.href = LAMS_URL + 'authoring/openAuthoring.do';
		}
	},
	
	
	/**
	 * Replace current canvas contents with the loaded sequence.
	 */
	openLearningDesign : function(learningDesignID, callback) {
		if (!learningDesignID){
			// do just a re-load
			learningDesignID = layout.ld.learningDesignID;
		}
		// get LD details
		$.ajax({
			type  : 'POST',
			async : true,
			cache : false,
			url : LAMS_URL + "authoring/openLearningDesign.do",
			dataType : 'json',
			data : {
				'learningDesignID': learningDesignID
			},
			success : function(response) {
				if (!response) {
					if (!isReadOnlyMode) {
						layout.infoDialog.data('show')(LABELS.SEQUENCE_LOAD_ERROR);
					}
					return;
				}
				
				var ld = response.ld;
				
				// remove existing activities
				GeneralLib.newLearningDesign(true);
				layout.ld = {
					'learningDesignID' : ld.learningDesignID,
					'folderID'		   : ld.workspaceFolderID,
					'folderPath'	   : ld.folderPath,
					'contentFolderID'  : ld.contentFolderID,
					'title'			   : ld.title,
					'maxUIID'		   : 0,
					'readOnly'		   : ld.readOnly && !ld.editOverrideLock,
					'canModify'		   : ld.copyTypeID == 1 || ld.editOverrideLock,
					'editOverrideLock' : ld.editOverrideLock,
					'copyTypeID'	   : ld.copyTypeID
				};
				
				if (!isReadOnlyMode) {
					$('#ldDescriptionFieldTitle').html(GeneralLib.escapeHtml(ld.title));
					CKEDITOR.instances['ldDescriptionFieldDescription'].setData(ld.description);
					$('#ldDescriptionLicenseSelect').val(ld.licenseID || 0).change();
					$('#ldDescriptionLicenseText').text(ld.licenseText || null);
				}
				
				var arrangeNeeded = false,
					// if system gate is found, it is Live Edit
					systemGate = null,
					// should we allow the author to enter activity authoring
					activitiesReadOnly = !layout.ld.canModify || (!canSetReadOnly && layout.ld.readOnly),
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
											LAMS_URL + activityData.authoringURL + (activityData.authoringURL.indexOf('?') < 0 ? '?' : '&') 
												+ 'toolContentID='   + activityData.toolContentID
												+ '&contentFolderID=' + layout.ld.contentFolderID,
											activityData.xCoord ? activityData.xCoord : 1,
											activityData.yCoord ? activityData.yCoord : 1,
											activityData.activityTitle,
											activityData.readOnly || activitiesReadOnly,
											activityData.evaluation);
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
											'name' 	  : this.groupName,
											'id'   	  : this.groupID,
											'uiid' 	  : this.groupUIID,
											'orderID' : this.orderID
											});
										
										if (this.groupUIID && layout.ld.maxUIID < this.groupUIID) {
											layout.ld.maxUIID = this.groupUIID;
										}
									});
									
									// sort groups by asceding order ID
									groups.sort(function(a,b) {
										return a.orderID - b.orderID;
									});
									
									activity = new ActivityDefs.GroupingActivity(
											activityData.activityID,
											activityData.activityUIID,
											activityData.xCoord,
											activityData.yCoord,
											activityData.activityTitle,
											activityData.readOnly || activitiesReadOnly,
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
						case 14: var gateType = gateType || 'condition';
						case 16:
							var gateType = gateType || 'password';
							activity = new ActivityDefs.GateActivity(
								activityData.activityID,
								activityData.activityUIID,
								activityData.xCoord,
								activityData.yCoord,
								activityData.activityTitle,
								activityData.description,
								activityData.readOnly || activitiesReadOnly,
								gateType,
								activityData.gateStartTimeOffset,
								activityData.gateActivityCompletionBased,
								activityData.gatePassword);
							
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
									activityData.readOnly || activitiesReadOnly);
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
									activityData.readOnly || activitiesReadOnly,
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
							arrangeNeeded |= (activityData.xCoord != null && activityData.yCoord != null)
											 || activityData.startXCoord == null
											 || activityData.startYCoord == null;
							var branchingType = branchingType || 'optional',
								branchingEdge = new ActivityDefs.BranchingEdgeActivity(activityData.activityID,
										activityData.activityUIID,
										arrangeNeeded ? 0 : activityData.startXCoord,
										arrangeNeeded ? 0 : activityData.startYCoord,
										activityData.activityTitle,
										activityData.readOnly || activitiesReadOnly,
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
						DecorationLib.addLabel(this.xcoord, this.ycoord, this.title, this.color, this.size);
					}
				});
				
				if (arrangeNeeded) {
				 	GeneralLib.arrangeActivities();
				} else {
					GeneralLib.resizePaper();
				}
				
				var parentFrame = window.parent.GeneralLib;
				if (parentFrame) {
					parentFrame.resizeImportPartFrame(+paper.attr('height'));
				}

				if (systemGate) {
					// if system gate exists, it is Live Edit
					layout.liveEdit = true;
					
					// remove unnecessary buttons, show Cancel, move Open after Save and Cancel
					$('#newButton, #openButton').parent().remove();
					$('#importSequenceButton, #previewButton').remove();
					$('#saveButton').parent().children('.dropdown-toggle, .dropdown-menu').remove();
					$('#saveButton').text(LABELS.LIVE_EDIT_SAVE);
					$('#cancelLiveEditButton').show();
				}
				
				GeneralLib.setModified(false);
				GeneralLib.updateAccess(response.access);
				
				if (!ld.validDesign && !isReadOnlyMode) {
					layout.infoDialog.data('show')(LABELS.SEQUENCE_NOT_VALID);
				}
				
				if (callback) {
					callback(); 
				}
			}
		});
	},
	
	/**
	 * Constructs LD JSON with all data needed for save.
     * "displayErrors" in true if we want to display alerts to users
	 */
	prepareLearningDesignData : function(displayErrors) {
		var activities = [],
			transitions = [],
			groupings = [],
			branchMappings = [],
			annotations = [],
			layoutActivityDefs = [],
			systemGate = null,
			error = null,
			weightsSum = null;
		
		// validate if groupings and inputs still exist for activities that need them
		$.each(layout.activities, function(){
			var coreActivity =  this.branchingActivity || this;
			if (coreActivity.grouping || coreActivity.input) {
				var candidate = this.branchingActivity ? coreActivity.start : this,
					groupingFound = false,
					inputFound = false;
				do {
					if (candidate.transitions && candidate.transitions.to.length > 0) {
						candidate = candidate.transitions.to[0].fromActivity;
					} else if (candidate.branchingActivity && !candidate.isStart) {
						candidate = candidate.branchingActivity.start;
					}  else if (!candidate.branchingActivity && candidate.parentActivity) {
						candidate = candidate.parentActivity;
					} else {
						candidate = null;
					}
					
					if (coreActivity.grouping == candidate) {
						groupingFound = true;
					}
					if (coreActivity.input == candidate) {
						inputFound = true;
					}
				} while (candidate != null);
				
				if (coreActivity.grouping && !groupingFound) {
					coreActivity.grouping = null;
					this.draw();
					
					if (displayErrors) {
						layout.ldStoreDialog.modal('hide');
						layout.infoDialog.data('show')(LABELS.GROUPING_DETACHED_ERROR.replace('{0}', coreActivity.title));
					}
					error = true;
					return false;
				}
				if (coreActivity.input && !inputFound) {
					coreActivity.input = null;
					// refresh properties box to remove buttons
					$('input', this.propertiesContent).first().change();
					
					if (displayErrors) {
						layout.ldStoreDialog.modal('hide');
						layout.infoDialog.data('show')(LABELS.INPUT_DETACHED_ERROR.replace('{0}', coreActivity.title));
					}
					error = true;
					return false;
				}
			}
		});
		
		if (error) {
			return false;
		}
		
		
		$.each(layout.activities, function(){
			if (this.parentActivity	&& (this.parentActivity instanceof ActivityDefs.BranchingActivity
							|| this.parentActivity instanceof ActivityDefs.BranchActivity)){
				// remove previously set parent activities as they will be re-set from the start
				this.parentActivity = null;
				this.orderID = null;
			}
			
			if (this.gradebookToolOutputDefinitionWeightable
				&& (this.gradebookToolOutputWeight || this.gradebookToolOutputWeight == 0)) {
				if (weightsSum == null) {
					weightsSum = 0;
				}
				weightsSum += +this.gradebookToolOutputWeight;
			}
		});
		
		if (weightsSum != null && weightsSum != 100) {
			if (displayErrors) {
				layout.ldStoreDialog.modal('hide');
				layout.infoDialog.data('show')(LABELS.WEIGHTS_SUM_ERROR);
			}
			return false;
		}
		
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
									if (displayErrors) {
										layout.infoDialog.data('show')(LABELS.CROSS_BRANCHING_ERROR);
									}
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
							if (displayErrors) {
								layout.infoDialog.data('show')(branchingActivity.title + LABELS.END_MATCH_ERROR);
							}
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
				parentActivityID = activity.parentActivity ? activity.parentActivity.id : null,
				gateActivityCompletionBased = false,
				activityTransitions = activity instanceof ActivityDefs.BranchingActivity ?
						activity.end.transitions : activity.transitions;
			
			if (activity.toolID) {
				activityTypeID = 1;
				iconPath = layout.toolMetadata[activity.learningLibraryID].iconPath;
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
					case 'schedule'   : 
						activityTypeID = 4; 
						gateActivityCompletionBased = activity.gateActivityCompletionBased
							//check the previous activity is available as well
							&& (activityTransitions && activityTransitions.to && activityTransitions.to.length > 0);
						break;
						
					case 'permission' : activityTypeID = 5; break;
					case 'password'   : activityTypeID = 16; break;
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
				'gateActivityCompletionBased' : gateActivityCompletionBased,
				'gatePassword'			 : activity.gateType == 'password' ? activity.password : null,
				'gateActivityLevelID'    : activity instanceof ActivityDefs.GateActivity ? 1 : null,
				'minOptions'			 : activity.minOptions || null,
				'maxOptions'			 : activity.maxOptions || null,
				'stopAfterActivity'		 : activity.stopAfterActivity ? true : false,
				'toolActivityUIID'		 : activity.input ? activity.input.uiid : null,
				'gradebookToolOutputDefinitionName' : activity.gradebookToolOutputDefinitionName == '<NONE>' ?
														null : activity.gradebookToolOutputDefinitionName,
				'gradebookToolOutputWeight' : activity.gradebookToolOutputWeight
			});
			
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
			if (displayErrors) {
				layout.infoDialog.data('show')(error);
			}
			return false;
		}
		
		// iterate over labels and regions
		$.each(layout.labels.concat(layout.regions), function(){
			var box = this.items.shape.getBBox(),
				isRegion = this instanceof DecorationDefs.Region,
				size = isRegion ? null : this.items.shape.attr('font-size');
			if (size) {
				size = size.substring(0, size.indexOf('px'));
			}
			
			annotations.push({
				'id'			 : this.id,
				'annotationUIID' : this.uiid,
				'title' 		 : this.title,
				'xCoord'    	 : parseInt(box.x),
				'yCoord'    	 : parseInt(box.y) + (size ? +size : 0),
				'endXCoord' 	 : isRegion ? parseInt(box.x2) : null,
				'endYCoord' 	 : isRegion ? parseInt(box.y2) : null,
				'color'	    	 : Snap.color(this.items.shape.attr('fill')).hex,
				'size'			 : size
			});
		});
	
		// serialise the sequence
		return {
			'copyTypeID'         : layout.ld.editOverrideLock ? layout.ld.copyTypeID : 1, // don't change the copyTypeId from 2 to 1 if a LiveEdit is in progress
			'maxID'				 : layout.ld.maxUIID,
			'readOnly'			 : false,
			'editOverrideLock'   : layout.ld.editOverrideLock,
			'contentFolderID'    : layout.ld.contentFolderID,
			'licenseID'			 : $('#ldDescriptionLicenseSelect').val(),
			'licenseText'   	 : $('#ldDescriptionLicenseSelect').val() == "0"
								   || $('#ldDescriptionLicenseSelect option:selected').attr('url')
								   ? null : $('#ldDescriptionLicenseText').val(),
			'systemGate'		 : systemGate,
			'activities'		 : activities,
			'transitions'		 : transitions,
			'groupings'			 : groupings,
			'branchMappings'     : branchMappings,
			'annotations'		 : annotations
		};
	},
	
	
	resizeImportPartFrame : function(svgHeight) {
		$('#ldStoreDialogImportPartFrame').height(svgHeight + 40);
	},
	
	
	/**
	 * Sets new paper dimensions and moves some static elements.
	 */
	resizePaper : function(width, height) {
		if (!paper) {
			return;
		}
		
		// the inital window height was saved just before templates were displayed
		var windowHeight = layout.initWindowHeight ? layout.initWindowHeight : $(window).height();
		// next runs use the regular window height
		layout.initWindowHeight = null;
		// height of window minus toolbar, padding...
		$('.templateContainer').height(windowHeight - 80);
		$('#canvas').height(windowHeight - 75)
		// width of window minus templates on the left; minimum is toolbar width so it does not collapse
					.width(Math.max($('#toolbar').width() - 180, $(window).width() - 190));
		
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
		width = Math.max(0, width, canvas.width() - 20);
		height = Math.max(0, height + (isReadOnlyMode ? 20 : 48), canvas.height() - 22);
		
		paper.attr({
			'width'  : width, 
			'height' : height
		});

		if (!isReadOnlyMode){
			if (layout.bin) {
				layout.bin.remove();
			}
			
			// draw rubbish bin on canvas
			layout.bin = paper.image(ActivityIcons.bin, width - 55, height - 55, 48, 48);
			// so it can be found when SVG code gets cloned
			$(layout.bin.node).attr('id', 'rubbishBin');
			
			HandlerLib.resetCanvasMode(true);
		}
	},
	
	
	/**
	 * Stores the sequece in database.
	 */
	saveLearningDesign : function(folderID, learningDesignID, title, readOnly) {
		var ld = GeneralLib.prepareLearningDesignData(true);
		if (!ld) {
			return false;
		}
		var systemGate = ld.systemGate,
			// final success/failure of the save
			result = false;
		
		// it is null if it is a new sequence
		ld.learningDesignID	= learningDesignID;
		ld.workspaceFolderID = folderID;
		ld.title = title.trim();
		ld.description = CKEDITOR.instances['ldDescriptionFieldDescription'].getData();
		ld.saveMode = layout.ld.learningDesignID && layout.ld.learningDesignID != learningDesignID
		   			  ? 1 : 0;
		ld.readOnly = readOnly;
		ld.systemGate = null;
		var data = {
			'ld'     : JSON.stringify(ld)
		};
		data[csrfTokenName] = csrfTokenValue;

		$.ajax({
			type     : 'POST',
			cache    : false,
			async    : false,
			url      : LAMS_URL + "authoring/saveLearningDesign.do",
			dataType : 'json',
			data     : data,
			success : function(response) {
				layout.ld.folderID = folderID;
				layout.ld.title = title;
				layout.ld.invalid = response.validation.length > 0;
				
				// check if there were any validation errors
				if (layout.ld.invalid) {
					var message = LABELS.SEQUENCE_VALIDATION_ISSUES + '<br/>';
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
						message += title + this.message + '<br/>';
					});
					
					layout.infoDialog.data('show')(message);
				}
				
				// if save (even partially) was successful
				if (response.learningDesignID) {
					// assing the database-generated values
					layout.ld.learningDesignID = response.learningDesignID;
					
					if (layout.liveEdit) {
						var missingGroupingOnActivities = GeneralLib.checkTBLGrouping();
						if (missingGroupingOnActivities) {
							var info = LABELS.SAVE_SUCCESSFUL_CHECK_GROUPING;
							$.each(missingGroupingOnActivities, function(){
									info += '<br /> * ' + this.title; 
							});
							layout.infoDialog.data('show')(info);
							// do not close Live Edit if TBL errors appear
							return;
						}
						// let backend know that system gate needs to be removed
						$.ajax({
							type  : 'POST',
							async : false,
							cache : false,
							url : LAMS_URL + 'authoring/finishLearningDesignEdit.do',
							data : {
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
								
								// create the updated LD image
								var svgSaveSuccessful = GeneralLib.saveLearningDesignImage();
								if (!svgSaveSuccessful) {
									layout.infoDialog.data('show')(LABELS.SVG_SAVE_ERROR);
									return;
								}
								
								// set as not modified so dialog will not prompt user on close
								GeneralLib.setModified(false);
								
								// close the Live Edit dialog
								layout.infoDialog.data('show')(LABELS.LIVEEDIT_SAVE_SUCCESSFUL, true);
								setTimeout(function(){
									window.parent.closeDialog('dialogAuthoring');
								}, 5000);
							}
						});
						
						// if it is Live Edit, exit
						return;
					}
					
					var svgSaveSuccessful = GeneralLib.saveLearningDesignImage();
					if (!svgSaveSuccessful) {
						layout.infoDialog.data('show')(LABELS.SVG_SAVE_ERROR);
					}
					
					if (!layout.ld.invalid) {
						var missingGroupingOnActivities = GeneralLib.checkTBLGrouping();
						if (missingGroupingOnActivities) {
							var info = LABELS.SAVE_SUCCESSFUL_CHECK_GROUPING;
							$.each(missingGroupingOnActivities, function(){
								info += '<br /> * ' + this.title; 
							});
							layout.infoDialog.data('show')(info);

						} else {
							layout.infoDialog.data('show')(LABELS.SAVE_SUCCESSFUL, true);
						}
					}
					
					GeneralLib.setModified(false);
					result = true;
				}
			},
			error : function(){
				layout.infoDialog.data('show')(LABELS.SEQUENCE_SAVE_ERROR);
			}
		});
		return result;
	},
	
	
	/**
	 * Stores SVG LD thumbnail on server.
	 */
	saveLearningDesignImage : function() {
		var svg = MenuLib.exportSVG(),
			result = false;
		// check if canvas is not empty
		if (svg === undefined) {
			return false;
		}
		
		// check if we are in SVG recreation mode (Monitoring, Add Lesson)
		// and we need to update activities' coordinates on the back end
		if (isReadOnlyMode && layout.wasArranged) {
			var ld = GeneralLib.prepareLearningDesignData(false);
			if (!ld){
				return false;
			}
			
			var allActivitiesSaved = true;
			$.each(ld.activities, function() {
				var activity = this;
				$.ajax({
					type : 'POST',
					url : LAMS_URL + 'authoring/saveActivityCoordinates.do',
					async: false,
					data : {
						'activity' : JSON.stringify({
							'activityID'  : activity.activityID,
							'xCoord'      : activity.xCoord,
							'yCoord'      : activity.yCoord,
							'startXCoord' : activity.startXCoord,
							'startYCoord' : activity.startYCoord,
							'endXCoord'   : activity.endXCoord,
							'endYCoord'   : activity.endYCoord
						})
					},
					error : function(){
						allActivitiesSaved = false;
					}
				});
				
				if (!allActivitiesSaved) {
					return false;
				}
			});
			
			if (!allActivitiesSaved) {
				return false;
			}
		}
		
		$.ajax({
			type : 'POST',
			url : LAMS_URL + 'authoring/saveLearningDesignImage.do',
			async: false,
			data : {
				'learningDesignID' : layout.ld.learningDesignID,
				'image' : MenuLib.exportSVG()
			},
			success : function(){
				result = true;
			}
		});
		return result;
	},
	

	/**
	 * Tells that current sequence was modified and not saved.
	 */
	setModified : function(modified) {
		if (isReadOnlyMode){
			return;
		}
		layout.modified = modified;
		if (!modified && layout.activities.length > 0) {
			$('#previewButton').prop('disabled', layout.ld.invalid === true);
			$('#exportLamsButton').removeClass('disabled');
			$('#ldDescriptionFieldModified').text('');
		} else {
			$('#previewButton').prop('disabled', true);
			$('#exportLamsButton').addClass('disabled');
			$('#ldDescriptionFieldModified').text('*');
		}
	},
	
	
	/**
	 * Displays sequence image in Open/Save dialog.
	 */
	showLearningDesignThumbnail : function(learningDesignID, title) {
		// display "loading" animation and finally LD thumbnail
		$('#ldScreenshotAuthor, #ldScreenshotLoading', layout.ldStoreDialog).hide();
		
		if (learningDesignID) {
			if ($('#ldStoreDialogImportPartButton', layout.ldStoreDialog).is(':visible')) {
				// get read-only Authoring of the chosen LD and prevent caching
				$('#ldStoreDialogImportPartFrame', layout.ldStoreDialog).attr('src',
				  LAMS_URL + 'authoring/generateSVG.do?selectable=true&learningDesignID='
				  		   + learningDesignID + '&_=' + new Date().getTime());
			} else {
				$('#ldScreenshotLoading', layout.ldStoreDialog).show();
				
				// load the thumbnail
				$.ajax({
					type     : 'POST',
					dataType : 'text',
					url : LD_THUMBNAIL_URL_BASE + learningDesignID,
					cache : false,
					success : function(response) {
						// hide "loading" animation
						$('#ldScreenshotLoading', layout.ldStoreDialog).hide();
						// show the thumbnail
						$('#ldScreenshotAuthor', layout.ldStoreDialog).html(response).show();
					},
					// the LD SVG is missing, try to re-generate it
					error : function(error) {
						// skip re-generate if it is an another error or generated SVG is empty
						if (error.status != 404){
							return;
						}
						// iframe just to load another instance of Authoring for a single purpose, generate the SVG
						$('<iframe />').appendTo('body').css('visibility', 'hidden').load(function(){
							// call svgGenerator.jsp code to store LD SVG on the server
							var frame = $(this),
								win = frame[0].contentWindow || frame[0].contentDocument;
							    // when LD opens, make a callback which save the thumbnail and displays it in current window
								win.GeneralLib.openLearningDesign(learningDesignID, function(){
									result = win.GeneralLib.saveLearningDesignImage();
									frame.remove();
									if (result) {
										// load the image again
										GeneralLib.showLearningDesignThumbnail(learningDesignID);
									} else {
										$('#ldScreenshotLoading', layout.ldStoreDialog).hide();
									}
								});
						}).attr('src', LAMS_URL + 'authoring/generateSVG.do?selectable=false');
					}
				});
			}
			
			if (title) {
				// copy title of the highligthed sequence to title field
				$('#ldStoreDialogNameContainer input', layout.ldStoreDialog).val(title).focus();
			}
			
			var tree =  layout.ldStoreDialog.data('ldTree'),
				ldNode = tree.getHighlightedNode();
				// no LD was chosen
			if (ldNode && learningDesignID != ldNode.data.learningDesignId) {
				ldNode.unhighlight(true);
			}
		}
		
		$('#ldStoreDialogAccessDiv > div.access', layout.ldStoreDialog).each(function(){
			var access = $(this);
			if (+access.data('learningDesignId') == learningDesignID){
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
				url : LAMS_URL + "authoring/getLearningDesignAccess.do",
				dataType : 'json',
				data : {
				},
				success : function(response) {
					access = response;
				}
			});
		}
		
		if (access) {
			var accessCell = $('#ldStoreDialogAccessDiv', layout.ldStoreDialog);
			accessCell.children('div.access').remove();
			$.each(access, function(){
				$('<div />').addClass('access')
							.data({
								'learningDesignId' : this.learningDesignId,
								'folderID'         : this.workspaceFolderId
							})
							.text(this.title)
							.appendTo(accessCell)
							.click(function(){
								var accessEntry = $(this);
								var isOpenDialog = $('#ldStoreDialogSaveButton', layout.ldStoreDialog).is(':hidden');
								
								//if item is already selected - either deselect it (in case of save dialog) or don't do anything (in case of open dialog)
								if (accessEntry.hasClass('selected')) {
									if (isOpenDialog) {
										return;
									} else {
										accessEntry.removeClass('selected');
										GeneralLib.showLearningDesignThumbnail(null, null);
										return;
									}
								}
								
								accessEntry.addClass('selected');
								
								var dialog = layout.ldStoreDialog,
									tree = dialog.data('ldTree'),
									node = tree.getHighlightedNode(),
									learningDesignID = +accessEntry.data('learningDesignId'),
									title = accessEntry.text();
									
								if (node) {
									node.unhighlight(true);
								}
									
								GeneralLib.showLearningDesignThumbnail(learningDesignID, title);
							})
							.dblclick(function(){
								$(this).trigger( "click" );
								
								// open/save sequence
								var buttonToClick = $('#ldStoreDialogSaveButton', layout.ldStoreDialog).is(':visible') 
										? '#ldStoreDialogSaveButton' : '#ldStoreDialogOpenButton'; 
								$(buttonToClick, $('#ldStoreDialogContents')).trigger( "click" );
							});
			});
		}
	},
	
	/**
	 *  Checks if activity, LD etc. name is correct
	 */
	 validateName : function(name) {
		 return name && GeneralLib.nameValidator.test(name);
	 }
};
