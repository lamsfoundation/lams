﻿﻿﻿﻿﻿﻿﻿﻿﻿// ********** GLOBAL VARIABLES **********
// copy of lesson SVG so it does no need to be fetched every time
var originalSequenceCanvas = null,
// DIV container for lesson SVG
// it gets accessed so many times it's worth to cache it here
	sequenceCanvas = $('#sequenceCanvas'),
// switch between SVG original size and fit-to-sreen (enlarge/shrink)
	learningDesignSvgFitScreen = false,
// info box show timeout
	sequenceInfoTimeout = 8000,
// which learner was selected in the search box
	sequenceSearchedLearner = null,
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
	lastTapTime = 0,
	lastTapTarget = null,
// popup window size
	popupWidth = 1280,
	popupHeight = 720,
	
	gateOpenIconPath   = 'images/svg/gateOpen.svg',
	gateOpenIconData   = null;



//********** LESSON TAB FUNCTIONS **********

/**
 * Sets up lesson tab.
 */
function initLessonTab(){
	// sets presence availability. buttons may be temporarily disable by the tour.
	$('#presenceButton').click(function(){
		var checked = $(this).toggleClass('btn-success').hasClass('btn-success');
		var data = {
			'presenceAvailable' : checked,
			'lessonID'      : lessonId
		};
		data[csrfTokenName] = csrfTokenValue;
		$.ajax({
			url : LAMS_URL + 'monitoring/monitoring/presenceAvailable.do',
			type : 'POST',
			cache : false,
			data : data, 
			success : function() {
				updatePresenceAvailableCount();
				if (checked) {
					$('#imButton').show();
					$('#imButton').prop('disabled', false);
					alert(LABELS.LESSON_PRESENCE_ENABLE_ALERT);
				} else {
					$('#imButton').removeClass('btn-success').hide();
					alert(LABELS.LESSON_PRESENCE_DISABLE_ALERT);
				}
			}
		});
	});
	
	// sets instant messaging availability
	$('#imButton').click(function(){
		var checked = $(this).toggleClass('btn-success').hasClass('btn-success');
		var data = {
			'presenceImAvailable' : checked,
			'lessonID'      : lessonId
		};
		data[csrfTokenName] = csrfTokenValue;
		$.ajax({
			url : LAMS_URL + 'monitoring/monitoring/presenceImAvailable.do',
			type : 'POST',
			cache : false,
			data : data,
			success : function() {
				if (checked) {
					$('#openImButton').show();
					$('#openImButton').prop('disabled', false);
					alert(LABELS.LESSON_IM_ENABLE_ALERT);
				} else {
					$('#openImButton').hide();
					alert(LABELS.LESSON_IM_DISABLE_ALERT);
				}
			}
		});
	});
	
	$('#openImButton').click(openChatWindow);

	//turn to inline mode for x-editable.js
	$.fn.editable.defaults.mode = 'inline';
	//enable renaming of lesson title  
	$('#lesson-name-strong').editable({
	    type: 'text',
	    pk: lessonId,
	    url: LAMS_URL + 'monitoring/monitoring/renameLesson.do?' + $("#csrf-form", window.parent.document).serialize(),
	    validate: function(value) {
		    //close editing area on validation failure
            if (!value.trim()) {
                $('.editable-open').editableContainer('hide', 'cancel');
                return 'Can not be empty!';
            }
        },
	    //assume server response: 200 Ok {status: 'error', msg: 'field cannot be empty!'}
	    success: function(response, newValue) {
	        if(response.status == 'error') {
	        	return response.msg; //msg will be shown in editable form
	        }
	    }
    //hide and show pencil on showing and hiding editing widget
	}).on('shown', function(e, editable) {
		$(this).nextAll('i.fa-pencil').hide();
	}).on('hidden', function(e, reason) {
		$(this).nextAll('i.fa-pencil').show();
	});
	
	// sets up calendar for schedule date choice
	$('#scheduleDatetimeField').datetimepicker({
		'minDate' : 0
	});
	// sets up calendar for schedule date choice
	$('#disableDatetimeField').datetimepicker({
		'minDate' : 0
	});
	
	// sets up dialog for editing class
	var classDialog = showDialog('classDialog',{
		'autoOpen'  : false,
		'width'     : 950,
		'title' 	: LABELS.LESSON_EDIT_CLASS,
		'resizable' : true,
		'open'      : function(){
			autoRefreshBlocked = true;
		},
		'close' : function(){
			autoRefreshBlocked = false;
			refreshMonitor();
		}
	}, false);
	
	$('.modal-body', classDialog).empty().append($('#classDialogContents').show());
	
	// search for users in the organisation with the term the Monitor entered
	$('.dialogSearchPhrase', classDialog).autocomplete({
			'source' : LAMS_URL + "monitoring/monitoring/autocomplete.do?scope=organisation&lessonID=" + lessonId,
			'delay'  : 700,
			'select' : function(event, ui){
				var phraseField = $(this),
					dialog = $('#classDialog');
			    // learner's ID in ui.item.value is not used here
				phraseField.val(ui.item.label);
				// show the "clear search phrase" button
				$('.dialogSearchPhraseClear', dialog).css('visibility', 'visible');
				// reset to page 1
				dialog.data('LearnerAjaxProperties').data.pageNumber = 1;
				showClassDialog('Learner');
				return false;
		   }
		})
		// run the real search when the Monitor presses Enter
		.keypress(function(e){
			if (e.which == 13) {
				var phraseField = $(this),
					dialog = $('#classDialog');
		            		
				phraseField.autocomplete("close");
				if (phraseField.val()) {
					$('.dialogSearchPhraseClear', dialog).css('visibility', 'visible');
		        }
				// reset to page 1
				dialog.data('LearnerAjaxProperties').pageNumber = 1;
				showClassDialog('Learner');
			}
		});

	var emailProgressDialog = showDialog('emailProgressDialog',{
			'autoOpen'  : false,
			'height'    : 700,
			'width'     : 510,
			'title' 	: LABELS.PROGRESS_EMAIL_TITLE,
			'resizable' : true,
			'open'      : function(){
				autoRefreshBlocked = true;
			},
			'close' : function(){
				autoRefreshBlocked = false;
			}
		}, false);
	$('.modal-body', emailProgressDialog).empty().append($('#emailProgressDialogContents').show());
	//initialize datetimepicker
	$("#emaildatePicker").datetimepicker();

	// sets gradebook on complete functionality
	$('#gradebookOnCompleteButton').click(function(){
		var checked = $(this).toggleClass('btn-success').hasClass('btn-success');
		var data = {
			'gradebookOnComplete' : checked,
			'lessonID'      : lessonId
		};
		data[csrfTokenName] = csrfTokenValue;
		$.ajax({
			url : LAMS_URL + 'monitoring/monitoring/gradebookOnComplete.do',
			type : 'POST',
			cache : false,
			data : data,
			success : function() {
				if (checked) {
					alert(LABELS.LESSON_ACTIVITY_SCORES_ENABLE_ALERT);
				} else {
					alert(LABELS.LESSON_ACTIVITY_SCORES_DISABLE_ALERT);
				}
			}
		});
	});
	

}

/**
 * Shows all learners in the lesson class.
 */
function showLessonLearnersDialog() {
	var ajaxProperties = {
		url : LAMS_URL + 'monitoring/monitoring/getLessonLearners.do',
		data : {
			'lessonID'  : lessonId
		}
	};
	
	showLearnerGroupDialog(ajaxProperties, LABELS.LESSON_GROUP_DIALOG_CLASS, true, false, false, true);
}

/** 
 * Lesson state field changed but the apply button not yet pressed
 */
function lessonStateFieldChanged() {

	//state chosen in the dropdown menu
	var state = +$('#lessonStateField').val();
	switch (state) {
		//'disable' is chosen
		case 4: 
			$('#lessonScheduler').show();
			$('#lessonStartApply').hide();
			$('#lessonStateApply').hide();
			$("#scheduleDisableLessonButton").html(LABELS.SCHEDULE);
			$("#scheduleDisableLessonButton").css('display', 'inline'); // must be inline or it will be wrong size
			$("#disableLessonButton").show();
			$('#lessonDisableApply').show();
			break;
		default:
			$('#lessonDisableApply').hide();
			$('#lessonStateApply').show();		
			break;
	}
}

/**
 * Apply the lesson state change and update widgets.
 */
function changeLessonState(){
	var method = null;
	
	//state chosen in the dropdown menu
	var state = +$('#lessonStateField').val();
	switch (state) {
	
		//'activate' is chosen
		case 3:
			switch (lessonStateId) {
				case 4: //SUSPENDED
					method = "unsuspendLesson";
					break;
				case 6: //ARCHIVED
					method = "unarchiveLesson";
					break;
			}
			break;
			
			
		//'disable' is handled by scheduleDisableLesson, disableLesson
		// case 4:

		//'archive' is chosen
		case 6: 
			method = "archiveLesson";
			break;
			
		//'remove' is chosen
		case 7: 
			if (confirm(LABELS.LESSON_REMOVE_ALERT)){
				if (confirm(LABELS.LESSON_REMOVE_DOUBLECHECK_ALERT)) {
					method = "removeLesson";
				}
			}
			break;
	}
	
	if (method) {
		applyStateChange(state, method)
	}
}

function scheduleDisableLesson() {
	var date = $('#disableDatetimeField').val();
	if (date) {
		if ( checkScheduleDate(lessonStartDate, date) ) {
			applyStateChange(4, "suspendLesson", date);
		} else {
			alert(LABELS.LESSON_ERROR_START_END_DATE);
		}
	} else {
		alert(LABELS.LESSON_ERROR_SCHEDULE_DATE);
	}
}			

function disableLesson() {
	applyStateChange(4, "suspendLesson");
}			

function applyStateChange(state, method, newLessonEndDate) {
	var params = {
		'lessonID'      : lessonId
	};
	params[csrfTokenName] = csrfTokenValue;

	if (newLessonEndDate) {
		params.lessonEndDate = newLessonEndDate;
	}
	
	$.ajax({
		url : LAMS_URL + 'monitoring/monitoring/' + method + ".do",
		data: params,
		type: "POST",
		cache : false,
	    success: function() {
			if (state == 7) {
				// user chose to finish the lesson, close monitoring and refresh the lesson list
				closeMonitorLessonDialog(true);
			} else {
				refreshMonitor('lesson');
			}
			if ( state == 4 ) {
				lessonEndDate = newLessonEndDate;
			}
	    }
	});
}

/**
 * Updates widgets in lesson tab according to response sent to refreshMonitor()
 */
function updateLessonTab(){
	$.ajax({
		dataType : 'json',
		url : LAMS_URL + 'monitoring/monitoring/getLessonDetails.do',
		cache : false,
		data : {
			'lessonID'  : lessonId
		},
		
		success : function(response) {
			// update lesson state label
			lessonStateId = +response.lessonStateID;
			var label = null,
				labelColour = 'warning';
			switch (lessonStateId) {
				case 1:
					label = LABELS.LESSON_STATE_CREATED;
					labelColour = 'warning';
					break;
				case 2:
					label = LABELS.LESSON_STATE_SCHEDULED;
					labelColour = 'warning';
					break;
				case 3:
					label = LABELS.LESSON_STATE_STARTED;
					labelColour = 'success';
					break;
				case 4:
					label = LABELS.LESSON_STATE_SUSPENDED;
					labelColour = 'danger';
					break;
				case 5:
					label = LABELS.LESSON_STATE_FINISHED;
					labelColour = 'danger';
					break;
				case 6:
					label = LABELS.LESSON_STATE_ARCHIVED;
					labelColour = 'danger';
					break;
				case 7:
					label = LABELS.LESSON_STATE_REMOVED;
					labelColour = 'danger'; 
					break;
			}
			$('#lessonStateLabel').attr('class', 'label label-' + labelColour).html(label + ' <i class="fa fa-angle-double-down"></i>');
			
			// update available options in change state dropdown menu
			var selectField = $('#lessonStateField');
			// remove all except "Select status" option
			selectField.children('option:not([value="-1"])').remove();
			switch (lessonStateId) {
				case 3:
					if ( ! ( lessonEndDate && lessonEndDate > "") ) {
						$('<option />').attr('value', 4).text(LABELS.LESSON_STATE_ACTION_DISABLE).appendTo(selectField);
					}
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
			var scheduleControls = $('#scheduleDatetimeField, #scheduleLessonButton, #startLessonButton, #lessonScheduler, #scheduleDisableLessonButton, #disableLessonButton'),
				startDateField = $('#lessonStartDateSpan'),
				lessonFinishDateSpan = $('#lessonFinishDateSpan'),
				disableDateSpan = $('#lessonDisableApply'),
				lessonStateChanger = $('#lessonStateChanger'),
				stateLabel = $('#lessonStateLabel');
			switch (lessonStateId) {
				//created but not started lesson
				case 1:
					scheduleControls.css('display','inline');
				 	if ( response.finishDate ) {
				 		lessonFinishDateSpan.text(LABELS.LESSON_FINISH.replace("%0",response.finishDate)).css('display','inline');
						$("#scheduleDisableLessonButton").html(LABELS.RESCHEDULE);
						$("#disableLessonButton").css('display', 'none');
				 	}
					startDateField.hide();
					lessonFinishDateSpan.hide();
					lessonStateChanger.hide();
					break;
				//scheduled lesson
				case 2:
					scheduleControls.css('display','inline');
					startDateField.text(LABELS.LESSON_START.replace("%0",response.startDate)).add('#startLessonButton').css('display','inline');
					$("#scheduleLessonButton").html(LABELS.RESCHEDULE);
				 	if ( response.finishDate ) {
				 		lessonFinishDateSpan.text(LABELS.LESSON_FINISH.replace("%0",response.finishDate)).css('display','block');
						$("#scheduleDisableLessonButton").html(LABELS.RESCHEDULE);
						$("#disableLessonButton").css('display', 'none');
				 	} else {
				 		lessonFinishDateSpan.css('display','none');
				 	}
					lessonStateChanger.hide();
					break;
				//started lesson
				default: 	
					startDateField.text("").css('display','none'); // we may have just started the lesson and needed to clear the scheduled date message
				 	if ( response.finishDate ) {
						scheduleControls.css('display','inline');
						$("#lessonStartApply").css('display','none');
				 		lessonFinishDateSpan.text(LABELS.LESSON_FINISH.replace("%0",response.finishDate)).css('display','inline');
						$("#scheduleDisableLessonButton").html(LABELS.RESCHEDULE);
				 	} else {
						scheduleControls.css('display','none');
						$("#scheduleDisableLessonButton").html(LABELS.SCHEDULE);
				 		lessonFinishDateSpan.text("").css('display','none');
				 	}

				 	lessonStateChanger.css('display','inline');
				 	stateLabel.attr('title',response.startDate);
				 	break;
			}
			
			updateContributeActivities(response.contributeActivities);
			$('#lesson-name-strong').html(response.lessonName);
			$('#lesson-instructions').html(response.lessonInstructions);
		}
	});
	
	drawChart('pie', 'chartDiv',
			  LAMS_URL + 'monitoring/monitoring/getLessonChartData.do?lessonID=' + lessonId,
			  true);
	
	updatePresenceAvailableCount();
}

function checkScheduleDate(startDateString, endDateString) {
	var startDate = startDateString && startDateString > "" ? Date.parse(startDateString) : 0;
	var endDate =  endDateString && endDateString > "" ? Date.parse(endDateString) : 0;
	return ( endDate == 0 || startDate < endDate );
}

function scheduleLesson(){
	var date = $('#scheduleDatetimeField').val();
	if (date) {
		if ( checkScheduleDate (date, lessonEndDate) ) {
			$.ajax({
				url : LAMS_URL + 'monitoring/monitoring/startOnScheduleLesson.do',
				cache : false,
				data : {
					'lessonID'        : lessonId,
					'lessonStartDate' : date
				},
				success : function() {
					lessonStartDate = date;
					refreshMonitor('lesson');
				}
			});
		} else {
			alert(LABELS.LESSON_ERROR_START_END_DATE);
		}
	} else {
		alert(LABELS.LESSON_ERROR_SCHEDULE_DATE);
	}
}


function startLesson(){
	var data = {
		'lessonID': lessonId
	};
	data[csrfTokenName] = csrfTokenValue;
	$.ajax({
		dataType : 'text',
		url : LAMS_URL + 'monitoring/monitoring/startLesson.do',
		data : data,
		cache : false,
		type : 'POST',
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
	window.open(LAMS_URL + 'learning/presenceChat.jsp?lessonID=' + lessonId 
			+ '&presenceEnabledPatch=true&presenceImEnabled=true&presenceShown=true&createDateTime='
			+ createDateTimeStr
			,'Chat'
			,'width=650,height=350,resizable=no,scrollbars=no,status=no,menubar=no,toolbar=no');
}

function showEmailDialog(userId){
	
	//check whether current window is a top level one (otherwise it's an iframe or popup)
	var isTopLevelWindow = window.top == window.self;
	//calculate width and height based on the dimensions of the window to which dialog is added
	var dialogWindow = isTopLevelWindow ? $(window) : $(window.parent);
	
	var dialog = showDialog("dialogEmail", {
		'autoOpen'  : true,
		'height'    : Math.max(380, Math.min(700, dialogWindow.height() - 30)),
		'width'     : Math.max(380, Math.min(700, dialogWindow.width() - 60)),
		'modal'     : true,
		'resizable' : true,
		'title'     : LABELS.EMAIL_TITLE,
		//dialog needs to be added to a top level window to avoid boundary limitations of the interim iframe
		"isCreateInParentWindow" : !isTopLevelWindow, 
		'open'      : function(){
			autoRefreshBlocked = true;
			var dialog = $(this);
			// load contents after opening the dialog
			$('iframe', dialog).attr('src',
					LAMS_URL + 'emailUser/composeMail.do?lessonID=' + lessonId
					+ '&userID=' + userId);
		},
		'close' : function(){
			autoRefreshBlocked = false;
			$(this).remove();
		}
	}, false, true);
}


function updatePresenceAvailableCount(){
	var checked = $('#presenceButton').hasClass('btn-success'),
		counter = $('#presenceCounter');
	if (checked) {
		$.ajax({
			dataType : 'text',
			url : LAMS_URL + 'learning/learner/getPresenceChatActiveUserCount.do?',
			cache : false,
			data : {
				'lessonID'      : lessonId
			},
			success : function(result) {
				counter.text(result).show();
			}
		});
	} else {
		counter.hide();
	}
}

function updateContributeActivities(contributeActivities) {
	$('.contributeRow').remove();
	var header = $('#contributeHeader'),
		row = header;
	
	// special case - add a Live Edit option. This does not directly map to an activity
	if ( lockedForEdit && lockedForEditUserId == userId) {
		// show Live Edit task only if currently editing myself, not if someone else is editing.
		// put it at the top of the contribution list
		var cell = $('<div />').addClass('contributeActivityCell').text(LABELS.LIVE_EDIT_BUTTON);
		var row = $('<div />').addClass('contributeRow').insertAfter(row).append(cell);
		var entryContent = LABELS.LIVE_EDIT_TOOLTIP 
			+ '<span class="btn btn-xs btn-primary pull-right" onClick="javascript:openLiveEdit()" title="' + LABELS.CONTRIBUTE_TOOLTIP
			+ '">' + LABELS.CONTRIBUTE_BUTTON + '</span>';
		cell = $('<div />').addClass('contributeEntryCell').html(entryContent);
		row = row.append(cell);
	}
	if (contributeActivities) {
		$.each(contributeActivities, function(){
			var contributeId = 'contribute' + this.activityID,
				contributeActivity = this,
				cell = $('<div />').addClass('contributeActivityCell').text(this.title).attr('id', contributeId);
			row = $('<div />').addClass('contributeRow').insertAfter(row).append(cell);
			
			$.each(this.contributeEntries, function(){
				var entryContent = '';
				switch (this.contributionType) {
					case 3  : entryContent = LABELS.CONTRIBUTE_GATE; break;
					case 6  : entryContent = LABELS.CONTRIBUTE_GROUPING; break;
					case 7  : entryContent = LABELS.CONTRIBUTE_TOOL; break;
					case 9  : entryContent = LABELS.CONTRIBUTE_BRANCHING; break;
					case 11 : entryContent = LABELS.CONTRIBUTE_CONTENT_EDITED; break; 
					case 12 : entryContent = LABELS.CONTRIBUTE_GATE_PASSWORD; break; 
				}
				switch (this.contributionType) {
					case 3  : 
					case 12 : if (this.isComplete) {
						 		entryContent += '<div class="pull-right"><button class="btn btn-xs btn-success" '
						 					 + 'onClick="javascript:openPopUp(\'' + this.url 
						 					 + '\',\'ContributeActivity\', 800, 1280, true)" '
						 					 + 'title="' + LABELS.CONTRIBUTE_OPENED_GATE_TOOLTIP + '">'
						 					 + LABELS.CONTRIBUTE_OPENED_GATE 
						 					 + '</button></div>';
							} else {
								entryContent += '<div class="pull-right btn-group btn-group-xs"><button onClick="javascript:openGateNow('
                                    + contributeActivity.activityID + ')" type="button" class="btn btn-xs btn-primary" title="' 
									+ LABELS.CONTRIBUTE_OPEN_GATE_NOW_TOOLTIP + '">' 
									+ LABELS.CONTRIBUTE_OPEN_GATE_NOW_BUTTON + '</button>'
									+ '<button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" '
									+ 		   'aria-expanded="false"><span class="caret"></span><span class="sr-only"'
									+ '>Toggle Dropdown</span></button><ul class="dropdown-menu"><li>'
									+ '<a href="#" onClick="javascript:openPopUp(\''
                                    + this.url + '\',\'ContributeActivity\', 800, 1280, true)" title="' 
									+ LABELS.CONTRIBUTE_OPEN_GATE_TOOLTIP + '">' 
									+ LABELS.CONTRIBUTE_OPEN_GATE_BUTTON + '</a></li></ul></div>';
							}
							break;
					default : entryContent += '<span id="' + contributeId + 'Btn" class="btn btn-xs btn-primary pull-right" '
						 + 'onClick="javascript:openPopUp(\''
						 + this.url + '\',\'ContributeActivity\', 800, 1280, true)" title="' + LABELS.CONTRIBUTE_TOOLTIP
						 + '">' + LABELS.CONTRIBUTE_BUTTON + '</span>';
				}

				cell = $('<div />').addClass('contributeEntryCell').html(entryContent);
				row = row.append(cell);
			});
		});
	}
	
	if ($('.contributeRow').length == 0) {
		$('#requiredTasks').hide();
	} else {
		$('#requiredTasks').show();
	}
}

/**
 * Set up when the progress emails should be sent to monitors
 */
function configureProgressEmail(){
	fillEmailProgress();
	$('#emailProgressDialog').modal('show');
}

/**
 * Adds/removes date to the set of progress report emailing dates
 */
function editEmailProgressDate(dateCheckbox){
	var dateid = dateCheckbox.parent().attr('dateid'),
		add = dateCheckbox.is(':checked');
	var data = {
		'lessonID' : lessonId,
		'id' : dateid,
		'add' : add
	};	
	data[csrfTokenName] = csrfTokenValue;
	$.ajax({
		url : LAMS_URL + 'monitoring/emailProgress/updateEmailProgressDate.do',
		type : 'POST',
		cache : false,
		data : data,
		success : function( dateObj ) {
			dateCheckbox.parent().attr('dateid', dateObj.id);
			dateCheckbox.parent().attr('datems', dateObj.ms);
			dateCheckbox.parent().children().last().html(dateObj.date); 
		}
	});
}

/**
 * Fills the dates from the server for the email progress
 */
function fillEmailProgress() {
	var dialog = $('#emailProgressDialog'),
		table = $('#emailProgressDialogTable', dialog),
		list = $('.dialogList', table).empty(),
		dates = null;
		ajaxProperties = dialog.data('ajaxProperties'),
		dates = null;
	
	if (!ajaxProperties) {
		// initialise ajax config
		ajaxProperties = {
		dataType : 'json',
		url : LAMS_URL + 'monitoring/emailProgress/getEmailProgressDates.do',
		cache : false,
		async : false,
		data : {
			'lessonID'  : lessonId
		}};
		dialog.data('ajaxProperties', ajaxProperties);
	}

	ajaxProperties.success = function(response) {
		dates = response.dates;
	}

	$.ajax(ajaxProperties);

	$.each(dates, function(dateIndex, date) {
		addCheckbox(date, list, true);
	});	

	colorDialogList(table);
}

function addCheckbox(dateObj, list, checked) {
	// check for an existing matching date
	var alreadyExists = false;
	var existingDivs = $("div", list);
	$.each(existingDivs, function(divIndex, div) {
		if ( div.getAttribute('dateid') == dateObj.id ) {
			alreadyExists = true;
			return false;
		}
	});	
	if ( alreadyExists )
		return;

	// does not exist so add to list
	var checkbox = $('<input />').attr({
   	 	'type' : 'checkbox'
     }).change(function(){
    	 editEmailProgressDate($(this));
     }),

     dateString = $('<span/>').html(dateObj.date),
   	
     dateDiv = $('<div />').attr({
			'dateid'  : dateObj.id,
			'datems'  : dateObj.ms
			})
         .addClass('dialogListItem')
          .append(dateString)
	      .prepend(checkbox)
	      .appendTo(list);
   	
	checkbox.prop('checked', checked);
	return checkbox;
}

function sendProgressEmail() {
	if ( confirm(LABELS.PROGRESS_EMAIL_SEND_NOW_QUESTION) ) {
		$.ajax({
			dataType : 'json',
			url : LAMS_URL + 'monitoring/emailProgress/sendLessonProgressEmail.do',
			cache : false,
			data : {
				'lessonID'  : lessonId
				},		
			success : function(response) {
				if ( response.error || ! response.sent > 0 )
					alert(LABELS.PROGRESS_EMAIL_SEND_FAILED+"\n"+(response.error ? response.error : ""));
				else 
					alert(LABELS.PROGRESS_EMAIL_SUCCESS.replace('[0]',response.sent));
			}
		});	
	}
}

function addEmailProgressDate() {
	var table = $('#emailProgressDialogTable', '#emailProgressDialog'),
		list = $('.dialogList', table),
		newDateMS = $('#emaildatePicker').datetimepicker('getDate');

	if ( newDateMS != null ) {
		if ( newDateMS.getTime() < Date.now()  ) {
			alert(LABELS.ERROR_DATE_IN_PAST);
		} else {
			var dateObj = { id: newDateMS.getTime(), date: getEmailDateString(newDateMS)},
				checkbox = addCheckbox(dateObj, list, true);
			editEmailProgressDate(checkbox); // update back end
			addEmailProgressSeries(false, table);
		}
	} else {
		alert(LABELS.PROGRESS_SELECT_DATE_FIRST);
	}
}



function getEmailDateString(date) {
	return date.toLocaleDateString('en', {year: 'numeric', month: 'long', day: 'numeric', hour: 'numeric', minute: 'numeric', hour12: false });
}

function addEmailProgressSeries(forceQuestion, table) {
	if ( ! table ) {
		table = $('#emailProgressDialogTable', '#emailProgressDialog');
	} 
	var list = $('.dialogList', table),
		items = $('.dialogListItem', list);
	
	if ( forceQuestion && items.length < 2 ) {
		alert(LABELS.PROGRESS_ENTER_TWO_DATES_FIRST);
	} else if ( items.length == 2 || forceQuestion ) {
    	var numDates = prompt(LABELS.PROGRESS_EMAIL_GENERATE_ONE+"\n\n"+LABELS.PROGRESS_EMAIL_GENERATE_TWO);
    	if ( numDates > 0 ) {
    		var dates=[];
    		var maxDate = 0;
    		items.each( function() {
    			var nextDate = $(this).attr('dateid');
    			dates.push($(this).attr('dateid'));
    			if ( maxDate < nextDate ) 
    				maxDate = nextDate;
    		});
    		if ( dates[1] < dates[0] ) {
    			var swap = dates[1];
    			dates[1] = dates[0];
    			dates[0] = swap;
    		}
    		var diff = dates[1] - dates[0];
    		if ( diff > 0 ) {
				var genDateMS = maxDate;
    			for (var i = 0; i < numDates; i++) {
    				genDateMS = +genDateMS + +diff;
    				var genDateObj = { id: genDateMS, date: getEmailDateString(new Date(genDateMS))};
    			    var checkbox = addCheckbox(genDateObj, list, false);
    			}
    		}
    	}
    }
	colorDialogList(table);
} 

function openGateNow(activityId) {
	var data = {
		'activityId' : activityId
	};
	data[csrfTokenName] = csrfTokenValue;
	$.ajax({
		'type' : 'post',
		'url'  : LAMS_URL + 'monitoring/gate/openGate.do',
		'data'  : data,
		'success' : function(){
			updateLessonTab();
		}
	});
}

function closeGate(activityId) {
	var data = {
		'activityId' : activityId
	};
	data[csrfTokenName] = csrfTokenValue;
	$.ajax({
		'type' : 'post',
		'url'  : LAMS_URL + 'monitoring/gate/closeGate.do',
		'data'  : data,
		'success' : function(){
			updateLessonTab();
		}
	});
}
//********** SEQUENCE TAB FUNCTIONS **********

/**
 * Sets up the sequence tab.
 */
function initSequenceTab(){
	var learnerGroupDialogContents = $('#learnerGroupDialogContents');
	$('#learnerGroupDialogForceCompleteButton, #learnerGroupDialogForceCompleteAllButton', learnerGroupDialogContents).click(function() {
		var dialog = $('#learnerGroupDialog'),
			// are we moving selected learners or all of learners who are currently in the activity
			moveAll = $(this).attr('id') == 'learnerGroupDialogForceCompleteAllButton',
			selectedLearners = moveAll ? null : $('.dialogList div.dialogListItemSelected', dialog),
			// go to "force complete" mode, similar to dragging user to an activity
			activityId = dialog.data('ajaxProperties').data.activityID,
			dropArea = sequenceCanvas.add('#completedLearnersContainer');
		dropArea.css('cursor', 'pointer')
				.one('click', function(event) {
					dropArea.off('click').css('cursor', 'default');
					if (moveAll) {
						// setting learners as 'true' is a special switch meaning "move all"
						forceComplete(activityId, true, event.pageX, event.pageY);
					} else {
						var learners = [];
						selectedLearners.each(function(){
							var learner = $(this);
							learners.push({
								'id'     : learner.attr('userId'),
								'name'   : learner.text()
							});
						});
						forceComplete(activityId, learners, event.pageX, event.pageY);
					}
				});
		dialog.modal('hide');
		
		if (moveAll) {
			alert(LABELS.FORCE_COMPLETE_CLICK.replace('[0]', ''));
		} else {
			var learnerNames = '';
			selectedLearners.each(function(){
				learnerNames += $(this).text() + ', ';
			});
			learnerNames = learnerNames.slice(0, -2);
			alert(LABELS.FORCE_COMPLETE_CLICK.replace('[0]', '"' + learnerNames + '"'));
		}
	});
	
	$('#learnerGroupDialogViewButton', learnerGroupDialogContents).click(function() {
		var dialog = $('#learnerGroupDialog'),
			selectedLearner = $('.dialogList div.dialogListItemSelected', dialog);
		if (selectedLearner.length == 1) {
			// open pop up with user progress in the given activity
			openPopUp(selectedLearner.attr('viewUrl'), "LearnActivity", popupHeight, popupWidth, true);
		}
	});
	
	$('#learnerGroupDialogEmailButton', learnerGroupDialogContents).click(function() {
		var dialog = $('#learnerGroupDialog'),
			selectedLearner = $('.dialogList div.dialogListItemSelected', dialog);
		if (selectedLearner.length == 1) {
			showEmailDialog(selectedLearner.attr('userId'));
		}
	});
	
	$('#learnerGroupDialogCloseButton', learnerGroupDialogContents).click(function(){
		$('#learnerGroupDialog').modal('hide');
	});
	
    // initialise lesson dialog
	var learnerGroupDialog = showDialog('learnerGroupDialog',{
			'autoOpen'  : false,
			'width'     : 450,
			'height'	: 450,
			'resizable' : true,
			'open'      : function(){
				autoRefreshBlocked = true;
				// until operator selects an user, buttons remain disabled
				$('button.learnerGroupDialogSelectableButton').blur().prop('disabled', true);
			},
			'close' 	: function(){
				autoRefreshBlocked = false;
			}
		}, false);
	
	$('.modal-body', learnerGroupDialog).empty().append(learnerGroupDialogContents.show());
	
	// search for users with the term the Monitor entered
	$('.dialogSearchPhrase', learnerGroupDialog).autocomplete({
			'source' : LAMS_URL + "monitoring/monitoring/autocomplete.do?scope=lesson&lessonID=" + lessonId,
			'delay'  : 700,
			'select' : function(event, ui){
				var phraseField = $(this),
					dialog = $('#learnerGroupDialog');
			    // learner's ID in ui.item.value is not used here
				phraseField.val(ui.item.label);
				$('.dialogSearchPhraseClear', dialog).css('visibility', 'visible');
				// reset to page 1
				dialog.data('ajaxProperties').data.pageNumber = 1;
				showLearnerGroupDialog();
				return false;
			}
		})
		// run the real search when the Monitor presses Enter
		.keypress(function(e){
			if (e.which == 13) {
				var phraseField = $(this),
					dialog = $('#learnerGroupDialog');
				
				phraseField.autocomplete("close");
				if (phraseField.val()) {
					$('.dialogSearchPhraseClear', dialog).css('visibility', 'visible');
				}
				// reset to page 1
				dialog.data('ajaxProperties').data.pageNumber = 1;
				showLearnerGroupDialog();
			}
		});
	
	// search for users with the term the Monitor entered
	$("#sequenceSearchPhrase").autocomplete( {
		'source' : LAMS_URL + "monitoring/monitoring/autocomplete.do?scope=lesson&lessonID=" + lessonId,
		'delay'  : 700,
		'response' : function(event, ui) {
			$.each(ui.content, function(){
				// only add portrait if user has got one
				let valueParts = this.value.split('_');
				if (valueParts.length > 1) {
					this.value = valueParts[0];
					// portrait div will be added as text, then in open() function below we fix it
					this.label += definePortrait(valueParts[1], this.value, STYLE_SMALL, true, LAMS_URL);
				}
			});
		},
		'open'   : function(event, ui) {
			$('.ui-menu-item-wrapper', $(this).autocomplete( "widget" )).each(function(){
				let menuItem = $(this);
				// portrait, if exists, was added as text; now we make it proper html
				menuItem.html(menuItem.text());
				let portrait = menuItem.children('div');
				if (portrait.length == 0) {
					// no portrait, nothing to do
					return;
				}
				// rearrange item contents
				portrait.detach();
				let label = $('<p />').text(menuItem.text());
				// this extra class makes it a flex box
				menuItem.empty().addClass('autocomplete-menu-item-with-portrait');
				menuItem.append(label).append(portrait);
			});
		},
		'select' : function(event, ui){
			// put the learner first name, last name and login into the box
			$(this).val(ui.item.label);
			// mark the learner's ID and make him highlighted after the refresh
			sequenceSearchedLearner = ui.item.value;
			updateSequenceTab();
			return false;
		}
	});
	
	var forceBackwardsDialogContents = $('#forceBackwardsDialogContents');
	showDialog('forceBackwardsDialog', {
		'autoOpen'	: false,
		'modal'     : true,
		'resizable' : true,
		'height'	: 300,
		'width'  	: 400,
		'title'		: LABELS.FORCE_COMPLETE_BUTTON,
		'open'      : function(){
			autoRefreshBlocked = true;
		},
		'close' 	: function(){
			autoRefreshBlocked = false;
		}
	}, false);
	// only need to do this once as then it updates the msg field directly.
	$('.modal-body', '#forceBackwardsDialog').empty().append($('#forceBackwardsDialogContents').show());
	
	$('#forceBackwardsRemoveContentNoButton', forceBackwardsDialogContents).click(function(){
		var forceBackwardsDialog = $('#forceBackwardsDialog');
		forceCompleteExecute(forceBackwardsDialog.data('learners'), null,
			 forceBackwardsDialog.data('activityId'),
			 false);
		forceBackwardsDialog.modal('hide');
	});

	$('#forceBackwardsRemoveContentYesButton', forceBackwardsDialogContents).click(function(){
		var forceBackwardsDialog = $('#forceBackwardsDialog');
		forceCompleteExecute(forceBackwardsDialog.data('learners'), null,
			 forceBackwardsDialog.data('activityId'),
			 true);
		forceBackwardsDialog.modal('hide');
	});

	$('#forceBackwardsCloseButton', forceBackwardsDialogContents).click(function(){
		$('#forceBackwardsDialog').modal('hide');
	});

	// small info box on Sequence tab, activated when the tab is showed
	var sequenceInfoDialog = showDialog('sequenceInfoDialog', {
		'autoOpen'   : false,
		'width'      : 300,
		'modal'      : false, 
		'resizable'  : false,
		'draggable'  : false,
		'title'		 : LABELS.HELP,
		'open'      : function(){
			// close after given time
			setTimeout(function(){
				$('#sequenceInfoDialog').modal('hide')
			}, sequenceInfoTimeout);
		},
		'close' 	: null,
		'data'		: {
			'position' : {
				'my' : 'left top',
				'at' : 'left top',
				'of' : '#sequenceCanvas'
			}
		}
	}, false);
	$(sequenceInfoDialog).click(function(){
		$('#sequenceInfoDialog').modal('hide');
	}).find('.modal-header').remove();
	
	$('#sequenceInfoDialog .modal-body').empty().append($('#sequenceInfoDialogContents').show());
	
	canvasFitScreen(learningDesignSvgFitScreen, true);
}

function showIntroductionDialog(lessonId) {
	
	showDialog('introductionDialog', {
		'height'	: 450,
		'width'  	: Math.max(380, Math.min(800, $(window).width() - 60)),
		'resizable' : false,
		'title'		: LABELS.LESSON_INTRODUCTION,
		'open'      : function(){
			$('iframe', this).attr('src', LAMS_URL + 'editLessonIntro/edit.do?lessonID='+lessonId);
			$('iframe', this).css('height', '360px'); 
			autoRefreshBlocked = true;
		},
		'close' 	: function(){
			closeIntroductionDialog()
		}
	}, false);
}	
	
function closeIntroductionDialog() {
	autoRefreshBlocked = false;
	$('#introductionDialog').remove();
}
/**
 * Updates learner progress in sequence tab according to respose sent to refreshMonitor()
 */
function updateSequenceTab() {
	if (sequenceRefreshInProgress) {
		return;
	}
	sequenceRefreshInProgress = true;
	
	sequenceCanvas.css('visibility', 'hidden');

	if (originalSequenceCanvas) {
		// put bottom layer, LD SVG
		sequenceCanvas.html(originalSequenceCanvas);
	} else {
		var exit = loadLearningDesignSVG();
		if (exit) {
			// when SVG gets re-created, this update method will be run again
			sequenceRefreshInProgress = false;
			return;
		}
	}
	
	// clear all completed learner icons except the door
	$('#completedLearnersContainer :not(img#completedLessonLearnersIcon)').remove();
	
	var sequenceTopButtonsContainer = $('#sequenceTopButtonsContainer');
	if ($('img#sequenceCanvasLoading', sequenceTopButtonsContainer).length == 0){
		$('#sequenceCanvasLoading')
				.clone().appendTo(sequenceTopButtonsContainer)
				.css('display', 'block');
	}
	
	$.ajax({
		dataType : 'json',
		url : LAMS_URL + 'monitoring/monitoring/getLessonProgress.do',
		cache : false,
		data : {
			'lessonID'  : lessonId,
			'searchedLearnerId' : sequenceSearchedLearner
		},		
		success : function(response) {
			// activities have uiids but no ids, set it here
			$.each(response.activities, function(){
				$('g[uiid="' + this.uiid + '"]', sequenceCanvas).attr('id', this.id);
			});

			// remove the loading animation
			$('img#sequenceCanvasLoading', sequenceTopButtonsContainer).remove();

			var learnerCount = 0;
			$.each(response.activities, function(index, activity){
				var activityGroup = $('g[id="' + activity.id + '"]', sequenceCanvas),
					isGate = [3,4,5,14].indexOf(activity.type) > -1;
					
				learnerCount += activity.learnerCount;
				
				if (isGate) {
					var gateClosedIcon = activityGroup.find('.gateClosed');
					
					if (activity.gateOpen) {
						if (!gateOpenIconData) {
							// if SVG is not cached, get it synchronously
							$.ajax({
								url : LAMS_URL + gateOpenIconPath,
								async : false,
								dataType : 'text',
								success : function(response) {
									gateOpenIconData = response;
								}
							});		
						}
						
						 $(gateOpenIconData).attr({
								x : gateClosedIcon.attr('x'),
								y : gateClosedIcon.attr('y'),
								width : gateClosedIcon.attr('width'),
								height : gateClosedIcon.attr('height'),
							}).appendTo(activityGroup)
							  .show();
							
						gateClosedIcon.remove();
					} else {
						gateClosedIcon.show();
					}
				}
				
				if (response.contributeActivities) {
					$.each(response.contributeActivities, function(){
						if (activity.id == this.activityID) {
							 activity.requiresAttention = true;
							 return false;
						}
					});
				}
				
				// put learner and attention icons on each activity shape
				addActivityIcons(activity);
			});
			
			// modyfing SVG in DOM does not render changes, so we need to reload it
			sequenceCanvas.html(sequenceCanvas.html());
			// SVG needs resizing after reload
			window.parent.resizeSequenceCanvas();
			
			// only now show SVG so there is no "jump" when resizing
			sequenceCanvas.css('visibility', 'visible');
			
			if (sequenceSearchedLearner != null && !response.searchedLearnerFound) {
				// the learner has not started the lesson yet, display an info box
				sequenceClearSearchPhrase();
				$('#sequenceInfoDialogContents').html(LABELS.PROGRESS_NOT_STARTED);
				$('#sequenceInfoDialog').modal('show');
			}
			
			var learnerTotalCount = learnerCount + response.completedLearnerCount;
			$('#learnersStartedPossibleCell').html('<span id="tour-learner-count">'+learnerTotalCount + ' / ' + response.numberPossibleLearners+'</span>');
			addCompletedLearnerIcons(response.completedLearners, response.completedLearnerCount, learnerTotalCount);
			
			$.each(response.activities, function(activityIndex, activity){
				addActivityIconsHandlers(activity);
				
				if (activity.url) {
					var activityGroup = $('g[id="' + activity.id + '"]');
					activityGroup.css('cursor', 'pointer');
					dblTap(activityGroup, function(){  
						// double click on activity shape to open Monitoring for this activity
						openPopUp(LAMS_URL + activity.url, "MonitorActivity", popupHeight, popupWidth, true, true);
					});
				}
			});
			
			// remove any existing popovers
			$('.popover[role="tooltip"]').remove();
			
			initializePortraitPopover(LAMS_URL, 'large', 'right');

			// update the cache global values so that the contributions & the Live Edit buttons will update
			lockedForEdit = response.lockedForEdit;
			lockedForEditUserId = response.lockedForEditUserId;
			lockedForEditUsername = response.lockedForEditUsername; 
			updateLiveEdit();
			
			sequenceRefreshInProgress = false;
		}
	});
}

function updateLiveEdit() {
	if ( liveEditEnabled ) {
		if ( lockedForEdit ) {
			if ( userId == lockedForEditUserId ) {
				$("#liveEditButton").removeClass('btn-default');
				$("#liveEditButton").addClass('btn-primary');
				$("#liveEditButton").show();
				$("#liveEditWarning").hide();
				$("#liveEditWarning").text("");
			} else {
				$("#liveEditButton").removeClass('btn-primary');
				$("#liveEditButton").addClass('btn-default');
				$("#liveEditButton").hide();
				$("#liveEditWarning").text(LABELS.LIVE_EDIT_WARNING.replace("%0",lockedForEditUsername));
				$("#liveEditWarning").show();
			}
		} else {
			$("#liveEditButton").removeClass('btn-primary');
			$("#liveEditButton").addClass('btn-default');
			$("#liveEditButton").show();
			$("#liveEditWarning").hide();
			$("#liveEditWarning").text("");
		}
	} else {
		$("#liveEditButton").hide();
		$("#liveEditWarning").hide();
	}
}
	
function loadLearningDesignSVG() {
	var exit = false;
	// fetch SVG just once, since it is immutable
	$.ajax({
		dataType : 'text',
		url : LAMS_URL + 'home/getLearningDesignThumbnail.do?',
		async : false,
		cache : false,
		data : {
			'ldId'      : ldId
		},
		success : function(response) {
			originalSequenceCanvas = response;
			sequenceCanvas = $('#sequenceCanvas').html(originalSequenceCanvas);
		},
		error : function(error) {
			exit = true;
			// the LD SVG is missing, try to re-generate it; if it is an another error, fail
			if (error.status != 404) {
				return;
			}

			// iframe just to load Authoring for a single purpose, generate the SVG
			var frame = $('<iframe />').appendTo('body').css('visibility', 'hidden');
			frame.on('load', function(){
				// disable current onload handler as closing the dialog reloads the iframe
				frame.off('load');
				
				// call svgGenerator.jsp code to store LD SVG on the server
				var win = frame[0].contentWindow || frame[0].contentDocument;
				$(win.document).ready(function(){
				    // when LD opens, make a callback which save the thumbnail and displays it in current window
					win.GeneralLib.openLearningDesign(ldId, function(){
						result = win.GeneralLib.saveLearningDesignImage();
						frame.remove();
						if (result) {
							// load the image again
							updateSequenceTab();
						}
					});
				});
			});
			// load svgGenerator.jsp to render LD SVG
			frame.attr('src', LAMS_URL + 'authoring/generateSVG.do?selectable=false');
		}
	});
	
	return exit;
}


/**
 * Forces given learners to move to activity indicated on SVG by coordinated (drag-drop)
 */
function forceComplete(currentActivityId, learners, x, y) {
	autoRefreshBlocked = true;
	
	var foundActivities = [],
		targetActivity = null,
		// if "true", then we are moving all learners from the given activity
		// otherwise it is a list of selected learners IDs
		moveAll = learners === true;
	// check all activities and "users who finished lesson" bar
	$('g.svg-activity', sequenceCanvas).add('#completedLearnersContainer').each(function(){
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
		if (this.hasClass('svg-activity-floating')) {
			// no force complete to support activities
			targetActivity = null;
			return false;
		}
		// the enveloping OptionalActivity has priority
		if (targetActivity == null || this.hasClass('svg-activity-optional')) {
			targetActivity = this;
		}
	});
	
	if (!targetActivity) {
		return;
	}
	
	var targetActivityId = null,
		executeForceComplete = false,
		isEndLesson = !targetActivity.is('g'),
		learnerNames = '';
	
	if (!moveAll) {
		$.each(learners, function(){
			learnerNames += this.name + ', ';
		});
		learnerNames = '"' + learnerNames.slice(0, -2) + '"';
	}

	
	if (isEndLesson) {
		executeForceComplete =  currentActivityId && confirm(LABELS.FORCE_COMPLETE_END_LESSON_CONFIRM
				.replace('[0]', learnerNames));
	} else {
		var targetActivityId = +targetActivity.attr('id');
		if (currentActivityId != targetActivityId) {
			var targetActivityName = targetActivity.hasClass('svg-activity-gate') ? "Gate" : targetActivity.find('.svg-activity-title-label').text(),
				moveBackwards = currentActivityId == null;
			
			// check if target activity is before current activity
			if (currentActivityId) {
				$.ajax({
					dataType : 'text',
					url : LAMS_URL + 'monitoring/monitoring/isActivityPreceding.do',
					async : false,
					cache : false,
					data : {
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
				var msgString = LABELS.FORCE_COMPLETE_REMOVE_CONTENT
						.replace('[0]', learnerNames).replace('[1]', targetActivityName);
				$('#forceBackwardsMsg', '#forceBackwardsDialogContents').html(msgString);				
				$('#forceBackwardsDialog').data({
					'learners' : learners,
					'activityId': targetActivityId});
				$('#forceBackwardsDialog').modal('show');
				// so autoRefreshBlocked = false is not set
				return;
			} else {
				// move the learner forward
				executeForceComplete = confirm(LABELS.FORCE_COMPLETE_ACTIVITY_CONFIRM
							.replace('[0]', learnerNames).replace('[1]', targetActivityName));
			}
		}
	}
	
	if (executeForceComplete) {
		forceCompleteExecute(moveAll ? null : learners, moveAll ? currentActivityId : null, targetActivityId, false);
	}

	autoRefreshBlocked = false;
}


/**
 * Tell server to force complete the learner.
 */
function forceCompleteExecute(learners, moveAllFromActivityId, activityId, removeContent) {
	var learnerIds = '';
	if (learners) {
		$.each(learners, function() {
			learnerIds += this.id + ',';
		});
		learnerIds = learnerIds.slice(0, -1);
	}
	
	var data={
		'lessonID'   		    : lessonId,
		// either we list selected learners to move
		// or we move all learners from the given activity
		'learnerID'  		    : learnerIds,
		'moveAllFromActivityID' : moveAllFromActivityId,
		'activityID' 		    : activityId,
		'removeContent'		    : removeContent
	};
	data[csrfTokenName] = csrfTokenValue;
	
	$.ajax({
		url : LAMS_URL + 'monitoring/monitoring/forceComplete.do',
		type : 'POST',
		dataType : 'text',
		cache : false,
		data : data,
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
	var learningDesignSvg = $('svg.svg-learning-design', sequenceCanvas),
		isTool = activity.type == 1,
		isGrouping = activity.type == 2,
		// branching and gates require extra adjustments
		isBranching =  [10,11,12,13].indexOf(activity.type) > -1,
		isGate = [3,4,5,14].indexOf(activity.type) > -1,
		isContainer = [6,7].indexOf(activity.type) > -1,
		activityGroup = $('g[id="' + activity.id + '"]', learningDesignSvg),
		requiresAttentionIcon = activity.requiresAttention ? 
				$('<img />')
					.attr({
						'id'    : 'act' + activity.id + 'attention',
						'src'   : LAMS_URL + 'images/exclamation.svg',
						'title' : LABELS.CONTRIBUTE_ATTENTION
					})
					.addClass('activity-requires-attention')
				: null,
		allLearnersIcon = activity.learnerCount > 0 ?
				$('<div />')
					.attr('id', 'act' + activity.id + 'learnerGroup')
					.addClass('more-learner-icon')
				: null;
		

	if (isTool || isGrouping) {
		if (activity.learnerCount > 0) {
			// if learners reached the activity, make room for their icons: make activity icon and label smaller and move to top
			$('svg', activityGroup).attr({
				'x'     : coord.x + 20,
				'y'     : coord.y + 3,
				'width' : '30px',
				'height': '30px'
			});
			
			// switch from wide banner to narrow one
			$('.svg-tool-banner-narrow', activityGroup).show();
			$('.svg-tool-banner-wide', activityGroup).hide();
			
			$('.svg-activity-title-label', activityGroup).parent('foreignObject').remove();
			$('<text>').text(activity.title.length < 20 ? activity.title : activity.title.substring(0, 20) + '...')
					   .attr({
							'x' : coord.x + 55,
							'y' : coord.y + 20
						})
						.addClass('svg-activity-title-label svg-activity-title-label-small')
						.appendTo(activityGroup);
						
			var learnersContainer = $('<div />').addClass('learner-icon-container');
			$('<foreignObject />').append(learnersContainer).appendTo(activityGroup).attr({
				'x' : coord.x + 20,
				'y' : coord.y + 40,
				'width'  : 184,
				'height' : 40
			});
			
			$.each(activity.learners, function(learnerIndex, learner){
				if (learnerIndex >= 5 && activity.learnerCount > 6) {
					return false;					
				}
				$(definePortrait(learner.portraitId, learner.id, STYLE_SMALL, true, LAMS_URL))
					  .css({
						'left'        : learnerIndex * (activity.learnerCount < 5 ? 46 : 28)  + 'px',
						'z-index'     : 100 + learnerIndex,
						'padding-top' : '2px'
					  })
					  .addClass('new-popover learner-icon')
					  .attr({
						'id'            : 'act' + activity.id + 'learner' + learner.id,
						'data-id'       : 'popover-' + learner.id,
						'data-toggle'   : 'popover',
						'data-portrait' : learner.portraitId,
						'data-fullname' : getLearnerDisplayName(learner)
					  })
					  .appendTo(learnersContainer);
			});
			
			if (activity.learnerCount > 6) {
				allLearnersIcon
					  .css({
						'left'     : '140px',
						'z-index'  : 108,
						'margin-top' : '1px'
					  })
					  .text('+' + (activity.learnerCount - 5))
					  .appendTo(learnersContainer);
			}
		}
		
		if (requiresAttentionIcon) {
			$('<foreignObject />').append(requiresAttentionIcon).appendTo(activityGroup).attr({
				'x' : coord.x + 180,
				'y' : coord.y - 1,
				'width'  : 20,
				'height' : 20
			});
		}
	} else if (isGate) {
		if (activity.learnerCount > 0) {
			$('<foreignObject />').append(allLearnersIcon).appendTo(activityGroup).attr({
				'x' : coord.x + 20,
				'y' : coord.y + 20,
				'width' : 40,
				'height' : 40
			});
			allLearnersIcon.text(activity.learnerCount);
		}
		
		if (requiresAttentionIcon) {
			$('<foreignObject />').append(requiresAttentionIcon).appendTo(activityGroup).attr({
				'x' : coord.x + 25,
				'y' : coord.y -  5,
				'width'  : 20,
				'height' : 20
			});
		}
	} else if (isBranching) {
		if (activity.learnerCount > 0) {
			$('<foreignObject />').append(allLearnersIcon).appendTo(activityGroup).attr({
				'x' : coord.x,
				'y' : coord.y,
				'width' : 40,
				'height' : 40
			});
			allLearnersIcon.text(activity.learnerCount);
		}		
		
		if (requiresAttentionIcon) {
			$('<foreignObject />').append(requiresAttentionIcon).appendTo(activityGroup).attr({
				'x' : coord.x + 8,
				'y' : coord.y - 28,
				'width'  : 20,
				'height' : 20
			});
		}
	} else if (isContainer) {
		if (activity.learnerCount > 0) {
			$('<foreignObject />').append(allLearnersIcon).appendTo(activityGroup).attr({
				'x' : coord.x + coord.width - 20,
				'y' : coord.y - 17,
				'width' : 40,
				'height' : 40
			});
			allLearnersIcon.text(activity.learnerCount);
		}
		
		if (requiresAttentionIcon) {
			$('<foreignObject />').append(requiresAttentionIcon).appendTo(activityGroup).attr({
				'x' : coord.x,
				'y' : coord.y + 15,
				'width'  : 20,
				'height' : 20
			});
		}
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
			let learnerIcon = $('div#act' + activity.id + 'learner' + learner.id, sequenceCanvas)
							  .css('cursor', 'pointer')
							  // drag learners to force complete activities
							  .draggable({
								'appendTo'    : '.svg-learner-draggable-area',
								'containment' : '.svg-learner-draggable-area',
							    'distance'    : 20,
							    'scroll'      : false,
							    'cursorAt'	  : {'left' : 10, 'top' : 15},
							    'helper' : function(){
							    	return learnerIcon.clone();
							    },
								'start' : function(){
									 autoRefreshBlocked = true;
								},
								'stop' : function(event, ui) {
									var learners = [{
										'id'   : learner.id,
										'name' : getLearnerDisplayName(learner, true)
									}];
									// jQuery droppable does not work for SVG, so this is a workaround
									forceComplete(activity.id, learners, ui.offset.left, ui.offset.top);
								}
							});
			
			if (usersViewable) {
				dblTap(learnerIcon, function(event){
					 // double click on learner icon to see activity from his perspective
					var url = LAMS_URL + 'monitoring/monitoring/getLearnerActivityURL.do?userID=' 
						               + learner.id + '&activityID=' + activity.id + '&lessonID=' + lessonId;
					openPopUp(url, "LearnActivity", popupHeight, popupWidth, true);
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
		$('#act' + activity.id + 'learnerGroup', sequenceCanvas)
		   .click(function(event){
				 // double click on learner group icon to see list of learners
				var ajaxProperties = {
						url : LAMS_URL + 'monitoring/monitoring/getCurrentLearners.do',
						data : {
							'activityID' : activity.id
						}
					};
				showLearnerGroupDialog(ajaxProperties, activity.title, false, true, usersViewable, false);
			});
	}
	
	if (activity.requiresAttention){
		$('#act' + activity.id + 'attention', sequenceCanvas).click(function(event){
			event.stopPropagation();
			// switch to first tab where attention prompts are listed
			if ($('#tblmonitor-tab-content').length == 0) {
				// wer are in regular monitor, so switch to first tab to perform tasks
				doSelectTab(1);
			} else {
				// we are in TBL mode, so switch back to regular monitor to perform tasks
				switchToRegularMonitor(true);
			}
			 
		});
	}
}


/**
 * Add learner icons in "finished lesson" bar.
 */
function addCompletedLearnerIcons(learners, learnerCount, learnerTotalCount) {
	var iconsContainer = $('#completedLearnersContainer');
	
	if (learners) {
		// create learner icons, along with handlers
		$.each(learners, function(learnerIndex, learner){
			let icon = $(definePortrait(learner.portraitId, learner.id, STYLE_SMALL, true, LAMS_URL))
				  .addClass('new-popover learner-icon')
				  .attr({
					'id'            : 'learner-complete-' + learner.id,
					'data-id'       : 'popover-' + learner.id,
					'data-toggle'   : 'popover',
					'data-portrait' : learner.portraitId,
					'data-fullname' : getLearnerDisplayName(learner)
				  })
				// drag learners to force complete activities
				  .draggable({
					'appendTo'    : '.svg-learner-draggable-area',
					'containment' : '.svg-learner-draggable-area',
				    'distance'    : 20,
				    'scroll'      : false,
				    'cursorAt'	  : {'left' : 10, 'top' : 15},
					'helper'      : function(){
						// copy of the icon for dragging
						return icon.clone();
					},
					'start' : function(){
						autoRefreshBlocked = true;
					},
					'stop' : function(event, ui) {
						var learners = [{
							'id'   : learner.id,
							'name' : getLearnerDisplayName(learner, true)
						}];
						// jQuery droppable does not work for SVG, so this is a workaround
						forceComplete(null, learners, ui.offset.left, ui.offset.top);
					}
				})
				.appendTo(iconsContainer);
			
			if (learner.id == sequenceSearchedLearner){
				highlightSearchedLearner(icon);
			}
		});
		
		
	$('<div />')
	  .addClass('more-learner-icon')
	  .text(learnerCount)
	  .appendTo(iconsContainer)
      .click(function(){
			var ajaxProperties = {
					url : LAMS_URL + 'monitoring/monitoring/getCurrentLearners.do',
					data : {
						'lessonID'   : lessonId
					}
				};
			showLearnerGroupDialog(ajaxProperties, LABELS.LEARNER_FINISHED_DIALOG_TITLE, false, true, false, false);
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
	
	var group = $('g[id="' + activity.id + '"]', sequenceCanvas);
	if (group.length == 0) {
		return;
	}
	
	return {
		'x'     : +group.data('x'),
		'y'     : +group.data('y'),
		'x2'    : +group.data('x') + +group.data('width'),
		'y2'    : +group.data('y') + +group.data('height'),
		'width' : +group.data('width'),
		'height': +group.data('height'),
	}
	
}


/**
 * Shows where the searched learner is.
 */
function highlightSearchedLearner(icon) {
	// show the "clear" button
	$('#sequenceSearchPhraseClear').css('visibility', 'visible');
	
	// border and z-index are manipulated via CSS
	icon.addClass('learner-searched');
	
	toggleInterval = setInterval(function(){
		icon.toggle();
	}, 500);
	
	setTimeout(function(){
		clearInterval(toggleInterval);
		//if the search box was cleared during blinking, act accordingly
		if (!sequenceSearchedLearner) {
			icon.removeClass('learner-searched');
		}
	}, 3000);
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
function showClassDialog(role){
	// fetch available and already participating learners and monitors
	if (!role) {
		// first time show, fill both lists
		fillClassList('Learner', false);
		fillClassList('Monitor', true);
	
		$('#classDialog').modal('show');
	} else {
		// refresh after page shift or search
		fillClassList(role, role.toLowerCase() == 'monitor');
	}
}


/**
 * Fills class member list with user information.
 */
function fillClassList(role, disableCreator) {
	var dialog = $('#classDialog'),
		table = $('#class' + role + 'Table', dialog),
		list = $('.dialogList', table).empty(),
		searchPhrase = role == 'Learner' ? $('.dialogSearchPhrase', table).val().trim() : null,
		ajaxProperties = dialog.data(role + 'AjaxProperties'),
		users = null,
		userCount = null;
	
	if (!ajaxProperties) {
		// initialise ajax config
		ajaxProperties = {
		dataType : 'json',
		url : LAMS_URL + 'monitoring/monitoring/getClassMembers.do',
		cache : false,
		async : false,
		data : {
			'lessonID'  : lessonId,
					'role'      	 : role.toUpperCase(),
					'pageNumber'	 : 1,
					'orderAscending' : true
		}
			};
	
		dialog.data(role + 'AjaxProperties', ajaxProperties);
	}

	// add properties for this call only
	if (!searchPhrase){
		searchPhrase = null;
	}
	ajaxProperties.data.searchPhrase = searchPhrase;
	ajaxProperties.success = function(response) {
		users = response.users;
		userCount = response.userCount;
}

	$.ajax(ajaxProperties);

	// hide unnecessary controls
	togglePagingCells(table, ajaxProperties.data.pageNumber, Math.ceil(userCount / 10));
	
	$.each(users, function(userIndex, user) {
		var checkbox = $('<input />').attr({
	    	 'type' : 'checkbox'
	      }).change(function(){
	    	editClassMember($(this));
	      }),
	    		
	      userDiv = $('<div />').attr({
				'userId'  : user.id
				})
	          .addClass('dialogListItem')
		      .html(getLearnerDisplayName(user))
		      .prepend(checkbox)
		      .appendTo(list);
	    	
		if (user.classMember) {
			checkbox.prop('checked', 'checked');
			if (user.readonly) {
				// user creator must not be deselected
				checkbox.attr('disabled', 'disabled');
			}
		}
		
		if (disableCreator && user.lessonCreator) {
			userDiv.addClass('dialogListItemDisabled');
		} else {
			userDiv.click(function(event){
				if (event.target == this && !checkbox.is(':disabled')) {
		    		checkbox.prop('checked', checkbox.is(':checked') ? null : 'checked');
		    		checkbox.change();
		    	}
		    })
		}
	});	

	colorDialogList(table);
}

/**
 * Adds/removes a Learner/Monitor to/from the class.
 */
function editClassMember(userCheckbox){
	var data={ 
		'lessonID' : lessonId,
		'userID'   : userCheckbox.parent().attr('userId'),
		'role'     : userCheckbox.closest('table').is('#classMonitorTable') ? 'MONITOR' : 'LEARNER',
		'add'      : userCheckbox.is(':checked')
	};
	data[csrfTokenName] = csrfTokenValue;

	$.ajax({
		url : LAMS_URL + 'monitoring/monitoring/updateLessonClass.do',
		type : 'POST',
		cache : false,
		data : data
	});
}

/**
 * Adds all learners to the class.
 */
function addAllLearners(){
	if (confirm(LABELS.CLASS_ADD_ALL_CONFIRM)) {
		$.ajax({
			url : LAMS_URL + 'monitoring/monitoring/addAllOrganisationLearnersToLesson.do',
			type : 'POST',
			cache : false,
			data : {
				'lessonID' : lessonId
			},
			success : function(){
				alert(LABELS.CLASS_ADD_ALL_SUCCESS);
				$('#classDialog').modal('hide');
			}
		});
	}
}

/**
 * Opens Authoring for live edit.
 */
function openLiveEdit(){
	if (confirm(LABELS.LIVE_EDIT_CONFIRM)) {
		$.ajax({
			dataType : 'text',
			url : LAMS_URL + 'monitoring/monitoring/startLiveEdit.do',
			cache : false,
			async : false,
			data : {
				'ldId'      : ldId
			},
			success : function(response) {
				if (response) {
					alert(response);
				} else {
					window.parent.showAuthoringDialog(ldId, lessonId);
					closeMonitorLessonDialog();
				}
			}
		});	
	}
}

/**
 * Adjusts sequence canvas (SVG) based on space available in the dialog.
 */
function resizeSequenceCanvas(width, height){
	var svg = $('svg.svg-learning-design', sequenceCanvas),
		viewBoxParts = svg.attr('viewBox').split(' '),
		svgHeight = +viewBoxParts[3],
		sequenceCanvasHeight = learningDesignSvgFitScreen ? height - 140 : Math.max(svgHeight + 10, height - 140);
	
	// By default sequenceCanvas div is as high as SVG, but for SVG vertical centering
	// we want it to be as large as available space (iframe height minus toolbars)
	// or if SVG is higher, then as high as SVG
	sequenceCanvas.css({
		'height'  : sequenceCanvasHeight + 'px'
	});
	
	// if we want SVG to fit screen, then we make it as wide & high as sequenceCanvas div
	// but no more than extra 30% because small SVGs look weird if they are too large
	if (learningDesignSvgFitScreen) {
		var svgWidth = +viewBoxParts[2],
			sequenceCanvasWidth = sequenceCanvas.width();
		svg.attr({
			'preserveAspectRatio' : 'xMidYMid meet',
			'width' : Math.min(svgWidth * 1.3, sequenceCanvasWidth - 10),
			'height': Math.min(svgHeight * 1.3, sequenceCanvasHeight - 10)
		})
	}
}

function canvasFitScreen(fitScreen, skipResize) {
	learningDesignSvgFitScreen = fitScreen;
	if (!skipResize) {
		updateSequenceTab();
	}
	
	if (fitScreen) {
		$('#canvasFitScreenButton').hide();
		$('#canvasOriginalSizeButton').show();
	} else {
		$('#canvasFitScreenButton').show();
		$('#canvasOriginalSizeButton').hide();
	}
}

//********** LEARNERS TAB FUNCTIONS **********

/**
 * Inits Learners tab widgets.
 */
function initLearnersTab() {
	// search for users with the term the Monitor entered
	$("#learnersSearchPhrase").autocomplete( {
		'source'   : LAMS_URL + "monitoring/monitoring/autocomplete.do?scope=lesson&lessonID=" + lessonId,
		'delay'    : 700,
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
	var pageStartIndex = pageNumber - 5,
		pageEndIndex = pageNumber + 5 - Math.min(pageStartIndex,0);
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
		  '<tr><td class="active progressBarLabel" id="progressBarLabel;00;"><div id="portrait-;00;" class="roffset5"/><span class="portrait-sm-lineheight" style="font-weight: bold">;11;</span>';

		learnerProgressCellsTemplate +=
			'<a class="btn btn-xs btn-default pull-right tour-email-button" href="#" onClick="javascript:showEmailDialog(;00;)"><i class="fa fa-envelope-o"></i> '
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
		url : LAMS_URL + 'monitoring/monitoring/getLearnerProgressPage.do',
		cache : false,
		data : {
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
					addPortrait($("#portrait-"+this.id), this.portraitId, this.id, 'small', true, LAMS_URL );
					
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
 * Clears previous run search for phrase, in Learners tab.
 */
function learnersClearSearchPhrase(){
	$('#learnersSearchPhrase').val('').autocomplete("close");
	loadLearnerProgressPage(1, '');
	$('#learnersSearchPhraseClear').hide();
}

/**
 * Clears previous run search for phrase, in Edit Class dialog.
 */
function classClearSearchPhrase(){
	var dialog = $('#classDialog');
	$('.dialogSearchPhrase', dialog).val('').autocomplete("close");
	dialog.data('LearnerAjaxProperties').data.pageNumber = 1;
	showClassDialog('Learner');
	$('.dialogSearchPhraseClear', dialog).css('visibility', 'hidden');
}


/**
 * Clears previous run search for phrase, in Learner Group dialogs.
 */
function learnerGroupClearSearchPhrase(){
	var dialog = $('#learnerGroupDialog');
	$('.dialogSearchPhrase', dialog).val('').autocomplete("close");
	dialog.data('ajaxProperties').data.pageNumber = 1;
	showLearnerGroupDialog();
	$('.dialogSearchPhraseClear', dialog).css('visibility', 'hidden');
}

//********** GRADEBOOK TAB FUNCTIONS **********

/**
 * Inits Gradebook Tab.
 */
function initGradebookTab() {
	$("#gradebookLoading").hide();
}

/**
 * Refreshes Gradebook Tab.
 */
function updateGradebookTab() {
	$("#gradebookLoading").show();
	$("#gradebookDiv").load(LAMS_URL + 'gradebook/gradebookMonitoring.do?lessonID=' + lessonId, function() {
		  $("#gradebookLoading").hide();
	});
}

//********** COMMON FUNCTIONS **********

function isAutoRefreshBlocked(){
	return autoRefreshBlocked || $('#learnerGroupDialog').hasClass('in');
}

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
			if (!isAutoRefreshBlocked()) {
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
	} else if (tabName == 'gradebook'){
		updateGradebookTab();
	}
}


/**
 * Tells parent document to close this Monitor dialog.
 */
function closeMonitorLessonDialog(refresh) {
	//check method is available in order to avoid JS errors (as it's not available from integrations)
	if (typeof window.parent.closeDialog === "function") {
		window.parent.closeDialog('dialogMonitorLesson' + lessonId, refresh);
	}
}

/**
 * Show a dialog with user list and optional Force Complete and View Learner buttons.
 */
function showLearnerGroupDialog(ajaxProperties, dialogTitle, allowSearch, allowForceComplete, allowView, allowEmail) {
	var learnerGroupDialog = $('#learnerGroupDialog'),
		learnerGroupList = $('.dialogList', learnerGroupDialog).empty(),
		// no parameters provided? just work on what we saved
		isRefresh = ajaxProperties == null,
		learners = null,
		learnerCount = null;
	
	if (isRefresh) {
		// ajax and other properties were saved when the dialog was opened
		ajaxProperties = learnerGroupDialog.data('ajaxProperties');
		allowForceComplete = learnerGroupDialog.data('allowForceComplete');
		allowView = learnerGroupDialog.data('allowView');
		allowEmail = learnerGroupDialog.data('allowEmail');
		allowSearch = $('#learnerGroupSearchRow', learnerGroupDialog).is(':visible');
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
		
		$('#learnerGroupSearchRow', learnerGroupDialog).css('display', allowSearch ? 'table-row' : 'none');
	}
	
	var pageNumber = ajaxProperties.data.pageNumber;

	// set values for current variable instances
	ajaxProperties.success = function(response) {
		learners = response.learners;
		learnerCount = response.learnerCount;
	};
	
	var searchPhrase = allowSearch ?  $('.dialogSearchPhrase', learnerGroupDialog).val().trim() : null;
	ajaxProperties.data.searchPhrase = searchPhrase; 
	
	// make the call
	$.ajax(ajaxProperties);

	// did all users already drift away to an another activity or there was an error?
	// close the dialog and refresh the main screen
	if (!learnerCount && !searchPhrase) {
		if (isRefresh) {
			 learnerGroupDialog.modal('hide');
		}
		updateSequenceTab();
		return;
	}
	
	// did some users already drift away to an another activity?
	// move back until you get a page with any users
	var maxPageNumber = Math.ceil(learnerCount / 10);
	if (maxPageNumber > 0 && pageNumber > maxPageNumber) {
		shiftLearnerGroupList(-1);
		return;
	}
	
	// hide unnecessary controls
	togglePagingCells(learnerGroupDialog, pageNumber, maxPageNumber);

	$.each(learners, function(learnerIndex, learner) {
		var viewUrl = allowView ? LAMS_URL + 'monitoring/monitoring/getLearnerActivityURL.do?userID=' 
        				       	  + learner.id + '&activityID=' + ajaxProperties.data.activityID + '&lessonID=' + lessonId
        				        : null,
				learnerDiv = $('<div />').attr({
									'userId'  : learner.id,
									'viewUrl'    : viewUrl
									})
			                      .addClass('dialogListItem')
							      .appendTo(learnerGroupList),
				portraitDiv = $('<div />').attr({
						'id': 'user-'+learner.id,
						})
						.addClass('roffset5')
						.appendTo(learnerDiv);
				addPortrait( portraitDiv, learner.portraitId, learner.id, 'small', true, LAMS_URL );
				$('<span/>').html(getLearnerDisplayName(learner))
					.addClass('portrait-sm-lineheight')
					.appendTo(learnerDiv);

		if (allowForceComplete || allowView || allowEmail) {
			learnerDiv.click(function(event){
				// select the learner
				var learnerDiv = $(this),
					selectedSiblings = learnerDiv.siblings('div.dialogListItem.dialogListItemSelected');
				    	// enable buttons
			    $('button.learnerGroupDialogSelectableButton', learnerGroupDialog).prop('disabled', false);
			    
				if (allowForceComplete && (event.metaKey || event.ctrlKey)) {
					var isSelected = learnerDiv.hasClass('dialogListItemSelected');
					if (isSelected) {
						// do not un-select last learner
						if (selectedSiblings.length > 0) {
							learnerDiv.removeClass('dialogListItemSelected')
						}
					} else {
						learnerDiv.addClass('dialogListItemSelected');
					}
					if (selectedSiblings.length + (isSelected ? 0 : 1) > 1) {
						// disable view button - only one learner can be viewed and multiple are selected
						$('button#learnerGroupDialogViewButton', learnerGroupDialog).prop('disabled', true);
					}
				} else {
					learnerDiv.addClass('dialogListItemSelected');
					// un-select other learners
					selectedSiblings.removeClass('dialogListItemSelected');
				}
			    });
			if (allowView){
				dblTap(learnerDiv, function(){
					// same as clicking View Learner button
					openPopUp(viewUrl, "LearnActivity", popupHeight, popupWidth, true);
				});
			}
		}
	});
	
	colorDialogList(learnerGroupDialog);
	
	if (!isRefresh) {
		// show buttons and labels depending on parameters
		$('span#learnerGroupMultiSelectLabel, button#learnerGroupDialogForceCompleteButton', learnerGroupDialog)
			.css('display', allowForceComplete ? 'inline' : 'none');
		$('button#learnerGroupDialogViewButton', learnerGroupDialog)
			.css('display', allowView ? 'inline' : 'none');
		$('button#learnerGroupDialogEmailButton', learnerGroupDialog)
			.css('display', allowEmail ? 'inline' : 'none');
	
	$('.modal-title', learnerGroupDialog).text(dialogTitle);
	learnerGroupDialog.data({
		 // save properties for refresh
		 'ajaxProperties' : ajaxProperties,
		 'allowForceComplete' : allowForceComplete,
		 'allowView' : allowView,
		 'allowEmail' : allowEmail
		})
		.modal('show');	
	}
}


/**
 * Formats learner name.
 */
function getLearnerDisplayName(learner, raw) {
	return raw ? learner.lastName + ', ' + learner.firstName + ' (' + learner.login + ')' + (learner.group ? ' - ' + learner.group : '')
			   : escapeHtml(learner.lastName) + ', ' + escapeHtml(learner.firstName) + ' (' + escapeHtml(learner.login) + ')' 
			   	 + (learner.group ? ' - ' + escapeHtml(learner.group) : '');
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
function sortLearnerGroupList() {
	var learnerGroupDialog = $('#learnerGroupDialog'),
		sortIcon = $('td.sortCell span', learnerGroupDialog),
		ajaxProperties = learnerGroupDialog.data('ajaxProperties'),
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

/**
 * Change order of learner sorting in Edit Class dialog.
 */
function sortClassList(role) {
	var classDialog = $('#classDialog'),
		table = $('#class' + role + 'Table', classDialog),
		sortIcon = $('td.sortCell span', table),
		ajaxProperties = classDialog.data(role + 'AjaxProperties'),
		// reverse current order after click
		orderAscending = !ajaxProperties.data.orderAscending;

	if (orderAscending) {
		sortIcon.removeClass('ui-icon-triangle-1-s').addClass('ui-icon-triangle-1-n');
	} else {
		sortIcon.removeClass('ui-icon-triangle-1-n').addClass('ui-icon-triangle-1-s');
	}
	
	ajaxProperties.data.orderAscending = orderAscending;
	// refresh the list
	showClassDialog(role);
}

/**
 * Colours a list of users
 */
function colorDialogList(parent) {
	$('.dialogList div.dialogListItem', parent).each(function(userIndex, userDiv){
		// every odd learner has different background
		$(userDiv).css('background-color', userIndex % 2 ? '#f5f5f5' : 'inherit');
	});
}

/**
* Change page in the group dialog.
*/
function shiftLearnerGroupList(shift) {
	var learnerGroupDialog = $('#learnerGroupDialog'),
		ajaxProperties = learnerGroupDialog.data('ajaxProperties'),
		pageNumber = ajaxProperties.data.pageNumber + shift;
	if (pageNumber < 0) {
		pageNumber = 1;
	}
	ajaxProperties.data.pageNumber = pageNumber;
	// refresh the dialog with new parameters
	showLearnerGroupDialog();
}

/**
* Change page in the Edit Class dialog.
*/
function shiftClassList(role, shift) {
	var classDialog = $('#classDialog'),
		ajaxProperties = classDialog.data(role + 'AjaxProperties'),
		pageNumber = ajaxProperties.data.pageNumber + shift;
	if (pageNumber < 0) {
		pageNumber = 1;
	}
	ajaxProperties.data.pageNumber = pageNumber;
	// refresh the dialog with new parameters
	showClassDialog(role);
}


/**
 * Hides/shows paging controls
 */
function togglePagingCells(parent, pageNumber, maxPageNumber) {
	if (pageNumber + 10 <= maxPageNumber) {
		$('td.pagePlus10Cell', parent).css('visibility', 'visible');
	} else {
		$('td.pagePlus10Cell', parent).css('visibility', 'hidden');
	}
	if (pageNumber - 10 < 1) {
		$('td.pageMinus10Cell', parent).css('visibility', 'hidden');
	} else {
		$('td.pageMinus10Cell', parent).css('visibility', 'visible');
	}
	if (pageNumber + 1 <= maxPageNumber) {
		$('td.pagePlus1Cell', parent).css('visibility', 'visible');
	} else {
		$('td.pagePlus1Cell', parent).css('visibility', 'hidden');
	}		
	if (pageNumber - 1 < 1) {
		$('td.pageMinus1Cell', parent).css('visibility', 'hidden');
	} else {
		$('td.pageMinus1Cell', parent).css('visibility', 'visible');
	}
	if (maxPageNumber < 2) {
		$('td.pageCell', parent).css('visibility', 'hidden');
	} else {
		$('td.pageCell', parent).css('visibility', 'visible').text(pageNumber + ' / ' + maxPageNumber);
	}
}

/**
 * Works as dblclick for mobile devices.
 */
function dblTap(elem, dblClickFunction) {
 	 // double tap detection on mobile devices; it works also for mouse clicks
	 // temporarly switched to click as jQuery mobile was removed for bootstrapping
	 elem.click(function(event) {
		// was the second click quick enough after the first one?
		var currentTime = new Date().getTime(),
			tapLength = currentTime - lastTapTime;
		lastTapTime = currentTime;
		
		if (lastTapTarget && lastTapTarget.classList.contains('learner-icon') && tapLength < 10) {
			// after clicking learner icon there is a propagation to activity, which must be ignored
			// we can not stop propagation completetly as force complete stops working
			return;
		}
		
		// is the second click on the same element as the first one?
		if (event.currentTarget == lastTapTarget) {
			if (tapLength < tapTimeout && tapLength > 0) {
				event.preventDefault();
				dblClickFunction(event);
			}
		}
		
		lastTapTarget = event.currentTarget;
	});
}
