
// refresh progress bar on first/next activity load
function initLearnerPage(toolSessionId, userId) {
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
                    activityItem = $('<li>').text(activityData.name).appendTo(progressBarItems);
                if (activityData.status === 0) {
                    activityItem.addClass('progress-bar-item-current')
                        .prepend('<i class="fa-solid fa-fw fa-circle progress-bar-icon">');
                } else if (activityData.status === 1) {
                    activityItem.addClass('progress-bar-item-complete')
                        .prepend('<i class="fa-solid fa-fw fa-check progress-bar-icon">');
                    if (activityData.url) {
                        activityItem.addClass('progress-bar-item-openable').click(function () {
                            window.open(activityData.url, "_blank");
                        });
                    }
                } else {
                    activityItem.addClass('progress-bar-item-incomplete')
                        .prepend('<i class="fa-regular fa-fw fa-circle progress-bar-icon">');
                }
            }
        }
    });
}

function nextActivity(){
    let contentFrame = $('#learner-page-content-frame');
    contentFrame.on('load', function (){
        location.href = contentFrame[0].contentDocument.URL;
    });
    contentFrame[0].contentWindow.submitForm('finish');
}