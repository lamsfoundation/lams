<%-- 
Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
License Information: http://lamsfoundation.org/licensing/lams/2.0/

  This program is free software; you can redistribute it and/or modify
  it under the terms of the GNU General Public License version 2 as 
  published by the Free Software Foundation.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
  USA

  http://www.gnu.org/licenses/gpl.txt
--%>

<%@ taglib uri="tags-html" prefix="html" %>
<%@ taglib uri="tags-bean" prefix="bean" %>
<%@ taglib uri="tags-logic" prefix="logic" %>
<%@ taglib uri="tags-core" prefix="c" %>		
<%@ taglib uri="tags-fmt" prefix="fmt" %>

<tr> 
	<td><H1><fmt:message key="label.schedule.gate.title"/></H1></td> 
</tr> 					  
<tr>
	<td><fmt:message key="label.schedule.gate.open.message"/> <fmt:formatDate value="${GateForm.map.startingTime}" type="both" dateStyle="full" timeStyle="full"/></td>
</tr>
<c:if test="${GateForm.map.endingTime!=null}">
<tr>
	<td><fmt:message key="label.schedule.gate.close.message"/> <fmt:formatDate value="${GateForm.map.endingTime}" type="both" dateStyle="full" timeStyle="full"/></td>
</tr>
</c:if>
	