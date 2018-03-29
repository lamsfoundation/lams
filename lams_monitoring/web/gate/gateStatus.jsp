<c:if test="${not GateForm.map.gate.gateOpen}">

				<c:if test="${not GateForm.map.readOnly}">
					<html:form action="/gate?method=openGate" target="_self">
						<input type="hidden" name="activityId" value="${GateForm.map.activityId}" />
						<p><html:submit styleClass="btn btn-primary btn-sm voffset10"><fmt:message key="label.gate.open"/></html:submit></p>
					</html:form>
				</c:if>        
        
</c:if>
<c:if test="${GateForm.map.gate.gateOpen}">

	<div id="gateStatus" class="alert alert-success" style="margin-bottom: 10px; padding: 8px">
				<div class="media">
					<div class="media-left">
						<i class="fa fa-lg fa-sign-in"></i>
					</div>
					<div class="media-body">
						<strong><fmt:message key="label.gate.gate.open"/>
						<c:if test="${not empty GateForm.map.gate.gateOpenTime}">
							&nbsp;<lams:Date value="${GateForm.map.gate.gateOpenTime}" timeago="true" />
						</c:if>
						<c:if test="${not empty GateForm.map.gate.gateOpenUser}">
							&nbsp;<fmt:message key="label.gate.gate.open.user">
								<fmt:param value="${GateForm.map.gate.gateOpenUser.firstName} ${GateForm.map.gate.gateOpenUser.lastName}" />
							</fmt:message>
						</c:if>
						</strong>
					</div>
				</div>
	</div>

</c:if>
