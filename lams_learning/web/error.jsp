<%-- 
Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
License Information: http://lamsfoundation.org/licensing/lams/2.0/

  This program is free software; you can redistribute it and/or modify
  it under the terms of the GNU General Public License version 2 as 
  published by the Free Software Foundation.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
  USA

  http://www.gnu.org/licenses/gpl.txt
--%>

<%@ taglib uri="tags-html" prefix="html" %>
<%@ taglib uri="tags-logic" prefix="logic" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ page import="org.apache.struts.action.Action" %>
<%
String cprotocol = request.getProtocol();
if(cprotocol.startsWith("HTTPS")){
	cprotocol = "https://";
}else{
	cprotocol = "http://";
}
String pathToShare = cprotocol+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/..";
%>

<logic:messagesPresent> 
<table border="0" cellspacing="2" cellpadding="2" summary="This table is being used for layout purposes only">
<tr>
	<td width="10%"  align="right" >
		<img src="<%=pathToShare%>/images/error.jpg" alt="Error occured"/>
	</td>
	<td width="90%" valign="middle" class="body" colspan="2">
		 <html:messages id="error" message="false"> 
			 <c:out value="${error}" escapeXml="false"/><BR>
		 </html:messages> 
	</td>
</tr>
</logic:messagesPresent>
