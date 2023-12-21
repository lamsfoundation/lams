// refresh progress bar on first/next activity load
function initLearnerPage(toolSessionId, lessonId, userId) {
    $.ajax({
        url: LAMS_URL + 'learning/learner/getLearnerProgress.do',
        data: {
            'toolSessionID': toolSessionId,
            'lessonID': lessonId,
            'userID': userId
        },
        cache: false,
        dataType: 'json',
        success: function (result) {
            $('.component-page-wrapper .component-page-content #lesson-name').text(result.lessonName);

            // draw support activities if they exist
            const ACTIVITY_ITEM_TEMPLATE = '<li><div class="row align-items-center"><div class="icon"></div><div class="col text-center"></div></div></li>';
            if (result.support) {
                let supportBarItems = $('.component-page-wrapper .offcanvas #offcanvas-support-bar')
                	.removeClass('d-none')
                    .find('#support-bar-items')
                    .empty();
                $.each(result.support, function (activityIndex, activityData) {
                    let activityItem = $(ACTIVITY_ITEM_TEMPLATE)
                        .addClass('list-group-item list-group-item-action progress-bar-item-openable').appendTo(supportBarItems),
                        activityIcon = $('<i>').attr({
	                        'class': 'fa-solid fa-fw fa-lg fa-up-right-from-square progress-bar-icon',
	                        'title': LABEL_SUPPORT_ACTIVITY
						}),
                    	activityLink = $('<a>').text(activityData.name).attr({
	                        'target': '_blank',
	                        'href': activityData.url,
	                        'role': 'menuitem',
	                        'title': LABEL_CLICK_TO_OPEN
	                    });
	                $(".icon", activityItem).prepend(activityIcon);
	                $(".col", activityItem).append(activityLink);
                });
            }

            let progressBarItems = $('.component-page-wrapper .offcanvas #progress-bar-items').empty(),
                completedActivityCount = 0;
            $.each(result.activities, function (activityIndex, activityData) {
                let activityItem = $(ACTIVITY_ITEM_TEMPLATE)
                		.addClass('list-group-item list-group-item-action')
                		.appendTo(progressBarItems),
                    activityName = !activityData.name && activityData.type === 'g' ? 'Gate' : activityData.name,
                    activityIcon = $('<i class="fa-fw fa-lg progress-bar-icon"></i>');
                    $(".icon", activityItem).prepend(activityIcon);

				//current
                if (activityData.status === 0) {
					activityItem.addClass('active');
					$(".col", activityItem).text(activityName);
					activityIcon.addClass('fa-regular fa-pen-to-square')
						.attr('title', LABEL_CURRENT_ACTIVITY);
                
                //completed
                } else if (activityData.status === 1) {
                    completedActivityCount++;

					activityItem.addClass('progress-bar-item-complete');
					activityIcon.addClass('fa-solid fa-square-check')
						.attr('title', LABEL_COMPLETED_ACTIVITY);
                    if (activityData.url) {
                        let activityLink = $('<a>').text(activityName).attr({
                            'target': '_blank',
                            'href': activityData.url,
                            'role': 'menuitem',
                            'title': LABEL_CLICK_TO_OPEN
                        });
                        activityItem.addClass('progress-bar-item-openable');
                        $(".col", activityItem).append(activityLink);
                    }
                
                //not yet finished
                } else {
                    activityItem.addClass('progress-bar-item-incomplete');
					$(".col", activityItem).text(activityName);
					activityIcon.addClass('fa-regular fa-square')
						.attr('title', LABEL_NOT_STARTED_ACTIVITY);
                }
            });

            let progressBarWidgetValue = Math.round(completedActivityCount / result.activities.length * 100),
                progressBarWidget = $('.component-page-wrapper .component-page-content #progress-bar-widget');
            $('.progress-bar', progressBarWidget)
                .css('width', progressBarWidgetValue + '%')
                .attr('aria-valuenow', progressBarWidgetValue);
            $('#progress-bar-widget-value').text(progressBarWidgetValue + '%');


            initCommandWebsocket(result.lessonID);
        }
    });
}

function initCommandWebsocket(lessonId) {
    initWebsocket('commandWebsocket', LAMS_URL.replace('http', 'ws')
        + 'learning/commandWebsocket?lessonID=' + lessonId,
        function (e) {
            // read JSON object
            var command = JSON.parse(e.data);
            if (command.message) {
                // some tools implement autosave feature
                // if it is such a tool, trigger it
                if (command.message === 'autosave') {
                    // the name of this function is same in all tools
                    if (typeof learnerAutosave == 'function') {
                        learnerAutosave(true);
                    }
                } else {
                    alert(command.message);
                }
            }

            // if learner's currently displayed page has hookTrigger same as in the JSON
            // then a function also defined on that page will run
            if (command.hookTrigger && command.hookTrigger == commandWebsocketHookTrigger
                && typeof commandWebsocketHook === 'function') {
                commandWebsocketHook(command.hookParams);
            }

            if (command.redirectURL) {
                window.location.href = command.redirectURL;
            }

            if (command.discussion) {
                var discussionCommand = $('#discussion-sentiment-command');
                if (discussionCommand.length === 0) {
                    discussionCommand = $('<div />').attr('id', 'discussion-sentiment-command').appendTo('body');
                }
                discussionCommand.load(LAMS_URL + "learning/discussionSentiment/" + command.discussion + ".do", {
                    lessonId : lessonId
                });
            }
            // reset ping timer
            websocketPing('commandWebsocket', true);
        });

    // check if there is a running discussion; if so, a websocket command will come to display the widget
    $.ajax({
        url: LAMS_URL + "learning/discussionSentiment/checkLearner.do",
        data: {
            lessonId: lessonId
        }
    });
}

//TODO:remove this not-used function
function toggleProgressBar(forceClose) {
    let pageContent = $('.component-page-wrapper .component-page-content'),
        progressBar = $('.component-page-wrapper .offcanvas'),
        topToggleButton = $('header #hamb', pageContent),
        isExpanded = forceClose || topToggleButton.attr('aria-expanded') == 'true';
        
    topToggleButton.attr('aria-expanded', !isExpanded)
        .children('i').toggleClass(topToggleButton.data('closed-class')).toggleClass(topToggleButton.data('opened-class'));
    progressBar.toggleClass('active').attr('aria-expanded', !isExpanded);

    $('body').off('keyup');

    if (isExpanded) {
        progressBar.attr('inert', '');
        pageContent.removeAttr('inert');
    } else {
        pageContent.attr('inert', '');
        progressBar.removeAttr('inert');
        $('.btn-close', progressBar).focus();

        $('body').on('keyup', function (event) {
            if (event.key === "Escape") {
                toggleProgressBar(true);
            }
        });
    }
}