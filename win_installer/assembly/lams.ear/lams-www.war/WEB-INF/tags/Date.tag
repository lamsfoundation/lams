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
  *	Author: Fiona Malikoff
  *	Description: Format a date, using the locale, based on standard parameters.
  * Need to use long for the date otherwise the AU locale comes out as 1/2/06 and
  * full is needed to include the timezone.
  */
 
 %>
<%@ tag body-content="empty" %>
<%@ attribute name="value" required="true" rtexprvalue="true" type="java.util.Date" %>
<%@ attribute name="style" required="false" rtexprvalue="true"%>
<%@ attribute name="type" required="false" rtexprvalue="true"%>
<%@ attribute name="timeago" required="false" rtexprvalue="true"%>

<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-core" prefix="c" %>

<c:if test="${empty type}">
	<c:set var="type" value="both" scope="request"/>
</c:if>

<c:if test="${not empty timeago}">
	<time class="timeago" datetime="<fmt:formatDate value='${value}' timeZone="GMT" pattern="yyyy-MM-dd'T'HH:mm:ss.S'Z'"/>">
</c:if>
	
<c:choose>
	<c:when test="${style=='short'}">
		<fmt:formatDate value="${value}" type="${type}" dateStyle="short" timeStyle="short" />
	</c:when>
	<c:when test="${style=='full'}">
		<fmt:formatDate value="${value}" type="${type}" dateStyle="long" timeStyle="full" />
	</c:when>
	<c:otherwise>
		<fmt:formatDate value="${value}" type="${type}" dateStyle="long" timeStyle="medium" />
	</c:otherwise>
</c:choose>

<c:if test="${not empty timeago}">
	</time>
</c:if>