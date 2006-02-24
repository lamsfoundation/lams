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
<%@ taglib uri="tags-bean" prefix="bean" %>
<%@ taglib uri="tags-html" prefix="html" %>
<%@ taglib uri="tags-logic" prefix="logic" %>
 
<html> 
	<head>
		<title>JSP for loginRepositoryForm form</title>
	</head>
	<body>
		<H1>Repository Demonstration</H1>
		<p>The first step in using the repository is to "log in" to a workspace with a tool name and indentification string.
		Generally each tool will have its own workspace. If the "log in" is successful, then a Ticket
		is returned. This ticket is used to access the workspace (get files, add new files, etc). In
		this demonstration, the ticket is placed in the session and the action classes get
		the ticket from the session and call the repository.</p>
		<p>To start, either create your own workspace or log in to an existing workspace. If
		the JUNIT test data has been loaded into the database, then there will be a workspace
		"atoolWorkspace", with the tool name "atool" and indentification string "atool". </p>
		<p>To create a new workspace, type in the new values in the fields below and click "createNewWorkspace".
		This will create the workspace and log you into the workspace.</p>
		<html:form action="/loginRepository">
			<TABLE>
			<TR><TD>Tool Name:</TD>
				<TD><html:text property="toolName"/><html:errors property="toolName"/></TD></TR>
			<TR><TD>Indentification String (aka Password):</TD>
				<TD><html:text property="indentificationString"/><html:errors property="indentificationString"/></TD></TR>
			<TR><TD>Workspace Name:</TD>
				<TD><html:text property="workspaceName"/><html:errors property="workspaceName"/></TD></TR>
			</TABLE>
			 <html:submit property="method" value="loginToWorkspace"/>
			 <html:submit property="method" value="createNewWorkspace"/>
		</html:form>
	</body>
</html>
