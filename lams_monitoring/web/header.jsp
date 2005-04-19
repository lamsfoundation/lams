<!--
Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
USA

http://www.gnu.org/licenses/gpl.txt
-->
<%@ taglib uri="/WEB-INF/jstl/c.tld" prefix="c" %>
<%@ page contentType="text/html; charset=iso-8859-1" language="java" %>
<%
String protocol = request.getProtocol();
if(protocol.startsWith("HTTPS")){
	protocol = "https://";
}else{
	protocol = "http://";
}
String pathToRoot = protocol+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";

%>
<tr>
<td>
<table width="100%" border="0" cellpadding="0" cellspacing="0" summary="This table is being used for layout purposes only">
  <tr> 
    <td width="136" height="10"></td>
    <td width="92%" height="10"></td>
  </tr>
  <tr bgcolor="#282871"> 
    <td width="50%" height="15" align="left">
    		<img height=8 width=8 src="<%=pathToRoot%>images/spacer.gif">
    		<font color="#FFFFFF" size="1" face="Verdana, Arial, Helvetica, sans-serif"><c:out value="${pageheader}"/></font>
    </td>        
    <td width="50%" height="15" align="right" > 
    		<a href="<%=pathToRoot%>doc/LAMS_Learner_Guide_b60.pdf" target = 0>
    			<font color="#FFFFFF" size="1" face="Verdana, Arial, Helvetica, sans-serif">[HELP]</font>
    		</a>
    		<img height=8 width=8 src="<%=pathToRoot%>/images/spacer.gif"  alt="space image">
    </td>
  </tr>
</table>
</td>
</tr>
