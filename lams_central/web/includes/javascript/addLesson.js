﻿// ********** MAIN FUNCTIONS **********
var tree,
	lastSelectedUsers = {},
	sortOrderAscending = {},
	generatingLearningDesign = false;
	submitInProgress = false,
	originalThumbnailWidth = 0,
	originalThumbnailHeight = 0,


/**
 * For tabs changing.
 */
function doSelectTab(tabId) {
	selectTab(tabId);
}

/**
 * Sets up widgets on the main.jsp page
 */
function initLessonTab(){
	
	// generate LD initial tree; folderContents is declared in newLesson.jsp
	var treeNodes = parseFolderContents(folderContents);
	// there should be no focus, just highlight
	YAHOO.widget.TreeView.FOCUS_CLASS_NAME = null;
	tree = new YAHOO.widget.TreeView('learningDesignTree', treeNodes);
	tree.setDynamicLoad(function(node, callback){
		// load subfolder contents
		$.ajax({
			url : LAMS_URL + 'home.do',
			data : {
				'method' : 'getFolderContents',
				'folderID' : node.data.folderID
			},
			cache : false,
			async: false,
			dataType : 'json',
			success : function(result) {
				var childNodeData = parseFolderContents(result);
				$.each(childNodeData, function(){
						new YAHOO.widget.TextNode(this, node);
					});
				}
			}
		);
		
		// required by YUI
		callback();
	});
	
	tree.singleNodeHighlight = true;
	tree.subscribe('clickEvent', function(event){
		if (!event.node.data.learningDesignId){
			// it is a folder
			return false;
		}
		
		$('#lessonNameInput').val(event.node.label);
		
		// display "loading" animation and finally LD thumbnail
		$('.ldChoiceDependentCanvasElement').css('display', 'none');
		if (event.node.highlightState == 0) {
			loadLearningDesignSVG(event.node.data.learningDesignId);
		} else {
			toggleCanvasResize(CANVAS_RESIZE_OPTION_NONE);
		}
	});
	tree.subscribe('clickEvent',tree.onEventToggleHighlight);
	tree.render();
	
	// expand the first (user) folder
	tree.getRoot().children[0].expand();
}


function initClassTab(){
	// users variable is declared in newLesson.jsp
	fillUserContainer(users.selectedLearners, 'selected-learners');
	fillUserContainer(users.unselectedLearners, 'unselected-learners');
	fillUserContainer(users.selectedMonitors, 'selected-monitors');
	fillUserContainer(users.unselectedMonitors, 'unselected-monitors');
	
	// allow dragging of user divs
	$('.draggableUser').each(function(){
		$(this).draggable({ 'scope'       : getDraggableScope($(this).parents('.userContainer').attr('id')),
							'appendTo'    : 'body',
							'containment' : '#classTable',
						    'revert'      : 'invalid',
						    'distance'    : 20,
						    'scroll'      : false,
						    'cursor'      : 'move',
							'helper'      : function(event){
								// include the user from which dragging started
								$(this).addClass('draggableUserSelected');
								
								// copy selected users
								var helperContainer = $('<div />');
								$(this).siblings('.draggableUserSelected').andSelf().each(function(){
									$(this).clone().appendTo(helperContainer);
								});
								return helperContainer;
							}	
		}).click(function(event){
			var wasSelected = $(this).hasClass('draggableUserSelected');
			var parentId = $(this).parent().attr('id');
			// this is needed for shift+click
			var lastSelectedUser = lastSelectedUsers[parentId];
			
			if (event.shiftKey && lastSelectedUser && lastSelectedUser != this) {
				// clear current selection
				$(this).siblings().andSelf().removeClass('draggableUserSelected');
				
				// find range of users to select
				var lastSelectedIndex = $(lastSelectedUser).index();
				var index = $(this).index();
				
				var startingElem = lastSelectedIndex > index ? this : lastSelectedUser;
				var endingElem = lastSelectedIndex > index ? lastSelectedUser : this;
				
				$(startingElem).nextUntil(endingElem).andSelf().add(endingElem)
					.addClass('draggableUserSelected');
			} else {
				if (!event.ctrlKey) {
					// clear current sleection
					$(this).siblings().andSelf().removeClass('draggableUserSelected');
				}
				
				if (wasSelected && !event.shiftKey){
					$(this).removeClass('draggableUserSelected');
					lastSelectedUsers[parentId] = null;
				} else {
					$(this).addClass('draggableUserSelected');
					lastSelectedUsers[parentId] = this;
				}
			}
		});
	});
	
	// allow putting dragged users into container divs
	$('.userContainer').each(function(){
		var containerId = $(this).attr('id');
		
		$(this).droppable({'scope'       : containerId,
						   'activeClass' : 'droppableHighlight',
						   'tolerance'   : 'touch',
						   'accept'      : function (draggable) {
							   return acceptDraggable(draggable, containerId);
						   },
						   'drop'        : function () {
							   transferUsers(containerId);
						   }
		});
	});
	
	$('.sortUsersButton').click(function(){
		sortUsers($(this).attr('id'));
	});
}


function initAdvancedTab(){
	CKEDITOR.on('instanceReady', function(e){
		// CKEditor needs to load first, then only hide the whole div to prevent errors
		$('#introDescriptionDiv').hide();
	});
	
	$('#splitLearnersCountField').attr({
		'step'        : 1,
		'min'         : 1,
		'max'         : users.selectedLearners ? users.selectedLearners.length : 1
	}).change(updateSplitLearnersFields);
	
	$('#splitLearnersField').change(function(){
		if ($(this).is(':checked')) {
			$('#splitLearnersTable').show('slow');
			updateSplitLearnersFields();
		} else {
			$('#splitLearnersTable').hide('slow');
		}
	});
	
	$('#introEnableField').change(function(){
		if ($(this).is(':checked')) {
			$('#introDescriptionDiv').show('slow');
		} else {
			$('#introDescriptionDiv').hide('slow');
		}
	});
	
	$('#presenceEnableField').change(function(){
		$('#imEnableField').prop('disabled', !$(this).is(':checked'));
	});
	
	$('#schedulingEnableField').change(function(){
		if ($(this).is(':checked')) {
			$('#schedulingDatetimeField').show('slow');
		} else {
			$('#schedulingDatetimeField').hide('slow');
		}
	});
	
	$('#startMonitorField').change(function(){
		var checked = !$(this).is(':checked');
		if (!checked) {
			$('#schedulingEnableField, #precedingLessonEnableField, ' +
	          '#timeLimitEnableField, #timeLimitIndividualField').prop('checked', false).change();
			$('#schedulingDatetimeField').val(null);
		}
		
		$('#schedulingEnableField, #precedingLessonEnableField, #timeLimitEnableField, #timeLimitIndividualField,' +
		  '#precedingLessonIdField, #schedulingDatetimeField').prop('disabled', !checked);
	});
	
	$('#schedulingDatetimeField').datetimepicker({
		'minDate' : 0
	});
}

function initConditionsTab(){
	$('#precedingLessonEnableField').change(function(){
		if ($(this).is(':checked')) {
			$('#precedingLessonIdField').show('slow');
		} else {
			$('#precedingLessonIdField').hide('slow');
		}
	});
	
	$('#timeLimitDaysField').attr({
		'min'         : 0,
		'max'         : 180,
		'step'		  : 1
	}).val(30);
	
	$('#timeLimitEnableField').change(function(){
		if ($(this).is(':checked')) {
			$('#timeLimitDiv').show('slow');
		} else {
			$('#timeLimitDiv').hide('slow');
		}
	});
}


function addLesson(){
	// prevent double clicking of Add button
	if (submitInProgress) {
		return;
	}
	// some validation at first
	var lessonName = $('#lessonNameInput').val();
	if (lessonName){
		$('#lessonNameInput').removeClass('errorField');
	}
	
	var ldNode = tree.getHighlightedNode();
	if (!ldNode || !ldNode.data.learningDesignId) {
		$('#ldNotChosenError').show();
		doSelectTab(1);
		return;
	}
	$('#ldIdField').val(ldNode.data.learningDesignId);
	
	if (lessonName){
		var nameValidator = /^[^<>^*@%$]*$/igm;	
		if (!nameValidator.test(lessonName)) {
			$('#lessonNameInput').addClass('errorField');
			doSelectTab(1);
			alert(LABEL_NAME_INVALID_CHARACTERS);
			return;
		}
	} else {
		$('#lessonNameInput').addClass('errorField');
		doSelectTab(1);
		return;
	}
	$('#lessonNameField').val(lessonName);
	
	
	var learners = getSelectedUserList('selected-learners');
	if (learners == ''){
		$('<div />').addClass('errorMessage')
		            .text(LABEL_MISSING_LEARNERS)
		            .appendTo('#selected-learners');
		doSelectTab(2);
		return;
	}
	$('#learnersField').val(learners);
	
	var monitors = getSelectedUserList('selected-monitors');
	if (monitors == ''){
		$('<div />').addClass('errorMessage')
		            .text(LABEL_MISSING_MONITORS)
		            .appendTo('#selected-monitors');
		doSelectTab(2);
		return;
	}
	$('#monitorsField').val(monitors);
	
	if ($('#splitLearnersField').is(':checked')) {
		var maxLearnerCount = $('#selected-learners div.draggableUser').length,
			learnerCount = $('#splitLearnersCountField').val(),
			instances = Math.ceil(maxLearnerCount/learnerCount);
		$('#splitNumberLessonsField').val(instances);
	}
	
	// copy CKEditor contents to textarea for submit
	$('#introDescription').val(CKEDITOR.instances['introDescription'].getData());

	submitInProgress = true;
	$('#lessonForm').ajaxSubmit({
		'success' : function(){
			window.parent.closeDialog('dialogAddLesson', true);
	}});
}

// ********** LESSON TAB FUNCTIONS **********

function loadLearningDesignSVG(ldId) {
	
	$.ajax({
		dataType : 'text',
		url : LAMS_URL + 'home.do',
		async : false,
		cache : false,
		data : {
			'method'    : 'getLearningDesignThumbnail',
			'ldId'      : ldId,
			'_t'		: new Date().getTime()
			
		},
		success : function(response) {
			// hide "loading" animation
			$('.ldChoiceDependentCanvasElement').css('display', 'none');
			generatingLearningDesign = false;
			
			// show the thumbnail
			$('#ldScreenshotAuthor').html(response);
			$('#ldScreenshotAuthor').css('display', 'block').css('width', 'auto').css('height', 'auto');

			originalThumbnailWidth = $('svg','#ldScreenshotAuthor').attr('width');
			originalThumbnailHeight = $('svg','#ldScreenshotAuthor').attr('height');

			// resize if needed
			var resized = resizeSequenceThumbnail();
			toggleCanvasResize(resized ? CANVAS_RESIZE_OPTION_FIT
					: CANVAS_RESIZE_OPTION_NONE);
		},
		error : function(error) {

			// the LD SVG is missing, try to re-generate it; if it is an another error, fail
			if (error.status != 404 || generatingLearningDesign ) {
				$('.ldChoiceDependentCanvasElement').css('display', 'none');
				$('#ldCannotLoadSVG').css('display', 'inline');
				generatingLearningDesign = false;
				return;
			}

			generatingLearningDesign = true;
			// iframe just to load Authoring for a single purpose, generate the SVG
			$('<iframe />').appendTo('body').load(function(){
				// call svgGenerator.jsp code to store LD SVG on the server
				var frame = $(this),
					win = frame[0].contentWindow || frame[0].contentDocument;
				win.GeneralLib.saveLearningDesignImage();
				frame.remove();
				// load the image again, avoid caching
				loadLearningDesignSVG(ldId);
			}).attr('src', LAMS_URL 
						   + 'authoring/author.do?method=generateSVG&selectable=false&learningDesignID='
						   + ldId);
		}
	});
	
}

function resizeSequenceThumbnail(reset) {

	var returnValue = false;
	var svg = $('svg','#ldScreenshotAuthor');
	if ( svg ) {
		if ( reset ) {
			svg.attr('width',originalThumbnailWidth);
			svg.attr('height',originalThumbnailHeight);
			if ( originalThumbnailWidth > 550 ) {
				$('#ldScreenshotAuthor').css('width', 550).css('height', originalThumbnailHeight);
			}
		} else { 
			var svgWidth = svg.attr('width'),
				svgHeight = svg.attr('height');
			if ( svgWidth > 550 ) {
				svg.attr('width', 550);
				svg.attr('height', Math.ceil(svgHeight * (550 / svgWidth)));
				returnValue = true;
			} 
			$('#ldScreenshotAuthor').css('width', 'auto').css('height', 'auto');
		}
	}
	
	return returnValue;
}


/**
 * Chooses whether LD thumbnail will be shrinked or full size.
 */
function toggleCanvasResize(mode) {

	var toggleCanvasResizeLink = $('#toggleCanvasResizeLink');
	switch (mode) {
	case CANVAS_RESIZE_OPTION_NONE:
		toggleCanvasResizeLink.css('display', 'none');
		break;
	case CANVAS_RESIZE_OPTION_FIT:
		toggleCanvasResizeLink.html(CANVAS_RESIZE_LABEL_FULL).one('click',
				function() {
					toggleCanvasResize(CANVAS_RESIZE_OPTION_FULL)
				});
		toggleCanvasResizeLink.css('display', 'inline');
		resizeSequenceThumbnail();
		break;
	case CANVAS_RESIZE_OPTION_FULL:
		toggleCanvasResizeLink.html(CANVAS_RESIZE_LABEL_FIT).one('click',
				function() {
					toggleCanvasResize(CANVAS_RESIZE_OPTION_FIT)
				});
		toggleCanvasResizeLink.css('display', 'inline');
		resizeSequenceThumbnail(true);
		break;
	}
}


/**
 * Parses response in JSON format into readable for YUI.
 */
function parseFolderContents(nodeJSON) {
	var result = [];

	if (nodeJSON.folders) {
		$.each(nodeJSON.folders, function(){
			result.push({'type'            : 'text',
					  	 'label'           : this.isRunSequencesFolder ?
					  			 				LABEL_RUN_SEQUENCES_FOLDER : this.name,
					  	 'folderID'		   : this.folderID
						 });
		});
	}
	if (nodeJSON.learningDesigns) {
		$.each(nodeJSON.learningDesigns, function(){
			result.push({'type'             : 'text',
			  	         'label'            : this.name,
			  	         'isLeaf'           : true,
			  	         'learningDesignId' : this.learningDesignId
				        });
		});
	}
	
	return result;
}

// ********** CLASS TAB FUNCTIONS **********

function fillUserContainer(users, containerId) {
	if (users) {
		// create user DIVs
		$.each(users, function(index, userJSON) {
			$('#' + containerId).append(
					$('<div />').attr({
									'userId'  : userJSON.userID
									})
			                    .addClass('draggableUser')
    						    .text(userJSON.firstName + ' ' + userJSON.lastName 
    						    		  + ' (' + userJSON.login + ')'
    						    	 )
    		);
		});
		
		sortUsers('sort-' + containerId);
	}
}


function getDraggableScope(containerId) {
	// switch scope to opposite after drop, so user can be dragged back if needed
	var scopeParts = containerId.split('-');
	return (scopeParts[0] == 'selected' ? 'unselected' : 'selected') + '-' + scopeParts[1];
}


function colorDraggableUsers(container) {
	// every second line is different
	$(container).find('div.draggableUser').each(function(index, userDiv){
		// exact colour should be defined in CSS, but it's easier this way...
		$(this).css('background-color', index % 2 ? '#dfeffc' : 'inherit');
	});
}


function getSelectedUserList(containerId) {
	var list = '';
	$('#' + containerId).children('div.draggableUser').each(function(){
		list += $(this).attr('userId') + ',';
	});
	return list;
}

function sortUsers(buttonId) {
	var container = $('#' + buttonId.substring(buttonId.indexOf('-') + 1));
	var users = container.children('div.draggableUser');
	if (users.length > 1) {
		var sortOrderAsc = sortOrderAscending[buttonId];
		
		users.each(function(){
			$(this).detach();
		}).sort(function(a, b){
			var keyA = $(a).text().toLowerCase();
			var keyB = $(b).text().toLowerCase();
			var result = keyA > keyB ? 1 : keyA < keyB ? -1 : 0;
			return sortOrderAsc ? -result : result;
		}).each(function(){
			$(this).appendTo(container);
		});
		
		var button = $('#' + buttonId);
		if (sortOrderAsc) {
			button.html('▼');
			sortOrderAscending[buttonId] = false;
		} else {
			button.html('▲');
			sortOrderAscending[buttonId] = true;
		}
		
		colorDraggableUsers(container);
	}
}

function acceptDraggable(draggable, toContainerId) {
	// forbid current user from being removed from monitors
	return toContainerId != 'unselected-monitors'
		|| $(draggable).attr('userId') != userId;
}

function transferUsers(toContainerId) {
	var toContainer = $('#' + toContainerId);
	var fromContainerId = getDraggableScope(toContainerId);
	var fromContainer = $('#' + fromContainerId);
	var selectedUsers =  fromContainer.children('.draggableUserSelected');
	if (selectedUsers.length > 0){
	   // remove error message, if exists
	   toContainer.children('.errorMessage').remove();
	   
	   // move the selected users
	   selectedUsers.each(function(){
		   if (acceptDraggable(this, toContainerId)) {
			  $(this).css({'top'  : '0px',
	              		   'left' : '0px',
	          })
	          .draggable('option', 'scope', fromContainerId)
	          .appendTo(toContainer);
		   }
	   });
	            
	   
	   // recolour both containers
	   toContainer.children().removeClass('draggableUserSelected');
	   colorDraggableUsers(toContainer);
	   colorDraggableUsers(fromContainer);
	   
	   if (toContainerId.indexOf('learners') > 0) {
		   // number of selected learners changed, so update this control too
		   updateSplitLearnersFields();
	   }
	}
}

// ********** ADVANCED TAB FUNCTIONS **********

function updateSplitLearnersFields(){
	if ($('#splitLearnersField').is(':checked')) {
		// put users into groups
		var maxLearnerCount = $('#selected-learners div.draggableUser').length,
			learnerCount = $('#splitLearnersCountField').val(),
			instances = Math.ceil(maxLearnerCount/learnerCount);
		learnerCount = Math.ceil(maxLearnerCount/instances);
		var description = SPLIT_LEARNERS_DESCRIPTION.replace('[0]', instances).replace('[1]', learnerCount);
		$('#splitLearnersDescription').html(description);
	}
}