<%@ page language="java"%>

<%@ page import="java.util.*"%>
<%@ page import="com.lamsinternational.lams.contentrepository.IVersionDetail"%>
<%@ page import="com.lamsinternational.lams.contentrepository.NodeKey"%>
<%@ page import="com.lamsinternational.lams.contentrepository.struts.action.RepositoryDispatchAction"%>

<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
 
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
		
		<P>Note: Node 1 for the atool workspace does not have a file in the repository so it will generate an error.
		 This is due to the data being designed for unit testing, not demonstration.</p>
		 
		<form>
			<INPUT TYPE="button" VALUE="Add File/Package" onClick="parent.location='addNode.jsp'">
		</form>
		<html:form action="/nodeSelection?method=logout">
			<html:submit value = "Logout"/>
		</html:form>
		
		<html:form action="/nodeSelection?method=getNode">
		<p><html:errors/></p>
		<p>
			<jsp:useBean id="nodeMap" type="java.util.Map" scope="request"/>
			<TABLE>
				<TR>
					<TD>Version</TD>
					<TD>Created Date Time</TD>
					<TD>Version Description</TD>
					<TD>File Name ( Application Cache )</TD>
				</TR>

			<% Iterator iter = nodeMap.keySet().iterator();
			   Map nodeCache = (Map) session.getAttribute(RepositoryDispatchAction.NODE_CACHE);
 			   while ( iter.hasNext()) {
					Long key = (Long) iter.next();
			%>
						<TR><TD colspan="4"><STRONG>Node </STRONG><html:submit property="uuid" value="<%=key.toString()%>"/></TD></TR>
			<%						
					Set versionDetails = (Set) nodeMap.get(key);
					Iterator setIter = versionDetails.iterator();
					while ( setIter.hasNext() ) {
						IVersionDetail detail = (IVersionDetail) setIter.next();
						NodeKey nodeKey = new NodeKey(key, detail.getVersionId());
						String nodeName = nodeCache != null ? (String)nodeCache.get(nodeKey) : "";
			%>
						<TR>
							<TD><%=detail.getVersionId()%></TD>
							<TD><%=detail.getCreatedDateTime()%></TD>
							<TD><%=detail.getDescription()%></TD>
							<TD><%=nodeName!=null?nodeName:""%></TD>
						</TR>
			<%
					} // end version details iterator
			%>
						<TR><TD colspan="4">&nbsp;</TD></TR>
			<%
				}// end node iterator
			%>
			</TABLE>
		</html:form>
	</body>
</html>
