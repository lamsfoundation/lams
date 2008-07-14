<table cellpadding="0" >

	<tr>
		<td class="field-name" colspan="2">
				<h1><fmt:message key="monitoring.label.group" /> ${summary.sessionName}	</h1>
		</td>
	</tr>

	<c:forEach var="user" items="${summary.users}">
						
		<tr>
			<td class="field-name" style="width:250px">
				<fmt:message key="label.monitoring.vieawallmarks.learner" />
			</td>
			<td>
				<c:out value="${user.loginName}" />
			</td>
		</tr>	
					
		<c:if test="${spreadsheet.learnerAllowedToSave}">
			<tr>
				<td class="field-name" style="width:250px">
					<fmt:message key="label.monitoring.vieawallmarks.spreadsheet.submitted" />
				</td>
				<td>
					<c:choose>
						<c:when test="${user.userModifiedSpreadsheet != null}">
							<fmt:message key="label.monitoring.vieawallmarks.true" />
						</c:when>
						<c:otherwise>
							<fmt:message key="label.monitoring.vieawallmarks.false" />
						</c:otherwise>
					</c:choose>
				</td>
			</tr>					
		</c:if>	
			
		<c:if test="${spreadsheet.markingEnabled}">					
			<tr>
				<td class="field-name" style="width:250px">
					<fmt:message key="label.monitoring.vieawallmarks.marks" />
				</td>
				<td>
					<c:choose>
						<c:when test="${(user.userModifiedSpreadsheet != null) && (user.userModifiedSpreadsheet.mark != null)}">
							<c:out value="${user.userModifiedSpreadsheet.mark.marks}" escapeXml="false" />
						</c:when>
						<c:otherwise>
							<fmt:message key="label.learning.not.available" />
						</c:otherwise>
					</c:choose>
				</td>
			</tr>
						
			<tr>
				<td class="field-name" style="width:250px">
					<fmt:message key="label.monitoring.vieawallmarks.comments" />
				</td>
				<td>
					<c:choose>
						<c:when test="${(user.userModifiedSpreadsheet != null) && (user.userModifiedSpreadsheet.mark != null)}">
							<c:out value="${user.userModifiedSpreadsheet.mark.comments}" escapeXml="false" />
						</c:when>
						<c:otherwise>
							<fmt:message key="label.learning.not.available" />
						</c:otherwise>
					</c:choose>
				</td>
			</tr>
		</c:if>		
					
		<tr>
			<td colspan="2">
				<hr size="1" style="width:250px;" align="left"/>
			</td>
		</tr>
		<br><br>
						
	</c:forEach>
</table>

<%-- Display reflection entries --%>
<c:if test="${sessionMap.reflectOn}">
	<h3>
		<fmt:message key="label.export.reflection" />
	</h3>
		
	<c:forEach var="reflectDTO" items="${summary.reflectDTOList}">
			<h4>
				${reflectDTO.fullName}
			</h4>
			<lams:out value="${reflectDTO.reflect}" escapeHtml="true" />
	</c:forEach>
</c:if>
<br>
<br>
<br>
<br>

