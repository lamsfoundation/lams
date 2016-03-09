function initMainPage() {
	initButtons();
	
	if ($('#orgTabs').length > 0) {
		$('#orgTabs').tabs({
			'activate' : function(event, ui){
				loadOrgTab(ui.newPanel);
			}
		}).addClass('ui-tabs-vertical ui-helper-clearfix')
		  .find('li').removeClass('ui-corner-top').addClass('ui-corner-left');
	
		loadOrgTab($('.orgTab').first());
	}
	
	$("#actionAccord").accordion({
		'heightStyle' : 'content'
	});
}

function loadOrgTab(orgTab, refresh) {
	if (!orgTab) {
		orgTab = $('div.orgTab[id^=orgTab-' + $('#orgTabs').tabs('option','active') + ']');
	}
	if (orgTab && (refresh || !orgTab.text())) {
		var orgTabId = orgTab.attr("id");
		var orgId = orgTabId.split('-')[3];
		
		orgTab.load(
			"displayGroup.do",
			{
				stateId : stateId,
				orgId   : orgId
			},
			function() {
				initButtons(orgTabId);
			});
	}
}


function initButtons(containerId) {
	var container = containerId ? $('#' + containerId) : document;

	$(".ui-button", container).button();
	$(".split-ui-button", container).each(function(){
		var buttonContainer = $(this);
		var buttons = buttonContainer.children();
		
		buttons.first().button()
			   .next().button({
			text : false,
			icons : {
				primary : "ui-icon-triangle-1-s"
			}
		});
		
		buttonContainer.buttonset().next().hide().menu();
		
		buttons.each(function(){
			var button = $(this);
			if (!button.attr('onclick')) {
				button.click(function() {
					var menu = $(this).parent().next().show().position({
						my : "right top",
						at : "right bottom",
						of : $(this).parent()
					});
					$(document).one("click", function() {
						menu.hide();
					});
					return false;
				});
			}
		});
	});
}


function makeOrgSortable(orgId) {
	var org = jQuery("div.orgTab[id$='-org-" + orgId + "']");
	$(".lesson-table", org).each(function() {
		makeSortable(this);
	});
	
	$("a.sorting", org).attr({
		"onClick" : null,
		"title"   : LABELS.SORTING_DISABLE
	}).off('click').click(function(){
		makeOrgUnsortable(orgId);
	}).find("img")
	  .attr("src", "images/sorting_enabled.gif");
	
}

function makeOrgUnsortable(orgId) {
	var org = jQuery("div.orgTab[id$='-org-" + orgId + "']");
	$(".lesson-table", org).each(function() {
		$(this).sortable('destroy');
	});
	
	$("a.sorting", org).attr({
		"onClick" : null,
		"title"   : LABELS.SORTING_ENABLE
	}).off('click').click(function(){
		makeOrgSortable(orgId);
	}).find("img")
	  .attr("src", "images/sorting_disabled.gif");
}

function makeSortable(element) {
	$(element).sortable({
		axis : "y",
		delay : 100,
		tolerance : 'pointer',
		cursor : 'n-resize',
		helper : function(e, tr) {
			var $originals = tr.children();
			var $helper = tr.clone();
			$helper.children().each(function(index) {
				// Set helper cell sizes to match the original
				// sizes
				$(this).width($originals.eq(index).width())
			});
			return $helper;
		},
		forceHelperSize : true,
		forcePlaceholderSize : true,
		containment : 'parent',
		stop : function() {
			var ids = $(this).sortable('toArray');
			
			var jLessonsId = $(this).attr("id");
			var dashIndex = jLessonsId.indexOf("-");
			var orgId = (dashIndex > 0 ? jLessonsId.substring(0,
					dashIndex) : jLessonsId);

			$.ajax({
				url : "servlet/saveLessonOrder",
				data : {
					orgId : orgId,
					ids : ids.join(",")
				},
				error : function() {
					loadOrgTab(null, true);
				}
			});
		}
	}).disableSelection();
}



function showMonitorLessonDialog(lessonID) {
	var id = "dialogMonitorLesson" + lessonID,
		dialog = showDialog(id, {
			'lessonID' : lessonID,
			'autoOpen' : false,
			'height' : 600,
			'width' : 1024,
			'draggable' : false,
			'dialogClass' : 'tabbedDialog',
			'title' : LABELS.MONITORING_TITLE,
			'open' : function() {
				// load contents after opening the dialog
				$('iframe', this).attr('src', LAMS_URL
					+ 'home.do?method=monitorLesson&lessonID='
					+ $(this).dialog('option', 'lessonID'));
			},

		}, true);
	
	// if it was just created
	if (dialog) {
		// tell the dialog contents that it was resized
		dialog.closest('.ui-dialog').on('resizestop dialogextendmaximize dialogextendrestore', function(){
			var frame = $('iframe', dialog)[0],
				win = frame.contentWindow || frame.contentDocument;
			win.resizeSequenceCanvas(dialog.width() - 10, dialog.height() - 10);
		});
		dialog.dialog('open');
	}
}


function showAddLessonDialog(orgID) {
	showDialog("dialogAddLesson", {
		'orgID' : orgID,
		'autoOpen' : true,
		'modal' : true,
		'draggable' : false,
		'dialogClass' : 'tabbedDialog addLessonDialog',
		'height' : 600,
		'width' : 800,
		'open' : function() {
			// load contents after opening the dialog
			$('iframe', this)
					.attr('src', LAMS_URL
						+ 'home.do?method=addLesson&organisationID='
						+ $(this).dialog('option', 'orgID'));
			
			//in case of mobile devices allow iframe scrolling
			if( /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent) ) {
			    setTimeout(function() {
			    	 $('.dialogContainer').css('overflow-y','scroll');
			        $('.dialogContainer').css('-webkit-overflow-scrolling','touch');
			    },500);
			}
		}
	});
}


function showOrgGroupDialog(orgID) {
	showDialog("dialogOrgGroup", {
		'orgID' : orgID,
		'modal' : true,
		'height' : 460,
		'width' : 460,
		'title' : LABELS.COURSE_GROUPS_TITLE,
		'open' : function() {
			// load contents after opening the dialog
			$('iframe', this)
					.attr('src', LAMS_URL
						+ 'OrganisationGroup.do?method=viewGroupings&organisationID='
						+ $(this).dialog('option', 'orgID'));
		}
	}, true);
}

function showAddSingleActivityLessonDialog(orgID, toolID, learningLibraryID) {
	showDialog("dialogAddSingleActivityLesson", {
		'orgID' : orgID,
		'toolID' : toolID,
		'learningLibraryID' : learningLibraryID,
		'height' : 600,
		'width' : 850,
		'modal' : true,
		'title' : LABELS.SINGLE_ACTIVITY_LESSON_TITLE,
		'open' : function() {
			var dialog = $(this),
				toolID = dialog.dialog('option', 'toolID');
			$.ajax({
				async : false,
				cache : false,
				url : LAMS_URL + "authoring/author.do",
				dataType : 'json',
				data : {
					'method' : 'createToolContent',
					'toolID' : toolID
				},
				success : function(response) {
					dialog.dialog('option', {
						'toolContentID' :  response.toolContentID,
						'contentFolderID' : response.contentFolderID
					});

					$('iframe', dialog).load(function(){
						if ($(this).contents().find('span.editForm').length > 0){
							closeAddSingleActivityLessonDialog('save');
						}
					})
					.attr('src', response.authorURL + '&notifyCloseURL='
						+ encodeURIComponent(LAMS_URL
						+ 'dialogCloser.jsp?function=closeAddSingleActivityLessonDialog&noopener=true'));
				}
			});
		}
	}, true);
}


function showNotificationsDialog(orgID, lessonID) {
	var id = "dialogNotifications" + (lessonID ? "Lesson" + lessonID : "Org" + orgID);
	showDialog(id, {
		'orgID' : orgID,
		'lessonID' : lessonID,
		'height' : 600,
		'width' : 850,
		'title' : LABELS.EMAIL_NOTIFICATIONS_TITLE,
		'open' : function() {
			var dialog = $(this),
				lessonID = dialog.dialog('option', 'lessonID');
			// if lesson ID is given, use lesson view; otherwise
			// use course view
			if (lessonID) {
				// load contents after opening the dialog
				$('iframe', dialog).attr('src', LAMS_URL
					+ 'monitoring/emailNotifications.do?method=getLessonView&lessonID='
					+ lessonID);
			} else {
				var orgID = dialog.dialog('option', 'orgID');
				$('iframe', dialog).attr('src', LAMS_URL
					+ 'monitoring/emailNotifications.do?method=getCourseView&organisationID='
					+ orgID);
			}
		}
	}, true);
}

function showGradebookCourseDialog(orgID){
	var id = "dialoGradebookCourse" + orgID;
	showDialog(id, {
		'orgID' : orgID,
		'height' : 650,
		'width' : 850,
		'title' : LABELS.GRADEBOOK_COURSE_TITLE,
		'open' : function() {
			var orgID = $(this).dialog('option', 'orgID');
			// load contents after opening the dialog
			$('iframe', this).attr('src', LAMS_URL
				+ 'gradebook/gradebookMonitoring.do?dispatch=courseMonitor&organisationID=' + orgID);
		}
	}, true);
}

function showGradebookLessonDialog(lessonID){
	var id = "dialogGradebookLesson" + lessonID;
	showDialog(id, {
		'lessonID' : lessonID,
		'height' : 650,
		'width' : 850,
		'title' : LABELS.GRADEBOOK_LESSON_TITLE,
		'open' : function() {
			var lessonID = $(this).dialog('option', 'lessonID');
			// load contents after opening the dialog
			$('iframe', this).attr('src', LAMS_URL
				+ 'gradebook/gradebookMonitoring.do?lessonID=' + lessonID);
		}
	}, true);
}

function showGradebookLearnerDialog(orgID){
	var id = "dialoGradebookLearner" + orgID;
	showDialog(id, {
		'orgID' : orgID,
		'height' : 400,
		'width' : 750,
		'title' : LABELS.GRADEBOOK_LEARNER_TITLE,
		'open' : function() {
			var orgID = $(this).dialog('option', 'orgID');
			// load contents after opening the dialog
			$('iframe', this).attr('src', LAMS_URL
				+ 'gradebook/gradebookLearning.do?dispatch=courseLearner&organisationID=' + orgID);
		}
	}, true);
}

function showConditionsDialog(lessonID){
	var id = "dialogConditions" + lessonID;
	showDialog(id, {
		'lessonID' : lessonID,
		'height' : 450,
		'width' : 610,
		'title' : LABELS.CONDITIONS_TITLE,
		'open' : function() {
			var lessonID = $(this).dialog('option', 'lessonID');
			// load contents after opening the dialog
			$('iframe', this).attr('src', LAMS_URL
				+ 'lessonConditions.do?method=getIndexLessonConditions&lsId=' + lessonID);
		}
	}, true);
}

function showSearchLessonDialog(orgID){
	var id = "dialogSearchLesson" + orgID;
	showDialog(id, {
		'orgID' : orgID,
		'height' : 400,
		'width' : 600,
		'title' : LABELS.SEARCH_LESSON_TITLE,
		'open' : function() {
			var orgID = $(this).dialog('option', 'orgID');
			// load contents after opening the dialog
			$('iframe', this).attr('src', LAMS_URL
				+ 'findUserLessons.do?dispatch=getResults&courseID=' + orgID);
		}
	}, true);
}


function showFlashlessAuthoringDialog(){
	showDialog('dialogFlashlessAuthoring', {
		'height' : $(window).height() - 10,
		'width' : 1280,
		'title' : 'Authoring',
		'beforeClose' : function(){
			// if LD was modified, ask the user if he really wants to exit
			var canClose = $('iframe', this)[0].contentWindow.GeneralLib.canClose();
			if (canClose || confirm(LABELS.NAVIGATE_AWAY_CONFIRM)) {
				$('iframe', this).attr('src', null);
			} else {
				return false;
			}
		},
		'open' : function() {
			var orgID = $(this).dialog('option', 'orgID');
			// load contents after opening the dialog
			$('iframe', this).attr('src', LAMS_URL + 'authoring/author.do?method=openAuthoring');
		}
	}, true);
}


function closeAddSingleActivityLessonDialog(action) {
	var id = 'dialogAddSingleActivityLesson',
		dialog = $('#' + id),
		save = action == 'save';
	
	if (save) {
		$.ajax({
			async : false,
			cache : false,
			url : LAMS_URL + "authoring/author.do",
			dataType : 'text',
			data : {
				'method' : 'createSingleActivityLesson',
				'organisationID'  : dialog.dialog('option', 'orgID'),
				'toolID' : dialog.dialog('option', 'toolID'),
				'toolContentID' : dialog.dialog('option', 'toolContentID'),
				'contentFolderID' : dialog.dialog('option', 'contentFolderID'),
				'learningLibraryID' : dialog.dialog('option', 'learningLibraryID')
			}
		});
	}
	closeDialog(id, save);
}


function closeDialog(id, refresh) {
	// was the dialog just closed or a lesson removed
	// if latter, refresh the list
	if (refresh) {
		loadOrgTab(null, true);
	}
	$("#" + id).dialog('close');
}


/**
 * Loads contents to already open organisation groups dialog.
 */
function loadOrgGroupDialogContents(title, width, height, url) {
	var dialog = $('#dialogOrgGroup');
	if (title) {
		dialog.dialog('option', 'title', title);
	}
	if (width && height) {
		dialog.dialog('option', {
			'width'    : width,
			'height'   : height,
		}).dialog('option', 'position', 'center');
	}
	if (url) {
		$('iframe', dialog).contents().find("body").html('');
		$('iframe', dialog).attr('src', url);
		$('div.ui-dialog-titlebar .customDialogButton').remove();
	}
}

/**
 * Called from within Course Groups dialog, it saves groups and loads grouping page.
 */
function saveOrgGroups() {
	var groupsSaved = $('#dialogOrgGroup iframe')[0].contentWindow.saveGroups();
	if (groupsSaved) {
		loadOrgGroupDialogContents(null, 460, 460,
			LAMS_URL + 'OrganisationGroup.do?method=viewGroupings&organisationID='
			         + $('#dialogOrgGroup').dialog('option', 'orgID'));
	}
}

function removeLesson(lessonID) {
	if (confirm(LABELS.REMOVE_LESSON_CONFIRM1)) {
		if (confirm(LABELS.REMOVE_LESSON_CONFIRM2)) {
			$.ajax({
				async : false,
				url : LAMS_URL + "monitoring/monitoring.do",
				data : "method=removeLesson&lessonID=" + lessonID,
				type : "post",
				success : function(json) {
					if (json.removeLesson == true) {
						loadOrgTab(null, true);
					} else {
						alert(json.removeLesson);
					}
				}
			});
		}
	}
}