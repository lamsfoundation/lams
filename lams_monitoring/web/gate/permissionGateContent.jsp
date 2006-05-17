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
		<td>
			<!-- Page Content table--> 
			<table width="90%" border="0" cellspacing="0" cellpadding="0" summary="This table is being used for layout purposes only"> 
				<tr><td>&nbsp; </td></tr> 
				<tr> 
					<td><H1><fmt:message key="label.permission.gate.title"/></H1></td> 
				</tr> 					  
				<%@ include file="gateInfo.jsp" %>
				<tr><td>&nbsp; </td></tr>
				<tr><td>&nbsp; </td></tr>
				<tr> 
					<td class="body" valign="middle"><fmt:message key="label.gate.you.open.message"/>

					<c:if test="${not GateForm.map.gate.gateOpen}" >
						<html:form action="/gate?method=openGate" target="_self">
							<html:submit styleClass="button"><fmt:message key="label.gate.open"/></html:submit> 
						</html:form>
					</c:if>
				</td>
				</tr>
				<tr>
					<td align="right"><%@ include file="gateStatus.jsp" %></td>
				</tr>
			</table> 
			<!-- end of Page Content table--> 
		</td>
	</tr>