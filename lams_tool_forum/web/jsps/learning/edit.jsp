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

<!-- ********************  CSS ********************** -->
<link href="${lams}css/uppy.min.css" rel="stylesheet" type="text/css" />
<link href="${lams}css/uppy.custom.css" rel="stylesheet" type="text/css" />

<!-- ********************  javascript ********************** -->
<lams:JSImport src="includes/javascript/common.js" />
<script type="text/javascript" src="${lams}includes/javascript/tabcontroller.js"></script>
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
                        var messageUid = response.messageUid;
                        if ( messageUid ) {
                            var rootUid = response.rootUid;
                            var messDiv = document.getElementById('outermsg'+messageUid);
                            if ( ! messDiv) {
			        				alert('<spring:escapeBody javaScriptEscape="true"><fmt:message key="error.cannot.redisplay.please.refresh"/></spring:escapeBody>');
                            } else {
                                // make sure the old edit form is gone, so if something goes wrong
                                // the user won't try to submit it again
                                $('#edit').remove();
                                var loadString = "<lams:WebAppURL />learning/viewMessage.do?topicID=" + rootUid + "&sessionMapID=" + response.sessionMapID + "&messageUid="+messageUid;
                                $.ajaxSetup({ cache: true });
                                $(messDiv).load(loadString, function() {
                                    $('#pb-msg'+messageUid).focus();
                                    setupForumStarability("<c:url value='/learning/rateMessage.do'/>?toolSessionID=${sessionMap.toolSessionID}&sessionMapID=${sessionMapID}");
                                    highlightMessage();
                                });
                            }

                        } else {
                            // No valid id? Validation failed! Assume it is the form coming back.
                            $('#edit').html(response);
                        }
                        hideBusy("itemAttachmentArea");
                    }
                });
            } // end validateForm()
            else {
                enableSubmitButton();
            }
            return false;
        });
    });

    function cancelEdit() {
        $('#edit').remove();
    }
</script>

<form:form action="updateTopic.do" focus="message.subject" enctype="multipart/form-data" id="messageForm" modelAttribute="messageForm">
    <form:hidden path="sessionMapID"/>

    <div class="card m-2">
        <div class="card-header">
            <fmt:message key="title.message.edit" />
        </div>

        <div class="card-body">
            <lams:errors5/>
            <%@ include file="/jsps/learning/message/topiceditform.jsp"%>
        </div>
    </div>
</form:form>