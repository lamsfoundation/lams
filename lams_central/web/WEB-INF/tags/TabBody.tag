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
  * TabBody.tag
  *	Author: Mitchell Seaton
  *	Description: Creates the body container for a tab element
  * Wiki: 
  */
 
 %>
<%@ tag body-content="scriptless" %>
<%@ attribute name="id" required="true" rtexprvalue="true" %>
<%@ attribute name="title" required="false" rtexprvalue="true" %>
<%@ attribute name="titleKey" required="false" rtexprvalue="true" %>
<%@ attribute name="page" required="false" rtexprvalue="true" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-bean" prefix="bean" %>

<!-- begin tab content -->
<div id="tabbody${id}">
<c:if test="${title != null}">
	<h2><font size=2> <b><bean:write name="title" scope="page"/></b></font></h2>
</c:if>
<c:if test="${titleKey != null && title == null}">
	<h2><font size=2> <b><bean:message name="titleKey" scope="page"/></b></font></h2>
</c:if>

<div id="formtablecontainer">
<c:choose>
	<c:when test="${page != null}">
		<jsp:include page="${page}"/>
	</c:when>
	<c:otherwise>
		<jsp:doBody/>
	</c:otherwise>
</c:choose>
</div>

</div>
<!-- end tab content -->