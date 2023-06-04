<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />


<lams:PageLearner title="${sessionMap.title}" toolSessionID="${sessionMap.toolSessionID}" >

	<div id="instructions" class="instructions">
		<lams:out value="${sessionMap.reflectInstructions}" escapeHtml="true" />
	</div>
	<hr class="mx-5">
	<div class="container-lg">

		<div class="card lcard lcard-no-borders shadow mb-3">
			<div class="card-header lcard-header-button-border">
				<fmt:message key="${waitingMessageKey}" />
			</div>
			<div class="card-body mb-3">
				<c:if test="${not empty groupUsers}">

					<div class="mb-2">
						<fmt:message key="label.users.from.group" />
					</div>
					
					<div id="usersInGroup" class="row mt-2" role="list">
						<c:forEach var="user" items="${groupUsers}">
							<div role="listitem" class="col-md-4 my-2 text-md-start">
								<lams:Portrait userId="${user.userID}"/>
								<span>
									<c:out value="${user.firstName} ${user.lastName}" escapeXml="true" />
								</span>
							</div>
						</c:forEach>
					</div>
					
				</c:if>
			</div>
		</div>
		<div class="activity-bottom-buttons">
			<button id="finishButton" name="refreshButton" onclick="refresh();" class="btn btn-primary">
				<fmt:message key="label.refresh" />
			</button>
		</div>

	</div>				
	<script type="text/javascript">
		function refresh() {
			location.reload(true);
		}
		
		//refresh page every 30 sec
		setTimeout("refresh();",30000);
	
	</script>
</lams:PageLearner>

