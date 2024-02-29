<%@ include file="/common/taglibs.jsp"%>

<script>
	$(document).ready(function(){
		<c:if test="${not empty groupLeader}">
			$('input[name="change-leader-radio-${groupId}"][value="${groupLeader.userId}"]').prop('checked', true);
		</c:if>
			 
		// Change leader button handler 
		var changeLeaderModalGroup${groupId} = $('#change-leader-modal-${groupId}').modal();
		
		$('.change-leader-button', changeLeaderModalGroup${groupId}).click(function () {
				var leaderUserId = $('input[name="change-leader-radio-${groupId}"]:checked').val();
				if (!leaderUserId || leaderUserId == '${not empty groupLeader ? groupLeader.userId : ""}') {
					return;
				}
				
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
				<div class="modal-title">
					<h4>
						<fmt:message key="label.change.leader"/>
					</h4>
					<c:if test="${empty toolSessionId}">
						<small>
							<fmt:message key="label.note.leader.change"/>
						</small>
					</c:if>
				</div>
				<button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
			</div>
			
			<div class="modal-body">
				<h4>${groupName}</h4> 
				<fmt:message key="label.current.leader"/>&nbsp;
				<c:choose>
					<c:when test="${empty groupLeader}">
						<fmt:message key="label.current.leader.none"/>
					</c:when>
					<c:otherwise>
						<c:out value="${groupLeader.getFullName()}" escapeXml="true" />
					</c:otherwise>
				</c:choose>
				
				<div id="usersInGroup">
					<c:forEach var="user" items="${members}">
						<div class="mt-3 ms-3 form-check">
							  <input class="form-check-input mt-2" type="radio" name="change-leader-radio-${groupId}"
							         id="change-leader-radio-${groupId}-${user.userId}" value="${user.userId}">
							  <label class="form-check-label" for="change-leader-radio-${groupId}-${user.userId}">
							    <lams:Portrait userId="${user.userId}"/>
								<span>
									<c:out value="${user.getFullName()}" escapeXml="true" />
									<c:forEach var="availableLearnerId" items="${availableLearners}">
										<c:if test="${user.userId eq availableLearnerId}">
											&nbsp;<i class="fa fa-check text-success" title="<fmt:message key='label.learner.available.in.activity'/>"></i>
										</c:if>
									</c:forEach>
								</span>
							  </label>
						</div>
					</c:forEach>
				</div>
			</div>
			
			<div class="modal-footer">	
				<a href="#" data-bs-dismiss="modal" class="btn btn-secondary">
					<fmt:message key="button.cancel"/>
				</a>
				<a href="#" data-bs-dismiss="modal" class="btn btn-primary change-leader-button">
					<fmt:message key="button.save"/>
				</a>
			</div>
		</div>
	          
	</div>
</div>