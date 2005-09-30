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
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ page import="org.apache.struts.action.Action" %>
<%@ page import="org.apache.struts.action.ActionErrors" %>
<%
String curProtocol = request.getProtocol();
if(curProtocol.startsWith("HTTPS")){
	curProtocol = "https://";
}else{
	curProtocol = "http://";
}
String root = curProtocol+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>

<logic:messagesPresent message="true">
<tr>
	<td width="20%"  align="right" >
		<img src="<%=root%>/images/success.gif" alt="Message"/>
	</td>
	<td width="80%" valign="center" align="left" class="body" colspan="2">
	<ul>
		<html:messages message="true" id="msg">
		<li><font class='successMessage'><c:out value="${msg}"/></font></li>
		</html:messages>
	</ul>
	</td>
</tr>
</logic:messagesPresent>

