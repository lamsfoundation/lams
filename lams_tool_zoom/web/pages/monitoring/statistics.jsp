<%@ include file="/common/taglibs.jsp"%>

<c:set var="dto" value="${contentDTO}" />

<c:forEach var="session" items="${dto.sessionDTOs}">

	<c:if test="${dto.groupedActivity}">	
	    <div class="panel panel-default" >
        <div class="panel-heading">
			<span class="panel-title">
				<c:out value="${session.sessionName}" escapeXml="true"/>
			</span>
        </div>
	</c:if>

	<table class="table table-condensed">
		<tr>
			<td>
				<fmt:message>heading.totalLearnersInGroup</fmt:message>
			</td>
			<td>
				${session.numberOfLearners}
			</td>
		</tr>
	</table>
	
	<c:if test="${dto.groupedActivity}">	
		</div>
	</c:if>
	
</c:forEach>
