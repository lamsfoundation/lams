<%@ include file="/common/taglibs.jsp"%>
<c:if test="${not empty param.sessionMapID}"><c:set var="sessionMapID" value="${param.sessionMapID}" /></c:if>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:if test="${empty recordList}">
    <%-- In some cases record list is passed as an attribute, in other - in session map. --%>
    <c:set var="recordList" value="${sessionMap.recordList}" />
</c:if>
<%-- This page modifies its content depending on the page it was included from. --%>
<c:if test="${empty includeMode}"><c:set var="includeMode" value="learning" /></c:if>
<c:set var="daco" value="${sessionMap.daco}" />
<c:set var="questionSummaries" value="${sessionMap.questionSummaries}" />
<c:set var="ordinal"><spring:escapeBody javaScriptEscape="true"><fmt:message key="label.authoring.basic.answeroption.ordinal"/></spring:escapeBody></c:set>

<c:set var="userRecordCount" value="${includeMode=='monitoring' ? sessionMap.recordCount : fn:length(recordList)}" />
<c:set var="groupRecordCount" value="${sessionMap.totalRecordCount}" />

<div id="questionSummariesDiv">

    <!--  summary panel  -->
    <div class="card-subheader">
        <fmt:message key="label.learning.tableheader.questions" />
    </div>

    <div class="ltable table-sm" id="summaryTable">
        <div class="row table-active">
            <div class="col-6 offset-6 singleSummaryCell"><fmt:message key="label.learning.tableheader.summary" /></div>
        </div>

        <div class="row table-active">
            <div class="col-3 offset-6 singleSummaryCell">
                <c:choose>
                    <c:when test="${empty userFullName}">
                        <fmt:message key="label.learning.tableheader.summary.learner" />
                    </c:when>
                    <c:otherwise>
                        <c:out value="${userFullName}" escapeXml="true"/>
                    </c:otherwise>
                </c:choose>
            </div>
            <div class="col-3 singleSummaryCell"><fmt:message key="label.learning.tableheader.summary.group" /></div>
        </div>

        <div class="row table-active">
            <div class="col-6">
                <fmt:message key="label.learning.heading.recordcount" />
            </div>
            <div class="col-3 singleSummaryCell">
                ${userRecordCount }
            </div>
            <div class="col-3 singleSummaryCell">
                ${groupRecordCount }
            </div>
        </div>
    </div>

    <div class="ltable table-striped table-sm no-header" id="summaryTable">
        <c:forEach var="question" items="${daco.dacoQuestions}" varStatus="questionStatus">
            <c:set var="questionSummary" value="${questionSummaries[questionStatus.index]}" />
            <div class="row">
                <div class="col-6">
                    <!-- <div class="bigNumber">${questionStatus.index+1}</div> -->
                    <c:out value="${question.description}" escapeXml="false"/>

                    <div class="hint">
                        <c:choose>
                            <c:when test="${question.summary==1}">
                                <fmt:message key="label.common.summary.sum" />
                            </c:when>
                            <c:when test="${question.summary==2}">
                                <fmt:message key="label.common.summary.average" />
                            </c:when>
                            <c:when test="${question.summary==3}">
                                <fmt:message key="label.common.summary.count" />
                            </c:when>
                            <c:otherwise>
                                <fmt:message key="label.common.summary.none" />
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>

                <c:choose>
                    <c:when test="${question.type==3 && not empty question.summary}">
                        <c:choose>
                            <%-- Part of the content is displayed depending on the summary type --%>
                            <c:when test="${question.summary==1 || question.summary==2}">
                                <c:choose>
                                    <%-- If no records were provided --%>
                                    <c:when test="${(question.summary==1 and empty questionSummary.userSummary[0].sum)
												 || (question.summary==2 and empty questionSummary.userSummary[0].average)}">
                                        <div class="col-3 singleSummaryCell hint">
                                            <fmt:message key="label.learning.heading.norecords" />
                                        </div>
                                        <div class="col-3 singleSummaryCell hint">
                                            <fmt:message key="label.learning.heading.norecords" />
                                        </div>
                                    </c:when>

                                    <c:otherwise>
                                        <%-- First column shows the summaries for the learner,
                                            second one for the whole group (session) --%>
                                        <div class="col-3 singleSummaryCell">
                                            <c:choose>
                                                <c:when test="${question.summary==1}">
                                                    ${questionSummary.userSummary[0].sum}
                                                </c:when>
                                                <c:otherwise>
                                                    ${questionSummary.userSummary[0].average}
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                        <div class="col-3 singleSummaryCell">
                                            <c:choose>
                                                <c:when test="${question.summary==1}">
                                                    ${questionSummary.groupSummary[0].sum}
                                                </c:when>
                                                <c:otherwise>
                                                    ${questionSummary.groupSummary[0].average}
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </c:when>

                            <c:when test="${question.summary==3}">
                                <div class="col-3">
                                    <div class="ltable no-header alternative-color-inner-table">
                                        <c:forEach var="singleAnswer" items="${questionSummary.userSummary}" begin="1">
                                            <div class="row">
                                                <c:choose>
                                                    <c:when test="${empty singleAnswer.answer}">
                                                        <div class="hint" style="width: 20px;">
                                                            <fmt:message key="label.learning.summary.emptyanswer" />:
                                                        </div>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <div style="width: 20px;">
                                                                ${singleAnswer.answer}:
                                                        </div>
                                                    </c:otherwise>
                                                </c:choose>
                                                <div class="col">
                                                        ${singleAnswer.count}
                                                </div>
                                            </div>
                                        </c:forEach>
                                    </div>
                                </div>
                                <div class="col-3">
                                    <div class="ltable no-header alternative-color-inner-table">
                                        <c:forEach var="singleAnswer" items="${questionSummary.groupSummary}" begin="1">
                                            <div class="row">
                                                <div style="width: 20px;">
                                                        ${singleAnswer.answer}:
                                                </div>
                                                <div class="col">
                                                        ${singleAnswer.count}
                                                </div>
                                            </div>
                                        </c:forEach>
                                    </div>
                                </div>
                            </c:when>
                        </c:choose>
                    </c:when>

                    <c:when test="${(question.type==7 || question.type==8 || question.type==9)  && not empty question.summary}">
                        <div class="col-3 text-center">
                            <c:forEach var="singleAnswer" items="${questionSummary.userSummary}">
                                <div>
										<span class="me-2">
											${fn:substring(ordinal,singleAnswer.answer-1,singleAnswer.answer)})
										</span>
                                    <span>
											<c:choose>
                                                <c:when test="${question.summary==1}">
                                                    ${singleAnswer.sum}
                                                </c:when>
                                                <c:otherwise>
                                                    ${singleAnswer.average}
                                                </c:otherwise>
                                            </c:choose>
										</span>
                                </div>
                            </c:forEach>
                        </div>

                        <div class="col-3 text-center">
                            <c:forEach var="singleAnswer" items="${questionSummary.groupSummary}">
                                <div>
										<span class="me-2">
											${fn:substring(ordinal,singleAnswer.answer-1,singleAnswer.answer)})
										</span>
                                    <span>
											<c:choose>
                                                <c:when test="${question.summary==1}">
                                                    ${singleAnswer.sum}
                                                </c:when>
                                                <c:otherwise>
                                                    ${singleAnswer.average}
                                                </c:otherwise>
                                            </c:choose>
										</span>
                                </div>
                            </c:forEach>
                        </div>
                    </c:when>

                    <c:otherwise>
                        <div class="col-3 singleSummaryCell">-</div>
                        <div class="col-3 singleSummaryCell">-</div>
                    </c:otherwise>
                </c:choose>
            </div>
        </c:forEach>
    </div>

    <c:if test='${includeMode=="learning" and mode != "teacher"}'>
        <button type="button" class="btn btn-secondary btn-icon-refresh btn-disable-on-submit float-end mb-3" onclick="javascript:refreshQuestionSummaries('${sessionMapID}')">
            <fmt:message key="label.common.summary.refresh" />
        </button>
    </c:if>
</div>