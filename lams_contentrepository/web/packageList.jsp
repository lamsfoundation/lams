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
<%@ page language="java"%>

<%@ page import="java.util.*"%>
<%@ page import="org.lamsfoundation.lams.contentrepository.IValue"%>
<%@ page import="org.lamsfoundation.lams.contentrepository.IVersionedNode"%>
<%@ page import="org.lamsfoundation.lams.contentrepository.PropertyName"%>

<%@ taglib uri="tags-bean" prefix="bean" %>
<%@ taglib uri="tags-html" prefix="html" %>
<%@ taglib uri="tags-logic" prefix="logic" %>

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
