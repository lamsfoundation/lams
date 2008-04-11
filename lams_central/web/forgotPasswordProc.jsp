<%@page import="org.apache.struts.action.ActionMessages" %>
<%@page import="org.lamsfoundation.lams.web.ForgotPasswordServlet" %>
<%@page import="org.lamsfoundation.lams.util.MessageService" %>
<%@page import="org.springframework.web.context.WebApplicationContext" %>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils" %>

<%@ taglib uri="tags-html" prefix="html" %>
<%@ taglib uri="tags-logic" prefix="logic" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

<%

String message="";

String stateStr = request.getParameter("state");
int state = Integer.parseInt(stateStr);

WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(this.getServletContext());
MessageService messageService = (MessageService)ctx.getBean("centralMessageService");

switch (state)
{
case 100: message = "<font color='red'>" + messageService.getMessage("error.support.email.not.set") + "</font>"; break;
case 101: message = "<font color='red'>" + messageService.getMessage("error.email.does.not.match") + "</font>"; break;
case 102: message = "<font color='red'>" + messageService.getMessage("error.user.not.found") + "</font>"; break;
case 103: message = "<font color='red'>" + messageService.getMessage("error.password.request.expired") + "</font>"; break;
case 104: message = messageService.getMessage("forgot.password.email.sent"); break;
case 105: message = messageService.getMessage("heading.password.changed.screen"); break;
}

%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<lams:html>

<lams:head>
    <lams:css  style="core"/>
    <title><fmt:message key="title.forgot.password"/></title>
    <link rel="icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />
    <link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />
<script language="javascript" type="text/javascript">
        function toHome() {window.location="<lams:LAMSURL/>index.do";};
</script>
</lams:head>


<body class="stripes" >
<br>
<table class="body"><tr><td align="center">
<div id="content">
    <div id="title" align="left">
                    <h1><fmt:message key="label.forgot.password.confirm"/></h1>
    </div>
    <br>
    <%=message %>
    <br><br>
    <html:button property="cancel" styleClass="button" onclick="javascript:toHome();"><fmt:message key="label.ok"/></html:button>
</div>
</td></tr></table>
</body> 

</lams:html>          