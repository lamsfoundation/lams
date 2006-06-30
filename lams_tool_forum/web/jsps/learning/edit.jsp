<%@ include file="/includes/taglibs.jsp"%>

<html:errors property="error" />

<html:form action="/learning/updateTopic.do" focus="message.subject" onsubmit="return validateMessageForm(this);" enctype="multipart/form-data">
	<%@ include file="/jsps/learning/message/topiceditform.jsp"%>
</html:form>

