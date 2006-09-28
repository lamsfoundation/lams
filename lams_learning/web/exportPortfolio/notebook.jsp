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
<html:html locale = "true">

<head>
	<meta http-equiv="content-type" content="text/html; charset=UTF-8">
	<fmt:setBundle basename = "org.lamsfoundation.lams.learning.ApplicationResources" />
	<c:set var="pageTitle">
		<fmt:message key="export.portfolio.notebook.title">
			<fmt:param><c:out value="${portfolio.lessonName}"/></fmt:param>
		</fmt:message>
	</c:set>
	<title><c:out value="${pageTitle}"/></title>
	    
	<lams:css localLinkPath="../"/>

</head>

<body class="stripes">

		<div id="content">
		
		<h1>
			<c:out value="${pageTitle}"/>
		</h1>

		<div align="left" width="90">
			<c:forEach var="entry" items="${portfolio.notebookPortfolios}" varStatus="status">
			<p>
				<b><c:out value="${entry.title}"/></b>
				<br><i>
				<c:choose>
					<c:when test="${entry.teacherViewable}"><fmt:message key="export.portfolio.notebook.public.label"/></c:when>
					<c:otherwise><fmt:message key="export.portfolio.notebook.private.label"/></c:otherwise>
				</c:choose>
				</i>
				<br><i><fmt:message key="export.portfolio.notebook.created.label">
						<fmt:param><lams:Date value="${entry.created}"/></fmt:param>
					</fmt:message></i>
				<br><i><fmt:message key="export.portfolio.notebook.modified.label">
						<fmt:param><lams:Date value="${entry.modified}"/></fmt:param>
					</fmt:message></i>
			</p>
			<p><c:out value="${entry.entry}"/></p>
			<hr>
			</c:forEach>
		</div>

		</div>
		<div id="footer"></div>
	</div>

</body>
	
</html:html>