<%@ include file="/taglibs.jsp"%>

<c:set var="localeLanguage"><lams:user property="localeLanguage" /></c:set>
<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.timeago.js"></script>
<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/timeagoi18n/jquery.timeago.${fn:toLowerCase(localeLanguage)}.js"></script>

<script type="text/javascript">
	$(document).ready(function() {$("time.timeago").timeago();});
	

	function openGateNow(activityId) {
		var data = {
			'activityId' : activityId,
			'<csrf:tokenname/>' : '<csrf:tokenvalue/>'
		};
		$.ajax({
			'type' : 'post',
			'url'  : '<lams:LAMSURL/>monitoring/gate/openGate.do',
			'data'  : data,
			'success' : function(){
				refresh();
			}
		});
	}
</script>

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
<div class="col-xs-12 col-md-12 col-lg-12">

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
						<i class="fa fa-check-square" 
						   onClick="javascript:openPopUp('${permissionGate.url}','ContributeActivity', 600, 800, true)"
						   title="<fmt:message key='button.task.gate.opened.tooltip'/>"></i>
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

						<button type="button" class="btn btn-sm btn-primary pull-right loffset10"
								title="<fmt:message key='button.task.gate.open.tooltip'/>"
								onClick="javascript:openPopUp('${permissionGate.url}','ContributeActivity', 600, 800, true)">
							<fmt:message key="button.task.gate.open"/>
						</button>
						<button type="button" class="btn btn-sm btn-primary pull-right"
								title="<fmt:message key='button.task.gate.open.now.tooltip'/>"
								onClick="javascript:openGateNow(${permissionGate.activityID})">
							<fmt:message key="button.task.gate.open.now"/>
						</button>
					</c:otherwise>
				</c:choose>
						
			</div>
		</div>			
			
	</c:forEach> 

</div>
</div>
