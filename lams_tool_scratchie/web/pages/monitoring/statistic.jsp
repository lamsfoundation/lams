<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="summaryList" value="${sessionMap.summaryList}"/>



	<c:if test="${empty summaryList}">
		<div align="center">
			<b> <fmt:message key="message.monitoring.summary.no.session" /> </b>
		</div>
	</c:if>
	
	<c:forEach var="summary" items="${summaryList}" varStatus="firstGroup">

	<h2><fmt:message key="monitoring.label.group" /> ${summary.sessionName}</h2> 

	<table cellspacing="3" style="width: 400px; padding-left: 30px;" class="alternative-color">
		<tr>
			<th width="20px;" style="text-align: center; padding-left: 0px;">
				#
			</th>				
			<th width="150px;" style="padding-left: 0px;">
				<fmt:message key="label.monitoring.summary.user.name" />
			</th>
			<th width="80px;" style="padding-left: 0px; text-align: center;'">
				<fmt:message key="label.monitoring.summary.mark" />
			</th>					
		</tr>	
	
		<c:forEach var="user" items="${summary.users}" varStatus="status">
			<tr>
				<td>
					${status.index + 1}
				</td>				
				<td>
					<c:out value="${user.firstName} ${user.lastName}" escapeXml="true"/>
				</td>
				<td align="center">
					<c:choose>
						<c:when test='${summary.totalAttempts == 0}'>-</c:when> 
						<c:otherwise>${summary.mark}</c:otherwise>
					</c:choose>
				</td>
			</tr>
		</c:forEach>
	</c:forEach>
</table>

