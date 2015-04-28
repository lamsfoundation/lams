// ********** GLOBAL VARIABLES **********
// copy of lesson/branching SVG so it does no need to be fetched every time
// HTML with SVG of the lesson
var originalSequenceCanvas = null,
// DIV container for lesson/branching SVG
// it gets accessed so many times it's worth to cache it here
	sequenceCanvas = null,
// ID of currently shown branching activity; if NULL, the whole lesson is shown
	sequenceBranchingId = null,
// info box show timeout
	sequenceInfoTimeout = 10000,
// how learners in pop up lists are currently sorted
	sortOrderAsc = {
		learnerGroup : false,
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
	numberActiveLearners = 0,
// page in Learners tab
	learnerProgressCurrentPageNumber = 1,
// search phrase in Learners tab
	learnersSearchPhrase = null,

//auto refresh all tabs every 30 seconds
	autoRefreshInterval = 30 * 1000,
	autoRefreshIntervalObject = null,
// when user is doing something, do not auto refresh
	autoRefreshBlocked = false,

// double tap support
	tapTimeout = 500,
	lastTap = 0;

// ********* GENERAL TABS FUNCTIONS *********

function initTabs(){
	$('#tabs').tabs({
		'activate' : function(event, ui) {
			var sequenceInfoDialog = $('#sequenceInfoDialog');
			if (ui.newPanel.attr('id') == 'tabSequence') {
				if (sequenceInfoDialog.length > 0
						&& !sequenceInfoDialog.dialog('option', 'showed')) {
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
				helper = dialog.clone().empty();
			helper.css({
				'border': 'thin black solid',
				'width' : dialog.width(),
				'height' : dialog.height(),
				'background' : 'transparent'
			});
			return helper;
		},
		'appendTo' : window.parent.document.body,
		'drag' : function(event, ui) {
			// adjust position to be relative to parent document, not iframe contents
			var dialogName = "dialogMonitorLesson" + lessonId,
				dialog = $('#' + dialogName, window.parent.document).closest('.ui-dialog');
			ui.position.top += dialog.position().top;
			ui.position.left += dialog.position().left;
		},
		'stop' : function(event, ui) {
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
			sortDialogList('classLearner');
			sortDialogList('classMonitor');
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

	$('#classLearnerSortButton').click(function(){
		sortDialogList('classLearner');
	});	
	$('#classMonitorSortButton').click(function(){
		sortDialogList('classMonitor');
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
	$.ajax({
		dataType : 'json',
		url : LAMS_URL + 'monitoring/monitoring.do',
		cache : false,
		data : {
			'method'    : 'getClassMembers',
			'lessonID'  : lessonId,
			'role'      : 'LEARNER',
			'classOnly' : true
		},
		success : function(response) {
			 showLearnerGroupDialog(null, LABELS.LESSON_GROUP_DIALOG_CLASS, response, false, false, true);
		}
	});
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
	var checked = $('#presenceAvailableField').is(':checked');
	var counter = $('#presenceAvailableCount');
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
			'width'     : 330,
			'minWidth'  : 330,
			'modal'     : true,
			'resizable' : true,
			'show'      : 'fold',
			'hide'      : 'fold',
			'open'      : function(){
				autoRefreshBlocked = true;
				// reset sort order
				sortOrderAsc['learnerGroup'] = false;
				sortDialogList('learnerGroup');
				colorDialogList('learnerGroup');
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
			            			var activityId = $(this).dialog('option', 'activityId');
			            			var dropArea = sequenceCanvas.add('#completedLearnersContainer');
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
	
	$('#learnerGroupSortButton').click(function(){
		sortDialogList('learnerGroup');
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
}
	


/**
 * Updates learner progress in sequence tab according to respose sent to refreshMonitor()
 */
function updateSequenceTab() {
	if (sequenceRefreshInProgress) {
		return;
	}
	sequenceRefreshInProgress = true;
	
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
				originalSequenceCanvas = response;
				sequenceCanvas = $('#sequenceCanvas')
					// remove previously set padding and dimensions, if any
					.removeAttr('style')
					.html(originalSequenceCanvas)
					// if it was faded out by showBranchingSequence()
					.fadeIn();
				
				resizeSequenceCanvas();
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
			'branchingActivityID' : sequenceBranchingId
		},		
		success : function(response) {
			// remove the loading animation
			$('img#sequenceCanvasLoading', sequenceTopButtonsContainer).remove();

			var learnerCount = 0,
				reloadSVG = false;
			$.each(response.activities, function(index, activity){
				// are there any learners in this or any activity?
				learnerCount += activity.learners ? activity.learners.length : 0;
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
			
			var completedLearners = response.completedLearners,
				learnerTotalCount = learnerCount + (completedLearners ? completedLearners.length : 0 );
			$('#learnersStartedPossibleCell').text(learnerTotalCount + ' / ' + response.numberPossibleLearners);
			addCompletedLearnerIcons(completedLearners, learnerTotalCount);
			
			$.each(response.activities, function(activityIndex, activity){
				addActivityIconsHandlers(activity);
				
				var isBranching = [10,11,12,13].indexOf(activity.type) > -1;
				if (activity.url || (isBranching && !activity.flaFormat)) {
					// find the activity in SVG
					var coord = getActivityCoordinates(activity);
					if (!coord || !coord.elem) {
						return;
					}
					
					var activityElems = [coord.elem],
						dblClickFunction = 
							// different behaviour for regular/branching activities
							isBranching ? 
							function(){ showBranchingSequence(activity.id); }
							:
							function(){  
								// double click on activity shape to open Monitoring for this activity
								openPopUp(LAMS_URL + activity.url, "MonitorActivity", 720, 900, true, true);
							};
							
					// find the activity image, but skip learner and attention icons
					$('image:not([id^="act"])', sequenceCanvas).each(function(){
						var image = $(this),
							x = +image.attr('x'),
							y = +image.attr('y');
						if (x > coord.x && x < coord.x2 && y > coord.y && y < coord.y2) {
							activityElems.push(image);
						}
					});
					
					// find activity group, if it is not hidden
					$.each(activityElems, function(){
						$(this).css('cursor', 'pointer')
							  // double tap detection on mobile devices; it works also for mouse clicks
							  .tap(function(event){
								  var currentTime = new Date().getTime(),
								  	  tapLength = currentTime - lastTap;
								  if (tapLength < tapTimeout && tapLength > 0) {
									  event.preventDefault();
									  dblClickFunction();
								  }
								  lastTap = currentTime;
							  });
					});
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
	// check all activities and "users who finished lesson" bar
	$('rect[id^="act"], g polygon', sequenceCanvas).add('#completedLearnersContainer').each(function(){
		// find which activity learner was dropped on
		var act = $(this),
			actX = act.offset().left,
			actY = act.offset().top,
			actWidth = act.width(),
			actHeight = act.height();
		if (!actWidth) {
			actWidth = +act.attr('width');
			actHeight = +act.attr('height');
		}
		if (!actWidth && act.is('polygon')){
			// just for Gate activity
			var polygonPoints = act.attr('points').split(' ');
			actWidth = +polygonPoints[5].split(',')[0] - +polygonPoints[2].split(',')[0];
			actHeight = +polygonPoints[0].split(',')[1] - +polygonPoints[3].split(',')[1];
		}
		var actEndX = actX + actWidth,
			actEndY = actY + actHeight;
		
		if (x >= actX && x<= actEndX && y>= actY && y<=actEndY) {
			var targetActivityId = null;
			var executeForceComplete = false;
			
			if (act.attr('id') == 'completedLearnersContainer') {
				executeForceComplete =  currentActivityId && confirm(LABELS.FORCE_COMPLETE_END_LESSON_CONFIRM
						.replace('[0]',learnerName));
			} else {
				var targetActivityId = act.parent().attr('id');
				if (currentActivityId != targetActivityId) {
					
					var precedingActivityId = currentActivityId,
						targetActivityName = act.is('polygon') ? "Gate" 
							: act.siblings('text[id^="TextElement"]').text();
					
					// find out if we are moving learner forward or backwards
					while (precedingActivityId){
						// find transition line and extract activity IDs from them
						var transitionLine = $('line[id$="to_' 
								+ precedingActivityId + '"]:not([id^="arrow"])'
								, sequenceCanvas);
						precedingActivityId = transitionLine.length == 1 ? 
								transitionLine.attr('id').split('_')[0] : null;
						if (targetActivityId == precedingActivityId) {
							break;
						}
					};
					
					// check if the target activity was found or we are moving the learner from end of lesson
					if (!currentActivityId || precedingActivityId) {
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
			// we found our target, stop iteration
			return false;
		}
	});
	
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
	if (!activity.learners && !activity.requiresAttention) {
		return;
	}
	
	// fint the activity in SVG
	var coord = getActivityCoordinates(activity);
	if (!coord) {
		return;
	}
	
	// add group of users icon
	var activityGroup = $('g#' + activity.id, sequenceCanvas),
		// in old SVG format, add to a group; in new, go straight for the SVG root element
		appendTarget = (activityGroup.length > 0 ? activityGroup : $('svg', sequenceCanvas))[0],
		// branching and gates require extra adjustments
		isNewBranching =  [10,11,12,13].indexOf(activity.type) > -1 && activity.flaFormat,
		isGate = [3,4,5,14].indexOf(activity.type) > -1;
	
	if (activity.learners){
		var	groupTitle = activity.learners.length + ' ' + LABELS.LEARNER_GROUP_COUNT + ' ' + LABELS.LEARNER_GROUP_SHOW,
		// if icons do not fit in shape anymore, show a group icon
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
		}, activity.learners.length, appendTarget);
		appendXMLElement('title', null, groupTitle, element);
	
		var actTooltip = LABELS.LEARNER_GROUP_LIST_TITLE;
		// draw single icons for the first few learners;
		// don't do it for gate and optional activities, and new branching/optional sequences format
		if ([3,4,5,7,13,14].indexOf(activity.type) == -1 && !activity.flaFormat) {
			$.each(activity.learners, function(learnerIndex, learner){
				var learnerDisplayName = getLearnerDisplayName(learner);
				actTooltip += '\n' + learnerDisplayName;
				
				if (learnerIndex < 7) {
					element = appendXMLElement('image', {
						'id'         : 'act' + activity.id + 'learner' + learner.id,
						'x'          : coord.x + learnerIndex*15 + 1,
						// a bit lower for Optional Activity
						'y'          : coord.y,
						'height'     : 16,
						'width'      : 16,
						'xlink:href' : LAMS_URL + 'images/icons/user.png',
						'style'		 : 'cursor : pointer'
					}, null, appendTarget);
					appendXMLElement('title', null, learnerDisplayName, element);
				}
			});
		}
		
		appendXMLElement('title', null, actTooltip, appendTarget);
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
	if (!activity.learners && !activity.requiresAttention) {
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
				learnerIcon.dblclick(function(event){
					 // double click on learner icon to see activity from his perspective
					event.stopPropagation();
					var url = LAMS_URL + 'monitoring/monitoring.do?method=getLearnerActivityURL&userID=' 
						               + learner.id + '&activityID=' + activity.id + '&lessonID=' + lessonId;
					openPopUp(url, "LearnActivity", 600, 800, true);
				});
			}
		});
		
		
		$('*[id^="act' + activity.id + 'learnerGroup"]', sequenceCanvas).dblclick(function(event){
			 // double click on learner group icon to see list of learners
			event.stopPropagation();
			showLearnerGroupDialog(activity.id, activity.title, activity.learners, true, usersViewable);
		});
	}
	
	if (activity.requiresAttention){
		$('*[id^="act' + activity.id + 'attention"]', sequenceCanvas).click(function(event){
			event.stopPropagation();
			// switch to first tab where attention prompts are listed
			$('#tabs').tabs('select', 0);     
		});
	}
}


/**
 * Add learner icons in "finished lesson" bar.
 */
function addCompletedLearnerIcons(learners, learnerTotalCount) {
	var iconsContainer = $('#completedLearnersContainer');
	var completedLearnerCount = (learners ? learners.length : 0 );
	// show (current/total) label
	$('<span />').attr({
		'title' : LABELS.LEARNER_FINISHED_COUNT
			.replace('[0]', completedLearnerCount).replace('[1]', learnerTotalCount)
	}).text('(' + completedLearnerCount + '/' + learnerTotalCount + ')')
	  .appendTo(iconsContainer);
	
	if (learners) {
		// create learner icons, along with handlers
		$.each(learners, function(learnerIndex, learner){
			// maximum 55 icons in the bar
			if (learnerIndex < 55) {
				// make an icon for each learner
				$('<img />').attr({
					'src' : LAMS_URL + 'images/icons/user.png',
					'title'      : getLearnerDisplayName(learner)
				}).css('cursor', 'pointer')
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
			}
		});
		
		// show a group icon
		$('<img />').attr({
			'src' : LAMS_URL + 'images/icons/group.png',
			'title'      : LABELS.LEARNER_GROUP_SHOW
		}).css('cursor', 'pointer')
		  .dblclick(function(){
			showLearnerGroupDialog(null, LABELS.LEARNER_FINISHED_DIALOG_TITLE, learners, true, false);
		}).appendTo(iconsContainer);
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
	if ([10,11,12,13].indexOf(activity.type) > -1 && activity.flaFormat) {
		return {
			'x'  : activity.x,
			'y'  : activity.y
		}
	}
	
	// get either rectangle from old Batik SVG format
	// or path from new Flashless Authoring format (IE and other browsers format paths differently)
	var elem = $('rect[x="'    + activity.x + '.0"][y="' + activity.y + '.0"], ' + 
				 'rect[x="'    + activity.x + '"][y="'   + activity.y + '"], ' + 
				 'path[d^="M'  + activity.x + ',' 	     + activity.y +'"], ' + 
				 'path[d^="M ' + activity.x + ' '        + activity.y +'"]',
				 sequenceCanvas);
	if (elem.length == 0) {
		return;
	}
	
	// if it's a rectangle, it has these attributes
	var width = elem.attr('width'),
		height = elem.attr('height');
	if (width) {
		return {
			'elem' : elem,
			'x'    : activity.x,
			'y'    : activity.y,
			'x2'   : activity.x + +width,
			'y2'   : activity.y + +height
		}
	} else {
		// extract width and height from path M<x>,<y>H<width>V<height>... or M <x> <y> H <width> V <height>...
		var match = /H\s?(\d+)\s?V\s?(\d+)/i.exec(elem.attr('d'));
		if (match) {
			return {
				'elem' : elem,
				'x'    : activity.x,
				'y'    : activity.y + 1,
				'x2'   : +match[1],
				'y2'   : +match[2]
			}
		}
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
			'role'      : 'LEARNER',
			'classOnly' : false
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
			'role'      : 'MONITOR',
			'classOnly' : false
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
	    			$('#' + listId + 'SelectAll').attr('checked', 'checked');
	    		}
	    	} else {
	    		$('#' + listId + 'SelectAll').attr('checked', null);
	    	}
	    	
	      });
		if (user.classMember) {
			checkbox.attr('checked', 'checked');
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
		    		checkbox.attr('checked', checkbox.is(':checked') ? null : 'checked');
		    	}
		    })
		}
	});	

	$('#' + listId + 'SelectAll').attr('checked', selectAllInitState ? 'checked' : null);
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
					openPopUp(LAMS_URL + 'home.do?method=author&layout=editonfly&learningDesignID=' + ldId,
							'LiveEdit', 600, 800, false);
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
	// Initial resize sends no parameters.
	// When resizing the dialog, parameters are sent.
	// Taking body's dimensions when resizing yields erroneous results.
	width = width || $('body').width();
	height = height || $('body').height();
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
			'padding-top'  : canvasPaddingTop,
			'padding-left' : canvasPaddingLeft,
			'width'        : canvasWidth - canvasPaddingLeft,
			'height'       : canvasHeight - canvasPaddingTop
		});
}

//********** LEARNERS TAB FUNCTIONS **********

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
	var pageCount = Math.ceil(numberActiveLearners / 10);
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
	var controlRow = $('#tabLearnerControlTable tr');
	if (numberActiveLearners < 10 
			&& (!learnersSearchPhrase || learnersSearchPhrase == '')) {
		// do not show the bar at all
		$('.learnersHeaderCell', controlRow).hide();
		return;
	}
	// show the bar
	$('.learnersHeaderCell', controlRow).show();
	
	var pageCount = Math.ceil(numberActiveLearners / 10);
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
function loadLearnerProgressPage(pageNumber){
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
		/* + <a class="button" title="'
		+ LABELS.TIME_CHART_TOOLTIP + '" href="#" onClick="javascript:openPopUp(\''
		+ LAMS_URL + 'monitoring/monitoring.do?method=viewTimeChart&lessonID='
		+ lessonId + '&learnerID=;00;\',\'TimeChart\',600,800,true)">'
		+ LABELS.TIME_CHART 
		+ '</a>'*/
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
			numberActiveLearners = response.numberActiveLearners;
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
 * Run search for the phrase which user provided in text field.
 */
function learnersRunSearchPhrase(){
	var searchPhraseField = $('#learnersSearchPhrase');
	learnersSearchPhrase = searchPhraseField.val();
	if (learnersSearchPhrase && learnersSearchPhrase.trim() != '') {
		var pageNumber = parseInt(learnersSearchPhrase);
		// must be a positive integer
		if (isNaN(pageNumber) || !isFinite(pageNumber) || pageNumber < 0){
			// if it was not a number, run a normal search
			loadLearnerProgressPage(1);
		} else {
			// it was a number, reset the field and go to the given page
			learnersSearchPhrase = null;
			searchPhraseField.val(null);
			loadLearnerProgressPage(pageNumber);
		}
	} else {
		learnersSearchPhrase = null;
	}
}


/**
 * Clears previous run search for phrase.
 */
function learnersClearSearchPhrase(){
	learnersSearchPhrase = null;
	$('#learnersSearchPhrase').val(null);
	loadLearnerProgressPage(1);
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
	var dialogName = "dialogMonitorLesson" + lessonId;
	window.parent.closeMonitorLessonDialog(dialogName, refresh);
}

/**
 * Show a dialog with user list and optional Force Complete and View Learner buttons.
 */
function showLearnerGroupDialog(activityId, dialogTitle, learners, allowForceComplete, allowView, allowEmail) {
	var learnerGroupList = $('#learnerGroupList').empty();
	var learnerGroupDialog = $('#learnerGroupDialog');
	
	if (learners) {
		$.each(learners, function(learnerIndex, learner) {
			var viewUrl = LAMS_URL + 'monitoring/monitoring.do?method=getLearnerActivityURL&userID=' 
            				       + learner.id + '&activityID=' + activityId + '&lessonID=' + lessonId,
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
					learnerDiv.dblclick(function(){
						// same as clicking View Learner button
						openPopUp(viewUrl, "LearnActivity", 600, 800, true);
					});
				}
			}
		});
	}
	
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
			 'activityId' : activityId
			})
		.dialog('open');	
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
 * Change order of learner sorting in group dialog.
 */
function sortDialogList(listId) {
	var list = $('#' + listId + 'List');
	var items = list.children('div.dialogListItem');
	var orderAsc = sortOrderAsc[listId];
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

function selectAllInDialogList(listId) {
	var targetState = $('#' + listId + 'SelectAll').is(':checked') ? 'checked' : null;
	$('#' + listId + 'List input').each(function(){
		if (!$(this).is(':disabled')) {
			$(this).attr('checked', targetState);
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