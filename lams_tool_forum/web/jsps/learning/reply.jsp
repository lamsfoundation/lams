<%@ include file="/includes/taglibs.jsp"%>

<html:errors property="error" />
<html:form action="/learning/replyTopic.do" focus="message.subject" onsubmit="return validateMessageForm(this);" enctype="multipart/form-data">
	<%@ include file="/jsps/learning/message/topicreplyform.jsp"%>
</html:form>
