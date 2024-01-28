<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<c:set var="lams">
    <lams:LAMSURL />
</c:set>
<c:set var="sessionMap" value="${sessionScope[generalLearnerFlowDTO.sessionMapID]}" scope="request" />
<c:set var="isUserLeader" value="${sessionMap.isUserLeader}" scope="request" />
<c:set var="mode" value="${sessionMap.mode}" scope="request" />
<c:set var="isLeadershipEnabled" value="${sessionMap.content.useSelectLeaderToolOuput}" scope="request" />
<c:set var="hasEditRight" value="${!isLeadershipEnabled || isLeadershipEnabled && isUserLeader}" scope="request" />
<c:set var="localeLanguage"><lams:user property="localeLanguage" /></c:set>
<fmt:message key="activity.title" var="title"/>

<lams:PageLearner title="${generalLearnerFlowDTO.activityTitle}" toolSessionID="${sessionMap.toolSessionID}" >

    <style media="screen,projection" type="text/css">
        div.growlUI h1, div.growlUI h2 {
            color: white;
            margin: 5px 5px 5px 0px;
            text-align: center;
            font-size: 18px;
        }
    </style>

    <div id="container-main">
        <!-- Advanced settings and notices -->

        <c:if test="${generalLearnerFlowDTO.noReeditAllowed}">
            <lams:Alert5 type="danger" id="noRedosAllowed" close="false">
                <fmt:message key="label.noredo.enabled" />
            </lams:Alert5>
        </c:if>

        <c:if test="${not empty sessionMap.submissionDeadline}">
            <lams:Alert5 type="danger" id="submission-deadline" close="false">
                <fmt:message key="authoring.info.teacher.set.restriction">
                    <fmt:param>
                        <lams:Date value="${sessionMap.submissionDeadline}" />
                    </fmt:param>
                </fmt:message>
            </lams:Alert5>
        </c:if>

        <c:if test="${isLeadershipEnabled}">
            <lams:LeaderDisplay idName="leader-enabled" username="${sessionMap.groupLeader.fullname}" userId="${sessionMap.groupLeader.queUsrId}"/>
        </c:if>

        <!-- End advanced settings and notices -->

        <div id="instructions" class="instructions">
            <c:out value="${generalLearnerFlowDTO.activityInstructions}" escapeXml="false" />
        </div>

        <!-- form needs to be outside page so that the form bean can be picked up by Page tag. -->
        <form:form action="${generalLearnerFlowDTO.questionListingMode == 'questionListingModeSequential' ? 'getNextQuestion.do' : 'submitAnswersContent.do'}"
                   method="POST" target="_self" id="qaLearningForm" modelAttribute="qaLearningForm">
            <form:hidden path="toolSessionID" id="tool-session-id" />
            <form:hidden path="userID" />
            <form:hidden path="sessionMapID" />
            <form:hidden path="questionIndex" />
            <form:hidden path="totalQuestionCount" />

            <div id="errors">
                <lams:errors5/>
            </div>

            <div id="questions">
                <c:choose>
                    <c:when test="${(generalLearnerFlowDTO.questionListingMode == 'questionListingModeSequential') && hasEditRight}">
                        <c:if test="${generalLearnerFlowDTO.totalQuestionCount != 1}">
                            <c:if test="${generalLearnerFlowDTO.initialScreen == 'true'}">
                                <lams:Alert5 type="info" id="feedback-seq" close="false">
                                    <fmt:message key="label.feedback.seq" />&nbsp;
                                    <c:out value="${generalLearnerFlowDTO.remainingQuestionCount}" />&nbsp;
                                    <fmt:message key="label.questions.simple" />
                                </lams:Alert5>
                            </c:if>
                        </c:if>

                        <c:if test="${generalLearnerFlowDTO.initialScreen != 'true'}">
                            <lams:Alert5 type="info" id="questions-remaining" close="false">
                                <fmt:message key="label.questions.remaining" />&nbsp;
                                <c:out value="${generalLearnerFlowDTO.remainingQuestionCount}" />&nbsp;
                            </lams:Alert5>
                        </c:if>

                        <jsp:include page="/learning/SequentialAnswersContent.jsp" />
                    </c:when>

                    <c:otherwise>
                        <c:if test="${generalLearnerFlowDTO.totalQuestionCount != 1}">
                            <lams:Alert5 type="info" id="feedback-combined" close="false">
                                <fmt:message key="label.feedback.combined" />&nbsp;
                                <c:out value="${generalLearnerFlowDTO.remainingQuestionCount}" />&nbsp;
                                <fmt:message key="label.questions.simple" />
                            </lams:Alert5>
                        </c:if>

                        <jsp:include page="/learning/CombinedAnswersContent.jsp" />
                    </c:otherwise>
                </c:choose>
            </div>
        </form:form>
    </div>

    <lams:JSImport src="includes/javascript/common.js" />
    <script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
    <script type="text/javascript" src="${lams}includes/javascript/jquery.form.js"></script>
    <script type="text/javascript" src="${lams}includes/javascript/jquery.blockUI.js"></script>
    <script type="text/javascript" src="${lams}includes/javascript/jquery.timeago.js"></script>
    <script type="text/javascript" src="${lams}includes/javascript/bootstrap.min.js"></script>
    <script type="text/javascript" src="${lams}includes/javascript/timeagoi18n/jquery.timeago.${fn:toLowerCase(localeLanguage)}.js"></script>
    <script type="text/JavaScript">
        var minWordsLimitLabel = '<fmt:message key="label.minimum.number.words" ><fmt:param>{0}</fmt:param></fmt:message>';

        function submitMethod(actionMethod) {

            //validate answers
            var submit = true;
            if (actionMethod != 'getPreviousQuestion') {

                //check for min words limit
                jQuery(".min-words-limit-enabled").each(function() {

                    var questionId = (${generalLearnerFlowDTO.questionListingMode == 'questionListingModeSequential'}) ? "" : $(this).data("sequence-id");
                    var isCkeditor = $(this).data("is-ckeditor");

                    var value;
                    var instance;
                    if (isCkeditor) {
                        instance = CKEDITOR.instances["answer" + questionId];
                        value = CKEDITOR.instances["answer" + questionId].getData();

                    } else {
                        instance = $("#answer"+ questionId);
                        value =  $("#answer"+ questionId).val();
                    }

                    var numberEnteredWords = getNumberOfWords(value, isCkeditor);

                    var minWordsLimit = $(this).data("min-words-limit");
                    if (numberEnteredWords < minWordsLimit) {
                        var minWordsLimitLabelStr = minWordsLimitLabel.replace("{0}", ": " + minWordsLimit);
                        showToast("<fmt:message key="label.question" /> "+ questionId +". " +  minWordsLimitLabelStr);
                        instance.focus();
                        return submit = false;
                    }
                });

                //check for blank answers
                if (submit) {
                    jQuery(".text-area").each(function() {
                        if (jQuery.trim($(this).val()) == "") {
                            if (confirm("<fmt:message key="warning.empty.answers" />")) {
                                doSubmit(actionMethod);
                                return false;
                            } else {
                                this.focus();
                                return submit = false;
                            }
                        }
                    });
                }
            }

            if (submit) {
                doSubmit(actionMethod);
            }
        }

        function doSubmit(actionMethod) {
            $('.btn').prop('disabled', true);
            document.forms.qaLearningForm.action=actionMethod+".do";
            document.forms.qaLearningForm.submit();
        }

        <c:if test="${!hasEditRight && mode != 'teacher'}">
        setInterval("checkLeaderProgress();",45000);// Auto-Refresh every 1 minute for non-leaders
        </c:if>

        function checkLeaderProgress() {
            $.ajax({
                async: false,
                url: '<c:url value="checkLeaderProgress.do"/>',
                data: 'toolSessionID=' + $("#tool-session-id").val(),
                dataType: 'json',
                type: 'post',
                success: function (json) {
                    if (json.isLeaderResponseFinalized) {
                        location.reload();
                    }
                }
            });
        }

        //autoSaveAnswers if hasEditRight
        if (${hasEditRight}) {

			var interval = "30000", // = 30 seconds
				autosaveWindowId = new Date().getTime(); // all we need for this ID is to be unique;
            window.setInterval(learnerAutosave, interval);

            function learnerAutosave(isCommand){
                // isCommand means that the autosave was triggered by force complete or another command websocket message
                // in this case do not check multiple tabs open, just autosave
                if (!isCommand) {
				  let shouldAutosave = preventLearnerAutosaveFromMultipleTabs(autosaveWindowId, interval);
                    if (!shouldAutosave) {
                        return;
                    }
                }

                //fire onchange event for textareas/ckeditors
                if (${generalLearnerFlowDTO.allowRichEditor}) {
                    for ( instance in CKEDITOR.instances ) {
                        CKEDITOR.instances[instance].updateElement();
                    }
                } else {
                    $("textarea[name$=__textarea]").change();
                }

                //ajax form submit
                $('#qaLearningForm').ajaxSubmit({
                    url: '<c:url value="autoSaveAnswers.do?date=" />' + new Date().getTime(),
                    success: function() {
                        $.growlUI('<i class="fa fa-lg fa-floppy-o"></i> <fmt:message key="label.learning.draft.autosaved" />');
                    }
                });
            }
        }

        //min words counter
        $(document).ready(function() {

            //character count fuction in case ckeditor is OFF
            function counter(event) {
                var questionId = event.data.questionId;
                var minWordsLimit = event.data.minWordsLimit;
                var value = $("#answer"+ questionId).val();

                var numberEnteredWords = getNumberOfWords(value, false);
                var numberRequiredWords = (minWordsLimit > numberEnteredWords) ? minWordsLimit - numberEnteredWords : 0;
                $('#words-required-' + questionId).html(numberRequiredWords);
            };

            //character count fuction in case ckeditor is ON
            function ckeditorCounter(event) {
                var questionId = event.listenerData.questionId;
                var minWordsLimit = event.listenerData.minWordsLimit;
                var value = event.editor.getData();

                var numberEnteredWords = getNumberOfWords(value, true);
                var numberRequiredWords = (minWordsLimit > numberEnteredWords) ? minWordsLimit - numberEnteredWords : 0;
                $('#words-required-' + questionId).html(numberRequiredWords);
            };

            //check for min words limit
            jQuery(".min-words-limit-enabled").each(function() {

                var isCkeditor = $(this).data("is-ckeditor");
                var questionId = (${generalLearnerFlowDTO.questionListingMode == 'questionListingModeSequential'}) ? "" : $(this).data("sequence-id");
                var minWordsLimit = $(this).data("min-words-limit");

                //assign function
                if (isCkeditor) {
                    var ckeditor = CKEDITOR.instances["answer" + questionId];

                    ckeditor.on('change', ckeditorCounter, null, {questionId: questionId, minWordsLimit: minWordsLimit} );
                    //count characters initially
                    ckeditor.on('instanceReady', ckeditorCounter, null, {questionId: questionId, minWordsLimit: minWordsLimit});

                } else {
                    $("#answer" + questionId)
                        .on('change keydown keypress keyup paste', {questionId: questionId, minWordsLimit: minWordsLimit}, counter)
                        .trigger( "change", {questionId: questionId, minWordsLimit: minWordsLimit} );
                }

            });

            <%-- Connect to command websocket only if it is learner UI --%>
            <c:if test="${isLeadershipEnabled and mode == 'learner'}">
            // command websocket stuff for refreshing
            // trigger is an unique ID of page and action that command websocket code in Page.tag recognises
            commandWebsocketHookTrigger = 'qa-leader-change-refresh-${generalLearnerFlowDTO.toolSessionID}';
            // if the trigger is recognised, the following action occurs
            commandWebsocketHook = function() {
                location.reload();
            };
            </c:if>
        });

    </script>

</lams:PageLearner>