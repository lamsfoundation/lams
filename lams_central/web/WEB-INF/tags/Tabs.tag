<% 
/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
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
<%@ tag body-content="scriptless" %>
<%@ attribute name="collection" type="java.util.Collection" required="false" rtexprvalue="true" %>
<%@ attribute name="control" required="false" rtexprvalue="true" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-lams" prefix="lams" %>
<c:set var="dControl" value="false" scope="request"/>
<c:if test="${control}">
	<c:set var="dControl" value="${control}" scope="request"/>
</c:if>

<div class="tabmenu"><ul>
<c:choose>
	<c:when test="${collection != null}">
			<c:set var="count" value="0"/>
			<c:forEach var="tab" begin="0" items="${collection}">
				<c:set var="count" value="${count+1}"/>
				<lams:Tab id="${count}" value="${tab}"/>	
			</c:forEach>
	</c:when>
	<c:otherwise>
		<jsp:doBody/>
	</c:otherwise>
</c:choose>
</ul></div>