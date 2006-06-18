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

<%@ taglib uri="tags-html" prefix="html"%>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-lams" prefix="lams" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html locale = "true">

<head>
	<meta http-equiv="content-type" content="text/html; charset=UTF-8">
	<title><c:out value="${portfolio.lessonName}"/></title>
    <fmt:setBundle basename = "org.lamsfoundation.lams.learning.ApplicationResources" />
	<lams:css/>
</head>

<body>
	<H1><c:out value="${portfolio.lessonName}"/></H1>
	
	<p><c:out value="${portfolio.lessonDescription}"/></p>
	
	<c:forEach var="actport" items="${portfolio.activityPortfolios}" varStatus="status">
		<c:if test="${status.first}">
			<OL>
		</c:if>

		<LI>
			<a href="<c:out value="${actport.toolLink}"/>"/><c:out value="${actport.activityName}"/></a>: 
				<c:out value="${actport.activityDescription}"/>
		</LI>						
		<c:if test="${status.last}">
			</OL>
		</c:if>
	</c:forEach>
</body>
	
</html:html>