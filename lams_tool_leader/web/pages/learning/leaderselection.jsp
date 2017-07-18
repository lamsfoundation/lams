<%@ include file="/common/taglibs.jsp"%>

<script type="text/javascript">

	$(window).load(function(){
		$("#leaderSelectionDialog").modal({
			show: ${isSelectLeaderActive},
			keyboard: true
		});
	});

	function leaderSelection() {
		$.ajax({
	    	async: false,
	        url: '<c:url value="/learning.do"/>',
	        data: 'dispatch=becomeLeader&toolSessionID=${toolSessionID}',
	        type: 'post',
	        success: function (json) {
	          	location.reload();
	        }
	   	});
	}

    function finishActivity(){
    	document.getElementById("finishButton").disabled = true;
		location.href = '<c:url value="/learning.do"/>?dispatch=finishActivity&toolSessionID=${toolSessionID}';
    }
    
 	//init the connection with server using server URL but with different protocol
 	var leaderWebsocket = new WebSocket('<lams:WebAppURL />'.replace('http', 'ws') 
 			+ 'learningWebsocket?toolSessionID=' + ${toolSessionID}),
		leaderWebsocketPingTimeout = null,
		leaderWebsocketPingFunc = null;
 		
	leaderWebsocketPingFunc = function(skipPing){
		if (leaderWebsocket.readyState == leaderWebsocket.CLOSING 
				|| leaderWebsocket.readyState == leaderWebsocket.CLOSED){
			location.reload();
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
 	
 	leaderWebsocket.onclose = function(e){
 		// react only on abnormal close
 		if (e.code === 1006) {
 	 		location.reload();
 		}
 	};
 	
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

<lams:Page type="learner" title="${content.title}">

	<c:choose>
		<c:when test="${not empty groupLeader}">
			<h4>
				<fmt:message key="label.group.leader" /> &nbsp;
				<mark><c:out value="${groupLeader.firstName} ${groupLeader.lastName}" escapeXml="true" /></mark>
			</h4>
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

	<div id="usersInGroup" class="voffset10">
		<c:forEach var="user" items="${groupUsers}" varStatus="status">
			<div id="user-${user.userId}">
				<div class="user voffset2 loffset10">
					<c:out value="${user.firstName} ${user.lastName}" escapeXml="true" />
				</div>
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
			<html:link href="#nogo" styleClass="btn btn-primary pull-right na" styleId="finishButton" onclick="finishActivity()">
				<span class="nextActivity"> <c:choose>
						<c:when test="${activityPosition.last}">
							<fmt:message key="button.submit" />
						</c:when>
						<c:otherwise>
							<fmt:message key="button.finish" />
						</c:otherwise>
					</c:choose>
				</span>
			</html:link>
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
					<c:forEach var="user" items="${groupUsers}" varStatus="status">
						<div id="user-${user.userId}" class="voffset2">
							<div class="user loffset10" id="user-${user.userId}">
								<c:out value="${user.firstName} ${user.lastName}" escapeXml="true" />
							</div>
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
