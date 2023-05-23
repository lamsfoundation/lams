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
                    let activityItem = $('<li>').attr('role', 'presentation').addClass('support-bar-item progress-bar-item-openable'),
                        activityIconUrl = getActivityIconUrl(activityData),
                        activityIcon = null;

                    if (activityIconUrl) {
                        activityIcon = $('<img class="progress-bar-icon">').attr('src', LAMS_URL + activityIconUrl);
                    } else {
                        activityIcon = $('<i class="fa-solid fa-fw fa-up-right-from-square progress-bar-icon"></i>');
                    }

                    let activityLink = $('<a>').text(activityData.name).attr({
                        'target': '_blank',
                        'href': activityData.url,
                        'role': 'menuitem',
                        'title': 'Open completed activity'
                    });
                    activityItem.addClass('progress-bar-item-openable').append(activityIcon).append(activityLink)
                        .appendTo(supportBarItems);
                });
            }

            let progressBarItems = $('.component-page-wrapper .component-sidebar #progress-bar-items').empty(),
                completedActivityCount = 0;
            $.each(result.activities, function (activityIndex, activityData) {
                let activityItem = $('<li>').attr('role', 'presentation').appendTo(progressBarItems),
                    activityName = !activityData.name && activityData.type === 'g' ? 'Gate' : activityData.name,
                    activityIconUrl = getActivityIconUrl(activityData),
                    activityIcon = null;

                if (activityIconUrl) {
                    activityIcon = $('<img class="progress-bar-icon">').attr('src', LAMS_URL + activityIconUrl);
                } else {
                    activityIcon = $('<i class="fa-solid fa-fw progress-bar-icon"></i>');
                }

                if (activityData.status === 0) {
                    activityItem.addClass('progress-bar-item-current').append(activityIcon)
                        .append($('<span class="progress-bar-activity-name">').text(activityName));
                    if (!activityIconUrl) {
                        activityIcon.addClass('fa-pen-to-square');
                    }
                } else if (activityData.status === 1) {
                    completedActivityCount++;

                    activityItem.addClass('progress-bar-item-complete').prepend(activityIcon);
                    if (!activityIconUrl) {
                        activityIcon.addClass('fa-square-check');
                    }
                    if (activityData.url) {
                        let activityLink = $('<a class="progress-bar-activity-name">').text(activityName).attr({
                            'target': '_blank',
                            'href': activityData.url,
                            'role': 'menuitem',
                            'title': 'Open completed activity'
                        });
                        activityItem.addClass('progress-bar-item-openable').append(activityLink);
                    }
                } else {
                    activityItem.addClass('progress-bar-item-incomplete').append(activityIcon)
                        .append($('<span class="progress-bar-activity-name">').text(activityName));
                    if (!activityIconUrl) {
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

function getActivityIconUrl(activityData){
    if (activityData.iconURL){
        return activityData.iconURL;
    }
    if (activityData.type === 'g') {
        return 'images/svg/gate' + (activityData.gateOpen ? 'Open' : 'Closed') + '.svg';
    }
    if (activityData.isGrouping) {
        return 'images/svg/grouping.svg';
    }
    if (activityData.type === 'b') {
        return 'images/svg/branchingStart.svg';
    }
    if (activityData.type === 'o') {
        return 'images/svg/optional.svg';
    }
}