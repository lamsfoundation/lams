<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration" %>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys" %>
<%@ page import="org.lamsfoundation.lams.util.FileValidatorUtil" %>
<c:set var="UPLOAD_FILE_LARGE_MAX_SIZE"><%=Configuration.get(ConfigurationKeys.UPLOAD_FILE_LARGE_MAX_SIZE)%></c:set>
<c:set var="UPLOAD_FILE_MAX_SIZE"><%=Configuration.get(ConfigurationKeys.UPLOAD_FILE_MAX_SIZE)%></c:set>
<c:set var="EXE_FILE_TYPES"><%=Configuration.get(ConfigurationKeys.EXE_EXTENSIONS)%></c:set>
<c:set var="lams"><lams:LAMSURL /></c:set>
<c:set var="tool"><lams:WebAppURL /></c:set>
<c:set var="ctxPath" value="${pageContext.request.contextPath}" scope="request" />
<c:set var="language"><lams:user property="localeLanguage"/></c:set>
<c:set var="sessionMapID" value="${messageForm.sessionMapID}"/>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="originalMessage" value="${sessionMap.originalMessage}"/>

<!-- ********************  CSS ********************** -->
<link href="${lams}css/uppy.min.css" rel="stylesheet" type="text/css" />
<link href="${lams}css/uppy.custom.css" rel="stylesheet" type="text/css" />

<!-- ********************  javascript ********************** -->
<lams:JSImport src="includes/javascript/common.js" />
<script type="text/javascript" src="${lams}includes/javascript/tabcontroller.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.jRating.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.treetable.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.jscroll.js"></script>
<lams:JSImport src="includes/javascript/upload.js" />

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
<lams:JSImport src="includes/javascript/message.js" relative="true" />
<script type="text/javascript">
    jQuery(document).ready(function() {
        // The treetable code uses the clicks to expand and collapse the replies but then
        // the buttons will not work. So stop the event propogating up the event chain.
        $("#messageForm").click(function (e) {
            e.stopPropagation();
        });
        $("#messageForm").keydown(function (e) {
            e.stopPropagation();
        });

        $('#messageForm').submit(function() { // catch the form's submit event
            disableSubmitButton();
            if ( validateForm() ) {

                if ( typeof CKEDITOR !== 'undefined' ) {
                    for ( instance in CKEDITOR.instances )
                        CKEDITOR.instances[instance].updateElement();
                }

                showBusy("itemAttachmentArea");
                var formData = new FormData(this);
                $.ajax({ // create an AJAX call...
                    data: formData,
                    processData: false, // tell jQuery not to process the data
                    contentType: false, // tell jQuery not to set contentType
                    type: $(this).attr('method'), // GET or POST
                    url: $(this).attr('action'), // the file to call
                    success: function (response) {
                        var threadDiv = document.getElementById('thread'+response.threadUid);
                        var threadUid = response.threadUid;
                        var messageUid = response.messageUid;
                        var rootUid = response.rootUid;

                        if ( rootUid ) {
                            if ( ! threadDiv) {
                                // must have replied to the top level, so show the posting at the top.
                                threadDiv =  document.createElement("div");
                                threadDiv.id = 'reply'+messageUid;
                                $('#insertTopLevelRepliesHere').after(threadDiv);
                            }
                            // make sure the old reply form is gone, so if something goes wrong
                            // the user won't try to submit it again
                            $('#reply').remove();

                            if ( ! threadDiv) {
			        			alert('<spring:escapeBody javaScriptEscape="true"><fmt:message key="error.cannot.redisplay.please.refresh"/></spring:escapeBody>');
                            } else {
                                var loadString = "<lams:WebAppURL />learning/viewTopicThread.do?topicID=" + rootUid + "&sessionMapID=" + response.sessionMapID + "&threadUid=" + threadUid+"&messageUid="+messageUid;
                                $.ajaxSetup({ cache: true });
                                $(threadDiv).load(loadString, function() {
                                    setupJRating("<c:url value='/learning/rateMessage.do'/>?toolSessionID=${sessionMap.toolSessionID}&sessionMapID=${sessionMapID}");
                                    highlightMessage();
                                    // expand up to the reply - in case it is buried down in a lot of replies
                                    // don't need to do this if we have started a new thread.
                                    if ( threadUid != messageUid ) {
                                        $('#tree' + threadUid).treetable("reveal",messageUid);
                                        $('#pb-msg'+messageUid).focus();
                                    }
                                });
                            }

                            if ( response.noMorePosts ) {
                                $('.replybutton').hide();
                            }


                        } else {
                            // No new id? Validation failed!
                            $('#reply').html(response);
                        }
                    }
                });
            } // end validateForm()
            else {
                enableSubmitButton();
                hideBusy("itemAttachmentArea");
            }
            return false;
        });
    });

    function cancelReply() {
        $('#reply').remove();
    }
</script>

<form:form action="replyTopic.do" focus="message.body" enctype="multipart/form-data" modelAttribute="messageForm" id="messageForm" >
    <form:hidden path="sessionMapID"/>

    <div class="card m-2">
        <div class="card-header">
            <fmt:message key="title.message.reply" />
        </div>

        <div class="card-body">
            <lams:errors5/>
            <%@ include file="/jsps/learning/message/topicreplyform.jsp"%>
        </div>
    </div>
</form:form>