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
<c:set var="scratchie" value="${sessionMap.scratchie}" />
<c:set var="isUserLeader" value="${sessionMap.isUserLeader}" />
<c:set var="isScratchingFinished" value="${sessionMap.isScratchingFinished}" />
<c:set var="hideFinishButton" value="${!isUserLeader && (!isScratchingFinished)}" />

<lams:PageLearner title="${title}" toolSessionID="${toolSessionID}">
    <!-- ********************  CSS ********************** -->
    <link rel="stylesheet" type="text/css" href="${lams}css/jquery-ui-bootstrap-theme.css" />
    <link rel="stylesheet" type="text/css" href="<lams:WebAppURL/>includes/css/scratchie.css">
    <link rel="stylesheet" type="text/css" href="${lams}css/jquery.countdown.css" />
    <link rel="stylesheet" type="text/css" href="${lams}css/jquery.jgrowl.css" />
    <link rel="stylesheet" type="text/css" href="${lams}css/circle.css" />
    <link rel="stylesheet" type="text/css" href="<lams:WebAppURL/>includes/css/scratchie-learning.css" />

    <!-- ********************  javascript ********************** -->
    <script type="text/javascript" src="${lams}includes/javascript/common.js"></script>
    <script>
        //Resolve name collision between jQuery UI and Twitter Bootstrap
        $.widget.bridge('uitooltip', $.ui.tooltip);
    </script>
    <script type="text/javascript" src="${lams}includes/javascript/tabcontroller.js"></script>
    <script type="text/javascript" src="${lams}includes/javascript/jquery.timeago.js"></script>
    <script type="text/javascript" src="${lams}includes/javascript/jquery.plugin.js"></script>
    <script type="text/javascript" src="${lams}includes/javascript/jquery.countdown.js"></script>
    <script type="text/javascript" src="${lams}includes/javascript/jquery.blockUI.js"></script>
    <script type="text/javascript" src="${lams}includes/javascript/jquery.jgrowl.js"></script>
    <script type="text/javascript" src="${lams}includes/javascript/jquery.form.js"></script>
    <script type="text/javascript">
        var isScratching = false,
            requireAllAnswers = <c:out value="${scratchie.requireAllAnswers}" />;

        $(document).ready(function(){
            //handler for VSA input fields
            $('.submit-user-answer-input').keypress(function(event){
                var keycode = (event.keyCode ? event.keyCode : event.which);
                if(keycode == '13') {
                    var itemUid = $(this).data("item-uid");
                    scratchVsa(itemUid);
                    return false;
                }
            });

            //handler for VSA submit buttons
            $(".submit-user-answer").on('click', function(){
                var itemUid = $(this).data("item-uid");
                scratchVsa(itemUid);
                return false;
            });

            //autocomplete for VSA
            $('.ui-autocomplete-input').each(function(){
                $(this).autocomplete({
                    'source' : '<c:url value="/learning/vsaAutocomplete.do"/>?itemUid=' + $(this).data("item-uid"),
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

            <c:if test="${scratchie.revealOnDoubleClick}">
            $('.scratchie-link').on('touchend', function(){
                if (!this.hasAttribute('onDblClick')) {
                    return;
                }

                // allow single touch scratching on iPads even if double click scratching is enabled
                var itemUid = $(this).data('itemUid'),
                    optionUid = $(this).data('optionUid');
                scratchMcq(itemUid, optionUid);
            });
            </c:if>

            // hide Finish button for non-leaders until leader finishes
            if (${hideFinishButton}) {
                $("#finishButton").hide();
            } else if (requireAllAnswers) {
                checkAllCorrectMcqAnswersFound();
            }

            <%-- Connect to command websocket only if it is learner UI --%>
            <c:if test="${mode == 'learner'}">
            // command websocket stuff for refreshing
            // trigger is an unique ID of page and action that command websocket code in Page.tag recognises
            commandWebsocketHookTrigger = 'scratchie-leader-change-refresh-${toolSessionID}';
            // if the trigger is recognised, the following action occurs
            commandWebsocketHook = function() {
                location.reload();
            };
            </c:if>

            //initialize tooltips showing user names next to confidence levels
            $('[data-bs-toggle="tooltip"]').tooltip();
        });

        //Scratch SVG. Function is used by both scratchMcq() and scratchVsa().
        function scratchImage(itemUid, optionUid, isCorrect) {
            // first show animation, then put static image
            var svg = $('#svg-' + itemUid + '-' + optionUid);

            //show VSA question image
            if (svg.css('visibility') != 'visible') {
                svg.css('visibility', 'visible');
            }

            svg.addClass("show-" + (isCorrect ? 'tick' : 'cross'));
        }

        //scratch MCQ answer
        function scratchMcq(itemUid, optionUid){
            if (isScratching) {
                // do not allow parallel scratching
                return;
            }

            isScratching = true;
            $.ajax({
                url: '<c:url value="/learning/recordItemScratched.do"/>',
                data: 'sessionMapID=${sessionMapID}&optionUid=' + optionUid + '&itemUid=' + itemUid,
                dataType: 'json',
                type: 'post',
                success: function (json) {
                    if (json == null) {
                        return false;
                    }

                    scratchImage(itemUid, optionUid, json.optionCorrect);

                    //disable scratching
                    var imageLinkIds = json.optionCorrect ? "[id^=imageLink-" + itemUid + "]" : '#imageLink' + '-' + itemUid + '-' + optionUid;
                    var imageLinks = $(imageLinkIds);
                    imageLinks.removeAttr("href").removeAttr('onClick').removeAttr('onDblClick');
                    imageLinks.css('cursor', 'default');
                    imageLinks.attr('aria-disabled', 'true');

                    if (json.optionCorrect) {
                        //fade all related images
                        $("[id^=svg-" + itemUid + "]").not("#svg-" + itemUid + "-" + optionUid).fadeTo(1300, 0.3);

                        //enable Finish button
                        if (requireAllAnswers) {
                            checkAllCorrectMcqAnswersFound();
                        }
                    }

                },
                complete : function(){
                    // enable scratching again
                    isScratching = false;
                }
            });
        }

        //scratch VSA answer
        function scratchVsa(itemUid) {
            var input = $("#input-" + itemUid),
                answer = input.val();

            if (answer == "") {
                return;
            }

            if (isScratching) {
                // do not allow parallel scratching
                return;
            }

            isScratching = true;

            $.ajax({
                url: '<c:url value="/learning/recordVsaAnswer.do"/>',
                data: {
                    sessionMapID: "${sessionMapID}",
                    itemUid: itemUid,
                    answer: answer
                },
                dataType: 'json',
                type: 'post',
                success: function (json) {
                    if (json == null) {
                        return false;
                    }

                    var loggedAnswerHash = json.loggedAnswerHash,
                        isAnswerUnique = loggedAnswerHash == -1,
                        answerHashToScratch = isAnswerUnique ? hashCode(answer) : loggedAnswerHash
                    trId = "#tr-" + itemUid + "-" + answerHashToScratch;

                    //if answer was not provided yet, add it to the list
                    if (isAnswerUnique) {
                        paintNewVsaAnswer(itemUid, answer);
                    }

                    var isScrathed = $(trId + ' .scratched,' + trId + ' .show-tick,' + trId + ' .show-cross', "#vsa-" + itemUid).length > 0;
                    //highlight already scratched answer
                    if (isScrathed) {
                        var tableRowTohighlight = $(trId, "#vsa-" + itemUid);
                        $([document.documentElement, document.body]).animate(
                            {
                                scrollTop: tableRowTohighlight.offset().top
                            },
                            1000,
                            function() {
                                tableRowTohighlight.effect("highlight", 1500);
                            }
                        );

                        //scratch it otherwise
                    } else {
                        scratchImage(itemUid, answerHashToScratch, json.isAnswerCorrect);

                        if (json.isAnswerCorrect) {
                            //disable further answering
                            $("[id^=svg-" + itemUid + "]").not("#svg-" + itemUid + "-" + answerHashToScratch).fadeTo(1300, 0.3);

                            //disable submit button
                            $("#type-your-answer-" + itemUid).hide();
                        }
                    }
                },
                complete : function(){
                    // enable scratching again
                    isScratching = false;
                }
            });

            //blank input field
            input.val("");
        }

        //add new VSA answer to the table (required in case user entered answer not present in the previous answers)
        function paintNewVsaAnswer(itemUid, answer) {
            var optionsLength = $("#vsa-" + itemUid + " .row").length;
            var svgId = itemUid + '-' + hashCode(answer);

            //get SVG from .jsp
            $.ajax({
                type : "POST",
                async: false,
                url : '<c:url value="/pages/learning/parts/scratchieSvg.jsp"/>',
                data : "type=full&svgId=" + svgId + "&letter=" + optionsLength + "&isHidden=false",
                success : function(scratchieSvg) {
                    var trElem =
                        '<div id="tr-' + svgId + '" class="row">' +
                        '<div class="scartchie-image-col">' +
                        scratchieSvg +
                        '</div>' +

                        '<div class="col answer-with-confidence-level-portrait">' +
                        '<div class="answer-description">' +
                        xmlEscape(answer) +
                        '</div>' +
                        '<hr class="hr-confidence-level" />' +
                        '<div style="padding-bottom: 10px;">' +
                        '<spring:escapeBody javaScriptEscape="true"><lams:Portrait userId="${sessionMap.groupLeaderUserId}"/></spring:escapeBody>' +
                        '</div>' +
                        '</div>' +
                        '</div>';
                    $("#vsa-" + itemUid).append(trElem);
                }
            });
        }

        function checkAllCorrectMcqAnswersFound() {
			var numberOfAvailableScratches = $('[id^=imageLink-][${scratchie.revealOnDoubleClick ? "ondblclick" : "onclick"}]').length;
            if (numberOfAvailableScratches > 0) {
                $('#finishButton')
                    .prop('disabled', true)
                    .css('pointer-events', 'none')
                    .parent().attr('data-title', '<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.learning.require.all.answers" /></spring:escapeBody>')
                    .tooltip();
            } else {
                $('#finishButton')
                    .prop('disabled', false)
                    .css('pointer-events', 'auto')
                    .parent().tooltip('dispose');
            }
        }

        function xmlEscape(value) {
            return value.replace(/&/g, '&amp;')
                .replace(/</g, '&lt;')
                .replace(/>/g, '&gt;')
                .replace(/"/g, '&#034;')
                .replace(/'/g, '&#039;');
        }

        //a direct replacement for Java's String.hashCode() method
        function hashCode(str) {
            var hash = 0;
            if (str.length == 0) {
                return hash;
            }
            for (var i = 0; i < str.length; i++) {
                var char = str.charCodeAt(i);
                hash = ((hash<<5)-hash)+char;
                hash = hash & hash; // Convert to 32bit integer
            }
            return hash;
        }

        // time limit feature
        <c:if test="${mode != 'teacher'}">
        $(document).ready(function(){
            // time limit feature
            initWebsocket('scratchieTimeLimit${scratchie.contentId}',
                '<lams:WebAppURL />'.replace('http', 'ws')
                + 'learningWebsocket?toolSessionID=${toolSessionID}&toolContentID=${scratchie.contentId}',
                function (e) {

                    // create JSON object
                    var input = JSON.parse(e.data);

                    if (input.pageRefresh) {
                        location.reload();
                        return;
                    }

                    if (input.clearTimer == true) {
                        // teacher stopped the timer, destroy it
                        $('#countdown').countdown('destroy').remove();
                    } else if (typeof input.secondsLeft != 'undefined'){
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
                    } else if (${not isUserLeader}){
                        // reflect the leader's choices
                        $.each(input, function(itemUid, options) {
                            $.each(options, function(optionUid, optionProperties){

                                if (optionProperties.isVSA) {
                                    var answer = optionUid;
                                    optionUid = hashCode(optionUid);

                                    //check if such image exists, create it otherwise
                                    if ($('#image-' + itemUid + '-' + optionUid).length == 0) {
                                        paintNewVsaAnswer(eval(itemUid), answer);
                                    }
                                }

                                scratchImage(itemUid, optionUid, optionProperties.isCorrect);
                            });
                        });
                    }

                    // reset ping timer
                    websocketPing('scratchieTimeLimit${scratchie.contentId}', true);
                });
        });

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
                onExpiry: function(periods) {
                    if (isWebsocketClosed('scratchieTimeLimit${scratchie.contentId}')){
                        console.error('Time limit websocket closed on time expire, reloading page');
                        alert('Connection issue. The page will now reload.');
                        document.location.reload();
                        return;
                    }
                    $.blockUI({
                        message: '<h1 id="timelimit-expired" role="alert">' +
                            '<i class="fa fa-refresh fa-spin fa-1x fa-fw"></i> ' +
                            '<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.time.is.over" /></spring:escapeBody>' +
                            '</h1>'
                    });

                    setTimeout(function() {
                        if (${isUserLeader}) {
                            finish(true);
                        } else {
                            location.reload();
                        }
                    }, 4000);
                },
                description: "<div id='countdown-label'><spring:escapeBody javaScriptEscape='true'><fmt:message key='label.countdown.time.left' /></spring:escapeBody></div>"
            });
        }
        </c:if>

        //autosave feature
        <c:if test="${isUserLeader && (mode != 'teacher')}">
        var autosaveInterval = "60000"; // 60 seconds interval
        window.setInterval(learnerAutosave,	autosaveInterval);

        function learnerAutosave(isCommand){
            // isCommand means that the autosave was triggered by force complete or another command websocket message
            // in this case do not check multiple tabs open, just autosave
            if (!isCommand) {
                let shouldAutosave = preventLearnerAutosaveFromMultipleTabs(autosaveInterval);
                if (!shouldAutosave) {
                    return;
                }
            }

            //ajax form submit
            $('#burning-questions').ajaxSubmit({
                url: "<lams:WebAppURL/>learning/autosaveBurningQuestions.do?sessionMapID=${sessionMapID}&date=" + new Date().getTime(),
                success: function() {
                    $.jGrowl(
                        "<span aria-live='polite'><i class='fa fa-lg fa-floppy-o'></i> <spring:escapeBody javaScriptEscape='true'><fmt:message key='label.burning.questions.autosaved' /></spring:escapeBody><span>",
                        { life: 2000, closeTemplate: '' }
                    );
                }
            });
        }
        </c:if>

        function finish(isTimelimitExpired) {
            var proceed = true;
            // ask for leave confirmation only if time limit is not expired
            if (!isTimelimitExpired) {
				var numberOfAvailableScratches = $("[id^=imageLink-][${scratchie.revealOnDoubleClick ? "ondblclick" : "onclick"}], [id^=type-your-answer-]:visible").length;
                proceed = numberOfAvailableScratches == 0 ||
                    confirm("<spring:escapeBody javaScriptEscape='true'><fmt:message key='label.one.or.more.questions.not.completed'/></spring:escapeBody>");
            }

            if (proceed) {
                document.getElementById("finishButton").disabled = true;

                var myForm = $('#burning-questions');
                myForm.attr("action", '<lams:WebAppURL />learning/showResults.do?sessionMapID=${sessionMapID}&date=' + new Date().getTime());
                myForm.submit();
            }

            return false;
        }
    </script>

    <div id="container-main">
        <lams:LeaderDisplay username="${sessionMap.groupLeaderName}" userId="${sessionMap.groupLeaderUserId}"/>

        <c:if test="${sessionMap.userFinished && (mode == 'teacher')}">
            <div class="mt-3">
                <lams:Alert5 id="score" type="info" close="false">
                    <fmt:message key="label.you.ve.got">
                        <fmt:param>${score}</fmt:param>
                        <fmt:param>${scorePercentage}</fmt:param>
                    </fmt:message>
                </lams:Alert5>
            </div>
        </c:if>

        <c:if test="${isUserLeader and scratchie.revealOnDoubleClick}">
            <lams:Alert5 type="info" id="reveal-double-click-warning" close="false">
                <fmt:message key="label.learning.reveal.double.click" />
            </lams:Alert5>
        </c:if>

        <c:if test="${not empty sessionMap.submissionDeadline}">
            <lams:Alert5 id="submissionDeadline" close="true" type="info">
                <fmt:message key="authoring.info.teacher.set.restriction">
                    <fmt:param>
                        <lams:Date value="${sessionMap.submissionDeadline}" />
                    </fmt:param>
                </fmt:message>
            </lams:Alert5>
        </c:if>

        <c:if test="${mode != 'teacher'}">
            <div id="timelimit-start-dialog" role="alertdialog" aria-labelledby="are-you-ready">
                <h4 id="are-you-ready">
                    <fmt:message key='label.are.you.ready' />
                </h4>
                <button type="button" name="ok" id="timelimit-start-ok" class="button">
                    <fmt:message key='label.ok' />
                </button>
            </div>
        </c:if>

        <lams:errors5/>

        <c:if test="${not empty scratchie.instructions}">
            <div id="instructions" class="instructions">
                <c:out value="${scratchie.instructions}" escapeXml="false" />
            </div>
        </c:if>

        <div id="questionListArea">
            <%@ include file="questionlist.jsp"%>
        </div>

        <c:if test="${mode != 'teacher'}">
            <div class="activity-bottom-buttons">
                <button type="button" name="finishButton" id="finishButton" onclick="return finish(false);" class="btn btn-primary na">
                    <fmt:message key="label.submit" />
                </button>
            </div>
        </c:if>        
    </div>
</lams:PageLearner>