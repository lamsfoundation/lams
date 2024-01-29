<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration" %>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys" %>
<%@ page import="org.lamsfoundation.lams.util.FileValidatorUtil" %>
<%@ page import="org.lamsfoundation.lams.tool.forum.ForumConstants"%>
<c:set var="UPLOAD_FILE_LARGE_MAX_SIZE"><%=Configuration.get(ConfigurationKeys.UPLOAD_FILE_LARGE_MAX_SIZE)%></c:set>
<c:set var="UPLOAD_FILE_MAX_SIZE"><%=Configuration.get(ConfigurationKeys.UPLOAD_FILE_MAX_SIZE)%></c:set>
<c:set var="EXE_FILE_TYPES"><%=Configuration.get(ConfigurationKeys.EXE_EXTENSIONS)%></c:set>
<c:set var="lams"><lams:LAMSURL /></c:set>
<c:set var="tool"><lams:WebAppURL /></c:set>
<c:set var="ctxPath" value="${pageContext.request.contextPath}" scope="request" />
<c:set var="language"><lams:user property="localeLanguage"/></c:set>
<c:set var="sessionMapID" value="${messageForm.sessionMapID}" />
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

<lams:PageLearner title="${sessionMap.title}" toolSessionID="${sessionMap.toolSessionID}">

    <!-- ********************  CSS ********************** -->
    <link href="${lams}css/uppy.min.css" rel="stylesheet" type="text/css" />
    <link href="${lams}css/uppy.custom.css" rel="stylesheet" type="text/css" />

    <!-- ********************  javascript ********************** -->
    <lams:JSImport src="includes/javascript/common.js" />
    <script type="text/javascript" src="${lams}includes/javascript/tabcontroller.js"></script>
    <script type="text/javascript" src="${lams}includes/javascript/jquery.treetable.js"></script>
    <script type="text/javascript" src="${lams}includes/javascript/jquery.jscroll.js"></script>

    <%@ include file="/common/uppylang.jsp"%>

    <script type="text/javascript">
        var removeItemAttachmentUrl = "<lams:WebAppURL />learning/deleteAttachment.do";
			var warning = '<spring:escapeBody javaScriptEscape="true"><fmt:message key="warn.minimum.number.characters" /></spring:escapeBody>';
			var LABEL_MAX_FILE_SIZE = '<spring:escapeBody javaScriptEscape="true"><fmt:message key="errors.maxfilesize"><param>{0}</param></fmt:message></spring:escapeBody>';
			var LABEL_NOT_ALLOWED_FORMAT = '<spring:escapeBody javaScriptEscape="true"><fmt:message key="error.attachment.executable"/></spring:escapeBody>';

        var UPLOAD_FILE_MAX_SIZE = '<c:out value="${UPLOAD_FILE_MAX_SIZE}"/>',
            UPLOAD_FILE_LARGE_MAX_SIZE = '<c:out value="${UPLOAD_FILE_LARGE_MAX_SIZE}"/>',
            // convert Java syntax to JSON
            EXE_FILE_TYPES = JSON.parse("[" + "${EXE_FILE_TYPES}".replace(/\.\w+/g, '"$&"') + "]"),
   		    EXE_FILE_ERROR = '<spring:escapeBody javaScriptEscape="true"><fmt:message key="error.attachment.executable"/></spring:escapeBody>';

    </script>
    <script type="text/javascript" src="${tool}includes/javascript/learner.js"></script>
    <script type="text/javascript" src="${tool}includes/javascript/message.js"></script>
    <script type="text/javascript">
        function doSubmit() {
            disableSubmitButton();
            if (validateForm()) {
                showBusy("itemAttachmentArea");
                return true;
            }
            enableSubmitButton();
            return false;
        }
    </script>

    <div id="container-main">
        <form:form action="createTopic.do" id="messageForm" modelAttribute="messageForm" onsubmit="return doSubmit();"
                   method="post" focus="message.subject" enctype="multipart/form-data">
            <form:hidden path="sessionMapID" />

            <div class="card lcard">
                <div class="card-header">
                    <fmt:message key="title.message.edit" />
                </div>

                <div class="card-body">
                    <lams:errors5/>
                    <%@ include file="/jsps/learning/message/topicform.jsp"%>
                </div>
            </div>
        </form:form>
    </div>
</lams:PageLearner>