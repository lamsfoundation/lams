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
	 * Tab.tag
	 *	Author: Fiona Malikoff
	 *	Description: Creates a tab element within the Hybrid Tab Header.
	 * Wiki: 
	 */
%>
<%@ tag body-content="empty"%>
<%@ attribute name="id" required="true" rtexprvalue="true"%>
<%@ attribute name="value" required="false" rtexprvalue="true"%>
<%@ attribute name="key" required="false" rtexprvalue="true"%>
<%@ attribute name="inactive" required="false" rtexprvalue="true"%>
<%@ attribute name="methodCall" required="false" rtexprvalue="true"%>

<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-lams" prefix="lams"%>

<%-- Check if  bundle is set --%>
<c:if test="${not empty bundle}">
	<fmt:setBundle basename="${bundle}"/>
</c:if>

<c:set var="lams">
	<lams:LAMSURL />
</c:set>

<c:set var="tabTitle" value="${value}" />

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

<c:if test="${key != null && value == null}">
	<c:set var="tabTitle">
		<fmt:message key="${key}" />
	</c:set>
</c:if>

<c:set var="class" value=""/>
<c:if test="${id == 1}">
	<c:set var="class">class="active"</c:set>
</c:if>
	
<c:set var="disabled" value=""/>
<c:if test="${inactive}">
	<c:set var="class">class="disabled"</c:set>
</c:if>

<li role="presentation" ${class}><a href="#t${id}" aria-controls="t${id}" role="tab" onclick="${methodCall}(${id});return false;">${tabTitle}</a></li>
