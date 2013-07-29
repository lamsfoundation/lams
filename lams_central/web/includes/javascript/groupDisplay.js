function initMainPage() {
	$('#orgTabs').tabs({
		'activate' : function(event, ui){
			loadOrgTab(ui.newPanel);
		}
	}).addClass('ui-tabs-vertical ui-helper-clearfix')
	  .find('li').removeClass('ui-corner-top').addClass('ui-corner-left');
	
	loadOrgTab($('.orgTab').first());
	
	$("#actionAccord").accordion({
		'heightStyle' : 'content'
	});
	
	$(".split-menu-button li em")
			.live(
					"click",
					function(event) {
						var hidden = $(this).parents("li").children("ul").is(
								":hidden");
						$(this).parents("li").children("ul").hide();
						$(this).parents("li").children("a").removeClass(
								"zoneCur");

						if (hidden) {
							$(this).parents("li").children("ul").toggle()
									.parents("li").children("a").addClass(
											"zoneCur");
						}

						event.stopPropagation();
					});

	$("html").click(function() {
		$(".split-menu-button>ul>li>ul").hide();
	});

	// initialise lesson dialog
	$('#addLessonDialog').dialog(
		{
			'autoOpen' : false,
			'height' : 600,
			'width' : 800,
			'modal' : true,
			'resizable' : false,
			'hide' : 'fold',
			'open' : function() {
				// load contents after opening the dialog
				$('#addLessonFrame')
						.attr(
								'src',
								LAMS_URL
										+ 'home.do?method=addLesson&organisationID='
										+ $(this).dialog('option',
												'orgID'));
			},
			'close' : function() {
				// refresh if lesson was added
				if ($(this).dialog('option', 'refresh')) {
					loadOrgTab(null, true);
				}
			}
		// tabs are the title bar, so remove dialog's one
		}).closest('.ui-dialog').children('.ui-dialog-titlebar')
		  .remove();
	
	// initialise single activity lesson dialog
	$('#addSingleActivityLessonFrame').load(function(){
		if ($(this).contents().find('span.editForm').length > 0){
			closeAddSingleActivityLessonDialog('save');
		}
	});
	$('#addSingleActivityLessonDialog').dialog(
		{
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
						dialog.dialog('option', 'toolContentID', response.toolContentID);
						dialog.dialog('option', 'contentFolderID', response.contentFolderID);
						$('#addSingleActivityLessonFrame').attr('src',
								response.authorURL + '&notifyCloseURL='
								+ encodeURIComponent(LAMS_URL
										+ 'dialogCloser.jsp?function=closeAddSingleActivityLessonDialog&noopener=true'));
					}
				});
			},
			'close' : function() {
				$('#addSingleActivityLessonFrame').attr('src', null);
				// refresh if lesson was added
				if ($(this).dialog('option', 'refresh')) {
					loadOrgTab(null, true);
				}
			}
		});
	
	// initialise monitor dialog
	$('#monitorDialog').dialog(
		{
			'autoOpen' : false,
			'height' : 600,
			'width' : 800,
			'modal' : true,
			'resizable' : false,
			'hide' : 'fold',
			'open' : function() {
				// load contents after opening the dialog
				$('#monitorFrame')
						.attr(
								'src',
								LAMS_URL
										+ 'monitoring/monitoring.do?method=monitorLesson&lessonID='
										+ $(this).dialog('option',
												'lessonID'));
			},
			'close' : function() {
				// refresh if lesson was added
				if ($(this).dialog('option', 'refresh')) {
					loadOrgTab(null, true);
				}
			}
		}).closest('.ui-dialog').children('.ui-dialog-titlebar')
		  .remove();

	// initialise notifications dialog
	$('#notificationsDialog').dialog(
		{
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
					$('#notificationsFrame')
							.attr(
									'src',
									LAMS_URL
											+ 'monitoring/emailNotifications.do?method=getLessonView&lessonID='
											+ lessonID);
				} else {
					var orgID = $(this).dialog('option', 'orgID');
					$('#notificationsFrame')
							.attr(
									'src',
									LAMS_URL
											+ 'monitoring/emailNotifications.do?method=getCourseView&organisationID='
											+ orgID);
				}
			},
			'close' : function() {
				$('#notificationsFrame').attr('src', null);
			}
		});
}

function loadOrgTab(orgTab, refresh) {
	if (!orgTab) {
		orgTab = $('div[id^=orgTab-' + $('#orgTabs').tabs('option','active') + ']');
	}
	if (refresh || !orgTab.text()) {
		var orgId = orgTab.attr("id").split('-')[2];
		
		orgTab.load(
			"displayGroup.do",
			{
				display : 'group',
				stateId : stateId,
				orgId   : orgId
			},
			function() {
				$('.mycourses-left-buttons', orgTab).remove();
				toggleGroupContents(orgTab, stateId);
				$("a[class*='thickbox']", orgTab).each(function() {
					tb_init(this);
				});
				initMoreActions(orgTab);
			});
	}
}


function initLoadGroup(element, stateId, display) {
	jQuery(element).load(
			"displayGroup.do",
			{
				display : display,
				stateId : stateId,
				orgId : jQuery(element).attr("id")
			},
			function() {
				if (display == 'header') {
					jQuery("span.j-group-icon", element).html(
							"<img src='images/tree_closed.gif'/>");
				} else if (display == 'group') {
					jQuery("span.j-group-icon", element).html(
							"<img src='images/tree_open.gif'/>");
				}
				toggleGroupContents(element, stateId);
				jQuery(element).css("display", "block");
				jQuery("a[class*='thickbox']", element).each(function() {
					tb_init(this);
				});
				initMoreActions(element);
			});
}

function toggleGroupContents(element, stateId) {
	jQuery("a.j-group-header, span.j-group-icon", element).click(function() {
		var row = jQuery("div.row", element);
		var courseBg = jQuery(row).parent("div.course-bg");
		var orgId = jQuery(courseBg).attr("id");
		var course = jQuery(row).nextAll("div.j-course-contents");
		var groupIcon = jQuery("span.j-group-icon", element);
		if (jQuery(course).html() == null) {
			loadGroupContents(courseBg, stateId);
			saveCollapsed(orgId, "false");
			jQuery(groupIcon).html("<img src='images/tree_open.gif'/>");
		} else {
			var display = course.css("display");
			if (jQuery.browser.msie && jQuery.browser.version == '6.0') {
				course.slideToggle("fast");
			} else {
				course.toggle("fast");
			}
			if (display == "none") {
				saveCollapsed(orgId, "false");
				jQuery(groupIcon).html("<img src='images/tree_open.gif'/>");
			} else if (display == "block") {
				saveCollapsed(orgId, "true");
				jQuery(groupIcon).html("<img src='images/tree_closed.gif'/>");
			}
		}
	});
}

function saveCollapsed(orgId, collapsed) {
	jQuery.ajax({
		url : "servlet/updateCollapsedGroup",
		data : {
			orgId : orgId,
			collapsed : collapsed
		}
	});
}

function loadGroupContents(courseBg, stateId) {
	var orgId = jQuery(courseBg).attr("id");
	jQuery.ajax({
		url : "displayGroup.do",
		data : {
			orgId : orgId,
			stateId : stateId,
			display : 'contents'
		},
		success : function(html) {
			jQuery(courseBg).append(html);
			// unregister and re-register thickbox for this
			// group in order to avoid double
			// registration of thickbox for existing elements
			// (i.e. group 'add lesson' link)
			$('a.thickbox' + jQuery(courseBg).attr("id")).unbind("click");
			tb_init('a.thickbox' + jQuery(courseBg).attr("id"));
		}
	});
}

function initMoreActions(element) {

	var id = jQuery(element).attr("id");
	var menuSelector = "a#more-actions-button-" + id;
	var ulSelector = "ul#more-actions-list-" + id;

	$(menuSelector).click(
			function() {
				// slide up all other menus
				$("ul[id^=more-actions-list-]:visible:not(" + ulSelector + ")")
						.slideUp("fast");

				// show this menu
				$(ulSelector).css("top",
						$(this).position().top + this.offsetHeight);
				$(ulSelector).css("left", $(this).position().left);
				$(ulSelector).slideToggle("fast");
				return false;
			});

	$(window).resize(
			function() {
				if ($(menuSelector).length == 0)
					return;

				$(ulSelector).css(
						"top",
						$(menuSelector).position().top
								+ $(menuSelector).offsetHeight);
				$(ulSelector).css("left", $(menuSelector).position().left);
			});

}

function makeOrgSortable(orgId) {
	var org = jQuery("div.course-bg#" + orgId);
	if (jQuery("div.j-lessons", org).size() > 0) {
		var jLessons = jQuery("div.j-lessons#" + orgId + "-lessons");
		var jLessonsTable = jQuery("table.lesson-table tbody", jLessons);
		makeSortable(jLessonsTable);
		jQuery("div.j-subgroup-lessons>table.lesson-table tbody", org).each(
				function() {
					makeSortable(jQuery(this));
				});
		jQuery("div.mycourses-right-buttons", jLessons).html(
				"<a class=\"sorting\" onclick=\"makeOrgUnsortable(" + orgId
						+ ")\" title=\"" + LABELS.SORTING_DISABLE
						+ "\"><img src=\"images/sorting_enabled.gif\"></a>");
	}
}

function makeOrgUnsortable(orgId) {
	var org = jQuery("div.course-bg#" + orgId);
	var jLessons = jQuery("div.j-lessons#" + orgId + "-lessons");
	var jLessonsTable = jQuery("table.lesson-table tbody", jLessons);
	jLessonsTable.sortable("destroy");
	jQuery("div.j-subgroup-lessons>table.lesson-table tbody", org).each(
			function() {
				jQuery(this).sortable("destroy");
			});
	jQuery("div.mycourses-right-buttons", jLessons).html(
			"<a class=\"sorting\" onclick=\"makeOrgSortable(" + orgId
					+ ")\" title=\"" + LABELS.SORTING_ENABLE
					+ "\"><img src=\"images/sorting_disabled.gif\"></a>");
}

function makeSortable(element) {
	jQuery(element).sortable(
			{
				axis : "y",
				delay : 100,
				tolerance : 'pointer',
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
					var ids = jQuery(this).sortable('toArray');

					var jLessonsId = jQuery(this).parents(
							"div[class$='lessons']").attr("id");
					var dashIndex = jLessonsId.indexOf("-");
					var orgId = (dashIndex > 0 ? jLessonsId.substring(0,
							dashIndex) : jLessonsId);

					jQuery.ajax({
						url : "servlet/saveLessonOrder",
						data : {
							orgId : orgId,
							ids : ids.join(",")
						},
						error : function(a, b) {
							refresh();
						}
					});
				}
			}).disableSelection();
}

function showMonitorLessonDialog(lessonID) {
	$("#monitorDialog").dialog('option', 'lessonID', lessonID).dialog('open');
}

function showAddLessonDialog(orgID) {
	$("#addLessonDialog").dialog('option', 'orgID', orgID).dialog('open');
}

function showAddSingleActivityLessonDialog(orgID, toolID) {
	$("#addSingleActivityLessonDialog").dialog('option', {
		'orgID' : orgID,
		'toolID' : toolID
	}).dialog('open');
}

function showNotificationsDialog(organisationID, lessonID) {
	$("#notificationsDialog").dialog('option', {
		'orgID' : organisationID,
		'lessonID' : lessonID
	}).dialog('open');
}

function closeAddLessonDialog(refresh) {
	$('#addLessonFrame').attr('src', null);
	// was the dialog just closed or a new lesson really added?
	// if latter, refresh the list
	$("#addLessonDialog").dialog('option', 'refresh', refresh ? true : false)
			.dialog('close');
}

function closeAddSingleActivityLessonDialog(action) {
	$('#addSingleActivityLessonFrame').attr('src', null);
	var dialog = $('#addSingleActivityLessonDialog');
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

function closeMonitorLessonDialog(refresh) {
	$('#monitorFrame').attr('src', null);
	// was the dialog just closed or a new lesson really added?
	// if latter, refresh the list
	$("#monitorDialog").dialog('option', 'refresh', refresh ? true : false)
			.dialog('close');
}

function removeLesson(lessonID) {
	if (confirm(LABELS.REMOVE_LESSON_CONFIRM1)) {
		if (confirm(LABELS.REMOVE_LESSON_CONFIRM2)) {
			$.ajax({
				async : false,
				url : LAMS_URL + "monitoring/monitoring.do",
				data : "method=removeLessonJson&lessonID=" + lessonID,
				type : "post",
				success : function(json) {
					if (json.removeLesson == "true") {
						refresh();
					} else {
						alert(json.removeLesson);
					}
				}
			});
		}
	}
}

function refresh() {
	document.location.reload();
}

function closeWizard() {
	setTimeout(refresh, 1000);
	tb_remove();
}