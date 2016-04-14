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
 * Tabs.tag
 *	Author: Mitchell Seaton
 *	Description: Create a tab list from a input collection or nested Tab tags.
 * Wiki: 
 */

		%>
<%@ tag body-content="scriptless"%>
<%@ attribute name="collection" type="java.util.Collection" required="false" rtexprvalue="true"%>
<%@ attribute name="control" required="false" rtexprvalue="true"%>
<%@ attribute name="useKey" required="false" rtexprvalue="true"%>
<%@ attribute name="format" required="false" rtexprvalue="true"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<c:set var="dControl" value="false" scope="request" />
<c:if test="${control}">
	<c:set var="dControl" value="${control}" scope="request" />
</c:if>
<c:choose>
<c:when test="${empty format}">
	<c:set var="dFormat" value="nav-tabs" scope="request" />
</c:when>
<c:otherwise>
	<c:set var="dFormat" value="${format}" scope="request" />
</c:otherwise>
</c:choose>

<!-- tab holder table -->
<ul id="page-tabs" class="nav ${dFormat}" role="tablist">
	<jsp:doBody />
</ul>

<%--
	Usually methodCall is selectTab, but the calling code can override methodCall if desired.
	this is handy if the page needs different logic on initialisation and user switching tabs
	
	-- 			onclick="${methodCall}(${id});return false;">
	
--%>

<c:if test="${methodCall == null}">
	<c:choose>
		<c:when test="${dControl}">
			<c:set var="methodCall" value="doSelectTab" />
		</c:when>
		<c:otherwise>
			<c:set var="methodCall" value="selectTab" />
		</c:otherwise>
	</c:choose>
</c:if>
