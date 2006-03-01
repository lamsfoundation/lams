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
  * Tab.tag
  *	Author: Mitchell Seaton
  *	Description: Creates a tab element.
  * Wiki: 
  */
 
 %>
<%@ tag body-content="empty" %>
<%@ attribute name="id" required="true" rtexprvalue="true" %>
<%@ attribute name="value" required="true" rtexprvalue="true" %>
<%@ taglib uri="tags-core" prefix="c" %>
<c:set var="methodCall" value="selectTab"/>
<c:if test="${dControl}">
	<c:set var="methodCall" value="doSelectTab"/>
</c:if>
<li id="tab${id}" class="tabitem"><div class="tableft"><div class="tabright"><a href="javascript:${methodCall}(${id});">${value}</a> <!-- IE CSS Bug, If you remove the space infront this comment then height of the Tab will change in IE - Anthony --></div></div></li>