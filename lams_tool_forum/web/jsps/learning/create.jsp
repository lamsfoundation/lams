<%@ include file="/includes/taglibs.jsp" %>

<html:errors property="error" />
<div align="center">
<html:form action="/learning/createTopic.do" focus="message.subject"
	onsubmit="return validateMessageForm(this);"  enctype="multipart/form-data">
<fieldset>
<%@ include file="/jsps/message/topicform.jsp" %>
 </fieldset>
 </html:form>
</div>
