<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL/></c:set>
<%-- param has higher level for request attribute --%>
<c:if test="${not empty param.sessionMapID}">
    <c:set var="sessionMapID" value="${param.sessionMapID}" />
</c:if>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="mode" value="${sessionMap.mode}" />
<c:set var="toolSessionID" value="${sessionMap.toolSessionID}" />
<c:set var="assessment" value="${sessionMap.assessment}" />
<c:set var="pageNumber" value="${sessionMap.pageNumber}" />
<c:set var="isResubmitAllowed" value="${sessionMap.isResubmitAllowed}" />
<c:set var="hasEditRight" value="${sessionMap.hasEditRight}"/>
<c:set var="secondsLeft" value="${sessionMap.secondsLeft}"/>
<c:set var="result" value="${sessionMap.assessmentResult}" />
<c:set var="isUserLeader" value="${sessionMap.isUserLeader}"/>
<c:set var="isLeadershipEnabled" value="${assessment.useSelectLeaderToolOuput}"/>
<!-- hasEditRight=${hasEditRight} -->

<lams:PageLearner title="${assessment.title}" toolSessionID="${toolSessionID}">
    <link href="<lams:WebAppURL/>includes/css/assessment.css" rel="stylesheet" type="text/css">
    <link rel="stylesheet" type="text/css" href="${lams}css/jquery.countdown.css" />
    <link rel="stylesheet" type="text/css" href="${lams}css/jquery.jgrowl.css" />
    <c:if test="${not empty codeStyles}">
        <link rel="stylesheet" type="text/css" href="${lams}css/codemirror.css" />
        <link rel="stylesheet" type="text/css" href="${lams}css/codemirror_simplescrollbars.css" />
    </c:if>

    <script type="text/javascript" src="${lams}includes/javascript/jquery.plugin.js"></script>
    <script type="text/javascript" src="${lams}includes/javascript/jquery.form.js"></script>
    <script type="text/javascript" src="${lams}includes/javascript/jquery.countdown.js"></script>
    <script type="text/javascript" src="${lams}includes/javascript/jquery.blockUI.js"></script>
    <script type="text/javascript" src="${lams}includes/javascript/jquery.jgrowl.js"></script>
    <script type="text/javascript" src="${lams}includes/javascript/Sortable.js"></script>
    <c:if test="${not empty codeStyles}">
        <script type="text/javascript" src="${lams}includes/javascript/codemirror/codemirror.js"></script>
        <script type="text/javascript" src="${lams}includes/javascript/codemirror/addon/scroll/simplescrollbars.js"></script>
        <script type="text/javascript" src="${lams}includes/javascript/codemirror/addon/selection/active-line.js"></script>
        <c:if test="${hasEditRight}">
            <script type="text/javascript" src="${lams}includes/javascript/codemirror/addon/comment/continuecomment.js"></script>
            <script type="text/javascript" src="${lams}includes/javascript/codemirror/addon/edit/closebrackets.js"></script>
            <script type="text/javascript" src="${lams}includes/javascript/codemirror/addon/edit/matchbrackets.js"></script>
        </c:if>
        <script type="text/javascript">
            CodeMirror.defaults.readOnly = ${!hasEditRight};
            CodeMirror.defaults.lineNumbers = true;
            CodeMirror.defaults.continueComments = true;
            CodeMirror.defaults.autoCloseBrackets = true;
            CodeMirror.defaults.matchBrackets = true;
            CodeMirror.defaults.scrollbarStyle = 'simple';
            CodeMirror.defaults.styleActiveLine = true;
        </script>
    </c:if>
    <%-- codeStyles is a set, so each code style will be listed only once --%>
    <c:forEach items="${codeStyles}" var="codeStyle">
        <c:choose>
            <c:when test="${codeStyle == 1}">
                <script type="text/javascript" src="${lams}includes/javascript/codemirror/mode/python.js"></script>
            </c:when>
            <c:when test="${codeStyle == 2}">
                <script type="text/javascript" src="${lams}includes/javascript/codemirror/mode/javascript.js"></script>
            </c:when>
            <c:when test="${codeStyle >= 3}">
                <script type="text/javascript" src="${lams}includes/javascript/codemirror/mode/clike.js"></script>
            </c:when>
        </c:choose>
    </c:forEach>
    <script type="text/javascript">
        var skipValidation = false;

        $(document).ready(function(){
            //if isLeadershipEnabled - enable/disable submit buttons for hedging marks type of questions
            if (${isLeadershipEnabled}) {
                $(".mark-hedging-select").on('change keydown keypress keyup paste', function() {

                    var questionIndex = $(this).data("question-index");
                    var selects = $("select[name^=question" + questionIndex + "_]");
                    var maxMark = selects.length == 0 ? 0 : eval(selects.first().find('option:last-child').val())
                    var totalSelected = countHedgeQuestionSelectTotal(questionIndex);

                    var isButtonEnabled = (totalSelected == maxMark);

                    //if totalSelected equals to question's maxMark - show button
                    if (isButtonEnabled) {
                        $("[type=button][name=submit-hedging-question" + questionIndex + "]").prop("disabled", "").removeClass("button-disabled");
                    } else {
                        $("[type=button][name=submit-hedging-question" + questionIndex + "]").prop("disabled", "true").addClass("button-disabled");
                    }
                }).trigger("change");
            }

            //init options sorting feature
            if (${hasEditRight}) {
                $(".ordering-question-type").each(function () {
                    new Sortable($(this)[0], {
                        animation: 150,
                        ghostClass: 'sortable-placeholder',
                        direction: 'vertical',
                        store: {
                            set: function (sortable) {
                                //update all sequenceIds in order to later save it as options' order
                                for (var i = 0; i < sortable.el.children.length; i++) {
                                    var divContainingInput = sortable.el.children[i];
                                    var input = $("input", $(divContainingInput));
                                    input.val(i);
                                }
                            }
                        }
                    });
                });
            }

            initWebsocket('assessmentTimeLimit${sessionMap.assessment.contentId}',
                    '<lams:WebAppURL />'.replace('http', 'ws')
                    + 'learningWebsocket?toolContentID=${sessionMap.assessment.contentId}',
                    function (e) {
                        // read JSON object
                        var input = JSON.parse(e.data);

                        if (input.clearTimer == true) {
                            // teacher stopped the timer, destroy it
                            $('#countdown').countdown('destroy').remove();
                        } else {
                            // teacher updated the timer
                            var secondsLeft = +input.secondsLeft,
                                    counterInitialised = $('#countdown').length > 0;

                            if (counterInitialised) {
                                // just set the new time
                                $('#countdown').countdown('option', 'until', secondsLeft + 'S');
                            } else {
                                // initialise the timer
                                displayCountdown(secondsLeft);
                            }
                        }

                        // reset ping timer
                        websocketPing('assessmentTimeLimit${sessionMap.assessment.contentId}', true);
                    });

            //autocomplete for VSA
            $('.ui-autocomplete-input').each(function(){
                $(this).autocomplete({
                    'source' : '<c:url value="/learning/vsaAutocomplete.do"/>?questionUid=' + $(this).data("question-uid"),
                    'delay'  : 500,
                    'minLength' : 3
                });
            });

            // show etherpads only on Discussion expand
            $('.question-etherpad-collapse').on('show.bs.collapse', function(){
                var etherpad = $('.etherpad-container', this);
                if (!etherpad.hasClass('initialised')) {
                    var id = etherpad.attr('id'),
                        groupId = id.substring('etherpad-container-'.length);
                    etherpadInitMethods[groupId]();
                }
            });

            //check if we came back due to failed answers' validation (missing required question's answer or min words limit not reached)
            if (${param.isAnswersValidationFailed == true}) {
                validateAnswers();
            }

            <%-- Connect to command websocket only if it is learner UI --%>
            <c:if test="${isLeadershipEnabled and mode == 'learner'}">
            // command websocket stuff for refreshing
            // trigger is an unique ID of page and action that command websocket code in Page.tag recognises
            commandWebsocketHookTrigger = 'assessment-leader-triggered-refresh-${toolSessionID}';
            // if the trigger is recognised, the following action occurs
            commandWebsocketHook = function() {
                location.reload();
            };
            </c:if>

            <c:if test="${sessionMap.autoexpandJustification and (!isLeadershipEnabled or isUserLeader)}">
            // auto expand justification panel on first click
            $('.question-area').find('input, select, .ordering-option').on('click change', function(){
                $(this).closest('.question-area').children('.answer-justification-container:not(.autoopened)')
                    .addClass('autoopened').children('.collapse').collapse('show');
            });
            </c:if>
        });

        function countHedgeQuestionSelectTotal(questionIndex) {
            var totalSelected = 0;
            $("select[name^=question" + questionIndex + "_]").each(function() {
                totalSelected += eval(this.value);
            });
            return totalSelected;
        }

			var counterInitialised = false;

        function displayCountdown(secondsLeft){
            var countdown = '<div id="countdown" role="timer"></div>' +
                '<div id="screenreader-countdown" aria-live="polite" class="visually-hidden" aria-atomic="true"></div>';

            $.blockUI({
                message: countdown,
                showOverlay: false,
                focusInput: false,
                css: {
                    top: '40px',
                    left: '',
                    right: '0%',
                    opacity: '.8',
                    width: '230px',
                    cursor: 'default',
                    border: 'none'
                }
            });

            $('#countdown').countdown({
                until: '+' + secondsLeft +'S',
                format: 'hMS',
                compact: true,
                alwaysExpire : true,
                onTick: function(periods) {
                    // check for 30 seconds or less and display timer in red
                    var secondsLeft = $.countdown.periodsToSeconds(periods);
                    if (secondsLeft <= 30) {
                        $(this).addClass('countdown-timeout');
                    } else {
                        $(this).removeClass('countdown-timeout');
                    }

                    //handle screenreaders
                    var screenCountdown = $("#screenreader-countdown");
                    var hours = $("#countdown").countdown('getTimes')[4];
                    var minutes = $("#countdown").countdown('getTimes')[5];
                    if (screenCountdown.data("hours") != hours || screenCountdown.data("minutes") != minutes) {
                        var timeLeftText = "<spring:escapeBody javaScriptEscape='true'><fmt:message key='label.countdown.time.left' /></spring:escapeBody> ";
                        if (hours > 0) {
                            timeLeftText += hours + " <spring:escapeBody javaScriptEscape='true'><fmt:message key='label.hours' /></spring:escapeBody> ";
                        }
                        timeLeftText += minutes + " <spring:escapeBody javaScriptEscape='true'><fmt:message key='label.minutes' /></spring:escapeBody> ";
                        screenCountdown.html(timeLeftText);

                        screenCountdown.data("hours", hours);
                        screenCountdown.data("minutes", minutes);
                    }
                },
                <c:if test="${hasEditRight && (mode != 'teacher')}">
                onExpiry: function(periods) {
                    if (isWebsocketClosed('assessmentTimeLimit${sessionMap.assessment.contentId}')){
                        console.error('Time limit websocket closed on time expire, reloading page');
                        alert('Connection issue. The page will now reload.');
                        document.location.reload();
                        return;
                    }
                    $.blockUI({
                        message: '<h1 id="timelimit-expired" role="alert">' +
                            '<i class="fa fa-refresh fa-spin fa-1x fa-fw"></i> ' +
                            '<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.learning.blockui.time.is.over" /></spring:escapeBody>' +
                            '</h1>'
                    });

                    setTimeout(function() {
                        submitAll(true);
                    }, 4000);
                },
                </c:if>
                description: "<div id='countdown-label'><spring:escapeBody javaScriptEscape='true'><fmt:message key='label.learning.countdown.time.left' /></spring:escapeBody></div>"
            });
        }

        //autosave feature
        <c:if test="${hasEditRight && (mode != 'teacher')}">
        var autosaveInterval = ${isLeadershipEnabled and isUserLeader ? 10000 : 30000}; // 30 or 10 seconds interval

        function learnerAutosave(isCommand){
            // isCommand means that the autosave was triggered by force complete or another command websocket message
            // in this case do not check multiple tabs open, just autosave
            if (!isCommand) {
                let shouldAutosave = preventLearnerAutosaveFromMultipleTabs(autosaveInterval);
                if (!shouldAutosave) {
                    return;
                }
            }

            if (typeof CodeMirror != 'undefined') {
                $('.CodeMirror').each(function(){
                    this.CodeMirror.save();
                });
            }

            //copy value from CKEditor (only available in essay type of questions) to textarea before ajax submit
            $("textarea[id^='question']").each(function()  {
                var ckeditorData = CKEDITOR.instances[this.name].getData();
                //skip out empty values
                this.value = ((ckeditorData == null) || (ckeditorData.replace(/&nbsp;| |<br \/>|\s|<p>|<\/p>|\xa0/g, "").length == 0)) ? "" : ckeditorData;
            });

            // copy value from lams:textarea (only available in essay and mark hedging type of questions) to hidden input before ajax submit
            $("textarea[name$=__textarea]").change();

            //ajax form submit
            $('#answers').ajaxSubmit({
                url: "<c:url value='/learning/autoSaveAnswers.do'/>?sessionMapID=${sessionMapID}&date=" + new Date().getTime(),
                success: function(response) {
                    if (response != 'ok') {
                        onLearnerAutosaveError();
                        return;
                    }

                    $.jGrowl(
                        "<span aria-live='polite'>" +
                        "<i class='fa fa-lg fa-floppy-o'></i> " +
                        "<spring:escapeBody javaScriptEscape='true'><fmt:message key='label.learning.draft.autosaved' /></spring:escapeBody>" +
                        "</span>",
                        { life: 2000, closeTemplate: '' }
                    );
                },
                error : onLearnerAutosaveError
            });
        }
        window.setInterval(learnerAutosave, autosaveInterval);

        function onLearnerAutosaveError() {
            alert('<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.learning.draft.autosave.error" /></spring:escapeBody>');
            location.reload();
        }
        </c:if>

        function disableButtons() {
            $('.btn').prop('disabled',true);
        }

        function nextPage(pageNumber){
            if (${assessment.questionsPerPage != -1} && !validateAnswers()) {
                return;
            }
            disableButtons();
            var myForm = $("#answers");
            myForm.attr("action", "<c:url value='/learning/nextPage.do?sessionMapID=${sessionMapID}&pageNumber='/>" + pageNumber);
            myForm.submit();
        }

        function submitAll(isTimelimitExpired){
            if (typeof CodeMirror != 'undefined') {
                $('.CodeMirror').each(function(){
                    this.CodeMirror.save();
                });
            }

            // only if time limit is not expired
            if (!isTimelimitExpired && !validateAnswers()) {
                return;
            }

            disableButtons();

            // copy value from lams:textarea (only available in essay and mark hedging type of questions) to hidden input before submit
            $("textarea[name$=__textarea]").change();

            var myForm = $("#answers");
            myForm.attr("action", "<c:url value='/learning/submitAll.do?sessionMapID=${sessionMapID}'/>&isTimelimitExpired=" + isTimelimitExpired);
            myForm.submit();
        }

        function submitSingleMarkHedgingQuestion(singleMarkHedgingQuestionUid, questionIndex){
            //only if time limit is not expired
            if ((typeof isTimelimitExpired !== 'undefined') && isTimelimitExpired) {
                return;
            }

            //ajax form submit
            $('#answers').ajaxSubmit({
                url: "<c:url value='/learning/submitSingleMarkHedgingQuestion.do'/>?sessionMapID=${sessionMapID}&singleMarkHedgingQuestionUid=" + singleMarkHedgingQuestionUid +"&questionIndex="+ questionIndex +"&date=" + new Date().getTime(),
                target: '#mark-hedging-question-' + singleMarkHedgingQuestionUid,
                success: function() {
                    $('#question-area-' + questionIndex).removeClass('bg-warning');
                }
            });
        }

        function validateAnswers() {
            if (${!hasEditRight}) {
                return true;
            }

            if (typeof CodeMirror != 'undefined') {
                $('.CodeMirror').each(function(){
                    this.CodeMirror.save();
                });
            }

            // copy value from lams:textarea (only available in essay and mark hedging type of questions) to hidden input before submit
            $("textarea[name$=__textarea]").change();

            var missingRequiredQuestions = [];
            var minWordsLimitNotReachedQuestions = [];
            var markHedgingWrongTotalQuestions = [];

            <c:forEach var="question" items="${sessionMap.pagedQuestions[pageNumber-1]}" varStatus="status">
            <c:if test="${question.answerRequired}">
            <c:choose>
            <c:when test="${question.type == 1}">
            //multiplechoice
            if ($("input[name^=question${status.index}]:checked").length == 0) {
                missingRequiredQuestions.push("${status.index}");
            }
            </c:when>

            <c:when test="${question.type == 2}">
            //matchingpairs

            var eachSelectHasBeenAnswered = true;
            $("select[name^=question${status.index}_]").each(function() {
                eachSelectHasBeenAnswered &= this.value != -1;
            });

            if (!eachSelectHasBeenAnswered) {
                missingRequiredQuestions.push("${status.index}");
            }

            </c:when>

            <c:when test="${(question.type == 3) || (question.type == 4) || (question.type == 6 && !question.allowRichEditor and empty question.codeStyle)}">
            // short answer or numerical or essay without ckeditor or code style
            var inputText = $("input[name=question${status.index}]")[0];
            if($.trim(inputText.value).length == 0) {
                missingRequiredQuestions.push("${status.index}");
            }
            </c:when>


            <c:when test="${question.type == 5}">
            //truefalse
            if ($("input[name=question${status.index}]:checked").length == 0) {
                missingRequiredQuestions.push("${status.index}");
            }
            </c:when>

            <c:when test="${question.type == 6 && question.allowRichEditor}">
            // essay with ckeditor
            var ckeditorData = CKEDITOR.instances["question${status.index}"].getData();
            //can't be null and empty value
            if((ckeditorData == null) || (ckeditorData.replace(/&nbsp;| |<br \/>|\s|<p>|<\/p>|\xa0/g, "").length == 0)) {
                missingRequiredQuestions.push("${status.index}");
            }
            </c:when>

            <c:when test="${question.type == 6 && not empty question.codeStyle}">
            // essay with code style
            var textarea = $("textarea[name=question${status.index}]"),
                codeMirror = textarea.siblings('.CodeMirror')[0],
                text = codeMirror.CodeMirror.doc.getValue();

            if($.trim(text).length == 0) {
                missingRequiredQuestions.push("${status.index}");
            }
            </c:when>

            <c:when test="${question.type == 7}">
            //ordering - do nothing
            </c:when>

            <c:when test="${question.type == 8}">
            //mark hedging - processed below
            </c:when>
            </c:choose>
            </c:if>

            //essay question which has min words limit
            <c:if test="${question.type == 6 && question.minWordsLimit != 0}">
            var wordCount${status.index} = $('#word-count${status.index}').text();
            //min words limit is not reached
            if(wordCount${status.index} < ${question.minWordsLimit}) {
                minWordsLimitNotReachedQuestions.push("${status.index}");
            }
            </c:if>

            //mark hedging question and leader aware feature is ON => question is required to answer
            <c:if test="${question.type == 8}">

            var questionIndex = ${status.index};

            if (${isLeadershipEnabled}) {
                if ($("[type=button][name=submit-hedging-question"+ questionIndex +"]").length > 0) {
                    markHedgingWrongTotalQuestions.push("${status.index}");
                }

            } else {
                var maxMark = ${question.maxMark};
                var totalSelected = countHedgeQuestionSelectTotal(questionIndex);

                //if totalSelected not equals to question's maxMark OR textarea is empty or contains only white-space - show warning
                if (totalSelected != maxMark) {
                    markHedgingWrongTotalQuestions.push("${status.index}");
                }
            }

            </c:if>

            </c:forEach>

            //return true in case all required questions were answered, false otherwise
            if (missingRequiredQuestions.length == 0 && minWordsLimitNotReachedQuestions.length == 0
                && markHedgingWrongTotalQuestions.length == 0) {
                return true;

            } else {
                <c:if test="${mode == 'author'}">
                if (!skipValidation) {
                    alert('<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.authoring.preview.skip.validation" /></spring:escapeBody>');
                    skipValidation = true;
                } else {
                    return true;
                }
                </c:if>

                //remove .bg-warning from all questions
                $('[id^=question-area-]').removeClass('bg-warning');
                $('#warning-answers-required').hide();
                $('#warning-words-limit').hide();
                $('#warning-mark-hedging-wrong-total').hide();

                if (missingRequiredQuestions.length != 0) {
                    //add .bg-warning class to those needs to be filled
                    for (i = 0; i < missingRequiredQuestions.length; i++) {
                        $("#question-area-" + missingRequiredQuestions[i]).addClass('bg-warning');
                    }
                    //show alert message as well
                    $("#warning-answers-required").show();

                }

                if (minWordsLimitNotReachedQuestions.length != 0) {
                    //add .bg-warning class to those needs to be filled
                    for (i = 0; i < minWordsLimitNotReachedQuestions.length; i++) {
                        $("#question-area-" + minWordsLimitNotReachedQuestions[i]).addClass('bg-warning');
                    }
                    //show alert message as well
                    $("#warning-words-limit").show();
                }

                if (markHedgingWrongTotalQuestions.length != 0) {
                    //add .bg-warning class to those needs to be filled
                    for (i = 0; i < markHedgingWrongTotalQuestions.length; i++) {
                        $("#question-area-" + markHedgingWrongTotalQuestions[i]).addClass('bg-warning');
                    }
                    //show alert message as well
                    $("#warning-mark-hedging-wrong-total").show();
                }

                $("html, body").animate({ scrollTop: 0 }, "slow");//window.scrollTo(0, 0);

                return false;
            }
        }

        function getNumberOfWords(value) {
            var wordsForCounting = value.trim().replace(/['";:,.?\-!]+/g, '');
            return value ? (value.match(/\S+/g) || []).length : 0;
        }

        function logLearnerInteractionEvent(eventType, qbToolQuestionUid, optionUid) {
            $.ajax({
                url: '<c:url value="/learning/logLearnerInteractionEvent.do"/>',
                data: {
                    eventType         : eventType,
                    qbToolQuestionUid : qbToolQuestionUid,
                    optionUid         : optionUid
                },
                cache : false,
                type  : 'post',
                dataType : 'text'
            });
        }
    </script>

    <div id="container-main">
        <c:if test="${not empty sessionMap.submissionDeadline && (sessionMap.mode == 'author' || sessionMap.mode == 'learner')}">
            <lams:Alert5 id="submission-deadline" type="info" close="true">
                <fmt:message key="authoring.info.teacher.set.restriction" >
                    <fmt:param><lams:Date value="${sessionMap.submissionDeadline}" /></fmt:param>
                </fmt:message>
            </lams:Alert5>
        </c:if>

        <c:if test="${isLeadershipEnabled}">
            <lams:LeaderDisplay idName="leader-info"
                                username="${sessionMap.groupLeader.firstName} ${sessionMap.groupLeader.lastName}" userId="${sessionMap.groupLeader.userId}"/>
        </c:if>

        <c:if test="${assessment.allowDiscloseAnswers}">
            <lams:Alert5 id="learning-disclose-info" type="info" close="true">
                <fmt:message key="label.learning.disclose.tip" />
            </lams:Alert5>
        </c:if>

        <c:if test="${hasEditRight}">
            <lams:Alert5 id="learning-submit-all-info" type="info" close="true">
                <fmt:message key="label.learning.submit.all.tip" />
            </lams:Alert5>
        </c:if>

        <div id="instructions" class="instructions">
            <c:out value="${assessment.instructions}" escapeXml="false"/>
        </div>

        <lams:Alert5 id="warning-answers-required" type="warning" close="true">
            <fmt:message key="warn.answers.required" />
        </lams:Alert5>

        <lams:Alert5 id="warning-words-limit" type="warning" close="true">
            <fmt:message key="warn.answers.word.requirements.limit" />
        </lams:Alert5>

        <lams:Alert5 id="warning-mark-hedging-wrong-total" type="warning" close="true">
            <fmt:message key="warn.mark.hedging.wrong.total" />
        </lams:Alert5>

        <lams:errors/>

        <%@ include file="parts/paging.jsp"%>

        <div id="all-questions">
            <%@ include file="parts/allquestions.jsp"%>
        </div>

        <%@ include file="parts/paging.jsp"%>

        <div class="activity-bottom-buttons">
            <c:if test="${mode != 'teacher' && hasEditRight}">
                <button type="button" name="submitAll" onclick="return submitAll(false);" class="btn btn-primary na">
                    <fmt:message key="label.learning.submit.all" />
                </button>
            </c:if>
        </div>
    </div>
</lams:PageLearner>