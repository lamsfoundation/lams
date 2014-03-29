<%@ include file="/common/taglibs.jsp"%>

<script type="text/javascript">
	$(document).bind('pageinit', function(){
		
		$("#leader-selection-dialog").dialog({
			closeBtn: "none"
		});
		$("#dialog-button-yes").click(function (e) {
	        $.ajax({
	        	async: false,
	            url: '<c:url value="/learning.do"/>',
	            data: 'dispatch=becomeLeader&toolSessionID=${toolSessionID}',
	            type: 'post',
	            success: function (json) {
	            	location.reload();
	            }
	       	});
		});
		$("#dialog-button-no").click(function (e) {
			location.href = location.href + "&isDialogClosed=true";
		});
		
		if (${isSelectLeaderActive && !param.isDialogClosed}) {
			$.mobile.changePage("#leader-selection-dialog", {role: "dialog"});
		}
		
	});
	
	function finishActivity(){
		document.getElementById("finishButton").disabled = true;
		location.href = '<c:url value="/learning.do"/>?dispatch=finishActivity&toolSessionID=${toolSessionID}';
	}
</script>

<div data-role="header" data-theme="b" data-nobackbtn="true">
	<h1>
		<c:out value="${content.title}" escapeXml="true"/>
	</h1>
</div>

<div data-role="content">
	
	<h2>
		<fmt:message key="label.group.leader" />
		<c:choose>
			<c:when test="${not empty groupLeader}">
				<c:out value="${groupLeader.firstName} ${groupLeader.lastName}" escapeXml="true"/>
			</c:when>
			<c:otherwise>
				<i><fmt:message key="label.no.leader.yet" /></i>
			</c:otherwise>
		</c:choose>
	</h2>
		
	<div>
		<fmt:message key="label.users.from.group" />
	</div>
			
	<div>
		<ul>
			<c:forEach var="user" items="${groupUsers}" varStatus="status">
				<li>
					<c:out value="${user.firstName} ${user.lastName}" escapeXml="true"/>
				</li>
			</c:forEach>
		</ul>
	</div>

</div>

<div data-role="footer" data-theme="b" class="ui-bar">
	<span class="ui-finishbtn-right">
		<a href="#nogo" id="finishButton" onclick="finishActivity();" data-role="button" data-icon="arrow-r" data-theme="b">
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
		</a>
	</span>
</div>
