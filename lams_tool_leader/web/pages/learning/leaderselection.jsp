<%@ include file="/common/taglibs.jsp"%>

<script type="text/javascript">
	$(function() {
		$("#leaderSelectionDialog").dialog({
			bgiframe: true,
			autoOpen: ${isSelectLeaderActive},
			height: 500,
			width: 400,
			modal: true,
			buttons: {
				'<fmt:message key="label.yes.become.leader" />': function() {
			        $.ajax({
			        	async: false,
			            url: '<c:url value="/learning.do"/>',
			            data: 'dispatch=becomeLeader&toolSessionID=${toolSessionID}',
			            type: 'post',
			            success: function (json) {
			            	location.reload();
			            }
			       	});
				},
				
				'<fmt:message key="label.no" />': function() {
					$(this).dialog('close');
				}
			}
		});
	});

    function finishActivity(){
    	document.getElementById("finishButton").disabled = true;
		location.href = '<c:url value="/learning.do"/>?dispatch=finishActivity&toolSessionID=${toolSessionID}';
    }
</script>

<div id="content">
	<h1>
		${content.title}
	</h1>
	
	<h4>
		<fmt:message key="label.group.leader" />
		<c:choose>
			<c:when test="${not empty groupLeader}">
				${groupLeader.firstName} ${groupLeader.lastName}
			</c:when>
			<c:otherwise>
				<i><fmt:message key="label.no.leader.yet" /></i>
			</c:otherwise>
		</c:choose>
	</h4>

	<div>
		<fmt:message key="label.users.from.group" />
	</div>
		
	<div>
		<ul>
			<c:forEach var="user" items="${groupUsers}" varStatus="status">
				<li>
					${user.firstName} ${user.lastName}
				</li>
			</c:forEach>
		</ul>
	</div>

	<div class="space-bottom-top align-right">
		<html:link href="#nogo" styleClass="button" styleId="finishButton" onclick="finishActivity()">
			<span class="nextActivity">
				<c:choose>
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

</div>

<div id="leaderSelectionDialog" title="${content.title}" class="dialog">
	<div style="font-weight:bold; margin: 10px 0 20px;">
		${content.instructions}
		<br>
		<fmt:message key="label.are.you.going.to.be.leader" />
	</div>
		
	<div>
		<fmt:message key="label.users.from.group" />
	</div>
		
	<div style="text-align: right;">
		<c:forEach var="user" items="${groupUsers}" varStatus="status">
			<div>
				${user.firstName} ${user.lastName}
			</div>
		</c:forEach>
	</div>
</div>

