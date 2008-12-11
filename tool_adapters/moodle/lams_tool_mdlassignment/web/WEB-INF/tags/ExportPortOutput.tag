<% 
/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0
 * as published by the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
 
 /**
  * Passon
  *	Author: Fiona Malikoff
  *	Description: Outputs the Activity details on the main page in export portfolio. Recursive tag.
  * 
  */
 
 %>
<%@ tag body-content="empty" %>
<%@ attribute name="actport" required="true" rtexprvalue="true" type="org.lamsfoundation.lams.learning.export.ActivityPortfolio" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

<LI>
	<c:choose>
	<c:when test="${not empty actport.toolLink}">
		<a href="<c:out value="${actport.toolLink}"/>" target="_blank"/><c:out value="${actport.activityName}"/></a>
	</c:when>
	<c:otherwise>
		<c:out value="${actport.activityName}"/>		
	</c:otherwise>
	</c:choose>

	<c:forEach var="childactport" items="${actport.childPortfolios}" varStatus="childstatus">
		<c:if test="${childstatus.first}">
			<BR><UL>
		</c:if>
		<lams:ExportPortOutput actport="${childactport}"/>
		<c:if test="${childstatus.last}">
			</UL>
		</c:if>
	</c:forEach>

</LI>						

