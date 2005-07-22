<%@ include file="/includes/taglibs.jsp" %>
<%@ include file="/includes/messages.jsp" %>

<html:errors property="error" />
<html:javascript formName="messageForm" dynamicJavascript="false" staticJavascript="false"/>
<div align="center">
<html:form action="/learning/message/editMessage.do" focus="message.body"
	styleId="messageForm" onsubmit="return validateMessageForm(this);" >
<fieldset>
<%--
<html:hidden property="message.parent" />
--%>

<html:hidden property="parentId"/>
<html:hidden property="topicId"/>
<html:hidden property="message.id"/>
<html:hidden property="message.isAuthored"/>
<html:hidden property="message.isAnnonymous"/>
<%@ include file="includes/form.jsp" %>
 </fieldset>
 </html:form>
</div>
