<%@ page contentType="text/html; charset=iso-8859-1" language="java" %>
<%
String title = request.getParameter("title");
/**/
String protocol = request.getProtocol();
if(protocol.startsWith("HTTPS")){
	protocol = "https://";
}else{
	protocol = "http://";
}
String pathToRoot = protocol+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
//out.println("<br>pathToRoot="+pathToRoot+"<br>");

%>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
  <tr> 
    <td width="136" height="10"></td>
    <td width="92%" height="10"></td>
  </tr>
  <tr bgcolor="#282871"> 
    <td width="50%" height="15" align="left">
    		<img height=8 width=8 src="<%=pathToRoot%>/images/spacer.gif">
    		<font color="#FFFFFF" size="1" face="Verdana, Arial, Helvetica, sans-serif"><%=title%></font>
    </td>
    <td width="50%" height="15" align="right" > 
    		<a href="<%=pathToRoot%>/doc/LAMS_Learner_Guide_b60.pdf" target = 0>
    			<font color="#FFFFFF" size="1" face="Verdana, Arial, Helvetica, sans-serif">[HELP]</font>
    		</a>
    		<img height=8 width=8 src="<%=pathToRoot%>/images/spacer.gif">
    </td>
  </tr>
</table>
