<%@ include file="/includes/taglibs.jsp" %>
<%@ include file="/includes/messages.jsp" %>

<html:errors property="error" />
<html:javascript formName="messageForm" dynamicJavascript="true" staticJavascript="false"/>
<div align="center">
<html:form action="/authoring/forum/createTopic.do" focus="message.subject"
	styleId="messageForm" onsubmit="return validateMessageForm(this);" >
<fieldset>
<%@ include file="includes/form.jsp" %>
 </fieldset>
 </html:form>
</div>
