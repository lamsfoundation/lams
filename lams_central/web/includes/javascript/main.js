$(document).ready(function () {
	
	// open active course. in case active course is not yet chosen by user, it will be opened after tablesorter's pager receives all orgs
	if (activeOrgId != null) {
		loadOrganisation();
	}
	
	refreshPrivateNotificationCount();
	
	var $tablesorter = $(".tablesorter").tablesorter({
		theme: 'bootstrap',
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
            ajaxUrl : LAMS_URL + "/index.do?dispatch=getOrgs&page={page}&size={size}&{sortList:column}&{filterList:fcol}",
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
							rows +=     '<i class="fa fa-chevron-circle-right fa-lg pull-right"></i>';
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

	$('.tablesorter').bind('filterEnd pagerComplete', function(event, data){
		
		//hide pager if total amount of courses is less than 10
		if (data.totalRows < 10) {
			$(".tablesorter-pager").hide();
    	} else {
    		$(".tablesorter-pager").show();
	    }

		//in case active course is not yet chosen by user, select the fist one from the list
	    if ((activeOrgId == null) && (event.type == "pagerComplete") && (data.totalRows > 0)) {
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

    // Full height of offcanvas bar
    function adjustOffcanvasBarHeight() {
        var heightWithoutNavbar = $("body > #wrapper").height() - 61;
        $(".offcanvasd-panel").css("min-height", heightWithoutNavbar + "px");

        var navbarHeigh = $('#offcanvas').height();
        var wrapperHeigh = $('#page-wrapper').height();

        if (navbarHeigh > wrapperHeigh) {
            $('#page-wrapper').css("min-height", navbarHeigh + "px");
        }

        if (navbarHeigh < wrapperHeigh) {
            $('#page-wrapper').css("min-height", $(window).height() + "px");
        }
    }

    $(window).bind("load resize scroll", function () {
    	adjustOffcanvasBarHeight();
    });
    adjustOffcanvasBarHeight();

});


function selectOrganisation(newOrgId) {
	//remove active CSS class from the old org
	if (activeOrgId != null) {
		$("#org-row-" + activeOrgId + ", #favorite-li-" + activeOrgId).removeClass("active");
		$("#org-row-" + activeOrgId + " a>i").remove();
	}	
	
	//add active CSS class
	$("#org-row-" + newOrgId + ", #favorite-li-" + newOrgId).addClass("active");
	$("#org-row-" + newOrgId + " a").append( "<i class='fa fa-chevron-circle-right fa-lg pull-right'></i>" )
	
	activeOrgId = newOrgId;
	loadOrganisation();
	
	//store last visited
	$("#org-container").load(
		"index.do",
		{
			dispatch : "storeLastVisitedOrganisation",
			orgId   : activeOrgId
		}
	);
}


function loadOrganisation() {	
	$("#org-container").load(
		"displayGroup.do",
		{
			stateId : stateId,
			orgId   : activeOrgId
		},
		function( response, status, xhr ) {
			//in case of any server error - open offcanvas bar so user can select another course
			if ( status == "error" ) {
				$("body").removeClass("offcanvas-hidden");
			}			
		}
	);
}


function toggleFavoriteOrganisation(orgId) {
	$("#favorite-organisations-container").load(
		"index.do",
		{
			dispatch: "toggleFavoriteOrganisation",
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


function showMyProfileDialog() {
	showDialog("dialogMyProfile", {
		'title' : LABELS.MY_PROFILE,
		'modal' : true,
		'width' : Math.max(380, Math.min(770, $(window).width() - 60)),
		'height' : 430,
		'open' : function() {
			var dialog = $(this);
			// load contents after opening the dialog
			$('iframe', dialog).attr('src', LAMS_URL + 'index.do?method=profile');
			
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


function makeOrgSortable() {
	var org = jQuery("#org-container");
	$(".lesson-table", org).each(function() {
		makeSortable(this);
	});
	
	$("a.sorting", org).attr({
		"onClick" : null,
		"title"   : LABELS.SORTING_DISABLE
	}).off('click').click(function(){
		makeOrgUnsortable();
	}).find("img")
	  .attr("src", "images/sorting_enabled.gif");
}

function makeOrgUnsortable() {
	var org = jQuery("#org-container");
	$(".lesson-table", org).each(function() {
		$(this).sortable('destroy');
	});
	
	$("a.sorting", org).attr({
		"onClick" : null,
		"title"   : LABELS.SORTING_ENABLE
	}).off('click').click(function(){
		makeOrgSortable();
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
					//loadOrganisation();
				}
			});
		}
	}).disableSelection();
}


function showMonitorLessonDialog(lessonID) {

	var id = "dialogMonitorLesson" + lessonID,
		dialog = showDialog(id, {
			'data' : {
				'lessonID' : lessonID
			},
			'autoOpen' : false,
			'height': Math.max(380, Math.min(600, $(window).height() - 30)),
			'width' : Math.max(380, Math.min(1024, $(window).width() - 60)),
			'title' : LABELS.MONITORING_TITLE,
			'open' : function() {
				// load contents after opening the dialog
				$('iframe', this).attr('src', LAMS_URL
					+ 'home.do?method=monitorLesson&lessonID='
					+ $(this).data('lessonID'));
			},

		}, true, true);
	
	// if it was just created
	if (dialog) {
		// tell the dialog contents that it was resized
		$('.modal-content', dialog).on('resizestop', resizeSequenceCanvas);
		// initial resize
		$('iframe', dialog).load(resizeSequenceCanvas);
		
		dialog.modal('show');
	}
}

function resizeSequenceCanvas(){
	var body = $('.modal-body'),
		frame = $('iframe', body);
	if (frame.length > 0) {
		var win = frame[0].contentWindow || frame[0].contentDocument;
		if (win.resizeSequenceCanvas) {
			win.resizeSequenceCanvas(body.width(), body.height());
		}
	}
}


function showAddLessonDialog(orgID) {
	showDialog("dialogAddLesson", {
		'data' : {
			'orgID' : orgID
		},
		'height': Math.max(380, Math.min(740, $(window).height() - 30)),
		'width' : Math.max(380, Math.min(850, $(window).width() - 60)),
		'title' : LABELS.ADD_LESSON_TITLE,
		'open' : function() {
			var dialog = $(this);
			// load contents after opening the dialog
			$('iframe', dialog)
					.attr('src', LAMS_URL
						+ 'home.do?method=addLesson&organisationID='
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
			$('iframe', this).attr('src', LAMS_URL + 'OrganisationGroup.do?method=viewGroupings&organisationID=' + orgID
												   + (activityID ? '&activityID=' + activityID : ''));
		}
	}, true);
}

function showOrgGroupDialog(url) {
	$('#dialogOrgGrouping').modal('hide');
	showDialog("dialogOrgGroup", {
		'width' : Math.max(380, Math.min(850, $(window).width() - 60)),
		'height': Math.max(380, Math.min(470, $(window).height() - 30)),
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
				url : LAMS_URL + "authoring/author.do",
				dataType : 'json',
				data : {
					'method' : 'createToolContent',
					'toolID' : toolID
				},
				success : function(response) {
					dialog.data({
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
		'data' : {
			'orgID' : orgID,
			'lessonID' : lessonID
		},
		'height': Math.max(380, Math.min(650, $(window).height() - 30)),
		'width' : Math.max(380, Math.min(800, $(window).width() - 60)),
		'title' : LABELS.EMAIL_NOTIFICATIONS_TITLE,
		'open' : function() {
			var dialog = $(this),
				lessonID = dialog.data('lessonID');
			// if lesson ID is given, use lesson view; otherwise use course view
			if (lessonID) {
				// load contents after opening the dialog
				$('iframe', dialog).attr('src', LAMS_URL
					+ 'monitoring/emailNotifications.do?method=getLessonView&lessonID='
					+ lessonID);
			} else {
				$('iframe', dialog).attr('src', LAMS_URL
					+ 'monitoring/emailNotifications.do?method=getCourseView&organisationID='
					+ orgID);
			}
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
		url : LAMS_URL + "notification.do",
		dataType : 'text',
		data : {
			'method' : 'getPendingNotificationCount'
		},
		success : function(count) {
			$('#notificationsPendingCount').text(count == 0 ? '0' : count);
		}
	});
}

function showGradebookCourseDialog(orgID){
	var id = "dialoGradebookCourse" + orgID;
	showDialog(id, {
		'orgID' : orgID,
		'height': Math.max(380, Math.min(650, $(window).height() - 30)),
		'width' : Math.max(380, Math.min(800, $(window).width() - 60)),
		'title' : LABELS.GRADEBOOK_COURSE_TITLE,
		'open' : function() {
			// load contents after opening the dialog
			$('iframe', this).attr('src', LAMS_URL
				+ 'gradebook/gradebookMonitoring.do?dispatch=courseMonitor&organisationID=' + orgID);
		}
	}, true);
}

function showGradebookLessonDialog(lessonID){
	var id = "dialogGradebookLesson" + lessonID;
	showDialog(id, {
		'data' : {
			'lessonID' : lessonID
		},
		'height': Math.max(380, Math.min(650, $(window).height() - 30)),
		'width' : Math.max(380, Math.min(800, $(window).width() - 60)),
		'title' : LABELS.GRADEBOOK_LESSON_TITLE,
		'open' : function() {
			var lessonID = $(this).data('lessonID');
			// load contents after opening the dialog
			$('iframe', this).attr('src', LAMS_URL + 'gradebook/gradebookMonitoring.do?lessonID=' + lessonID);
		}
	}, true);
}



function showGradebookLearnerDialog(orgID){
	var id = "dialogGradebookLearner" + orgID;
	showDialog(id, {
		'data'  : {
			'orgID' : orgID
		},
		// too little difference between min height of 380 and standard height of 400,
		// so just keep it always 400
		'height' : 400,
		'width' : Math.max(380, Math.min(750, $(window).width() - 60)),
		'title' : LABELS.GRADEBOOK_LEARNER_TITLE,
		'open' : function() {
			var orgID = $(this).data('orgID');
			// load contents after opening the dialog
			$('iframe', this).attr('src', LAMS_URL
				+ 'gradebook/gradebookLearning.do?dispatch=courseLearner&organisationID=' + orgID);
		}
	}, true);
}

function showConditionsDialog(lessonID){
	var id = "dialogConditions" + lessonID;
	showDialog(id, {
		'data' : {
			'lessonID' : lessonID
		},
		'height': Math.max(380, Math.min(600, $(window).height() - 30)),
		'width' : Math.max(380, Math.min(610, $(window).width() - 60)),
		'title' : LABELS.CONDITIONS_TITLE,
		'open' : function() {
			var lessonID = $(this).data('lessonID');
			// load contents after opening the dialog
			$('iframe', this).attr('src', LAMS_URL
				+ 'lessonConditions.do?method=getIndexLessonConditions&lsId=' + lessonID);
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
				+ 'findUserLessons.do?dispatch=getResults&courseID=' + orgID);
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
					frame.off('load').load(function(){
						// disable current onload handler as closing the dialog
						// reloads the iframe
						frame.off('load');
						
						// call svgGenerator.jsp code to store LD SVG on the
						// server
						var win = frame[0].contentWindow || frame[0].contentDocument;
						win.GeneralLib.saveLearningDesignImage();
						
						closeDialog(id, true);
					});
					// load svgGenerator.jsp to render LD SVG
					frame.attr('src', LAMS_URL + 'authoring/author.do?method=generateSVG&selectable=false&learningDesignID='
											   + learningDesignID);
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
	$("#" + id).modal('hide');
}


//used in main.jsp and /lti/addlesson.jsp
function showAuthoringDialog(learningDesignID){
	showDialog('dialogAuthoring', {
		'height' : Math.max(300, $(window).height() - 30),
		'width' : Math.max(600, Math.min(1280, $(window).width() - 60)),
		'title' : LABELS.AUTHORING_TITLE,
		'beforeClose' : function(){
			// if LD was modified, ask the user if he really wants to exit
			var innerLib = $('iframe', this)[0].contentWindow.GeneralLib,
				// no innerLib means that an exception occured in Authoring
				// and the interface is not usable anyway
				canClose = !innerLib || innerLib.canClose() || confirm(LABELS.NAVIGATE_AWAY_CONFIRM);
			if (canClose) {
				$('iframe', this).attr('src', null);
			} else {
				return false;
			}
		},
		'open' : function() {
			var url = LAMS_URL + 'authoring/author.do?method=openAuthoring';
			
			if (learningDesignID) {
				url += '&learningDesignID=' + learningDesignID;
			}
			
			// load contents after opening the dialog
			$('iframe', this).attr('src', url);
		}
	}, true);
}


function removeLesson(lessonID) {
	if (confirm(LABELS.REMOVE_LESSON_CONFIRM1)) {
		if (confirm(LABELS.REMOVE_LESSON_CONFIRM2)) {
			$.ajax({
				async : false,
				url : LAMS_URL + "monitoring/monitoring.do",
				data : "method=removeLesson&lessonID=" + lessonID,
				type : "POST",
				success : function(json) {
					if (json.removeLesson == true) {
						loadOrganisation();
					} else {
						alert(json.removeLesson);
					}
				}
			});
		}
	}
}


function showEmailDialog(userId, lessonId){
	var dialog = showDialog("dialogEmail", {
		'autoOpen'  : true,
		'height'    : Math.max(380, Math.min(700, $(window).height() - 30)),
		'width'     : Math.max(380, Math.min(700, $(window).width() - 60)),
		'modal'     : true,
		'resizable' : true,
		'title'     : LABELS.EMAIL_TITLE,
		'open'      : function(){
			autoRefreshBlocked = true;
			var dialog = $(this);
			// load contents after opening the dialog
			$('iframe', dialog).attr('src',
					LAMS_URL + 'emailUser.do?method=composeMail&lessonID=' + lessonId
					+ '&userID=' + userId);
		},
		'close' : function(){
			autoRefreshBlocked = false;
			$(this).remove();
		}
	}, false, true);
}


function closeEmailDialog(){
	$('#dialogEmail').modal('hide');
}