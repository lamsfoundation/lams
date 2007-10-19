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

<%@ taglib uri="tags-tiles" prefix="tiles" %>
<%@ taglib uri="tags-html" prefix="html" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-lams" prefix="lams" %>


<div id="content">

	<h1><c:out value="${title}"/></h1>

	<c:if test="${not empty description}">
		<p>&nbsp;</p>
		<p><c:out value="${description}"/></p>
	</c:if>

	<table class="alternative-color" cellspacing="0">
		<tr>
			<th><fmt:message key="label.branching.branch.heading"/></th>
			<th>&nbsp;</th>
		</tr>
			
		<c:forEach items="${branching.branches}" var="branch">
		<tr>
			<td width="25%" class="first">
				<c:out value="${branch.branchName}"/>
			</td>
			<td>
				<c:forEach items="${branch.groups}" var="group" varStatus="status">
 					<c:if test="${showGroupName}">
						<strong><c:out value="${group.groupName}"/></strong><BR>
					</c:if>
					<c:forEach items="${group.users}" var="user">
						<c:out value="${user.firstName}"/> <c:out value="${user.lastName}"/><BR>
					</c:forEach>
				</c:forEach>
			</td>
		</tr>
		</c:forEach>
	</table>
		
	<c:if test="${not localFiles}">
		<%@ include file="../template/finishbutton.jsp" %>
	</c:if>
</div>  <!--closes content-->


