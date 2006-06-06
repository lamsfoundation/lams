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

<%@ taglib uri="tags-tiles" prefix="tiles" %>
<%@ taglib uri="tags-html" prefix="html" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

<tr>
	<td>
	<!-- Page Content table--> 
		<h1><c:out value="${title}"/></h1>
	
		<p><c:out value="${description}"/></p>

		<c:choose>
		<c:when test="${empty groups}">
			<p><fmt:message key="label.grouping.no.groups.created"/></p>
		</c:when>
		<c:otherwise>
			<table width="100%" border="0" cellpadding="3" cellspacing="4" class="body" summary="This table is being used for layout purposes">
				<c:forEach items="${groups}" var="group">
				<tr>
					<td align="right" class="bodyBold" style="{border-right: solid #CCCCCC 1px; border-bottom: solid #CCCCCC 1px; }">
						<c:out value="${group.groupName}"/>
					</td>
					<td width="85%" align="left" class="body"  style="{border-bottom: solid #CCCCCC 1px; }">
						<table width="100%" border="0" cellpadding="0" cellspacing="0">
							<c:forEach items="${group.users}" var="user">
							<tr>
								<td class="bodyBold">
									<c:out value="${user.firstName}"/> <c:out value="${user.lastName}"/>
								</td>
							</tr>
							</c:forEach>
						</table>		
					</td>
				</tr>
				</c:forEach>
			</table>
		</c:otherwise>
		</c:choose>
	<!-- end of Page Content table--> 
	</td>
</tr>