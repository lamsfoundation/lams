<!DOCTYPE html>

<%@ include file="/common/taglibs.jsp"%>

<lams:html>
	<c:set var="lams">
		<lams:LAMSURL />
	</c:set>
	<c:set var="tool">
		<lams:WebAppURL />
	</c:set>
	
	<lams:head>  
		<title>
			<fmt:message key="activity.title" />
		</title>
		<lams:css/>
		<link type="text/css" href="${lams}css/jquery-ui-bootstrap-theme.css" rel="stylesheet" />
		<style type="text/css">
	    	.dialog{display: none;}
	    	.ui-dialog-titlebar-close{display: none;}
	    	.ui-widget-overlay{opacity:0.9;}
	    </style>
	
		<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/common.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/bootstrap.min.js"></script>
		<script type="text/javascript">
			$(document).ready(function(){
				$("#leaderSelectionDialog").modal({
					show: ${isSelectLeaderActive},
					keyboard: true
				});
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
		    	document.getElementById("finishButton").disabled = true;
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
	</lams:head>
	
	<body class="stripes">
		

		<lams:Page type="learner" title="${content.title}">
			<c:choose>
				<c:when test="${not empty groupLeader}">
					<lams:LeaderDisplay username="${groupLeader.firstName} ${groupLeader.lastName}" userId="${groupLeader.userId}"/>
				</c:when>
				<c:otherwise>
					<lams:Alert type="warning" id="no-leader" close="false">
		                <h4>
		                    <fmt:message key="label.no.leader.yet.title" />
		                </h4>
		                <p>
						    <fmt:message key="label.no.leader.yet.body" />
		                </p>
					</lams:Alert>
				</c:otherwise>
			</c:choose>
		
			<div>
				<fmt:message key="label.users.from.group" />
			</div>
		
			<div id="usersInGroup">
				<c:forEach var="user" items="${groupUsers}">
					<div id="user-${user.userId}" class="voffset10 loffset10">
						<lams:Portrait userId="${user.userId}"/>
						<span>
							<c:out value="${user.firstName} ${user.lastName}" escapeXml="true" />
						</span>
					</div>
				</c:forEach>
			</div>
		
			<div id="actionbuttons" class="voffset20">
				<button type="button" onclick="location.reload();" class="btn btn-sm btn-default">
					<i class="fa fa-refresh"></i> 
					<span class="hidden-xs">
						<fmt:message key="label.refresh" />
					</span>
				</button>
				
				<c:if test="${!isSelectLeaderActive}">
					<a href="#nogo" class="btn btn-primary pull-right na" id="finishButton" onclick="finishActivity()">
						<span class="nextActivity"> <c:choose>
								<c:when test="${isLastActivity}">
									<fmt:message key="button.submit" />
								</c:when>
								<c:otherwise>
									<fmt:message key="button.finish" />
								</c:otherwise>
							</c:choose>
						</span>
					</a>
				</c:if>
			</div>
			
		</lams:Page>

		<div id="leaderSelectionDialog" class="modal fade">
			<div class="modal-dialog" role="document">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<div class="modal-title" id="exampleModalLabel">${content.title}</div>
					</div>
					<div class="modal-body">
						<!-- begin -->
						<div class="panel" id="leaderInstructions">
							<c:out value="${content.instructions}" escapeXml="false" />
						</div>
						<div class="lead">
							<fmt:message key="label.are.you.going.to.be.leader" />
						</div>
	
						<div class="voffset10">
							<fmt:message key="label.users.from.group" />
						</div>
	
						<div id="usersInGroup" class="voffset10">
							<c:forEach var="user" items="${groupUsers}">
								<div id="user-${user.userId}" class="voffset10 loffset10">
									<lams:Portrait userId="${user.userId}"/>
									<span>
										<c:out value="${user.firstName} ${user.lastName}" escapeXml="true" />
									</span>
								</div>
							</c:forEach>
						</div>
					</div>

					<div class="modal-footer">
						<button data-dismiss="modal" class="btn btn-sm btn-default">
							<fmt:message key="label.no" />
						</button>
						<button onclick="leaderSelection();" class="btn btn-sm btn-primary">
							<fmt:message key="label.yes.become.leader" />
						</button>
					</div>
				</div>
			</div>
		</div>

		<div class="footer">
		</div>					
	</body>
</lams:html>

