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

<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

<!DOCTYPE html>
<lams:html>
<lams:head>
    <title><c:out value="${title}"/></title>
    
	<lams:css/>
	<style type="text/css">
		iframe {
			border: none;
			width: 100%;
			height: 425px;
		}
	</style>
</lams:head>

<body class="stripes">
<div id="content">
	<h1>
		<c:out value="${title}"/>
	</h1>
	
	<c:if test="${not empty description}">
		<p><c:out value="${description}"/></p>
	</c:if>
	

	<p>
		<strong><fmt:message key="label.grouping.general.instructions.heading"/></strong>
	</p>
	
	<c:if test="${not usedForBranching}">
		<p>
			<fmt:message key="label.grouping.general.instructions.line1"/>
		</p>
	</c:if>
	
	<p>
		<fmt:message key="label.grouping.general.instructions.line2"/>
	</p>
	
	<c:if test="${usedForBranching}">
		<p><fmt:message key="label.grouping.general.instructions.branching"/></p>
	</c:if>
	
	<iframe src="<lams:LAMSURL/>OrganisationGroup.do?method=viewGroupings&lessonID=${param.lessonID}&activityID=${param.activityID}"></iframe>
</div>
</body>
</lams:html>