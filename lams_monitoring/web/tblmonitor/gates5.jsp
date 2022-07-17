<%@ include file="/taglibs.jsp"%>

<script type="text/javascript">
	$(document).ready(function() {$("time.timeago").timeago();}); 
</script>

<!-- Tables -->
<div class="container-fluid">
	<div class="row">
		<div class="col-8 offset-2 text-center">
			<h3>
				<fmt:message key="label.gates"/>
			</h3>
		</div>
	</div>
	
	<c:forEach var="permissionGate" items="${permissionGates}">
		<div class="row mb-3">
			<div class="col-8 offset-2">	
				<div class="card gate-card">
					<div class="card-body">
						<span class="card-title"><c:out value="${permissionGate.title}" escapeXml="false"/></span>
						<c:choose>
							<c:when test="${permissionGate.complete}">
								<i class="fa fa-check-square gate-opened-icon fs-3 text-success float-end mt-2" 
								   onClick="javascript:openGateSelectively('${permissionGate.url}')"
								   title="<fmt:message key='button.task.gate.opened.tooltip'/>"></i>
								<br />
								<small>
									<fmt:message key="label.gate.gate.open"/>
									<c:if test="${not empty permissionGate.openTime}">
										&nbsp;<lams:Date value="${permissionGate.openTime}" timeago="true" />
									</c:if>
									<c:if test="${not empty permissionGate.openUser}">
										&nbsp;<fmt:message key="label.gate.gate.open.user">
											<fmt:param value="${permissionGate.openUser}" />
										</fmt:message>
									</c:if>
								</small>
		
							</c:when>
							<c:otherwise>
								<button type="button" class="btn btn-sm btn-primary float-end ms-3 mt-2"
										title="<fmt:message key='button.task.gate.open.tooltip'/>"
										onClick="javascript:openGateSelectively('${permissionGate.url}')">
									<fmt:message key="button.task.gate.open"/>
								</button>
								<button type="button" class="btn btn-sm btn-primary float-end mt-2"
										title="<fmt:message key='button.task.gate.open.now.tooltip'/>"
										onClick="javascript:openGateNow(${permissionGate.activityID})">
									<fmt:message key="button.task.gate.open.now"/>
								</button>
								
								<c:if test="${permissionGate.waitingLearnersCount > 0}">
									<br />
									<small class="m-r">
										<i class="fa fa-users fa-users-at-gate"></i>
															 
										<fmt:message key="label.users.knocking.on.gate">
											<fmt:param>${permissionGate.waitingLearnersCount}</fmt:param>
										</fmt:message>
									</small>
								</c:if>
							</c:otherwise>
						</c:choose>
								
					</div>
				</div>
			</div>			
		</div>
	</c:forEach> 
</div>