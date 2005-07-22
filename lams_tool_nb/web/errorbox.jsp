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

<%@ taglib uri="/WEB-INF/struts/struts-html-el.tld" prefix="html-el" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/jstl/c.tld" prefix="c" %>
<%@ page import="org.apache.struts.action.Action" %>
<%
String cprotocol = request.getProtocol();
if(cprotocol.startsWith("HTTPS")){
	cprotocol = "https://";
}else{
	cprotocol = "http://";
}
String pathToShare = cprotocol+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/../..";
%>
<div id="datatablecontainer">
<h2></h2>
<logic:messagesPresent> 
<table border="0" cellspacing="2" cellpadding="2" summary="This table is being used for layout purposes only">
<tr>
	<td width="10%"  align="right" >
		<img src="<%=pathToShare%>/images/error.jpg" alt="Error occured"/>
	</td>
	<td width="90%" valign="middle" class="body" colspan="2">
		 <html-el:messages id="error" message="false"> 
			 <c:out value="${error}" escapeXml="false"/><BR>
		 </html-el:messages> 
	</td>
</tr>
</logic:messagesPresent>
</div>

