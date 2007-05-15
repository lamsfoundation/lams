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


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
            "http://www.w3.org/TR/html4/loose.dtd">
<lams:html>

<head>
	<meta http-equiv="content-type" content="text/html; charset=UTF-8">
	<title><c:out value="${portfolio.lessonName}"/></title>
    <fmt:setBundle basename = "org.lamsfoundation.lams.learning.ApplicationResources" />

	<lams:css localLinkPath="./"/>

</head>

<body class="stripes">

		<div id="content">
		
		<h1>
			<c:out value="${portfolio.lessonName}"/>
		</h1>

		<c:choose>
			<c:when test="${not empty portfolio.learnerName}">
				<H2><fmt:message key="export.portfolio.for.user.heading"/> <c:out value="${portfolio.learnerName}"/></H2>
			</c:when>
			<c:otherwise>
				<H2><fmt:message key="export.portfolio.for.class.heading"/> <c:out value="${portfolio.learnerName}"/></H2>
			</c:otherwise>
		</c:choose>
			
		<p>&nbsp;</p>
		
		<c:if test="${not empty portfolio.lessonDescription}">
			<p><c:out value="${portfolio.lessonDescription}"/></p>
		</c:if>

		<p><fmt:message key="export.portfolio.lesson.started.date.label"/> 
			<lams:Date value="${portfolio.lessonStartDate}"/></p>

		<p><fmt:message key="export.portfolio.generated.date.label"/> 
			<lams:Date value="${portfolio.portfolioCreatedDate}"/></p>

		<c:if test="${!empty portfolio.notebookPortfolios}">
			<p>
				<a href="${portfolio.notebookLink}" target="_blank">
					<fmt:message key="export.portfolio.notebook.link.label"/>
				</a>
			</p>
		</c:if>

		<c:if test="${empty portfolio.activityPortfolios}">
			<p><fmt:message key="export.portfolio.noneAttempted.message"/></p>
		</c:if>
	
		<div class="align-left" width="90">
			<c:forEach var="actport" items="${portfolio.activityPortfolios}" varStatus="status">
				<c:if test="${status.first}">
					<UL>
				</c:if>
	
				<lams:ExportPortOutput actport="${actport}"/>
				<c:if test="${status.last}">
					</UL>
				</c:if>
			</c:forEach>
		</div>
		
		</div>
		<div id="footer"></div>
	</div>

</body>
	
</lams:html>