
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'MyJsp.jsp' starting page</title>
    
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
    
    <!--
    <link rel="stylesheet" type="text/css" href="styles.css">
    -->
  </head>
  
  <body>
   <form action="<%= response.encodeURL("j_security_check") %>" method="post" name="form1" id="form1">
        <table width="150" height="87" border="0" align="center" cellpadding="0" cellspacing="0">
          <tr>
            <td width="51" align="right" class="smallText">Username</td>
            <td width="1">&nbsp;</td>
            <td width="90" align="right"> <input name="j_username" type="text" class="textField" size="15" /></td>
          </tr>
          <tr>
            <td align="right" class="smallText">Password</td>
            <td>&nbsp;</td>
            <td align="right"> <input name="j_password" type="password" class="textField" size="15" AUTOCOMPLETE="off"/></td>
          </tr>
          <tr>
            <td height="23" align="right">&nbsp;</td>
            <td>&nbsp;</td>
            <td align="right"> <input type="submit" value="Login"/>
            </td>
          </tr>
        </table>
       
      </form>

  </body>
</html>
