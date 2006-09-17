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
	  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
	  USA

	  http://www.gnu.org/licenses/gpl.txt
	--%>

	<%@ taglib uri="tags-html" prefix="html" %>
	<%@ taglib uri="tags-bean" prefix="bean" %>
	<%@ taglib uri="tags-logic" prefix="logic" %>
	<%@ taglib uri="tags-core" prefix="c" %>		
	<%@ taglib uri="tags-fmt" prefix="fmt" %>

	<h1 class="no-tabs-below"><fmt:message key="label.permission.gate.title"/></h1>
	<div id="login-header">
	</div id="login-header">

	<div id="login-content">

		<p>&nbsp;</p> 
		<%@ include file="gateInfo.jsp" %>
		<p><fmt:message key="label.gate.you.open.message"/>

		<%-- set to read only if in export portfolio --%>
		<c:if test="${not GateForm.map.readOnly}"> 
			<c:if test="${not GateForm.map.gate.gateOpen}" >
				<html:form action="/gate?method=openGate" target="_self">
					<p><html:submit styleClass="button"><fmt:message key="label.gate.open"/></html:submit></p>
				</html:form>
			</c:if>
		</c:if>

		</p>

		<%@ include file="gateStatus.jsp" %>

	</div>  <!--closes content-->

