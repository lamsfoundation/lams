<%@page import="org.apache.struts.action.ActionMessages" %>
<%@page import="org.lamsfoundation.lams.web.ForgotPasswordServlet" %>
<%@page import="org.lamsfoundation.lams.util.MessageService" %>
<%@page import="org.springframework.web.context.WebApplicationContext" %>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@ page import="org.lamsfoundation.lams.util.Configuration" %>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys" %>

<%@ taglib uri="tags-html" prefix="html" %>
<%@ taglib uri="tags-logic" prefix="logic" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

<%
String languageKey = request.getParameter("languageKey");
String stateStr = request.getParameter("state");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<lams:html>

<lams:head>
    <lams:css  style="core"/>
    <title><fmt:message key="title.forgot.password"/></title>
    <link rel="icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />
    <link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />
</lams:head>

<script language="javascript" type="text/javascript">
        function toHome() 
        {
            var isSuccess = <%=stateStr%>;
            if (isSuccess == "1")
            {
                window.location="<lams:LAMSURL/>index.do";
            }
            else
            {
                window.location="<lams:LAMSURL/>forgotPassword.jsp";
            }
        };
</script>

<body class="stripes" >
    <div id="page"><!--main box 'page'-->

    
    <h1 class="no-tabs-below"><fmt:message key="label.forgot.password.confirm"/></h1>
    
    <div id="header-no-tabs"></div>
    <div id="content" align="center">
	    <table border="0" class="body"><tr><td>
		    <%if (stateStr.equals("0")){out.print("<font color='Red'>");}%>
		    <fmt:message key="<%=languageKey %>"/>
		    <%if (stateStr.equals("0")){out.print("</font>");}%>
	    </td></tr></table>
	    <br><br>
	    <html:button property="cancel" styleClass="button" onclick="javascript:toHome();"><fmt:message key="label.ok"/></html:button>
	</div>
    
    <div id="footer"><div id="footer"></div><!--closes footer-->
    
</div>
</body> 

</lams:html>          