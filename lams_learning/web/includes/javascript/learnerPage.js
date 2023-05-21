// refresh progress bar on first/next activity load
function initLearnerPage(toolSessionId, userId) {
    $('.component-page-wrapper .sidebar-toggle-button').click(function () {
        let topToggleButton = $('.component-page-wrapper .component-page-content > header .sidebar-toggle-button'),
            isExpanded = topToggleButton.attr('aria-expanded') == 'true';
        topToggleButton.attr('aria-expanded', !isExpanded)
            .children('i').toggleClass(topToggleButton.data('closed-class')).toggleClass(topToggleButton.data('opened-class'));
        $('.component-page-wrapper .component-sidebar').toggleClass('active').attr('aria-expanded', !isExpanded);
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
                        .prepend('<i class="fa-solid fa-fw fa-circle-plus progress-bar-icon">').appendTo(supportBarItems);
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
                        activityIcon.addClass('fa-circle');
                    }
                } else if (activityData.status === 1) {
                    completedActivityCount++;

                    activityItem.addClass('progress-bar-item-complete').prepend(activityIcon);
                    if (activityData.type === 'g') {
                        activityIcon.addClass('fa-hourglass-full');
                    } else {
                        activityIcon.addClass('fa-check');
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
                        activityIcon.addClass('fa-circle');
                    }
                }
            });

            let progressBarWidgetValue = Math.round(completedActivityCount / result.activities.length * 100),
                progressBarWidget = $('.component-page-wrapper .component-page-content #progress-bar-widget');
            $('.progress-bar', progressBarWidget)
                .css('width', progressBarWidgetValue + '%')
                .attr('aria-valuenow', progressBarWidgetValue);
            $('#progress-bar-widget-value').text(progressBarWidgetValue + '%');
        }
    });
}

function showMyProfileDialog() {
    let dialog = showDialog("dialogMyProfile", {
        'title' : "My profile",
        'modal' : true,
        'width' : Math.max(380, Math.min(1000, $(window).width() - 60)),
        'height' : 430,
        'open' : function() {
            var dialog = $(this);
            // load contents after opening the dialog
            $('iframe', dialog).attr({'src': LAMS_URL + 'index.do?redirect=profile',
                'id' : 'myProfileModal'});

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

    dialog.children('.modal-dialog').addClass('modal-lg');
}
