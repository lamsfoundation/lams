<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
 
<html> 
	<head>
		<title>JSP for loginRepositoryForm form</title>
	</head>
	<body>
		<H1>Repository Demonstration</H1>
		<p>Please enter the tools details to examine the repository for this tool/workspace. If you
		are using the basic JUNIT test data, use tool name "atool", indentification string "atool"
		and workspace name "atoolWorkspace". </p>
		<html:form action="/loginRepository?method=login">
			<TABLE>
			<TR><TD>Tool Name:</TD>
				<TD><html:text property="toolName"/><html:errors property="toolName"/></TD></TR>
			<TR><TD>Indentification String (aka Password):</TD>
				<TD><html:text property="indentificationString"/><html:errors property="indentificationString"/></TD></TR>
			<TR><TD>Workspace Name</TD>
				<TD><html:text property="workspaceName"/><html:errors property="workspaceName"/></TD></TR>
			</TABLE>
			 <html:submit/>
		</html:form>
	</body>
</html>
