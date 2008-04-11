<%@page import="org.apache.struts.action.ActionMessages" %>


<%@ taglib uri="tags-html" prefix="html" %>
<%@ taglib uri="tags-logic" prefix="logic" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-lams" prefix="lams" %>


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
        
        }
        
        
</script>
</lams:head>


<body class="stripes" >
<form action="<lams:LAMSURL/>/ForgotPasswordRequest" method="get">
    <input type="hidden" name="method" id="method" value="requestEmail" />
    
    <p><p><p>
    <table><tr><td align="center">    
	<div id="content">
	
	            <div id="title" align="left">
		            <h1><fmt:message key="label.forgot.password"/></h1>
		            <br/>
		            <fmt:message key="label.forgot.password.instructions"/>
	            </div>
	            
	            <table >
	                <tr>
	                    <td class="align-right">
	                        <fmt:message key="label.username"/>:
	                    </td>
	                    <td class="align-left">
	                        <input type="text" name="login" id="login" />
	                    </td>
	                </tr>
	                <tr>
	                    <td class="align-right">
	                        <fmt:message key="label.email"/>:
	                    </td>
	                    <td class="align-left">
	                        <input type="text" name="email" id="email" />
	                        
	                    </td>
	                </tr>
	                
	                <tr>
	                    <td>&nbsp;</td>
	                
	                    <td>
	                        <html:submit styleClass="button" onclick="javascript:validateForm();"><fmt:message key="label.ok"/></html:submit>     
	                        <html:button property="cancel" styleClass="button" onclick="javascript:toHome();"><fmt:message key="button.cancel"/></html:button>
	                    
	                    </td>
	                </tr>
	            </table>
	</div>
	</td></tr></table>
<form>
</body> 

</lams:html>          