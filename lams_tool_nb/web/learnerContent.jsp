<!DOCTYPE html>
<%@ page import="org.lamsfoundation.lams.tool.noticeboard.NoticeboardConstants"%>
<%@ include file="/includes/taglibs.jsp"%>

<lams:PageLearner toolSessionID="${nbLearnerForm.toolSessionID}" title="${nbLearnerForm.title}">
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

    <div id="container-main" role="region">

        <c:out value="${nbLearnerForm.basicContent}" escapeXml="false" />

        <hr>
    <form:form modelAttribute="nbLearnerForm" target="_self" onsubmit="disableFinishButton();" id="nbLearnerForm">
        <form:hidden path="mode" />
        <form:hidden path="toolSessionID" />

        <c:if test="${allowComments}">
            <lams:Comments toolSessionId="${nbLearnerForm.toolSessionID}"
                           toolSignature="<%=NoticeboardConstants.TOOL_SIGNATURE%>" likeAndDislike="${likeAndDislike}"
                           anonymous="${anonymous}"/>
        </c:if>

        <c:if test="${not nbLearnerForm.readOnly}">
            <div class="activity-bottom-buttons">
            	<button type="button" id="finishButton" class="btn btn-primary na">
                	<c:choose>
                    	<c:when test="${isLastActivity}">
                        	<fmt:message key="button.submit" />
                        </c:when>
                        <c:otherwise>
                            <fmt:message key="button.finish" />
                        </c:otherwise>
                   </c:choose>
                </button>
            </div>
        </c:if>
    </form:form>
    </div>
</lams:PageLearner>
