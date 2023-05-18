
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
        url : LAMS_URL + 'learning/learner/getLearnerProgress.do',
        data : {
            'toolSessionID' : toolSessionId,
            'userID'   : userId
        },
        cache : false,
        dataType : 'json',
        success : function(result){
            $('.component-page-wrapper .component-page-content #lesson-name').text(result.lessonName);
            let progressBarItems = $('.component-page-wrapper .component-sidebar #progress-bar-items').empty();

            for (var activityIndex = 0; activityIndex < result.activities.length; activityIndex++) {
                let activityData = result.activities[activityIndex],
                    activityItem = $('<li>').attr('role', 'navigation menubaritem').appendTo(progressBarItems);
                if (activityData.status === 0) {
                    activityItem.addClass('progress-bar-item-current').text(activityData.name)
                        .prepend('<i class="fa-solid fa-fw fa-circle progress-bar-icon">');
                } else if (activityData.status === 1) {
                    activityItem.addClass('progress-bar-item-complete')
                        .prepend('<i class="fa-solid fa-fw fa-check progress-bar-icon">')
                    if (activityData.url) {
                        let activityLink = $('<a>').text(activityData.name).attr({
                            'target' : '_blank',
                            'href' : activityData.url,
                            'role' : 'navigation',
                            'title' : 'Open completed activty "' + activityData.url + '"'
                        });
                        activityItem.addClass('progress-bar-item-openable').append(activityLink);
                    }
                } else {
                    activityItem.addClass('progress-bar-item-incomplete').text(activityData.name)
                        .prepend('<i class="fa-regular fa-fw fa-circle progress-bar-icon">');
                }
            }
        }
    });
}