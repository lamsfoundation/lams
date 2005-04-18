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
<%@ taglib uri="/WEB-INF/struts-html-el.tld" prefix="html-el" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="org.apache.struts.action.Action" %>
<%@ page import="org.apache.struts.action.ActionErrors" %>
<%
String cprotocol = request.getProtocol();
if(cprotocol.startsWith("HTTPS")){
	cprotocol = "https://";
}else{
	cprotocol = "http://";
}
String rootPath = cprotocol+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>
<logic:present name="<%=Action.ERROR_KEY%>">
<tr>
	<td width="10%"  align="right" >
		<img src="<%=rootPath%>/images/error.jpg" alt="Error occured"/>
	</td>
	<td width="90%" valign="center" class="body" colspan="2">
		<html-el:errors/>
	</td>
</tr>
</logic:present>