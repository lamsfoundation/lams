// refresh progress bar on first/next activity load
function initLearnerPage(toolSessionId, userId) {
    $('.component-page-wrapper .sidebar-toggle-button').click(function (event) {
        event.stopPropagation();
        toggleProgressBar();
    });

    $.ajax({
        url: LAMS_URL + 'learning/learner/getLearnerProgress.do',
        data: {
            'toolSessionID': toolSessionId,
            'userID': userId
        },
        cache: false,
        dataType: 'json',
        success: function (result) {
            $('.component-page-wrapper .component-page-content #lesson-name').text(result.lessonName);

            // draw support activities if they exist
            if (result.support) {
                let supportBarItems = $('.component-page-wrapper .component-sidebar #support-bar').removeClass('d-none')
                    .find('#support-bar-items').empty();
                $.each(result.support, function (activityIndex, activityData) {
                    let activityItem = $('<li>').attr('role', 'presentation')
                        .addClass('support-bar-item progress-bar-item-openable')
                        .prepend('<i class="fa-solid fa-fw fa-up-right-from-square progress-bar-icon">').appendTo(supportBarItems);
                    let activityLink = $('<a>').text(activityData.name).attr({
                        'target': '_blank',
                        'href': activityData.url,
                        'role': 'menuitem',
                        'title': 'Open completed activity'
                    });
                    activityItem.addClass('progress-bar-item-openable').append(activityLink);
                });
            }

            let progressBarItems = $('.component-page-wrapper .component-sidebar #progress-bar-items').empty(),
                completedActivityCount = 0;
            $.each(result.activities, function (activityIndex, activityData) {
                let activityItem = $('<li>').attr('role', 'presentation').appendTo(progressBarItems),
                    activityName = !activityData.name && activityData.type === 'g' ? 'Gate' : activityData.name,
                    activityIcon = $('<i class="fa-solid fa-fw progress-bar-icon"></i>');

                if (activityData.status === 0) {
                    activityItem.addClass('progress-bar-item-current').text(activityName).prepend(activityIcon);
                    if (activityData.type === 'g') {
                        activityIcon.addClass('fa-hourglass-half');
                    } else {
                        activityIcon.addClass('fa-pen-to-square');
                    }
                } else if (activityData.status === 1) {
                    completedActivityCount++;

                    activityItem.addClass('progress-bar-item-complete').prepend(activityIcon);
                    if (activityData.type === 'g') {
                        activityIcon.addClass('fa-hourglass-full');
                    } else {
                        activityIcon.addClass('fa-square-check');
                    }
                    if (activityData.url) {
                        let activityLink = $('<a>').text(activityName).attr({
                            'target': '_blank',
                            'href': activityData.url,
                            'role': 'menuitem',
                            'title': 'Open completed activity'
                        });
                        activityItem.addClass('progress-bar-item-openable').append(activityLink);
                    }
                } else {
                    activityItem.addClass('progress-bar-item-incomplete').text(activityName).prepend(activityIcon);
                    if (activityData.type === 'g') {
                        activityIcon.addClass('fa-hourglass-start');
                    } else {
                        activityIcon.addClass('fa-square');
                    }
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
    let commandWebsocketInitTime = Date.now(),
        commandWebsocket = null,
        commandWebsocketPingTimeout = null,
        commandWebsocketPingFunc = null,
        commandWebsocketReconnectAttempts = 0,
        commandWebsocketHookTrigger = null,
        commandWebsocketHook = null;

    commandWebsocketPingFunc = function (skipPing) {
        if (commandWebsocket.readyState == commandWebsocket.CLOSING
            || commandWebsocket.readyState == commandWebsocket.CLOSED) {
            return;
        }

        // check and ping every 3 minutes
        commandWebsocketPingTimeout = setTimeout(commandWebsocketPingFunc, 3 * 60 * 1000);
        // initial set up does not send ping
        if (!skipPing) {
            commandWebsocket.send("ping");
        }
    };

    // it is not an obvious place to init the websocket, but we need lesson ID
    commandWebsocket = new WebSocket(LAMS_URL .replace('http', 'ws')
        + 'learning/commandWebsocket?lessonID=' + lessonId);

    commandWebsocket.onclose = function (e) {
        // check reason and whether the close did not happen immediately after websocket creation
        // (possible access denied, user logged out?)
        if (e.code === 1006 &&
            Date.now() - commandWebsocketInitTime > 1000 &&
            commandWebsocketReconnectAttempts < 20) {
            commandWebsocketReconnectAttempts++;
            // maybe iPad went into sleep mode?
            // we need this websocket working, so init it again after delay
            setTimeout(initCommandWebsocket, 3000);
        }
    };

    // set up timer for the first time
    commandWebsocketPingFunc(true);

    // when the server pushes new commands
    commandWebsocket.onmessage = function (e) {
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
                showToast(command.message);
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
                lessonId: lessonId
            });
        }

        // reset ping timer
        clearTimeout(commandWebsocketPingTimeout);
        commandWebsocketPingFunc(true);
    };

    // check if there is a running discussion; if so, a websocket command will come to display the widget
    $.ajax({
        url: LAMS_URL + "learning/discussionSentiment/checkLearner.do",
        data: {
            lessonId: lessonId
        }
    });
}

function toggleProgressBar(forceClose) {
    let pageContent = $('.component-page-wrapper .component-page-content'),
        progressBar = $('.component-page-wrapper .component-sidebar'),
        topToggleButton = $('header .sidebar-toggle-button', pageContent),
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
        $('.sidebar-toggle-button', progressBar).focus();

        $('body').on('keyup', function (event) {
            if (event.key === "Escape") {
                toggleProgressBar(true);
            }
        });
    }
}