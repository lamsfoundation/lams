<%@ tag body-content="scriptless"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-lams" prefix="lams" %>

<%@ attribute name="mode" required="true" rtexprvalue="true"%>
<%@ attribute name="groupUsers" required="true" rtexprvalue="true"
	type="java.util.AbstractCollection<org.lamsfoundation.lams.usermanagement.User>"%>
<%@ attribute name="isUserIdPropertyCapitalized" required="fale" rtexprvalue="true"%>
<%@ attribute name="refreshKey" required="false" rtexprvalue="true"%> 
<%@ attribute name="waitingForLeaderKey" required="false" rtexprvalue="true"%>

<%-- Default value for I18N keys --%>
<c:if test="${empty isUserIdPropertyCapitalized}">
	//org.lamsfoundation.lams.usermanagement.User has non-capitalized userId property
	<c:set var="isUserIdPropertyCapitalized" value="false" />
</c:if> 
<c:if test="${empty refreshKey}">
	<c:set var="waitingForLeaderKey" value="label.waiting.for.leader" />
</c:if> 
<c:if test="${empty refreshKey}">
	<c:set var="refreshKey" value="label.refresh" />
</c:if>

<script type="text/javascript">
	function refresh() {
		location.reload();
	}

	<c:if test="${mode != teacher}">
		//refresh page every 30 sec
		setTimeout("refresh();", 30000);
	</c:if>
</script>

<div class="container-lg">
	<lams:Alert5 id="waiting-for-leader-alert" type="info">
		<fmt:message key="${waitingForLeaderKey}" />
	</lams:Alert5>

	<div class="card lcard mt-1">
		<div class="card-header">
			<fmt:message key="label.users.from.group" />
		</div>

		<div class="card-body mb-3">
			<div id="usersInGroup" class="row mt-2" role="list">
				<c:forEach var="user" items="${groupUsers}">
					<c:choose>
						<c:when test="${isUserIdPropertyCapitalized}">
							<c:set var="userId" value="${user.userID}"/>
						</c:when>
						<c:otherwise>
							<c:set var="userId" value="${user.userId}"/>
						</c:otherwise>
					</c:choose>
					<div role="listitem" class="col-md-4 my-2 text-md-start">
						<lams:Portrait userId="${userId}" />
						<span> 
							<c:out value="${user.firstName} ${user.lastName}" escapeXml="true" />
						</span>
					</div>
				</c:forEach>
			</div>
		</div>
	</div>

	<c:if test="${mode != teacher}">
		<div class="activity-bottom-buttons">
			<button name="refreshButton" id="refreshButton" onclick="refresh();" class="btn btn-primary btn-icon-refresh">
				<fmt:message key="${refreshKey}" />
			</button>
		</div>
	</c:if>
</div>