<%@page import="org.apache.struts.action.ActionMessages" %>


<%@ taglib uri="tags-html" prefix="html" %>
<%@ taglib uri="tags-logic" prefix="logic" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

<%
String key = request.getParameter("key");
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
        
        function validateForm() 
        {
            // TODO: need to do validation
            // Check password and confirm are the same
        }
        
        
</script>
</lams:head>


<body class="stripes" >
<form action="<lams:LAMSURL/>/ForgotPasswordRequest" method="get">
    <input type="hidden" name="method" id="method" value="requestPasswordChange" />
    <input type="hidden" name="key" id="key" value="<%=key %>" />
    <p><p><p>
    <table><tr><td align="center">    
    <div id="content">
    
                <div id="title" align="left">
                    <h1><fmt:message key="label.forgot.password"/></h1>
                </div>
                
                <table class="body">
                <tr>
                    <td class="align-right">
                        <fmt:message key="label.password.new.password"/>:
                    </td>
                    <td class="align-left">
                        <input type="password" id="newPassword" name="newPassword" size="50" maxlength="50"/>
                    </td>
                </tr>
                <tr>
                    <td class="align-right">
                        <fmt:message key="label.password.confirm.new.password"/>:
                    </td>
                    <td class="align-left">
                        <input type="password" id="confirmNewPassword" name="confirmNewPassword" size="50" maxlength="50"/>                   
                    </td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                
                    <td>
                        <html:submit styleClass="button"><fmt:message key="button.save"/></html:submit>     
                    </td>
                </tr>
            </table>
    </div>
    </td></tr></table>
<form>
</body> 

</lams:html>          