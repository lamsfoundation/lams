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
  * Learner Flash Enabled
  *	Author: Fiona Malikoff
  *	Description: Is Flash enabled for learner?
  * 
  */
 
 %>
<%@ tag body-content="empty" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

<c:set var="serverEnableFlash"><lams:Configuration key="EnableFlash"/></c:set>
<c:set var="userEnableFlash"><lams:user property="enableFlash"/></c:set>

<c:choose>
	<c:when test="${serverEnableFlash and userEnableFlash}">true</c:when>
	<c:otherwise>false</c:otherwise>
</c:choose>
	

