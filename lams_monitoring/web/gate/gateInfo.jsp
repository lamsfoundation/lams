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
<%@ taglib uri="tags-lams" prefix="lams" %>

<c:if test="${not empty GateForm.map.gate.description}">
	<!-- general information section--> 
	<p><lams:out value="${GateForm.map.gate.description}" escapeHtml="true"/></p>
</c:if>
	
<!--waiting learner information table-->
<p><strong>
		<fmt:message key="label.gate.waiting.learners">  
			<fmt:param value="${GateForm.map.waitingLearners}"/>
			<fmt:param value="${GateForm.map.totalLearners}"/>
		</fmt:message>
	</strong>
</p>


		<%-- set to read only if in export portfolio --%>
		<c:if test="${not GateForm.map.readOnly}"> 
			<c:if test="${not GateForm.map.gate.gateOpen}" >
				<html:form action="/gate?method=openGate" target="_self">
					<input type="hidden" name="activityId" value="${param.activityID}" />
					<p><html:submit styleClass="button"><fmt:message key="label.gate.open"/></html:submit></p>
				</html:form>
			</c:if>
		</c:if>

