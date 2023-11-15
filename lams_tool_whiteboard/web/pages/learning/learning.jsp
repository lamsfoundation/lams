<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL /></c:set>
<%-- param has higher level for request attribute --%>
<c:if test="${not empty param.sessionMapID}">
    <c:set var="sessionMapID" value="${param.sessionMapID}" />
</c:if>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="mode" value="${sessionMap.mode}" />
<c:set var="toolSessionID" value="${sessionMap.toolSessionID}" />
<c:set var="whiteboard" value="${sessionMap.whiteboard}" />
<c:set var="finishedLock" value="${sessionMap.finishedLock}" />
<c:set var="hasEditRight" value="${sessionMap.hasEditRight}"/>
<c:set var="localeLanguage"><lams:user property="localeLanguage" /></c:set>

<lams:PageLearner title="${whiteboard.title}" toolSessionID="${toolSessionID}">
    <link rel="stylesheet" type="text/css" href="${lams}css/jquery.countdown.css" />
    <style media="screen,projection" type="text/css">
        .countdown-timeout {
            color: #FF3333 !important;
        }

        #countdown {
            width: 150px;
            font-size: 110%;
            font-style: italic;
            color:#47bc23;
            text-align: center;
        }

        #whiteboard-frame {
            width: 100%;
            height: 700px;
            margin-bottom: 20px;
            border: 1px solid #c1c1c1;
        }

        .full-screen-launch-button {
            margin-bottom: 5px;
        }

        .full-screen-exit-button {
            display: none;
            margin-bottom: 5px;
        }

        .full-screen-content-div.fullscreen {
            padding: 20px 30px 70px 0;
        }

        .full-screen-content-div.fullscreen .full-screen-flex-div {
            margin: 0 2%;
        }

        .full-screen-content-div.fullscreen .full-screen-flex-div,
        .full-screen-content-div.fullscreen .full-screen-main-div,
        .full-screen-content-div.fullscreen #whiteboard-frame {
            height: 100%;
            width: 100%;
        }
    </style>

    <script type="text/javascript" src="${lams}includes/javascript/jquery.plugin.js"></script>
    <script type="text/javascript" src="${lams}includes/javascript/jquery.countdown.js"></script>
    <script type="text/javascript" src="${lams}includes/javascript/jquery.blockUI.js"></script>
    <script type="text/javascript" src="${lams}includes/javascript/fullscreen.js"></script>
    <script type="text/javascript">
        checkNextGateActivity('finish-button', '${toolSessionID}', '', finishSession);

        $(document).ready(function(){
            $('[data-bs-toggle="tooltip"]').each((i, el) => {
                new bootstrap.Tooltip($(el))
            });

            // Use container-fluid just for this tool
            $('.panel-learner-page').closest('.container').removeClass('container').addClass('container-fluid');

            setupFullScreenEvents();
            document.addEventListener("fullscreenchange", whiteboardFullScreenChanged);
            document.addEventListener("mozfullscreenchange", whiteboardFullScreenChanged);
            document.addEventListener("webkitfullscreenchange", whiteboardFullScreenChanged);
            document.addEventListener("msfullscreenchange", whiteboardFullScreenChanged);

            let timeLimitExceeded = ${timeLimitExceeded};
            initWebsocket('whiteboardTimeLimit${sessionMap.toolContentID}',
                '<lams:WebAppURL />'.replace('http', 'ws')
                + 'learningWebsocket?toolContentID=${sessionMap.toolContentID}',
                function (e) {
                    // create JSON object
                    var input = JSON.parse(e.data);

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
                        } else if (timeLimitExceeded){
                            if (secondsLeft > 0) {
                                // teacher gave extra time, reload to writable Whiteboard
                                location.reload();
                                return;
                            }
                        } else {
                            // initialise the timer
                            displayCountdown(secondsLeft);
                        }
                    }

                    // reset ping timer
                    websocketPing('whiteboardTimeLimit${sessionMap.toolContentID}', true);
                });
        });

        if (${!hasEditRight && mode != "teacher" && !finishedLock}) {
            setInterval("checkLeaderProgress();", 15000);// Auto-Refresh every 15 seconds for non-leaders
        }

        function checkLeaderProgress() {
            $.ajax({
                async: false,
                url: '<c:url value="/learning/checkLeaderProgress.do"/>',
                data: 'toolSessionID=${toolSessionID}',
                dataType: 'json',
                type: 'post',
                success: function (json) {
                    if (json.isLeaderResponseFinalized) {
                        $("#finish-button, #continue-button").show();
                    }
                }
            });
        }

        function finishSession(){
            document.location.href ='<c:url value="/learning/finish.do?sessionMapID=${sessionMapID}&mode=${mode}&toolSessionID=${toolSessionID}"/>';
        }

        function continueReflect(){
            document.location.href='<c:url value="/learning/newReflection.do?sessionMapID=${sessionMapID}"/>';
        }

        // time limit feature
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
                    opacity: '1',
                    width: '150px',
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
                    if (isWebsocketClosed('whiteboardTimeLimit${sessionMap.toolContentID}')){
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
                        location.reload();
                    }, 4000);
                },
                description: "<div id='countdown-label'><spring:escapeBody javaScriptEscape='true'><fmt:message key='label.time.left' /></spring:escapeBody></div>"
            });
        }

        function whiteboardFullScreenChanged() {
            var fullscreenElement = document.fullscreenElement || document.mozFullScreenElement || document.webkitFullscreenElement;
            $('.full-screen-content-div').toggleClass('fullscreen', fullscreenElement && fullscreenElement != null);
        }
    </script>
    <%@ include file="websocket.jsp"%>

    <div id="container-main">

        <!--  Warnings -->
        <c:if test="${not empty sessionMap.submissionDeadline && (sessionMap.mode == 'author' || sessionMap.mode == 'learner')}">
            <lams:Alert5 id="submission-deadline" type="info" close="true">
                <fmt:message key="authoring.info.teacher.set.restriction" >
                    <fmt:param><lams:Date value="${sessionMap.submissionDeadline}" /></fmt:param>
                </fmt:message>
            </lams:Alert5>
        </c:if>

        <c:if test="${sessionMap.lockOnFinish and mode != 'teacher'}">
            <lams:Alert5 type="warning" id="warn-lock" close="false">
                <c:choose>
                    <c:when test="${sessionMap.userFinished}">
                        <fmt:message key="message.activityLocked" />
                    </c:when>
                    <c:otherwise>
                        <fmt:message key="message.warnLockOnFinish" />
                    </c:otherwise>
                </c:choose>
            </lams:Alert5>
        </c:if>

        <lams:errors/>

        <div id="instructions" class="instructions">
            <c:out value="${whiteboard.instructions}" escapeXml="false" />
        </div>

        <div class="full-screen-content-div">
            <div class="full-screen-flex-div">
                <button type="button" class="btn btn-secondary float-end ms-1 full-screen-launch-button" onclick="javascript:launchIntoFullscreen(this)"
                        title="<fmt:message key='label.fullscreen.open' />">
                    <i class="fa-solid fa-maximize" aria-hidden="true"></i>
                </button>
                <button type="button" class="btn btn-secondary float-end ms-1 full-screen-exit-button" onclick="javascript:exitFullscreen()"
                        title="<fmt:message key='label.fullscreen.close' />">
                    <i class="fa fa-compress" aria-hidden="true"></i>
                </button>

                <div class="full-screen-main-div">
                    <iframe id="whiteboard-frame" title="Whiteboard"
                            src='${whiteboardServerUrl}/?whiteboardid=${wid}&username=${whiteboardAuthorName}${empty whiteboardAccessToken ? "" : "&accesstoken=".concat(whiteboardAccessToken)}&copyfromwid=${sourceWid}${empty whiteboardCopyAccessToken ? "" : "&copyaccesstoken=".concat(whiteboardCopyAccessToken)}'>
                    </iframe>
                </div>
            </div>
        </div>

        <!-- Reflection -->
        <c:if test="${sessionMap.userFinished and sessionMap.reflectOn}">
            <lams:NotebookReedit
                    reflectInstructions="${sessionMap.reflectInstructions}"
                    reflectEntry="${sessionMap.reflectEntry}"
                    isEditButtonEnabled="${mode != 'teacher'}"
                    notebookHeaderLabelKey="title.reflection"/>
        </c:if>

        <c:if test="${mode != 'teacher'}">
            <div class="activity-bottom-buttons">
                <c:choose>
                    <c:when test="${whiteboard.galleryWalkEnabled}">
                        <button type="button" data-bs-toggle="tooltip" class="btn btn-primary na ${mode == 'author' ? '' : 'disabled'}"
                                <c:choose>
                                    <c:when test="${mode == 'author'}">
                                        title="<fmt:message key='label.gallery.walk.wait.start.preview' />"
                                        onClick="javascript:location.href = location.href + '&galleryWalk=forceStart'"
                                    </c:when>
                                    <c:otherwise>
                                        title="<fmt:message key='label.gallery.walk.wait.start' />"
                                    </c:otherwise>
                                </c:choose>
                        >
                            <fmt:message key="label.continue" />
                        </button>
                    </c:when>

                    <c:when test="${sessionMap.reflectOn && (not sessionMap.userFinished)}">
                        <button type="button" name="FinishButton" id="continue-button" onclick="return continueReflect()" class="btn btn-primary na">
                            <fmt:message key="label.continue" />
                        </button>
                    </c:when>

                    <c:when test="${!hasEditRight && !sessionMap.userFinished && !sessionMap.isLeaderResponseFinalized}">
                        <%-- show no button for non-leaders until leader will finish activity  --%>
                    </c:when>

                    <c:otherwise>
						<a href="#nogo" name="FinishButton" id="finish-button"
						   class="btn btn-primary voffset5 pull-right na"
								<c:if test="${!hasEditRight && !sessionMap.userFinished && !sessionMap.isLeaderResponseFinalized}">
									style="display: none"
								</c:if>>
							<span class="nextActivity">
                            <c:choose>
                                <c:when test="${sessionMap.isLastActivity}">
                                    <fmt:message key="label.submit" />
                                </c:when>
                                <c:otherwise>
                                    <fmt:message key="label.finished" />
                                </c:otherwise>
                            </c:choose>
                        </button>
                    </c:otherwise>
                </c:choose>
            </div>
        </c:if>

    </div>
</lams:PageLearner>