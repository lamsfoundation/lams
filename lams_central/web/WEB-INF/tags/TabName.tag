<%/****************************************************************
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
 * TabName Tag
 *	Author: Mitchell Seaton
 *	Description: Shortens name that are too long to fit inside a tab
 */

		%>
<%@ tag body-content="scriptless" %>

<%@ attribute name="url" required="true" rtexprvalue="true"%>

<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-function" prefix="fn"%>

<c:set var="limit">10</c:set>
<c:set var="fullname"><jsp:doBody/></c:set>
<c:set var="hasAlt" value="false"/>

<c:choose>
	<c:when test="${fn:length(fullname) > limit}">
			<c:set var="tabname" value="${fn:substring(fullname, 0, limit)}..."/>
	</c:when>
	<c:otherwise>
		<c:set var="tabname" value="${fullname}"/>
	</c:otherwise>
</c:choose>



<a class="tab-middle-link" href="<c:out value='${url}' />" title="<c:out value='${fullname}'/>">
	<c:out value="${tabname}" escapeXml="false"/>	
</a>