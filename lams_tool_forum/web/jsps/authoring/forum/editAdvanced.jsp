<%@ include file="/includes/taglibs.jsp" %>
<%@ include file="/includes/messages.jsp" %>

<html:errors property="error" />
<html:javascript formName="forumForm" dynamicJavascript="true" staticJavascript="false"/>
<div align="center">
<%-- <legend><bean:message key="title.forum.details" /></legend> --%>
<html:form action="/authoring/forum/editAdvanced.do" styleId="forumForm" onsubmit="return validateForumForm(this);" >
<fieldset>
<%@ include file="includes/advanced.jsp" %>
 </fieldset>
  </html:form>
</div>
