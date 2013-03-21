// ********** MAIN FUNCTIONS **********
// copy of lesson SVG so it does no need to be fetched every t
var originalSequenceCanvas = null;
// DIV container for lesson SVG
var sequenceCanvas = null;
// how learners in pop up are currently sorted
var learnerGroupListAscending = false;

function initLessonTab(){
	refreshSequenceCanvas();
	
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
				// reset sort order
				learnerGroupListAscending = false;
				sortLearnerGroupList();
				$('div.learnerGroupListItem', this).each(function(learnerIndex, learnerDiv){
					// every odd learner has different background
					$(learnerDiv).css('background-color', learnerIndex % 2 ? '#dfeffc' : 'inherit');
				});
				// until user selects an user, buttons remain disabled
				$('button.learnerGroupDialogSelectableButton').blur().removeClass('ui-state-hover')
					.attr('disabled', 'disabled');
			},
			'buttons' : [
			             {
			            	'text'   : FORCE_COMPLETE_BUTTON_LABEL,
			            	'id'     : 'learnerGroupDialogForceCompleteButton',
			            	'class'  : 'learnerGroupDialogSelectableButton',
			            	'click'  : function() {
			            		var selectedLearner = $('#learnerGroupList div.learnerGroupListItemSelected');
			            		// make sure there is only one selected learner
			            		if (selectedLearner.length == 1) {
			            			// go to "force complete" mode, similar to draggin user to an activity
			            			var activityId = $(this).dialog('option', 'activityId');
			            			var dropArea = sequenceCanvas.add('#completedLearnersContainer');
			            			dropArea.css('cursor', 'url('
			            					+ LAMS_URL + 'images/icons/user.png),pointer')
			            				.one('click', function(event) {
			            					dropArea.off('click').css('cursor', 'default');
			            					forceComplete(activityId, selectedLearner.attr('learnerId'), 
			            							selectedLearner.text(), event.pageX, event.pageY);
			            				});
				            		$(this).dialog('close');
				            		alert(FORCE_COMPLETE_CLICK_LABEL.replace('[0]', selectedLearner.text()));
			            		}
							}
			             },
			             {
			            	'text'   : VIEW_LEARNER_BUTTON_LABEL,
			            	'id'     : 'learnerGroupDialogViewButton',
			            	'class'  : 'learnerGroupDialogSelectableButton',
			            	'click'  : function() {
			            		var selectedLearner = $('#learnerGroupList div.learnerGroupListItemSelected');
			            		if (selectedLearner.length == 1) {
			            			openWindow(LAMS_URL + selectedLearner.attr('viewUrl'), "LearnActivity", 800, 600);
			            		}
							}
			             },
			             {
			            	'text'   : CLOSE_BUTTON_LABEL,
			            	'id'     : 'learnerGroupDialogCloseButton',
			            	'click'  : function() {
								$(this).dialog('close');
							} 
			             }
			]
		});
	
	$('#sortLearnerGroupListButton').click(sortLearnerGroupList);
}

function initSequenceTab(){
	
}

function initLearnersTab(){
	
}

/**
 * Reloads Learning Design SVG and adds learner progress information.
 */
function refreshSequenceCanvas(){
	if (originalSequenceCanvas) {
		// put bottom layer, LD SVG
		sequenceCanvas.html(originalSequenceCanvas);
	} else {
		// fetch SVG just once, since it is immutable
		$.ajax( {
			type : 'GET',
			dataType : 'text',
			url : LAMS_URL + 'home.do',
			async : false,
			cache : false,
			data : {
				'method'    : 'createLearningDesignThumbnail',
				'svgFormat' : 1,
				'ldId'      : ldId
			},
			success : function(response) {
				originalSequenceCanvas = response;
				sequenceCanvas = $('#sequenceCanvas').html(originalSequenceCanvas);
				
				var canvasHeight = sequenceCanvas.height();
				var canvasWidth = sequenceCanvas.width();
				var svg = $('svg', sequenceCanvas);
				var canvasPaddingTop = canvasHeight/2 - svg.attr('height')/2;
				var canvasPaddingLeft = canvasWidth/2 - svg.attr('width')/2;

				if (canvasPaddingTop > 0) {
					sequenceCanvas.css({
						'padding-top' : canvasPaddingTop,
						'height'      : canvasHeight - canvasPaddingTop		
					});
				}
				if (canvasPaddingLeft > 0) {
					sequenceCanvas.css({
						'padding-left' : canvasPaddingLeft,
						'width'        : canvasWidth - canvasPaddingLeft		
					});
				}
			}
		});
	}
	
	// get progress data
	$.ajax({
		type : 'GET',
		dataType : 'json',
		url : LAMS_URL + 'monitoring/monitoring.do',
		cache : false,
		data : {
			'method'    : 'getLessonProgressJSON',
			'lessonID'  : lessonId
		},
		success : function(response) {
			var learnerCount = 0;
			$.each(response.activities, function(){
				if (this.learners) {
					// are there any learners in this or any activity?
					learnerCount += this.learners.length;
					// put learner icons on each activity shape
					addLearnerIcons(this);
				}
			});
			
			if (learnerCount > 0) {
				// IMPORTANT! Reload SVG, otherwise added icons will not get displayed
				sequenceCanvas.html(sequenceCanvas.html());
			}
			
			addCompletedLearnerIcons(response.completedLearners, learnerCount);
			
			$.each(response.activities, function(activityIndex, activity){
				addLearnerIconsHandlers(activity);
				
				if (activity.url) {
					var activityGroup = $('rect[id="act' + activity.id + '"]', sequenceCanvas).parent();
					activityGroup.css('cursor', 'pointer').dblclick(function(){
						// double click on activity shape to open Monitoring for this activity
						openWindow(LAMS_URL + activity.url, "MonitorActivity", 900, 720);
					});
				}
			});
		}
	});
}

/**
 * Forces given learner to move to activity indicated on SVG by coordinated (drag-drop)
 */
function forceComplete(currentActivityId, learnerId, learnerName, x, y) {
	// check all activities and "users who finished lesson" bar
	$('rect[id^="act"]', sequenceCanvas).add('#completedLearnersContainer').each(function(){
		// find which activity learner was dropped on
		var act = $(this);
		var actX = act.offset().left;
		var actY = act.offset().top;
		var actEndX = actX + (act.width() ? act.width() : +act.attr('width'));
		var actEndY = actY + (act.height() ? act.height() : +act.attr('height'));
		if (x >= actX && x<= actEndX && y>= actY && y<=actEndY) {
			var previousActivityId = null;
			var executeForceComplete = false;
			
			if (act.attr('id') == 'completedLearnersContainer') {
				executeForceComplete = confirm(FORCE_COMPLETE_END_LESSON_CONFIRM_LABEL
						.replace('[0]',learnerName));
			} else {
				var targetActivityId = act.parent().attr('id');
				var targetActivityName = act.siblings('text[id^="TextElement"]').text();
				
				// find if it is not already completed/current activity - it is forbidden
				var precedingActivityId = targetActivityId;
				// move step by step to the sequence beginning
				// if current activity is found, it is a activity still in front, so it's OK
				do {
					// find transition line and extract activity IDs from them
					var transitionLine = $('line[id$="to_' 
							+ precedingActivityId + '"]:not([id^="arrow"])'
							, sequenceCanvas);
					precedingActivityId = transitionLine.length == 1 ? 
							transitionLine.attr('id').split('_')[0] : null;
					if (previousActivityId == null) {
						previousActivityId = precedingActivityId;
					}
					if (precedingActivityId == currentActivityId) {
						break;
					}
				} while (precedingActivityId);
				
				if (precedingActivityId) {
					executeForceComplete = confirm(FORCE_COMPLETE_ACTIVITY_CONFIRM_LABEL
							.replace('[0]', learnerName).replace('[1]', targetActivityName));
				} else {
					alert(FORCE_COMPLETE_DROP_FAIL_LABEL
							.replace('[0]', learnerName).replace('[1]', targetActivityName));
				}
			}
			
			if (executeForceComplete) {
				// tell server to force complete the learner
				$.ajax({
					type : 'GET',
					dataType : 'xml',
					url : LAMS_URL + 'monitoring/monitoring.do',
					cache : false,
					data : {
						'method'     : 'forceComplete',
						'lessonID'   : lessonId,
						'learnerID'  : learnerId,
						'activityID' : previousActivityId
					},
					success : function(response) {
						// inform user of result
						var messageElem = $(response).find('var[name="messageValue"]');
						if (messageElem.length > 0){
							alert(messageElem.text());
						}
						
						// progress changed, show it to monitor
						refreshSequenceCanvas();
					}
				});
			}
			// we found our target, stop iteration
			return false;
		}
	});
}

/**
 * Draw user icons on top of activities.
 */
function addLearnerIcons(activity) {
	var activityRect = $('rect[id="act' + activity.id + '"]', sequenceCanvas);
	var activityGroup = activityRect.parent();
	var actX = +activityRect.attr('x') + 1;
	var actY = +activityRect.attr('y') + 1;
	var actTooltip = LEARNER_GROUP_LIST_TITLE_LABEL;
	
	$.each(activity.learners, function(learnerIndex, learner){
		if (activity.learners.length > 8 && learnerIndex == 7) {
			// maximum 8 icons fit in an activity 
			var actRightBorder = actX + +activityRect.attr('width');
			var groupTitle = activity.learners.length + ' ' + LEARNER_GROUP_COUNT_LABEL
				+ ' ' + LEARNER_GROUP_SHOW_LABEL;
			// if icons do not fit in shape anymore, show a group icon
			var element = appendXMLElement('image', {
				'id'         : 'act' + activity.id + 'learnerGroup',
				'x'          : actRightBorder - 19,
				'y'          : actY + 1,
				'height'     : 16,
				'width'      : 16,
				'xlink:href' : LAMS_URL + 'images/icons/group.png'
			}, null, activityGroup[0]);
			appendXMLElement('title', null, groupTitle, element);
			// add a small number telling how many learners are in the group
			element = appendXMLElement('text', {
				'id'         : 'act' + activity.id + 'learnerGroupText',
				'x'          : actRightBorder - 10,
				'y'          : actY + 24,
				'text-anchor': 'middle',
				'font-family': 'Verdana',
				'font-size'  : 8
			}, activity.learners.length, activityGroup[0]);
			appendXMLElement('title', null, groupTitle, element);
			// stop processing learners
			return false;
		} else {
			/* make an icon for each learner */
			var element = appendXMLElement('image', {
				'id'         : 'act' + activity.id + 'learner' + learner.id,
				'x'          :  actX + learnerIndex*15,
				'y'          :  actY,
				'height'     : 16,
				'width'      : 16,
				'xlink:href' : LAMS_URL + 'images/icons/user.png'
			}, null, activityGroup[0]);
			var learnerDisplayName = getLearnerDisplayName(learner);
			appendXMLElement('title', null, learnerDisplayName, element);
			actTooltip += '\n' + learnerDisplayName;
		}
	});
	
	appendXMLElement('title', null, actTooltip, activityGroup[0]);
}

/**
 * After SVG refresh, add click/dblclick/drag handlers to user icons.
 */
function addLearnerIconsHandlers(activity) {
	if (activity.learners) {
		var activityGroup = $('rect[id="act' + activity.id + '"]', sequenceCanvas).parent();
		
		$.each(activity.learners, function(learnerIndex, learner){	
			$('image[id="act' + activity.id + 'learner' + learner.id + '"]', activityGroup)
			 .dblclick(function(event){
				 // double click on learner icon to see activity from his perspective
				event.stopPropagation();
				openWindow(LAMS_URL + learner.url, "LearnActivity", 800, 600);
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
				'stop' : function(event, ui) {
					// jQuery droppable does not work for SVG, so this is a workaround
					forceComplete(activity.id, learner.id, getLearnerDisplayName(learner),
							      ui.offset.left, ui.offset.top);
				}
			});
		});

		var learnerGroupIcon = $('*[id^="act' + activity.id + 'learnerGroup"]', activityGroup);
		// 0 is for no group icon, 2 is for icon + digits
		if (learnerGroupIcon.length == 2) {
			var activityName = $('text[id^="TextElement"]', activityGroup).text();
			learnerGroupIcon.dblclick(function(event){
				 // double click on learner icon to see activity from his perspective
				event.stopPropagation();
				showLearnerGroupDialog(activity.id, activityName, activity.learners);
			})
		}
	}
}

/**
 * Add learner icons in "finished lesson" bar.
 */
function addCompletedLearnerIcons(learners, activitiesLearnerCount) {
	var iconsContainer = $('#completedLearnersContainer');
	// clear all icons except the door
	iconsContainer.children(':not(img#completedLearnersDoorIcon)').remove();
	var completedLearnerCount = (learners ? learners.length : 0 );
	var learnerTotalCount = completedLearnerCount + activitiesLearnerCount;
	// show (current/total) label
	$('<span />').attr({
		'title' : LEARNER_FINISHED_COUNT_LABEL
			.replace('[0]', completedLearnerCount).replace('[1]', learnerTotalCount)
	}).text('(' + completedLearnerCount + '/' + learnerTotalCount + ')')
	  .appendTo(iconsContainer);
	
	if (learners) {
		// create learner icons, along with handlers
		$.each(learners, function(learnerIndex, learner){
			// maximum 41 icons in the bar
			if (learners.length > 43 && learnerIndex == 42) {
				// if icons do not fit in cell anymore, show a group icon
				$('<img />').attr({
					'src' : LAMS_URL + 'images/icons/group.png',
					'title'      : LEARNER_GROUP_SHOW_LABEL
				}).css('cursor', 'pointer')
				  .dblclick(function(){
					showLearnerGroupDialog(null, LEARNER_FINISHED_DIALOG_TITLE_LABEL, learners);
				}).appendTo(iconsContainer);
				// stop processing learners
				return false;
			} else {
				// make an icon for each learner
				$('<img />').attr({
					'src' : LAMS_URL + 'images/icons/user.png',
					'title'      : getLearnerDisplayName(learner)
				}).appendTo(iconsContainer);
			}
		});
	}
}

/**
 * Show a dialog with learner list and optional Force Complete and View Learner buttons.
 */
function showLearnerGroupDialog(activityId, dialogTitle, learners) {
	var learnerGroupList = $('#learnerGroupList').empty();
	var learnerGroupDialog = $('#learnerGroupDialog');
	$.each(learners, function(learnerIndex, learner) {
		$(learnerGroupList).append($('<div />').attr({
										'learnerId'  : learner.id,
										'viewUrl'    : learner.url
										})
				                      .addClass('learnerGroupListItem')
        						      .text(getLearnerDisplayName(learner))
        						      .click(function(){
        						    	  // select a learner
        						    	  $(this).addClass('learnerGroupListItemSelected')
        						    	  	.siblings('div.learnerGroupListItem')
        						    	  	.removeClass('learnerGroupListItemSelected');
        						    	  // enable buttons
        						    	  $('button.learnerGroupDialogSelectableButton')
        						    	  	.attr('disabled', null);
        						      })
        						      .dblclick(function(){
        						    	  // same as clicking View Learner button
        						    	  openWindow(LAMS_URL + learner.url, "LearnActivity", 800, 600);
        						      })
        						   );
	});
	
	// no activity ID, i.e. showing finshed learners, so no buttons
	$('button.learnerGroupDialogSelectableButton').css('display', activityId ? 'inline' : 'none');
	
	learnerGroupDialog
		.dialog('option', 
			{
			 'title' : dialogTitle,
			 'activityId' : activityId,
			  
			})
		.dialog('open');	
}

/**
 * Format learner name.
 */
function getLearnerDisplayName(learner) {
	return learner.firstName + ' ' + learner.lastName + ' (' + learner.login + ')';
}

/**
 * Change order of learner sorting in group dialog.
 */
function sortLearnerGroupList() {
	var learnerGroupList = $('#learnerGroupList');
	var learners = learnerGroupList.children('div.learnerGroupListItem');
	if (learners.length > 1) {
		learners.each(function(){
			$(this).detach();
		}).sort(function(a, b){
			var keyA = $(a).text().toLowerCase();
			var keyB = $(b).text().toLowerCase();
			var result = keyA > keyB ? 1 : keyA < keyB ? -1 : 0;
			return learnerGroupListAscending ? -result : result;
		}).each(function(){
			$(this).appendTo(learnerGroupList);
		});
		
		var button = $('#sortLearnerGroupListButton');
		if (learnerGroupListAscending) {
			button.html('▼');
			learnerGroupListAscending = false;
		} else {
			button.html('▲');
			learnerGroupListAscending = true;
		}
	}
}

function openWindow(url, title, width, height) {
	window.open(url, title, "width=" + width + ",height=" + height
			+ ",resizable=yes,scrollbars=yes,status=yes,menubar=no,toolbar=no");
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