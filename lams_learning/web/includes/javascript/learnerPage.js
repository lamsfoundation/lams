
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
            $('#lesson-name').text(result.lessonName);
        }
    });
}