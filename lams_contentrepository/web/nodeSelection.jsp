<%@ page language="java"%>

<%@ page import="java.util.*"%>
<%@ page import="com.lamsinternational.lams.contentrepository.IVersionDetail"%>

<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
 
<html> 
	<head>
		<title>JSP for loginRepositoryForm form</title>
	</head>
	<body>
		<H1>Select Node</H1>
		<p>Click on the numbered button to view the current version of this node. The number is the UUID of the node.</p>
		<P>Note: Node 1 for the atool workspace does not have a file in the repository so it will generate an error.
		 This is due to the data being designed for unit testing, not demonstration.</p>
		
		<html:form action="/nodeSelection?method=getNode">
		<p><html:errors/></p>
		<p>
			<jsp:useBean id="nodeMap" type="java.util.Map" scope="request"/>
			<TABLE>

			<% Iterator iter = nodeMap.keySet().iterator();
				while ( iter.hasNext()) {
					Object key = iter.next();
			%>
						<TR><TD colspan="3"><STRONG>Node </STRONG><html:submit property="uuid" value="<%=key.toString()%>"/></TD></TR>
			<%						
					Set versionDetails = (Set) nodeMap.get(key);
					Iterator setIter = versionDetails.iterator();
					while ( setIter.hasNext() ) {
						IVersionDetail detail = (IVersionDetail) setIter.next();
			%>
						<TR>
							<TD><%=detail.getVersionId()%></TD>
							<TD><%=detail.getCreatedDateTime()%></TD>
							<TD><%=detail.getDescription()%></TD>
						</TR>
			<%
					} // end version details iterator
			%>
						<TR><TD colspan="3">&nbsp;</TD></TR>
			<%
				}// end node iterator
			%>
			</TABLE>
		</html:form>
	</body>
</html>
