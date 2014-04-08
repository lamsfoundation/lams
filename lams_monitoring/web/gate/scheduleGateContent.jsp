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
<%@ taglib uri="tags-lams" prefix="lams" %>

	<div id="content">

		<h1><fmt:message key="label.schedule.gate.title"/></h1>

		<%@ include file="gateInfo.jsp" %>
		<c:choose>
			<c:when test="${GateForm.map.activityCompletionBased}">
				<fmt:message key="label.schedule.gate.activity.completion.based"/>
			</c:when>
			<c:otherwise>
				<c:if test="${not GateForm.map.gate.gateOpen}" >
					<p><fmt:message key="label.schedule.gate.open.message"/> <lams:Date value="${GateForm.map.startingTime}"/></p>
				</c:if>
				
				<c:if test="${GateForm.map.endingTime!=null}">
					<p><fmt:message key="label.schedule.gate.close.message"/> <lams:Date value="${GateForm.map.endingTime}"/></p>
				</c:if>
			</c:otherwise>
		</c:choose>

		<%@ include file="gateStatus.jsp" %>

	</div>  <!--closes content-->

