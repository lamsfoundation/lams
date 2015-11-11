<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="sessionDtos" value="${sessionMap.sessionDtos}"/>

<h2>
	<fmt:message key="label.number.learners.per.session" />
</h2>

<table cellspacing="3" style="width: 400px; padding-left: 30px;">

	<c:choose>
		<c:when test="${empty sessionDtos}">
			<div align="center">
				<b> <fmt:message key="message.monitoring.summary.no.session" /> </b>
			</div>
		</c:when>
		
		<c:otherwise>
			<tr>
				<th width="150px;" style="padding-left: 0px;">
					<fmt:message key="label.monitoring.summary.user.name" />
				</th>
				<th width="80px;" style="padding-left: 0px;">
					<fmt:message key="label.monitoring.summary.total" />
				</th>					
			</tr>	
		</c:otherwise>
	
	</c:choose>
	
	<c:forEach var="sessionDto" items="${sessionDtos}" varStatus="firstGroup">
		<tr>				
			<td>
				<fmt:message key="monitoring.label.group" /> ${sessionDto.sessionName}
			</td>
			<td>
				${sessionDto.numberLearners}
			</td>
		</tr>
	</c:forEach>
</table>
