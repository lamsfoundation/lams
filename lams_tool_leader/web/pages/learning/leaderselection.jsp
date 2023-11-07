<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL /></c:set>
	
<lams:PageLearner title="${content.title}" toolSessionID="${toolSessionID}">
	<link type="text/css" href="${lams}css/jquery-ui-bootstrap-theme5.css" rel="stylesheet" />
	<style type="text/css">
	.dialog {
		display: none;
	}
	
	.ui-dialog-titlebar-close {
		display: none;
	}
	
	.ui-widget-overlay {
		opacity: 0.9;
	}
	</style>

	<script type="text/javascript">
		checkNextGateActivity('finishButton', '${toolSessionID}', '', finishActivity);
		
			$(document).ready(function(){
				if (${isSelectLeaderActive}) {
					$("#leaderSelectionDialog").modal('show');
				}
			});
		
			function leaderSelection() {
				$.ajax({
			    	async: false,
			        url: '<c:url value="/learning/becomeLeader.do?toolSessionID=${toolSessionID}"/>',
			        type: 'post',
			        dataType : 'text',
			        success: function () {
			          	location.reload();
			        }
			   	});
			}
		
		    function finishActivity(){
				location.href = '<c:url value="/learning/finishActivity.do?toolSessionID=${toolSessionID}"/>';
		    }
		
		 	//init the connection with server using server URL but with different protocol
		 	var leaderWebsocketInitTime = Date.now(),
		 		leaderWebsocket = new WebSocket('<lams:WebAppURL />'.replace('http', 'ws') 
		 			+ 'learningWebsocket?toolSessionID=' + ${toolSessionID}),
				leaderWebsocketPingTimeout = null,
				leaderWebsocketPingFunc = null;
		 	
		 	leaderWebsocket.onclose = function(e){
		 		// react only on abnormal close
		 		if (e.code === 1006 &&
		 		 	Date.now() - leaderWebsocketInitTime > 1000) {
		 	 		location.reload();
		 		}
		 	};
		 	
			leaderWebsocketPingFunc = function(skipPing){
				if (leaderWebsocket.readyState == leaderWebsocket.CLOSING 
						|| leaderWebsocket.readyState == leaderWebsocket.CLOSED){
					return;
				}
				
				// check and ping every 3 minutes
				leaderWebsocketPingTimeout = setTimeout(leaderWebsocketPingFunc, 3*60*1000);
				// initial set up does not send ping
				if (!skipPing) {
					leaderWebsocket.send("ping");
				}
			};
			
			// set up timer for the first time
			leaderWebsocketPingFunc(true);
		 	
			// run when the leader has just been selected
			leaderWebsocket.onmessage = function(e) {
				// no need to reset ping timer as the only possible message is page refresh
				
				// create JSON object
				var input = JSON.parse(e.data);
				
				// The leader has just been selected and all non-leaders should refresh their pages in order
		     	// to see new leader's name and a Finish button.
				if (input.pageRefresh) {
					location.reload();
					return;
				}
			};
	</script>

	<div id="container-main">
		<c:choose>
			<c:when test="${not empty groupLeader}">
				<lams:LeaderDisplay
					username="${groupLeader.firstName} ${groupLeader.lastName}"
					userId="${groupLeader.userId}" />
			</c:when>
			<c:otherwise>
				<fmt:message key="label.no.leader.yet.title" var="alertTitle"/>
				<lams:Alert5 type="warning" id="no-leader" close="false"
						title="${alertTitle}">
					<fmt:message key="label.no.leader.yet.body" />
				</lams:Alert5>
			</c:otherwise>
		</c:choose>

		<div class="card lcard mt-3">
			<div class="card-header">
				<fmt:message key="label.users.from.group" />
			</div>
			
			<div class="card-body mb-3">
				<div id="usersInGroup" class="row mt-2" role="list">
					<c:forEach var="user" items="${groupUsers}">
						<div id="user-${user.userId}" role="listitem" class="col-md-4 my-2 text-md-start">
							<lams:Portrait userId="${user.userId}" />
							<span> 
								<c:out value="${user.firstName} ${user.lastName}" escapeXml="true" />
							</span>
						</div>
					</c:forEach>
				</div>
			</div>
		</div>

		<div id="actionbuttons" class="activity-bottom-buttons">
			<c:if test="${!isSelectLeaderActive}">
				<button type="button" class="btn btn-primary na" id="finishButton">
					<c:choose>
						<c:when test="${isLastActivity}">
							<fmt:message key="button.submit" />
						</c:when>
						<c:otherwise>
							<fmt:message key="button.finish" />
						</c:otherwise>
					</c:choose>
				</button>
			</c:if>

			<button type="button" onclick="location.reload();"
				class="btn btn-secondary me-2 d-flex align-items-center">
				<i class="fa fa-refresh me-1"></i> 
				<span class="d-none d-sm-block">
					<fmt:message key="label.refresh" />
				</span>
			</button>
		</div>

	</div>

	<!-- leaderSelection dialog -->
	<div id="leaderSelectionDialog" class="modal fade" data-bs-keyboard="true" tabindex="-1" aria-labelledby="exampleModalLabel" aria-modal="true">
		<div class="modal-dialog modal-dialog-centered">
			<div class="modal-content">
				<div class="modal-header text-bg-secondary">
					<h1 class="modal-title text-white fs-5" id="exampleModalLabel">
						<fmt:message key="label.are.you.going.to.be.leader" />
					</h1>
					<button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
				</div>
					
				<div class="modal-body">
					<!-- begin -->
					<div id="instructions" class="instructions" aria-label="<fmt:message key='label.authoring.basic.instruction'/>">
						<c:out value="${content.instructions}" escapeXml="false" />
					</div>
						
					<div class="ltable table-hover mt-3">
						<div class="card-header">
							<fmt:message key="label.users.from.group" />
						</div>

						<c:forEach var="user" items="${groupUsers}">
							<div id="user-${user.userId}-modal" class="ps-2">
								<lams:Portrait userId="${user.userId}" />
								<span class="ms-2"> 
									<c:out value="${user.firstName} ${user.lastName}" escapeXml="true" />
								</span>
							</div>
						</c:forEach>
					</div>
				</div>

				<div class="modal-footer">
					<button type="button" data-bs-dismiss="modal" class="btn btn-secondary">
						<i class="fa fa-xmark fa-lg me-1"></i>
						<fmt:message key="label.no" />
					</button>
					
					<button type="button" onclick="leaderSelection();" class="btn btn-primary">
						<i class="fa fa-check fa-lg me-1"></i>
						<fmt:message key="label.yes.become.leader" />
					</button>
				</div>
			</div>
		</div>
	</div>				

</lams:PageLearner>
