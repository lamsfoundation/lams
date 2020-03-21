<%@ include file="/taglibs.jsp"%>

<c:set var="localeLanguage"><lams:user property="localeLanguage" /></c:set>
<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.timeago.js"></script>
<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/timeagoi18n/jquery.timeago.${fn:toLowerCase(localeLanguage)}.js"></script>

<script type="text/javascript">
	$(document).ready(function() {$("time.timeago").timeago();});
</script>

<!-- Header -->
<div class="row no-gutter">
	<div class="col-12 col-lg-12 col-xl-8">
		<h3>
			<fmt:message key="label.gates"/>
		</h3>
	</div>
</div>
<!-- End header -->

<!-- Tables -->
<div class="row no-gutter">
<div class="col-12 col-lg-12 col-xl-12">

	<c:forEach var="permissionGate" items="${permissionGates}">
			
		<div class="panel panel-default">
			<div class="panel-heading">
			</div>
			
			<div class="panel-body">
				<c:out value="${permissionGate.title}" escapeXml="false"/>
				<c:choose>
					<c:when test="${permissionGate.complete}">
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
						<i class="fa fa-check-square"></i>
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
