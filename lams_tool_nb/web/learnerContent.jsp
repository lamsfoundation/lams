<!DOCTYPE html>
<%@ page import="org.lamsfoundation.lams.tool.noticeboard.NoticeboardConstants"%>
<%@ include file="/includes/taglibs.jsp"%>

<lams:PageLearner toolSessionID="${nbLearnerForm.toolSessionID}" title="${nbLearnerForm.title}">
    <script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui.js"></script>
    <lams:JSImport src="includes/javascript/dialog5.js" />

    <script>
        checkNextGateActivity('finishButton', '${nbLearnerForm.toolSessionID}', '', function(){
            submitForm('finish');
        });

        function disableFinishButton() {
            var finishButton = document.getElementById("finishButton");
            if (finishButton != null) {
                finishButton.disabled = true;
            }
        }

        function submitForm(methodName) {
            var f = document.getElementById('nbLearnerForm');
            f.action = methodName + ".do";
            f.submit();
        }
    </script>


    <p role="region">
        <c:out value="${nbLearnerForm.basicContent}" escapeXml="false" />
    </p>

    <form:form modelAttribute="nbLearnerForm" target="_self" onsubmit="disableFinishButton();" id="nbLearnerForm">
        <form:hidden path="mode" />
        <form:hidden path="toolSessionID" />

        <c:if test="${userFinished and reflectOnActivity}">
            <div class="card">
                <div class="card-body">
                    <p class="fst-italic">
                        <lams:out value="${reflectInstructions}" escapeHtml="true" />
                    </p>
                    <p class="mt-5">
                        <c:choose>
                            <c:when test="${empty reflectEntry}">
                                <fmt:message key="message.no.reflection.available" />
                            </c:when>
                            <c:otherwise>
                                <lams:out escapeHtml="true" value="${reflectEntry}" />
                            </c:otherwise>
                        </c:choose>
                    </p>
                </div>
            </div>


        </c:if>

        <c:if test="${allowComments}">
            <hr/>
            <lams:Comments toolSessionId="${nbLearnerForm.toolSessionID}"
                           toolSignature="<%=NoticeboardConstants.TOOL_SIGNATURE%>" likeAndDislike="${likeAndDislike}"
                           anonymous="${anonymous}" />
        </c:if>

        <c:if test="${not nbLearnerForm.readOnly}">
            <div class="activity-bottom-buttons">
                <c:choose>
                    <c:when test="${reflectOnActivity}">
                        <button class="btn btn-primary" onclick="submitForm('reflect')">
                            <fmt:message key="button.continue" />
                        </button>
                    </c:when>
                    <c:otherwise>
                        <button id="finishButton" class="btn btn-primary">
                            <c:choose>
                                <c:when test="${isLastActivity}">
                                    <fmt:message key="button.submit" />
                                </c:when>
                                <c:otherwise>
                                    <fmt:message key="button.finish" />
                                </c:otherwise>
                            </c:choose>
                        </button>
                    </c:otherwise>
                </c:choose>
            </div>

        </c:if>

    </form:form>
</lams:PageLearner>