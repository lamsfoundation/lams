$(document).ready(function () {
	
	// Open active course. If it's not yet chosen by user - it will be opened once tablesorter's pager receives all orgs
	if (activeOrgId != null) {
		loadOrganisation();
	}
	
	refreshPrivateNotificationCount();
	
	var $tablesorter = $(".tablesorter").tablesorter({
		theme: 'bootstrap',
		widthFixed: true,
		headerTemplate : '{content} {icon}',
		widgets: ["filter"],
	    widgetOptions : {
	        filter_columnFilters: false
	    },
	    widthFixed: true,
	    sortInitialOrder: 'desc',
        sortList: [[1]],
        headers: { 0: { sorter: false, filter: false} }
	});

	$.tablesorter.filter.bindSearch($tablesorter, $('#offcanvas-search-input') );

	$(".tablesorter").each(function() {
		$(this).tablesorterPager({
			savePages: false,
            container: $(this).find(".ts-pager"),
            output: '{startRow} to {endRow} ({totalRows})',
            cssPageDisplay: '.pagedisplay',
            cssPageSize: '.pagesize',
            cssDisabled: 'disabled',
            ajaxUrl : LAMS_URL + "/index/getOrgs.do?page={page}&size={size}&{sortList:column}&{filterList:fcol}",
			ajaxProcessing: function (data, table) {
		    	if (data && data.hasOwnProperty('rows')) {
		    		var rows = [],
		            json = {};
		    		
					for (i = 0; i < data.rows.length; i++){
						var orgData = data.rows[i];
						var orgId = orgData["id"];
						
						rows += '<tr>';

						rows += '<td style="display: none;">';
						rows += '</td>';
						
						rows += '<td id="org-row-' + orgId +'"';
						if (activeOrgId == orgId) {
							rows += ' class="active"';
						}
						rows += '>';
						
						rows += 	'<a data-id="' + orgId + '" href="#" onClick="javascript:selectOrganisation(' + orgId + ')">';
						rows += 		orgData["name"];						
						if (activeOrgId == orgId) {
							rows +=     '<i class="fa fa-chevron-circle-right fa-lg pull-right mr-n1"></i>';
						}
						rows += 	'</a>';
						rows += '</td>';
						
						rows += '</tr>';
					}
		            
					json.total = data.total_rows;
					json.rows = $(rows);
					return json;
		    	}
			}
		})
	});

	$('.tablesorter').bind('pagerComplete', function(event, data){
		
		//hide pager if total amount of courses is less than 10
		if (data.totalRows < 10) {
			$(".tablesorter-pager").hide();
    	} else {
    		$(".tablesorter-pager").show();
	    }

		//in case active course is not yet chosen by user, select the fist one from the list
	    if ((activeOrgId == null) && (data.totalRows > 0)) {
	    	var firstOrgId = $('.tablesorter a').first().data("id");
			selectOrganisation(firstOrgId);
		}
	});

    // handler for offcanvas bar minimize button
    $('.offcanvas-toggle').on('click', function () {
        $("body").toggleClass("offcanvas-hidden");
    });
    function hideOffcanvasBar() {
    	$("body").removeClass("offcanvas-hidden");
    }

    //make offcanvas bar scrollable
    $(window).bind("load", function () {
    	$('.offcanvas-scroll-area').slimScroll({
           height: '100%',
           railOpacity: 0.9
        });
    });
    
	//collapse subcourse. (Doing it manually instead of using bootstrap collapse in order to prevent bootstrap choppiness)
    $(document).on('click', '.subcourse-title', function() { 
		var orgId = $(this).data("groupid");
		var collapsed = $("#" + orgId + "-lessons").hasClass("show");
		
		$("#" + orgId + "-lessons").toggleClass("show");
		$(this).toggleClass("collapsed");
		$("i", $(this)).toggleClass("fa-minus-square-o fa-plus-square-o");

		//store course collapse in DB
		$.ajax({
			url: LAMS_URL + "/collapseOrganisation.do",
			data: {
				orgId: orgId, 
				collapsed: collapsed
			}
		});
    });

});


function selectOrganisation(newOrgId) {
	//remove active CSS class from the old org
	if (activeOrgId != null) {
		$("#org-row-" + activeOrgId + ", #favorite-li-" + activeOrgId + " a").removeClass("active");
		$("#org-row-" + activeOrgId + " a>i").remove();
	}	
	
	//add active CSS class
	$("#org-row-" + newOrgId + ", #favorite-li-" + newOrgId + " a").addClass("active");
	$("#org-row-" + newOrgId + " a").append( "<i class='fa fa-chevron-circle-right fa-lg pull-right mr-n1'></i>" )
	
	activeOrgId = newOrgId;
	loadOrganisation();
	
	//store last visited organisation
	$.ajax({
		url : "index/storeLastVisitedOrganisation.do",
		data : {
			orgId   : activeOrgId
		}
	});
}


function loadOrganisation() {
	$("#page-wrapper").finish().fadeOut(200, function() {
		$("#org-container").load(
			"displayGroup.do",
			{
				orgId   : activeOrgId
			},
			function( response, status, xhr ) {
				//in case of any server error - open offcanvas bar so user can select another course
				if ( status == "error" ) {
					$("body").removeClass("offcanvas-hidden");
					return;
				}
					
				initDataTables();
					
				// if screen is smaller than 768px (i.e. offcanvas occupies 100%) and offcanvas is shown - then hide it on user selecting an organisation
				if (window.matchMedia('(max-width: 768px)').matches && !$("body").hasClass("offcanvas-hidden")) {
					//do it with a small delay so it will be understood that the new organisation is selected indeed
					$("body").addClass('offcanvas-hidden', 300);
				}
					
    			//in case there is no lessons-table, show page back (instead of doing it in datatables init event)
    			if ($('.lessons-table').length == 0) {
    				$("#page-wrapper").finish().fadeTo(400, 1);
    			}
			}
		);    
	});
}


function toggleFavoriteOrganisation(orgId) {
	$("#favorite-organisations-container").load(
		"index/toggleFavoriteOrganisation.do",
		{
			orgId   : orgId,
			activeOrgId : activeOrgId
		},
		function() {
			if ($("#favorite-star").hasClass("fa-star-o")) {
				$("#favorite-star").switchClass("fa-star-o", "fa-star").attr('title', LABELS.REMOVE_ORG_FAVORITE);
			} else {
				$("#favorite-star").switchClass("fa-star", "fa-star-o").attr('title', LABELS.MARK_ORG_FAVORITE);
			}
		}
	);	
}

function toggleFavoriteLesson(orgId, lessonId) {
	$.ajax({
		url : LAMS_URL + "index/toggleFavoriteLesson.do",
		dataType : 'json',
		cache : false,
		data : {
			'lessonId' : lessonId
		},
		complete : function(response) {
			var star = $("#favorite-lesson-star-" + lessonId);
			if (star.hasClass("fa-star-o")) {
				star.switchClass("fa-star-o", "fa-star").attr('title', LABELS.REMOVE_LESSON_FAVORITE);
			} else {
				star.switchClass("fa-star", "fa-star-o").attr('title', LABELS.MARK_LESSON_FAVORITE);
			}
			
			//update "favorite" column, so Stared order would work correctly
			$("#lessons-table-" + orgId).DataTable().cell( $("#favorite-lesson-td-" + lessonId) ).data("" + star.hasClass("fa-star")).draw();
		}
	});
}


function showMyProfileDialog() {
	showDialog("dialogMyProfile", {
		'title' : LABELS.MY_PROFILE,
		'modal' : true,
		'width' : Math.max(380, Math.min(770, $(window).width() - 60)),
		'height' : 430,
		'open' : function() {
			var dialog = $(this);
			// load contents after opening the dialog
			$('iframe', dialog).attr('src', LAMS_URL + 'index.do?redirect=profile');
			
			// in case of mobile devices allow iframe scrolling
			if( /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent) ) {
			    setTimeout(function() {
			    	dialog.css({
			    		'overflow-y' : 'scroll',
			    		'-webkit-overflow-scrolling' : 'touch'
			    	});
			    },500);
			}
		}
	});
}


function showMonitorLessonDialog(lessonID) {

	var id = "dialogMonitorLesson" + lessonID,
		dialog = showDialog(id, {
			'data' : {
				'isMonitorDialog' : true,
				'lessonID' : lessonID
			},
			'autoOpen' : false,
			'height': Math.max(380, Math.min(800, $(window).height() - 30)),
			'width' : Math.max(380, Math.min(1024, $(window).width() - 60)),
			'title' : LABELS.MONITORING_TITLE,
			'open' : function() {
				// load contents after opening the dialog
				$('iframe', this).attr('src', LAMS_URL
					+ 'home/monitorLesson.do?lessonID='
					+ $(this).data('lessonID'));
			},

		}, true, true);
	
	// if it was just created
	if (dialog) {
		// tell the dialog contents that it was resized
		$('.modal-content', dialog).on('resizestop', resizeSequenceCanvas);
		// initial resize
		$('iframe', dialog).on('load', resizeSequenceCanvas);
		
		dialog.modal('show');
	}
}

/**
 * Adjust the position of LD SVG in Monitoring.
 */
function resizeSequenceCanvas(){
	$('div[id^="dialogMonitorLesson"] iframe').each(function(){
		var win = this.contentWindow || this.contentDocument;
		if (win.resizeSequenceCanvas) {
			var body = $(this).closest('.modal-body');
			win.resizeSequenceCanvas(body.width(), body.height());
		}
	});
}


function showAddLessonDialog(orgID) {
	showDialog("dialogAddLesson", {
		'data' : {
			'orgID' : orgID
		},
		'height': Math.max(380, Math.min(700, $(window).height() - 30)),
		'width' : Math.max(380, Math.min(850, $(window).width() - 60)),
		'title' : LABELS.ADD_LESSON_TITLE,
		'open' : function() {
			var dialog = $(this);
			// load contents after opening the dialog
			$('iframe', dialog)
					.attr('src', LAMS_URL
						+ 'home/addLesson.do?organisationID='
						+ dialog.data('orgID'));
			
			// in case of mobile devices allow iframe scrolling
			if( /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent) ) {
			    setTimeout(function() {
			    	dialog.css({
			    		'overflow-y' : 'scroll',
			    		'-webkit-overflow-scrolling' : 'touch'
			    	});
			    },500);
			}
		}
	});
}

function showOrgGroupingDialog(orgID, activityID) {
	$('#dialogOrgGroup').modal('hide');
	showDialog("dialogOrgGrouping", {
		'width' : 460,
		'height': 460,
		'title' : LABELS.COURSE_GROUPS_TITLE,
		'open' : function() {
			// load contents after opening the dialog
			$('iframe', this).attr('src', LAMS_URL + 'organisationGroup/viewGroupings.do?organisationID=' + orgID
												   + (activityID ? '&activityID=' + activityID : ''));
		}
	}, true);
}

function showOrgGroupDialog(url) {
	$('#dialogOrgGrouping').modal('hide');
	showDialog("dialogOrgGroup", {
		'width' : Math.max(380, Math.min(960, $(window).width() - 60)),
		'height': Math.max(380, Math.min(750, $(window).height() - 30)),
		'title' : LABELS.COURSE_GROUPS_TITLE,
		'open' : function() {
			// load contents after opening the dialog
			$('iframe', this).attr('src', url);
		}
	}, true);
}

function showAddSingleActivityLessonDialog(orgID, toolID, learningLibraryID) {
	showDialog("dialogAddSingleActivityLesson", {
		'data' : {
			'orgID' : orgID,
			'toolID' : toolID,
			'learningLibraryID' : learningLibraryID
		},
		'height' : Math.max(400, $(window).height() - 30),
		'width' : Math.max(380, Math.min(850, $(window).width() - 60)),
		'title' : LABELS.SINGLE_ACTIVITY_LESSON_TITLE,
		'open' : function() {
			var dialog = $(this),
				toolID = dialog.data('toolID');
			$.ajax({
				async : false,
				cache : false,
				url : LAMS_URL + "authoring/createToolContent.do",
				dataType : 'json',
				data : {
					'toolID' : toolID
				},
				success : function(response) {
					dialog.data({
						'toolContentID' :  response.toolContentID,
						'contentFolderID' : response.contentFolderID
					});

					$('iframe', dialog).on('load', function(){
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


function showPrivateNotificationsDialog(){
	var notificationDialog = showDialog("dialogPrivateNotifications", {
		'height': Math.max(380, Math.min(600, $(window).height() - 30)),
		'width' : Math.max(380, Math.min(600, $(window).width() - 60)),
		'title' : LABELS.PRIVATE_NOTIFICATIONS_TITLE,
		'close' : function(){
			refreshPrivateNotificationCount();
			$(this).remove();
		},
		'open' : function() {
			var dialog = $(this);
			$('iframe', dialog).attr('src', LAMS_URL + 'notificationsprivate.jsp');

			// in case of mobile devices allow iframe scrolling
			if( /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent) ) {
			    setTimeout(function() {
			    	dialog.css({
			    		'overflow-y' : 'scroll',
			    		'-webkit-overflow-scrolling' : 'touch'
			    	});
			    },500);
			}

		}
	}, false);
	
}

function refreshPrivateNotificationCount(){
	$.ajax({
		cache : false,
		url : LAMS_URL + "notification/getPendingNotificationCount.do",
		dataType : 'text',
		data : {
		},
		success : function(count) {
			$('#notificationsPendingCount').text(count == 0 ? '0' : count);
			
			//#notificationsPendingCount will have .btn-primary class when there are some messages available and .btn-secondary otherwise
			if (count == 0) {
				$('#notificationsPendingCount').removeClass("btn-primary").addClass("btn-secondary");
			} else {
				$('#notificationsPendingCount').removeClass("btn-secondary").addClass("btn-primary");
			}
		}
	});
}

function showGradebookCourseDialog(orgID){
	var id = "dialoGradebookCourse" + orgID;
	showDialog(id, {
		'orgID' : orgID,
		'height': Math.max(380, Math.min(900, $(window).height() - 30)),
		'width' : Math.max(380, Math.min(955, $(window).width() - 60)),
		'title' : LABELS.GRADEBOOK_COURSE_TITLE,
		'open' : function() {
			console.log("width "+$(window).width()+":"+Math.max(380, Math.min(955, $(window).width() - 60)));
			// load contents after opening the dialog
			$('iframe', this).attr('src', LAMS_URL
				+ 'gradebook/gradebookMonitoring/courseMonitor.do?organisationID=' + orgID);
		}
	}, true);
}

function showGradebookLearnerDialog(orgID){
	var id = "dialogGradebookLearner" + orgID;
	showDialog(id, {
		'data'  : {
			'orgID' : orgID
		},
		'height': Math.max(380, Math.min(650, $(window).height() - 30)),
		'width' : Math.max(380, Math.min(800, $(window).width() - 60)),
		'title' : LABELS.GRADEBOOK_LEARNER_TITLE,
		'open' : function() {
			var orgID = $(this).data('orgID');
			// load contents after opening the dialog
			$('iframe', this).attr('src', LAMS_URL
				+ 'gradebook/gradebookLearning/courseLearner.do?organisationID=' + orgID);
		}
	}, true);
}

function showConditionsDialog(lessonID){
	var id = "dialogConditions" + lessonID;
	showDialog(id, {
		'data' : {
			'lessonID' : lessonID
		},
		'height': Math.max(380, Math.min(650, $(window).height() - 30)),
		'width' : Math.max(380, Math.min(610, $(window).width() - 60)),
		'title' : LABELS.CONDITIONS_TITLE,
		'open' : function() {
			var lessonID = $(this).data('lessonID');
			// load contents after opening the dialog
			$('iframe', this).attr('src', LAMS_URL
				+ 'lessonConditions/getIndexLessonConditions.do?lsId=' + lessonID);
		}
	}, true);
}

function showSearchLessonDialog(orgID){
	var id = "dialogSearchLesson" + orgID;
	showDialog(id, {
		'data' : {
			'orgID' : orgID
		},
		'height': Math.max(380, Math.min(600, $(window).height() - 30)),
		'width' : Math.max(380, Math.min(830, $(window).width() - 60)),
		'title' : LABELS.SEARCH_LESSON_TITLE,
		'open' : function() {
			var orgID = $(this).data('orgID');
			// load contents after opening the dialog
			$('iframe', this).attr('src', LAMS_URL
				+ 'findUserLessons/getResults.do?courseID=' + orgID);
		}
	}, true);
}

function showKumaliveRubricsDialog(orgID){
	var id = "dialogKumaliveRubrics" + orgID;
	showDialog(id, {
		'data' : {
			'orgID' : orgID
		},
		'height': Math.max(380, Math.min(650, $(window).height() - 30)),
		'width' : Math.max(380, Math.min(610, $(window).width() - 60)),
		'title' : LABELS.KUMALIVE_RUBRICS_TITLE,
		'open' : function() {
			var orgID = $(this).data('orgID');
			// load contents after opening the dialog
			$('iframe', this).attr('src', LAMS_URL	+ 'learning/kumalive/getRubrics.do?organisationID=' + orgID);
		}
	}, false);
}

function showKumaliveReportDialog(orgID){
	var id = "dialogKumaliveReports" + orgID;
	showDialog(id, {
		'data' : {
			'orgID' : orgID
		},
		'height': Math.max(380, Math.min(800, $(window).height() - 30)),
		'width' : Math.max(380, Math.min(1024, $(window).width() - 60)),
		'title' : LABELS.KUMALIVE_REPORT_TITLE,
		'open' : function() {
			var orgID = $(this).data('orgID');
			// load contents after opening the dialog
			$('iframe', this).attr('src', LAMS_URL	+ 'learning/kumalive/getReport.do?organisationID=' + orgID);
		}
	}, false);
}

function showOutcomeDialog() {
	showDialog("dialogOutcome", {
		'height': Math.max(380, Math.min(700, $(window).height() - 30)),
		'width' : Math.max(380, Math.min(850, $(window).width() - 60)),
		'title' : LABELS.OUTCOME_MANAGE_TITLE,
		'open'  : function() {
			var dialog = $(this);
			// load contents after opening the dialog
			$('iframe', dialog)
					.attr('src', LAMS_URL
						+ 'outcome.do?method=outcomeManage');
		}
	});
}

function closeAddSingleActivityLessonDialog(action) {
	var id = 'dialogAddSingleActivityLesson',
		dialog = $('#' + id),
		save = action == 'save';
	
	if (save) {
		$.ajax({
			async : false,
			cache : false,
			url : LAMS_URL + "authoring/createSingleActivityLesson.do",
			dataType : 'text',
			data : {
				'organisationID'  : dialog.data('orgID'),
				'toolID' : dialog.data('toolID'),
				'toolContentID' : dialog.data('toolContentID'),
				'contentFolderID' : dialog.data('contentFolderID'),
				'learningLibraryID' : dialog.data('learningLibraryID')
			},
			// create LD SVG
			success : function(learningDesignID) {
				// check if the LD was created successfully
				if (learningDesignID) {
					var frame = $('iframe', dialog);
					// disable previous onload handler, set in
					// showAddSingleActivityLessonDialog()
					frame.off('load').on('load', function(){
						// disable current onload handler as closing the dialog reloads the iframe
						frame.off('load');
						
						// call svgGenerator.jsp code to store LD SVG on the server
						var win = frame[0].contentWindow || frame[0].contentDocument;
						$(win.document).ready(function(){
						    // when LD opens, make a callback which save the thumbnail and displays it in current window
							win.GeneralLib.openLearningDesign(learningDesignID, function(){
								win.GeneralLib.saveLearningDesignImage();
								closeDialog(id, true);
							});
						});
					});
					// load svgGenerator.jsp to render LD SVG
					frame.attr('src', LAMS_URL + 'authoring/generateSVG.do?selectable=false');
				}
			}
		});
	} else {
		closeDialog(id, false);
	}
}


function closeDialog(id, refresh) {
	// was the dialog just closed or a lesson removed
	// if latter, refresh the list
	if (refresh) {
		loadOrganisation();
	}
	var dialog = $("#" + id).modal('hide'),
		relaunchMonitorLessonID = dialog.data('relaunchMonitorLessonID');
	if (relaunchMonitorLessonID) {
		showMonitorLessonDialog(relaunchMonitorLessonID);
	}
}


function removeLesson(lessonID) {
	if (confirm(LABELS.REMOVE_LESSON_CONFIRM1)) {
		if (confirm(LABELS.REMOVE_LESSON_CONFIRM2)) {
			$.ajax({
				url : LAMS_URL + "monitoring/monitoring/removeLesson.do",
				type: "POST",
				async : false,
				data: $("#csrf-form").serialize() + "&lessonID=" + lessonID,
			    success: function(json) {
					if (json.removeLesson == true) {
						loadOrganisation();
					} else {
						alert(json.removeLesson);
					}    
			    }
			})
		}
	}
}
