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

<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib uri="tags-bean" prefix="bean"%>
<%@ taglib uri="tags-html" prefix="html"%>
<%@ taglib uri="tags-tiles" prefix="tiles"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-lams" prefix="lams" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<html:html locale="true" xhtml="true">

	<tiles:useAttribute name="pageTitle" ignore="false" />
	<tiles:useAttribute name="title" ignore="false" />
	
	<head>
		<title><c:out value="${pageTitle}" /></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<lams:css/>
		<META HTTP-EQUIV="Refresh" CONTENT="60;URL=<lams:WebAppURL/>/gate.do?method=knockGate">
	  </head>

	<body>
		<table width="95%" border="0" cellpadding="0" cellspacing="0" summary="This table is being used for layout purposes">
		<!-- Table has one column -->
			<tiles:insert attribute="body" />
			<tr><td>&nbsp;</td></tr>
			<%@ include file="../gate/gateNext.jsp" %>			  
		</table>
	</body>

</html:html>
