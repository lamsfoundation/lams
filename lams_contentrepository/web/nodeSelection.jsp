<%@ page language="java"%>

<%@ page import="java.util.*"%>
<%@ page import="com.lamsinternational.lams.contentrepository.IVersionDetail"%>
<%@ page import="com.lamsinternational.lams.contentrepository.NodeKey"%>
<%@ page import="com.lamsinternational.lams.contentrepository.struts.action.RepositoryDispatchAction"%>

<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>

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
		
		<P>Note: Nodes 1 thru 2 for the atool workspace does not have a file in the repository so it will generate an error.
		 This is due to the data being designed for unit testing, not demonstration.</p>
		 

		<html:form action="/nodeSelection">
			<p><html:errors/></p>
			<!-- hidden parameters - used for input on submit -->
			<p><input type="hidden" name="method" value="">
			<input type="hidden" name="version" value="">
			<input type="hidden" name="uuid" value="">

			<p><INPUT name="upload" onClick="parent.location='addNode.jsp'" TYPE="button" size="50" VALUE="Add File/Package" > 
			<input name="logout" onClick="setActionSubmit('logout', '', '')" type="button" size="50" value="Logout"/></p>

			<jsp:useBean id="nodeMap" type="java.util.Map" scope="request"/>
			<TABLE cellspacing="10">

			<% Iterator iter = nodeMap.keySet().iterator();
			   Map nodeCache = (Map) session.getAttribute(RepositoryDispatchAction.NODE_CACHE);
 			   while ( iter.hasNext()) {
					Long key = (Long) iter.next();
			%>
				<TR><TD colspan="6"><STRONG>Node <%=key.toString()%>:</STRONG>
				<input name="getNode" onClick="setActionSubmit('getNode', '<%=key.toString()%>', '')" type="button" size="50" value="Get Latest Version"/>
				<input name="uploadNewVersion" onClick="parent.location='addNode.jsp?uuid=<%=key.toString()%>'" type="button" size="50" value="Upload New Version"/>
				<input name="deleteNode" onClick="setActionSubmit('deleteNode', '<%=key.toString()%>', '')" type="button" size="50" value="Delete Node (All Versions)"/>
				</TD></TR>
			<%						
					Set versionDetails = (Set) nodeMap.get(key);
					Iterator setIter = versionDetails.iterator();
					while ( setIter.hasNext() ) {
						IVersionDetail detail = (IVersionDetail) setIter.next();
						NodeKey nodeKey = new NodeKey(key, detail.getVersionId());
						String nodeName = nodeCache != null ? (String)nodeCache.get(nodeKey) : "";
			%>
						<TR>
							<TD>Version: <%=detail.getVersionId()%></TD>
							<TD><input name="getNode" onClick="setActionSubmit('getNode', '<%=key.toString()%>', '<%=detail.getVersionId()%>')" type="button" size="50" value="Get File"/></TD>
							<TD><input name="deleteVersion" onClick="setActionSubmit('deleteNode', '<%=key.toString()%>', '<%=detail.getVersionId()%>')" type="button" size="50" value="Delete Version"/>
							<TD>Created&nbsp;Date&nbsp;Time: <%=detail.getCreatedDateTime()%></TD>
							<TD>Version&nbsp;Description: <%=detail.getDescription()%></TD>
							<TD>File&nbsp;Name&nbsp;(Application&nbsp;Cache):<%=nodeName!=null?nodeName:""%></TD>
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
