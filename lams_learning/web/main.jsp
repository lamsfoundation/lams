<%--
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
--%>

<%@ page language="java"%>
<%@ taglib uri="http://jakarta.apache.org/struts/struts-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/struts-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/struts-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<html:html locale="true" xhtml="true">

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
		<link href="css/learner.css" rel="stylesheet" type="text/css">
		<title>Learner :: LAMS</title>
		<script language="JavaScript" type="text/JavaScript"><!--
			// TODO: replace with proper URL
			var serverURL = "http://127.0.0.1:8080/lams/learning/";
			//-->
		</script>
	</head>

	<frameset rows="*" cols="178,*">
		<frame src="controlFrame.jsp" name="controlFrame" scrolling="NO">
		<frame src="content.do" name="contentFrame" scrolling="YES">
	</frameset>
	
	<noframes>
		<body>
			<fmt:message key="message.activity.parallel.noFrames" />
		</body>
	</noframes>

</html:html>
