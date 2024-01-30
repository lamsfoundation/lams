<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL /></c:set>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="mode" value="${sessionMap.mode}" />
<c:set var="assessment" value="${sessionMap.assessment}" />
<c:set var="pageNumber" value="${sessionMap.pageNumber}" />
<c:set var="hasEditRight" value="${sessionMap.hasEditRight}"/>
<c:set var="isUserLeader" value="${sessionMap.isUserLeader}"/>
<c:set var="isLeadershipEnabled" value="${assessment.useSelectLeaderToolOuput}"/>
<c:set var="localeLanguage"><lams:user property="localeLanguage" /></c:set>
<c:set var="showQuestionMonitoringActionButtons" value="${not empty sessions}" />

<c:if test="${param.embedded}">
    <link href="${lams}css/rating.css" rel="stylesheet" type="text/css">
    <link href="<lams:WebAppURL/>includes/css/assessment.css" rel="stylesheet" type="text/css">
</c:if>

<style>
    <c:if test="${param.embedded and empty toolSessionID and (assessment.allowDiscloseAnswers or assessment.allowDiscussionSentiment)}">
    .card-header .asterisk {
        margin-right: 30px;
    }
    </c:if>
</style>

<script type="text/javascript" src="${lams}includes/javascript/jquery.timeago.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/timeagoi18n/jquery.timeago.${fn:toLowerCase(localeLanguage)}.js"></script>
<lams:JSImport src="includes/javascript/rating.js" />
<script type="text/javascript">
        //vars for rating.js
        AVG_RATING_LABEL = '<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.average.rating"><fmt:param>@1@</fmt:param><fmt:param>@2@</fmt:param></fmt:message></spring:escapeBody>',
        YOUR_RATING_LABEL = '<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.your.rating"><fmt:param>@1@</fmt:param><fmt:param>@2@</fmt:param><fmt:param>@3@</fmt:param></fmt:message></spring:escapeBody>',
        COMMENTS_MIN_WORDS_LIMIT = 0,
        MAX_RATES = 0,
        MIN_RATES = 0,
        COUNT_RATED_ITEMS = true,
        COMMENT_TEXTAREA_TIP_LABEL = '<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.comment.textarea.tip"/></spring:escapeBody>',
        WARN_COMMENTS_IS_BLANK_LABEL = '<spring:escapeBody javaScriptEscape="true"><fmt:message key="error.resource.image.comment.blank"/></spring:escapeBody>';
    <%-- Do not allow rating if it is a teacher view --%>
    <c:if test="${not empty toolSessionID}">
    var SESSION_ID = ${toolSessionID};
    </c:if>

    function refreshToRating(questionUid) {
        // LDEV-5052 Refresh page and scroll to the given ID on comment submit

        // setting href does not navigate if url contains #, we still need a reload
        location.hash = '#rating-table-' + questionUid;
        location.reload();
        return false;
    }

    <c:if test="${param.embedded and empty toolSessionID and assessment.allowDiscussionSentiment}">
    function startDiscussionSentiment(toolQuestionUid, burningQuestionUid, markAsActive) {
        // burningQuestionUid is not used in Assessment, but must be added for function signature consistency

        $('#discussion-sentiment-chart-card-container-' + toolQuestionUid).load(
            '${lams}learning/discussionSentiment/startMonitor.do',
            {
                toolQuestionUid : toolQuestionUid,
                markAsActive    : markAsActive
            },
            function(){
                $('#discussion-sentiment-start-button-' + toolQuestionUid).remove();
            }
        )
    }
    </c:if>

    $(document).ready(function() {
        $("time.timeago").timeago();

        <c:if test="${param.showQuestionDetailsButton}">
        tb_init('a.thickbox');
        </c:if>

        <%-- Connect to command websocket only if it is learner UI --%>
        <c:if test="${not empty toolSessionID}">
        // command websocket stuff for refreshing
        // trigger is an unique ID of page and action that command websocket code in Page.tag recognises
        commandWebsocketHookTrigger = 'assessment-results-refresh-${assessment.contentId}';
        // if the trigger is recognised, the following action occurs
        commandWebsocketHook = function() {
            // delay reload by a period to prevent flood of reloads from students
            setTimeout(function(){
                location.reload();
            }, Math.round(8000 * Math.random()));
        };
        </c:if>

        <c:if test="${param.embedded and empty toolSessionID and assessment.allowDiscussionSentiment}">
        $.ajax({
            url : '${lams}learning/discussionSentiment/checkMonitor.do',
            data : {
                toolContentId : ${assessment.contentId}
            },
            dataType : 'json',
            success : function(result){
                result.forEach(function(discussion){
                    startDiscussionSentiment(discussion.toolQuestionUid, null, false);
                });
            }
        });
        </c:if>
    });
</script>

<c:forEach var="question" items="${sessionMap.pagedQuestions[pageNumber-1]}" varStatus="status">
    <c:set var="questionIndex" value="${status.index}"/>

    <div class="card lcard">
        <div class="card-header">

            <c:if test="${param.embedded and empty toolSessionID}">
                <div class="monitor-question-buttons">
                    <c:if test="${assessment.allowDiscloseAnswers and showQuestionMonitoringActionButtons}">
                        <div class="btn-group-sm disclose-button-group ms-2" questionUid="${question.uid}">
                                <%-- Allow disclosing correct answers only for multiple choice questions --%>
                            <c:if test="${question.type == 1}">
                                <div class="btn btn-secondary disclose-correct-button"
                                <c:if test="${question.correctAnswersDisclosed}">
                                     disabled="disabled"><i class="fa fa-check">&nbsp;</i
                                </c:if>>
                                    <fmt:message key="label.disclose.correct.answers" />
                                </div>
                            </c:if>
                            <div class="btn btn-secondary disclose-groups-button"
                                 <c:if test="${question.groupsAnswersDisclosed}">disabled="disabled">
                                <i class="fa fa-check">&nbsp;</i</c:if>>
                                <fmt:message key="label.disclose.groups.answers" />
                            </div>
                        </div>
                    </c:if>

                    <c:if test="${param.showQuestionDetailsButton or assessment.allowDiscussionSentiment}">
                        <div class="btn-group-sm mt-3">
                            <c:if test="${param.showQuestionDetailsButton}">
                                <a class="thickbox btn btn-secondary"
                                   href='<c:url value="/monitoring/questionSummary.do?sessionMapID=${sessionMapID}"/>&questionUid=${question.uid}&KeepThis=true&TB_iframe=true&modal=true'>
                                    <i class="fa fa-info-circle"></i>&nbsp; <fmt:message
                                        key="label.monitoring.summary.results.question" />
                                </a>
                            </c:if>

                            <c:if test="${assessment.allowDiscussionSentiment and showQuestionMonitoringActionButtons}">
                                <div id="discussion-sentiment-start-button-${question.uid}"
                                     class="btn btn-secondary discussion-sentiment-start-button"
                                     onClick="javascript:startDiscussionSentiment(${question.uid}, null, true)">
                                    <i class="fa fa-comments"></i>&nbsp;
                                    <fmt:message key="label.monitoring.discussion.start" />
                                </div>
                            </c:if>
                        </div>
                    </c:if>
                </div>
            </c:if>

            <c:if test="${assessment.displayMaxMark}">
				<span class="float-end badge alert alert-info fw-normal m-1 p-1">
					<fmt:message key="label.learning.max.mark">
                        <fmt:param value="${question.maxMark}" />
                    </fmt:message>
				</span>
            </c:if>

            <c:if test="${question.answerRequired}">
				<span class="asterisk float-end">
					<i class="fa fa-asterisk text-danger"
                       title="<fmt:message key="label.answer.required"/>"
                       alt="<fmt:message key="label.answer.required"/>"></i>
				</span>
            </c:if>

            <div class="card-title">
                <c:if test="${assessment.numbered}">
                    ${questionIndex + sessionMap.questionNumberingOffset}.
                </c:if>

                <c:if test="${not sessionMap.hideTitles}">
                    ${question.title}
                </c:if>
            </div>
        </div>

        <div class="card-body" id="question-area-${questionIndex}">
            <div class="mb-4">
                ${question.question}
                <div class="clearfix"></div>
            </div>

            <c:choose>
                <c:when test="${question.type == 1}">
                    <c:set var="justificationEligible" value="true" />
                    <%@ include file="multiplechoice.jsp"%>
                </c:when>
                <c:when test="${question.type == 2}">
                    <c:set var="justificationEligible" value="true" />
                    <%@ include file="matchingpairs.jsp"%>
                </c:when>
                <c:when test="${question.type == 3}">
                    <c:set var="justificationEligible" value="false" />
                    <%@ include file="vsa.jsp"%>
                </c:when>
                <c:when test="${question.type == 4}">
                    <c:set var="justificationEligible" value="true" />
                    <%@ include file="numerical.jsp"%>
                </c:when>
                <c:when test="${question.type == 5}">
                    <c:set var="justificationEligible" value="true" />
                    <%@ include file="truefalse.jsp"%>
                </c:when>
                <c:when test="${question.type == 6}">
                    <c:set var="justificationEligible" value="false" />
                    <%@ include file="essay.jsp"%>
                </c:when>
                <c:when test="${question.type == 7}">
                    <c:set var="justificationEligible" value="true" />
                    <%@ include file="ordering.jsp"%>
                </c:when>
                <c:when test="${question.type == 8}">
                    <c:set var="justificationEligible" value="true" />
                    <%@ include file="markhedging.jsp"%>
                </c:when>
            </c:choose>

            <c:if test="${question.type != 8}">
                <%@ include file="markandpenaltyarea.jsp"%>
            </c:if>

            <c:if test="${not empty toolSessionID}">
                <c:if test="${assessment.enableConfidenceLevels and question.type != 8}">
                    <%@ include file="confidencelevel.jsp"%>
                </c:if>

                <%@ include file="historyresponses.jsp"%>
            </c:if>

            <c:if test="${justificationEligible and (assessment.allowAnswerJustification or (question.type == 8 and question.hedgingJustificationEnabled))}">
                <c:choose>
                    <c:when test="${assessment.allowDiscloseAnswers and question.groupsAnswersDisclosed and fn:length(sessions) > 1}">
                        <%-- Display a table with other groups' justifications --%>
                        <div class="card-subheader mt-4">
                            <fmt:message key="label.answer.justification" />
                        </div>

                        <div class="ltable table-sm mb-2">
                            <div class="row">
                                <div class="w-25">
                                    <fmt:message key="monitoring.label.group" />
                                </div>
                                <div class="col">
                                    <fmt:message key="label.answer.justification" />
                                </div>
                            </div>

                            <c:forEach var="session" items="${sessions}" varStatus="status">
                                <c:set var="sessionResults" value="" />

                                <%-- Get the needed piece of information from a complicated questionSummaries structure --%>
                                <c:set var="questionSummary" value="${questionSummaries[question.uid]}" />
                                <c:set var="sessionResults" value="${questionSummary.questionResultsPerSession[questionIndex]}" />
                                <c:set var="sessionResults" value="${sessionResults[fn:length(sessionResults)-1]}" />

                                <c:if test="${not empty sessionResults.uid and not empty sessionResults.justification}">
                                    <div class="row">
                                        <div class="w-25">
                                            <c:if test="${not empty session.groupLeader}">
                                                <lams:Portrait userId="${session.groupLeader.userId}" />&nbsp;
                                            </c:if>
                                            <c:choose>
                                                <c:when test="${not empty toolSessionID and toolSessionID == session.sessionId}">
                                                    <b><fmt:message key="label.your.team" /></b>
                                                </c:when>
                                                <c:otherwise>
                                                    <%-- Sessions are named after groups --%>
                                                    <c:out value="${session.sessionName}" escapeXml="true" />
                                                </c:otherwise>
                                            </c:choose>
                                        </div>

                                        <div class="col">
                                            <c:out value="${sessionResults.justificationEscaped}" escapeXml="false" />
                                        </div>
                                    </div>
                                </c:if>
                            </c:forEach>
                        </div>
                    </c:when>

                    <c:when test="${not empty toolSessionID and not empty question.justification}">
                        <%-- Display only own justification --%>
                        <div class="card-subheader mt-2">
                            <fmt:message key="label.answer.justification" />
                        </div>
                        <p>
                            <c:out value="${question.justificationHtml}" escapeXml="false" />
                        </p>
                    </c:when>
                </c:choose>
            </c:if>

            <c:if test="${param.embedded and empty toolSessionID and assessment.allowDiscussionSentiment}">
                <div id="discussion-sentiment-chart-card-container-${question.uid}"></div>
            </c:if>
        </div>
    </div>
</c:forEach>