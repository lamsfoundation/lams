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
<%@ page language="java"%>

<%@ page import="java.util.*"%>
<%@ page import="org.lamsfoundation.lams.contentrepository.IValue"%>
<%@ page import="org.lamsfoundation.lams.contentrepository.IVersionedNode"%>
<%@ page import="org.lamsfoundation.lams.contentrepository.PropertyName"%>

<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>

<html> 
	<head>
		<title>Package List</title>
	</head>
	<body>

		<jsp:useBean id="packageList" type="java.util.List" scope="request"/>

		<%
		Iterator iter = packageList.iterator();
		if ( iter.hasNext() ) {
			// first node is the pacakge
			IVersionedNode packageNode = (IVersionedNode) iter.next();
		%>
			<H1>Files in package <%=packageNode.getUUID()%></H1>
			<UL>
		<% 
			// rest of nodes are the children
			while ( iter.hasNext() ) {
				IVersionedNode childNode= (IVersionedNode) iter.next();
				IValue filenameProperty = childNode.getProperty(PropertyName.FILENAME);
				String filename = filenameProperty != null ? filenameProperty.getString() : "unknown";
		%>
				<LI>UUID: <%=childNode.getUUID()%> Filename: <%=filename%></LI>
		<%			
			}
		%>
			</UL>
		<%			
		} else {
		%>
			<P>Error: No nodes found in list</P>
		<% 
		}
		%>
			
		<INPUT TYPE="button" VALUE="View Node List" onClick="parent.location='nodeSelection.do?method=getList'">
	</body>
</html>
