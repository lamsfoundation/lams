﻿﻿﻿﻿﻿﻿﻿﻿﻿// ********** GLOBAL VARIABLES **********
// copy of lesson SVG so it does no need to be fetched every time
let originalSequenceCanvas = null,
	// DIV container for lesson SVG
	// it gets accessed so many times it's worth to cache it here
	sequenceCanvas = $('#sequenceCanvas'),
	// switch between SVG original size and fit-to-sreen (enlarge/shrink)
	learningDesignSvgFitScreen = false,
// info box show timeout
	sequenceInfoTimeout = 8000,
	// which learner was selected in the search box
	sequenceSearchedLearner = null,
	// for synchronisation purposes
	sequenceRefreshInProgress = false,
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

function refreshMonitor(tabName, isAuto){
	if (autoRefreshIntervalObject && !isAuto) {
		clearInterval(autoRefreshIntervalObject);
		autoRefreshIntervalObject = null;
	}

	if (!autoRefreshIntervalObject) {
		autoRefreshIntervalObject = setInterval(function(){
			// refreshMonitor(null, true);
		}, autoRefreshInterval);
	}
	
	updateLessonTab();
	updateSequenceTab();
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
			updateContributeActivities(response.contributeActivities);
		}
	});
	
	
	drawChart('pie', 'chartDiv',
		  LAMS_URL + 'monitoring/monitoring/getLessonChartData.do?lessonID=' + lessonId,
		  true);
}


function updateContributeActivities(contributeActivities) {
	let requiredTasksPanel = $('#required-tasks'),
		requiredTasksContent = $('#required-tasks-content', requiredTasksPanel),
		row = null;
	$('.contribute-row', requiredTasksContent).remove();
	
	/*
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
	*/
	
	if (contributeActivities) {
		$.each(contributeActivities, function(){
			let contributeActivity = this;
			if (contributeActivity.title) {
				$('<div />').addClass('label contribute-activity-title contribute-row')
							.text(contributeActivity.title)
							.attr('id', 'contribute' + contributeActivity.activityID)
							.appendTo(requiredTasksContent);
			}
			
			let row = $('<div />').addClass('row contribute-row' + (contributeActivity.title ? ' ml-1' : ''))
								  .appendTo(requiredTasksContent);
			
			$.each(this.contributeEntries, function(){
				var entryContent = '<div class="col-3 label">';
				switch (this.contributionType) {
					case 3  : entryContent += LABELS.CONTRIBUTE_GATE; break;
					case 6  : entryContent += LABELS.CONTRIBUTE_GROUPING; break;
					case 7  : entryContent += LABELS.CONTRIBUTE_TOOL; break;
					case 9  : entryContent += LABELS.CONTRIBUTE_BRANCHING; break;
					case 11 : entryContent += LABELS.CONTRIBUTE_CONTENT_EDITED; break; 
					case 12 : entryContent += LABELS.CONTRIBUTE_GATE_PASSWORD; break; 
				}
				entryContent += '</div><div class="col-9 text-right">';
				switch (this.contributionType) {
					case 3  : 
					case 12 : if (this.isComplete) {
						 		entryContent += '<div class="pull-right"><button class="contribute-gate-opened-button btn btn-success" '
						 					 + 'onClick="javascript:openPopUp(\'' + this.url 
						 					 + '\',\'ContributeActivity\', 800, 1280, true)" '
						 					 + 'title="' + LABELS.CONTRIBUTE_OPENED_GATE_TOOLTIP + '">'
						 					 + LABELS.CONTRIBUTE_OPENED_GATE 
						 					 + '</button></div>';
							} else {
								entryContent += '<div class="btn-group" role="group"><button onClick="javascript:openGateNow('
                                    + contributeActivity.activityID + ')" type="button" class="btn" title="' 
									+ LABELS.CONTRIBUTE_OPEN_GATE_NOW_TOOLTIP + '">' 
									+ LABELS.CONTRIBUTE_OPEN_GATE_NOW_BUTTON 
									+ '</button><button type="button" class="btn dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">'
									+ '<div class="dropdown-menu"><a href="#" class="dropdown-item" onClick="javascript:openPopUp(\''
                                    + this.url + '\',\'ContributeActivity\', 800, 1280, true)" title="' 
									+ LABELS.CONTRIBUTE_OPEN_GATE_TOOLTIP + '">' 
									+ LABELS.CONTRIBUTE_OPEN_GATE_BUTTON + '</a></div></div>';
									
							}
							break;
					default : entryContent += '<button type="button" class="btn contribute-go-button" onClick="javascript:openPopUp(\''
						 + this.url + '\',\'ContributeActivity\', 800, 1280, true)" title="' + LABELS.CONTRIBUTE_TOOLTIP
						 + '">' + LABELS.CONTRIBUTE_BUTTON + '</button>';
				}

				row.html(entryContent + "</div>");
			});
		});
		
		requiredTasksPanel.show();
	} else {
		requiredTasksPanel.hide();
	}
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
		var forceBackwardsDialog = $('#forceBackwardsDialog'),
			learners = forceBackwardsDialog.data('learners'),
			moveAll = learners === true;
		forceCompleteExecute(moveAll ? null : learners,
			 moveAll ? forceBackwardsDialog.data('currentActivityId') : null,
			 forceBackwardsDialog.data('activityId'),
			 false);
		forceBackwardsDialog.modal('hide');
	});

	$('#forceBackwardsRemoveContentYesButton', forceBackwardsDialogContents).click(function(){
		var forceBackwardsDialog = $('#forceBackwardsDialog');
			learners = forceBackwardsDialog.data('learners'),
			moveAll = learners === true;
		forceCompleteExecute(moveAll ? null : learners,
			 moveAll ? forceBackwardsDialog.data('currentActivityId') : null,
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
					
					if (activity.gateOpen && gateClosedIcon.length > 0) {
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
						
						 $(gateOpenIconData).clone().attr({
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
				$('#forceBackwardsMsg', '#forceBackwardsDialog').html(msgString);				
				$('#forceBackwardsDialog').data({
					'learners' : learners,
					'currentActivityId' : currentActivityId,
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
			if (learnerIndex >= 23) {
				// display only first few learners, not all of them
				return false;
			}
			
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
	  .text(learnerCount + '/' + learnerTotalCount)
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
	var svg = $('svg.svg-learning-design', sequenceCanvas);
	
	if (svg.length === 0){
		// skip resizing if the SVG has not loaded (yet)
		return;
	}
	
	var viewBoxParts = svg.attr('viewBox').split(' '),
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
	
function openPopUp(url, title, h, w, status, forceNewWindow) {

	var width = screen.width;
	var height = screen.height;

	var left = ((width / 2) - (w / 2));
	var top = ((height / 2) - (h / 2));

	if (forceNewWindow) {
		// opens a new window rather than loading content to existing one
		title += new Date().getTime();
	}

	window.open(url, title, "HEIGHT=" + h + ",WIDTH=" + w
			+ ",resizable=yes,scrollbars=yes,status=" + status
			+ ",menubar=no, toolbar=no"
			+ ",top=" + top + ",left=" + left);
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