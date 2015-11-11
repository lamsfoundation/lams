﻿// ********** GLOBAL VARIABLES **********
// copy of lesson/branching SVG so it does no need to be fetched every time
// HTML with SVG of the lesson
var originalSequenceCanvas = null,
// is the LD SVG in Flashless format or the old, Flash format
	flaFormat = false,
// DIV container for lesson/branching SVG
// it gets accessed so many times it's worth to cache it here
	sequenceCanvas = null,
// ID of currently shown branching activity; if NULL, the whole lesson is shown
	sequenceBranchingId = null,
// info box show timeout
	sequenceInfoTimeout = 10000,
// which learner was selected in the search box
	sequenceSearchedLearner = null,
// how learners in pop up lists are currently sorted
	sortOrderAsc = {
		classLearner : false,
		classMonitor : false
	},
// container for learners' progress bars metadata
	bars = null,
// placeholder for single learner's progress bar and title
	learnerProgressCellsTemplate = null,
// for synchronisation purposes
	learnersRefreshInProgress = false,
	sequenceRefreshInProgress = false,

// total number of learners with ongoing progress
	learnerPossibleNumber = 0,
// page in Learners tab
	learnerProgressCurrentPageNumber = 1,

//auto refresh all tabs every 30 seconds
	autoRefreshInterval = 30 * 1000,
	autoRefreshIntervalObject = null,
// when user is doing something, do not auto refresh
	autoRefreshBlocked = false,

// double tap support
	tapTimeout = 500,
	lastTap = 0,

// after first entering of branching in old SVGs layout gets a bit broken
// setting this property fixes it
	branchingEntered = false;

// ********* GENERAL TABS FUNCTIONS *********

function initTabs(){
	$('#tabs').tabs({
		'activate' : function(event, ui) {
			var sequenceInfoDialog = $('#sequenceInfoDialog');
			if (ui.newPanel.attr('id') == 'tabSequence') {
				if (sequenceTabShowInfo && !sequenceInfoDialog.dialog('option', 'showed')) {
					sequenceInfoDialog.dialog('open');
				}
			} else if (sequenceInfoDialog.dialog('isOpen')) {
				sequenceInfoDialog.dialog('close');
			}
		}
	}).draggable({
		// scroll plugin has to be disabled here as it throws errors
		// when dragging helper appended to parent document
		'scroll' : false,
		'handle' : $('#tabs>ul'),
		'helper' : function(){
			// since this code is inside an iframe, the helper is
			// a transparent div with black borders attached to parent document
			var dialogName = "dialogMonitorLesson" + lessonId,
				dialog = $('#' + dialogName, window.parent.document).closest('.ui-dialog'),	
				helper = $('<div />').attr('id', 'dragHelper').css({
					'width'  : dialog.width(),
					'height' : dialog.height()
				});
			return helper;
		},
		'appendTo' : window.parent.document.body,
		'start' : function(){
			$(window.parent.document.body).mouseup(function(){
				// IE sometimes does not trigger drag stop, so we need to help it
				$('#tabs').trigger('mouseup');
			});
		},
		'drag' : function(event, ui) {
			// adjust position to be relative to parent document, not iframe contents
			var dialogName = "dialogMonitorLesson" + lessonId,
				dialog = $('#' + dialogName, window.parent.document).closest('.ui-dialog');
			ui.position.top += dialog.position().top;
			ui.position.left += dialog.position().left;
		},
		'stop' : function(event, ui) {
			$(window.parent.document.body).off('mouseup');
			var dialogName = "dialogMonitorLesson" + lessonId,
				dialog = $('#' + dialogName, window.parent.document).closest('.ui-dialog');
			dialog.offset({
				'top' : ui.offset.top,
				'left' : ui.offset.left
			});
		}
	}).mousedown(function(){
		// bring clicked Monitor window to front
		var dialogName = "dialogMonitorLesson" + lessonId;
		window.parent.moveDialogToTop(dialogName);
	});
}


//********** LESSON TAB FUNCTIONS **********

/**
 * Sets up lesson tab.
 */
function initLessonTab(){
	// sets export portfolio availability
	$('#exportAvailableField').change(function(){
		var checked = $(this).is(':checked');
		$.ajax({
			dataType : 'xml',
			url : LAMS_URL + 'monitoring/monitoring.do',
			cache : false,
			data : {
				'method'    : 'learnerExportPortfolioAvailable',
				'learnerExportPortfolio' : checked,
				'lessonID'      : lessonId
			}
		});
	});
	
	// sets presence availability
	$('#presenceAvailableField').change(function(){
		var checked = $(this).is(':checked');
		$.ajax({
			dataType : 'xml',
			url : LAMS_URL + 'monitoring/monitoring.do',
			cache : false,
			data : {
				'method'    : 'presenceAvailable',
				'presenceAvailable' : checked,
				'lessonID'      : lessonId
			},
			success : function() {
				updatePresenceAvailableCount();
				
				if (checked) {
					$('#imAvailableField').attr('disabled', null);
					alert(LABELS.LESSON_PRESENCE_ENABLE_ALERT);
				} else {
					$('#imAvailableField').attr({
						'checked'  : null,
						'disabled' : 'disabled'
					});
					alert(LABELS.LESSON_PRESENCE_DISABLE_ALERT);
				}
			}
		});
	});
	
	// sets instant messaging availability
	$('#imAvailableField').change(function(){
		var checked = $(this).is(':checked');
		$.ajax({
			dataType : 'xml',
			url : LAMS_URL + 'monitoring/monitoring.do',
			cache : false,
			data : {
				'method'    : 'presenceImAvailable',
				'presenceImAvailable' : checked,
				'lessonID'      : lessonId
			},
			success : function() {
				if (checked) {
					$('#openImButton').css('display', 'inline');
					alert(LABELS.LESSON_IM_ENABLE_ALERT);
				} else {
					$('#openImButton').css('display', 'none');
					alert(LABELS.LESSON_IM_DISABLE_ALERT);
				}
			}
		});
	});
	
	// sets up calendar for schedule date choice
	$('#scheduleDatetimeField').datetimepicker({
		'minDate' : 0
	});
	
	// sets up dialog for editing class
	$('#classDialog').dialog({
		'autoOpen'  : false,
		'height'    : 360,
		'width'     : 700,
		'minWidth'  : 700,
		'modal'     : true,
		'resizable' : true,
		'show'      : 'fold',
		'hide'      : 'fold',
		'open'      : function(){
			autoRefreshBlocked = true;
			// reset sort order
			sortOrderAsc['classLearner'] = false;
			sortOrderAsc['classMonitor'] = false;
			sortLessonClassDialogList('classLearner');
			sortLessonClassDialogList('classMonitor');
			colorDialogList('classLearner');
			colorDialogList('classMonitor');
			
			var selectedLearners = getSelectedClassUserList('classLearnerList');
			$(this).dialog('option', 'initSelectedLearners', selectedLearners);
		},
		'close' : function(){
			autoRefreshBlocked = false;
		},
		'buttons' : [
		             {
		            	'text'   : LABELS.SAVE_BUTTON,
		            	'id'     : 'classDialogSaveButton',
		            	'click'  : function() {
		            		var removedLearners = [],
		            			dialog = $(this),
		            			initSelectedLearners = dialog.dialog('option', 'initSelectedLearners'),
		            			learners = getSelectedClassUserList('classLearnerList'),
		            			monitors = getSelectedClassUserList('classMonitorList');
		            		
		            		// check for learners removed from lesson
		            		$.each(initSelectedLearners, function(index, selectedLearnerId){
		            			if ($.inArray(selectedLearnerId, learners) == -1) {
		            				removedLearners.push(selectedLearnerId);
		            			}
		            		});
		            		
		            		// check if monitoring user really wants to remove progress
		            		if (removedLearners.length > 0 && !confirm(LABELS.LEARNER_GROUP_REMOVE_PROGRESS)){
		            			removedLearners = [];
		            		}
		            		
		            		$.ajax({
		            			url : LAMS_URL + 'monitoring/monitoring.do',
		            			type : 'POST',
		            			cache : false,
		            			data : {
		            				'method'    	  : 'updateLessonClass',
		            				'lessonID'  	  : lessonId,
		            				'learners'  	  : learners.join(),
		            				'monitors'  	  : monitors.join(),
		            				'removedLearners' : removedLearners.join()
		            			},
		            			success : function() {
		            				dialog.dialog('close');
		            				if (removedLearners.length > 0) {
		            					refreshMonitor();
		            				} else {
		            					refreshMonitor('lesson');
		            				}
		            			}
		            		});
						}
		             },
		             {
		            	'text'   : LABELS.CANCEL_BUTTON,
		            	'id'     : 'classDialogCancelButton',
		            	'click'  : function() {
							$(this).dialog('close');
						} 
		             }
		]
	});

	// sets up dialog for emailing learners
	$('#emailDialog').dialog({
		'autoOpen'  : false,
		'height'    : 530,
		'width'     : 700,
		'modal'     : true,
		'resizable' : false,
		'show'      : 'fold',
		'hide'      : 'fold',
		'title'     : LABELS.EMAIL_BUTTON,
		'open'      : function(){
			autoRefreshBlocked = true;
			$('#emailFrame').attr('src',
					LAMS_URL + 'emailUser.do?method=composeMail&lessonID=' + lessonId
					+ '&userID=' + $(this).dialog('option', 'userId'));
		},
		'close' : function(){
			autoRefreshBlocked = false;
		}
	});
}


/**
 * Shows all learners in the lesson class.
 */
function showLessonLearnersDialog() {
	var ajaxProperties = {
		url : LAMS_URL + 'monitoring/monitoring.do',
		data : {
			'method'    : 'getLessonLearners',
			'lessonID'  : lessonId
		}
	};
	
	showLearnerGroupDialog(ajaxProperties, LABELS.LESSON_GROUP_DIALOG_CLASS, false, false, true);
}

/**
 * Changes lesson state and updates widgets.
 */
function changeLessonState(){
	var method = null;
	var state = +$('#lessonStateField').val();
	switch (state) {
		case 3: //STARTED
			switch (lessonStateId) {
				case 4: //SUSPENDED
					method = "unsuspendLesson";
					break;
				case 6: //ARCHIVED
					method = "unarchiveLesson";
					break;
			}
			break;
		case 4: 
			method = "suspendLesson";
			break;
		case 6: 
			method = "archiveLesson";
			break;
		case 7: //FINISHED
			if (confirm(LABELS.LESSON_REMOVE_ALERT)){
				if (confirm(LABELS.LESSON_REMOVE_DOUBLECHECK_ALERT)) {
					method = "removeLesson";
				}
			}
			break;
	}
	
	if (method) {
		$.ajax({
			dataType : 'xml',
			url : LAMS_URL + 'monitoring/monitoring.do',
			cache : false,
			data : {
				'method'    : method,
				'lessonID'  : lessonId
			},
			success : function() {
				if (state == 7) {
					// user chose to finish the lesson, close monitoring and refresh the lesson list
					closeMonitorLessonDialog(true);
				} else {
					refreshMonitor('lesson');
				}
			}
		});
	}
}


/**
 * Updates widgets in lesson tab according to respose sent to refreshMonitor()
 */
function updateLessonTab(){
	$.ajax({
		dataType : 'json',
		url : LAMS_URL + 'monitoring/monitoring.do',
		cache : false,
		data : {
			'method'    : 'getLessonDetails',
			'lessonID'  : lessonId
		},
		
		success : function(response) {
			// update lesson state label
			lessonStateId = +response.lessonStateID;
			var label = null;
			switch (lessonStateId) {
				case 1:
					label = LABELS.LESSON_STATE_CREATED;
					break;
				case 2:
					label = LABELS.LESSON_STATE_SCHEDULED;
					break;
				case 3:
					label = LABELS.LESSON_STATE_STARTED;
					break;
				case 4:
					label = LABELS.LESSON_STATE_SUSPENDED;
					break;
				case 5:
					label = LABELS.LESSON_STATE_FINISHED;
					break;
				case 6:
					label = LABELS.LESSON_STATE_ARCHIVED;
					break;
				case 7:
					label = LABELS.LESSON_STATE_REMOVED;
					break;
			}
			$('#lessonStateLabel').text(label);
			
			// update available options in change state dropdown menu
			var selectField = $('#lessonStateField');
			// remove all except "Select status" option
			selectField.children('option:not([value="-1"])').remove();
			switch (lessonStateId) {
				case 3:
					$('<option />').attr('value', 4).text(LABELS.LESSON_STATE_ACTION_DISABLE).appendTo(selectField);
					$('<option />').attr('value', 6).text(LABELS.LESSON_STATE_ACTION_ARCHIVE).appendTo(selectField);
					$('<option />').attr('value', 7).text(LABELS.LESSON_STATE_ACTION_REMOVE).appendTo(selectField);
					break;
				case 4:
					$('<option />').attr('value', 3).text(LABELS.LESSON_STATE_ACTION_ACTIVATE).appendTo(selectField);
					$('<option />').attr('value', 6).text(LABELS.LESSON_STATE_ACTION_ARCHIVE).appendTo(selectField);
					$('<option />').attr('value', 7).text(LABELS.LESSON_STATE_ACTION_REMOVE).appendTo(selectField);
					break;
				case 5:
					break;
				case 6:
					$('<option />').attr('value', 3).text(LABELS.LESSON_STATE_ACTION_ACTIVATE).appendTo(selectField);
					$('<option />').attr('value', 7).text(LABELS.LESSON_STATE_ACTION_REMOVE).appendTo(selectField);
					break;
			}
			
			// show/remove widgets for lesson scheduling
			var scheduleControls = $('#scheduleDatetimeField, #scheduleLessonButton, #startLessonButton');
			var startDateField = $('#lessonStartDateSpan');
			switch (lessonStateId) {
				 case 1:
					 scheduleControls.css('display','inline');
					 startDateField.css('display','none');
					 break;
				 case 2:
					 scheduleControls.css('display','none');
					 startDateField.text(response.startDate).add('#startLessonButton').css('display','inline');
					 break;
				default: 			
					scheduleControls.css('display','none');
				 	startDateField.text(response.startDate).css('display','inline');
				 	break;
			}
			
			updateContributeActivities(response.contributeActivities);
		}
	});
	
	updatePresenceAvailableCount();
}


function scheduleLesson(){
	var date = $('#scheduleDatetimeField').val();
	if (date) {
		$.ajax({
			dataType : 'xml',
			url : LAMS_URL + 'monitoring/monitoring.do',
			cache : false,
			data : {
				'method'          : 'startOnScheduleLesson',
				'lessonID'        : lessonId,
				'lessonStartDate' : date
			},
			success : function() {
				refreshMonitor('lesson');
			}
		});
	} else {
		alert(LABELS.LESSON_ERROR_SCHEDULE_DATE);
	}
}


function startLesson(){
	$.ajax({
		dataType : 'xml',
		url : LAMS_URL + 'monitoring/monitoring.do',
		cache : false,
		data : {
			'method'          : 'startLesson',
			'lessonID'        : lessonId
		},
		success : function() {
			refreshMonitor('lesson');
		}
	});
}


/**
 * Stringifies user IDs who were selected in Edit Class dialog. 
 */
function getSelectedClassUserList(containerId) {
	var list = [];
	$('#' + containerId).children('div.dialogListItem').each(function(){
		if ($('input:checked', this).length > 0){
			list.push($(this).attr('userId'));
		}
	});
	return list;
}


function openChatWindow(){
	// variables are set in JSP page
	window.open(LAMS_URL + 'learning/lessonChat.jsp?lessonID=' + lessonId 
			+ '&presenceEnabledPatch=true&presenceImEnabled=true&presenceShown=true&createDateTime='
			+ createDateTimeStr
			,'Chat'
			,'width=650,height=350,resizable=no,scrollbars=no,status=no,menubar=no,toolbar=no');
}


function showEmailDialog(userId){
	$('#emailDialog').dialog('option',{
		'userId'        : userId
	}).dialog('open');
}


function closeEmailDialog(){
	$('#emailFrame').attr('src', null);
	$('#emailDialog').dialog('close');
}


function updatePresenceAvailableCount(){
	var checked = $('#presenceAvailableField').is(':checked'),
		counter = $('#presenceAvailableCount');
	if (checked) {
		$.ajax({
			dataType : 'json',
			url : LAMS_URL + 'PresenceChat.do',
			cache : false,
			data : {
				'method'    : 'getChatContent',
				'lessonID'      : lessonId
			},
			success : function(result) {
				$('span', counter).text(result.roster.length);
				counter.css('display', null);
			}
		});

	} else {
		counter.css('display', 'none');
	}
}


function selectLearnerURL(){
	$('#learnerURLField').select().focus().blur(function(){
		$('#copyLearnerURL').hide();
	});
	$('#copyLearnerURL').show();
}

function updateContributeActivities(contributeActivities) {
	$('.contributeRow').remove();
	var header = $('#contributeHeader');
	if (contributeActivities) {
		var row = header;
		$.each(contributeActivities, function(){
			var cell = $('<td colspan="2" />').addClass('contributeActivityCell').text(this.title);
			row = $('<tr />').addClass('contributeRow').insertAfter(row).append(cell);
			
			$.each(this.contributeEntries, function(){
				var entryContent = '';
				switch(this.contributionType) {
					case 3  : entryContent = LABELS.CONTRIBUTE_GATE; break;
					case 6  : entryContent = LABELS.CONTRIBUTE_GROUPING; break;
					case 9  : entryContent = LABELS.CONTRIBUTE_BRANCHING; break;
					case 11 : entryContent = LABELS.CONTRIBUTE_CONTENT_EDITED; break; 
				}
				entryContent += '<a href="#" class="button" onClick="javascript:openPopUp(\''
							 + this.url + '\',\'ContributeActivity\', 600, 800, true)" title="' + LABELS.CONTRIBUTE_TOOLTIP
							 + '">' + LABELS.CONTRIBUTE_BUTTON + '</a>';
				cell = $('<td colspan="2" />').addClass('contributeEntryCell').html(entryContent);
				row = $('<tr />').addClass('contributeRow').insertAfter(row).append(cell);
			});
		});
	}
	if ($('.contributeRow').length == 0) {
		header.hide();
	} else {
		header.show();
	}
}

//********** SEQUENCE TAB FUNCTIONS **********

/**
 * Sets up the sequence tab.
 */
function initSequenceTab(){
    // initialise lesson dialog
	$('#learnerGroupDialog').dialog({
			'autoOpen'  : false,
			'height'    : 360,
			'width'     : 400,
			'minWidth'  : 400,
			'modal'     : true,
			'resizable' : true,
			'show'      : 'fold',
			'hide'      : 'fold',
			'open'      : function(){
				autoRefreshBlocked = true;
				// until operator selects an user, buttons remain disabled
				$('button.learnerGroupDialogSelectableButton').blur().removeClass('ui-state-hover')
					.attr('disabled', 'disabled');
			},
			'close' 	: function(){
				autoRefreshBlocked = false;
			},
			'buttons' : [
			             {
			            	'text'   : LABELS.FORCE_COMPLETE_BUTTON,
			            	'id'     : 'learnerGroupDialogForceCompleteButton',
			            	'class'  : 'learnerGroupDialogSelectableButton',
			            	'click'  : function() {
			            		var selectedLearner = $('#learnerGroupList div.dialogListItemSelected');
			            		// make sure there is only one selected learner
			            		if (selectedLearner.length == 1) {
			            			// go to "force complete" mode, similar to draggin user to an activity
			            			var activityId = $(this).dialog('option', 'ajaxProperties').data.activityID,
			            				dropArea = sequenceCanvas.add('#completedLearnersContainer');
			            			dropArea.css('cursor', 'url('
			            					+ LAMS_URL + 'images/icons/user.png),pointer')
			            				.one('click', function(event) {
			            					dropArea.off('click').css('cursor', 'default');
			            					forceComplete(activityId, selectedLearner.attr('userId'), 
			            							selectedLearner.text(), event.pageX, event.pageY);
			            				});
				            		$(this).dialog('close');
				            		alert(LABELS.FORCE_COMPLETE_CLICK.replace('[0]', selectedLearner.text()));
			            		}
							}
			             },
			             {
			            	'text'   : LABELS.VIEW_LEARNER_BUTTON,
			            	'id'     : 'learnerGroupDialogViewButton',
			            	'class'  : 'learnerGroupDialogSelectableButton',
			            	'click'  : function() {
			            		var selectedLearner = $('#learnerGroupList div.dialogListItemSelected');
			            		if (selectedLearner.length == 1) {
			            			// open pop up with user progress in the given activity
			            			openPopUp(selectedLearner.attr('viewUrl'), "LearnActivity", 600, 800, true);
			            		}
							}
			             },
			             {
			            	'text'   : LABELS.EMAIL_BUTTON,
			            	'id'     : 'learnerGroupDialogEmailButton',
			            	'class'  : 'learnerGroupDialogSelectableButton',
			            	'click'  : function() {
			            		var selectedLearner = $('#learnerGroupList div.dialogListItemSelected');
			            		if (selectedLearner.length == 1) {
			            			showEmailDialog(selectedLearner.attr('userId'));
			            		}
			            	}	
			             },
			             {
			            	'text'   : LABELS.CLOSE_BUTTON,
			            	'id'     : 'learnerGroupDialogCloseButton',
			            	'click'  : function() {
								$(this).dialog('close');
							} 
			             }
			]
		});
	
	// small info box on Sequence tab, activated when the tab is showed
	$('#sequenceInfoDialog').dialog({
		'autoOpen'   : false,
		'height'     : 35,
		'width'      : 290,
		'modal'      : false,
		'resizable'  : false,
		'show'       : 'fold',
		'hide'       : 'fold',
		'dialogClass': 'dialog-no-title',
		'position'   : {my: "left top",
					   at: "left top+10",
					   of: '#sequenceTopButtonsContainer'
				      },
		'open'      : function(){
			var dialog = $(this);
			// show only once in this Monitor
			dialog.dialog('option', 'showed', true);
			// close after given time
			setTimeout(function(){
				dialog.dialog('close');
			}, sequenceInfoTimeout);
		}
	});
	
	$('#forceBackwardsDialog').dialog({
		'autoOpen'  : false,
		'modal'     : true,
		'resizable' : false,
		'minWidth'  : 350,
		'show'      : 'fold',
		'hide'      : 'fold',
		'title'		: LABELS.FORCE_COMPLETE_BUTTON,
		'open'      : function(){
			autoRefreshBlocked = true;
		},
		'close' 	: function(){
			autoRefreshBlocked = false;
		},
		'buttons' : [
		             {
		            	'text'   : LABELS.FORCE_COMPLETE_REMOVE_CONTENT_NO,
		            	'click'  : function() {
		            		$(this).dialog('close');
		            		forceCompleteExecute($(this).dialog('option', 'learnerId'),
       							 				 $(this).dialog('option', 'activityId'),
       							 				 false);
						}
		             },
		             {
		            	'text'   : LABELS.FORCE_COMPLETE_REMOVE_CONTENT_YES,
		            	'click'  : function() {
							$(this).dialog('close');
		            		forceCompleteExecute($(this).dialog('option', 'learnerId'),
		            							 $(this).dialog('option', 'activityId'),
		            							 true);
						}
		             },
		             {
		            	'text'   : LABELS.CLOSE_BUTTON,
		            	'click'  : function() {
							$(this).dialog('close');
						} 
		             }
		]
	});
	
	// search for users with the term the Monitor entered
	$("#sequenceSearchPhrase").autocomplete( {
		'source' : LAMS_URL + "monitoring/monitoring.do?method=autocompleteMonitoringLearners&lessonID=" + lessonId,
		'delay'  : 700,
		'select' : function(event, ui){
			// put the learner first name, last name and login into the box
			$(this).val(ui.item.label);
			// mark the learner's ID and make him highlighted after the refresh
			sequenceSearchedLearner = ui.item.value;
			updateSequenceTab();
			return false;
		}
	});
}
	


/**
 * Updates learner progress in sequence tab according to respose sent to refreshMonitor()
 */
function updateSequenceTab() {
	if (sequenceRefreshInProgress) {
		return;
	}
	sequenceRefreshInProgress = true;
	// SVG modifications are made only the first time this method is called
	var sequenceCanvasFirstFetch = false;
	
	if (originalSequenceCanvas) {
		// put bottom layer, LD SVG
		sequenceCanvas.html(originalSequenceCanvas);
	} else {
		// fetch SVG just once, since it is immutable
		$.ajax({
			dataType : 'text',
			url : LAMS_URL + 'home.do',
			async : false,
			cache : false,
			data : {
				'method'    : 'createLearningDesignThumbnail',
				'svgFormat' : 1,
				'ldId'      : ldId,
				'branchingActivityID' : sequenceBranchingId
			},
			success : function(response) {
				sequenceCanvasFirstFetch = true;
				originalSequenceCanvas = response;
				// store body dimensions before manipulating HTML
				// otherwise resizing will yield bad results
				var width = $('body').width(),
					height = $('body').height();
				sequenceCanvas = $('#sequenceCanvas')
					// remove previously set padding and dimensions, if any
					.removeAttr('style')
					.html(originalSequenceCanvas)
					// if it was faded out by showBranchingSequence()
					.fadeIn(function(){
						resizeSequenceCanvas(width, height);
					});
			}
		});
	}
	
	// clear all completed learner icons except the door
	$('#completedLearnersContainer :not(img#completedLearnersDoorIcon)').remove();
	
	var sequenceTopButtonsContainer = $('#sequenceTopButtonsContainer');
	if ($('img#sequenceCanvasLoading', sequenceTopButtonsContainer).length == 0){
		$('#sequenceCanvasLoading')
				.clone().appendTo(sequenceTopButtonsContainer)
				.css('display', 'block');
	}
	
	$.ajax({
		dataType : 'json',
		url : LAMS_URL + 'monitoring/monitoring.do',
		cache : false,
		data : {
			'method'    : 'getLessonProgress',
			'lessonID'  : lessonId,
			'branchingActivityID' : sequenceBranchingId,
			'searchedLearnerId' : sequenceSearchedLearner
		},		
		success : function(response) {
			if (sequenceCanvasFirstFetch) {
				// once Flashless SVG format is detected, it applies for all activities
				flaFormat = response.flaFormat;
				
				// FLA activities have uiids but no ids, set it here
				$.each(response.activities, function(activityIndex, activity){
					$('g[uiid="' + activity.uiid + '"]', sequenceCanvas).attr('id', activity.id);
				});
				
				// add some metadata to activities
				$.each(response.activities, function(activityIndex, activity){
					var activityGroup = $('g[id="' + activity.id + '"]', sequenceCanvas),
						isGate = [3,4,5,14].indexOf(activity.type) > -1,
						isOptional = activity.type == 7,
						isFloating = activity.type == 15;
					if (isGate) {
						activityGroup.attr('class', 'gate');
					} else if (isOptional) {
						activityGroup.attr('class', 'optional');
					} else if (isFloating) {
						activityGroup.attr('class', 'floating');
					}
				});
				
				originalSequenceCanvas = sequenceCanvas.html();
			}
			
			// remove the loading animation
			$('img#sequenceCanvasLoading', sequenceTopButtonsContainer).remove();

			var learnerCount = 0,
				reloadSVG = false;
			$.each(response.activities, function(index, activity){
				// are there any learners in this or any activity?
				learnerCount += activity.learnerCount;
				if (response.contributeActivities) {
					$.each(response.contributeActivities, function(){
						if (activity.id == this.activityID) {
							 activity.requiresAttention = true;
							 reloadSVG = true;
							 return false;
						}
					});
				}
				// put learner and attention icons on each activity shape
				addActivityIcons(activity);
			});
			
			reloadSVG |= learnerCount > 0;
			if (reloadSVG) {
				// IMPORTANT! Reload SVG, otherwise added icons will not get displayed
				sequenceCanvas.html(sequenceCanvas.html());
			}
			
			if (sequenceSearchedLearner != null && !response.searchedLearnerFound) {
				// the learner has not started the lesson yet, display an info box
				sequenceClearSearchPhrase();
				$('#sequenceInfoDialog').text(LABELS.PROGRESS_NOT_STARTED).dialog('open');
			}
			
			var learnerTotalCount = learnerCount + response.completedLearnerCount;
			$('#learnersStartedPossibleCell').text(learnerTotalCount + ' / ' + response.numberPossibleLearners);
			addCompletedLearnerIcons(response.completedLearners, response.completedLearnerCount, learnerTotalCount);
			
			$.each(response.activities, function(activityIndex, activity){
				addActivityIconsHandlers(activity);
				
				var isBranching = [10,11,12,13].indexOf(activity.type) > -1;
				if (activity.url || (isBranching && !flaFormat)) {
					var activityGroup = $('g[id="' + activity.id + '"]'),
						dblClickFunction = 
							// different behaviour for regular/branching activities
							isBranching ? 
							function(){ showBranchingSequence(activity.id); }
							:
							function(){  
								// double click on activity shape to open Monitoring for this activity
								openPopUp(LAMS_URL + activity.url, "MonitorActivity", 720, 900, true, true);
							};
					
					activityGroup.css('cursor', 'pointer');
					dblTap(activityGroup, dblClickFunction);
				}
			});	
			
			sequenceRefreshInProgress = false;
		}
	});
}


/**
 * Forces given learner to move to activity indicated on SVG by coordinated (drag-drop)
 */
function forceComplete(currentActivityId, learnerId, learnerName, x, y) {
	autoRefreshBlocked = true;
	
	var foundActivities = [],
		targetActivity = null;
	// check all activities and "users who finished lesson" bar
	// rootElement is only in Flash LDs
	$('g[id]:not([id*="_to_"]):not(#rootElement)', sequenceCanvas).add('#completedLearnersContainer').each(function(){
		// find which activity learner was dropped on
		var act = $(this),
			coord = {
					'x' : act.offset().left,
					'y' : act.offset().top
				}
		if (act.is('g')) {
			var box = act[0].getBBox();
			coord.width = box.width;
			coord.height = box.height;
		} else {
			// end of lesson container
			coord.width = act.width();
			coord.height = act.height();
		}
		
		coord.x2 = coord.x + coord.width;
		coord.y2 = coord.y + coord.height;
		
		if (x >= coord.x && x <= coord.x2 && y >= coord.y && y <= coord.y2) {
			foundActivities.push(act);
		}
	});
	
	$.each(foundActivities, function(){
		if (this.attr('class') == 'floating') {
			// no force complete to support activities
			targetActivity = null;
			return false;
		}
		// the enveloping OptionalActivity has priority
		if (targetActivity == null || this.attr('class') == 'optional') {
			targetActivity = this;
		}
	});
	
	if (!targetActivity) {
		return;
	}
	
	var targetActivityId = null,
		executeForceComplete = false,
		isEndLesson = !targetActivity.is('g');
	
	if (isEndLesson) {
		executeForceComplete =  currentActivityId && confirm(LABELS.FORCE_COMPLETE_END_LESSON_CONFIRM
				.replace('[0]',learnerName));
	} else {
		var targetActivityId = +targetActivity.attr('id');
		if (currentActivityId != targetActivityId) {
			var targetActivityName = targetActivity.attr('class') == 'gate' ? "Gate" : targetActivity.children('text').text(),
				moveBackwards = false;
			
			// check if target activity is before current activity
			if (currentActivityId) {
				$.ajax({
					dataType : 'text',
					url : LAMS_URL + 'monitoring/monitoring.do',
					async : false,
					cache : false,
					data : {
						'method'     	 : 'isActivityPreceding',
						'activityA' 	 :  targetActivityId,
						'activityB'		 :  currentActivityId
					},
					success : function(response) {
						moveBackwards = response == 'true';
					}
				});
			}
			
			// check if the target activity was found or we are moving the learner from end of lesson
			if (moveBackwards) {
				// move the learner backwards
				$('#forceBackwardsDialog').text(LABELS.FORCE_COMPLETE_REMOVE_CONTENT
							.replace('[0]', learnerName).replace('[1]', targetActivityName))
							.dialog('option', {
								'learnerId' : learnerId,
								'activityId': targetActivityId
							})
							.dialog('open');
				// so autoRefreshBlocked = false is not set
				return;
			} else {
				// move the learner forward
				executeForceComplete = confirm(LABELS.FORCE_COMPLETE_ACTIVITY_CONFIRM
							.replace('[0]', learnerName).replace('[1]', targetActivityName));
			}
		}
	}
	
	if (executeForceComplete) {
		forceCompleteExecute(learnerId, targetActivityId, false);
	}

	autoRefreshBlocked = false;
}


/**
 * Tell server to force complete the learner.
 */
function forceCompleteExecute(learnerId, activityId, removeContent) {
	$.ajax({
		dataType : 'text',
		url : LAMS_URL + 'monitoring/monitoring.do',
		cache : false,
		data : {
			'method'     		 : 'forceComplete',
			'lessonID'   		 : lessonId,
			'learnerID'  		 : learnerId,
			'activityID' 		 : activityId,
			'removeContent'		 : removeContent
		},
		success : function(response) {
			// inform user of result
			alert(response);
									
			// progress changed, show it to monitor
			refreshMonitor('sequence');
		}
	});
}


/**
 * Draw user and attention icons on top of activities.
 */
function addActivityIcons(activity) {
	if (activity.learnerCount == 0 && !activity.requiresAttention) {
		return;
	}
	
	// fint the activity in SVG
	var coord = getActivityCoordinates(activity);
	if (!coord) {
		return;
	}
	
	// add group of users icon
	var appendTarget = $('svg', sequenceCanvas)[0],
		// branching and gates require extra adjustments
		isNewBranching =  [10,11,12,13].indexOf(activity.type) > -1 && flaFormat,
		isGate = [3,4,5,14].indexOf(activity.type) > -1;
	
	if (activity.learnerCount > 0){
		var	groupTitle = activity.learnerCount + ' ' + LABELS.LEARNER_GROUP_COUNT + ' ' + LABELS.LEARNER_GROUP_SHOW,
			element = appendXMLElement('image', {
			'id'         : 'act' + activity.id + 'learnerGroup',
			'x'          : isNewBranching ? coord.x + 2  : (isGate ? coord.x + 10 : coord.x2 - 18),
			'y'          : isNewBranching ? coord.y - 12 : coord.y + 1,
			'height'     : 16,
			'width'      : 16,
			'xlink:href' : LAMS_URL + 'images/icons/group.png',
			'style'		 : 'cursor : pointer'
		}, null, appendTarget);
		appendXMLElement('title', null, groupTitle, element);
		// add a small number telling how many learners are in the group
		element = appendXMLElement('text', {
			'id'         : 'act' + activity.id + 'learnerGroupText',
			'x'          : isNewBranching ? coord.x + 9  : (isGate ? coord.x + 17 : coord.x2 - 9),
			'y'          : isNewBranching ? coord.y + 12 : coord.y + 25,
			'text-anchor': 'middle',
			'font-family': 'Verdana',
			'font-size'  : 8,
			'style'		 : 'cursor : pointer'
		}, activity.learnerCount, appendTarget);
		appendXMLElement('title', null, groupTitle, element);
	
		if (activity.learners) {
		// draw single icons for the first few learners;
		// don't do it for gate and optional activities, and new branching/optional sequences format
			if ([3,4,5,7,13,14].indexOf(activity.type) == -1 && !flaFormat) {
			$.each(activity.learners, function(learnerIndex, learner){
				var learnerDisplayName = getLearnerDisplayName(learner);
					element = appendXMLElement('image', {
						'id'         : 'act' + activity.id + 'learner' + learner.id,
						'x'          : coord.x + learnerIndex*15 + 1,
						// a bit lower for Optional Activity
						'y'          : coord.y,
						'height'     : 16,
						'width'      : 16,
							'xlink:href' : LAMS_URL + 'images/icons/' 
										   + (learner.id == sequenceSearchedLearner ? 'user_red.png' : 'user.png'),
						'style'		 : 'cursor : pointer'
					}, null, appendTarget);
					appendXMLElement('title', null, learnerDisplayName, element);
				});
			}
		}
	} 

	if (activity.requiresAttention) {
		var element = appendXMLElement('image', {
			'id'         : 'act' + activity.id + 'attention',
			'x'          : isNewBranching ? coord.x + 14 : coord.x2 - 19,
			'y'          : isNewBranching ? coord.y + 6  : coord.y2 - 19,
			'height'     : 16,
			'width'      : 16,
			'xlink:href' : LAMS_URL + 'images/icons/exclamation.png',
			'style'		 : 'cursor : pointer'
		}, null, appendTarget);
		appendXMLElement('title', null, LABELS.CONTRIBUTE_ATTENTION, element);
	}
}


/**
 * After SVG refresh, add click/dblclick/drag handlers to icons.
 */
function addActivityIconsHandlers(activity) {
	if (activity.learnerCount == 0 && !activity.requiresAttention) {
		return;
	}

	// gate activity does not allows users' view
	var usersViewable = [3,4,5,14].indexOf(activity.type) == -1;
	
	if (activity.learners){
		$.each(activity.learners, function(learnerIndex, learner){
			var learnerIcon = $('image[id="act' + activity.id + 'learner' + learner.id + '"]', sequenceCanvas)
							  .css('cursor', 'pointer')
							  // drag learners to force complete activities
							  .draggable({
								'appendTo'    : '#tabSequence',
								'containment' : '#tabSequence',
							    'distance'    : 20,
							    'scroll'      : false,
							    'cursorAt'	  : {'left' : 10, 'top' : 15},
								'helper'      : function(event){
									// copy of the icon for dragging
									return $('<img />').attr('src', LAMS_URL + 'images/icons/user.png');
								},
								'start' : function(){
									autoRefreshBlocked = true;
								},
								'stop' : function(event, ui) {
									// jQuery droppable does not work for SVG, so this is a workaround
									forceComplete(activity.id, learner.id, getLearnerDisplayName(learner, true),
											      ui.offset.left, ui.offset.top);
								}
							});
			
			if (usersViewable) {
				dblTap(learnerIcon, function(event){
					 // double click on learner icon to see activity from his perspective
					event.stopPropagation();
					var url = LAMS_URL + 'monitoring/monitoring.do?method=getLearnerActivityURL&userID=' 
						               + learner.id + '&activityID=' + activity.id + '&lessonID=' + lessonId;
					openPopUp(url, "LearnActivity", 600, 800, true);
				});
			}
			
			if (learner.id == sequenceSearchedLearner){
				// do it here instead of addActivityIcons()
				// as in that method the icons are added to the document yet
				// and they have no offset for calculations
				highlightSearchedLearner(learnerIcon);
			}
		});
	}
		
	if (activity.learnerCount > 0){
		var learnerGroup = $('*[id^="act' + activity.id + 'learnerGroup"]', sequenceCanvas);
		dblTap(learnerGroup, function(event){
			 // double click on learner group icon to see list of learners
			event.stopPropagation();
			var ajaxProperties = {
					url : LAMS_URL + 'monitoring/monitoring.do',
					data : {
						'method'     : 'getCurrentLearners',
						'activityID' : activity.id,
						'flaFormat' : flaFormat
					}
				};
			showLearnerGroupDialog(ajaxProperties, activity.title, true, usersViewable, false);
		});
	}
	
	if (activity.requiresAttention){
		$('*[id^="act' + activity.id + 'attention"]', sequenceCanvas).click(function(event){
			event.stopPropagation();
			// switch to first tab where attention prompts are listed
			$('#tabs').tabs('option', 'active', 0);     
		});
	}
}


/**
 * Add learner icons in "finished lesson" bar.
 */
function addCompletedLearnerIcons(learners, learnerCount, learnerTotalCount) {
	var iconsContainer = $('#completedLearnersContainer');
	// show (current/total) label
	$('<span />').attr({
						'title' : LABELS.LEARNER_FINISHED_COUNT.replace('[0]', learnerCount).replace('[1]', learnerTotalCount)
			   }).text('(' + learnerCount + '/' + learnerTotalCount + ')')
	  .appendTo(iconsContainer);
	
	if (learners) {
		// create learner icons, along with handlers
		$.each(learners, function(learnerIndex, learner){
				// make an icon for each learner
			var icon = $('<img />').attr({
				'src' : LAMS_URL + 'images/icons/' 
				   		+ (learner.id == sequenceSearchedLearner ? 'user_red.png' : 'user.png'),
				'style'		 : 'cursor : pointer',
					'title'      : getLearnerDisplayName(learner)
			})
				// drag learners to force complete activities
				  .draggable({
					'appendTo'    : '#tabSequence',
					'containment' : '#tabSequence',
				    'distance'    : 20,
				    'scroll'      : false,
				    'cursorAt'	  : {'left' : 10, 'top' : 15},
					'helper'      : function(event){
						// copy of the icon for dragging
						return $('<img />').attr('src', LAMS_URL + 'images/icons/user.png');
					},
					'start' : function(){
						autoRefreshBlocked = true;
					},
					'stop' : function(event, ui) {
						// jQuery droppable does not work for SVG, so this is a workaround
						forceComplete(null, learner.id, getLearnerDisplayName(learner, true),
								      ui.offset.left, ui.offset.top);
					}
				})
				.appendTo(iconsContainer);
			
			if (learner.id == sequenceSearchedLearner){
				highlightSearchedLearner(icon);
			}
		});
		
		// show a group icon
		var groupIcon = $('<img />').attr({
			'src' : LAMS_URL + 'images/icons/group.png',
			'title'      : LABELS.LEARNER_GROUP_SHOW
		}).css('cursor', 'pointer').appendTo(iconsContainer);
		
		dblTap(groupIcon, function(){
			var ajaxProperties = {
					url : LAMS_URL + 'monitoring/monitoring.do',
					data : {
						'method'     : 'getCurrentLearners',
						'lessonID'   : lessonId
					}
				};
			showLearnerGroupDialog(ajaxProperties, LABELS.LEARNER_FINISHED_DIALOG_TITLE, true, false, false);
		});
	}
}


/**
 * Extracts activity using SVG attributes.
 */
function getActivityCoordinates(activity){
	// fix missing coordinates, not set by Flash Authoring
	if (!activity.x) {
		activity.x = 0;
	}
	if (!activity.y) {
		activity.y = 0;
	}
	
	// special processing for gates
	if ([3,4,5,14].indexOf(activity.type) > -1) {
		return {
			'x'  : activity.x,
			'y'  : activity.y - 18,
			'x2' : activity.x + 39,
			'y2' : activity.y + 40
		}
	}
	
	// special processing for new format of branching and optional sequences
	if ([10,11,12,13].indexOf(activity.type) > -1 && flaFormat) {
		return {
			'x'  : activity.x,
			'y'  : activity.y
		}
	}
	
	var group = $('g[id="' + activity.id + '"]', sequenceCanvas);
	if (group.length == 0) {
		return;
	}
	var elem = $('rect, path', group),
		// if it's a rectangle, it has these attributes; rectangles are in old SVGs
		width = elem.attr('width'),
		height = elem.attr('height');
	if (width) {
		return {
			'x'    : activity.x,
			'y'    : activity.y,
			'x2'   : activity.x + +width,
			'y2'   : activity.y + +height
		}
	} else {
		// extract width and height from path M<x>,<y>h<width>v<height>... or M <x> <y> h <width> v <height>...
		var match = /h\s?(\d+)\s?v\s?(\d+)/.exec(elem.attr('d'));
		if (match) {
			return {
				'x'    : activity.x,
				'y'    : activity.y + 1,
				'x2'   : activity.x + +match[1],
				'y2'   : activity.y + +match[2]
			}
		}
	}
}


/**
 * Shows where the searched learner is.
 */
function highlightSearchedLearner(icon) {
	// show the "clear" button
	$('#sequenceSearchPhraseClear').css('visibility', 'visible');
	
	
	var highlighter = $('#sequenceSearchedLearnerHighlighter'),
		isVisible = highlighter.is(':visible');
	
	highlighter.show().offset({
			'top'  : icon.offset().top - 25,
			'left' : icon.offset().left - 4
		});
	
	// blink only after the search, not after subsequent refreshes
	if (!isVisible) {
		toggleInterval = setInterval(function(){
			highlighter.toggle();
		}, 500);
		
		setTimeout(function(){
			clearInterval(toggleInterval);
			//if the search box was cleared during blinking, act accordingly
			if (sequenceSearchedLearner) {
				highlighter.show();
			} else {
				highlighter.hide();
			}
		}, 3000);
	}
}


/**
 * Cancels the performed search.
 */
function sequenceClearSearchPhrase(refresh){
	$('#sequenceSearchPhrase').val('');
	$('#sequenceSearchPhraseClear').css('visibility', 'hidden');
	$('#sequenceSearchedLearnerHighlighter').hide();
	sequenceSearchedLearner = null;
	if (refresh) {
	updateSequenceTab();
}
}


/**
 * Shows Edit Class dialog for class manipulation.
 */
function showClassDialog(){
	var learners = [];
	var monitors = [];
	
	// fetch available and already participating learners and monitors
	$.ajax({
		dataType : 'json',
		url : LAMS_URL + 'monitoring/monitoring.do',
		cache : false,
		async : false,
		data : {
			'method'    : 'getClassMembers',
			'lessonID'  : lessonId,
			'role'      : 'LEARNER'
		},
		success : function(response) {
			learners = response;
		}
	});
	$.ajax({
		dataType : 'json',
		url : LAMS_URL + 'monitoring/monitoring.do',
		cache : false,
		async : false,
		data : {
			'method'    : 'getClassMembers',
			'lessonID'  : lessonId,
			'role'      : 'MONITOR'
		},
		success : function(response) {
			monitors = response;
		}
	});
	
	// fill lists
	fillClassDialogList('classLearner', learners, false);
	fillClassDialogList('classMonitor', monitors, true);

	$('#classDialog')
		.dialog('option',
			{
			 'title' : LABELS.LESSON_EDIT_CLASS
			})
		.dialog('open');
}


/**
 * Fills class member list with user information.
 */
function fillClassDialogList(listId, users, disableCreator) {
	var list = $('#' + listId + 'List').empty();
	var selectAllInitState = true;
	
	$.each(users, function(userIndex, user) {
		var checkbox = $('<input />').attr({
	    	 'type' : 'checkbox'
	      }).change(function(){
	    	var itemState = $(this).is(':checked');
	    	if (itemState) {
	    		var selectAllState = true;
	    		$('input', list).each(function(){
	    			if (!$(this).is(':checked')) {
	    				selectAllState = false;
	    				return false;
	    			}
	    		});
	    		
	    		if (selectAllState) {
	    			$('#' + listId + 'SelectAll').prop('checked', 'checked');
	    		}
	    	} else {
	    		$('#' + listId + 'SelectAll').prop('checked', null);
	    	}
	    	
	      });
		if (user.classMember) {
			checkbox.prop('checked', 'checked');
			if (disableCreator && user.lessonCreator) {
				// user creator must not be deselected
				checkbox.attr('disabled', 'disabled');
			}
		} else {
			selectAllInitState = false;
		}
		
		var userDiv = $('<div />').attr({
			'userId'  : user.id
			})
          .addClass('dialogListItem')
	      .html(getLearnerDisplayName(user))
	      .prepend(checkbox)
	      .appendTo(list);
		
		if (disableCreator && user.lessonCreator) {
			userDiv.addClass('dialogListItemDisabled');
		} else {
			userDiv.click(function(event){
				if (event.target == this) {
		    		checkbox.prop('checked', checkbox.is(':checked') ? null : 'checked');
		    	}
		    })
		}
	});	

	$('#' + listId + 'SelectAll').prop('checked', selectAllInitState ? 'checked' : null);
}


/**
 * Opens Authoring for live edit.
 */
function openLiveEdit(){
	if (confirm(LABELS.LIVE_EDIT_CONFIRM)) {
		$.ajax({
			dataType : 'text',
			url : LAMS_URL + 'monitoring/monitoring.do',
			cache : false,
			async : false,
			data : {
				'method'    : 'startLiveEdit',
				'ldId'      : ldId
			},
			success : function(response) {
				if (response) {
					alert(response);
				} else {
					window.parent.showFlashlessAuthoringDialog(ldId, 'editonfly');
					closeMonitorLessonDialog();
				}
			}
		});	
	}
}


/**
 * Replaces canvas with the given branchin activity contents
 */
function showBranchingSequence(branchingActivityId){
	sequenceBranchingId = branchingActivityId;
	originalSequenceCanvas = null;
	branchingEntered = true;
	$('#closeBranchingButton').show();
	sequenceCanvas.fadeOut(function(){
		sequenceCanvas.html(null);
		updateSequenceTab();
	});
}


/**
 * Shows Learning Design in canvas.
 */
function closeBranchingSequence(){
	showBranchingSequence(null);
	$('#closeBranchingButton').hide();
}

/**
 * Adjusts sequence canvas (SVG) based on space available in the dialog.
 */
function resizeSequenceCanvas(width, height){
	var sequenceCanvas = $('#sequenceCanvas'),
		// can it be done nicer?
		canvasHeight = height - $('#tabs>ul').height() - $('#sequenceTopButtonsContainer').height()
	  				   - Math.min(20, $('#completedLearnersContainer').height()) - 45,
		canvasWidth = width - 15,
		svg = $('svg', sequenceCanvas),
		// center a small SVG inside large DIV
		canvasPaddingTop = Math.max(0, canvasHeight/2 - svg.attr('height')/2),
		canvasPaddingLeft =  Math.max(0, canvasWidth/2 - svg.attr('width')/2);
		
		sequenceCanvas.css({
			'padding-top'    : canvasPaddingTop,
			// after first entering of Branching in old SVGs we need this adjustment
			'padding-bottom' : branchingEntered ? 20 : 0,
			'padding-left'   : canvasPaddingLeft,
			'width'          : canvasWidth - canvasPaddingLeft,
			'height'         : canvasHeight - canvasPaddingTop
		});
}

//********** LEARNERS TAB FUNCTIONS **********

/**
 * Inits Learners tab widgets.
 */
function initLearnersTab() {
	// search for users with the term the Monitor entered
	$("#learnersSearchPhrase").autocomplete( {
		'source' : LAMS_URL + "monitoring/monitoring.do?method=autocompleteMonitoringLearners&lessonID=" + lessonId,
		'delay'  : 700,
		'select' : function(event, ui){
		    // learner's ID in ui.item.value is not used here
			$(this).val(ui.item.label);
			$('#learnersSearchPhraseClear').show();
			loadLearnerProgressPage(1, ui.item.label);
			return false;
		}
	})
	// run the real search when the Monitor presses Enter
	.keypress(function(e){
		if (e.which == 13) {
			$(this).autocomplete("close");
			if ($(this).val()) {
				$('#learnersSearchPhraseClear').show();
			}
			loadLearnerProgressPage(1);
		}
	});
}


/**
 * Handler for shift page numbers bar.
 */
function learnersPageShift(increment){
	var pageNumberCell = $('#learnersPageLeft').next();
	if (pageNumberCell.hasClass('learnersHeaderPageCell')) {
		var startIndex = +pageNumberCell.text() + (increment ? 10 : -10);
		var endIndex = startIndex + 9;
		shiftLearnerProgressPageHeader(startIndex, endIndex);
	}
}


/**
 * Do the actual shifting of page numbers bar.
 */
function shiftLearnerProgressPageHeader(startIndex, endIndex) {
	var pageLeftCell = $('#learnersPageLeft');
	var pageCount = Math.ceil(learnerPossibleNumber / 10);
	$('#tabLearnerControlTable td.learnersHeaderPageCell').remove();
	
	if (startIndex < 1) {
		startIndex = 1;
		endIndex = 10;
	}
	if (endIndex > pageCount) {
		// put a bit more on the left, since right end of scale is too short
		startIndex -= endIndex - pageCount;
		if (startIndex < 1) {
			startIndex = 1;
		}
		endIndex = pageCount;
	}

	for (var pageIndex = endIndex; pageIndex >= startIndex; pageIndex--) {
		// produce the page number cells
		var pageHeaderCell = 
			$('<td class="learnersHeaderCell learnersHeaderPageCell"></td>')
			.text(pageIndex)
			.insertAfter(pageLeftCell)
			.click(function(){
				loadLearnerProgressPage(+$(this).text());
			});
		if (pageIndex == learnerProgressCurrentPageNumber){
			// highlight the currently selected one
			pageHeaderCell.addClass('selectedLearnersHeaderPageCell');
		}
	}
}

/**
 * After page change, refresh values in the control bar.
 */
function updateLearnerProgressHeader(pageNumber) {
	var controlRow = $('#tabLearnerControlTable tr'),
		learnersSearchPhrase = $('#learnersSearchPhrase').val();
	if (learnerPossibleNumber < 10 && (!learnersSearchPhrase || learnersSearchPhrase.trim() == '')) {
		// do not show the bar at all
		$('.learnersHeaderCell', controlRow).hide();
		return;
	}
	// show the bar
	$('.learnersHeaderCell', controlRow).show();
	
	var pageCount = Math.ceil(learnerPossibleNumber / 10);
	if (!pageNumber) {
		pageNumber = 1;
	} else if (pageNumber > pageCount) {
		pageNumber = pageCount;
	}
	learnerProgressCurrentPageNumber = pageNumber;
	// update "Page X / Y" field
	$('#learnersPageCounter').html(pageNumber + '&nbsp;/&nbsp;' + pageCount + '&nbsp;');
	
	// remove arrows for shifting page numbers, if they are not needed
	if (pageCount < 10) {
		$('td.learnersPageShifter', controlRow).hide();
	}
	
	// calculate currently visible page numbers
	var pageStartIndex = pageNumber - 5;
	var pageEndIndex = pageNumber + 5 - Math.min(pageStartIndex,0);
	shiftLearnerProgressPageHeader(pageStartIndex, pageEndIndex, pageNumber);
}

/**
 * Load the give page of learners' progress.
 */
function loadLearnerProgressPage(pageNumber, learnersSearchPhrase){
	// prevent double refresh at the same time
	if (learnersRefreshInProgress) {
		return;
	}
	learnersRefreshInProgress = true;
	
	if (!learnerProgressCellsTemplate) {
		// fill the placeholder, after all required variables were initialised
		learnerProgressCellsTemplate =
		  '<tr><td class="progressBarLabel" id="progressBarLabel;00;"><div>;11;</div>';
		
		if (enableExportPortfolio) {
			learnerProgressCellsTemplate +=
				'<a class="button" title="' 
		+ LABELS.EXPORT_PORTFOLIO_LEARNER_TOOLTIP + '" href="#" onClick="javascript:openPopUp(\''
		+ LAMS_URL + 'learning/exportWaitingPage.jsp?mode=learner&role=teacher&lessonID='
		+ lessonId + '&userID=;00;\',\'ExportPortfolio\',240,640,true)">'
		+ LABELS.EXPORT_PORTFOLIO
				+ '</a>';
		}

		learnerProgressCellsTemplate +=
			'<a class="button" href="#" onClick="javascript:showEmailDialog(;00;)">'
		+ LABELS.EMAIL_BUTTON
		+ '</a></td></tr><tr><td class="progressBarCell" id="progressBar;00;"></td></tr>';
	}
	
	// remove existing progress bars
	$('#tabLearnersTable').html(null);
	bars = {};
	var isProgressSorted = $('#orderByCompletionCheckbox:checked').length > 0;
	// either go to the given page or refresh the current one
	pageNumber = pageNumber || learnerProgressCurrentPageNumber;
	learnersSearchPhrase = learnersSearchPhrase || $('#learnersSearchPhrase').val();
	if (learnersSearchPhrase && learnersSearchPhrase.trim() == ''){
		learnersSearchPhrase = null;
	}
	
	$.ajax({
		dataType : 'json',
		url : LAMS_URL + 'monitoring/monitoring.do',
		cache : false,
		data : {
			'method'           : 'getLearnerProgressPage',
			'lessonID'         : lessonId,
			'searchPhrase'     : learnersSearchPhrase,
			'pageNumber'       : pageNumber,
			'isProgressSorted' : isProgressSorted
			
		},
		
		success : function(response) {
			learnerPossibleNumber = response.learnerPossibleNumber;
			updateLearnerProgressHeader(pageNumber);
			
			if (response.learners) {
				$.each(response.learners, function(){
					var barId = 'bar' + this.id;
					// create a new bar metadata entry
					bars[barId] = {
						'userId'      : this.id,
						'containerId' : 'progressBar' + this.id
					};
					
					// prepare HTML for progress bar
					var learnerProgressCellsInstance = learnerProgressCellsTemplate
						.replace(/;00;/g, this.id)
						.replace(/;11;/g, getLearnerDisplayName(this));
					$(learnerProgressCellsInstance).appendTo('#tabLearnersTable');
					
					// request data to build progress bar SVG
					fillProgressBar(barId);
				});
			}
			
			learnersRefreshInProgress = false;
		}
	});

}


/**
 * Refreshes the existing progress bars. 
 */
function updateLearnersTab(){
	// prevent double refresh
	if (learnersRefreshInProgress) {
		return;
	}
	learnersRefreshInProgress = true;
	
	for (var barId in bars) {
		fillProgressBar(barId);
	}
	learnersRefreshInProgress = false;
}


/**
 * Clears previous run search for phrase.
 */
function learnersClearSearchPhrase(){
	$('#learnersSearchPhrase').val('').autocomplete("close");
	loadLearnerProgressPage(1, '');
	$('#learnersSearchPhraseClear').hide();
}

//********** COMMON FUNCTIONS **********

/**
 * Updates all changeable elements of monitoring screen.
 */
function refreshMonitor(tabName, isAuto){
	if (autoRefreshIntervalObject && !isAuto) {
		clearInterval(autoRefreshIntervalObject);
		autoRefreshIntervalObject = null;
	}

	if (!autoRefreshIntervalObject) {
		autoRefreshIntervalObject = setInterval(function(){
			if (!autoRefreshBlocked) {
				refreshMonitor(null, true);
			}
		}, autoRefreshInterval);
	}
	
	if (!tabName) {
		// update Lesson tab widgets (state, number of learners etc.)
		updateLessonTab();
		// update learner progress in Sequence tab
		updateSequenceTab();
		// update learner progress in Learners tab
		loadLearnerProgressPage();
	} else if (tabName == 'lesson') {
		updateLessonTab();
	} else if (tabName == 'sequence'){
		updateLessonTab();
		updateSequenceTab();
	} else if (tabName == 'learners'){
		updateLessonTab();
		updateLearnersTab();
	}
}


/**
 * Tells parent document to close this Monitor dialog.
 */
function closeMonitorLessonDialog(refresh) {
	window.parent.closeDialog('dialogMonitorLesson' + lessonId, refresh);
}

/**
 * Show a dialog with user list and optional Force Complete and View Learner buttons.
 */
function showLearnerGroupDialog(ajaxProperties, dialogTitle, allowForceComplete, allowView, allowEmail) {
	var learnerGroupDialog = $('#learnerGroupDialog'),
		learnerGroupList = $('#learnerGroupList', learnerGroupDialog).empty(),
		// no parameters provided? just work on what we saved
		isRefresh = ajaxProperties == null,
		learners = null,
		learnerCount = null;
	
	if (isRefresh) {
		// ajax and other properties were saved when the dialog was opened
		ajaxProperties = learnerGroupDialog.dialog('option', 'ajaxProperties');
		allowForceComplete = learnerGroupDialog.dialog('option', 'allowForceComplete');
		allowView = learnerGroupDialog.dialog('option', 'allowView');
		allowEmail = learnerGroupDialog.dialog('option', 'allowEmail');
	} else {
		// add few standard properties to ones provided by method calls
		ajaxProperties = $.extend(true, ajaxProperties, {
			dataType : 'json',
			cache : false,
			async : false,
			data : {
				'pageNumber' 	 : 1,
				'orderAscending' : true
			}
		});
	}
	
	var pageNumber = ajaxProperties.data.pageNumber;

	// set values for current variable instances
	ajaxProperties.success = function(response) {
		learners = response.learners;
		learnerCount = response.learnerCount;
	};
	
	// make the call
	$.ajax(ajaxProperties);

	// did all users already drift away to an another activity or there was an error?
	// close the dialog and refresh the main screen
	if (!learnerCount) {
		if (isRefresh) {
			 learnerGroupDialog.dialog('close');
		}
		updateSequenceTab();
		return;
	}
	
	// did some users already drift away to an another activity?
	// move back until you get a page with any users
	var maxPageNumber = Math.ceil(learnerCount / 10);
	if (pageNumber > maxPageNumber) {
		shiftLearnerGroup(-1);
		return;
	}
	
	// hide unnecessary controls
	if (pageNumber + 10 <= maxPageNumber) {
		$('#learnerGroupPagePlus10', learnerGroupDialog).css('visibility', 'visible');
	} else {
		$('#learnerGroupPagePlus10', learnerGroupDialog).css('visibility', 'hidden');
	}
	if (pageNumber - 10 < 1) {
		$('#learnerGroupPageMinus10', learnerGroupDialog).css('visibility', 'hidden');
	} else {
		$('#learnerGroupPageMinus10', learnerGroupDialog).css('visibility', 'visible');
	}
	if (pageNumber + 1 <= maxPageNumber) {
		$('#learnerGroupPagePlus1', learnerGroupDialog).css('visibility', 'visible');
	} else {
		$('#learnerGroupPagePlus1', learnerGroupDialog).css('visibility', 'hidden');
	}		
	if (pageNumber - 1 < 1) {
		$('#learnerGroupPageMinus1', learnerGroupDialog).css('visibility', 'hidden');
	} else {
		$('#learnerGroupPageMinus1', learnerGroupDialog).css('visibility', 'visible');
	}
	if (maxPageNumber < 2) {
		$('#learnerGroupPage', learnerGroupDialog).css('visibility', 'hidden');
	} else {
		$('#learnerGroupPage', learnerGroupDialog).css('visibility', 'visible').text(pageNumber + ' / ' + maxPageNumber);
	}
	
		$.each(learners, function(learnerIndex, learner) {
		var viewUrl = allowView ? LAMS_URL + 'monitoring/monitoring.do?method=getLearnerActivityURL&userID=' 
        				       	  + learner.id + '&activityID=' + ajaxProperties.data.activityID + '&lessonID=' + lessonId
        				        : null,
				learnerDiv = $('<div />').attr({
									'userId'  : learner.id,
									'viewUrl'    : viewUrl
									})
			                      .addClass('dialogListItem')
							      .html(getLearnerDisplayName(learner))
							      .appendTo(learnerGroupList);
			
			if (allowForceComplete || allowView || allowEmail) {
				learnerDiv.click(function(){
			    	  // select a learner
			    	  $(this).addClass('dialogListItemSelected')
			    	  	.siblings('div.dialogListItem')
			    	  	.removeClass('dialogListItemSelected');
				    	// enable buttons
				    	$('button.learnerGroupDialogSelectableButton')
				    		.attr('disabled', null);
			    });
				if (allowView){
					dblTap(learnerDiv, function(){
						// same as clicking View Learner button
						openPopUp(viewUrl, "LearnActivity", 600, 800, true);
					});
				}
			}
		});
	
	colorDialogList('learnerGroup');
	
	if (!isRefresh) {
	// show buttons depending on parameters
	$('button#learnerGroupDialogForceCompleteButton')
		.css('display', allowForceComplete ? 'inline' : 'none');
	$('button#learnerGroupDialogViewButton')
		.css('display', allowView ? 'inline' : 'none');
	$('button#learnerGroupDialogEmailButton')
		.css('display', allowEmail ? 'inline' : 'none');

	learnerGroupDialog
		.dialog('option', 
			{
			 'title' : dialogTitle,
				 // save properties for refresh
				 'ajaxProperties' : ajaxProperties,
				 'allowForceComplete' : allowForceComplete,
				 'allowView' : allowView,
				 'allowEmail' : allowEmail
			})
		.dialog('open');	
}
}


/**
 * Formats learner name.
 */
function getLearnerDisplayName(learner, raw) {
	return raw ? learner.firstName + ' ' + learner.lastName + ' (' + learner.login + ')'
			   : escapeHtml(learner.firstName) + ' ' + escapeHtml(learner.lastName) + ' (' + escapeHtml(learner.login) + ')';
}


/**
 * Escapes HTML tags to prevent XSS injection.
 */
function escapeHtml(unsafe) {
	if (unsafe == undefined) {
		return "";
	}
	
    return unsafe
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#039;");
}


/**
 * Change order of learner sorting in class dialog.
 */
function sortLessonClassDialogList(listId) {
	var list = $('#' + listId + 'List'),
		items = list.children('div.dialogListItem'),
		orderAsc = sortOrderAsc[listId];
	if (items.length > 1) {
		items.each(function(){
			$(this).detach();
		}).sort(function(a, b){
			var keyA = $(a).text().toLowerCase();
			var keyB = $(b).text().toLowerCase();
			var result = keyA > keyB ? 1 : keyA < keyB ? -1 : 0;
			return orderAsc ? -result : result;
		}).each(function(){
			$(this).appendTo(list);
		});
		
		var button = $('#' + listId + 'SortButton');
		if (orderAsc) {
			button.html('▼');
			sortOrderAsc[listId] = false;
		} else {
			button.html('▲');
			sortOrderAsc[listId] = true;
		}
	}
}

/**
 * Change order of learner sorting in group dialog.
 */
function sortLearnerGroupDialogList() {
	var learnerGroupDialog = $('#learnerGroupDialog'),
		sortIcon = $('#learnerGroupSort span', learnerGroupDialog),
		ajaxProperties = learnerGroupDialog.dialog('option', 'ajaxProperties'),
		// reverse current order after click
		orderAscending = !ajaxProperties.data.orderAscending;

	if (orderAscending) {
		sortIcon.removeClass('ui-icon-triangle-1-s').addClass('ui-icon-triangle-1-n');
	} else {
		sortIcon.removeClass('ui-icon-triangle-1-n').addClass('ui-icon-triangle-1-s');
	}
	
	ajaxProperties.data.orderAscending = orderAscending;
	// refresh the list
	showLearnerGroupDialog();
}

function selectAllInDialogList(listId) {
	var targetState = $('#' + listId + 'SelectAll').is(':checked') ? 'checked' : null;
	$('#' + listId + 'List input').each(function(){
		if (!$(this).is(':disabled')) {
			$(this).prop('checked', targetState);
		}		
	});
}


function colorDialogList(listId) {
	$('#' + listId + 'List div.dialogListItem').each(function(userIndex, userDiv){
		// every odd learner has different background
		$(userDiv).css('background-color', userIndex % 2 ? '#dfeffc' : 'inherit');
	});
}

/**
* Change page in the group dialog.
*/
function shiftLearnerGroupList(shift) {
	var learnerGroupDialog = $('#learnerGroupDialog'),
		ajaxProperties = learnerGroupDialog.dialog('option', 'ajaxProperties'),
		pageNumber = ajaxProperties.data.pageNumber + shift;
	if (pageNumber < 0) {
		pageNumber = 1;
	}
	ajaxProperties.data.pageNumber = pageNumber;
	// refresh the dialog with new parameters
	showLearnerGroupDialog();
}


/**
 * Makes a XML element with given attributes.
 * jQuery does not work well with SVG in Chrome, so all this manipulation need to be done manually. 
 */
function appendXMLElement(tagName, attributesObject, content, target) {
	var elementText = '<' + tagName + (content ? '>' + content + '</' + tagName + '>'
											   : ' />');
	var element = $.parseXML(elementText).firstChild;
	if (attributesObject) {
		for (attrKey in attributesObject) {
			element.setAttribute(attrKey, attributesObject[attrKey]);
		}
	}

	target.appendChild(element);
	return element;
}

/**
 * Works as dblclick for mobile devices.
 */
function dblTap(elem, dblClickFunction) {
 	 // double tap detection on mobile devices; it works also for mouse clicks
 	 elem.tap(function(event){
		  var currentTime = new Date().getTime(),
		  	  tapLength = currentTime - lastTap;
		  if (tapLength < tapTimeout && tapLength > 0) {
			  event.preventDefault();
			  dblClickFunction(event);
		  }
		  lastTap = currentTime;
	  });
}