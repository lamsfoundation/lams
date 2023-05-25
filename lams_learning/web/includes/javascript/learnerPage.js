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
        }
    });
}

function toggleProgressBar(forceClose) {
    let pageContent = $('.component-page-wrapper .component-page-content'),
        topToggleButton = $('header .sidebar-toggle-button', pageContent),
        isExpanded = forceClose || topToggleButton.attr('aria-expanded') == 'true';
    topToggleButton.attr('aria-expanded', !isExpanded)
        .children('i').toggleClass(topToggleButton.data('closed-class')).toggleClass(topToggleButton.data('opened-class'));
    $('.component-page-wrapper .component-sidebar').toggleClass('active').attr('aria-expanded', !isExpanded);
    $('.component-sidebar').focus();

    pageContent.off('click');
    $('body').off('keyup');

    if (!isExpanded) {
        pageContent.one('click', function (){
            toggleProgressBar(true);
        });
        $('body').on('keyup', function (event){
            if (event.key === "Escape") {
                toggleProgressBar(true);
            }
        });
    }
}