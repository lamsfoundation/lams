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
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<fmt:formatDate value="${value}" type="both" dateStyle="long" timeStyle="full" />

