var dialogTemplate = $('<div class="modal fade dialogContainer " tabindex="-1" role="dialog">' +
	'<div class="modal-dialog" role="document">' +
	'<div class="modal-content">' +
	'<div class="modal-header">' +
	'<h5 class="modal-title"></h5>' +
	'<button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>'+
	'<button type="button" class="close dialogMaximise" aria-label="Maximise">' +
	'<i class="fa fa-plus" aria-hidden="true"></i>' +
	'</button>' +
	'<button type="button" class="close dialogMinimise" aria-label="Minimise">' +
	'<i class="fa fa-minus" aria-hidden="true"></i>' +
	'</button>' +
	'</div>' +
	'<div class="modal-body">' +
	'<iframe></iframe>' +
	'</div>' +
	'</div>' +
	'</div>' +
	'</div>');


/**
 * Checks if the dialog is already opened.
 * If not, creates a new dialog with the given ID and init parameters.
 */
function showDialog(id, initParams, extraButtons, recreate) {
	var dialog = $('#' + id);
	// is it opened already?
	if (dialog.length > 0) {
		if (recreate){
			dialog.modal('hide');

			//try to open already existing dialog
		} else {
			//restore minimised dialog
			if (dialog.hasClass('dialogMin')) {
				restoreMinimisedDialog(dialog);

			} else {
				// we do not support multiple opened bootstrap dialogs yet
				// dialog.dialog('moveToTop');
			}

			return;
		}
	}

	// checks whether the dialog should be created inside a current window or in a parent one. The latter is preferred
	//in case we want dialog to be not constrained by the boundaries of a current window
	var targetWindow = initParams.isCreateInParentWindow ? window.parent : window;

	// create a new dialog by cloning a template
	dialog = dialogTemplate.clone().appendTo(targetWindow.$('body'));
	dialog.data('isCreateInParentWindow', initParams.isCreateInParentWindow);

	// use the input attributes or fall back to default ones
	initParams = $.extend({
		'autoOpen' : true,
		'draggable' : true,
		'resizable' : extraButtons == true,
		'startMaximized' : false,
		'beforeClose' : function(){
			$('iframe', this).attr('src', null);
		},
		'close' : function() {
			// completely delete the dialog
			$(this).remove();
		}
	}, initParams);

	// update title
	$('.modal-title', dialog).attr('id',  id + 'Label').text(initParams.title);
	dialog.attr({
		'id' : id,
		'aria-labelledby' : id + 'Label'
	});

	var modalDialog = $('.modal-dialog', dialog),
		modalContent = $('.modal-content', dialog);

	if (initParams.width) {
		modalDialog.width(initParams.width);
	}
	if (initParams.height) {
		modalContent.height(initParams.height);
	}
	if (initParams.resizable) {
		modalContent.resizable();
	}

	// disable draggable for Android and iPhone as it breaks the close buttons
	var draggable = initParams.draggable && ! /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent);
	if (draggable) {
		modalDialog.draggable({
			'cancel' : '.modal-body, button.close',
			'revert' : function() {
				return modalDialog.offset().top < -5;
			}
		});
	}
	dialog.data("isDraggable", draggable);

	// store extra attributes for dialog content internal use
	if (initParams.data) {
		dialog.data(initParams.data);
	}

	dialog.on('show.bs.modal', initParams.open);
	dialog.on('hide.bs.modal', initParams.beforeClose);
	dialog.on('hidden.bs.modal', initParams.close);

	dialog.modal({
		'keyboard' : false,
		'backdrop' : 'static'
	});

	if (initParams.autoOpen) {
		dialog.modal('show');
	}

	if (extraButtons) {
		var maximizeButton = $('.dialogMaximise', dialog).click(function(){
			var icon = $('i', this),
				wasMaxed = icon.hasClass('fa-clone'),
				internalDialog = $('.modal-dialog', dialog),
				internalContent = $('.modal-content', dialog);
			icon.toggleClass('fa-plus').toggleClass('fa-clone');
			if (wasMaxed) {
				// restore dialog
				var oldWidth = dialog.data('oldWidth'),
					oldHeight = dialog.data('oldHeight');
				internalDialog.width(oldWidth);
				internalContent.width(oldWidth);
				internalContent.height(oldHeight);

				// enable resizing
				$('.ui-resizable-handle', dialog).show();
			} else {
				// store previous size
				dialog.data({
					'oldWidth' : internalContent.width(),
					'oldHeight': internalContent.height()
				});
				internalDialog.css({
					'width' : '100%',
					'height': '100%'
				});
				internalContent.css('width', 'calc(100% - 15px)');
				internalContent.height(internalDialog.height() - 20);

				// disable resizing
				$('.ui-resizable-handle', dialog).hide();
			}
			// center the dialog
			internalDialog.position(
				dialog.data('isCreateInParentWindow') ?
					{
						'my' : 'center center',
						'at' : 'center center',
						'of' : window.parent
					}
					:
					{
						'my' : 'top',
						'at' : 'center top',
						'of' : window
					}
			);

			internalContent.trigger('resizestop');
		});

		if (initParams.startMaximized) {
			maximizeButton.click();
		}

		$('.dialogMinimise', dialog).click(function() {
			if (dialog.hasClass('dialogMin')) {
				restoreMinimisedDialog(dialog);
			} else {
				// swap icon
				$('.dialogMinimise i', dialog).toggleClass('fa-minus').toggleClass('fa-clone');
				// store current left position
				dialog.data('oldLeft', dialog.css('left'));
				dialog.css('left', $('.dialogMin').length * 260 + 5 + 'px');
				dialog.addClass('dialogMin');
				// remove overlay
				dialog.siblings('.modal-backdrop').hide();
				// disable maximising
				$('.dialogMaximise', dialog).hide();
				// disable rezising
				$('.ui-resizable-handle', dialog).hide();
				if (draggable) {
					modalDialog.draggable('disable');
				}
				//enable scrolling of the parent's content
				dialog.parent().removeClass("modal-open");
			};
		});

		dialog.on('hide.bs.modal', function(){
			shiftMinimisedDialogs($(this));
		});
	} else {
		$('.dialogMinimise, .dialogMaximise', dialog).remove();
	}

	return dialog;
}

function restoreMinimisedDialog(dialog) {
	// swap icon
	$('.dialogMinimise i', dialog).toggleClass('fa-minus').toggleClass('fa-clone');
	shiftMinimisedDialogs(dialog);
	// restore old left position
	dialog.css('left', dialog.data('oldLeft'));
	dialog.removeClass('dialogMin');
	dialog.siblings('.modal-backdrop').show();
	$('.dialogMaximise', dialog).show();
	$('.ui-resizable-handle', dialog).show();
	if (dialog.data("isDraggable")) {
		var modalDialog = $('.modal-dialog', dialog);
		modalDialog.draggable('enable');
	}
	//disable scrolling of the parent's content
	dialog.parent().addClass("modal-open");

	// in case of monitor dialog reload its content. It's required as all monitor dialogs use the same ids for their HTML elements
	var isMonitorDialog = dialog.data('isMonitorDialog');
	if (isMonitorDialog) {
		//hide iframe until it gets loaded
		$('iframe', dialog).hide();
		$('iframe', dialog).on('load', function() {
			$('iframe', dialog).show();
		});

		//reload iframe
		$('iframe', dialog)["0"].contentWindow.location.reload();
	}
}

/**
 * Moves other minimised dialogs to the left after the given dialog was restored or closed.
 */
function shiftMinimisedDialogs(dialog){
	var left = dialog.offset().left;
	$('.dialogMin').each(function(){
		var thisDialogLeft = $(this).offset().left;
		// if the dialog was to the right of the given dialog, shift it to left
		if (left < thisDialogLeft) {
			$(this).css('left', thisDialogLeft - 260 + 'px');
		}
	});
}


/**
 * Focuses on the dialog. Called from within the contained iframe.
 */
function moveDialogToTop(id) {
	// $('#' + id).dialog('moveToTop');
}

function openAuthoring(learningDesignID, relaunchMonitorLessonID) {
	var url = LAMS_URL + 'authoring/openAuthoring.do';

	if (learningDesignID) {
		url += '?learningDesignID=' + learningDesignID;
	}
	if (relaunchMonitorLessonID) {
		url += '&relaunchMonitorLessonID=' + relaunchMonitorLessonID;
	}

	window.location.href = url;
}

//used by both /lams_central/web/main.jsp and /lams_central/web/lti/addlesson.jsp pages
function showAuthoringDialog(learningDesignID, relaunchMonitorLessonID){
	var dialog = showDialog('dialogAuthoring', {
		'height' : Math.max(300, $(window).height() - 30),
		'width' : Math.max(600, Math.min(1290, $(window).width() - 60)),
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
			var url = LAMS_URL + 'authoring/openAuthoring.do';

			if (learningDesignID) {
				url += '?learningDesignID=' + learningDesignID;
			}

			// load contents after opening the dialog
			$('iframe', this).attr({'src' : url, 'id' : 'authoringModal'});
		}
	}, true);

	if (relaunchMonitorLessonID) {
		dialog.data('relaunchMonitorLessonID', relaunchMonitorLessonID);
	}

	// resize the paper when dialog is resized
	$('.modal-content', dialog).on('resizestop', function() {
		$('iframe', this)[0].contentWindow.GeneralLib.resizePaper();
	});
}


//used by both /lams_central/web/main.jsp and /lams_monitoring/web/monitor.jsp pages
function showNotificationsDialog(orgID, lessonID) {
	//check whether current window is a top level one (otherwise it's an iframe or popup)
	var isTopLevelWindow = window.top == window.self;

	var id = "dialogNotifications" + (lessonID ? "Lesson" + lessonID : "Org" + orgID);
	showDialog(id, {
		'data' : {
			'orgID' : orgID,
			'lessonID' : lessonID
		},
		'height': 700,
		//dialog needs to be added to a top level window to avoid boundary limitations of the interim iframe
		"isCreateInParentWindow" : !isTopLevelWindow,
		'title' : LABELS.EMAIL_NOTIFICATIONS_TITLE,
		'open' : function() {
			var dialog = $(this);
			// load contents after opening the dialog
			$('iframe', dialog).attr('src', LAMS_URL
				+ 'monitoring/emailNotifications/getLessonView.do?newUI=true&lessonID='
				+ lessonID);
		}
	}, false).find('.modal-dialog').addClass('modal-xl');
}


//used by Page.tag
function showMyPortraitDialog() {
	showDialog("dialogMyProfile", {
		'title' : "Portrait",
		'modal' : true,
		'width' : Math.max(380, Math.min(770, $(window).width() - 60)),
		'open' : function() {
			var dialog = $(this);
			// load contents after opening the dialog
			$('iframe', dialog).attr('src', LAMS_URL + 'index.do?redirect=portrait&isReturnButtonHidden=true');

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

function showConfirm(body, callback) {
	let dialog = $('#confirmationDialog').data('confirmed', null).off('hidden.bs.modal').on('hidden.bs.modal', function(){
		if (dialog.data('confirmed')) {
			callback(true);
		}
	});

	$('.modal-body', dialog).html(body)

	$("#confirmationDialogConfirmButton").off('click').on("click", function(){
		dialog.data('confirmed', true);
		dialog.modal('hide');
	});

	$("#confirmationDialogCancelButton").off('click').on("click", function(){
		dialog.modal('hide');
	});

	dialog.modal('show');
}

function showToast(text) {
	let toast = $('#toast-template').clone().attr('id', null).appendTo('#toast-container');
	toast.find('.toast-body', toast).text(text);
	toast = new bootstrap.Toast(toast[0]);
	toast.show();
}