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
