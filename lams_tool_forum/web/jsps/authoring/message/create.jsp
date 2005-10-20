<%@ include file="/includes/taglibs.jsp" %>

<html:errors property="error" />
<div align="center">
<html:form action="/authoring/createTopic.do" focus="message.subject"
	onsubmit="return validateMessageForm(this);" >
<fieldset>
<%@ include file="/jsps/message/topiclist.jsp" %>
<%@ include file="/jsps/message/form.jsp" %>
 </fieldset>
 </html:form>
</div>
