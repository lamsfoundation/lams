
<?xml version="1.0" encoding="iso-8859-1"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=iso-8859-1" language="java" %>
<%@ page import="com.lamsinternational.lams.util.JspRedirectStrategy" %>
<%
	String failed = request.getParameter("failed");
	if (failed == null) //Login page
		{
		if (JspRedirectStrategy.loginPageRedirected(request,response))
				return;
		}
	else //Error page
		{
		if (JspRedirectStrategy.errorPageRedirected(request,response))
				return;	
		}
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<!--
flash is searching for this string, so leave it!:
j_security_login_page
-->
<head>
<title>Login - LAMS :: Learning Activity Management System</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<link href="style.css" rel="stylesheet" type="text/css" />

<script language="JavaScript" type="text/JavaScript">
<!--
function pviiClassNew(obj, new_style) { //v2.7 by PVII
  obj.className=new_style;
}
//-->
</script>
</head>
<body bgcolor="#9DC5EC" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table width="95%" height="95%" border="0" align="center" cellpadding="0" cellspacing="0">
      <tr>
        <td>
        

<!-- header start -->
      <table width="100%" border="0" cellpadding="0" cellspacing="0">
        <tr> 
          <td> <img height=10 src="images/spacer.gif"> 
            <!-- NOT WORKING MESSED UP BY ROLL OVER ON BUTTON
			<div id="title">
          <div align="right"><font color="#FFFFFF" size="2" 
          face="Verdana, Arial, Helvetica, sans-serif">
          <strong>Welcome</strong></font></div>
        </div>-->
          </td>
        </tr>
        <tr> 
          <td width="92%" height="7" > <img height="53" src="images/learner_header_right.gif" 
    	width="100%"></td>
        </tr>
        <tr bgcolor="#1C8AC6"> 
          <td height="8" align="right" > <font color="#FFFFFF" size="1" 
    face="Verdana, Arial, Helvetica, sans-serif"> Welcome</font><img height=8 width=8 
    src="images/spacer.gif"></td>
        </tr>
      </table>
<!-- header end -->

</td>
              <tr> 
              <td bgcolor="#FFFFFF" height="100%" valign="top">
                <!-- place tool content here -->

<table width="98%" height="100%" border="0" align="center" cellpadding="2" cellspacing="0">
  <!-- fwtable fwsrc="log_on.png" fwbase="learner_framework_progress_journal.jpg" fwstyle="Dreamweaver" fwdocid = "742308039" fwnested="0" -->

  <tr>
    <td colspan="3" align="center" valign="middle"><img height="7" src="images/spacer.gif" width="10" /></td>
    </tr>
  <tr>
    <td align="center" valign="middle"> <p><img height="300" src="images/customer_logo.gif" width="300" /></p>
     </td>
    <td width="100%">&nbsp;</td>
    <td width="200" style="{	border-left: solid #CCCCCC 1px;
					
							 	}">
<br><br>
<form action="<%= response.encodeURL("j_security_check") %>" method="post" name="form1" id="form1">
          <%
	//String failed = request.getParameter("failed");
	if ( failed != null ){
			%>

        <span class="error">Sorry, that username or password is not known. Please
        try again.</span>

 <%
     }
 %>
        <table width="150" height="87" border="0" align="center" cellpadding="0" cellspacing="0">
          <tr>
            <td width="51" align="right" class="smallText">Username</td>
            <td width="1">&nbsp;</td>
            <td width="90" align="right"> <input name="j_username" type="text" class="textField" size="15" /></td>
          </tr>
          <tr>
            <td align="right" class="smallText">Password</td>
            <td>&nbsp;</td>
            <td align="right"> <input name="j_password" type="password" class="textField" size="15" /></td>
          </tr>
          <tr>
            <td height="23" align="right">&nbsp;</td>
            <td>&nbsp;</td>
            <td align="right"> <input type="submit" value="Login" class="button" onmouseover="pviiClassNew(this,'buttonover')" onmouseout="pviiClassNew(this,'button')" />
            </td>
          </tr>
        </table>
       
        </form>
    <img height="1" src="images/spacer.gif" width="200" /></td>

  </tr>
  <tr valign="bottom">
    <td colspan="3" >	
      <table width="100%" border="0" cellpadding="0" cellspacing="0" class="lightNote">
        <tr valign="bottom">
          <td><a href="javascript:alert('LAMS&#8482; &copy; 2002-2005 LAMS Foundation. 
										\nAll rights reserved.
										\n\nLAMS is a trademark of LAMS Foundation.
										\nDistribution of this software is prohibited.');" class="lightNoteLink">&copy; 2002-2005
              LAMS Foundation.</a></td>
          <td align="center">This copy of LAMS&#8482; is authorised for use by
            the registered users only.</td>
          <td align="right"> Version 1.1</td>
        </tr>
      </table>
   		  </td>
    </tr>
</table>

                <!-- end tool content here -->
			  </td>
            </tr>
	<tr> 
         <td>
         

<!-- footer start -->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr bgcolor="#282871"> 
    <td width="136" height="8"></td>
    <td width="92%" height="8" align="right" ></td>
  </tr>
</table>
<!-- footer end -->

</td>
      </tr>
</table>
</body>
</html>
