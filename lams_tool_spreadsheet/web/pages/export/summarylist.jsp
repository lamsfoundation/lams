<%@ page import="org.lamsfoundation.lams.tool.spreadsheet.SpreadsheetConstants"%>
<table cellpadding="0" >

	<tr>
		<td class="field-name" colspan="2">
				<h1><fmt:message key="monitoring.label.group" /> ${summary.sessionName}	</h1>
		</td>
	</tr>
	
	<html:hidden property="spreadsheet.code" styleId="spreadsheet.code" value="${code}"/>

	<c:forEach var="user" items="${summary.users}">
						
		<tr>
			<td class="field-name" style="width:250px">
				<fmt:message key="label.monitoring.vieawallmarks.learner" />
			</td>
			<td>
				<c:choose>
					<c:when test="${spreadsheet.learnerAllowedToSave}">
						<c:set var="code" value="${null}" />			
						<c:if test="${user.userModifiedSpreadsheet != null}">
							<c:set var="code" value="${user.userModifiedSpreadsheet.userModifiedSpreadsheet}" />
						</c:if>
					    <html:hidden property="spreadsheet.code${user.uid}" styleId="spreadsheet.code${user.uid}" value="${code}"/>
					    <html:hidden property="userName${user.uid}" styleId="userName${user.uid}" value="${user.loginName}"/>
					    
						<a href="#" onclick="javascript:popitup2(${user.uid});" > ${user.loginName}</a>
					</c:when>
					<c:otherwise>
						${user.loginName}
					</c:otherwise>
				</c:choose>			
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
							<fmt:formatNumber type="number" maxFractionDigits="<%= SpreadsheetConstants.MARK_NUM_DEC_PLACES %>" value="${user.userModifiedSpreadsheet.mark.marks}"/>
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
				<c:out value="${reflectDTO.fullName}" escapeXml="true"/>
			</h4>
			<lams:out value="${reflectDTO.reflect}" escapeHtml="true" />
	</c:forEach>
</c:if>
<br>
<br>
<br>
<br>

