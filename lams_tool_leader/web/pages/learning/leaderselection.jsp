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
</script>


<lams:Page type="learner" title="${content.title}">


	<h4>
		<fmt:message key="label.group.leader" />&nbsp;
		
		<c:choose>
			<c:when test="${not empty groupLeader}">
				<mark><c:out value="${groupLeader.firstName} ${groupLeader.lastName}" escapeXml="true" /></mark>
			</c:when>
			<c:otherwise>
				<i><fmt:message key="label.no.leader.yet" /></i>
			</c:otherwise>
		</c:choose>
	</h4>

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
	<button type="button" onclick="location.reload();" class="btn btn-sm btn-default"><i class="fa fa-refresh"></i> <span class="hidden-xs">Refresh</span></button>
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
</div>
</lams:Page>


<c:set var="title">
	<c:out value="" escapeXml="true" />
</c:set>
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

			<!-- ends -->
		</div>
	</div>
</div>
</div>


