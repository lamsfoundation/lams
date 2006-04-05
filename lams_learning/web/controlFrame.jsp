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
<%@ taglib uri="tags-bean" prefix="bean"%>
<%@ taglib uri="tags-html" prefix="html"%>
<%@ taglib uri="tags-tiles" prefix="tiles"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<html:html locale="true" xhtml="true">

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
		<lams:css/>
		<title>Learner :: LAMS</title>
	</head>

	<frameset rows="40,*" cols="*" framespacing="0" frameborder="NO" border="0">
		<frame src="controlTopFrame.jsp" name="topFrame" scrolling="NO" noresize >
		<!-- real frame, which uses Flash <frame src="controlBottomFrame.jsp" name="bottomFrame" scrolling="NO"> -->
		<frame src="dummylearner.do?method=getActiveLessons" name="bottomFrame" scrolling="NO">
	</frameset>
	
	<noframes>
		<body>
			<fmt:message key="message.activity.parallel.noFrames" />
		</body>
	</noframes>

</html:html>
