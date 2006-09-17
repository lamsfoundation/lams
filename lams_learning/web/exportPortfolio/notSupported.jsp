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
<%@ taglib uri="tags-html" prefix="html"%>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-lams" prefix="lams" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html locale="true" xhtml="true">

<c:set var="pageTitle">
	<fmt:message key="${title.export.portfolio}">
		<fmt:param><c:out value="${requestScope.lessonName}"/></fmt:param>
	</fmt:message>
</c:set>

<head>
	<title><c:out value="${pageTitle}"/></title>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8">
		<lams:css/>
	</head>

	<body>
	<div id="page-learner"><!--main box 'page'-->

	<h1 class="no-tabs-below"><c:out value="${pageTitle}"/></h1>
	<div id="header-no-tabs-learner">

	</div><!--closes header-->

	<div id="content-learner">

		<p>	<fmt:message key="${error.export.portfolio.not.supported}">
				<fmt:param><c:out value="${requestScope.activityTitle}"/></fmt:param>
			</fmt:message>
		</p>
		
	</div>  <!--closes content-->

	</body>
	
</html:html>