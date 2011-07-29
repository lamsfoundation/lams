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
<%@ attribute name="highlight" required="false" rtexprvalue="true" %>

<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-function" prefix="fn"%>

<c:set var="limit">12</c:set>
<c:set var="fullname"><jsp:doBody/></c:set>
<c:set var="titleValue" value=""/>

<c:choose>
	<c:when test="${fn:length(fullname) > limit}">
			<c:set var="titleValue" value="${fullname}"/>
			<c:set var="tabname" value="${fn:substring(fullname, 0, limit-2)}..."/>
	</c:when>
	<c:otherwise>
		<c:set var="tabname" value="${fullname}"/>
	</c:otherwise>
</c:choose>

<c:if test="${highlight eq true}">
	<c:set var="classVar" value="tab-link-highlight" />
</c:if>

<a class="tab-middle-link ${classVar}" href="<c:out value='${url}' />" title="<c:out value='${titleValue}'/>" style="border:0;">
	<c:out value="${tabname}" escapeXml="false"/>	
</a>