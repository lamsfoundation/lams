// ********** MAIN FUNCTIONS **********
var tree;
var lastSelectedUsers = [];
var submitInProgress = false;
		
function initLessonTab(){
	$('#ldScreenshotAuthor').load(function(){
		// hide "loading" animation
		$('.ldChoiceDependentCanvasElement').css('display', 'none');
		// show the thumbnail
		$('#ldScreenshotAuthor').css('display', 'inline');
		// resize if needed
		var resized = resizeImage('ldScreenshotAuthor', 400, null);
		toggleCanvasResize(resized ? CANVAS_RESIZE_OPTION_FIT
				: CANVAS_RESIZE_OPTION_NONE);
	});
	
	// generate LD tree; folderContents is declared in newLesson.jsp
	var treeNodes = parseFolderTreeNode(folderContents);
	// there should be no focus, just highlight
	YAHOO.widget.TreeView.FOCUS_CLASS_NAME = null;
	tree = new YAHOO.widget.TreeView('learningDesignTree', treeNodes);
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
			$('#ldScreenshotLoading').css('display', 'inline');
			$('#ldScreenshotAuthor').attr('src', LD_THUMBNAIL_URL_BASE + event.node.data.learningDesignId);
			$('#ldScreenshotAuthor').css('width', 'auto').css('height', 'auto');
		} else {
			toggleCanvasResize(CANVAS_RESIZE_OPTION_NONE);
		}
	});
	tree.subscribe('clickEvent',tree.onEventToggleHighlight);
	tree.render();
	
	// if empty folders were empty in the start, they would have been displayed as leafs
	// instead, there is a dummy element in the start which is removed
	// when user opens the folder
	var emptyFolderNodes = tree.getNodesBy(function(node){
		var firstNode = node.children[0];
		return firstNode && firstNode.data.isDummy;
	});
	
	$.each(emptyFolderNodes, function(){
		this.setDynamicLoad(function(node, callback){
			tree.removeChildren(node);
			callback();
		});
	});
	
	tree.getRoot().children[0].expand();
}


function initClassTab(){
	// users variable is declared in newLesson.jsp
	fillUserContainer(users.selectedLearners, 'selected-learners');
	fillUserContainer(users.unselectedLearners, 'unselected-learners');
	fillUserContainer(users.selectedMonitors, 'selected-monitors');
	fillUserContainer(users.unselectedMonitors, 'unselected-monitors');
	
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
	
	$('.userContainer').each(function(){
		colorDraggableUsers(this);
		
		$(this).droppable({'scope'       : $(this).attr('id'),
						   'activeClass' : 'droppableHighlight',
						   'tolerance'   : 'touch',
						   'accept'      : function (draggable) {
							   // forbid current user from being removed from monitors
							   return $(this).attr('id') != 'unselected-monitors'
							       || $(draggable).attr('userId') != userId;
						   },
						   'drop'        : function (event, ui) {
							   // remove error message, if exists
							   $(this).children('.errorMessage').remove();
							   
							   // move the selected users
							   var previousContainer = ui.draggable.parent();
							   previousContainer.children('.draggableUserSelected')
							            .css({'top'  : '0px',
								              'left' : '0px',
							                 })
							            .draggable('option', 'scope', getDraggableScope($(this).attr('id')))
							            .appendTo(this);
							   
							   // recolour both containers
							   $(this).children().removeClass('draggableUserSelected');
							   colorDraggableUsers(this);
							   colorDraggableUsers(previousContainer);
							   
							   if ($(this).attr('id').indexOf('learners') > 0) {
								   // number of selected learners changed, so update this control too
								   updateSplitLearnersFields();
							   }
						   }
		});
	});
}


function initAdvancedTab(){
	$('#splitLearnersCountField').spinner({
		'disabled'    : true,
		'incremental' : false,
		'min'         : 1,
		'max'         : users.selectedLearners ? users.selectedLearners.length : 1,
		'stop'        : updateSplitLearnersFields
	}).spinner('value', 1);
	
	$('#splitLearnersField').change(function(){
		updateSplitLearnersFields();
	});
	
	$('#introEnableField').change(function(){
		$('#introSection input, #introSection textarea').prop('disabled', !$(this).is(':checked'));
	});
	
	$('#presenceEnableField').change(function(){
		$('#imEnableField').prop('disabled', !$(this).is(':checked'));
	});
	
	$('#schedulingEnableField').change(function(){
		$('#schedulingDatetimeField').val(null).prop('disabled', !$(this).is(':checked'));
	});
	
	$('#startMonitorField').change(function(){
		var checked = !$(this).is(':checked');
		if (!checked) {
			$('#schedulingEnableField, #precedingLessonEnableField, ' +
	          '#timeLimitEnableField, #timeLimitIndividualField').attr('checked', false);
			$('#timeLimitIndividualField, #precedingLessonIdField, #schedulingDatetimeField').prop('disabled', true);
			$('#timeLimitDaysField').spinner('disable');
			$('#schedulingDatetimeField').val(null);
		}
		
		$('#schedulingEnableField, #precedingLessonEnableField, #timeLimitEnableField').prop('disabled', !checked);
	});
	
	$('#schedulingDatetimeField').datetimepicker({
		'minDate' : 0
	});
}

function initConditionsTab(){
	$('#precedingLessonEnableField').change(function(){
		$('#precedingLessonIdField').prop('disabled', !$(this).is(':checked'));
	});
	
	$('#timeLimitDaysField').spinner({
		'disabled'    : true,
		'min'         : 0,
		'max'         : 180
	}).spinner('value', 30);
	
	$('#timeLimitEnableField').change(function(){
		$('#timeLimitDaysField').spinner($(this).is(':checked') ? 'enable' : 'disable');
		$('#timeLimitIndividualField').prop('disabled', !$(this).is(':checked'));
	});
}


function addLesson(){
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
		$('#tabs').tabs('option', 'selected', 0);
		return;
	}
	$('#ldIdField').val(ldNode.data.learningDesignId);
	
	if (!lessonName){
		$('#lessonNameInput').addClass('errorField');
		$('#tabs').tabs('option', 'selected', 0);
		return;
	}
	$('#lessonNameField').val(lessonName);
	
	
	var learners = getSelectedUserList('selected-learners');
	if (learners == ''){
		$('<div />').addClass('errorMessage')
		            .text(LABEL_MISSING_LEARNERS)
		            .appendTo('#selected-learners');
		$('#tabs').tabs('option', 'selected', 1);
		return;
	}
	$('#learnersField').val(learners);
	
	var monitors = getSelectedUserList('selected-monitors');
	if (monitors == ''){
		$('<div />').addClass('errorMessage')
		            .text(LABEL_MISSING_MONITORS)
		            .appendTo('#selected-monitors');
		$('#tabs').tabs('option', 'selected', 1);
		return;
	}
	$('#monitorsField').val(learners);
	
	if ($('#splitLearnersField').is(':checked')) {
		var maxLearnerCount = $('#selected-learners div.draggableUser').length;
		var learnerCount = $('#splitLearnersCountField').spinner('value');
		var instances = Math.ceil(maxLearnerCount/learnerCount);
		$('#splitNumberLessonsField').val(instances);
	}
	
	submitInProgress = true;
	$('#lessonForm').ajaxSubmit({
		'success' : function(){
			window.parent.closeAddLessonDialog(true);
	}});
}

// ********** LESSON TAB FUNCTIONS **********

function resizeImage(id, width, height) {
	var resized = false;
	var elem = $('#' + id);

	if (width != null && elem.width() > width) {
		elem.css('width', width);
		resized = true;
	}
	if (height != null && elem.height() > height) {
		elem.css('height', height);
		resized = true;
	}
	return resized;
}


function toggleCanvasResize(mode) {
	var toggleCanvasResizeLink = $('#toggleCanvasResizeLink');
	switch (mode) {
	case CANVAS_RESIZE_OPTION_NONE:
		toggleCanvasResizeLink.css('display', 'none');
		$('#canvasDiv').css('overflow', 'visible');
		break;
	case CANVAS_RESIZE_OPTION_FIT:
		toggleCanvasResizeLink.html(CANVAS_RESIZE_LABEL_FULL).one('click',
				function() {
					toggleCanvasResize(CANVAS_RESIZE_OPTION_FULL)
				});
		toggleCanvasResizeLink.css('display', 'inline');
		resizeImage('ldScreenshotAuthor', 385, null);
		$('#canvasDiv').css('overflow', 'visible');
		break;
	case CANVAS_RESIZE_OPTION_FULL:
		toggleCanvasResizeLink.html(CANVAS_RESIZE_LABEL_FIT).one('click',
				function() {
					toggleCanvasResize(CANVAS_RESIZE_OPTION_FIT)
				});
		toggleCanvasResizeLink.css('display', 'inline');
		$('#ldScreenshotAuthor').css('width', 'auto').css('height', 'auto');
		$('#canvasDiv').css('overflow', 'auto');
		break;
	}
}


function parseFolderTreeNode(nodeJSON) {
	var result = [];
	
	if (!nodeJSON.folders && !nodeJSON.learningDesigns) {
		// add dummy node, otherwise empty folder is displayed as a leaf
		result.push({'type'    : 'text',
			         'label'   : '(dummy)',
				     'isDummy' : true
				    });
	} else {
		if (nodeJSON.folders) {
			$.each(nodeJSON.folders, function(){
				result.push({'type'            : 'text',
						  	 'label'           : this.name,
						     'children'        : parseFolderTreeNode(this)
							 });
			});
		}
		if (nodeJSON.learningDesigns) {
			$.each(nodeJSON.learningDesigns, function(){
				result.push({'type'             : 'text',
				  	         'label'            : this.name,
				  	         'learningDesignId' : this.learningDesignId
					        });
			});
		}
	}
	
	return result;
}

// ********** CLASS TAB FUNCTIONS **********

function fillUserContainer(users, containerID) {
	if (users) {
		// create user DIVs
		$.each(users, function(index, userJSON) {
			$('#' + containerID).append($('<div />').attr('userId', userJSON.userID)
					                      .addClass('draggableUser')
            						      .text(userJSON.firstName + ' ' + userJSON.lastName 
            						    		  + ' (' + userJSON.login + ')')
            						   );
		});
	}
}


function getDraggableScope(containerId) {
	// switch scope to opposite after drop, so user can be dragged back if needed
	var scopeParts = containerId.split('-');
	return (scopeParts[0] == 'selected' ? 'unselected' : 'selected') + '-' + scopeParts[1];
}


function colorDraggableUsers(container) {
	// every second line is different
	$(container).find('div').each(function(index, userDiv){
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

// ********** ADVANCED TAB FUNCTIONS **********

function updateSplitLearnersFields(){
	var splitEnabled = $('#splitLearnersField').is(':checked');
	if (splitEnabled) {
		$('#splitLearnersCountField').spinner('enable');
		
		// put users into groups
		var maxLearnerCount = $('#selected-learners div.draggableUser').length;
		var learnerCount = $('#splitLearnersCountField').spinner('option', 'max', maxLearnerCount < 1 ? 1 : maxLearnerCount)
		                                            .spinner('value');
		var instances = Math.ceil(maxLearnerCount/learnerCount);
		learnerCount = Math.ceil(maxLearnerCount/instances);
		var description = SPLIT_LEARNERS_DESCRIPTION.replace('[0]', instances).replace('[1]', learnerCount);
		$('#splitLearnersDescription').html(description).show();
	} else {
		$('#splitLearnersCountField').spinner('disable');
		$('#splitLearnersDescription').hide();
	}
}