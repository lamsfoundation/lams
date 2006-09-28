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
  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
  USA

  http://www.gnu.org/licenses/gpl.txt
--%>

<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib uri="tags-bean" prefix="bean"%>
<%@ taglib uri="tags-html" prefix="html"%>
<%@ taglib uri="tags-tiles" prefix="tiles"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-lams" prefix="lams" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<html:html locale="true" xhtml="true">

	<tiles:useAttribute name="title" ignore="false" />

	<%-- includeBodyTag, this variable is used to stop the default behaviour of
	displaying body tags (for use with a frameset) --%>
	<tiles:useAttribute name="includeBodyTag" ignore="true" />

	
	<head>
		<title>LAMS</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		
		<%-- if localFiles == true then wanted for export portfolio and must run offline --%>
		<c:choose>
		<c:when test="${localFiles == true}">
			<lams:css localLinkPath="../"/>
		</c:when>
		<c:otherwise>
			<lams:css/>
			<c:set var="lams"><lams:LAMSURL/></c:set>
			<script src="${lams}includes/javascript/AC_RunActiveContent.js" type="text/javascript"></script>
			<script type="text/javascript" src="${lams}includes/javascript/common.js"></script>
		</c:otherwise>
		</c:choose>
	  </head>

	<c:choose>
			<c:when test="${includeBodyTag}">
				<body>
					<div id="page-learner"><!--main box 'page'-->
						<tiles:insert attribute="body" />
					</div>
				</body>
				
			</c:when>
			<c:otherwise>
					<div id="page-learner"><!--main box 'page'-->
						<tiles:insert attribute="body" />
					</div>
			</c:otherwise>

	</c:choose>

</html:html>
