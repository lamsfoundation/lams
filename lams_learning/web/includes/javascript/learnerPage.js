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
                    let activityItem = $('<li>').attr('role', 'navigation menubaritem')
                        .addClass('support-bar-item progress-bar-item-openable')
                        .prepend('<i class="fa-solid fa-fw fa-circle-plus progress-bar-icon">').appendTo(supportBarItems);
                    let activityLink = $('<a>').text(activityData.name).attr({
                        'target': '_blank',
                        'href': activityData.url,
                        'role': 'navigation',
                        'title': 'Open completed activty "' + activityData.url + '"'
                    });
                    activityItem.addClass('progress-bar-item-openable').append(activityLink);
                });
            }

            let progressBarItems = $('.component-page-wrapper .component-sidebar #progress-bar-items').empty();
            $.each(result.activities, function (activityIndex, activityData) {
                let activityItem = $('<li>').attr('role', 'navigation menubaritem').appendTo(progressBarItems),
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
                            'role': 'navigation',
                            'title': 'Open completed activty "' + activityData.url + '"'
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
        }
    });
}