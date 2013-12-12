<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="summaryList" value="${sessionMap.summaryList}"/>


<table cellspacing="3" style="width: 400px; padding-left: 30px;">
	<c:if test="${empty summaryList}">
		<div align="center">
			<b> <fmt:message key="message.monitoring.summary.no.session" /> </b>
		</div>
	</c:if>
	
	<c:forEach var="summary" items="${summaryList}" varStatus="firstGroup">
		<tr>
			<td colspan="4" style="padding-top: 40px;">
				<B><fmt:message key="monitoring.label.group" /> ${summary.sessionName}</B> 
			</td>
		</tr>
		<tr>
			<th width="20px;" style="text-align: center; padding-left: 0px;">
				#
			</th>				
			<th width="150px;" style="padding-left: 0px;">
				<fmt:message key="label.monitoring.summary.user.name" />
			</th>
			<th width="80px;" style="padding-left: 0px;">
				<fmt:message key="label.monitoring.summary.mark" />
			</th>					
		</tr>	
	
		<c:forEach var="user" items="${summary.users}" varStatus="status">
			<tr>
				<td>
					${status.index + 1}
				</td>				
				<td>
					${user.firstName} ${user.lastName}
				</td>
				<td>
					<c:choose>
						<c:when test='${summary.totalAttempts == 0}'>-</c:when> 
						<c:otherwise>${summary.mark}</c:otherwise>
					</c:choose>
				</td>
			</tr>
		</c:forEach>
	</c:forEach>
</table>

