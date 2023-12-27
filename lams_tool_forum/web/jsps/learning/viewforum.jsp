<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration" %>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys" %>
<c:set var="UPLOAD_FILE_MAX_SIZE"><%=Configuration.get(ConfigurationKeys.UPLOAD_FILE_MAX_SIZE)%></c:set>
<c:set var="EXE_FILE_TYPES"><%=Configuration.get(ConfigurationKeys.EXE_EXTENSIONS)%></c:set>
<c:set var="lams"><lams:LAMSURL /></c:set>
<c:set var="tool"><lams:WebAppURL /></c:set>
<c:set var="ctxPath" value="${pageContext.request.contextPath}" scope="request" />
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"	scope="request" />
<c:set var="newtopic">
    <lams:WebAppURL />learning/newTopic.do?sessionMapID=${sessionMapID}
</c:set>
<c:set var="finish">
    <lams:WebAppURL />learning/finish.do?sessionMapID=${sessionMapID}
</c:set>
<c:set var="refresh">
    <lams:WebAppURL />learning/viewForum.do?mode=${sessionMap.mode}&toolSessionID=${sessionMap.toolSessionID}&sessionMapID=${sessionMapID}
</c:set>

<lams:PageLearner title="${sessionMap.title}" toolSessionID="${sessionMap.toolSessionID}">

    <!-- ********************  javascript ********************** -->
    <lams:JSImport src="includes/javascript/common.js" />
    <script type="text/javascript" src="${lams}includes/javascript/tabcontroller.js"></script>
    <script type="text/javascript" src="${lams}includes/javascript/jquery.jRating.js"></script>
    <script type="text/javascript" src="${lams}includes/javascript/jquery.treetable.js"></script>
    <script type="text/javascript" src="${lams}includes/javascript/jquery.jscroll.js"></script>
    <lams:JSImport src="includes/javascript/upload.js" />
    <script type="text/javascript">
        var removeItemAttachmentUrl = "<lams:WebAppURL />learning/deleteAttachment.do";
        var warning = '<spring:escapeBody javaScriptEscape="true"><fmt:message key="warn.minimum.number.characters" /></spring:escapeBody>';
        var LABEL_MAX_FILE_SIZE = '<spring:escapeBody javaScriptEscape="true"><fmt:message key="errors.maxfilesize"><param>{0}</param></fmt:message></spring:escapeBody>';
        var LABEL_NOT_ALLOWED_FORMAT = '<spring:escapeBody javaScriptEscape="true"><fmt:message key="error.attachment.executable"/></spring:escapeBody>';
        var EXE_FILE_TYPES = '${EXE_FILE_TYPES}';
        var UPLOAD_FILE_MAX_SIZE = '${UPLOAD_FILE_MAX_SIZE}';

        checkNextGateActivity('finishButton', '${sessionMap.toolSessionID}', '', submitFinish);

        function submitFinish() {
            location.href = '${finish}';
        }
    </script>
    <script type="text/javascript" src="${tool}includes/javascript/learner.js"></script>

    <div id="container-main">
        <!-- Announcements and advanced settings -->
        <c:if test="${not empty sessionMap.submissionDeadline}">
            <lams:Alert5 id="submissionDeadline" type="info" close="true">
                <fmt:message key="authoring.info.teacher.set.restriction" >
                    <fmt:param><lams:Date value="${sessionMap.submissionDeadline}" /></fmt:param>
                </fmt:message>
            </lams:Alert5>
        </c:if>

        <c:if test="${sessionMap.mode == 'author' || sessionMap.mode == 'learner'}">
            <c:if test="${sessionMap.lockedWhenFinished}">
                <lams:Alert5 id="lockWhenFinished" type="info" close="true">
                    <c:choose>
                        <c:when test="${sessionMap.finishedLock}">
                            <fmt:message key="label.responses.locked.reminder" />
                        </c:when>
                        <c:otherwise>
                            <fmt:message key="label.responses.locked" />
                        </c:otherwise>
                    </c:choose>
                </lams:Alert5>
            </c:if>

            <c:if test="${not sessionMap.allowNewTopics and ( sessionMap.minimumReply ne 0 or sessionMap.maximumReply ne 0)}">
                <lams:Alert5 id="postingLimits" type="info" close="true">
                    <c:choose>
                        <c:when test="${sessionMap.minimumReply ne 0 and sessionMap.maximumReply ne 0}">
                            <fmt:message key="label.postingLimits.forum.reminder">
                                <fmt:param value="${sessionMap.minimumReply}"/>
                                <fmt:param value="${sessionMap.maximumReply}"/>
                            </fmt:message>
                        </c:when>
                        <c:when test="${sessionMap.minimumReply ne 0}">
                            <fmt:message key="label.postingLimits.forum.reminder.min">
                                <fmt:param value="${sessionMap.minimumReply}"/>
                            </fmt:message>
                        </c:when>
                        <c:when test="${sessionMap.maximumReply ne 0}">
                            <fmt:message key="label.postingLimits.forum.reminder.max">
                                <fmt:param value="${sessionMap.maximumReply}"/>
                            </fmt:message>
                        </c:when>
                    </c:choose>
                </lams:Alert5>
            </c:if>

            <!-- Rating announcements -->
            <c:if test="${sessionMap.allowRateMessages && (sessionMap.minimumRate ne 0 || sessionMap.maximumRate ne 0)}">
                <lams:Alert5 id="rateMessages" type="info" close="true">
                    <c:choose>
                        <c:when test="${sessionMap.minimumRate ne 0 and sessionMap.maximumRate ne 0}">
                            <fmt:message key="label.rateLimits.forum.reminder">
                                <fmt:param value="${sessionMap.minimumRate}"/>
                                <fmt:param value="${sessionMap.maximumRate}"/>
                            </fmt:message>
                        </c:when>

                        <c:when test="${sessionMap.minimumRate ne 0}">
                            <fmt:message key="label.rateLimits.forum.reminder.min">
                                <fmt:param value="${sessionMap.minimumRate}"/>
                            </fmt:message>
                        </c:when>

                        <c:when test="${sessionMap.maximumRate ne 0}">
                            <fmt:message key="label.rateLimits.forum.reminder.max">
                                <fmt:param value="${sessionMap.maximumRate}"/>
                            </fmt:message>
                        </c:when>
                    </c:choose>
                    &nbsp;

                    <fmt:message key="label.rateLimits.topic.reminder">
                        <fmt:param value="<span id='numOfRatings'>${sessionMap.numOfRatings}</span>"/>
                    </fmt:message>
                </lams:Alert5>
            </c:if>
        </c:if>

        <lams:errors5/>
        
        <div id="instructions" class="instructions">
            <c:out value="${sessionMap.instruction}" escapeXml="false" />
        </div>

        <!-- main UI -->
        <%@ include file="/jsps/learning/message/topiclist.jsp"%>

        <c:if test='${sessionMap.mode != "teacher"}'>
            <div class="activity-bottom-buttons">
                <c:if test="${sessionMap.isMinRatingsCompleted}">
                	<button type="button" name="finish" id="finishButton" class="btn btn-primary na">
                    	<c:choose>
                        	<c:when test="${sessionMap.isLastActivity}">
                            	<fmt:message key="label.submit" />
                            </c:when>
                            <c:otherwise>
                                <fmt:message key="label.finish" />
                            </c:otherwise>
                        </c:choose>
                    </button>
                </c:if>

                <c:if test='${(not sessionMap.finishedLock) && (sessionMap.allowNewTopics)}'>
                    <button type="button" onclick="javascript:location.href='${newtopic}';" class="btn btn-secondary me-2">
                        <i class="fa fa-xm fa-plus"></i>
                        <fmt:message key="label.newtopic" />
                    </button>
                </c:if>

                <button type="button" onclick="javascript:location.href='${refresh}';" class="btn btn-secondary btn-icon-refresh me-2">
                    <fmt:message key="label.refresh" />
                </button>
            </div>
        </c:if>
    </div>
</lams:PageLearner>