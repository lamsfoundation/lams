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
<%@ page import="org.lamsfoundation.lams.contentrepository.IVersionDetail"%>
<%@ page import="org.lamsfoundation.lams.contentrepository.IVersionedNode"%>
<%@ page import="org.lamsfoundation.lams.contentrepository.PropertyName"%>
<%@ page import="org.lamsfoundation.lams.contentrepository.NodeType"%>

<%@ taglib uri="tags-bean" prefix="bean" %>
<%@ taglib uri="tags-html" prefix="html" %>
<%@ taglib uri="tags-logic" prefix="logic" %>

<script language="JavaScript" type="text/JavaScript">
<!--

 function setActionSubmit(methodSelected, uuidSelected, versionSelected){
	document.forms[0].method.value = methodSelected;
	document.forms[0].uuid.value = uuidSelected;
	document.forms[0].version.value = versionSelected;
	document.forms[0].submit();
 }
 //-->
</script>
 
<html> 
	<head>
		<title>JSP for loginRepositoryForm form</title>
	</head>
	<body>

		<H1>View Node</H1>
		
		<p>Click on the numbered button to view the current version of this node. The number is the UUID of the node.</p>
		
		<p>The nodes with a name in the application cache column have been added in this session and hence the 
		name is known to the application cache (in the session). The rest of the nodes have been added to the 
		workspace at some other time. </p>
		
		<P>Note: Nodes 1 thru 4 for the atool workspace may have been generated in unit testing so they may result in errors. Best to add your own nodes and test them.</p>
		 

		<html:form action="/nodeSelection">
			<p><html:errors/></p>
			<!-- hidden parameters - used for input on submit -->
			<p><input type="hidden" name="method" value="">
			<input type="hidden" name="version" value="">
			<input type="hidden" name="uuid" value="">

			<p><INPUT name="upload" onClick="parent.location='addNode.jsp'" TYPE="button" size="50" VALUE="Add File/Package" > 
			<input name="logout" onClick="setActionSubmit('logout', '', '')" type="button" size="50" value="Logout"/></p>

			<jsp:useBean id="nodeList" type="java.util.List" scope="request"/>
			<TABLE cellspacing="10">

			<% Iterator iter = nodeList.iterator();
 			   while ( iter.hasNext()) {
					IVersionedNode node = (IVersionedNode) iter.next();
					Set versionDetails = (Set) iter.next();
					Long uuid = node.getUUID();
					String nodeType = node.getNodeType();
					String getButtonText = ( node.isNodeType(NodeType.FILENODE) ? "View File" : "View Package");
			%>
				<TR><TD colspan="6"><STRONG><%=nodeType%> Node <%=uuid.toString()%>:</STRONG>
				<input name="getNode" onClick="parent.location='download?uuid=<%=uuid.toString()%>'" type="button" size="50" value="Get Latest Version"/>
				<input name="uploadNewVersion" onClick="parent.location='addNode.jsp?uuid=<%=uuid.toString()%>&type=<%=nodeType%>'" type="button" size="50" value="Upload New Version"/>
				<input name="deleteNode" onClick="setActionSubmit('deleteNode', '<%=uuid.toString()%>', '')" type="button" size="50" value="Delete Node (All Versions)"/>
				</TD></TR>
				<TR>
					<TD>Version</TD>
					<TD>Created Date Time</TD>
					<TD>Version Description</TD>
					<TD>&nbsp;</TD>
					<TD>&nbsp;</TD>
				</TR>
			<%						
					Iterator setIter = versionDetails.iterator();
					while ( setIter.hasNext() ) {
						IVersionDetail detail = (IVersionDetail) setIter.next();
			%>
						<TR>
							<TD><%=detail.getVersionId()%></TD>
							<TD><%=detail.getCreatedDateTime()%></TD>
							<TD><%=detail.getDescription()%></TD>
							<TD><input name="getNode" onClick="parent.location='download?uuid=<%=uuid.toString()%>&version=<%=detail.getVersionId()%>'" type="button" size="50" value="Get File"/></TD>
							<TD><input name="copyNode" onClick="setActionSubmit('copyNode', '<%=uuid.toString()%>', '<%=detail.getVersionId()%>')" type="button" size="50" value="Copy Version"/>
							<TD><input name="deleteVersion" onClick="setActionSubmit('deleteNode', '<%=uuid.toString()%>', '<%=detail.getVersionId()%>')" type="button" size="50" value="Delete Version"/>
			<%
						if ( node.isNodeType(NodeType.PACKAGENODE) ) {
			%>				
							<TD><input name="viewPackage" onClick="setActionSubmit('viewPackage', '<%=uuid.toString()%>', '<%=detail.getVersionId()%>')" type="button" size="50" value="View Files In Package"/>
			<%
						}
			%>				
						</TR>
			<%
					} // end version details iterator
			%>
						<TR><TD colspan="6">&nbsp;</TD></TR>
			<%
				}// end node iterator
			%>
			</TABLE>
		</html:form>
	</body>
</html>
