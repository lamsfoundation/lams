<%@ include file="/common/taglibs.jsp"%>

<script>
	$(document).ready(function(){
		// Change leader button handler 
		$('#change-leader-modal-${groupId}').modal()
			.find('.change-leader-button').click(function () {
				var leaderUserId = $('#change-leader-select-${groupId}').val();
		        $.ajax({
		            url: '<lams:WebAppURL />monitoring/changeLeaderForGroup.do',
		            data: {
		            	toolContentID : ${toolContentID},
		            	userID: leaderUserId,
		    			'<csrf:tokenname/>' : '<csrf:tokenvalue/>',
			        },
		            type: 'post',
		            success: function (response) {
			            if (typeof onChangeLeaderCallback == 'function') {
				            onChangeLeaderCallback(response, leaderUserId, '${toolSessionId}');
				        }
		            }
		       	});
			});
	});
</script>

<div class="modal fade" id="change-leader-modal-${groupId}">
	<div class="modal-dialog modal-lg">
	<div class="modal-content">
	
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal" aria-hidden="true"><i class="fa fa-lg fa-close"></i></button>
			<h4 class="modal-title">
				<fmt:message key="label.change.leader"/>
			</h4>
			<c:if test="${empty toolSessionId}">
				<small>
					<fmt:message key="label.note.leader.change"/>
				</small>
			</c:if>
		</div>
	          
		<!-- Begin -->
		<div class="modal-body">
			<div class="panel panel-default">
				<div class="panel-heading">
					<h4 class="panel-title">${groupName}:</h4> 
					<small>
						<fmt:message key="label.current.leader"/>
						<c:if test="${not empty groupLeader}">
							${groupLeader.lastName},&nbsp;${groupLeader.firstName} 
						</c:if>
					</small>
				</div>
				
				<div class="panel-body">
					<select class="select-picker" id="change-leader-select-${groupId}">
						<c:forEach var="member" items="${members}">
							<option value="${member.userId}">${member.lastName},&nbsp;${member.firstName}</option>
						</c:forEach>
					</select>
				</div>
			</div>
			<!-- end -->  
	              
			<div class="modal-footer">	
				<a href="#" data-dismiss="modal" class="btn btn-default">
					<fmt:message key="button.cancel"/>
				</a>
				<a href="#" data-dismiss="modal" class="btn btn-default change-leader-button">
					<fmt:message key="button.save"/>
				</a>
			</div>
		</div>
	          
	</div>
	</div>
</div>