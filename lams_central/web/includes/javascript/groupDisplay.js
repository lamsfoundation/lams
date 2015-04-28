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
	$(element).sortable(
			{
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
	var dialogName = "dialogMonitorLesson" + lessonID,
		dialog = $('#' + dialogName);
	if (dialog.length > 0) {
		// is it open already?
		dialog.dialog('moveToTop');
		return;
	}
	
	// create a new dialog by cloning a template
	dialog = $('#dialogContainer').clone();
	dialog.attr('id', dialogName);
	$('iframe', dialog).attr('id', null);
	
	dialog.dialog({
		'lessonID' : lessonID,
		'autoOpen' : false,
		'height' : 600,
		'width' : 1024,
		'modal' : false,
		'draggable' : false,
		'resizable' : true,
		'hide' : 'fold',
		'open' : function() {
			// load contents after opening the dialog
			$('iframe', this).attr('src', LAMS_URL
				+ 'home.do?method=monitorLesson&lessonID='
				+ $(this).dialog('option', 'lessonID'));
		},
		'beforeClose' : function(){
			$('iframe', this).attr('src', null);
		},
		'close' : function() {
			// completely delete the dialog
			$(this).remove();
		}
	});
	// tell the dialog contents that it was resized
	dialog.closest('.ui-dialog').on('resizestop', function(event, ui){
		var frame = $('iframe', dialog)[0],
			win = frame.contentWindow || frame.contentDocument;
		win.resizeSequenceCanvas(ui.size.width, ui.size.height);
	});
	// tabs are the title bar, so remove dialog's one
	dialog.closest('.ui-dialog').children('.ui-dialog-titlebar').remove();
	dialog.dialog('open');
}


function moveDialogToTop(dialogName) {
	// called from withing dialog's iframe
	$('#' + dialogName).dialog('moveToTop');
}


function showAddLessonDialog(orgID) {
	var dialog = $('#dialogContainer').dialog({
		'orgID' : orgID,
		'autoOpen' : false,
		'height' : 600,
		'width' : 800,
		'modal' : true,
		'resizable' : false,
		'hide' : 'fold',
		'open' : function() {
			// load contents after opening the dialog
			$('#dialogFrame')
					.attr('src', LAMS_URL
						+ 'home.do?method=addLesson&organisationID='
						+ $(this).dialog('option', 'orgID'));
		},
		'beforeClose' : function(){
			$('#dialogFrame').attr('src', null);
		},
		'close' : function() {
			// refresh if lesson was added
			if ($(this).dialog('option', 'refresh')) {
				loadOrgTab(null, true);
			}
			$(this).dialog('destroy');
		}
	});
	// tabs are the title bar, so remove dialog's one
	dialog.closest('.ui-dialog').children('.ui-dialog-titlebar').remove();
	dialog.dialog('open');
}

function showOrgGroupDialog(orgID) {
	var dialog = $('#dialogContainer').dialog({
		'orgID' : orgID,
		'autoOpen' : false,
		'height' : 460,
		'width' : 460,
		'modal' : true,
		'resizable' : false,
		'hide' : 'fold',
		'title' : LABELS.COURSE_GROUPS_TITLE,
		'open' : function() {
			// load contents after opening the dialog
			$('#dialogFrame')
					.attr('src', LAMS_URL
						+ 'OrganisationGroup.do?method=viewGroupings&organisationID='
						+ $(this).dialog('option', 'orgID'));
		},
		'beforeClose' : function(){
			$('#dialogFrame').attr('src', null);
		},
		'close' : function() {
			$(this).dialog('destroy');
		}
	}).dialog('open');
}

function showAddSingleActivityLessonDialog(orgID, toolID) {
	$('#dialogContainer').dialog({
		'orgID' : orgID,
		'toolID' : toolID,
		'autoOpen' : false,
		'height' : 600,
		'width' : 850,
		'modal' : true,
		'resizable' : false,
		'hide' : 'fold',
		'title' : LABELS.SINGLE_ACTIVITY_LESSON_TITLE,
		'open' : function() {
			var dialog = $(this);
			var toolID = dialog.dialog('option', 'toolID');
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

					$('#dialogFrame').load(function(){
						if ($(this).contents().find('span.editForm').length > 0){
							closeAddSingleActivityLessonDialog('save');
						}
					})
					.attr('src', response.authorURL + '&notifyCloseURL='
						+ encodeURIComponent(LAMS_URL
						+ 'dialogCloser.jsp?function=closeAddSingleActivityLessonDialog&noopener=true'));
				}
			});
		},
		'beforeClose' : function(){
			$('#dialogFrame').off('load').attr('src', null);
		},
		'close' : function() {
			$('#dialogFrame').off('load').attr('src', null);
			// refresh if lesson was added
			if ($(this).dialog('option', 'refresh')) {
				loadOrgTab(null, true);
			}
			$(this).dialog('destroy');
		}
	}).dialog('open');
}

function showNotificationsDialog(orgID, lessonID) {
	$('#notificationsDialogContainer').dialog({
		'orgID' : orgID,
		'lessonID' : lessonID,
		'autoOpen' : false,
		'height' : 600,
		'width' : 850,
		'modal' : true,
		'resizable' : false,
		'hide' : 'fold',
		'title' : LABELS.EMAIL_NOTIFICATIONS_TITLE,
		'open' : function() {
			var lessonID = $(this).dialog('option', 'lessonID');
			// if lesson ID is given, use lesson view; otherwise
			// use course view
			if (lessonID) {
				// load contents after opening the dialog
				$('#notificationsDialogFrame').attr('src', LAMS_URL
					+ 'monitoring/emailNotifications.do?method=getLessonView&lessonID='
					+ lessonID);
			} else {
				var orgID = $(this).dialog('option', 'orgID');
				$('#notificationsDialogFrame').attr('src', LAMS_URL
					+ 'monitoring/emailNotifications.do?method=getCourseView&organisationID='
					+ orgID);
			}
		},
		'beforeClose' : function(){
			$('#notificationsDialogFrame').attr('src', null);
		},
		'close' : function() {
			$(this).dialog('destroy');
		}
	}).dialog('open');
}

function showGradebookCourseDialog(orgID){
	$('#dialogContainer').dialog({
		'orgID' : orgID,
		'autoOpen' : false,
		'height' : 650,
		'width' : 850,
		'modal' : true,
		'resizable' : false,
		'hide' : 'fold',
		'title' : LABELS.GRADEBOOK_COURSE_TITLE,
		'open' : function() {
			var orgID = $(this).dialog('option', 'orgID');
			// load contents after opening the dialog
			$('#dialogFrame').attr('src', LAMS_URL
				+ 'gradebook/gradebookMonitoring.do?dispatch=courseMonitor&organisationID=' + orgID);
		},
		'beforeClose' : function(){
			$('#dialogFrame').attr('src', null);
		},
		'close' : function() {
			$(this).dialog('destroy');
		}
	}).dialog('open');
}

function showGradebookLessonDialog(lessonID){
	$('#dialogContainer').dialog({
		'lessonID' : lessonID,
		'autoOpen' : false,
		'height' : 650,
		'width' : 850,
		'modal' : true,
		'resizable' : false,
		'hide' : 'fold',
		'title' : LABELS.GRADEBOOK_LESSON_TITLE,
		'open' : function() {
			var lessonID = $(this).dialog('option', 'lessonID');
			// load contents after opening the dialog
			$('#dialogFrame').attr('src', LAMS_URL
				+ 'gradebook/gradebookMonitoring.do?lessonID=' + lessonID);
		},
		'beforeClose' : function(){
			$('#dialogFrame').attr('src', null);
		},
		'close' : function() {
			$(this).dialog('destroy');
		}
	}).dialog('open');
}

function showGradebookLearnerDialog(orgID){
	$('#dialogContainer').dialog({
		'orgID' : orgID,
		'autoOpen' : false,
		'height' : 400,
		'width' : 750,
		'modal' : true,
		'resizable' : false,
		'hide' : 'fold',
		'title' : LABELS.GRADEBOOK_LEARNER_TITLE,
		'open' : function() {
			var orgID = $(this).dialog('option', 'orgID');
			// load contents after opening the dialog
			$('#dialogFrame').attr('src', LAMS_URL
				+ 'gradebook/gradebookLearning.do?dispatch=courseLearner&organisationID=' + orgID);
		},
		'beforeClose' : function(){
			$('#dialogFrame').attr('src', null);
		},
		'close' : function() {
			$(this).dialog('destroy');
		}
	}).dialog('open');
}

function showConditionsDialog(lessonID){
	$('#dialogContainer').dialog({
		'lessonID' : lessonID,
		'autoOpen' : false,
		'height' : 450,
		'width' : 610,
		'modal' : true,
		'resizable' : false,
		'hide' : 'fold',
		'title' : LABELS.CONDITIONS_TITLE,
		'open' : function() {
			var lessonID = $(this).dialog('option', 'lessonID');
			// load contents after opening the dialog
			$('#dialogFrame').attr('src', LAMS_URL
				+ 'lessonConditions.do?method=getIndexLessonConditions&lsId=' + lessonID);
		},
		'beforeClose' : function(){
			$('#dialogFrame').attr('src', null);
		},
		'close' : function() {
			$(this).dialog('destroy');
		}
	}).dialog('open');
}

function showSearchLessonDialog(orgID){
	$('#dialogContainer').dialog({
		'orgID' : orgID,
		'autoOpen' : false,
		'height' : 400,
		'width' : 600,
		'modal' : true,
		'resizable' : false,
		'hide' : 'fold',
		'title' : LABELS.SEARCH_LESSON_TITLE,
		'open' : function() {
			var orgID = $(this).dialog('option', 'orgID');
			// load contents after opening the dialog
			$('#dialogFrame').attr('src', LAMS_URL
				+ 'findUserLessons.do?dispatch=getResults&courseID=' + orgID);
		},
		'beforeClose' : function(){
			$('#dialogFrame').attr('src', null);
		},
		'close' : function() {
			$(this).dialog('destroy');
		}
	}).dialog('open');
}

function closeAddLessonDialog(refresh) {
	// was the dialog just closed or a new lesson really added?
	// if latter, refresh the list
	$("#dialogContainer").dialog('option', 'refresh', refresh ? true : false)
			.dialog('close');
}

function closeAddSingleActivityLessonDialog(action) {
	var dialog = $('#dialogContainer');
	var save = action == 'save';
	
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
				'contentFolderID' : dialog.dialog('option', 'contentFolderID')
			}
		});
	}
	dialog.dialog('option', 'refresh', save).dialog('close');
}

function closeMonitorLessonDialog(dialogName, refresh) {
	// was the dialog just closed or 
	// if latter, refresh the list
	$("#" + dialogName).dialog('option', 'refresh', refresh ? true : false)
			.dialog('close');
}

/**
 * Loads contents to already open dialog.
 */
function loadDialogContents(title, width, height, url) {
	if (title) {
		$("#dialogContainer").dialog('option', 'title', title);
	}
	if (width && height) {
		$("#dialogContainer").dialog('option', {
			'width'    : width,
			'height'   : height,
		}).dialog('option', 'position', 'center');
	}
	if (url) {
		$('#dialogFrame').contents().find("body").html('');
		$('#dialogFrame').attr('src', url);
		$('div.ui-dialog-titlebar .customDialogButton').remove();
	}
}

/**
 * Called from within Course Groups dialog, it saves groups and loads grouping page.
 */
function saveOrgGroups() {
	var groupsSaved = document.getElementById('dialogFrame').contentWindow.saveGroups();
	if (groupsSaved) {
		loadDialogContents(null, 460, 460,
			LAMS_URL + 'OrganisationGroup.do?method=viewGroupings&organisationID='
			         + $('#dialogContainer').dialog('option', 'orgID'));
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