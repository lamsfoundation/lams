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
 * TabBody.tag
 *	Author: Mitchell Seaton
 *	Description: Creates the body container for a tab element
 * Wiki: 
 */

		%>
<%@ tag body-content="scriptless"%>
<%@ attribute name="id" required="true" rtexprvalue="true"%>
<%@ attribute name="tabTitle" required="false" rtexprvalue="true"%>
<%@ attribute name="titleKey" required="false" rtexprvalue="true"%>
<%@ attribute name="page" required="false" rtexprvalue="true"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-bean" prefix="bean"%>

<!-- begin tab content -->
<div class="box" id="tabbody${id}">
	<c:choose>
		<c:when test="${page != null}">
			<jsp:include page="${page}" />
		</c:when>
		<c:otherwise>
			<jsp:doBody />
		</c:otherwise>
	</c:choose>

</div>
<!-- end tab content -->
