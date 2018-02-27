<%@ include file="/template/taglibs.jsp"%>

<!-- Header -->
<div class="row no-gutter">
	<div class="col-xs-12 col-md-12 col-lg-8">
		<h3>
			<fmt:message key="label.gates"/>
		</h3>
	</div>
</div>
<!-- End header -->

<!-- Tables -->
<div class="row no-gutter">
<div class="col-xs-12 col-md-12 col-lg-10">

	<c:forEach var="permissionGate" items="${permissionGates}">
			
		<div class="panel panel-default">
			<div class="panel-heading">
			</div>
			
			<div class="panel-body">
				<c:out value="${permissionGate.title}" escapeXml="false"/>
			
				<c:choose>
					<c:when test="${permissionGate.complete}">
						<i class="fa fa-check-square"></i>
						<!-- <br/>
							<small class="m-r">Opened 2 hours ago</small>
						 -->
					</c:when>
					<c:otherwise>
						<c:if test="${permissionGate.waitingLearnersCount > 0}">
							<br/>
							<small class="m-r">
								<i class="fa fa-users fa-users-at-gate"></i>
													 
								<fmt:message key="label.users.knocking.on.gate">
									<fmt:param>${permissionGate.waitingLearnersCount}</fmt:param>
								</fmt:message>
							</small>
						</c:if>

						<button type="button" class="btn btn-sm btn-primary pull-right"
								onClick="javascript:openPopUp('${permissionGate.url}','ContributeActivity', 600, 800, true)">
							<fmt:message key="label.gate.open"/>
						</button>
					</c:otherwise>
				</c:choose>
						
			</div>
		</div>			
			
	</c:forEach> 

</div>
</div>
