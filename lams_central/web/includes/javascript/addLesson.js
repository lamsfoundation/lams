﻿﻿// ********** MAIN FUNCTIONS **********
var tree,
	lastSelectedUsers = {},
	sortOrderAscending = {},
	generatingLearningDesign = false;
	submitInProgress = false,
	originalThumbnailWidth = 0,
	originalThumbnailHeight = 0;

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
	tree = $('#learningDesignTree');
	
	// customise treeview label
	ldTreeview.LABEL_RUN_SEQUENCES_FOLDER = LABEL_RUN_SEQUENCES_FOLDER;
	
	ldTreeview.init('#learningDesignTree', 
	   function(event, node) {
			// hide existing LD image
			$('.ldChoiceDependentCanvasElement').css('display', 'none');
			
			// if a LD is selected
			if (node.state.selected && node.learningDesignId) {
				$('#lessonNameInput').val(node.label);
				//focus element only if it's visible in the current viewport (to avoid unwanted scrolling)
				if (isElementInViewport($('#lessonNameInput'))) {
					$('#lessonNameInput').focus();
				}
				// display "loading" animation and finally LD thumbnail
				loadLearningDesignSVG(node.learningDesignId);
			} else {
				// a folder got selected or LD got deselected
				$('#lessonNameInput').val(null);
				toggleCanvasResize(CANVAS_RESIZE_OPTION_NONE);
			}
		},
	  function(event, node) {
			if (!node.learningDesignId){
				if (!node.state.expanded) {
					ldTreeview.refresh(tree, node);
				}
				return;
			}
			
			// start lesson
			addLesson(node.learningDesignId, node.label);
	  });
	
	// ability to start a lesson on pressing Enter button in a lesson name input
	$('#lessonNameInput').on('keyup', function (e) {
	    if (e.keyCode == 13) {
	    	 addLesson();
	    }
	});
}

//checks whether element is visible in the current viewport
function isElementInViewport(el) {
	el = el[0];
    var rect = el.getBoundingClientRect();

    return (
        rect.top >= 0 &&
        rect.left >= 0 &&
        rect.bottom <= (window.innerHeight || document.documentElement.clientHeight) && /*or $(window).height() */
        rect.right <= (window.innerWidth || document.documentElement.clientWidth) /*or $(window).width() */
    );
}


function initClassTab(){
	// users variable is declared in newLesson.jsp
	fillUserContainer(users.selectedLearners, 'selected-learners');
	fillUserContainer(users.unselectedLearners, 'unselected-learners');
	fillUserContainer(users.selectedMonitors, 'selected-monitors');
	fillUserContainer(users.unselectedMonitors, 'unselected-monitors');
	
	// allow dragging of user divs
	$('.draggableItem').each(function(){
		$(this).draggable({ 'scope'       : getDraggableScope($(this).parents('.userContainer').attr('id')),
							'appendTo'    : 'body',
							'containment' : '#classTable',
						    'revert'      : 'invalid',
						    'distance'    : 20,
						    'scroll'      : false,
						    'cursor'      : 'move',
							'helper'      : function(event){
								// include the user from which dragging started
								$(this).addClass('draggableSelected');
								
								// copy selected users
								var helperContainer = $('<div />');
								$(this).siblings('.draggableSelected').andSelf().each(function(){
									$(this).clone().appendTo(helperContainer);
								});
								return helperContainer;
							}	
		}).click(function(event){
			var wasSelected = $(this).hasClass('draggableSelected');
			var parentId = $(this).parent().attr('id');
			// this is needed for shift+click
			var lastSelectedUser = lastSelectedUsers[parentId];
			
			if (event.shiftKey && lastSelectedUser && lastSelectedUser != this) {
				// clear current selection
				$(this).siblings().andSelf().removeClass('draggableSelected');
				
				// find range of users to select
				var lastSelectedIndex = $(lastSelectedUser).index();
				var index = $(this).index();
				
				var startingElem = lastSelectedIndex > index ? this : lastSelectedUser;
				var endingElem = lastSelectedIndex > index ? lastSelectedUser : this;
				
				$(startingElem).nextUntil(endingElem).andSelf().add(endingElem)
					.addClass('draggableSelected');
			} else {
				if (!event.ctrlKey) {
					// clear current sleection
					$(this).siblings().andSelf().removeClass('draggableSelected');
				}
				
				if (wasSelected && !event.shiftKey){
					$(this).removeClass('draggableSelected');
					lastSelectedUsers[parentId] = null;
				} else {
					$(this).addClass('draggableSelected');
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
			$('#scheduleStartTime').show('slow');
			$('#scheduleEndTime').show('slow');
		} else {
			$('#scheduleStartTime').hide('slow');
			$('#scheduleEndTime').hide('slow');
		}
	});
	
	$('#startMonitorField').change(function(){
		var checked = !$(this).is(':checked');
		if (!checked) {
			$('#schedulingEnableField, #precedingLessonEnableField, ' +
	          '#timeLimitEnableField, #timeLimitIndividualField').prop('checked', false).change();
			$('#schedulingDatetimeField').val(null);
			$('#schedulingEndDatetimeField').val(null);
		}
		
		$('#schedulingEnableField, #precedingLessonEnableField, #timeLimitEnableField, #timeLimitIndividualField,' +
		  '#precedingLessonIdField, #schedulingDatetimeField, #schedulingEndDatetimeField').prop('disabled', !checked);
	});
	
	$('#schedulingDatetimeField').datetimepicker({
		'minDate' : 0
	});
	$('#schedulingEndDatetimeField').datetimepicker({
		'minDate' : 0
	});
	
	
	$('#schedulingEndDatetimeField').change(function(){
		if ( $('#schedulingEndDatetimeField').val() == "" ) {
			$('#timeLimitIndividualField').prop('disabled', false);
		} else {
			checkScheduleDate();
		}
	});

	$('#schedulingDatetimeField').change(function(){
		checkScheduleDate();
	});
}

function checkScheduleDate() {
	var startDate = $('#schedulingDatetimeField').val() > "" ? Date.parse($('#schedulingDatetimeField').val()) : 0;
	var endDate = $('#schedulingEndDatetimeField').val() > "" ? Date.parse($('#schedulingEndDatetimeField').val()) : 0;
	if ( endDate > 0 && startDate >= endDate  ) {
		$("#schedulingError").css("display","block");
	} else {
		$("#schedulingError").css("display","none");
	}
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
			if ( $('#schedulingEndDatetimeField').val() > "" ) {
				$('#timeLimitIndividualField').prop('checked', true);
				$('#timeLimitIndividualField').prop('disabled', true);
			}
		} else {
			$('#timeLimitDiv').hide('slow');
		}
	});
}


function addLesson(learningDesignId, lessonName){
	// prevent double clicking of Add button
	if (submitInProgress) {
		return;
	}
	// some validation at first
	if (!lessonName) {
		lessonName = $('#lessonNameInput').val();
	}
	if (lessonName){
		$('#lessonNameInput').removeClass('errorBorder');
	}
	
	if (!learningDesignId) {
		var ldNode = tree.treeview('getSelected')[0];
		learningDesignId = ldNode.learningDesignId;
	}
	
	if (!learningDesignId) {
		$('#ldNotChosenError').show();
		doSelectTab(1);
		return;
	}
	$('#ldIdField').val(learningDesignId);
	
	if (lessonName){
		var nameValidator = /^[^<>^*@%$]*$/igm;	
		if (!nameValidator.test(lessonName)) {
			$('#lessonNameInput').addClass('errorBorder');
			doSelectTab(1);
			alert(LABEL_NAME_INVALID_CHARACTERS);
			return;
		}
	} else {
		$('#lessonNameInput').addClass('errorBorder');
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

	if ( $("#schedulingEnableField").is(':checked') && $("#timeLimitEnableField").is(':checked') ) {
		if ( $('#schedulingEndDatetimeField').val() > "" && $("#schedulingEnableField").is(':checked') && ! $("#timeLimitIndividualField").is(':checked') ) {
			$("#timelimitError").css("display","block");
			doSelectTab(4);
			return;
		} else {
			$("#timelimitError").css("display","none");
		}
	}
	
	$('#addButton').button('loading');
	
	if ($('#splitLearnersField').is(':checked')) {
		var maxLearnerCount = $('#selected-learners div.draggableItem').length,
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

function previewLesson(){
	var ldNode = tree.treeview('getSelected')[0],
		popupWidth = 1280,
		popupHeight = 720;

	if (!ldNode || !ldNode.learningDesignId) {
		$('#ldNotChosenError').show();
		doSelectTab(1);
		return;
	}
	var previewButton = $('#previewButton').button('loading');
	// initialize, create and enter the preview lesson
	$.ajax({
		url : LAMS_URL + 'monitoring/monitoring/initializeLesson.do',
		data : {
			'learningDesignID' : ldNode.learningDesignId,
			'copyType' : 3,
			'lessonName' : LABEL_PREVIEW_LESSON_DEFAULT_TITLE
		},
		cache : false,
		dataType : 'text',
		success : function(lessonID) {
			if (!lessonID) {
				alert(LABELS.PREVIEW_ERROR);
				previewButton.button('reset');
				return;
			}
			
			$.ajax({
				url : LAMS_URL + 'monitoring/monitoring/startPreviewLesson.do',
				data : {
					'lessonID' : lessonID
				},
				cache : false,
				dataType : 'text',
				success : function() {
					// open preview pop up window
					window.open(LAMS_URL + 'home/learner.do?mode=preview&lessonID='+lessonID,'Preview',
								'width=' + popupWidth + ',height=' + popupHeight + ',resizable,status=yes');
					previewButton.button('reset');
				}
			});

		}
	});
}

// ********** LESSON TAB FUNCTIONS **********

function loadLearningDesignSVG(ldId) {
	
	$.ajax({
		dataType : 'text',
		url : LAMS_URL + 'home/getLearningDesignThumbnail.do',
		async : false,
		cache : false,
		data : {
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
			$('<iframe />').appendTo('body').css('visibility', 'hidden').load(function(){
				// call svgGenerator.jsp code to store LD SVG on the server
				var frame = $(this),
					win = frame[0].contentWindow || frame[0].contentDocument;
			    // when LD opens, make a callback which save the thumbnail and displays it in current window
				win.GeneralLib.openLearningDesign(ldId, function(){
					result = win.GeneralLib.saveLearningDesignImage();
					frame.remove();
					if (result) {
						// load the image again
						loadLearningDesignSVG(ldId);
					}
				});
			}).attr('src', LAMS_URL + 'authoring/generateSVG.do?selectable=false');
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

// ********** CLASS TAB FUNCTIONS **********

function fillUserContainer(users, containerId) {
	if (users) {
		// create user DIVs
		$.each(users, function(index, userJSON) {
			$('#' + containerId).append(
					$('<div />').attr({
									'userId'  : userJSON.userID
									})
			                    .addClass('draggableItem')
    						    .text(userJSON.lastName + ', ' + userJSON.firstName 
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
	$(container).find('div.draggableItem').each(function(index, userDiv){
		$(this).removeClass( index % 2 ? 'draggableEven' : 'draggableOdd');
		$(this).addClass( index % 2 ? 'draggableOdd' : 'draggableEven');
	});
}


function getSelectedUserList(containerId) {
	var list = '';
	$('#' + containerId).children('div.draggableItem').each(function(){
		list += $(this).attr('userId') + ',';
	});
	return list;
}

function sortUsers(buttonId) {
	var container = $('#' + buttonId.substring(buttonId.indexOf('-') + 1));
	var users = container.children('div.draggableItem');
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
	var selectedUsers =  fromContainer.children('.draggableSelected');
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
	   toContainer.children().removeClass('draggableSelected');
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
		var maxLearnerCount = $('#selected-learners div.draggableItem').length,
			learnerCount = $('#splitLearnersCountField').val(),
			instances = Math.ceil(maxLearnerCount/learnerCount);
		learnerCount = Math.ceil(maxLearnerCount/instances);
		var description = SPLIT_LEARNERS_DESCRIPTION.replace('[0]', instances).replace('[1]', learnerCount);
		$('#splitLearnersDescription').html(description);
	}
}
