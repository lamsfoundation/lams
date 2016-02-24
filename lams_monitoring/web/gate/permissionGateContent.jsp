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
	<script type="text/javascript">
		function onSubmitForm(){
			var select = document.getElementById(document.pressed);
			if (select.selectedIndex==-1){
				return false;
			}
			else {
				document.getElementById("userId").value=select.options[select.selectedIndex].value;
				return true;
			}
		}
	</script>
	<div id="content">

		<h1><fmt:message key="label.permission.gate.title"/></h1>
		
		<%@ include file="gateInfo.jsp" %>
		
		
		<c:if test="${not GateForm.map.gate.gateOpen}" >
			<p><fmt:message key="label.gate.you.open.message"/></p>
		</c:if>

		<%@ include file="gateStatus.jsp" %>
		<c:if test="${not GateForm.map.gate.gateOpen}" >
			<hr />
			<p><fmt:message key="label.gate.open.single.learner"/></p>
			<html:form action="/gate?method=openGateForSingleUser" onsubmit="return onSubmitForm();" target="_self">
			<input type="hidden" id="activityId" name="activityId" value="${GateForm.map.activityId}" />
			<input type="hidden" id="userId" name="userId" />
			<table>
				<tr>
					<th><fmt:message key="label.gate.list.all.learners"/></th>
					<th><fmt:message key="label.gate.list.waiting.learners"/></th>
					<th><fmt:message key="label.gate.list.allowed.learners"/></th>
		   		 </tr>
				<tr>
					<td width="34%">
						<select style="width: 160px" id="forbidden" name="forbidden" size="10">
							<c:forEach var="learner" items="${GateForm.map.forbiddenLearnerList}">
								<option value="${learner.userId}"><c:out value="${learner.firstName} ${learner.lastName}" escapeXml="true"/></option>
							</c:forEach>
						</select>
					</td >
					<td width="33%">
						<select style="width: 160px"  id="waiting" name="waiting" size="10">
							<c:forEach var="learner" items="${GateForm.map.waitingLearnerList}">
								<option value="${learner.userId}"><c:out value="${learner.firstName} ${learner.lastName}" escapeXml="true"/></option>
							</c:forEach>
						</select>
					</td>
					<td width="33%">
						<select style="width: 160px" id="allowed" name="allowed" size="10" disabled="disabled">
							<c:forEach var="learner" items="${GateForm.map.allowedToPassLearnerList}">
								<option><c:out value="${learner.firstName} ${learner.lastName}" escapeXml="true"/></option>
							</c:forEach>
						</select>
					</td>
				</tr>
				<tr>
					<td>
						<input style="width: 160px" type="submit"  class="button"  value="<fmt:message key="label.gate.allow"/>" onclick="document.pressed='forbidden'"/>
					</td>
					<td>
						<input style="width: 160px" type="submit" class="button"  value="<fmt:message key="label.gate.allow"/>" onclick="document.pressed='waiting'"/>
					</td>
					<td>
						 &nbsp;
					</td>
				</tr>
			</table>
			</html:form>
		</c:if>
	</div>  <!--closes content-->
